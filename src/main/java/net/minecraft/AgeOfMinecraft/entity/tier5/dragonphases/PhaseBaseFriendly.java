package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
public abstract class PhaseBaseFriendly implements IPhase
{
	protected final EntityEnderDragon dragon;
	public PhaseBaseFriendly(EntityEnderDragon dragonIn)
	{
		this.dragon = dragonIn;
	}
	public boolean getIsStationary()
	{
		return false;
	}
	public void doClientRenderEffects() { }
	public void doLocalUpdate() { }
	public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc, EntityPlayer plyr) { }
	public void initPhase() { }
	public void removeAreaEffect() { }
	public float getMaxRiseOrFall()
	{
		return 0.6F;
	}
	public Vec3d getTargetLocation()
	{
		return null;
	}
	public float getAdjustedDamage(MultiPartEntityPart pt, DamageSource src, float damage)
	{
		return damage;
	}
	public float getYawFactor()
	{
		float f = MathHelper.sqrt(this.dragon.motionX * this.dragon.motionX + this.dragon.motionZ * this.dragon.motionZ) + 1.0F;
		float f1 = Math.min(f, 40.0F);
		return 0.7F / f1 / f;
	}
}


