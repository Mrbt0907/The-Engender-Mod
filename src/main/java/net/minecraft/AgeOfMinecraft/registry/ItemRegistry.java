package net.minecraft.AgeOfMinecraft.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityMooshroom;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityRabbit;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityIronGolem;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityCommandBlockWither;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityMagmaCube;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.items.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.Minecraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSimpleFoiled;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("unused")
public class ItemRegistry
{
	private static IForgeRegistry<Item> registry;
	private static final List<Block> blocks = new ArrayList<Block>();
	private static final Map<Integer, Map<String, Item>> fusions = new HashMap<Integer, Map<String, Item>>();
	private static final Map<Integer, Map<String, Item>> fusionSpawners = new HashMap<Integer, Map<String, Item>>();
	
	public static ItemManaCollector manaContainer = new ItemManaCollector(0);
	public static ItemManaCollector entropyContainer = new ItemManaCollector(1);
	public static ItemManaCollector artifact1 = new ItemManaCollector(2);
	public static Item witheredNetherStar = new ItemSimpleFoiled();
	public static Item woodencleaver = new ItemCleaver(ToolMaterial.WOOD);
	public static Item stonecleaver = new ItemCleaver(ToolMaterial.STONE);
	public static Item ironcleaver = new ItemCleaver(ToolMaterial.IRON);
	public static Item goldencleaver = new ItemCleaver(ToolMaterial.GOLD);
	public static Item diamondcleaver = new ItemCleaver(ToolMaterial.DIAMOND);
	public static Item statChecker = new ItemEngenderStatChecker();
	public static Item carrier = new ItemCarrier();
	public static Item heromaker = new ItemHeroMaker();
	public static Item lastchance = new ItemLastChance();
	public static Item trainingstick = new ItemTrainingStick();
	public static Item blowhorn = new ItemMoralHorn();
	public static Item blowhorn2 = new ItemDragonsHorn();
	public static Item convertingStaff = new ItemConvertingStaff();
	public static Item summoningStaff = new ItemSummoningStaff();
	public static Item commandingStaff = new ItemCommandingStaff();
	public static Item portalStaff = new ItemPortalStaff();
	public static ItemLearningBook learningBookBasic;
	public static ItemLearningBook learningBookBasicCombat;
	public static ItemLearningBook learningBookBasicCooking;
	public static ItemLearningBook learningBookBasicExercise;
	public static ItemLearningBook learningBookBasicKnowledge;
	public static ItemLearningBook learningBookModern;
	public static ItemLearningBook learningBookModernCombat;
	public static ItemLearningBook learningBookModernBrute;
	public static ItemLearningBook learningBookModernCooking;
	public static ItemLearningBook learningBookModernEating;
	public static ItemLearningBook learningBookModernExercise;
	public static ItemLearningBook learningBookModernRunning;
	public static ItemLearningBook learningBookModernKnowledge;
	public static ItemLearningBook learningBookModernPacifist;
	public static ItemLearningBook learningBookAdvanced;
	public static ItemLearningBook learningBookAdvancedCombat;
	public static ItemLearningBook learningBookAdvancedBrute;
	public static ItemLearningBook learningBookAdvancedWarrior;
	public static ItemLearningBook learningBookAdvancedCooking;
	public static ItemLearningBook learningBookAdvancedEating;
	public static ItemLearningBook learningBookAdvancedDieting;
	public static ItemLearningBook learningBookAdvancedExercise;
	public static ItemLearningBook learningBookAdvancedRunning;
	public static ItemLearningBook learningBookAdvancedLifting;
	public static ItemLearningBook learningBookAdvancedKnowledge;
	public static ItemLearningBook learningBookAdvancedPacifist;
	public static ItemLearningBook learningBookAdvancedWisdom;
	
