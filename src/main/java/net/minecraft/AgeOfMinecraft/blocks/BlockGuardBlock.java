package net.minecraft.AgeOfMinecraft.blocks;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGuardBlock
extends Block
{
	public BlockGuardBlock()
	{
		super(Material.IRON);
		setTickRandomly(true);
		setCreativeTab(ETab.engender);
		setUnlocalizedName("guard_block");
		setHardness(5.0F);
		setResistance(6000000.0F);
		setHarvestLevel("pickaxe", 0);
		setLightLevel(1.0F);
	}

	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Sets all of your engendered mobs to guard a 32x32 area");
		tooltip.add(TextFormatting.GOLD + "Right click on the block to activate");
		tooltip.add(TextFormatting.GOLD + "Shift right click on a mob to unbind them");
	}
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		double d0 = (double)pos.getX() + 0.5D;
		double d1 = (double)pos.getY() + 1D;
		double d2 = (double)pos.getZ() + 0.5D;
		
		worldIn.spawnParticle(EnumParticleTypes.END_ROD, true, d0, d1 + 0.1D, d2, 0.0D, 0.0D, 0.0D, new int[0]);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!worldIn.isRemote && playerIn != null)
		{
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List list = worldIn.getEntitiesWithinAABB(EntityFriendlyCreature.class, playerIn.getEntityBoundingBox().grow(48D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			
			if ((list != null) && (!list.isEmpty()))
			{
				worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.MASTER, 1F, 1F);
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(i1);
					
					if (entity != null && entity.isOnSameTeam(playerIn) && entity.canUseGuardBlock())
					{
						entity.spawnExplosionParticle();
						entity.randPosX = entity.posX;
						entity.randPosY = entity.posY;
						entity.randPosZ = entity.posZ;
						entity.setGuardBlock(pos);
					}
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}