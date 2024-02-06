package net.minecraft.AgeOfMinecraft.entity.tier5;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityFrostRay extends EntityFireball
{
	public Entity targetEntity;
	
	public EntityFrostRay(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
		this.setNoGravity(true);
	}

	public EntityFrostRay(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		this(worldIn);
		this.shootingEntity = shooter;
		this.setLocationAndAngles(accelX, accelY, accelZ, shooter.rotationYaw, shooter.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.accelerationX = accelX;
		this.accelerationY = accelY;
		this.accelerationZ = accelZ;
	}
	public EntityFrostRay(World worldIn, Entity target, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		this(worldIn);
		this.shootingEntity = shooter;
		this.setLocationAndAngles(accelX, accelY, accelZ, shooter.rotationYaw, shooter.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.targetEntity = target;
		this.accelerationX = accelX;
		this.accelerationY = accelY;
		this.accelerationZ = accelZ;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
	}

	public EntityFrostRay(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		this(worldIn);
		this.setLocationAndAngles(accelX, accelY, accelZ, 0F, 0F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.accelerationX = accelX;
		this.accelerationY = accelY;
		this.accelerationZ = accelZ;
	}
	protected EnumParticleTypes getParticleType()
	{
		return EnumParticleTypes.END_ROD;
	}

	/**
	* Called when this EntityFireball hits a block or entity.
	*/
	protected void onImpact(RayTraceResult result)
	{
	}
	public boolean isBurning()
	{
		return false;
	}

	/**
	* Returns true if other Entities should be prevented from moving through this Entity.
	*/
	public boolean canBeCollidedWith()
	{
		return false;
	}

	/**
	* Called when the entity is attacked.
	*/
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}
	protected float getMotionFactor()
	{
		return 0.99F;
	}
	public void onUpdate()
	{
		if (!this.world.isRemote)
		{
			this.setFlag(6, this.isGlowing());
		}
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
		this.setNoGravity(true);
		this.onEntityUpdate();
		if (this.targetEntity != null && !this.targetEntity.isEntityAlive())
		this.targetEntity = null;
		if (this.ticksExisted == 1)
		{
			this.accelerationX = this.posX;
			this.accelerationY = this.posY;
			this.accelerationZ = this.posZ;
		}
		if (this.ticksExisted > 3 && targetEntity != null)
		{
			this.setLocationAndAngles(targetEntity.posX, targetEntity.posY + (targetEntity.getEyeHeight() * 0.2D), targetEntity.posZ, targetEntity.rotationYaw, targetEntity.rotationPitch);
		}
		if (this.ticksExisted > 4 && this.shootingEntity != null && targetEntity != null)
		{
			targetEntity.hurtResistantTime = 0;
			if (!this.world.isRemote && this.shootingEntity instanceof EntityFriendlyCreature)
			{
				((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage((EntityLivingBase)targetEntity, " was vaporized by ", new EntityDamageSource("indirectMagic", shootingEntity).setMagicDamage().setDamageBypassesArmor().setDamageIsAbsolute(), targetEntity.isNonBoss() ? 80F : 20F);
				((EntityFriendlyCreature)this.shootingEntity).inflictCustomStatusEffect(this.world.getDifficulty(), (EntityLivingBase)targetEntity, MobEffects.WITHER, 100, 1);
			}
			this.setDead();
		}
		if (this.ticksExisted > 2)
		{
			short short1 = (short)(int)getDistanceSq(this.accelerationX, this.accelerationY, this.accelerationZ);
			for (int i = 0; i < short1; i++)
			{
				double d9 = i / (short1 - 1.0D);
				double d6 = this.posX + (this.posX - this.accelerationX) * -d9;
				double d7 = this.posY + (this.posY - this.accelerationY) * -d9;
				double d8 = this.posZ + (this.posZ - this.accelerationZ) * -d9;
				if (this.world.isRemote)
				this.world.spawnParticle(EnumParticleTypes.SNOWBALL, true, d6, d7, d8, 0.0D, 0.025D, 0.0D, new int[0]);
			}
			this.setPosition(this.posX, this.posY, this.posZ);
		}
		else
		{
			this.motionX *= 0D;
			this.motionY *= 0D;
			this.motionZ *= 0D;
		}
		this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.025D, 0.0D, new int[0]);
		if (this.ticksExisted > 20 || (!this.world.isRemote && this.targetEntity == null))
		this.setDead();
	}
}