package net.minecraft.AgeOfMinecraft.entity.ai;

import java.util.List;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.ai.EntityAIBase;


public class EntityAIFollowWildAdult extends EntityAIBase
{
	/** The child that is following its parent. */
	EntityFriendlyCreature childAnimal;
	EntityFriendlyCreature parentAnimal;
	double moveSpeed;
	private int delayCounter;
	
	public EntityAIFollowWildAdult(EntityFriendlyCreature animal, double speed)
	{
		this.childAnimal = animal;
		this.moveSpeed = speed;
	}

	/**
	* Returns whether the EntityAIBase should begin execution.
	*/
	public boolean shouldExecute()
	{
		if (!this.childAnimal.isWild())
		{
			return false;
		}
		else if (this.childAnimal.getOwnerId() != null)
		{
			return false;
		}
		else if (this.childAnimal.getGrowingAge() >= 0)
		{
			return false;
		}
		else
		{
			List<EntityFriendlyCreature> list = this.childAnimal.world.<EntityFriendlyCreature>getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().grow(16D));
			EntityFriendlyCreature entityanimal = null;
			double d0 = Double.MAX_VALUE;
			
			for (EntityFriendlyCreature entityanimal1 : list)
			{
				if (entityanimal1.getGrowingAge() >= 0 && this.childAnimal.getClass() == entityanimal1.getClass() && entityanimal1.isWild() && entityanimal1.getOwnerId() == null)
				{
					double d1 = this.childAnimal.getDistanceSq(entityanimal1);
					
					if (d1 <= d0)
					{
						d0 = d1;
						entityanimal = entityanimal1;
					}
				}
			}

			if (entityanimal == null)
			{
				return false;
			}
			else if (d0 < 16.0D)
			{
				return false;
			}
			else
			{
				this.parentAnimal = entityanimal;
				return true;
			}
		}
	}

	/**
	* Returns whether an in-progress EntityAIBase should continue executing
	*/
	public boolean shouldContinueExecuting()
	{
		if (!this.childAnimal.isWild())
		{
			return false;
		}
		else if (this.childAnimal.getOwnerId() != null)
		{
			return false;
		}
		else if (this.childAnimal.getGrowingAge() >= 0)
		{
			return false;
		}
		else if (!this.parentAnimal.isEntityAlive())
		{
			return false;
		}
		else
		{
			double d0 = this.childAnimal.getDistanceSq(this.parentAnimal);
			return d0 >= 16.0D && d0 <= 256.0D;
		}
	}

	/**
	* Execute a one shot task or start executing a continuous task
	*/
	public void startExecuting()
	{
		this.delayCounter = 0;
	}

	/**
	* Resets the task
	*/
	public void resetTask()
	{
		this.parentAnimal = null;
	}

	/**
	* Updates the task
	*/
	public void updateTask()
	{
		if (--this.delayCounter <= 0)
		{
			this.delayCounter = 20;
			this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
		}
	}
}