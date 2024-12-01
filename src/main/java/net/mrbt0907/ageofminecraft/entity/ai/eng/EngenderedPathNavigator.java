package net.mrbt0907.ageofminecraft.entity.ai.eng;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;

public class EngenderedPathNavigator extends PathNavigate
{
	public boolean shouldAvoidSun;
	public EngenderedPathNavigator(EntityEngendered entity, World world)
	{
		super(entity, world);
	}

	protected PathFinder getPathFinder()
	{
		nodeProcessor = new WalkNodeProcessor();
		nodeProcessor.setCanEnterDoors(true);
		return new PathFinder(nodeProcessor);
	}
	
	@Override
	protected boolean canNavigate()
	{
		return !entity.isRiding();
	}

	@Override
	public Path getPathToPos(BlockPos pos)
	{
		BlockPos blockpos;
		if (world.getBlockState(pos).getMaterial() == Material.AIR)
		{
			for (blockpos = pos.down(); blockpos.getY() > 0 && world.getBlockState(blockpos).getMaterial() == Material.AIR; blockpos = blockpos.down());
			if (blockpos.getY() > 0)
				return getRealPathToPos(blockpos.up());

			while (blockpos.getY() < world.getHeight() && world.getBlockState(blockpos).getMaterial() == Material.AIR)
				blockpos = blockpos.up();
			pos = blockpos;
			blockpos = null;
		}

		if (!world.getBlockState(pos).getMaterial().isSolid())
		{
			return getRealPathToPos(pos);
		}
		else
		{
			for (blockpos = pos.up(); blockpos.getY() < world.getHeight() && world.getBlockState(blockpos).getMaterial().isSolid(); blockpos = blockpos.up());
			return getRealPathToPos(blockpos);
		}
	}
	
	public Path getRealPathToPos(BlockPos pos)
	{
		if (!this.canNavigate())
        {
            return null;
        }
        else if (this.currentPath != null && !this.currentPath.isFinished() && pos.equals(this.targetPos))
        {
            return this.currentPath;
        }
        else
        {
            this.targetPos = pos;
            float f = this.getPathSearchRange();
            this.world.profiler.startSection("pathfind");
            BlockPos blockpos = new BlockPos(this.entity);
            int i = (int)(f + 8.0F);
            ChunkCache chunkcache = new ChunkCache(this.world, blockpos.add(-i, -i, -i), blockpos.add(i, i, i), 0);
            Path path = this.getPathFinder().findPath(chunkcache, this.entity, this.targetPos, f);
            
            this.world.profiler.endSection();
            return path;
        }
	}
	
	@Override
	protected Vec3d getEntityPosition()
	{
		return new Vec3d(entity.posX, entity.posY, entity.posZ);
	}
	
	@Override
	public Path getPathToEntityLiving(Entity entity)
	{
		return getPathToPos(new BlockPos(entity));
	}
	
	@Override
	protected void removeSunnyPath()
	{
		super.removeSunnyPath();
		if (shouldAvoidSun)
		{
			if (world.canSeeSky(new BlockPos(MathHelper.floor(entity.posX), (int)(entity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor(entity.posZ))))
				return;

			for (int i = 0; i < currentPath.getCurrentPathLength(); ++i)
			{
				PathPoint pathpoint = currentPath.getPathPointFromIndex(i);

				if (world.canSeeSky(new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z)))
				{
					currentPath.setCurrentPathLength(i - 1);
					return;
				}
			}
		}
	}
	
	@Override
	protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ)
	{
		int i = MathHelper.floor(posVec31.x);
		int j = MathHelper.floor(posVec31.z);
		double d0 = posVec32.x - posVec31.x;
		double d1 = posVec32.z - posVec31.z;
		double d2 = d0 * d0 + d1 * d1;

		if (d2 < 1.0E-8D)
			return false;
		else
		{
			double d3 = 1.0D / Math.sqrt(d2);
			d0 = d0 * d3;
			d1 = d1 * d3;
			sizeX = sizeX + 2;
			sizeZ = sizeZ + 2;

			if (!isSafeToStandAt(i, (int)posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d1))
				return false;
			else
			{
				sizeX = sizeX - 2;
				sizeZ = sizeZ - 2;
				double d4 = 1.0D / Math.abs(d0);
				double d5 = 1.0D / Math.abs(d1);
				double d6 = (double)i - posVec31.x;
				double d7 = (double)j - posVec31.z;

				if (d0 >= 0.0D) ++d6;
				if (d1 >= 0.0D) ++d7;

				d6 = d6 / d0;
				d7 = d7 / d1;
				int k = d0 < 0.0D ? -1 : 1;
				int l = d1 < 0.0D ? -1 : 1;
				int i1 = MathHelper.floor(posVec32.x);
				int j1 = MathHelper.floor(posVec32.z);
				int k1 = i1 - i;
				int l1 = j1 - j;

				while (k1 * k > 0 || l1 * l > 0)
				{
					if (d6 < d7)
					{
						d6 += d4;
						i += k;
						k1 = i1 - i;
					}
					else
					{
						d7 += d5;
						j += l;
						l1 = j1 - j;
					}

					if (!isSafeToStandAt(i, (int)posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d1))
						return false;
				}

				return true;
			}
		}
	}
	
	/**
	 * Returns true when an entity could stand at a position, including solid blocks under the entire entity.
	 */
	private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d vec31, double p_179683_8_, double p_179683_10_)
	{
		int i = x - sizeX / 2;
		int j = z - sizeZ / 2;

		if (!isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_))
			return false;
		else
		{
			for (int k = i; k < i + sizeX; ++k)
			{
				for (int l = j; l < j + sizeZ; ++l)
				{
					double d0 = (double)k + 0.5D - vec31.x;
					double d1 = (double)l + 0.5D - vec31.z;

					if (d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0D)
					{
						PathNodeType pathnodetype = nodeProcessor.getPathNodeType(world, k, y - 1, l, entity, sizeX, sizeY, sizeZ, true, true);

						if (pathnodetype == PathNodeType.WATER)
							return false;

						if (pathnodetype == PathNodeType.LAVA)
							return false;

						if (pathnodetype == PathNodeType.OPEN)
							return false;

						pathnodetype = nodeProcessor.getPathNodeType(world, k, y, l, entity, sizeX, sizeY, sizeZ, true, true);
						float f = entity.getPathPriority(pathnodetype);

						if (f < 0.0F || f >= 8.0F)
							return false;

						if (pathnodetype == PathNodeType.DAMAGE_FIRE || pathnodetype == PathNodeType.DANGER_FIRE || pathnodetype == PathNodeType.DAMAGE_OTHER)
							return false;
					}
				}
			}

			return true;
		}
	}
	
	/**
	 * Returns true if an entity does not collide with any solid blocks at the position.
	 */
	private boolean isPositionClear(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d p_179692_7_, double p_179692_8_, double p_179692_10_)
	{
		for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1)))
		{
			double d0 = (double)blockpos.getX() + 0.5D - p_179692_7_.x;
			double d1 = (double)blockpos.getZ() + 0.5D - p_179692_7_.z;

			if (d0 * p_179692_8_ + d1 * p_179692_10_ >= 0.0D)
			{
				Block block = world.getBlockState(blockpos).getBlock();

				if (!block.isPassable(world, blockpos))
					return false;
			}
		}

		return true;
	}
}
