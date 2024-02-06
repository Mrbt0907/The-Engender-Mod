package net.minecraft.AgeOfMinecraft.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DungeonHooks;

public class ESpawner 
{
	public static void init()
	{
		DungeonHooks.addDungeonMob(new ResourceLocation("creeper"), 25);
		DungeonHooks.addDungeonMob(new ResourceLocation("cave_spider"), 50);
		DungeonHooks.addDungeonMob(new ResourceLocation("silverfish"), 50);
		DungeonHooks.addDungeonMob(new ResourceLocation("enderman"), 10);
		DungeonHooks.addDungeonMob(new ResourceLocation("wither_skeleton"), 1);
		DungeonHooks.addDungeonMob(new ResourceLocation("endermite"), 1);
		DungeonHooks.addDungeonMob(new ResourceLocation("blaze"), 1);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","zombiehelpful"), 50);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","skeletonhelpful"), 25);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","spiderhelpful"), 25);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","creeperhelpful"), 10);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","silverfishhelpful"), 10);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","endermanhelpful"), 1);
		DungeonHooks.addDungeonMob(new ResourceLocation("ageofminecraft","blazehelpful"), 10);
	}
}
