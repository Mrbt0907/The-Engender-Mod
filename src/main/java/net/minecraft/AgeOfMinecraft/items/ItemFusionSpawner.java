package net.minecraft.AgeOfMinecraft.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.registry.EngenderSetup;
import net.minecraft.AgeOfMinecraft.registry.SoundRegistry;

public class ItemFusionSpawner extends Item
{
	public final int tier;
	protected final EnumRarity rarity;
	protected final Class<? extends EntityFriendlyCreature> entityClass;
	protected final BiConsumer<EntityPlayer, Object[]> spawnMechanics;
	protected final Consumer<EntityFriendlyCreature> spawnMechanicsPost;
	
	public ItemFusionSpawner(Class<? extends EntityFriendlyCreature> entityClass, int tier, BiConsumer<EntityPlayer, Object[]> spawnMechanics, Consumer<EntityFriendlyCreature> spawnMechanicsPost)
	{
		this.entityClass = entityClass;
		this.tier = tier;
		this.spawnMechanics = spawnMechanics;
		this.spawnMechanicsPost = spawnMechanicsPost;
		switch (tier)
		{
			case 0:
				setMaxStackSize(16);
				rarity = EnumRarity.COMMON;
				break;
			case 1:
				setMaxStackSize(16);
				rarity = EnumRarity.UNCOMMON;
				break;
			case 2:
				setMaxStackSize(8);
				rarity = EnumRarity.RARE;
				break;
			case 3:
				setMaxStackSize(8);
				rarity = EnumRarity.EPIC;
				break;
			case 4:
				setMaxStackSize(4);
				rarity = EngenderSetup.SUPEREPIC;
				break;
			case 5:
				setMaxStackSize(1);
				rarity = EngenderSetup.UBEREPIC;
				break;
			default:
				setMaxStackSize(1);
				rarity = EngenderSetup.UBEREPIC;
		}
	}
	
	public ItemFusionSpawner(Class<? extends EntityFriendlyCreature> entityClass, int tier, BiConsumer<EntityPlayer, Object[]> spawnMechanics)
	{
		this(entityClass, tier, spawnMechanics, null);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return rarity;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (world.isRemote)
			return EnumActionResult.SUCCESS;
		else
		{
			int amount = 1;
			Object[] args = new Object[8];
			
			if (!player.canPlayerEdit(pos.offset(facing), facing, stack))
				return EnumActionResult.FAIL;
			if (player.capabilities.isCreativeMode)
			{
				if (player.isSneaking() && tier < 6)
					amount = 5;
			}
			else
				stack.shrink(1);
			
			args[0] = amount;
			args[1] = entityClass;
			args[2] = pos;
			args[3] = facing;
			args[4] = hitX;
			args[5] = hitY;
			args[6] = hitZ;
			args[7] = spawnMechanicsPost;
			spawnMechanics.accept(player, args);
			
			if (player instanceof EntityPlayerMP)
				CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
			world.playSound(pos.getX(), pos.up().getY(), pos.getZ(), SoundRegistry.createMob, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			return EnumActionResult.SUCCESS;
		}
	}
}
