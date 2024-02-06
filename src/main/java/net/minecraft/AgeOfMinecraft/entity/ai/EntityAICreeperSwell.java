package net.minecraft.AgeOfMinecraft.entity.ai;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAICreeperSwell extends EntityAIBase
{
	EntityCreeper swellingCreeper;
	EntityLivingBase creeperAttackTarget;
	public EntityAICreeperSwell(EntityCreeper p_i1655_1_)
	{
		this.swellingCreeper = p_i1655_1_;
		setMutexBits(1);
	}
	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();
		return ((this.swellingCreeper.getCreeperState() > 0) || ((entitylivingbase != null) && (this.swellingCreeper.getDistanceSq(entitylivingbase) < 9.0D))) && (this.swellingCreeper.getHealth() <= this.swellingCreeper.getMaxHealth() / 2.0F);
	}
	public void startExecuting()
	{
		this.swellingCreeper.getNavigator().clearPath();
		this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
	}
	public void resetTask()
	{
		this.creeperAttackTarget = null;
	}
	public void updateTask()
	{
		if (this.creeperAttackTarget == null || (this.creeperAttackTarget instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)creeperAttackTarget).getOwnerId() == swellingCreeper.getOwnerId()))
		{
			this.swellingCreeper.setCreeperState(-1);
		}
		else if (this.swellingCreeper.getDistanceSq(this.creeperAttackTarget) > 49.0D)
		{
			this.swellingCreeper.setCreeperState(-1);
		}
		else if (!this.swellingCreeper.getEntitySenses().canSee(this.creeperAttackTarget))
		{
			this.swellingCreeper.setCreeperState(-1);
		}
		else
		{
			this.swellingCreeper.setCreeperState(1);
		}
	}
}