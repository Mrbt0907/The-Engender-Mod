package net.mrbt0907.ageofminecraft.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.mrbt0907.ageofminecraft.EngenderMod;
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
		switch (commandID)
		{
		}
	}
}
