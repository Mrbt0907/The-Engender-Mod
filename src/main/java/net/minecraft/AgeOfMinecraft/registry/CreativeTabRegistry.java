package net.minecraft.AgeOfMinecraft.registry;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

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
		public ItemStack getTabIconItem() {return new ItemStack(ItemRegistry.witherStormItem);}
	};
}
