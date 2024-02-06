package net.minecraft.AgeOfMinecraft.items;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EntityItemCarrier;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStorm;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemCarrier extends Item{
	public ItemCarrier()
	{
		setRegistryName("carrier");
		setUnlocalizedName("carrier");
		setCreativeTab(ETab.engender);
		this.setMaxStackSize(1);
		this.addPropertyOverride(new ResourceLocation("carrying"), new IItemPropertyGetter()
		{
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				return ItemCarrier.this.getState(stack);
			}
		});
		}

		public float getState(ItemStack stack)
		{
			if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
			
			if (stack.getTagCompound().hasKey("Entity"))
			{
				if (stack.getTagCompound().hasKey("WitherStorm"))
				{
					return 1F;
				}

				if (stack.getTagCompound().hasKey("IsHero"))
				{
					if (stack.getTagCompound().hasKey("IsBoss"))
						return 0.6F;
					else
						return 0.4F;
				}
				else
				{
					if (stack.getTagCompound().hasKey("IsBoss"))
						return 0.3F;
					else
						return 0.1F;
				}
			}
			else
			return 0F;
		}

		public EnumRarity getRarity(ItemStack stack)
		{
			return EnumRarity.RARE;
		}
		@Override
		public boolean hasCustomEntity(ItemStack stack)
		{
			return true;
		}

		@Override
		public Entity createEntity(World world, Entity location, ItemStack itemstack)
		{
			EntityItemCarrier newItem = new EntityItemCarrier(world);
			newItem.setPickupDelay(40);
			newItem.setNoDespawn();
			newItem.copyLocationAndAnglesFrom(location);
			newItem.motionX = location.motionX;
			newItem.motionY = location.motionY;
			newItem.motionZ = location.motionZ;
			newItem.setItem(itemstack);
			return newItem;
		}

		/**
		* Returns true if the item can be used on the given entity, e.g. shears on sheep.
		*/
		public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
		{
			if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
			ItemStack item = player.getHeldItem(hand);
			
			if (target instanceof EntityFriendlyCreature)
			{
				EntityFriendlyCreature mob = (EntityFriendlyCreature)target;
				if (!stack.getTagCompound().hasKey("Entity") && mob != null && mob.isEntityAlive() && mob.getPassengers().isEmpty() && mob.getRidingEntity() == null)
				{
					mob.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F);
					mob.playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 1.0F, 2.0F);
					mob.world.setEntityState(mob, (byte)20);
					player.swingArm(hand);
					if (!player.world.isRemote)
					{
						mob.setGuardBlock(null);
						NBTTagCompound tag = mob.serializeNBT();
						item.getTagCompound().setTag("Entity", tag);
						item.getTagCompound().setString("EntityName", mob.getName());
						if (mob.isBoss())
						item.getTagCompound().setBoolean("IsBoss", true);
						if (mob.isHero())
						item.getTagCompound().setBoolean("IsHero", true);
						if (mob instanceof EntityWitherStorm)
						{
							item.getTagCompound().setBoolean("WitherStorm", true);
							mob.onKillCommand();
						}
						else
						{
									mob.setDead();
								}
							}
						}

						return true;
					}
					else
					{
						return false;
					}
				}

				public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
				{
					ItemStack stack = player.getHeldItem(hand);
					if(!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
					if (!stack.getTagCompound().hasKey("Entity"))
					return EnumActionResult.FAIL;
					else
					{
						pos = pos.offset(facing);
						Entity entity = EntityList.createEntityFromNBT(stack.getTagCompound().getCompoundTag("Entity"), world);
						
						if (entity instanceof EntityFriendlyCreature)
						{
							EntityFriendlyCreature entityliving = (EntityFriendlyCreature)entity;
							entityliving.writeToNBT(stack.getTagCompound().getCompoundTag("Entity"));
							entityliving.playLivingSound();
							if (entity instanceof EntityEnderDragon)
							entity.setLocationAndAngles(player.posX, player.posY + 4D, player.posZ, entity.rotationYaw, entity.rotationPitch);
							else
							entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, entity.rotationYaw, entity.rotationPitch);
							if (!player.world.isRemote && entityliving instanceof EntityShulker)
							entityliving.startRiding(player);
							entity.world.setEntityState(entity, (byte)20);
							entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 2.0F);
							entity.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 2.0F);
							entity.playSound(ESound.createMob, 1.0F, 1.0F);
							stack.getTagCompound().removeTag("Entity");
							stack.getTagCompound().removeTag("EntityName");
							if (stack.getTagCompound().hasKey("IsBoss"))
							stack.getTagCompound().removeTag("IsBoss");
							if (stack.getTagCompound().hasKey("IsHero"))
							stack.getTagCompound().removeTag("IsHero");
							if (stack.getTagCompound().hasKey("WitherStorm"))
							stack.getTagCompound().removeTag("WitherStorm");
							if (!world.isRemote)
							world.spawnEntity(entity);
							return EnumActionResult.SUCCESS;
						}
						else
						return EnumActionResult.PASS;
					}
				}

				public static Entity spawnMob(World worldIn, ItemStack stack, double x, double y, double z)
				{
					if(!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
					Entity entity = null;
					
					if (stack.getTagCompound().getCompoundTag("Entity") != null)
					{
						entity = EntityList.createEntityFromNBT(stack.getTagCompound().getCompoundTag("Entity"), worldIn);
						
						if (entity != null && entity instanceof EntityFriendlyCreature)
						{
							EntityFriendlyCreature entityliving = (EntityFriendlyCreature)entity;
							entityliving.writeToNBT(stack.getTagCompound().getCompoundTag("Entity"));
							entityliving.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
							entityliving.rotationYawHead = entityliving.rotationYaw;
							entityliving.renderYawOffset = entityliving.rotationYaw;
							entityliving.playLivingSound();
							entityliving.world.setEntityState(entityliving, (byte)20);
							entityliving.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 2.0F);
							entityliving.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 2.0F);
							entityliving.playSound(ESound.createMob, 1.0F, 1.0F);
							stack.getTagCompound().removeTag("Entity");
							stack.getTagCompound().removeTag("EntityName");
							if (!worldIn.isRemote)
							worldIn.spawnEntity(entityliving);
						}
					}

					return entity;
				}

				public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
				{
					if(!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
					tooltip.add("Pick up one of your engendered mobs and carry them around");
					tooltip.add(TextFormatting.GOLD + "Right click to pick up and place your engendered mob");
					tooltip.add("");
					if(stack.hasTagCompound() && stack.getTagCompound().hasKey("EntityName"))
					tooltip.add(TextFormatting.BLUE + "Holding: " + stack.getTagCompound().getString("EntityName") + TextFormatting.WHITE);
					else
					tooltip.add(TextFormatting.BLUE + "Holding: Nothing");
				}

				public boolean getShareTag()
				{
					return true;
				}
			}