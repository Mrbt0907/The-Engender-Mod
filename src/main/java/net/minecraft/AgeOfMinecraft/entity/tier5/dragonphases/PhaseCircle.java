package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.Vec3d;

public class PhaseCircle
extends PhaseBaseFriendly
{
	private Path field_188677_b;
	private Vec3d field_188678_c;
	private boolean field_188679_d;
	public PhaseCircle(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public PhaseList<PhaseCircle> getPhaseList()
	{
		return PhaseList.HOLDING_PATTERN;
	}
	public void doLocalUpdate()
	{
		double d0 = this.field_188678_c == null ? 0.0D : this.field_188678_c.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
		if ((d0 < 10.0D) || (d0 > 25500.0D))
		{
			func_188675_j();
		}
	}
	public void initPhase()
	{
		this.field_188677_b = null;
		this.field_188678_c = null;
	}
	public Vec3d getTargetLocation()
	{
		return this.field_188678_c;
	}
	private void func_188675_j()
	{
		if ((this.field_188677_b != null) && (this.field_188677_b.isFinished()))
		{
			if (this.dragon.getRNG().nextInt(5) == 0)
			{
				this.dragon.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
				return;
			}
			EntityLivingBase entity = this.dragon.getAttackTarget();
		
			if ((entity != null) && (this.dragon.getRNG().nextInt(2) == 0))
			{
				this.dragon.getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
				((PhaseRamAttack)this.dragon.getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER)).func_188668_a(new Vec3d(entity.posX, entity.posY, entity.posZ));
				return;
			}
			if ((entity != null) && (this.dragon.getRNG().nextInt(3) == 0))
			{
				func_188674_a(entity);
				return;
			}
		}
		if ((this.field_188677_b == null) || (this.field_188677_b.isFinished()))
		{
			int j = this.dragon.initPathPoints();
			int k = j;
			if (this.dragon.getRNG().nextInt(8) == 0)
			{
				this.field_188679_d = (!this.field_188679_d);
				k = j + 6;
			}
			if (this.field_188679_d)
			{
				k++;
			}
			else
			{
				k--;
			}
			k -= 12;
			k &= 0x7;
			k += 12;
			this.field_188677_b = this.dragon.findPath(j, k, (PathPoint)null);
			if (this.field_188677_b != null)
			{
				this.field_188677_b.incrementPathIndex();
			}
		}
		func_188676_k();
	}
	private void func_188674_a(EntityLivingBase p_188674_1_)
	{
		if (!p_188674_1_.isOnSameTeam(this.dragon))
		{
			this.dragon.getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
			((PhaseFireballAndStrafe)this.dragon.getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER)).func_188686_a(p_188674_1_);
		}
	}
	private void func_188676_k()
	{
		if ((this.field_188677_b != null) && (!this.field_188677_b.isFinished()))
		{
			Vec3d vec3d = this.field_188677_b.getCurrentPos();
			this.field_188677_b.incrementPathIndex();
			double d0 = vec3d.x;
			double d1 = vec3d.z;
			double d2;
			for (;;)
			{
				if (this.dragon.getAttackTarget() != null)
				{
					d2 = vec3d.y;
				} else {
						d2 = vec3d.y + 10.0D + (this.dragon.getRNG().nextDouble() * 5.0D);
					}
					if (d2 >= vec3d.y)
					{
						break;
					}
				}
				if (this.dragon.getOwner() != null)
				{
					this.field_188678_c = new Vec3d(this.dragon.getOwner().posX + d0, this.dragon.getOwner().posY + d2, this.dragon.getOwner().posZ + d1);
				} else {
						this.field_188678_c = new Vec3d(d0, d2, d1);
					}
				}
			}
		}

		
		