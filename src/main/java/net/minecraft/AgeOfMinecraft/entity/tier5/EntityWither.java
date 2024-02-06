package net.minecraft.AgeOfMinecraft.entity.tier5;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.music.IMusicInteractable;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.ExtendMultiPartEntityPart;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Undead;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAttackRangedAlly;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityCommandBlockWither;
import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWither extends EntityFriendlyCreature implements IEntityMultiPart, IRangedAttackMob, IJumpingMount, Flying, Armored, Undead, IMusicInteractable
{
	private static final DataParameter<Integer> FIRST_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> SECOND_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> THIRD_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
	private static final DataParameter<Integer>[] HEAD_TARGETS = new DataParameter[] { FIRST_HEAD_TARGET, SECOND_HEAD_TARGET, THIRD_HEAD_TARGET };
private static final DataParameter<Integer> INVULNERABILITY_TIME = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
private float[] xRotationHeads = new float[2];
private float[] yRotationHeads = new float[2];
private float[] xRotOHeads = new float[2];
private float[] yRotOHeads = new float[2];
private int[] nextHeadUpdate = new int[2];
private int[] idleHeadUpdates = new int[2];
private float[] headRandomTurn = new float[2];
private static final DataParameter<Integer> HOVERTIMER = EntityDataManager.<Integer>createKey(EntityWither.class, DataSerializers.VARINT);
private static final DataParameter<Integer> RAMTIMER = EntityDataManager.<Integer>createKey(EntityWither.class, DataSerializers.VARINT);
private static final DataParameter<Boolean> SPAWNEDSKELETONS = EntityDataManager.createKey(EntityWither.class, DataSerializers.BOOLEAN);
private double hoverX, hoverZ;
public ExtendMultiPartEntityPart[] partArray;
public ExtendMultiPartEntityPart mainHead = new ExtendMultiPartEntityPart(this, "mainHead", 1.0F, 1.0F);
public ExtendMultiPartEntityPart rightHead = new ExtendMultiPartEntityPart(this, "rightHead", 0.75F, 0.75F);
public ExtendMultiPartEntityPart leftHead = new ExtendMultiPartEntityPart(this, "leftHead", 0.75F, 0.75F);
public ExtendMultiPartEntityPart spine = new ExtendMultiPartEntityPart(this, "spine", 0.4F, 1.6F);
public ExtendMultiPartEntityPart lowerspine = new ExtendMultiPartEntityPart(this, "lowerspine", 0.4F, 0.6F);
public ExtendMultiPartEntityPart rightRibs = new ExtendMultiPartEntityPart(this, "rightRibs", 0.5F, 0.8F);
public ExtendMultiPartEntityPart leftRibs = new ExtendMultiPartEntityPart(this, "leftRibs", 0.5F, 0.8F);
public ExtendMultiPartEntityPart rightsupport = new ExtendMultiPartEntityPart(this, "rightsupport", 0.4F, 0.4F);
public ExtendMultiPartEntityPart leftsupport = new ExtendMultiPartEntityPart(this, "leftsupport", 0.4F, 0.4F);
private int blockBreakCounter;
private int lastActiveTime;
private int timeSinceIgnited;

public EntityWither(World worldIn)
{
	super(worldIn);
	this.partArray = new ExtendMultiPartEntityPart[]{
		this.mainHead,this.rightHead,this.leftHead,this.spine,this.lowerspine,this.rightRibs,this.leftRibs,this.rightsupport,this.leftsupport
	};
	setHealth(getMaxHealth());
	setSize(0.9F, 3.3F);
	this.isOffensive = true;
	this.isImmuneToFire = true;
	((PathNavigateGround)getNavigator()).setCanSwim(true);
	this.tasks.addTask(0, new AIDoNothing());
	this.tasks.addTask(1, new EntityAISwimming(this));
	this.tasks.addTask(2, new EntityAIFollowLeader(this, 4.0D, 100F, 16F));
	this.tasks.addTask(3, new EntityAIAttackRangedAlly(this, 4.0D, 50, 20.0F));
	this.tasks.addTask(3, new EntityAIWanderAvoidWaterFlying(this, 2D));
	this.tasks.addTask(7, new EntityAILookIdle(this));
	this.ignoreFrustumCheck = true;
	this.experienceValue = 500;
	this.moveHelper = new EntityFlyHelper(this);
}

public boolean attackable()
{
	return this.getInvulTime() <= 0 && super.attackable();
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

public boolean hasSpawnedSkeletons()
{
	return ((Boolean)getDataManager().get(SPAWNEDSKELETONS)).booleanValue();
}
public void setCanSpawnSkeletons(boolean childZombie)
{
	getDataManager().set(SPAWNEDSKELETONS, Boolean.valueOf(childZombie));
}

public void updateBossBar()
{
	super.updateBossBar();
	this.bossInfo.setColor(BossInfo.Color.PURPLE);
	this.bossInfo.setDarkenSky(true);
}

@SideOnly(Side.CLIENT)
public float getCreeperFlashIntensity(float p_70831_1_)
{
	return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / 30;
}

public boolean isBoss()
{
	return true;
}

public boolean affectedByCommandingStaff()
{
	return false;
}

public EnumTier getTier()
{
	return EnumTier.TIER5;
}

public int getSpawnTimer()
{
	return 1;
}
/**
* Bonus damage vs mobs that implement Light
*/
public float getBonusVSLight()
{
	return 1.5F;
}

/**
* Bonus damage vs mobs that implement Armored
*/
public float getBonusVSArmored()
{
	return 2F;
}

/**
* Bonus damage vs mobs that implement Flying
*/
public float getBonusVSFlying()
{
	return 4F;
}

public boolean canBeMatedWith()
{
	return false;
}

public boolean canBeMarried()
{
	return false;
}

public boolean isChild()
{
	return false;
}

public void setChild(boolean childZombie) { }

protected void applyEntityAttributes()
{
	super.applyEntityAttributes();
	getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
	this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(3D);
	getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
	getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
	getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(30.0D);
	getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(15.0D);
	getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
}

public double getKnockbackResistance()
{
	return 1D;
}
protected void entityInit()
{
	super.entityInit();
	this.dataManager.register(FIRST_HEAD_TARGET, Integer.valueOf(0));
	this.dataManager.register(SECOND_HEAD_TARGET, Integer.valueOf(0));
	this.dataManager.register(THIRD_HEAD_TARGET, Integer.valueOf(0));
	this.dataManager.register(INVULNERABILITY_TIME, Integer.valueOf(0));
	this.dataManager.register(HOVERTIMER, Integer.valueOf(0));
	this.dataManager.register(RAMTIMER, Integer.valueOf(0));
	getDataManager().register(SPAWNEDSKELETONS, Boolean.valueOf(false));
}
public int getMaxSpawnedInChunk()
{
	return 1;
}
public void writeEntityToNBT(NBTTagCompound tagCompound)
{
	super.writeEntityToNBT(tagCompound);
	tagCompound.setBoolean("SpawnSkeletons", this.hasSpawnedSkeletons());
	tagCompound.setInteger("Invul", getInvulTime());
	tagCompound.setInteger("Hover", getHoverTime());
	tagCompound.setInteger("Ram", getRamTime());
}
public void readEntityFromNBT(NBTTagCompound tagCompund)
{
	super.readEntityFromNBT(tagCompund);
	this.setCanSpawnSkeletons(tagCompund.getBoolean("SpawnSkeletons"));
	setInvulTime(tagCompund.getInteger("Invul"));
	setHoverTime(tagCompund.getInteger("Hover"));
	setRamTime(tagCompund.getInteger("Ram"));
}
protected SoundEvent getAmbientSound()
{
	return SoundEvents.ENTITY_WITHER_AMBIENT;
}
protected SoundEvent getHurtSound(DamageSource source)
{
	return SoundEvents.ENTITY_WITHER_HURT;
}
protected SoundEvent getDeathSound()
{
	return SoundEvents.ENTITY_WITHER_DEATH;
}

protected boolean canTriggerWalking()
{
	return false;
}
public float getEyeHeight()
{
	return isSneaking() ? this.height * 0.83F - 0.1F : this.height * 0.83F;
}
public double getMountedYOffset()
{
	return this.height * 0.945D;
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
}

public void handleStopJump()
{
}public void travel(float strafe, float vertical, float forward)
	{
		if ((isBeingRidden()) && (canBeSteered()))
		{
			if (getInvulTime() > 0)
			{
				int i = getInvulTime() - 1;
				if (i <= 0)
				{
					createEngenderModExplosionFireless(this, this.posX, this.posY + getEyeHeight(), this.posZ, this.isHero() ? 35F : 7.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.world.playBroadcastSound(1023, new BlockPos(this), 0);
				}
				setInvulTime(i);
				if (this.ticksExisted % 1 == 0)
				{
					heal(1.0F);
				}
			}

			EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
			entitylivingbase.fallDistance *= 0F;
			entitylivingbase.hurtResistantTime = 40;
			entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 40, 4));
			this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
			this.rotationPitch = entitylivingbase.rotationPitch;
			setRotation(this.rotationYaw, this.rotationPitch);
			this.rotationYawHead = (this.renderYawOffset = this.rotationYaw);
			strafe = entitylivingbase.moveStrafing;
			forward = entitylivingbase.moveForward;
			this.jumpMovementFactor = 0.2F;
			if ((((EntityLivingBase)getControllingPassenger()).moveStrafing != 0.0F) && (this.ticksExisted % 10 == 0) && (!this.world.isRemote))
			{
				 Vec3d vec3 = entitylivingbase.getLook(1.0F);
				double d0 = (entitylivingbase.posX + (vec3.x * 50D));
				double d1 = (entitylivingbase.posY + entitylivingbase.getEyeHeight() + (vec3.y * 50D));
				double d2 = (entitylivingbase.posZ + (vec3.z * 50D));
				launchWitherSkullToCoords(0, d0, d1, d2, this.rand.nextFloat() < 0.001F);
			}
			if (canPassengerSteer())
			{
				setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
				super.travel(strafe, vertical, forward);
			}
			for (int i = 0; i < 256; i++)
			{
				int in = MathHelper.floor(this.posX - 1.5D + this.rand.nextDouble() * 3.0D);
				int j = MathHelper.floor(this.posY + 3.0D - this.rand.nextDouble() * 8.0D);
				int k = MathHelper.floor(this.posZ - 1.5D + this.rand.nextDouble() * 3.0D);
				BlockPos blockpos = new BlockPos(in, j, k);
				IBlockState iblockstate = this.world.getBlockState(blockpos);
				Block block = iblockstate.getBlock();
				if ((block != Blocks.AIR))
				{
					setPosition(this.posX, this.posY + 0.01D, this.posZ);
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
			for (int i = 1; i < 3; i++)
			{
				if (!this.world.isRemote && this.ticksExisted >= this.nextHeadUpdate[(i - 1)])
				{
					this.nextHeadUpdate[(i - 1)] = (this.ticksExisted + 10 + this.rand.nextInt(10));
					if ((this.world.getDifficulty() == EnumDifficulty.NORMAL) || (this.world.getDifficulty() == EnumDifficulty.HARD))
					{
						int k2 = i - 1;
						int l2 = this.idleHeadUpdates[(i - 1)];
						this.idleHeadUpdates[k2] = (this.idleHeadUpdates[(i - 1)] + 1);
						if (l2 > 15)
						{
							float f = 10.0F;
							float f1 = 5.0F;
							double d0 = MathHelper.nextDouble(this.rand, this.posX - f, this.posX + f);
							double d1 = MathHelper.nextDouble(this.rand, this.posY - f1, this.posY + f1);
							double d2 = MathHelper.nextDouble(this.rand, this.posZ - f, this.posZ + f);
							launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
							this.idleHeadUpdates[(i - 1)] = 0;
						}
					}
					int i1 = getWatchedTargetId(i);
					if (i1 > 0)
					{
						Entity entity = this.world.getEntityByID(i1);
						if ((!isSneaking()) && (entity != null) && (entity.isEntityAlive()) && (getDistanceSq(entity) <= 900.0D) && (canEntityBeSeen(entity)))
						{
							launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
							this.idleHeadUpdates[(i - 1)] = 0;
							if (this.moralRaisedTimer > 200)
							{
								this.nextHeadUpdate[(i - 1)] = (this.ticksExisted + 20 + this.rand.nextInt(10));
							} else {
									this.nextHeadUpdate[(i - 1)] = (this.ticksExisted + 40 + this.rand.nextInt(20));
								}
							}
							else {
								updateWatchedTargetId(i, 0);
							}
						}
						else
						{
							List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(20.0D, 8.0D, 20.0D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
							for (int k1 = 0; (k1 < 10) && (!list.isEmpty()); k1++)
							{
								EntityLivingBase entitylivingbase1 = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));
								if ((entitylivingbase1 != this) && (entitylivingbase1.isEntityAlive()) && (canEntityBeSeen(entitylivingbase1)) && (!isOnSameTeam(entitylivingbase1) || (entitylivingbase1 instanceof EntityFriendlyCreature && this.isOnSameTeam(entitylivingbase1) && entitylivingbase1 != this && ((EntityFriendlyCreature)entitylivingbase1).getFakeHealth() > 0F && this.getFakeHealth() > 0F)) && (entitylivingbase1 != getOwner()))
								{
									if ((entitylivingbase1 instanceof EntityPlayer))
									{
										if (!((EntityPlayer)entitylivingbase1).capabilities.disableDamage)
										{
											updateWatchedTargetId(i, entitylivingbase1.getEntityId());
										}
									}
									else {
										updateWatchedTargetId(i, entitylivingbase1.getEntityId());
									}
								}
								if ((entitylivingbase1 == this) || (!entitylivingbase1.isEntityAlive()) || (!canEntityBeSeen(entitylivingbase1)) || (isOnSameTeam(entitylivingbase1)) || (entitylivingbase1 == getOwner()))
								{
									list.remove(entitylivingbase1);
								}
							}
						}
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
			else {
				if (this.getAttackTarget() != null)
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
		}
		public void performSpecialAttack()
		{
			playLivingSound();
			playSound(ESound.witherSpecial, 10.0F, 1.0F);
			setSpecialAttackTimer(1600);
		}
		public boolean isImmuneToExplosions()
		{
			return this.getInvulTime() > 1 || super.isImmuneToExplosions();
		}

		public boolean canUseGuardBlock()
		{
			return false;
		}

		public void becomeAHero()
		{
			super.becomeAHero();
			func_82206_m();
		}

		public void onLivingUpdate()
		{
			if (!this.getCurrentBook().isEmpty())
			{
				this.idleHeadUpdates[0] = 0;
				this.idleHeadUpdates[1] = 0;
			}

			if (!this.isAIDisabled() && !world.isRemote)
			{
				if (this.isEntityAlive() && (getAttackTarget() != null) && getAttackTarget().isEntityAlive() && this.isOffensive && !this.isChild() && !this.isOnSameTeam(getAttackTarget()) && this.getDistanceSq(getAttackTarget()) < (double)((this.reachWidth * this.reachWidth) + ((getAttackTarget() instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)getAttackTarget()).reachWidth : getAttackTarget().width) * (getAttackTarget() instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)getAttackTarget()).reachWidth : getAttackTarget().width)) + 9D) && ((this.ticksExisted + this.getEntityId()) % 10 == 0))
				attackEntityAsMob(getAttackTarget());
				
				if (this.isEntityAlive())
				MobChunkLoader.updateLoaded(this);
				else
				MobChunkLoader.stopLoading(this);
			}

			this.setRamTime(this.getRamTime() - 1);
			this.setHoverTime(this.getHoverTime() - 1);
			if (this.hoverX == 0D)
			this.hoverX = this.posX;
			if (this.hoverZ == 0D)
			this.hoverZ = this.posZ;
			
			for (int i = 0; i < this.nextHeadUpdate.length; i++)
			{
				if (this.nextHeadUpdate[i] > 90)
				this.xRotationHeads[i] = func_82204_b(this.xRotationHeads[i], 90F, 15.0F);
			}

			float f2 = this.renderYawOffset * 0.017453292F;
			float f19 = MathHelper.sin(f2);
			float f3 = MathHelper.cos(f2);
			double ex = !this.onGround && !this.isBeingRidden() && !this.isInvisible() ? MathHelper.cos((float)this.ticksExisted * 0.2F) * 0.2F : 0D;
			
			this.mainHead.onUpdate();
			this.mainHead.setLocationAndAngles(this.posX, this.posY + this.getEyeHeight() - 0.4D + ex, this.posZ, 0.0F, 0.0F);
			this.rightHead.onUpdate();
			this.rightHead.setLocationAndAngles(this.posX - f3 * 1.15F, this.posY + this.getEyeHeight() - 0.7D + ex, this.posZ - f19 * 1.15F, 0.0F, 0.0F);
			this.leftHead.onUpdate();
			this.leftHead.setLocationAndAngles(this.posX + f3 * 1.15F, this.posY + this.getEyeHeight() - 0.7D + ex, this.posZ + f19 * 1.15F, 0.0F, 0.0F);
			this.rightsupport.onUpdate();
			this.rightsupport.setLocationAndAngles(this.posX - f3 * 0.5F, this.posY + this.getEyeHeight() - 0.8D + ex, this.posZ - f19 * 0.5F, 0.0F, 0.0F);
			this.leftsupport.onUpdate();
			this.leftsupport.setLocationAndAngles(this.posX + f3 * 0.5F, this.posY + this.getEyeHeight() - 0.8D + ex, this.posZ + f19 * 0.5F, 0.0F, 0.0F);
			this.spine.onUpdate();
			this.spine.setLocationAndAngles(this.posX - f19 * (-0.25F + (MathHelper.cos((float)this.ticksExisted * 0.1F + (float)Math.PI)) * 0.1F), this.posY + 0.75D + ex, this.posZ - f3 * (0.25F - (MathHelper.cos((float)this.ticksExisted * 0.1F + (float)Math.PI)) * 0.1F), 0.0F, 0.0F);
			this.lowerspine.onUpdate();
			this.lowerspine.setLocationAndAngles(this.posX - f19 * (-0.75F + (MathHelper.cos((float)this.ticksExisted * 0.1F - 1.0F + (float)Math.PI)) * 0.2F), this.posY + 0.25D + ex, this.posZ - f3 * (0.75F - (MathHelper.cos((float)this.ticksExisted * 0.1F - 1.0F + (float)Math.PI)) * 0.2F), 0.0F, 0.0F);
			this.rightRibs.onUpdate();
			this.rightRibs.setLocationAndAngles(this.spine.posX + f3 * 0.375F, this.posY + 0.95D + ex, this.spine.posZ + f19 * 0.375F, 0.0F, 0.0F);
			this.leftRibs.onUpdate();
			this.leftRibs.setLocationAndAngles(this.spine.posX - f3 * 0.375F, this.posY + 0.95D + ex, this.spine.posZ - f19 * 0.375F, 0.0F, 0.0F);
			if (!this.world.isRemote && this.getInvulTime() <= 1)
			{
				if (this.isArmored() && !this.hasSpawnedSkeletons() && this.getFakeHealth() <= 0)
				this.motionY = -(0.9D + this.motionY);
				else if (this.getWatchedTargetId(0) > 0 && getControllingPassenger() == null && !(getControllingPassenger() instanceof EntityPlayer))
				{
					Entity entity = this.world.getEntityByID(this.getWatchedTargetId(0));
					if (entity != null)
					{
						if (entity instanceof EntityWither || entity instanceof EntityCommandBlockWither)
						{
							for (int i = 0; i < 256; i++)
							{
								int in = MathHelper.floor(this.posX - 1.5D + this.rand.nextDouble() * 3.0D);
								int j = MathHelper.floor(this.posY + 3.0D - this.rand.nextDouble() * 8.0D);
								int k = MathHelper.floor(this.posZ - 1.5D + this.rand.nextDouble() * 3.0D);
								BlockPos blockpos = new BlockPos(in, j, k);
								IBlockState iblockstate = this.world.getBlockState(blockpos);
								Block block = iblockstate.getBlock();
								if ((block != Blocks.AIR))
								{
									setPosition(this.posX, this.posY + 0.01D, this.posZ);
								}
							}

							if (this.getDistance(entity) > 4D && this.getRamTime() <= 0)
							{
								this.hoverX = entity.posX;
								this.hoverZ = entity.posZ;
								if (!(entity instanceof EntityWither) && !(entity instanceof EntityCommandBlockWither) && this.posY < entity.posY)
								{
									this.onGround = false;
									this.isAirBorne = true;
									this.motionY += (0.5D - this.motionY);
								}
								if (!(entity instanceof EntityWither) && !(entity instanceof EntityCommandBlockWither) && this.posY > entity.posY)
								{
									this.onGround = false;
									this.isAirBorne = true;
									this.motionY -= (0.5D + this.motionY);
								}
							}
						}

						if ((entity instanceof EntityWither || entity instanceof EntityCommandBlockWither) && ((EntityLiving)entity).getAttackTarget() != this)
						{
							if (this.posY < entity.posY - 1D)
							{
								this.onGround = false;
								this.isAirBorne = true;
								this.motionY += (0.5D - this.motionY);
							}
							if (this.posY > entity.posY + 1D)
							{
								this.onGround = false;
								this.isAirBorne = true;
								this.motionY -= (0.5D + this.motionY);
							}
						}
						else
						{
							if (this.posY < (this.isArmored() ? entity.posY - (this.hasSpawnedSkeletons() && this.getFakeHealth() <= 0 ? 8D : 1D) : entity.posY + 4D + entity.getEyeHeight()))
							{
								this.onGround = false;
								this.isAirBorne = true;
								this.motionY += (0.5D - this.motionY);
							}
							if (this.posY > entity.posY + (this.isArmored() ? entity.posY - (this.hasSpawnedSkeletons() && this.getFakeHealth() <= 0 ? 8D : 1D) : entity.posY + 5D + entity.getEyeHeight()))
							{
								this.onGround = false;
								this.isAirBorne = true;
								this.motionY -= (0.5D + this.motionY);
							}
						}

						this.faceEntity(entity, 180F, 40F);
						if ((this.getHoverTime() > 40 && !this.isArmored()) || (this.isArmored() && this.getRamTime() < 140 && this.getRamTime() > 100))
						{
							double d0 = this.hoverX - this.posX;
							double d1 = this.hoverZ - this.posZ;
							double d3 = d0 * d0 + d1 * d1;
							if (d3 > 36D)
							{
								double d5 = MathHelper.sqrt(d3);
								if (this.moralRaisedTimer > 200 || this.isArmored() || (this.getRamTime() < 140 && this.getRamTime() > 100))
								{
									this.motionX += (d0 / d5 * 1.8D - this.motionX);
									this.motionZ += (d1 / d5 * 1.8D - this.motionZ);
								}
								else
								{
									this.motionX += (d0 / d5 * 0.6D - this.motionX);
									this.motionZ += (d1 / d5 * 0.6D - this.motionZ);
								}
							}
						}
					}
				}

				if (this.isArmored())
				{
					if (!this.hasSpawnedSkeletons() && this.getFakeHealth() <= 0 && this.isArmored() && this.onGround)
					{
						createEngenderModExplosionFireless(this, this.posX, this.posY + getEyeHeight(), this.posZ, this.isHero() ? 35F : 7.0F, this.world.getGameRules().getBoolean("mobGriefing"));
						this.world.playBroadcastSound(1023, new BlockPos(this), 0);
						for (int a = 0; a < 4; ++a)
						{
							EntitySkeleton entityliving = new EntitySkeleton(world);
							entityliving.copyLocationAndAnglesFrom(this);
							entityliving.rotationYawHead = entityliving.rotationYaw;
							entityliving.renderYawOffset = entityliving.rotationYaw;
							entityliving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityliving)), null);
							entityliving.setSkeletonType(1);
							entityliving.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
							if (entityliving.getRNG().nextInt(2) > 0)
							entityliving.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.STONE_SWORD));
							world.spawnEntity(entityliving);
							entityliving.setOwnerId(this.getOwnerId());
						}
						this.setCanSpawnSkeletons(true);
					}

					if (getRamTime() <= 0 && this.getAttackTarget() != null)
					{
						this.setRamTime(200);
						this.faceEntity(getAttackTarget(), 180F, 0F);
					}

					if (this.getRamTime() == 120)
					{
						double d1 = this.getAttackTarget() != null && this.getDistance(this.getAttackTarget()) > 24D ? this.getDistance(this.getAttackTarget()) : 24D;
						Vec3d vec3d = this.getLook(1.0F);
						this.hoverX = this.posX + vec3d.x * d1;
						this.hoverZ = this.posZ + vec3d.z * d1;
					}

					if (this.getRamTime() < 140 && this.getRamTime() > 100)
					{
						this.motionY = -0.1D;
						
						double d0 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) * 5D;
						double d1 = (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) * 2D;
						double d2 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) * 5D;
						
						List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(5D));
						
						if (!list.isEmpty())
						{
							for (int i1 = 0; i1 < list.size(); i1++)
							{
								Entity e = (Entity)list.get(i1);
								
								if (e.isEntityAlive() && e instanceof EntityLivingBase)
								{
									EntityLivingBase entity = (EntityLivingBase)e;
									
									if (!this.isOnSameTeam(entity))
									{
										double d3 = entity.posX - d0;
										double d4 = entity.posZ - d1;
										double d5 = entity.posZ - d2;
										double d6 = d3 * d3 + d4 * d4 + d5 * d5;
										this.inflictEngenderMobDamage(entity, " was rammed into by ", DamageSource.causeMobDamage(this), (float)this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() * 2F);
										entity.renderYawOffset = entity.rotationYaw = entity.rotationYawHead = (float)MathHelper.atan2(entity.motionZ, entity.motionX) * (180F / (float)Math.PI) - 90.0F;
										entity.setRevengeTarget(null);
										if (entity instanceof EntityLiving)
										((EntityLiving)entity).setAttackTarget(null);
										entity.addVelocity(d3 / d6 * 5.0D, d4 / d6 * 1.0D, d5 / d6 * 5.0D);
									}
								}
							}
						}

						if (!this.world.isRemote && (this.world.getGameRules().getBoolean("mobGriefing")))
						{
							int i11 = MathHelper.floor(this.posY);
							int l1 = MathHelper.floor(this.posX);
							int i2 = MathHelper.floor(this.posZ);
							boolean flag = false;
							for (int k2 = -2; k2 <= 2; k2++)
							{
								for (int l2 = -2; l2 <= 2; l2++)
								{
									for (int j = -1; j <= 3; j++)
									{
										int i3 = l1 + k2;
										int k = i11 + j;
										int l = i2 + l2;
										BlockPos blockpos = new BlockPos(i3, k, l);
										IBlockState iblockstate = this.world.getBlockState(blockpos);
										Block block = iblockstate.getBlock();
										if ((!block.isAir(iblockstate, this.world, blockpos)) && (EntityWither.canDestroyBlock(block)))
										{
											flag = (this.world.destroyBlock(blockpos, true)) || (flag);
										}
									}
								}
							}
							if (flag)
							{
								this.world.playEvent((EntityPlayer)null, 1022, new BlockPos(this), 0);
							}
						}
					}
				}
				else
				{
					if (!this.world.isRemote && this.getAttackTarget() != null && getHoverTime() <= 120 && getHoverTime() >= 60 && this.ticksExisted % 20 == 0)
					{
						this.attackEntityWithRangedAttack(this.getAttackTarget(), 1F);
					}

					if (this.getAttackTarget() != null && getHoverTime() <= 0)
					{
						this.setHoverTime(120);
						this.hoverX = this.getAttackTarget().posX + (rand.nextGaussian() * 10D);
						this.hoverZ = this.getAttackTarget().posZ + (rand.nextGaussian() * 10D);
					}
				}
			}

			if (isSneaking() || (this.getRamTime() < 180 && this.getRamTime() > 100))
			setSize(0.9F, 2.3F);
			else
			setSize(0.9F, 3.3F);
			
			if (this.motionY < 0.0D || (this.getAttackTarget() != null && !this.isBeingRidden()))
			{
				this.motionY *= 0.6D;
			}
			if (isSneaking() || this.getInvulTime() > 0)
			setAttackTarget(null);
			
			if (this.posY < 0)
			this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
			super.onLivingUpdate();
			for (int i = 0; i < 2; i++)
			{
				this.yRotOHeads[i] = this.yRotationHeads[i];
				this.xRotOHeads[i] = this.xRotationHeads[i];
			}
			int i;
			for (i = 0; i < 2; i++)
			{
				if (this.getAttackTarget() != null)
				headRandomTurn[i] = 0F;
				int j = getWatchedTargetId(i + 1);
				Entity entity1 = null;
				if (j > 0)
				{
					entity1 = this.world.getEntityByID(j);
				}
				if (entity1 != null)
				{
					double d1 = func_82214_u(i + 1);
					double d3 = func_82208_v(i + 1);
					double d5 = func_82213_w(i + 1);
					double d6 = entity1.posX - d1;
					double d7 = entity1.posY + entity1.getEyeHeight() - d3;
					double d8 = entity1.posZ - d5;
					double d9 = MathHelper.sqrt(d6 * d6 + d8 * d8);
					float f = (float)(Math.atan2(d8, d6) * 180.0D / 3.141592653589793D) - 90.0F;
					float f1 = (float)-(Math.atan2(d7, d9) * 180.0D / 3.141592653589793D);
					this.xRotationHeads[i] = func_82204_b(this.xRotationHeads[i], f1, 40.0F);
					this.yRotationHeads[i] = func_82204_b(this.yRotationHeads[i], f, 10.0F);
				}
				else
				{
					if ((this.ticksExisted + this.getEntityId()) % 400 == 0)
					headRandomTurn[0] = rand.nextFloat() * 360F - 180F;
					if ((this.ticksExisted + this.getEntityId()) % 400 == 0)
					headRandomTurn[1] = rand.nextFloat() * 360F - 180F;
					this.yRotationHeads[i] = func_82204_b(this.yRotationHeads[i], this.prevRenderYawOffset + (this.renderYawOffset - this.prevRenderYawOffset) + headRandomTurn[i], 2.0F);
				}
			}
			if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 512.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
			{
				performSpecialAttack();
			}
			if ((isHero()) && (getSpecialAttackTimer() == 1400))
			{
				createEngenderModExplosionFireless(this, this.posX, this.posY + getEyeHeight(), this.posZ, 7.0F, false);
				this.world.playBroadcastSound(1023, new BlockPos(this), 0);
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(128.0D, 128.0D, 128.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
				if ((list != null) && (!list.isEmpty()))
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityLivingBase entity = (EntityLivingBase)list.get(i1);
						if (entity != null)
						{
							if ((!isOnSameTeam(entity)) && (entity.isNonBoss()) && (!(entity instanceof EntityFriendlyCreature)))
							{
								entity.hurtResistantTime = 0;
								entity.attackEntityFrom(DamageSource.GENERIC, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.ANVIL, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.CACTUS, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.DRAGON_BREATH, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.DROWN, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.FALL, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.FALLING_BLOCK, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.FLY_INTO_WALL, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.IN_FIRE, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.IN_WALL, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.LAVA, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.ON_FIRE, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.STARVE, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.WITHER, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.HOT_FLOOR, Float.MAX_VALUE);
								entity.attackEntityFrom(DamageSource.FIREWORKS, Float.MAX_VALUE);
								entity.attackEntityFrom((new DamageSource("generic")).setDamageBypassesArmor().setDamageIsAbsolute(), Float.MAX_VALUE);
								if (EngenderConfig.general.useMessage && (!entity.isEntityAlive()) && (!isWild()))
								{
									entity.setDead();
									getOwner().sendMessage(new TextComponentTranslation(entity.getName() + " doesn't exist anymore...", new Object[0]));
								}
							}
							else
							{
								list.remove(entity);
							}
						}
					}
				}
			}
			if (this.world.isRemote)
			{
				for (i = 0; i < 2; i++)
				{
					this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
				}
				if ((isHero()) && (getSpecialAttackTimer() >= 1400))
				{
					for (int i1 = 0; i1 < 128; i1++)
					{
						this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 1.75D, this.posZ, this.rand.nextDouble() - 0.5D, this.rand.nextDouble() - 0.5D, this.rand.nextDouble() - 0.5D, new int[0]);
					}
				}
			}
			boolean flag = isArmored();
			for (int j = 0; j < 3; j++)
			{
				double d10 = func_82214_u(j);
				double d2 = func_82208_v(j);
				double d4 = func_82213_w(j);
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
				if ((flag) && (this.world.rand.nextInt(4) == 0))
				{
					this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
				}
			}

			if (this.isInWater())
			for (int j = 0; j < this.partArray.length; j++)
			{
				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.partArray[j].posX + (this.rand.nextGaussian() * (this.partArray[j].width / 2)), this.partArray[j].posY + (this.rand.nextDouble() * (this.partArray[j].width / 2)), this.partArray[j].posZ + (this.rand.nextGaussian() * (this.partArray[j].width / 2)), 0.0D, 0.0D, 0.0D, new int[0]);
			}
			if (getInvulTime() > 0)
			{
				for (int j = 0; j < 3; j++)
				{
					this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0D, this.posY + this.rand.nextFloat() * 3.3F, this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
				}
			}
		}
		protected void updateAITasks()
		{
			if (getInvulTime() > 0)
			{
				int i = getInvulTime() - 1;
				if (i <= 0)
				{
					createEngenderModExplosionFireless(this, this.posX, this.posY + getEyeHeight(), this.posZ, this.isHero() ? 35F : 7.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.world.playBroadcastSound(1023, new BlockPos(this), 0);
				}
				setInvulTime(i);
				if (this.ticksExisted % 1 == 0)
				{
					heal(1.0F);
				}
			}
			else
			{
				super.updateAITasks();
				
				if (this.getAttackTarget() == null && this.isArmored() && this.rand.nextInt(400) == 0)
				{
					setInvulTime(220);
					this.ticksExisted = 0;
					playLivingSound();
					playSound(ESound.createMob, 10.0F, 0.75F);
					playSound(ESound.createBossMob, 1.0E7F, 1.0F);
				}

				for (int i = 1; i < 3; i++)
				{
					if (this.ticksExisted >= this.nextHeadUpdate[(i - 1)])
					{
						this.nextHeadUpdate[(i - 1)] = (this.ticksExisted + 20 + this.rand.nextInt(20));
						if ((this.world.getDifficulty() == EnumDifficulty.HARD))
						{
							int k2 = i - 1;
							int l2 = this.idleHeadUpdates[(i - 1)];
							this.idleHeadUpdates[k2] = (this.idleHeadUpdates[(i - 1)] + 1);
							if (l2 > 15)
							{
								float f = 10.0F;
								float f1 = 5.0F;
								double d0 = MathHelper.nextDouble(this.rand, this.posX - f, this.posX + f);
								double d1 = MathHelper.nextDouble(this.rand, this.posY - f1, this.posY + f1);
								double d2 = MathHelper.nextDouble(this.rand, this.posZ - f, this.posZ + f);
								launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
								this.idleHeadUpdates[(i - 1)] = 0;
							}
						}
						int i1 = getWatchedTargetId(i);
						if (i1 > 0)
						{
							Entity entity = this.world.getEntityByID(i1);
							if ((!isSneaking()) && (entity != null) && (entity.isEntityAlive()) && (getDistanceSq(entity) <= 2000.0D) && (canEntityBeSeen(entity)))
							{
								launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
								this.idleHeadUpdates[(i - 1)] = 0;
								if (this.moralRaisedTimer > 200)
								{
									this.nextHeadUpdate[(i - 1)] = (this.ticksExisted + 20 + this.rand.nextInt(10) - (int)(this.getEntityAttribute(EntityFriendlyCreature.DEXTERITY).getBaseValue() * 0.1D));
								} else {
										this.nextHeadUpdate[(i - 1)] = (this.ticksExisted + 40 + this.rand.nextInt(20) - (int)(this.getEntityAttribute(EntityFriendlyCreature.DEXTERITY).getBaseValue() * 0.2D));
									}
								}
								else {
									updateWatchedTargetId(i, 0);
								}
							}
							else
							{
								List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(32.0D, 32.0D, 32.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
								for (int k1 = 0; (k1 < 10) && (!list.isEmpty()); k1++)
								{
									EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));
									if (entitylivingbase != this && entitylivingbase.isEntityAlive() && canEntityBeSeen(entitylivingbase) && (!this.isOnSameTeam(entitylivingbase) || (this.getAttackTarget() != null && rand.nextInt(120) == 0)))
									{
										updateWatchedTargetId(i, entitylivingbase.getEntityId());
									}
									else if (!entitylivingbase.isEntityAlive() || !canEntityBeSeen(entitylivingbase) || (this.isOnSameTeam(entitylivingbase) && rand.nextInt(80) == 0))
									{
										list.remove(entitylivingbase);
									}
								}
							}
						}
					}
					if (getAttackTarget() != null)
					{
						updateWatchedTargetId(0, getAttackTarget().getEntityId());
					}
					else
					{
						updateWatchedTargetId(0, 0);
					}
					if (this.blockBreakCounter > 0)
					{
						this.blockBreakCounter -= 1;
						if ((this.blockBreakCounter == 0) && (this.world.getGameRules().getBoolean("mobGriefing")))
						{
							int i11 = MathHelper.floor(this.posY);
							int l1 = MathHelper.floor(this.posX);
							int i2 = MathHelper.floor(this.posZ);
							boolean flag = false;
							for (int k2 = -1; k2 <= 1; k2++)
							{
								for (int l2 = -1; l2 <= 1; l2++)
								{
									for (int j = 0; j <= 3; j++)
									{
										int i3 = l1 + k2;
										int k = i11 + j;
										int l = i2 + l2;
										BlockPos blockpos = new BlockPos(i3, k, l);
										IBlockState iblockstate = this.world.getBlockState(blockpos);
										Block block = iblockstate.getBlock();
										if ((!block.isAir(iblockstate, this.world, blockpos)) && (canDestroyBlock(block)))
										{
											flag = (this.world.destroyBlock(blockpos, true)) || (flag);
										}
									}
								}
							}
							if (flag)
							{
								this.world.playEvent((EntityPlayer)null, 1022, new BlockPos(this), 0);
							}
						}
					}
					if (this.ticksExisted % 20 == 0)
					{
						heal(1.0F);
					}
				}
			}
			public static boolean canDestroyBlock(Block p_181033_0_)
			{
				return (p_181033_0_ != Blocks.BEDROCK) && (p_181033_0_ != Blocks.END_PORTAL) && (p_181033_0_ != Blocks.END_PORTAL_FRAME) && (p_181033_0_ != Blocks.COMMAND_BLOCK) && (p_181033_0_ != Blocks.REPEATING_COMMAND_BLOCK) && (p_181033_0_ != Blocks.CHAIN_COMMAND_BLOCK) && (p_181033_0_ != Blocks.BARRIER);
			}
			public void func_82206_m()
			{
				setInvulTime(220);
				setHealth(getMaxHealth() / 3.0F);
			}

			public boolean attackEntityAsMob(Entity p_70652_1_)
			{
				if (super.attackEntityAsMob(p_70652_1_))
				{
					if (p_70652_1_ instanceof EntityLivingBase)
					this.inflictCustomStatusEffect(this.world.getDifficulty(), (EntityLivingBase)p_70652_1_, MobEffects.WITHER, 10, 1);
					
					if (!p_70652_1_.isEntityAlive())
					this.heal(10F);
					else
					applyEnchantments(this, p_70652_1_);
					return true;
				}
				return false;
			}
			private double func_82214_u(int p_82214_1_)
			{
				if (p_82214_1_ <= 0)
				{
					return this.posX;
				}
				float f = (this.renderYawOffset + 180 * (p_82214_1_ - 1)) / 180.0F * 3.1415927F;
				float f1 = MathHelper.cos(f);
				return this.posX + f1 * (1.25D * this.getEntityAttribute(FITTNESS).getAttributeValue());
			}
			private double func_82208_v(int p_82208_1_)
			{
				return isSneaking() ? this.posY + (1.25D * this.getEntityAttribute(FITTNESS).getAttributeValue()) : this.posY + (2.25D * this.getEntityAttribute(FITTNESS).getAttributeValue());
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
			/**
			* Sets the Entity inside a web block.
			*/
			public void setInWeb()
			{
			}

			private double func_82213_w(int p_82213_1_)
			{
				if (p_82213_1_ <= 0)
				{
					return this.posZ;
				}
				float f = (this.renderYawOffset + 180 * (p_82213_1_ - 1)) / 180.0F * 3.1415927F;
				float f1 = MathHelper.sin(f);
				return this.posZ + f1 * (1.25D * this.getEntityAttribute(FITTNESS).getAttributeValue());
			}
			private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_)
			{
				float f3 = MathHelper.wrapDegrees(p_82204_2_ - p_82204_1_);
				if (f3 > p_82204_3_)
				{
					f3 = p_82204_3_;
				}
				if (f3 < -p_82204_3_)
				{
					f3 = -p_82204_3_;
				}
				return p_82204_1_ + f3;
			}
			private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_)
			{
				if (!this.world.isRemote && this.getPolymorphTime() > 0 && this.rand.nextBoolean())
				{
					float j = this.renderYawOffset * 0.017453292F;
					MathHelper.cos(j);
					MathHelper.sin(j);
					double d3 = func_82214_u(p_82216_1_);
					double d4 = func_82208_v(p_82216_1_);
					double d5 = func_82213_w(p_82216_1_);
					EntityInvisibleFangsProjectile entitymagicmissiles = new EntityInvisibleFangsProjectile(this.world, p_82216_2_, this, d3, d4, d5);
					this.world.spawnEntity(entitymagicmissiles);
				}
				else
				launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX + (p_82216_2_.motionX * p_82216_2_.motionX), p_82216_2_.posY + (p_82216_2_.height > 8? 7D : p_82216_2_.height * 0.5D), p_82216_2_.posZ + (p_82216_2_.motionZ * p_82216_2_.motionZ), (p_82216_1_ == 0) && !p_82216_2_.canEntityBeSeen(this));
			}
			private void launchWitherSkullToCoords(int p_82209_1_, double p_82209_2_, double p_82209_4_, double p_82209_6_, boolean p_82209_8_)
			{
				playSound(SoundEvents.ENTITY_WITHER_SHOOT, this.getSoundVolume(), 1.0F);
				double d3 = func_82214_u(p_82209_1_);
				double d4 = func_82208_v(p_82209_1_);
				double d5 = func_82213_w(p_82209_1_);
				double d6 = p_82209_2_ - d3;
				double d7 = p_82209_4_ - d4;
				double d8 = p_82209_6_ - d5;
				EntityWitherSkullOther entitywitherskull = new EntityWitherSkullOther(this.world, this, d6, d7, d8);
				if (p_82209_8_)
				{
					entitywitherskull.setInvulnerable(true);
				}
				float f = (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				entitywitherskull.damage = f;
				entitywitherskull.posY = d4;
				entitywitherskull.posX = d3;
				entitywitherskull.posZ = d5;
				if (!this.world.isRemote)
				this.world.spawnEntity(entitywitherskull);
			}
			public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
			{
				launchWitherSkullToEntity(0, p_82196_1_);
			}
			@Nullable
			protected ResourceLocation getLootTable()
			{
				return ELoot.ENTITIES_WITHER;
			}
			protected void despawnEntity()
			{
				this.idleTime = 0;
			}

			protected float getSoundVolume()
			{
				return isSneaking() ? 0.1F : 2.0F;
			}
			@SideOnly(Side.CLIENT)
			public int getBrightnessForRender()
			{
				return 15728880;
			}
			public void addPotionEffect(PotionEffect potioneffectIn)
			{
				if (!potioneffectIn.getPotion().isBadEffect())
				super.addPotionEffect(potioneffectIn);
			}

			public boolean takesFallDamage()
			{
				return false;
			}

			public boolean isEntityImmuneToCoralium()
			{
				return true;
			}

			public boolean isEntityImmuneToDread()
			{
				return true;
			}

			public boolean isEntityImmuneToAntiMatter()
			{
				return true;
			}

			@SideOnly(Side.CLIENT)
			public float getHeadYRotation(int p_82207_1_)
			{
				return this.yRotationHeads[p_82207_1_];
			}

			@SideOnly(Side.CLIENT)
			public float getHeadXRotation(int p_82210_1_)
			{
				return this.xRotationHeads[p_82210_1_];
			}

			public int getHoverTime()
			{
				return ((Integer)this.dataManager.get(HOVERTIMER)).intValue();
			}
			public void setHoverTime(int p_82215_1_)
			{
				this.dataManager.set(HOVERTIMER, Integer.valueOf(p_82215_1_));
			}

			public int getRamTime()
			{
				return ((Integer)this.dataManager.get(RAMTIMER)).intValue();
			}
			public void setRamTime(int p_82215_1_)
			{
				this.dataManager.set(RAMTIMER, Integer.valueOf(p_82215_1_));
			}

			public int getInvulTime()
			{
				return ((Integer)this.dataManager.get(INVULNERABILITY_TIME)).intValue();
			}
			public void setInvulTime(int p_82215_1_)
			{
				this.dataManager.set(INVULNERABILITY_TIME, Integer.valueOf(p_82215_1_));
			}

			public int getWatchedTargetId(int p_82203_1_)
			{
				return ((Integer)this.dataManager.get(HEAD_TARGETS[p_82203_1_])).intValue();
			}

			public void updateWatchedTargetId(int targetOffset, int newId)
			{
				this.dataManager.set(HEAD_TARGETS[targetOffset], Integer.valueOf(newId));
			}

			public boolean isArmored()
			{
				return getHealth() <= getMaxHealth() / 2F || (this.getFakeHealth() <= this.getMaxHealth() && this.getFakeHealth() > 0);
			}

			public boolean isEntityUndead()
			{
				return true;
			}
			protected SoundEvent getRegularHurtSound()
			{
				return ESound.woodHit;
			}
			protected SoundEvent getPierceHurtSound()
			{
				return ESound.woodHitPierce;
			}
			protected SoundEvent getCrushHurtSound()
			{
				return ESound.woodHitCrush;
			}

			protected void onDeathUpdate()
			{
				this.lastActiveTime = this.timeSinceIgnited;
				
				++this.deathTicks;
				this.timeSinceIgnited = this.deathTicks;
				this.setRamTime(0);
				this.setHoverTime(0);
				
				if (!this.world.isRemote && this.deathTicks == 120)
				{
					this.playSound(ESound.blast, 10F, 1F);
					createEngenderModExplosionFireless(this, this.posX, this.posY + 1.0D, this.posZ, 14F, this.world.getGameRules().getBoolean("mobGriefing"));
					List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(128D));
					this.world.playEvent(3000, getPosition(), 0);
					playSound(ESound.blast, 10F, 1F);
					if ((list != null) && (!list.isEmpty()))
					{
						for (int i = 0; i < list.size(); i++)
						{
							Entity entity = (Entity)list.get(i);
							double scale = (128D - entity.getDistance(posX, posY, posZ)) / 128D;
							
							Vec3d dir = new Vec3d(entity.posX - posX, entity.posY - posY, entity.posZ - posZ);
							dir = dir.normalize();
							if (entity instanceof EntityLivingBase)
							if (entity.isEntityAlive())
							{
								if (entity.getDistance(this) <= 16D)
								{
									entity.hurtResistantTime = 0;
									this.inflictEngenderMobDamage((EntityLivingBase)entity, " was blown up by ", DamageSource.causeExplosionDamage(this), 96);
								}
								entity.addVelocity(dir.x * 2.5D * scale, 1.5D + rand.nextDouble(), dir.z * 2.5D * scale);
							}
							else
							entity.addVelocity(dir.x * 2.5D * scale, 1.5D + rand.nextDouble(), dir.z * 2.5D * scale);
						}
					}
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

				if (!this.world.isRemote)
				{
					if (this.deathTicks == 1)
					{
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

			public boolean canBeCollidedWith()
			{
				return true;
			}

			public World getWorld()
			{
				return this.world;
			}

			public Entity[] getParts()
			{
				return this.partArray;
			}

			public int getDamageCap()
			{
				return 50;
			}

			public boolean attackEntityFrom(DamageSource source, float amount)
			{
				return this.attackEntityFromPart(mainHead, source, amount);
			}

			public boolean attackEntityFromPart(MultiPartEntityPart witherPart, DamageSource source, float damage)
			{
				if (this.isArmored())
				damage = damage / 2F + Math.min(damage, 1.0F);
				
				if (witherPart == this.spine || witherPart == this.rightRibs || witherPart == this.leftRibs)
				damage = damage / 10F + Math.min(damage, 1.0F);
				
				if (witherPart == this.lowerspine || witherPart == this.rightsupport || witherPart == this.leftsupport)
				damage = damage / 2F + Math.min(damage, 1.0F);
				
				if (witherPart == this.rightHead && !source.isExplosion())
				this.nextHeadUpdate[0] += 200;
				
				if (witherPart == this.leftHead && !source.isExplosion())
				this.nextHeadUpdate[1] += 200;
				
				if ((source.getTrueSource() != null) && ((source.getTrueSource() instanceof EntityLivingBase)))
				{
					if ((!isWild()) && (source.getTrueSource() == getOwner()))
					{
						updateWatchedTargetId(1, 0);
						updateWatchedTargetId(2, 0);
					}
				}
				if (this.isEntityInvulnerable(source) || this.getInvulTime() > 0 || damage < 0.01F || source == DamageSource.WITHER || source == DamageSource.IN_WALL || source == DamageSource.DROWN || source == DamageSource.CRAMMING || source == DamageSource.CACTUS)
				{
					return false;
				}
				else
				{
					if (this.isArmored() && source.isProjectile())
					{
						return false;
					}

					super.attackEntityFrom(source, damage);
					if (this.blockBreakCounter <= 0)
					this.blockBreakCounter = 20;
					
					for (int i = 0; i < this.idleHeadUpdates.length; ++i)
					this.idleHeadUpdates[i] += 3;
					if (!source.isMagicDamage())
					for (int i = 0; i < this.nextHeadUpdate.length; i++)
					if (this.nextHeadUpdate[i] <= 80)
					this.nextHeadUpdate[i] -= 20;
					
					return true;
				}
			}

			class AIDoNothing extends EntityAIBase
			{
				public AIDoNothing()
				{
					setMutexBits(7);
				}
				public boolean shouldExecute()
				{
					return EntityWither.this.getInvulTime() > 0;
				}
			}

			@Override
			public void setSwingingArms(boolean swingingArms) {}

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
	return 3;
}

@Override
@SideOnly(Side.CLIENT)
public SoundEvent getMusic()
{
	return isOnSameTeam(FMLClientHandler.instance().getClientPlayerEntity()) ? null : ESound.withertheme;
}
		}