	public static ItemLearningBook learningBookComplex;
	public static ItemLearningBook learningBookComplexCombat;
	public static ItemLearningBook learningBookComplexBrute;
	public static ItemLearningBook learningBookComplexWarrior;
	public static ItemLearningBook learningBookComplexCooking;
	public static ItemLearningBook learningBookComplexEating;
	public static ItemLearningBook learningBookComplexDieting;
	public static ItemLearningBook learningBookComplexExercise;
	public static ItemLearningBook learningBookComplexRunning;
	public static ItemLearningBook learningBookComplexLifting;
	public static ItemLearningBook learningBookComplexKnowledge;
	public static ItemLearningBook learningBookComplexPacifist;
	public static ItemLearningBook learningBookComplexWisdom;
	public static ItemLearningBook learningBookMaster;
	public static ItemLearningBook learningBookMasterCombat;
	public static ItemLearningBook learningBookMasterBrute;
	public static ItemLearningBook learningBookMasterWarrior;
	public static ItemLearningBook learningBookMasterCooking;
	public static ItemLearningBook learningBookMasterEating;
	public static ItemLearningBook learningBookMasterDieting;
	public static ItemLearningBook learningBookMasterExercise;
	public static ItemLearningBook learningBookMasterRunning;
	public static ItemLearningBook learningBookMasterLifting;
	public static ItemLearningBook learningBookMasterKnowledge;
	public static ItemLearningBook learningBookMasterPacifist;
	public static ItemLearningBook learningBookMasterWisdom;
	public static ItemLearningBook learningBookArtifactStrength;
	public static ItemLearningBook learningBookArtifactStamina;
	public static ItemLearningBook learningBookArtifactSpeed;
	public static ItemLearningBook learningBookArtifactIntellegence;
	public static ItemLearningBook learningBookArtifact;
	public static ItemFusion fusionItemBat;
	public static ItemFusion fusionItemChicken;
	public static ItemFusion fusionItemCow;
	public static ItemFusion fusionItemMooshroom;
	public static ItemFusion fusionItemParrot;
	public static ItemFusion fusionItemPig;
	public static ItemFusion fusionItemRabbit;
	public static ItemFusion fusionItemSheep;
	public static ItemFusion fusionItemOzelot;
	public static ItemFusion fusionItemSquid;
	public static ItemFusion fusionItemVillager;
	public static ItemFusion fusionItemSnowman;
	public static ItemFusion fusionItemSilverfish;
	public static ItemFusion fusionItemEndermite;
	public static ItemFusion fusionItemWolf;
	public static ItemFusion fusionItemSpider;
	public static ItemFusion fusionItemZombie;
	public static ItemFusion fusionItemSkeleton;
	public static ItemFusion fusionItemCreeper;
	public static ItemFusion fusionItemSlime;
	public static ItemFusion fusionItemMagmaCube;
	public static ItemFusion fusionItemSpiderJockey;
	public static ItemFusion fusionItemChickenJockey;
	public static ItemFusion fusionItemBlaze;
	public static ItemFusion fusionItemEnderman;
	public static ItemFusion fusionItemCaveSpider;
	public static ItemFusion fusionItemPigZombie;
	public static ItemFusion fusionItemGuardian;
	public static ItemFusion fusionItemGhast;
	public static ItemFusion fusionItemWitch;
	public static ItemFusion fusionItemWitherSkeleton;
	public static ItemFusion fusionItemKillerRabbit;
	public static ItemFusion fusionItemElderGuardian;
	public static ItemFusion fusionItemGiant;
	public static ItemFusion fusionItemVillagerGolem;
	public static ItemFusion fusionItemEnderDragon;
	public static ItemFusion fusionItemWither;
	public static ItemFusion fusionItemShulker;
	public static ItemFusion fusionItemSkeletonTrap;
	public static ItemFusion fusionItemStray;
	public static ItemFusion fusionItemHusk;
	public static ItemFusion fusionItemPolarBear;
	public static ItemFusion fusionItemVex;
	public static ItemFusion fusionItemVindicator;
	public static ItemFusion fusionItemLlama;
	public static ItemFusion fusionItemEvoker;
	public static ItemFusion fusionItemEversource;
	public static ItemFusion fusionItemIceSpider;
	public static ItemFusion fusionItemCreeder;
	public static ItemFusion fusionItemIcyEnderCreeper;
	public static ItemFusion fusionItemIceGolem;
	public static ItemFusion fusionItemMagmaGolem;
	public static ItemFusion fusionItemPrisonSlime;
	public static ItemFusion fusionItemPrisonZombie;
	public static ItemFusion fusionItemPrisonSpider;
	public static ItemFusion fusionItemPrisonGolem;
	public static ItemFusion fusionItemGhasther;
	public static ItemFusion fusionItemAbomniableSnowman;
	public static ItemFusion fusionItemIllusioner;
	public static ItemFusion fusionItemWitherStorm;
	public static ItemFusionSpawner batItem;
	public static ItemFusionSpawner chickenItem;
	public static ItemFusionSpawner cowItem;
	public static ItemFusionSpawner mooshroomItem;
	public static ItemFusionSpawner parrotItem;
	public static ItemFusionSpawner pigItem;
	public static ItemFusionSpawner rabbitItem;
	public static ItemFusionSpawner sheepItem;
	public static ItemFusionSpawner ozelotItem;
	public static ItemFusionSpawner squidItem;
	public static ItemFusionSpawner llamaItem;
	public static ItemFusionSpawner villagerItem;
	public static ItemFusionSpawner snowmanItem;
	public static ItemFusionSpawner silverfishItem;
	public static ItemFusionSpawner endermiteItem;
	public static ItemFusionSpawner wolfItem;
	public static ItemFusionSpawner spiderItem;
	public static ItemFusionSpawner zombieItem;
	public static ItemFusionSpawner skeletonItem;
	public static ItemFusionSpawner creeperItem;
	public static ItemFusionSpawner polarBearItem;
	public static ItemFusionSpawner slimeItem;
	public static ItemFusionSpawner magmacubeItem;
	public static ItemFusionSpawner vexItem;
	public static ItemFusionSpawner spiderjockeyItem;
	public static ItemFusionSpawner chickenjockeyItem;
	public static ItemFusionSpawner blazeItem;
	public static ItemFusionSpawner endermanItem;
	public static ItemFusionSpawner cavespiderItem;
	public static ItemFusionSpawner pigzombieItem;
	public static ItemFusionSpawner guardianItem;
	public static ItemFusionSpawner ghastItem;
	public static ItemFusionSpawner huskItem;
	public static ItemFusionSpawner shulkerItem;
	public static ItemFusionSpawner strayItem;
	public static ItemFusionSpawner witchItem;
	public static ItemFusionSpawner vindicatorItem;
	public static ItemFusionSpawner witherskeletonItem;
	public static ItemFusionSpawner killerrabbitItem;
	public static ItemFusionSpawner elderguardianItem;
	public static ItemFusionSpawner the4horsemenItem;
	public static ItemFusionSpawner evokerItem;
	public static ItemFusionSpawner giantItem;
	public static ItemFusionSpawner villagergolemItem;
	public static ItemFusionSpawner enderdragonItem;
	public static ItemFusionSpawner witherItem;
	public static ItemFusionSpawner eversourceItem;
	public static ItemFusionSpawner iceSpiderItem;
	public static ItemFusionSpawner creederItem;
	public static ItemFusionSpawner icyEnderCreeperItem;
	public static ItemFusionSpawner iceGolemItem;
	public static ItemFusionSpawner magmaGolemItem;
	public static ItemFusionSpawner prisonSlimeItem;
	public static ItemFusionSpawner prisonZombieItem;
	public static ItemFusionSpawner prisonSpiderItem;
	public static ItemFusionSpawner prisonGolemItem;
	public static ItemFusionSpawner ghastherItem;
	public static ItemFusionSpawner abomniableSnowmanItem;
	public static ItemFusionSpawner illusionerItem;
	public static ItemFusionSpawner witherStormItem;
	public static Item darkstonecleaver;
	public static Item abyssalnitecleaver;
	public static Item refinedcoraliumcleaver;
	public static Item dreadiumcleaver;
	public static Item ethaxiumcleaver;
	public static Item peGunLevel1;
	public static Item peGunLevel2;
	public static Item peGunLevel3;
	public static Item peGunLevel4;
	public static Item peGunLevel5;
	public static Item abyssalPortalStaff;
	public static ItemFusion fusionItemAbyssalZombie;
	public static ItemFusion fusionItemAbyssalniteGolem;
	public static ItemFusion fusionItemChagarothSpawn;
	public static ItemFusion fusionItemChagarothFist;
	public static ItemFusion fusionItemCoraliumSquid;
	public static ItemFusion fusionItemDreadAbyssalniteGolem;
	public static ItemFusion fusionItemDreadling;
	public static ItemFusion fusionItemDreadSpawn;
	public static ItemFusion fusionItemDepthsGhoul;
	public static ItemFusion fusionItemShadowCreature;
	public static ItemFusion fusionItemGreaterDreadSpawn;
	public static ItemFusion fusionItemOmotholGhoul;
	public static ItemFusion fusionItemShadowMonster;
	public static ItemFusion fusionItemShoggoth;
	public static ItemFusion fusionItemRemnant;
	public static ItemFusion fusionItemSpectralDragon;
	public static ItemFusion fusionItemAsorah;
	public static ItemFusion fusionItemChagaroth;
	public static ItemFusion fusionItemDreadguard;
	public static ItemFusion fusionItemGatekeeperMinion;
	public static ItemFusion fusionItemLesserDreadbeast;
	public static ItemFusion fusionItemShadowBeast;
	public static ItemFusion fusionItemSkeletonGoliath;
	public static ItemFusion fusionItemSacthoth;
	public static ItemFusionSpawner shadowCreatureItem;
	public static ItemFusionSpawner shadowMonsterItem;
	public static ItemFusionSpawner shadowBeastItem;
	public static ItemFusionSpawner sacthothItem;
	public static ItemFusionSpawner skeletonGoliathItem;
	public static ItemFusionSpawner dreadguardItem;
	public static ItemFusionSpawner depthsGhoulItem;
	public static ItemFusionSpawner omotholGhoulItem;
	public static ItemFusionSpawner chagarothSpawnItem;
	public static ItemFusionSpawner chagarothFistItem;
	public static ItemFusionSpawner dreadSpawnItem;
	public static ItemFusionSpawner greaterDreadSpawnItem;
	public static ItemFusionSpawner lesserDreadbeastItem;
	public static ItemFusionSpawner chagarothItem;
	public static ItemFusionSpawner abyssalniteGolemItem;
	public static ItemFusionSpawner dreadAbyssalniteGolemItem;
	public static ItemFusionSpawner remnantItem;
	public static ItemFusionSpawner abyssalZombieItem;
	public static ItemFusionSpawner dreadlingItem;
	public static ItemFusionSpawner shoggothItem;
	public static ItemFusionSpawner gatekeeperminionItem;
	public static ItemFusionSpawner coraliumSquidItem;
	public static ItemFusionSpawner spectralDragonItem;
	public static ItemFusionSpawner asorahItem;
	public static ItemFusion fusionItemJzahar;
	public static ItemFusionSpawner jzaharItem;
	
