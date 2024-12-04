package net.mrbt0907.ageofminecraft.gui;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.blocks.ContainerMobSpawner;
import net.mrbt0907.ageofminecraft.blocks.TileFusionCrafter;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.items.ItemEngenderStatChecker;
import net.mrbt0907.ageofminecraft.util.mrbtutil.ItemUtils;
import net.mrbt0907.ageofminecraft.util.mrbtutil.WorldUtils;

public class EngenderGuiHandler implements IGuiHandler
{
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		player.getHeldItem(player.swingingHand);
		
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
		switch(ID)
		{
			case EngenderMod.statCheckerGUIID:
				ItemStack stack = player.getHeldItem(player.swingingHand);
				if(!stack.isEmpty() && stack.getItem() instanceof ItemEngenderStatChecker)
				{
					UUID uuid = ItemUtils.loadNBT(stack).getUniqueId("uuid");
					EngenderMod.info(uuid);
					Entity entity = WorldUtils.getEntity(world, uuid);
					if (entity instanceof EntityEngendered)
						return new GuiEngenderMobInventory(player, (EntityEngendered) entity);
					
					return null;
				}
			case EngenderMod.engenderfuserGUIID:
				TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
				if(entity != null && entity instanceof TileFusionCrafter)
					return new GuiEngenderFusionCrafter(player.inventory, (TileFusionCrafter)entity);
		}
		return null;
	}
}
