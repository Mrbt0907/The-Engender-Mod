package net.mrbt0907.ageofminecraft.entity.tier1;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;

public class EntityChicken extends EntityEngendered implements IJumpingMount
{
	protected float jumpPower;
	public float wingRotation;
	public float destPos;
	public float oFlapSpeed;
	public float oFlap;
	public float wingRotDelta = 1.0F;
	public int timeUntilNextEgg;
	public boolean chickenJockey;
	
	public EntityChicken(World worldIn)
	{
		super(worldIn);
		timeUntilNextEgg = rand.nextInt(1800) + 1800;
		setPathPriority(PathNodeType.WATER, 0.0F);
		setSize(0.4F, 0.7F);
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}

	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		chickenJockey = tagCompund.getBoolean("IsChickenJockey");
		if (tagCompund.hasKey("EggLayTime"))
		{
			timeUntilNextEgg = tagCompund.getInteger("EggLayTime");
		}
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setBoolean("IsChickenJockey", chickenJockey);
		tagCompound.setInteger("EggLayTime", timeUntilNextEgg);
	}
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

		oFlap = wingRotation;
		oFlapSpeed = destPos;
		destPos = ((float)(destPos + (onGround ? -1 : 4) * 0.3D));
		destPos = MathHelper.clamp(destPos, 0.0F, 1.0F);
		if ((!onGround) && (wingRotDelta < 1.0F))
			wingRotDelta = 1.0F;
		wingRotDelta = ((float)(wingRotDelta * 0.9D));
		if (!onGround && motionY < 0.0D && isEntityAlive())
			motionY *= 0.6D;
		wingRotation += wingRotDelta * 2.0F;
		
		if (!world.isRemote && !isChild() && !isChickenJockey() && --timeUntilNextEgg <= 0)
		{
			playSound(SoundEvents.ENTITY_CHICKEN_EGG, getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			dropItem(Items.EGG, 1);
			timeUntilNextEgg = rand.nextInt(1800) + 1800;
		}
	}
	
	public void updatePassenger(Entity passenger)
	{
		super.updatePassenger(passenger);
		double f = Maths.fastSin(renderYawOffset * 0.017453292F);
		double f1 = Maths.fastCos(renderYawOffset * 0.017453292F);
		float f2 = 0.1F;
		float f3 = 0.0F;
		passenger.setPosition(posX + f2 * f, posY + getMountedYOffset() + passenger.getYOffset() + f3, posZ - f2 * f1);
		if ((passenger instanceof EntityLivingBase))
			((EntityLivingBase)passenger).renderYawOffset = renderYawOffset;
	}
	
	//----- OVERRIDES -----\\
	protected ResourceLocation getLootTable() {return LootRegistry.ENTITIES_CHICKEN;}
	protected SoundEvent getAmbientSound() {return SoundEvents.ENTITY_CHICKEN_AMBIENT;}
	protected SoundEvent getHurtSound(DamageSource source) {return SoundEvents.ENTITY_CHICKEN_HURT;}
	protected SoundEvent getDeathSound() {return SoundEvents.ENTITY_CHICKEN_DEATH;}
	protected void playStepSound(BlockPos pos, Block blockIn) {playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);}
	
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
	
	public float getEyeHeight()
	{
		return height * 0.95F;
	}
	
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
	
	public double getMountedYOffset()
	{
		return (double)height * 0.65D;
	}

	public boolean isChickenJockey()
	{
		return chickenJockey;
	}
	
	public void setChickenJockey(boolean jockey)
	{
		chickenJockey = jockey;
	}		
}