package net.minecraft.AgeOfMinecraft.entity.tier6;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.EntityAreaEffectCloudOther;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWitherStormSkull
extends EntityFireball
{
	private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.createKey(EntityWitherStormSkull.class, DataSerializers.BOOLEAN);
	public float damage = 20.0F;
	public EntityWitherStormSkull(World worldIn)
	{
		super(worldIn);
		setSize(1F, 1F);
	}
	public EntityWitherStormSkull(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(worldIn, shooter, accelX, accelY, accelZ);
		setSize(1F, 1F);
	}
	protected EnumParticleTypes getParticleType()
	{
		return rand.nextInt(2) == 0 ? EnumParticleTypes.LAVA : EnumParticleTypes.SMOKE_LARGE;
	}
	protected float getMotionFactor()
	{
		return 0.95F;
	}
	@SideOnly(Side.CLIENT)
	public EntityWitherStormSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		super(worldIn, x, y, z, accelX, accelY, accelZ);
		setSize(1F, 1F);
	}
	public boolean isBurning()
	{
		return false;
	}
	public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn)
	{
		float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
		Block block = blockStateIn.getBlock();
		if (EntityWither.canDestroyBlock(block))
		{
			f = Math.min(0.8F, f);
		}
		return f;
	}
	protected void onImpact(RayTraceResult movingObject)
	{
		if (!this.world.isRemote)
		{
			if (this.shootingEntity != null)
			{
				List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
				if ((list != null) && (!list.isEmpty()))
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityLivingBase entity1 = (EntityLivingBase)list.get(i1);
						if (entity1 != null && entity1 instanceof IEntityMultiPart) {if (this.shootingEntity != null)
						EntityFriendlyCreature.createEngenderModExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, 1F, false, false);
						this.shootingEntity.attackEntityAsMob(entity1);
						applyEnchantments(this.shootingEntity, entity1);
						setDead();
					}
				}
			}
		}
	}

	if (movingObject.entityHit != null)
	{
		this.copyLocationAndAnglesFrom(movingObject.entityHit);
		movingObject.entityHit.hurtResistantTime = 0;
		byte b0 = 20;
		if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
		{
			b0 = 40;
		}
		else if (this.world.getDifficulty() == EnumDifficulty.HARD)
		{
			b0 = 80;
		}

		if ((this.shootingEntity != null) && (this.shootingEntity instanceof EntityFriendlyCreature) && ((movingObject.entityHit instanceof EntityLivingBase)) && (!((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam((EntityLivingBase)movingObject.entityHit)))
		{
			if (this.shootingEntity.attackEntityAsMob(movingObject.entityHit))
			{
				if (b0 > 0 && movingObject.entityHit instanceof EntityLivingBase)
				{
					((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 20 * b0));
					((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 20 * b0, this.world.getDifficulty().getDifficultyId()));
					((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * b0, this.world.getDifficulty().getDifficultyId()));
					((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.WITHER, Integer.MAX_VALUE, 3));
				}
				List<EntityLivingBase> list1 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(6D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
				if ((list1 != null) && (!list1.isEmpty()))
				{
					for (int i1 = 0; i1 < list1.size(); i1++)
					{
						EntityLivingBase entity1 = (EntityLivingBase)list1.get(i1);
						if (!((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam(entity1))
						{
							this.shootingEntity.attackEntityAsMob(entity1);
							if (b0 > 0 && movingObject.entityHit instanceof EntityLivingBase)
							{
								((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 20 * b0));
								((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 20 * b0, this.world.getDifficulty().getDifficultyId()));
								((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20 * b0, this.world.getDifficulty().getDifficultyId()));
								((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.WITHER, Integer.MAX_VALUE, 3));
							}
						}
					}
				}
				if (this.shootingEntity != null)
				{
					List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(this.shootingEntity instanceof EntityWitherStormHead ? 9F : 3F), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
					
					if ((list != null) && (!list.isEmpty()))
					{
						for (int i1 = 0; i1 < list.size(); i1++)
						{
							EntityLivingBase entity1 = (EntityLivingBase)list.get(i1);
							if (!((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam(entity1))
							{
								((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage((EntityLivingBase)movingObject.entityHit, " was destroyed by ", DamageSource.causeMobDamage(this.shootingEntity), damage);
							}
						}
					}

					if (movingObject.entityHit != null)
					{
						if ((this.shootingEntity != null) && (this.shootingEntity instanceof EntityFriendlyCreature) && ((movingObject.entityHit instanceof EntityLivingBase)) && (!((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam((EntityLivingBase)movingObject.entityHit)))
						{
							movingObject.entityHit.hurtResistantTime = 0;
							((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage((EntityLivingBase)movingObject.entityHit, " was destroyed by ", DamageSource.causeMobDamage(this.shootingEntity), damage);
						}
					}

					EntityAreaEffectCloudOther entityareaeffectcloud = new EntityAreaEffectCloudOther(this.world, this.posX, this.posY, this.posZ);
					if (this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature)
					entityareaeffectcloud.setOwner((EntityFriendlyCreature)this.shootingEntity);
					entityareaeffectcloud.setParticle(EnumParticleTypes.SMOKE_NORMAL);
					entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.WITHER, 40 * b0, 1 + this.world.getDifficulty().getDifficultyId()));
					entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.BLINDNESS, 20 * b0));
					entityareaeffectcloud.setRadius(4.0F);
					entityareaeffectcloud.setDuration(120);
					this.world.spawnEntity(entityareaeffectcloud);
					EntityFriendlyCreature.createEngenderModExplosion((EntityFriendlyCreature)this.shootingEntity, this.posX, this.posY, this.posZ, this.shootingEntity instanceof EntityWitherStormHead ? 6.0F : 2F, this.world.getGameRules().getBoolean("mobGriefing"), this.world.getGameRules().getBoolean("mobGriefing"));
					this.setDead();
				}

				setDead();
				
				if (!movingObject.entityHit.isEntityAlive())
				{
					this.shootingEntity.heal(25.0F);
				}
				else
				{
					applyEnchantments(this.shootingEntity, movingObject.entityHit);
				}
			}
		}
	}
	else
	{
		if (this.shootingEntity != null)
		EntityFriendlyCreature.createEngenderModExplosion((EntityFriendlyCreature)this.shootingEntity, this.posX, this.posY, this.posZ, this.shootingEntity instanceof EntityWitherStormHead ? 6.0F : 2F, this.world.getGameRules().getBoolean("mobGriefing"), this.world.getGameRules().getBoolean("mobGriefing"));
		this.setDead();
	}
}
public boolean canBeCollidedWith()
{
	return false;
}
public boolean attackEntityFrom(DamageSource source, float amount)
{
	return false;
}
protected void entityInit()
{
	this.dataManager.register(INVULNERABLE, Boolean.valueOf(false));
}
public boolean isInvulnerable()
{
	return ((Boolean)this.dataManager.get(INVULNERABLE)).booleanValue();
}
public void setInvulnerable(boolean invulnerable)
{
	this.dataManager.set(INVULNERABLE, Boolean.valueOf(invulnerable));
}
protected boolean func_184564_k()
{
	return false;
}
public void onUpdate()
{
	super.onUpdate();
	List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
	if (!list.isEmpty())
	for (EntityLivingBase entity1 : list)
	if (this.shootingEntity != null && entity1 instanceof IEntityMultiPart && entity1 != null && entity1.isEntityAlive() && !((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam(entity1))this.onImpact(new RayTraceResult(entity1));
}
}