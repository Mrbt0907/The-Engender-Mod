package net.minecraft.AgeOfMinecraft.entity.ai;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEvoker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.world.World;


public class EntityAIFriendlyAttackMelee extends EntityAIBase
{
	World world;
	protected EntityFriendlyCreature attacker;
	/** An amount of decrementing ticks that allows the entity to attack once the tick reaches 0. */
	protected double attackTick;
	/** The speed with which the mob will approach the target */
	double speedTowardsTarget;
	/** When true, the mob will continue chasing its target, even if it can't find a path to them right now. */
	boolean longMemory;
	/** The PathEntity of our entity. */
	Path entityPathEntity;
	private int delayCounter;
	protected final int attackInterval = 20;
	private int failedPathFindingPenalty = 0;
	private boolean canPenalize = false;
	
	public EntityAIFriendlyAttackMelee(EntityFriendlyCreature creature, double speedIn, boolean useLongMemory)
	{
		this.attacker = creature;
		this.world = creature.world;
		this.speedTowardsTarget = speedIn;
		this.longMemory = useLongMemory;
		this.setMutexBits(1);
	}

	/**
	* Returns whether the EntityAIBase should begin execution.
	*/
	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
		
		if (entitylivingbase == null)
		{
			return false;
		}
		else if (!entitylivingbase.isEntityAlive())
		{
			return false;
		}
		else
		{
			return !this.attacker.shouldFleeAtLowHealth();
		}
	}

	/**
	* Returns whether an in-progress EntityAIBase should continue executing
	*/
	public boolean shouldContinueExecuting()
	{
		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
		if (entitylivingbase == null)
		{
			return false;
		}
		else if (!entitylivingbase.isEntityAlive())
		{
			return false;
		}
		else
		{
			return !this.attacker.shouldFleeAtLowHealth();
		}
	}

	/**
	* Execute a one shot task or start executing a continuous task
	*/
	public void startExecuting()
	{
		this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
		this.attacker.setArmsRaised(true);
		this.attacker.setSitResting(false);
		this.delayCounter = 0;
	}

	/**
	* Resets the task
	*/
	public void resetTask()
	{
		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
		
		if (entitylivingbase instanceof EntityPlayer && (((EntityPlayer)entitylivingbase).isSpectator() || ((EntityPlayer)entitylivingbase).isCreative()))
		{
			this.attacker.setAttackTarget((EntityLivingBase)null);
		}

		this.attacker.getNavigator().clearPath();
		this.attacker.setArmsRaised(false);
	}

	/**
	* Updates the task
	*/
	public void updateTask()
	{
		this.attacker.setSitResting(false);
		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
		if (entitylivingbase != null)
		{
			this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, attacker.getHorizontalFaceSpeed(), attacker.getVerticalFaceSpeed());
			double d0 = this.attacker.getDistanceSq(entitylivingbase);
			--this.delayCounter;
			
			if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && this.delayCounter <= 0)
			{
				entitylivingbase.getEntityBoundingBox();
				this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
				
				if (this.canPenalize)
				{
					this.delayCounter += failedPathFindingPenalty;
					if (this.attacker.getNavigator().getPath() != null)
					{
						failedPathFindingPenalty = 0;
					}
					else
					{
						failedPathFindingPenalty += 10;
					}
				}
				if (d0 > 1024.0D)
				{
					this.delayCounter += 10;
				}
				else if (d0 > 128.0D)
				{
					this.delayCounter += 5;
				}
			}

			this.checkAndPerformAttack(entitylivingbase, d0);
			this.attackTick = Math.max(this.attackTick - 1, 0);
			if (speedTowardsTarget != 0D && !(this.attacker instanceof EntityEvoker) && d0 > this.getAttackReachSqr(entitylivingbase))
			this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget);
			else
			this.attacker.renderYawOffset = this.attacker.rotationYaw = this.attacker.rotationYawHead;
			if (this.attacker.getNavigator().noPath())
			this.attacker.setArmsRaised(false);
			else
			this.attacker.setArmsRaised(true);
		}
	}

	protected void checkAndPerformAttack(EntityLivingBase attackTarget, double p_190102_2_)
	{
		double d0 = this.getAttackReachSqr(attackTarget);
		
		if (p_190102_2_ <= d0 && this.attackTick <= 0 && attacker.posY + attacker.height > attackTarget.posY)
		{
			this.attackTick = 20 - (attacker.getEntityAttribute(EntityFriendlyCreature.DEXTERITY).getBaseValue() * 0.1D);
			this.attacker.attackEntityAsMob(attackTarget);
			this.attacker.attackWithAdditionalEffects(attackTarget);
		}
	}

	protected double getAttackReachSqr(EntityLivingBase attackTarget)
	{
		return (double)((this.attacker.reachWidth * this.attacker.reachWidth) + ((attackTarget instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)attackTarget).reachWidth : attackTarget.width) * (attackTarget instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)attackTarget).reachWidth : attackTarget.width)) + 4D);
	}
}