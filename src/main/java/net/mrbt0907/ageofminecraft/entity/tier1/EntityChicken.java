package net.mrbt0907.ageofminecraft.entity.tier1;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.entity.Animal;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EntityFriendlyCreature;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.Light;
import net.mrbt0907.ageofminecraft.entity.Tiny;
import net.mrbt0907.ageofminecraft.entity.ai.EntityAIFollowLeader;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.entity.tier3.EntityZombie;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;

public class EntityChicken extends EntityEngendered implements IJumpingMount
{
	protected float jumpPower;
	public float wingRotation;
	public float destPos;
	public float field_70884_g;
	public float field_70888_h;
	public float wingRotDelta = 1.0F;
	public int timeUntilNextEgg;
	public boolean chickenJockey;
	public EntityChicken(World worldIn)
	{
		super(worldIn);
		this.timeUntilNextEgg = this.rand.nextInt(1800) + 1800;
		setPathPriority(PathNodeType.WATER, 0.0F);
		this.experienceValue = 1;
        this.setSize(0.4F, 0.7F);
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}
	
	public float getEyeHeight()
	{
		return this.height * 0.95F;
	}

	//----- OVERRIDES -----\\
	protected ResourceLocation getLootTable() {return LootRegistry.ENTITIES_CHICKEN;}
	protected SoundEvent getAmbientSound() {return SoundEvents.ENTITY_CHICKEN_AMBIENT;}
	protected SoundEvent getHurtSound(DamageSource source) {return SoundEvents.ENTITY_CHICKEN_HURT;}
	protected SoundEvent getDeathSound() {return SoundEvents.ENTITY_CHICKEN_DEATH;}
	protected void playStepSound(BlockPos pos, Block blockIn) {playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);}
	protected float getSoundVolume() {return 0.4F;}
	
	@Override
	public EnumTier getTier() {return null;}
	@Override
	public long getBaseVigor() {return 4;}
	@Override
	public long getBaseStrength() {return 0;}
	@Override
	public long getBaseStamina() {return 0;}
	@Override
	public long getBaseIntelligence() {return 0;}
	@Override
	public long getBaseDexterity() {return 20;}
	@Override
	public long getBaseAgility() {return 0;}
	@Override
	public EnumAIStance getDefaultStance() {return EnumAIStance.PASSIVE;}
	
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
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		this.field_70888_h = this.wingRotation;
		this.field_70884_g = this.destPos;
		this.destPos = ((float)(this.destPos + (this.onGround ? -1 : 4) * 0.3D));
		this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
		if ((!this.onGround) && (this.wingRotDelta < 1.0F))
		{
			this.wingRotDelta = 1.0F;
		}
		this.wingRotDelta = ((float)(this.wingRotDelta * 0.9D));
		if (!this.onGround && this.motionY < 0.0D && this.isEntityAlive())
		this.motionY *= 0.6D;
		
		this.wingRotation += this.wingRotDelta * 2.0F;
		if ((!this.world.isRemote) && (!isChild()) && (!isChickenJockey()) && (--this.timeUntilNextEgg <= 0))
		{
			playSound(SoundEvents.ENTITY_CHICKEN_EGG, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			dropItem(Items.EGG, 1);
			this.timeUntilNextEgg = this.rand.nextInt(1800) + 1800;
		}
	}

	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.chickenJockey = tagCompund.getBoolean("IsChickenJockey");
		if (tagCompund.hasKey("EggLayTime"))
		{
			this.timeUntilNextEgg = tagCompund.getInteger("EggLayTime");
		}
	}
	
	protected int getExperiencePoints(EntityPlayer player)
	{
		return isChickenJockey() ? 10 : super.getExperiencePoints(player);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setBoolean("IsChickenJockey", this.chickenJockey);
		tagCompound.setInteger("EggLayTime", this.timeUntilNextEgg);
	}
	
	public double getMountedYOffset()
	{
		return (double)this.height * 0.65D;
	}

	public void updatePassenger(Entity passenger)
	{
		super.updatePassenger(passenger);
		float f = MathHelper.sin(this.renderYawOffset * 0.017453292F);
		float f1 = MathHelper.cos(this.renderYawOffset * 0.017453292F);
		float f2 = 0.1F;
		float f3 = 0.0F;
		passenger.setPosition(this.posX + f2 * f, this.posY + this.getMountedYOffset() + passenger.getYOffset() + f3, this.posZ - f2 * f1);
		if ((passenger instanceof EntityLivingBase))
		{
			((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset;
		}
	}

			public boolean isChickenJockey()
			{
				return this.chickenJockey;
			}
			public void setChickenJockey(boolean jockey)
			{
				this.chickenJockey = jockey;
			}
		}