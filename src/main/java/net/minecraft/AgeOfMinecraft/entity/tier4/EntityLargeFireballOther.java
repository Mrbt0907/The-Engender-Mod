package net.minecraft.AgeOfMinecraft.entity.tier4;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityLargeFireballOther extends EntityLargeFireball
{
	public float damage = 17.0F;
	public EntityLargeFireballOther(World worldIn)
	{
		super(worldIn);
	}
	@SideOnly(Side.CLIENT)
	public EntityLargeFireballOther(World worldIn, double p_i1768_2_, double p_i1768_4_, double p_i1768_6_, double p_i1768_8_, double p_i1768_10_, double p_i1768_12_)
	{
		super(worldIn, p_i1768_2_, p_i1768_4_, p_i1768_6_, p_i1768_8_, p_i1768_10_, p_i1768_12_);
	}
	public EntityLargeFireballOther(World worldIn, EntityLivingBase p_i1769_2_, double p_i1769_3_, double p_i1769_5_, double p_i1769_7_)
	{
		super(worldIn, p_i1769_2_, p_i1769_3_, p_i1769_5_, p_i1769_7_);
	}
	public boolean isImmuneToExplosions()
	{
		return true;
	}
	protected void onImpact(RayTraceResult movingObject)
	{
		if (!this.world.isRemote)
		{
			if (movingObject.entityHit != null && !(movingObject.entityHit instanceof EntityLargeFireballOther))
			{
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature && movingObject.entityHit instanceof EntityLivingBase)
				{
					if (!movingObject.entityHit.isImmuneToFire() && movingObject.entityHit instanceof EntityAnimal)
					movingObject.entityHit.setFire(30);
					
					if (!((EntityFriendlyCreature)this.shootingEntity).isWild() && movingObject.entityHit instanceof net.minecraft.entity.monster.EntityGhast)
					{
						damage = 1000F;
					}

					if (!((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam((EntityLivingBase)movingObject.entityHit) || (movingObject.entityHit instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam((EntityFriendlyCreature)movingObject.entityHit) && ((EntityFriendlyCreature)this.shootingEntity).getFakeHealth() > 0F))
					{
						this.copyLocationAndAnglesFrom(movingObject.entityHit);
						((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage((EntityLivingBase)movingObject.entityHit, " was fireballed by ", DamageSource.causeFireballDamage(this, shootingEntity), damage);
						applyEnchantments(((EntityFriendlyCreature)this.shootingEntity), movingObject.entityHit);
						boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
						EntityFriendlyCreature.createEngenderModExplosion((EntityFriendlyCreature)this.shootingEntity, this.posX, this.posY, this.posZ, this.explosionPower, flag, flag);
						if (!movingObject.entityHit.isImmuneToFire())
						movingObject.entityHit.setFire(30);
						setDead();
					}
				}
			}
			else if (movingObject.entityHit == null)
			{
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature)
				EntityFriendlyCreature.createEngenderModExplosion((EntityFriendlyCreature)this.shootingEntity, this.posX, this.posY, this.posZ, this.explosionPower, false, false);
				setDead();
			}
		}
	}

	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	public void onUpdate()
	{
		super.onUpdate();
		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(2D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
		if (!list.isEmpty())
		for (EntityLivingBase entity1 : list)
		if (this.shootingEntity != null && entity1 instanceof IEntityMultiPart && entity1 != null && entity1.isEntityAlive() && !((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam(entity1))this.onImpact(new RayTraceResult(entity1));
	}
}