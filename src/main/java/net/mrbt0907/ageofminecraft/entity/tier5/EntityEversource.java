package net.mrbt0907.ageofminecraft.entity.tier5;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.entity.EntityFriendlyCreature;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.Light;
import net.mrbt0907.ageofminecraft.entity.ai.EntityAIFollowLeader;
import net.mrbt0907.ageofminecraft.entity.tier3.EntityZombie;
import net.mrbt0907.ageofminecraft.registry.ItemRegistry;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;
import net.mrbt0907.ageofminecraft.registry.SoundRegistry;

public class EntityEversource
extends EntityFriendlyCreature implements IJumpingMount, Light
{
	public float wingRotation;
	public float destPos;
	public float field_70884_g;
	public float field_70888_h;
	public float wingRotDelta = 1.0F;
	public int timeUntilNextEgg;
	public boolean chickenJockey;
	public EntityEversource(World worldIn)
	{
		super(worldIn);
		this.timeUntilNextEgg = 200 + this.rand.nextInt(600);
		setPathPriority(PathNodeType.WATER, 0.0F);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.0D, 16F, 8F));
		this.tasks.addTask(5, new EntityAIWander(this, 0.5D, 80));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.experienceValue = 20;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityEversource(this.world);
	}
	public float getEyeHeight()
	{
		return this.height * 0.95F;
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER5;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}public double getKnockbackResistance()
		{
			return 1D;
		}

		public boolean canBeMatedWith()
		{
			return false;
		}

		public boolean canBeMarried()
		{
			return false;
		}

		public float getBlockPathWeight(BlockPos pos)
		{
			return this.world.getBlockState(pos.down()).getBlock() == this.spawnableBlock ? 10.0F : this.world.getLightBrightness(pos) - 0.5F;
		}
		public void onLivingUpdate()
		{
			super.onLivingUpdate();
			setSize(0.4F, 0.7F);
			
			if (!this.world.isRemote && this.isEntityAlive() && (getAttackTarget() != null) && getAttackTarget().isEntityAlive() && !this.isOnSameTeam(getAttackTarget()) && this.getDistanceSq(getAttackTarget()) < (double)((this.width * this.width) + (getAttackTarget().width * getAttackTarget().width) + 9D))
			attackEntityAsMob(getAttackTarget());
			
			if ((getControllingPassenger() != null) && ((getControllingPassenger() instanceof EntityZombie)))
			{
				EntityZombie passenger = (EntityZombie)getControllingPassenger();
				this.renderYawOffset = (this.rotationYaw = this.rotationYawHead = passenger.rotationYawHead);
				this.rotationPitch = passenger.rotationPitch;
				if (passenger.getAttackTarget() != null)
				{
					getNavigator().tryMoveToEntityLiving(passenger.getAttackTarget(), 1.0D);
				}
			}
			this.field_70888_h = this.wingRotation;
			this.field_70884_g = this.destPos;
			this.destPos = ((float)(this.destPos + (this.onGround ? -1 : 4) * 0.3D));
			this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
			if ((!this.onGround) && (this.wingRotDelta < 1.0F))
			{
				this.wingRotDelta = 1.0F;
			}
			this.wingRotDelta = ((float)(this.wingRotDelta * 0.9D));
			if (!this.onGround && this.motionY < 0.0D && this.isEntityAlive())
			this.motionY *= 0.6D;
			this.wingRotation += this.wingRotDelta * 2.0F;
			if ((!this.world.isRemote) && (!isChild()) && (!isChickenJockey()) && (--this.timeUntilNextEgg <= 0))
			{
				playSound(SoundRegistry.createMob, this.getSoundVolume(), 1.0F);
				playSound(SoundEvents.ENTITY_CHICKEN_EGG, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				dropItem(randomSpawnItem(), 1);
				this.timeUntilNextEgg = this.isHero() ? (100 + this.rand.nextInt(100)) : (200 + this.rand.nextInt(600));
			}
		}
		private Item randomSpawnItem()
		{
			switch (this.rand.nextInt(42))
			{
				default:
				return ItemRegistry.chickenItem;
				case 1:
				return ItemRegistry.batItem;
				case 2:
				return ItemRegistry.cowItem;
				case 3:
				return ItemRegistry.mooshroomItem;
				case 4:
				return ItemRegistry.pigItem;
				case 5:
				return ItemRegistry.rabbitItem;
				case 6:
				return ItemRegistry.sheepItem;
				case 7:
				return ItemRegistry.ozelotItem;
				case 8:
				return ItemRegistry.squidItem;
				case 9:
				return ItemRegistry.llamaItem;
				case 10:
				return ItemRegistry.villagerItem;
				case 11:
				return ItemRegistry.snowmanItem;
				case 12:
				return ItemRegistry.silverfishItem;
				case 13:
				return ItemRegistry.endermiteItem;
				case 14:
				return ItemRegistry.wolfItem;
				case 15:
				return ItemRegistry.spiderItem;
				case 16:
				return ItemRegistry.zombieItem;
				case 17:
				return ItemRegistry.skeletonItem;
				case 18:
				return ItemRegistry.polarBearItem;
				case 19:
				return ItemRegistry.slimeItem;
				case 20:
				return ItemRegistry.magmacubeItem;
				case 21:
				return ItemRegistry.vexItem;
				case 22:
				return ItemRegistry.spiderjockeyItem;
				case 23:
				return ItemRegistry.chickenjockeyItem;
				case 24:
				return ItemRegistry.blazeItem;
				case 25:
				return ItemRegistry.endermanItem;
				case 26:
				return ItemRegistry.cavespiderItem;
				case 27:
				return ItemRegistry.pigzombieItem;
				case 28:
				return ItemRegistry.guardianItem;
				case 29:
				return ItemRegistry.ghastItem;
				case 30:
				return ItemRegistry.huskItem;
				case 31:
				return ItemRegistry.shulkerItem;
				case 32:
				return ItemRegistry.strayItem;
				case 33:
				return ItemRegistry.witchItem;
				case 34:
				return ItemRegistry.vindicatorItem;
				case 35:
				return ItemRegistry.witherskeletonItem;
				case 36:
				return ItemRegistry.killerrabbitItem;
				case 37:
				return ItemRegistry.elderguardianItem;
				case 38:
				return ItemRegistry.the4horsemenItem;
				case 39:
				return ItemRegistry.evokerItem;
				case 40:
				return ItemRegistry.giantItem;
				case 41:
				return ItemRegistry.villagergolemItem;
			}
		}
		public boolean takesFallDamage()
		{
			return false;
		}
		protected SoundEvent getAmbientSound()
		{
			return SoundEvents.ENTITY_CHICKEN_AMBIENT;
		}
		protected SoundEvent getHurtSound(DamageSource source)
		{
			return SoundEvents.ENTITY_CHICKEN_HURT;
		}
		protected SoundEvent getDeathSound()
		{
			return SoundEvents.ENTITY_CHICKEN_DEATH;
		}
		protected void playStepSound(BlockPos pos, Block blockIn)
		{
			playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F / this.getFittness());
		}
		@Nullable
		protected ResourceLocation getLootTable()
		{
			return LootRegistry.ENTITIES_CHICKEN;
		}
		public void readEntityFromNBT(NBTTagCompound tagCompund)
		{
			super.readEntityFromNBT(tagCompund);
			this.chickenJockey = tagCompund.getBoolean("IsChickenJockey");
			if (tagCompund.hasKey("EggLayTime"))
			{
				this.timeUntilNextEgg = tagCompund.getInteger("EggLayTime");
			}
		}
		protected int getExperiencePoints(EntityPlayer player)
		{
			return isChickenJockey() ? 10 : super.getExperiencePoints(player);
		}
		public void writeEntityToNBT(NBTTagCompound tagCompound)
		{
			super.writeEntityToNBT(tagCompound);
			tagCompound.setBoolean("IsChickenJockey", this.chickenJockey);
			tagCompound.setInteger("EggLayTime", this.timeUntilNextEgg);
		}
		public double getMountedYOffset()
		{
			return (double)this.height * 0.65D;
		}

		public void updatePassenger(Entity passenger)
		{
			super.updatePassenger(passenger);
			float f = MathHelper.sin(this.renderYawOffset * 0.017453292F);
			float f1 = MathHelper.cos(this.renderYawOffset * 0.017453292F);
			float f2 = 0.1F;
			float f3 = 0.0F;
			passenger.setPosition(this.posX + f2 * f, this.posY + this.getMountedYOffset() + passenger.getYOffset() + f3, this.posZ - f2 * f1);
			if ((passenger instanceof EntityLivingBase))
			{
				((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset;
			}
		}
		public boolean interact(EntityPlayer player, EnumHand hand)
		{
			ItemStack stack = player.getHeldItem(hand);
			
			if (stack.isEmpty() && getRidingEntity() == null)
			{
				if (!isWild() && this.isOnSameTeam(player) && !this.isChild() && !this.world.isRemote)
				player.startRiding(this);
				return true;
			}
			else
			{
				return false;
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
					if (this.jumpPower > 0.0F && this.onGround)
					{
						this.motionY = (double)this.jumpPower * this.getFittness();
						
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
			public boolean isChickenJockey()
			{
				return this.chickenJockey;
			}
			public void setChickenJockey(boolean jockey)
			{
				this.chickenJockey = jockey;
			}
		}

		
		