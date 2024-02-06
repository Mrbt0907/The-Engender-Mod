package net.minecraft.AgeOfMinecraft.blocks;

import java.util.Random;

import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSSensor extends Block
{
	
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	
    public BlockSSensor(String name)
    {
    	super(Material.CLAY);
    	setHardness(0.6F);
        setResistance(10.0F);
        setUnlocalizedName(name);
        setCreativeTab(ETab.engender);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        world.setBlockState(pos, state.withProperty(POWER, 0), 3);
        world.scheduleBlockUpdate(pos, this, 100, 1);
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos,
    		EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
    		EntityLivingBase placer) {
    	worldIn.scheduleBlockUpdate(pos, this, 10, 1);
    	return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return ((Integer)blockState.getValue(POWER)).intValue();
    }
    
    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {POWER});
    }
}

