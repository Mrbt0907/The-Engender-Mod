package net.minecraft.AgeOfMinecraft.entity.tier3;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Undead;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAttackRangedBowAlly;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeSnow;

public class EntitySkeleton
extends EntityFriendlyCreature
implements IRangedAttackMob, Undead
{
	private static final DataParameter<Integer> SKELETON_VARIANT = EntityDataManager.createKey(EntitySkeleton.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntitySkeleton.class, DataSerializers.BOOLEAN);
	private final EntityAIAttackRangedBowAlly<EntitySkeleton> aiArrowAttack = new EntityAIAttackRangedBowAlly<EntitySkeleton>(this, 1.0D, 5, 15.0F);
	private final EntityAIFriendlyAttackMelee aiAttackOnCollide = new EntityAIFriendlyAttackMelee(this, 1.2D, true)
	{
		public void startExecuting()
		{
			super.startExecuting();
			ItemStack itemstack = EntitySkeleton.this.getHeldItem(EnumHand.MAIN_HAND);
			if (((itemstack == null) || !(itemstack.getItem() instanceof ItemBow)) && (EntitySkeleton.this.getRidingEntity() != null) && ((EntitySkeleton.this.getRidingEntity() instanceof EntitySkeletonHorse)))
			{
				((EntitySkeletonHorse)EntitySkeleton.this.getRidingEntity()).getNavigator().setPath(EntitySkeleton.this.getNavigator().getPathToEntityLiving(EntitySkeleton.this.getAttackTarget()), 1.2D);
			}
		}
	};
	private int helmetCount = 1;
	public EntitySkeleton(World worldIn)
	{
		super(worldIn);
		this.isOffensive = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(2, new EntityAIFleeSun(this, 1.0D));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.2D, 32.0F, 6.0F));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		if ((getSkeletonType() == 1))
		{
			setSize(0.6F, 2.39F);
		}
		else
		{
			setSize(0.5F, 1.95F);
		}
		if ((worldIn != null) && (!worldIn.isRemote))
		{
			setCombatTask();
		}
		this.experienceValue = 4;
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntitySkeleton(this.world);
	}
	public int timesToConvert()
	{
		return getSkeletonType() != 0 ? 27 : 13;
	}
	public EnumTier getTier()
	{
		return getSkeletonType() != 0 ? EnumTier.TIER4 : EnumTier.TIER3;
	}
	public String getName()
	{
		if (hasCustomName())
		{
			return getCustomNameTag();
		}
			if ((getRidingEntity() != null) && ((getRidingEntity() instanceof EntitySkeletonHorse)))
			{
				switch (this.getSkeletonType())
				{
					case 1:
					return TranslateUtil.translateServer("entity.WitherSkeletonHorsemanHelpful.name");
					case 2:
					return TranslateUtil.translateServer("entity.StrayHorsemanHelpful.name");
					default:
					return TranslateUtil.translateServer("entity.SkeletonHorsemanHelpful.name");
				}
			}
			else
			{
				switch (this.getSkeletonType())
				{
					case 1:
					return TranslateUtil.translateServer("entity.WitherSkeletonHelpful.name");
					case 2:
					return TranslateUtil.translateServer("entity.StrayHelpful.name");
					default:
					return TranslateUtil.translateServer("entity.SkeletonHelpful.name");
				}
			}
		
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6D);
	}
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SKELETON_VARIANT, Integer.valueOf(0));
		this.dataManager.register(ATTACKING, Boolean.valueOf(false));
	}
	
	protected SoundEvent getAmbientSound()
	{
		switch (this.getSkeletonType())
		{
			case 1:
			return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
			case 2:
			return SoundEvents.ENTITY_STRAY_AMBIENT;
			default:
			return SoundEvents.ENTITY_SKELETON_AMBIENT;
		}
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		switch (this.getSkeletonType())
		{
			case 1:
			return SoundEvents.ENTITY_WITHER_SKELETON_HURT;
			case 2:
			return SoundEvents.ENTITY_STRAY_HURT;
			default:
			return SoundEvents.ENTITY_SKELETON_HURT;
		}
	}
	protected SoundEvent getDeathSound()
	{
		switch (this.getSkeletonType())
		{
			case 1:
			return SoundEvents.ENTITY_WITHER_SKELETON_DEATH;
			case 2:
			return SoundEvents.ENTITY_STRAY_DEATH;
			default:
			return SoundEvents.ENTITY_SKELETON_DEATH;
		}
	}
	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		switch (this.getSkeletonType())
		{
			case 1:
			playSound(SoundEvents.ENTITY_WITHER_SKELETON_STEP, 0.1F, (1F) / this.getFittness());
			case 2:
			playSound(SoundEvents.ENTITY_STRAY_STEP, 0.1F, ( 1F) / this.getFittness());
			default:
			playSound(SoundEvents.ENTITY_SKELETON_STEP, 0.1F, (1F) / this.getFittness());
		}
	}

	protected float getSoundPitch()
	{
		return super.getSoundPitch();
	}
	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		if (super.attackEntityAsMob(p_70652_1_))
		{
			if ((getSkeletonType() == 1) && ((p_70652_1_ instanceof EntityLivingBase)))
			{
				int i = 5;
				if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
				{
					i = 10;
				}
				else if (this.world.getDifficulty() == EnumDifficulty.HARD)
				{
					i = 20;
				}
				if (i > 0)
				{
					((EntityLivingBase)p_70652_1_).addPotionEffect(new PotionEffect(MobEffects.WITHER, i * 20, 0 + this.world.getDifficulty().getDifficultyId()));
				}
			}

			if ((getSkeletonType() == 2) && ((p_70652_1_ instanceof EntityLivingBase)))
			{
				int i = 10;
				if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
				{
					i = 20;
				}
				else if (this.world.getDifficulty() == EnumDifficulty.HARD)
				{
					i = 40;
				}
				if (i > 0)
				{
					((EntityLivingBase)p_70652_1_).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, i * 20, 0 + this.world.getDifficulty().getDifficultyId()));
				}
			}
			if (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null && getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemBow)
			{
				this.swingArm(EnumHand.OFF_HAND);
				this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundVolume(), 1F);
				this.knockBack(this, 0.5F, MathHelper.sin(this.rotationYawHead * 0.017453292F), -MathHelper.cos(this.rotationYawHead * 0.017453292F));
				((EntityLivingBase)p_70652_1_).knockBack(this, 1F, MathHelper.sin(this.rotationYawHead * 0.017453292F), -MathHelper.cos(this.rotationYawHead * 0.017453292F));
			}

			if (getItemStackFromSlot(EntityEquipmentSlot.OFFHAND) != null && getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == Items.SHIELD)
			{
				this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, this.getSoundVolume(), 1F);
				this.knockBack(this, 0.5F, -MathHelper.sin(this.rotationYawHead * 0.017453292F), MathHelper.cos(this.rotationYawHead * 0.017453292F));
				((EntityLivingBase)p_70652_1_).knockBack(this, 1F, MathHelper.sin(this.rotationYawHead * 0.017453292F), -MathHelper.cos(this.rotationYawHead * 0.017453292F));
			}

			if (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null && (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == Items.FLINT_AND_STEEL || getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == Items.FIRE_CHARGE))
			{
				p_70652_1_.setFire(12);
			}
			return true;
		}
		return false;
	}
	public boolean isPotionApplicable(PotionEffect potioneffectIn)
	{
		return potioneffectIn.getPotion() == MobEffects.WITHER && this.getSkeletonType() == 1 ? false : super.isPotionApplicable(potioneffectIn);
	}
	public boolean isEntityUndead()
	{
		return true;
	}
	public void performSpecialAttack()
	{
		if ((getHeldItem(EnumHand.MAIN_HAND) != null) && (getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow))
		{
			setSpecialAttackTimer(500);
			playSound(ESound.skeletonSpecial, 10.0F, 1.0F);
		}
		else
		{
			getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 5.0F);
			setSpecialAttackTimer(500);
		}
	}
	public void onLivingUpdate()
	{
		ItemStack hats = this.helmetCount > 0 ? new ItemStack(Items.LEATHER_HELMET, this.helmetCount) : ItemStack.EMPTY;
		basicInventory.setInventorySlotContents(7, hats);
		if (this.isRiding() && this.getRidingEntity() instanceof EntitySkeletonHorse)
		this.getPassengers().clear();
		
		if (getOwner() != null)
		{
			if (getDistanceSq(getOwner()) >= 2304.0D && this.isElytraFlying())
			{
				double d01 = getOwner().posX - this.posX;
				double d11 = getOwner().posY - this.posY;
				double d21 = getOwner().posZ - this.posZ;
				float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11 + d21 * d21);
				this.motionX = (d01 / f2 * 0.5D * 0.5D + this.motionX * 0.5D);
				this.motionY = (d11 / f2 * 0.5D * 0.5D + this.motionZ * 0.5D);
				this.motionZ = (d21 / f2 * 0.5D * 0.5D + this.motionZ * 0.5D);
				faceEntity(getOwner(), 180.0F, 180.0F);
			}
		}

		if (this.world.canSeeSky(getPosition()) && ((this.getAttackTarget() != null && this.onGround) || (!this.isWild() && this.getOwner().posY > this.posY && this.getOwner().isElytraFlying())) && getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA)
		{
			this.motionY = 1F;
			this.setFlag(7, true);
		}

		if (!this.onGround && getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA && !this.isElytraFlying())
		this.setFlag(7, true);
		if (this.onGround && this.isElytraFlying())
		this.setFlag(7, false);
		
		if (this.isElytraFlying())
		{
			this.renderYawOffset = this.rotationYaw = -((float)MathHelper.atan2(this.motionX, this.motionZ)) * (180F / (float)Math.PI);
		}

		if ((getSkeletonType() == 1))
		{
			setSize(0.6F, 2.39F);
		}
		else
		{
			setSize(0.5F, 1.95F);
		}

		ItemStack item = getHeldItem(EnumHand.MAIN_HAND);
		ItemStack secitem = getHeldItem(EnumHand.OFF_HAND);
		if (!item.isEmpty() && !secitem.isEmpty() && (item.getItem() instanceof ItemBow) && !(secitem.getItem() instanceof ItemBow) && (secitem.getItem() != Items.TIPPED_ARROW) && (getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) <= 128.0D) && (getAttackTarget().posY <= this.posY + 3.0D))
		{
			setItemStackToSlot(EntityEquipmentSlot.MAINHAND, secitem);
			setItemStackToSlot(EntityEquipmentSlot.OFFHAND, item);
		}
		if (!item.isEmpty() && !secitem.isEmpty() && (secitem.getItem() instanceof ItemBow) && !(item.getItem() instanceof ItemBow) && (item.getItem() != Items.TIPPED_ARROW) && (getAttackTarget() != null) && ((getDistanceSq(getAttackTarget()) > 128.0D) || ((getAttackTarget().posY > this.posY + 3.0D))))
		{
			setItemStackToSlot(EntityEquipmentSlot.MAINHAND, secitem);
			setItemStackToSlot(EntityEquipmentSlot.OFFHAND, item);
		}
		if (!item.isEmpty() && (item.getItem() instanceof ItemBow))
		{
			if ((getAttackTarget() != null) && (getAttackTarget().isEntityAlive()) && (getDistanceSq(getAttackTarget()) < 512.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
			{
				performSpecialAttack();
			}
			if ((getAttackTarget() != null) && (isHero()) && (getSpecialAttackTimer() < 490) && (getSpecialAttackTimer() > 470))
			{
				attackEntityWithRangedAttack(getAttackTarget(), 2.0F);
			}
		}
		else if ((getAttackTarget() != null) && (getAttackTarget().isEntityAlive()) && (getDistanceSq(getAttackTarget()) < 32.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			performSpecialAttack();
		}
		if ((getRidingEntity() != null) && ((getRidingEntity() instanceof EntitySkeletonHorse)))
		{
			getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
			if (isSneaking())
			{
				getRidingEntity().setSneaking(true);
			} else {
					getRidingEntity().setSneaking(false);
				}
				if (isSprinting())
				{
					getRidingEntity().setSprinting(true);
				} else {
						getRidingEntity().setSprinting(false);
					}
				}
				if ((getSkeletonType() == 1 && getRidingEntity() == null))
				{
					getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
				}

				if ((this.world.isRemote) && (getSkeletonType() == 1))
				{
					this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
				}
				if (this.helmetCount < 0)
				{
					this.helmetCount = 0;
				}
				if (((getSkeletonType() == 1) || (isAntiMob()) || (isChild()) || (isHero())) && (this.helmetCount != 0))
				{
					if (!this.world.isRemote)
					{
						for (int i = 0; i < this.helmetCount; i++)
						{
							playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
							this.dropItem(Items.LEATHER_HELMET, 1);
							this.helmetCount -= 1;
						}
					}
				}

				ItemStack head = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				
				if (!head.isEmpty() && head.getItem() == Items.LEATHER_HELMET && this.isEntityAlive() && !this.world.isDaytime() && !this.world.isRemote)
				{
					swingArm(EnumHand.MAIN_HAND);
					this.helmetCount += 1;
					setItemStackToSlot(EntityEquipmentSlot.HEAD,ItemStack.EMPTY);
					playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
				}

				if (this.isEntityAlive() && this.world.isDaytime() && !this.world.isRemote && !isChild() && getSkeletonType() != 1 && !this.isAntiMob() && !isHero() && !isImmuneToFire())
				{
					float f = this.getBrightness();
					
					if (f > 0.5F && this.ticksExisted % (!this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() ? 80 : 10) == 0 && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)))
					{
						boolean flag = true;
						ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
						
						if (!itemstack.isEmpty())
						{
							if (itemstack.isItemStackDamageable())
							{
								itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
								
								if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
								{
									this.renderBrokenItemStack(itemstack);
									this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
								}
							}

							flag = false;
						}

						if (flag)
						{
							if (this.helmetCount > 0)
							{
								swingArm(EnumHand.MAIN_HAND);
								swingArm(EnumHand.OFF_HAND);
								this.helmetCount -= 1;
								setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
								playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
							}
							else
							{
								setFire(8);
							}
						}
					}
				}
				super.onLivingUpdate();
			}
			public void updateRidden()
			{
				super.updateRidden();
				if ((getRidingEntity() instanceof EntityCreature))
				{
					EntityCreature entitycreature = (EntityCreature)getRidingEntity();
					this.renderYawOffset = entitycreature.renderYawOffset;
					entitycreature.rotationPitch = this.rotationPitch;
					entitycreature.rotationYawHead = this.rotationYawHead;
					if (getAttackTarget() != null)
					{
						entitycreature.setAttackTarget(getAttackTarget());
					}
					if (this.ticksExisted % 40 == 0)
					{
						this.renderYawOffset = (this.rotationYaw = this.rotationYawHead);
						if (getAttackTarget() == null)
						{
							entitycreature.heal(5.0F);
						} else {
								entitycreature.heal(1.0F);
							}
						}
					}
				}
				public void onDeath(DamageSource cause)
				{
					super.onDeath(cause);
					if (!this.world.isRemote)
					{
						for (int i = 0; i < this.helmetCount; i++)
						{
							this.dropItem(Items.LEATHER_HELMET, 1);
							this.helmetCount -= 1;
						}
					}

					if (((cause.getTrueSource() instanceof EntityCreeper)) && (((EntityCreeper)cause.getTrueSource()).getPowered()))
					{
						entityDropItem(new ItemStack(Items.SKULL, 1, getSkeletonType() == 1 ? 1 : 0), 0.0F);
					}
				}
				@Nullable
				protected ResourceLocation getLootTable()
				{
					switch (this.getSkeletonType())
					{
						case 1:
						return ELoot.ENTITIES_WITHER_SKELETON;
						case 2:
						return ELoot.ENTITIES_STRAY;
						default:
						return ELoot.ENTITIES_SKELETON;
					}
				}
				protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
				{
					if (this.rand.nextFloat() < 0.25F * difficulty.getClampedAdditionalDifficulty())
					{
						int i = this.rand.nextInt(2);
						float f = this.world.getDifficulty() == EnumDifficulty.HARD ? 0.325F : 0.25F;
						
						if (this.rand.nextFloat() < 0.1F)
						{
							++i;
						}

						if (this.rand.nextFloat() < 0.15F)
						{
							++i;
						}

						if (this.rand.nextFloat() < 0.2F)
						{
							++i;
						}

						boolean flag = true;
						
						for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
						{
							if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
							{
								ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
								
								if (!flag && this.rand.nextFloat() < f)
								{
									break;
								}

								flag = false;
								
								if (itemstack.isEmpty())
								{
									Item item = getArmorByChance(entityequipmentslot, i);
									
									if (item != null)
									{
										this.setItemStackToSlot(entityequipmentslot, new ItemStack(item));
									}
								}
							}
						}
					}
					if (this.getSkeletonType() == 1)
					{
						this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
						if (getRNG().nextInt(3) == 0)
						{
							int i = this.rand.nextInt(3);
							
							if (i == 0)
							{
								this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
							}
							else
							{
								this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.STONE_SWORD));
							}
						}
					}
					else
					{
						setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
						if (this.rand.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD ? 0.05F : 0.01F))
						{
							int i = this.rand.nextInt(3);
							ItemStack stack = new ItemStack(Items.TIPPED_ARROW);
							if (i == 0)
							{
								PotionUtils.addPotionToItemStack(stack, PotionTypes.STRONG_POISON);
								this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
							}
							else
							{
								PotionUtils.addPotionToItemStack(stack, PotionTypes.LONG_WEAKNESS);
								this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
							}
						}
						else if (getRNG().nextInt(20) == 0)
						{
							this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.WOODEN_SWORD));
						}
					}
				}

				@Nullable
				public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
				{
					livingdata = super.onInitialSpawn(difficulty, livingdata);
					Biome biome = this.world.getBiome(new BlockPos(this));
					
					if ((this.world.provider instanceof WorldProviderHell && this.rand.nextInt(5) != 0) || this.getSkeletonType() == 1)
					{
						this.tasks.addTask(4, this.aiAttackOnCollide);
						setSkeletonType(1);
					}
					else if (biome instanceof BiomeSnow && this.rand.nextInt(5) != 0)
					{
						setSkeletonType(2);
						this.tasks.addTask(4, this.aiArrowAttack);
					}
					else {
						this.tasks.addTask(4, this.aiArrowAttack);
					}
					setEquipmentBasedOnDifficulty(difficulty);
					setEnchantmentBasedOnDifficulty(difficulty);
					if (getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null)
					{
						Calendar calendar = this.world.getCurrentDate();
						if ((calendar.get(2) + 1 == 10) && (calendar.get(5) == 31) && (this.rand.nextFloat() < 0.25F))
						{
							setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
							this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
						}
					}
					return livingdata;
				}
				public void setCombatTask()
				{
					if ((this.world != null) && (!this.world.isRemote))
					{
						this.tasks.removeTask(this.aiAttackOnCollide);
						this.tasks.removeTask(this.aiArrowAttack);
						ItemStack itemstack = getHeldItemMainhand();
						
						if ((itemstack != null) && (itemstack.getItem() instanceof ItemBow))
						{
							this.tasks.addTask(4, this.aiArrowAttack);
						}
						else
						{
							this.tasks.addTask(4, this.aiAttackOnCollide);
						}
					}
				}
				public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_)
				{
					
						EntityTippedArrowOther entityarrow = new EntityTippedArrowOther(this.world, this);
						double d0 = target.posX - this.posX;
						double d1 = (target.posY + target.getEyeHeight()) - (this.posY + (double)this.getEyeHeight() - 0.10000000149011612D);
						double d2 = target.posZ - this.posZ;
						double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
						entityarrow.shoot(d0, d1 + d3 * (getDistance(target) * 0.013D), d2, 1.4F, isHero() ? 1F : 9F);
						int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
						int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
						if (getRidingEntity() != null)
						{
							entityarrow.setDamage(p_82196_2_ * 3.0F + this.rand.nextGaussian() * 0.25D + 0.5D);
						} else {
								entityarrow.setDamage(p_82196_2_ * 1.5F + this.rand.nextGaussian() * 0.25D + 0.5D);
							}
							if (target instanceof EntityDragon && this.posY < target.posY - 10D)
							entityarrow.motionY += 1D;
							if (i > 0)
							{
								entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D);
							}

							if (this.isHero())
							{
								entityarrow.setDamage(entityarrow.getDamage() * 2D);
							}

							if (this.moralRaisedTimer > 200)
							{
								entityarrow.setDamage(entityarrow.getDamage() * 1.5D);
							}
							if (getRidingEntity() != null)
							{
								j += 2;
								entityarrow.setIsCritical(true);
							}
							if (j > 0)
							{
								entityarrow.setKnockbackStrength(j);
							}
							if ((getSkeletonType() == 2))
							{
								entityarrow.addEffect(new PotionEffect(MobEffects.SLOWNESS, 600));
							}
							if ((EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, this) > 0) || (getSkeletonType() == 1))
							{
								entityarrow.setFire(100);
							}
							if ((getHeldItemOffhand() != null) && (getHeldItemOffhand().getItem() == Items.TIPPED_ARROW))
							{
								entityarrow.setPotionEffect(getHeldItemOffhand());
							}
							playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
						this.world.spawnEntity(entityarrow);
						
					}
					public boolean isBurning()
					{
						return getSkeletonType() == 1 ? false : super.isBurning();
					}
					public int getSkeletonType()
					{
						return ((Integer)this.dataManager.get(SKELETON_VARIANT)).intValue();
					}
					public void setSkeletonType(int p_82201_1_)
					{
						this.dataManager.set(SKELETON_VARIANT, Integer.valueOf(p_82201_1_));
						this.isImmuneToFire = (p_82201_1_ == 1);
						if (p_82201_1_ == 1)
						{
							getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
						}
					}
					public void readEntityFromNBT(NBTTagCompound tagCompund)
					{
						super.readEntityFromNBT(tagCompund);
						if (tagCompund.hasKey("SkeletonType", 99))
						{
							int i = tagCompund.getByte("SkeletonType");
							setSkeletonType(i);
						}
						if ((tagCompund.hasKey("Helmets", 99)))
						{
							this.helmetCount = tagCompund.getInteger("Helmets");
						}
						setCombatTask();
					}
					public void writeEntityToNBT(NBTTagCompound tagCompound)
					{
						super.writeEntityToNBT(tagCompound);
						tagCompound.setByte("SkeletonType", (byte)getSkeletonType());
						tagCompound.setInteger("Helmets", this.helmetCount);
					}
					public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
					{
						super.setItemStackToSlot(slotIn, stack);
						if ((!this.world.isRemote) && (slotIn == EntityEquipmentSlot.MAINHAND))
						{
							setCombatTask();
						}
					}
					public float getEyeHeight()
					{
						return this.height * 0.84F;
					}
					public double getYOffset()
					{
						return getSkeletonType() == 1 ? super.getYOffset() - 0.54D : super.getYOffset() - 0.45D;
					}
					public boolean interact(EntityPlayer player, EnumHand hand)
					{
						ItemStack stack = player.getHeldItem(hand);
						ItemStack heldItem = new ItemStack(stack.getItem());
						
						if (this.isOnSameTeam(player) && stack.isEmpty() && player.isSneaking() && this.getRidingEntity() == null)
						{
							List<EntitySkeletonHorse> list = this.world.getEntitiesWithinAABB(EntitySkeletonHorse.class, getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
							if ((list != null) && (!list.isEmpty()) && !this.isBeingRidden())
							{
								for (int i1 = 0; i1 < list.size(); i1++)
								{
									EntitySkeletonHorse entity = (EntitySkeletonHorse)list.get(i1);
									if (entity != null && !entity.isBeingRidden() && !this.world.isRemote)
									{
										entity.setHorseTamed(true);
										entity.setRearing(true);
										entity.ticksExisted = 0;
										this.startRiding(entity);
										playSound(SoundEvents.ENTITY_HORSE_ARMOR, 1.0F, 1.0F);
										break;
									}
								}
							}
							return true;
						}
						else if (!stack.isEmpty() && (stack.getItem() == Items.LEATHER_HELMET))
						{
							this.helmetCount += 1;
							playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
							player.swingArm(hand);
							if (!this.world.isRemote)
							stack.shrink(1);
							return true;
						}
						else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) && (getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD || stack.getItem() == Items.BONE || stack.getItem() == Item.getItemFromBlock(Blocks.END_ROD) || stack.getItem() == Items.FEATHER) && stack.getItem() != Items.LEATHER_HELMET)
						{
							setItemStackToSlot(EntityEquipmentSlot.HEAD, stack);
							this.playEquipSound(stack);
							player.swingArm(hand);
							if (!this.world.isRemote)
							{
								heldItem.setTagCompound(stack.getTagCompound());
								heldItem.setItemDamage(stack.getItemDamage());
								setItemStackToSlot(EntityEquipmentSlot.HEAD, heldItem);
								stack.shrink(1);
							}
							return true;
						}
						else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()) && getSlotForItemStack(stack) == EntityEquipmentSlot.CHEST)
						{
							setItemStackToSlot(EntityEquipmentSlot.CHEST, stack);
							this.playEquipSound(stack);
							player.swingArm(hand);
							if (!this.world.isRemote)
							{
								heldItem.setTagCompound(stack.getTagCompound());
								heldItem.setItemDamage(stack.getItemDamage());
								setItemStackToSlot(EntityEquipmentSlot.CHEST, heldItem);
								stack.shrink(1);
							}
							return true;
						}
						else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()) && getSlotForItemStack(stack) == EntityEquipmentSlot.LEGS)
						{
							setItemStackToSlot(EntityEquipmentSlot.LEGS, stack);
							this.playEquipSound(stack);
							player.swingArm(hand);
							if (!this.world.isRemote)
							{
								heldItem.setTagCompound(stack.getTagCompound());
								heldItem.setItemDamage(stack.getItemDamage());
								setItemStackToSlot(EntityEquipmentSlot.LEGS, heldItem);
								stack.shrink(1);
							}
							return true;
						}
						else if (!stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty()) && getSlotForItemStack(stack) == EntityEquipmentSlot.FEET)
						{
							setItemStackToSlot(EntityEquipmentSlot.FEET, stack);
							this.playEquipSound(stack);
							player.swingArm(hand);
							if (!this.world.isRemote)
							{
								heldItem.setTagCompound(stack.getTagCompound());
								heldItem.setItemDamage(stack.getItemDamage());
								setItemStackToSlot(EntityEquipmentSlot.FEET, heldItem);
								stack.shrink(1);
							}
							return true;
						}

						else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) && stack.getItem() != Items.NAME_TAG && ((getSlotForItemStack(stack) == EntityEquipmentSlot.MAINHAND) || (stack.getItem() instanceof ItemSword) || (stack.getItem() instanceof ItemTool) || (stack.getItem() == Items.BOW)))
						{
							playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 2.0F);
							player.swingArm(hand);
							if (!this.world.isRemote)
							{
								heldItem.setTagCompound(stack.getTagCompound());
								heldItem.setItemDamage(stack.getItemDamage());
								setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem);
								stack.shrink(1);
							}
							return true;
						}
						else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty()) && ((getSlotForItemStack(stack) == EntityEquipmentSlot.OFFHAND) || (stack.getItem() instanceof ItemSword) || (stack.getItem() instanceof ItemTool) || (stack.getItem() instanceof ItemFood && (!(stack.getItem() instanceof ItemAppleGold))) || (stack.getItem() == Items.TIPPED_ARROW) || (stack.getItem().getItemUseAction(stack) == EnumAction.BLOCK) || (stack.getItem() == Items.TOTEM_OF_UNDYING)))
						{
							playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 2.0F);
							player.swingArm(hand);
							if (!this.world.isRemote)
							{
								heldItem.setTagCompound(stack.getTagCompound());
								heldItem.setItemDamage(stack.getItemDamage());
								setItemStackToSlot(EntityEquipmentSlot.OFFHAND, heldItem);
								stack.shrink(1);
							}
							return true;
						}
						else
						{
							return false;
						}
					}
					protected SoundEvent getRegularHurtSound()
					{
						return getTotalArmorValue() > 10 ? ESound.metalHit : ESound.woodHit;
					}
					protected SoundEvent getPierceHurtSound()
					{
						return ESound.woodHitPierce;
					}
					protected SoundEvent getCrushHurtSound()
					{
						return ESound.woodHitCrush;
					}
					@Override
					public void setSwingingArms(boolean swingingArms) {}
				}

				
				