	public static Item draconicPortalStaff;
	public static ItemFusion fusionItemChaosGuardian;
	public static ItemFusionSpawner chaosGuardianItem;
	
	public static ItemFusion fusionPigSpider;
	public static ItemFusion fusionMutantSnowGolem;
	public static ItemFusion fusionMutantZombie;
	public static ItemFusion fusionMutantCreeper;
	public static ItemFusion fusionMutantSkeleton;
	public static ItemFusion fusionMutantEnderman;
	public static ItemFusionSpawner pigSpiderItem;
	public static ItemFusionSpawner mutantSnowGolemItem;
	public static ItemFusionSpawner mutantCreeperItem;
	public static ItemFusionSpawner mutantSkeletonItem;
	public static ItemFusionSpawner mutantZombieItem;
	public static ItemFusionSpawner mutantEndermanItem;
	
	public static final List<ItemLearningBook> SKILL_BOOKS = new ArrayList<ItemLearningBook>();
	
	public static void addBlock(Block block)
	{
		blocks.add(block);
	}
	
	public static void addEngenderedEntity(Class<? extends EntityFriendlyCreature> entityClass, int tier, int mana, int entropy, int fusionTime)
	{
		addEngenderedEntity(entityClass, tier, mana, entropy, fusionTime, SpawnerRegistry.SPAWN_NORMAL, null);
	}
	
