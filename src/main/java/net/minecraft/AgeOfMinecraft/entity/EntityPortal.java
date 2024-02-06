package net.minecraft.AgeOfMinecraft.entity;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityChicken;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityCow;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityMooshroom;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityOcelot;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityParrot;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityRabbit;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntitySheep;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityEndermite;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityLlama;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySnowman;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySquid;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityWolf;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityBlaze;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityCaveSpider;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPigZombie;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityVindicator;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityWitch;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityElderGuardian;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEvoker;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityIllusioner;
import net.minecraft.AgeOfMinecraft.registry.EItem;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityPortal extends EntityFriendlyCreature implements IEntityMultiPart, Massive, Armored, Structure
{
	private static final DataParameter<Integer> TOWER1_TARGET = EntityDataManager.<Integer>createKey(EntityPortal.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TOWER2_TARGET = EntityDataManager.<Integer>createKey(EntityPortal.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TOWER3_TARGET = EntityDataManager.<Integer>createKey(EntityPortal.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TOWER4_TARGET = EntityDataManager.<Integer>createKey(EntityPortal.class, DataSerializers.VARINT);
	private static final DataParameter<Integer>[] TARGETS = new DataParameter[] {TOWER1_TARGET, TOWER2_TARGET, TOWER3_TARGET, TOWER4_TARGET};
private static final DataParameter<Integer> METADATA = EntityDataManager.createKey(EntityPortal.class, DataSerializers.VARINT);
private int[] towerUpdate = new int[4];
public double targetX;
public double targetY;
public double targetZ;
public MultiPartEntityPart[] partArray;
public MultiPartEntityPart portal = new MultiPartEntityPart(this, "portal", 4.0F, 1.0F);
public MultiPartEntityPart tower1 = new MultiPartEntityPart(this, "tower1", 1.0F, 4.0F);
public MultiPartEntityPart tower2 = new MultiPartEntityPart(this, "tower2", 1.0F, 4.0F);
public MultiPartEntityPart tower3 = new MultiPartEntityPart(this, "tower3", 1.0F, 4.0F);
public MultiPartEntityPart tower4 = new MultiPartEntityPart(this, "tower4", 1.0F, 4.0F);
public MultiPartEntityPart side1 = new MultiPartEntityPart(this, "side1", 1.0F, 1.0F);
public MultiPartEntityPart side2 = new MultiPartEntityPart(this, "side2", 1.0F, 1.0F);
public MultiPartEntityPart side3 = new MultiPartEntityPart(this, "side3", 1.0F, 1.0F);
public MultiPartEntityPart side4 = new MultiPartEntityPart(this, "side4", 1.0F, 1.0F);
public EntityPortal(World worldIn)
{
	super(worldIn);
	this.partArray = new MultiPartEntityPart[]{
		this.portal,this.tower1,this.tower2,this.tower3,this.tower4,this.side1,this.side2,this.side3,this.side4
	};
	setSize(4F, 1F);
	this.reachWidth = 6F;
	this.isImmuneToFire = true;
	playSound(ESound.portalMake, 100.0F, 1.0F);
	playSound(ESound.portalAmbient, 5.0F, 1.0F);
	this.experienceValue = 18000;
	this.setLevel(300);
	this.setLocationAndAngles((int)this.posX, (int)this.posY, (int)this.posZ, 0.0F, -90.0F);
}
public int getMetaData()
{
	return ((Integer)this.dataManager.get(METADATA)).intValue();
}

public void setMetaData(int p_82215_1_)
{
	this.dataManager.set(METADATA, Integer.valueOf(p_82215_1_));
}
public int getSpawnTimer()
{
	return 80;
}

public boolean canWearEasterEggs()
{
	return false;
}
/**
* Get this Entity's EnumCreatureAttribute
*/
public EnumCreatureAttribute getCreatureAttribute()
{
	return ESetup.CONSTRUCT;
}
protected void entityInit()
{
	super.entityInit();
	this.dataManager.register(METADATA, Integer.valueOf(0));
	this.dataManager.register(TOWER1_TARGET, Integer.valueOf(0));
	this.dataManager.register(TOWER2_TARGET, Integer.valueOf(0));
	this.dataManager.register(TOWER3_TARGET, Integer.valueOf(0));
	this.dataManager.register(TOWER4_TARGET, Integer.valueOf(0));
}
public int getWatchedTargetId(int p_82203_1_)
{
	return ((Integer)this.dataManager.get(TARGETS[p_82203_1_])).intValue();
}

public void updateWatchedTargetId(int targetOffset, int newId)
{
	this.dataManager.set(TARGETS[targetOffset], Integer.valueOf(newId));
}
protected void applyEntityAttributes()
{
	super.applyEntityAttributes();
	getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000.0D);
	getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(96.0D);
	getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
	getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20D);
	getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(30.0D);
	getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(20.0D);
}
public void writeEntityToNBT(NBTTagCompound tagCompound)
{
	super.writeEntityToNBT(tagCompound);
	tagCompound.setInteger("MetaData", getMetaData());
}

public void readEntityFromNBT(NBTTagCompound tagCompund)
{
	super.readEntityFromNBT(tagCompund);
	setMetaData(tagCompund.getInteger("MetaData"));
}

public boolean canUseGuardBlock()
{
	return false;
}
public boolean isChild()
{
	return false;
}
public void setChild(boolean childZombie) { }

public double getDefaultStrengthStat()
{
	return 100F;
}

public double getDefaultStaminaStat()
{
	return 100F;
}

public double getDefaultIntelligenceStat()
{
	return 0F;
}

public double getDefaultDexterityStat()
{
	return 0F;
}

public double getDefaultAgilityStat()
{
	return 0F;
}

public double getDefaultFittnessStat()
{
	return 1F;
}

public boolean isEntityImmuneToCoralium()
{
	return true;
}

public boolean isEntityImmuneToDread()
{
	return true;
}

public boolean isEntityImmuneToAntiMatter()
{
	return true;
}

public boolean isEntityImmuneToDarkness()
{
	return true;
}

public void onKillEntity(EntityLivingBase entityLivingIn)
{
	super.onKillEntity(entityLivingIn);
	EntityLightningBolt shot = new EntityLightningBolt(this.world, entityLivingIn.posX - 0.5D, entityLivingIn.posY, entityLivingIn.posZ - 0.5D, true);
	float f = -(float)(MathHelper.atan2(shot.posZ - this.posZ, shot.posZ - this.posZ) * (180D / Math.PI)) - 90.0F;
	shot.rotationYaw = f;
	this.world.addWeatherEffect(shot);
	entityLivingIn.onStruckByLightning(shot);
	entityLivingIn.motionY += 4D;
	if (entityLivingIn instanceof net.minecraft.entity.monster.EntityCreeper || entityLivingIn instanceof net.minecraft.entity.monster.EntityZombie || entityLivingIn instanceof AbstractSkeleton)
	{
		net.minecraft.entity.monster.EntityCreeper creeper = new net.minecraft.entity.monster.EntityCreeper(world);
		if (!this.world.isRemote)
		world.spawnEntity(creeper);
		creeper.copyLocationAndAnglesFrom(entityLivingIn);
		creeper.onStruckByLightning(shot);
		entityLivingIn.onDeath(DamageSource.causeMobDamage(creeper).setDamageBypassesArmor());
		creeper.setDead();
		entityLivingIn.motionX = 0D;
		entityLivingIn.motionZ = 0D;
		entityLivingIn.knockBack(shot, 2F, MathHelper.sin(shot.rotationYaw * 0.017453292F), -MathHelper.cos(shot.rotationYaw * 0.017453292F));
		entityLivingIn.motionY = 0D;
		if (entityLivingIn.isAirBorne)
		entityLivingIn.motionY += rand.nextDouble() * 1.5D;
		else
		entityLivingIn.motionY += rand.nextDouble() * 3D;
	}

	if (entityLivingIn.height < 2 && entityLivingIn.width < 2 && entityLivingIn.isNonBoss())
	{
		entityLivingIn.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2.0F, 2.0F);
		entityLivingIn.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 2.0F, 2.0F);
		entityLivingIn.playSound(SoundEvents.BLOCK_LAVA_POP, 2.0F, 2.0F);
		entityLivingIn.world.setEntityState(entityLivingIn, (byte)20);
		entityLivingIn.setDead();
	}
}

