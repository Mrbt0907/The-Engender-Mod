package net.minecraft.AgeOfMinecraft.entity.tier3;
import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPrisonSlime extends EntitySlime
{
	public EntityPrisonSlime(World worldIn)
	{
		super(worldIn);
		this.isImmuneToFire = true;
	}
	public void setSlimeSize(int size)
	{
		super.setSlimeSize(size);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(size * 1);
	}
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15728880;
	}
	public float getBrightness()
	{
		return 1.0F;
	}
	protected EntitySlime createInstance()
	{
		return new EntityPrisonSlime(this.world);
	}
	protected void handleJumpLava()
	{
		this.motionY = (0.22F + getSlimeSize() * 0.05F);
		this.isAirBorne = true;
	}
	protected int getAttackStrength()
	{
		return super.getAttackStrength() + 1;
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return this.isSmallSlime() ? ELoot.ENTITIES_PRISON_SLIME : LootTableList.EMPTY;
	}
	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		if (super.attackEntityAsMob(p_70652_1_))
		{
			p_70652_1_.motionX = 0D;
			p_70652_1_.motionY = 0D;
			p_70652_1_.motionZ = 0D;
			
			if (p_70652_1_ instanceof EntityLivingBase)
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
}