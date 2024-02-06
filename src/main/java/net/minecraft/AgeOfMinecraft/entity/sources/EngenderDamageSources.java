package net.minecraft.AgeOfMinecraft.entity.sources;

import net.minecraft.util.DamageSource;

public class EngenderDamageSources
{
	public static final DamageSource ERASURE =  new DamageSource("engender.erasure").setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource VOID =  new DamageSource("engender.void").setDamageAllowedInCreativeMode().setDamageBypassesArmor().setDamageIsAbsolute();
}
