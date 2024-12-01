package net.mrbt0907.ageofminecraft;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.mrbt0907.ageofminecraft.blocks.TileFusionCrafter;
import net.mrbt0907.ageofminecraft.registry.EnchantmentRegistry;
import net.mrbt0907.ageofminecraft.registry.EngenderSetup;
import net.mrbt0907.ageofminecraft.registry.EntityRegistry;
import net.mrbt0907.ageofminecraft.registry.FusionRecipeRegistry;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;
import net.mrbt0907.ageofminecraft.registry.SoundRegistry;
import net.mrbt0907.ageofminecraft.registry.SpawnerRegistry;

public class CommonProxy
{
	public void preInit(FMLPreInitializationEvent event)
	{
		new EngenderSetup();
		SpawnerRegistry.init();
		EnchantmentRegistry.init();
		LootRegistry.registerAllModdedLootTables();
		SoundRegistry.registerSounds();
		GameRegistry.registerTileEntity(TileFusionCrafter.class, new ResourceLocation(EngenderMod.MODID, "mob_spawner_spc"));
		EntityRegistry.registerEntity();
	}
	
	public void init(FMLInitializationEvent event) {}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		FusionRecipeRegistry.INSTANCE.postInit();
	}
}

		
		