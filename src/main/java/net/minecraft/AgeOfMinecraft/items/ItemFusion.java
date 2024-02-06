package net.minecraft.AgeOfMinecraft.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;


public class ItemFusion extends Item
{
	private ItemTierItem itemToFuse;
	private int manaCost;
	private int entropyCost;
	
	public ItemFusion(ItemTierItem fuse, String name, int mana, int entropy)
	{
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(ETab.engender);
		this.setMaxStackSize(16);
		this.itemToFuse = fuse;
		this.manaCost = mana;
		this.entropyCost = entropy;
	}

	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Place in the Fusion Crafter to fuse");
		tooltip.add(TextFormatting.AQUA + "Mana Cost: " + this.getManaCost());
		if (this.getEntropyCost() > 0)
		tooltip.add(TextFormatting.RED + "Entropy Cost: " + this.getEntropyCost());
	}

	public ItemTierItem getItemToFuse()
	{
		return this.itemToFuse;
	}

	public int getManaCost()
	{
		return this.manaCost;
	}

	public int getEntropyCost()
	{
		return this.entropyCost;
	}
}