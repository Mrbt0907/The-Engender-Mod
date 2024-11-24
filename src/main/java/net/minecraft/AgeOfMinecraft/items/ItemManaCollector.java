package net.minecraft.AgeOfMinecraft.items;

import java.util.List;

import net.endermanofdoom.mca.MCA;
import net.minecraft.AgeOfMinecraft.entity.EntityManaOrb;
import net.minecraft.AgeOfMinecraft.registry.EngenderSetup;
import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemSimpleFoiled;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class ItemManaCollector extends ItemSimpleFoiled implements baubles.api.IBauble
{
	private final int type;
	
	public ItemManaCollector(int data)
	{
		type = data;
		setMaxStackSize(1);
		setHasSubtypes(true);
		if (data != 2)
		addPropertyOverride(new ResourceLocation("percent"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				return getState(stack);
			}
		});
	}

	@Override
	public baubles.api.BaubleType getBaubleType(ItemStack itemstack)
	{
		return baubles.api.BaubleType.TRINKET;
	}

	@Override
	public void onEquipped(ItemStack itemstack, EntityLivingBase player)
	{
		 player.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, .75F, 1.9f);
	}

	@Override
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player)
	{
		player.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, .75F, 2f);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entityIn)
	{
		World world = entityIn.world;
			
		if (!world.isRemote && entityIn instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entityIn;
			if (!player.isSpectator())
			{
				List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(24D));
				if (list != null && !list.isEmpty())
				{
					for (int i = 0; i < list.size(); i++)
					{
						Entity entity = (Entity)list.get(i);
							
						if (entity instanceof EntityManaOrb)
						{
							EntityManaOrb orb = (EntityManaOrb)entity;
							if (type == 2)
							{
								orb.magnet = stack;
								orb.closestPlayer = player;
							}
							else
							{
								if (orb.getEntropy())
								{
									if (getEntropy(stack) < getMaxEntropy(stack))
									{
										orb.magnet = stack;
										orb.closestPlayer = player;
									}
								}
								else
								{
									if (getMana(stack) < getMaxMana(stack))
									{
										orb.magnet = stack;
										orb.closestPlayer = player;
									}
								}
							}
						}
					}
				}
			}
		}
	}

		public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected)
		{
			if (entityIn instanceof EntityLivingBase)
			onWornTick(stack, (EntityLivingBase)entityIn);
		}

		public float getState(ItemStack stack)
		{
			float currentHolding = type == 1 ? getEntropy(stack) : getMana(stack);
			float maxHolding = type == 1 ? getMaxEntropy(stack) : getMaxMana(stack);
			
			if (currentHolding >= maxHolding * 0.75F)
			return 1F;
			else if (currentHolding >= maxHolding * 0.5F)
			return 0.75F;
			else if (currentHolding >= maxHolding * 0.25F)
			return 0.5F;
			else if (currentHolding >= 1)
			return 0.25F;
			else
			return 0F;
		}

		public void increaseHolding(int amount, ItemStack stack, boolean isEntropy)
		{
			if (isEntropy)
			setEntropy((amount < 0 ? getEntropy(stack) - amount : getEntropy(stack) + amount), stack);
			else
			setMana((amount < 0 ? getMana(stack) - amount : getMana(stack) + amount), stack);
		}

		public void setMana(int amount, ItemStack stack)
		{
			if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("mana", amount > getMaxMana(stack) ? getMaxMana(stack) : amount);
		}

		public void setEntropy(int amount, ItemStack stack)
		{
			if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("entropy", amount > getMaxEntropy(stack) ? getMaxEntropy(stack) : amount);
		}

		public int getMana(ItemStack stack)
		{
			return stack.hasTagCompound() && stack.getTagCompound().hasKey("mana") ? (int)stack.getTagCompound().getInteger("mana") : 0;
		}

		public int getEntropy(ItemStack stack)
		{
			return stack.hasTagCompound() && stack.getTagCompound().hasKey("entropy") ? (int)stack.getTagCompound().getInteger("entropy") : 0;
		}

		public EnumRarity getRarity(ItemStack stack)
		{
			if(!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("mana", 0);
				stack.getTagCompound().setInteger("entropy", 0);
			}
			return type == 2 ? MCA.LEGENDARY : EnumRarity.EPIC;
		}

		public int getMaxMana(ItemStack stack)
		{
			return type == 2 ? Integer.MAX_VALUE : type == 0 ? getManaCrystalMana(stack) : 0;
		}

		public int getMaxEntropy(ItemStack stack)
		{
			return type == 2 ? Integer.MAX_VALUE : type == 1 ? getEntropyCrystalMana(stack) : 0;
		}

		public int getManaCrystalMana(ItemStack stack)
		{
			switch (getMetadata(stack))
			{
				case 1:
				{
					return 1500;
				}
				case 2:
				{
					return 3000;
				}
				case 3:
				{
					return 5000;
				}
				case 4:
				{
					return 10000;
				}
				case 5:
				{
					return 35000;
				}
				case 6:
				{
					return 75000;
				}
				case 7:
				{
					return 125000;
				}
				case 8:
				{
					return 250000;
				}
				case 9:
				{
					return 1000000;
				}
				default:
				{
					return 1000;
				}
			}
		}

		public int getEntropyCrystalMana(ItemStack stack)
		{
			switch (getMetadata(stack))
			{
				case 1:
				{
					return 40;
				}
				case 2:
				{
					return 80;
				}
				case 3:
				{
					return 150;
				}
				case 4:
				{
					return 400;
				}
				case 5:
				{
					return 750;
				}
				case 6:
				{
					return 1000;
				}
				case 7:
				{
					return 2000;
				}
				case 8:
				{
					return 5000;
				}
				case 9:
				{
					return 10000;
				}
				default:
				{
					return 20;
				}
			}
		}

		public void addInformation(ItemStack stack, World player, List<String> l, ITooltipFlag B)
		{
			if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
			switch (type)
			{
				case 0:
				{
					l.add("Allows you to collect Mana, then funnel it into the Fusion Crafter to make mobs.");
					if (getMana(stack) > 0)
					l.add(TextFormatting.AQUA + "Mana Count : " + getMana(stack) + "/" + getMaxMana(stack));
					break;
				}
				case 1:
				{
					l.add("Allows you to collect Entropy, then funnel it into the Fusion Crafter to make mobs.");
					if (getEntropy(stack) > 0)
					l.add(TextFormatting.DARK_RED + "Entropy Count : " + getEntropy(stack) + "/" + getMaxEntropy(stack));
					break;
				}case 2:
					{
						l.add("(" + TextFormatting.GOLD + "ARTIFACT" + TextFormatting.GRAY + ")");
						l.add("Allows you to collect INFINITE Mana and Entropy, then funnel it into the Fusion Crafter to make mobs.");
						if (getMana(stack) > 0)
						l.add(TextFormatting.AQUA + "Mana Count : " + getMana(stack) + "/Infinite");
						if (getEntropy(stack) > 0)
						l.add(TextFormatting.DARK_RED + "Entropy Count : " + getEntropy(stack) + "/Infinite");
						break;
					}
				}
			}

			/**
			* returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
			*/
			public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
			{
				ItemStack stack = new ItemStack(this, 1, type == 2 ? 0 : 9);
				if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
				if (type != 1)
				((ItemManaCollector) stack.getItem()).setMana(((ItemManaCollector) stack.getItem()).getMaxMana(stack), stack);
				if (type != 0)
				((ItemManaCollector) stack.getItem()).setEntropy(((ItemManaCollector) stack.getItem()).getMaxEntropy(stack), stack);
				
				if (isInCreativeTab(tab))
				{
					items.add(new ItemStack(this, 1, type == 2 ? 0 : 9));
					items.add(stack);
				}
			}
		}