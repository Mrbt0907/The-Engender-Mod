package net.minecraft.AgeOfMinecraft.entity.ai;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStorm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;


public class EntityAIFindNearestUnalliedTarget extends EntityAIBase
{
	private final EntityFriendlyCreature mob;
	private final Predicate<EntityLivingBase> predicate;
	private final Sorter sorter;
	private EntityLivingBase target;
	private final Class <? extends EntityLivingBase > classToCheck;
	
	public EntityAIFindNearestUnalliedTarget(EntityFriendlyCreature mobIn, Class <? extends EntityLivingBase > p_i45884_2_)
	{
		this.mob = mobIn;
		this.classToCheck = p_i45884_2_;
		
		this.predicate = new Predicate<EntityLivingBase>()
		{
			public boolean apply(@Nullable EntityLivingBase p_apply_1_)
			{
				double d0 = EntityAIFindNearestUnalliedTarget.this.getFollowRange();
				
				if (p_apply_1_.isSneaking())
				{
					d0 *= 0.800000011920929D;
				}

				if (p_apply_1_.isInvisible())
				{
					return false;
				}
				else if (!mob.attackable())
				{
					return false;
				}
				else if (mob.getAttackTarget() != null)
				{
					return false;
				}
				else
				{
					return (double)p_apply_1_.getDistance(EntityAIFindNearestUnalliedTarget.this.mob) > d0 || mob instanceof EntityWitherStorm ? false : EntityAITarget.isSuitableTarget(EntityAIFindNearestUnalliedTarget.this.mob, p_apply_1_, false, true);
				}
			}
		};
		this.sorter = new Sorter(mobIn);
	}

	/**
	* Returns whether the EntityAIBase should begin execution.
	*/
	public boolean shouldExecute()
	{
		double d0 = this.getFollowRange();
		List<EntityLivingBase> list = this.mob.world.<EntityLivingBase>getEntitiesWithinAABB(this.classToCheck, this.mob.getEntityBoundingBox().grow(d0), this.predicate);
		Collections.sort(list, this.sorter);
		
		if (list.isEmpty())
		{
			return false;
		}
		else
		{
			this.target = list.get(0);
			return true;
		}
	}

	/**
	* Returns whether an in-progress EntityAIBase should continue executing
	*/
	public boolean shouldContinueExecuting()
	{
		EntityLivingBase entitylivingbase = this.mob.getAttackTarget();
		
		if (entitylivingbase == null)
		{
			return false;
		}
		else if (!entitylivingbase.isEntityAlive())
		{
			return false;
		}
		else if (mob.isOnSameTeam(entitylivingbase))
		{
			return false;
		}
		else
		{
			double d0 = this.getFollowRange();
			
			if (this.mob.getDistanceSq(entitylivingbase) > d0 * d0)
			{
				return false;
			}
			else
			{
				return !(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative();
			}
		}
	}

	/**
	* Execute a one shot task or start executing a continuous task
	*/
	public void startExecuting()
	{
		this.mob.setAttackTarget(this.target);
		super.startExecuting();
	}

	/**
	* Reset the task's internal state. Called when this task is interrupted by another one
	*/
	public void resetTask()
	{
		this.mob.setAttackTarget((EntityLivingBase)null);
		super.startExecuting();
	}

	protected double getFollowRange()
	{
		IAttributeInstance iattributeinstance = this.mob.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
		return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
	}
	public static class Sorter implements Comparator<Entity>
	{
		private final Entity entity;
		
		public Sorter(Entity entityIn)
		{
			this.entity = entityIn;
		}

		public int compare(Entity p_compare_1_, Entity p_compare_2_)
		{
			double d0 = this.entity.getDistanceSq(p_compare_1_);
			double d1 = this.entity.getDistanceSq(p_compare_2_);
			
			if (d0 < d1)
			{
				return -1;
			}
			else
			{
				return d0 > d1 ? 1 : 0;
			}
		}
	}
}