package net.minecraft.AgeOfMinecraft.registry;

import net.minecraft.AgeOfMinecraft.triggers.ConvertMobTrigger;
import net.minecraft.AgeOfMinecraft.triggers.SpawnMobTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class EngenderSetup 
{
	public static final ConvertMobTrigger CONVERT_MOB = (ConvertMobTrigger)CriteriaTriggers.register(new ConvertMobTrigger());
	public static final SpawnMobTrigger SPAWN_MOB = (SpawnMobTrigger)CriteriaTriggers.register(new SpawnMobTrigger());
}
