package net.minecraft.AgeOfMinecraft.enchantments;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class EnchantmentWitherStormKiller extends Enchantment
{
	public EnchantmentWitherStormKiller(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
	{
		super(rarityIn, EnumEnchantmentType.BREAKABLE, slots);
	}
	/**
	* Returns the minimal value of enchantability needed on the enchantment level passed.
	*/
	public int getMinEnchantability(int enchantmentLevel)
	{
		return enchantmentLevel * 50;
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
	public int getMaxLevel()
	{
		return 1;
	}
	public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
	{
		return creatureType == ESetup.WITHER_STORM ? 25000F : 50F;
	}
	public String getName()
	{
		return "enchantment.superweapon";
	}

	public String getTranslatedName(int level)
	{
		String s = TextFormatting.LIGHT_PURPLE + I18n.translateToLocal(this.getName());
		
		return level == 1 && this.getMaxLevel() == 1 ? s : s + " " + I18n.translateToLocal("enchantment.level." + level);
	}
}