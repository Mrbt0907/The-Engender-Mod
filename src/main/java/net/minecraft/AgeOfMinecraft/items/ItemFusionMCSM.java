package net.minecraft.AgeOfMinecraft.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;


public class ItemFusionMCSM extends ItemFusion
{
	public ItemFusionMCSM(ItemTierItem fuse, String name, int mana, int entropy)
	{
		super(fuse, name, mana, entropy);
	}

	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.LIGHT_PURPLE + "(Minecraft Story Mode)" + TextFormatting.WHITE);
	}
}