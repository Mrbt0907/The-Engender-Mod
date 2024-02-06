package net.minecraft.AgeOfMinecraft.entity.tier5;

import java.util.List;
import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityAbstractIllagers;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EntitySpellcasterIllager;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAttackRangedBowAlly;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityTippedArrowOther;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityIllusioner extends EntitySpellcasterIllager implements IRangedAttackMob
{
	private static final DataParameter<Integer> DISGUISE_ID = EntityDataManager.createKey(EntityIllusioner.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> DISGUSE_TIMER = EntityDataManager.createKey(EntityIllusioner.class, DataSerializers.VARINT);
	private final EntityAIAttackRangedBowAlly aiArrowAttack = new EntityAIAttackRangedBowAlly(this, 0.5D, 20, 15.0F);
	private final EntityAIFriendlyAttackMelee aiAttackOnCollide = new EntityAIFriendlyAttackMelee(this, 1.0D, true);
	public EntityIllusioner(World worldIn)
	{
		super(worldIn);
		this.experienceValue = 50;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 0.75D, 32.0F, 6.0F));
		this.tasks.addTask(2, new EntitySpellcasterIllager.AICastingSpell());
		this.tasks.addTask(3, new EntityIllusioner.AIIllusionFormSpell());
		this.tasks.addTask(3, new EntityIllusioner.AIDisguiseSpell());
		this.tasks.addTask(4, new EntityIllusioner.AIMirriorSpell());
		this.tasks.addTask(4, new EntityIllusioner.AIFearSpell());
		this.tasks.addTask(4, new EntityIllusioner.AIBlindnessSpell());
		this.tasks.addTask(4, new EntityIllusioner.AIReinforcingSpell());
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
		this.tasks.addTask(6, new EntityAIWander(this, 0.5D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		setCombatTask();
	}

	public void setCombatTask()
	{
		if ((this.world != null) && (!this.world.isRemote))
		{
			this.tasks.removeTask(this.aiAttackOnCollide);
			this.tasks.removeTask(this.aiArrowAttack);
			ItemStack itemstack = getHeldItemMainhand();
			
			if ((itemstack != null) && (itemstack.getItem() instanceof ItemBow))
			{
				this.tasks.addTask(4, this.aiArrowAttack);
			}
			else
			{
				this.tasks.addTask(4, this.aiAttackOnCollide);
			}
		}
	}

	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(DISGUISE_ID, Integer.valueOf(0));
		this.getDataManager().register(DISGUSE_TIMER, Integer.valueOf(0));
	}
	public int getDisguiseTime()
	{
		return ((Integer)this.dataManager.get(DISGUSE_TIMER)).intValue();
	}

	public void setDisguiseTime(int age)
	{
		this.dataManager.set(DISGUSE_TIMER, Integer.valueOf(age));
	}
	public int getDisguiseID()
	{
		return ((Integer)this.dataManager.get(DISGUISE_ID)).intValue();
	}

	public void setDisguiseID(int age)
	{
		this.dataManager.set(DISGUISE_ID, Integer.valueOf(age));
	}
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		
		tagCompound.setInteger("DiguiseID", this.getDisguiseID());
		tagCompound.setInteger("DiguiseTime", this.getDisguiseTime());
	}
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		
		this.setDisguiseID(tagCompund.getInteger("DiguiseID"));
		this.setDisguiseTime(tagCompund.getInteger("DiguiseTime"));
		setCombatTask();
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(32.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}

	/**
	* Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
	* when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
	*/
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
	{
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
		setCombatTask();
		return super.onInitialSpawn(difficulty, livingdata);
	}

	protected ResourceLocation getLootTable()
	{
		return LootTableList.EMPTY;
	}

	/**
	* Returns whether this Entity is on the same team as the given Entity.
	*/
	public boolean isOnSameTeam(Entity entityIn)
	{
		if (super.isOnSameTeam(entityIn))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	protected SoundEvent getAmbientSound()
	{
		if (this.getDisguiseID() > 0 && this.getDisguiseTime() > 0)
		{
			this.playSound(SoundEvents.ENTITY_ILLUSION_ILLAGER_AMBIENT, 0.05F, this.getSoundPitch());
			switch (this.getDisguiseID())
			{
				case 2:
				return SoundEvents.VINDICATION_ILLAGER_AMBIENT;
				case 3:
				return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
				default:
				return SoundEvents.ENTITY_VILLAGER_AMBIENT;
			}
		}
		else
		{
			return SoundEvents.ENTITY_ILLUSION_ILLAGER_AMBIENT;
		}
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_ILLAGER_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return SoundEvents.ENTITY_ILLUSION_ILLAGER_HURT;
	}

	protected SoundEvent getSpellSound()
	{
		return SoundEvents.ENTITY_ILLAGER_CAST_SPELL;
	}

	/**
	* Attack the specified entity using a ranged attack.
	*/
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
		
			EntityTippedArrowOther entityarrow = new EntityTippedArrowOther(this.world, this);
			double d0 = target.posX - this.posX;
			double d1 = (target.posY + target.getEyeHeight() - 0.5D) - (this.posY + (double)this.getEyeHeight() - 0.10000000149011612D);
			double d2 = target.posZ - this.posZ;
			double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
			entityarrow.shoot(d0, d1 + d3 * (getDistance(target) * 0.013D), d2, 1.4F, 1F);
			int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
			int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
			if (getRidingEntity() != null)
			{
				entityarrow.setDamage(distanceFactor * 3.0F + this.rand.nextGaussian() * 0.25D + 0.5D);
			} else {
					entityarrow.setDamage(distanceFactor * 1.5F + this.rand.nextGaussian() * 0.25D + 0.5D);
				}
				if (target instanceof EntityDragon && this.posY < target.posY - 10D)
				entityarrow.motionY += 1D;
				if (i > 0)
				{
					entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D);
				}

				if (this.isHero())
				{
					entityarrow.setDamage(entityarrow.getDamage() * 2D);
				}

				if (this.moralRaisedTimer > 200)
				{
					entityarrow.setDamage(entityarrow.getDamage() * 1.5D);
				}
				if (getRidingEntity() != null)
				{
					j += 2;
					entityarrow.setIsCritical(true);
				}
				if (j > 0)
				{
					entityarrow.setKnockbackStrength(j);
				}
				if ((EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, this) > 0))
				{
					entityarrow.setFire(100);
				}
				if ((getHeldItemOffhand() != null) && (getHeldItemOffhand().getItem() == Items.TIPPED_ARROW))
				{
					entityarrow.setPotionEffect(getHeldItemOffhand());
				}
				playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
				this.world.spawnEntity(entityarrow);
			
		}

		protected EntityArrow createArrowEntity(float p_193097_1_)
		{
			EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
			entitytippedarrow.setEnchantmentEffectsFromEntity(this, p_193097_1_);
			return entitytippedarrow;
		}

		@SideOnly(Side.CLIENT)
		public EntityAbstractIllagers.IllagerArmPose getArmPose()
		{
			if (this.isSpellcasting() && this.getDisguiseID() != 1)
			{
				return EntityAbstractIllagers.IllagerArmPose.SPELLCASTING;
			}
			else if (this.isArmsRaised() && this.getDisguiseID() != 1)
			{
				return EntityAbstractIllagers.IllagerArmPose.BOW_AND_ARROW;
			}
			else
			{
				return EntityAbstractIllagers.IllagerArmPose.CROSSED;
			}
		}
		public void onLivingUpdate()
		{
			super.onLivingUpdate();
			if (this.getDisguiseTime() > 0)
			this.setDisguiseTime(this.getDisguiseTime() - 1);
			
			if (this.getDisguiseID() != 0 && (this.getDisguiseTime() < 0 || (this.getDisguiseTime() > 0 && this.hurtResistantTime > 0)))
			{
				this.playSound(SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE, 1.0F, 1.0F);
				this.spawnExplosionParticle();
				this.setDisguiseID(0);
				this.setDisguiseTime(0);
			}
		}

		
		public class EntityAIPanicFear extends EntityAIBase
		{
			protected final EntityCreature creature;
			protected double speed;
			protected double randPosX;
			protected double randPosY;
			protected double randPosZ;
			protected int feartimer;
			
			public EntityAIPanicFear(EntityCreature creature, double speedIn)
			{
				this.creature = creature;
				this.speed = speedIn;
				this.setMutexBits(1);
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				return feartimer < 400 && this.findRandomPosition();
			}

			protected boolean findRandomPosition()
			{
				Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 5, 4);
				
				if (vec3d == null)
				{
					return false;
				}
				else
				{
					this.randPosX = vec3d.x;
					this.randPosY = vec3d.y;
					this.randPosZ = vec3d.z;
					return true;
				}
			}

			/**
			* Execute a one shot task or start executing a continuous task
			*/
			public void startExecuting()
			{
				this.creature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
			}

			/**
			* Returns whether an in-progress EntityAIBase should continue executing
			*/
			public boolean shouldContinueExecuting()
			{
				return feartimer < 400;
			}
			public void updateTask()
			{
				this.creature.setRevengeTarget(null);
				this.creature.setAttackTarget(null);
				if (!this.creature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed))
				{
					Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.creature, 5, 4);
					if (vec3d != null)
					{
						this.randPosX = vec3d.x;
						this.randPosY = vec3d.y;
						this.randPosZ = vec3d.z;
					}
				}
				++feartimer;
			}
		}

		public class AIFearSpell extends EntitySpellcasterIllager.AIUseSpell
		{
			private EntityAIPanicFear fear;
			private AIFearSpell()
			{
				super();
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				if (!super.shouldExecute())
				{
					return false;
				}
				else
				{
					return !EntityIllusioner.this.getAttackTarget().isEntityUndead() && EntityIllusioner.this.getAttackTarget().isNonBoss() && (EntityIllusioner.this.getAttackTarget() instanceof EntityCreature || (EntityIllusioner.this.getAttackTarget() instanceof EntityFriendlyCreature && !((EntityFriendlyCreature)EntityIllusioner.this.getAttackTarget()).isBoss() && ((EntityFriendlyCreature)EntityIllusioner.this.getAttackTarget()).getIntelligence() < 24));
				}
			}
			/**
			* Returns whether an in-progress EntityAIBase should continue executing
			*/
			public boolean shouldContinueExecuting()
			{
				return EntityIllusioner.this.getAttackTarget() != null && super.shouldContinueExecuting();
			}

			/**
			* Execute a one shot task or start executing a continuous task
			*/
			public void startExecuting()
			{
				super.startExecuting();
				EntityIllusioner.this.getAttackTarget().getEntityId();
			}

			protected int getCastingTime()
			{
				return 40;
			}

			protected int getCastingInterval()
			{
				return 500;
			}

			protected void castSpell()
			{
				List<EntityCreature> list = EntityIllusioner.this.world.getEntitiesWithinAABB(EntityCreature.class, EntityIllusioner.this.getAttackTarget().getEntityBoundingBox().grow(8D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
				EntityIllusioner.this.world.playBroadcastSound(1023, new BlockPos(EntityIllusioner.this.getAttackTarget()), 0);
				((EntityCreature)EntityIllusioner.this.getAttackTarget()).playSound(SoundEvents.ENTITY_WITHER_AMBIENT, 2F, 1F);
				for (EntityCreature entity: list)
				{
					fear = new EntityAIPanicFear(entity, 1.5D);
					if (!entity.tasks.taskEntries.contains(fear) && !EntityIllusioner.this.isOnSameTeam(entity))
					entity.tasks.addTask(0, fear);
				}
			}

			protected SoundEvent getSpellPrepareSound()
			{
				return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
			}

			protected EntitySpellcasterIllager.SpellType getSpellType()
			{
				return EntitySpellcasterIllager.SpellType.FEAR;
			}
		}

		public class AIBlindnessSpell extends EntitySpellcasterIllager.AIUseSpell
		{
			private int lastTargetId;
			
			private AIBlindnessSpell()
			{
				super();
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				if (!super.shouldExecute())
				{
					return false;
				}
				else if (EntityIllusioner.this.getAttackTarget().getEntityId() == this.lastTargetId)
				{
					return false;
				}
				else
				{
					return EntityIllusioner.this.getAttackTarget() instanceof EntityPlayer || (EntityIllusioner.this.getAttackTarget() instanceof EntityFriendlyCreature && EntityIllusioner.this.getAttackTarget().isNonBoss());
				}
			}
			/**
			* Returns whether an in-progress EntityAIBase should continue executing
			*/
			public boolean shouldContinueExecuting()
			{
				return EntityIllusioner.this.getAttackTarget() != null && super.shouldContinueExecuting();
			}

			/**
			* Execute a one shot task or start executing a continuous task
			*/
			public void startExecuting()
			{
				super.startExecuting();
				this.lastTargetId = EntityIllusioner.this.getAttackTarget().getEntityId();
			}

			protected int getCastingTime()
			{
				return 20;
			}

			protected int getCastingInterval()
			{
				return 180;
			}

			protected void castSpell()
			{
				EntityIllusioner.this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400));
			}

			protected SoundEvent getSpellPrepareSound()
			{
				return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
			}

			protected EntitySpellcasterIllager.SpellType getSpellType()
			{
				return EntitySpellcasterIllager.SpellType.BLINDNESS;
			}
		}

		
		public class AIMirriorSpell extends EntitySpellcasterIllager.AIUseSpell
		{
			private AIMirriorSpell()
			{
				super();
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				if (!super.shouldExecute())
				{
					return false;
				}
				else
				{
					return EntityIllusioner.this.getGhostTime() <= 0;
				}
			}

			protected int getCastingTime()
			{
				return 20;
			}

			protected int getCastingInterval()
			{
				return 340;
			}

			protected void castSpell()
			{
				EntityIllusioner.this.spawnExplosionParticle();
				EntityIllusioner.this.setGhostTime(1200);
			}

			@Nullable
			protected SoundEvent getSpellPrepareSound()
			{
				return SoundEvents.ENTITY_ILLAGER_PREPARE_MIRROR;
			}

			protected EntitySpellcasterIllager.SpellType getSpellType()
			{
				return EntitySpellcasterIllager.SpellType.DISAPPEAR;
			}
		}

		public class AIDisguiseSpell extends EntitySpellcasterIllager.AIUseSpell
		{
			private AIDisguiseSpell()
			{
				super();
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				if (EntityIllusioner.this.isWild())
				{
					return false;
				}
				else if (EntityIllusioner.this.getAttackTarget() != null)
				{
					return false;
				}
				else if (EntityIllusioner.this.isSpellcasting())
				{
					return false;
				}
				else if (EntityIllusioner.this.ticksExisted < this.spellCooldown)
				{
					return false;
				}
				else
				{
					return EntityIllusioner.this.getDisguiseTime() <= 0;
				}
			}
			/**
			* Returns whether an in-progress EntityAIBase should continue executing
			*/
			public boolean shouldContinueExecuting()
			{
				return EntityIllusioner.this.getAttackTarget() == null && super.shouldContinueExecuting();
			}

			protected int getCastWarmupTime()
			{
				return 20;
			}

			protected int getCastingTime()
			{
				return 40;
			}

			protected int getCastingInterval()
			{
				return 100;
			}

			protected void castSpell()
			{
				EntityIllusioner.this.playSound(SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE, 1.0F, 1.0F);
				EntityIllusioner.this.spawnExplosionParticle();
				EntityIllusioner.this.setGhostTime(0);
				EntityIllusioner.this.setDisguiseTime(12000);
				if (EntityIllusioner.this.world.isDaytime() && EntityIllusioner.this.world.canSeeSky(EntityIllusioner.this.getPosition()))
				EntityIllusioner.this.setDisguiseID(1);
				else if (EntityIllusioner.this.getLevel() < 50)
				EntityIllusioner.this.setDisguiseID(2);
				else
				EntityIllusioner.this.setDisguiseID(3);
			}

			@Nullable
			protected SoundEvent getSpellPrepareSound()
			{
				return SoundEvents.ENTITY_ILLAGER_PREPARE_MIRROR;
			}

			protected EntitySpellcasterIllager.SpellType getSpellType()
			{
				return EntitySpellcasterIllager.SpellType.CHANGE_SELF;
			}
		}

		public class AIReinforcingSpell extends EntitySpellcasterIllager.AIUseSpell
		{
			final Predicate<EntityFriendlyCreature> wololoSelector = new Predicate<EntityFriendlyCreature>()
			{
				public boolean apply(EntityFriendlyCreature p_apply_1_)
				{
					return !p_apply_1_.isWild();
				}
			};
			
			public AIReinforcingSpell()
			{
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				if (!super.shouldExecute())
				{
					return false;
				}
				else
				{
					List<EntityFriendlyCreature> list = EntityIllusioner.this.world.<EntityFriendlyCreature>getEntitiesWithinAABB(EntityFriendlyCreature.class, EntityIllusioner.this.getEntityBoundingBox().grow(32D), this.wololoSelector);
					
					if (list.isEmpty())
					{
						return false;
					}
					else
					{
						EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(EntityIllusioner.this.rand.nextInt(list.size()));
						if (entity.getGhostTime() > 0 || !entity.isEntityAlive() || entity == EntityIllusioner.this)
						{
							list.remove(entity);
							return false;
						}
						else
						{
							EntityIllusioner.this.setAllyTarget(entity);
							return true;
						}
					}
				}
			}

			/**
			* Returns whether an in-progress EntityAIBase should continue executing
			*/
			public boolean shouldContinueExecuting()
			{
				return EntityIllusioner.this.getAllyTarget() != null && EntityIllusioner.this.getAllyTarget().isEntityAlive() && EntityIllusioner.this.isOnSameTeam(EntityIllusioner.this.getAllyTarget()) && EntityIllusioner.this.getAllyTarget().getGhostTime() <= 0 && this.spellWarmup > 0;
			}

			/**
			* Resets the task
			*/
			public void resetTask()
			{
				super.resetTask();
				EntityIllusioner.this.setAllyTarget((EntityFriendlyCreature)null);
			}

			protected void castSpell()
			{
				EntityFriendlyCreature entitysheep = EntityIllusioner.this.getAllyTarget();
				
				if (entitysheep != null && entitysheep.isEntityAlive() && EntityIllusioner.this.isOnSameTeam(entitysheep))
				{
					if (entitysheep.getGhostTime() <= 0)
					{
						entitysheep.spawnExplosionParticle();
						entitysheep.setGhostTime(1200);
						entitysheep.hurtResistantTime = 10;
						this.resetTask();
					}
				}
			}

			protected int getCastWarmupTime()
			{
				return 20;
			}

			protected int getCastingTime()
			{
				return 40;
			}

			protected int getCastingInterval()
			{
				return 100;
			}

			protected SoundEvent getSpellPrepareSound()
			{
				return SoundEvents.ENTITY_ILLAGER_PREPARE_MIRROR;
			}

			protected SpellType getSpellType()
			{
				return SpellType.BUFFER_ILLUSIONER;
			}
		}

		public class AIIllusionFormSpell extends EntitySpellcasterIllager.AIUseSpell
		{
			final Predicate<EntityFriendlyCreature> wololoSelector = new Predicate<EntityFriendlyCreature>()
			{
				public boolean apply(EntityFriendlyCreature p_apply_1_)
				{
					return !p_apply_1_.isWild();
				}
			};
			
			public AIIllusionFormSpell()
			{
			}

			/**
			* Returns whether the EntityAIBase should begin execution.
			*/
			public boolean shouldExecute()
			{
				if (EntityIllusioner.this.getAttackTarget() != null)
				{
					return false;
				}
				else if (EntityIllusioner.this.isSpellcasting())
				{
					return false;
				}
				else if (EntityIllusioner.this.ticksExisted < this.spellCooldown)
				{
					return false;
				}
				else
				{
					List<EntityFriendlyCreature> list = EntityIllusioner.this.world.<EntityFriendlyCreature>getEntitiesWithinAABB(EntityFriendlyCreature.class, EntityIllusioner.this.getEntityBoundingBox().grow(32D), this.wololoSelector);
					
					if (list.isEmpty())
					{
						return false;
					}
					else
					{
						EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(EntityIllusioner.this.rand.nextInt(list.size()));
						if (entity.isBoss() || entity.getIllusionFormTime() > 0 || !entity.isEntityAlive() || entity == EntityIllusioner.this || entity instanceof EntityIllusioner || !EntityIllusioner.this.isOnSameTeam(entity))
						{
							list.remove(entity);
							return false;
						}
						else
						{
							EntityIllusioner.this.setAllyTarget(entity);
							return true;
						}
					}
				}
			}

			/**
			* Returns whether an in-progress EntityAIBase should continue executing
			*/
			public boolean shouldContinueExecuting()
			{
				return EntityIllusioner.this.getAllyTarget() != null && EntityIllusioner.this.getAllyTarget().isEntityAlive() && EntityIllusioner.this.isOnSameTeam(EntityIllusioner.this.getAllyTarget()) && this.spellWarmup > 0;
			}

			/**
			* Resets the task
			*/
			public void resetTask()
			{
				super.resetTask();
				EntityIllusioner.this.setAllyTarget((EntityFriendlyCreature)null);
			}

			protected void castSpell()
			{
				EntityFriendlyCreature entitysheep = EntityIllusioner.this.getAllyTarget();
				
				if (entitysheep != null && entitysheep.isEntityAlive() && EntityIllusioner.this.isOnSameTeam(entitysheep))
				{
					if (entitysheep.getIllusionFormTime() <= 0)
					{
						entitysheep.playSound(ESound.bugSpecial, 1F, 1F);
						entitysheep.spawnExplosionParticle();
						entitysheep.spawnExplosionParticle();
						entitysheep.spawnExplosionParticle();
						entitysheep.spawnExplosionParticle();
						entitysheep.spawnExplosionParticle();
						entitysheep.spawnExplosionParticle();
						entitysheep.spawnExplosionParticle();
						entitysheep.spawnExplosionParticle();
						entitysheep.spawnExplosionParticle();
						entitysheep.spawnExplosionParticle();
						entitysheep.setIllusionFormTime(12000);
						entitysheep.hurtResistantTime = 10;
						entitysheep.ticksExisted = -10;
						this.resetTask();
					}
				}
			}

			protected int getCastWarmupTime()
			{
				return 20;
			}

			protected int getCastingTime()
			{
				return 60;
			}

			protected int getCastingInterval()
			{
				return 200;
			}

			protected SoundEvent getSpellPrepareSound()
			{
				return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
			}

			protected SpellType getSpellType()
			{
				return SpellType.ILLUSION_FORM;
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