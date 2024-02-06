package net.minecraft.AgeOfMinecraft.items;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class ItemEngenderStatChecker extends Item
{
	public static EntityFriendlyCreature viewedEntity;
	public ItemEngenderStatChecker()
	{
		setRegistryName("statchecker");
		setUnlocalizedName("statchecker");
		setCreativeTab(ETab.engender);
		setMaxStackSize(1);
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("View the stats of any engendered mob");
		tooltip.add(TextFormatting.GOLD + "Right click on an engendered mob to view stats");
	}

	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
	{
		if (target instanceof EntityFriendlyCreature)
		{
			EntityFriendlyCreature entitypig = (EntityFriendlyCreature)target;
			
			if (entitypig.isEntityAlive())
			{
				playerIn.swingArm(hand);
				ItemEngenderStatChecker.viewedEntity = entitypig;
				if(playerIn.world.isRemote)
				FMLNetworkHandler.openGui(playerIn, EngenderMod.instance, EngenderMod.statCheckerGUIID, playerIn.world, (int)target.posX, (int)target.posY, (int)target.posZ);
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (attacker instanceof EntityPlayer && target instanceof EntityLivingBase)
		itemInteractionForEntity(stack, (EntityPlayer)attacker, target, EnumHand.MAIN_HAND);
		return true;
	}
}


