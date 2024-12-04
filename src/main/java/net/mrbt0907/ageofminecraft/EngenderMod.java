package net.mrbt0907.ageofminecraft;

import static net.mrbt0907.ageofminecraft.EngenderCompat.*;

import org.apache.logging.log4j.Logger;

import net.minecraft.world.GameRules;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.mrbt0907.ageofminecraft.commands.CommandKillEngenderMobs;
import net.mrbt0907.ageofminecraft.entity.EntityFriendlyCreature;
import net.mrbt0907.ageofminecraft.events.EngenderEventHandler;
import net.mrbt0907.ageofminecraft.gui.EngenderGuiHandler;
import net.mrbt0907.ageofminecraft.items.capabilities.CapabilityManager;
import net.mrbt0907.ageofminecraft.network.EngenderNetworkReciever;
import net.mrbt0907.ageofminecraft.registry.BlockRegistry;
import net.mrbt0907.ageofminecraft.registry.ItemRegistry;
import net.mrbt0907.ageofminecraft.util.mrbtutil.network.NetworkHandler;

@Mod(modid=EngenderMod.MODID, name=EngenderMod.MODNAME, version=EngenderMod.VERSION, acceptedMinecraftVersions="[1.12.2]", dependencies="required-after:mac@[2.5,)")

public class EngenderMod
{
	@Mod.Instance
	public static EngenderMod instance;
	@SidedProxy(clientSide="net.mrbt0907.ageofminecraft.ClientProxy", serverSide="net.mrbt0907.ageofminecraft.CommonProxy")
	public static CommonProxy proxy;
	public static final String MODNAME = "Engender - The Age of Minecraft";
	public static final String MODID = "ageofminecraft";
	public static final String VERSION = "1.0.0";
	
	private static Logger logger;
	public static final EngenderNetworkReciever network = new EngenderNetworkReciever();
	public static final int statCheckerGUIID = 100;
	public static final int engenderfuserGUIID = 101;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		logger = e.getModLog();
		ConfigManager.sync(MODID, Config.Type.INSTANCE); 
		info("Loading The Engender Mod...");
		debug("Pre-Initialization started");
		EngenderCompat.preInit();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(EngenderEventHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.register(BlockRegistry.class);
		MinecraftForge.EVENT_BUS.register(ItemRegistry.class);
		MinecraftForge.EVENT_BUS.register(CapabilityManager.class);
		NetworkHandler.register(network);
		NetworkRegistry.INSTANCE.registerGuiHandler(EngenderMod.instance, new EngenderGuiHandler());
		EngenderMod.debug("Engender detected the following mods:\nIce and Fire: " + ICE_AND_FIRE_LOADED + "\nSCP - Lockdown: " + SCP_LOCKDOWN_LOADED);
		EngenderMod.debug("NOTE: ALL INTERNAL ADDONS HAVE BEEN REMOVED");
		CapabilityManager.preInit();
		proxy.preInit(e);
		debug("Pre-Initialization finished");
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		debug("Initialization started");
		proxy.init(e);
		debug("Initialization finished");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		debug("Post-Initialization started!");
		proxy.postInit(e);
		debug("Post-Initialization finished");
		info("Finished The Engender Mod!");
	}
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CommandKillEngenderMobs());
		for (WorldServer world : e.getServer().worlds)
		{
			GameRules gamerule = world.getGameRules();
			if (!gamerule.hasRule("friendlyFire"))
				gamerule.addGameRule("friendlyFire", "false", GameRules.ValueType.BOOLEAN_VALUE);
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) 
	{
		if (event.getModID().equals(MODID))
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
	}

	public static void info(Object message)
	{
		logger.info(message);
	}
	
	public static void debug(Object message)
	{
		if (EngenderConfig.debugMode)
			logger.info("[DEBUG] " + message);
	}
	
	public static void warn(Object message)
	{
		if (EngenderConfig.debugMode)
			logger.warn(message);
	}

	public static void error(Object message)
	{
		if (EngenderConfig.debugMode)
		{
			Throwable exception;
			
				if (message instanceof Throwable)
					exception = (Throwable) message;
				else
					exception = new Exception(String.valueOf(message));

				exception.printStackTrace();
		}
	}
	
	public static void fatal(Object message)
	{
		Error error;
		
		if (message instanceof Error)
			error = (Error) message;
		else
			error = new Error(String.valueOf(message));
		
		throw error;
	}
}