	public static void addEngenderedEntity(Class<? extends EntityFriendlyCreature> entityClass, int tier, int mana, int entropy, int fusionTime, BiConsumer<EntityPlayer, Object[]> spawnMechanics)
	{
		addEngenderedEntity(entityClass, tier, mana, entropy, fusionTime, spawnMechanics, null);
	}
	
	public static void addEngenderedEntity(Class<? extends EntityFriendlyCreature> entityClass, int tier, int mana, int entropy, int fusionTime, Consumer<EntityFriendlyCreature> spawnMechanicsPost)
	{
		addEngenderedEntity(entityClass, tier, mana, entropy, fusionTime, SpawnerRegistry.SPAWN_NORMAL, spawnMechanicsPost);
	}
	
	public static void addEngenderedEntity(Class<? extends EntityFriendlyCreature> entityClass, int tier, int mana, int entropy, int fusionTime, BiConsumer<EntityPlayer, Object[]> spawnMechanics, Consumer<EntityFriendlyCreature> spawnMechanicsPost)
	{
		EntityEntry registryName = net.minecraftforge.fml.common.registry.EntityRegistry.getEntry(entityClass);
		String name = "";
		if (registryName == null)
		{
			EngenderMod.error("Could not register fusions for entity class " + entityClass + " because entity registry returned a null entity.");
			return;
		}
		name = registryName.getName().toLowerCase().replaceAll("[^a-z0-9]", "").replaceFirst("helpful$", "");
		
		addEngenderedEntity(entityClass, name, tier, mana, entropy, fusionTime, spawnMechanics, spawnMechanicsPost);
	}
	
