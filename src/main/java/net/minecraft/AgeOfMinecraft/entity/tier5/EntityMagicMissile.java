package net.minecraft.AgeOfMinecraft.entity.tier5;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityMagicMissile extends EntityFireball
{
	public Entity targetEntity;
	
	public EntityMagicMissile(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
	}

	public EntityMagicMissile(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
	}
	public EntityMagicMissile(World worldIn, Entity target, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		this(worldIn, shooter, accelX, accelY, accelZ);
		this.targetEntity = target;
		this.playSound(ESound.magicMissileFire, 1F, 1F);
		this.accelerationX = 0D;
		this.accelerationY = 0D;
		this.accelerationZ = 0D;
	}

	public EntityMagicMissile(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		super(worldIn, x, y, z, accelX, accelY, accelZ);
		this.setSize(0.25F, 0.25F);
		this.noClip = true;
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
		if (this.ticksExisted > 40 && !this.world.isRemote && this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature && result.entityHit != null && result.entityHit.hurtResistantTime <= 0 && result.entityHit instanceof EntityLivingBase)
		{
			if (!((EntityFriendlyCreature)shootingEntity).isOnSameTeam((EntityLivingBase)result.entityHit))
			{
				((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage((EntityLivingBase)targetEntity, " was shot by ", new EntityDamageSourceIndirect("arrow", this, (EntityFriendlyCreature)this.shootingEntity).setMagicDamage().setProjectile(), 2F);
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 0D, 0D, 0D, new int[0]);
				this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.5F);
				this.setDead();
				((EntityLivingBase)targetEntity).hurtResistantTime = 0;
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
		return this.ticksExisted > 90 ? 0.9F : this.ticksExisted / 100F;
	}
	public void onUpdate()
	{
		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		if (this.shootingEntity != null)
		{
			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(0.5D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
			if ((list != null) && (!list.isEmpty()))
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityLivingBase entity1 = (EntityLivingBase)list.get(i1);
					if (entity1 != null && entity1.isEntityAlive() && targetEntity != null && entity1 == targetEntity){entity1.hurtResistantTime = 0;
					this.onImpact(new RayTraceResult(entity1));
				}
			}
		}
	}
	if (this.targetEntity != null && !this.targetEntity.isEntityAlive())
	this.targetEntity = null;
	
	this.world.spawnParticle(EnumParticleTypes.END_ROD, this.posX, this.posY + 0.125D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
	
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
	if (this.ticksExisted > 20 && this.targetEntity != null)
	{
		double d0 = this.targetEntity.posX - this.posX;
		double d1 = this.targetEntity.posY - this.posY;
		double d2 = this.targetEntity.posZ - this.posZ;
		float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
		this.motionX = d0 / (double)f2 * (this.getMotionFactor() * this.getMotionFactor()) * (this.getMotionFactor() * this.getMotionFactor()) + this.motionX * (this.getMotionFactor() * this.getMotionFactor());
		this.motionY = d1 / (double)f2 * (this.getMotionFactor() * this.getMotionFactor()) * (this.getMotionFactor() * this.getMotionFactor()) + this.motionY * (this.getMotionFactor() * this.getMotionFactor());
		this.motionZ = d2 / (double)f2 * (this.getMotionFactor() * this.getMotionFactor()) * (this.getMotionFactor() * this.getMotionFactor()) + this.motionZ * (this.getMotionFactor() * this.getMotionFactor());
	}
	this.setPosition(this.posX, this.posY, this.posZ);
	if (!this.world.isRemote && this.ticksExisted > 20 && this.targetEntity == null)
	{
		if ((this.targetEntity == null || (this.targetEntity != null && !this.targetEntity.isEntityAlive())) && this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature)
		{
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(100D));
			if (entities != null && !entities.isEmpty())
			{
				for (int i = 0; i < entities.size(); i++)
				{
					EntityLivingBase entity = (EntityLivingBase)entities.get(rand.nextInt(entities.size()));
					if (entity.isEntityAlive() && entity instanceof EntityLivingBase && !((EntityFriendlyCreature)shootingEntity).isOnSameTeam((EntityLivingBase)entity))
					this.targetEntity = entity;
				}
			}
			else
			{
				this.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.5F);
				this.setDead();
			}
		}
	}
}
}