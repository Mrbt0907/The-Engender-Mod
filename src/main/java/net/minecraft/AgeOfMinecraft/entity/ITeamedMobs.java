package net.minecraft.AgeOfMinecraft.entity;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Team;

public abstract interface ITeamedMobs
{
	public abstract Team getTeam();
	public abstract UUID getOwnerId();
	public abstract Entity getOwner();
	public abstract EnumTier getTier();
}


