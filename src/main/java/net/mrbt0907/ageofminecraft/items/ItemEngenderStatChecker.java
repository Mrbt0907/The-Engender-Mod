package net.mrbt0907.ageofminecraft.items;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;

public class ItemEngenderStatChecker extends Item
{
	public ItemEngenderStatChecker()
	{
		setMaxStackSize(1);
	}
	
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("View the stats of any engendered mob");
		tooltip.add(TextFormatting.GOLD + "Right click on an engendered mob to view stats");
	}

	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
	{
		if (target instanceof EntityEngendered)
		{
			playerIn.swingArm(hand);
			if (playerIn.world.isRemote)
			{
				net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(new net.mrbt0907.ageofminecraft.gui.GuiEngenderMobInventory(playerIn, (EntityEngendered) target));
				//FMLNetworkHandler.openGui(playerIn, EngenderMod.instance, EngenderMod.statCheckerGUIID, playerIn.world, (int)target.posX, (int)target.posY, (int)target.posZ);
			}
			return true;
		}
		return false;
	}

	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (attacker instanceof EntityPlayer)
			itemInteractionForEntity(stack, (EntityPlayer)attacker, target, attacker.swingingHand);
		return true;
	}
}


