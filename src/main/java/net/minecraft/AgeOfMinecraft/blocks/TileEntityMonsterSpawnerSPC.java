package net.minecraft.AgeOfMinecraft.blocks;
import java.util.Random;

import net.minecraft.AgeOfMinecraft.items.ItemFusion;
import net.minecraft.AgeOfMinecraft.items.ItemManaCollector;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMonsterSpawnerSPC extends TileEntity implements ITickable, IInventory
{
	private NonNullList<ItemStack> fuserItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
	private float currentItemBurnTime;
	public float fuseTime;
	public float totalSpawnMobTime;
	public float mana;
	public float entropy;
	private String fuserCustomName;
	/**
	* Returns the number of slots in the inventory.
	*/
	public int getSizeInventory()
	{
		return 3;
	}

	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.fuserItemStacks)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	/**
	* Returns the stack in the given slot.
	*/
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.fuserItemStacks.get(index);
	}

	/**
	* Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	*/
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.fuserItemStacks, index, count);
	}

	/**
	* Removes a stack from the given slot and returns it.
	*/
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.fuserItemStacks, index);
	}

	/**
	* Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	*/
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack itemstack = this.fuserItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.fuserItemStacks.set(index, stack);
		
		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag)
		{
			this.totalSpawnMobTime = this.timeToSpawnmob(stack);
			this.fuseTime = 0;
			this.markDirty();
		}
	}
	public String getName()
	{
		return hasCustomName() ? this.fuserCustomName : "Fusion Crafter";
	}
	/**
	* Get the formatted ChatComponent that will be used for the sender's username in chat
	*/
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(this.getName());
	}
	public boolean hasCustomName()
	{
		return (this.fuserCustomName != null) && (this.fuserCustomName.length() > 0);
	}
	public void setCustomInventoryName(String p_145951_1_)
	{
		this.fuserCustomName = p_145951_1_;
	}
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		@SuppressWarnings("unused")
		NBTTagList nbttaglist = compound.getTagList("Items", 10);
		this.fuserItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.fuserItemStacks);
		this.fuseTime = compound.getShort("CookTime");
		this.totalSpawnMobTime = compound.getShort("CookTimeTotal");
		this.mana = compound.getShort("Mana");
		this.entropy = compound.getShort("Entropy");
		this.currentItemBurnTime = getItemBurnTime((ItemStack)this.fuserItemStacks.get(1));
		if (compound.hasKey("CustomName", 8))
		this.fuserCustomName = compound.getString("CustomName");
	}
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setShort("CookTime", (short)this.fuseTime);
		compound.setShort("CookTimeTotal", (short)this.totalSpawnMobTime);
		compound.setShort("Mana", (short)this.getMana());
		compound.setShort("Entropy", (short)this.getEntropy());
		ItemStackHelper.saveAllItems(compound, this.fuserItemStacks);
		if (hasCustomName())
		compound.setString("CustomName", this.fuserCustomName);
		return compound;}
		@SideOnly(Side.CLIENT)
		public static boolean isBurning(IInventory p_174903_0_)
		{
			return p_174903_0_.getField(0) > 0;
		}
		public void update()
		{
			Random rand = this.getWorld().rand;
			
			double d3 = (double)(pos.getX() + rand.nextFloat());
			double d4 = (double)(pos.getY() + rand.nextFloat());
			double d5 = (double)(pos.getZ() + rand.nextFloat());
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
			if (this.fuseTime > 0)
			{
				world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
			}

			if (rand.nextInt(500) == 0)
			{
				switch (rand.nextInt(40))
				{
					case 0:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_BAT_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 1:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_CHICKEN_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 2:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_COW_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 3:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_CAT_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 4:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_PIG_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 5:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_RABBIT_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 6:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SHEEP_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 7:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ENDERMITE_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 8:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SILVERFISH_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 9:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SNOWMAN_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 10:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_LLAMA_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 11:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SQUID_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 12:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 13:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_WOLF_GROWL, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 14:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_CREEPER_HURT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 15:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_MAGMACUBE_JUMP, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 16:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_POLAR_BEAR_WARNING, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 17:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SKELETON_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 18:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 19:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_STRAY_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 20:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 21:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SPIDER_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 22:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ZOMBIE_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 23:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 24:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_HUSK_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 25:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 26:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.VINDICATION_ILLAGER_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 27:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 28:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ENDERMEN_SCREAM, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 29:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_GHAST_HURT, SoundCategory.BLOCKS, 10F, 1F, false);
					break;
					case 30:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_GUARDIAN_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 31:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT, SoundCategory.BLOCKS, 2F, 1F, false);
					break;
					case 32:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SHULKER_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 33:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, SoundCategory.BLOCKS, 10F, 1F, false);
					break;
					case 34:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.BLOCKS, 10F, 1F, false);
					break;
					case 35:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ZOMBIE_AMBIENT, SoundCategory.BLOCKS, 5F, 0.5F, false);
					break;
					case 36:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 37:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_IRONGOLEM_HURT, SoundCategory.BLOCKS, 1F, 1F, false);
					break;
					case 38:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.BLOCKS, 2F, 1F, false);
					break;
					case 39:this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), ESound.witherStormRoar, SoundCategory.BLOCKS, 10F, 1F, false);
				}
			}

			boolean flag1 = false;
			
			ItemStack itemstack = (ItemStack)this.fuserItemStacks.get(1);
			if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemManaCollector && ((ItemManaCollector) itemstack.getItem()).getMana(itemstack) > 0 && this.getMana() < this.getMaxMana())
			{
				((ItemManaCollector) itemstack.getItem()).setMana(((ItemManaCollector) itemstack.getItem()).getMana(itemstack) - 1, itemstack);
				++this.mana;
				if (((ItemManaCollector) itemstack.getItem()).getMana(itemstack) > 200)
				{
					((ItemManaCollector) itemstack.getItem()).setMana(((ItemManaCollector) itemstack.getItem()).getMana(itemstack) - 199, itemstack);
					this.mana += 199;
				}
				if (((ItemManaCollector) itemstack.getItem()).getMana(itemstack) == 1)
				this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1F, 0.5F);
			}
			if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemManaCollector && ((ItemManaCollector) itemstack.getItem()).getEntropy(itemstack) > 0 && this.getEntropy() < this.getMaxEntropy())
			{
				((ItemManaCollector) itemstack.getItem()).setEntropy(((ItemManaCollector) itemstack.getItem()).getEntropy(itemstack) - 1, itemstack);
				++this.entropy;
				if (((ItemManaCollector) itemstack.getItem()).getEntropy(itemstack) > 50)
				{
					((ItemManaCollector) itemstack.getItem()).setEntropy(((ItemManaCollector) itemstack.getItem()).getEntropy(itemstack) - 49, itemstack);
					this.entropy += 49;
				}
				if (((ItemManaCollector) itemstack.getItem()).getEntropy(itemstack) == 1)
				this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 0.1F, 1F);
			}
			if (!this.world.isRemote)
			{
				if (!((ItemStack)this.fuserItemStacks.get(0)).isEmpty())
				{
					if (this.canSmelt() && this.mana >= ((ItemFusion)((ItemStack)this.fuserItemStacks.get(0)).getItem()).getManaCost() && this.entropy >= ((ItemFusion)((ItemStack)this.fuserItemStacks.get(0)).getItem()).getEntropyCost())
					{
						this.totalSpawnMobTime = timeToSpawnmob((ItemStack)this.fuserItemStacks.get(0));
						this.currentItemBurnTime = this.fuseTime;
						++this.fuseTime;
						
						if (this.fuseTime >= this.totalSpawnMobTime)
						{
							this.fuseTime = 0;
							this.world.playSound(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1F, 2F);
							this.mana -= ((ItemFusion)((ItemStack)this.fuserItemStacks.get(0)).getItem()).getManaCost();
							this.entropy -= ((ItemFusion)((ItemStack)this.fuserItemStacks.get(0)).getItem()).getEntropyCost();
							this.smeltItem();
							flag1 = true;
						}
					}
					else
					{
						--this.fuseTime;
					}
				}
				else if (this.fuseTime > 0)
				{
					--this.fuseTime;
				}
			}

			if (flag1)
			{
				this.markDirty();
			}
		}
		public int timeToSpawnmob(ItemStack p_174904_1_)
		{
			Item item = p_174904_1_.getItem();
			if (item instanceof ItemFusion)
			return ((ItemFusion)item).getItemToFuse().getTimeToSpawnMob() * 20;
			
			return 0;
		}
		private boolean canSmelt()
		{
			if (((ItemStack)this.fuserItemStacks.get(0)).isEmpty())
			{
				return false;
			}
			else
			{
				ItemStack itemstack = MobSpawnerRecipes.instance().getSmeltingResult((ItemStack)this.fuserItemStacks.get(0));
				
				if (itemstack.isEmpty())
				{
					return false;
				}
				else
				{
					ItemStack itemstack1 = (ItemStack)this.fuserItemStacks.get(2);
					if (itemstack1.isEmpty()) return true;
					if (!itemstack1.isItemEqual(itemstack)) return false;
					if (this.mana < ((ItemFusion)((ItemStack)this.fuserItemStacks.get(0)).getItem()).getManaCost())return false;if (this.entropy < ((ItemFusion)((ItemStack)this.fuserItemStacks.get(0)).getItem()).getEntropyCost())return false;
					int result = itemstack1.getCount() + itemstack.getCount();
					return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
				}
			}
		}
		public void smeltItem()
		{
			if (canSmelt())
			{
				ItemStack itemstack = (ItemStack)this.fuserItemStacks.get(0);
				ItemStack itemstack1 = MobSpawnerRecipes.instance().getSmeltingResult(itemstack);
				ItemStack itemstack2 = (ItemStack)this.fuserItemStacks.get(2);
				
				if (itemstack2.isEmpty())
				{
					this.fuserItemStacks.set(2, itemstack1.copy());
				}
				else if (itemstack2.getItem() == itemstack1.getItem())
				{
					itemstack2.grow(itemstack1.getCount());
				}

				itemstack.shrink(1);
			}
		}
		public static int getItemBurnTime(ItemStack stack)
		{
			return stack.isEmpty() ? 0 : 40;
		}
		public boolean isUsableByPlayer(EntityPlayer player)
		{
			if (this.world.getTileEntity(this.pos) != this)
			{
				return false;
			}
			else
			{
				return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
			}
		}
		public void openInventory(EntityPlayer player) { }
		public void closeInventory(EntityPlayer player) { }
		/**
		* Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
		* guis use Slot.isItemValid
		*/
		public boolean isItemValidForSlot(int index, ItemStack stack)
		{
			if (index == 2)
			{
				return false;
			}
			else if (index == 0)
			{
				return stack.getItem() instanceof ItemFusion;
			}
			else
			{
				return stack.getItem() instanceof ItemManaCollector;
			}
		}
		public int[] getSlotsForFace(EnumFacing side)
		{
			if (side == EnumFacing.UP)
			{
				return new int[] {0, 1};
		}
		else
		{
			return new int[] {2};
	}
}
/**
* Returns true if automation can insert the given item in the given slot from the given side.
*/
public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
{
	return this.isItemValidForSlot(index, itemStackIn);
}

/**
* Returns true if automation can extract the given item in the given slot from the given side.
*/
public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
{
	return true;
}
public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
{
	return new ContainerMobSpawner(playerInventory, this);
}
public int getField(int id)
{
	switch (id)
	{
		case 0:
		return (int) this.currentItemBurnTime;
		case 1:
		return (int) this.fuseTime;
		case 2:
		return (int) this.totalSpawnMobTime;
		case 3:
		return (int) this.mana;
		case 4:
		return (int) this.entropy;
		default:
		return 0;
	}
}

public void setField(int id, int value)
{
	switch (id)
	{
		case 0:
		this.currentItemBurnTime = value;
		break;
		case 1:
		this.fuseTime = value;
		break;
		case 2:
		this.totalSpawnMobTime = value;
		break;
		case 3:
		this.mana = value;
		break;
		case 4:
		this.entropy = value;
	}
}

public int getFieldCount()
{
	return 5;
}
public float getMana()
{
	return this.mana;
}
public float getEntropy()
{
	return this.entropy;
}
public float getMaxMana()
{
	return 2000000;
}
public float getMaxEntropy()
{
	return 20000;
}

public void clear()
{
	this.fuserItemStacks.clear();
}

public int getInventoryStackLimit()
{
	return 64;
}
}