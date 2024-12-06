package net.mrbt0907.ageofminecraft.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.util.mrbtutil.network.INetworkReciever;

public class EngenderNetworkReciever implements INetworkReciever
{
	@Override
	public String getID()
	{
		return EngenderMod.MODID;
	}

	@Override
	public void onClientRecieved(int commandID, NBTTagCompound nbt)
	{	
		switch (commandID)
		{
		}
	}

	@Override
	public void onServerRecieved(int commandID, NBTTagCompound nbt, EntityPlayerMP player)
	{
		List<Entity> entities = new ArrayList<Entity>(player.world.loadedEntityList);
		List<EntityEngendered> superEntities;
		switch (commandID)
		{
			case 0:
				superEntities = PacketCommandStaff.unpackEntities(player, nbt);
				for (EntityEngendered superEntity : superEntities)
					superEntity.setStance(nbt.getInteger("stance"));
				break;
			case 1: //Client sends order to attack
				UUID target = nbt.getUniqueId("target"); EntityLivingBase targetEntity = null;
				
				for (Entity entity : entities)
					if (entity.getUniqueID().equals(target) && entity.isEntityAlive() && entity instanceof EntityLivingBase)
					{
						if (player.isOnSameTeam(entity)) return;
						targetEntity = (EntityLivingBase) entity;
						break;
					}
				if (targetEntity != null)
				{
					superEntities = PacketCommandStaff.unpackEntities(player, nbt);
					for (EntityEngendered superEntity : superEntities)
					{
						superEntity.setAttackTarget(targetEntity);
						if (superEntity.getStance().equals(EnumAIStance.PASSIVE))
							superEntity.setStance(EnumAIStance.DEFENSIVE);
					}
				}
				break;
			case 2: //Client sends order to set units to follow
				superEntities = PacketCommandStaff.unpackEntities(player, nbt);
				for (EntityEngendered superEntity : superEntities)
				{
					superEntity.followPos = null;
					superEntity.setAttackTarget(null);
				}
				break;
			case 3: //Client sends order to move units
				BlockPos position = BlockPos.fromLong(nbt.getLong("position"));
				boolean shouldAdd = nbt.getBoolean("shouldAdd");
				superEntities = PacketCommandStaff.unpackEntities(player, nbt);
				
				for (EntityEngendered superEntity : superEntities)
				{
					superEntity.setAttackTarget(null);
					if (superEntity.followPos == null || !shouldAdd)
						superEntity.followPos = new BlockPos[] {position};
					else if (shouldAdd)
					{
						BlockPos[] followPos = new BlockPos[superEntity.followPos.length + 1];
						for (int i = 0; i < superEntity.followPos.length; i++)
							followPos[i] = superEntity.followPos[i];
						followPos[followPos.length - 1] = position;
						superEntity.followPos = followPos;
					}
				}
				break;
				
		}
	}
}
