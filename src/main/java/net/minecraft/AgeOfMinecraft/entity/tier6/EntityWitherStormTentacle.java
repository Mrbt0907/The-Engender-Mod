package net.minecraft.AgeOfMinecraft.entity.tier6;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.Undead;
import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWitherStormTentacle extends EntityFriendlyCreature implements Massive, Armored, Flying, Undead
{
	public EntityWitherStorm residentWitherStorm;
	public EntityWitherStormTentacle(World worldIn)
	{
		super(worldIn);
		this.isOffensive = true;
		this.isImmuneToFire = true;
		this.setSize(6.0F, 6.0F);
		this.experienceValue = 300;
		this.setLevel(300);
		this.ignoreFrustumCheck = true;
		this.forceSpawn = true;
		((PathNavigateGround)getNavigator()).setBreakDoors(false);
		((PathNavigateGround)getNavigator()).setEnterDoors(false);
		this.tasks.removeTask(new EntityAIOpenDoor(this, true));
		this.tasks.removeTask(new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F));
		this.tasks.removeTask(new EntityAIWatchClosest2(this, EntityGolem.class, 3.0F, 1F));
		this.tasks.removeTask(new EntityAIWatchClosest2(this, net.minecraft.entity.passive.EntityVillager.class, 3.0F, 1F));
	}
	
	public boolean leavesNoCorpse()
	{
		return true;
	}
	public boolean isBoss()
	{
		return true;
	}

	public void addTrackingPlayer(EntityPlayerMP player) { }
	
	public void removeTrackingPlayer(EntityPlayerMP player) { }
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		return this.residentWitherStorm != null ? this.residentWitherStorm.processInitialInteract(player, hand) : false;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(256.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(75.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
	}
	public boolean attackEntityAsMob(Entity entityIn)
	{
		if (super.attackEntityAsMob(entityIn))
		{
			entityIn.playSound(ESound.witherStormTentacleWhack, 10.0F, this.getSoundPitch() + 0.15F);
			List<EntityLivingBase> list1 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, entityIn.getEntityBoundingBox().grow(3D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
			
			if ((list1 != null) && (!list1.isEmpty()))
			{
				for (int i1 = 0; i1 < list1.size(); i1++)
				{
					EntityLivingBase entity1 = (EntityLivingBase)list1.get(i1);
					if (!this.isOnSameTeam(entity1))
					{
						super.attackEntityAsMob(entity1);
					}
				}
			}
			this.world.playEvent(3000, entityIn.getPosition(), 0);
			return true;
		}
		else
		{
			return false;
		}
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
		return 0F;
	}

	public double getDefaultDexterityStat()
	{
		return 100F;
	}

	public double getDefaultAgilityStat()
	{
		return 100F;
	}

	public double getDefaultFittnessStat()
	{
		return 1F;
	}
	public void kill() { }
	public boolean canUseGuardBlock()
	{
		return false;
	}
	public float getEyeHeight()
	{
		return 3.0F;
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
	public String getName()
	{
		if (hasCustomName())
		{
			return getCustomNameTag();
		}
		return "Wither Storm Tentacle";
	}
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {if (this.residentWitherStorm == null)super.updateFallState(y, onGroundIn, state, pos);}
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
	public void travel(float strafe, float vertical, float forward)
	{
		if (this.residentWitherStorm != null)
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
				float f = 0.8F;
				moveRelative(strafe, vertical, forward, 0.02F);
				f = 0.8F;
				move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
				this.motionX *= f;
				this.motionY *= f;
				this.motionZ *= f;
			}
			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d1 = this.posX - this.prevPosX;
			double d0 = this.posZ - this.prevPosZ;
			float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
			if (f2 > 1.0F)
			{
				f2 = 1.0F;
			}
			this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
			this.limbSwing += this.limbSwingAmount;
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
		return isSneaking() || this.residentWitherStorm == null ? 1.0F : 100.0F;
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
		this.idleTime = 0;
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
		if (this.residentWitherStorm == null || (this.residentWitherStorm != null && !this.residentWitherStorm.isEntityAlive()))
		super.addPotionEffect(p_70690_1_);
	}
	public void setDead()
	{
		super.setDead();
		if (this.residentWitherStorm != null)
		{
			if (this == this.residentWitherStorm.tentacle1)
			{
				this.residentWitherStorm.tentacle1 = null;
			}
			if (this == this.residentWitherStorm.tentacle2)
			{
				this.residentWitherStorm.tentacle2 = null;
			}
			if (this == this.residentWitherStorm.tentacle3)
			{
				this.residentWitherStorm.tentacle3 = null;
			}
			if (this == this.residentWitherStorm.tentacle4)
			{
				this.residentWitherStorm.tentacle4 = null;
			}
			if (this == this.residentWitherStorm.tentacle5)
			{
				this.residentWitherStorm.tentacle5 = null;
			}
		}
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_WITHER_STORM_TENTACLE;
	}
	public boolean canBreatheUnderwater()
	{
		return true;
	}

	public void onEntityUpdate()
	{
		int i = getAir();
		super.onEntityUpdate();
		if ((isEntityAlive()) && (this.residentWitherStorm == null))
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
		if (this.residentWitherStorm != null && !world.isRemote)
		{
			if (this.isEntityAlive())
			MobChunkLoader.updateLoaded(this);
			else
			MobChunkLoader.stopLoading(this);
		}

		if (this.residentWitherStorm != null && residentWitherStorm.isDead)
		residentWitherStorm = null;
		
		if (this.residentWitherStorm != null)
		{
			float rot = this.residentWitherStorm.rotationYawHead * 0.017453292F;
			float oned = MathHelper.sin(rot);
			float twod = MathHelper.cos(rot);
			
			if (this.residentWitherStorm.doesntContainACommandBlock())
			{
				if (this.residentWitherStorm.tentacle1 != null && this == this.residentWitherStorm.tentacle1)
				this.setLocationAndAngles(this.residentWitherStorm.posX, this.residentWitherStorm.posY - 2.0D, this.residentWitherStorm.posZ, 0.0F, 0.0F);
				if (this.residentWitherStorm.tentacle2 != null && this == this.residentWitherStorm.tentacle2)
				this.setLocationAndAngles(this.residentWitherStorm.posX + twod * 4.0F, this.posY - 1.0D, this.residentWitherStorm.posZ + oned * 4.0F, 0.0F, 0.0F);
				if (this.residentWitherStorm.tentacle3 != null && this == this.residentWitherStorm.tentacle3)
				this.setLocationAndAngles(this.residentWitherStorm.posX - twod * -2.0F, this.posY + 4.0D, this.residentWitherStorm.posZ - oned * -2.0F, 0.0F, 0.0F);
				if (this.residentWitherStorm.tentacle4 != null && this == this.residentWitherStorm.tentacle4)
				this.setLocationAndAngles(this.residentWitherStorm.posX + twod * -5.0F, this.posY + 3.0D, this.residentWitherStorm.posZ + oned * -5.0F, 0.0F, 0.0F);
				if (this.residentWitherStorm.tentacle5 != null && this == this.residentWitherStorm.tentacle5)
				this.setLocationAndAngles(this.residentWitherStorm.posX, this.residentWitherStorm.posY + 2.0D, this.residentWitherStorm.posZ, 0.0F, 0.0F);
			}
			else
			{
				if (this.residentWitherStorm.tentacle1 != null && this == this.residentWitherStorm.tentacle1)
				this.setLocationAndAngles(this.residentWitherStorm.posX, this.residentWitherStorm.posY - 12.0D, this.posZ, 0.0F, 0.0F);
				if (this.residentWitherStorm.tentacle2 != null && this == this.residentWitherStorm.tentacle2)
				this.setLocationAndAngles(this.residentWitherStorm.posX + twod * 2.0F, this.residentWitherStorm.posY - 8.0D, this.posZ + oned * 2.0F, 0.0F, 0.0F);
				if (this.residentWitherStorm.tentacle3 != null && this == this.residentWitherStorm.tentacle3)
				this.setLocationAndAngles(this.residentWitherStorm.posX - twod * -8.0F, this.residentWitherStorm.posY + 8.0D, this.posZ - oned * -8.0F, 0.0F, 0.0F);
				if (this.residentWitherStorm.tentacle4 != null && this == this.residentWitherStorm.tentacle4)
				this.setLocationAndAngles(this.residentWitherStorm.posX + twod * -5.0F, this.residentWitherStorm.posY + 4.0D, this.posZ + oned * -5.0F, 0.0F, 0.0F);
				if (this.residentWitherStorm.tentacle5 != null && this == this.residentWitherStorm.tentacle5)
				this.setLocationAndAngles(this.residentWitherStorm.posX, this.residentWitherStorm.posY + 3.0D, this.residentWitherStorm.posZ, 0.0F, 0.0F);
			}
		}

		if (this.ticksExisted == 25)
		this.ticksExisted += 40 + rand.nextInt(120);
		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		if (getHealth() <= 0.0F)
		{
			this.residentWitherStorm = null;
			float f13 = (this.rand.nextFloat() - 0.5F) * 6.0F;
			float f15 = (this.rand.nextFloat() - 0.5F) * 6.0F;
			float f17 = (this.rand.nextFloat() - 0.5F) * 6.0F;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f13, this.posY + 2.0D + f15, this.posZ + f17, 0.0D, 0.0D, 0.0D, new int[0]);
		}
		if (this.residentWitherStorm != null)
		{
			this.onGround = false;
			this.isAirBorne = true;
		}
		if ((this.residentWitherStorm != null) && (this.residentWitherStorm.hurtTime <= 0))
		{
			this.residentWitherStorm.hurtTime = 10;
		}

		if (this.ticksExisted % 10 == 0 && this.residentWitherStorm != null)
		heal(1.0F);
		
		if (this.isInWater() && this.residentWitherStorm == null)
		this.motionY += 0.25D;
		this.renderYawOffset = (this.rotationYaw = this.rotationYawHead);
		EntityLivingBase entity = getAttackTarget();
		if (!this.isInvisible() && (entity != null) && ((entity instanceof EntityLivingBase)) && (canEntityBeSeen(entity)) && (getDistanceSq(entity) < 2916.0D) && ((this.ticksExisted + this.getEntityId()) % (30 + rand.nextInt(10)) == 0))
		{
			attackEntityAsMob(entity);
		}
		if (posY > 200)
			posY = 200;
		else if (posY < 0)
			posY = 0;
		super.onLivingUpdate();
	}
	public int getDamageCap()
	{
		return 50;
	}
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source.getDamageType() == "chaosImplosion" || source.getDamageType() == "de.GuardianFireball" || source.getDamageType() == "de.GuardianEnergyBall" || source.getDamageType() == "de.GuardianChaosBall")
		amount *= 0.2F;
		
		if (isEntityInvulnerable(source))
		{
			return false;
		}
		if (source.getDamageType() != "chaosImplosion" && (!source.isProjectile()) && (!source.isMagicDamage()) && (source != DamageSource.LAVA) && (source != DamageSource.ON_FIRE) && (source != DamageSource.IN_FIRE) && (source != DamageSource.IN_WALL) && (source != DamageSource.FALL) && (source != DamageSource.DROWN) && (!(source.getTrueSource() instanceof EntityWitherStormSkull)))
		{
			if (this.residentWitherStorm != null)
			{
				this.residentWitherStorm.attackEntityFrom(source, amount * 0.3F);
			}
			return super.attackEntityFrom(source, amount);
		}

		return false;
	}
	protected SoundEvent getCrushHurtSound()
	{
		return ESound.fleshHitCrushHeavy;
	}
}


