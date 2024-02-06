package net.minecraft.AgeOfMinecraft.entity.tier5;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.Elemental;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMagmaGolem extends EntityFriendlyCreature implements Armored, Elemental
{
	public EntityMagmaGolem(World worldIn)
	{
		super(worldIn);
		setSize(1.25F, 2.68F);
		this.isOffensive = true;
		this.experienceValue = 20;
		this.isImmuneToFire = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.0D, 48.0F, 8.0F));
		this.tasks.addTask(3, new EntityAIFriendlyAttackMelee(this, 1.0D, true));
		this.tasks.addTask(5, new EntityAIWander(this, 0.67D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityMagmaGolem(this.world);
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER5;
	}
	/**
	* Get this Entity's EnumCreatureAttribute
	*/
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return ESetup.CONSTRUCT;
	}
	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 4F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 1.25F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.75F;
	}

	public int getMaxSpawnedInChunk()
	{
		return 1;
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0D);
	}
	public double getKnockbackResistance()
	{
		return 1D;
	}
	public void performSpecialAttack()
	{
		this.motionY = 1.0D;
		playSound(ESound.golemSpecial, 10.0F, 1.0F);
	}
	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (stack.isEmpty() && !isWild() && this.isOnSameTeam(player) && !this.isChild() && !player.isSneaking() && !this.world.isRemote)
		{
			player.startRiding(this);
			return true;
		}
		else
		{
			return false;
		}
	}
	protected boolean canFitPassenger(Entity passenger)
	{
		return getPassengers().size() < 2;
	}
	public void updatePassenger(Entity passenger)
	{
		if (isPassenger(passenger))
		{
			int i = getPassengers().indexOf(passenger);
			float f3 = this.renderYawOffset * 3.1415927F / 180.0F;
			float f11 = MathHelper.sin(f3);
			float f4 = MathHelper.cos(f3);
			float sho = limbSwing - limbSwingAmount * (1.0F - (this.ticksExisted * 0.001F)) + 6.0F;
			float sho1 = (Math.abs(sho % 13.0F - 13.0F * 0.5F) - 13.0F * 0.25F) / (13.0F * 0.25F);
			
				if (i == 1)
				passenger.setPosition(this.posX - f4 * (0.65F + (limbSwingAmount >= 0.01D ? (sho1 * -0.2D) : 0)), this.posY + 1.375D, this.posZ - f11 * (0.65F + (limbSwingAmount >= 0.01D ? (sho1 * -0.2D) : 0)));
				if (i == 0)
				{
					passenger.setPosition(this.posX + f4 * (0.65F + (limbSwingAmount >= 0.01D ? (sho1 * 0.2D) : 0)), this.posY + 1.375D, this.posZ + f11 * (0.65F + (limbSwingAmount >= 0.01D ? (sho1 * 0.2D) : 0)));
				}
			
		}
	}
	public void travel(float strafe, float vertical, float forward)
	{
		if (isBeingRidden())
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
			this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
			this.rotationPitch = entitylivingbase.rotationPitch;
			setRotation(this.rotationYaw, this.rotationPitch);
			this.rotationYawHead = (this.renderYawOffset = this.rotationYaw);
			strafe = entitylivingbase.moveStrafing / 3;
			forward = entitylivingbase.moveForward / 3;
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
			List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(1.0D));
			for (int i = 0; i < list.size(); i++)
			{
				Entity entity = (Entity)list.get(i);
				if (entity instanceof EntityLivingBase && !isOnSameTeam((EntityLivingBase)entity) && !this.world.isRemote && this.ticksExisted % 10 == 0)
				{
					attackEntityAsMob(entity);
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
		else
		{
			super.travel(strafe, vertical, forward);
		}
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		setSize(1.25F, 2.68F);
		
		if (isWet())
		{
			playSound(SoundEvents.ENTITY_GENERIC_BURN, 1F, 1F);
			attackEntityFrom((new DamageSource("cooler")).setDamageBypassesArmor().setDamageIsAbsolute().setDifficultyScaled(), 8F);
		}
		if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 64.0D) && (getSpecialAttackTimer() <= 0) && (this.onGround) && (isHero()))
		{
			performSpecialAttack();
		}

		if ((this.motionX * this.motionX + this.motionZ * this.motionZ != 0.0D) && (this.rand.nextInt(5) == 0))
		{
			int i = MathHelper.floor(this.posX);
			int j = MathHelper.floor(this.posY - 0.20000000298023224D);
			int k = MathHelper.floor(this.posZ);
			IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));
			if (iblockstate.getMaterial() != Material.AIR)
			{
				this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, getEntityBoundingBox().minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, 4.0D * (this.rand.nextFloat() - 0.5D), 0.5D, (this.rand.nextFloat() - 0.5D) * 4.0D, new int[] { Block.getStateId(iblockstate) });
			}
		}
	}
	public boolean attackEntityAsMob(Entity entityIn)
	{
		this.attackTimer = 10;
		this.world.setEntityState(this, (byte)4);
		playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
		
		AttributeModifier irongolemrandomizer = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D839"), "Iron Golem randomizer", rand.nextDouble() * 0.5D, 1).setSaved(false);
		IAttributeInstance iattributeinstanceattack = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		if (!iattributeinstanceattack.hasModifier(irongolemrandomizer))
		iattributeinstanceattack.applyModifier(irongolemrandomizer);
		
		if (super.attackEntityAsMob(entityIn))
		{
			if (iattributeinstanceattack.hasModifier(irongolemrandomizer))
			iattributeinstanceattack.removeModifier(irongolemrandomizer);
			return true;
		}
		else
		{
			return false;
		}
	}
	public void attackWithAdditionalEffects(Entity entity)
	{
		double amount = 0.5D;
		if (this.world.getDifficulty() == EnumDifficulty.EASY)
		amount *= 0.75D;
		if (this.world.getDifficulty() == EnumDifficulty.HARD)
		amount *= 1.5D;
		if (!entity.isEntityAlive() && entity instanceof EntityLivingBase)
		{
			((EntityLivingBase)entity).prevRenderYawOffset = ((EntityLivingBase)entity).renderYawOffset = ((EntityLivingBase)entity).prevRotationYaw = ((EntityLivingBase)entity).rotationYaw = ((EntityLivingBase)entity).prevRotationYawHead = ((EntityLivingBase)entity).rotationYawHead = this.rotationYawHead;
			float xRatio = MathHelper.sin(this.rotationYawHead * 0.017453292F);
			float zRatio = -MathHelper.cos(this.rotationYawHead * 0.017453292F);
			entity.isAirBorne = true;
			float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
			entity.motionX /= 2.0D;
			entity.motionZ /= 2.0D;
			entity.motionX -= xRatio / (double)f * 2F;
			entity.motionZ -= zRatio / (double)f * 2F;
			entity.motionY /= 2.0D;
			entity.motionY += amount;
			if (entity instanceof EntityPlayerMP)
			((EntityPlayerMP)entity).connection.sendPacket(new SPacketEntityVelocity((EntityPlayerMP)entity));
		}
		entity.motionY += amount;
		entity.setFire(15);
	}
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (super.attackEntityFrom(source, amount))
		{
			if (!source.isMagicDamage() && source.getTrueSource() instanceof EntityLivingBase)
			{
				EntityLivingBase entitylivingbase = (EntityLivingBase)source.getTrueSource();
				if (!entitylivingbase.isImmuneToFire() && !source.isExplosion() && (!entitylivingbase.isOnSameTeam(this) || (this.getOwner() != null && entitylivingbase == this.getOwner())))
				{
					entitylivingbase.attackEntityFrom((new EntityDamageSource("litOnFire", this)).setIsThornsDamage().setDamageBypassesArmor().setFireDamage(), 2F);
					playSound(SoundEvents.ENTITY_GENERIC_BURN, getSoundVolume(), getSoundPitch());
					if (rand.nextInt(3) == 0)
					entitylivingbase.setFire(5 + rand.nextInt(5));
				}
			}

			return true;
		}
		else
		{
			return false;
		}
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
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 4)
		{
			this.attackTimer = 10;
			playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
		}
		else if (id == 11)
		{
			this.holdRoseTick = 100;
		}
		else if (id == 12)
		{
			this.holdRoseTick = 0;
		}
		else
		{
			super.handleStatusUpdate(id);
		}
	}
	public void setHoldingRose(boolean p_70851_1_)
	{
		this.holdRoseTick = (p_70851_1_ ? 100 : 0);
		this.world.setEntityState(this, p_70851_1_ ? (byte)11 : (byte)12);
	}
	public boolean takesFallDamage()
	{
		return false;
	}
	public void fall(float distance, float damageMultiplier)
	{
		if ((getSpecialAttackTimer() <= 0) && (isHero()))
		{
			setSpecialAttackTimer(300);
			playSound(ESound.golemSmash, 10.0F, 1.0F);
			createEngenderModExplosionFireless(this, this.posX, this.posY - 2.0D, this.posZ, 3.0F, false);
			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(24.0D, 3.0D, 24.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if ((list != null) && (!list.isEmpty()))
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityLivingBase entity = (EntityLivingBase)list.get(i1);
					if (entity != null)
					{
						if (!isOnSameTeam(entity))
						{
							entity.motionY += 1.5D;
							if ((entity instanceof IMob))
							{
								entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), 24.0F);
							} else {
									entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), 12.0F);
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
		protected SoundEvent getHurtSound(DamageSource source)
		{
			return SoundEvents.ENTITY_IRONGOLEM_HURT;
		}
		protected SoundEvent getDeathSound()
		{
			return SoundEvents.ENTITY_IRONGOLEM_DEATH;
		}
		protected void playStepSound(BlockPos pos, Block blockIn)
		{
			playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, (this.isChild() ? 1.5F : 1.0F) / this.getFittness());
		}
		@Nullable
		protected ResourceLocation getLootTable()
		{
			return ELoot.ENTITIES_MAGMA_GOLEM;
		}
		protected SoundEvent getRegularHurtSound()
		{
			return ESound.woodHit;
		}
		protected SoundEvent getPierceHurtSound()
		{
			return ESound.woodHitPierce;
		}
		protected SoundEvent getCrushHurtSound()
		{
			return ESound.woodHitCrush;
		}
	}