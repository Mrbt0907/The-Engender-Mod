package net.minecraft.AgeOfMinecraft.nexudium;

import java.util.UUID;

import net.endermanofdoom.mac.util.FileUtil;
import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class NexudiumServer extends Nexudium
{	
	public static void onTick()
	{
		TEAMS.forEach((teamUUID, team) -> team.onUpdateServer());
	}
	
	public static void onPlayerLogin(EntityPlayerMP player)
	{
		if (!leaderExists(player.getUniqueID()))
			createTeam(UUID.randomUUID(), player.getPersistentID());
		syncData((EntityPlayerMP) player);
	}
	
	public static void onEntitySpawned(EntityLivingBase entity)
	{
		TEAMS.forEach((teamUUID, team) -> team.onEntitySpawned(entity));
	}
	
	public static void syncData()
	{
		EngenderMod.debug("Sending nexudium data to all players...");
		
		EngenderMod.network.sendToClients(0, writeNBT(new NBTTagCompound()));
	}
	
	public static void syncData(EntityPlayerMP player)
	{
		NBTTagCompound nbt = writeNBT(new NBTTagCompound());
		EngenderMod.debug("Sending nexudium data to player " + player.getName() + "... ");
		EngenderMod.network.sendToClients(0, nbt, player);
	}
	
	public static void loadData()
	{
		readNBT(FileUtil.loadCompactNBT(FileUtil.getWorldFolderPath() + FileUtil.getWorldFolderName() + "engender", "nexudium", true));
	}
	
	public static void saveData()
	{
		
		FileUtil.saveCompactNBT(FileUtil.getWorldFolderPath() + FileUtil.getWorldFolderName() + "engender", "nexudium", writeNBT(new NBTTagCompound()));
	}
}
