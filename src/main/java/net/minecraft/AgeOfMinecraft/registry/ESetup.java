package net.minecraft.AgeOfMinecraft.registry;

import net.minecraft.AgeOfMinecraft.triggers.ConvertMobTrigger;
import net.minecraft.AgeOfMinecraft.triggers.SpawnMobTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;

public class ESetup 
{
	public static final EnumCreatureAttribute ENDER = EnumHelper.addCreatureAttribute("ENDER");
	public static final EnumCreatureAttribute CONSTRUCT = EnumHelper.addCreatureAttribute("CONSTRUCT");
	public static final EnumCreatureAttribute WITHER_STORM = EnumHelper.addCreatureAttribute("WITHER_STORM");
	public static final EnumRarity SUPEREPIC = EnumHelper.addRarity("SUPEREPIC", TextFormatting.DARK_PURPLE, "SuperEpic");
	public static final EnumRarity UBEREPIC = EnumHelper.addRarity("UBEREPIC", TextFormatting.GOLD, "UberEpic");
	
	public static final ConvertMobTrigger CONVERT_MOB = (ConvertMobTrigger)CriteriaTriggers.register(new ConvertMobTrigger());
	public static final SpawnMobTrigger SPAWN_MOB = (SpawnMobTrigger)CriteriaTriggers.register(new SpawnMobTrigger());
}
