package net.minecraft.AgeOfMinecraft.items;

import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class ItemTierItem extends Item
{
	public static int itemTier;
	public Item item;
	
	public ItemTierItem(int tier)
	{
		setCreativeTab(ETab.engender);
		setMaxStackSize(16);
		this.item = this;
	}

	public static int getItemTier()
	{
		return itemTier;
	}

	public int getTimeToSpawnMob()
	{
		return 0;
	}

	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.COMMON;
	}

	public void triggerAction(EntityPlayer playerIn, ItemStack stack)
	{
		if (playerIn instanceof EntityPlayerMP)
		CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)playerIn, stack);
	}
}