public boolean takesFallDamage()
{
	return false;
}

public void fall(float distance, float damageMultiplier)
{
	int i = MathHelper.ceil((distance - 6F) * damageMultiplier);
	
	if (i > 0)
	{
		playSound(ESound.golemSmash, 10.0F, 0.75F);
		playSound(ESound.golemSmash, 10.0F, 0.5F);
		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4.0D + i, 2.0D, 4.0D + i), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
		if ((list != null) && (!list.isEmpty()))
		{
			for (int i1 = 0; i1 < list.size(); i1++)
			{
				EntityLivingBase entity = (EntityLivingBase)list.get(i1);
				if (entity != null)
				{
					if (!isOnSameTeam(entity))
					{
						entity.motionY += 1D + (i * 0.05D);
						entity.attackEntityFrom(DamageSource.causeExplosionDamage((Explosion)null), 5F + i);
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

public boolean attackEntityAsMob(Entity entityIn)
{
	super.attackEntityAsMob(entityIn);
	//this.playSound(ModSoundEvents.lightningShot, 10F, 1.0F);
	if (entityIn instanceof EntityLivingBase)
	{
		++entityIn.motionY;
		if (!entityIn.isEntityAlive() && !entityIn.isDead)
		this.onKillEntity((EntityLivingBase) entityIn);
		((EntityLivingBase)entityIn).knockBack(this, 1F, MathHelper.sin(entityIn.rotationYaw * 0.017453292F), -MathHelper.cos(entityIn.rotationYaw * 0.017453292F));
		if (!(entityIn instanceof EntityFriendlyCreature))
		entityIn.motionY += rand.nextDouble();
	}
	entityIn.setFire(100);
	if (entityIn instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entityIn).getFakeHealth() > 0F)
	{
		entityIn.motionY = 0D;
		entityIn.extinguish();
	}
	if (entityIn instanceof EntityLivingBase && entityIn instanceof IEntityMultiPart)
	{
		((EntityLivingBase)entityIn).motionX = 0.0D;
		((EntityLivingBase)entityIn).motionY = 0.0D;
		((EntityLivingBase)entityIn).motionZ = 0.0D;
	}

	return true;
}

private double func_82214_u(int p_82214_1_)
{
	switch (p_82214_1_)
	{
		case 0:return this.posX + 3.25D;
		case 1:return this.posX + 3.25D;
		case 2:
		return this.posX - 3.25D;
		case 3:return this.posX - 3.25D;
	}
	return this.posX;
}
private double func_82208_v(int p_82208_1_)
{
	return this.posY + 3.5D;
}

private double func_82213_w(int p_82213_1_)
{
	switch (p_82213_1_)
	{
		case 0:return this.posZ + 3.25D;
		case 1:return this.posZ - 3.25D;
		case 2:
		return this.posZ + 3.25D;
		case 3:return this.posZ - 3.25D;
	}
	return this.posZ;
}

public void onLivingUpdate()
{this.setSize(4F, 1F);
if (!this.world.isRemote && this.world.getGameRules().getBoolean("mobGriefing"))
{
	int x = MathHelper.floor(this.posX);
	int y = MathHelper.floor(this.posY);
	int z = MathHelper.floor(this.posZ);
	for (int x1 = -3; x1 <= 3; x1++)
	{
		for (int z1 = -3; z1 <= 3; z1++)
		{
			for (int y1 = 0; y1 <= 3; y1++)
			{
				BlockPos blockpos = new BlockPos(x + x1, y + y1, z + z1);
				IBlockState iblockstate = this.world.getBlockState(blockpos);
				Block block = iblockstate.getBlock();
				if (!block.isAir(iblockstate, this.world, blockpos) && block.getBlockHardness(iblockstate, world, blockpos) >= 0)
				{
					this.world.destroyBlock(blockpos, true);
				}
			}
		}
	}
}

if (!this.world.isRemote && !this.isWild() && this.getOwner() instanceof EntityPlayer && this.getMetaData() >= 4)
{
}
float f = this.getJukeboxToDanceTo() != null ? MathHelper.cos((float)this.ticksExisted * 0.25F) * 0.25F : MathHelper.cos((float)this.ticksExisted * 0.05F) * 0.25F;
this.portal.onUpdate();
this.portal.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
this.tower1.onUpdate();
this.tower1.setLocationAndAngles(this.posX + 2.75D + f, this.posY + 0.325D, this.posZ + 2.75D + f, 0.0F, 0.0F);
this.tower2.onUpdate();
this.tower2.setLocationAndAngles(this.posX + 2.75D + f, this.posY + 0.325D, this.posZ - 2.75D - f, 0.0F, 0.0F);
this.tower3.onUpdate();
this.tower3.setLocationAndAngles(this.posX - 2.75D - f, this.posY + 0.325D, this.posZ - 2.75D - f, 0.0F, 0.0F);
this.tower4.onUpdate();
this.tower4.setLocationAndAngles(this.posX - 2.75D - f, this.posY + 0.325D, this.posZ + 2.75D + f, 0.0F, 0.0F);
this.side1.onUpdate();
this.side1.setLocationAndAngles(this.posX + 2.5D, this.posY + 0.5D, this.posZ, 0.0F, 0.0F);
this.side1.setEntityBoundingBox(new AxisAlignedBB(this.side1.posX - 0.5D, this.side1.posY, this.side1.posZ - 2D, this.side1.posX + 0.5D, this.side1.posY + 1.0D, this.side1.posZ + 2D));
this.side2.onUpdate();
this.side2.setLocationAndAngles(this.posX - 2.5D, this.posY + 0.5D, this.posZ, 0.0F, 0.0F);
this.side2.setEntityBoundingBox(new AxisAlignedBB(this.side2.posX - 0.5D, this.side2.posY, this.side2.posZ - 2D, this.side2.posX + 0.5D, this.side2.posY + 1.0D, this.side2.posZ + 2D));
this.side3.onUpdate();
this.side3.setLocationAndAngles(this.posX, this.posY + 0.5D, this.posZ + 2.5D, 0.0F, 0.0F);
this.side3.setEntityBoundingBox(new AxisAlignedBB(this.side3.posX - 2D, this.side3.posY, this.side3.posZ - 0.5D, this.side3.posX + 2D, this.side3.posY + 1.0D, this.side3.posZ + 0.5D));
this.side4.onUpdate();
this.side4.setLocationAndAngles(this.posX, this.posY + 0.5D, this.posZ - 2.5D, 0.0F, 0.0F);
this.side4.setEntityBoundingBox(new AxisAlignedBB(this.side4.posX - 2D, this.side4.posY, this.side4.posZ - 0.5D, this.side4.posX + 2D, this.side4.posY + 1.0D, this.side4.posZ + 0.5D));

if (!this.world.isRemote)
{
	for (int i = 0; i < 4; i++)
	{
		if (this.getRevengeTarget() != null)
		{
			updateWatchedTargetId(i, this.getRevengeTarget().getEntityId());
		}

		if (this.ticksExisted > 80 && this.isEntityAlive() && this.ticksExisted >= this.towerUpdate[(i)])
		{
			int i1 = getWatchedTargetId(i);
			if (i1 > 0)
			{
				Entity entity = this.world.getEntityByID(i1);
				if (entity != null && entity.isEntityAlive() && this.getDistance(entity) <= getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue() && this.canEntityBeSeen(entity))
				{
					if (this.isOnSameTeam(entity))
					updateWatchedTargetId(i, 0);
					double d1 = func_82214_u(i);
					double d2 = func_82208_v(i);
					double d3 = func_82213_w(i);
					this.fireLightning(entity, d1, d2, d3);
					if (this.moralRaisedTimer > 200)
					this.towerUpdate[(i)] = (this.ticksExisted + 1);
					else if (this.getMetaData() > 1)
					this.towerUpdate[(i)] = (this.ticksExisted + 30);
					else
					this.towerUpdate[(i)] = (this.ticksExisted + 60);
				}
				else {
					updateWatchedTargetId(i, 0);
				}
			}
			else
			{
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
				for (int k1 = 0; (k1 < 10) && (!list.isEmpty()); k1++)
				{
					EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));
					if (entitylivingbase != this && entitylivingbase.isEntityAlive() && canEntityBeSeen(entitylivingbase) && !this.isOnSameTeam(entitylivingbase))
					{
						if ((entitylivingbase instanceof EntityPlayer))
						{
							if (!((EntityPlayer)entitylivingbase).capabilities.disableDamage)
							{
								updateWatchedTargetId(i, entitylivingbase.getEntityId());
							}
						}
						else {
							updateWatchedTargetId(i, entitylivingbase.getEntityId());
						}
					}
					else
					{
						list.remove(entitylivingbase);
					}
				}
			}
		}
	}
}
this.experienceValue = 6000 * (1 + this.getMetaData());
if (this.getMetaData() > 0)
{
	addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, Integer.MAX_VALUE, this.getMetaData() > 2 ? 1 : 0));
	addPotionEffect(new PotionEffect(MobEffects.STRENGTH, Integer.MAX_VALUE, this.getMetaData() > 2 ? 1 : 0));
}
List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(1D, 4D, 1D).offset(0D, 2D, 0D), EntitySelectors.IS_ALIVE);

