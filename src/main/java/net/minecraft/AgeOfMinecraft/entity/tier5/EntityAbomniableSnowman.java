package net.minecraft.AgeOfMinecraft.entity.tier5;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.Elemental;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityAbomniableSnowman extends EntityFriendlyCreature implements IRangedAttackMob, Armored, Massive, Elemental
{
	 private AISpecialRangedAttack aiArrowAttack = new AISpecialRangedAttack(this, 1D, 40, 32F);
	 private EntityAIFriendlyAttackMelee aiAttackOnCollide = new EntityAIFriendlyAttackMelee(this, 1D, true);
	
	public EntityAbomniableSnowman(World worldIn)
	{
		super(worldIn);
		setSize(2.5F, 5.36F);
		this.experienceValue = 50;
		this.isOffensive = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.0D, 48.0F, 8.0F));
		this.tasks.addTask(3, new EntityAIFriendlyAttackMelee(this, 1.0D, true));
		this.tasks.addTask(5, new EntityAIWander(this, 0.67D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	public void updateBossBar()
	{
		super.updateBossBar();
		this.bossInfo.setColor(BossInfo.Color.WHITE);
	}
	public boolean isBoss()
	{
		return true;
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityAbomniableSnowman(this.world);
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER5;
	}
	/**
	* Get this Entity's EnumCreatureAttribute
	*/
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return ESetup.CONSTRUCT;
	}
	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 3F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 1.5F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.75F;
	}

	public int getMaxSpawnedInChunk()
	{
		return 1;
	}
	protected void entityInit()
	{
		super.entityInit();
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(25.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(15.0D);
	}
	public double getKnockbackResistance()
	{
		return 1D;
	}
	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		player.getHeldItem(hand);
		
		if (!isWild() && this.isOnSameTeam(player) && !this.isChild() && !player.isSneaking() && !this.world.isRemote)
		{
			player.startRiding(this);
			return true;
		}
		else
		{
			return false;
		}
	}
	protected boolean canFitPassenger(Entity passenger)
	{
		return getPassengers().size() < 2;
	}
	public void updatePassenger(Entity passenger)
	{
		if (isPassenger(passenger))
		{
			int i = getPassengers().indexOf(passenger);
			float f3 = this.renderYawOffset * 3.1415927F / 180.0F;
			float f11 = MathHelper.sin(f3);
			float f4 = MathHelper.cos(f3);
			float sho = limbSwing - limbSwingAmount * (1.0F - (this.ticksExisted * 0.001F)) + 6.0F;
			float sho1 = (Math.abs(sho % 13.0F - 13.0F * 0.5F) - 13.0F * 0.25F) / (13.0F * 0.25F);
			if (i == 1)
			passenger.setPosition(this.posX - f4 * (1.3F + (limbSwingAmount >= 0.01D ? (sho1 * 0.4D) : 0)), this.posY + 3.5D, this.posZ - f11 * (1.3F + (limbSwingAmount >= 0.01D ? (sho1 * 0.4D) : 0)));
			if (i == 0)passenger.setPosition(this.posX + f4 * (1.3F + (limbSwingAmount >= 0.01D ? (sho1 * 0.4D) : 0)), this.posY + 3.5D, this.posZ + f11 * (1.3F + (limbSwingAmount >= 0.01D ? (sho1 * 0.4D) : 0)));
			
}
		}
		public void travel(float strafe, float vertical, float forward)
		{
			if (isBeingRidden())
			{
				EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
				this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
				this.rotationPitch = entitylivingbase.rotationPitch;
				setRotation(this.rotationYaw, this.rotationPitch);
				this.rotationYawHead = (this.renderYawOffset = this.rotationYaw);
				strafe = entitylivingbase.moveStrafing / 3;
				forward = entitylivingbase.moveForward / 3;
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
				List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(1.0D));
				for (int i = 0; i < list.size(); i++)
				{
					Entity entity = (Entity)list.get(i);
					if (entity instanceof EntityLivingBase && !isOnSameTeam((EntityLivingBase)entity) && !this.world.isRemote && this.ticksExisted % 10 == 0)
					{
						attackEntityAsMob(entity);
					}
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
				super.travel(strafe, vertical, forward);
			}
		}
		public void onLivingUpdate()
		{
			super.onLivingUpdate();
			
			setSize(2.5F, 5.36F);
			
			if (this.getAttackTarget() != null)
			{
				if (this.getDistanceSq(this.getAttackTarget()) > 100D || this.getAttackTarget() instanceof EntityFlying || this.getAttackTarget().posY > this.posY + 4D)
				{
					this.tasks.addTask(2, this.aiArrowAttack);
					this.tasks.removeTask(this.aiAttackOnCollide);
				}
				else
				{
					this.tasks.addTask(2, this.aiAttackOnCollide);
					this.tasks.removeTask(this.aiArrowAttack);
				}
			}
			if ((this.motionX * this.motionX + this.motionZ * this.motionZ != 0.0D) && (this.rand.nextInt(5) == 0))
			{
				int i = MathHelper.floor(this.posX);
				int j = MathHelper.floor(this.posY - 0.20000000298023224D);
				int k = MathHelper.floor(this.posZ);
				IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));
				if (iblockstate.getMaterial() != Material.AIR)
				{
					this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, getEntityBoundingBox().minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, 4.0D * (this.rand.nextFloat() - 0.5D), 0.5D, (this.rand.nextFloat() - 0.5D) * 4.0D, new int[] { Block.getStateId(iblockstate) });
				}
			}
		}
		public boolean attackEntityAsMob(Entity entityIn)
		{
			this.attackTimer = 10;
			this.world.setEntityState(this, (byte)4);
			playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
			
			AttributeModifier irongolemrandomizer = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D839"), "Iron Golem randomizer", rand.nextDouble() * 2.5D, 1).setSaved(false);
			IAttributeInstance iattributeinstanceattack = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
			if (!iattributeinstanceattack.hasModifier(irongolemrandomizer))
			iattributeinstanceattack.applyModifier(irongolemrandomizer);
			
			if (super.attackEntityAsMob(entityIn))
			{
				if (iattributeinstanceattack.hasModifier(irongolemrandomizer))
				iattributeinstanceattack.removeModifier(irongolemrandomizer);
				return true;
			}
			else
			{
				return false;
			}
		}
		public void attackWithAdditionalEffects(Entity entity)
		{
			double amount = 1D;
			if (this.world.getDifficulty() == EnumDifficulty.EASY)
			amount *= 0.75D;
			if (this.world.getDifficulty() == EnumDifficulty.HARD)
			amount *= 1.5D;
			if (!entity.isEntityAlive() && entity instanceof EntityLivingBase)
			{
				((EntityLivingBase)entity).prevRenderYawOffset = ((EntityLivingBase)entity).renderYawOffset = ((EntityLivingBase)entity).prevRotationYaw = ((EntityLivingBase)entity).rotationYaw = ((EntityLivingBase)entity).prevRotationYawHead = ((EntityLivingBase)entity).rotationYawHead = this.rotationYawHead;
				float xRatio = MathHelper.sin(this.rotationYawHead * 0.017453292F);
				float zRatio = -MathHelper.cos(this.rotationYawHead * 0.017453292F);
				entity.isAirBorne = true;
				float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
				entity.motionX /= 3.0D;
				entity.motionZ /= 3.0D;
				entity.motionX -= xRatio / (double)f * 2F;
				entity.motionZ -= zRatio / (double)f * 2F;
				entity.motionY /= 2.0D;
				entity.motionY += amount;
				if (entity instanceof EntityPlayerMP)
				((EntityPlayerMP)entity).connection.sendPacket(new SPacketEntityVelocity((EntityPlayerMP)entity));
			}
			entity.motionY += amount;
		}
		@SideOnly(Side.CLIENT)
		public void handleStatusUpdate(byte id)
		{
			if (id == 4)
			{
				this.attackTimer = 10;
				playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
			}
			else
			{
				super.handleStatusUpdate(id);
			}
		}
		public boolean takesFallDamage()
		{
			return false;
		}
		public void fall(float distance, float damageMultiplier)
		{
			if ((getSpecialAttackTimer() <= 0) && (isHero()))
			{
				setSpecialAttackTimer(300);
				playSound(ESound.golemSmash, 10.0F, 1.0F);
				createEngenderModExplosionFireless(this, this.posX, this.posY - 2.0D, this.posZ, 3.0F, false);
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(24.0D, 3.0D, 24.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
				if ((list != null) && (!list.isEmpty()))
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityLivingBase entity = (EntityLivingBase)list.get(i1);
						if (entity != null)
						{
							if (!isOnSameTeam(entity))
							{
								entity.motionY += 1.5D;
								if ((entity instanceof IMob))
								{
									entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), 24.0F);
								} else {
										entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), 12.0F);
									}
									entity.isAirBorne = true;
									float f = MathHelper.sqrt(MathHelper.sin(this.rotationYaw * 0.017453292F) * MathHelper.sin(this.rotationYaw * 0.017453292F) + -MathHelper.cos(this.rotationYaw * 0.017453292F) * -MathHelper.cos(this.rotationYaw * 0.017453292F));
									entity.motionX /= 2.0D;
									entity.motionZ /= 2.0D;
									entity.motionX -= MathHelper.sin(this.rotationYaw * 0.017453292F) / f * 1.0D;
									entity.motionZ -= -MathHelper.cos(this.rotationYaw * 0.017453292F) / f * 1.0D;
								}
								if (EngenderConfig.general.useMessage && (!entity.isEntityAlive()) && (!isWild()))
								{
									getOwner().sendMessage(new TextComponentTranslation(entity.getName() + " was blown up by " + getName() + " (" + getOwner().getName() + ")", new Object[0]));
								}
							}
						}
					}
				}
			}
			protected SoundEvent getHurtSound(DamageSource source)
			{
				return SoundEvents.ENTITY_IRONGOLEM_HURT;
			}
			protected SoundEvent getDeathSound()
			{
				return SoundEvents.ENTITY_IRONGOLEM_DEATH;
			}
			protected void playStepSound(BlockPos pos, Block blockIn)
			{
				playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, (this.isChild() ? 1.5F : 1.0F) / this.getFittness());
			}
			@Nullable
			protected ResourceLocation getLootTable()
			{
				return ELoot.ENTITIES_IRON_GOLEM;
			}

			protected void onDeathUpdate()
			{
				this.hurtTime = 10;
				++this.deathTicks;
				
				if (this.deathTicks == 100)
				{
					if (!this.world.isRemote && (this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
					{
						int i = this.getExperiencePoints(this.attackingPlayer);
						i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
						while (i > 0)
						{
							int j = EntityXPOrb.getXPSplit(i);
							i -= j;
							this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 8.0D, this.posZ, j));
						}
					}

					for (int k = 0; k < 20; ++k)
					{
						double d2 = this.rand.nextGaussian() * 0.02D;
						double d0 = this.rand.nextGaussian() * 0.02D;
						double d1 = this.rand.nextGaussian() * 0.02D;
						this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
					}

					this.setDead();
				}

				this.playSound(SoundEvents.BLOCK_SNOW_BREAK, 1F, 1F);
				for (int k = 0; k < this.deathTicks; ++k)
				{
					double d2 = this.rand.nextGaussian() * 0.02D;
					double d0 = this.rand.nextGaussian() * 0.02D;
					double d1 = this.rand.nextGaussian() * 0.02D;
					this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (double)(this.rand.nextGaussian() * this.width * 0.5F), this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextGaussian() * this.width * 0.5F), d2, d0, d1, new int[] { Block.getStateId(Blocks.SNOW.getDefaultState()) });
				}
				if (!this.world.isRemote)
				{
					if (this.deathTicks == 1)
					{
						this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
						if (getOwner() != null)
						{
							for (EntityPlayer entityplayer : world.playerEntities)
							{
								world.playSound(null, entityplayer.getPosition(), getDeathSound(), this.getSoundCategory(), getSoundVolume(), 1.0F);
								entityplayer.sendStatusMessage(new TextComponentTranslation("\u00A74"+ this.getOwner().getName() + "'s " + this.getName() + " has been killed!!!"), true);
							}
							((EntityPlayerMP)getOwner()).sendMessage(new TextComponentTranslation("Your Wither has been destroyed!", new Object[0]));
						}
					}
				}
			}

			public void throwSnowball(double x, double y, double z)
			{
				EntitySnowballHarmful entitysnowball = new EntitySnowballHarmful(this.world, this);
				float f = MathHelper.sqrt(x * x + z * z) * 0.25F;
				entitysnowball.shoot(x, y + f, z, 1.6F, 1F);
				this.attackTimer = 10;
				this.world.setEntityState(this, (byte)4);
				this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
				this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
				this.world.spawnEntity(entitysnowball);
				entitysnowball.damage = 8F;
			}

			public void whirlSnowball(EntityLivingBase target)
			{
				EntitySnowballHarmful entitysnowball = new EntitySnowballHarmful(this.world, this);
				float f2 = this.renderYawOffset * 0.017453292F;
				float f19 = MathHelper.sin(f2);
				float f3 = MathHelper.cos(f2);
				double d1 = (target.posX + (rand.nextDouble() * 2D - 1D)) - (this.posX + f19 * 1.5F);
				double d2 = (target.posY - 2D - (rand.nextDouble() * 4D) + target.height) - (this.posY + this.getEyeHeight());
				double d3 = (target.posZ + (rand.nextDouble() * 2D - 1D)) - (this.posZ - f3 * 1.5F);
				float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.25F;
				entitysnowball.shoot(d1, d2 + f, d3, 1.6F, 1F);
				this.attackTimer = 10;
				this.world.setEntityState(this, (byte)4);
				this.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
				this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
				this.world.spawnEntity(entitysnowball);
				entitysnowball.damage = 4F;
			}

			public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_)
			{
				float f2 = this.renderYawOffset * 0.017453292F;
				float f19 = MathHelper.sin(f2);
				float f3 = MathHelper.cos(f2);
				double d1 = target.posX - (this.posX + f19 * 1.5F);
				double d2 = (target.posY - 3D + target.height) - (this.posY + this.getEyeHeight());
				double d3 = target.posZ - (this.posZ - f3 * 1.5F);
				this.throwSnowball(d1, d2, d3);
			}

			
			public class AISpecialRangedAttack extends EntityAIBase
			{
				/** The entity the AI instance has been applied to */
				private final EntityAbomniableSnowman entityHost;
				/** The entity (as a RangedAttackMob) the AI instance has been applied to. */
				private final IRangedAttackMob rangedAttackEntityHost;
				private EntityLivingBase attackTarget;
				/**
				* A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
				* maxRangedAttackTime.
				*/
				private int rangedAttackTime;
				private final double entityMoveSpeed;
				private int seeTime;
				private final float attackRadius;
				private final float maxAttackDistance;
				
				public AISpecialRangedAttack(IRangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn)
				{
					this(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
				}

				public AISpecialRangedAttack(IRangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn)
				{
					this.rangedAttackTime = -1;
					
					if (!(attacker instanceof EntityLivingBase))
					{
						throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
					}
					else
					{
						this.rangedAttackEntityHost = attacker;
						this.entityHost = (EntityAbomniableSnowman)attacker;
						this.entityMoveSpeed = movespeed;
						this.attackRadius = maxAttackDistanceIn;
						this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
						this.setMutexBits(3);
					}
				}

				/**
				* Returns whether the EntityAIBase should begin execution.
				*/
				public boolean shouldExecute()
				{
					EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
					
					if (entitylivingbase == null)
					{
						return false;
					}
					else
					{
						this.attackTarget = entitylivingbase;
						return true;
					}
				}

				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
				}

				/**
				* Resets the task
				*/
				public void resetTask()
				{
					this.attackTarget = null;
					this.seeTime = 0;
					this.rangedAttackTime = -1;
				}

				/**
				* Updates the task
				*/
				public void updateTask()
				{
					double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
					boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);
					
					if (flag)
					{
						++this.seeTime;
					}
					else
					{
						this.seeTime = 0;
					}

					if ((d0 <= (double)this.maxAttackDistance && this.seeTime >= 20) || !this.entityHost.onGround || entityMoveSpeed == 0D)
					{
						this.entityHost.getNavigator().clearPath();
					}
					else
					{
						this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
					}

					this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
					
					--this.rangedAttackTime;
					if (this.rangedAttackTime <= 0)
					{
						if (!flag)
						{
							return;
						}
						if (this.entityHost.getHealth() <= this.entityHost.getMaxHealth() / 2)
						{
							if (this.rangedAttackTime % 2 == 0 && this.rangedAttackTime <= -20 && this.rangedAttackTime >= -100)
							{
								entityHost.whirlSnowball(this.attackTarget);
							}
							if (this.rangedAttackTime <= -120)
							this.rangedAttackTime = 20;
						}
						else
						{
							if (this.rangedAttackTime % 20 == 0)
							{
								float f = MathHelper.sqrt(d0) / this.attackRadius;
								float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
								this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
							}
							if (this.rangedAttackTime <= -80)
							this.rangedAttackTime = 40;
						}
					}
				}
			}

			@Override
			public void setSwingingArms(boolean swingingArms) {}
		}