package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PhaseApproachOwner extends PhaseBaseFriendly
{
	private Path currentPath;
	private Vec3d targetLocation;
	public PhaseApproachOwner(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public PhaseList<PhaseApproachOwner> getPhaseList()
	{
		return PhaseList.LANDING_APPROACH;
	}
	public void initPhase()
	{
		this.currentPath = null;
		this.targetLocation = null;
	}
	public void doLocalUpdate()
	{
		double d0 = this.targetLocation == null ? 0.0D : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
		if ((d0 < 10.0D) || (d0 > 25500.0D))
		{
			func_188681_j();
		}
	}
	public Vec3d getTargetLocation()
	{
		return this.targetLocation;
	}
	private void func_188681_j()
	{
		if ((this.currentPath == null) || (this.currentPath.isFinished()))
		{
			int i = this.dragon.initPathPoints();
			BlockPos blockpos = this.dragon.world.getTopSolidOrLiquidBlock(net.minecraft.world.gen.feature.WorldGenEndPodium.END_PODIUM_LOCATION);
			EntityPlayer entityplayer = this.dragon.world.getNearestAttackablePlayer(blockpos, 128.0D, 128.0D);
			if (this.dragon.getOwner() != null)
			blockpos = new BlockPos(this.dragon.getOwner());
			int j;
			if ((entityplayer != null) && (entityplayer != this.dragon.getOwner()))
			{
				Vec3d vec3d = new Vec3d(entityplayer.posX, 0.0D, entityplayer.posZ).normalize();
				j = this.dragon.getNearestPpIdx(-vec3d.x * 40.0D, 105.0D, -vec3d.z * 40.0D);
			}
			else
			{
				j = this.dragon.getNearestPpIdx(40.0D, blockpos.getY(), 0.0D);
			}
			PathPoint pathpoint = new PathPoint(blockpos.getX(), blockpos.getY(), blockpos.getZ());
			this.currentPath = this.dragon.findPath(i, j, pathpoint);
			if (this.currentPath != null)
			{
				this.currentPath.incrementPathIndex();
			}
		}
		func_188682_k();
		if ((this.currentPath != null) && (this.currentPath.isFinished()))
		{
			this.dragon.getPhaseManager().setPhase(PhaseList.LANDING);
		}
	}
	private void func_188682_k()
	{
		if ((this.currentPath != null) && (!this.currentPath.isFinished()))
		{
			Vec3d vec3d = this.currentPath.getCurrentPos();
			this.currentPath.incrementPathIndex();
			double d0 = vec3d.x;
			double d1 = vec3d.z;
			double d2;
			for (;;)
			{
				d2 = vec3d.y + this.dragon.getRNG().nextFloat() * 20.0F;
				if (d2 >= vec3d.y)
				{
					break;
				}
			}
			this.targetLocation = new Vec3d(d0, d2, d1);
		}
	}
}


