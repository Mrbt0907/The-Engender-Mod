package net.minecraft.AgeOfMinecraft.entity.tier1;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.Animal;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.Tiny;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityChicken extends EntityFriendlyCreature implements IJumpingMount, Light, Tiny, Animal
{
	public float wingRotation;
	public float destPos;
	public float field_70884_g;
	public float field_70888_h;
	public float wingRotDelta = 1.0F;
	public int timeUntilNextEgg;
	public boolean chickenJockey;
	public EntityChicken(World worldIn)
	{
		super(worldIn);
		this.timeUntilNextEgg = this.rand.nextInt(600) + 600;
		setPathPriority(PathNodeType.WATER, 0.0F);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.4D, 24F, 8F));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D, 80));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.experienceValue = 1;
	}
	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 0.5F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.1F;
	}
	public boolean canBeButchered()
	{
		return true;
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityChicken(this.world);
	}
	public float getEyeHeight()
	{
		return this.height * 0.95F;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
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
		if (this.isBeingRidden() && this.rand.nextInt(100) == 0 && this.getControllingPassenger().height > this.getFittness() + 0.5F)
		this.attackEntityFrom(DamageSource.IN_WALL, 1F);
		
		if (isHero() && !this.world.isRemote && this.getAttackTarget() != null)
		{
			this.getAttackTarget().hurtResistantTime = 0;
			this.getLookHelper().setLookPositionWithEntity(getAttackTarget(), 180F, 40F);
			EntityEgg entitysnowball = new EntityEgg(this.world, this);
			double d0 = this.getAttackTarget().posY + this.getAttackTarget().getEyeHeight() - 1.100000023841858D;
			double d1 = this.getAttackTarget().posX - this.posX;
			double d2 = d0 - entitysnowball.posY;
			double d3 = this.getAttackTarget().posZ - this.posZ;
			float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
			entitysnowball.shoot(d1, d2 + f, d3, 1.6F, 4.0F);
			playSound(SoundEvents.ENTITY_EGG_THROW, 1F, 1.5F / (getRNG().nextFloat() * 0.4F + 0.8F));
			this.world.spawnEntity(entitysnowball);
		}
		if (isChickenJockey())
		this.isOffensive = true;
		else
		this.isOffensive = false;
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
			playSound(SoundEvents.ENTITY_CHICKEN_EGG, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			dropItem(Items.EGG, 1);
			this.timeUntilNextEgg = this.rand.nextInt(600) + 600;
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
		return ELoot.ENTITIES_CHICKEN;
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
			if (player.isSneaking())
			{
				List<EntityZombie> list = this.world.getEntitiesWithinAABB(EntityZombie.class, getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
				if (getRidingEntity() == null && (list != null) && (!list.isEmpty()) && !this.isBeingRidden() && this.hasOwner(player))
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityZombie entity = (EntityZombie)list.get(i1);
						if (entity != null)
						{
							if (entity.isChild() && isOnSameTeam(entity) && !entity.isRiding() && !this.world.isRemote)
							{
								player.swingArm(EnumHand.MAIN_HAND);
								entity.startRiding(this);
								playSound(SoundEvents.ENTITY_CHICKEN_AMBIENT, 1.0F, 1.5F);
								break;
							}
						}
					}
				}
				return true;
			}else
				{
					if (!isWild() && this.isOnSameTeam(player) && !this.isChild() && !this.world.isRemote)
					player.startRiding(this);
					return true;
				}
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
					this.rotationYawHead = entitylivingbase.rotationYawHead;
					this.rotationPitch = entitylivingbase.rotationPitch;
					setRotation(this.rotationYaw, this.rotationPitch);
					strafe = entitylivingbase.moveStrafing;
					forward = entitylivingbase.moveForward;
					
					if (forward != 0F)
					{
						this.rotationYaw = this.renderYawOffset = this.rotationYawHead;
						this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
					}
					if (this.jumpPower > 0.0F && this.onGround)
					{
						this.motionY = (0.6D * (double)this.jumpPower) * this.getFittness();
						
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
			
			@Override
			public EnumTier getTier()
			{
				return EnumTier.TIER1;
			}
		}