if (!list.isEmpty())
{
	for (int l = 0; l < list.size(); ++l)
	{
		Entity[] aentity = this.getParts();
		
		if (aentity != null)
		{
			for (Entity part : aentity)
			{
				List<Entity> partlist = this.world.getEntitiesInAABBexcluding(this, part.getEntityBoundingBox(), EntitySelectors.IS_ALIVE);
				
				if (!partlist.isEmpty())
				{
					for (int l1 = 0; l1 < partlist.size(); ++l1)
					{
						Entity entity = (Entity)partlist.get(l1);
						if (entity instanceof EntityLivingBase && !entity.noClip && !(entity instanceof IEntityMultiPart))
						{
							if (entity.collided)
							entity.attackEntityFrom(DamageSource.IN_WALL, 1F);
							part.applyEntityCollision(entity);
							entity.applyEntityCollision(part);
							entity.motionY += 0.1D;
						}
					}
				}
			}
		}
	}
}
this.motionX = 0.0D;
this.motionZ = 0.0D;
if (this.motionY > 0.0D)
this.motionY = 0.0D;
super.onLivingUpdate();
this.prevRenderYawOffset = (this.renderYawOffset = this.prevRotationYaw = this.rotationYaw = this.prevRotationYawHead = this.rotationYawHead = 0.0F);
this.prevRotationPitch = (this.rotationPitch = 90.0F);
this.isAirBorne = false;
this.onGround = true;
setSprinting(false);
if (this.world.isRemote)
{
	if (this.isEntityAlive())
	for (int i = 0; i < 3 && this.ticksExisted > 60; i++)
	{
		if (this.towerUpdate[i] > 20)
		world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.func_82214_u(i), this.func_82208_v(i), this.func_82208_v(i), (double)((float)this.func_82214_u(i) + rand.nextFloat()) - 0.5D, (double)((float)this.func_82208_v(i) - rand.nextFloat() - 1.0F), (double)((float)this.func_82208_v(i) + rand.nextFloat()) - 0.5D, new int[0]);
		
		if (this.getMetaData() > 0)
		this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + 1D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 1D, 0.0D, 0.0D, new int[0]);
		if (this.getMetaData() > 1)
		{
			this.world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, true, this.posX + 3.25D + f + (this.rand.nextDouble() - 0.5D), this.posY + 4.0D + (this.rand.nextDouble() - 0.5D), this.posZ + 3.25D + f + (this.rand.nextDouble() - 0.5D), f * 0.1D, 0.02D, f * 0.1D, new int[0]);
			this.world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, true, this.posX + 3.25D + f + (this.rand.nextDouble() - 0.5D), this.posY + 4.0D + (this.rand.nextDouble() - 0.5D), this.posZ - 3.25D - f + (this.rand.nextDouble() - 0.5D), f * 0.1D, 0.02D, f * 0.1D, new int[0]);
			this.world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, true, this.posX - 3.25D - f + (this.rand.nextDouble() - 0.5D), this.posY + 4.0D + (this.rand.nextDouble() - 0.5D), this.posZ - 3.25D - f + (this.rand.nextDouble() - 0.5D), f * 0.1D, 0.02D, f * 0.1D, new int[0]);
			this.world.spawnParticle(EnumParticleTypes.SPELL_INSTANT, true, this.posX - 3.25D - f + (this.rand.nextDouble() - 0.5D), this.posY + 4.0D + (this.rand.nextDouble() - 0.5D), this.posZ + 3.25D + f + (this.rand.nextDouble() - 0.5D), f * 0.1D, 0.02D, f * 0.1D, new int[0]);
		}
		if (this.getMetaData() > 2)
		this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + 1.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 1D, 0.0D, 0.5D, new int[0]);
		if (this.getMetaData() > 3)
		{
			this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + 0.8D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.05D, 0.0D, new int[0]);
			this.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + 0.8D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.05D, 0.0D, new int[0]);
			this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + 0.8D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.05D, 0.0D, new int[0]);
		}
		this.world.spawnParticle(EnumParticleTypes.TOWN_AURA, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + 1.0D + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D + (this.rand.nextDouble() * 0.1D - 0.05D), 0.0D + this.rand.nextDouble() * 0.2D, 0.0D + (this.rand.nextDouble() * 0.1D - 0.05D), new int[0]);
		this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + 0.8D + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.5D, 0.0D, new int[0]);
		this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + 0.8D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.05D, 0.0D, new int[0]);
		this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + 0.8D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.05D, 0.0D, new int[0]);
		this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, this.posX + 3.25D + f + (this.rand.nextDouble() - 0.5D), this.posY + 4.0D + (this.rand.nextDouble() - 0.5D), this.posZ + 3.25D + f + (this.rand.nextDouble() - 0.5D), f * 0.1D, 0.02D, f * 0.1D, new int[0]);
		this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, this.posX + 3.25D + f + (this.rand.nextDouble() - 0.5D), this.posY + 4.0D + (this.rand.nextDouble() - 0.5D), this.posZ - 3.25D - f + (this.rand.nextDouble() - 0.5D), f * 0.1D, 0.02D, f * 0.1D, new int[0]);
		this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, this.posX - 3.25D - f + (this.rand.nextDouble() - 0.5D), this.posY + 4.0D + (this.rand.nextDouble() - 0.5D), this.posZ - 3.25D - f + (this.rand.nextDouble() - 0.5D), f * 0.1D, 0.02D, f * 0.1D, new int[0]);
		this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, this.posX - 3.25D - f + (this.rand.nextDouble() - 0.5D), this.posY + 4.0D + (this.rand.nextDouble() - 0.5D), this.posZ + 3.25D + f + (this.rand.nextDouble() - 0.5D), f * 0.1D, 0.02D, f * 0.1D, new int[0]);
	}
}
if ((this.ticksExisted + this.getEntityId()) % (this.getJukeboxToDanceTo() != null ? 20 : 200) == 0)
{
	playSound(ESound.portalWhoosh, 5.0F, 1.0F);
}
if ((this.ticksExisted + this.getEntityId()) % 670 == 0)
{
	playSound(ESound.portalAmbient, 5.0F, 1.0F);
}
if ((this.ticksExisted + this.getEntityId()) > 60 && ((this.ticksExisted + this.getEntityId()) % (this.getJukeboxToDanceTo() != null ? 20 : (this.getMetaData() > 2 ? 50 : 100)) == 0) && (this.rand.nextInt((this.getJukeboxToDanceTo() != null ? 5 : 10)) == 0))
{
	playSound(ESound.portalWhoosh, 10.0F, getSoundPitch() + 1.9F);
	int i = 0;
	if (this.rand.nextInt(2) == 0)
	{
		i++;
	}
	if (this.rand.nextInt(3) == 0)
	{
		i++;
	}
	if (this.rand.nextInt(6) == 0)
	{
		i++;
	}
	if (this.rand.nextInt(12) == 0)
	{
		i++;
	}
	switch (i)
	{
		case 0:switch (this.rand.nextInt(9))
		{
			case 0:net.minecraft.AgeOfMinecraft.entity.tier1.EntityBat bat = new net.minecraft.AgeOfMinecraft.entity.tier1.EntityBat(this.world);
			bat.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
			if (!isWild())
			bat.setOwnerId(getOwnerId());
			bat.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(bat)), null);
			if (!this.world.isRemote)
			this.world.spawnEntity(bat);
			break;
			case 1:EntityChicken chicken = new EntityChicken(this.world);
			chicken.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
			if (!isWild())
			chicken.setOwnerId(getOwnerId());
			chicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(chicken)), null);
			if (!this.world.isRemote)
			this.world.spawnEntity(chicken);
			break;
			case 2:EntityCow cow = new EntityCow(this.world);
			cow.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
			if (!isWild())
			cow.setOwnerId(getOwnerId());
			cow.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(cow)), null);
			if (!this.world.isRemote)
			this.world.spawnEntity(cow);
			break;
			case 3:EntityMooshroom mooshroom = new EntityMooshroom(this.world);
			mooshroom.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
			if (!isWild())
			mooshroom.setOwnerId(getOwnerId());
			mooshroom.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(mooshroom)), null);
			if (!this.world.isRemote)
			this.world.spawnEntity(mooshroom);
			break;
			case 4:EntityOcelot ocelot = new EntityOcelot(this.world);
			ocelot.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
			if (!isWild())
			ocelot.setOwnerId(getOwnerId());
			ocelot.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(ocelot)), null);
			if (!this.world.isRemote)
			this.world.spawnEntity(ocelot);
			break;
			case 5:net.minecraft.AgeOfMinecraft.entity.tier1.EntityPig pig = new net.minecraft.AgeOfMinecraft.entity.tier1.EntityPig(this.world);
			pig.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
			if (!isWild())
			pig.setOwnerId(getOwnerId());
			pig.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(pig)), null);
			if (!this.world.isRemote)
			this.world.spawnEntity(pig);
			break;
			case 6:EntityRabbit rabbit = new EntityRabbit(this.world);
			rabbit.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
			if (!isWild())
			rabbit.setOwnerId(getOwnerId());
			rabbit.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(rabbit)), null);
			if (!this.world.isRemote)
			this.world.spawnEntity(rabbit);
			break;
			case 7:EntitySheep sheep = new EntitySheep(this.world);
			sheep.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
			if (!isWild())
			sheep.setOwnerId(getOwnerId());
			sheep.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(sheep)), null);
			if (!this.world.isRemote)
			this.world.spawnEntity(sheep);
			break;
			case 8:EntityParrot parrot = new EntityParrot(this.world);
			parrot.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
			if (!isWild())
			parrot.setOwnerId(getOwnerId());
			parrot.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(parrot)), null);
			if (!this.world.isRemote)
			this.world.spawnEntity(parrot);
			break; }
			break;
			case 1:switch (this.rand.nextInt(7))
			{
				case 0:EntityEndermite endermite = new EntityEndermite(this.world);
				endermite.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
				if (!isWild())
				endermite.setOwnerId(getOwnerId());
				endermite.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(endermite)), null);
				if (!this.world.isRemote)
				this.world.spawnEntity(endermite);
				break;
				case 1:net.minecraft.AgeOfMinecraft.entity.tier2.EntitySilverfish silverfish = new net.minecraft.AgeOfMinecraft.entity.tier2.EntitySilverfish(this.world);
				silverfish.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
				if (!isWild())
				silverfish.setOwnerId(getOwnerId());
				silverfish.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(silverfish)), null);
				if (!this.world.isRemote)
				this.world.spawnEntity(silverfish);
				break;
				case 2:EntitySnowman snowgolem = new EntitySnowman(this.world);
				snowgolem.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
				if (!isWild())
				snowgolem.setOwnerId(getOwnerId());
				snowgolem.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(snowgolem)), null);
				if (!this.world.isRemote)
				this.world.spawnEntity(snowgolem);
				break;
				case 3:EntitySquid squid = new EntitySquid(this.world);
				squid.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
				if (!isWild())
				squid.setOwnerId(getOwnerId());
				squid.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(squid)), null);
				if (!this.world.isRemote)
				this.world.spawnEntity(squid);
				break;
				case 4:EntityVillager villager = new EntityVillager(this.world);
				villager.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
				if (!isWild())
				villager.setOwnerId(getOwnerId());
				villager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(villager)), null);
				if (!this.world.isRemote)
				this.world.spawnEntity(villager);
				break;
				case 5:EntityWolf wolf = new EntityWolf(this.world);
				wolf.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
				if (!isWild())
				wolf.setOwnerId(getOwnerId());
				wolf.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(wolf)), null);
				if (!this.world.isRemote)
				this.world.spawnEntity(wolf);
				break;
				case 6:EntityLlama llama = new EntityLlama(this.world);
				llama.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
				if (!isWild())
				llama.setOwnerId(getOwnerId());
				llama.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(llama)), null);
				if (!this.world.isRemote)
				this.world.spawnEntity(llama);
				break;}
				break;
				case 2:switch (this.rand.nextInt(7))
				{
					case 0:net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper creeper = new net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper(this.world);
					creeper.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
					if (!isWild())
					creeper.setOwnerId(getOwnerId());
					creeper.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(creeper)), null);
					if (!this.world.isRemote)
					this.world.spawnEntity(creeper);
					break;
					case 1:net.minecraft.AgeOfMinecraft.entity.tier3.EntityMagmaCube magmacube = new net.minecraft.AgeOfMinecraft.entity.tier3.EntityMagmaCube(this.world);
					magmacube.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
					if (!isWild())
					magmacube.setOwnerId(getOwnerId());
					magmacube.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(magmacube)), null);
					if (!this.world.isRemote)
					this.world.spawnEntity(magmacube);
					break;
					case 2:EntitySkeleton skeleton = new EntitySkeleton(this.world);
					skeleton.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
					if (!isWild())
					skeleton.setOwnerId(getOwnerId());
					skeleton.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(skeleton)), null);
					if (!this.world.isRemote)
					this.world.spawnEntity(skeleton);
					break;
					case 3:net.minecraft.AgeOfMinecraft.entity.tier3.EntitySlime slime = new net.minecraft.AgeOfMinecraft.entity.tier3.EntitySlime(this.world);
					slime.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
					if (!isWild())
					slime.setOwnerId(getOwnerId());
					slime.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(slime)), null);
					if (!this.world.isRemote)
					this.world.spawnEntity(slime);
					break;
					case 4:EntitySpider spider = new EntitySpider(this.world);
					spider.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
					if (!isWild())
					spider.setOwnerId(getOwnerId());
					spider.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(spider)), null);
					if (!this.world.isRemote)
					this.world.spawnEntity(spider);
					break;
					case 5:EntityZombie zombie = new EntityZombie(this.world);
					zombie.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
					if (!isWild())
					zombie.setOwnerId(getOwnerId());
					zombie.setZombieType(0);
					zombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(zombie)), null);
					if (!this.world.isRemote)
					this.world.spawnEntity(zombie);
					break;
					case 6:EntityVex vex = new EntityVex(this.world);
					vex.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
					if (!isWild())
					vex.setOwnerId(getOwnerId());
					vex.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(vex)), null);
					if (!this.world.isRemote)
					this.world.spawnEntity(vex);
					break;}
					break;
					case 3:switch (this.rand.nextInt(12))
					{
						case 0:EntityBlaze blaze = new EntityBlaze(this.world);
						blaze.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						blaze.setOwnerId(getOwnerId());
						blaze.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(blaze)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(blaze);
						break;
						case 1:EntityCaveSpider cavespider = new EntityCaveSpider(this.world);
						cavespider.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						cavespider.setOwnerId(getOwnerId());
						cavespider.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(cavespider)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(cavespider);
						break;
						case 2:net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman enderman = new net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman(this.world);
						enderman.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						enderman.setOwnerId(getOwnerId());
						enderman.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(enderman)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(enderman);
						break;
						case 3:EntityGhast ghast = new EntityGhast(this.world);
						ghast.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						ghast.setOwnerId(getOwnerId());
						ghast.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(ghast)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(ghast);
						break;
						case 4:EntityGuardian guardian = new EntityGuardian(this.world);
						guardian.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						guardian.setOwnerId(getOwnerId());
						guardian.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(guardian)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(guardian);
						break;
						case 5:EntityPigZombie pigzombie = new EntityPigZombie(this.world);
						pigzombie.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						pigzombie.setOwnerId(getOwnerId());
						pigzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(pigzombie)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(pigzombie);
						break;
						case 6:EntityRabbit killerrabbit = new EntityRabbit(this.world);
						killerrabbit.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						killerrabbit.setOwnerId(getOwnerId());
						killerrabbit.setRabbitType(99);
						killerrabbit.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(killerrabbit)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(killerrabbit);
						break;
						case 7:EntitySkeleton witherskeleton = new EntitySkeleton(this.world);
						witherskeleton.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						witherskeleton.setOwnerId(getOwnerId());
						witherskeleton.setSkeletonType(1);
						witherskeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
						if (getRNG().nextInt(2) > 0)
						{
							witherskeleton.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.STONE_SWORD));
						}
						witherskeleton.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(witherskeleton)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(witherskeleton);
						break;
						case 8:EntityShulker shulker = new EntityShulker(this.world);
						shulker.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						shulker.setOwnerId(getOwnerId());
						shulker.applyEntityCollision(this);
						shulker.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(shulker)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(shulker);
						break;
						case 9:EntityWitch witch = new EntityWitch(this.world);
						witch.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						witch.setOwnerId(getOwnerId());
						witch.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(witch)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(witch);
						break;
						case 10:EntityZombie zombie = new EntityZombie(this.world);
						zombie.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						zombie.setOwnerId(getOwnerId());
						zombie.setZombieType(1);
						zombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(zombie)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(zombie);
						break;
						case 11:EntitySkeleton stray = new EntitySkeleton(this.world);
						stray.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						stray.setOwnerId(getOwnerId());
						stray.setSkeletonType(2);
						stray.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(stray)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(stray);
						break;
						case 12:EntityVindicator vindicator = new EntityVindicator(this.world);
						vindicator.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
						if (!isWild())
						vindicator.setOwnerId(getOwnerId());
						vindicator.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(vindicator)), null);
						if (!this.world.isRemote)
						this.world.spawnEntity(vindicator);
						break;}
						break;
						case 4:switch (this.rand.nextInt(6))
						{
							case 0:EntityElderGuardian elderguardian = new EntityElderGuardian(this.world);
							elderguardian.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
							if (!isWild())
							elderguardian.setOwnerId(getOwnerId());
							elderguardian.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(elderguardian)), null);
							if (!this.world.isRemote)
							this.world.spawnEntity(elderguardian);
							break;
							case 1:net.minecraft.AgeOfMinecraft.entity.tier5.EntityGiant giant = new net.minecraft.AgeOfMinecraft.entity.tier5.EntityGiant(this.world);
							giant.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
							if (!isWild())
							giant.setOwnerId(getOwnerId());
							giant.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(giant)), null);
							if (!this.world.isRemote)
							this.world.spawnEntity(giant);
							break;
							case 2:net.minecraft.AgeOfMinecraft.entity.tier5.EntityIronGolem irongolem = new net.minecraft.AgeOfMinecraft.entity.tier5.EntityIronGolem(this.world);
							irongolem.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
							if (!isWild())
							irongolem.setOwnerId(getOwnerId());
							irongolem.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(irongolem)), null);
							if (!this.world.isRemote)
							this.world.spawnEntity(irongolem);
							break;
							case 3:net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither wither = new net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither(this.world);
							wither.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
							if (!isWild())
							wither.setOwnerId(getOwnerId());
							wither.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(wither)), null);
							if (!this.world.isRemote)
							this.world.spawnEntity(wither);
							break;
							case 4:EntityEvoker evoker = new EntityEvoker(this.world);
							evoker.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
							if (!isWild())
							evoker.setOwnerId(getOwnerId());
							evoker.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(evoker)), null);
							if (!this.world.isRemote)
							this.world.spawnEntity(evoker);
							break;
							case 5:EntityIllusioner illusioner = new EntityIllusioner(this.world);
							illusioner.setLocationAndAngles(this.posX + (this.rand.nextFloat() * 4.0F - 2.0F), this.posY + 1.5D, this.posZ + (this.rand.nextFloat() * 4.0F - 2.0F), 0.0F, 0.0F);
							if (!isWild())
							illusioner.setOwnerId(getOwnerId());
							illusioner.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(illusioner)), null);
							if (!this.world.isRemote)
							this.world.spawnEntity(illusioner);
							break;
						}
						break; }
					}
				}
				protected void onDeathUpdate()
				{
					this.deathTime += 1;
					if (this.deathTime == 1)
					{
						playSound(ESound.buildingDeath, 10.0F, 1.0F);
						if (!this.world.isRemote)
						this.entityDropItem(new ItemStack(EItem.portalStaff, 1, this.getMetaData()), 1F);
						for (int k = 0; k < 2500; k++)
						{
							double d2 = this.rand.nextGaussian() * 0.05D;
							this.rand.nextGaussian();
							double d1 = this.rand.nextGaussian() * 0.05D;
							this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height * 4.0F, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, -0.25D, d1, new int[0]);
							this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height * 4.0F, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, -0.25D, d1, new int[0]);
							this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height * 4.0F, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, 0.5D, d1, new int[0]);
						}
					}
					if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
					{
						int i = this.getExperiencePoints(this.attackingPlayer) / 60;
						i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
						while (i > 0)
						{
							int j = EntityXPOrb.getXPSplit(i);
							i -= j;
							this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
						}
					}

					if (this.deathTime == 60)
					{
						setDead();
					}
				}
				public EnumTier getTier()
				{
					return EnumTier.TIER6;
				}

				public boolean isImmuneToExplosions()
				{
					return true;
				}
				@Nullable
				protected ResourceLocation getLootTable()
				{
					return ELoot.ENTITIES_PORTAL;
				}

				protected SoundEvent getDeathSound()
				{
					return null;
				}
				protected SoundEvent getHurtSound(DamageSource source)
				{
					return null;
				}
				public void setAttackTarget(EntityLivingBase entitylivingbaseIn)
				{
					if ((isEntityAlive()) && (this.ticksExisted > 80))
					{
						super.setAttackTarget(entitylivingbaseIn);
					}
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
				public boolean processInteract(EntityPlayer player, EnumHand hand)
				{
					ItemStack stack = player.getHeldItem(hand);
					if ((this.ticksExisted > 40) && stack.isEmpty() && (!isWild()) && (player == getOwner()))
					{
						if (player.isSneaking())
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
											this.playSound(SoundEvents.ITEM_TOTEM_USE, 1.0F, 1.0F);
											entity.changeDimension(1);
										}
									}
								}
							}
							this.playSound(SoundEvents.ITEM_TOTEM_USE, 1.0F, 1.0F);
							player.changeDimension(1);
							return true;
						}
						else
						{
							player.world.playSound(player, new BlockPos(player), net.minecraft.init.SoundEvents.BLOCK_ANVIL_USE, net.minecraft.util.SoundCategory.PLAYERS, 100.0F, 0.5F);
							setHealth(0.0F);
							return true;
						}
					}
					else
					{
						return false;
					}
				}

				public boolean canBeCollidedWith()
				{
					return true;
				}

				public World getWorld()
				{
					return this.world;
				}
				public int getDamageCap()
				{
					return 100;
				}

				public boolean attackEntityFromPart(MultiPartEntityPart portalPart, DamageSource source, float damage)
				{
					if (portalPart == side1 || portalPart == side2 || portalPart == side3 || portalPart == side4)
					damage = 0F;
					
					return super.attackEntityFrom(source, damage);
				}

				public boolean attackEntityFrom(DamageSource source, float amount)
				{
					return this.attackEntityFromPart(portal, source, amount);
				}

				public Entity[] getParts()
				{
					return this.partArray;
				}
				public AxisAlignedBB getCollisionBoundingBox()
				{
					return this.getEntityBoundingBox();
				}
				public void applyEntityCollision(Entity entity)
				{
					if (entity instanceof EntityLivingBase && !(entity instanceof IEntityMultiPart) && (entity.posY + entity.getEyeHeight() <= this.posY + 1D))
					entity.attackEntityFrom(DamageSource.IN_WALL, 1F);
				}
				public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) { }
			}