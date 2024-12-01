package net.mrbt0907.ageofminecraft.registry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.blocks.*;

@SuppressWarnings("unused")
public class BlockRegistry
{
	private static IForgeRegistry<Block> registry;
	public static final BlockFusionCrafter fusionCrafter = new BlockFusionCrafter();
	public static final BlockGuardBlock guard_block = new BlockGuardBlock();
	
	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> event)
	{
		EngenderMod.debug("Registering blocks...");
		registry = event.getRegistry();
		
		Blocks.COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		Blocks.CHAIN_COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		Blocks.REPEATING_COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		Blocks.STRUCTURE_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		Blocks.BARRIER.setCreativeTab(CreativeTabs.MISC);
		
		addBlock("mob_spawner_spc", fusionCrafter, CreativeTabRegistry.ENGENDER);
		addBlock("guard_block", guard_block, CreativeTabRegistry.ENGENDER);
		registry = null;
	}
	
	private static void addTileEntity(String registryName, Class<? extends TileEntity> tile)
	{
		if (tile != null)
			GameRegistry.registerTileEntity(tile, new ResourceLocation(EngenderMod.MODID, registryName));
	}
	
	private static void addBlock(String registryName, Block block)
	{
		addBlock(registryName, null, block, null);
	}
	
	
	private static void addBlock(String registryName, Block block, CreativeTabs creativeTab)
	{
		addBlock(registryName, null, block, creativeTab);
	}
	
	
	private static void addBlock(String registryName, String oreDictName, Block block)
	{
		addBlock(registryName, oreDictName, block, null);
	}
	
	private static void addBlock(String registryName, String oreDictName, Block block, CreativeTabs creativeTab)
	{
		if (registry != null)
		{
			block.setRegistryName(new ResourceLocation(EngenderMod.MODID, registryName));
			block.setUnlocalizedName(registryName);
			
			if (oreDictName != null)
				OreDictionary.registerOre(oreDictName, block);
			if (creativeTab != null)
				block.setCreativeTab(creativeTab);
			
			registry.register(block);			
			
			ItemRegistry.addBlock(block);
			EngenderMod.debug("Registered block " + block.getRegistryName().getResourceDomain() + ":" + block.getRegistryName().getResourcePath());
			return;
		}
		
		EngenderMod.error("Registry event returned null");
	}
}