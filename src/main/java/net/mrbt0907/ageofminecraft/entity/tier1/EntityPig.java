package net.mrbt0907.ageofminecraft.entity.tier1;
import net.minecraft.block.Block;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;

public class EntityPig extends EntityEngendered implements IJumpingMount
{
	protected float jumpPower;
	private static final DataParameter<Boolean> SADDLED = EntityDataManager.createKey(EntityPig.class, DataSerializers.BOOLEAN);
	
	public EntityPig(World worldIn)
	{
		super(worldIn);
		setSize(0.9F, 0.9F);
	}
	
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SADDLED, Boolean.valueOf(false));
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setBoolean("Saddle", getSaddled());
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		setSaddled(tagCompund.getBoolean("Saddle"));
	}

	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		if (!world.isRemote && getSaddled())
		{
			dropItem(Items.SADDLE, 1);
			setSaddled(false);
		}
	}
	
	//----- OVERRIDES -----\\
	protected ResourceLocation getLootTable() {return LootRegistry.ENTITIES_PIG;}
	protected SoundEvent getAmbientSound() {return SoundEvents.ENTITY_PIG_AMBIENT;}
	protected SoundEvent getHurtSound(DamageSource source) {return SoundEvents.ENTITY_PIG_HURT;}
	protected SoundEvent getDeathSound() {return SoundEvents.ENTITY_PIG_DEATH;}
	protected void playStepSound(BlockPos pos, Block blockIn) {playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);}
	protected float getSoundVolume() {return 0.4F;}
	
	@Override
	public EnumTier getTier() {return null;}
	@Override
	public long getBaseVigor() {return 10;}
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
	@Override
	public EnumAIStance getDefaultStance() {return EnumAIStance.AGGRESSIVE;}
	
	@Override
	public void setJumpPower(int jumpPower)
	{
		if (isBeingRidden())
		{
			if (jumpPower < 0)
				jumpPower = 0;

			if (jumpPower >= 90)
				this.jumpPower = 1.0F;
			else
				this.jumpPower = 0.4F + 0.4F * (float)jumpPower / 90.0F;
		}
	}

	@Override
	public boolean canJump()
	{
		return true;
	}

	@Override
	public void handleStartJump(int jumpPower)
	{
		playLivingSound();
	}

	@Override
	public void handleStopJump() {}

	public boolean getSaddled()
	{
		return ((Boolean)this.dataManager.get(SADDLED)).booleanValue();
	}
	
	public void setSaddled(boolean saddled)
	{
		if (saddled)
		{
			this.dataManager.set(SADDLED, Boolean.valueOf(true));
		}
		else
		{
			this.dataManager.set(SADDLED, Boolean.valueOf(false));
		}
	}
}