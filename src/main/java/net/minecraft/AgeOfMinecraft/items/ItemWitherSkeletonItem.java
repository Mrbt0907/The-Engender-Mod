package net.minecraft.AgeOfMinecraft.items;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemWitherSkeletonItem
extends ItemTierItem
{
	public ItemWitherSkeletonItem(int tier)
	{
		super(3);
		setRegistryName("witherskeleton");
		setUnlocalizedName("witherskeleton");
		itemTier = 3;
	}
	public int getTimeToSpawnMob()
	{
		return 40;
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.EPIC;
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
		EntitySkeleton entityliving = new EntitySkeleton(worldIn);
		pos = pos.offset(facing);
		entityliving.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
		entityliving.rotationYawHead = entityliving.rotationYaw;
		entityliving.renderYawOffset = entityliving.rotationYaw;
		entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
		entityliving.setSkeletonType(1);
		entityliving.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
		if (entityliving.getRNG().nextInt(2) > 0)
		{
			entityliving.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.STONE_SWORD));
		}
		if (!worldIn.isRemote)
		{
			worldIn.spawnEntity(entityliving);
			int i = 180;
			while (i > 0)
			{
				int j = EntityXPOrb.getXPSplit(i);
				i -= j;
				if (!worldIn.getGameRules().getBoolean("disableExpItemDrops"))
				entityliving.world.spawnEntity(new EntityXPOrb(entityliving.world, entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, j));
			}
		}
		if (entityliving != null)
		{
			entityliving.setOwnerId(playerIn.getUniqueID());
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


