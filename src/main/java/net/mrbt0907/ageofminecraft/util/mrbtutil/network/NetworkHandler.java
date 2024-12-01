package net.mrbt0907.ageofminecraft.util.mrbtutil.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.mrbt0907.ageofminecraft.EngenderMod;

public class NetworkHandler
{
	public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(EngenderMod.MODID);
	
	public static void preInit()
	{
		instance.registerMessage(PacketNBT.class, PacketNBT.class, 0, Side.CLIENT);
		instance.registerMessage(PacketNBT.class, PacketNBT.class, 0, Side.SERVER);
	}
	
	public static void register(INetworkReciever... recievers)
	{
		for(INetworkReciever reciever : recievers)
			PacketNBT.register(reciever);
	}
	
	public static boolean sendToClients(String recieverID, int commandID, NBTTagCompound nbt, Object... targets)
	{
		if (nbt == null)
		{
			EngenderMod.error(new NullPointerException("NBT was null"));
			return false;
		}
		
		return sendClientPacket(new PacketNBT(recieverID, commandID, nbt), targets);
	}
	
	public static boolean sendToServer(String recieverID, int commandID, NBTTagCompound nbt)
	{
		if (nbt == null)
		{
			EngenderMod.error(new NullPointerException("NBT was null"));
			return false;
		}
		
		return sendServerPacket(new PacketNBT(recieverID, commandID, nbt));
	}
	
	private static boolean sendClientPacket(IMessage message, Object... targets)
	{
		if (message == null) return false;
		if (targets.length > 0)
			for (Object target : targets)
			{
				if (target instanceof Integer)
					instance.sendToDimension(message, (int)targets[0]);
				else if (target instanceof World)
					instance.sendToDimension(message, ((World)targets[0]).provider.getDimension());
				else if (target instanceof EntityPlayerMP)
					instance.sendTo(message, (EntityPlayerMP)targets[0]);
			}
		else
			instance.sendToAll(message);
		
		return true;
	}
	
	private static boolean sendServerPacket(IMessage message)
	{
		if (message == null) return false;
		instance.sendToServer(message);
		return true;
	}
}