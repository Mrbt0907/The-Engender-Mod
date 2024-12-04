package net.mrbt0907.ageofminecraft.entity.ai.eng;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;

public class EntityTargetLead  extends EntityAIBase
{
	private final EntityEngendered entity;
	private EntityLivingBase target;
	
	public EntityTargetLead(EntityEngendered entity)
	{
		this.entity = entity;
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (!entity.hasOwner() || !entity.getStance().equals(EnumAIStance.AGGRESSIVE)) return false;
		Entity owner = entity.getOwner();
		if (owner == null || !(owner instanceof EntityLivingBase)) return false;
		
		target = ((EntityLivingBase)owner).getLastAttackedEntity();
		
		if (entity.isOnSameTeam(target))
			target = null;
		
		return target != null;
	}
	
	@Override
	public boolean shouldContinueExecuting()
	{
		return entity.getAttackTarget() != null && target != null && target.isEntityAlive() && entity.getStance().equals(EnumAIStance.AGGRESSIVE);
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
