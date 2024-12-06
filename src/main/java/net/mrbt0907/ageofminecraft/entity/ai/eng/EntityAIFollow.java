package net.mrbt0907.ageofminecraft.entity.ai.eng;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;

public class EntityAIFollow extends EntityAIBase
{
	private final EntityEngendered entity;
	private final double speedMultiplier;
	private final PathNavigate pathFinder;
	private Entity owner;
	
	private int timeToRecalcPath;
	private float oldWaterCost;
	
	public EntityAIFollow(EntityEngendered entity, double speedMultiplier)
	{
		this.entity = entity;
		this.speedMultiplier = speedMultiplier;
		pathFinder = entity.getNavigator();
		setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute()
	{
		Entity owner = entity.getOwner();
		return entity.followPos == null && !entity.getLeashed() && entity.hasOwner() && owner != null && !entity.getStance().equals(EnumAIStance.STAND_GROUND) && entity.getAttackTarget() == null;
	}
	
	public void startExecuting()
	{
		owner = entity.getOwner();
		timeToRecalcPath = 0;
		oldWaterCost = entity.getPathPriority(PathNodeType.WATER);
		entity.setPathPriority(PathNodeType.WATER, 0.0F);
	}
	
	public void resetTask()
	{
		owner = null;
		pathFinder.clearPath();
		entity.setPathPriority(PathNodeType.WATER, oldWaterCost);
	}
	
	public void updateTask()
	{
		entity.getLookHelper().setLookPositionWithEntity(owner, 10.0F, (float)entity.getVerticalFaceSpeed());
		double speed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * speedMultiplier;
		if (--timeToRecalcPath <= 0)
		{
			timeToRecalcPath = 10;

			if (!pathFinder.tryMoveToXYZ(owner.lastTickPosX, entity.world.getHeight(owner.getPosition()).getY(), owner.lastTickPosZ, speed) && !entity.getLeashed() && !entity.isRiding() && entity.getDistanceSq(owner) >= 144.0D)
			{
				int x = MathHelper.floor(owner.posX) - 2;
				int z = MathHelper.floor(owner.posZ) - 2;
				int y = MathHelper.floor(entity.world.getHeight(owner.getPosition()).getY());

				for (int i = 0; i <= 4; ++i)
					for (int ii = 0; ii <= 4; ++ii)
						if ((i < 1 || ii < 1 || i > 3 || ii > 3) && isTeleportFriendlyBlock(x, z, y, i, ii))
						{
							entity.setLocationAndAngles((double)((float)(x + i) + 0.5F), (double)y, (double)((float)(z + ii) + 0.5F), entity.rotationYaw, entity.rotationPitch);
							pathFinder.clearPath();
							return;
						}
			}
		}
	}

	protected boolean isTeleportFriendlyBlock(int x, int p_192381_2_, int y, int p_192381_4_, int p_192381_5_)
	{
		BlockPos blockpos = new BlockPos(x + p_192381_4_, y - 1, p_192381_2_ + p_192381_5_);
		IBlockState iblockstate = entity.world.getBlockState(blockpos);
		return iblockstate.getBlockFaceShape(entity.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(entity) && entity.world.isAirBlock(blockpos.up()) && entity.world.isAirBlock(blockpos.up(2));
	}
}
