package net.mrbt0907.ageofminecraft.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.mrbt0907.ageofminecraft.enchantments.EnchantmentConviction;
import net.mrbt0907.ageofminecraft.enchantments.EnchantmentCrusher;
import net.mrbt0907.ageofminecraft.enchantments.EnchantmentDisintigration;
import net.mrbt0907.ageofminecraft.enchantments.EnchantmentDisruption;
import net.mrbt0907.ageofminecraft.enchantments.EnchantmentNeglection;
import net.mrbt0907.ageofminecraft.enchantments.EnchantmentObliteration;
import net.mrbt0907.ageofminecraft.enchantments.EnchantmentWitherStormKiller;

public class EnchantmentRegistry 
{
	public static final Enchantment crusher = new EnchantmentCrusher();
	public static final Enchantment disruption = new EnchantmentDisruption();
	public static final Enchantment conviction = new EnchantmentConviction();
	public static final Enchantment neglection = new EnchantmentNeglection(Enchantment.Rarity.VERY_RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	public static final Enchantment obliteration = new EnchantmentObliteration(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	public static final Enchantment disintigration = new EnchantmentDisintigration(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	public static final Enchantment stormkiller = new EnchantmentWitherStormKiller(Enchantment.Rarity.VERY_RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
	
	public static void init()
	{
		GameData.register_impl(crusher.setRegistryName(new ResourceLocation("ageofminecraft", "crusher")));
		GameData.register_impl(disruption.setRegistryName(new ResourceLocation("ageofminecraft", "disruption")));
		GameData.register_impl(conviction.setRegistryName(new ResourceLocation("ageofminecraft", "conviction")));
		GameData.register_impl(disintigration.setRegistryName(new ResourceLocation("ageofminecraft", "disintigration")));
		GameData.register_impl(obliteration.setRegistryName(new ResourceLocation("ageofminecraft", "obliteration")));
		GameData.register_impl(neglection.setRegistryName(new ResourceLocation("ageofminecraft", "neglection")));
		GameData.register_impl(stormkiller.setRegistryName(new ResourceLocation("ageofminecraft", "superweapon")));
	}
}
