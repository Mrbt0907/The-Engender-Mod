package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseTakeoff extends PhaseBaseFriendly
{
	private boolean firstTick;
	private Path currentPath;
	private Vec3d targetLocation;
	
	public PhaseTakeoff(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}

	/**
	* Gives the phase a chance to update its status.
	* Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
	*/
	public void doLocalUpdate()
	{
		if (!this.firstTick && this.currentPath != null)
		{
			BlockPos blockpos = this.dragon.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
			double d0 = this.dragon.getDistanceSqToCenter(blockpos);
			
			if (d0 > 100.0D)
			{
				this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
			}
		}
		else
		{
			this.firstTick = false;
			this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
		}
	}

	/**
	* Called when this phase is set to active
	*/
	public void initPhase()
	{
		this.firstTick = true;
		this.currentPath = null;
		this.targetLocation = null;
	}

	/**
	* Returns the location the dragon is flying toward
	*/
	@Nullable
	public Vec3d getTargetLocation()
	{
		return this.targetLocation;
	}

	public PhaseList<PhaseTakeoff> getPhaseList()
	{
		return PhaseList.TAKEOFF;
	}
}