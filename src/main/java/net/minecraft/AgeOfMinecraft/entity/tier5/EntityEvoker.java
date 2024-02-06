package net.minecraft.AgeOfMinecraft.entity.tier5;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EntitySpellcasterIllager;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityPig;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityRabbit;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySlime;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityBlaze;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityLargeFireballOther;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntitySmallFireballOther;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;


public class EntityEvoker extends EntitySpellcasterIllager implements IRangedAttackMob
{
	public EntityEvoker(World worldIn)
	{
		super(worldIn);
		this.experienceValue = 50;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIFollowLeader(this, 1.2D, 32.0F, 6.0F));
		this.tasks.addTask(1, new EntitySpellcasterIllager.AICastingSpell());
		this.tasks.addTask(2, new EntityEvoker.AIConvertingSpell());
		this.tasks.addTask(2, new EntityEvoker.AIReinforcingSpell());
		this.tasks.addTask(2, new EntityEvoker.AIPolymorphSpell());
		this.tasks.addTask(2, new EntityEvoker.AIWololoSpell());
		this.tasks.addTask(2, new EntityEvoker.AISummonSpell());
		this.tasks.addTask(3, new EntityEvoker.AISummonMeteorStormSpell());
		this.tasks.addTask(3, new EntityEvoker.AIDisintigrationRaySpell());
		this.tasks.addTask(3, new EntityEvoker.AILightningBoltSpell());
		this.tasks.addTask(3, new EntityEvoker.AIPoisonSpraySpell());
		this.tasks.addTask(4, new EntityEvoker.AISmallFireballSpell());
		this.tasks.addTask(4, new EntityEvoker.AIFireballSpell());
		this.tasks.addTask(4, new EntityEvoker.AIFrostRaySpell());
		this.tasks.addTask(4, new EntityEvoker.AIMagicMissileSpell());
		this.tasks.addTask(5, new EntityEvoker.AIAttackSpell());
		this.tasks.addTask(6, new EntityAIFriendlyAttackMelee(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWander(this, 0.8D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}
	public double getKnockbackResistance()
	{
		return 1D;
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityEvoker(this.world);
	}
	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 1.5F;
	}
	/**
	* Bonus damage vs mobs that implement Flying
	*/
	public float getBonusVSFlying()
	{
		return 3F;
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

	public EnumTier getTier()
	{
		return EnumTier.TIER5;
	}
	protected float getSoundPitch()
	{
		return super.getSoundPitch();
	}

	/**
	* Get this Entity's EnumCreatureAttribute
	*/
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ILLAGER;
	}

	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_EVOCATION_ILLAGER;
	}
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_)
	{
		double d1 = 1.25D;
		Vec3d vec3d = this.getLook(1.0F);
		double d2 = target.posX - (this.posX + vec3d.x * d1);
		double d3 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (0.5D + this.posY + (double)(this.height / 2.0F));
		double d4 = target.posZ - (this.posZ + vec3d.z * d1);
		world.playEvent((EntityPlayer)null, 1016, new BlockPos(this), 0);
		EntityLargeFireballOther entitylargefireball = new EntityLargeFireballOther(world, this, d2, d3, d4);
		entitylargefireball.explosionPower = 4;
		entitylargefireball.posX = this.posX + vec3d.x * d1;
		entitylargefireball.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
		entitylargefireball.posZ = this.posZ + vec3d.z * d1;
		world.spawnEntity(entitylargefireball);
		this.swingArm(EnumHand.MAIN_HAND);
	}
	public void attackEntityWithRangedAttack2(EntityLivingBase target, float p_82196_2_)
	{
		double d1 = 1.25D;
		Vec3d vec3d = this.getLook(1.0F);
		double d2 = target.posX - (this.posX + vec3d.x * d1);
		double d3 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (0.5D + this.posY + (double)(this.height / 2.0F));
		double d4 = target.posZ - (this.posZ + vec3d.z * d1);
		world.playEvent((EntityPlayer)null, 1016, new BlockPos(this), 0);
		EntitySmallFireballOther entitylargefireball = new EntitySmallFireballOther(world, this, d2, d3, d4);
		entitylargefireball.posX = this.posX + vec3d.x * d1;
		entitylargefireball.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
		entitylargefireball.posZ = this.posZ + vec3d.z * d1;
		world.spawnEntity(entitylargefireball);
		this.swingArm(EnumHand.MAIN_HAND);
	}
	public void attackEntityWithSpray(EntityLivingBase target, float p_82196_2_)
	{
		double d = 1.25D;
		Vec3d vec3d = this.getLook(1.0F);
		EntityPoisonSpray entitysnowball = new EntityPoisonSpray(this.world, this);
		double d0 = target.posY + target.getEyeHeight() - 1.100000023841858D;
		double d1 = target.posX - (this.posX + vec3d.x * d);
		double d2 = d0 - entitysnowball.posY;
		double d3 = target.posZ - (this.posZ + vec3d.z * d);
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.1F;
		entitysnowball.shoot(d1, d2 + f, d3, 1.2F, 1F);
		this.playSound(SoundEvents.BLOCK_SLIME_STEP, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entitysnowball);
		this.swingArm(EnumHand.MAIN_HAND);
	}
	public void fireRay(Entity entity, double x, double y, double z)
	{
		if (entity != null && entity.isEntityAlive())
		{
			double d3 = x;
			double d4 = y;
			double d5 = z;
			double d6 = entity.posX - d3;
			double d7 = entity.posY - d4;
			double d8 = entity.posZ - d5;
			EntityDisintigrationRay entitywitherskull = new EntityDisintigrationRay(this.world, entity, this, d6, d7, d8);
			entitywitherskull.posY = d4;
			entitywitherskull.posX = d3;
			entitywitherskull.posZ = d5;
			entitywitherskull.accelerationY = d4;
			entitywitherskull.accelerationX = d3;
			entitywitherskull.accelerationZ = d5;
			entitywitherskull.targetEntity = entity;
			this.world.spawnEntity(entitywitherskull);
		}
	}
	public void fireCone(Entity entity, double x, double y, double z)
	{
		if (entity != null && entity.isEntityAlive())
		{
			double d3 = x;
			double d4 = y;
			double d5 = z;
			double d6 = entity.posX - d3;
			double d7 = entity.posY - d4;
			double d8 = entity.posZ - d5;
			EntityFrostRay entitywitherskull = new EntityFrostRay(this.world, entity, this, d6, d7, d8);
			entitywitherskull.posY = d4;
			entitywitherskull.posX = d3;
			entitywitherskull.posZ = d5;
			entitywitherskull.accelerationY = d4;
			entitywitherskull.accelerationX = d3;
			entitywitherskull.accelerationZ = d5;
			entitywitherskull.targetEntity = entity;
			this.world.spawnEntity(entitywitherskull);
		}
	}

	
	public void performSpecialAttack()
	{
		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(64.0D, 64.0D, 64.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
		if ((list != null) && (!list.isEmpty()))
		{
			for (int i1 = 0; i1 < list.size(); i1++)
			{
				EntityLivingBase entity = (EntityLivingBase)list.get(i1);
				if ((entity != null) && !isOnSameTeam(entity))
				{
					if (!isOnSameTeam(entity))
					{
						try
						{
							ReflectionHelper.findField(entity.getClass(), new String[] { "shieldTime" }).setInt(entity, 0);
						}
						catch (Exception e)
						{
						}
						double d1 = entity.posX + (rand.nextFloat() * 16F - 8F);
						double d2 = entity.posY + 20D + (rand.nextFloat() * 20F - 10F);
						double d3 = entity.posZ + (rand.nextFloat() * 16F - 8F);
						this.fireLightning(entity, d1, d2, d3);
						this.world.addWeatherEffect(new EntityLightningBolt(this.world, entity.posX, entity.posY, entity.posZ, true));
						
}
					}
				}
			}
			setSpecialAttackTimer(1800);
		}

		protected void updateAITasks()
		{
			super.updateAITasks();
			if (this.getConvertingTarget() != null && this.getDistanceSq(getConvertingTarget()) > 256D)
			this.getNavigator().tryMoveToEntityLiving(getConvertingTarget(), 1D);
			if (this.getAttackTarget() != null && this.getDistanceSq(getAttackTarget()) > 512D)
			this.getNavigator().tryMoveToEntityLiving(getAttackTarget(), 1.0D);
			if (this.getAllyTarget() != null && !this.getAllyTarget().isEntityAlive())
			this.setConvertingTarget((EntityFriendlyCreature)null);
			if (this.getConvertingTarget() != null && !this.getConvertingTarget().isEntityAlive())
			this.setConvertingTarget((EntityFriendlyCreature)null);
			if (this.getWololoTarget() != null && !this.getWololoTarget().isEntityAlive())
			this.setWololoTarget((EntitySheep)null);
			if (this.getAttackTarget() != null && !this.getAttackTarget().isEntityAlive())
			this.setAttackTarget((EntityFriendlyCreature)null);
		}

		public void onLivingUpdate()
		{
			super.onLivingUpdate();
			
}
			/**
			* Called to update the entity's position/logic.
			*/
			public void onUpdate()
			{
				super.onUpdate();
				if (this.getJukeboxToDanceTo() != null)
				{
					if (this.ticksExisted % 10 == 0)
					this.playSound(SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON, EntityEvoker.this.isSneaking() ? 0.1F : 1.0F, rand.nextFloat() * 2F);
					float f = this.renderYawOffset * 0.017453292F + MathHelper.cos((float)this.ticksExisted * 0.6662F) * 0.5F;
					float f1 = MathHelper.cos(f);
					float f2 = MathHelper.sin(f);
					this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (double)f1 * ((this.isChild() ? 0.3D : 0.6D) * this.getFittness()), this.posY + ((this.isChild() ? 0.9D : 1.8D) * this.getFittness()), this.posZ + (double)f2 * ((this.isChild() ? 0.3D : 0.6D) * this.getFittness()), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), new int[0]);
					this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX - (double)f1 * ((this.isChild() ? 0.3D : 0.6D) * this.getFittness()), this.posY + ((this.isChild() ? 0.9D : 1.8D) * this.getFittness()), this.posZ - (double)f2 * ((this.isChild() ? 0.3D : 0.6D) * this.getFittness()), rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), new int[0]);
				}
				if (!this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() && this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty())
				{
					this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, this.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
					this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
				}
				if (!this.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() && this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty())
				{
					this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, this.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
					this.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
				}
				if (!this.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() && this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty())
				{
					this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, this.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
					this.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
				}
				if (!this.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() && this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty())
				{
					this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, this.getItemStackFromSlot(EntityEquipmentSlot.LEGS));
					this.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
				}
			}
			@Nullable
			public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
			{
				this.setEquipmentBasedOnDifficulty(difficulty);
				this.setEnchantmentBasedOnDifficulty(difficulty);
				return super.onInitialSpawn(difficulty, livingdata);
			}

			/**
			* Gives armor or weapon for entity based on given DifficultyInstance
			*/
			protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
			{
				this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.TOTEM_OF_UNDYING));
				this.setDropChance(EntityEquipmentSlot.HEAD, 0.0F);
				this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.TOTEM_OF_UNDYING));
				this.setDropChance(EntityEquipmentSlot.CHEST, 0.0F);
				this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.TOTEM_OF_UNDYING));
				this.setDropChance(EntityEquipmentSlot.LEGS, 0.0F);
				this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.TOTEM_OF_UNDYING));
				this.setDropChance(EntityEquipmentSlot.FEET, 0.0F);
			}

			protected SoundEvent getAmbientSound()
			{
				return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
			}

			protected SoundEvent getDeathSound()
			{
				return SoundEvents.EVOCATION_ILLAGER_DEATH;
			}

			protected SoundEvent getHurtSound(DamageSource source)
			{
				return SoundEvents.ENTITY_EVOCATION_ILLAGER_HURT;
			}
			protected float applyPotionDamageCalculations(DamageSource p_70672_1_, float p_70672_2_)
			{
				p_70672_2_ = super.applyPotionDamageCalculations(p_70672_1_, p_70672_2_);
				if (p_70672_1_.getTrueSource() instanceof EntityLivingBase && this.isOnSameTeam((EntityLivingBase)p_70672_1_.getTrueSource()))
				{
					p_70672_2_ = 0.0F;
				}
				if (p_70672_1_.isMagicDamage() && p_70672_1_.getDamageType() != "antimatter")
				{
					p_70672_2_ = (float)(p_70672_2_ * 0.05D);
				}
				return p_70672_2_;
			}

			protected SoundEvent getSpellSound()
			{
				return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
			}

			class AIAttackSpell extends EntitySpellcasterIllager.AIUseSpell
			{
				private AIAttackSpell()
				{
				}

				protected int getCastingTime()
				{
					return 40;
				}

				protected int getCastingInterval()
				{
					return 100;
				}
				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
				}

				protected void castSpell()
				{
					EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
					double d0 = EntityEvoker.this.posY;
					double d1 = EntityEvoker.this.posY;
					float f = (float)MathHelper.atan2(entitylivingbase.posZ - EntityEvoker.this.posZ, entitylivingbase.posX - EntityEvoker.this.posX);
					
					if (EntityEvoker.this.getDistance(entitylivingbase) < 4D && entitylivingbase.posY <= d0 + 1D)
					{
						d0 = EntityEvoker.this.posY;
						d1 = EntityEvoker.this.posY;
						for (int i = 0; i < 5; ++i)
						{
							float f1 = f + (float)i * (float)Math.PI * 0.4F;
							this.spawnFangs(EntityEvoker.this.posX + (double)MathHelper.cos(f1) * 1D, EntityEvoker.this.posZ + (double)MathHelper.sin(f1) * 1D, d0, d1, f1, 0);
						}
						for (int i = 0; i < 10; ++i)
						{
							float f1 = f + (float)i * (float)Math.PI * 0.2F;
							this.spawnFangs(EntityEvoker.this.posX + (double)MathHelper.cos(f1) * 2D, EntityEvoker.this.posZ + (double)MathHelper.sin(f1) * 2D, d0, d1, f1, 10);
						}

						if (EntityEvoker.this.getLevel() >= 5)
						for (int i = 0; i < 15; ++i)
						{
							float f1 = f + (float)i * (float)Math.PI * 0.1F;
							this.spawnFangs(EntityEvoker.this.posX + (double)MathHelper.cos(f1) * 3D, EntityEvoker.this.posZ + (double)MathHelper.sin(f1) * 3D, d0, d1, f1, 20);
						}

						if (EntityEvoker.this.getLevel() >= 10)
						for (int k = 0; k < 20; ++k)
						{
							float f2 = f + (float)k * (float)Math.PI * 0.1F;
							this.spawnFangs(EntityEvoker.this.posX + (double)MathHelper.cos(f2) * 4D, EntityEvoker.this.posZ + (double)MathHelper.sin(f2) * 4D, d0, d1, f2, 30);
						}
						if (EntityEvoker.this.getLevel() >= 15)
						for (int k = 0; k < 25; ++k)
						{
							float f2 = f + (float)k * (float)Math.PI * 0.1F;
							this.spawnFangs(EntityEvoker.this.posX + (double)MathHelper.cos(f2) * 5D, EntityEvoker.this.posZ + (double)MathHelper.sin(f2) * 5D, d0, d1, f2, 40);
						}

						if (EntityEvoker.this.getLevel() >= 20)
						for (int k = 0; k < 30; ++k)
						{
							float f2 = f + (float)k * (float)Math.PI * 0.1F;
							this.spawnFangs(EntityEvoker.this.posX + (double)MathHelper.cos(f2) * 6D, EntityEvoker.this.posZ + (double)MathHelper.sin(f2) * 6D, d0, d1, f2, 50);
						}

						if (EntityEvoker.this.getLevel() >= 25)
						for (int k = 0; k < 35; ++k)
						{
							float f2 = f + (float)k * (float)Math.PI * 0.1F;
							this.spawnFangs(EntityEvoker.this.posX + (double)MathHelper.cos(f2) * 7D, EntityEvoker.this.posZ + (double)MathHelper.sin(f2) * 7D, d0, d1, f2, 60);
						}

						if (EntityEvoker.this.getLevel() >= 30)
						for (int k = 0; k < 40; ++k)
						{
							float f2 = f + (float)k * (float)Math.PI * 0.1F;
							this.spawnFangs(EntityEvoker.this.posX + (double)MathHelper.cos(f2) * 8D, EntityEvoker.this.posZ + (double)MathHelper.sin(f2) * 8D, d0, d1, f2, 70);
						}
					}
					else
					{
						if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive())
						{
							if (!EntityEvoker.this.world.isRemote)
							{
								float j = EntityEvoker.this.renderYawOffset * 0.017453292F;
								MathHelper.cos(j);
								MathHelper.sin(j);
								EntityInvisibleFangsProjectile entitymagicmissiles = new EntityInvisibleFangsProjectile(EntityEvoker.this.world, entitylivingbase, EntityEvoker.this, EntityEvoker.this.posX, EntityEvoker.this.posY , EntityEvoker.this.posZ );
								EntityEvoker.this.world.spawnEntity(entitymagicmissiles);
							}
						}
					}
				}

				private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_)
				{
					BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
					boolean flag = true;
					double d0 = 0.0D;
					
					if (flag && !EntityEvoker.this.world.isRemote)
					{
						EntityEvokerFangOther entityevokerfangs = new EntityEvokerFangOther(EntityEvoker.this.world, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, EntityEvoker.this);
						EntityEvoker.this.world.spawnEntity(entityevokerfangs);
					}
				}

				protected SoundEvent getSpellPrepareSound()
				{
					return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
				}

				protected SpellType getSpellType()
				{
					return SpellType.FANGS;
				}
			}
			class AIFireballSpell extends EntitySpellcasterIllager.AIUseSpell
			{
				private AIFireballSpell()
				{
				}
				public boolean shouldExecute()
				{
					if (!super.shouldExecute())
					{
						return false;
					}
					else if (EntityEvoker.this.getAttackTarget() != null && EntityEvoker.this.getDistance(EntityEvoker.this.getAttackTarget()) > 45.72D && EntityEvoker.this.getDistanceSq(EntityEvoker.this.getAttackTarget()) < 45D)
					{
						return false;
					}
					else
					{
						return EntityEvoker.this.getLevel() >= 3;
					}
				}
				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
				}

				protected int getCastingTime()
				{
					return 40;
				}

				protected int getCastingInterval()
				{
					return 160;
				}

				protected void castSpell()
				{
					EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
					if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive())
					{
						EntityEvoker.this.attackEntityWithRangedAttack(entitylivingbase, 1F);
					}
				}

				protected SoundEvent getSpellPrepareSound()
				{
					return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
				}

				protected SpellType getSpellType()
				{
					return SpellType.FIREBALL;
				}
			}
			class AISmallFireballSpell extends EntitySpellcasterIllager.AIUseSpell
			{
				private AISmallFireballSpell()
				{
				}
				public boolean shouldExecute()
				{
					if (!super.shouldExecute())
					{
						return false;
					}
					else if (EntityEvoker.this.getAttackTarget() != null && EntityEvoker.this.getDistance(EntityEvoker.this.getAttackTarget()) > 36.576D)
					{
						return false;
					}
					else
					{
						return EntityEvoker.this.getLevel() >= 1;
					}
				}
				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
				}

				protected int getCastingTime()
				{
					return 40;
				}

				protected int getCastingInterval()
				{
					return 80;
				}

				protected void castSpell()
				{
					EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
					if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive())
					{
						EntityEvoker.this.attackEntityWithRangedAttack2(entitylivingbase, 1F);
					}
				}

				protected SoundEvent getSpellPrepareSound()
				{
					return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
				}

				protected SpellType getSpellType()
				{
					return SpellType.FIREBOLT;
				}
			}
			class AIPoisonSpraySpell extends EntitySpellcasterIllager.AIUseSpell
			{
				private AIPoisonSpraySpell()
				{
				}
				public boolean shouldExecute()
				{
					if (!super.shouldExecute())
					{
						return false;
					}
					else if (EntityEvoker.this.getAttackTarget() != null && EntityEvoker.this.getDistance(EntityEvoker.this.getAttackTarget()) > 9.144D)
					{
						return false;
					}
					else
					{
						return EntityEvoker.this.getLevel() >= 2;
					}
				}
				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
				}

				protected int getCastingTime()
				{
					return 40;
				}

				protected int getCastingInterval()
				{
					return 120;
				}

				protected void castSpell()
				{
					EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
					if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive())
					{
						EntityEvoker.this.attackEntityWithSpray(entitylivingbase, 1F);
					}
				}

				protected SoundEvent getSpellPrepareSound()
				{
					return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
				}

				protected SpellType getSpellType()
				{
					return SpellType.POISON_SPRAY;
				}
			}
			class AIMagicMissileSpell extends EntitySpellcasterIllager.AIUseSpell
			{
				private AIMagicMissileSpell()
				{
				}
				public boolean shouldExecute()
				{
					if (!super.shouldExecute())
					{
						return false;
					}
					else if (EntityEvoker.this.getAttackTarget() != null && EntityEvoker.this.getDistance(EntityEvoker.this.getAttackTarget()) > 36.576D)
					{
						return false;
					}
					else
					{
						return EntityEvoker.this.getLevel() >= 5;
					}
				}
				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
				}

				protected int getCastingTime()
				{
					return 40;
				}

				protected int getCastingInterval()
				{
					return 160;
				}

				protected void castSpell()
				{
					EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
					if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive())
					{
						for (int i = 0; i < (EntityEvoker.this.isHero() ? 18 : 9); ++i)
						{
							if (!EntityEvoker.this.world.isRemote)
							{
								EntityMagicMissile entitymagicmissiles = new EntityMagicMissile(EntityEvoker.this.world, entitylivingbase, EntityEvoker.this, EntityEvoker.this.posX, EntityEvoker.this.posY + 2D, EntityEvoker.this.posZ);
								entitymagicmissiles.posY = EntityEvoker.this.posY + 2D;
								Random random = new Random();
								entitymagicmissiles.motionX += random.nextDouble() * 2.0D - 1.0D;
								entitymagicmissiles.motionY += random.nextDouble() * 2.0D;
								entitymagicmissiles.motionZ += random.nextDouble() * 2.0D - 1.0D;
								EntityEvoker.this.world.spawnEntity(entitymagicmissiles);
							}
						}
					}
				}

				protected SoundEvent getSpellPrepareSound()
				{
					return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
				}

				protected SpellType getSpellType()
				{
					return SpellType.MAGIC_MISSLE;
				}
			}
			class AIDisintigrationRaySpell extends EntitySpellcasterIllager.AIUseSpell
			{
				private AIDisintigrationRaySpell()
				{
				}
				public boolean shouldExecute()
				{
					if (!super.shouldExecute())
					{
						return false;
					}
					else if (EntityEvoker.this.getAttackTarget() != null && EntityEvoker.this.getDistance(EntityEvoker.this.getAttackTarget()) > 18.288D)
					{
						return false;
					}
					else
					{
						return EntityEvoker.this.getLevel() >= 20;
					}
				}
				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
				}

				protected int getCastingTime()
				{
					return 40;
				}

				protected int getCastingInterval()
				{
					return 360;
				}

				protected void castSpell()
				{
					EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
					if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive())
					{
						try
						{
							ReflectionHelper.findField(entitylivingbase.getClass(), new String[] { "shieldTime" }).setInt(entitylivingbase, 0);
						}
						catch (Exception e)
						{
						}

						float f = EntityEvoker.this.renderYawOffset * 0.017453292F;
						float f1 = MathHelper.cos(f);
						float f2 = MathHelper.sin(f);
						double d1 = EntityEvoker.this.posX - f2 * 0.4F;
						double d2 = EntityEvoker.this.posY + 1.25D;
						double d3 = EntityEvoker.this.posZ + f1 * 0.4F;
						EntityEvoker.this.fireRay(entitylivingbase, d1, d2, d3);
					}
				}

				protected SoundEvent getSpellPrepareSound()
				{
					return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
				}

				protected SpellType getSpellType()
				{
					return SpellType.DISINTIGRATION_RAY;
				}
			}
			class AIFrostRaySpell extends EntitySpellcasterIllager.AIUseSpell
			{
				private AIFrostRaySpell()
				{
				}
				public boolean shouldExecute()
				{
					if (!super.shouldExecute())
					{
						return false;
					}
					else if (EntityEvoker.this.getAttackTarget() != null && EntityEvoker.this.getDistance(EntityEvoker.this.getAttackTarget()) > 18.288D)
					{
						return false;
					}
					else
					{
						return EntityEvoker.this.getLevel() >= 15;
					}
				}
				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
				}

				protected int getCastingTime()
				{
					return 40;
				}

				protected int getCastingInterval()
				{
					return 200;
				}

				protected void castSpell()
				{
					EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
					if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive())
					{
						try
						{
							ReflectionHelper.findField(entitylivingbase.getClass(), new String[] { "shieldTime" }).setInt(entitylivingbase, 0);
						}
						catch (Exception e)
						{
						}

						float f = EntityEvoker.this.renderYawOffset * 0.017453292F;
						float f1 = MathHelper.cos(f);
						float f2 = MathHelper.sin(f);
						double d1 = EntityEvoker.this.posX - f2 * 0.4F;
						double d2 = EntityEvoker.this.posY + 1.25D;
						double d3 = EntityEvoker.this.posZ + f1 * 0.4F;
						EntityEvoker.this.fireCone(entitylivingbase, d1, d2, d3);
					}
				}

				protected SoundEvent getSpellPrepareSound()
				{
					return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
				}

				protected SpellType getSpellType()
				{
					return SpellType.FROST_RAY;
				}
			}
			class AILightningBoltSpell extends EntitySpellcasterIllager.AIUseSpell
			{
				private AILightningBoltSpell()
				{
				}
				public boolean shouldExecute()
				{
					if (!super.shouldExecute())
					{
						return false;
					}
					else if (EntityEvoker.this.getAttackTarget() != null && EntityEvoker.this.getDistance(EntityEvoker.this.getAttackTarget()) > ((EntityEvoker.this.isHero() && EntityEvoker.this.getSpecialAttackTimer() <= 0) ? 64D : 30.48D))
					{
						return false;
					}
					else
					{
						return EntityEvoker.this.getLevel() >= 10;
					}
				}
				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
				}

				protected int getCastingTime()
				{
					return 40;
				}

				protected int getCastingInterval()
				{
					return EntityEvoker.this.isHero() && EntityEvoker.this.getSpecialAttackTimer() <= 0 ? 1200 : 240;
				}

				protected void castSpell()
				{
					EntityLivingBase entitylivingbase = EntityEvoker.this.getAttackTarget();
					if (!entitylivingbase.world.isRemote && (entitylivingbase != null) && entitylivingbase.isEntityAlive())
					{
						try
						{
							ReflectionHelper.findField(entitylivingbase.getClass(), new String[] { "shieldTime" }).setInt(entitylivingbase, 0);
						}
						catch (Exception e)
						{
						}

						if (EntityEvoker.this.isHero() && EntityEvoker.this.getSpecialAttackTimer() <= 0)EntityEvoker.this.performSpecialAttack();
						else
						{
							float f = EntityEvoker.this.renderYawOffset * 0.017453292F;
							float f1 = MathHelper.cos(f);
							float f2 = MathHelper.sin(f);
							double d1 = EntityEvoker.this.posX - f2 * 0.4F;
							double d2 = EntityEvoker.this.posY + 1.25D;
							double d3 = EntityEvoker.this.posZ + f1 * 0.4F;
							EntityEvoker.this.fireLightning(entitylivingbase, d1, d2, d3);
						}
					}
				}

				protected SoundEvent getSpellPrepareSound()
				{
					return EntityEvoker.this.isHero() && EntityEvoker.this.getSpecialAttackTimer() <= 0 ? SoundEvents.ENTITY_LIGHTNING_THUNDER : SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
				}

				protected SpellType getSpellType()
				{
					return SpellType.LIGHTNING_BOLT;
				}
			}
			class AISummonSpell extends EntitySpellcasterIllager.AIUseSpell
			{
				private AISummonSpell()
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
						int count = 0;
						List<EntityFriendlyCreature> list = EntityEvoker.this.world.<EntityFriendlyCreature>getEntitiesWithinAABB(EntityFriendlyCreature.class, EntityEvoker.this.getEntityBoundingBox().grow(32D), EntitySelectors.IS_ALIVE);
						
						if (!list.isEmpty())
						{
							EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(EntityEvoker.this.rand.nextInt(list.size()));
							if (!entity.hasLimitedLife())
							{
								list.remove(entity);
							}
							else
							{
								++count;
								if (count > 10)
								return false;
							}
						}
						return true;
					}
				}
				/**
				* Returns whether an in-progress EntityAIBase should continue executing
				*/
				public boolean shouldContinueExecuting()
				{
					return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
				}

				protected int getCastingTime()
				{
					return 60;
				}

				protected int getCastingInterval()
				{
					return 400;
				}

				protected void castSpell()
				{
					switch (EntityEvoker.this.rand.nextInt(10))
					{
						case 1:
						{
							if (EntityEvoker.this.getLevel() >= 10)
							{
								for (int i = 0; i < (4 + EntityEvoker.this.rand.nextInt(6)) * (EntityEvoker.this.isHero() ? 2 : 1); ++i)
								{
									BlockPos blockpos = (new BlockPos(EntityEvoker.this)).add(-3 + EntityEvoker.this.rand.nextInt(6), EntityEvoker.this.rand.nextInt(4), -3 + EntityEvoker.this.rand.nextInt(6));
									EntityZombie entityvex = new EntityZombie(EntityEvoker.this.world);
									entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
									entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
									if (!EntityEvoker.this.isWild())
									entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
									entityvex.setLimitedLife((int) (EntityEvoker.this.getIntelligence() * (EntityEvoker.this.getLevel() <= 10 ? 10 : EntityEvoker.this.getLevel())));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4000));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 4000));
									EntityEvoker.this.world.spawnEntity(entityvex);
								}
								break;
							}
						}
						case 2:
						{
							if (EntityEvoker.this.getLevel() >= 20)
							{
								for (int i = 0; i < (2 + EntityEvoker.this.rand.nextInt(6)) * (EntityEvoker.this.isHero() ? 2 : 1); ++i)
								{
									BlockPos blockpos = (new BlockPos(EntityEvoker.this)).add(-3 + EntityEvoker.this.rand.nextInt(6), EntityEvoker.this.rand.nextInt(4), -3 + EntityEvoker.this.rand.nextInt(6));
									EntitySkeleton entityvex = new EntitySkeleton(EntityEvoker.this.world);
									entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
									entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
									if (!EntityEvoker.this.isWild())
									entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
									entityvex.setLimitedLife((int) (EntityEvoker.this.getIntelligence() * (EntityEvoker.this.getLevel() <= 10 ? 10 : EntityEvoker.this.getLevel())));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4000));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 4000));
									EntityEvoker.this.world.spawnEntity(entityvex);
								}
								break;
							}
						}
						case 3:
						{
							if (EntityEvoker.this.getLevel() >= 50)
							{
								for (int i = 0; i < (2 + EntityEvoker.this.rand.nextInt(4)) * (EntityEvoker.this.isHero() ? 2 : 1); ++i)
								{
									BlockPos blockpos = (new BlockPos(EntityEvoker.this)).add(-3 + EntityEvoker.this.rand.nextInt(6), EntityEvoker.this.rand.nextInt(4), -3 + EntityEvoker.this.rand.nextInt(6));
									EntityBlaze entityvex = new EntityBlaze(EntityEvoker.this.world);
									entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
									entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
									if (!EntityEvoker.this.isWild())
									entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
									entityvex.setLimitedLife((int) (EntityEvoker.this.getIntelligence() * (EntityEvoker.this.getLevel() <= 10 ? 10 : EntityEvoker.this.getLevel())));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4000));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 4000));
									EntityEvoker.this.world.spawnEntity(entityvex);
								}
								break;
							}
						}
						case 4:
						{
							if (EntityEvoker.this.getLevel() >= 100)
							{
								for (int i = 0; i < (2 + EntityEvoker.this.rand.nextInt(2)) * (EntityEvoker.this.isHero() ? 2 : 1); ++i)
								{
									BlockPos blockpos = (new BlockPos(EntityEvoker.this)).add(-3 + EntityEvoker.this.rand.nextInt(6), EntityEvoker.this.rand.nextInt(4), -3 + EntityEvoker.this.rand.nextInt(6));
									EntityEnderman entityvex = new EntityEnderman(EntityEvoker.this.world);
									entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
									entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
									if (!EntityEvoker.this.isWild())
									entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
									entityvex.setLimitedLife((int) (EntityEvoker.this.getIntelligence() * (EntityEvoker.this.getLevel() <= 10 ? 10 : EntityEvoker.this.getLevel())));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4000));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 4000));
									EntityEvoker.this.world.spawnEntity(entityvex);
								}
								break;
							}
						}
						case 5:
						{
							if (EntityEvoker.this.getLevel() >= 150)
							{
								for (int i = 0; i < 2 * (EntityEvoker.this.isHero() ? 2 : 1); ++i)
								{
									BlockPos blockpos = (new BlockPos(EntityEvoker.this)).add(-3 + EntityEvoker.this.rand.nextInt(6), EntityEvoker.this.rand.nextInt(4), -3 + EntityEvoker.this.rand.nextInt(6));
									EntityIceGolem entityvex = new EntityIceGolem(EntityEvoker.this.world);
									entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
									entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
									if (!EntityEvoker.this.isWild())
									entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
									entityvex.setLimitedLife((int) (EntityEvoker.this.getIntelligence() * (EntityEvoker.this.getLevel() <= 10 ? 10 : EntityEvoker.this.getLevel())));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4000));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 4000));
									EntityEvoker.this.world.spawnEntity(entityvex);
								}
								break;
							}
						}
						case 6:
						{
							if (EntityEvoker.this.getLevel() >= 200)
							{
								for (int i = 0; i < 2 * (EntityEvoker.this.isHero() ? 2 : 1); ++i)
								{
									BlockPos blockpos = (new BlockPos(EntityEvoker.this)).add(-3 + EntityEvoker.this.rand.nextInt(6), EntityEvoker.this.rand.nextInt(4), -3 + EntityEvoker.this.rand.nextInt(6));
									EntityMagmaGolem entityvex = new EntityMagmaGolem(EntityEvoker.this.world);
									entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
									entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
									if (!EntityEvoker.this.isWild())
									entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
									entityvex.setLimitedLife((int) (EntityEvoker.this.getIntelligence() * (EntityEvoker.this.getLevel() <= 10 ? 10 : EntityEvoker.this.getLevel())));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4000));
									entityvex.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 4000));
									EntityEvoker.this.world.spawnEntity(entityvex);
								}
								break;
							}
						}
						case 7:
						{
							if (EntityEvoker.this.getLevel() >= 300)
							{
								BlockPos blockpos = (new BlockPos(EntityEvoker.this)).add(-3 + EntityEvoker.this.rand.nextInt(6), EntityEvoker.this.rand.nextInt(4), -3 + EntityEvoker.this.rand.nextInt(6));
								EntityWither entityvex = new EntityWither(EntityEvoker.this.world);
								entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
								entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
								if (!EntityEvoker.this.isWild())
								entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
								entityvex.setLimitedLife((int) (EntityEvoker.this.getIntelligence() * (EntityEvoker.this.getLevel() <= 10 ? 10 : EntityEvoker.this.getLevel())));
								entityvex.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4000));
								entityvex.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 4000));
								EntityEvoker.this.world.spawnEntity(entityvex);
								break;
							}
						}
						default:
						{
							for (int i = 0; i < (4 + EntityEvoker.this.rand.nextInt(8)) * (EntityEvoker.this.isHero() ? 2 : 1); ++i)
							{
								BlockPos blockpos = (new BlockPos(EntityEvoker.this)).add(-3 + EntityEvoker.this.rand.nextInt(6), EntityEvoker.this.rand.nextInt(4), -3 + EntityEvoker.this.rand.nextInt(6));
								EntityVex entityvex = new EntityVex(EntityEvoker.this.world);
								entityvex.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
								entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
								if (!EntityEvoker.this.isWild())
								entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
								entityvex.setBoundOrigin(blockpos);
								entityvex.setLimitedLife((int) (EntityEvoker.this.getIntelligence() * (EntityEvoker.this.getLevel() <= 10 ? 10 : EntityEvoker.this.getLevel())));
								entityvex.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 4000));
								entityvex.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 4000));
								EntityEvoker.this.world.spawnEntity(entityvex);
							}
						}
					}}
						
						protected SoundEvent getSpellPrepareSound()
						{
							return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
						}

						protected SpellType getSpellType()
						{
							return SpellType.SUMMON_VEX;
						}
					}
					class AISummonMeteorStormSpell extends EntitySpellcasterIllager.AIUseSpell
					{
						private AISummonMeteorStormSpell()
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
								return EntityEvoker.this.getLevel() >= 50;
							}
						}
						/**
						* Returns whether an in-progress EntityAIBase should continue executing
						*/
						public boolean shouldContinueExecuting()
						{
							return EntityEvoker.this.getAttackTarget() != null && super.shouldContinueExecuting();
						}

						protected int getCastingTime()
						{
							return 40;
						}

						protected int getCastingInterval()
						{
							return 1200;
						}

						protected void castSpell()
						{
							List<Entity> list = EntityEvoker.this.world.getEntitiesWithinAABBExcludingEntity(EntityEvoker.this, EntityEvoker.this.getEntityBoundingBox().grow(64D));
							if (list != null && !list.isEmpty())
							{
								for (int i = 0; i < list.size(); i++)
								{
									Entity entity = (Entity)list.get(i);
									if (entity != null && entity instanceof EntityLivingBase && !EntityEvoker.this.isOnSameTeam((EntityLivingBase)entity))
									{
										double d1 = (entity.posX + (rand.nextDouble() * 50D - 25D));
										double d2 = (entity.posY + 100D + (rand.nextDouble() * 50D - 25D));
										double d3 = (entity.posZ + (rand.nextDouble() * 50D - 25D));
										double d4 = entity.posX - d1;
										double d5 = entity.posY - d2;
										double d6 = entity.posZ - d3;
										EntityLargeFireballOther entitylargefireball = new EntityLargeFireballOther(world, EntityEvoker.this, d4, d5, d6);
										world.playEvent((EntityPlayer)null, 1016, new BlockPos(entitylargefireball), 0);
										entitylargefireball.explosionPower = 8;
										entitylargefireball.posX = d1;
										entitylargefireball.posY = d2;
										entitylargefireball.posZ = d3;
										if (!EntityEvoker.this.world.isRemote)
										world.spawnEntity(entitylargefireball);
									}
								}
							}
						}

						protected SoundEvent getSpellPrepareSound()
						{
							return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
						}

						protected SpellType getSpellType()
						{
							return SpellType.METEOR_STORM;
						}
					}

					
					public class AIWololoSpell extends EntitySpellcasterIllager.AIUseSpell
					{
						final Predicate<EntitySheep> wololoSelector = new Predicate<EntitySheep>()
						{
							public boolean apply(EntitySheep p_apply_1_)
							{
								return EntityEvoker.this.getOwner() != null ? p_apply_1_.getFleeceColor() == EnumDyeColor.RED : p_apply_1_.getFleeceColor() == EnumDyeColor.BLUE;
							}
						};
						
						public AIWololoSpell()
						{
						}

						/**
						* Returns whether the EntityAIBase should begin execution.
						*/
						public boolean shouldExecute()
						{
							if (EntityEvoker.this.getAttackTarget() != null)
							{
								return false;
							}
							else if (EntityEvoker.this.isSpellcasting())
							{
								return false;
							}
							else if (EntityEvoker.this.ticksExisted < this.spellCooldown)
							{
								return false;
							}
							else if (!EntityEvoker.this.world.getGameRules().getBoolean("mobGriefing"))
							{
								return false;
							}
							else
							{
								List<EntitySheep> list = EntityEvoker.this.world.<EntitySheep>getEntitiesWithinAABB(EntitySheep.class, EntityEvoker.this.getEntityBoundingBox().grow(16.0D, 4.0D, 16.0D), this.wololoSelector);
								
								if (list.isEmpty())
								{
									return false;
								}
								else
								{
									EntityEvoker.this.setWololoTarget((EntitySheep)list.get(EntityEvoker.this.rand.nextInt(list.size())));
									return true;
								}
							}
						}

						/**
						* Returns whether an in-progress EntityAIBase should continue executing
						*/
						public boolean shouldContinueExecuting()
						{
							return EntityEvoker.this.getWololoTarget() != null && this.spellWarmup > 0;
						}

						/**
						* Resets the task
						*/
						public void resetTask()
						{
							super.resetTask();
							EntityEvoker.this.setWololoTarget((EntitySheep)null);
						}

						protected void castSpell()
						{
							EntitySheep entitysheep = EntityEvoker.this.getWololoTarget();
							
							if (entitysheep != null && entitysheep.isEntityAlive())
							{
								EntityEvoker.this.playSound(ESound.converted, 1.0F, 1.0F);
								if (EntityEvoker.this.isWild())
								entitysheep.setFleeceColor(EnumDyeColor.RED);
								else
								entitysheep.setFleeceColor(EnumDyeColor.BLUE);
							}
						}

						protected int getCastWarmupTime()
						{
							return 40;
						}

						protected int getCastingTime()
						{
							return 40;
						}

						protected int getCastingInterval()
						{
							return 40;
						}

						protected SoundEvent getSpellPrepareSound()
						{
							return SoundEvents.EVOCATION_ILLAGER_PREPARE_WOLOLO;
						}

						protected SpellType getSpellType()
						{
							return SpellType.WOLOLO;
						}
					}

					public class AIConvertingSpell extends EntitySpellcasterIllager.AIUseSpell
					{
						final Predicate<EntityFriendlyCreature> wololoSelector = new Predicate<EntityFriendlyCreature>()
						{
							public boolean apply(EntityFriendlyCreature p_apply_1_)
							{
								return p_apply_1_.isWild() && p_apply_1_.isEntityAlive() && !p_apply_1_.isBoss() && p_apply_1_.getTier() != EnumTier.TIER6;
							}
						};
						
						public AIConvertingSpell()
						{
						}

						/**
						* Returns whether the EntityAIBase should begin execution.
						*/
						public boolean shouldExecute()
						{
							if (EntityEvoker.this.isWild())
							{
								return false;
							}
							else if (EntityEvoker.this.isSpellcasting())
							{
								return false;
							}
							else if (EntityEvoker.this.ticksExisted < this.spellCooldown)
							{
								return false;
							}
							else
							{
								List<EntityFriendlyCreature> list = EntityEvoker.this.world.<EntityFriendlyCreature>getEntitiesWithinAABB(EntityFriendlyCreature.class, EntityEvoker.this.getEntityBoundingBox().grow(32D), this.wololoSelector);
								
								if (list.isEmpty())
								{
									return false;
								}
								else
								{
									EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(EntityEvoker.this.rand.nextInt(list.size()));
									if (EntityEvoker.this.isOnSameTeam(entity) || entity.isBoss() || entity.isHero() || entity.hasLimitedLife())
									{
										list.remove(entity);
										return false;
									}
									else
									{
										EntityEvoker.this.setConvertingTarget(entity);
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
							return EntityEvoker.this.getConvertingTarget() != null && EntityEvoker.this.getConvertingTarget().isEntityAlive() && EntityEvoker.this.getConvertingTarget().isWild() && this.spellWarmup > 0;
						}

						/**
						* Resets the task
						*/
						public void resetTask()
						{
							super.resetTask();
							if (EntityEvoker.this.getConvertingTarget() != null && !EntityEvoker.this.getConvertingTarget().isWild())
							EntityEvoker.this.setConvertingTarget((EntityFriendlyCreature)null);
						}

						protected void castSpell()
						{
							EntityFriendlyCreature entitysheep = EntityEvoker.this.getConvertingTarget();
							
							if (entitysheep != null && entitysheep.isEntityAlive())
							{
								entitysheep.spawnExplosionParticle();
								entitysheep.getNavigator().tryMoveToEntityLiving(EntityEvoker.this, 1.2D);
								entitysheep.incrementConversion((EntityPlayer)EntityEvoker.this.getOwner());
								for (int i1 = 0; i1 < EntityEvoker.this.getLevel() / 10 + 1; i1++)
								entitysheep.incrementConversion((EntityPlayer)EntityEvoker.this.getOwner());
							}
						}

						protected int getCastWarmupTime()
						{
							return 10;
						}

						protected int getCastingTime()
						{
							return 20;
						}

						protected int getCastingInterval()
						{
							return 10;
						}

						protected SoundEvent getSpellPrepareSound()
						{
							return SoundEvents.EVOCATION_ILLAGER_PREPARE_WOLOLO;
						}

						protected SpellType getSpellType()
						{
							return SpellType.CONVERT;
						}
					}

					public class AIReinforcingSpell extends EntitySpellcasterIllager.AIUseSpell
					{
						final Predicate<EntityFriendlyCreature> wololoSelector = new Predicate<EntityFriendlyCreature>()
						{
							public boolean apply(EntityFriendlyCreature p_apply_1_)
							{
								return !p_apply_1_.isWild() && p_apply_1_.isEntityAlive();
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
							if (EntityEvoker.this.isWild())
							{
								return false;
							}
							else if (EntityEvoker.this.isSpellcasting())
							{
								return false;
							}
							else if (EntityEvoker.this.ticksExisted < this.spellCooldown)
							{
								return false;
							}
							else
							{
								List<EntityFriendlyCreature> list = EntityEvoker.this.world.<EntityFriendlyCreature>getEntitiesWithinAABB(EntityFriendlyCreature.class, EntityEvoker.this.getEntityBoundingBox().grow(32D), this.wololoSelector);
								
								if (list.isEmpty())
								{
									return false;
								}
								else
								{
									EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(EntityEvoker.this.rand.nextInt(list.size()));
									if ((!entity.isPotionActive(MobEffects.WEAKNESS) && !entity.isPotionActive(MobEffects.SLOWNESS) && !entity.isPotionActive(MobEffects.NAUSEA) && !entity.isPotionActive(MobEffects.LEVITATION) && !entity.isPotionActive(MobEffects.BLINDNESS) && !entity.isPotionActive(MobEffects.POISON) && !entity.isPotionActive(MobEffects.WITHER) && !entity.isBurning()) || entity.hasLimitedLife())
									{
										list.remove(entity);
										return false;
									}
									else
									{
										EntityEvoker.this.setAllyTarget(entity);
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
							return EntityEvoker.this.getAllyTarget() != null && EntityEvoker.this.getAllyTarget().isEntityAlive() && EntityEvoker.this.isOnSameTeam(EntityEvoker.this.getAllyTarget()) && (EntityEvoker.this.getAllyTarget().isPotionActive(MobEffects.NAUSEA) || EntityEvoker.this.getAllyTarget().isPotionActive(MobEffects.LEVITATION) || EntityEvoker.this.getAllyTarget().isPotionActive(MobEffects.WEAKNESS) || EntityEvoker.this.getAllyTarget().isPotionActive(MobEffects.BLINDNESS) || EntityEvoker.this.getAllyTarget().isPotionActive(MobEffects.SLOWNESS) || EntityEvoker.this.getAllyTarget().isPotionActive(MobEffects.WITHER) || EntityEvoker.this.getAllyTarget().isPotionActive(MobEffects.POISON) || EntityEvoker.this.getAllyTarget().isBurning()) && this.spellWarmup > 0;
						}

						/**
						* Resets the task
						*/
						public void resetTask()
						{
							super.resetTask();
							EntityEvoker.this.setAllyTarget((EntityFriendlyCreature)null);
						}

						protected void castSpell()
						{
							EntityFriendlyCreature entitysheep = EntityEvoker.this.getAllyTarget();
							
							if (entitysheep != null && entitysheep.isEntityAlive() && EntityEvoker.this.isOnSameTeam(entitysheep))
							{
								if (entitysheep.isBurning() || entitysheep.isPotionActive(MobEffects.LEVITATION) || entitysheep.isPotionActive(MobEffects.WEAKNESS) || entitysheep.isPotionActive(MobEffects.BLINDNESS) || entitysheep.isPotionActive(MobEffects.LEVITATION) || entitysheep.isPotionActive(MobEffects.NAUSEA) || entitysheep.isPotionActive(MobEffects.SLOWNESS) || entitysheep.isPotionActive(MobEffects.POISON) || entitysheep.isPotionActive(MobEffects.WITHER))
								{
									if (entitysheep instanceof net.minecraft.AgeOfMinecraft.entity.tier1.EntitySheep)
									entitysheep.setCustomNameTag("jeb_");
									entitysheep.spawnExplosionParticle();
									entitysheep.extinguish();
									entitysheep.removeActivePotionEffect(MobEffects.POISON);
									entitysheep.removeActivePotionEffect(MobEffects.WITHER);
									entitysheep.removeActivePotionEffect(MobEffects.SLOWNESS);
									entitysheep.removeActivePotionEffect(MobEffects.WEAKNESS);
									entitysheep.removeActivePotionEffect(MobEffects.BLINDNESS);
									entitysheep.removeActivePotionEffect(MobEffects.NAUSEA);
									entitysheep.removeActivePotionEffect(MobEffects.LEVITATION);
									entitysheep.removeActivePotionEffect(MobEffects.HUNGER);
									entitysheep.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1000 * EntityEvoker.this.getLevel()));
									entitysheep.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 1000 * EntityEvoker.this.getLevel()));
									EntityEvoker.this.getOwner().extinguish();
									EntityEvoker.this.getOwner().removeActivePotionEffect(MobEffects.POISON);
									EntityEvoker.this.getOwner().removeActivePotionEffect(MobEffects.WITHER);
									EntityEvoker.this.getOwner().removeActivePotionEffect(MobEffects.SLOWNESS);
									EntityEvoker.this.getOwner().removeActivePotionEffect(MobEffects.WEAKNESS);
									EntityEvoker.this.getOwner().removeActivePotionEffect(MobEffects.NAUSEA);
									EntityEvoker.this.getOwner().removeActivePotionEffect(MobEffects.BLINDNESS);
									EntityEvoker.this.getOwner().removeActivePotionEffect(MobEffects.LEVITATION);
									EntityEvoker.this.getOwner().removeActivePotionEffect(MobEffects.HUNGER);
									EntityEvoker.this.getOwner().addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 1000 * EntityEvoker.this.getLevel()));
									EntityEvoker.this.getOwner().addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
									EntityEvoker.this.getOwner().addPotionEffect(new PotionEffect(MobEffects.SATURATION, 20));
									EntityEvoker.this.getOwner().addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 1000 * EntityEvoker.this.getLevel()));
									if ((entitysheep instanceof EntityPig && !((EntityPig)entitysheep).getSaddled()) || (entitysheep instanceof EntityCreeper && !((EntityCreeper)entitysheep).getPowered()))
									{
										entitysheep.onStruckByLightning((EntityLightningBolt)null);
										entitysheep.world.addWeatherEffect(new EntityLightningBolt(entitysheep.world, entitysheep.posX - 0.5D, entitysheep.posY, entitysheep.posZ - 0.5D, true));
									}
									if ((entitysheep instanceof EntityRabbit && ((EntityRabbit)entitysheep).getRabbitType() != 99))
									{
										((EntityRabbit)entitysheep).setRabbitType(99);
										entitysheep.ticksExisted = 1;
										entitysheep.world.addWeatherEffect(new EntityLightningBolt(entitysheep.world, entitysheep.posX - 0.5D, entitysheep.posY, entitysheep.posZ - 0.5D, true));
									}
									if ((entitysheep instanceof EntitySlime && ((EntitySlime)entitysheep).getSlimeSize() <= 1))
									{
										((EntitySlime)entitysheep).setSlimeSize(rand.nextInt(4) == 0 ? 4 : 2);
										entitysheep.ticksExisted = 1;
										entitysheep.playSound(SoundEvents.ENTITY_GENERIC_SMALL_FALL, 2F, 1F);
										entitysheep.world.addWeatherEffect(new EntityLightningBolt(entitysheep.world, entitysheep.posX - 0.5D, entitysheep.posY, entitysheep.posZ - 0.5D, true));
									}
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
							return 0;
						}

						protected SoundEvent getSpellPrepareSound()
						{
							return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
						}

						protected SpellType getSpellType()
						{
							return SpellType.BUFFER_EVOKER;
						}
					}

					public class AIPolymorphSpell extends EntitySpellcasterIllager.AIUseSpell
					{
						private AIPolymorphSpell()
						{
							super();
						}
						public boolean shouldExecute()
						{
							if (!super.shouldExecute())
							{
								return false;
							}
							else
							{
								return EntityEvoker.this.getPolymorphTime() <= 0 && EntityEvoker.this.getLevel() >= 50;
							}
						}

						protected int getCastingTime()
						{
							return 60;
						}

						protected int getCastingInterval()
						{
							return 2400;
						}

						protected void castSpell()
						{
							if (EntityEvoker.this.getLevel() >= 300)
							{
								EntityEnderDragon entityvex = new EntityEnderDragon(EntityEvoker.this.world);
								entityvex.copyLocationAndAnglesFrom(EntityEvoker.this);
								entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(EntityEvoker.this.getPosition()), (IEntityLivingData)null);
								entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
								entityvex.setIsHero(EntityEvoker.this.isHero());
								entityvex.setLastChance(EntityEvoker.this.hasLastChance());
								entityvex.setLevel(EntityEvoker.this.getLevel());
								entityvex.setGrowingAge(EntityEvoker.this.getGrowingAge());
								entityvex.playSound(ESound.bugSpecial, 10F, 0.5F);
								entityvex.playSound(ESound.blast, 10F, 1F);
								entityvex.spawnExplosionParticle();
								entityvex.setPolymorphTime(getCastingInterval());
								entityvex.setCustomNameTag(EntityEvoker.this.getName());
								entityvex.renderYawOffset = entityvex.rotationYaw = entityvex.rotationYawHead + 180F;
								NBTTagCompound tag = EntityEvoker.this.serializeNBT();
								entityvex.polymorpherData = tag;
								EntityEvoker.this.world.spawnEntity(entityvex);
								EntityEvoker.this.world.removeEntity(EntityEvoker.this);
							}
							else if (EntityEvoker.this.getLevel() < 300 && EntityEvoker.this.getLevel() >= 200)
							{
								EntityWither entityvex = new EntityWither(EntityEvoker.this.world);
								entityvex.copyLocationAndAnglesFrom(EntityEvoker.this);
								entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(EntityEvoker.this.getPosition()), (IEntityLivingData)null);
								entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
								entityvex.setIsHero(EntityEvoker.this.isHero());
								entityvex.setLastChance(EntityEvoker.this.hasLastChance());
								entityvex.setLevel(EntityEvoker.this.getLevel());
								entityvex.setGrowingAge(EntityEvoker.this.getGrowingAge());
								entityvex.playSound(ESound.bugSpecial, 10F, 0.5F);
								entityvex.playSound(ESound.blast, 10F, 1F);
								entityvex.spawnExplosionParticle();
								entityvex.setPolymorphTime(getCastingInterval());
								entityvex.setCustomNameTag(EntityEvoker.this.getName());
								NBTTagCompound tag = EntityEvoker.this.serializeNBT();
								entityvex.polymorpherData = tag;
								EntityEvoker.this.world.spawnEntity(entityvex);
								EntityEvoker.this.world.removeEntity(EntityEvoker.this);
							}
							else if (EntityEvoker.this.getLevel() < 200 && EntityEvoker.this.getLevel() >= 100)
							{
								EntityGiant entityvex = new EntityGiant(EntityEvoker.this.world);
								entityvex.copyLocationAndAnglesFrom(EntityEvoker.this);
								entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(EntityEvoker.this.getPosition()), (IEntityLivingData)null);
								entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
								entityvex.setIsHero(EntityEvoker.this.isHero());
								entityvex.setLastChance(EntityEvoker.this.hasLastChance());
								entityvex.setLevel(EntityEvoker.this.getLevel());
								entityvex.setGrowingAge(EntityEvoker.this.getGrowingAge());
								entityvex.playSound(ESound.bugSpecial, 10F, 0.5F);
								entityvex.playSound(ESound.blast, 10F, 1F);
								entityvex.spawnExplosionParticle();
								entityvex.setPolymorphTime(getCastingInterval());
								entityvex.setCustomNameTag(EntityEvoker.this.getName());
								NBTTagCompound tag = EntityEvoker.this.serializeNBT();
								entityvex.polymorpherData = tag;
								EntityEvoker.this.world.spawnEntity(entityvex);
								EntityEvoker.this.world.removeEntity(EntityEvoker.this);
							}
							else
							{
								EntityIronGolem entityvex = new EntityIronGolem(EntityEvoker.this.world);
								entityvex.copyLocationAndAnglesFrom(EntityEvoker.this);
								entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(EntityEvoker.this.getPosition()), (IEntityLivingData)null);
								entityvex.setOwnerId(EntityEvoker.this.getOwnerId());
								entityvex.setIsHero(EntityEvoker.this.isHero());
								entityvex.setLastChance(EntityEvoker.this.hasLastChance());
								entityvex.setLevel(EntityEvoker.this.getLevel());
								entityvex.setGrowingAge(EntityEvoker.this.getGrowingAge());
								entityvex.playSound(ESound.bugSpecial, 10F, 0.5F);
								entityvex.playSound(ESound.blast, 10F, 1F);
								entityvex.spawnExplosionParticle();
								entityvex.setPolymorphTime(getCastingInterval());
								entityvex.setCustomNameTag(EntityEvoker.this.getName());
								NBTTagCompound tag = EntityEvoker.this.serializeNBT();
								entityvex.polymorpherData = tag;
								EntityEvoker.this.world.spawnEntity(entityvex);
								EntityEvoker.this.world.removeEntity(EntityEvoker.this);
							}
						}

						@Nullable
						protected SoundEvent getSpellPrepareSound()
						{
							EntityEvoker.this.playSound(SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON, 1.0F, 1.0F);
							return SoundEvents.ENTITY_ILLAGER_PREPARE_MIRROR;
						}

						protected EntitySpellcasterIllager.SpellType getSpellType()
						{
							return EntitySpellcasterIllager.SpellType.POLYMORPH;
						}
					}

					@Override
					public void setSwingingArms(boolean swingingArms) {}
				}

				