package net.minecraft.AgeOfMinecraft.entity.tier4;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.Ender;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityEndermite;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStorm;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntityEnderman
extends EntityFriendlyCreature implements IJumpingMount, Armored, Ender
{
	private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
	private static final AttributeModifier attackingSpeedBoostModifier = new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15000000596046448D, 0).setSaved(false);
	private static final Set<Block> carriableBlocks = Sets.newIdentityHashSet();
	private static final DataParameter<Optional<IBlockState>> CARRIED_BLOCK = EntityDataManager.createKey(EntityEnderman.class, DataSerializers.OPTIONAL_BLOCK_STATE);
	private int field_184720_bx = 0;
	public boolean andr;
	private static final DataParameter<Boolean> OMNI_DODGE = EntityDataManager.createKey(EntityEnderman.class, DataSerializers.BOOLEAN);
	public EntityEnderman(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 2.875F);
		this.stepHeight = 1.0F;
		this.isOffensive = true;
		setPathPriority(PathNodeType.WATER, -1.0F);
		setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
		setPathPriority(PathNodeType.DANGER_CACTUS, -1.0F);
		this.tasks.addTask(0, new AIPlaceBlock(this));
		this.tasks.addTask(0, new AITakeBlock(this));
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.5D, 48.0F, 12.0F));
		this.tasks.addTask(3, new EntityAIFriendlyAttackMelee(this, 1.5D, true));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.experienceValue = 10;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityEnderman(this.world);
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
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

			return TranslateUtil.translateServer("entity." + s + ".name") + (this.canDodgeAllAttacks() ? " (Ultra Instinct)" : "");
		
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
		this.dataManager.register(CARRIED_BLOCK, Optional.<IBlockState>absent());
		this.dataManager.register(OMNI_DODGE, Boolean.valueOf(false));
	}
	public EnumTier getTier()
	{
		return this.canDodgeAllAttacks() || this.andr ? EnumTier.TIER6 : EnumTier.TIER4;
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
		IBlockState iblockstate = getHeldBlockState();
		tagCompound.setBoolean("Andrea", andr);
		tagCompound.setBoolean("OmniDodge", canDodgeAllAttacks());
		if (iblockstate != null)
		{
			tagCompound.setShort("carried", (short)Block.getIdFromBlock(iblockstate.getBlock()));
			tagCompound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
		}
	}
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		IBlockState iblockstate;
		this.setDodgeAllAttacks(Boolean.valueOf(tagCompund.getBoolean("OmniDodge")));
		this.andr = tagCompund.getBoolean("Andrea");
		if (tagCompund.hasKey("carried", 8))
		{
			iblockstate = Block.getBlockFromName(tagCompund.getString("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
		}
		else
		{
			iblockstate = Block.getBlockById(tagCompund.getShort("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
		}
		if ((iblockstate == null) || (iblockstate.getBlock() == null) || (iblockstate.getMaterial() == Material.AIR))
		{
			iblockstate = null;
		}
		setHeldBlockState(iblockstate);
	}
	public boolean canDodgeAllAttacks()
	{
		return ((Boolean)this.dataManager.get(OMNI_DODGE)).booleanValue();
	}
	public void setDodgeAllAttacks(boolean powered)
	{
		this.dataManager.set(OMNI_DODGE, Boolean.valueOf(powered));
	}
	protected float getSoundPitch()
	{
		return andr ? super.getSoundPitch() - 0.25F : super.getSoundPitch();
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
						teleportTo(entity.posX, entity.posY, entity.posZ);
						this.teleportRandomly();
						attackEntityAsMob(entity);
					}
				}
			}
		}
		setSpecialAttackTimer(this.andr ? 20 : 1200);
	}
	public void setCustomNameTag(String name)
	{
		super.setCustomNameTag(name);
		
		if (EngenderConfig.debugMode && !this.andr && "Andrea".equals(name))
		{
			this.ticksExisted = 0;
			this.becomeAHero();
			this.andr = true;
		}
		else
		{
			this.andr = false;
		}
	}
	public void onLivingUpdate()
	{
		ItemStack block = this.getHeldBlockState() != null ? new ItemStack(this.getHeldBlockState().getBlock()) : ItemStack.EMPTY;
		basicInventory.setInventorySlotContents(7, block);
		if (!this.world.isRemote && this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityWitherStorm && rand.nextInt(100) == 0)
		{
			this.teleportTo(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ);
			getAttackTarget().motionY = -0.1D;
			((EntityWitherStorm)getAttackTarget()).Grow(((EntityWitherStorm)getAttackTarget()).getSize() - 2);
			getAttackTarget().hurtResistantTime = 0;
			this.setHeldBlockState(Blocks.OBSIDIAN.getDefaultState());
			this.attackEntityAsMob(getAttackTarget());
		}

		if (this.getLevel() >= 300 && this.getStrength() >= 100F && this.getStamina() >= 100F && this.getIntelligence() >= 100F && this.getDexterity() >= 100F && this.getAgility() >= 100F)
		this.setDodgeAllAttacks(true);
		
		if (this.getAttackTarget() != null && this.canDodgeAllAttacks() && this.getNavigator().noPath())
		this.teleportToEntity(this.getAttackTarget());
		
		if (this.canDodgeAllAttacks())
		{
			this.clearActivePotions();
			this.extinguish();
			if (this.getEnergy() <= 0F)
			{
				this.setHealth(0F);
				this.playSound(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
			}
		}

		if (!this.isHero() && this.andr)
		this.becomeAHero();
		
			this.setSize(0.5F, 2.875F);
		

		if ((getAttackTarget() != null) && (getAttackTarget().isEntityAlive()) && (getDistanceSq(getAttackTarget()) < 512.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			performSpecialAttack();
		}
		if (isWet() && !this.andr && this.hurtResistantTime <= 10)
		{
			attackEntityFrom((new DamageSource("waterburn")).setFireDamage().setDamageBypassesArmor(), 2F);
		}
		if (getAttackTarget() != null)
		{
			if (this.ticksExisted % 400 == 0)
			{
				playSound(SoundEvents.ENTITY_ENDERMEN_STARE, this.isSneaking() ? 1.0F : 2.5F, 1.0F);
			}
			if ((getAttackTarget().height <= 2.25F) && (getAttackTarget().isNonBoss()) && ((getAttackTarget() instanceof EntityLiving)) && !(getAttackTarget() instanceof EntityFriendlyCreature))
			{
				((EntityLiving)getAttackTarget()).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 9));
				((EntityLiving)getAttackTarget()).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60, 0));
				((EntityLiving)getAttackTarget()).getLookHelper().setLookPosition(getAttackTarget().posX + (this.rand.nextDouble() * 60.0D - 30.0D), getAttackTarget().posY + (this.rand.nextDouble() * 60.0D - 30.0D), getAttackTarget().posZ + (this.rand.nextDouble() * 60.0D - 30.0D), 180.0F, 180.0F);
				((EntityLiving)getAttackTarget()).setAttackTarget((EntityLivingBase)null);
				getAttackTarget().renderYawOffset = getAttackTarget().rotationYaw = getAttackTarget().rotationYawHead;
				((EntityLiving)getAttackTarget()).targetTasks.taskEntries.clear();
			}
			
			if (this.canDodgeAllAttacks() && this.getEnergy() > 20F && rand.nextInt(80) == 0)
			{
				getAttackTarget().hurtResistantTime = 0;
				this.teleportRandomly();
				this.motionX = this.motionY = this.motionZ = 0D;
				this.setEnergy(this.getEnergy() - 3F);
				this.attackEntityAsMob(getAttackTarget());
			}
		}
		if (this.world.isRemote && this.isEntityAlive())
		{
			for (int i = 0; i < 2; i++)
			{
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
				if (this.canDodgeAllAttacks())
				{
					this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0D, 0D, 0D, new int[0]);
					this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0D, 0D, 0D, new int[0]);
					this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0D, 0D, 0D, new int[0]);
					this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0D, 0D, 0D, new int[0]);
					this.world.spawnParticle(EnumParticleTypes.END_ROD, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0D, 0D, 0D, new int[0]);
				}
			}
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
		if (this.isEntityAlive() && !this.isBeingRidden() && this.getGuardBlock() == null && (getOwner() != null) && (getDistanceSq(getOwner()) > 4096.0D || !this.canEntityBeSeen(getOwner())) && (!this.world.isRemote))
		{
			setAttackTarget(null);
			getNavigator().clearPath();
			teleportTo(getOwner().posX, getOwner().posY, getOwner().posZ);
		}
		if (this.isEntityAlive() && !this.isBeingRidden() && (getRevengeTarget() != null) && (getRNG().nextInt(20) == 0))
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
		
		if (this.isEntityAlive() && isWet() && !this.andr)
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
		if (flag || this.canDodgeAllAttacks())
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
			return true;
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
		
		if (stack.isEmpty() && getRidingEntity() == null)
		{
			IBlockState iblockstate = getHeldBlockState();
			if (this.hasOwner(player) && iblockstate != null)
			{
				if (!this.world.isRemote)
				{
					entityDropItem(new ItemStack(iblockstate.getBlock(), 1, iblockstate.getBlock().getMetaFromState(iblockstate)), 0.0F);
				}
				setHeldBlockState((IBlockState)null);
			}else if (!isWild() && this.isOnSameTeam(player) && !this.isChild() && !player.isSneaking() && !this.world.isRemote)
				{
					player.startRiding(this);
				}
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
						setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * (this.andr ? 15F : 1.5F));
						super.travel(strafe, vertical, forward);
					}
					else if ((entitylivingbase instanceof EntityPlayer))
					{
						this.motionX = 0.0D;
						this.motionY = 0.0D;
						this.motionZ = 0.0D;
					}

					if (this.andr)
					this.jumpMovementFactor = 1F;
					else
					this.jumpMovementFactor = 0.02F;
					
					if (this.jumpPower > 0.0F && this.onGround)
					{
						this.motionY = (double)this.jumpPower * (this.andr ? 25 : 1) * this.getFittness();
						
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
			/**
			* Drop the equipment for this entity.
			*/
			protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier)
			{
				super.dropEquipment(wasRecentlyHit, lootingModifier);
				IBlockState iblockstate = this.getHeldBlockState();
				
				if (iblockstate != null)
				{
					Item item = Item.getItemFromBlock(iblockstate.getBlock());
					int i = item.getHasSubtypes() ? iblockstate.getBlock().getMetaFromState(iblockstate) : 0;
					this.entityDropItem(new ItemStack(item, 1, i), 0.0F);
				}
			}

			@Nullable
			protected ResourceLocation getLootTable()
			{
				return ELoot.ENTITIES_ENDERMAN;
			}
			public void setHeldBlockState(IBlockState state)
			{
				this.dataManager.set(CARRIED_BLOCK, Optional.fromNullable(state));
			}
			public IBlockState getHeldBlockState()
			{
				return (IBlockState)((Optional)this.dataManager.get(CARRIED_BLOCK)).orNull();
			}
			public boolean takesFallDamage()
			{
				return false;
			}
			public boolean attackEntityFrom(DamageSource source, float amount)
			{
				Entity entity = source.getTrueSource();
				
				if (this.canDodgeAllAttacks() && this.getEnergy() > 0F)
				{
					this.teleportRandomly();
					this.motionX = this.motionY = this.motionZ = 0D;
					this.setEnergy(this.getEnergy() - 3F);
					if (entity instanceof EntityLivingBase && !this.isOnSameTeam((EntityLivingBase)entity))
					this.attackEntityAsMob((EntityLivingBase)entity);
					return false;
				}
				if (this.andr && source instanceof EntityDamageSourceIndirect)
				{
					return false;
				}

				
				if (entity instanceof EntityLivingBase && this.andr && amount < 50F)
				{
					EntityLivingBase creature = (EntityLivingBase)entity;
					creature.attackEntityFrom(DamageSource.GENERIC.setDamageBypassesArmor().setDamageAllowedInCreativeMode().setDamageIsAbsolute(), amount);
					creature.knockBack(this, amount * 0.1F, -MathHelper.sin(creature.rotationYawHead * 0.017453292F), MathHelper.cos(creature.rotationYawHead * 0.017453292F));
					
}
					
					if (this.andr && (source.isFireDamage() || source.isExplosion() || source.isProjectile() || source.isMagicDamage() || amount < 50F))
					{
						return false;
					}

					if (amount >= 1.0F && this.andr)
					{
						amount *= 0.0001F;
					}

					this.setSitResting(false);
					
					if (this.isEntityAlive() && !this.andr && ((isEntityInvulnerable(source)) || ((source.getTrueSource() instanceof EntityCreeper)) || ((source instanceof EntityDamageSourceIndirect)) || (source.isExplosion()) || (source.isProjectile())))
					{
						if (this.teleportRandomly())
						{
							return true;
						}

						return false;
					}
					boolean flag = super.attackEntityFrom(source, amount);
					if (this.isEntityAlive() && source.isUnblockable() && !this.andr)
					{
						teleportRandomly();
					}
					return flag;
				}
				public static void setCarriable(Block block, boolean canCarry)
				{
					if (canCarry) carriableBlocks.add(block); else
					carriableBlocks.remove(block);
				}
				public static boolean getCarriable(Block block)
				{
					return carriableBlocks.contains(block);
				}
				static
				{
					carriableBlocks.add(Blocks.GRASS);
					carriableBlocks.add(Blocks.DIRT);
					carriableBlocks.add(Blocks.SAND);
					carriableBlocks.add(Blocks.SANDSTONE);
					carriableBlocks.add(Blocks.RED_SANDSTONE);
					carriableBlocks.add(Blocks.SANDSTONE);
					carriableBlocks.add(Blocks.STAINED_HARDENED_CLAY);
					carriableBlocks.add(Blocks.HARDENED_CLAY);
					carriableBlocks.add(Blocks.HAY_BLOCK);
					carriableBlocks.add(Blocks.GRAVEL);
					carriableBlocks.add(Blocks.RED_FLOWER);
					carriableBlocks.add(Blocks.YELLOW_FLOWER);
					carriableBlocks.add(Blocks.BROWN_MUSHROOM);
					carriableBlocks.add(Blocks.RED_MUSHROOM);
					carriableBlocks.add(Blocks.PLANKS);
					carriableBlocks.add(Blocks.LOG);
					carriableBlocks.add(Blocks.LOG2);
					carriableBlocks.add(Blocks.CHORUS_FLOWER);
					carriableBlocks.add(Blocks.CHORUS_PLANT);
					carriableBlocks.add(Blocks.TNT);
					carriableBlocks.add(Blocks.CACTUS);
					carriableBlocks.add(Blocks.CLAY);
					carriableBlocks.add(Blocks.PUMPKIN);
					carriableBlocks.add(Blocks.MELON_BLOCK);
					carriableBlocks.add(Blocks.BONE_BLOCK);
					carriableBlocks.add(Blocks.NETHER_WART_BLOCK);
					carriableBlocks.add(Blocks.NETHER_BRICK);
					carriableBlocks.add(Blocks.MAGMA);
					carriableBlocks.add(Blocks.MYCELIUM);
					carriableBlocks.add(Blocks.NETHERRACK);
					carriableBlocks.add(Blocks.TRAPPED_CHEST);
					carriableBlocks.add(Blocks.TRIPWIRE_HOOK);
					carriableBlocks.add(Blocks.TRIPWIRE);
					carriableBlocks.add(Blocks.CARPET);
					carriableBlocks.add(Blocks.ICE);
					carriableBlocks.add(Blocks.FROSTED_ICE);
					carriableBlocks.add(Blocks.DISPENSER);
					carriableBlocks.add(Blocks.RED_NETHER_BRICK);
					carriableBlocks.add(Blocks.GLASS);
					carriableBlocks.add(Blocks.GLASS_PANE);
					carriableBlocks.add(Blocks.GLOWSTONE);
					carriableBlocks.add(Blocks.SOUL_SAND);
					carriableBlocks.add(Blocks.LEAVES);
					carriableBlocks.add(Blocks.LEAVES2);
					carriableBlocks.add(Blocks.PACKED_ICE);
					carriableBlocks.add(Blocks.COAL_ORE);
					carriableBlocks.add(Blocks.IRON_ORE);
					carriableBlocks.add(Blocks.LAPIS_ORE);
					carriableBlocks.add(Blocks.REDSTONE_ORE);
					carriableBlocks.add(Blocks.GOLD_ORE);
					carriableBlocks.add(Blocks.DIAMOND_ORE);
					carriableBlocks.add(Blocks.EMERALD_ORE);
				}
				static class AIPlaceBlock extends EntityAIBase
				{
					private final EntityEnderman enderman;
					public AIPlaceBlock(EntityEnderman p_i45843_1_)
					{
						this.enderman = p_i45843_1_;
					}
					public boolean shouldExecute()
					{
						return this.enderman.getHeldBlockState() != null && (!this.enderman.world.getGameRules().getBoolean("mobGriefing") || this.enderman.getRNG().nextInt(2000) == 0 || this.enderman.isRiding());
					}
					public void updateTask()
					{
						Random random = this.enderman.getRNG();
						World world = this.enderman.world;
						int i = MathHelper.floor(this.enderman.posX - 1.0D + random.nextDouble() * 2.0D);
						int j = MathHelper.floor(this.enderman.posY + random.nextDouble() * 2.0D);
						int k = MathHelper.floor(this.enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
						BlockPos blockpos = new BlockPos(i, j, k);
						IBlockState iblockstate = world.getBlockState(blockpos);
						IBlockState iblockstate1 = world.getBlockState(blockpos.down());
						IBlockState iblockstate2 = this.enderman.getHeldBlockState();
						if ((iblockstate2 != null) && (func_188518_a(world, blockpos, iblockstate2.getBlock(), iblockstate, iblockstate1)))
						{
							this.enderman.world.playEvent(2001, blockpos, Block.getIdFromBlock(iblockstate2.getBlock()));
							world.setBlockState(blockpos, iblockstate2, 3);
							this.enderman.setHeldBlockState((IBlockState)null);
						}
					}
					private boolean func_188518_a(World p_188518_1_, BlockPos p_188518_2_, Block p_188518_3_, IBlockState p_188518_4_, IBlockState p_188518_5_)
					{
						return p_188518_5_.getMaterial() == Material.AIR ? false : p_188518_4_.getMaterial() != Material.AIR ? false : !p_188518_3_.canPlaceBlockAt(p_188518_1_, p_188518_2_) ? false : p_188518_5_.isFullCube();
					}
				}
				static class AITakeBlock extends EntityAIBase
				{
					private final EntityEnderman enderman;
					public AITakeBlock(EntityEnderman p_i45841_1_)
					{
						this.enderman = p_i45841_1_;
					}
					public boolean shouldExecute()
					{
						return this.enderman.getHeldBlockState() == null && !this.enderman.isBeingRidden() && (this.enderman.world.getGameRules().getBoolean("mobGriefing"));
					}
					public void updateTask()
					{
						Random random = this.enderman.getRNG();
						World world = this.enderman.world;
						int i = MathHelper.floor(this.enderman.posX - 3.0D + random.nextDouble() * 6.0D);
						int j = MathHelper.floor(this.enderman.posY - 1.0D + random.nextDouble() * 4.0D);
						int k = MathHelper.floor(this.enderman.posZ - 3.0D + random.nextDouble() * 6.0D);
						BlockPos blockpos = new BlockPos(i, j, k);
						IBlockState iblockstate = world.getBlockState(blockpos);
						Block block = iblockstate.getBlock();
						RayTraceResult raytraceresult = world.rayTraceBlocks(new Vec3d(MathHelper.floor(this.enderman.posX) + 0.5F, j + 0.5F, MathHelper.floor(this.enderman.posZ) + 0.5F), new Vec3d(i + 0.5F, j + 0.5F, k + 0.5F), false, true, false);
						boolean flag = (raytraceresult != null) && (raytraceresult.getBlockPos().equals(blockpos));
						if ((EntityEnderman.carriableBlocks.contains(block)) && (flag))
						{
							this.enderman.swingArm(EnumHand.MAIN_HAND);
							this.enderman.swingArm(EnumHand.OFF_HAND);
							this.enderman.getLookHelper().setLookPosition(i, j, k, 180F, 40F);
							this.enderman.world.playEvent(1021, blockpos, 0);
							this.enderman.world.playEvent(2001, blockpos, Block.getIdFromBlock(block));
							this.enderman.setHeldBlockState(iblockstate);
							world.setBlockToAir(blockpos);
						}
					}
				}

			}

			
			