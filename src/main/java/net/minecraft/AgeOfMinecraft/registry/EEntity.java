package net.minecraft.AgeOfMinecraft.registry;
import net.minecraft.AgeOfMinecraft.EngenderMod;
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
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;


public class EEntity
{
	private static int id = 0;
	public static void registerEntity()
	{
		createEntityWithEgg(EntityBat.class, "BatHelpful", 4996656, 986895, 64);
		createEntityWithEgg(EntityChicken.class, "ChickenHelpful", 10592673, 16711680, 64);
		createEntityWithEgg(EntityCow.class, "CowHelpful", 4470310, 10592673, 64);
		createEntityWithEgg(EntityMooshroom.class, "MushroomcowHelpful", 10489616, 12040119, 64);
		createEntityWithEgg(EntityParrot.class, "ParrotHelpful", 894731, 16711680, 64);
		createEntityWithEgg(EntityPig.class, "PigHelpful", 15771042, 14377823, 64);
		createEntityWithEgg(EntityRabbit.class, "RabbitHelpful", 10051392, 7555121, 64);
		createEntityWithEgg(EntitySheep.class, "SheepHelpful", 15198183, 16758197, 64);
		createEntityWithEgg(EntityOcelot.class, "OzelotHelpful", 15720061, 5653556, 64);
		createEntityWithEgg(EntitySquid.class, "SquidHelpful", 2243405, 7375001, 64);
		createEntityWithEgg(EntityLlama.class, "LlamaHelpful", 12623485, 10051392, 64);
		createEntityWithEgg(EntityVillager.class, "VillagerHelpful", 5651507, 12422002, 64);
		createEntityWithEgg(EntitySnowman.class, "SnowmanHelpful", 16382457, 14543594, 64);
		createEntityWithEgg(EntitySilverfish.class, "SilverfishHelpful", 7237230, 3158064, 64);
		createEntityWithEgg(EntityEndermite.class, "EndermiteHelpful", 1447446, 7237230, 64);
		createEntityWithEgg(EntityWolf.class, "WolfHelpful", 14144467, 13545366, 64);
		createEntityWithEgg(EntitySpider.class, "SpiderHelpful", 3419431, 11013646, 64);
		createEntityWithEgg(EntityZombie.class, "ZombieHelpful", 44975, 7969893, 64);
		createEntityWithEgg(EntitySkeleton.class, "SkeletonHelpful", 12698049, 4802889, 64);
		createEntityWithEgg(EntityCreeper.class, "CreeperHelpful", 894731, 0, 64);
		createEntityWithEgg(EntityPolarBear.class, "PolarBearHelpful", 15921906, 9803152, 64);
		createEntityWithEgg(EntitySlime.class, "SlimeHelpful", 5349438, 8306542, 64);
		createEntityWithEgg(EntityMagmaCube.class, "LavaSlimeHelpful", 3407872, 16579584, 64);
		createEntityWithEgg(EntityPrisonSlime.class, "PrisonSlimeHelpful", 0xb5301c, 0, 64);
		createEntityWithEgg(EntityVex.class, "VexHelpful", 8032420, 15265265, 64);
		createEntityWithEgg(EntityBlaze.class, "BlazeHelpful", 16167425, 16775294, 64);
		createEntityWithEgg(EntityCaveSpider.class, "CaveSpiderHelpful", 803406, 11013646, 64);
		createEntityWithEgg(EntityCreeder.class, "CreederHelpful", 0x4b3534, 0x2c2120, 64);
		createEntityWithEgg(EntityEnderman.class, "EndermanHelpful", 1447446, 0, 64);
		createEntityWithEgg(EntityGhast.class, "GhastHelpful", 16382457, 12369084, 64);
		createEntityWithEgg(EntityGuardian.class, "GuardianHelpful", 5931634, 15826224, 64);
		createEntityWithEgg(EntityIceSpider.class, "IceSpiderHelpful", 1433817, 57591, 64);
		createEntityWithEgg(EntityIcyEnderCreeper.class, "IcyEnderCreeperHelpful", 16382457, 0, 64);
		createEntityWithEgg(EntityPigZombie.class, "PigZombieHelpful", 15373203, 5009705, 64);
		createEntityWithEgg(EntityPrisonSpider.class, "PrisonSpiderHelpful", 3419431, 11013646, 64);
		createEntityWithEgg(EntityShulker.class, "ShulkerHelpful", 9725844, 5060690, 64);
		createEntityWithEgg(EntityVindicator.class, "VindicatorHelpful", 9804699, 2580065, 64);
		createEntityWithEgg(EntityWitch.class, "WitchHelpful", 3407872, 5349438, 64);
		createEntityWithEgg(EntityAbomniableSnowman.class, "AbomniableSnowmanHelpful", 16382457, 14543594, 256);
		createEntityWithEgg(EntityElderGuardian.class, "ElderGuardianHelpful", 13552826, 7632531, 256);
		createEntityWithEgg(EntityEnderDragon.class, "EnderDragonHelpful", 0x171717, 0xcc00fa, 2048);
		createEntityWithEgg(EntityEversource.class, "EversourceHelpful", 10592673, 10489616, 256);
		createEntityWithEgg(EntityEvoker.class, "EvokerHelpful", 9804699, 1973274, 256);
		createEntityWithEgg(EntityGhasther.class, "GhastherHelpful", 0x32231f, 0x251512, 256);
		createEntityWithEgg(EntityGiant.class, "GiantHelpful", 44975, 7969893, 256);
		createEntityWithEgg(EntityIceGolem.class, "IceGolemHelpful", 14144467, 14543594, 256);
		createEntityWithEgg(EntityIllusioner.class, "IllusionerHelpful", 1267859, 9804699, 256);
		createEntityWithEgg(EntityIronGolem.class, "VillagerGolemHelpful", 14144467, 14377823, 256);
		createEntityWithEgg(EntityMagmaGolem.class, "MagmaGolemHelpful", 3407872, 16167425, 256);
		createEntityWithEgg(EntityPrisonGolem.class, "PrisonGolemHelpful", 0x292828, 0x060606, 256);
		createEntityWithEgg(EntityWither.class, "WitherBossHelpful", 0x1a1a1a, 0x545454, 2048);
		createEntityWithEgg(EntityPortal.class, "Portal", 0x575757, 0x463a60, 2048);
		createEntityWithEgg(EntityCommandBlockWither.class, "WitherBossCommandBlockHelpful", 0x1a1a1a, 0x545454, 2048);
		createEntityWithEgg(EntityWitherStorm.class, "WitherStormBossHelpful", 986135, 1838892, 2048);
		createEntityWithEgg(EntityDarkness.class, "darkness", 0x171717, 0xcc00fa, 2048);
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
		
		EntitySpawnPlacementRegistry.setPlacementType(EntitySquid.class, EntityLiving.SpawnPlacementType.IN_WATER);
		EntitySpawnPlacementRegistry.setPlacementType(EntityGuardian.class, EntityLiving.SpawnPlacementType.IN_WATER);
		
		EntityRegistry.addSpawn(net.minecraft.entity.monster.EntityEndermite.class, 20, 1, 4, EnumCreatureType.MONSTER, Biomes.SKY);
		EntityRegistry.addSpawn(net.minecraft.entity.monster.EntityShulker.class, 1, 1, 1, EnumCreatureType.MONSTER, Biomes.SKY);
		EntityRegistry.addSpawn(net.minecraft.entity.monster.EntityBlaze.class, 20, 1, 4, EnumCreatureType.MONSTER, Biomes.HELL);
		EntityRegistry.addSpawn(net.minecraft.entity.monster.EntityWitherSkeleton.class, 10, 1, 4, EnumCreatureType.MONSTER, Biomes.HELL);
	}
	
	public static void createEntityWithEgg(Class<? extends Entity> entityClass, String entityName, int primary, int secondary, int updateDistance)
	{
		createEntity(entityClass, entityName, updateDistance);
		EntityRegistry.registerEgg(new ResourceLocation(EngenderMod.MODID, entityName), primary, secondary);
	}

	public static void createEntity(Class<? extends Entity> entityClass, String entityName, int updateDistance)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(EngenderMod.MODID, entityName), entityClass, entityName, ++id, EngenderMod.instance, updateDistance, 1, true);
	}
}