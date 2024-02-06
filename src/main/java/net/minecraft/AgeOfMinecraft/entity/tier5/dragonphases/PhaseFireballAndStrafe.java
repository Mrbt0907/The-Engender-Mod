package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PhaseFireballAndStrafe extends PhaseBaseFriendly
{
	private int field_188690_c;
	private Path field_188691_d;
	private Vec3d field_188692_e;
	private EntityLivingBase attackTarget;
	private boolean field_188694_g;
	public PhaseFireballAndStrafe(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public void doLocalUpdate()
	{
		if (this.attackTarget == null)
		{
			this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
		}
		else
		{
			if ((this.field_188691_d != null) && (this.field_188691_d.isFinished()))
			{
				double d0 = this.attackTarget.posX;
				double d1 = this.attackTarget.posZ;
				double d2 = d0 - this.dragon.posX;
				double d3 = d1 - this.dragon.posZ;
				double d4 = MathHelper.sqrt(d2 * d2 + d3 * d3);
				double d5 = Math.min(0.4000000059604645D + d4 / 80.0D - 1.0D, 10.0D);
				this.field_188692_e = new Vec3d(d0, this.attackTarget.posY + d5, d1);
			}
			double d12 = this.field_188692_e == null ? 0.0D : this.field_188692_e.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
			if ((d12 < 100.0D) || (d12 > 22500.0D))
			{
				func_188687_j();
			}
			double d13 = 64.0D;
			if (this.attackTarget.getDistanceSq(this.dragon) < d13 * d13)
			{
				if (this.dragon.canEntityBeSeen(this.attackTarget))
				{
					this.field_188690_c += 1;
					Vec3d vec3d1 = new Vec3d(this.attackTarget.posX - this.dragon.posX, 0.0D, this.attackTarget.posZ - this.dragon.posZ).normalize();
					Vec3d vec3d = new Vec3d(MathHelper.sin(this.dragon.rotationYaw * 0.017453292F), 0.0D, -MathHelper.cos(this.dragon.rotationYaw * 0.017453292F)).normalize();
					float f1 = (float)vec3d.dotProduct(vec3d1);
					float f = (float)(Math.acos(f1) * 57.29577951308232D);
					f += 0.5F;
					if ((this.field_188690_c >= 5) && (f >= 0.0F) && (f < 10.0F))
					{
						this.dragon.getLook(1.0F);
						double d6 = this.dragon.dragonPartHead.posX;
						double d7 = this.dragon.dragonPartHead.posY + 2D;
						double d8 = this.dragon.dragonPartHead.posZ;
						double d9 = (this.attackTarget.posX) - d6;
						double d10 = (this.attackTarget.posY + 1D) - d7;
						double d11 = (this.attackTarget.posZ) - d8;
						this.dragon.world.playEvent((EntityPlayer)null, 1016, new BlockPos(this.dragon), 0);
						EntityDragonFireballOther entitydragonfireball = new EntityDragonFireballOther(this.dragon.world, this.dragon, d9, d10, d11);
						entitydragonfireball.posX = d6;
						entitydragonfireball.posY = d7;
						entitydragonfireball.posZ = d8;
						this.dragon.world.spawnEntity(entitydragonfireball);
						this.field_188690_c = 0;
						if (this.field_188691_d != null)
						{
							while (!this.field_188691_d.isFinished())
							{
								this.field_188691_d.incrementPathIndex();
							}
						}
						this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
						this.dragon.setAttackTarget(null);
					}
				}
				else if (this.field_188690_c > 0)
				{
					this.field_188690_c -= 1;
				}
			}
			else if (this.field_188690_c > 0)
			{
				this.field_188690_c -= 1;
			}
		}
	}
	private void func_188687_j()
	{
		if ((this.field_188691_d == null) || (this.field_188691_d.isFinished()))
		{
			if (this.dragon.getRNG().nextInt(8) == 0)
			{
				this.field_188694_g = (!this.field_188694_g);
			}
			if (this.field_188694_g)
			{
			}
			else
			{
			}
			if (this.field_188691_d != null)
			{
				this.field_188691_d.incrementPathIndex();
			}
		}
		func_188688_k();
	}
	private void func_188688_k()
	{
		if ((this.field_188691_d != null) && (!this.field_188691_d.isFinished()))
		{
			Vec3d vec3d = this.field_188691_d.getCurrentPos();
			this.field_188691_d.incrementPathIndex();
			double d0 = vec3d.x;
			double d2 = vec3d.z;
			double d1;
			for (;;)
			{
				d1 = vec3d.y + 30.0D + this.dragon.getRNG().nextFloat() * 20.0F;
				if (d1 >= vec3d.y)
				{
					break;
				}
			}
			this.field_188692_e = new Vec3d(d0, d1, d2);
		}
	}
	public void initPhase()
	{
		this.field_188690_c = 0;
		this.field_188692_e = null;
		this.field_188691_d = null;
		this.attackTarget = null;
	}
	public void func_188686_a(EntityLivingBase p_188686_1_)
	{
		this.attackTarget = p_188686_1_;
		int i = this.dragon.initPathPoints();
		int j = this.dragon.getNearestPpIdx(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ);
		int k = MathHelper.floor(this.attackTarget.posX);
		int l = MathHelper.floor(this.attackTarget.posZ);
		double d0 = k - this.dragon.posX;
		double d1 = l - this.dragon.posZ;
		double d2 = MathHelper.sqrt(d0 * d0 + d1 * d1);
		double d3 = Math.min(0.4000000059604645D + d2 / 80.0D - 1.0D, 10.0D);
		int i1 = MathHelper.floor(this.attackTarget.posY + d3);
		PathPoint pathpoint = new PathPoint(k, i1, l);
		this.field_188691_d = this.dragon.findPath(i, j, pathpoint);
		if (this.field_188691_d != null)
		{
			this.field_188691_d.incrementPathIndex();
			func_188688_k();
		}
	}
	public Vec3d getTargetLocation()
	{
		return this.field_188692_e;
	}
	public PhaseList<PhaseFireballAndStrafe> getPhaseList()
	{
		return PhaseList.STRAFE_PLAYER;
	}
}


