package net.mrbt0907.ageofminecraft.entity.ai.eng;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;

public class EntityAIAttack extends EntityAIBase
{
	private final EntityEngendered entity;
	private final double speedMultiplier;
	private final PathNavigate pathFinder;
	private EntityLivingBase target;
	
	public EntityAIAttack(EntityEngendered entity, double speedMultiplier)
	{
		this.entity = entity;
		this.speedMultiplier = speedMultiplier;
		pathFinder = entity.getNavigator();
		setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute()
	{
		target = entity.getAttackTarget();
		return target != null;
	}
	
	@Override
	public boolean shouldContinueExecuting()
    {
		return target != null && target.isEntityAlive();
    }
	
	@Override
	public void startExecuting()
    {
		
    }
}
