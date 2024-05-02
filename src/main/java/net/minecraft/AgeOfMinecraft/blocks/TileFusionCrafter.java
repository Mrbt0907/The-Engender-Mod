package net.minecraft.AgeOfMinecraft.blocks;
import java.util.Random;

import net.endermanofdoom.mac.item.ItemUtils;
import net.endermanofdoom.mac.util.StringUtil;
import net.minecraft.AgeOfMinecraft.items.ItemManaCollector;
import net.minecraft.AgeOfMinecraft.registry.BlockRegistry;
import net.minecraft.AgeOfMinecraft.registry.FusionRecipeRegistry;
import net.minecraft.AgeOfMinecraft.registry.FusionRecipeRegistry.FusionRecipe;
import net.minecraft.AgeOfMinecraft.registry.ItemRegistry;
import net.minecraft.AgeOfMinecraft.registry.SoundRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TileFusionCrafter extends TileEntity implements ITickable, ISidedInventory
{
	private final NonNullList<ItemStack> INVENTORY = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
	private final SoundEvent[] SOUNDS = new SoundEvent[]
	{
		SoundEvents.ENTITY_BAT_AMBIENT,
		SoundEvents.ENTITY_CHICKEN_AMBIENT,
		SoundEvents.ENTITY_COW_AMBIENT,
		SoundEvents.ENTITY_CAT_AMBIENT,
		SoundEvents.ENTITY_PIG_AMBIENT,
		SoundEvents.ENTITY_RABBIT_AMBIENT,
		SoundEvents.ENTITY_SHEEP_AMBIENT,
		SoundEvents.ENTITY_ENDERMITE_AMBIENT,
		SoundEvents.ENTITY_SILVERFISH_AMBIENT,
		SoundEvents.ENTITY_SNOWMAN_AMBIENT,
		SoundEvents.ENTITY_LLAMA_AMBIENT,
		SoundEvents.ENTITY_SQUID_AMBIENT,
		SoundEvents.ENTITY_VILLAGER_NO,
		SoundEvents.ENTITY_WOLF_GROWL,
		SoundEvents.ENTITY_CREEPER_HURT,
		SoundEvents.ENTITY_MAGMACUBE_JUMP,
		SoundEvents.ENTITY_POLAR_BEAR_WARNING,
		SoundEvents.ENTITY_SKELETON_AMBIENT,
		SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT,
		SoundEvents.ENTITY_STRAY_AMBIENT,
		SoundEvents.ENTITY_SLIME_JUMP,
		SoundEvents.ENTITY_SPIDER_AMBIENT,
		SoundEvents.ENTITY_ZOMBIE_AMBIENT,
		SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT,
		SoundEvents.ENTITY_HUSK_AMBIENT,
		SoundEvents.ENTITY_VEX_CHARGE,
		SoundEvents.VINDICATION_ILLAGER_AMBIENT,
		SoundEvents.ENTITY_BLAZE_AMBIENT,
		SoundEvents.ENTITY_ENDERMEN_SCREAM,
		SoundEvents.ENTITY_GHAST_HURT,
		SoundEvents.ENTITY_GUARDIAN_AMBIENT,
		SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT,
		SoundEvents.ENTITY_SHULKER_AMBIENT,
		SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY,
		SoundEvents.ENTITY_ENDERDRAGON_GROWL,
		SoundEvents.ENTITY_ZOMBIE_AMBIENT,
		SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT,
		SoundEvents.ENTITY_IRONGOLEM_HURT,
		SoundEvents.ENTITY_WITHER_AMBIENT,
		SoundRegistry.witherStormRoar
	};
	
	public String name;
	protected int curFuseTime;
	protected int fuseTime = -1;
	public int mana;
	public int entropy;
	
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		INVENTORY.set(0, ItemStack.EMPTY);
		INVENTORY.set(1, ItemStack.EMPTY);
		INVENTORY.set(2, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, INVENTORY);
		curFuseTime = compound.getInteger("CurrentFuseTime");
		mana = compound.getInteger("Mana");
		entropy = compound.getInteger("Entropy");
		if (compound.hasKey("CustomName"))
			name = compound.getString("CustomName");
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("CurrentFuseTime", curFuseTime);
		compound.setInteger("Mana", mana);
		compound.setInteger("Entropy", entropy);
		if (hasCustomName())
			compound.setString("CustomName", name);
		ItemStackHelper.saveAllItems(compound, INVENTORY);
		return compound;
	}
	
	public void update()
	{
		Random rand = world.rand;
		if (world.isRemote)
		{
			double d3 = (double)(pos.getX() + rand.nextFloat());
			double d4 = (double)(pos.getY() + rand.nextFloat());
			double d5 = (double)(pos.getZ() + rand.nextFloat());
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
			if (fuseTime > -1)
				world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
			if (rand.nextInt(500) == 0)
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SOUNDS[rand.nextInt(SOUNDS.length)], SoundCategory.HOSTILE, 1F, 1F, false);

		}
		else
		{
			//-- Input Slot --\\
			boolean markDirty = false;
			ItemStack input = INVENTORY.get(0);
			if (!input.isEmpty() && canFuse())
			{
				FusionRecipe recipe = FusionRecipeRegistry.INSTANCE.getRecipe(input);
				if (fuseTime < 0)
				{
					fuseTime = recipe.fusionTime * 20;
					curFuseTime = fuseTime;
				}
					
				if (curFuseTime == 0)
				{
					fuseItem();
					mana -= recipe.mana;
					entropy -= recipe.entropy;
					markDirty = true;
					
					if (!input.isEmpty())
						curFuseTime = fuseTime;
					else
						fuseTime = -1;
				}
				else
					curFuseTime--;
			}
			else
			{
				if (curFuseTime != 0)
					curFuseTime = 0;
				if (curFuseTime != -1)
					fuseTime = -1;
			}
				
			
			if (markDirty)
				markDirty();
			
			//-- Crystal Slot --\\
			ItemStack stackA = INVENTORY.get(1);
			if (!stackA.isEmpty() && stackA.getItem() instanceof ItemManaCollector)
			{
				ItemManaCollector crystal = (ItemManaCollector) stackA.getItem();
				int mana = crystal.getMana(stackA), entropy = crystal.getEntropy(stackA);
				int maxMana = getMaxMana(), maxEntropy = getMaxEntropy();
				
				if (mana > 0 && this.mana < maxMana)
				{
					int manaDigits = Math.max(String.valueOf(mana).length() - 2, 0);
					int manaFinal = Math.min(Integer.parseInt(StringUtil.repeat("1", "0", manaDigits)), maxMana - this.mana);
						
					crystal.setMana(mana - manaFinal, stackA);
					this.mana += manaFinal;
						
					if (mana == 0)
						world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 0.5F);
				}
				if (entropy > 0 && this.entropy < maxEntropy)
				{
					int entropyDigits = Math.max(String.valueOf(entropy).length() - 2, 0);
					int entropyFinal = Math.min(Integer.parseInt(StringUtil.repeat("1", "0", entropyDigits)), maxEntropy - this.entropy);
						
					crystal.setEntropy(entropy - entropyFinal, stackA);
					this.entropy += entropyFinal;
						
					if (crystal.getEntropy(stackA) == 0)
						world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 0.5F);
				}
			}
		}
	}
	
	protected boolean canFuse()
	{
		ItemStack stack = INVENTORY.get(0);
		ItemStack output = INVENTORY.get(2);
		FusionRecipe recipe = FusionRecipeRegistry.INSTANCE.getRecipe(stack);
		
		if (stack.isEmpty() || recipe == null)
			return false;
		
		return recipe.mana <= mana && recipe.entropy <= entropy && (output.isEmpty() || output.getItem().equals(recipe.output.getItem()));
	}
	
	protected void fuseItem()
	{
		ItemStack input = INVENTORY.get(0);
		ItemStack output = INVENTORY.get(2);
		FusionRecipe recipe = FusionRecipeRegistry.INSTANCE.getRecipe(input);

		if (output.isEmpty())
			INVENTORY.set(2, recipe.output.copy());
		else if (output.getItem().equals(recipe.output.getItem()))
			output.grow(recipe.output.getCount());

		input.shrink(1);
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 0.6F, 2F);
	}
	
	public int getFuseTime()
	{
		return fuseTime;
	}
	
	public int getCurFuseTime()
	{
		return curFuseTime;
	}
	
	public int getMaxMana()
	{
		return 1000000;
	}
	
	public int getMaxEntropy()
	{
		return 10000;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		switch (index)
		{
			case 0: return FusionRecipeRegistry.INSTANCE.getRecipe(stack) != null;
			case 1: return stack.getItem() instanceof ItemManaCollector;
			default: return false;
		}
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		if (world.getTileEntity(pos) != this)
			return false;
		else
			return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public void openInventory(EntityPlayer player) {}
	
	@Override
	public void closeInventory(EntityPlayer player) {}
	
	@Override
	public boolean isEmpty()
	{
		for (int i = 0; i < INVENTORY.size(); i++)
			if (!INVENTORY.get(i).isEmpty())
				return false;
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return INVENTORY.get(index);
	}

	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(INVENTORY, index, count);
	}

	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(INVENTORY, index);
	}

	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack itemstack = INVENTORY.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		INVENTORY.set(index, stack);
		
		if (stack.getCount() > getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());

		if (index == 0 && !flag)
		{
			fuseTime = -1;
			markDirty();
		}
	}
	
	public int getFieldCount()
	{
		return 4;
	}
	
	@Override
	public int getField(int id)
	{
		switch (id)
		{
			case 0: return curFuseTime;
			case 1: return fuseTime;
			case 2: return mana;
			case 3: return entropy;
			default: return 0;
		}
	}
	
	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
			case 0: curFuseTime = value; break;
			case 1: fuseTime = value; break;
			case 2: mana = value; break;
			case 3: entropy = value; break;
		}
	}

	@Override
	public void clear()
	{
		mana = 0;
		entropy = 0;
		INVENTORY.clear();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public int getSizeInventory()
	{
		return 3;
	}

	@Override
	public String getName()
	{
		return hasCustomName() ? name : BlockRegistry.fusionCrafter.getLocalizedName();
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(getName());
	}

	@Override
	public boolean hasCustomName()
	{
		return name != null && name.length() > 0;
	}

	private static final int[] SLOTS_INPUT = new int[] {0, 1}; 
	private static final int[] SLOTS_OUTPUT = new int[] {1, 2};
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side.equals(EnumFacing.DOWN) ? SLOTS_OUTPUT : SLOTS_INPUT;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing side)
	{
		ItemStack input = INVENTORY.get(index);
		switch(index)
		{
			case 0: case 1:
				return side != EnumFacing.DOWN && (input.isEmpty() || ItemUtils.compareItemStacks(stack, input));
			default: return false;
		}
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing side)
	{
		ItemStack input = INVENTORY.get(index);
		switch(index)
		{
			case 1:
				return side == EnumFacing.DOWN && !input.isEmpty() && ItemRegistry.manaContainer.getMana(input) <= 0 && ItemRegistry.manaContainer.getEntropy(input) <= 0;
			case 2:
				return side == EnumFacing.DOWN && !input.isEmpty();
			default: return false;
		}
	}
}