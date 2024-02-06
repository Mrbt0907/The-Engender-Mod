package net.minecraft.AgeOfMinecraft.util;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

public class RegistryHelper 
{
	public static CreativeTabs createTab(String name, Item item)
	{
		return createTab(name, "minecraft", item);
	}
	
	public static CreativeTabs createTab(String name, String modid, Item item)
	{
		if (modid != null && Loader.isModLoaded(modid))
		{
			CreativeTabs tab = new CreativeTabs(EngenderMod.MODID + ((name != null) ? "_" + name : ""))
			{
				@Override
				public ItemStack getTabIconItem() {return new ItemStack(item);}
			};
			return tab;
		}
		else
			return null;
	}
	
	public static void impl(Block block, String name)
	{
		impl(block, name, ETab.engender);
	}
	
	public static void impl(Block block, String name, CreativeTabs tab)
	{
		GameData.register_impl(block.setRegistryName(name).setUnlocalizedName(name).setCreativeTab(tab));
		GameData.register_impl(new ItemBlock(block).setRegistryName(name));
	}
	
	public static void impl(Block[] blocks, String[] names)
	{
		impl(blocks, names, ETab.engender);
	}
	
	public static void impl(Block[] blocks, String[] names, CreativeTabs tab)
	{
		Block block; String name;
		if (names.length != blocks.length) {EngenderMod.warn("Block array (" + blocks[0].toString() + ") does not have enough names.  Skipping..."); return;}
		for (int i = 0; i <= blocks.length-1; i++) 
		{
			block = blocks[i];	name = names[i];
			GameData.register_impl(block.setRegistryName(name).setUnlocalizedName(name).setCreativeTab(tab));
			GameData.register_impl(new ItemBlock(block).setRegistryName(name));
		}
	}
	
	public static void impl(Block block, String name, String local_name)
	{
		impl(block, name, local_name, ETab.engender);
	}
	
	public static void impl(Block block, String name, String local_name, CreativeTabs tab)
	{
		GameData.register_impl(block.setRegistryName(name).setUnlocalizedName(local_name).setCreativeTab(tab));
		GameData.register_impl(new ItemBlock(block).setRegistryName(name));
	}
	
	public static void impl(Block[] blocks, String[] names, String[] local_names)
	{
		impl(blocks, names, local_names, ETab.engender);
	}
	
	public static void impl(Block[] blocks, String[] names, String[] local_names, CreativeTabs tab)
	{
		Block block; String name; String local_name;
		if (names.length != blocks.length) {EngenderMod.warn("Block array (" + blocks[0].toString() + ") does not have enough names.  Skipping..."); return;}
		for (int i = 0; i <= blocks.length-1; i++) 
		{
			block = blocks[i]; name = names[i]; local_name = (i > local_names.length-1) ? name : local_names[i];
			GameData.register_impl(block.setRegistryName(name).setUnlocalizedName(local_name).setCreativeTab(tab));
			GameData.register_impl(new ItemBlock(block).setRegistryName(name));
		}
	}
	
	public static void impl(Item item, String name)
	{
		impl(item, name, ETab.engender);
	}
	
	public static void impl(Item item, String name, CreativeTabs tab)
	{
		GameData.register_impl(item.setRegistryName(name).setUnlocalizedName(name).setCreativeTab(tab));
	}
	
	public static void impl(Item[] items, String[] names)
	{
		impl(items, names, ETab.engender);
	}
	
	public static void impl(Item[] items, String[] names, CreativeTabs tab)
	{
		Item item; String name;
		if (names.length != items.length) {EngenderMod.warn("Item array (" + items[0].toString() + ") does not have enough names.  Skipping..."); return;}
		for (int i = 0; i <= items.length-1; i++) 
		{
			item = items[i]; name = names[i];
			GameData.register_impl(item.setRegistryName(name).setUnlocalizedName(name).setCreativeTab(tab));
		}
	}
	
	public static void impl(Item item, String name, String local_name)
	{
		impl(item, name, local_name, ETab.engender);
	}
	
	public static void impl(Item item, String name, String local_name, CreativeTabs tab)
	{
		GameData.register_impl(item.setRegistryName(name).setUnlocalizedName(local_name).setCreativeTab(tab));
	}
	
	public static void impl(Item[] items, String[] names, String[] local_names)
	{
		impl(items, names, local_names, ETab.engender);
	}
	
	public static void impl(Item[] items, String[] names, String[] local_names, CreativeTabs tab)
	{
		Item item; String name; String local_name;
		if (names.length != items.length) {EngenderMod.warn("Item array (" + items[0].toString() + ") does not have enough names.  Skipping..."); return;}
		for (int i = 0; i <= items.length-1; i++) 
		{
			item = items[i]; name = names[i]; local_name = (i > local_names.length-1) ? name : local_names[i];
			GameData.register_impl(item.setRegistryName(name).setUnlocalizedName(local_name).setCreativeTab(tab));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void render(Block block) 
	{
		render(block, 0);
	}
	
	@SideOnly(Side.CLIENT)
	public static void render(Block block, int meta) 
	{
		Item item = Item.getItemFromBlock(block);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(EngenderMod.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
	
	@SideOnly(Side.CLIENT)
	public static void render(Block[] blocks) 
	{
		Block block; Item item;
		for (int i=0; i <= blocks.length-1; i++)
		{
			block = blocks[i]; item = Item.getItemFromBlock(block);
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(EngenderMod.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void render(Block block, int[] metas) 
	{
		Item item; int meta;
		for (int i=0; i <= metas.length-1; i++)
		{
			item = Item.getItemFromBlock(block); meta = metas[i];
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(EngenderMod.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void render(Item item)
	{
		render(item, 0);
	}
	
	@SideOnly(Side.CLIENT)
	public static void render(Item item, int meta)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(EngenderMod.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
	
	@SideOnly(Side.CLIENT)
	public static void render(Item[] items) 
	{
		Item item;
		for (int i=0; i <= items.length-1; i++)
		{
			item = items[i];
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(EngenderMod.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void render(Item item, int[] metas) 
	{
		int meta;
		for (int i=0; i <= metas.length-1; i++)
		{
			meta = metas[i];
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(EngenderMod.MODID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
		}
	}
}