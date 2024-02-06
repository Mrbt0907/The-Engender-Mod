package net.minecraft.AgeOfMinecraft.entity.tier3;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.ForgeHooks;

public class EntitySlime
extends EntityFriendlyCreature implements IJumpingMount
{
	private static final DataParameter<Integer> SLIME_SIZE = EntityDataManager.createKey(EntitySlime.class, DataSerializers.VARINT);
	public float squishAmount;
	public float squishFactor;
	public float prevSquishFactor;
	private boolean wasOnGround;
	public EntitySlime(World worldIn)
	{
		super(worldIn);
		this.isOffensive = true;
			this.moveHelper = new SlimeMoveHelper(this);
			this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.1D, 32.0F, 6.0F));
			this.tasks.addTask(2, new EntitySlime.AISlimeFloat(this));
			this.tasks.addTask(3, new EntitySlime.AISlimeAttack(this));
			this.tasks.addTask(4, new EntitySlime.AISlimeFaceRandom(this));
			this.tasks.addTask(5, new EntitySlime.AISlimeHop(this));
		
	}
	
	public boolean leavesNoCorpse()
	{
		return true;
	}
	
	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 1.25F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 2F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.5F;
	}
	public boolean isChild()
	{
		return this.getSlimeSize() == 0;
	}

	public void setChild(boolean childZombie) { }
	

	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SLIME_SIZE, Integer.valueOf(1));
	}
	public int timesToConvert()
	{
		return 4 * getSlimeSize();
	}
	public void setSlimeSize(int size)
	{
		this.dataManager.set(SLIME_SIZE, Integer.valueOf(size));
		
		this.setSize(0.5F * size, 0.5F * size);
		setPosition(this.posX, this.posY, this.posZ);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(size * size);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35F + 0.15F * size);
		setHealth(getMaxHealth());
		this.experienceValue = size;
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER3;
	}
	public int getSlimeSize()
	{
		return ((Integer)this.dataManager.get(SLIME_SIZE)).intValue();
	}
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("Size", getSlimeSize() - 1);
		tagCompound.setBoolean("wasOnGround", this.wasOnGround);
	}
	public boolean func_189101_db()
	{
		return getSlimeSize() <= 1;
	}
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		int i = tagCompund.getInteger("Size");
		if (i < 0)
		{
			i = 0;
		}
		setSlimeSize(i + 1);
		this.wasOnGround = tagCompund.getBoolean("wasOnGround");
	}
	protected EnumParticleTypes getParticleType()
	{
		return EnumParticleTypes.SLIME;
	}
	protected SoundEvent getJumpSound()
	{
		return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_JUMP : SoundEvents.ENTITY_SLIME_JUMP;
	}
	public void onUpdate()
	{
		ItemStack size = new ItemStack(this instanceof EntityMagmaCube? Items.MAGMA_CREAM : Items.SLIME_BALL, this.getSlimeSize());
		size.setStackDisplayName("Size: " + this.getSlimeSize());
		basicInventory.setInventorySlotContents(7, size);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(getAttackStrength());
		this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
		this.prevSquishFactor = this.squishFactor;
		super.onUpdate();
		if ((this.onGround) && (!this.wasOnGround))
		{
			int i = getSlimeSize();
			if (spawnCustomParticles()) i = 0;
			for (int j = 0; j < i * 8; j++)
			{
				float f = this.rand.nextFloat() * 6.2831855F;
				float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
				float f2 = MathHelper.sin(f) * this.width * f1;
				float f3 = MathHelper.cos(f) * this.width * f1;
				World world = this.world;
				EnumParticleTypes enumparticletypes = getParticleType();
				double d0 = this.posX + f2;
				double d1 = this.posZ + f3;
				world.spawnParticle(enumparticletypes, d0, getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D, new int[0]);
			}
			playSound(func_184709_cY(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / (0.8F));
			this.squishAmount = -0.5F;
		}
		else if ((!this.onGround) && (this.wasOnGround))
		{
			this.squishAmount = 1.0F;
		}
		this.wasOnGround = this.onGround;
		alterSquishAmount();
	}
	protected void alterSquishAmount()
	{
		this.squishAmount *= 0.6F;
	}
	protected int getJumpDelay()
	{
		return this.moralRaisedTimer > 200 ? this.rand.nextInt(10) + 5 : this.rand.nextInt(20) + 10;
	}
	protected EntitySlime createInstance()
	{
		return new EntitySlime(this.world);
	}
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		if (SLIME_SIZE.equals(key))
		{
			int size = getSlimeSize();
		
			this.setSize(0.5F * size, 0.5F * size);
			this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
		}
		super.notifyDataManagerChange(key);
	}
	public void setDead()
	{
		int i = getSlimeSize();
		if ((!this.world.isRemote) && (i > 1) && (getHealth() <= 0.0F))
		{
			int j = 2 + this.rand.nextInt(3);
			for (int k = 0; k < j; k++)
			{
				float f = (k % 2 - 0.5F) * i / 4.0F;
				float f1 = (k / 2 - 0.5F) * i / 4.0F;
				EntitySlime entityslime = createInstance();
				entityslime.writeEntityToNBT(getEntityData());
				entityslime.setOwnerId(this.getOwnerId());
				entityslime.setSlimeSize(i / 2);
				entityslime.setLocationAndAngles(this.posX + f, this.posY, this.posZ + f1, this.rand.nextFloat() * 360.0F, 0.0F);
				this.world.spawnEntity(entityslime);
			}
		}
		super.setDead();
	}
	public void performSpecialAttack()
	{
		this.jump();
		for (int k = 0; k < getSlimeSize(); k++)
		{
			this.motionY += 0.25D;
		}
		playSound(func_184710_cZ(), getSoundVolume(), ((getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
	}
	public boolean takesFallDamage()
	{
		return false;
	}
	public void fall(float distance, float damageMultiplier)
	{
		this.moveStrafing = this.moveForward = 0F;
		if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 64D * getSlimeSize()) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			setSpecialAttackTimer(100 * getSlimeSize());
			playSound(ESound.golemSmash, 10.0F, 2.0F - getSlimeSize() * 0.25F);
			createEngenderModExplosionFireless(this, this.posX, this.posY - 0.5D, this.posZ, getSlimeSize(), false);
			if ((getAttackTarget() != null) && (!isOnSameTeam(getAttackTarget())))
			{
				double d01 = getAttackTarget().posX - this.posX;
				double d11 = getAttackTarget().posZ - this.posZ;
				float f21 = MathHelper.sqrt(d01 * d01 + d11 * d11);
				double hor = f21 / 16.0F * 1.25D;
				this.motionX = (d01 / f21 * hor * hor + this.motionX * hor);
				this.motionZ = (d11 / f21 * hor * hor + this.motionZ * hor);
				this.motionY = 0.6000000238418579D;
				double dou = getDistanceSq(getAttackTarget());
				if (dou <= 16.0D)
				this.attackEntityAsMob(getAttackTarget());
			}
			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(16D * getSlimeSize()), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if ((list != null) && (!list.isEmpty()))
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityLivingBase entity = (EntityLivingBase)list.get(i1);
					if (entity != null)
					{
						if (!isOnSameTeam(entity))
						{
							entity.motionY += 0.75D;
							this.attackEntityAsMob(entity);
						}
					}
				}
			}
		}
	}
	private int field_179924_h;
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		this.collideWithNearbyEntities();
		if (this.isRiding() && this.getRidingEntity() instanceof EntityLivingBase)
		this.renderYawOffset = this.rotationYaw = this.rotationYawHead = ((EntityLivingBase)this.getRidingEntity()).rotationYawHead;
		

		if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 32.0D * getSlimeSize()) && (getSpecialAttackTimer() <= 0) && (this.onGround) && (isHero()))
		{
			performSpecialAttack();
		}
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
	public void applyEntityCollision(Entity entityIn)
	{
		super.applyEntityCollision(entityIn);
		if (entityIn instanceof EntityLivingBase)
		{
			EntityLivingBase entity = (EntityLivingBase)entityIn;
			if (this.ticksExisted > 10 && canEntityBeSeen(entity) && (getDistanceSq(entity) < this.width * 1.5D * (this.width * 1.5D)) && attackEntityAsMob(entity))
			{
				this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, this.getSoundVolume(), this.getSoundPitch());
			}
		}
	}
	public void travel(float strafe, float vertical, float forward)
	{
		if ((isBeingRidden()) && (canBeSteered()))
		{
			if ((this.isInWater()) || (this.isInLava()))
			{
				if (this.getRNG().nextFloat() < 0.8F)
				{
					this.motionY += (0.1D - this.motionY);
				}
			}

			EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
			this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
			this.rotationPitch = entitylivingbase.rotationPitch;
			setRotation(this.rotationYaw, this.rotationPitch);
			this.rotationYawHead = (this.renderYawOffset = this.rotationYaw);
			strafe = entitylivingbase.moveStrafing;
			forward = entitylivingbase.moveForward;
			this.jumpMovementFactor = 0.05F;
			if (canPassengerSteer())
			{
				setAIMoveSpeed(0.0F);
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
				this.jump();
				this.motionY += jumpPower * (this instanceof EntityMagmaCube ? 1D : 0.25D);
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
		else
		{
			this.jumpMovementFactor = 0.02F;
			super.travel(strafe, vertical, forward);
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
		playSound(func_184710_cZ(), getSoundVolume(), ((getRNG().nextFloat() - getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
	}

	public void handleStopJump()
	{
	}public boolean interact(EntityPlayer player, EnumHand hand)
		{
			ItemStack stack = player.getHeldItem(hand);
			
			if (!stack.isEmpty() && this.isOnSameTeam(player) && this.getSlimeSize() < (this.isHero() ? 16 : 4) && (stack.getItem() == (this instanceof EntityMagmaCube ? Items.MAGMA_CREAM : Items.SLIME_BALL)))
			{
				if (!player.capabilities.isCreativeMode)
				{
					stack.shrink(1);
				}
				playSound(func_184709_cY(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / (0.8F));
				this.setSlimeSize(this.getSlimeSize() + 1);
				playSound(func_184709_cY(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / (0.8F));
				int i = getSlimeSize();
				if (spawnCustomParticles()) i = 0;
				for (int j = 0; j < i * 8; j++)
				{
					float f = this.rand.nextFloat() * 6.2831855F;
					float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
					float f2 = MathHelper.sin(f) * i * 0.5F * f1;
					float f3 = MathHelper.cos(f) * i * 0.5F * f1;
					World world = this.world;
					EnumParticleTypes enumparticletypes = getParticleType();
					double d0 = this.posX + f2;
					double d1 = this.posZ + f3;
					world.spawnParticle(enumparticletypes, d0, getEntityBoundingBox().minY, d1, 0.0D, 0.0D, 0.0D, new int[0]);
				}
				return true;
			}
			else if (this.isSmallSlime())
			{
				
				return true;
			}
			else if (stack.isEmpty() && getRidingEntity() == null && !this.isSmallSlime())
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
		public float getEyeHeight()
		{
			return 0.625F * this.height;
		}
		protected boolean canDamagePlayer()
		{
			return true;
		}
		protected int getAttackStrength()
		{
			return this.moralRaisedTimer > 200 ? getSlimeSize() * 2 : getSlimeSize();
		}
		protected SoundEvent getHurtSound(DamageSource source)
		{
			return func_189101_db() ? SoundEvents.ENTITY_SMALL_SLIME_HURT : SoundEvents.ENTITY_SLIME_HURT;
		}
		protected SoundEvent getDeathSound()
		{
			return func_189101_db() ? SoundEvents.ENTITY_SMALL_SLIME_DEATH : SoundEvents.ENTITY_SLIME_DEATH;
		}
		protected SoundEvent func_184709_cY()
		{
			return func_189101_db() ? SoundEvents.ENTITY_SMALL_SLIME_HURT : SoundEvents.ENTITY_SLIME_SQUISH;
		}
		protected float getSoundPitch()
		{
			return super.getSoundPitch();
		}
		@Nullable
		protected ResourceLocation getLootTable()
		{
			return this.isSmallSlime() ? ELoot.ENTITIES_SLIME : LootTableList.EMPTY;
		}
		public boolean isSmallSlime()
		{
			return this.getSlimeSize() <= 1;
		}
		protected float getSoundVolume()
		{
			return (this.isSneaking() ? 0.025F : 0.25F) * getSlimeSize();
		}
		public int getVerticalFaceSpeed()
		{
			return 40;
		}
		protected boolean makesSoundOnJump()
		{
			return false;
		}
		protected boolean makesSoundOnLand()
		{
			return getSlimeSize() > 2;
		}
		protected void jump()
		{
			playSound(getJumpSound(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / (1.1F));
			this.motionY += 0.42D;
			this.isAirBorne = true;
			ForgeHooks.onLivingJump(this);
		}
		public double getMountedYOffset()
		{
			return this.height - 0.25D + (this.squishFactor * (this.getSlimeSize() * 0.25D) * (this instanceof EntityMagmaCube ? 3 : 1.5));
		}
		public double getYOffset()
		{
			return 0.5D;
		}
		@Nullable
		public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
		{
			 int i = this.rand.nextInt(3);
			if ((i < 2) && (this.rand.nextFloat() < 0.5F * difficulty.getClampedAdditionalDifficulty()))
			 i++;
			
			 int j = 1 << i;
			if (this.rand.nextInt(200) == 0)
			setSlimeSize(8);
			else
				setSlimeSize(j);
			return super.onInitialSpawn(difficulty, livingdata);
		}
		protected SoundEvent func_184710_cZ()
		{
			return func_189101_db() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
		}
		protected boolean spawnCustomParticles()
		{
			return false;
		}
		static class AISlimeAttack extends EntityAIBase
		{
			private final EntitySlime slime;
			private int growTieredTimer;
			
			public AISlimeAttack(EntitySlime slimeIn)
			{
				this.slime = slimeIn;
				this.setMutexBits(2);
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
				return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : !(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage);
			}

			/**
			* Execute a one shot task or start executing a continuous task
			*/
			public void startExecuting()
			{
				this.growTieredTimer = 300;
				super.startExecuting();
			}

			/**
			* Returns whether an in-progress EntityAIBase should continue executing
			*/
			public boolean shouldContinueExecuting()
			{
				EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
				return entitylivingbase == null ? false : (!entitylivingbase.isEntityAlive() ? false : (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage ? false : --this.growTieredTimer > 0));
			}

			/**
			* Updates the task
			*/
			public void updateTask()
			{
				this.slime.faceEntity(this.slime.getAttackTarget(), 10.0F, 10.0F);
				((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.slime.rotationYaw, this.slime.canDamagePlayer());
				if (this.slime.ticksExisted % 10 == 0 && this.slime.canEntityBeSeen(this.slime.getAttackTarget()) && this.slime.getDistanceSq(this.slime.getAttackTarget()) < (this.slime.width * 1.5D) * (this.slime.width * 1.5D) && this.slime.getAttackTarget() != null)
				this.slime.attackEntityAsMob(this.slime.getAttackTarget());
			}
		}

		static class AISlimeFaceRandom extends EntityAIBase
		{
			private final EntitySlime slime;
			private float chosenDegrees;
			private int nextRandomizeTime;
			
			public AISlimeFaceRandom(EntitySlime slimeIn)
			{
				this.slime = slimeIn;
				this.setMutexBits(2);
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				return this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.isPotionActive(MobEffects.LEVITATION));
			}

			/**
			* Updates the task
			*/
			public void updateTask()
			{
				if (--this.nextRandomizeTime <= 0)
				{
					this.nextRandomizeTime = 40 + this.slime.getRNG().nextInt(60);
					this.chosenDegrees = (float)this.slime.getRNG().nextInt(360);
				}
				((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.chosenDegrees, false);
			}
		}

		static class AISlimeFloat extends EntityAIBase
		{
			private final EntitySlime slime;
			
			public AISlimeFloat(EntitySlime slimeIn)
			{
				this.slime = slimeIn;
				this.setMutexBits(5);
				((PathNavigateGround)slimeIn.getNavigator()).setCanSwim(true);
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				return this.slime.isInWater() || this.slime.isInLava();
			}

			/**
			* Updates the task
			*/
			public void updateTask()
			{
				if (this.slime.getRNG().nextFloat() < 0.8F)
				{
					this.slime.getJumpHelper().setJumping();
				}

				((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2D);
			}
		}

		static class AISlimeHop extends EntityAIBase
		{
			private final EntitySlime slime;
			
			public AISlimeHop(EntitySlime slimeIn)
			{
				this.slime = slimeIn;
				this.setMutexBits(5);
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				return true;
			}

			/**
			* Updates the task
			*/
			public void updateTask()
			{
				((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0D);
			}
		}

		public static class SlimeMoveHelper extends EntityMoveHelper
		{
			private float yRot;
			private int jumpDelay;
			private final EntitySlime slime;
			private boolean isAggressive;
			
			public SlimeMoveHelper(EntitySlime slimeIn)
			{
				super(slimeIn);
				this.slime = slimeIn;
				this.yRot = 180.0F * slimeIn.rotationYaw / (float)Math.PI;
			}

			public void setDirection(float p_179920_1_, boolean p_179920_2_)
			{
				this.yRot = p_179920_1_;
				this.isAggressive = p_179920_2_;
			}

			public void setSpeed(double speedIn)
			{
				this.speed = speedIn;
				this.action = EntityMoveHelper.Action.MOVE_TO;
			}

			public void onUpdateMoveHelper()
			{
				this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.yRot, 90.0F);
				this.entity.rotationYawHead = this.entity.rotationYaw;
				this.entity.renderYawOffset = this.entity.rotationYaw;
				
				if (this.action != EntityMoveHelper.Action.MOVE_TO || this.slime.getJukeboxToDanceTo() != null || !((EntityFriendlyCreature)this.entity).getCurrentBook().isEmpty())
				{
					this.entity.setMoveForward(0.0F);
				}
				else
				{
					this.action = EntityMoveHelper.Action.WAIT;
					
					if (this.entity.onGround && !this.entity.isBeingRidden() && !this.entity.isRiding())
					{
						this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
						
						if (this.jumpDelay-- <= 0)
						{
							this.jumpDelay = this.slime.getJumpDelay();
							
							if (this.isAggressive)
							{
								this.jumpDelay /= 3;
							}
							if (this.entity.isBurning())
							{
								this.jumpDelay /= 3;
							}

							this.slime.getJumpHelper().setJumping();
						}
						else
						{
							this.slime.moveStrafing = 0.0F;
							this.slime.moveForward = 0.0F;
							this.entity.setAIMoveSpeed(0.0F);
						}
					}
					else
					{
						this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
					}
				}
			}
		}

	}

	
	