package net.mrbt0907.ageofminecraft.util.mrbtutil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUtils
{
	public static boolean compareItemStacks(ItemStack stackA, ItemStack stackB)
    {
        return stackA.getItem().equals(stackB.getItem()) && (stackB.getMetadata() == 32767 || stackB.getMetadata() == stackA.getMetadata());
    }
	
	public static NBTTagCompound loadNBT(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			nbt = new NBTTagCompound();
		return nbt;
	}
	
	public static void saveNBT(ItemStack stack, NBTTagCompound nbt)
	{
		stack.setTagCompound(nbt);
	}
}
