package net.mrbt0907.ageofminecraft.entity.ai.eng;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;

public class EntityTargetAttacker extends EntityAIBase
{
	private final EntityEngendered entity;
	private final boolean shouldReinforce;
	private EntityLivingBase target;
	
	public EntityTargetAttacker(EntityEngendered entity, boolean shouldReinforce)
	{
		this.entity = entity;
		this.shouldReinforce = shouldReinforce;
	}
	
	@Override
	public boolean shouldExecute()
	{
		target = entity.getRevengeTarget();
		if (target != null && !entity.isOnSameTeam(target))
			return true;
		
		target = null;
		if (entity.ticksExisted % 100 != 0 || entity.getAttackTarget() != null || !shouldReinforce) return false;
		
		IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
		double followDistance = Math.pow(attribute.getAttributeValue(), 2.0D), resultDistance = followDistance, targetDistance;
		List<Entity> entities = new ArrayList<Entity>(entity.world.loadedEntityList);
		
		for(Entity entity : entities)
		{
			targetDistance = Maths.distance(this.entity.posX, this.entity.posY, this.entity.posZ, entity.posX, entity.posY, entity.posZ);
			if (!entity.equals(this.entity) && targetDistance < resultDistance && this.entity.isOnSameTeam(entity) && entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getRevengeTarget() != null && !this.entity.isOnSameTeam(((EntityLivingBase)entity).getRevengeTarget()))
			{
				resultDistance = targetDistance;
				target = (EntityLivingBase) entity;
			}
		}
		
		if (target != null)
		{
			target = target.getRevengeTarget();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting()
	{
		return entity.getAttackTarget() != null && target != null && target.isEntityAlive();
	}
	
	@Override
	public void resetTask()
	{
		entity.setAttackTarget(null);
		target = null;
	}
	
	@Override
	public void startExecuting()
	{
		entity.setAttackTarget(target);
	}
}
