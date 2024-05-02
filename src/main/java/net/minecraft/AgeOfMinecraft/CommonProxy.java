package net.minecraft.AgeOfMinecraft;

import net.minecraft.AgeOfMinecraft.blocks.TileFusionCrafter;
import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.registry.PotionRegistry;
import net.minecraft.AgeOfMinecraft.registry.EnchantmentRegistry;
import net.minecraft.AgeOfMinecraft.registry.EntityRegistry;
import net.minecraft.AgeOfMinecraft.registry.FusionRecipeRegistry;
import net.minecraft.AgeOfMinecraft.registry.LootRegistry;
import net.minecraft.AgeOfMinecraft.registry.EngenderSetup;
import net.minecraft.AgeOfMinecraft.registry.SoundRegistry;
import net.minecraft.AgeOfMinecraft.registry.SpawnerRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		new EngenderSetup();
		SpawnerRegistry.init();
		PotionRegistry.registerPotions();
		EnchantmentRegistry.init();
		LootRegistry.registerAllModdedLootTables();
		SoundRegistry.registerSounds();
		GameRegistry.registerTileEntity(TileFusionCrafter.class, new ResourceLocation(EngenderMod.MODID, "mob_spawner_spc"));
		MobChunkLoader.init();
		EntityRegistry.registerEntity();
	}
	
	public void init(FMLInitializationEvent event) {}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		FusionRecipeRegistry.INSTANCE.postInit();
	}
}

		
		