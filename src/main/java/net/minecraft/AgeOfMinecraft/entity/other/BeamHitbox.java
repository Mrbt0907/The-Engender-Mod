package net.minecraft.AgeOfMinecraft.entity.other;

import java.util.List;

import net.endermanofdoom.mac.util.math.Maths.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BeamHitbox
{
	private final IBeamHitboxHandler parent;
	private World world;
	private String name;
	private Vec3 startPos;
	private Vec3 endPos;
	private double size;
	
	public BeamHitbox(IBeamHitboxHandler parent, World world, String name, double size)
	{
		this.parent = parent;
		this.world = world;
		this.name = name;
		this.size = size;
	}
	
	public void onUpdate()
	{
		if (!world.isRemote && startPos != null && endPos != null)
		{
			
			RayTraceResult result = world.rayTraceBlocks(startPos.toVec3MC(), endPos.toVec3MC());
			
			if (result != null)
				if (result.typeOfHit.equals(RayTraceResult.Type.BLOCK))
					onBlockCollision(result.getBlockPos());
				else if (result.typeOfHit.equals(RayTraceResult.Type.ENTITY))
					onEntityCollision(result.entityHit);
			
			Entity entity;
			List<Entity> entities = world.loadedEntityList;
			double distance;
			for (int i = 0; i < entities.size(); i++)
			{
				entity = entities.get(i);
				distance = distance(entity);
				if (distance >= 0.0D && entity.canBeAttackedWithItem() && distance <= size)
					onEntityCollision(entity);
			}
		}
	}
	
	public void onBlockCollision(BlockPos pos)
	{
		parent.onBlockCollision(pos);
	}
	
	public void onEntityCollision(Entity entity)
	{
		parent.onEntityCollision(entity);
	}
	
	public Vec3 getStartPos()
	{
		return startPos;
	}
	
	public Vec3 getEndPos()
	{
		return endPos;
	}
	
	public BeamHitbox setPosition(double startX, double startY, double startZ, float yaw, float pitch, double distance)
	{
		//double x = startX + distance * Math.cos(pitch) * Math.sin(yaw), y = startY + distance * Math.sin(pitch), z = startZ + distance * Math.cos(pitch) * Math.cos(yaw);
		double x = startX + distance * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(-yaw));
		double y = startY + distance * Math.sin(Math.toRadians(pitch));
		double z = startZ + distance * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
		return setPosition(startX, startY, startZ, x, y, z);
	}
	
	public BeamHitbox setPosition(double startX, double startY, double startZ, double endX, double endY, double endZ)
	{
		if (startPos == null)
			startPos = new Vec3(startX, startY, startZ);
		else
		{
			startPos.posX = startX;
			startPos.posY = startY;
			startPos.posZ = startZ;
		}
		
		if (endPos == null)
			endPos = new Vec3(endX, endY, endZ);
		else
		{
			endPos.posX = endX;
			endPos.posY = endY;
			endPos.posZ = endZ;
		}
		return this;
	}
	
	public BeamHitbox setSize(double size)
	{
		this.size = size;
		return this;
	}
	
	public double getSize()
	{
		return size;
	}
	
	public BeamHitbox resetPosition()
	{
		startPos = null;
		endPos = null;
		return this;
	}
	
	public boolean hasPosition()
	{
		return startPos != null && endPos != null;
	}
	
	public String getName()
	{
		return name;
	}
	
	public double distance(Entity entity)
	{
		return hasPosition() ? distance(entity.posX, entity.posY, entity.posZ) : -1.0D;
	}
	
	public double distance(double x, double y, double z)
	{
		if (!hasPosition()) return -1.0D;
		
		double b = Math.sqrt(Math.pow((startPos.posX - endPos.posX), 2) 
	            + Math.pow((startPos.posY - endPos.posY), 2) 
	            + Math.pow((startPos.posZ - endPos.posZ), 2));

	    double S = Math.sqrt(Math.pow((startPos.posY - y) * (endPos.posZ - z) - (startPos.posZ - z) * (endPos.posY - y), 2) +
	            Math.pow((startPos.posZ - z) * (endPos.posX - x) - (startPos.posX - x) * (endPos.posZ - z), 2) +
	            Math.pow((startPos.posX - x) * (endPos.posY - y) - (startPos.posY - y) * (endPos.posX - x), 2)) / 2;

	    return 2 * S / b;
	}
}
