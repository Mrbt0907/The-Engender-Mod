package net.minecraft.AgeOfMinecraft.registry;
import net.minecraft.AgeOfMinecraft.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

public class EBlock
{
	public static BlockMonsterSpawnerSPC mob_spawner_spc;
	public static BlockGuardBlock guard_block;
	public static BlockBeaconSPC beacon_spc;
	public static void init()
	{
		Blocks.COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		Blocks.CHAIN_COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		Blocks.REPEATING_COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		Blocks.STRUCTURE_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		Blocks.BARRIER.setCreativeTab(CreativeTabs.MISC);
		mob_spawner_spc = new BlockMonsterSpawnerSPC();
		guard_block = new BlockGuardBlock();
		// beacon_spc = new BlockBeaconSPC();
		register();
	}
	public static void register()
	{
		GameData.register_impl(mob_spawner_spc.setRegistryName("mob_spawner_spc"));
		GameData.register_impl(guard_block.setRegistryName("guard_block"));
		 //GameData.register_impl(beacon_spc.setRegistryName("beacon_spc"));
		GameData.register_impl(new ItemBlock(mob_spawner_spc).setRegistryName(mob_spawner_spc.getRegistryName()));
		GameData.register_impl(new ItemBlock(guard_block).setRegistryName(guard_block.getRegistryName()));
		//GameData.register_impl(new ItemBlock(beacon_spc).setRegistryName(beacon_spc.getRegistryName()));
	}
	@SideOnly(Side.CLIENT)
	public static void registerRenders()
	{
		registerRender(mob_spawner_spc);
		registerRender(guard_block);
		//registerRender(beacon_spc);
	}
	@SideOnly(Side.CLIENT)
	public static void registerRender(Block block)
	{
		Item item = Item.getItemFromBlock(block);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("ageofminecraft:" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}


