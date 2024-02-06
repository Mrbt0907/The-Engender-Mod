package net.minecraft.AgeOfMinecraft.items;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;


public class ItemTrainingStick extends Item{
	public ItemTrainingStick()
	{
		setRegistryName("trainingstick");
		setUnlocalizedName("trainingstick");
		setCreativeTab(ETab.engender);
		this.setMaxStackSize(1);
	}

	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
	{
		List<EntityFriendlyCreature> list = worldIn.<EntityFriendlyCreature>getEntitiesWithinAABB(EntityFriendlyCreature.class, player.getEntityBoundingBox().grow(32D), EntitySelectors.NOT_SPECTATING);
		
		if (!list.isEmpty())
		{
			player.swingArm(hand);
			for (EntityFriendlyCreature mob : list)
			{
				if (!player.world.isRemote && mob != null && mob.isEntityAlive() && mob.isOnSameTeam(player))
				{
					if (player.isSneaking())
					{
						mob.setFakeHealth(0F);
					}
					else
					{
						mob.setFakeHealth(mob.getMaxHealth() * 2);
						mob.setAttackTarget(null);
						mob.getNavigator().clearPath();
						if (mob.getGuardBlock() != null)
						{
							mob.randPosX = mob.getGuardBlock().getX();
							mob.randPosZ = mob.getGuardBlock().getZ();
						}
					}
					mob.incrementConversion(player);
				}
			}
			if (!player.world.isRemote)
			if (player.isSneaking())
			player.sendMessage(new TextComponentTranslation("Your mobs have stopped training.", new Object[0]));
			else
			player.sendMessage(new TextComponentTranslation("Your mobs are ready to train!", new Object[0]));
			

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		else
		{
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
		}
	}

	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Force your engendered mobs to train in combat");
		tooltip.add(TextFormatting.GOLD + "Right click to activate");
		tooltip.add(TextFormatting.GOLD + "Shift right click to deactivate");
	}
}