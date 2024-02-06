package net.minecraft.AgeOfMinecraft.entity;

import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;


public class EntityBodyHelperDragon extends EntityBodyHelper
{
	/** Instance of EntityLiving. */
	private final EntityLivingBase theLiving;
	/** Used to progressively ajust the rotation of the body to the rotation of the head */
	private int rotationTickCounter;
	private float prevRenderYawHead;
	
	public EntityBodyHelperDragon(EntityLivingBase livingIn)
	{
		super(livingIn);
		this.theLiving = livingIn;
	}

	/**
	* Update the Head and Body rendenring angles
	*/
	public void updateRenderAngles()
	{
		double d0 = this.theLiving.posX - this.theLiving.prevPosX;
		double d1 = this.theLiving.posZ - this.theLiving.prevPosZ;
		
		if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D)
		{
			this.theLiving.renderYawOffset = this.theLiving.rotationYaw;
			this.theLiving.rotationYawHead = this.computeAngleWithBound(this.theLiving.renderYawOffset, this.theLiving.rotationYawHead, 360F);
			this.prevRenderYawHead = this.theLiving.rotationYawHead;
			this.rotationTickCounter = 0;
		}
		else
		{
			if (this.theLiving.getPassengers().isEmpty() || !(this.theLiving.getPassengers().get(0) instanceof EntityLiving))
			{
				float f = 360F;
				
				if (Math.abs(this.theLiving.rotationYawHead - this.prevRenderYawHead) > 5.0F)
				{
					this.rotationTickCounter = 0;
					this.prevRenderYawHead = this.theLiving.rotationYawHead;
				}
				else
				{
					++this.rotationTickCounter;
					if (this.rotationTickCounter > 10)
					{
						f = Math.max(1.0F - (float)(this.rotationTickCounter - 10) / 10.0F, 0.0F) * 360F;
					}
				}

				this.theLiving.renderYawOffset = this.computeAngleWithBound(this.theLiving.rotationYawHead, this.theLiving.renderYawOffset, f);
			}
		}
	}

	/**
	* Return the new angle2 such that the difference between angle1 and angle2 is lower than angleMax. Args : angle1,
	* angle2, angleMax
	*/
	private float computeAngleWithBound(float angle, float targetAngle, float maxIncrease)
	{
		float f = MathHelper.wrapDegrees(targetAngle - angle);
		
		if (f > maxIncrease)
		{
			f = maxIncrease;
		}

		if (f < -maxIncrease)
		{
			f = -maxIncrease;
		}

		return angle + f;
	}
}
