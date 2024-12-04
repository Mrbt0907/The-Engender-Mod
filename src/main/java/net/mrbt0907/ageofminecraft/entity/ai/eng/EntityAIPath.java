package net.mrbt0907.ageofminecraft.entity.ai.eng;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;

public class EntityAIPath  extends EntityAIBase
{
	private final EntityEngendered entity;
	private final double speedMultiplier;
	private final PathNavigate pathFinder;
	
	private int timeToRecalcPath;
	private int index;
	private int ticks;
	
	public EntityAIPath(EntityEngendered entity, double speedMultiplier)
	{
		this.entity = entity;
		this.speedMultiplier = speedMultiplier;
		pathFinder = entity.getNavigator();
		setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute()
	{
		return !entity.getLeashed() && entity.followPos != null && entity.followPos.length > 0 && entity.getAttackTarget() == null;
	}
	
	public void startExecuting()
	{
		timeToRecalcPath = 0;
	}
	
	public void resetTask()
	{
		index = 0;
		ticks = 0;
		pathFinder.clearPath();
	}
	
	public void updateTask()
	{
		if (--timeToRecalcPath <= 0 && entity.followPos != null && entity.followPos.length > 0)
		{
			if (index > entity.followPos.length)
				index = 0;
			try
			{
				BlockPos pos = entity.followPos[index];
				double speed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * speedMultiplier;
				timeToRecalcPath = 10;
				pathFinder.tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), speed);
				
				if (ticks <= 0 && Maths.distance(entity.posX, entity.posY, entity.posZ, pos.getX(), pos.getY(), pos.getZ()) < 16.0D)
					ticks = 1;
				
				if (entity.followPos.length > 1)
				{
					if (ticks > 0)
						ticks++;
					
					if (ticks > 10)
					{
						ticks = 0;
						if (++index >= entity.followPos.length)
							index = 0;
					}
				}
			}
			catch (Exception exception) {index = 0;}
		}
	}
}