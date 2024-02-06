package net.minecraft.AgeOfMinecraft;

import net.minecraft.AgeOfMinecraft.blocks.ContainerMobSpawner;
import net.minecraft.AgeOfMinecraft.blocks.TileEntityMonsterSpawnerSPC;
import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.gui.GuiEngenderFusionCrafter;
import net.minecraft.AgeOfMinecraft.gui.GuiEngenderMobInventory;
import net.minecraft.AgeOfMinecraft.items.ItemEngenderStatChecker;
import net.minecraft.AgeOfMinecraft.registry.EEffect;
import net.minecraft.AgeOfMinecraft.registry.EEnchant;
import net.minecraft.AgeOfMinecraft.registry.EEntity;
import net.minecraft.AgeOfMinecraft.registry.EItem;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.registry.ESpawner;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.AgeOfMinecraft.registry.EBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class CommonProxy implements IGuiHandler
{
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		player.getHeldItem(EnumHand.MAIN_HAND);
		
		switch(ID)
		{
			case EngenderMod.engenderfuserGUIID:
			if(entity != null && entity instanceof TileEntityMonsterSpawnerSPC)
			return new ContainerMobSpawner(player.inventory, (TileEntityMonsterSpawnerSPC)entity);
		}
		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		
		switch(ID)
		{
			case EngenderMod.statCheckerGUIID:
			if(!stack.isEmpty() && stack.getItem() instanceof ItemEngenderStatChecker)
			return new GuiEngenderMobInventory(player, ItemEngenderStatChecker.viewedEntity);
			case EngenderMod.engenderfuserGUIID:
			if(entity != null && entity instanceof TileEntityMonsterSpawnerSPC)
			return new GuiEngenderFusionCrafter(player.inventory, (TileEntityMonsterSpawnerSPC)entity);
		}
		return null;
	}
	
	public void preInit(FMLPreInitializationEvent e)
	{
		new ESetup();
		ETab.init();
		ESpawner.init();
		MobChunkLoader.init();
		EBlock.init();
		EItem.ENMO();
		EEffect.registerPotions();
		EEnchant.init();
		ELoot.registerAllModdedLootTables();
		ESound.registerSounds();
		GameRegistry.registerTileEntity(TileEntityMonsterSpawnerSPC.class, new ResourceLocation(EngenderMod.MODID, "mob_spawner_spc"));
		
	}
	
	public void init(FMLInitializationEvent e)
	{
		
		EEntity.registerEntity();
	}
	
	public void postInit(FMLPostInitializationEvent e) 
	{
	}
}

		
		