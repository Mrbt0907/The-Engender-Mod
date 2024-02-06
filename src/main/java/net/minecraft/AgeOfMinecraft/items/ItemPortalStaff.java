package net.minecraft.AgeOfMinecraft.items;
import java.util.List;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.EntityPortal;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ItemPortalStaff extends Item
{
	public ItemPortalStaff()
	{
		setRegistryName("portalstaff");
		setUnlocalizedName("portalstaff");
		setMaxStackSize(1);
		setCreativeTab(ETab.engender);
		this.setHasSubtypes(true);
	}
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Summon an ancient portal to obliterate");
		tooltip.add("your enemies and build your army");
		tooltip.add(TextFormatting.GOLD + "Hold right click while standing on the ground");
	}
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
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
		return 30;
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
	playerIn.world.playSound(playerIn, new BlockPos(playerIn), SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.PLAYERS, 100.0F, 0.5F);
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
	playerIn.addExhaustion(10F);
	playerIn.getCooldownTracker().setCooldown(this, 600 + (stack.getMetadata() * 200));
	EntityPortal portal = new EntityPortal(worldIn);
	portal.setLocationAndAngles((int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ, 0.0F, 0.0F);
	portal.setOwnerId(playerIn.getUniqueID());
	portal.playSound(ESound.portalMake, 100.0F, 1.0F);
	portal.playSound(ESound.portalAmbient, 5.0F, 1.0F);
	if (!worldIn.isRemote)
	{
		worldIn.spawnEntity(portal);
		portal.setMetaData(stack.getMetadata());
		portal.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000.0D + (portal.getMetaData() * 500D));
		portal.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20.0D + (portal.getMetaData() * 5D));
		portal.heal(portal.getMaxHealth());
		stack.shrink(1);
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


