package net.minecraft.AgeOfMinecraft.registry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.endermanofdoom.mac.entity.EntityArrowEX;
import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EntityManaOrb;
import net.minecraft.AgeOfMinecraft.entity.EntityPortal;
import net.minecraft.AgeOfMinecraft.entity.EntityPortalLightning;
import net.minecraft.AgeOfMinecraft.entity.cameos.Darkness.EntityDarkProjectile;
import net.minecraft.AgeOfMinecraft.entity.cameos.Darkness.EntityDarkness;
import net.minecraft.AgeOfMinecraft.entity.tier1.*;
import net.minecraft.AgeOfMinecraft.entity.tier2.*;
import net.minecraft.AgeOfMinecraft.entity.tier3.*;
import net.minecraft.AgeOfMinecraft.entity.tier4.*;
import net.minecraft.AgeOfMinecraft.entity.tier5.*;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.EntityAreaEffectCloudOther;
import net.minecraft.AgeOfMinecraft.entity.tier6.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;


public class EntityRegistry
{
	private static int id = 0;
	public static void registerEntity()
	{
		createEngenderedEntity(EntityBat.class, "BatHelpful", 64, 0, 1, 0, 5); 
		createEngenderedEntity(EntityChicken.class, "ChickenHelpful", 64, 0, 1, 0, 5); 
		createEngenderedEntity(EntityCow.class, "CowHelpful", 64, 0, 2, 0, 7); 
		createEntity(EntityMooshroom.class, "MushroomcowHelpful", 64);
		addEngenderedVariant(EntityMooshroom.class, "mooshroom", 0, 4, 0, 5);
		createEngenderedEntity(EntityParrot.class, "ParrotHelpful", 64, 0, 1, 0, 10); 
		createEngenderedEntity(EntityPig.class, "PigHelpful", 64, 0, 2, 0, 7); 
		createEngenderedEntity(EntityRabbit.class, "RabbitHelpful", 64, 0, 1, 0, 6); 
		addEngenderedVariant(EntityRabbit.class, "killerbunny", 3, 200, 0, 24, rabbit -> ((EntityRabbit)rabbit).setRabbitType(99));
		createEngenderedEntity(EntitySheep.class, "SheepHelpful", 64, 0, 3, 0, 7); 
		createEngenderedEntity(EntityOcelot.class, "OzelotHelpful", 64, 0, 2, 0, 6); 
		createEngenderedEntity(EntitySquid.class, "SquidHelpful", 64, 0, 6, 0, 8); 
		createEngenderedEntity(EntityLlama.class, "LlamaHelpful", 64, 1, 4, 0, 16); 
		createEngenderedEntity(EntityVillager.class, "VillagerHelpful", 64, 1, 15, 0, 60); 
		createEngenderedEntity(EntitySnowman.class, "SnowmanHelpful", 64, 1, 4, 0, 10); 
		createEngenderedEntity(EntitySilverfish.class, "SilverfishHelpful", 64, 1, 5, 0, 8); 
		createEngenderedEntity(EntityEndermite.class, "EndermiteHelpful", 64, 1, 5, 0, 10); 
		createEngenderedEntity(EntityWolf.class, "WolfHelpful", 64, 1, 6, 0, 20); 
		createEngenderedEntity(EntitySpider.class, "SpiderHelpful", 64, 2, 8, 0, 20); 
		createEngenderedEntity(EntityZombie.class, "ZombieHelpful", 64, 2, 20, 0, 25); 
		addEngenderedVariant(EntityZombie.class, "chickenjockey", 2, 10, 0, 22, SpawnerRegistry.SPAWN_CHICKEN_JOCKEY, zombie -> {zombie.setChild(true); zombie.setGrowingAge(-48000);});
		addEngenderedVariant(EntityZombie.class, "husk", 3, 40, 0, 36, zombie -> ((EntityZombie)zombie).setZombieType(1));
		addEngenderedVariant(EntityZombie.class, "prisonzombie", 3, 60, 0, 40, zombie -> ((EntityZombie)zombie).setZombieType(2));
		createEngenderedEntity(EntitySkeleton.class, "SkeletonHelpful", 64, 2, 20, 0, 20); 
		addEngenderedVariant(EntitySkeleton.class, "spiderjockey", 2, 32, 0, 30, SpawnerRegistry.SPAWN_JOCKEY);
		addEngenderedVariant(EntitySkeleton.class, "stray", 3, 80, 0, 28, skeleton -> ((EntitySkeleton)skeleton).setSkeletonType(2));
		addEngenderedVariant(EntitySkeleton.class, "skeletontrap", 4, 2400, 80, 100, SpawnerRegistry.SPAWN_FOUR_HORSEMEN);
		addEngenderedVariant(EntitySkeleton.class, "witherskeleton", 3, 125, 0, 40, skeleton -> ((EntitySkeleton)skeleton).setSkeletonType(1));
		createEngenderedEntity(EntityCreeper.class, "CreeperHelpful", 64, 2, 25, 0, 30); 
		createEngenderedEntity(EntityPolarBear.class, "PolarBearHelpful", 64, 2, 30, 0, 32); 
		createEngenderedEntity(EntitySlime.class, "SlimeHelpful", 64, 2, 8, 0, 18); 
		createEntity(EntityMagmaCube.class, "LavaSlimeHelpful", 64);
		addEngenderedVariant(EntityMagmaCube.class, "magmacube", 2, 10, 0, 30);
		createEngenderedEntity(EntityPrisonSlime.class, "PrisonSlimeHelpful", 64, 2, 12, 0, 32); 
		createEngenderedEntity(EntityVex.class, "VexHelpful", 64, 2, 15, 0, 36); 
		createEngenderedEntity(EntityBlaze.class, "BlazeHelpful", 64, 3, 75, 0, 34); 
		createEngenderedEntity(EntityCaveSpider.class, "CaveSpiderHelpful", 64, 3, 40, 0, 32); 
		createEngenderedEntity(EntityCreeder.class, "CreederHelpful", 64, 3, 100, 0, 60); 
		createEngenderedEntity(EntityEnderman.class, "EndermanHelpful", 64, 3, 150, 0, 60); 
		createEngenderedEntity(EntityGhast.class, "GhastHelpful", 64, 3, 250, 0, 50); 
		createEngenderedEntity(EntityGuardian.class, "GuardianHelpful", 64, 3, 120, 0, 30); 
		createEngenderedEntity(EntityIceSpider.class, "IceSpiderHelpful", 64, 3, 30, 0, 25); 
		createEngenderedEntity(EntityIcyEnderCreeper.class, "IcyEnderCreeperHelpful", 64, 3, 100, 0, 45); 
		createEngenderedEntity(EntityPigZombie.class, "PigZombieHelpful", 64, 3, 80, 0, 28); 
		createEngenderedEntity(EntityPrisonSpider.class, "PrisonSpiderHelpful", 64, 3, 50, 0, 35); 
		createEngenderedEntity(EntityShulker.class, "ShulkerHelpful", 64, 3, 200, 0, 40); 
		createEngenderedEntity(EntityVindicator.class, "VindicatorHelpful", 64, 3, 150, 0, 45); 
		createEngenderedEntity(EntityWitch.class, "WitchHelpful", 64, 3, 100, 0, 38); 
		createEngenderedEntity(EntityAbomniableSnowman.class, "AbomniableSnowmanHelpful", 256, 4, 2500, 50, 300); 
		createEngenderedEntity(EntityElderGuardian.class, "ElderGuardianHelpful", 256, 4, 1500, 20, 60); 
		createEngenderedEntity(EntityEnderDragon.class, "EnderDragonHelpful", 256, 4, 24000, 500, 720); 
		createEngenderedEntity(EntityEversource.class, "EversourceHelpful", 256, 4, 5000, 1000, 240); 
		createEngenderedEntity(EntityEvoker.class, "EvokerHelpful", 256, 4, 6000, 350, 300); 
		createEngenderedEntity(EntityGhasther.class, "GhastherHelpful", 256, 4, 4000, 150, 460); 
		createEngenderedEntity(EntityGiant.class, "GiantHelpful", 256, 4, 2000, 30, 300); 
		createEngenderedEntity(EntityIceGolem.class, "IceGolemHelpful", 256, 4, 800, 10, 80); 
		createEngenderedEntity(EntityIllusioner.class, "IllusionerHelpful", 256, 4, 6000, 350, 160); 
		createEntity(EntityIronGolem.class, "VillagerGolemHelpful", 256);
		addEngenderedVariant(EntityIronGolem.class, "irongolem", 4, 1500, 20, 140);
		createEngenderedEntity(EntityMagmaGolem.class, "MagmaGolemHelpful", 256, 4, 1000, 10, 140); 
		createEngenderedEntity(EntityPrisonGolem.class, "PrisonGolemHelpful", 256, 4, 20, 0, 180); 
		createEntity(EntityWither.class, "WitherBossHelpful", 2048);
		createEntity(EntityCommandBlockWither.class, "WitherBossCommandBlockHelpful", 2048);
		addEngenderedVariant(EntityCommandBlockWither.class, "witherstorm", 5, 100000, 5000, 1200);
		createEngenderedEntity(EntityWitherStorm.class, "WitherStormBossHelpful", 2048, 6, -1, -1, -1);
		createEngenderedEntity(EntityDarkness.class, "darkness", 2048, 6, -1, -1, -1);
		createEntity(EntityPortal.class, "Portal", 2048);
		createEntity(EntityWitherStormHead.class, "WitherStormBossHeadHelpful", 2048);
		createEntity(EntityWitherStormTentacle.class, "WitherStormBossTentacleHelpful", 2048);
		createEntity(EntityWitherStormTentacleDevourer.class, "WitherStormBossTentacleDevourerHelpful", 2048);
		createEntity(EntityMagicMissile.class, "MagicMissile", 256);
		createEntity(EntityWitherStormSkull.class, "WitherStormSkull", 2048);
		createEntity(EntityPortalLightning.class, "PortalLightning", 256);
		createEntity(EntityDisintigrationRay.class, "DisintigrationRay", 64);
		createEntity(EntityFrostRay.class, "FrostRay", 64);
		createEntity(EntitySnowballHarmful.class, "SnowballHarmful", 256);
		createEntity(EntityAreaEffectCloudOther.class, "AreaEffectCloudOther", 64);
		createEntity(EntityTippedArrowOther.class, "TippedArrowOther", 256);
		createEntity(EntityInvisibleFangsProjectile.class, "InvisibleFangsProjectile", 512);
		createEntity(EntityManaOrb.class, "ManaOrb", 64);
		createEntity(EntityDarkProjectile.class, "darkball", 2048);
		createEntity(EntityArrowEX.class, "arrowEX", 64);
		
		EntitySpawnPlacementRegistry.setPlacementType(EntitySquid.class, EntityLiving.SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityGuardian.class, EntityLiving.SpawnPlacementType.IN_WATER);
		
		net.minecraftforge.fml.common.registry.EntityRegistry.addSpawn(net.minecraft.entity.monster.EntityEndermite.class, 20, 1, 4, EnumCreatureType.MONSTER, Biomes.SKY);
		net.minecraftforge.fml.common.registry.EntityRegistry.addSpawn(net.minecraft.entity.monster.EntityShulker.class, 1, 1, 1, EnumCreatureType.MONSTER, Biomes.SKY);
		net.minecraftforge.fml.common.registry.EntityRegistry.addSpawn(net.minecraft.entity.monster.EntityBlaze.class, 20, 1, 4, EnumCreatureType.MONSTER, Biomes.HELL);
		net.minecraftforge.fml.common.registry.EntityRegistry.addSpawn(net.minecraft.entity.monster.EntityWitherSkeleton.class, 10, 1, 4, EnumCreatureType.MONSTER, Biomes.HELL);
	}
	
