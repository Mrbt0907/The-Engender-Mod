package net.minecraft.AgeOfMinecraft.items;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.tier6.EntityCommandBlockWither;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
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

public class ItemWitherStormItem
extends ItemTierItem
{
	public ItemWitherStormItem(int tier)
	{
		super(5);
		setRegistryName("witherstorm");
		setUnlocalizedName("witherstorm");
		itemTier = 5;
	}
	public int getTimeToSpawnMob()
	{
		return 1200;
	}
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		return ESetup.UBEREPIC;
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
		EntityCommandBlockWither entityliving = new EntityCommandBlockWither(worldIn);
		pos = pos.offset(facing);
		entityliving.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
		entityliving.rotationYawHead = entityliving.rotationYaw;
		entityliving.renderYawOffset = entityliving.rotationYaw;
		entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
		entityliving.setInvulTime(1);
		if (!worldIn.isRemote)
		{
			worldIn.spawnEntity(entityliving);
			int i = 1600000;
			while (i > 0)
			{
				int j = EntityXPOrb.getXPSplit(i);
				i -= j;
				if (!worldIn.getGameRules().getBoolean("disableExpItemDrops"))
				entityliving.world.spawnEntity(new EntityXPOrb(entityliving.world, entityliving.posX, entityliving.posY + entityliving.getEyeHeight() + 25.0D, entityliving.posZ, j));
			}
		}
		if (entityliving != null)
		{
			entityliving.setOwnerId(playerIn.getUniqueID());
			this.triggerAction(playerIn, stack);
			entityliving.playLivingSound();
			entityliving.playSound(ESound.createMob, 10.0F, 0.5F);
			entityliving.playSound(ESound.createBossMob, Float.MAX_VALUE, 0.75F);
			if (!playerIn.capabilities.isCreativeMode)
			{
				stack.shrink(1);
			}
		}
		return EnumActionResult.SUCCESS;
	}
}


