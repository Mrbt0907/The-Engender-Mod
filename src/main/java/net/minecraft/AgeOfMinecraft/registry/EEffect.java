package net.minecraft.AgeOfMinecraft.registry;

import java.util.UUID;

import net.minecraft.AgeOfMinecraft.effects.PotionBleeding;
import net.minecraft.AgeOfMinecraft.effects.PotionEngender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class EEffect {

	public static final Potion BREAKING = new PotionEngender("breaking", true, 13015637, 0, 0, -2D).registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR,UUID.fromString("d8e4ae61-e49d-4931-aa67-0e7662665550").toString(), 0D, 0);
	public static final Potion BLEEDING = new PotionBleeding().registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,UUID.fromString("d8e4ae61-e49d-4931-aa67-0e7662665551").toString(), 0D, 0);

	public static final PotionType normal_breaking = new PotionType("breaking",new PotionEffect[] { new PotionEffect(BREAKING, 3600) }).setRegistryName("breaking");
	public static final PotionType long_breaking = new PotionType("long_breaking",new PotionEffect[] { new PotionEffect(BREAKING, 9600) }).setRegistryName("long_breaking");
	public static final PotionType strong_breaking = new PotionType("strong_breaking",new PotionEffect[] { new PotionEffect(BREAKING, 1800, 1) }).setRegistryName("strong_breaking");

	public static void registerPotions() 
	{
		ForgeRegistries.POTIONS.register(BREAKING);
		ForgeRegistries.POTIONS.register(BLEEDING);
		ForgeRegistries.POTION_TYPES.register(normal_breaking);
		ForgeRegistries.POTION_TYPES.register(long_breaking);
		ForgeRegistries.POTION_TYPES.register(strong_breaking);
		PotionHelper.addMix(PotionTypes.AWKWARD, Items.CLAY_BALL, normal_breaking);
		PotionHelper.addMix(normal_breaking, Items.REDSTONE, long_breaking);
		PotionHelper.addMix(normal_breaking, Items.GLOWSTONE_DUST, strong_breaking);
	}
}