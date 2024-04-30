package net.minecraft.AgeOfMinecraft.blocks;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.AgeOfMinecraft.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@SuppressWarnings("rawtypes")
public class MobSpawnerRecipes
{
	private static final MobSpawnerRecipes smeltingBase = new MobSpawnerRecipes();
	private Map<ItemStack, ItemStack> smeltingList = Maps.newHashMap();
	private Map<ItemStack, Float> experienceList = Maps.newHashMap();
	public static MobSpawnerRecipes instance()
	{
		return smeltingBase;
	}
	private MobSpawnerRecipes()
	{
		addSmelting(ItemRegistry.fusionItemBat, new ItemStack(ItemRegistry.batItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemChicken, new ItemStack(ItemRegistry.chickenItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemCow, new ItemStack(ItemRegistry.cowItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemMooshroom, new ItemStack(ItemRegistry.mooshroomItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemParrot, new ItemStack(ItemRegistry.parrotItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemPig, new ItemStack(ItemRegistry.pigItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemRabbit, new ItemStack(ItemRegistry.rabbitItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemSheep, new ItemStack(ItemRegistry.sheepItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemOzelot, new ItemStack(ItemRegistry.ozelotItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemSquid, new ItemStack(ItemRegistry.squidItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemVillager, new ItemStack(ItemRegistry.villagerItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemSnowman, new ItemStack(ItemRegistry.snowmanItem), 5.0F);
		addSmelting(ItemRegistry.fusionItemSilverfish, new ItemStack(ItemRegistry.silverfishItem), 15.0F);
		addSmelting(ItemRegistry.fusionItemEndermite, new ItemStack(ItemRegistry.endermiteItem), 18.0F);
		addSmelting(ItemRegistry.fusionItemWolf, new ItemStack(ItemRegistry.wolfItem), 18.0F);
		addSmelting(ItemRegistry.fusionItemSpider, new ItemStack(ItemRegistry.spiderItem), 20.0F);
		addSmelting(ItemRegistry.fusionItemZombie, new ItemStack(ItemRegistry.zombieItem), 22.0F);
		addSmelting(ItemRegistry.fusionItemSkeleton, new ItemStack(ItemRegistry.skeletonItem), 30.0F);
		addSmelting(ItemRegistry.fusionItemCreeper, new ItemStack(ItemRegistry.creeperItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemSlime, new ItemStack(ItemRegistry.slimeItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemMagmaCube, new ItemStack(ItemRegistry.magmacubeItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemSpiderJockey, new ItemStack(ItemRegistry.spiderjockeyItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemChickenJockey, new ItemStack(ItemRegistry.chickenjockeyItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemBlaze, new ItemStack(ItemRegistry.blazeItem), 500.0F);
		addSmelting(ItemRegistry.fusionItemEnderman, new ItemStack(ItemRegistry.endermanItem), 400.0F);
		addSmelting(ItemRegistry.fusionItemCaveSpider, new ItemStack(ItemRegistry.cavespiderItem), 100.0F);
		addSmelting(ItemRegistry.fusionItemPigZombie, new ItemStack(ItemRegistry.pigzombieItem), 200.0F);
		addSmelting(ItemRegistry.fusionItemGuardian, new ItemStack(ItemRegistry.guardianItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemGhast, new ItemStack(ItemRegistry.ghastItem), 700.0F);
		addSmelting(ItemRegistry.fusionItemWitch, new ItemStack(ItemRegistry.witchItem), 600.0F);
		addSmelting(ItemRegistry.fusionItemWitherSkeleton, new ItemStack(ItemRegistry.witherskeletonItem), 180.0F);
		addSmelting(ItemRegistry.fusionItemKillerRabbit, new ItemStack(ItemRegistry.killerrabbitItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemElderGuardian, new ItemStack(ItemRegistry.elderguardianItem), 10000.0F);
		addSmelting(ItemRegistry.fusionItemGiant, new ItemStack(ItemRegistry.giantItem), 8000.0F);
		addSmelting(ItemRegistry.fusionItemVillagerGolem, new ItemStack(ItemRegistry.villagergolemItem), 8000.0F);
		addSmelting(ItemRegistry.fusionItemEnderDragon, new ItemStack(ItemRegistry.enderdragonItem), 160000.0F);
		addSmelting(ItemRegistry.fusionItemWither, new ItemStack(ItemRegistry.witherItem), 160000.0F);
		addSmelting(ItemRegistry.fusionItemShulker, new ItemStack(ItemRegistry.shulkerItem), 300.0F);
		addSmelting(ItemRegistry.fusionItemSkeletonTrap, new ItemStack(ItemRegistry.the4horsemenItem), 12000.0F);
		addSmelting(ItemRegistry.fusionItemStray, new ItemStack(ItemRegistry.strayItem), 150.0F);
		addSmelting(ItemRegistry.fusionItemHusk, new ItemStack(ItemRegistry.huskItem), 150.0F);
		addSmelting(ItemRegistry.fusionItemPolarBear, new ItemStack(ItemRegistry.polarBearItem), 75.0F);
		addSmelting(ItemRegistry.fusionItemVex, new ItemStack(ItemRegistry.vexItem), 45.0F);
		addSmelting(ItemRegistry.fusionItemVindicator, new ItemStack(ItemRegistry.vindicatorItem), 300.0F);
		addSmelting(ItemRegistry.fusionItemLlama, new ItemStack(ItemRegistry.llamaItem), 10.0F);
		addSmelting(ItemRegistry.fusionItemEvoker, new ItemStack(ItemRegistry.evokerItem), 18000.0F);
		addSmelting(ItemRegistry.fusionItemIllusioner, new ItemStack(ItemRegistry.illusionerItem), 18000.0F);
		addSmelting(ItemRegistry.fusionItemEversource, new ItemStack(ItemRegistry.eversourceItem), 18000.0F);
		addSmelting(ItemRegistry.fusionItemIceSpider, new ItemStack(ItemRegistry.iceSpiderItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemPrisonSlime, new ItemStack(ItemRegistry.prisonSlimeItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemPrisonZombie, new ItemStack(ItemRegistry.prisonZombieItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemPrisonSpider, new ItemStack(ItemRegistry.prisonSpiderItem), 50.0F);
		addSmelting(ItemRegistry.fusionItemCreeder, new ItemStack(ItemRegistry.creederItem), 100.0F);
		addSmelting(ItemRegistry.fusionItemIcyEnderCreeper, new ItemStack(ItemRegistry.icyEnderCreeperItem), 400.0F);
		addSmelting(ItemRegistry.fusionItemIceGolem, new ItemStack(ItemRegistry.iceGolemItem), 8000.0F);
		addSmelting(ItemRegistry.fusionItemMagmaGolem, new ItemStack(ItemRegistry.magmaGolemItem), 8000.0F);
		addSmelting(ItemRegistry.fusionItemPrisonGolem, new ItemStack(ItemRegistry.prisonGolemItem), 8000.0F);
		addSmelting(ItemRegistry.fusionItemGhasther, new ItemStack(ItemRegistry.ghastherItem), 160000.0F);
		addSmelting(ItemRegistry.fusionItemAbomniableSnowman, new ItemStack(ItemRegistry.abomniableSnowmanItem), 160000.0F);
		addSmelting(ItemRegistry.fusionItemWitherStorm, new ItemStack(ItemRegistry.witherStormItem), 6000000.0F);
	}

	public void addSmeltingRecipeForBlock(Block input, ItemStack stack, float experience)
	{
		addSmelting(Item.getItemFromBlock(input), stack, experience);
	}

	public void addSmelting(Item input, ItemStack stack, float experience)
	{
		addSmeltingRecipe(new ItemStack(input), stack, experience);
	}

	public void addSmeltingRecipe(ItemStack input, ItemStack stack, float experience)
	{
		this.smeltingList.put(input, stack);
		this.experienceList.put(stack, Float.valueOf(experience));
	}

	
	public ItemStack getSmeltingResult(ItemStack stack)
	{
		Iterator<?> iterator = this.smeltingList.entrySet().iterator();
		Map.Entry entry;
		do
		{
			if (!iterator.hasNext())
			{
				return null;
			}
			entry = (Map.Entry)iterator.next();
		}
		while (!compareItemStacks(stack, (ItemStack)entry.getKey()));
		return (ItemStack)entry.getValue();
	}

	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return (stack2.getItem() == stack1.getItem()) && ((stack2.getMetadata() == 32767) || (stack2.getMetadata() == stack1.getMetadata()));
	}
	public Map<ItemStack, ItemStack> getSmeltingList()
	{
		return this.smeltingList;
	}
	public float getSmeltingExperience(ItemStack stack)
	{
		float ret = stack.getItem().getSmeltingExperience(stack);
		if (ret != -1.0F) { return ret;
	}
	Iterator<?> iterator = this.experienceList.entrySet().iterator();
	Map.Entry entry;
	do
	{
		if (!iterator.hasNext())
		{
			return 0.0F;
		}
		entry = (Map.Entry)iterator.next();
	}
	while (!compareItemStacks(stack, (ItemStack)entry.getKey()));
	return ((Float)entry.getValue()).floatValue();
}
}