package net.mrbt0907.ageofminecraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.blocks.ContainerMobSpawner;
import net.mrbt0907.ageofminecraft.blocks.TileFusionCrafter;
import net.mrbt0907.ageofminecraft.items.ItemEngenderStatChecker;

public class EngenderGuiHandler implements IGuiHandler
{
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		player.getHeldItem(EnumHand.MAIN_HAND);
		
		switch(ID)
		{
			case EngenderMod.engenderfuserGUIID:
			if(entity != null && entity instanceof TileFusionCrafter)
			return new ContainerMobSpawner(player.inventory, (TileFusionCrafter)entity);
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
			if(entity != null && entity instanceof TileFusionCrafter)
			return new GuiEngenderFusionCrafter(player.inventory, (TileFusionCrafter)entity);
		}
		return null;
	}
	
}
