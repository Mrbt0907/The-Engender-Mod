package net.minecraft.AgeOfMinecraft.entity.tier5;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;


public class EntityElderGuardian extends EntityGuardian implements Massive
{
	public EntityElderGuardian(World worldIn)
	{
		super(worldIn);
		this.enablePersistence();
		this.experienceValue = 50;
		this.setSize(2.35F, 2.35F);
		if (this.wander != null)
		{
			this.wander.setExecutionChance(400);
		}
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}
	public double getKnockbackResistance()
	{
		return 1D;
	}
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_ELDER_GUARDIAN;
	}
	public int func_175464_ck()
	{
		return 60;
	}
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_ELDER_GUARDIAN_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		this.setAttackTarget((EntityLivingBase)null);
		if (!this.isInWater())
		this.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH_LAND, this.getSoundVolume(), this.getSoundPitch());
		return SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH;
	}
	public double getMountedYOffset()
	{
		return this.onGround && !isInWater() ? this.height * 1.1D : this.height * 0.85D;
	}
	protected void jump()
	{
		this.hurtTime = 0;
		this.playSound(SoundEvents.ENTITY_GUARDIAN_FLOP, 1.0F, this.isChild() ? 1.5F : 1F);
		this.motionY += (this.isChild() ? 0.5D : 0.6D) - this.motionY;
		this.onGround = false;
		this.isAirBorne = true;
	}
	public float getRenderSizeModifier()
	{
		return 2.35F;
	}
	public float getEyeHeight()
	{
		return this.height * 0.5F;
	}
	protected float getSoundPitch()
	{
		return  super.getSoundPitch();
	}
	public int getVerticalFaceSpeed()
	{
		return ((this.onGround) && this.getAttackTarget() == null ? 5 : 180);
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER5;
	}
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		super.notifyDataManagerChange(key);
		this.setSize(2.35F, 2.35F);
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.fallDistance = 0F;
		
	}
	public boolean isElder()
	{
		return true;
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityElderGuardian(this.world);
	}
	protected void updateAITasks()
	{
		super.updateAITasks();
		
		if ((this.ticksExisted + this.getEntityId()) % 200 == 0)
		{
			List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntities(EntityLivingBase.class, new Predicate<EntityLivingBase>()
			{
				public boolean apply(@Nullable EntityLivingBase p_apply_1_)
				{
					return EntityElderGuardian.this.getDistanceSq(p_apply_1_) < 2500.0D && EntityElderGuardian.this.getRNG().nextInt(20) == 0;
				}
			});
				int j = 1;
				int k = 6000;
				int l = 1200;
				
				for (EntityLivingBase entityplayermp : list)
				{
					Potion potion;if (!this.isOnSameTeam(entityplayermp))
					potion = MobEffects.SLOWNESS;
					else
					potion = MobEffects.SPEED;
					if (!entityplayermp.isPotionActive(potion) || entityplayermp.getActivePotionEffect(potion).getAmplifier() < j || entityplayermp.getActivePotionEffect(potion).getDuration() < l)
					{
						this.world.playSound(null, entityplayermp.posX, entityplayermp.posY, entityplayermp.posZ, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, this.getSoundCategory(), this.getSoundVolume(), this.isChild() ? 1.5F : 1.0F);
						entityplayermp.addPotionEffect(new PotionEffect(potion, k, j, true, false));
						entityplayermp.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0, true, false));
					}
				}
			}
			if (this.ticksExisted % 20 == 0)
			{
				List<EntityPlayerMP> list = this.world.<EntityPlayerMP>getPlayers(EntityPlayerMP.class, new Predicate<EntityPlayerMP>()
				{
					public boolean apply(@Nullable EntityPlayerMP p_apply_1_)
					{
						return EntityElderGuardian.this.getDistanceSq(p_apply_1_) < 2500.0D;
					}
				});
					
					for (EntityPlayerMP entityplayermp : list)
					{
						Potion potion;if (this.isOnSameTeam(entityplayermp))
						potion = MobEffects.HASTE;
						else
						potion = MobEffects.MINING_FATIGUE;
						if (this.ticksExisted == 40 || ((!entityplayermp.isPotionActive(potion)) || (entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2) || (entityplayermp.getActivePotionEffect(potion).getDuration() < 1200)))
						{
							entityplayermp.connection.sendPacket(new SPacketChangeGameState(10, 0.0F));
							entityplayermp.addPotionEffect(new PotionEffect(potion, 6000, 2, true, false));
							entityplayermp.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 100, 0, true, false));
							faceEntity(entityplayermp, 180.0F, getVerticalFaceSpeed());
						}
					}
				}
			}
		}
		