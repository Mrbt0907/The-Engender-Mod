package net.minecraft.AgeOfMinecraft.items;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.AgeOfMinecraft.registry.EngenderSetup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;


public class ItemFusion extends Item
{
	public final int fusionTime;
	public final int manaCost;
	public final int entropyCost;
	public final int tier;
	protected final EnumRarity rarity;
	
	public ItemFusion(int tier, int mana, int entropy, int fusionTime)
	{
		this.manaCost = mana;
		this.entropyCost = entropy;
		this.fusionTime = fusionTime * 20;
		this.tier = tier;
		switch (tier)
		{
			case 0:
				setMaxStackSize(16);
				rarity = EnumRarity.COMMON;
				break;
			case 1:
				setMaxStackSize(16);
				rarity = EnumRarity.UNCOMMON;
				break;
			case 2:
				setMaxStackSize(8);
				rarity = EnumRarity.RARE;
				break;
			case 3:
				setMaxStackSize(8);
				rarity = EnumRarity.EPIC;
				break;
			case 4:
				setMaxStackSize(4);
				rarity = EngenderSetup.SUPEREPIC;
				break;
			case 5:
				setMaxStackSize(1);
				rarity = EngenderSetup.UBEREPIC;
				break;
			default:
				setMaxStackSize(1);
				rarity = EngenderSetup.UBEREPIC;
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Place in the Fusion Crafter to fuse");
		if (manaCost > 0)
			tooltip.add(TextFormatting.AQUA + "Mana Cost: " + manaCost);
		if (entropyCost > 0)
			tooltip.add(TextFormatting.RED + "Entropy Cost: " + entropyCost);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return rarity;
	}
}