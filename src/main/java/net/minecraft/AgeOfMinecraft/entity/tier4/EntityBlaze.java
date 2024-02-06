package net.minecraft.AgeOfMinecraft.entity.tier4;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.Elemental;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBlaze
extends EntityFriendlyCreature implements IJumpingMount, Light, Flying, Elemental
{
	private float heightOffset = 0.5F;
	private int heightOffsetUpdateTime;
	private static final DataParameter<Byte> ON_FIRE = EntityDataManager.createKey(EntityBlaze.class, DataSerializers.BYTE);
	public EntityBlaze(World worldIn)
	{
		super(worldIn);
		setPathPriority(PathNodeType.WATER, -1.0F);
		this.isOffensive = true;
		this.isImmuneToFire = true;
		this.experienceValue = 10;
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.0D, 48.0F, 8.0F));
		this.tasks.addTask(4, new AIFireballAttack(this));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.moveHelper = new EntityFlyHelper(this);
	}

	/**
	* Returns new PathNavigateGround instance
	*/
	protected PathNavigate createNavigator(World worldIn)
	{
		PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
		pathnavigateflying.setCanOpenDoors(true);
		pathnavigateflying.setCanFloat(true);
		pathnavigateflying.setCanEnterDoors(true);
		return pathnavigateflying;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.75D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityBlaze(this.world);
	}
	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 0.8F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 1.25F;
	}
	/**
	* Bonus damage vs mobs that implement Flying
	*/
	public float getBonusVSFlying()
	{
		return 2F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.75F;
	}

	public EnumTier getTier()
	{
		return EnumTier.TIER4;
	}
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(ON_FIRE, Byte.valueOf((byte)0));
	}
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_BLAZE_AMBIENT;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_BLAZE_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_BLAZE_DEATH;
	}
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15728880;
	}
	public float getBrightness()
	{
		return 1.0F;
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

	public void performSpecialAttack()
	{
		setSpecialAttackTimer(800);
	}
	public void onLivingUpdate()
	{
		this.setSize(0.6F, 1.8F);
		
		if (isWet())
		{
			playSound(SoundEvents.ENTITY_GENERIC_BURN, 1F, 1F);
			attackEntityFrom((new DamageSource("cooler")).setDamageBypassesArmor().setDamageIsAbsolute().setDifficultyScaled(), 4F);
			this.getMoveHelper().setMoveTo(posX, posY + 3, posZ, 1D);
		}
		if ((isHero()) && (getSpecialAttackTimer() > 790))
		{
			this.motionX = 0.0D;
			this.motionZ = 0.0D;
			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if ((list != null) && (!list.isEmpty()))
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityLivingBase entity = (EntityLivingBase)list.get(i1);
					if (entity != null)
					{
						if (!isOnSameTeam(entity))
						{
							entity.setFire(60);
							entity.hurtResistantTime = 0;
							this.inflictEngenderMobDamage(entity, " was scorched to death by ", (new DamageSource("burn")).setDamageBypassesArmor().setFireDamage(), 1F);
						}
					}
				}
			}
		}
		if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 256.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			performSpecialAttack();
		}
		if ((!this.onGround) && (this.motionY < 0.0D) && this.isEntityAlive())
		{
			this.motionY *= 0.6D;
		}
		if (this.world.isRemote)
		{
			if ((isHero()) && (getSpecialAttackTimer() > 790))
			{
				for (int i = 0; i < 3000; i++)
				{
					this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 1.0D, this.posZ, this.rand.nextDouble() - 0.5D, this.rand.nextDouble() - 0.5D, this.rand.nextDouble() - 0.5D, new int[0]);
					this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY + 1.0D, this.posZ, this.rand.nextDouble() - 0.5D, this.rand.nextDouble() - 0.5D, this.rand.nextDouble() - 0.5D, new int[0]);
				}
			}
			if (!isWet() && this.isEntityAlive() && this.getIllusionFormTime() <= 0)
			{
				if ((this.rand.nextInt(24) == 0) && (!isSilent()))
				{
					this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, getSoundCategory(), this.getSoundVolume(), this.rand.nextFloat() * 0.7F + 0.3F, false);
				}

				for (int i = 0; i < 2; i++)
				{
					if (isSneaking() || this.isChild())
						this.world.spawnParticle(this.isAntiMob() ? EnumParticleTypes.EXPLOSION_NORMAL : EnumParticleTypes.SMOKE_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
					else
						this.world.spawnParticle(this.isAntiMob() ? EnumParticleTypes.EXPLOSION_NORMAL : EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

		}
		super.onLivingUpdate();
	}
	protected void updateAITasks()
	{
		--this.heightOffsetUpdateTime;
		
		if (this.heightOffsetUpdateTime <= 0)
		{
			this.heightOffsetUpdateTime = 100;
			this.heightOffset = (this.rand.nextFloat() * 6.0F);
		}

		EntityLivingBase entitylivingbase = this.getAttackTarget();
		
		if (entitylivingbase != null && this.posY + this.getEyeHeight() - this.heightOffset < entitylivingbase.posY + entitylivingbase.getEyeHeight())
		{
			this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
			this.isAirBorne = true;
		}
		super.updateAITasks();
	}
	public boolean takesFallDamage()
	{
		return false;
	}
	protected boolean canTriggerWalking()
	{
		return false;
	}
	public boolean isBurning()
	{
		return func_70845_n();
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_BLAZE;
	}
	public boolean func_70845_n()
	{
		return (((Byte)this.dataManager.get(ON_FIRE)).byteValue() & 0x1) != 0;
	}

	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		ItemStack heldItem = new ItemStack(stack.getItem());
		
		if (!stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) && (stack.getItem() != Items.SPAWN_EGG) && ((getSlotForItemStack(stack) == EntityEquipmentSlot.MAINHAND) || (stack.getItem() instanceof ItemSword) || (stack.getItem() instanceof ItemTool) || (stack.getItem() == Items.BOW)))
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
		else if (!stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty()) && ((getSlotForItemStack(stack) == EntityEquipmentSlot.OFFHAND) || (stack.getItem() instanceof ItemSword) || (stack.getItem() instanceof ItemTool) || (stack.getItem() instanceof ItemFood && !(stack.getItem() instanceof ItemAppleGold)) || (stack.getItem() == Items.TIPPED_ARROW) || (stack.getItem() == Items.SHIELD)))
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
		else if (!stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) && getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD)
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
		else if (!stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()) && getSlotForItemStack(stack) == EntityEquipmentSlot.CHEST)
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
		else if (!stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()) && getSlotForItemStack(stack) == EntityEquipmentSlot.LEGS)
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
		else if (this.isOnSameTeam(player) && stack.isEmpty() && player.isSneaking())
		{
			this.dropEquipmentUndamaged();
			player.swingArm(hand);
			return true;
		}
		else if (stack.isEmpty() && getRidingEntity() == null)
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
			if ((isBeingRidden()) && (canBeSteered()))
			{
				EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
				this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
				this.rotationPitch = entitylivingbase.rotationPitch - 5F;
				setRotation(this.rotationYaw, this.rotationPitch);
				this.rotationYawHead = (this.renderYawOffset = this.rotationYaw);
				strafe = entitylivingbase.moveStrafing;
				forward = entitylivingbase.moveForward;
				this.jumpMovementFactor = 0.03F;
				if (canPassengerSteer())
				{
					setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
					super.travel(strafe, vertical, forward);
				}
				this.prevLimbSwingAmount = this.limbSwingAmount;
				
				if ((((EntityLivingBase)getControllingPassenger()).moveStrafing != 0.0F) && (this.ticksExisted % 40 == 0) && (!this.world.isRemote))
				{
					this.world.playEvent((EntityPlayer)null, 1018, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
					Vec3d vec3 = this.getLook(1.0F);
					EntitySmallFireballOther entitysmallfireball = new EntitySmallFireballOther(this.world, this, (this.posX + vec3.x * 16D) - (this.posX + vec3.x), (this.posY + this.getEyeHeight() + vec3.y * 16D) - (this.posY + this.getEyeHeight() + vec3.y), (this.posZ + vec3.z * 16D) - (this.posZ + vec3.z));
					entitysmallfireball.posY = (this.posY + this.height / 2.0F + 0.5D);
					this.world.spawnEntity(entitysmallfireball);
					swingArm(EnumHand.MAIN_HAND);
					float dm = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
					entitysmallfireball.damage = (dm * 0.6F);
				}
				for (int i = 0; i < 64; i++)
				{
					int in = MathHelper.floor(this.posX);
					int j = MathHelper.floor(this.posY - this.rand.nextDouble() * (4.0D + this.heightOffset));
					int k = MathHelper.floor(this.posZ);
					BlockPos blockpos = new BlockPos(in, j, k);
					IBlockState iblockstate = this.world.getBlockState(blockpos);
					Block block = iblockstate.getBlock();
					if ((block != Blocks.AIR))
					{
						setPosition(this.posX, this.posY + 0.005D, this.posZ);
					}
				}
				if (this.jumpPower > 0.0F)
				{
					this.motionY += (double)this.jumpPower;
					
					if (this.isPotionActive(MobEffects.JUMP_BOOST))
					{
						this.motionY += (double)((float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
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
				this.jumpMovementFactor = 0.02F;
				super.travel(strafe, vertical, forward);
			}
		}
		public void setOnFire(boolean onFire)
		{
			byte b0 = ((Byte)this.dataManager.get(ON_FIRE)).byteValue();
			if (onFire && !this.isWet())
			{
				b0 = (byte)(b0 | 0x1);
			}
			else
			{
				b0 = (byte)(b0 & 0xFFFFFFFE);
			}
			this.dataManager.set(ON_FIRE, Byte.valueOf(b0));
		}
		public boolean attackEntityFrom(DamageSource source, float amount)
		{
			if (amount == 0 && source.getDamageType() == "thrown" && source instanceof EntityDamageSourceIndirect && ((EntityDamageSourceIndirect)source).getTrueSource() instanceof EntitySnowball)
			amount = 3F;
			
			return super.attackEntityFrom(source, amount);
		}
		protected boolean isValidLightLevel()
		{
			return true;
		}
		protected SoundEvent getRegularHurtSound()
		{
			return ESound.metalHit;
		}
		protected SoundEvent getPierceHurtSound()
		{
			return ESound.metalHitPierce;
		}
		protected SoundEvent getCrushHurtSound()
		{
			return ESound.metalHitCrush;
		}

		protected float getSoundPitch()
		{
			return super.getSoundPitch();
		}
		static class AIFireballAttack extends EntityAIBase
		{
			private EntityBlaze blaze;
			private int attackStep;
			private int attackTime;
			public AIFireballAttack(EntityBlaze blazeIn)
			{
				this.blaze = blazeIn;
				this.setMutexBits(3);
			}
			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
				return entitylivingbase != null && entitylivingbase.isEntityAlive();
			}

			/**
			* Execute a one shot task or start executing a continuous task
			*/
			public void startExecuting()
			{
				this.attackStep = 0;
			}

			/**
			* Reset the task's internal state. Called when this task is interrupted by another one
			*/
			public void resetTask()
			{
				this.blaze.setOnFire(false);
			}
			/**
			* Keep ticking a continuous task that has already been started
			*/
			public void updateTask()
			{
				--this.attackTime;
				EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
				this.blaze.getLookHelper().setLookPositionWithEntity(entitylivingbase, this.blaze.getHorizontalFaceSpeed(), this.blaze.getVerticalFaceSpeed());
				double d0 = this.blaze.getDistance(entitylivingbase);
				
				if (d0 < 3D)
				{
					if (this.attackTime <= 0)
					{
						this.attackTime = 20;
						this.blaze.attackEntityAsMob(entitylivingbase);
					}

					this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
				}
				else if (d0 < this.getFollowDistance())
				{
					double d1 = entitylivingbase.posX - this.blaze.posX;
					double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (this.blaze.posY + (double)(this.blaze.height / 2.0F));
					double d3 = entitylivingbase.posZ - this.blaze.posZ;
					
					if (this.attackTime <= 0)
					{
						++this.attackStep;
						
						if (this.attackStep == 1)
						{
							this.attackTime = this.blaze.moralRaisedTimer > 0 ? 30 : 60;
							this.blaze.setOnFire(true);
						}
						else if (this.attackStep <= 4)
						{
							this.attackTime = this.blaze.moralRaisedTimer > 0 ? 3 : 6;
						}
						else
						{
							this.attackTime = this.blaze.moralRaisedTimer > 0 ? 50 : 100;
							this.attackStep = 0;
							this.blaze.setOnFire(false);
						}

						if (this.attackStep > 1)
						{
							float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
							this.blaze.world.playEvent((EntityPlayer)null, 1018, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
							
							for (int i = 0; i < 1; ++i)
							{
								EntitySmallFireballOther entitysmallfireball = new EntitySmallFireballOther(this.blaze.world, this.blaze, d1 + (this.blaze.isHero() ? 0 : this.blaze.getRNG().nextGaussian() * (double)f), d2, d3 + (this.blaze.isHero() ? 0 : this.blaze.getRNG().nextGaussian() * (double)f));
								entitysmallfireball.posY = this.blaze.posY + (double)(this.blaze.height / 2.0F) + 0.5D;
								this.blaze.world.spawnEntity(entitysmallfireball);
							}
						}
					}
				}
				else
				{
					this.blaze.getNavigator().clearPath();
					this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
				}

				super.updateTask();
			}

			private double getFollowDistance()
			{
				IAttributeInstance iattributeinstance = this.blaze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
				return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
			}
		}
	}