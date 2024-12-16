package net.mrbt0907.ageofminecraft.util.mrbtutil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiPredicate;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class WorldUtils
{
	public static Entity getEntity(World world, UUID uuid)
	{
		if (uuid == null) return null;
		List<Entity> entities = new ArrayList<Entity>(world.loadedEntityList);
		for (Entity entity : entities)
			if (entity.getUniqueID().equals(uuid))
				return entity;	
		return null;
	}
	
	public static Entity getEntity(World world, Entity target, BiPredicate<Entity, Entity> predicate)
	{
		if (target == null) return null;
		double distance = Double.MAX_VALUE, targetDistance;
		Entity result = null;
		List<Entity> entities = new ArrayList<Entity>(world.loadedEntityList);
		for (Entity entity : entities)
		{
			targetDistance = entity.getDistanceSq(target);
			if (targetDistance < distance && (predicate == null ? true : predicate.test(target, entity)))
			{
				result = entity;
				distance = targetDistance;
			}
		}
		
		return result;
	}
}
