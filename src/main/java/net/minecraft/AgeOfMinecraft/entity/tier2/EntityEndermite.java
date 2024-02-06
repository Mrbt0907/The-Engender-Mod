package net.minecraft.AgeOfMinecraft.entity.tier2;
import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.Ender;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.Tiny;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityEndermite
extends EntityFriendlyCreature implements Light, Tiny, Ender
{
	public EntityEndermite(World worldIn)
	{
		super(worldIn);
		setSize(0.4F, 0.3F);
		this.isOffensive = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.2D, 24.0F, 6.0F));
		this.tasks.addTask(2, new EntityAIFriendlyAttackMelee(this, 1.2D, true));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D, 80));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.experienceValue = 1;
	}
	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 0.25F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.25F;
	}

	public int timesToConvert()
	{
		return 3;
	}
	public boolean isASwarmingMob()
	{
		return true;
	}
	public float getEyeHeight()
	{
		return this.height / 3;
	}

	public boolean isChild()
	{
		return false;
	}
	public void setChild(boolean childZombie) { }
	
	public boolean canBeMatedWith()
	{
		return false;
	}

	public boolean canBeMarried()
	{
		return false;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER2;
	}
	public void performSpecialAttack()
	{
		setSpecialAttackTimer(400);
		playSound(ESound.bugSpecial, 10.0F, 1.0F);
		if (!this.world.isRemote)
		{
			for (int i = 0; i < 2; i++)
			{
				EntityEndermite mob = new EntityEndermite(this.world);
				mob.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				mob.onInitialSpawn(this.world.getDifficultyForLocation(getPosition()), null);
				this.world.spawnEntity(mob);
				mob.setOwnerId(getOwnerId());
			}
		}
	}
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_ENDERMITE_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_ENDERMITE_DEATH;
	}
	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		playSound(SoundEvents.ENTITY_ENDERMITE_STEP, 0.15F, 1.0F / this.getFittness());
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_ENDERMITE;
	}
	public void onUpdate()
	{
		this.prevRenderYawOffset = this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
		super.onUpdate();
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		setSize(0.4F, 0.3F);
		if ((getAttackTarget() != null) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			performSpecialAttack();
		}

		if ((getSpecialAttackTimer() >= 380) && (isHero()))
		{
			this.getNavigator().clearPath();
			this.motionX = 0D;
			this.motionZ = 0D;
			float f2 = this.renderYawOffset * 0.017453292F;
			float f19 = MathHelper.sin(f2);
			float f3 = MathHelper.cos(f2);
			for (int i = 0; i < 32; i++)
			{
				this.world.spawnParticle(EnumParticleTypes.END_ROD, true, this.posX, this.posY + this.rand.nextDouble() * this.height * 2.0D, this.posZ, f3 * 0.15F, 0.01D, f19 * 0.15F, new int[0]);
				this.world.spawnParticle(EnumParticleTypes.END_ROD, true, this.posX, this.posY + this.rand.nextDouble() * this.height * 2.0D, this.posZ, f3 * -0.15F, 0.01D, f19 * -0.15F, new int[0]);
			}
		}
		if (this.world.isRemote)
		this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
		
}
		public EnumCreatureAttribute getCreatureAttribute()
		{
			return EnumCreatureAttribute.ARTHROPOD;
		}
	}

	
	