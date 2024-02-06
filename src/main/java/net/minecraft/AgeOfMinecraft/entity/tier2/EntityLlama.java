package net.minecraft.AgeOfMinecraft.entity.tier2;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.Animal;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAttackRangedAlly;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityLlama extends EntityFriendlyCreature implements IRangedAttackMob, Light, Animal
{
	private static final DataParameter<Integer> DATA_COLOR_ID = EntityDataManager.<Integer>createKey(EntityLlama.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DATA_VARIANT_ID = EntityDataManager.<Integer>createKey(EntityLlama.class, DataSerializers.VARINT);
	
	public EntityLlama(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 1.87F);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.2D, 32.0F, 6.0F));
		this.tasks.addTask(3, new EntityAIAttackRangedAlly(this, 1.2D, 20, 50, 12F));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.experienceValue = 3;
	}
	
	public float getBlockPathWeight(BlockPos pos)
	{
		return this.world.getBlockState(pos.down()).getBlock() == this.spawnableBlock ? 10.0F : this.world.getLightBrightness(pos) - 0.5F;
	}
	
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityLlama(this.world);
	}

	/**
	* (abstract) Protected helper method to write subclass entity data to NBT.
	*/
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("Variant", this.getVariant());
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.setVariant(compound.getInteger("Variant"));
	}
	public boolean canBeButchered()
	{
		return true;
	}
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
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((double)(15.0F + (float)this.rand.nextInt(8) + (float)this.rand.nextInt(9)));
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(DATA_COLOR_ID, Integer.valueOf(-1));
		this.dataManager.register(DATA_VARIANT_ID, Integer.valueOf(0));
	}

	public int getVariant()
	{
		return MathHelper.clamp(((Integer)this.dataManager.get(DATA_VARIANT_ID)).intValue(), 0, 3);
	}

	public void setVariant(int variantIn)
	{
		this.dataManager.set(DATA_VARIANT_ID, Integer.valueOf(variantIn));
	}

	public void updatePassenger(Entity passenger)
	{
		if (this.isPassenger(passenger))
		{
			float f = MathHelper.cos(this.renderYawOffset * 0.017453292F);
			float f1 = MathHelper.sin(this.renderYawOffset * 0.017453292F);
			passenger.setPosition(this.posX + (double)(0.3F * f1), this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ - (double)(0.3F * f));
		}
	}

	/**
	* Returns the Y offset from the entity's position for any entity riding this one.
	*/
	public double getMountedYOffset()
	{
		return (double)this.height * 0.67D;
	}

	protected boolean handleEating(EntityPlayer player, ItemStack stack)
	{
		float f = 0.0F;
		boolean flag = false;
		Item item = stack.getItem();
		
		if (item == Items.WHEAT)
		{
			f = 2.0F;
		}

		if (this.getHealth() < this.getMaxHealth() && f > 0.0F)
		{
			this.heal(f);
			flag = true;
		}

		if (flag && !this.isSilent())
		{
			this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
		}

		return flag;
	}

	/**
	* Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
	* when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
	*/
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		
		int i;
		
		if (livingdata instanceof EntityLlama.GroupData)
		{
			i = ((EntityLlama.GroupData)livingdata).variant;
		}
		else
		{
			i = this.rand.nextInt(4);
			livingdata = new EntityLlama.GroupData(i);
		}

		this.setVariant(i);
		return livingdata;
	}

	@SideOnly(Side.CLIENT)
	public boolean hasColor()
	{
		return this.getColor() != null;
	}

	protected SoundEvent getAngrySound()
	{
		return SoundEvents.ENTITY_LLAMA_ANGRY;
	}

	protected SoundEvent getAmbientSound()
	{
		return this.getAttackTarget() != null || rand.nextInt(5) == 0 ? SoundEvents.ENTITY_LLAMA_ANGRY : SoundEvents.ENTITY_LLAMA_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_LLAMA_HURT;
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_LLAMA_DEATH;
	}

	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		this.playSound(SoundEvents.ENTITY_LLAMA_STEP, 0.15F, 1.0F / this.getFittness());
	}

	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_LLAMA;
	}

	public void makeMad()
	{
		SoundEvent soundevent = this.getAngrySound();
		
		if (soundevent != null)
		{
			this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
		}
	}

	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (stack.isEmpty() && getRidingEntity() == null)
		{
			if (!isWild() && this.isOnSameTeam(player) && !this.isChild() && !this.world.isRemote)
			player.startRiding(this);
			return true;
		}
		else
		{
			return false;
		}
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.setSize(0.9F, 1.87F);
	}
	public void travel(float strafe, float vertical, float forward)
	{
		if (isBeingRidden())
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
			this.rotationYawHead = entitylivingbase.rotationYawHead;
			this.rotationPitch = entitylivingbase.rotationPitch;
			setRotation(this.rotationYaw, this.rotationPitch);
			strafe = entitylivingbase.moveStrafing;
			forward = entitylivingbase.moveForward;
			
			if (forward != 0F)
			{
				this.rotationYaw = this.renderYawOffset = this.rotationYawHead;
				this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
			}
			if (this.isInWater() || this.isInLava())
			{
				this.motionY += 0.05D;
			}
			if (canPassengerSteer())
			{
				setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * ((this.isInWater() || this.isInLava()) ? 20F : 1F));
				super.travel(strafe, vertical, forward);
			}
			else if ((entitylivingbase instanceof EntityPlayer))
			{
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}
			if ((entitylivingbase.moveForward > 0.0F) && (this.ticksExisted % 7 == 0))
			{
				playStepSound(new BlockPos(this), this.world.getBlockState(new BlockPos(this)).getBlock());
			}
			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d1 = this.posX - this.prevPosX;
			double d0 = this.posZ - this.prevPosZ;
			float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 2.0F;
			if (f2 > 1.0F)
			{
				f2 = 1.0F;
			}
			this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.6F;
			this.limbSwing += this.limbSwingAmount * 0.25F;
			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d5 = this.posX - this.prevPosX;
			double d7 = this.posZ - this.prevPosZ;
			float f10 = MathHelper.sqrt(d5 * d5 + d7 * d7) * 4.0F;
			
			if (f10 > 1.0F)
			{
				f10 = 1.0F;
			}

			this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
			this.limbSwing += this.limbSwingAmount;
		}
		else
		{
			super.travel(strafe, vertical, forward);
		}
	}

	@Nullable
	public EnumDyeColor getColor()
	{
		int i = ((Integer)this.dataManager.get(DATA_COLOR_ID)).intValue();
		return i == -1 ? null : EnumDyeColor.byMetadata(i);
	}

	public int getMaxTemper()
	{
		return 30;
	}

	private void spit(EntityLivingBase target)
	{
		EntityLlamaSpitOther entityllamaspit = new EntityLlamaSpitOther(this.world, this);
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY - 1.25D - entityllamaspit.posY;
		double d2 = target.posZ - this.posZ;
		float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.25F;
		entityllamaspit.shoot(d0, d1 + (double)f, d2, 2F, 4.0F);
		this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LLAMA_SPIT, this.getSoundCategory(), this.getSoundVolume(), this.getSoundPitch());
		this.world.spawnEntity(entityllamaspit);
	}

	/**
	* Attack the specified entity using a ranged attack.
	*/
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		if (this.getDistance(target) <= (double)(target.width + this.width))
		{
			this.attackEntityAsMob(target);
			this.getNavigator().tryMoveToEntityLiving(target, 1.2D);
		}
		else
		this.spit(target);
	}

	static class GroupData implements IEntityLivingData
	{
		public int variant;
		
		private GroupData(int variantIn)
		{
			this.variant = variantIn;
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {}
	
	@Override
	public EnumTier getTier()
	{
		return EnumTier.TIER1;
	}
}