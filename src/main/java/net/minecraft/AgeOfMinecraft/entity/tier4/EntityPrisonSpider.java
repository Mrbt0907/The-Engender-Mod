package net.minecraft.AgeOfMinecraft.entity.tier4;
import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPrisonSpider extends EntitySpider
{
	public EntityPrisonSpider(World worldIn)
	{
		super(worldIn);
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
	}

	protected float getSoundPitch()
	{
		return super.getSoundPitch();
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityPrisonSpider(this.world);
	}
	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		if (super.attackEntityAsMob(p_70652_1_))
		{
			p_70652_1_.motionX = 0D;
			p_70652_1_.motionY = 0D;
			p_70652_1_.motionZ = 0D;
			
			if ((p_70652_1_ instanceof EntityLivingBase))
			{
				this.inflictCustomStatusEffect(this.world.getDifficulty(), (EntityLivingBase)p_70652_1_, MobEffects.SLOWNESS, 5, 1);
				if (this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD)
				this.inflictCustomStatusEffect(this.world.getDifficulty(), (EntityLivingBase)p_70652_1_, MobEffects.SLOWNESS, 5, 0);
				if (this.world.getDifficulty() == EnumDifficulty.HARD)
				this.inflictCustomStatusEffect(this.world.getDifficulty(), (EntityLivingBase)p_70652_1_, MobEffects.SLOWNESS, 5, 0);
				if (p_70652_1_ instanceof EntityLiving && ((EntityLiving)p_70652_1_).getAttackTarget() != null && this.world.getDifficulty() == EnumDifficulty.HARD && rand.nextInt(3) == 0)
				((EntityLiving)p_70652_1_).setAttackTarget(null);
			}
			return true;
		}
		return false;
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER4;
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_PRISON_SPIDER;
	}
}