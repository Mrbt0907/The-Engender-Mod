package net.mrbt0907.ageofminecraft.entity.ai.eng;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;

public class EntityAIOpenDoor extends EntityAIDoorInteract
{
	private EntityEngendered smartEntity;
	private int closeDoorTemporisation;
	private boolean canOpenDoor;
	
	public EntityAIOpenDoor(EntityEngendered entity)
	{
		super(entity);
		smartEntity = entity;
	}
	
	@Override
	public boolean shouldExecute()
    {
		boolean truth = super.shouldExecute();
		if (truth)
			canOpenDoor = smartEntity.getIntelligence() > 29L;
		return truth && canOpenDoor;
    }
	
	@Override
	public boolean shouldContinueExecuting()
	{
		return canOpenDoor && closeDoorTemporisation > 0 && super.shouldContinueExecuting();
	}

	@Override
	public void startExecuting()
	{
		closeDoorTemporisation = 20;
		doorBlock.toggleDoor(entity.world, doorPosition, true);
	}
	
	@Override
	public void resetTask()
	{
		if (canOpenDoor)
		{
			doorBlock.toggleDoor(entity.world, doorPosition, false);
			canOpenDoor = false;
		}
	}

	@Override
	public void updateTask()
	{
		--closeDoorTemporisation;
		super.updateTask();
	}
}