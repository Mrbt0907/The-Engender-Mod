package net.minecraft.AgeOfMinecraft.items;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.tier4.EntityIcyEnderCreeper;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemIcyEnderCreeperItem
extends ItemTierItem
{
	public ItemIcyEnderCreeperItem(int tier)
	{
		super(3);
		setRegistryName("icyendercreeper");
		setUnlocalizedName("icyendercreeper");
		itemTier = 3;
	}
	public int getTimeToSpawnMob()
	{
		return 45;
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.EPIC;
	}

	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.LIGHT_PURPLE + "(Minecraft Story Mode)" + TextFormatting.WHITE);
	}
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		if (worldIn.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack))
		{
			return EnumActionResult.FAIL;
		}
		EntityIcyEnderCreeper entityliving = new EntityIcyEnderCreeper(worldIn);
		pos = pos.offset(facing);
		entityliving.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
		entityliving.rotationYawHead = entityliving.rotationYaw;
		entityliving.renderYawOffset = entityliving.rotationYaw;
		entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
		if (!worldIn.isRemote)
		{
			worldIn.spawnEntity(entityliving);
			int i = 400;
			while (i > 0)
			{
				int j = EntityXPOrb.getXPSplit(i);
				i -= j;
				
				entityliving.world.spawnEntity(new EntityXPOrb(entityliving.world, entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, j));
			}
		}
		if (entityliving != null)
		{
			entityliving.setOwnerId(playerIn.getUniqueID());
			this.triggerAction(playerIn, stack);
			entityliving.playLivingSound();
			entityliving.playSound(ESound.createMob, 5.0F, 1.0F);
			if (!playerIn.capabilities.isCreativeMode)
			{
				stack.shrink(1);
			}
		}
		return EnumActionResult.SUCCESS;
	}
}


