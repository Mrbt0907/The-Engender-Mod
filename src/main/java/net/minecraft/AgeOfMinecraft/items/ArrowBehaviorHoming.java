package net.minecraft.AgeOfMinecraft.items;

import java.util.List;

import net.endermanofdoom.mac.entity.EntityArrowEX;
import net.endermanofdoom.mac.interfaces.IArrowBehavior;
import net.endermanofdoom.mac.util.math.Maths;
import net.minecraft.AgeOfMinecraft.entity.cameos.Darkness.EntityDarkProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ArrowBehaviorHoming implements IArrowBehavior
{
	public Entity target;
	public boolean canTarget = true;
	
	@Override
	public void onArrowTick(World world, EntityLivingBase shooter, EntityArrowEX arrow){}

	@Override
	public void onArrowTickAir(World world, EntityLivingBase shooter, EntityArrowEX arrow)
	{
		if (world.isRemote) return;
		
		
		if (canTarget && (target == null || !target.isEntityAlive()))
		{
			List<Entity> entities = world.loadedEntityList;
			double distance = 1024.0D, curDistance;
			boolean player;
			target = null;
				
			for (Entity entity : entities)
			{
				curDistance = arrow.getDistanceSq(entity);
					
				if (curDistance < distance && entity.isEntityAlive() && entity.canBeAttackedWithItem() && !(entity instanceof EntityDarkProjectile || entity instanceof IProjectile) && (shooter != null && !entity.equals(shooter) && !shooter.isOnSameTeam(entity)) || shooter == null)
				{
					player = entity instanceof EntityPlayer;
					if (player && !((EntityPlayer)entity).isSpectator() && !((EntityPlayer)entity).isCreative() || !player)
					{
						distance = curDistance;
						target = entity;
						canTarget = false;
					}
				}
			}
		}
		
		//Chase target
		if (target != null)
		{
			double tDist = Maths.distanceSq(target.posX, target.posY + target.getEyeHeight(), target.posZ, arrow.posX, arrow.posY, arrow.posZ);
			double x = (target.posX - arrow.posX) / tDist,
				y = (target.posY + target.getEyeHeight() - arrow.posY) / tDist,
				z = (target.posZ - arrow.posZ) / tDist;
			
			if (tDist > 48.0D || tDist < 5.0D)
			{
				target = null;
				return;
			}
			arrow.motionX *= 0.85D;
			arrow.motionY *= 0.85D;
			arrow.motionZ *= 0.85D;
			arrow.motionX += x * 0.45D;
			arrow.motionY += y * 0.45D;
			arrow.motionZ += z * 0.45D;
		}
	}

	@Override
	public void onArrowTickGround(World world, EntityLivingBase shooter, EntityArrowEX arrow) {}

	@Override
	public void onArrowTickWater(World world, EntityLivingBase shooter, EntityArrowEX arrow)
	{
		target = null;
	}

	@Override
	public void onArrowHit(World world, EntityLivingBase shooter, Entity victim, EntityArrowEX arrow) {}

	@Override
	public void onArrowHitBlock(World world, EntityLivingBase shooter, BlockPos position, EntityArrowEX arrow) {}

	@Override
	public void onArrowStop(World world, EntityLivingBase shooter, EntityArrowEX arrow)
	{
		target = null;
	}
}
