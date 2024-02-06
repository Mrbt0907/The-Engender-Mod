package net.minecraft.AgeOfMinecraft.items;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;


public class ItemHeroMaker extends Item
{
	public ItemHeroMaker()
	{
		setRegistryName("heromaker");
		setUnlocalizedName("heromaker");
		setCreativeTab(ETab.engender);
		setMaxStackSize(1);
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Turns an engendered mob into a hero engendered mob");
		tooltip.add(TextFormatting.GREEN + "Boosts all stats and gives special abilities");
		tooltip.add(TextFormatting.GOLD + "Right click on an engendered mob to use");
	}
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
	{
		if ((target instanceof EntityFriendlyCreature))
		{
			EntityFriendlyCreature entity = (EntityFriendlyCreature)target;
			
			if ((!entity.isHero()) && (entity.getTier() != EnumTier.TIER6))
			{
				if (playerIn instanceof EntityPlayerMP)
				CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)playerIn, stack);
				entity.becomeAHero();
				playerIn.swingArm(hand);
				playerIn.renderBrokenItemStack(stack);
				if (!entity.hasLimitedLife())
				stack.shrink(1);
				return true;
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