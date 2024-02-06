package net.minecraft.AgeOfMinecraft.nexudium;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.nbt.NBTTagCompound;

public class Nexudium
{
	protected static final Map<UUID, UUID> LEADERS = new LinkedHashMap<UUID, UUID>();
	protected static final Map<UUID, EngenderTeam> TEAMS = new LinkedHashMap<UUID, EngenderTeam>();
	
	public static void readNBT(NBTTagCompound nbt)
	{
		Set<String> teams = nbt.getKeySet();
		for (String teamUUID : teams)
		{
			UUID teamID = UUID.fromString(teamUUID);
			EngenderTeam team = getTeam(teamID);
			NBTTagCompound nbtTeam = nbt.getCompoundTag(teamUUID);
			
			if (team == null)
				team = createTeam(teamID, nbtTeam.hasUniqueId("leader") ? nbtTeam.getUniqueId("leader") : null);
			
			if (team.getLeaderUUID() != null)
				LEADERS.put(team.getLeaderUUID(), teamID);
			
			team.readNBT(nbtTeam);
		}
	}
	
	public static NBTTagCompound writeNBT(NBTTagCompound nbt)
	{
		for (Entry<UUID, EngenderTeam> entry : TEAMS.entrySet())
		{
			NBTTagCompound nbtTeam = entry.getValue().writeNBT(new NBTTagCompound());
			nbt.setTag(entry.getKey().toString(), nbtTeam);
		}
		return nbt;
	}
	
	public static EngenderTeam createTeam(UUID leader)
	{
		return createTeam(UUID.randomUUID(), leader);
	}
	
	public static EngenderTeam createTeam(@Nonnull UUID teamUUID, UUID leader)
	{
		EngenderTeam team = getTeam(teamUUID);
		if (team != null)
		{
			EngenderMod.warn("Could not create a new team as the specified team already exists. Returning existing team...");
			return team;
		}
		team = new EngenderTeam(teamUUID, leader);
		
		if (leader != null && !LEADERS.containsKey(leader))
			LEADERS.put(leader, teamUUID);
		
		TEAMS.put(teamUUID, team);
		
		EngenderMod.debug("Created a new team with uuid " + teamUUID);
		return team;
	}
	
	public static void removeTeam(@Nonnull UUID teamUUID)
	{
		EngenderTeam team = getTeam(teamUUID);
		if (team != null)
		{
			EngenderMod.debug("Removing team " + teamUUID + " from the Nexudium...");
			TEAMS.remove(teamUUID);
			UUID leaderUUID = team.getLeaderUUID();
			if (leaderUUID != null)
				LEADERS.remove(leaderUUID);
		}
		else
			EngenderMod.warn("Failed to remove team as team " + teamUUID + " does not exist. Skipping...");
	}
	
	public static void reset()
	{
		EngenderMod.debug("Nexudium is now resetting to a neutral state...");
		TEAMS.forEach((uuid, team) -> team.reset());
		TEAMS.clear();
		LEADERS.clear();
	}
	
	public static EngenderTeam getTeam(@Nonnull UUID teamUUID)
	{
		return TEAMS.get(teamUUID);
	}
	
	public static boolean teamExists(@Nonnull UUID teamUUID)
	{
		return TEAMS.containsKey(teamUUID);
	}
	
	public static boolean leaderExists(@Nonnull UUID leaderUUID)
	{
		return LEADERS.containsKey(leaderUUID);
	}
}
