package net.minecraft.AgeOfMinecraft.api.entity;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumSoundType;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySquid;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityType
{

	public static boolean canBeTurned(Entity entity)
	{
		return entity instanceof EntityPlayer || entity instanceof EntityZombie || entity instanceof EntitySquid ||
		entity instanceof net.minecraft.entity.monster.EntityZombie ||
		entity instanceof net.minecraft.entity.passive.EntitySquid ||
		entity instanceof EntitySkeleton ||
		entity instanceof net.minecraft.entity.monster.AbstractSkeleton ||
		entity instanceof EntityCreeper ||
		entity instanceof net.minecraft.entity.monster.EntityCreeper ||
		entity instanceof EntityVillager ||
		entity instanceof net.minecraft.entity.passive.EntityVillager;
	}

	//They ain't got time to bleed
	public static boolean doesntHaveTimeToBleed(Entity entity)
	{
		return entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isEntityUndead() || 
		entity instanceof net.minecraft.entity.monster.EntityBlaze ||
		entity instanceof net.minecraft.entity.monster.EntitySlime ||
		entity instanceof net.minecraft.entity.monster.EntityGolem ||
		entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entity).isUndead();
	}

	public static boolean isMetalLikeMob(Entity entity)
	{
		return entity instanceof net.minecraft.entity.monster.EntityBlaze || entity instanceof net.minecraft.entity.monster.EntityIronGolem || entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entity).getSoundType().equals(EnumSoundType.METAL);
	}

	public static boolean isWoodLikeMob(Entity entity)
	{
		return entity instanceof EntityWither || entity instanceof AbstractSkeleton || entity instanceof EntityShulker || entity instanceof EntityMooshroom ||
		entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entity).getSoundType().equals(EnumSoundType.WOOD);
	}

	/**
	* Returns true if the entity detects a Wither, false otherwise.
	*/
	public static boolean sensorsShowWithers(World world)
	{
		for (Entity entity : world.loadedEntityList)
			if (entity instanceof EntityWither)
				return true;
		return false;
	}

}
