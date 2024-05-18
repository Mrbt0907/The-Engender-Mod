package net.minecraft.AgeOfMinecraft.items;

import net.endermanofdoom.mac.entity.EntityArrowEX;
import net.endermanofdoom.mac.item.ItemArrowEX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemArrowExplosive extends ItemArrowEX
{
	@Override
	public EntityArrowEX onArrowCreate(World world, ItemStack stack, EntityLivingBase shooter, EntityArrowEX arrow)
	{
		return arrow;
	}

	@Override
	public void onArrowTick(World world, EntityLivingBase shooter, EntityArrowEX arrow)
	{
		
	}

	@Override
	public void onArrowTickAir(World world, EntityLivingBase shooter, EntityArrowEX arrow)
	{
		
	}

	@Override
	public void onArrowTickGround(World world, EntityLivingBase shooter, EntityArrowEX arrow)
	{
		
	}

	@Override
	public void onArrowTickWater(World world, EntityLivingBase shooter, EntityArrowEX arrow)
	{
		
	}

	@Override
	public void onArrowHit(World world, EntityLivingBase shooter, Entity victim, EntityArrowEX arrow)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrowHitBlock(World world, EntityLivingBase shooter, BlockPos position, EntityArrowEX arrow)
	{
		
	}

	@Override
	public void onArrowStop(World world, EntityLivingBase shooter, EntityArrowEX arrow)
	{
		world.newExplosion(arrow, arrow.posX, arrow.posY, arrow.posZ, 1.0F, false, true);
		arrow.setDead();
	}
}
