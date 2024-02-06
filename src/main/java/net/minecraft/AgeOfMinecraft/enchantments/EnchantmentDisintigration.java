package net.minecraft.AgeOfMinecraft.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;


@SuppressWarnings("deprecation")
public class EnchantmentDisintigration extends Enchantment
{
	public EnchantmentDisintigration(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots)
	{
		super(rarityIn, EnumEnchantmentType.WEAPON, slots);
	}

	/**
	* Returns the minimal value of enchantability needed on the enchantment level passed.
	*/
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 14;
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
	public String getTranslatedName(int level)
	{
		String s = TextFormatting.ITALIC + I18n.translateToLocal(this.getName());
		
		return level == 1 && this.getMaxLevel() == 1 ? s : s + " " + I18n.translateToLocal("enchantment.level." + level);
	}

	/**
	* Calculates the additional damage that will be dealt by an item with this enchantment. This alternative to
	* calcModifierDamage is sensitive to the targets EnumCreatureAttribute.
	*/
	public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
	{
		return creatureType == EnumCreatureAttribute.UNDEAD ? 6F : 1F;
	}

	/**
	* Return the name of key in translation table of this enchantment.
	*/
	public String getName()
	{
		return "enchantment.disintigration";
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
			
			if (!entitylivingbase.world.isRemote && entitylivingbase.isNonBoss() && !entitylivingbase.isEntityAlive() && entitylivingbase.isEntityUndead())
			{
				entitylivingbase.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2.0F, 0.5F);
				entitylivingbase.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2.0F, 0.75F);
				entitylivingbase.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2.0F, 1.25F);
				entitylivingbase.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2.0F, 1.75F);
				entitylivingbase.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 2.0F, 2.0F);
				entitylivingbase.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 2.0F, 1.75F);
				entitylivingbase.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 2.0F, 1.5F);
				entitylivingbase.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 2.0F, 1.25F);
				entitylivingbase.world.setEntityState(entitylivingbase, (byte)20);
				entitylivingbase.world.setEntityState(entitylivingbase, (byte)20);
				entitylivingbase.world.setEntityState(entitylivingbase, (byte)20);
				entitylivingbase.world.setEntityState(entitylivingbase, (byte)20);
				entitylivingbase.setDead();
			}
		}
	}
}