	public static void createEngenderedEntity(Class<? extends EntityFriendlyCreature> entityClass, String entityName, int updateDistance, int tier, int mana, int entropy, int fusionTime)
	{
		createEntity(entityClass, entityName, updateDistance);
		ItemRegistry.addEngenderedEntity(entityClass, tier, mana, entropy, fusionTime);
	}
	
	public static void createEntityWithEgg(Class<? extends Entity> entityClass, String entityName, int primary, int secondary, int updateDistance)
	{
		createEntity(entityClass, entityName, updateDistance);
		net.minecraftforge.fml.common.registry.EntityRegistry.registerEgg(new ResourceLocation(EngenderMod.MODID, entityName), primary, secondary);
	}

	public static void createEntity(Class<? extends Entity> entityClass, String entityName, int updateDistance)
	{
		net.minecraftforge.fml.common.registry.EntityRegistry.registerModEntity(new ResourceLocation(EngenderMod.MODID, entityName), entityClass, entityName, ++id, EngenderMod.instance, updateDistance, 1, true);
	}
	
	public static void addEngenderedVariant(Class<? extends EntityFriendlyCreature> entityClass, String fusionName, int tier, int mana, int entropy, int fusionTime)
	{
		addEngenderedVariant(entityClass, fusionName, tier, mana, entropy, fusionTime, SpawnerRegistry.SPAWN_NORMAL, null);
	}
	
	public static void addEngenderedVariant(Class<? extends EntityFriendlyCreature> entityClass, String fusionName, int tier, int mana, int entropy, int fusionTime, BiConsumer<EntityPlayer, Object[]> spawnMechanics)
	{
		addEngenderedVariant(entityClass, fusionName, tier, mana, entropy, fusionTime, spawnMechanics, null);
	}
	
	public static void addEngenderedVariant(Class<? extends EntityFriendlyCreature> entityClass, String fusionName, int tier, int mana, int entropy, int fusionTime, Consumer<EntityFriendlyCreature> spawnMechanicsPost)
	{

		addEngenderedVariant(entityClass, fusionName, tier, mana, entropy, fusionTime, SpawnerRegistry.SPAWN_NORMAL, spawnMechanicsPost);
	}
	
	public static void addEngenderedVariant(Class<? extends EntityFriendlyCreature> entityClass, String fusionName, int tier, int mana, int entropy, int fusionTime, BiConsumer<EntityPlayer, Object[]> spawnMechanics, Consumer<EntityFriendlyCreature> spawnMechanicsPost)
	{
		ItemRegistry.addEngenderedEntity(entityClass, fusionName, tier, mana, entropy, fusionTime, spawnMechanics, spawnMechanicsPost);
	}
}