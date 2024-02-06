package net.minecraft.AgeOfMinecraft.entity.tier4;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.Ender;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityEndermite;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntityIcyEnderCreeper
extends EntityFriendlyCreature implements IJumpingMount, Armored, Ender
{
	private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
	private static final AttributeModifier attackingSpeedBoostModifier = new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.1D, 0).setSaved(false);
	private int field_184720_bx = 0;
	public EntityIcyEnderCreeper(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 2.875F);
		this.stepHeight = 1.0F;
		this.isOffensive = true;
		setPathPriority(PathNodeType.WATER, -1.0F);
		setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
		setPathPriority(PathNodeType.DANGER_CACTUS, -1.0F);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.5D, 48.0F, 12.0F));
		this.tasks.addTask(3, new EntityAIFriendlyAttackMelee(this, 1.5D, true));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.experienceValue = 10;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityIcyEnderCreeper(this.world);
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
	}
	public int timesToConvert()
	{
		return 27;
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
		return 2.5F;
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
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn)
	{
		super.setAttackTarget(entitylivingbaseIn);
		IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if (entitylivingbaseIn == null)
		{
			iattributeinstance.removeModifier(attackingSpeedBoostModifier);
		}
		else
		{
			if (!iattributeinstance.hasModifier(attackingSpeedBoostModifier))
			{
				iattributeinstance.applyModifier(attackingSpeedBoostModifier);
			}
		}
	}
	protected void entityInit()
	{
		super.entityInit();
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER4;
	}
	/**
	* Get this Entity's EnumCreatureAttribute
	*/
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return ESetup.ENDER;
	}

	public void func_184716_o()
	{
		if (this.ticksExisted >= this.field_184720_bx + 400)
		{
			this.field_184720_bx = this.ticksExisted;
			if (!isSilent())
			{
				this.world.playSound(this.posX, this.posY + getEyeHeight(), this.posZ, SoundEvents.ENTITY_ENDERMEN_STARE, getSoundCategory(), this.isSneaking() ? 1.0F : 2.5F, 1.0F, false);
			}
		}
	}
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		if (isArmsRaised() && this.world.isRemote)
		{
			func_184716_o();
		}
		super.notifyDataManagerChange(key);
	}
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
	}
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
	}
	protected float getSoundPitch()
	{
		return super.getSoundPitch();
	}

	public float getEyeHeight()
	{
		return this.height * 0.89F;
	}
	public void performSpecialAttack()
	{
		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(128.0D, 128.0D, 128.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
		if ((list != null) && (!list.isEmpty()))
		{
			for (int i1 = 0; i1 < list.size(); i1++)
			{
				EntityLivingBase entity = (EntityLivingBase)list.get(i1);
				if (entity != null)
				{
					if (!isOnSameTeam(entity))
					{
						teleportToEntity(entity);
						attackEntityAsMob(entity);
					}
				}
			}
		}
		setSpecialAttackTimer(1200);
	}
	public void onLivingUpdate()
	{
		this.setSize(0.5F, 2.875F);
		
		if ((getAttackTarget() != null) && (getAttackTarget().isEntityAlive()) && (getDistanceSq(getAttackTarget()) < 512.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			performSpecialAttack();
		}
		if (isWet() && this.hurtResistantTime <= 10)
		{
			attackEntityFrom((new DamageSource("onFire")).setDamageBypassesArmor().setDamageIsAbsolute().setDifficultyScaled(), 2F);
		}
		if (getAttackTarget() != null)
		{
			if (this.ticksExisted % 400 == 0)
			{
				playSound(SoundEvents.ENTITY_ENDERMEN_STARE, this.isSneaking() ? 1.0F : 2.5F, 1.0F);
			}
			if ((getAttackTarget().height <= 2.5F) && (getAttackTarget().isNonBoss()) && ((getAttackTarget() instanceof EntityLiving)) && !(getAttackTarget() instanceof EntityFriendlyCreature))
			{
				((EntityLiving)getAttackTarget()).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 9));
				((EntityLiving)getAttackTarget()).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60, 0));
				((EntityLiving)getAttackTarget()).getLookHelper().setLookPosition(getAttackTarget().posX + (this.rand.nextDouble() * 60.0D - 30.0D), getAttackTarget().posY + (this.rand.nextDouble() * 60.0D - 30.0D), getAttackTarget().posZ + (this.rand.nextDouble() * 60.0D - 30.0D), 180.0F, 180.0F);
				((EntityLiving)getAttackTarget()).setAttackTarget((EntityLivingBase)null);
				getAttackTarget().renderYawOffset = getAttackTarget().rotationYaw = getAttackTarget().rotationYawHead;
			}
		}
		if (this.world.isRemote)
		{
			this.world.spawnParticle(EnumParticleTypes.END_ROD, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0D, 0D, 0D, new int[0]);
			this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
			
			for (int i = 0; i < 2; ++i)
			{
				double d0 = (double)((float)this.posX - 0.5D + rand.nextFloat());
				double d1 = (double)((float)this.posY + rand.nextFloat());
				double d2 = (double)((float)this.posZ - 0.5D + rand.nextFloat());
				double d3 = (this.rand.nextDouble() - 0.5D) * 2.0D;
				double d4 = -this.rand.nextDouble();
				double d5 = (this.rand.nextDouble() - 0.5D) * 2.0D;
				d1 += rand.nextDouble() * this.height;
				world.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);
			}
		}
		if (!this.isBeingRidden() && this.getGuardBlock() == null && (getOwner() != null) && (getDistanceSq(getOwner()) > 4096.0D || !this.canEntityBeSeen(getOwner())) && (!this.world.isRemote))
		{
			setAttackTarget(null);
			getNavigator().clearPath();
			teleportTo(getOwner().posX, getOwner().posY, getOwner().posZ);
		}
		if (!this.isBeingRidden() && (getRevengeTarget() != null) && (getRNG().nextInt(20) == 0))
		{
			if ((getRevengeTarget().getDistanceSq(this) < 2.0D) && (!this.world.isRemote))
			{
				teleportRandomly();
			}
			if ((getRevengeTarget().getDistanceSq(this) > 128.0D) && (!this.world.isRemote))
			{
				teleportToEntity(getRevengeTarget());
			}
		}
		super.onLivingUpdate();
	}
	protected void updateAITasks()
	{
		if (isWet())
		{
			this.setFire(10);
			teleportRandomly();
		}
		super.updateAITasks();
	}
	protected boolean teleportRandomly()
	{
		if ((this.world.isDaytime()) || (isWet()))
		{
			playLivingSound();
		}
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 32.0D;
		double d1 = this.posY + (this.rand.nextInt(64) - 32);
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 32.0D;
		return teleportTo(d0, d1, d2);
	}
	protected boolean teleportToEntity(Entity p_70816_1_)
	{
		Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX, getEntityBoundingBox().minY + this.height / 2.0F - p_70816_1_.posY + p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
		vec3d = vec3d.normalize();
		double d0 = 16.0D;
		double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * d0;
		double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3d.y * d0;
		double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * d0;
		return teleportTo(d1, d2, d3);
	}
	protected boolean teleportTo(double x, double y, double z)
	{
		EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0.0F);
		if (MinecraftForge.EVENT_BUS.post(event)) return false;
		boolean flag = attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ()) && !this.isRiding();
		if (flag)
		{
			this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
			playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
			if (!this.world.isRemote && this.rand.nextFloat() < 0.01F)
			{
				EntityEndermite entityendermite = new EntityEndermite(this.world);
				entityendermite.setOwnerId(getOwnerId());
				entityendermite.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
				this.world.spawnEntity(entityendermite);
			}
		}
		return flag;
	}

	public boolean attemptTeleport(double x, double y, double z)
	{
		double d0 = this.posX;
		double d1 = this.posY;
		double d2 = this.posZ;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		boolean flag = false;
		BlockPos blockpos = new BlockPos(this);
		World world = this.world;
		Random random = this.getRNG();
		
		if (world.isBlockLoaded(blockpos))
		{
			boolean flag1 = false;
			
			while (!flag1 && blockpos.getY() > 0)
			{
				BlockPos blockpos1 = blockpos.down();
				IBlockState iblockstate = world.getBlockState(blockpos1);
				
				if (iblockstate.getMaterial().blocksMovement())
				{
					flag1 = true;
				}
				else
				{
					--this.posY;
					blockpos = blockpos1;
				}
			}

			if (flag1)
			{
				this.setPositionAndUpdate(this.posX, this.posY, this.posZ);
				if (this.isBeingRidden())
				this.getControllingPassenger().setPositionAndUpdate(this.posX, this.posY, this.posZ);
				
				if (world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(this.getEntityBoundingBox()))
				{
					flag = true;
				}
			}
		}

		if (!flag)
		{
			this.setPositionAndUpdate(d0, d1, d2);
			return false;
		}
		else
		{
			for (int j = 0; j < 128; ++j)
			{
				double d6 = (double)j / 127.0D;
				float f = (random.nextFloat() - 0.5F) * 0.2F;
				float f1 = (random.nextFloat() - 0.5F) * 0.2F;
				float f2 = (random.nextFloat() - 0.5F) * 0.2F;
				double d3 = d0 + (this.posX - d0) * d6 + (random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				double d4 = d1 + (this.posY - d1) * d6 + random.nextDouble() * (double)this.height;
				double d5 = d2 + (this.posZ - d2) * d6 + (random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, (double)f, (double)f1, (double)f2, new int[0]);
			}

			if (this instanceof EntityCreature)
			{
				((EntityCreature)this).getNavigator().clearPath();
			}

			return true;
		}
	}
	protected SoundEvent getAmbientSound()
	{
		return this.isArmsRaised() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : SoundEvents.ENTITY_ENDERMEN_AMBIENT;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_ENDERMEN_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_ENDERMEN_DEATH;
	}
	protected Item getDropItem()
	{
		return Items.ENDER_PEARL;
	}
	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (stack.isEmpty() && getRidingEntity() == null && !isWild() && this.isOnSameTeam(player) && !this.isChild() && !player.isSneaking() && !this.world.isRemote)
		{
			player.startRiding(this);
			return true;
		}
		else if (!stack.isEmpty() && (stack.getItem() == Items.ENDER_EYE) && (hasOwner(player) || player.isOnSameTeam(this)))
		{
			List<EntityFriendlyCreature> list = this.world.getEntitiesWithinAABB(EntityFriendlyCreature.class, getEntityBoundingBox().grow(256D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if ((list != null) && (!list.isEmpty()) && !this.isBeingRidden())
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(i1);
					if (entity != null)
					{
						if (this.isOnSameTeam(entity))
						{
							this.world.playSound((EntityPlayer)null, entity.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
							entity.changeDimension(1);
						}
					}
				}
			}
			this.world.playSound((EntityPlayer)null, this.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
			this.world.playSound((EntityPlayer)null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
			changeDimension(1);
			player.changeDimension(1);
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
			double d8 = 0.5D;
			Vec3d vec3 = getLook(1.0F);
			double dx = vec3.x * d8;
			double dz = vec3.z * d8;
			passenger.setPosition(this.posX + dx, this.posY + 0.25D, this.posZ + dz);
		}
	}
	protected float jumpPower;
	
	public void setJumpPower(int jumpPowerIn)
	{
		if (this.isBeingRidden())
		{
			if (jumpPowerIn < 0)
			{
				jumpPowerIn = 0;
			}

			if (jumpPowerIn >= 90)
			{
				this.jumpPower = 1.0F;
			}
			else
			{
				this.jumpPower = 0.4F + 0.4F * (float)jumpPowerIn / 90.0F;
			}
		}
	}

	public boolean canJump()
	{
		return true;
	}

	public void handleStartJump(int p_184775_1_)
	{
		this.playLivingSound();
	}

	public void handleStopJump()
	{
	}public void travel(float strafe, float vertical, float forward)
		{
			if (isBeingRidden())
			{
				EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
				this.prevRotationYaw = (this.rotationYaw = this.rotationYawHead = entitylivingbase.rotationYaw);
				this.rotationPitch = 0.0F;
				setRotation(this.rotationYaw, this.rotationPitch);
				this.renderYawOffset = this.rotationYaw;
				strafe = entitylivingbase.moveStrafing;
				forward = entitylivingbase.moveForward;
				if (canPassengerSteer())
				{
					setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 1.5F);
					super.travel(strafe, vertical, forward);
				}
				else if ((entitylivingbase instanceof EntityPlayer))
				{
					this.motionX = 0.0D;
					this.motionY = 0.0D;
					this.motionZ = 0.0D;
				}

				this.jumpMovementFactor = 0.02F;
				
				if (this.jumpPower > 0.0F && this.onGround)
				{
					this.motionY = (double)this.jumpPower * this.getFittness();
					
					if (this.isPotionActive(MobEffects.JUMP_BOOST))
					{
						this.motionY += (double)((float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
					}

					this.isAirBorne = true;
					
					if (forward > 0.0F)
					{
						float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
						float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
						this.motionX += (double)(-0.4F * f * this.jumpPower);
						this.motionZ += (double)(0.4F * f1 * this.jumpPower);
					}

					this.jumpPower = 0.0F;
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
		@Nullable
		protected ResourceLocation getLootTable()
		{
			return ELoot.ENTITIES_ICY_ENDER_CREEPER;
		}
		public boolean takesFallDamage()
		{
			return false;
		}
		public boolean attackEntityFrom(DamageSource source, float amount)
		{
			source.getTrueSource();
			
			this.setSitResting(false);
			
			if (this.isEntityAlive() && ((isEntityInvulnerable(source)) || ((source.getTrueSource() instanceof EntityCreeper)) || ((source instanceof EntityDamageSourceIndirect)) || (source.isExplosion()) || (source.isProjectile())))
			{
				if (this.teleportRandomly())
				{
					return true;
				}

				return false;
			}
			boolean flag = super.attackEntityFrom(source, amount);
			if (source.isUnblockable())
			teleportRandomly();
			return flag;
		}
	}