package net.minecraft.AgeOfMinecraft.entity.tier5;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityInvisibleFangsProjectile extends EntitySmallFireball
{
	public Entity targetEntity;
	
	public EntityInvisibleFangsProjectile(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
		this.setInvisible(true);
	}

	public EntityInvisibleFangsProjectile(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
		this.setInvisible(true);
	}
	public EntityInvisibleFangsProjectile(World worldIn, Entity target, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		this(worldIn, shooter, accelX, accelY, accelZ);
		this.targetEntity = target;
		this.accelerationX = 0D;
		this.accelerationY = 0D;
		this.accelerationZ = 0D;
		this.setInvisible(true);
	}

	public EntityInvisibleFangsProjectile(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		super(worldIn, x, y, z, accelX, accelY, accelZ);
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
		this.setInvisible(true);
	}
	protected EnumParticleTypes getParticleType()
	{
		return EnumParticleTypes.SUSPENDED_DEPTH;
	}

	/**
	* Called when this EntityFireball hits a block or entity.
	*/
	protected void onImpact(RayTraceResult result)
	{
		if (this.ticksExisted > 40 && !this.world.isRemote && this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature && result.entityHit != null && result.entityHit instanceof EntityLivingBase)
		{
			if (!((EntityFriendlyCreature)shootingEntity).isOnSameTeam(result.entityHit))
			{
				((EntityFriendlyCreature)this.shootingEntity).attackEntityAsMob(targetEntity);
				this.setDead();
			}
		}
	}
	protected boolean isFireballFiery()
	{
		return false;
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
		return 0.85F;
	}
	public void onUpdate()
	{
		super.onUpdate();
		
		this.setInvisible(true);
		if (!this.world.isRemote && this.shootingEntity != null && this.targetEntity != null && this.ticksExisted > 2)
		{
			EntityEvokerFangOther entityevokerfangs = new EntityEvokerFangOther(this.world, this.posX, this.posY, this.posZ, (float)MathHelper.atan2(this.targetEntity.posZ - this.posZ, this.targetEntity.posX - this.posX), 5, this.shootingEntity != null ? this.shootingEntity : null);
			this.world.spawnEntity(entityevokerfangs);
		}
		if (this.shootingEntity != null)
		{
			List<EntityLivingBase> list1 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox(), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
			if ((list1 != null) && (!list1.isEmpty()))
			{
				for (int i1 = 0; i1 < list1.size(); i1++)
				{
					EntityLivingBase entity1 = (EntityLivingBase)list1.get(i1);
					if (this.getDistanceSq(entity1) < 0.1D && entity1 != null && entity1.isEntityAlive() && entity1 == this.targetEntity)
					{
						this.setDead();
					}
				}
			}
		}
		if (this.targetEntity != null && !this.targetEntity.isEntityAlive())
		this.targetEntity = null;
		
		if (this.motionX > getMotionFactor())
		this.motionX = getMotionFactor();
		if (this.motionY > getMotionFactor())
		this.motionY = getMotionFactor();
		if (this.motionZ > getMotionFactor())
		this.motionZ = getMotionFactor();
		if (this.motionX < -getMotionFactor())
		this.motionX = -getMotionFactor();
		if (this.motionY < -getMotionFactor())
		this.motionY = -getMotionFactor();
		if (this.motionZ < -getMotionFactor())
		this.motionZ = -getMotionFactor();
		if (this.targetEntity != null)
		{
			double d0 = this.targetEntity.posX - this.posX;
			double d1 = this.targetEntity.posY - this.posY;
			double d2 = this.targetEntity.posZ - this.posZ;
			float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
			this.motionX = d0 / (double)f2 * (this.getMotionFactor() * this.getMotionFactor()) * (this.getMotionFactor() * this.getMotionFactor()) + this.motionX * (this.getMotionFactor() * this.getMotionFactor());
			this.motionY = d1 / (double)f2 * (this.getMotionFactor() * this.getMotionFactor()) * (this.getMotionFactor() * this.getMotionFactor()) + this.motionY * (this.getMotionFactor() * this.getMotionFactor());
			this.motionZ = d2 / (double)f2 * (this.getMotionFactor() * this.getMotionFactor()) * (this.getMotionFactor() * this.getMotionFactor()) + this.motionZ * (this.getMotionFactor() * this.getMotionFactor());
		}
		if (!this.world.isRemote && this.targetEntity == null)
		{
			this.setDead();
		}
	}
}