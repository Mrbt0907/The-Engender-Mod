package net.mrbt0907.ageofminecraft.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.mrbt0907.ageofminecraft.EngenderMod;

public class CreativeTabRegistry 
{
	public static final CreativeTabs ENGENDER = new CreativeTabs(EngenderMod.MODID)
	{
		@Override
		public ItemStack getTabIconItem() {return new ItemStack(BlockRegistry.fusionCrafter);}
	};

	public static final CreativeTabs ENGENDER_EQUIPMENT = new CreativeTabs(EngenderMod.MODID)
	{
		@Override
		public ItemStack getTabIconItem() {return new ItemStack(ItemRegistry.portalStaff);}
	};

	public static final CreativeTabs ENGENDER_FUSION = new CreativeTabs(EngenderMod.MODID)
	{
		@Override
		public ItemStack getTabIconItem() {return new ItemStack(ItemRegistry.FUSION_SPAWNERS.get("witherstorm"));}
	};
}
