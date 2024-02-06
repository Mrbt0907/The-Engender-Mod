package net.minecraft.AgeOfMinecraft.entity.ai;

import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;
import net.minecraft.util.EnumHand;


public class EntityAIVindicatorBreakDoor extends EntityAIDoorInteract
{
	private int breakingTime;
	private int previousBreakProgress = -1;
	
	public EntityAIVindicatorBreakDoor(EntityLiving entityIn)
	{
		super(entityIn);
	}

	/**
	* Returns whether the EntityAIBase should begin execution.
	*/
	public boolean shouldExecute()
	{
		if (!super.shouldExecute())
		{
			return false;
		}
		else if (!this.entity.world.getBlockState(this.doorPosition).getBlock().canEntityDestroy(this.entity.world.getBlockState(this.doorPosition), this.entity.world, this.doorPosition, this.entity) || !net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this.entity, this.doorPosition, this.entity.world.getBlockState(this.doorPosition)))
		{
			return false;
		}
		else
		{
			return !BlockDoor.isOpen(this.entity.world, this.doorPosition);
		}
	}

	/**
	* Execute a one shot task or start executing a continuous task
	*/
	public void startExecuting()
	{
		super.startExecuting();
		this.breakingTime = 0;
	}

	/**
	* Returns whether an in-progress EntityAIBase should continue executing
	*/
	public boolean shouldContinueExecuting()
	{
		double d0 = this.entity.getDistanceSq(this.doorPosition);
		boolean flag;
		
		if (this.breakingTime <= 8)
		{
			if (!BlockDoor.isOpen(this.entity.world, this.doorPosition) && d0 < 4.0D)
			{
				flag = true;
				return flag;
			}
		}

		flag = false;
		return flag;
	}

	/**
	* Resets the task
	*/
	public void resetTask()
	{
		super.resetTask();
		this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, -1);
	}

	/**
	* Updates the task
	*/
	public void updateTask()
	{
		super.updateTask();
		
		if (this.entity.ticksExisted % 10 == 0)
		{
			++this.breakingTime;
			this.entity.swingArm(EnumHand.MAIN_HAND);
			this.entity.world.playEvent(1019, this.doorPosition, 0);
		}

		int i = (int)((float)this.breakingTime / 8.0F * 10.0F);
		
		if (i != this.previousBreakProgress)
		{
			this.entity.world.sendBlockBreakProgress(this.entity.getEntityId(), this.doorPosition, i);
			this.previousBreakProgress = i;
		}

		if (this.breakingTime == 8)
		{
			if (this.breakingTime > 8)
			this.breakingTime = 8;
			this.entity.world.setBlockToAir(this.doorPosition);
			this.entity.world.setBlockToAir(this.doorPosition.down());
			this.entity.playSound(ESound.heresJohnny, 2F, 1F);
			this.entity.world.playEvent(1021, this.doorPosition, 0);
			this.entity.world.playEvent(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
		}
	}
}