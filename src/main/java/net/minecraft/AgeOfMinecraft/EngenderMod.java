package net.minecraft.AgeOfMinecraft;

import org.apache.logging.log4j.Logger;

import net.endermanofdoom.mac.network.NetworkHandler;
import net.minecraft.AgeOfMinecraft.commands.CommandKillEngenderMobs;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumSoundType;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySquid;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.events.EngenderEventHandler;
import net.minecraft.AgeOfMinecraft.network.NetworkReciever;
import net.minecraft.AgeOfMinecraft.nexudium.NexudiumServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
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
import static net.minecraft.AgeOfMinecraft.EngenderCompat.*;

@Mod(modid=EngenderMod.MODID, name=EngenderMod.MODNAME, version=EngenderMod.VERSION, acceptedMinecraftVersions="[1.12.2]", dependencies="required-after:mac@[2.4,)")

public class EngenderMod
{
	public static final String MODNAME = "Engender - The Age of Minecraft";
	public static final String MODID = "ageofminecraft";
	public static final String VERSION = "0.8.5-indev";
	@SidedProxy(clientSide="net.minecraft.AgeOfMinecraft.ClientProxy", serverSide="net.minecraft.AgeOfMinecraft.CommonProxy")
	public static CommonProxy proxy;
	@Mod.Instance
	public static EngenderMod instance;
	private static Logger logger;
	public static final int statCheckerGUIID = 100;
	public static final int engenderfuserGUIID = 101;
	public static final NetworkReciever network = new NetworkReciever();
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		logger = e.getModLog();
		ConfigManager.sync("ageofminecraft", Config.Type.INSTANCE); 
		info("Loading The Engender Mod...");
		debug("Pre-Initialization started");
		NetworkHandler.register(network);
		EngenderCompat.preInit();
		EngenderMod.debug("Engender detected the following mods:\nIce and Fire: " + ICE_AND_FIRE_LOADED + "\nSCP - Lockdown: " + SCP_LOCKDOWN_LOADED);
		EngenderMod.debug("NOTE: ALL INTERNAL ADDONS HAVE BEEN REMOVED");
		
		NetworkRegistry.INSTANCE.registerGuiHandler(EngenderMod.instance, new CommonProxy());
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(EngenderEventHandler.INSTANCE);
		
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
		NexudiumServer.loadData();
		NexudiumServer.syncData();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) 
	{
		if (event.getModID().equals(MODID))
		{
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
			EntityFriendlyCreature.EXP_FACTOR = 1.0F / (float)EngenderConfig.mobs.levelFactor;
		}
	}

	/**
	* Returns true if the entity detects a Wither, false otherwise.
	*/
	public static boolean sensorsShowWithers(World world)
	{
		for (Entity entity : world.loadedEntityList)
			if (entity instanceof EntityWither)
				return true;
		return false;
	}

	public static boolean isWoodLikeMob(Entity entity)
	{
		return entity instanceof EntityWither || entity instanceof AbstractSkeleton || entity instanceof EntityShulker || entity instanceof EntityMooshroom ||
		entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entity).getSoundType().equals(EnumSoundType.WOOD);
	}

	public static boolean isMetalLikeMob(Entity entity)
	{
		return entity instanceof net.minecraft.entity.monster.EntityBlaze || entity instanceof net.minecraft.entity.monster.EntityIronGolem || entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entity).getSoundType().equals(EnumSoundType.METAL);
	}

	public static boolean canBeTurned(Entity entity)
	{
		return entity instanceof EntityPlayer || entity instanceof EntityZombie || entity instanceof EntitySquid ||
		entity instanceof net.minecraft.entity.monster.EntityZombie ||
		entity instanceof net.minecraft.entity.passive.EntitySquid ||
		entity instanceof EntitySkeleton ||
		entity instanceof net.minecraft.entity.monster.AbstractSkeleton ||
		entity instanceof EntityCreeper ||
		entity instanceof net.minecraft.entity.monster.EntityCreeper ||
		entity instanceof EntityVillager ||
		entity instanceof net.minecraft.entity.passive.EntityVillager;
	}
	
	//They ain't got time to bleed
	public static boolean doesntHaveTimeToBleed(Entity entity)
	{
		return entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isEntityUndead() || 
		entity instanceof net.minecraft.entity.monster.EntityBlaze ||
		entity instanceof net.minecraft.entity.monster.EntitySlime ||
		entity instanceof net.minecraft.entity.monster.EntityGolem ||
		entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entity).isUndead();
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