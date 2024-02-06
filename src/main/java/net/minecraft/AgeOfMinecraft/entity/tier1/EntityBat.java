package net.minecraft.AgeOfMinecraft.entity.tier1;
import java.util.Calendar;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.Animal;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.Tiny;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityBat extends EntityFriendlyCreature implements EntityFlying, Light, Flying, Tiny, Animal
{
	private static final DataParameter<Byte> HANGING = EntityDataManager.createKey(EntityBat.class, DataSerializers.BYTE);
	private BlockPos spawnPosition;
	public EntityBat(World worldIn)
	{
		super(worldIn);
		this.tasks.addTask(2, new EntityAIFriendlyAttackMelee(this, 1.0D, true));
		setIsBatHanging(true);
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 2D, 16F, 8F));
		this.experienceValue = 1;
	}
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(HANGING, Byte.valueOf((byte)0));
	}
	public boolean canBeMatedWith()
	{
		return false;
	}

	public boolean canBeMarried()
	{
		return false;
	}

	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 1.5F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 0.5F;
	}

	/**
	* Bonus damage vs mobs that implement Flying
	*/
	public float getBonusVSFlying()
	{
		return 2F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.1F;
	}

	/**
	* Bonus damage vs mobs that implement Tiny
	*/
	public float getBonusVSTiny()
	{
		return 1.25F;
	}

	/**
	* Bonus damage vs mobs that implement Structure
	*/
	public float getBonusVSStructure()
	{
		return 0.1F;
	}

	/**
	* Bonus damage vs mobs that implement Elemental
	*/
	public float getBonusVSElemental()
	{
		return 1;
	}

	/**
	* Bonus damage vs mobs that implement Undead
	*/
	public float getBonusVSUndead()
	{
		return 0.5F;
	}

	/**
	* Bonus damage vs mobs that implement Ender
	*/
	public float getBonusVSEnder()
	{
		return 0.25F;
	}

	/**
	* Bonus damage vs mobs that implement Animal
	*/
	public float getBonusVSAnimal()
	{
		return 1.5F;
	}
	protected float getSoundVolume()
	{
		return 0.1F;
	}
	protected float getSoundPitch()
	{
		return super.getSoundPitch() * 0.95F;
	}
	protected SoundEvent getAmbientSound()
	{
		return (getIsBatHanging()) && (this.rand.nextInt(4) != 0) ? null : SoundEvents.ENTITY_BAT_AMBIENT;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_BAT_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_BAT_DEATH;
	}
	public boolean canBePushed()
	{
		return false;
	}
	protected void collideWithEntity(Entity entityIn) { }
	protected void collideWithNearbyEntities() { }
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}
	public boolean getIsBatHanging()
	{
		return (((Byte)this.dataManager.get(HANGING)).byteValue() & 0x1) != 0;
	}
	public void setIsBatHanging(boolean isHanging)
	{
		byte b0 = ((Byte)this.dataManager.get(HANGING)).byteValue();
		if (isHanging)
		{
			this.dataManager.set(HANGING, Byte.valueOf((byte)(b0 | 0x1)));
		}
		else
		{
			this.dataManager.set(HANGING, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
		}
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		setSize(0.5F, 0.9F);
		
		if (!isWet() && this.isEntityAlive() && this.getIllusionFormTime() > 0)
		{
			if ((this.rand.nextInt(24) == 0) && (!isSilent()))
			{
				this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, getSoundCategory(), this.getSoundVolume(), this.rand.nextFloat() * 0.7F + 0.3F, false);
			}

			for (int i = 0; i < 2; i++)
			{
				if (isSneaking() || this.isChild())
					this.world.spawnParticle(this.isAntiMob() ? EnumParticleTypes.EXPLOSION_NORMAL : EnumParticleTypes.SMOKE_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * 0.6F, this.posY + this.rand.nextDouble() * 1.8F, this.posZ + (this.rand.nextDouble() - 0.5D) * 0.6F, 0.0D, 0.0D, 0.0D, new int[0]);
				else
					this.world.spawnParticle(this.isAntiMob() ? EnumParticleTypes.EXPLOSION_NORMAL : EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * 0.6F, this.posY + this.rand.nextDouble() * 1.8F, this.posZ + (this.rand.nextDouble() - 0.5D) * 0.6F, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}
	public void onUpdate()
	{
		super.onUpdate();
		if (getIsBatHanging())
		{
			this.motionX = (this.motionY = this.motionZ = 0.0D);
			this.posY = (MathHelper.floor(this.posY) + 1.0D - this.height);
		}
		else
			if (!this.onGround && this.motionY < 0.0D && this.isEntityAlive())
		this.motionY *= 0.6D;
	}
	protected void updateAITasks()
	{
		super.updateAITasks();
		BlockPos blockpos = new BlockPos(this);
		BlockPos blockpos1 = blockpos.up();
		if (getIsBatHanging())
		{
			if (!this.world.getBlockState(blockpos1).isNormalCube())
			{
				setIsBatHanging(false);
				this.world.playEvent((EntityPlayer)null, 1025, blockpos, 0);
			}
			else
			{
				if (this.rand.nextInt(200) == 0)
				{
					this.rotationYawHead = this.rand.nextInt(360);
				}
				if (this.world.getNearestPlayerNotCreative(this, 4.0D) != null)
				{
					setIsBatHanging(false);
					this.world.playEvent((EntityPlayer)null, 1025, blockpos, 0);
				}
			}
		}
		else
		{
			if ((this.spawnPosition != null) && ((!this.world.isAirBlock(this.spawnPosition)) || (this.spawnPosition.getY() < 1)))
			{
				this.spawnPosition = null;
			}
			if ((this.spawnPosition == null) || (this.rand.nextInt(30) == 0) || (this.spawnPosition.distanceSq((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0D))
			{
				this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
			}
			if ((this.rand.nextInt(100) == 0) && (this.world.getBlockState(blockpos1).isNormalCube()))
			{
				setIsBatHanging(true);
			}
			if (this.getActivePotionEffect(MobEffects.LUCK) == null)
			{
				if ((this.world.getClosestPlayerToEntity(this, 200D) != null) && this.getAttackTarget() == null && (this.world.getClosestPlayerToEntity(this, 200D) == getOwner()) && (getDistanceSq(getOwner()) > 200D))
				{
					double d01 = getOwner().posX - this.posX;
					double d11 = getOwner().posY - this.posY;
					double d21 = getOwner().posZ - this.posZ;
					float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11 + d21 * d21);
					this.motionX = (d01 / f2 * 0.5D * 0.5D + this.motionX * 0.5D);
					this.motionY = (d11 / f2 * 0.5D * 0.5D + this.motionZ * 0.5D);
					this.motionZ = (d21 / f2 * 0.5D * 0.5D + this.motionZ * 0.5D);
					faceEntity(getOwner(), 180.0F, 30.0F);
				}
				else
				{
					if (this.getAttackTarget() != null)
					{
						double d01 = getAttackTarget().posX - this.posX;
						double d11 = getAttackTarget().posZ - this.posZ;
						float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11);
						this.motionX = (d01 / f2 * 0.5D * 0.5D + this.motionX);
						this.motionZ = (d11 / f2 * 0.5D * 0.5D + this.motionZ);
						faceEntity(getAttackTarget(), 180.0F, 30.0F);
						if (this.posY < this.getAttackTarget().posY)
						this.motionY += (0.25D - this.motionY);
					}
					else
					{
						double d0 = this.spawnPosition.getX() + 0.5D - this.posX;
						double d1 = this.spawnPosition.getY() + 0.1D - this.posY;
						double d2 = this.spawnPosition.getZ() + 0.5D - this.posZ;
						this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
						this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
						this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
						float f = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / 3.141592653589793D) - 90.0F;
						float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
						this.moveForward = 0.5F;
						this.rotationYaw += f1;
					}
				}
			}
		}
	}
	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (stack.isEmpty() && this.hasOwner(player))
		{
			this.playHurtSound(null);
			this.performHurtAnimation();
			player.swingArm(EnumHand.MAIN_HAND);
			this.addPotionEffect(new PotionEffect(MobEffects.LUCK, 100));
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, player.getActivePotionEffect(MobEffects.NIGHT_VISION) != null ? player.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration() + 200 : 200));
			return true;
		}
		else
		{
			return false;
		}
	}
	protected boolean canTriggerWalking()
	{
		return false;
	}
	public boolean takesFallDamage()
	{
		return false;
	}
	protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_) { }
	public boolean doesEntityNotTriggerPressurePlate()
	{
		return true;
	}
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (isEntityInvulnerable(source))
		{
			return false;
		}
		if ((!this.world.isRemote) && (getIsBatHanging()))
		{
			setIsBatHanging(false);
		}
		return super.attackEntityFrom(source, amount);
	}
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.dataManager.set(HANGING, Byte.valueOf(tagCompund.getByte("BatFlags")));
	}
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setByte("BatFlags", ((Byte)this.dataManager.get(HANGING)).byteValue());
	}
	public float getEyeHeight()
	{
		return this.height / 2.0F;
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_BAT;
	}
	public boolean getCanSpawnHere()
	{
		BlockPos blockpos = new BlockPos(this.posX, getEntityBoundingBox().minY, this.posZ);
		if (blockpos.getY() >= this.world.getSeaLevel())
		{
			return false;
		}
		int i = this.world.getLightFromNeighbors(blockpos);
		int j = 4;
		if (isDateAroundHalloween(this.world.getCurrentDate()))
		{
			j = 7;
		}
		else if (this.rand.nextBoolean())
		{
			return false;
		}
		return i >= this.rand.nextInt(j) ? false : super.getCanSpawnHere();
	}
	private boolean isDateAroundHalloween(Calendar p_175569_1_)
	{
		return ((p_175569_1_.get(2) + 1 == 10) && (p_175569_1_.get(5) >= 20)) || ((p_175569_1_.get(2) + 1 == 11) && (p_175569_1_.get(5) <= 3));
	}
	
	@Override
	public EnumTier getTier()
	{
		return EnumTier.TIER1;
	}
}


