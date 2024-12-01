package net.mrbt0907.ageofminecraft.util.mrbtutil;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class EnchantmentUtil
{
	public static int getEnchantmentLevel(Enchantment enchantment, ItemStack stack)
	{
		if (stack.isEmpty())
			return 0;
		
		NBTTagList nbtList = stack.getEnchantmentTagList();
		int size = nbtList.tagCount();
		NBTTagCompound nbt;
		Enchantment enchantmentB;
		
		for (int i = 0; i < size; ++i)
		{
			nbt = nbtList.getCompoundTagAt(i);
			enchantmentB = Enchantment.getEnchantmentByID(nbt.getShort("id"));
			if (enchantment == enchantmentB)
				return nbt.getShort("lvl");
		}

		return 0;
	}
	
	public static int getEnchantmentLevel(String enchantment, ItemStack stack)
	{
		return getEnchantmentLevel(enchantment, stack, false);
	}
	
	public static int getEnchantmentLevel(String enchantment, ItemStack stack, boolean partialMatch)
	{
		if (stack.isEmpty())
			return 0;
		
		NBTTagList nbtList = stack.getEnchantmentTagList();
		int size = nbtList.tagCount();
		NBTTagCompound nbt;
		Enchantment enchantmentB;
		
		for (int i = 0; i < size; ++i)
		{
			nbt = nbtList.getCompoundTagAt(i);
			enchantmentB = Enchantment.getEnchantmentByID(nbt.getShort("id"));
			if (partialMatch && enchantment.equals(enchantmentB.getRegistryName().getResourcePath().toString()) || enchantment == enchantmentB.getRegistryName().toString())
				return nbt.getShort("lvl");
		}

		return 0;
	}
}
