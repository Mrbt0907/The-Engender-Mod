package net.minecraft.AgeOfMinecraft.entity.tier6;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EntityPortal;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.Undead;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAttackRangedAlly;
import net.minecraft.AgeOfMinecraft.entity.other.BeamHitbox;
import net.minecraft.AgeOfMinecraft.entity.other.IBeamHitboxHandler;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityGhasther;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither;
import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWitherStormHead extends EntityFriendlyCreature implements IRangedAttackMob, Massive, Armored, Flying, Undead, IBeamHitboxHandler
{
	public EntityWitherStorm residentWitherStorm;
	public final BeamHitbox beam = new BeamHitbox(this, world, "witherStormBeam", 2D);
	public int openMouthCounter;
	
	public EntityWitherStormHead(World worldIn)
	{
		super(worldIn);
		isOffensive = true;
		isImmuneToFire = true;
		setSize(9.0F, 9.0F);
		tasks.addTask(0, new EntityAIAttackRangedAlly(this, 0.0D, 60, 128.0F));
		experienceValue = 500;
		setLevel(300);
		ignoreFrustumCheck = true;
		forceSpawn = true;
		((PathNavigateGround)getNavigator()).setBreakDoors(false);
		((PathNavigateGround)getNavigator()).setEnterDoors(false);
		tasks.removeTask(new EntityAIOpenDoor(this, true));
		tasks.removeTask(new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F));
		tasks.removeTask(new EntityAIWatchClosest2(this, EntityGolem.class, 3.0F, 1F));
		tasks.removeTask(new EntityAIWatchClosest2(this, net.minecraft.entity.passive.EntityVillager.class, 3.0F, 1F));
	}
	
	public boolean leavesNoCorpse()
	{
		return true;
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
	}

	/**
	* Get this Entity's EnumCreatureAttribute
	*/
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return ESetup.WITHER_STORM;
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER6;
	}
	public boolean canWearEasterEggs()
	{
		return false;
	}
	public boolean isChild()
	{
		return false;
	}
	public void setChild(boolean childZombie) { }
	
	public double getDefaultStrengthStat()
	{
		return 100F;
	}

	public double getDefaultStaminaStat()
	{
		return 100F;
	}

	public double getDefaultIntelligenceStat()
	{
		return 32F + (rand.nextFloat() * 32F);
	}

	public double getDefaultDexterityStat()
	{
		return 64F + (rand.nextFloat() * 24F);
	}

	public double getDefaultAgilityStat()
	{
		return 16F + (rand.nextFloat() * 16F);
	}

	public double getDefaultFittnessStat()
	{
		return 1F;
	}
	public void outOfWorld() { }
	public boolean canUseGuardBlock()
	{
		return false;
	}
	public float getEyeHeight()
	{
		return 5.0F;
	}
	public boolean getAlwaysRenderNameTag()
	{
		return false;
	}
	public void setInWeb() { }
	public boolean takesFallDamage()
	{
		return false;
	}
	public void fall(float distance, float damageMultiplier)
	{
		playSound(ESound.witherStormFall, 10.0F, 1.0F);
	}
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {if (residentWitherStorm == null)super.updateFallState(y, onGroundIn, state, pos);}
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

	public boolean isEntityImmuneToDarkness()
	{
		return true;
	}
	public String getName()
	{
		if (hasCustomName())
		{
			return getCustomNameTag();
		}
		return "Wither Storm Head";
	}
	public void travel(float strafe, float vertical, float forward)
	{
		if (residentWitherStorm != null)
		{
			if (isInWater())
			{
				moveRelative(strafe, vertical, forward, 0.02F);
				move(MoverType.SELF, motionX, motionY, motionZ);
				motionX *= 0.800000011920929D;
				motionY *= 0.800000011920929D;
				motionZ *= 0.800000011920929D;
			}
			else if (isInLava())
			{
				moveRelative(strafe, vertical, forward, 0.02F);
				move(MoverType.SELF, motionX, motionY, motionZ);
				motionX *= 0.5D;
				motionY *= 0.5D;
				motionZ *= 0.5D;
			}
			else
			{
				float f = 0.8F;
				moveRelative(strafe, vertical, forward, 0.02F);
				f = 0.8F;
				move(MoverType.SELF, motionX, motionY, motionZ);
				motionX *= f;
				motionY *= f;
				motionZ *= f;
			}
			prevLimbSwingAmount = limbSwingAmount;
			double d1 = posX - prevPosX;
			double d0 = posZ - prevPosZ;
			float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
			if (f2 > 1.0F)
			{
				f2 = 1.0F;
			}
			limbSwingAmount += (f2 - limbSwingAmount) * 0.4F;
			limbSwing += limbSwingAmount;
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
	protected SoundEvent getAmbientSound()
	{
		openMouthCounter = 30;
		return ESound.witherStormRoar;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return ESound.witherStormHurt;
	}
	protected SoundEvent getDeathSound()
	{
		return ESound.witherStormHurt;
	}
	protected float getSoundVolume()
	{
		return isSneaking() || residentWitherStorm == null ? 1.0F : 100.0F;
	}
	protected float getSoundPitch()
	{
		return 1.0F;
	}
	public boolean canBePushed()
	{
		return false;
	}
	protected void despawnEntity()
	{
		idleTime = 0;
	}
	public boolean isEntityUndead()
	{
		return true;
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
	public void addPotionEffect(PotionEffect p_70690_1_)
	{
		if (residentWitherStorm == null || (residentWitherStorm != null && !residentWitherStorm.isEntityAlive()))
		super.addPotionEffect(p_70690_1_);
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_WITHER_STORM_HEAD;
	}
	public void setDead()
	{
		super.setDead();
		if (residentWitherStorm != null)
		{
			if (this == residentWitherStorm.centerHead)
			{
				residentWitherStorm.centerHead = null;
			}
			if (this == residentWitherStorm.rightHead)
			{
				residentWitherStorm.rightHead = null;
			}
			if (this == residentWitherStorm.leftHead)
			{
				residentWitherStorm.leftHead = null;
			}
		}
	}
	public double getMountedYOffset()
	{
		return height * 0.75D;
	}

	public void updatePassenger(Entity passenger)
	{
		if (isPassenger(passenger))
		{
			double d8 = 6D;
			Vec3d vec3 = getLook(1.0F);
			double dx = vec3.x * d8;
			double dy = vec3.y * d8;
			double dz = vec3.z * d8;
			passenger.setPosition(posX + dx, posY + dy + getMountedYOffset(), posZ + dz);
		}
	}

	public boolean canBreatheUnderwater()
	{
		return true;
	}

	@Override
	public void onBlockCollision(BlockPos pos)
	{
		IBlockState iblockstate = world.getBlockState(pos);
		Block block = iblockstate.getBlock();
		if (!block.equals(Blocks.AIR) && world.isBlockLoaded(pos) && world.canBlockSeeSky(pos) && iblockstate.getBlockHardness(world, pos) != -1)
		{
			if (iblockstate.getMaterial().isLiquid())
				world.setBlockToAir(pos);
			else
				world.spawnEntity(new EntityFallingBlock(world, pos.getX(), pos.getY(), pos.getZ(), iblockstate));
		}
	}

	@Override
	public void onEntityCollision(Entity entity)
	{
		if (entity != residentWitherStorm && entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() > 1.0F && !isOnSameTeam(entity))
		{
			((EntityLivingBase)entity).setHealth(1.0F);
		}
	}
	
	public void onEntityUpdate()
	{
		int i = getAir();
		super.onEntityUpdate();
		if ((isEntityAlive()) && (residentWitherStorm == null))
		{
			i--;
			setAir(i);
			if (getAir() == -10)
			{
				setAir(0);
				attackEntityFrom((new DamageSource("sever")).setDamageBypassesArmor().setDamageIsAbsolute(), 10F);
			}
		}
		else
		{
			setAir(100);
		}
	}

	public void onLivingUpdate()
	{
		if (residentWitherStorm != null && residentWitherStorm.isDead)
			residentWitherStorm = null;
		
		if (ticksExisted == 25)
			ticksExisted += 40 + rand.nextInt(120);
		
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		openMouthCounter -= 1;
		
		if (world.isRemote)
		{
			
		}
		else
		{
			if (residentWitherStorm != null)
			{
				float rot = residentWitherStorm.rotationYawHead * 0.017453292F;
				float oned = MathHelper.sin(rot);
				float twod = MathHelper.cos(rot);
				
				if (isEntityAlive())
					MobChunkLoader.updateLoaded(this);
				else
					MobChunkLoader.stopLoading(this);
				
				onGround = false;
				isAirBorne = true;
				if (residentWitherStorm.hurtTime <= 0)
					residentWitherStorm.hurtTime = 10;
				if (ticksExisted % 10 == 0)
					heal(1.0F);
				
				if (residentWitherStorm.doesntContainACommandBlock())
				{
					if (residentWitherStorm.centerHead != null && this == residentWitherStorm.centerHead)
						setLocationAndAngles(residentWitherStorm.posX + oned * -7.0F, residentWitherStorm.posY, residentWitherStorm.posZ - twod * -7.0F, 0.0F, 0.0F);
					else if (residentWitherStorm.rightHead != null && this == residentWitherStorm.rightHead)
						setLocationAndAngles(residentWitherStorm.posX + twod * -7.0F + oned * -7.0F, residentWitherStorm.posY, residentWitherStorm.posZ + oned * -7.0F - twod * -7.0F, 0.0F, 0.0F);
					else if (residentWitherStorm.leftHead != null && this == residentWitherStorm.leftHead)
						setLocationAndAngles(residentWitherStorm.posX - twod * -7.0F + oned * -7.0F, residentWitherStorm.posY, residentWitherStorm.posZ - oned * -7.0F - twod * -7.0F, 0.0F, 0.0F);
				}
				else
				{
					if (residentWitherStorm.centerHead != null && this == residentWitherStorm.centerHead)
						setLocationAndAngles(residentWitherStorm.posX + oned * -7.0F, residentWitherStorm.posY, residentWitherStorm.posZ - twod * -7.0F, 0.0F, 0.0F);
					else if (residentWitherStorm.rightHead != null && this == residentWitherStorm.rightHead)
						setLocationAndAngles(residentWitherStorm.posX + twod * -20.0F + oned * -4.0F, residentWitherStorm.posY + 10.0D, residentWitherStorm.posZ + oned * -20.0F - twod * -4.0F, 0.0F, 0.0F);
					else if (residentWitherStorm.leftHead != null && this == residentWitherStorm.leftHead)
						setLocationAndAngles(residentWitherStorm.posX - twod * -20.0F + oned * -4.0F, residentWitherStorm.posY + 10.0D, residentWitherStorm.posZ - oned * -20.0F - twod * -4.0F, 0.0F, 0.0F);
				}
				
				if (isBeingRidden() && getControllingPassenger() instanceof EntityPlayer)
				{
					EntityLivingBase passenger = (EntityLivingBase)getControllingPassenger();
					passenger.fallDistance *= 0F;
					passenger.hurtResistantTime = 40;
					passenger.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 40, 4));
					renderYawOffset = rotationYaw = rotationYawHead = passenger.rotationYaw;
					rotationPitch = passenger.rotationPitch;
					setAttackTarget(null);
				}
			}
			else
			{
				MobChunkLoader.stopLoading(this);
				if (isInWater())
					motionY += 0.25D;
			}
			
			//beam.setPosition(posX, posY, posZ, rotationYaw, rotationPitch, 32.0D);
			//beam.onUpdate();
			
			EntityLivingBase target = getAttackTarget();
			if (target != null && isOnSameTeam(target))
			{
				setAttackTarget(null);
				target = null;
			}
			
			if (!isInvisible() && target != null)
			{
				faceEntity(target, 20.0F, 180.0F);
				
				if (target.getHealth() > 0.0F && !(target instanceof EntityGhasther) && !(target instanceof EntityPortal) && !(target instanceof EntityWitherStorm) && !(target instanceof EntityWitherStormHead) && !(target instanceof EntityWitherStormTentacle) && !(target instanceof EntityWitherStormTentacleDevourer))
				{
					if (target.isNonBoss())
						target.addPotionEffect(new PotionEffect(MobEffects.WITHER, Integer.MAX_VALUE, 1));
					
					double d01 = posX - target.posX;
					double d11 = posY + getEyeHeight() - target.posY;
					double d21 = posZ - target.posZ;
					float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11 + d21 * d21);
					
					if (target instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman || target instanceof net.minecraft.entity.monster.EntityEnderman)
					{
						((EntityLiving)target).setAttackTarget(residentWitherStorm != null ? residentWitherStorm : this);
						if (rand.nextInt(500) == 0)
							if (target instanceof net.minecraft.entity.monster.EntityEnderman && ((net.minecraft.entity.monster.EntityEnderman)target).getHeldBlockState() == null)
							{
								attackEntityFrom(DamageSource.causeMobDamage(target), 2500);
								((net.minecraft.entity.monster.EntityEnderman)target).setHeldBlockState(Blocks.OBSIDIAN.getDefaultState());
							}
							else if (target instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman && ((net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman)target).getHeldBlockState() == null)
							{
								attackEntityFrom(DamageSource.causeMobDamage(target), 2500);
								((net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman)target).setHeldBlockState(Blocks.OBSIDIAN.getDefaultState());
							}
					}
					else if (target instanceof EntityDragon && target.getHealth() <= 1F)
					{
						((EntityLiving)target).spawnExplosionParticle();
					}	
					else if (!target.isNonBoss() ||  target instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)target).isHero())
					{
						((EntityLiving)target).setAttackTarget(this);
					}
					else if (target instanceof EntityLiving)
					{
						((EntityLiving)target).setAttackTarget(this);
						target.motionX = (d01 / f2 * 0.5D * 0.5D + target.motionX * 0.5D);
						target.motionY = (d11 / f2 * 0.5D * 0.5D + target.motionY * 0.5D);
						target.motionZ = (d21 / f2 * 0.5D * 0.5D + target.motionZ * 0.5D);
					}
					
					int in = 1;
					if (residentWitherStorm != null)
					{
						switch (residentWitherStorm.getPhase())
						{
							case Devourer:
								in *= 2;
								break;
							case ThunderStorm:
								in *= 4;
								break;
							default:
								break;
						}
					}
					
					if (residentWitherStorm != null && getHealth() <= 0F && world.getGameRules().getBoolean("mobGriefing") == true)
						for (int i1 = 0; i1 < 1500 * in; i1++)
						{
							int i11 = MathHelper.floor(target.posY) + MathHelper.getInt(rand, 2, 3 * in) * MathHelper.getInt(rand, -1, 1);
							int l1 = MathHelper.floor(target.posX) + MathHelper.getInt(rand, 2, 3 * in) * MathHelper.getInt(rand, -1, 1);
							int i2 = MathHelper.floor(target.posZ) + MathHelper.getInt(rand, 2, 3 * in) * MathHelper.getInt(rand, -1, 1);
							BlockPos blockpos = new BlockPos(l1, i11, i2);
							IBlockState iblockstate = world.getBlockState(blockpos);
							Block block = iblockstate.getBlock();
							if (!block.equals(Blocks.AIR) && world.isBlockLoaded(blockpos) && world.canBlockSeeSky(blockpos) && iblockstate.getBlockHardness(world, new BlockPos(l1, i11, i2)) != -1)
								if (iblockstate.getMaterial().isLiquid())
									world.setBlockToAir(new BlockPos(l1, i11, i2));
								else
									world.spawnEntity(new EntityFallingBlock(world, l1, i11, i2, block.getDefaultState()));
						}
					
					if (getDistanceSq(target) < 512.0D)
					{
						openMouthCounter = 5;
						if ((ticksExisted + getEntityId()) % 10 == 0)
							attackEntityAsMob(target);
					}
					
					List<EntityLiving> list1111 = world.getEntitiesWithinAABB(EntityLiving.class, target.getEntityBoundingBox().grow(residentWitherStorm != null ? residentWitherStorm.getSize() / 25000D + 4D : 4D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
					if ((list1111 != null) && (!list1111.isEmpty()))
					{
						for (int i1 = 0; i1 < list1111.size(); i1++)
						{
							EntityLiving entity1 = (EntityLiving)list1111.get(i1);
							if ((entity1 != null) && (entity1.isEntityAlive()) && ((!isOnSameTeam(entity1)) || ((entity1 instanceof EntityAnimal))) && (!(entity1 instanceof EntityFriendlyCreature)) && (!(entity1 instanceof EntityDragon)) && (!(entity1 instanceof EntityWitherStorm)) && (!(entity1 instanceof EntityWitherStormHead)) && (!(entity1 instanceof EntityWitherStormTentacle)) && (!(entity1 instanceof EntityWitherStormTentacleDevourer)))
							{
								double d011 = target.posX - entity1.posX;
								double d111 = target.posY - entity1.posY;
								double d211 = target.posZ - entity1.posZ;
								MathHelper.sqrt(d011 * d011 + d111 * d111 + d211 * d211);
								if (((entity1 instanceof net.minecraft.entity.monster.EntityEnderman)) || (target instanceof EntityFriendlyCreature && ((target instanceof EntityWitherStorm) || (target instanceof EntityWither) || (target instanceof EntityEnderDragon) || !((EntityFriendlyCreature)target).isNonBoss() || ((EntityFriendlyCreature)target).isHero())))
								{
									((EntityLiving)entity1).setAttackTarget(this);
									if (rand.nextInt(500) == 0 && (((entity1 instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman)) || ((entity1 instanceof net.minecraft.entity.monster.EntityEnderman))))
									{
										attackEntityFrom(DamageSource.causeMobDamage(entity1), 500);
										if (entity1 instanceof net.minecraft.entity.monster.EntityEnderman && ((net.minecraft.entity.monster.EntityEnderman)entity1).getHeldBlockState() == null)
										{
											((net.minecraft.entity.monster.EntityEnderman)entity1).setHeldBlockState(Blocks.OBSIDIAN.getDefaultState());
										}
									}
								}
								else
								{
									entity1.motionX = (d01 / f2 * 0.75D * 0.75D + entity1.motionX * 0.5D);
									entity1.motionY = (d11 / f2 * 0.75D * 0.75D + entity1.motionY * 0.5D);
									entity1.motionZ = (d21 / f2 * 0.75D * 0.75D + entity1.motionZ * 0.5D);
								}
							}
						}
					}
				}
				List<EntityLiving> list11111 = world.getEntitiesWithinAABB(EntityLiving.class, getEntityBoundingBox().grow(4.0D, 4.0D, 4.0D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
				if ((list11111 != null) && (!list11111.isEmpty()))
				{
					for (int i1 = 0; i1 < list11111.size(); i1++)
					{
						EntityLiving entity1 = (EntityLiving)list11111.get(i1);
						if ((residentWitherStorm == null) && (entity1 != null) && (entity1.isEntityAlive()) && ((!isOnSameTeam(entity1)) || ((entity1 instanceof EntityAnimal))) && (!(target instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman)) && (!(target instanceof net.minecraft.entity.monster.EntityEnderman)) && (!(entity1 instanceof EntityFriendlyCreature)) && (!(entity1 instanceof EntityWitherStorm)) && (!(entity1 instanceof EntityWitherStormHead)) && (!(entity1 instanceof EntityWitherStormTentacle)) && (!(entity1 instanceof EntityWitherStormTentacleDevourer)))
						{
							attackEntityAsMob(entity1);
						}

						if ((residentWitherStorm != null) && (entity1 != null) && getDistance(entity1) <= 10D && (entity1.isEntityAlive()) && ((!isOnSameTeam(entity1)) || ((entity1 instanceof EntityAnimal))) && (!(target instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman)) && (!(target instanceof net.minecraft.entity.monster.EntityEnderman)) && (!(entity1 instanceof EntityFriendlyCreature)) && (!(entity1 instanceof EntityWitherStorm)) && (!(entity1 instanceof EntityWitherStormHead)) && (!(entity1 instanceof EntityWitherStormTentacle)) && (!(entity1 instanceof EntityWitherStormTentacleDevourer)))
						{
							if (!isWild() && EngenderConfig.general.useMessage)
							{
								getOwner().sendMessage(new TextComponentTranslation(entity1.getName() + " was eaten by The Wither Storm (" + getOwner().getName() + ")", new Object[0]));
							}
							world.setEntityState(entity1, (byte)3);
							if (!entity1.isNonBoss())
							{
								entity1.setHealth(0);
								++entity1.motionY;
								++entity1.motionY;
								++entity1.motionY;
							}
							else
							entity1.setDead();
							residentWitherStorm.Grow(residentWitherStorm.getSize() + 1 + (int)entity1.getMaxHealth() + ((int)entity1.height * (int)entity1.height) + ((int)entity1.width * (int)entity1.width));
							residentWitherStorm.heal(1 + (int)entity1.getMaxHealth() + ((int)entity1.height * (int)entity1.height) + ((int)entity1.width * (int)entity1.width));
							openMouthCounter = 2;
						}
					}
				}
			}
		}
		
		if (getHealth() <= 0.0F)
		{
			residentWitherStorm = null;
			if (world.isRemote)
			{
				float f13 = (rand.nextFloat() - 0.5F) * 9.0F;
				float f15 = (rand.nextFloat() - 0.5F) * 9.0F;
				float f17 = (rand.nextFloat() - 0.5F) * 9.0F;
				world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + f13, posY + 2.0D + f15, posZ + f17, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
		
		if (posY > 200)
			posY = 200;
		else if (posY < 0)
			posY = 0;
		super.onLivingUpdate();
	}
	
	private void launchWitherSkullToEntity(EntityLivingBase p_82216_2_)
	{
		world.playEvent((EntityPlayer)null, 1024, new BlockPos(this), 0);
		double d1 = 2.0D;
		Vec3d vec3 = getLook(1.0F);
		double d2 = p_82216_2_.posX - (posX + vec3.x);
		double d3 = p_82216_2_.posY - p_82216_2_.motionY - (posY + 2.0D);
		double d4 = p_82216_2_.posZ - (posZ + vec3.z);
		playSound(SoundEvents.ENTITY_WITHER_SHOOT, 10.0F, 0.8F);
		EntityWitherStormSkull entitylargefireball = new EntityWitherStormSkull(world, this, d2, d3, d4);
		entitylargefireball.posX = (posX + vec3.x * d1);
		entitylargefireball.posY = (posY + 2.0D);
		entitylargefireball.posZ = (posZ + vec3.z * d1);
		openMouthCounter = 5;
		
		entitylargefireball.damage = (float) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();;
		world.spawnEntity(entitylargefireball);
	}
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
	{
		if (!isInvisible() && !isOnSameTeam(p_82196_1_))
			launchWitherSkullToEntity(p_82196_1_);
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (stack.isEmpty() && getRidingEntity() == null)
		{
			if (!isWild() && isOnSameTeam(player) && !isChild() && !world.isRemote)
				player.startRiding(this);
			return true;
		}
		
		return residentWitherStorm != null ? residentWitherStorm.processInitialInteract(player, hand) : false;
	}
	
	public int getDamageCap()
	{
		return 50;
	}

	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (isEntityInvulnerable(source))
			return false;
		
		if (source.getDamageType() == "chaosImplosion" || source.getDamageType() == "de.GuardianFireball" || source.getDamageType() == "de.GuardianEnergyBall" || source.getDamageType() == "de.GuardianChaosBall")
			amount *= 0.2F;
		
		if (source.getDamageType() != "chaosImplosion" && (!source.isProjectile()) && (!source.isMagicDamage()) && (source != DamageSource.LAVA) && (source != DamageSource.ON_FIRE) && (source != DamageSource.IN_FIRE) && (source != DamageSource.IN_WALL) && (source != DamageSource.FALL) && (source != DamageSource.DROWN) && (!(source.getTrueSource() instanceof EntityWitherStormSkull)))
		{
			if (residentWitherStorm != null)
				residentWitherStorm.attackEntityFrom(source, amount * 0.3F);
			return super.attackEntityFrom(source, amount);
		}

		return false;
	}
	
	public EnumPushReaction getPushReaction()
	{
		return EnumPushReaction.IGNORE;
	}
	
	protected SoundEvent getCrushHurtSound()
	{
		return ESound.fleshHitCrushHeavy;
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {}

	
}


