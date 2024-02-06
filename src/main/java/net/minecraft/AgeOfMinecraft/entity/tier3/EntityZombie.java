package net.minecraft.AgeOfMinecraft.entity.tier3;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Undead;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityChicken;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPigZombie;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityZombie
extends EntityFriendlyCreature implements Undead
{
	private static final UUID UUID1 = UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A1");
	private static final UUID UUID2 = UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A2");
	private static final UUID UUID3 = UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A3");
	private static final AttributeModifier attackDamageBoost1 = new AttributeModifier(UUID1, "First Boost", 1D, 0);
	private static final AttributeModifier attackDamageBoost2 = new AttributeModifier(UUID2, "Second Boost", 1D, 0);
	private static final AttributeModifier attackDamageBoost3 = new AttributeModifier(UUID3, "Third Boost", 1D, 0);
	/** The attribute which determines the chance that this mob will spawn reinforcements */
	protected static final IAttribute SPAWN_REINFORCEMENTS_CHANCE = (new RangedAttribute((IAttribute)null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).setDescription("Spawn Reinforcements Chance");
	private static final DataParameter<Integer> ZOMBIE_VARIANT = EntityDataManager.createKey(EntityZombie.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> VILLAGER_TYPE = EntityDataManager.createKey(EntityZombie.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> CONVERTING = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
	private int conversionTime;
	private int helmetCount = 1;
	public EntityZombie(World worldIn)
	{
		super(worldIn);
		this.isOffensive = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.1D, 32.0F, 6.0F));
		this.tasks.addTask(2, new EntityAIFriendlyAttackMelee(this, 1.0D, false));
		this.tasks.addTask(3, new EntityAIWander(this, 1.0D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		setSize(0.5F, 1.95F);
		this.experienceValue = 5;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
		this.getAttributeMap().registerAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.rand.nextDouble() * 0.1F);
	}
	protected void entityInit()
	{
		super.entityInit();
		getDataManager().register(ZOMBIE_VARIANT, Integer.valueOf(0));
		getDataManager().register(VILLAGER_TYPE, Integer.valueOf(0));
		getDataManager().register(CONVERTING, Boolean.valueOf(false));
	}

	public boolean isASwarmingMob()
	{
		return true;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityZombie(this.world);
	}


	protected float getSoundPitch()
	{
		return super.getSoundPitch();
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
			switch (this.getZombieType())
			{
				case 1:
				return TranslateUtil.translateServer("entity.HuskHelpful.name");
				case 2:
				return TranslateUtil.translateServer("entity.PrisonZombieHelpful.name");
				default:
				return TranslateUtil.translateServer("entity." + s + ".name");
			}
		
	}

	public EnumTier getTier()
	{
		return this.getZombieType() != 0 ? EnumTier.TIER4 : EnumTier.TIER3;
	}
	public boolean isVillager()
	{
		return this.isAntiMob() ? false : ((Integer)getDataManager().get(VILLAGER_TYPE)).intValue() > 0;
	}
	public int getVillagerType()
	{
		return ((Integer)getDataManager().get(VILLAGER_TYPE)).intValue() - 1;
	}
	public void setVillagerType(int villagerType)
	{
		getDataManager().set(VILLAGER_TYPE, Integer.valueOf(this.isAntiMob() ? 0 : villagerType + 1));
	}
	public int getZombieType()
	{
		return ((Integer)getDataManager().get(ZOMBIE_VARIANT)).intValue() - 1;
	}
	public void setZombieType(int villagerType)
	{
		getDataManager().set(ZOMBIE_VARIANT, Integer.valueOf(villagerType + 1));
	}
	public void setToNotVillager()
	{
		getDataManager().set(VILLAGER_TYPE, Integer.valueOf(0));
	}

	public void attackWithAdditionalEffects(Entity entity)
	{
		float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
		if (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null && (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == Items.FLINT_AND_STEEL || getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == Items.FIRE_CHARGE))
		{
			entity.setFire(12);
		}

		if (this.isBurning() && this.rand.nextFloat() < f * 0.5F)
		{
			entity.setFire(5 * (int)f);
		}

		if (this.getZombieType() == 1 && entity instanceof EntityLivingBase)
		{
			((EntityLivingBase)entity).motionY += 0.2D;
			this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, (EntityLivingBase)entity, MobEffects.HUNGER, 200 * (int)f, 0);
			if (this.world.getDifficulty() == EnumDifficulty.HARD)
			this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, (EntityLivingBase)entity, MobEffects.WEAKNESS, 200, 0);
		}

		if (this.getZombieType() == 2 && entity instanceof EntityLivingBase)
		{
			entity.motionX = 0D;
			entity.motionY = 0D;
			entity.motionZ = 0D;
			
			this.inflictCustomStatusEffect(this.world.getDifficulty(), (EntityLivingBase)entity, MobEffects.SLOWNESS, 5, 1);
			if (entity instanceof EntityLiving && ((EntityLiving)entity).getAttackTarget() != null && this.world.getDifficulty() == EnumDifficulty.HARD && rand.nextInt(3) == 0)
			((EntityLiving)entity).setAttackTarget(null);
		}
	}

	public void performSpecialAttack()
	{
		setSpecialAttackTimer(700);
	}
	protected boolean isMovementBlocked()
	{
		return getSpecialAttackTimer() > 600 || super.isMovementBlocked();
	}

	public void onLivingUpdate()
	{
		setSize(0.5F, 1.95F);
		
		if (this.isVillager() && (this.getZombieType() == 1 || this.getZombieType() == 2 || this.getZombieType() == 3))
		this.setToNotVillager();
		
		IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		
		if(getHealth() < getMaxHealth() * 0.75 && !iattributeinstance.hasModifier(attackDamageBoost1))
		iattributeinstance.applyModifier(attackDamageBoost1);
		if(getHealth() < getMaxHealth() * 0.5F && !iattributeinstance.hasModifier(attackDamageBoost2))
		iattributeinstance.applyModifier(attackDamageBoost2);
		if(getHealth() < getMaxHealth() * 0.25F && !iattributeinstance.hasModifier(attackDamageBoost3))
		iattributeinstance.applyModifier(attackDamageBoost3);
		
		if(getHealth() >= getMaxHealth() * 0.75 && iattributeinstance.hasModifier(attackDamageBoost1))
		iattributeinstance.removeModifier(attackDamageBoost1);
		if(getHealth() >= getMaxHealth() * 0.5F && iattributeinstance.hasModifier(attackDamageBoost2))
		iattributeinstance.removeModifier(attackDamageBoost2);
		if(getHealth() >= getMaxHealth() * 0.25F && iattributeinstance.hasModifier(attackDamageBoost3))
		iattributeinstance.removeModifier(attackDamageBoost3);
		
		if (this.getHeldItemMainhand().getItem() instanceof ItemAppleGold && this.isVillager() && this.isPotionActive(MobEffects.WEAKNESS))
		{
			if (this.ticksExisted > 53)
			this.ticksExisted = 20;
			this.swingArm(EnumHand.MAIN_HAND);
			this.setActiveHand(EnumHand.MAIN_HAND);
			if (this.ticksExisted % 2 == 0)
			this.rotationPitch = 40F;
			else
			this.rotationPitch = 0F;
			if (this.ticksExisted == 50)
			{
				for (int ai = 0; ai < ((ItemFood)this.getHeldItemMainhand().getItem()).getHealAmount(this.getHeldItemMainhand()); ++ai)
				this.spawnHeartParticle();
				heal(((ItemFood)this.getHeldItemMainhand().getItem()).getHealAmount(this.getHeldItemMainhand()));
				playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
				this.startConversion(200);
			}
		}

		if (this.isEntityAlive() && this.getHeldItemOffhand().getItem() instanceof ItemAppleGold && this.isVillager() && this.isPotionActive(MobEffects.WEAKNESS))
		{
			if (this.ticksExisted > 53)
			this.ticksExisted = 20;
			if (this.ticksExisted % 2 == 0)
			this.rotationPitch = 40F;
			else
			this.rotationPitch = 0F;
			this.swingArm(EnumHand.OFF_HAND);
			this.setActiveHand(EnumHand.OFF_HAND);
			if (this.ticksExisted == 50)
			{
				this.spawnHeartParticle();
				heal(((ItemFood)this.getHeldItemOffhand().getItem()).getHealAmount(this.getHeldItemOffhand()));
				playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
				this.startConversion(200);
			}
		}

		if (getOwner() != null)
		{
			if (getDistanceSq(getOwner()) >= 2304.0D && this.isElytraFlying())
			{
				double d01 = getOwner().posX - this.posX;
				double d11 = getOwner().posY - this.posY;
				double d21 = getOwner().posZ - this.posZ;
				float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11 + d21 * d21);
				this.motionX = (d01 / f2 * 0.5D * 0.5D + this.motionX * 0.5D);
				this.motionY = (d11 / f2 * 0.5D * 0.5D + this.motionZ * 0.5D);
				this.motionZ = (d21 / f2 * 0.5D * 0.5D + this.motionZ * 0.5D);
				faceEntity(getOwner(), 180.0F, 180.0F);
			}
		}

		if (this.world.canSeeSky(getPosition()) && ((this.getAttackTarget() != null && this.onGround) || (!this.isWild() && this.getOwner().posY > this.posY && this.getOwner().isElytraFlying())) && getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA)
		{
			this.motionY = 1F;
			this.setFlag(7, true);
		}

		if (!this.onGround && getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA && !this.isElytraFlying())
		this.setFlag(7, true);
		
		if (this.isElytraFlying())
		{
			this.renderYawOffset = this.rotationYaw = -((float)MathHelper.atan2(this.motionX, this.motionZ)) * (180F / (float)Math.PI);
		}

		if (!(this instanceof EntityPigZombie))
		{
			if ((isHero()) && (getSpecialAttackTimer() > 600))
			{
				this.motionX = 0.0D;
				this.motionZ = 0.0D;
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(32.0D, 32.0D, 32.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
				if ((list != null) && (!list.isEmpty()))
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityLivingBase entity = (EntityLivingBase)list.get(i1);
						if (entity != null)
						{
							if (!isOnSameTeam(entity))
							{
								entity.hurtResistantTime = 0;
								this.inflictEngenderMobDamage(entity, " was yelled at to death by ", (new DamageSource("yell")).setDamageBypassesArmor(), 0.05F);
							}
						}
					}
				}
			}
			if ((isHero()) && (getSpecialAttackTimer() > 600) && (getSpecialAttackTimer() < 640))
			{
				this.rotationPitch = -50.0F;
			}
			if ((isHero()) && (getSpecialAttackTimer() == 600))
			{
				List<EntityFriendlyCreature> list = this.world.getEntitiesWithinAABB(EntityFriendlyCreature.class, getEntityBoundingBox().grow(32.0D, 32.0D, 32.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
				if ((list != null) && (!list.isEmpty()))
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityFriendlyCreature entity = (EntityFriendlyCreature)list.get(i1);
						if (entity != null)
						{
							if (isOnSameTeam(entity))
							{
								this.moralRaisedTimer = 600;
								entity.moralRaisedTimer = 600;
							}
						}
					}
				}
			}
			if ((isHero()) && (getSpecialAttackTimer() == 640))
			{
				if (isChild())
				playSound(ESound.zombieSpecial, 10.0F, 1.5F);
				else
					playSound(ESound.zombieSpecial, 10.0F, 1.0F);
				spawnZombieAlly();
			}
			if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 128.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
			{
				performSpecialAttack();
			}
		}
		if (this.helmetCount < 0)
		{
			this.helmetCount = 0;
		}
		if ((((this instanceof EntityPigZombie)) || (isAntiMob()) || this.getZombieType() == 1 || (isChild()) || (isHero())) && (this.helmetCount != 0))
		{
			if (!this.world.isRemote)
			dropItem(Items.LEATHER_HELMET, 1);
			this.helmetCount -= 1;
		}

		ItemStack head = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		
		if (!head.isEmpty() && head.getItem() == Items.LEATHER_HELMET && this.isEntityAlive() && !this.world.isDaytime() && !this.world.isRemote)
		{
			swingArm(EnumHand.MAIN_HAND);
			this.helmetCount += 1;
			setItemStackToSlot(EntityEquipmentSlot.HEAD,ItemStack.EMPTY);
			playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
		}

		if (this.isEntityAlive() && this.world.isDaytime() && !this.world.isRemote && !isChild() && !isImmuneToFire() && !this.isAntiMob() && this.getZombieType() != 1 && !isHero())
		{
			float f = this.getBrightness();
			
			if (f > 0.5F && this.ticksExisted % (!this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() ? 80 : 10) == 0 && this.world.canSeeSky(new BlockPos(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ)))
			{
				boolean flag = true;
				ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				
				if (!itemstack.isEmpty())
				{
					if (itemstack.isItemStackDamageable())
					{
						itemstack.damageItem(1, this);
						
						if (itemstack.getItemDamage() >= itemstack.getMaxDamage())
						{
							this.renderBrokenItemStack(itemstack);
							this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
						}
					}

					flag = false;
				}

				if (flag)
				{
					if (this.helmetCount > 0)
					{
						swingArm(EnumHand.MAIN_HAND);
						this.helmetCount -= 1;
						setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
						playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
					}
					else
					{
						setFire(8);
					}
				}
			}
		}
		super.onLivingUpdate();
	}
	public void onUpdate()
	{
		ItemStack hats = this.helmetCount > 0 ? new ItemStack(Items.LEATHER_HELMET, this.helmetCount) : ItemStack.EMPTY;
		basicInventory.setInventorySlotContents(7, hats);
		
		if ((!this.world.isRemote) && (isConverting()))
		{
			int i = getConversionTimeBoost();
			this.conversionTime -= i;
			if (this.conversionTime <= 0)
			{
				convertToVillager();
			}
		}
		super.onUpdate();
	}
	public void updateRidden()
	{
		super.updateRidden();
		if ((getRidingEntity() instanceof EntityCreature))
		{
			EntityCreature entitycreature = (EntityCreature)getRidingEntity();
			this.renderYawOffset = entitycreature.renderYawOffset;
			entitycreature.rotationPitch = this.rotationPitch;
			entitycreature.rotationYawHead = this.rotationYawHead;
			if (getAttackTarget() != null)
			{
				entitycreature.setAttackTarget(getAttackTarget());
			}
			if (this.ticksExisted % 40 == 0)
			{
				this.renderYawOffset = (this.rotationYaw = this.rotationYawHead);
				if (getAttackTarget() == null)
				{
					entitycreature.heal(5.0F);
				} else {
						entitycreature.heal(1.0F);
					}
				}
			}
		}
		protected SoundEvent getAmbientSound()
		{
			return this.getZombieType() == 1 ? SoundEvents.ENTITY_HUSK_AMBIENT : (isVillager() ? SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT : SoundEvents.ENTITY_ZOMBIE_AMBIENT);
		}
		protected SoundEvent getHurtSound(DamageSource source)
		{
			return this.getZombieType() == 1 ? SoundEvents.ENTITY_HUSK_HURT : (isVillager() ? SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT : SoundEvents.ENTITY_ZOMBIE_HURT);
		}
		protected SoundEvent getDeathSound()
		{
			return this.getZombieType() == 1 ? SoundEvents.ENTITY_HUSK_DEATH : (isVillager() ? SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH : SoundEvents.ENTITY_ZOMBIE_DEATH);
		}
		protected void playStepSound(BlockPos pos, Block blockIn)
		{
			if (this instanceof EntityPigZombie)
			playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F / this.getFittness());
			playSound(this.getZombieType() == 1 ? SoundEvents.ENTITY_HUSK_STEP : (isVillager() ? SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP : SoundEvents.ENTITY_ZOMBIE_STEP), 0.15F, 1.0F / this.getFittness());
		}
		public void onDeath(DamageSource cause)
		{
			super.onDeath(cause);
			if (!this.world.isRemote)
			{
				if (this.helmetCount > 0)
				{
					dropItem(Items.LEATHER_HELMET, this.helmetCount);
				}
			}

			if (((cause.getTrueSource() instanceof EntityCreeper)) && (((EntityCreeper)cause.getTrueSource()).getPowered()))
			{
				entityDropItem(new ItemStack(Items.SKULL, 1, 2), 0.0F);
			}
		}

		@Nullable
		protected ResourceLocation getLootTable()
		{
			switch (this.getZombieType())
			{
				case 1:
				return ELoot.ENTITIES_HUSK;
				case 2:
				return ELoot.ENTITIES_PRISON_ZOMBIE;
				default:
				{
					if (this.isVillager())
					return ELoot.ENTITIES_ZOMBIE_VILLAGER;
					else
					return ELoot.ENTITIES_ZOMBIE;
				}
			}
		}
		protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
		{
			super.dropFewItems(p_70628_1_, p_70628_2_);
		}
		public boolean isEntityUndead()
		{
			return true;
		}

		/**
		* Gives armor or weapon for entity based on given DifficultyInstance
		*/
		protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
		{
			if (this.rand.nextFloat() < (this.world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.05F))
			{
				int i = this.rand.nextInt(3);
				
				if (i == 0)
				{
					this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
					if (getRNG().nextInt(3) == 0)
					this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.IRON_SWORD));
				}
				else
				{
					this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
					if (getRNG().nextInt(3) == 0)
					this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.IRON_SHOVEL));
				}
			}

			if (this.rand.nextFloat() < 0.25F * difficulty.getClampedAdditionalDifficulty())
			{
				int i = this.rand.nextInt(2);
				float f = this.world.getDifficulty() == EnumDifficulty.HARD ? 0.325F : 0.25F;
				
				if (this.rand.nextFloat() < 0.1F)
				{
					++i;
				}

				if (this.rand.nextFloat() < 0.15F)
				{
					++i;
				}

				if (this.rand.nextFloat() < 0.2F)
				{
					++i;
				}

				boolean flag = true;
				
				for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
				{
					if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
					{
						ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);
						
						if (!flag && this.rand.nextFloat() < f)
						{
							break;
						}

						flag = false;
						
						if (itemstack.isEmpty())
						{
							Item item = getArmorByChance(entityequipmentslot, i);
							
							if (item != null)
							{
								this.setItemStackToSlot(entityequipmentslot, new ItemStack(item));
							}
						}
					}
				}
			}
		}

		public void writeEntityToNBT(NBTTagCompound tagCompound)
		{
			super.writeEntityToNBT(tagCompound);
			tagCompound.setInteger("ZombieType", getZombieType());
			if (isVillager())
			{
				tagCompound.setBoolean("IsVillager", true);
				tagCompound.setInteger("VillagerProfession", getVillagerType());
			}
			tagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
			tagCompound.setInteger("Helmets", this.helmetCount);
		}
		public void readEntityFromNBT(NBTTagCompound tagCompund)
		{
			super.readEntityFromNBT(tagCompund);
			setZombieType(tagCompund.getInteger("ZombieType"));
			if (tagCompund.getBoolean("IsVillager"))
			{
				if (tagCompund.hasKey("VillagerProfession", 99))
				{
					setVillagerType(tagCompund.getInteger("VillagerProfession"));
				}
				else
				{
					setVillagerType(this.world.rand.nextInt(5));
				}
			}
			if (tagCompund.hasKey("ConversionTime", 99) && tagCompund.getInteger("ConversionTime") > -1)
			{
				this.startConversion(tagCompund.getInteger("ConversionTime"));
			}
			if ((tagCompund.hasKey("Helmets", 99)))
			{
				this.helmetCount = tagCompund.getInteger("Helmets");
			}
		}
		public void onKillEntity(EntityLivingBase entityLivingIn)
		{
			super.onKillEntity(entityLivingIn);
			if (((this.world.getDifficulty() == EnumDifficulty.NORMAL) || (this.world.getDifficulty() == EnumDifficulty.HARD)) && ((entityLivingIn instanceof net.minecraft.entity.passive.EntityVillager)))
			{
				net.minecraft.entity.passive.EntityVillager entityvillager = (net.minecraft.entity.passive.EntityVillager)entityLivingIn;
				EntityZombie entityzombie = new EntityZombie(this.world);
				entityzombie.rotationPitch = entityvillager.rotationPitch;
				entityzombie.renderYawOffset = entityzombie.rotationYaw = entityzombie.rotationYawHead = entityvillager.rotationYawHead;
				entityzombie.copyLocationAndAnglesFrom(entityLivingIn);
				this.world.removeEntity(entityLivingIn);
				entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), new EntityZombie.GroupData(false, true));
				entityzombie.setVillagerType(entityvillager.getProfession());
				entityzombie.setChild(entityLivingIn.isChild());
				entityzombie.setNoAI(entityvillager.isAIDisabled());
				if (!isWild())
				{
					entityzombie.setOwnerId(getOwnerId());
				}
				if (entityvillager.hasCustomName())
				{
					entityzombie.setCustomNameTag(entityvillager.getCustomNameTag());
				}
				this.world.spawnEntity(entityzombie);
				this.world.playEvent((EntityPlayer)null, 1026, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
			}
			if ((entityLivingIn instanceof net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager))
			{
				net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager entityvillager = (net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager)entityLivingIn;
				EntityZombie entityzombie = new EntityZombie(this.world);
				entityzombie.rotationPitch = entityvillager.rotationPitch;
				entityzombie.renderYawOffset = entityzombie.rotationYaw = entityzombie.rotationYawHead = entityvillager.rotationYawHead;
				entityzombie.copyLocationAndAnglesFrom(entityLivingIn);
				this.world.removeEntity(entityLivingIn);
				entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), new EntityZombie.GroupData(false, true));
				entityzombie.setVillagerType(entityvillager.getProfession());
				entityzombie.setChild(entityLivingIn.isChild());
				entityzombie.setNoAI(entityvillager.isAIDisabled());
				if (!isWild())
				{
					entityzombie.setOwnerId(getOwnerId());
				}
				if (entityvillager.hasCustomName())
				{
					entityzombie.setCustomNameTag(entityvillager.getCustomNameTag());
				}
				this.world.spawnEntity(entityzombie);
				this.world.playEvent((EntityPlayer)null, 1026, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
			}
		}
		public float getEyeHeight()
		{
			return (this.getZombieType() == 1 ? this.height * 0.9F : this.height * 0.87F);
		}
		protected boolean canEquipItem(ItemStack p_175448_1_)
		{
			return (p_175448_1_.getItem() == Items.EGG) && (isChild()) && (isRiding()) ? false : super.canEquipItem(p_175448_1_);
		}
		@Nullable
		public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
		{
			livingdata = super.onInitialSpawn(difficulty, livingdata);
			float f = difficulty.getClampedAdditionalDifficulty();
			if (livingdata == null)
			{
				livingdata = new EntityZombie.GroupData(this.world.rand.nextFloat() < ForgeModContainer.zombieBabyChance, this.world.rand.nextFloat() < 0.05F);
			}
			if ((livingdata instanceof GroupData))
			{
				GroupData groupdata = (GroupData)livingdata;
				Biome biome = this.world.getBiome(new BlockPos(this));
				
				if (biome instanceof BiomeDesert && this.rand.nextInt(5) != 0)
				{
					setZombieType(1);
					setToNotVillager();
				}

				if (groupdata.isVillager)
				{
					setVillagerType(this.rand.nextInt(5));
				}
				if (groupdata.isChild)
				{
					setChild(true);
					this.setGrowingAge(-60000);
					if (this.world.rand.nextFloat() < 0.05D)
					{
						List<EntityChicken> list = this.world.getEntitiesWithinAABB(EntityChicken.class, getEntityBoundingBox().grow(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);
						if (!list.isEmpty())
						{
							EntityChicken entitychicken = (EntityChicken)list.get(0);
							entitychicken.setChickenJockey(true);
							startRiding(entitychicken);
							entitychicken.setOwnerId(getOwnerId());
						}
					}
					else if (this.world.rand.nextFloat() < 0.05D)
					{
						EntityChicken entitychicken1 = new EntityChicken(this.world);
						entitychicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
						entitychicken1.onInitialSpawn(difficulty, (IEntityLivingData)null);
						entitychicken1.setChickenJockey(true);
						entitychicken1.setOwnerId(getOwnerId());
						this.world.spawnEntity(entitychicken1);
						startRiding(entitychicken1);
					}
				}
			}
			setEquipmentBasedOnDifficulty(difficulty);
			setEnchantmentBasedOnDifficulty(difficulty);
			if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
			{
				Calendar calendar = this.world.getCurrentDate();
				
				if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
				{
					this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
					this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
				}
			}

			double d0 = this.rand.nextDouble() * 1.5D * (double)f;
			
			if (d0 > 1.0D)
			{
				this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
			}

			if (this.rand.nextFloat() < f * 0.05F)
			{
				this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
				this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
			}

			return livingdata;
		}

		public void becomeAHero()
		{
			super.becomeAHero();
			this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
		}

		public boolean interact(EntityPlayer player, EnumHand hand)
		{
			ItemStack stack = player.getHeldItem(hand);
			ItemStack heldItem = new ItemStack(stack.getItem());
			
			if (this.isOnSameTeam(player) && this.isChild() && stack.isEmpty() && player.isSneaking() && this.getRidingEntity() == null)
			{
				List<EntityChicken> list = this.world.getEntitiesWithinAABB(EntityChicken.class, getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
				if ((list != null) && (!list.isEmpty()) && !this.isBeingRidden())
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityChicken entity = (EntityChicken)list.get(i1);
						if (entity != null && !entity.isBeingRidden() && this.isOnSameTeam(entity) && this.isChild() && !this.world.isRemote)
						{
							entity.ticksExisted = 0;
							this.startRiding(entity);
							playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
							break;
						}
					}
				}
				return true;
			}
			else if (this instanceof EntityPigZombie && !stack.isEmpty() && stack.getItem() == Items.SADDLE && getRidingEntity() == null && ((hasOwner(player)) || (player.isOnSameTeam(this))))
			{
				playSound(SoundEvents.ENTITY_PIG_SADDLE, 0.5F, 1.0F);
				player.startRiding(this);
				return true;
			}
			else if (!stack.isEmpty() && (stack.getItem() == Items.LEATHER_HELMET))
			{
				this.helmetCount += 1;
				playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
				player.swingArm(hand);
				if (!this.world.isRemote)
				stack.shrink(1);
				return true;
			}
			else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) && (getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD || stack.getItem() == Items.BONE || stack.getItem() == Item.getItemFromBlock(Blocks.END_ROD) || stack.getItem() == Items.FEATHER) && stack.getItem() != Items.LEATHER_HELMET)
			{
				setItemStackToSlot(EntityEquipmentSlot.HEAD, stack);
				this.playEquipSound(stack);
				player.swingArm(hand);
				if (!this.world.isRemote)
				{
					heldItem.setTagCompound(stack.getTagCompound());
					heldItem.setItemDamage(stack.getItemDamage());
					setItemStackToSlot(EntityEquipmentSlot.HEAD, heldItem);
					stack.shrink(1);
				}
				return true;
			}
			else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()) && getSlotForItemStack(stack) == EntityEquipmentSlot.CHEST)
			{
				setItemStackToSlot(EntityEquipmentSlot.CHEST, stack);
				this.playEquipSound(stack);
				player.swingArm(hand);
				if (!this.world.isRemote)
				{
					heldItem.setTagCompound(stack.getTagCompound());
					heldItem.setItemDamage(stack.getItemDamage());
					setItemStackToSlot(EntityEquipmentSlot.CHEST, heldItem);
					stack.shrink(1);
				}
				return true;
			}
			else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty()) && getSlotForItemStack(stack) == EntityEquipmentSlot.LEGS)
			{
				setItemStackToSlot(EntityEquipmentSlot.LEGS, stack);
				this.playEquipSound(stack);
				player.swingArm(hand);
				if (!this.world.isRemote)
				{
					heldItem.setTagCompound(stack.getTagCompound());
					heldItem.setItemDamage(stack.getItemDamage());
					setItemStackToSlot(EntityEquipmentSlot.LEGS, heldItem);
					stack.shrink(1);
				}
				return true;
			}
			else if (!stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty()) && getSlotForItemStack(stack) == EntityEquipmentSlot.FEET)
			{
				setItemStackToSlot(EntityEquipmentSlot.FEET, stack);
				this.playEquipSound(stack);
				player.swingArm(hand);
				if (!this.world.isRemote)
				{
					heldItem.setTagCompound(stack.getTagCompound());
					heldItem.setItemDamage(stack.getItemDamage());
					setItemStackToSlot(EntityEquipmentSlot.FEET, heldItem);
					stack.shrink(1);
				}
				return true;
			}

			else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) && stack.getItem() != Items.NAME_TAG && ((getSlotForItemStack(stack) == EntityEquipmentSlot.MAINHAND) || (stack.getItem() instanceof ItemSword) || (stack.getItem() instanceof ItemTool) || (stack.getItem() == Items.BOW)))
			{
				playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 2.0F);
				player.swingArm(hand);
				if (!this.world.isRemote)
				{
					heldItem.setTagCompound(stack.getTagCompound());
					heldItem.setItemDamage(stack.getItemDamage());
					setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem);
					stack.shrink(1);
				}
				return true;
			}
			else if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty()) && ((getSlotForItemStack(stack) == EntityEquipmentSlot.OFFHAND) || (stack.getItem() instanceof ItemSword) || (stack.getItem() instanceof ItemTool) || (stack.getItem() instanceof ItemFood && (!(stack.getItem() instanceof ItemAppleGold) || (stack.getItem() == Items.GOLDEN_APPLE && stack.getMetadata() == 0 && this.isVillager() && this.isPotionActive(MobEffects.WEAKNESS)))) || (stack.getItem() == Items.TIPPED_ARROW) || (stack.getItem().getItemUseAction(stack) == EnumAction.BLOCK) || (stack.getItem() == Items.TOTEM_OF_UNDYING)))
			{
				playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 2.0F);
				player.swingArm(hand);
				if (!this.world.isRemote)
				{
					heldItem.setTagCompound(stack.getTagCompound());
					heldItem.setItemDamage(stack.getItemDamage());
					setItemStackToSlot(EntityEquipmentSlot.OFFHAND, heldItem);
					stack.shrink(1);
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		protected void startConversion(int ticks)
		{
			this.conversionTime = ticks;
			getDataManager().set(CONVERTING, Boolean.valueOf(true));
			removePotionEffect(MobEffects.WEAKNESS);
			addPotionEffect(new PotionEffect(MobEffects.STRENGTH, ticks, Math.min(this.world.getDifficulty().getDifficultyId() - 1, 0)));
			this.world.setEntityState(this, (byte)16);
		}
		@SideOnly(Side.CLIENT)
		public void handleStatusUpdate(byte id)
		{
			if (id == 16)
			{
				if (!isSilent())
				{
					this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
				}
			}
			else {
				super.handleStatusUpdate(id);
			}
		}

		public boolean attackEntityFrom(DamageSource source, float amount)
		{
			if (super.attackEntityFrom(source, amount))
			{
				EntityLivingBase entitylivingbase = this.getAttackTarget();
				
				if (entitylivingbase == null && source.getTrueSource() instanceof EntityLivingBase)
				{
					entitylivingbase = (EntityLivingBase)source.getTrueSource();
				}

				if (entitylivingbase != null && this.isHero())
				{
					spawnZombieAlly();
				}

				return true;
			}
			else
			{
				return false;
			}
		}

		public void spawnZombieAlly()
		{
			int i = MathHelper.floor(this.posX);
			int j = MathHelper.floor(this.posY);
			int k = MathHelper.floor(this.posZ);
			if ((double)this.rand.nextFloat() < this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).getAttributeValue() && this.world.getGameRules().getBoolean("doMobSpawning"))
			{
				for (int l = 0; l < 50; ++l)
				{
					int i1 = i + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
					int j1 = j + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
					int k1 = k + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
					
					if (this.world.getBlockState(new BlockPos(i1, j1 - 1, k1)).isSideSolid(this.world, new BlockPos(i1, j1 - 1, k1), net.minecraft.util.EnumFacing.UP) && this.world.getLightFromNeighbors(new BlockPos(i1, j1, k1)) < 10)
					{
						EntityZombie entityzombie = new EntityZombie(this.world);
						entityzombie.setPosition((double)i1, (double)j1, (double)k1);
						
						if (!this.world.isAnyPlayerWithinRangeAt((double)i1, (double)j1, (double)k1, 7.0D) && this.world.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), entityzombie) && this.world.getCollisionBoxes(entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(entityzombie.getEntityBoundingBox()))
						{
							this.world.spawnEntity(entityzombie);
							entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), (IEntityLivingData)null);
							entityzombie.setZombieType(this.getZombieType());
							entityzombie.setOwnerId(this.getOwnerId());
							entityzombie.setIsAntiMob(this.isAntiMob());
							entityzombie.setGrowingAge(this.getGrowingAge());
							if (this.isVillager() && this.rand.nextFloat() < 0.25F)
							entityzombie.setVillagerType(this.rand.nextInt(5));
							this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).getBaseValue() - 0.05D);
							entityzombie.getEntityAttribute(SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0D);
							break;
						}
					}
				}
			}
		}

		public boolean isConverting()
		{
			return ((Boolean)getDataManager().get(CONVERTING)).booleanValue();
		}
		protected void convertToVillager()
		{
			net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager entityvillager = new net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager(this.world);
			entityvillager.copyLocationAndAnglesFrom(this);
			entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null);
			entityvillager.renderYawOffset = entityvillager.rotationYaw = entityvillager.rotationYawHead = this.rotationYawHead;
			entityvillager.rotationPitch = this.rotationPitch;
			entityvillager.setNoAI(isAIDisabled());
			entityvillager.setProfession(getVillagerType());
			entityvillager.setOwnerId(this.getOwnerId());
			if (hasCustomName())
			{
				entityvillager.setCustomNameTag(getCustomNameTag());
			}
			if (!this.world.isRemote)
			{
				if (this.helmetCount > 0)
				{
					dropItem(Items.LEATHER_HELMET, this.helmetCount);
				}
			}
			this.world.spawnEntity(entityvillager);
			entityvillager.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
			this.world.playEvent((EntityPlayer)null, 1027, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
			this.world.removeEntity(this);
		}
		protected int getConversionTimeBoost()
		{
			int i = 1;
			if (this.rand.nextFloat() < 0.01F)
			{
				int j = 0;
				BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
				for (int k = (int)this.posX - 4; (k < (int)this.posX + 4) && (j < 14); k++)
				{
					for (int l = (int)this.posY - 4; (l < (int)this.posY + 4) && (j < 14); l++)
					{
						for (int i1 = (int)this.posZ - 4; (i1 < (int)this.posZ + 4) && (j < 14); i1++)
						{
							Block block = this.world.getBlockState(blockpos$mutableblockpos.setPos(k, l, i1)).getBlock();
							if ((block == Blocks.IRON_BARS) || (block == Blocks.BED))
							{
								if (this.rand.nextFloat() < 0.3F)
								{
									i++;
								}
								j++;
							}
						}
					}
				}
			}
			return i;
		}
		protected SoundEvent getRegularHurtSound()
		{
			return getTotalArmorValue() >= 10 ? ESound.metalHit : ESound.fleshHit;
		}
		
		public class GroupData implements IEntityLivingData
		{
			public boolean isChild;
			public boolean isVillager;
			public GroupData(boolean isBaby, boolean isVillagerZombie)
			{
				this.isChild = false;
				this.isVillager = false;
				this.isChild = isBaby;
				this.isVillager = isVillagerZombie;
			}
		}
	}