package net.minecraft.AgeOfMinecraft.items;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemSpiderJockeyItem
extends ItemTierItem
{
	public ItemSpiderJockeyItem(int tier)
	{
		super(2);
		setRegistryName("spiderjockey");
		setUnlocalizedName("spiderjockey");
		itemTier = 2;
	}
	public int getTimeToSpawnMob()
	{
		return 30;
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
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
		EntitySpider entityliving = new EntitySpider(worldIn);
		pos = pos.offset(facing);
		entityliving.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
		entityliving.rotationYawHead = entityliving.rotationYaw;
		entityliving.renderYawOffset = entityliving.rotationYaw;
		entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
		if (!worldIn.isRemote)
		{
			worldIn.spawnEntity(entityliving);
			int i = 80;
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
			EntitySkeleton entitymount = new EntitySkeleton(worldIn);
			pos = pos.offset(facing);
			entitymount.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
			entitymount.rotationYawHead = entitymount.rotationYaw;
			entitymount.renderYawOffset = entitymount.rotationYaw;
			entitymount.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
			worldIn.spawnEntity(entitymount);
			entitymount.startRiding(entityliving);
			entitymount.setOwnerId(playerIn.getUniqueID());
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


