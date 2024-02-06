package net.minecraft.AgeOfMinecraft.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;


@SuppressWarnings("deprecation")
public class EnchantmentNeglection extends Enchantment
{
	public EnchantmentNeglection(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
	{
		super(rarityIn, EnumEnchantmentType.WEAPON, slots);
	}

	/**
	* Returns the minimal value of enchantability needed on the enchantment level passed.
	*/
	public int getMinEnchantability(int enchantmentLevel)
	{
		return enchantmentLevel * 30;
	}

	/**
	* Returns the maximum value of enchantability nedded on the enchantment level passed.
	*/
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return this.getMinEnchantability(enchantmentLevel) + 50;
	}

	public boolean isTreasureEnchantment()
	{
		return true;
	}

	/**
	* Returns the maximum level that the enchantment can have.
	*/
	public int getMaxLevel()
	{
		return 1;
	}

	/**
	* Return the name of key in translation table of this enchantment.
	*/
	public String getName()
	{
		return "enchantment.neglection";
	}
	public String getTranslatedName(int level)
	{
		String s = TextFormatting.GOLD + I18n.translateToLocal(this.getName());
		
		return level == 1 && this.getMaxLevel() == 1 ? s : s + " " + I18n.translateToLocal("enchantment.level." + level);
	}

	/**
	* Determines if this enchantment can be applied to a specific ItemStack.
	*/
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof ItemTool ? true : super.canApply(stack);
	}

	/**
	* Called whenever a mob is damaged with an item that has this enchantment on it.
	*/
	public void onEntityDamaged(EntityLivingBase user, Entity target, int level)
	{
		target.hurtResistantTime = 0;
	}
}