package net.minecraft.AgeOfMinecraft.entity.tier3;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAICustomLeapAttack;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityCaveSpider;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityCreeder;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySpider
extends EntityFriendlyCreature implements IJumpingMount, Light
{
	 private static final DataParameter<Boolean> SURVIVAL_TEST_SKIN = EntityDataManager.createKey(EntitySpider.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(EntitySpider.class, DataSerializers.BYTE);
	public EntitySpider(World worldIn)
	{
		super(worldIn);
		this.setSize(1.5F, 0.78F);
		this.isOffensive = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.1D, 32.0F, 6.0F));
		this.tasks.addTask(2, new EntityAICustomLeapAttack(this, 0.6F, 0.6F, 0.8F, 0.5F, 4.0D, 16.0D, 6));
		this.tasks.addTask(3, new EntityAIFriendlyAttackMelee(this, 1.1D, true));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.experienceValue = 3;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntitySpider(this.world);
	}
	public boolean isASwarmingMob()
	{
		return true;
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
		return 0.75F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.5F;
	}

	protected float getSoundPitch()
	{
		return  super.getSoundPitch();
	}
	protected PathNavigate getNewNavigator(World worldIn)
	{
		return new PathNavigateClimber(this, worldIn);
	}
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(SURVIVAL_TEST_SKIN, Boolean.valueOf(false));
		this.dataManager.register(CLIMBING, Byte.valueOf((byte)0));
	}
	public boolean isSurvivalTestSkin()
	{
		return ((Boolean)getDataManager().get(SURVIVAL_TEST_SKIN)).booleanValue();
	}
	public void setSurvivalTestSkin(boolean childZombie)
	{
		getDataManager().set(SURVIVAL_TEST_SKIN, Boolean.valueOf(childZombie));
	}
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		
		setSurvivalTestSkin(tagCompund.getBoolean("EasterEgg"));
	}
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		
		if (((Boolean)this.dataManager.get(SURVIVAL_TEST_SKIN)).booleanValue())
		{
			tagCompound.setBoolean("EasterEgg", true);
		}
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER3;
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
	public void onUpdate()
	{
		super.onUpdate();
		
		if (!this.world.isRemote)
		{
			setBesideClimbableBlock(this.collidedHorizontally);
			if ((isBeingRidden()) && (!this.onGround) && (this.ticksExisted % 10 == 0))
			{
				playSound(SoundEvents.ENTITY_SPIDER_AMBIENT, 1.0F, 1.5F);
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
				if ((list != null) && (!list.isEmpty()) && this.jumpPower >= 1F)
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityLivingBase entity1 = (EntityLivingBase)list.get(i1);
						if (entity1 != null && entity1.isEntityAlive())
						{
							this.attackEntityAsMob(entity1);
						}
					}
				}
			}
		}
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
	}
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_SPIDER_AMBIENT;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_SPIDER_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_SPIDER_DEATH;
	}
	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.1F, 1F / this.getFittness());
		
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_SPIDER;
	}
	public boolean isOnLadder()
	{
		return isBesideClimbableBlock();
	}
	public void setInWeb() { }
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ARTHROPOD;
	}
	public boolean isPotionApplicable(PotionEffect potioneffectIn)
	{
		return potioneffectIn.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(potioneffectIn);
	}
	public boolean isBesideClimbableBlock()
	{
		return (((Byte)this.dataManager.get(CLIMBING)).byteValue() & 0x1) != 0;
	}
	public void setBesideClimbableBlock(boolean climbing)
	{
		byte b0 = ((Byte)this.dataManager.get(CLIMBING)).byteValue();
		if (climbing)
		{
			b0 = (byte)(b0 | 0x1);
		}
		else
		{
			b0 = (byte)(b0 & 0xFFFFFFFE);
		}
		this.dataManager.set(CLIMBING, Byte.valueOf(b0));
	}
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		
		if (!this.world.isRemote && this.world.rand.nextInt(4) == 0)
		this.setSurvivalTestSkin(true);
		
		if (this.world.rand.nextInt(100) == 0 && this.getGrowingAge() >= 0)
		{
			EntitySkeleton entityskeleton = new EntitySkeleton(this.world);
			entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
			entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData)null);
			this.world.spawnEntity(entityskeleton);
			entityskeleton.startRiding(this);
			if (!this.isWild())
			entityskeleton.setOwnerId(this.getOwnerId());
		}
		if (livingdata == null)
		{
			livingdata = new GroupData();
			if ((this.world.rand.nextFloat() < 0.25F * difficulty.getClampedAdditionalDifficulty()))
			{
				((GroupData)livingdata).func_111104_a(this.world.rand);
			}
		}
		if ((livingdata instanceof GroupData))
		{
			Potion potion = ((GroupData)livingdata).field_188478_a;
			if (potion != null)
			{
				addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE));
			}
		}
		return livingdata;
	}
	public float getEyeHeight()
	{
		return this instanceof EntityCreeder ? this.height * 0.88F : (this.height * 0.74F);
	}
	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (player.isSneaking() && stack.isEmpty() && getRidingEntity() == null)
		{
			List<EntitySkeleton> list = this.world.getEntitiesWithinAABB(EntitySkeleton.class, getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if (getRidingEntity() == null && (list != null) && (!list.isEmpty()) && !this.isBeingRidden() && this.hasOwner(player))
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntitySkeleton entity = (EntitySkeleton)list.get(i1);
					if (entity != null)
					{
						if (isOnSameTeam(entity) && !entity.isRiding() && !this.world.isRemote)
						{
							player.swingArm(EnumHand.MAIN_HAND);
							entity.startRiding(this);
							playSound(SoundEvents.ENTITY_SPIDER_AMBIENT, 1.0F, 1.5F);
							break;
						}
					}
				}
			}
			return true;
		}else if (!player.isSneaking() && stack.isEmpty() && getRidingEntity() == null)
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
				if (passenger instanceof EntityAmbientCreature)
				{
					double d8 = 1.1D;
					Vec3d vec3 = getLook(1.0F);
					double dx = vec3.x * d8;
					double dz = vec3.z * d8;
					passenger.setPosition(this.posX + dx, this.posY + 0.25D, this.posZ + dz);
				}
				else
				{
					float f2 = this.renderYawOffset * 3.1415927F / 180.0F;
					float f19 = MathHelper.sin(f2);
					float f3 = MathHelper.cos(f2);
					
					passenger.setPosition(this.posX + f19 * 0.2F, this.posY + getMountedYOffset() + passenger.getYOffset(), this.posZ - f3 * 0.2F);
				}
			}
		}
		public double getMountedYOffset()
		{
			return this.height * 0.725D;
		}
		public boolean takesFallDamage()
		{
			return false;
		}
		public void onLivingUpdate()
		{
			super.onLivingUpdate();
			
			if (this.isOnLadder() || (this.isRiding() && this.getRidingEntity() instanceof EntityPlayer && ((EntityPlayer)this.getRidingEntity()).limbSwingAmount != 0F))
			++this.limbSwingAmount;
			
			if (this instanceof EntityCaveSpider)
			{
				this.setSize(0.8F, 0.475F);
			}
			else if (this instanceof EntityCreeder)
			{
				setSize(0.75F, 1.75F);
			}
			else
			{
				this.setSize(1.5F, 0.78F);
			}

			if ((getAttackTarget() != null) && (!getAttackTarget().canEntityBeSeen(this) || !this.onGround || this.posY < getAttackTarget().posY))
			{
				this.getMoveHelper().setMoveTo(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ, 1F);
			}
			if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 1024.0D) && (getDistanceSq(getAttackTarget()) > 64.0D) && (getSpecialAttackTimer() <= 0) && (this.onGround) && (isHero()))
			{
				faceEntity(getAttackTarget(), 180.0F, 30.0F);
				performSpecialAttack();
			}
		}
		public void performSpecialAttack()
		{
			double d01 = getAttackTarget().posX - this.posX;
			double d11 = getAttackTarget().posZ - this.posZ;
			float f21 = MathHelper.sqrt(d01 * d01 + d11 * d11);
			double hor = f21 / 16.0F * 1.3D;
			double ver = 0.9D;
			this.motionX = (d01 / f21 * hor * hor + this.motionX * hor);
			this.motionZ = (d11 / f21 * hor * hor + this.motionZ * hor);
			this.motionY = ver;
			playSound(SoundEvents.ENTITY_SPIDER_AMBIENT, 1.0F, 1.5F);
			setSpecialAttackTimer(100);
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
					playSound(SoundEvents.ENTITY_SPIDER_AMBIENT, 1.0F, 1.5F);
					this.jumpPower = 1.0F;
				}
				else
				{
					this.jumpPower = 0.4F + 0.4F * (float)jumpPowerIn / 90.0F;
				}
			}
		}

		@SideOnly(Side.CLIENT)
		public void handleStatusUpdate(byte id)
		{
			if (id == 30)
			{
				this.setSurvivalTestSkin(true);}
				else{
					super.handleStatusUpdate(id);
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
					EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
					
					if ((isBeingRidden()) && (canBeSteered()) && !(entitylivingbase instanceof EntityAmbientCreature))
					{
						this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
						this.rotationPitch = entitylivingbase.rotationPitch;
						setRotation(this.rotationYaw, this.rotationPitch);
						this.rotationYawHead = (this.renderYawOffset = this.rotationYaw);
						strafe = entitylivingbase.moveStrafing;
						forward = entitylivingbase.moveForward;
						this.jumpMovementFactor = 0.1F;
						if (canPassengerSteer())
						{
							setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
							super.travel(strafe, vertical, forward);
							setBesideClimbableBlock(this.collidedHorizontally);
						}
						else if ((entitylivingbase instanceof EntityPlayer))
						{
							this.motionX = 0.0D;
							this.motionY = 0.0D;
							this.motionZ = 0.0D;
						}
						if (this.jumpPower > 0.0F && this.onGround)
						{
							this.motionY = 0.6D * (double)this.jumpPower;
							if (isPotionActive(MobEffects.JUMP_BOOST))
							{
								this.motionY += (getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
							}
							this.isAirBorne = true;
							float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
							float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
							this.motionX = ((this instanceof EntityCaveSpider ? -2.5F : -2F) * f * this.jumpPower);
							this.motionZ = ((this instanceof EntityCaveSpider ? 2.5F : 2F) * f1 * this.jumpPower);
							this.jumpPower = 0.0F;
							ForgeHooks.onLivingJump(this);
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
						this.jumpMovementFactor = 0.02F;
						super.travel(strafe, vertical, forward);
					}
				}
				public static class GroupData implements IEntityLivingData
				{
					public Potion field_188478_a;
					public void func_111104_a(Random rand)
					{
						int i = rand.nextInt(5);
						if (i <= 1)
						{
							this.field_188478_a = MobEffects.SPEED;
						}
						else if (i <= 2)
						{
							this.field_188478_a = MobEffects.STRENGTH;
						}
						else if (i <= 3)
						{
							this.field_188478_a = MobEffects.REGENERATION;
						}
						else if (i <= 4)
						{
							this.field_188478_a = MobEffects.INVISIBILITY;
						}
					}
				}
			}

			
			