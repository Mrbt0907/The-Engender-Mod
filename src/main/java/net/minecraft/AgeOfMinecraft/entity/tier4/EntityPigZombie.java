package net.minecraft.AgeOfMinecraft.entity.tier4;
import java.util.UUID;

import javax.annotation.Nullable;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPigZombie extends EntityZombie implements IJumpingMount
{
	 private static final DataParameter<Boolean> OLDPEPIGMAN = EntityDataManager.createKey(EntityPigZombie.class, DataSerializers.BOOLEAN);
	 private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
	 private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.075D, 0).setSaved(false);
	 private int angerLevel;
	 private int randomSoundDelay = 40;
	 public EntityPigZombie(World worldIn)
	{
		super(worldIn);
		this.isImmuneToFire = true;
		this.randomSoundDelay = 0;
		this.setToNotVillager();
	}
	protected void entityInit()
	{
		super.entityInit();
		getDataManager().register(OLDPEPIGMAN, Boolean.valueOf(false));
	}
	public boolean isOldPEPigman()
	{
		return ((Boolean)getDataManager().get(OLDPEPIGMAN)).booleanValue();
	}
	public void setOldPEPigman(boolean childZombie)
	{
		getDataManager().set(OLDPEPIGMAN, Boolean.valueOf(childZombie));
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityPigZombie(this.world);
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	}
	protected boolean isValidLightLevel()
	{
		return true;
	}

	public String getName()
	{
		if (hasCustomName())
		{
			return getCustomNameTag();
		}

			String s = EntityList.getEntityString(this);
			
			if (s == null)
			{
				s = "generic";
			}

			return TranslateUtil.translateServer("entity." + s + ".name");
		
	}



	protected float getSoundPitch()
	{
		return super.getSoundPitch();
	}
	public void performSpecialAttack()
	{
		getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 5.0F);
		this.playSound(ESound.pigmanSpecial, this.getSoundVolume(), 1F);
		setSpecialAttackTimer(500);
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 4.0D) && (this.ticksExisted % 1 == 0) && (this.moralRaisedTimer > 200))
		{
			attackEntityAsMob(getAttackTarget());
		}
		if ((getAttackTarget() != null) && (getAttackTarget().isEntityAlive()) && (getDistanceSq(getAttackTarget()) < getAttackTarget().width + 9D) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			this.jump();
			getAttackTarget().motionY += 2D;
			this.playSound(ESound.pigmanSpecial, this.getSoundVolume(), 1F);
			performSpecialAttack();
		}
	}
	protected void updateAITasks()
	{
		IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if (isAngry())
		{
			if (!iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER))
			{
				setSprinting(true);
				iattributeinstance.applyModifier(ATTACK_SPEED_BOOST_MODIFIER);
			}
			this.angerLevel -= 1;
		}
		else if (iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER))
		{
			iattributeinstance.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
		}
		if ((this.randomSoundDelay > 0) && (--this.randomSoundDelay == 0))
		{
			playSound(SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, 10000.0F, getSoundPitch() + 1.8F);
		}
		if (this.moralRaisedTimer > 200)
		{
			addPotionEffect(new PotionEffect(MobEffects.SPEED, 20, 9));
			this.angerLevel = 600;
			playSound(SoundEvents.ENTITY_ZOMBIE_PIG_ANGRY, 1.0F, getSoundPitch() + 1.8F);
			this.hurtResistantTime = 0;
			this.limbSwingAmount *= 6.0F;
		}
		super.updateAITasks();
	}
	public boolean isNotColliding()
	{
		return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox());
	}
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setShort("Anger", (short)this.angerLevel);
		tagCompound.setBoolean("OldPEPigman", isOldPEPigman());
	}
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.angerLevel = tagCompund.getShort("Anger");
		setOldPEPigman(tagCompund.getBoolean("OldPEPigman"));
	}
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (isEntityInvulnerable(source))
		{
			return false;
		}
		Entity entity = source.getTrueSource();
		if ((entity instanceof EntityLivingBase))
		{
			becomeAngryAt(entity);
		}
		return super.attackEntityFrom(source, amount);
	}
	public void becomeAngryAt(Entity p_70835_1_)
	{
		this.angerLevel = 600;
		this.randomSoundDelay = this.rand.nextInt(150);
		if (((p_70835_1_ instanceof EntityLivingBase)) && (!isOnSameTeam((EntityLivingBase)p_70835_1_)))
		{
			setAttackTarget((EntityLivingBase)p_70835_1_);
		}
	}

	protected float jumpPower;
	
	public void setJumpPower(int jumpPowerIn)
	{
		if (this.isBeingRidden())
		{
			if (jumpPowerIn < 0)
			{
				jumpPowerIn = 0;
			}

			if (jumpPowerIn >= 90)
			{
				this.jumpPower = 1.0F;
			}
			else
			{
				this.jumpPower = 0.4F + 0.4F * (float)jumpPowerIn / 90.0F;
			}
		}
	}

	public boolean canJump()
	{
		return true;
	}

	public void handleStartJump(int p_184775_1_)
	{
		this.playLivingSound();
	}

	public void handleStopJump()
	{
	}public void travel(float strafe, float vertical, float forward)
		{
			if (isBeingRidden())
			{
				this.stepHeight = 1F;
				EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
				this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
				this.rotationPitch = entitylivingbase.rotationPitch;
				setRotation(this.rotationYaw, this.rotationPitch);
				this.rotationYawHead = (this.renderYawOffset = this.rotationYaw);
				strafe = entitylivingbase.moveStrafing;
				forward = entitylivingbase.moveForward;
				if (canPassengerSteer())
				{
					setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
					super.travel(strafe, vertical, forward);
				}
				else if ((entitylivingbase instanceof EntityPlayer))
				{
					this.motionX = 0.0D;
					this.motionY = 0.0D;
					this.motionZ = 0.0D;
				}
				if (this.jumpPower > 0.0F && this.onGround)
				{
					this.motionY = 0.8D * (double)this.jumpPower;
					
					if (this.isPotionActive(MobEffects.JUMP_BOOST))
					{
						this.motionY += (double)((float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
					}

					this.isAirBorne = true;
					
					if (forward > 0.0F)
					{
						float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
						float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
						this.motionX += (double)(-0.4F * f * this.jumpPower);
						this.motionZ += (double)(0.4F * f1 * this.jumpPower);
					}

					this.jumpPower = 0.0F;
				}

				this.prevLimbSwingAmount = this.limbSwingAmount;
				double d5 = this.posX - this.prevPosX;
				double d7 = this.posZ - this.prevPosZ;
				float f10 = MathHelper.sqrt(d5 * d5 + d7 * d7) * 4.0F;
				
				if (f10 > 1.0F)
				{
					f10 = 1.0F;
				}

				this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
				this.limbSwing += this.limbSwingAmount;
			}
			else {
				super.travel(strafe, vertical, forward);
			}
		}

		public boolean isAngry()
		{
			return this.angerLevel > 0;
		}
		protected SoundEvent getAmbientSound()
		{
			return SoundEvents.ENTITY_ZOMBIE_PIG_AMBIENT;
		}
		protected SoundEvent getHurtSound(DamageSource source)
		{
			return SoundEvents.ENTITY_ZOMBIE_PIG_HURT;
		}
		protected SoundEvent getDeathSound()
		{
			return SoundEvents.ENTITY_ZOMBIE_PIG_DEATH;
		}
		@Nullable
		protected ResourceLocation getLootTable()
		{
			return ELoot.ENTITIES_ZOMBIE_PIGMAN;
		}
		protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
		{
			this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
			if (getRNG().nextInt(3) == 0)
			{
				int i = this.rand.nextInt(3);
				
				if (i == 0)
				{
					this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
				}
				else
				{
					this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.GOLDEN_SWORD));
				}
			}
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
		}
		@Nullable
		public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
		{
			super.onInitialSpawn(difficulty, livingdata);
			setToNotVillager();
			if (!this.world.isRemote && this.world.rand.nextInt(4) == 0)
			this.setOldPEPigman(true);
			return livingdata;
		}
	}