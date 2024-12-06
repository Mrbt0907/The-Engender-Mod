package net.mrbt0907.ageofminecraft.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;

public class PacketCommandStaff
{
	public static void changeStance(List<EntityEngendered> entities, EnumAIStance stance)
	{
		if (entities == null || stance == null)
			EngenderMod.fatal(new NullPointerException("Entities or Stance was null"));
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("stance", stance.ordinal());
		nbt.setTag("entities", packEntities(entities));
		EngenderMod.NETWORK.sendToServer(0, nbt);
	}
	
	public static void orderAttack(List<EntityEngendered> entities, EntityLivingBase entity)
	{
		if (entity == null || entities == null)
			EngenderMod.fatal(new NullPointerException("Entity or Entities was null"));
		NBTTagCompound nbt = new NBTTagCompound(), nbtEntities = new NBTTagCompound();
		
		nbt.setUniqueId("target", entity.getUniqueID());
		for (EntityEngendered superEntity : entities)
			nbtEntities.setUniqueId(nbtEntities.getSize() + "", superEntity.getUniqueID());
		nbt.setTag("entities", packEntities(entities));
		
		EngenderMod.NETWORK.sendToServer(1, nbt);
	}
	
	public static void orderFollow(List<EntityEngendered> entities)
	{
		if (entities == null)
			EngenderMod.fatal(new NullPointerException("Entities was null"));
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setTag("entities", packEntities(entities));
		EngenderMod.NETWORK.sendToServer(2, nbt);
	}
	
	public static void orderMove(List<EntityEngendered> entities, BlockPos position)
	{
		orderMove(entities, position, false);
	}
	
	public static void orderMove(List<EntityEngendered> entities, BlockPos position, boolean shouldAddToPath)
	{
		if (entities == null || position == null)
			EngenderMod.fatal(new NullPointerException("Entities or BlockPos was null"));
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong("position", position.toLong());
		nbt.setBoolean("shouldAdd", shouldAddToPath);
		nbt.setTag("entities", packEntities(entities));
		EngenderMod.NETWORK.sendToServer(3, nbt);
	}
	
	private static NBTTagCompound packEntities(List<EntityEngendered> entities)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		for (EntityEngendered superEntity : entities)
			nbt.setUniqueId(nbt.getSize() + "", superEntity.getUniqueID());
		return nbt;
	}
	
	public static List<EntityEngendered> unpackEntities(EntityPlayer player, NBTTagCompound nbt)
	{
		List<Entity> entities = new ArrayList<Entity>(player.world.loadedEntityList);
		List<EntityEngendered> results = new ArrayList<EntityEngendered>(); EntityEngendered superEntity;
		NBTTagCompound nbtEntities = nbt.getCompoundTag("entities");
		Set<String> keys = nbtEntities.getKeySet();
		UUID uuid;
		
		for (String key : keys)
		{
			if (!key.matches("\\d+Most")) continue;
			uuid = nbtEntities.getUniqueId(key.replaceFirst("Most", ""));
			for (Entity entity : entities)
			{
				if (entity.getUniqueID().equals(uuid) && entity.isEntityAlive() && entity instanceof EntityEngendered)
				{
					superEntity = (EntityEngendered) entity;
					if (player.getUniqueID().equals(superEntity.getOwnerId()))
						results.add(superEntity);
					break;
				}
			}
		}
		
		return results;
	}
}
