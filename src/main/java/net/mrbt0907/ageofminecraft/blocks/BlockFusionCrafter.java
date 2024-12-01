package net.mrbt0907.ageofminecraft.blocks;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.entity.EntityManaOrb;

public class BlockFusionCrafter
extends BlockContainer
{
	public BlockFusionCrafter()
	{
		super(Material.IRON);
		setHardness(5.0F);
		setResistance(6000000.0F);
		setHarvestLevel("pickaxe", 0);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Infuses mana and entropy with a fusion catalyst to form completely tamed mobs known as Engender Mobs.");
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileFusionCrafter();
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (stack.hasDisplayName())
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileFusionCrafter)
				((TileFusionCrafter)tile).name = stack.getDisplayName();
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;
		else if (world.getTileEntity(pos) instanceof TileFusionCrafter)
		{
			FMLNetworkHandler.openGui(player, EngenderMod.instance, EngenderMod.engenderfuserGUIID, player.world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		
		return false;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileFusionCrafter)
		{
			TileFusionCrafter tileSpawner = (TileFusionCrafter) tile;
			InventoryHelper.dropInventoryItems(worldIn, pos, tileSpawner);
			
			if (tileSpawner.mana > 0)
				worldIn.spawnEntity(new EntityManaOrb(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileSpawner.mana, false));
			if (tileSpawner.entropy > 0)
				worldIn.spawnEntity(new EntityManaOrb(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileSpawner.entropy, true));
			
			worldIn.updateComparatorOutputLevel(pos, this);
		}
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
}