package net.minecraft.AgeOfMinecraft.items;
import java.util.List;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.registry.ESetup;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemSummoningStaff extends Item
{
	public ItemSummoningStaff()
	{
		setRegistryName("summoningstaff");
		setUnlocalizedName("summoningstaff");
		setMaxStackSize(1);
		setCreativeTab(ETab.engender);
		this.setHasSubtypes(true);
	}
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Teleport all of your engendered mobs to you");
		tooltip.add(TextFormatting.GREEN + "Tier 5 allows you to convert all wild engendered mobs also");
		tooltip.add(TextFormatting.GOLD + "Hold right click to use");
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		switch (stack.getMetadata())
		{
			case 0:
			return EnumRarity.COMMON;
			case 1:
			return EnumRarity.UNCOMMON;
			case 2:
			return EnumRarity.RARE;
			case 3:
			return EnumRarity.EPIC;
			case 4:
			return ESetup.SUPEREPIC;
			default:
			return ESetup.UBEREPIC;
		}
	}
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return super.hasEffect(stack) || stack.getMetadata() > 3;
	}
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 140;
	}
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		boolean flag = func_185060_a(playerIn) != null;
		ActionResult<ItemStack> ret = ForgeEventFactory.onArrowNock(itemStackIn, worldIn, playerIn, hand, flag);
		if (ret != null) { return ret;
	}
	if ((!playerIn.capabilities.isCreativeMode) && (!flag))
	{
		return !flag ? new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStackIn) : new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
	}
	playerIn.setActiveHand(hand);
	playerIn.world.playSound(playerIn, new BlockPos(playerIn), SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.PLAYERS, 100.0F, 1.0F);
	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
}
private ItemStack func_185060_a(EntityPlayer player)
{
	return player.getHeldItem(EnumHand.MAIN_HAND);
}
public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
{
	return onItemUseFinish(stack, worldIn, (EntityPlayer)entityLiving);
}
public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
{
	List<EntityFriendlyCreature> list = worldIn.getEntitiesWithinAABB(EntityFriendlyCreature.class, playerIn.getEntityBoundingBox().grow(64D + (stack.getMetadata() * 32D)), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
	List<?> everywhere = worldIn.loadedEntityList;if (list != null)
	{
		if (!list.isEmpty())
		{
			if (!playerIn.world.isRemote)
			playerIn.sendMessage(new TextComponentTranslation("You summon your mobs!", new Object[0]));
			playerIn.world.playSound(playerIn, new BlockPos(playerIn), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 100.0F, 1.0F);
			for (int i1 = 0; i1 < list.size(); i1++)
			{
				EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(i1);
				if ((entity != null) && (entity.isEntityAlive()) && (entity.getTier() != EnumTier.TIER6 || stack.getMetadata() > 2) && (entity.getTier() != EnumTier.TIER5 || stack.getMetadata() > 0))
				{
					playerIn.addExhaustion(0.05F);
					playerIn.getCooldownTracker().setCooldown(this, 1200 + i1);
					entity.ticksExisted = 0;
					entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
					if ((entity.isWild() && entity.getOwnerId() == null && stack.getMetadata() > 1) || (!entity.isWild() && entity.getOwnerId() == playerIn.getUniqueID()))
					{
						entity.setOwnerId(playerIn.getUniqueID());
						entity.setLocationAndAngles(playerIn.prevPosX, playerIn.prevPosY, playerIn.prevPosZ, playerIn.rotationYawHead, 0.0F);
					}
				}
			}
		}
	}
	if (everywhere != null && stack.getMetadata() > 3)
	{
		if (!everywhere.isEmpty())
		{
			if (!playerIn.world.isRemote && worldIn.getGameRules().getBoolean("showMobDeathMessages"))
			playerIn.sendMessage(new TextComponentTranslation("You summon your mobs!", new Object[0]));
			playerIn.world.playSound(playerIn, new BlockPos(playerIn), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 100.0F, 1.0F);
			for (int i1 = 0; i1 < list.size(); i1++)
			{
				Entity entity = (Entity)list.get(i1);
				if ((entity != null) && (entity.isEntityAlive()) && entity instanceof EntityFriendlyCreature)
				{
					playerIn.addExhaustion(0.05F);
					playerIn.getCooldownTracker().setCooldown(this, 1200 + i1);
					entity.ticksExisted = 0;
					entity.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
					if ((((EntityFriendlyCreature)entity).isWild() && ((EntityFriendlyCreature)entity).getOwnerId() == null && stack.getMetadata() > 1) || (!((EntityFriendlyCreature)entity).isWild() && ((EntityFriendlyCreature)entity).getOwnerId() == playerIn.getUniqueID()))
					{
						((EntityFriendlyCreature)entity).setOwnerId(playerIn.getUniqueID());
						entity.setLocationAndAngles(playerIn.prevPosX, playerIn.prevPosY, playerIn.prevPosZ, playerIn.rotationYawHead, 0.0F);
					}
				}
			}
		}
	}
	return stack;
}
/**
* returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
*/
public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
{
	if (this.isInCreativeTab(tab))
	{
		for (int i = 0; i <= 4; ++i)
		{
			items.add(new ItemStack(this, 1, i));
		}
	}
}
}


