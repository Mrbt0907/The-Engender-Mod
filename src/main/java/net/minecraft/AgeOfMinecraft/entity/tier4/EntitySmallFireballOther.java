package net.minecraft.AgeOfMinecraft.entity.tier4;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySmallFireballOther extends EntitySmallFireball
{
	public float damage = 6.0F;
	public EntitySmallFireballOther(World worldIn)
	{
		super(worldIn);
		setSize(0.3125F, 0.3125F);
	}
	public EntitySmallFireballOther(World worldIn, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_)
	{
		super(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
		setSize(0.3125F, 0.3125F);
	}
	public EntitySmallFireballOther(World worldIn, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_)
	{
		super(worldIn, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
		setSize(0.3125F, 0.3125F);
	}
	protected void onImpact(RayTraceResult movingObject)
	{
		if (!this.world.isRemote)
		{
			if (this.shootingEntity != null)
			{
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
				if ((list != null) && (!list.isEmpty()))
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityLivingBase entity1 = (EntityLivingBase)list.get(i1);
						if (entity1 != null && entity1 instanceof IEntityMultiPart) {this.shootingEntity.attackEntityAsMob(entity1);
						applyEnchantments(this.shootingEntity, entity1);
						setDead();
					}
				}
			}
		}
		if (movingObject.entityHit != null && movingObject.entityHit.hurtResistantTime <= 5 && movingObject.entityHit.isEntityAlive())
		{
			if (this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature && movingObject.entityHit instanceof EntityLivingBase)
			{
				if (!movingObject.entityHit.isImmuneToFire() && movingObject.entityHit instanceof EntityAnimal)
				movingObject.entityHit.setFire(10);
				
				if (!((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam((EntityLivingBase)movingObject.entityHit) || (movingObject.entityHit instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam((EntityFriendlyCreature)movingObject.entityHit) && ((EntityFriendlyCreature)this.shootingEntity).getFakeHealth() > 0F))
				{
					this.copyLocationAndAnglesFrom(movingObject.entityHit);
					((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage((EntityLivingBase)movingObject.entityHit, " was fireballed by ", DamageSource.causeFireballDamage(this, shootingEntity), damage);
					applyEnchantments(((EntityFriendlyCreature)this.shootingEntity), movingObject.entityHit);
					if (!movingObject.entityHit.isImmuneToFire())
					movingObject.entityHit.setFire(10);
					if (this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)this.shootingEntity).isHero())
					EntityFriendlyCreature.createEngenderModExplosion((EntityFriendlyCreature)this.shootingEntity, this.posX, this.posY, this.posZ, 1, this.world.getGameRules().getBoolean("mobGriefing"), false);
					setDead();
				}
			}
		}
		else if (movingObject.entityHit == null)
		{
			boolean flag1 = true;
			if ((this.shootingEntity != null) && ((this.shootingEntity instanceof EntityLiving)))
			{
				flag1 = this.world.getGameRules().getBoolean("mobGriefing");
			}
			if (flag1)
			{
				BlockPos blockpos = movingObject.getBlockPos().offset(movingObject.sideHit);
				if (this.world.isAirBlock(blockpos))
				{
					this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
					if (this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)this.shootingEntity).isHero())
					EntityFriendlyCreature.createEngenderModExplosion((EntityFriendlyCreature)this.shootingEntity, this.posX, this.posY, this.posZ, 1, this.world.getGameRules().getBoolean("mobGriefing"), false);
					setDead();
				}
			}
		}
	}

}
public boolean canBeCollidedWith()
{
	return false;
}
public boolean isImmuneToExplosions()
{
	return true;
}
public boolean attackEntityFrom(DamageSource source, float amount)
{
	return false;
}
public void onUpdate()
{
	super.onUpdate();
	List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
	if (!list.isEmpty())
	for (EntityLivingBase entity1 : list)
	if (this.shootingEntity != null && entity1 instanceof IEntityMultiPart && entity1 != null && entity1.isEntityAlive() && !((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam(entity1))this.onImpact(new RayTraceResult(entity1));
}
}