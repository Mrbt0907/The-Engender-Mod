package net.minecraft.AgeOfMinecraft.entity.tier4;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityShulkerBulletFriendly
extends EntityShulkerBullet
{
	private EntityFriendlyCreature field_184570_a;
	private Entity target;
	public EntityShulkerBulletFriendly(World worldIn)
	{
		super(worldIn);
	}
	public SoundCategory getSoundCategory()
	{
		return SoundCategory.MASTER;
	}
	@SideOnly(Side.CLIENT)
	public EntityShulkerBulletFriendly(World worldIn, double x, double y, double z, double motionXIn, double motionYIn, double motionZIn)
	{
		super(worldIn, x, y, z, motionXIn, motionYIn, motionZIn);
	}
	public EntityShulkerBulletFriendly(World worldIn, EntityFriendlyCreature p_i46772_2_, Entity p_i46772_3_, EnumFacing.Axis p_i46772_4_)
	{
		super(worldIn, p_i46772_2_, p_i46772_3_, p_i46772_4_);
		this.field_184570_a = p_i46772_2_;
		this.target = p_i46772_3_;
	}
	public void setDead()
	{
		if (!this.field_184570_a.getHeldItemMainhand().isEmpty())
		{
			EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(this.world, posX, posY, posZ, this.field_184570_a.getHeldItemMainhand());
			((WorldServer)this.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 2, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
			playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
			if (!this.world.isRemote)
			world.spawnEntity(entityfireworkrocket);
			if (this.target != null && this.target instanceof EntityLivingBase && ((EntityLivingBase)this.target).getHealth() <= 5F)
			this.target.startRiding(entityfireworkrocket);
		}
		super.setDead();
	}

	public boolean isImmuneToExplosions()
	{
		return true;
	}
	public void onUpdate()
	{
		super.onUpdate();
		if ((this.field_184570_a != null) && (this.field_184570_a.moralRaisedTimer > 200))
		{
			this.motionX *= 1.100000023841858D;
			this.motionY *= 1.100000023841858D;
			this.motionZ *= 1.100000023841858D;
		}
		if (this.target == null)
		{
			((WorldServer)this.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 2, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
			playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
			if (!this.world.isRemote)
			setDead();
		}
		else
		{
			if (!this.target.isEntityAlive())
			{
				this.setDead();
			}

			if (this.target instanceof IEntityMultiPart && this.target instanceof EntityLivingBase)
			{
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
				if (!list.isEmpty())
				for (EntityLivingBase entity1 : list)
				if (entity1 instanceof IEntityMultiPart && entity1 != null && entity1.isEntityAlive()){this.field_184570_a.attackEntityAsMob(this.target);
				applyEnchantments(this.field_184570_a, this.target);
				playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
				if (!this.world.isRemote)
				setDead();
			}
		}
	}
}
protected void func_184567_a(RayTraceResult result)
{
	if ((result.entityHit != null) && (this.target != null) && (result.entityHit == this.target) && result.entityHit instanceof EntityLivingBase)
	{
		if ((!this.field_184570_a.isOnSameTeam((EntityLivingBase)result.entityHit) || (field_184570_a instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker && ((net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker)field_184570_a).isPositiveShulker() && this.field_184570_a.isOnSameTeam((EntityLivingBase)result.entityHit))) && (this.field_184570_a != null))
		{
			field_184570_a.inflictEngenderMobDamage((EntityLivingBase)result.entityHit, " was shot by ", DamageSource.causeThrownDamage(this, field_184570_a), (float)field_184570_a.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue());
			((net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker)field_184570_a).inflictShulkerEffects((EntityLivingBase)result.entityHit);
			this.applyEnchantments(this.field_184570_a, result.entityHit);
			((WorldServer)this.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 2, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
			playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
			if (!this.world.isRemote)
			setDead();
		}
	}
}
}


