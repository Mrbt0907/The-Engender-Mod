package net.minecraft.AgeOfMinecraft.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.endermanofdoom.mac.item.ItemUtils;
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
	public String getItemStackDisplayName(ItemStack stack)
    {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
			if (mc.player != null && mc.player.capabilities.isCreativeMode)
				return super.getItemStackDisplayName(stack) + " " + (ItemUtils.loadNBT(stack).getBoolean("spawnWild") ? TextFormatting.RED + "[Wild]" : TextFormatting.BLUE + "[Friendly]");
		}
		return super.getItemStackDisplayName(stack);
    }
	
	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return rarity;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (world.isRemote)
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		else
		{
			if (player.capabilities.isCreativeMode && player.isSneaking())
			{
				NBTTagCompound nbt = ItemUtils.loadNBT(stack);
				boolean spawnWild = !nbt.getBoolean("spawnWild");
				nbt.setBoolean("spawnWild", spawnWild);
				ItemUtils.saveNBT(stack, nbt);
				
				if (spawnWild)
					player.sendStatusMessage(new TextComponentTranslation("item.fusionspawner.spawn_wild", TextFormatting.RED), true);
				else
					player.sendStatusMessage(new TextComponentTranslation("item.fusionspawner.spawn_friendly", TextFormatting.BLUE), true);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
			}
			return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (world.isRemote)
			return EnumActionResult.SUCCESS;
		else
		{
			boolean spawnWild = player.capabilities.isCreativeMode && ItemUtils.loadNBT(stack).getBoolean("spawnWild");
			int amount = 1;
			Object[] args = new Object[9];
				
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
			args[8] = spawnWild;
			spawnMechanics.accept(player, args);
				
			if (player instanceof EntityPlayerMP)
				CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)player, stack);
			player.playSound(SoundRegistry.createMob, 1.0F, 1.0F);
			
			return EnumActionResult.SUCCESS;
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
		tooltip.add("Right click on the ground to spawn this mob.");
		if (mc.player != null && mc.player.capabilities.isCreativeMode)
		{
			String wild = !ItemUtils.loadNBT(stack).getBoolean("spawnWild") ? TextFormatting.RED + "[Wild Mode]" : TextFormatting.BLUE + "[Friendly Mode]";
			tooltip.add("Shift right click on the ground to spawn five of this mob");
			tooltip.add("Shift right click in the air to change to " + wild);
		}
	}
}
