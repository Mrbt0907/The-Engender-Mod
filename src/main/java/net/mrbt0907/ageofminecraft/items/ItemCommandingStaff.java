package net.mrbt0907.ageofminecraft.items;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.items.capabilities.CapabilityCommandStaff;


public class ItemCommandingStaff extends Item
{
	public ItemCommandingStaff()
	{
		setMaxStackSize(1);
	}
	
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BLOCK;
	}
	
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Command your mobs using right click");
	}
	
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.COMMON;
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return super.hasEffect(stack);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (world.isRemote)
		{
			CapabilityCommandStaff capability = stack.getCapability(CapabilityCommandStaff.Provider.INSTANCE, CapabilityCommandStaff.Provider.FACE);
			boolean hasSelection = capability.hasSelection(), isSneaking = player.isSneaking();
			RayTraceResult raytrace = player.rayTrace(hasSelection ? 1000.0D : net.minecraft.client.Minecraft.getMinecraft().playerController.getBlockReachDistance(), net.minecraft.client.Minecraft.getMinecraft().getRenderPartialTicks());
			
			if (raytrace == null || raytrace.typeOfHit.equals(RayTraceResult.Type.MISS))
				capability.clearSelection();
			else if(raytrace.typeOfHit.equals(RayTraceResult.Type.ENTITY))
			{
				if (raytrace.entityHit instanceof EntityEngendered)
				{
					capability.selectUnit(player, (EntityEngendered) raytrace.entityHit, isSneaking);
				}
				else if (hasSelection)
				{
					
				}
			}
			else if (hasSelection)
			{
				
			}
		}
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		return onItemUseFinish(stack, worldIn, (EntityPlayer)entityLiving);
	}
}