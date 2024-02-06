package net.minecraft.AgeOfMinecraft.entity.tier4;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAvoidEntitySPC;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGhast
extends EntityFriendlyCreature implements Massive, Flying, Light
{
	private static final DataParameter<Boolean> SHOULD_FLY = EntityDataManager.createKey(EntityGhast.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(EntityGhast.class, DataSerializers.BOOLEAN);
	public boolean eleanor;
	private int explosionStrength = 1;
	public EntityGhast(World worldIn)
	{
		super(worldIn);
		setSize(4.5F, 4.5F);
		this.isImmuneToFire = true;
		this.isOffensive = true;
		this.moveHelper = new GhastMoveHelper(this);
		this.tasks.addTask(0, new AIFireballAttack(this));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.0D, 100F, 16.0F));
		this.tasks.addTask(2, new AIRandomFly(this));
		this.tasks.addTask(3, new AILookAround());
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.experienceValue = 20;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityGhast(this.world);
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
		return EnumTier.TIER4;
	}
	public int getMaxSpawnedInChunk()
	{
		return 1;
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

	public void setAttacking(boolean attacking)
	{
		this.dataManager.set(ATTACKING, Boolean.valueOf(attacking));
	}

	public void setFlying(boolean attacking)
	{
		this.dataManager.set(SHOULD_FLY, Boolean.valueOf(attacking));
	}

	public boolean isFlying()
	{
		return ((Boolean)this.dataManager.get(SHOULD_FLY)).booleanValue();
	}
	public void performSpecialAttack()
	{
		playSound(getHurtSound(null), getSoundVolume(), getSoundPitch());
		setSpecialAttackTimer(1200);
	}

	protected float getSoundPitch()
	{
		return super.getSoundPitch();
	}

	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (this.isBeingRidden() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityPlayer)
		{
			 EntityPlayer passenger = (EntityPlayer)this.getControllingPassenger();
			 this.renderYawOffset = this.rotationYaw = this.rotationYawHead = passenger.rotationYaw;
			 this.rotationPitch = 0F;
		}

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
							((EntityCreature)entity).tasks.addTask(0, new EntityAIAvoidEntitySPC(((EntityCreature)entity), EntityGhast.class, 128F, 1.5D, 1.5D));
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

		setSize(4.5F, 4.5F);
		
		
		this.setFlying(true);
		
		if (!this.isFlying() && !this.isBeingRidden() && this.isEntityAlive())
		this.motionY *= 0.6D;
		
		if (!this.isFlying() && (this.isInWater() || this.isInLava()) && this.getRNG().nextFloat() < 0.8F && this.isEntityAlive())
		this.getJumpHelper().setJumping();
		
		if (this.getAttackTarget() != null && this.getOwner() != null && !this.canEntityBeSeen(getAttackTarget()) && this.rand.nextInt(80) == 0)
		{
			this.playSound(this.getHurtSound(null), this.getSoundVolume(), this.getSoundPitch() + 0.25F);
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
			if ((this.isFlying() || this.isBeingRidden()) && this.isEntityAlive())
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
			if (this.eleanor && source instanceof EntityDamageSourceIndirect)
			{
				return false;
			}

			Entity entity = source.getTrueSource();
			
			if (entity instanceof EntityLivingBase && this.eleanor && amount < 50F)
			{
				EntityLivingBase creature = (EntityLivingBase)entity;
				creature.attackEntityFrom(DamageSource.GENERIC.setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute(), amount);
				creature.knockBack(this, amount * 0.1F, -MathHelper.sin(creature.rotationYawHead * 0.017453292F), MathHelper.cos(creature.rotationYawHead * 0.017453292F));
				
}
				
				if (this.eleanor && (source.isFireDamage() || source.isExplosion() || source.isProjectile() || source.isMagicDamage() || amount < 50F))
				{
					return false;
				}

				if (amount >= 1.0F && this.eleanor)
				{
					amount *= 0.0001F;
				}

				
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
					return super.attackEntityFrom(source, amount);
				}
			}
			protected void entityInit()
			{
				super.entityInit();
				this.dataManager.register(ATTACKING, Boolean.valueOf(false));
				this.dataManager.register(SHOULD_FLY, Boolean.valueOf(false));
			}
			protected void applyEntityAttributes()
			{
				super.applyEntityAttributes();
				getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
				getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
				getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(100.0D);
				getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(17.0D);
				getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(40.0D);
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
				return ELoot.ENTITIES_GHAST;
			}
			protected float getSoundVolume()
			{
				return isSneaking() ? 0.1F : 10.0F;
			}
			public void writeEntityToNBT(NBTTagCompound tagCompound)
			{
				super.writeEntityToNBT(tagCompound);
				tagCompound.setInteger("ExplosionPower", this.explosionStrength);
				if (this.eleanor)
				{
					tagCompound.setBoolean("Eleanor", true);
				}
			}
			public void readEntityFromNBT(NBTTagCompound tagCompund)
			{
				super.readEntityFromNBT(tagCompund);
				if (tagCompund.hasKey("ExplosionPower", 99))
				{
					this.explosionStrength = tagCompund.getInteger("ExplosionPower");
				}
				if (tagCompund.hasKey("Eleanor", 99))
				{
					this.eleanor = tagCompund.getBoolean("Eleanor");
				}
			}

			public void setCustomNameTag(String name)
			{
				super.setCustomNameTag(name);
				
				if (EngenderConfig.debugMode && "Eleanor".equals(name) && this.isHero())
				{
					this.ticksExisted = 0;
					this.becomeAHero();
					this.eleanor = true;
				}
				else
				{
					this.eleanor = false;
				}
			}
			public float getEyeHeight()
			{
				return this.height * 0.66F;
			}
			public double getMountedYOffset()
			{
				return this.height * (0.95D);
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
			static class AIFireballAttack extends EntityAIBase
			{
				private EntityGhast ghast;
				public int attackTimer;
				public AIFireballAttack(EntityGhast ghast)
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
				}
				public void resetTask()
				{
					this.ghast.setAttacking(false);
					this.ghast.setArmsRaised(false);
				}
				public void updateTask()
				{
					EntityLivingBase entitylivingbase = this.ghast.getAttackTarget();
					double d0 = 100.0D;
					if (entitylivingbase != null && entitylivingbase.getDistanceSq(this.ghast) < d0 * d0)
					{
						World world = this.ghast.world;
						this.attackTimer += 1;
						if (this.ghast.moralRaisedTimer > 200)
						{
							this.attackTimer += 1;
						}
						if (this.attackTimer == 10)
						{
							this.ghast.playSound(SoundEvents.ENTITY_GHAST_WARN, 10.0F, 0.8F + this.ghast.getRNG().nextFloat() * 0.4F + (this.ghast.isChild() ? 0.5F : 0F) + ( 0F));
						}
						if (this.attackTimer == 20)
						{
							double d1 = (this.ghast.isChild() ? 2D : 4D);
							Vec3d vec3 = this.ghast.getLook(1.0F);
							double d2 = (entitylivingbase.posX + (entitylivingbase.motionX * 10D)) - (this.ghast.posX + vec3.x * d1);
							double d3 = (entitylivingbase.posY + (entitylivingbase.height > 8? 7D : entitylivingbase.height * 0.5D)) - (this.ghast.posY + 1D);
							double d4 = (entitylivingbase.posZ + (entitylivingbase.motionZ * 10D)) - (this.ghast.posZ + vec3.z * d1);
							if (this.ghast.isChild())
							{
								EntitySmallFireballOther entitylargefireball = new EntitySmallFireballOther(world, this.ghast, d2, d3, d4);
								entitylargefireball.posX = (this.ghast.posX + vec3.x * d1);
								entitylargefireball.posY = (this.ghast.posY + 1D);
								entitylargefireball.posZ = (this.ghast.posZ + vec3.z * d1);
								float dm = (float)this.ghast.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
								entitylargefireball.damage = dm;
								this.ghast.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 10.0F, 1.5F);
								world.spawnEntity(entitylargefireball);
								if (this.ghast.moralRaisedTimer > 200)
								this.attackTimer = -15;
								else
								this.attackTimer = -30;
							}
							else
							{
								EntityLargeFireballOther entitylargefireball = new EntityLargeFireballOther(world, this.ghast, d2, d3, d4);
								entitylargefireball.explosionPower = this.ghast.getFireballStrength() * (this.ghast.isHero() ? 2 : 1);
								entitylargefireball.posX = (this.ghast.posX + vec3.x * d1);
								entitylargefireball.posY = (this.ghast.posY + 1D);
								entitylargefireball.posZ = (this.ghast.posZ + vec3.z * d1);
								float dm = (float)this.ghast.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
								entitylargefireball.damage = dm;
								this.ghast.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 10.0F, 1.0F);
								world.spawnEntity(entitylargefireball);
								if (this.ghast.moralRaisedTimer > 200)
								this.attackTimer = -20;
								else
									this.attackTimer = -40;
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
					private EntityGhast parentEntity = EntityGhast.this;
					public AILookAround()
					{
						setMutexBits(2);
					}
					public boolean shouldExecute()
					{
						return this.parentEntity.isFlying();
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
					private EntityGhast ghast;
					public AIRandomFly(EntityGhast ghast)
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
						double d0 = entitymovehelper.getX() - this.ghast.posX;
						double d1 = entitymovehelper.getY() - this.ghast.posY;
						double d2 = entitymovehelper.getZ() - this.ghast.posZ;
						double d3 = d0 * d0 + d1 * d1 + d2 * d2;
						return (d3 < 1.0D || d3 > 3600.0D) && !this.ghast.isBeingRidden();
					}
					public boolean shouldContinueExecuting()
					{
						return !this.ghast.isFlying() && !this.ghast.isBeingRidden();
					}
					public void startExecuting()
					{
						if (this.ghast.isBeingRidden())
						this.resetTask();
						Random random = this.ghast.getRNG();
						if (this.ghast.getOwner() != null)
						{
							if (this.ghast.getOwner().isSneaking() || !this.ghast.getCurrentBook().isEmpty())
							{
								double d0 = this.ghast.getOwner().posX + (random.nextFloat() * 2.0F - 1.0F) * 4.0F;
								double d1 = this.ghast.getOwner().posY + 8.0D + (random.nextFloat() * 2.0F - 1.0F) * 4.0F;
								double d2 = this.ghast.getOwner().posZ + (random.nextFloat() * 2.0F - 1.0F) * 4.0F;
								this.ghast.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
							}
							else if (!this.ghast.getCurrentBook().isEmpty())
							{
								double d0 = this.ghast.getOwner().posX;
								double d1 = this.ghast.getOwner().posY + 4F;
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
					private EntityGhast parentEntity;
					private int courseChangeCooldown;
					public GhastMoveHelper(EntityGhast ghast)
					{
						super(ghast);
						this.parentEntity = ghast;
					}
					public void onUpdateMoveHelper()
					{
						if (parentEntity.getControllingPassenger() != null && parentEntity.getControllingPassenger() instanceof EntityPlayer && this.parentEntity.isBeingRidden())
						{
							EntityPlayer passenger = (EntityPlayer)parentEntity.getControllingPassenger();
							parentEntity.renderYawOffset = parentEntity.rotationYaw = parentEntity.rotationYawHead = passenger.rotationYaw;
							parentEntity.rotationPitch = 0F;
							Vec3d vec3 = passenger.getLook(1.0F);
							double d0 = (parentEntity.posX) - (parentEntity.posX + (vec3.x * 50D));
							double d1 = (parentEntity.posY) - (parentEntity.posY + (vec3.y * 50D));
							double d2 = (parentEntity.posZ) - (parentEntity.posZ + (vec3.z * 50D));
							double d3 = d0 * d0 + d1 * d1 + d2 * d2;
							d3 = (double)MathHelper.sqrt(d3);
							if (passenger.moveForward > 0.0F)
							{
								this.parentEntity.motionX -= d0 / d3 * (this.parentEntity.moralRaisedTimer > 0 ? 0.2D : 0.1D);
								this.parentEntity.motionY -= d1 / d3 * (this.parentEntity.moralRaisedTimer > 0 ? 0.2D : 0.1D);
								this.parentEntity.motionZ -= d2 / d3 * (this.parentEntity.moralRaisedTimer > 0 ? 0.2D : 0.1D);
								if (this.parentEntity.motionX > 0.5D)
								this.parentEntity.motionX = 0.5D;
								if (this.parentEntity.motionY > 0.5D)
								this.parentEntity.motionY = 0.5D;
								if (this.parentEntity.motionZ > 0.5D)
								this.parentEntity.motionZ = 0.5D;
								if (this.parentEntity.motionX < -0.5D)
								this.parentEntity.motionX = -0.5D;
								if (this.parentEntity.motionY < -0.5D)
								this.parentEntity.motionY = -0.5D;
								if (this.parentEntity.motionZ < -0.5D)
								this.parentEntity.motionZ = -0.5D;
							}
						}
						if (this.parentEntity.isFlying() && !this.parentEntity.isBeingRidden())
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
										this.parentEntity.motionX += d0 / d3 * (this.parentEntity.moralRaisedTimer > 200 ? 0.2D : 0.1D);
										this.parentEntity.motionY += d1 / d3 * (this.parentEntity.moralRaisedTimer > 200 ? 0.2D : 0.1D);
										this.parentEntity.motionZ += d2 / d3 * (this.parentEntity.moralRaisedTimer > 200 ? 0.2D : 0.1D);
									}
									else
									{
										this.action = EntityMoveHelper.Action.WAIT;
									}
								}
							}
						}
						else
						{
							super.onUpdateMoveHelper();
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
			}

			
			