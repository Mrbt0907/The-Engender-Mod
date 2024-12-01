package net.mrbt0907.ageofminecraft.entity.tier1;

import net.minecraft.block.Block;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EntityAIFollow;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;

public class EntityCow extends EntityEngendered implements IJumpingMount
{
	protected float jumpPower;
	public EntityCow(World worldIn)
	{
		super(worldIn);
		setSize(0.9F, 1.3F);
	}
	
	protected void aiInit()
	{
		tasks.addTask(10, new EntityAIFollow(this, 1.1D));
		//tasks.addTask(0, new EntityAIWander(this, 1.0D, 100));
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}

	//----- OVERRIDES -----\\
	protected ResourceLocation getLootTable() {return LootRegistry.ENTITIES_COW;}
	protected SoundEvent getAmbientSound() {return SoundEvents.ENTITY_COW_AMBIENT;}
	protected SoundEvent getHurtSound(DamageSource source) {return SoundEvents.ENTITY_COW_HURT;}
	protected SoundEvent getDeathSound() {return SoundEvents.ENTITY_COW_DEATH;}
	protected void playStepSound(BlockPos pos, Block blockIn) {playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);}
	protected float getSoundVolume() {return 0.4F;}
	public void setJumpPower(int jumpPowerIn)
	{
		if (isBeingRidden())
		{
			if (jumpPowerIn < 0)
				jumpPowerIn = 0;
			if (jumpPowerIn >= 90)
				jumpPower = 1.0F;
			else
				jumpPower = 0.4F + 0.4F * (float)jumpPowerIn / 90.0F;
		}
	}
	public boolean canJump() {return true;}
	public void handleStartJump(int jumpPowerIn){playLivingSound();}
	public void handleStopJump() {}
	@Override
	public EnumTier getTier() {return null;}
	@Override
	public long getBaseVigor() {return 0;}
	@Override
	public long getBaseStrength() {return 0;}
	@Override
	public long getBaseStamina() {return 0;}
	@Override
	public long getBaseIntelligence() {return 0;}
	@Override
	public long getBaseDexterity() {return 0;}
	@Override
	public long getBaseAgility() {return 0;}
}