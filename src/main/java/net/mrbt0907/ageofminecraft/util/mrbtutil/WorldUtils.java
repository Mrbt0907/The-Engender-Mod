package net.mrbt0907.ageofminecraft.util.mrbtutil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}
