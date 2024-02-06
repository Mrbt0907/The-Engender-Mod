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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemConvertingStaff extends Item
{
	public ItemConvertingStaff()
	{
		setRegistryName("convertingstaff");
		setUnlocalizedName("convertingstaff");
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
		tooltip.add("Converts wild Engender mobs to your side");
		tooltip.add(TextFormatting.RED + "Higher tier engendered mobs take longer");
		tooltip.add(TextFormatting.GOLD + "Hold right click to start converting");
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
		return 20;
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
		List<EntityFriendlyCreature> list = playerIn.world.getEntitiesWithinAABB(EntityFriendlyCreature.class, playerIn.getEntityBoundingBox().grow(10D * (this.getDamage(stack) + 1)), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
		if (list != null)
		{
			if (!list.isEmpty())
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(i1);
					if ((entity != null) && (entity.isEntityAlive()) && (entity.isWild()) && entity.getOwnerId() == null && !entity.isBoss() && (entity.getTier() != EnumTier.TIER6))
					{
						if (!playerIn.world.isRemote && entity.convertionInt <= 0)
							playerIn.sendMessage(new TextComponentTranslation(entity.getName() + " is wild. Starting conversion...", new Object[0]));
						
						playerIn.getCooldownTracker().setCooldown(this, 20);
						for (int times = 0; times <= stack.getMetadata(); times++)
						entity.incrementConversion(playerIn);
					}
				}
			} else {
					if (!playerIn.world.isRemote)
					playerIn.sendMessage(new TextComponentTranslation("No engender mobs are in your vicinity.", new Object[0]));
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


