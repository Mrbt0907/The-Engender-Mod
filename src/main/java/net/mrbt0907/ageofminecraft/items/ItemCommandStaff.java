package net.mrbt0907.ageofminecraft.items;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.items.capabilities.CapabilityCommandStaff;
import net.mrbt0907.ageofminecraft.network.PacketCommandStaff;


public class ItemCommandStaff extends Item
{
	public ItemCommandStaff()
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
			double reach = 256.0D;
			//float partialTicks = net.minecraft.client.Minecraft.getMinecraft().getRenderPartialTicks();
			
			
			Vec3d lookVector = player.getLook(1.0F);
			Vec3d playerPos = player.getPositionVector().addVector(0.0D, player.getEyeHeight(), 0.0D);
			Vec3d targetPos = playerPos.addVector(lookVector.x * reach, lookVector.y * reach, lookVector.z * reach);
			EntityLivingBase target = findEntityOnPath(player, playerPos, targetPos); 
			if(target != null)
			{
				boolean team = target.isOnSameTeam(player);
				if (target instanceof EntityEngendered && team)
				{
					capability.selectUnit(player, (EntityEngendered) target, isSneaking);
				}
				else if (hasSelection && !team)
				{
					player.sendStatusMessage(new TextComponentTranslation("engender.command.attack", target.getName()), true);
					PacketCommandStaff.orderAttack(capability.getUnits(), target);
				}
			}
			else
			{
				RayTraceResult raytrace = player.rayTrace(reach, 1.0F);
				if (hasSelection)
				{
					if (raytrace != null && raytrace.typeOfHit.equals(RayTraceResult.Type.BLOCK))
					{
						player.sendStatusMessage(new TextComponentTranslation(player.isSneaking() ? "engender.command.patrol" : "engender.command.move"), true);
						PacketCommandStaff.orderMove(capability.getUnits(), raytrace.getBlockPos(), player.isSneaking());
					}
					else
					{
						List<EntityEngendered> entities = capability.getUnits();
						if (player.isSneaking())
						{
							player.sendStatusMessage(new TextComponentTranslation("engender.command.follow", entities.size()), true);
							PacketCommandStaff.orderFollow(entities);
						}
						else
						{
							entities.forEach(selectedEntity -> selectedEntity.selected = false);
							capability.clearSelection();
							player.sendStatusMessage(new TextComponentTranslation("engender.command.deselect.all", entities.size()), true);
						}
					}
				}
			}
		}
		player.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}
	
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		return onItemUseFinish(stack, worldIn, (EntityPlayer)entityLiving);
	}
	
	@Nullable
	protected EntityLivingBase findEntityOnPath(Entity owner, Vec3d start, Vec3d end)
	{
		EntityLivingBase target = null;
		List<Entity> entities = new ArrayList<Entity>(owner.world.loadedEntityList);
		double distance = 0.0D, targetDistance;

		for (Entity entity : entities)
		{
			if (entity != owner && entity instanceof EntityLivingBase)
			{
				AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
				RayTraceResult raytrace = axisalignedbb.calculateIntercept(start, end);

				if (raytrace != null)
				{
					targetDistance = start.squareDistanceTo(raytrace.hitVec);

					if ((targetDistance < distance || distance == 0.0D))
					{
						target = (EntityLivingBase) entity;
						distance = targetDistance;
					}
				}
			}
		}
		return target;
	}
}