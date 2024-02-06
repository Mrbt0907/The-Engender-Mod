package net.minecraft.AgeOfMinecraft.items;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex;
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

public class ItemVexItem
extends ItemTierItem
{
	public ItemVexItem(int tier)
	{
		super(2);
		setRegistryName("vex");
		setUnlocalizedName("vex");
		itemTier = 2;
	}
	public int getTimeToSpawnMob()
	{
		return 36;
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
		for (int a = 0; a < 3; ++a)
		{
			EntityVex entityliving = new EntityVex(worldIn);
			pos = pos.offset(facing);
			entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), null);
			entityliving.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
			if (!worldIn.isRemote)
			{
				worldIn.spawnEntity(entityliving);
				int i = 15;
				while (i > 0)
				{
					int j = EntityXPOrb.getXPSplit(i);
					i -= j;
					if (!worldIn.getGameRules().getBoolean("disableExpItemDrops"))
					entityliving.world.spawnEntity(new EntityXPOrb(entityliving.world, entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, j));
				}
			}
			if ((entityliving != null))
			{
				entityliving.setOwnerId(playerIn.getUniqueID());
				entityliving.playLivingSound();
				entityliving.playSound(ESound.createMob, 5.0F, 1.0F);
			}
		}

		if (!playerIn.capabilities.isCreativeMode)
		{
			stack.shrink(1);
		}
		return EnumActionResult.SUCCESS;
	}
}


