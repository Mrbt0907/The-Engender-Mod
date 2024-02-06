package net.minecraft.AgeOfMinecraft.entity;

import java.util.List;

import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityPortalLightning extends EntityFireball
{
	public Entity targetEntity;
	
	public EntityPortalLightning(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
		this.setNoGravity(true);
	}

	public EntityPortalLightning(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		this(worldIn);
		this.shootingEntity = shooter;
		this.setLocationAndAngles(accelX, accelY, accelZ, shooter.rotationYaw, shooter.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.accelerationX = accelX;
		this.accelerationY = accelY;
		this.accelerationZ = accelZ;
	}
	public EntityPortalLightning(World worldIn, Entity target, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
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

	public EntityPortalLightning(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
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
			if (!this.isDead)
			MobChunkLoader.updateLoaded(this);
			else
			MobChunkLoader.stopLoading(this);
		}
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
		this.setNoGravity(true);
		this.onEntityUpdate();
		if (this.targetEntity != null && !this.targetEntity.isEntityAlive())
		this.targetEntity = null;
		this.motionX *= 0D;
		this.motionY *= 0D;
		this.motionZ *= 0D;
		if (this.ticksExisted == 1)
		{
			this.accelerationX = this.posX;
			this.accelerationY = this.posY;
			this.accelerationZ = this.posZ;
			this.setPosition(this.posX, this.posY, this.posZ);
		}
		if (this.ticksExisted > 1 && targetEntity != null)
		{
			this.setLocationAndAngles(targetEntity.posX, targetEntity.posY + (targetEntity.getEyeHeight() * 0.2D), targetEntity.posZ, targetEntity.rotationYaw, targetEntity.rotationPitch);
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
				this.world.spawnParticle(EnumParticleTypes.END_ROD, true, d6, d7, d8, 0.0D, 0.01D, 0.0D, new int[0]);
			}
		}
		if (this.ticksExisted > 5 && this.shootingEntity != null && targetEntity != null && this.getDistance(targetEntity) <= 2D)
		{
			this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, ESound.lightningShot, SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
			targetEntity.onStruckByLightning(null);
			targetEntity.hurtResistantTime = 0;
			if (this.shootingEntity instanceof EntityFriendlyCreature)
			{
				this.world.addWeatherEffect(new EntityLightningBolt(this.world, accelerationX - 0.5D, accelerationY, accelerationZ - 0.5D, true));
				List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.getPosition()).grow(3D));
				for(Entity entity : entities)
				{
					if(entity instanceof EntityLivingBase)
					{
						((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage((EntityLivingBase)targetEntity, " was electrocuted by ", new EntityDamageSource("indirectMagic", shootingEntity).setMagicDamage().setDamageBypassesArmor().setDamageIsAbsolute(), 10F);
						if(entity.isEntityAlive())
						((EntityFriendlyCreature)this.shootingEntity).attackEntityAsMob(targetEntity);
					}
				}
				this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
				this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
				if ((targetEntity instanceof EntityLivingBase && !(targetEntity instanceof EntityPlayer) && !(targetEntity instanceof EntityLiving)))
				{
					++targetEntity.motionY;
					if ((targetEntity instanceof EntityLivingBase))
					{
						if (!targetEntity.isEntityAlive() && !targetEntity.isDead)
						this.shootingEntity.onKillEntity((EntityLivingBase) targetEntity);
					}
					targetEntity.setFire(100);
				}
			}
			this.setDead();
		}
		if (this.ticksExisted > 20 || (!this.world.isRemote && this.targetEntity == null))
		this.setDead();
	}
}