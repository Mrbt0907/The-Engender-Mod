package net.minecraft.AgeOfMinecraft.entity.tier4;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAttackRangedAlly;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWitch extends EntityFriendlyCreature implements IRangedAttackMob, Light
{
	private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
	private static final AttributeModifier MODIFIER = new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0).setSaved(false);
	private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.createKey(EntityWitch.class, DataSerializers.BOOLEAN);
	private int witchAttackTimer;
	public EntityWitch(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 1.95F);
		this.isOffensive = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.2D, 32.0F, 8.0F));
		this.tasks.addTask(3, new EntityAIAttackRangedAlly(this, 1.0D, 40, 10.0F));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.experienceValue = 10;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityWitch(this.world);
	}
	protected void entityInit()
	{
		super.entityInit();
		getDataManager().register(IS_AGGRESSIVE, Boolean.valueOf(false));
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
		return EnumCreatureAttribute.ILLAGER;
	}
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_WITCH_AMBIENT;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_WITCH_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_WITCH_DEATH;
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

	protected float getSoundPitch()
	{
		return  super.getSoundPitch();
	}
	public void setAggressive(boolean aggressive)
	{
		getDataManager().set(IS_AGGRESSIVE, Boolean.valueOf(aggressive));
	}
	public boolean func_184730_o()
	{
		return ((Boolean)getDataManager().get(IS_AGGRESSIVE)).booleanValue();
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	}
	public void performSpecialAttack()
	{
		playSound(ESound.witchSpecial, 10.0F, 1.0F);
		setSpecialAttackTimer(1200);
	}
	public void onLivingUpdate()
	{
		setSize(0.5F, 1.95F);
		EntityLiving entity;
		if ((isHero()) && (getSpecialAttackTimer() == 1180))
		{
			List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, getEntityBoundingBox().grow(24.0D, 24.0D, 24.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if ((list != null) && (!list.isEmpty()))
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					entity = (EntityLiving)list.get(i1);
					if (entity != null && entity.isNonBoss() && !(entity instanceof EntityFriendlyCreature) && !isOnSameTeam(entity))
					{
						EntityPig entityzombie = new EntityPig(this.world);
						entityzombie.copyLocationAndAnglesFrom(entity);
						this.world.removeEntity(entity);
						entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), null);
						entityzombie.spawnExplosionParticle();
						if (!this.world.isRemote)
						this.world.spawnEntity(entityzombie);
					}
				}
			}
		}
		if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 256.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			performSpecialAttack();
		}
		if (!this.world.isRemote)
		{
			if (witchAttackTimer <= 28 && this.witchAttackTimer > 0 && this.ticksExisted % 4 == 0 && this.isEntityAlive())
			this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_DRINK, getSoundCategory(), this.getSoundVolume(), this.getSoundPitch() - 0.2F);
			
			if (func_184730_o() && this.getHealth() > 0.0F && this.ticksExisted > 20)
			{
				if (this.witchAttackTimer-- <= 0)
				{
					this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
					setAggressive(false);
					ItemStack itemstack = getHeldItemMainhand();
					setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
					if (itemstack.getItem() == Items.POTIONITEM)
					{
						this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_BURP, getSoundCategory(), this.getSoundVolume(), this.getSoundPitch());
						List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
						
						if (list != null)
						{
							for (PotionEffect potioneffect : list)
							{
								this.addPotionEffect(new PotionEffect(potioneffect));
							}
						}
					}
					getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
				}
			}
			else
			{
				PotionType potiontype = null;
				if (this.rand.nextFloat() < 0.1F && (!isPotionActive(MobEffects.WATER_BREATHING)))
				{
					potiontype = PotionTypes.LONG_WATER_BREATHING;
				}
				else if (this.rand.nextFloat() < 0.1F && (!isPotionActive(MobEffects.FIRE_RESISTANCE)))
				{
					potiontype = PotionTypes.LONG_FIRE_RESISTANCE;
				}
				else if (this.rand.nextFloat() < 0.1F && (!isPotionActive(MobEffects.SPEED)))
				{
					potiontype = PotionTypes.STRONG_SWIFTNESS;
				}
				else if (this.rand.nextFloat() < 0.1F && (!isPotionActive(MobEffects.REGENERATION)))
				{
					potiontype = PotionTypes.LONG_REGENERATION;
				}
				else if (this.rand.nextFloat() < 0.1F && (!isPotionActive(MobEffects.NIGHT_VISION)))
				{
					potiontype = PotionTypes.LONG_NIGHT_VISION;
				}
				else if (this.rand.nextFloat() < 0.1F && (!isPotionActive(MobEffects.JUMP_BOOST)))
				{
					potiontype = PotionTypes.STRONG_LEAPING;
				}
				else if (this.rand.nextFloat() < 0.1F && (!isPotionActive(MobEffects.STRENGTH)) && this.getAttackTarget() != null)
				{
					potiontype = PotionTypes.STRONG_STRENGTH;
				}
				else if (this.rand.nextFloat() < 0.1F && (getHealth() < getMaxHealth()))
				{
					potiontype = PotionTypes.STRONG_HEALING;
				}
				if (potiontype != null && this.getHealth() > 0.0F && this.ticksExisted > 20 && !this.isChild())
				{
					setItemStackToSlot(EntityEquipmentSlot.MAINHAND, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potiontype));
					this.witchAttackTimer = getHeldItemMainhand().getMaxItemUseDuration();
					setAggressive(true);
					this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, getSoundCategory(), this.getSoundVolume(), this.getSoundPitch());
					IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
					iattributeinstance.removeModifier(MODIFIER);
					iattributeinstance.applyModifier(MODIFIER);
				}
			}
			if (!this.world.isRemote && !this.isChild() && (getOwner() != null) && (this.world.getClosestPlayerToEntity(this, 16.0D) != null) && (this.world.getClosestPlayerToEntity(this, 16.0D) == getOwner()) && (getDistance(getOwner()) < 16.0D) && this.canEntityBeSeen(getOwner()) && (this.rand.nextInt(200) == 0))
			{
				attackEntityWithRangedAttack(getOwner(), 0.0F);
				getLookHelper().setLookPositionWithEntity(getOwner(), 180.0F, 30.0F);
			}
			if (this.rand.nextFloat() < 0.01F)
			{
				this.world.setEntityState(this, (byte)15);
			}
		}
		super.onLivingUpdate();
	}
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 15)
		{
			for (int i = 0; i < 50; i++)
			{
				this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
		else {
			super.handleStatusUpdate(id);
		}
	}
	public boolean isPotionApplicable(PotionEffect potioneffectIn)
	{
		return potioneffectIn.getPotion() == MobEffects.POISON ? false : super.isPotionApplicable(potioneffectIn);
	}
	protected float applyPotionDamageCalculations(DamageSource source, float damage)
	{
		damage = super.applyPotionDamageCalculations(source, damage);
		
		if (source.getTrueSource() == this)
		{
			damage = 0.0F;
		}

		if (source.isMagicDamage())
		{
			damage = (float)((double)damage * 0.15D);
		}

		return damage;
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_WITCH;
	}
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_)
	{
		for (int i = 0; i < (this.isHero() ? 3 : 1); i++)
		{
			if (!func_184730_o() && this.canEntityBeSeen(target))
			{
				double d0 = target.posY;
				double d1 = target.posX + target.motionX - this.posX;
				double d2 = d0 - this.posY;
				double d3 = target.posZ + target.motionZ - this.posZ;
				MathHelper.sqrt(d1 * d1 + d3 * d3);
				PotionType potiontype = PotionTypes.STRONG_HARMING;
				if (isOnSameTeam(target))
				{
					switch (this.rand.nextInt(8))
					{
						case 0:potiontype = PotionTypes.STRONG_HEALING;
						break;
						case 1:potiontype = PotionTypes.LONG_FIRE_RESISTANCE;
						break;
						case 2:potiontype = PotionTypes.LONG_NIGHT_VISION;
						break;
						case 3:potiontype = PotionTypes.LONG_WATER_BREATHING;
						break;
						case 4:potiontype = PotionTypes.STRONG_REGENERATION;
						break;
						case 5:potiontype = PotionTypes.STRONG_SWIFTNESS;
						break;
						case 6:potiontype = PotionTypes.STRONG_STRENGTH;
						break;
						case 7:potiontype = PotionTypes.STRONG_LEAPING;
					}
				}
				if ((target instanceof EntityWitch || target instanceof net.minecraft.entity.monster.EntityWitch) && !this.isOnSameTeam(target))
				{
					target.attackEntityFrom(DamageSource.IN_WALL, 5.0F);
					potiontype = PotionTypes.AWKWARD;
					target.hurtResistantTime = 0;
					target.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 100, 4));
				}
				else if (target.isEntityUndead() && !this.isOnSameTeam(target))
				{
					potiontype = PotionTypes.STRONG_HEALING;
				}
				else if (target.isNonBoss() && (!target.isPotionActive(MobEffects.SLOWNESS)) && target.isPotionApplicable(new PotionEffect(MobEffects.SLOWNESS)) && (target != getOwner()))
				{
					potiontype = PotionTypes.LONG_SLOWNESS;
				}
				else if (target.isNonBoss() && (target.getHealth() >= 2.0F) && (!target.isEntityUndead()) && target.isPotionApplicable(new PotionEffect(MobEffects.POISON)) && (!(target instanceof net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider)) && (!(target instanceof net.minecraft.entity.monster.EntitySpider)) && (!target.isPotionActive(MobEffects.POISON)) && !this.isOnSameTeam(target))
				{
					potiontype = PotionTypes.STRONG_POISON;
				}
				else if (target.isNonBoss() && (!target.isPotionActive(MobEffects.WEAKNESS)) && target.isPotionApplicable(new PotionEffect(MobEffects.WEAKNESS)) && !this.isOnSameTeam(target))
				{
					potiontype = PotionTypes.LONG_WEAKNESS;
				}
				EntityPotionOther entitypotion = new EntityPotionOther(this.world, this, PotionUtils.addPotionToItemStack(this.getIntelligence() > 40 ? new ItemStack(Items.LINGERING_POTION) : new ItemStack(Items.SPLASH_POTION), potiontype));
				entitypotion.rotationPitch -= -20.0F;
				entitypotion.shoot(d1, d2, d3, 1F * (this.getDistance(target) / this.getDistance(target)), 9F);
				this.playLivingSound();
				this.swingArm(EnumHand.MAIN_HAND);
				this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
				this.world.spawnEntity(entitypotion);
			}
		}
	}
	public float getEyeHeight()
	{
		return this.height * 0.84F;
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {}
}


