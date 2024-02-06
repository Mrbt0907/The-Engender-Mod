package net.minecraft.AgeOfMinecraft.entity.tier5;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntitySnowballHarmful extends EntitySnowball
{
	 public float damage = 0F;
	public EntitySnowballHarmful(World worldIn)
	{
		super(worldIn);
	}

	public EntitySnowballHarmful(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public EntitySnowballHarmful(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 3)
		{
			for (int i = 0; i < 16; ++i)
			{
				this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}

	/**
	* Called when this EntityThrowable hits a block or entity.
	*/
	protected void onImpact(RayTraceResult result)
	{
		if (!this.world.isRemote && this.getThrower() == null)
		{
			this.setDead();
			return;
		}
		if (!this.world.isRemote && result.entityHit != null)
		{
			if (result.entityHit instanceof EntityLivingBase && this.getThrower() != null && this.getThrower() instanceof EntityFriendlyCreature && !((EntityFriendlyCreature)this.getThrower()).isOnSameTeam((EntityLivingBase)result.entityHit) || (result.entityHit instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)this.getThrower()).isOnSameTeam((EntityFriendlyCreature)result.entityHit) && ((EntityFriendlyCreature)this.getThrower()).getFakeHealth() > 0F))
			{
				((EntityLivingBase)result.entityHit).hurtResistantTime = 0;
				((EntityFriendlyCreature)this.getThrower()).inflictEngenderMobDamage((EntityLivingBase)result.entityHit, " was pummeled by ", DamageSource.causeThrownDamage(this, this.getThrower()), damage);
				this.world.setEntityState(this, (byte)3);
				this.setDead();
			}
		}
		if (!this.world.isRemote && result.entityHit == null)
		{
			this.world.setEntityState(this, (byte)3);
			this.setDead();
		}
	}
	public void onUpdate()
	{
		super.onUpdate();
		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
		if (!list.isEmpty())
		for (EntityLivingBase entity1 : list)
		if (this.getThrower() != null && entity1 instanceof IEntityMultiPart && entity1 != null && entity1.isEntityAlive() && !((EntityFriendlyCreature)this.getThrower()).isOnSameTeam(entity1))this.onImpact(new RayTraceResult(entity1));
	}
}