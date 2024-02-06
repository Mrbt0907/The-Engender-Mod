package net.minecraft.AgeOfMinecraft.registry;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ETab 
{
	public static CreativeTabs engender;
	public static CreativeTabs abyssal;
	public static CreativeTabs draconic;
	public static CreativeTabs mutant;
	
	public static void init()
	{
		engender = new CreativeTabs(EngenderMod.MODID)
		{
			@Override
			public ItemStack getTabIconItem() {return new ItemStack(EItem.witherStormItem);}
		};
	}
}
