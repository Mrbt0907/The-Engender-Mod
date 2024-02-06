package net.minecraft.AgeOfMinecraft.entity.tier5;
import java.util.List;

import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityGiant extends EntityFriendlyCreature implements Massive, Armored
{
	public EntityGiant(World worldIn)
	{
		super(worldIn);
		setSize(3.0F, 12.0F);
		this.stepHeight = 3.0F;
		this.isOffensive = true;
		this.isImmuneToFire = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.2D, 48.0F, 8.0F));
		this.tasks.addTask(3, new EntityAIFriendlyAttackMelee(this, 1.2D, true));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D, 80));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.experienceValue = 250;
		this.ignoreFrustumCheck = true;
		this.experienceValue = 50;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityGiant(this.world);
	}
	protected boolean isValidLightLevel()
	{
		return true;
	}
	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 2F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 1.25F;
	}
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}

	public EnumTier getTier()
	{
		return EnumTier.TIER5;
	}

	public String getName()
	{
		if (hasCustomName())
		{
			return getCustomNameTag();
		}

			String s = EntityList.getEntityString(this);
			
			if (s == null)
			{
				s = "generic";
			}

			return TranslateUtil.translateServer("entity." + s + ".name");
		
	}

	public boolean canBeMatedWith()
	{
		return false;
	}
	protected boolean canFitPassenger(Entity passenger)
	{
		return getPassengers().size() < 3;
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
	public void updatePassenger(Entity passenger)
	{
		if (isPassenger(passenger))
		{
			int i = getPassengers().indexOf(passenger);
			float f3 = this.renderYawOffset * 3.1415927F / 180.0F;
			float f11 = MathHelper.sin(f3);
			float f4 = MathHelper.cos(f3);
			
				if (i == 2)
				passenger.setPosition(this.posX - f4 * 2.25F, this.posY + (this.isHero() ? 9D : 8.5D), this.posZ - f11 * 2.25F);
				if (i == 1)
				passenger.setPosition(this.posX + f4 * 2.25F, this.posY + (this.isHero() ? 9D : 8.5D), this.posZ + f11 * 2.25F);
				if (i == 0)
				{
					passenger.setPosition(this.posX, this.posY + (this.isHero() ? (12D * this.getFittness()) : (11.35D * this.getFittness())), this.posZ);
				}
			
		}
	}
	public void attackWithAdditionalEffects(Entity entity)
	{
		double amount = 0.6D;
		if (this.world.getDifficulty() == EnumDifficulty.EASY)
		amount *= 0.75D;
		if (this.world.getDifficulty() == EnumDifficulty.HARD)
		amount *= 1.5D;
		((EntityLivingBase)entity).knockBack(this, 1F, MathHelper.sin(this.rotationYaw * 0.017453292F), -MathHelper.cos(this.rotationYaw * 0.017453292F));
		if (!entity.isEntityAlive() && entity instanceof EntityLivingBase)
		{
			((EntityLivingBase)entity).prevRenderYawOffset = ((EntityLivingBase)entity).renderYawOffset = ((EntityLivingBase)entity).prevRotationYaw = ((EntityLivingBase)entity).rotationYaw = ((EntityLivingBase)entity).prevRotationYawHead = ((EntityLivingBase)entity).rotationYawHead = this.rotationYawHead;
			float xRatio = MathHelper.sin(this.rotationYawHead * 0.017453292F);
			float zRatio = -MathHelper.cos(this.rotationYawHead * 0.017453292F);
			entity.isAirBorne = true;
			float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
			entity.motionX /= 2.0D;
			entity.motionZ /= 2.0D;
			entity.motionX -= xRatio / (double)f * 3F;
			entity.motionZ -= zRatio / (double)f * 3F;
			entity.motionY /= 2.0D;
			entity.motionY += amount;
			if (entity instanceof EntityPlayerMP)
			((EntityPlayerMP)entity).connection.sendPacket(new SPacketEntityVelocity((EntityPlayerMP)entity));
		}
		entity.motionY += amount;
	}
	public float getEyeHeight()
	{
		return this.height * 0.87F;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(50.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(24.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}
	public double getKnockbackResistance()
	{
		return 1D;
	}
	public void travel(float strafe, float vertical, float forward)
	{
		if (isBeingRidden())
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
			this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
			this.rotationPitch = 10.0F;
			setRotation(this.rotationYaw, this.rotationPitch);
			this.rotationYawHead = (this.renderYawOffset = this.rotationYaw);
			strafe = entitylivingbase.moveStrafing;
			forward = entitylivingbase.moveForward;
			if (canPassengerSteer())
			{
				setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
				super.travel(strafe, vertical, forward);
			}
			else if ((entitylivingbase instanceof EntityPlayer))
			{
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}
			List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(2.0D, 0.0D, 2.0D));
			for (int i = 0; i < list.size(); i++)
			{
				Entity entity = (Entity)list.get(i);
				if (entity instanceof EntityLivingBase && !isOnSameTeam((EntityLivingBase)entity) && !this.world.isRemote && this.ticksExisted % 10 == 0)
				{
					attackEntityAsMob(entity);
					double d01 = (getEntityBoundingBox().minX + getEntityBoundingBox().maxX) / 2.0D;
					double d11 = (getEntityBoundingBox().minZ + getEntityBoundingBox().maxZ) / 2.0D;
					double d2 = entity.posX - d01;
					double d3 = entity.posZ - d11;
					double d4 = d2 * d2 + d3 * d3;
					entity.addVelocity(d2 / d4 * 6.0D, 0.25D, d3 / d4 * 6.0D);
					if (entity.height >= 1.0F)
					{
						addVelocity(-(d2 / d4 * 1.25D), 0.25D, -(d3 / d4 * 1.25D));
					}
				}
			}

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
		else {
			super.travel(strafe, vertical, forward);
		}
	}
	public void onLivingUpdate()
	{
		if (this.world.isRemote)
		setSize(3.0F, 12.0F);
		else
		setSize(1.5F, 12.0F);
		
		this.reachWidth = 6F;
		
		if (isHero() && !this.isWild())
		{
			if (this.world.isRemote)
			{
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				double d3 = 10.0D;
				this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX + this.rand.nextFloat() * 3F * 2.0F - 3F - d0 * d3, this.posY + this.rand.nextFloat() * 12F - d1 * d3, this.posZ + this.rand.nextFloat() * 3F * 2.0F - 3F - d2 * d3, d0, 0.10000000149011612D, d2, new int[0]);
			}
		}
		if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 128.0D) && (getSpecialAttackTimer() <= 0) && (this.onGround) && (isHero()))
		{
			performSpecialAttack();
		}
		this.stepHeight = 4.0F;
		super.onLivingUpdate();
		for (int t = 0; t < 8; ++t)
		{
			if ((this.motionX != 0.0D) && (this.motionZ != 0.0D))
			{
				int i = MathHelper.floor(this.posX);
				int j = MathHelper.floor(this.posY - 0.25D);
				int k = MathHelper.floor(this.posZ);
				IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));
				if (iblockstate.getMaterial() != Material.AIR)
				{
					this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, getEntityBoundingBox().minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, 4.0D * (this.rand.nextFloat() - 0.5D), 0.5D, (this.rand.nextFloat() - 0.5D) * 4.0D, new int[] { Block.getStateId(iblockstate) });
				}
			}
		}
	}
	public void performSpecialAttack()
	{
		this.motionY = 2D;
		playSound(ESound.golemSpecial, 10.0F, 0.75F);
	}
	public boolean takesFallDamage()
	{
		return false;
	}
	public void fall(float distance, float damageMultiplier)
	{
		if ((getSpecialAttackTimer() <= 0) && (isHero()))
		{
			setSpecialAttackTimer(500);
			playSound(ESound.golemSmash, 10.0F, 0.9F);
			createEngenderModExplosionFireless(this, this.posX, this.posY - 3.0D, this.posZ, 6.0F, false);
			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(48.0D, 3.0D, 48.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if ((list != null) && (!list.isEmpty()))
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityLivingBase entity = (EntityLivingBase)list.get(i1);
					if (entity != null)
					{
						if (!isOnSameTeam(entity))
						{
							entity.motionY += this.rand.nextInt(30) == 0 ? 30D : 3D;
							if ((entity instanceof IMob))
							{
								entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), 50.0F);
							} else {
									entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), 25.0F);
								}
								entity.isAirBorne = true;
								float f = MathHelper.sqrt(MathHelper.sin(this.rotationYaw * 0.017453292F) * MathHelper.sin(this.rotationYaw * 0.017453292F) + -MathHelper.cos(this.rotationYaw * 0.017453292F) * -MathHelper.cos(this.rotationYaw * 0.017453292F));
								entity.motionX /= 2.0D;
								entity.motionZ /= 2.0D;
								entity.motionX -= MathHelper.sin(this.rotationYaw * 0.017453292F) / f * 1.0D;
								entity.motionZ -= -MathHelper.cos(this.rotationYaw * 0.017453292F) / f * 1.0D;
							}
							if (EngenderConfig.general.useMessage && (!entity.isEntityAlive()) && (!isWild()))
							{
								getOwner().sendMessage(new TextComponentTranslation(entity.getName() + " was blown up by " + getName() + " (" + getOwner().getName() + ")", new Object[0]));
							}
						}
					}
				}
			}
		}
		protected SoundEvent getAmbientSound()
		{
			return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
		}
		protected SoundEvent getHurtSound(DamageSource source)
		{
			return SoundEvents.ENTITY_ZOMBIE_HURT;
		}
		protected SoundEvent getDeathSound()
		{
			return SoundEvents.ENTITY_ZOMBIE_DEATH;
		}
		protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)
		{
			playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 3.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F + 0.8F) / this.getFittness());
		}
		protected float getSoundVolume()
		{
			return isSneaking() ? 0.1F : 5.0F;
		}
		@Nullable
		protected ResourceLocation getLootTable()
		{
			return ELoot.ENTITIES_GIANT;
		}
		protected float getSoundPitch()
		{
			return isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F + 0.5F;
		}
		protected SoundEvent getCrushHurtSound()
		{
			return ESound.fleshHitCrushHeavy;
		}

		public boolean canBeCollidedWith()
		{
			return true;
		}

		public World getWorld()
		{
			return this.world;
		}

		public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage)
		{
			return this.attackEntityFrom(source, damage);
		}
	}