	public static void addEngenderedEntity(Class<? extends EntityFriendlyCreature> entityClass, String entityName, int tier, int mana, int entropy, int fusionTime, BiConsumer<EntityPlayer, Object[]> spawnMechanics, Consumer<EntityFriendlyCreature> spawnMechanicsPost)
	{
		EntityEntry registryName = net.minecraftforge.fml.common.registry.EntityRegistry.getEntry(entityClass);
		if (registryName == null)
		{
			EngenderMod.error("Could not register fusions for entity class " + entityClass + " because entity registry returned a null entity.");
			return;
		}
		
		if (!fusions.containsKey(tier))
		{
			fusions.put(tier, new HashMap<String, Item>());
			fusionSpawners.put(tier, new HashMap<String, Item>());
		}
		ItemFusion fusion = null;
		ItemFusionSpawner fusionSpawner = null;
		if (mana > -1 && entropy > -1 && fusionTime > -1)
		{
			fusion = new ItemFusion(tier, mana, entropy, fusionTime);
			fusions.get(tier).put("fusion" + entityName, fusion);
		}
		fusionSpawner = spawnMechanicsPost == null ? new ItemFusionSpawner(entityClass, tier, spawnMechanics) : new ItemFusionSpawner(entityClass, tier, spawnMechanics, spawnMechanicsPost);
		fusionSpawners.get(tier).put(entityName, fusionSpawner);
		
		if (fusion != null)
			FusionRecipeRegistry.INSTANCE.addRecipe(new ItemStack(fusion), new ItemStack(fusionSpawner), mana, entropy, fusionTime);
	}
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event)
	{
		EngenderMod.debug("Registering items...");
		registry = event.getRegistry();
		for (Block block : blocks)
			addItem(block.getRegistryName().getResourcePath(), new ItemBlock(block));
		
		registerExtras();
		
		addItem("mana_collector", manaContainer, CreativeTabRegistry.ENGENDER_EQUIPMENT, 9);
		addItem("entropy_collector", entropyContainer, CreativeTabRegistry.ENGENDER_EQUIPMENT, 9);
		addItem("infinite_well_spring", artifact1, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("withered_nether_star", witheredNetherStar, CreativeTabRegistry.ENGENDER);
		addItem("wooden_cleaver", woodencleaver, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("stone_cleaver", stonecleaver, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("iron_cleaver", ironcleaver, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("golden_cleaver", goldencleaver, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("diamond_cleaver", diamondcleaver, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("statchecker", statChecker, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("carrier", carrier, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("heromaker", heromaker, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("last_chance", lastchance, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("trainingstick", trainingstick, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("moralhorn", blowhorn, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("enderdragonshorn", blowhorn2, CreativeTabRegistry.ENGENDER_EQUIPMENT);
		addItem("convertingstaff", convertingStaff, CreativeTabRegistry.ENGENDER_EQUIPMENT, 4);
		addItem("summoningstaff", summoningStaff, CreativeTabRegistry.ENGENDER_EQUIPMENT, 4);
		addItem("commandingstaff", commandingStaff, CreativeTabRegistry.ENGENDER_EQUIPMENT, 4);
		addItem("portalstaff", portalStaff, CreativeTabRegistry.ENGENDER_EQUIPMENT, 4);
		
		Integer[] fusionTiers = new Integer[fusionSpawners.size()];
		fusionTiers = fusionSpawners.keySet().toArray(fusionTiers);
		Arrays.sort(fusionTiers);
		for (int tier : fusionTiers)
		{
			for (Entry<String, Item> entry : fusionSpawners.get(tier).entrySet())
				addItem(entry.getKey(), entry.getValue(), CreativeTabRegistry.ENGENDER_FUSION);
		}
		for (int tier : fusionTiers)
		{
			for (Entry<String, Item> entry : fusions.get(tier).entrySet())
				addItem(entry.getKey(), entry.getValue(), CreativeTabRegistry.ENGENDER_FUSION);
		}
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(carrier, new BehaviorDefaultDispenseItem()
		{
			public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
			{
				EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
				double d0 = source.getX() + (double)enumfacing.getFrontOffsetX();
				double d1 = (double)((float)(source.getBlockPos().getY() + enumfacing.getFrontOffsetY()) + 0.2F);
				double d2 = source.getZ() + (double)enumfacing.getFrontOffsetZ();
				
				@SuppressWarnings("unused")
				Entity entity = ItemCarrier.spawnMob(source.getWorld(), stack, d0, d1, d2);
				
				super.dispenseStack(source, stack);
				return stack;
			}
		});
		
		blocks.clear();
		fusions.clear();
		fusionSpawners.clear();
	}
	
	private static void registerExtras()
	{
		if (!fusions.containsKey(4))
		{
			fusions.put(4, new HashMap<String, Item>());
			fusionSpawners.put(4, new HashMap<String, Item>());
		}
		
		ItemFusion witherFusion = new ItemFusion(4, 12000, 750, 540);
		ItemFusionSpawner wither = new ItemFusionSpawner(EntityWither.class, 4, SpawnerRegistry.SPAWN_NORMAL);
		fusions.get(4).put("fusionwither", witherFusion);
		fusions.get(4).put("witherboss", wither);
		FusionRecipeRegistry.INSTANCE.addRecipe(new ItemStack(witherFusion), new ItemStack(wither), witherFusion.manaCost, witherFusion.entropyCost, witherFusion.fusionTime);
		
	}
	
	private static void addItem(String registryName, Item item)
	{
		addItem(registryName, null, item, null, 0);
	}
	
	
	private static void addItem(String registryName, Item item, CreativeTabs creativeTab)
	{
		addItem(registryName, null, item, creativeTab, 0);
	}
	
	
	
	private static void addItem(String registryName, String oreDictName, Item item)
	{
		addItem(registryName, oreDictName, item, null, 0);
	}

	private static void addItem(String registryName, String oreDictName, Item item, CreativeTabs creativeTab)
	{
		addItem(registryName, oreDictName, item, null, 0);
	}
	
	private static void addItem(String registryName, Item item, int meta)
	{
		addItem(registryName, null, item, null, meta);
	}
	
	
	private static void addItem(String registryName, Item item, CreativeTabs creativeTab, int meta)
	{
		addItem(registryName, null, item, creativeTab, meta);
	}
	
	
	
	private static void addItem(String registryName, String oreDictName, Item item, int meta)
	{
		addItem(registryName, oreDictName, item, null, meta);
	}
	
	private static void addItem(String registryName, String oreDictName, Item item, CreativeTabs creativeTab, int meta)
	{
		if (registry != null)
		{
			item.setRegistryName(new ResourceLocation(EngenderMod.MODID, registryName));
			item.setUnlocalizedName(registryName);
			
			if (oreDictName != null)
				OreDictionary.registerOre(oreDictName, item);
			if (creativeTab != null)
				item.setCreativeTab(creativeTab);
			registry.register(item);			
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
				for (int i = 0;i <= meta;i++)
					net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, i, new net.minecraft.client.renderer.block.model.ModelResourceLocation(EngenderMod.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
			
			EngenderMod.debug("Registered item " + item.getRegistryName());
			return;
		}
		
		EngenderMod.error("Registry event returned null");
	}
		
		/**
		 * Create a Learning Book item [
		 * Tiers:
		 * 0 = Basic,* 1 = Modern,* 2 = Advanced,* 3 = Complex,* 4 = Master,* 5+ = Artifact ]
		 * @param LearningBook
		 * @param Name - Name of the book
		 * @param Description - Description of the book
		 * @param Durability - Max Available Successes
		 * @param Experience - Max Experience gained per read
		 * @param Strength - Max Experience gained per read
		 * @param Stamina - Max Experience gained per read
		 * @param Intellegence - Max Experience gained per read
		 * @param Dexterity - Max Experience gained per read
		 * @param Agility - Max Experience gained per read
		 */
		public static void createLearningBook(Item book, int tier, String name, String description, int durability, int experience, float strength, float stamina, float intelegence, float dexterity, float agility)
		{
			book = new ItemLearningBook(tier, name, description, durability, experience, strength, stamina, intelegence, dexterity, agility);
		}
	}