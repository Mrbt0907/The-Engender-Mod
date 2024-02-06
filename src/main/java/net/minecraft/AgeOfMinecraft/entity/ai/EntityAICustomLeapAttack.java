package net.minecraft.AgeOfMinecraft.entity.ai;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;

public class EntityAICustomLeapAttack
extends EntityAIBase
{
	EntityLiving leaper;
	EntityLivingBase leapTarget;
	float leapMotionX;
	float leapMotionY;
	float leapMotionZ;
	float leapMotionXGeneralPercent;
	float leapMotionZGeneralPercent;
	float leapMotionXPercent;
	float leapMotionZPercent;
	double minDistance;
	double maxDistance;
	int randomChance;
	public EntityAICustomLeapAttack(EntityLiving leapingEntity, float leapMotionYIn, float leapMotionHorizontal, float leapMotionHorizontalGeneralPercent, float leapMotionHorizontalPercent, double minDis, double maxDis, int chance)
	{
		this.leaper = leapingEntity;
		this.leapMotionY = leapMotionYIn;
		this.leapMotionX = leapMotionHorizontal;
		this.leapMotionZ = leapMotionHorizontal;
		this.leapMotionXGeneralPercent = leapMotionHorizontalGeneralPercent;
		this.leapMotionZGeneralPercent = leapMotionHorizontalGeneralPercent;
		this.leapMotionXPercent = leapMotionHorizontalPercent;
		this.leapMotionZPercent = leapMotionHorizontalPercent;
		this.minDistance = minDis;
		this.maxDistance = maxDis;
		this.randomChance = chance;
		setMutexBits(5);
	}
	public boolean shouldExecute()
	{
		this.leapTarget = this.leaper.getAttackTarget();
		if (this.leapTarget == null)
		{
			return false;
		}
		else
		{
			double d0 = this.leaper.getDistanceSq(this.leapTarget);
			return d0 >= minDistance && d0 <= maxDistance ? (!this.leaper.onGround ? false : this.leaper.getRNG().nextInt(2) == 0) : false;
		}
	}
	public boolean shouldContinueExecuting()
	{
		return !this.leaper.onGround;
	}
	public void startExecuting()
	{
		double d0 = this.leapTarget.posX - this.leaper.posX;
		double d1 = this.leapTarget.posZ - this.leaper.posZ;
		float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
		this.leaper.motionX += d0 / f * this.leapMotionX * this.leapMotionXGeneralPercent + this.leaper.motionX * this.leapMotionXPercent;
		this.leaper.motionZ += d1 / f * this.leapMotionZ * this.leapMotionZGeneralPercent + this.leaper.motionZ * this.leapMotionZPercent;
		this.leaper.motionY = this.leapMotionY;
		ForgeHooks.onLivingJump(leaper);
		double dou = this.leaper.getDistanceSq(this.leapTarget);
		if (dou <= this.minDistance + 16.0D)
		this.leaper.attackEntityAsMob(this.leapTarget);
	}
}


