package net.minecraft.AgeOfMinecraft.nexudium;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EngenderTeam
{
	protected final UUID healthModifier = UUID.fromString("809244b4-1ee3-4faa-9d7b-69bf3ca7fd98");
	protected EntityLivingBase leader;
	/**The owner of the team. Null means mother nature*/
	protected UUID leaderUUID;
	/**The unique id of the team.*/
	protected UUID teamUUID;
	
	public boolean hasHealthBoostI;
	
	public EngenderTeam(@Nonnull UUID teamUUID, UUID leader)
	{
		this.leaderUUID = leader;
		this.teamUUID = teamUUID;
	}
	
	public void readNBT(NBTTagCompound nbt)
	{
		leaderUUID = nbt.hasUniqueId("leader") ? nbt.getUniqueId("leader") : null;
		hasHealthBoostI = nbt.getBoolean("hasHealthBoostI");
	}
	
	public NBTTagCompound writeNBT(NBTTagCompound nbt)
	{
		if (leaderUUID != null)
			nbt.setUniqueId("leader", leaderUUID);
		nbt.setBoolean("hasHealthBoostI", hasHealthBoostI);
		return nbt;
	}
	
	public void onUpdateClient()
	{
		
	}
	
	public void onUpdateServer()
	{
		if (leaderUUID != null)
		{
			if (leader != null)
			{
				if (!leader.isEntityAlive())
				{
					hasHealthBoostI = true;
					leader = null;
				}
			}
		}
	}
	
	public void onEntitySpawned(EntityLivingBase entity)
	{
		if (leaderUUID != null && entity.getUniqueID().equals(leaderUUID))
		{
			//leader = entity;
			//IAttributeInstance instance = entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
			//AttributeModifier healthBoost = instance.getModifier(healthModifier);
			//if (hasHealthBoostI && healthBoost == null)
				//instance.applyModifier(new AttributeModifier(healthModifier, "engenderHealthIncrease", 60, 0));
		}
	}
	
	public void reset()
	{
		
	}
	
	public UUID getLeaderUUID()
	{
		return leaderUUID;
	}
}
