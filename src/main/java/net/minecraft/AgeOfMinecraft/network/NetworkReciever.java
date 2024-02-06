package net.minecraft.AgeOfMinecraft.network;

import net.endermanofdoom.mac.interfaces.INetworkReciever;
import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.nexudium.NexudiumClient;
import net.minecraft.nbt.NBTTagCompound;

public class NetworkReciever implements INetworkReciever
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
			case 0:
				EngenderMod.debug("Recieved Nexudium data from the server. Processing...");
				NexudiumClient.onDataRecieved(nbt);
				break;
			default:
				EngenderMod.warn("NetworkManager has recieved an unknown network message from the server with id of " + commandID + ". Skipping...");
		}
	}

	@Override
	public void onServerRecieved(int commandID, NBTTagCompound nbt)
	{
		switch (commandID)
		{
			default:
				EngenderMod.warn("NetworkManager has encountered an unknown network message from a client. Skipping...");
		}
	}
}
