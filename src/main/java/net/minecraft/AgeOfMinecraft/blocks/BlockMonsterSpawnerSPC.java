package net.minecraft.AgeOfMinecraft.blocks;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityManaOrb;
import net.minecraft.AgeOfMinecraft.registry.EBlock;
import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
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

public class BlockMonsterSpawnerSPC
extends BlockContainer
{
	public BlockMonsterSpawnerSPC()
	{
		super(Material.IRON);
		setUnlocalizedName("mob_spawner_spc");
		setHardness(5.0F);
		setResistance(6000000.0F);
		setHarvestLevel("pickaxe", 0);
		setCreativeTab(ETab.engender);
	}
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add("Fuse all of your fusion parts together with fuel");
	}
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMonsterSpawnerSPC();
	}
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (stack.hasDisplayName())
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if ((tileentity instanceof TileEntityMonsterSpawnerSPC))
			{
				((TileEntityMonsterSpawnerSPC)tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if ((tileentity instanceof TileEntityMonsterSpawnerSPC))
			{
				FMLNetworkHandler.openGui(playerIn, EngenderMod.instance, EngenderMod.engenderfuserGUIID, playerIn.world, pos.getX(), pos.getY(), pos.getZ());
			}
			return true;
		}
	}
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof TileEntityMonsterSpawnerSPC)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityMonsterSpawnerSPC)tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
			
			if (((TileEntityMonsterSpawnerSPC)tileentity).mana > 0)
			worldIn.spawnEntity(new EntityManaOrb(worldIn, pos.getX(), pos.getY(), pos.getZ(), (int)((TileEntityMonsterSpawnerSPC)tileentity).mana, false));
			
			if (((TileEntityMonsterSpawnerSPC)tileentity).entropy > 0)
			worldIn.spawnEntity(new EntityManaOrb(worldIn, pos.getX(), pos.getY(), pos.getZ(), (int)((TileEntityMonsterSpawnerSPC)tileentity).entropy, true));
		}
		super.breakBlock(worldIn, pos, state);
	}
	public static void setState(boolean active, World worldIn, BlockPos pos)
	{
		@SuppressWarnings("unused")
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity != null)
		{
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(EBlock.mob_spawner_spc);
	}
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(EBlock.mob_spawner_spc);
	}
}