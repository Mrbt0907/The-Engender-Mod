package net.minecraft.AgeOfMinecraft.nexudium;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class NexudiumClient extends Nexudium
{
	private static final Minecraft MC = Minecraft.getMinecraft();
	private static boolean inWorld;
	
	public static void onTick()
	{
		if (inWorld && MC.world == null)
		{
			inWorld = false;
			onWorldDisconnect();
		}
		else if (!inWorld && MC.world != null)
		{
			inWorld = true;
			onWorldJoin();
		}
		
		TEAMS.forEach((teamUUID, team) -> team.onUpdateClient());
	}
	
	public static void onWorldJoin()
	{
		
	}
	
	public static void onWorldDisconnect()
	{	
		reset();
	}
	
	public static void onRenderTick()
	{
		
	}
	
	public static void onDataRecieved(NBTTagCompound nbt)
	{
		readNBT(nbt);
	}
}
