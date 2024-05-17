package net.minecraft.AgeOfMinecraft.items.cameos.darkness;

import net.endermanofdoom.mac.item.ItemBowEX;
import net.endermanofdoom.mac.item.ItemUtils;
import net.endermanofdoom.mac.util.math.Vec3;
import net.minecraft.AgeOfMinecraft.entity.cameos.Darkness.EntityDarkProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemDarknessBow extends ItemBowEX
{
	protected final int minChargeTime = 4;
	public ItemDarknessBow()
	{
		super(null, 40);
		setArrowAccuracy(1.0F);
		setArrowAmount(1);
		setArrowDamage(50);
		setArrowVelocity(9.0F);
		setAutofire(true);
		setChargeTime(20);
	}
	
	@Override
	public void onStartUse(ItemStack stack, World world, EntityPlayer shooter)
	{
		
	}
	
	@Override
	public void onTickUse(ItemStack stack, World world, EntityPlayer shooter, int timeLeft)
	{
		
	}

	@Override
	public void onStopUse(ItemStack stack, World world, EntityPlayer shooter, int timeLeft)
	{
		NBTTagCompound nbt = ItemUtils.loadNBT(stack);
		nbt.setInteger("chargeTime", chargeTime);
		ItemUtils.saveNBT(stack, nbt);
	}

	@Override
	public void onShootPre(ItemStack stack, World world, EntityPlayer shooter, int timeLeft)
	{
		
	}

	@Override
	public void onShoot(ItemStack stack, World world, EntityPlayer shooter, EntityArrow arrow, int timeLeft, int arrowIndex)
	{
		if (world.isRemote || arrowIndex != 0) return;
		NBTTagCompound nbt = ItemUtils.loadNBT(stack);
		if (nbt.getInteger("shotLast") > shooter.ticksExisted && nbt.getInteger("shotLast") - shooter.ticksExisted <= 60) return;
		EntityDarkProjectile projectile = new EntityDarkProjectile(world, shooter, null, (byte) 2, true);
		Vec3 position = getShootPos(stack, world, shooter, arrowIndex);
		projectile.setPosition(position.posX, position.posY + 3.0D, position.posZ);
		world.spawnEntity(projectile);

		projectile = new EntityDarkProjectile(world, shooter, null, (byte) 2, true);
		position = getShootPos(stack, world, shooter, arrowIndex);
		projectile.setPosition(position.posX, position.posY - 5.0D, position.posZ);
		world.spawnEntity(projectile);
		
		nbt.setInteger("shotLast", shooter.ticksExisted + 60);
		ItemUtils.saveNBT(stack, nbt);
		
		if (world.isRemote)
			world.playSound(position.posX, position.posY, position.posZ, SoundEvents.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.HOSTILE, 15.0F, world.rand.nextFloat() * 0.3F +  0.6F, false);	
	}

	@Override
	public void onShootPost(ItemStack stack, World world, EntityPlayer shooter, int timeLeft)
	{
		NBTTagCompound nbt = ItemUtils.loadNBT(stack);
		if (!nbt.hasKey("chargeTime"))
			nbt.setInteger("chargeTime", chargeTime - 1);
		else if (nbt.getInteger("chargeTime") > minChargeTime)
			nbt.setInteger("chargeTime", nbt.getInteger("chargeTime") - 1);
		ItemUtils.saveNBT(stack, nbt);
	}

	@Override
	public void onShootFail(ItemStack stack, World world, EntityPlayer shooter, int timeLeft)
	{
		
	}
	
	@Override
	public int getChargeTime(ItemStack stack)
	{
		NBTTagCompound nbt = ItemUtils.loadNBT(stack);
		return nbt.hasKey("chargeTime") ? nbt.getInteger("chargeTime") : chargeTime;
	}

	@Override
	public EntityArrow onCreateArrow(ItemStack stack, World world, EntityPlayer shooter, EntityArrow arrow, int timeLeft, int arrowIndex)
	{
		return arrow;
	}
}
