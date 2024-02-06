package net.minecraft.AgeOfMinecraft.items;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSimpleFoiled;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemLastChance
extends ItemSimpleFoiled
{
	public ItemLastChance()
	{
		setRegistryName("last_chance");
		setUnlocalizedName("last_chance");
		setCreativeTab(ETab.engender);
		setMaxStackSize(1);
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.EPIC;
	}
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Give your mob an extra life upon death");
		tooltip.add(TextFormatting.RED + "Does not work with tier 6 engendered mobs");
	}
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
	{
		if ((target instanceof EntityFriendlyCreature))
		{
			EntityFriendlyCreature entity = (EntityFriendlyCreature)target;
			if (!entity.hasLastChance() && entity.getTier() != EnumTier.TIER6)
			{
				entity.setLastChance(true);
				entity.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1F, 1F);
				entity.spawnExplosionParticle();
				playerIn.swingArm(hand);
				playerIn.renderBrokenItemStack(stack);
				if (!entity.hasLimitedLife())
				stack.shrink(1);
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (attacker instanceof EntityPlayer && target instanceof EntityLivingBase)
		itemInteractionForEntity(stack, (EntityPlayer)attacker, target, EnumHand.MAIN_HAND);
		return true;
	}
}