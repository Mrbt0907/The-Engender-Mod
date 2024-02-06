package net.minecraft.AgeOfMinecraft.items;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;


public class ItemSkeletonTrapItem
extends ItemTierItem
{
	public ItemSkeletonTrapItem(int tier)
	{
		super(4);
		setRegistryName("skeletontrap");
		setUnlocalizedName("skeletontrap");
		itemTier = 4;
	}
	public int getTimeToSpawnMob()
	{
		return 100;
	}
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		return ESetup.SUPEREPIC;
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
		if (!worldIn.isRemote)
		{
			pos = pos.offset(facing);
			DifficultyInstance difficultyinstance = worldIn.getDifficultyForLocation(pos);
			worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, true));
			for (int i = 0; i < 4; i++)
			{
				EntitySkeletonHorse entityhorse = func_188515_a(difficultyinstance, worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
				EntitySkeleton entityskeleton1 = func_188514_a(difficultyinstance, entityhorse);
				entityskeleton1.setOwnerId(playerIn.getUniqueID());
				entityskeleton1.playLivingSound();
				int i1 = 3000;
				while (i1 > 0)
				{
					int j = EntityXPOrb.getXPSplit(i1);
					i1 -= j;
					if (!worldIn.getGameRules().getBoolean("disableExpItemDrops"))
					entityskeleton1.world.spawnEntity(new EntityXPOrb(worldIn, pos.getX() + 0.5D, pos.getY() + entityskeleton1.getEyeHeight(), pos.getZ() + 0.5D, j));
				}
			}

			worldIn.playSound(null, pos, ESound.createBossMob, SoundCategory.MASTER, 10.0F, 1.25F);
			if (!playerIn.capabilities.isCreativeMode)
			{
				stack.shrink(1);
			}
		}
		return EnumActionResult.SUCCESS;
	}
	private EntitySkeletonHorse func_188515_a(DifficultyInstance p_188515_1_, World world, double x, double y, double z)
	{
		EntitySkeletonHorse entityhorse = new EntitySkeletonHorse(world);
		entityhorse.onInitialSpawn(p_188515_1_, (IEntityLivingData)null);
		entityhorse.setPosition(x, y, z);
		entityhorse.hurtResistantTime = 200;
		entityhorse.enablePersistence();
		entityhorse.setHorseTamed(true);
		entityhorse.setGrowingAge(0);
		entityhorse.world.spawnEntity(entityhorse);
		entityhorse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(53.0D);
		entityhorse.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12.0D);
		entityhorse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3375D);
		entityhorse.addVelocity(entityhorse.getRNG().nextGaussian() * 0.5D, 0.0D, entityhorse.getRNG().nextGaussian() * 0.5D);
		return entityhorse;
	}
	private EntitySkeleton func_188514_a(DifficultyInstance p_188514_1_, EntitySkeletonHorse p_188514_2_)
	{
		EntitySkeleton entityskeleton = new EntitySkeleton(p_188514_2_.world);
		entityskeleton.onInitialSpawn(p_188514_1_, (IEntityLivingData)null);
		entityskeleton.setPosition(p_188514_2_.posX, p_188514_2_.posY, p_188514_2_.posZ);
		entityskeleton.hurtResistantTime = 200;
		entityskeleton.enablePersistence();
		entityskeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
		EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getHeldItemMainhand(), 30, true);
		EnchantmentHelper.addRandomEnchantment(entityskeleton.getRNG(), entityskeleton.getItemStackFromSlot(EntityEquipmentSlot.HEAD), 30, true);
		entityskeleton.world.spawnEntity(entityskeleton);
		entityskeleton.startRiding(p_188514_2_);
		return entityskeleton;
	}
}


