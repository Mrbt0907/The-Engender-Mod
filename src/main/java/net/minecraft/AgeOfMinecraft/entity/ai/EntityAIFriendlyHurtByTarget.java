package net.minecraft.AgeOfMinecraft.entity.ai;
import java.util.Iterator;
import java.util.List;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIFriendlyHurtByTarget extends EntityAITarget
{
	private boolean entityCallsForHelp;
	public EntityAIFriendlyHurtByTarget(EntityCreature p_i45885_1_, boolean p_i45885_2_, Class... p_i45885_3_)
	{
		super(p_i45885_1_, false);
		this.entityCallsForHelp = p_i45885_2_;
		setMutexBits(1);
	}
	public boolean shouldExecute()
	{
		return this.taskOwner.getRevengeTarget() == null && isSuitableTarget(this.taskOwner.getRevengeTarget(), false);
	}
	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
		if (this.entityCallsForHelp)
		{
			List<? extends EntityCreature> list = this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D).grow(32.0D, 32.0D, 32.0D));
			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				EntityFriendlyCreature entitycreature = (EntityFriendlyCreature)iterator.next();
				if ((this.taskOwner != entitycreature) && (!entitycreature.isOnSameTeam(this.taskOwner.getRevengeTarget())) && (entitycreature.isOnSameTeam(this.taskOwner)))
				{
					setEntityAttackTarget(entitycreature, this.taskOwner.getRevengeTarget());
				}
			}
		}
		super.startExecuting();
	}
	protected void setEntityAttackTarget(EntityCreature p_179446_1_, EntityLivingBase p_179446_2_)
	{
		p_179446_1_.setAttackTarget(p_179446_2_);
	}
}


