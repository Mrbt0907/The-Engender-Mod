package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.util.math.Vec3d;

public class PhaseRamAttack
extends PhaseBaseFriendly
{
	private Vec3d field_188670_c;
	private int field_188671_d = 0;
	public PhaseRamAttack(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public void doLocalUpdate()
	{
		if (this.field_188670_c == null)
		{
			this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
		}
		else if ((this.field_188671_d > 0) && (this.field_188671_d++ >= 10))
		{
			this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
		}
		else if (this.dragon.getPhaseManager().getCurrentPhase() != PhaseList.LANDING_APPROACH)
		{
			double d0 = this.field_188670_c.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
			if (d0 < 10.0D || d0 > 25500.0D)
			{
				this.field_188671_d += 1;
			}
		}
	}
	public void initPhase()
	{
		if (this.dragon.getAttackTarget() == null)
		this.field_188670_c = null;
		else
		this.field_188670_c = new Vec3d(this.dragon.getAttackTarget().posX, this.dragon.getAttackTarget().posY + (this.dragon.getRNG().nextDouble() * 2D - 1D), this.dragon.getAttackTarget().posZ);
		this.field_188671_d = 0;
	}
	public void func_188668_a(Vec3d p_188668_1_)
	{
		this.field_188670_c = p_188668_1_;
	}
	public float getMaxRiseOrFall()
	{
		return 3.0F;
	}
	public Vec3d getTargetLocation()
	{
		return this.field_188670_c;
	}
	public PhaseList<PhaseRamAttack> getPhaseList()
	{
		return PhaseList.CHARGING_PLAYER;
	}
}


