package net.minecraft.AgeOfMinecraft.items;

import java.util.function.Predicate;

import net.endermanofdoom.mac.item.ItemBowEX;
import net.endermanofdoom.mac.util.math.Maths;
import net.endermanofdoom.mac.util.math.Vec;
import net.endermanofdoom.mac.util.math.Vec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemStormBow extends ItemBowEX {

	public ItemStormBow()
	{
		super(null, 30);
		setMaxDamage(1000);
		setAutofire(true);
		setChargeTime(2);
		setArrowAmount(3);
	}

	@Override
	public Vec3 getShootPos(ItemStack stack, World world, EntityPlayer shooter, int arrowIndex)
	{
		Vec3 position = new Vec3(shooter);
		position.posX += Maths.random(-16.0D, 16.0D);
		position.posY += shooter.eyeHeight + 32.0D;
		position.posZ += Maths.random(-16.0D, 16.0D);
		return position;
	}
	
	@Override
	public Vec getShootRot(ItemStack stack, World world, EntityPlayer shooter, int arrowIndex)
	{
		return new Vec(90F, shooter.rotationYaw);
	}
	
	@Override
	public void onStartUse(ItemStack stack, World world, EntityPlayer shooter) {}

	@Override
	public void onTickUse(ItemStack stack, World world, EntityPlayer shooter, int timeLeft) {}

	@Override
	public void onStopUse(ItemStack stack, World world, EntityPlayer shooter, int timeLeft) {}

	@Override
	public void onShootPre(ItemStack stack, World world, EntityPlayer shooter, int timeLeft) {}

	@Override
	public void onShoot(ItemStack stack, World world, EntityPlayer shooter, EntityArrow arrow, int timeLeft, int arrowIndex) {}

	@Override
	public void onShootPost(ItemStack stack, World world, EntityPlayer shooter, int timeLeft) {}

	@Override
	public void onShootFail(ItemStack stack, World world, EntityPlayer shooter, int timeLeft) {}

	@Override
	public EntityArrow onCreateArrow(ItemStack stack, World world, EntityPlayer shooter, EntityArrow arrow, int timeLeft, int arrowIndex)
	{
		return arrow;
	}
}
