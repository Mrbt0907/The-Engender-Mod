package net.minecraft.AgeOfMinecraft.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;


public class MobChunkLoader implements LoadingCallback{
	public static MobChunkLoader instance;
	public static Map<Entity, Ticket> ticketList = new HashMap<>();
	public static Map<Entity, ArrayList<ChunkPos>> chunkList = new HashMap<>();
	public static boolean hasReportedIssue = false;
	
	public static void init()
	{
		instance = new MobChunkLoader();
		MinecraftForge.EVENT_BUS.register(instance);
		ForgeChunkManager.setForcedChunkLoadingCallback(EngenderMod.instance, instance);
	}

	public static void updateLoaded(Entity mob)
	{
		Ticket ticket;
		
		ArrayList<ChunkPos> dragonChunks = new ArrayList<>();
		for (int xx = ((int) mob.posX / 16) - 2; xx <= ((int) mob.posX / 16) + 2; xx++)
		for (int zz = ((int) mob.posZ / 16) - 2; zz <= ((int) mob.posZ / 16) + 2; zz++)
		dragonChunks.add(new ChunkPos(xx, zz));
		
		if (chunkList.containsKey(mob) && dragonChunks.hashCode() == chunkList.get(mob).hashCode())
		return;
		
		if (ticketList.containsKey(mob))
		{
			ticket = ticketList.get(mob);
			ForgeChunkManager.releaseTicket(ticket);
		}
		ticket = ForgeChunkManager.requestTicket(EngenderMod.instance, mob.world, ForgeChunkManager.Type.ENTITY);
		
		if (ticket != null)
		{
			ticket.bindEntity(mob);
			ticket.setChunkListDepth(25);
			ticketList.put(mob, ticket);
		}

		for (ChunkPos pos : dragonChunks)
		ForgeChunkManager.forceChunk(ticket, pos);
		
		chunkList.put(mob, dragonChunks);
	}

	public static void stopLoading(Entity guardian)
	{
		if (!ticketList.containsKey(guardian))
		return;
		Ticket ticket = ticketList.get(guardian);
		
		ForgeChunkManager.releaseTicket(ticket);
		
		ticketList.remove(guardian);
	}

	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		if (!tickets.isEmpty())
		for (Ticket ticket : tickets)
		ForgeChunkManager.releaseTicket(ticket);
	}
}
