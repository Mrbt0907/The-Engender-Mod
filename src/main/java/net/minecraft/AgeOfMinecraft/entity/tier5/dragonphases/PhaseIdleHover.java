package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.util.math.Vec3d;

public class PhaseIdleHover
extends PhaseBaseFriendly
{
	private Vec3d field_188680_b;
	public PhaseIdleHover(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public void doLocalUpdate()
	{
		if (this.field_188680_b == null)
		this.field_188680_b = new Vec3d(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
		if (!this.dragon.sitting)
		{
			this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
		}
	}
	public boolean getIsStationary()
	{
		return true;
	}
	public void initPhase()
	{
		this.field_188680_b = null;
	}
	public float getMaxRiseOrFall()
	{
		return 1.5F;
	}
	public Vec3d getTargetLocation()
	{
		return this.field_188680_b;
	}
	public PhaseList<PhaseIdleHover> getPhaseList()
	{
		return PhaseList.HOVER;
	}
}


