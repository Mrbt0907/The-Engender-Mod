package net.mrbt0907.ageofminecraft.entity.ai.eng;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;

public class EngenderedPathNavigator extends PathNavigate
{
	public boolean shouldAvoidSun;
	protected EntityEngendered entityEngender;
	
	public EngenderedPathNavigator(EntityEngendered entity, World world)
	{
		super(entity, world);
		entityEngender = entity;
	}
	
	@Override
	protected PathFinder getPathFinder()
	{
		return null;
	}
	
	@Override
	protected Vec3d getEntityPosition()
	{
		return null;
	}
	
	@Override
	public Path getPathToEntityLiving(Entity entityIn)
	{
		return getPathToPos(entityIn.getPosition());
	}
	
	@Nullable
	@Override
	public Path getPathToPos(BlockPos pos)
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
			Path path = new Path(new PathPoint[] {new PathPoint(entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ()), new PathPoint(pos.getX(), pos.getY(), pos.getZ())});
			this.world.profiler.endSection();
			return path;
		}
	}
	
	@Override
	protected boolean canNavigate()
	{
		return !entity.isRiding();
	}
	
	@Override
	protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ)
	{
		return true;
	}
}
