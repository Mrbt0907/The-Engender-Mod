package net.minecraft.AgeOfMinecraft.entity.tier5;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.music.IMusicInteractable;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAvoidEntitySPC;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityLargeFireballOther;
import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGhasther
extends EntityFriendlyCreature implements Massive, Flying, Armored, IMusicInteractable
{
	 private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntityGhasther.class, DataSerializers.BOOLEAN);
	private int explosionStrength = 5;
	public EnumBehaviour behaviour = EnumBehaviour.REGULAR;
	private int damageTillNextScream;
	public EntityGhasther(World worldIn)
	{
		super(worldIn);
		setSize(9F, 9F);
		this.experienceValue = 100;
		this.isImmuneToFire = true;
		this.isOffensive = true;
		this.moveHelper = new GhastMoveHelper(this);
		this.tasks.addTask(0, new AIFireballAttack(this));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.0D, 100F, 16.0F));
		this.tasks.addTask(2, new AIRandomFly(this));
		this.tasks.addTask(3, new AILookAround());
		this.tasks.addTask(4, new EntityAILookIdle(this));
	}

	public void updateBossBar()
	{
		super.updateBossBar();
		this.bossInfo.setColor(BossInfo.Color.YELLOW);
	}
	public boolean isBoss()
	{
		return true;
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityGhasther(this.world);
	}
	public boolean canUseGuardBlock()
	{
		return false;
	}

	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 0.5F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 3F;
	}
	/**
	* Bonus damage vs mobs that implement Flying
	*/
	public float getBonusVSFlying()
	{
		return 1.5F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 2F;
	}

	@SideOnly(Side.CLIENT)
	public boolean isAttacking()
	{
		return ((Boolean)this.dataManager.get(ATTACKING)).booleanValue();
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER5;
	}
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}

	public void setAttacking(boolean attacking)
	{
		this.dataManager.set(ATTACKING, Boolean.valueOf(attacking));
	}
	public void performSpecialAttack()
	{
		playSound(getHurtSound(null), getSoundVolume(), getSoundPitch());
		setSpecialAttackTimer(1200);
	}

	protected float getSoundPitch()
	{
		return super.getSoundPitch() - 0.25F;
	}

	/**
	* Sets the Entity inside a web block.
	*/
	public void setInWeb()
	{
	}

	
	public int getSpawnTimer()
	{
		return 80;
	}

	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (!world.isRemote)
		{
			if (!world.isRemote)
			{
				if (this.isEntityAlive() && (getAttackTarget() != null) && getAttackTarget().isEntityAlive() && this.isOffensive && !this.isChild() && !this.isOnSameTeam(getAttackTarget()) && this.getDistanceSq(getAttackTarget()) < (double)((this.reachWidth * this.reachWidth) + ((getAttackTarget() instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)getAttackTarget()).reachWidth : getAttackTarget().width) * (getAttackTarget() instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)getAttackTarget()).reachWidth : getAttackTarget().width)) + 9D) && ((this.ticksExisted + this.getEntityId()) % 10 == 0))
				attackEntityAsMob(getAttackTarget());
				
				if (this.isEntityAlive())
				MobChunkLoader.updateLoaded(this);
				else
				MobChunkLoader.stopLoading(this);
			}
		}

		setSize(9F, 9F);
		
		this.onGround = false;
		this.isAirBorne = true;
		
		if ((isHero()) && getSpecialAttackTimer() > 1100)
		{
			this.playSound(getHurtSound(null), getSoundVolume(), getSoundPitch());
			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(64D, 64D, 64D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if ((list != null) && (!list.isEmpty()))
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityLivingBase entity = (EntityLivingBase)list.get(i1);
					if (entity != null)
					{
						if (!isOnSameTeam(entity))
						{
							if (getSpecialAttackTimer() > 1190 && entity instanceof EntityCreature && !(entity instanceof EntityFriendlyCreature))
							((EntityCreature)entity).tasks.addTask(0, new EntityAIAvoidEntitySPC(((EntityCreature)entity), EntityGhasther.class, 128F, 1.5D, 1.5D));
							entity.hurtResistantTime = 0;
							this.inflictEngenderMobDamage(entity, "'s ears exploded thanks to ", DamageSource.WITHER, 0.25F);
							entity.addVelocity(rand.nextGaussian() * 0.05D, rand.nextGaussian() * 0.05D, rand.nextGaussian() * 0.05D);
						}
					}
				}
			}
		}

		if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 2048D) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			performSpecialAttack();
		}

		if (this.getAttackTarget() != null && this.getOwner() != null && !this.canEntityBeSeen(getAttackTarget()) && this.rand.nextInt(80) == 0)
		{
			this.playSound(this.getHurtSound(null), this.getSoundVolume(), this.getSoundPitch() + 0.25F);
		}
		if ((getControllingPassenger() != null) && ((getControllingPassenger() instanceof EntityPlayer)))
		{
			EntityPlayer passenger = (EntityPlayer)getControllingPassenger();
			this.renderYawOffset = this.rotationYaw = this.rotationYawHead = passenger.rotationYaw;
			this.rotationPitch = 0F;
			double d1 = 0.4D;
			if (this.moralRaisedTimer > 200)
			{
				d1 *= 2.0D;
			}
			Vec3d vec3 = passenger.getLook(1.0F);
			if (passenger.moveForward > 0.0F)
			{
				move(MoverType.SELF, vec3.x * d1, vec3.y * d1, vec3.z * d1);
			}
			if (passenger.moveForward < 0.0F)
			{
				move(MoverType.SELF, -(vec3.x * d1), -(vec3.y * d1), -(vec3.z * d1));
			}
		}
		if (getOwner() != null)
		{
			if ((getAttackTarget() == null) && (this.ticksExisted % 10 == 0))
			{
				double d0 = getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue();
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(d0, d0, d0), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
				if ((list != null) && (!list.isEmpty()))
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						Entity entity = (Entity)list.get(i1);
						if ((entity != null) && (entity.isEntityAlive()) && (canEntityBeSeen(entity)) && (!isOnSameTeam((EntityLivingBase)entity)) && (entity.getDistanceSq(getOwner()) <= 256.0D))
						{
							setAttackTarget((EntityLivingBase)entity);
						} else {
								list.remove(entity);
							}
						}
					}
				}
			}
		}
		public int getFireballStrength()
		{
			return this.explosionStrength;
		}
		public boolean takesFallDamage()
		{
			return false;
		}
		protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) { }
		public void travel(float strafe, float vertical, float forward)
		{
			if (this.deathTicks >= 300)
			super.travel(strafe, vertical, forward);
			if (!this.isAIDisabled() || this.isBeingRidden() || this.deathTicks < 300)
			{
				if (isInWater())
				{
					moveRelative(strafe, vertical, forward, 0.02F);
					move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
					this.motionX *= 0.800000011920929D;
					this.motionY *= 0.800000011920929D;
					this.motionZ *= 0.800000011920929D;
				}
				else if (isInLava())
				{
					moveRelative(strafe, vertical, forward, 0.02F);
					move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}
				else
				{
					float f = 0.95F;
					moveRelative(strafe, vertical, forward, 0.02F);
					f = 0.95F;
					move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
					this.motionX *= f;
					this.motionY *= f;
					this.motionZ *= f;
				}
			}
			else
			{
				super.travel(strafe, vertical, forward);
			}
		}
		public boolean isOnLadder()
		{
			return false;
		}
		protected boolean canTriggerWalking()
		{
			return false;
		}
		public boolean attackEntityFrom(DamageSource source, float amount)
		{
			if(amount > 50) amount = 50;
			
			if (this.isEntityInvulnerable(source))
			{
				return false;
			}
			else if (source.getImmediateSource() instanceof EntityLargeFireball && source.getTrueSource() instanceof EntityPlayer)
			{
				super.attackEntityFrom(source, 1000.0F);
				return true;
			}
			else
			{
				float f = this.getHealth();
				super.attackEntityFrom(source, amount);
				this.damageTillNextScream = (int)((float)this.damageTillNextScream + (f - this.getHealth()));
				
				return true;
			}
		}
		protected void entityInit()
		{
			super.entityInit();
			this.dataManager.register(ATTACKING, Boolean.valueOf(false));
		}
		protected void applyEntityAttributes()
		{
			super.applyEntityAttributes();
			getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
			getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
			getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
			getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(35.0D);
			getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(40.0D);
			getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
			getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0D);
			getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		}
		public double getKnockbackResistance()
		{
			return 1D;
		}
		protected SoundEvent getAmbientSound()
		{
			return SoundEvents.ENTITY_GHAST_AMBIENT;
		}
		protected SoundEvent getHurtSound(DamageSource source)
		{
			return SoundEvents.ENTITY_GHAST_HURT;
		}
		protected SoundEvent getDeathSound()
		{
			return SoundEvents.ENTITY_GHAST_DEATH;
		}
		@Nullable
		protected ResourceLocation getLootTable()
		{
			return ELoot.ENTITIES_GHASTHER;
		}
		protected float getSoundVolume()
		{
			return isSneaking() ? 0.1F : 10.0F;
		}
		public void writeEntityToNBT(NBTTagCompound tagCompound)
		{
			super.writeEntityToNBT(tagCompound);
			tagCompound.setInteger("ExplosionPower", this.explosionStrength);
		}
		public void readEntityFromNBT(NBTTagCompound tagCompund)
		{
			super.readEntityFromNBT(tagCompund);
			if (tagCompund.hasKey("ExplosionPower", 99))
			{
				this.explosionStrength = tagCompund.getInteger("ExplosionPower");
			}
		}
		public float getEyeHeight()
		{
			return this.height * 0.66F;
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
		public double getMountedYOffset()
		{
			return this.height * ( 0.95D);
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
		protected SoundEvent getCrushHurtSound()
		{
			return ESound.fleshHitCrushHeavy;
		}
		public void launchFireball(EntityGhasther ghast, double d2, double d3, double d4, double d5, double d6, double d7)
		{
			EntityLargeFireballOther entitylargefireball = new EntityLargeFireballOther(world, ghast, d2, d3, d4);
			entitylargefireball.explosionPower = ghast.getFireballStrength();
			entitylargefireball.posX = d5;
			entitylargefireball.posY = d6;
			entitylargefireball.posZ = d7;
			float dm = (float)ghast.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
			entitylargefireball.damage = dm;
			ghast.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 10.0F, 0.875F);
			world.spawnEntity(entitylargefireball);
		}
		public void chooseNewAttack()
		{
			if ((float)this.damageTillNextScream > 20F)
			{
				if (this.getHealth() <= this.getMaxHealth() / 3)
				this.behaviour = EnumBehaviour.RANDOM;
				else
				this.behaviour = EnumBehaviour.GHASTCALL;
			}
			else
			{
				switch (this.rand.nextInt(15))
				{
					case 1:
					case 2:
					case 3:
					this.behaviour = EnumBehaviour.SPREAD;
					break;
					case 4:
					case 5:
					case 6:
					this.behaviour = EnumBehaviour.PEPPER;
					break;
					case 7:
					case 8:
					this.behaviour = EnumBehaviour.TRISHOT;
					break;
					case 9:
					this.behaviour = EnumBehaviour.BOMBARD;
					break;
					case 10:
					this.behaviour = EnumBehaviour.MACHINEGUN;
					break;
					default:
					this.behaviour = EnumBehaviour.REGULAR;
				}}
				}
				protected void onDeathUpdate()
				{
					++this.deathTicks;
					this.world.spawnParticle(rand.nextFloat() <= 0.2F ? EnumParticleTypes.EXPLOSION_HUGE : EnumParticleTypes.EXPLOSION_LARGE, this.posX + (this.rand.nextFloat() * 9F - 4.5F), this.posY + (this.rand.nextFloat() * 9F - 4.5F), this.posZ + (this.rand.nextFloat() * 9F - 4.5F), 0.0D, 0.0D, 0.0D, new int[0]);
					
					if (this.deathTicks >= 300)
					{
						--this.rotationPitch;
						++this.deathTime;
						this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (this.rand.nextFloat() * 9F - 4.5F), this.posY + (this.rand.nextFloat() * 9F - 4.5F), this.posZ + (this.rand.nextFloat() * 9F - 4.5F), 0.0D, 0.0D, 0.0D, new int[0]);
					}

					if (!this.world.isRemote)
					{
						if (this.deathTicks == 340)
						{
							if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot"))
							{
								int i = this.getExperiencePoints(this.attackingPlayer);
								i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
								while (i > 0)
								{
									int j = EntityXPOrb.getXPSplit(i);
									i -= j;
									this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
									this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
									this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
									this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
									this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
								}
							}

							this.setDead();
							
							for (int k = 0; k < 10; ++k)
							{
								this.spawnExplosionParticle();
							}
						}
						if (this.deathTicks % 60 == 0)
						this.playHurtSound(null);
						if (this.deathTicks == 1)
						{
							++this.motionY;
							++this.motionY;
							if (getOwner() != null)
							{
								for (EntityPlayer entityplayer : world.playerEntities)
								{
									world.playSound(null, entityplayer.getPosition(), getDeathSound(), this.getSoundCategory(), getSoundVolume(), 1.0F);
									entityplayer.sendStatusMessage(new TextComponentTranslation("\u00A74"+ this.getOwner().getName() + "'s " + this.getName() + " has been killed!!!"), true);
								}
								((EntityPlayerMP)getOwner()).sendMessage(new TextComponentTranslation("Your Ghasther has been destroyed!", new Object[0]));
							}
						}
						if (this.deathTicks > 80)
						{
							this.move(MoverType.SELF, 0.0D, -0.2D, 0.0D);
						}
						this.renderYawOffset = this.rotationYawHead = this.rotationYaw += 5F;
					}
				}
				static class AIFireballAttack extends EntityAIBase
				{
					private EntityGhasther ghast;
					public int attackTimer;
					public AIFireballAttack(EntityGhasther ghast)
					{
						this.ghast = ghast;
					}
					public boolean shouldExecute()
					{
						return this.ghast.getAttackTarget() != null && !this.ghast.isSneaking();
					}
					public void startExecuting()
					{
						this.attackTimer = 0;
						this.ghast.setArmsRaised(true);
						this.ghast.chooseNewAttack();
					}
					public void resetTask()
					{
						this.ghast.setAttacking(false);
						this.ghast.setArmsRaised(false);
					}
					public void updateTask()
					{
						double d1 = ghast.isChild() ? 1.5D : 3D;
						Vec3d vec3 = ghast.getLook(1.0F);
						double mark1 = this.ghast.posX + vec3.x * d1;
						double mark2 = this.ghast.posY + 1D + vec3.y * d1;
						double mark3 = this.ghast.posZ + vec3.z * d1;
						EntityLivingBase entitylivingbase = this.ghast.getAttackTarget();
						double d0 = 100.0D;
						if (entitylivingbase != null && entitylivingbase.getDistanceSq(this.ghast) < d0 * d0)
						{
							this.attackTimer += 1;
							if (this.ghast.moralRaisedTimer > 200)
							{
								this.attackTimer += 1;
							}
							switch (this.ghast.behaviour)
							{
								case PEPPER:
								{
									if (this.attackTimer == 10)
									{
										this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, 0.6F + this.ghast.getRNG().nextFloat() * 0.4F);
									}
									if (this.attackTimer > 30 && this.attackTimer % 10 == 0)
									{
										double d2 = (entitylivingbase.posX) - mark1;
										double d3 = (entitylivingbase.posY + (entitylivingbase.height > 8? 7D : entitylivingbase.height * 0.5D)) - mark2;
										double d4 = (entitylivingbase.posZ) - mark3;
										this.ghast.launchFireball(ghast, d2, d3, d4, mark1, mark2, mark3);
										
}
										if (this.attackTimer == 80)
										{
											this.attackTimer = -40;
											this.ghast.chooseNewAttack();
										}
									}
									break;
									case TRISHOT:
									{
										if (this.attackTimer == 10)
										{
											this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, 0.6F + this.ghast.getRNG().nextFloat() * 0.4F);
										}
										if (this.attackTimer >= 20 && this.attackTimer % 20 == 0)
										{
											double d2 = (entitylivingbase.posX) - mark1;
											double d3 = (entitylivingbase.posY + (entitylivingbase.height > 6? 6D : entitylivingbase.height * 0.5D)) - mark2;
											double d4 = (entitylivingbase.posZ) - mark3;
											this.ghast.launchFireball(ghast, d2, d3, d4, mark1, mark2, mark3);
										}
										if (this.attackTimer == 60)
										{
											this.attackTimer = -40;
											this.ghast.chooseNewAttack();
										}
									}
									break;
									case CATCH:
									{
										if (this.attackTimer == 10)
										{
											this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, 0.5F + this.ghast.getRNG().nextFloat() * 0.4F);
										}
										if (this.attackTimer == 40)
										{
											for (int i = 0; i <= 6; i++)
											{
												double d2 = (entitylivingbase.posX + (i == 1 ? 0 : (ghast.getRNG().nextDouble() * 8D - 4D))) - mark1;
												double d3 = (entitylivingbase.posY + (i == 1 ? 0 : (ghast.getRNG().nextDouble() * 8D - 4D)) + (entitylivingbase.height > 8? 7D : entitylivingbase.height * 0.5D)) - mark2;
												double d4 = (entitylivingbase.posZ + (i == 1 ? 0 : (ghast.getRNG().nextDouble() * 8D - 4D))) - mark3;
												this.ghast.launchFireball(ghast, d2, d3, d4, mark1, mark2, mark3);
											}
										}
										if (this.attackTimer == 50)
										{
											this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, 0.5F + this.ghast.getRNG().nextFloat() * 0.4F);
										}
										if (this.attackTimer == 80)
										{
											for (int i = 0; i <= 6; i++)
											{
												double d2 = (entitylivingbase.posX + (i == 1 ? 0 : (ghast.getRNG().nextDouble() * 8D - 4D))) - mark1;
												double d3 = (entitylivingbase.posY + (i == 1 ? 0 : (ghast.getRNG().nextDouble() * 8D - 4D)) + (entitylivingbase.height > 8? 7D : entitylivingbase.height * 0.5D)) - mark2;
												double d4 = (entitylivingbase.posZ + (i == 1 ? 0 : (ghast.getRNG().nextDouble() * 8D - 4D))) - mark3;
												this.ghast.launchFireball(ghast, d2, d3, d4, mark1, mark2, mark3);
											}
											this.attackTimer = -40;
											this.ghast.chooseNewAttack();
										}
									}
									break;
									case SPREAD:
									{
										if (this.attackTimer == 10)
										{
											this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, 0.5F + this.ghast.getRNG().nextFloat() * 0.4F);
										}
										if (this.attackTimer == 40)
										{
											for (int i = 0; i <= 6 + ghast.getRNG().nextInt(3); i++)
											{
												double d2 = (entitylivingbase.posX + (i == 1 ? 0 : (ghast.getRNG().nextDouble() * 8D - 4D))) - mark1;
												double d3 = (entitylivingbase.posY + (i == 1 ? 0 : (ghast.getRNG().nextDouble() * 8D - 4D)) + (entitylivingbase.height > 8? 7D : entitylivingbase.height * 0.5D)) - mark2;
												double d4 = (entitylivingbase.posZ + (i == 1 ? 0 : (ghast.getRNG().nextDouble() * 8D - 4D))) - mark3;
												this.ghast.launchFireball(ghast, d2, d3, d4, mark1, mark2, mark3);
											}
											this.attackTimer = -40;
											this.ghast.chooseNewAttack();
										}
									}
									break;
									case BOMBARD:
									{
										if (this.attackTimer == 10)
										{
											this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, 0.4F + this.ghast.getRNG().nextFloat() * 0.4F);
										}
										if (this.attackTimer > 20 && this.attackTimer % 3 == 0)
										{
											double d2 = (entitylivingbase.posX + (ghast.getRNG().nextDouble() * 2D - 1D)) - mark1;
											double d3 = (entitylivingbase.posY + (ghast.getRNG().nextDouble() * 2D - 1D) + (entitylivingbase.height > 8? 7D : entitylivingbase.height * 0.5D)) - mark2;
											double d4 = (entitylivingbase.posZ + (ghast.getRNG().nextDouble() * 2D - 1D)) - mark3;
											this.ghast.launchFireball(ghast, d2, d3, d4, mark1, mark2, mark3);
										}
										if (this.attackTimer == 140)
										{
											this.attackTimer = -60;
											this.ghast.chooseNewAttack();
										}
									}
									break;
									case MACHINEGUN:
									{
										if (this.attackTimer == 10)
										{
											this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, 0.4F + this.ghast.getRNG().nextFloat() * 0.4F);
										}
										if (this.attackTimer > 20)
										{
											double d2 = (entitylivingbase.posX) - mark1;
											double d3 = (entitylivingbase.posY + (entitylivingbase.height > 6? 6D : entitylivingbase.height * 0.5D)) - mark2;
											double d4 = (entitylivingbase.posZ) - mark3;
											this.ghast.launchFireball(ghast, d2, d3, d4, mark1, mark2, mark3);
										}
										if (this.attackTimer == 80)
										{
											this.attackTimer = -80;
											this.ghast.chooseNewAttack();
										}
									}
									break;
									case GHASTCALL:
									{
										if (this.attackTimer == 40)
										{
											this.ghast.playHurtSound(null);
											this.ghast.playHurtSound(null);
											this.ghast.playHurtSound(null);
											this.ghast.playHurtSound(null);
											this.ghast.playHurtSound(null);
										}
										if (this.attackTimer >= 80)
										{
											int i = MathHelper.floor(this.ghast.posX);
											int k = MathHelper.floor(this.ghast.posZ);
											for (int h = (int)this.ghast.getHealth(); h <= this.ghast.getMaxHealth(); ++h)
											for (int l = 0; l < 10; ++l)
											{
												int i1 = i + MathHelper.getInt(this.ghast.rand, 16, 64) * MathHelper.getInt(this.ghast.rand, -1, 1);
												int k1 = k + MathHelper.getInt(this.ghast.rand, 16, 64) * MathHelper.getInt(this.ghast.rand, -1, 1);
												int j1 = this.ghast.world.getTopSolidOrLiquidBlock(new BlockPos((double)i1, (double)MathHelper.floor(this.ghast.posY), (double)k1)).getY();
												
												if (this.ghast.world.getBlockState(new BlockPos(i1, j1 - 1, k1)).isSideSolid(this.ghast.world, new BlockPos(i1, j1 - 1, k1), net.minecraft.util.EnumFacing.UP))
												{
													EntityGhast entityzombie = new EntityGhast(this.ghast.world);
													entityzombie.setPosition((double)i1, (double)j1, (double)k1);
													
													if (this.ghast.rand.nextInt(100) == 0 && !this.ghast.world.isAnyPlayerWithinRangeAt((double)i1, (double)j1, (double)k1, 7.0D) && this.ghast.world.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), entityzombie) && this.ghast.world.getCollisionBoxes(entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.ghast.world.containsAnyLiquid(entityzombie.getEntityBoundingBox()))
													{
														this.ghast.world.spawnEntity(entityzombie);
														entityzombie.onInitialSpawn(this.ghast.world.getDifficultyForLocation(new BlockPos(entityzombie)), (IEntityLivingData)null);
														entityzombie.setOwnerId(this.ghast.getOwnerId());
														entityzombie.setIsAntiMob(this.ghast.isAntiMob());
														entityzombie.setGrowingAge(this.ghast.getGrowingAge());
														break;
													}
												}
											}
											this.attackTimer = -80;
											this.ghast.chooseNewAttack();
											this.ghast.damageTillNextScream = 0;
										}
									}
									break;
									case RANDOM:
									{
										if (this.attackTimer == 10)
										{
											this.ghast.playHurtSound(null);
										}
										if (this.attackTimer > 20 && this.attackTimer % 10 == 0)
										{
											double d2 = (ghast.getRNG().nextDouble() * 64D - 32D);
											double d3 = (ghast.getRNG().nextDouble() * 64D - 32D);
											double d4 = (ghast.getRNG().nextDouble() * 64D - 32D);
											this.ghast.launchFireball(ghast, d2, d3, d4, mark1, mark2, mark3);
										}
										if (this.attackTimer == 140)
										{
											this.attackTimer = -60;
											this.ghast.chooseNewAttack();
											this.ghast.damageTillNextScream = 0;
										}
									}
									break;
									default:
									{
										if (this.ghast.behaviour == EnumBehaviour.GHASTCALL || this.ghast.behaviour == EnumBehaviour.RANDOM)
										this.attackTimer = 0;
										if (this.attackTimer == 10)
										{
											this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, 0.6F + this.ghast.getRNG().nextFloat() * 0.4F);
										}
										if (this.attackTimer == 20)
										{
											double d2 = (entitylivingbase.posX) - mark1;
											double d3 = (entitylivingbase.posY + (entitylivingbase.height > 8? 7D : entitylivingbase.height * 0.5D)) - mark2;
											double d4 = (entitylivingbase.posZ) - mark3;
											this.ghast.launchFireball(ghast, d2, d3, d4, mark1, mark2, mark3);
											this.attackTimer = -40;
											this.ghast.chooseNewAttack();
											if (ghast.getDistance(entitylivingbase) <= 10D)
											ghast.attackEntityAsMob(entitylivingbase);
										}
									}
								}
							} else if (this.attackTimer > 0)
								{
									this.attackTimer -= 1;
								}
								this.ghast.setAttacking(this.attackTimer > 10 || this.ghast.getSpecialAttackTimer() > 1100);
							}
						}
						class AILookAround extends EntityAIBase
						{
							private EntityGhasther parentEntity = EntityGhasther.this;
							public AILookAround()
							{
								setMutexBits(2);
							}
							public boolean shouldExecute()
							{
								return true;
							}
							public void updateTask()
							{
								if (this.parentEntity.getControllingPassenger() != null)
								{
									this.parentEntity.prevRotationYaw = (this.parentEntity.rotationYaw = this.parentEntity.getControllingPassenger().rotationYaw);
									this.parentEntity.rotationPitch = 0.0F;
									this.parentEntity.setRotation(this.parentEntity.rotationYaw, this.parentEntity.rotationPitch);
									this.parentEntity.rotationYawHead = (this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw);
								}
								else if (this.parentEntity.getAttackTarget() == null)
								{
									this.parentEntity.renderYawOffset = (this.parentEntity.rotationYaw = this.parentEntity.rotationYawHead = -(float)Math.atan2(this.parentEntity.motionX, this.parentEntity.motionZ) * 180.0F / 3.1415927F);
								}
								else
								{
									EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
									this.parentEntity.renderYawOffset = (this.parentEntity.rotationYaw = this.parentEntity.rotationYawHead);
									this.parentEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 180.0F, 180F);
								}
							}
						}
						static class AIRandomFly
						extends EntityAIBase
						{
							private EntityGhasther ghast;
							public AIRandomFly(EntityGhasther ghast)
							{
								this.ghast = ghast;
								setMutexBits(1);
							}
							public boolean shouldExecute()
							{
								EntityMoveHelper entitymovehelper = this.ghast.getMoveHelper();
								if (!entitymovehelper.isUpdating())
								{
									return true;
								}
								else
								{
									double d0 = entitymovehelper.getX() - this.ghast.posX;
									double d1 = entitymovehelper.getY() - this.ghast.posY;
									double d2 = entitymovehelper.getZ() - this.ghast.posZ;
									double d3 = d0 * d0 + d1 * d1 + d2 * d2;
									return d3 < 1.0D || d3 > 6400.0D;
								}
							}
							public boolean shouldContinueExecuting()
							{
								return false;
							}
							public void startExecuting()
							{
								Random random = this.ghast.getRNG();
								if (this.ghast.getOwner() != null)
								{
									if (this.ghast.getOwner().isSneaking())
									{
										double d0 = this.ghast.getOwner().posX + (random.nextFloat() * 2.0F - 1.0F) * 4.0F;
										double d1 = this.ghast.getOwner().posY + 8.0D + (random.nextFloat() * 2.0F - 1.0F) * 4.0F;
										double d2 = this.ghast.getOwner().posZ + (random.nextFloat() * 2.0F - 1.0F) * 4.0F;
										this.ghast.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
									}
									else if (!this.ghast.getCurrentBook().isEmpty())
									{
										double d0 = this.ghast.getOwner().posX;
										double d1 = this.ghast.getOwner().posY + 8F;
										double d2 = this.ghast.getOwner().posZ;
										this.ghast.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
									}
									else
									{
										double d0 = this.ghast.getOwner().posX + (random.nextFloat() * 2.0F - 1.0F) * 16F;
										double d1 = this.ghast.getOwner().posY + (this.ghast.isChild() ? 16D : 32D) + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
										double d2 = this.ghast.getOwner().posZ + (random.nextFloat() * 2.0F - 1.0F) * 16F;
										this.ghast.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
									}
								}
								else
								{
									double d0 = this.ghast.posX + (random.nextGaussian() * 32F);
									double d1 = this.ghast.posY + (random.nextGaussian() * 32F);
									double d2 = this.ghast.posZ + (random.nextGaussian() * 32F);
									this.ghast.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
								}
							}
						}
						static class GhastMoveHelper extends EntityMoveHelper
						{
							private EntityGhasther parentEntity;
							private int courseChangeCooldown;
							public GhastMoveHelper(EntityGhasther ghast)
							{
								super(ghast);
								this.parentEntity = ghast;
							}
							public void onUpdateMoveHelper()
							{
								if (this.action == EntityMoveHelper.Action.MOVE_TO && this.parentEntity.getJukeboxToDanceTo() == null)
								{
									double d0 = this.posX - this.parentEntity.posX;
									double d1 = this.posY - this.parentEntity.posY;
									double d2 = this.posZ - this.parentEntity.posZ;
									double d3 = d0 * d0 + d1 * d1 + d2 * d2;
									
									if (this.courseChangeCooldown-- <= 0)
									{
										this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
										d3 = (double)MathHelper.sqrt(d3);
										
										if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
										{
											this.parentEntity.motionX += d0 / d3 * (this.parentEntity.moralRaisedTimer > 200 ? 0.15D : 0.075D);
											this.parentEntity.motionY += d1 / d3 * (this.parentEntity.moralRaisedTimer > 200 ? 0.15D : 0.075D);
											this.parentEntity.motionZ += d2 / d3 * (this.parentEntity.moralRaisedTimer > 200 ? 0.15D : 0.075D);
										}
										else
										{
											this.action = EntityMoveHelper.Action.WAIT;
										}
									}
								}
							}
							private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
							{
								double d0 = (x - this.parentEntity.posX) / p_179926_7_;
								double d1 = (y - this.parentEntity.posY) / p_179926_7_;
								double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
								AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
								
								for (int i = 1; (double)i < p_179926_7_; ++i)
								{
									axisalignedbb = axisalignedbb.offset(d0, d1, d2);
									
									if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty())
									{
										return false;
									}
								}

								return true;
							}
						}
						private static enum EnumBehaviour{
							REGULAR(),
							SPREAD(),
							BOMBARD(),
							CATCH(),
							MACHINEGUN(),
							PEPPER(),
							TRISHOT(),
							GHASTCALL(),
							ENRAGED(),
							RANDOM(),
							FINALE()
						}
						@Override
						@SideOnly(Side.CLIENT)
						public boolean isMusicDead()
						{
							return isDead;
						}

						@Override
						@SideOnly(Side.CLIENT)
						public int getMusicPriority()
						{
							return 4;
						}

						@Override
						@SideOnly(Side.CLIENT)
						public SoundEvent getMusic()
						{
							return isOnSameTeam(FMLClientHandler.instance().getClientPlayerEntity()) ? null : ESound.ghasthertheme;
						}
					}