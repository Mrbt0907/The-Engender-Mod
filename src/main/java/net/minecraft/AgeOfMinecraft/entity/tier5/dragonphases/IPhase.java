package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;

import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract interface IPhase
{
	public abstract boolean getIsStationary();
	public abstract void doClientRenderEffects();
	public abstract void doLocalUpdate();
	public abstract void onCrystalDestroyed(EntityEnderCrystal paramEntityEnderCrystal, BlockPos paramBlockPos, DamageSource paramDamageSource, EntityPlayer paramEntityPlayer);
	public abstract void initPhase();
	public abstract void removeAreaEffect();
	public abstract float getMaxRiseOrFall();
	public abstract float getYawFactor();
	public abstract PhaseList<? extends IPhase> getPhaseList();
	public abstract Vec3d getTargetLocation();
	public abstract float getAdjustedDamage(MultiPartEntityPart paramMultiPartEntityPart, DamageSource paramDamageSource, float paramFloat);
}


