package net.minecraft.AgeOfMinecraft.blocks;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.AgeOfMinecraft.registry.EItem;
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
		addSmelting(EItem.fusionItemBat, new ItemStack(EItem.batItem), 5.0F);
		addSmelting(EItem.fusionItemChicken, new ItemStack(EItem.chickenItem), 5.0F);
		addSmelting(EItem.fusionItemCow, new ItemStack(EItem.cowItem), 5.0F);
		addSmelting(EItem.fusionItemMooshroom, new ItemStack(EItem.mooshroomItem), 5.0F);
		addSmelting(EItem.fusionItemParrot, new ItemStack(EItem.parrotItem), 5.0F);
		addSmelting(EItem.fusionItemPig, new ItemStack(EItem.pigItem), 5.0F);
		addSmelting(EItem.fusionItemRabbit, new ItemStack(EItem.rabbitItem), 5.0F);
		addSmelting(EItem.fusionItemSheep, new ItemStack(EItem.sheepItem), 5.0F);
		addSmelting(EItem.fusionItemOzelot, new ItemStack(EItem.ozelotItem), 5.0F);
		addSmelting(EItem.fusionItemSquid, new ItemStack(EItem.squidItem), 5.0F);
		addSmelting(EItem.fusionItemVillager, new ItemStack(EItem.villagerItem), 5.0F);
		addSmelting(EItem.fusionItemSnowman, new ItemStack(EItem.snowmanItem), 5.0F);
		addSmelting(EItem.fusionItemSilverfish, new ItemStack(EItem.silverfishItem), 15.0F);
		addSmelting(EItem.fusionItemEndermite, new ItemStack(EItem.endermiteItem), 18.0F);
		addSmelting(EItem.fusionItemWolf, new ItemStack(EItem.wolfItem), 18.0F);
		addSmelting(EItem.fusionItemSpider, new ItemStack(EItem.spiderItem), 20.0F);
		addSmelting(EItem.fusionItemZombie, new ItemStack(EItem.zombieItem), 22.0F);
		addSmelting(EItem.fusionItemSkeleton, new ItemStack(EItem.skeletonItem), 30.0F);
		addSmelting(EItem.fusionItemCreeper, new ItemStack(EItem.creeperItem), 50.0F);
		addSmelting(EItem.fusionItemSlime, new ItemStack(EItem.slimeItem), 50.0F);
		addSmelting(EItem.fusionItemMagmaCube, new ItemStack(EItem.magmacubeItem), 50.0F);
		addSmelting(EItem.fusionItemSpiderJockey, new ItemStack(EItem.spiderjockeyItem), 50.0F);
		addSmelting(EItem.fusionItemChickenJockey, new ItemStack(EItem.chickenjockeyItem), 50.0F);
		addSmelting(EItem.fusionItemBlaze, new ItemStack(EItem.blazeItem), 500.0F);
		addSmelting(EItem.fusionItemEnderman, new ItemStack(EItem.endermanItem), 400.0F);
		addSmelting(EItem.fusionItemCaveSpider, new ItemStack(EItem.cavespiderItem), 100.0F);
		addSmelting(EItem.fusionItemPigZombie, new ItemStack(EItem.pigzombieItem), 200.0F);
		addSmelting(EItem.fusionItemGuardian, new ItemStack(EItem.guardianItem), 50.0F);
		addSmelting(EItem.fusionItemGhast, new ItemStack(EItem.ghastItem), 700.0F);
		addSmelting(EItem.fusionItemWitch, new ItemStack(EItem.witchItem), 600.0F);
		addSmelting(EItem.fusionItemWitherSkeleton, new ItemStack(EItem.witherskeletonItem), 180.0F);
		addSmelting(EItem.fusionItemKillerRabbit, new ItemStack(EItem.killerrabbitItem), 50.0F);
		addSmelting(EItem.fusionItemElderGuardian, new ItemStack(EItem.elderguardianItem), 10000.0F);
		addSmelting(EItem.fusionItemGiant, new ItemStack(EItem.giantItem), 8000.0F);
		addSmelting(EItem.fusionItemVillagerGolem, new ItemStack(EItem.villagergolemItem), 8000.0F);
		addSmelting(EItem.fusionItemEnderDragon, new ItemStack(EItem.enderdragonItem), 160000.0F);
		addSmelting(EItem.fusionItemWither, new ItemStack(EItem.witherItem), 160000.0F);
		addSmelting(EItem.fusionItemShulker, new ItemStack(EItem.shulkerItem), 300.0F);
		addSmelting(EItem.fusionItemSkeletonTrap, new ItemStack(EItem.the4horsemenItem), 12000.0F);
		addSmelting(EItem.fusionItemStray, new ItemStack(EItem.strayItem), 150.0F);
		addSmelting(EItem.fusionItemHusk, new ItemStack(EItem.huskItem), 150.0F);
		addSmelting(EItem.fusionItemPolarBear, new ItemStack(EItem.polarBearItem), 75.0F);
		addSmelting(EItem.fusionItemVex, new ItemStack(EItem.vexItem), 45.0F);
		addSmelting(EItem.fusionItemVindicator, new ItemStack(EItem.vindicatorItem), 300.0F);
		addSmelting(EItem.fusionItemLlama, new ItemStack(EItem.llamaItem), 10.0F);
		addSmelting(EItem.fusionItemEvoker, new ItemStack(EItem.evokerItem), 18000.0F);
		addSmelting(EItem.fusionItemIllusioner, new ItemStack(EItem.illusionerItem), 18000.0F);
		addSmelting(EItem.fusionItemEversource, new ItemStack(EItem.eversourceItem), 18000.0F);
		addSmelting(EItem.fusionItemIceSpider, new ItemStack(EItem.iceSpiderItem), 50.0F);
		addSmelting(EItem.fusionItemPrisonSlime, new ItemStack(EItem.prisonSlimeItem), 50.0F);
		addSmelting(EItem.fusionItemPrisonZombie, new ItemStack(EItem.prisonZombieItem), 50.0F);
		addSmelting(EItem.fusionItemPrisonSpider, new ItemStack(EItem.prisonSpiderItem), 50.0F);
		addSmelting(EItem.fusionItemCreeder, new ItemStack(EItem.creederItem), 100.0F);
		addSmelting(EItem.fusionItemIcyEnderCreeper, new ItemStack(EItem.icyEnderCreeperItem), 400.0F);
		addSmelting(EItem.fusionItemIceGolem, new ItemStack(EItem.iceGolemItem), 8000.0F);
		addSmelting(EItem.fusionItemMagmaGolem, new ItemStack(EItem.magmaGolemItem), 8000.0F);
		addSmelting(EItem.fusionItemPrisonGolem, new ItemStack(EItem.prisonGolemItem), 8000.0F);
		addSmelting(EItem.fusionItemGhasther, new ItemStack(EItem.ghastherItem), 160000.0F);
		addSmelting(EItem.fusionItemAbomniableSnowman, new ItemStack(EItem.abomniableSnowmanItem), 160000.0F);
		addSmelting(EItem.fusionItemWitherStorm, new ItemStack(EItem.witherStormItem), 6000000.0F);
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