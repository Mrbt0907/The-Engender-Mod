package net.minecraft.AgeOfMinecraft.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;


@SuppressWarnings("deprecation")
public class EnchantmentObliteration extends Enchantment
{
	public EnchantmentObliteration(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
	{
		super(rarityIn, EnumEnchantmentType.WEAPON, slots);
	}

	/**
	* Returns the minimal value of enchantability needed on the enchantment level passed.
	*/
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 20;
	}
	public String getTranslatedName(int level)
	{
		String s = TextFormatting.ITALIC + I18n.translateToLocal(this.getName());
		
		return TextFormatting.BOLD + (level == 1 && this.getMaxLevel() == 1 ? s : s + " " + I18n.translateToLocal("enchantment.level." + level));
	}

	/**
	* Returns the maximum value of enchantability nedded on the enchantment level passed.
	*/
	public int getMaxEnchantability(int enchantmentLevel)
	{
		return super.getMinEnchantability(enchantmentLevel) + 30;
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
		return "enchantment.obliteration";
	}

	/**
	* Determines if this enchantment can be applied to a specific ItemStack.
	*/
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof ItemAxe ? true : super.canApply(stack);
	}

	/**
	* Called whenever a mob is damaged with an item that has this enchantment on it.
	*/
	public void onEntityDamaged(EntityLivingBase user, Entity target, int level)
	{
		if (target instanceof EntityLivingBase)
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)target;
			
			if (!entitylivingbase.world.isRemote && entitylivingbase.isNonBoss() && !entitylivingbase.isEntityAlive())
			{
				entitylivingbase.setDead();
				entitylivingbase.world.createExplosion(user, target.posX, target.posY, target.posZ, entitylivingbase.height + entitylivingbase.width, false);
				entitylivingbase.world.setEntityState(entitylivingbase, (byte)20);
			}
		}
	}
}