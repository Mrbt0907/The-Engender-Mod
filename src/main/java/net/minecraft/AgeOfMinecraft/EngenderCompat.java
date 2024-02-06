package net.minecraft.AgeOfMinecraft;

import net.minecraftforge.fml.common.Loader;

public class EngenderCompat
{
	public static boolean ICE_AND_FIRE_LOADED = false;
	public static boolean SCP_LOCKDOWN_LOADED = false;
	
	public static void preInit()
	{
		//ABYSSALCRAFT_LOADED = Loader.isModLoaded("abyssalcraft");
		//DRACONIC_EVOLUTION_LOADED = Loader.isModLoaded("draconicevolution");
		ICE_AND_FIRE_LOADED = Loader.isModLoaded("iceandfire");
		//MUTANT_BEASTS_LOADED = Loader.isModLoaded("mutantbeasts");
		//PRIMITIVE_MOBS_LOADED = Loader.isModLoaded("primitivemobs");
		SCP_LOCKDOWN_LOADED = Loader.isModLoaded("scp");
		//TEKTOPIA_LOADED = Loader.isModLoaded("tektopia");
		//TINKERS_CONSTRUCT_LOADED = Loader.isModLoaded("tconstruct");
		//TWILIGHT_FOREST_LOADED = Loader.isModLoaded("twilightforest");
	}
}
