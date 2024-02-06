package net.minecraft.AgeOfMinecraft.entity.tier5;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.EntityAreaEffectCloudOther;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWitherSkullOther
extends EntityWitherSkull
{
	 public float damage = 8.0F;
	public EntityWitherSkullOther(World worldIn)
	{
		super(worldIn);
		setSize(0.3125F, 0.3125F);
	}
	public EntityWitherSkullOther(World worldIn, EntityLivingBase p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_)
	{
		super(worldIn, p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_);
		setSize(0.3125F, 0.3125F);
	}
	@SideOnly(Side.CLIENT)
	public EntityWitherSkullOther(World worldIn, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_)
	{
		super(worldIn, p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_);
		setSize(0.3125F, 0.3125F);
	}
	protected void onImpact(RayTraceResult movingObject)
	{
		byte b0 = 10;
		if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
		b0 = 20;
		else if (this.world.getDifficulty() == EnumDifficulty.HARD)
		b0 = 60;
		if (!this.world.isRemote && movingObject.entityHit != this.shootingEntity && movingObject.entityHit != null)
		{
			if (movingObject.entityHit instanceof EntityLivingBase && this.shootingEntity instanceof EntityFriendlyCreature && !((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam((EntityLivingBase)movingObject.entityHit) || (movingObject.entityHit instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam((EntityFriendlyCreature)movingObject.entityHit) && ((EntityFriendlyCreature)this.shootingEntity).getFakeHealth() > 0F))
			{
				this.copyLocationAndAnglesFrom(movingObject.entityHit);
				List<EntityLivingBase> list1 = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
				
				if (!list1.isEmpty())
				for (EntityLivingBase entitylivingbase : list1)
				if (!((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam(entitylivingbase))
				((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage(entitylivingbase, " was shot by ", DamageSource.causeMobDamage(this.shootingEntity), damage);
				
				((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage((EntityLivingBase)movingObject.entityHit, " was shot by ", DamageSource.causeMobDamage(this.shootingEntity), damage);
				((EntityFriendlyCreature)this.shootingEntity).inflictCustomStatusEffect(this.world.getDifficulty(), (EntityLivingBase)movingObject.entityHit, MobEffects.WITHER, 10, 1);
				this.setDead();
				EntityAreaEffectCloudOther entityareaeffectcloud = new EntityAreaEffectCloudOther(this.world, this.posX, this.posY, this.posZ);
				if (this.shootingEntity != null && this.shootingEntity instanceof EntityFriendlyCreature)
				entityareaeffectcloud.setOwner((EntityFriendlyCreature)this.shootingEntity);
				entityareaeffectcloud.setParticle(EnumParticleTypes.SMOKE_NORMAL);
				entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.WITHER, 20 * b0, 1 + this.world.getDifficulty().getDifficultyId()));
				entityareaeffectcloud.setDuration(40 + rand.nextInt(60));
				entityareaeffectcloud.setRadius(3F);
				entityareaeffectcloud.setRadiusPerTick((0F - entityareaeffectcloud.getRadius()) / entityareaeffectcloud.getDuration());
				this.world.spawnEntity(entityareaeffectcloud);
				EntityFriendlyCreature.createEngenderModExplosion((EntityFriendlyCreature)this.shootingEntity, this.posX, this.posY, this.posZ, 2F, false, this.world.getGameRules().getBoolean("mobGriefing") && this.isInvulnerable());
				this.setDead();
			}
		}
		if (!this.world.isRemote && movingObject.entityHit == null)
		{
			List<EntityLivingBase> list1 = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
			
			if (!list1.isEmpty())
			for (EntityLivingBase entitylivingbase : list1)
			if (!((EntityFriendlyCreature)this.shootingEntity).isOnSameTeam(entitylivingbase))
			((EntityFriendlyCreature)this.shootingEntity).inflictEngenderMobDamage(entitylivingbase, " was shot by ", DamageSource.causeMobDamage(this.shootingEntity), damage);
			EntityAreaEffectCloudOther entityareaeffectcloud = new EntityAreaEffectCloudOther(this.world, this.posX, this.posY, this.posZ);
			entityareaeffectcloud.setOwner((EntityFriendlyCreature)this.shootingEntity);
			entityareaeffectcloud.setParticle(EnumParticleTypes.SMOKE_NORMAL);
			entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.WITHER, 20 * b0, 1 + this.world.getDifficulty().getDifficultyId()));
			entityareaeffectcloud.setDuration(40 + rand.nextInt(60));
			entityareaeffectcloud.setRadius(3F);
			entityareaeffectcloud.setRadiusPerTick((0F - entityareaeffectcloud.getRadius()) / entityareaeffectcloud.getDuration());
			this.world.spawnEntity(entityareaeffectcloud);
			EntityFriendlyCreature.createEngenderModExplosion((EntityFriendlyCreature)this.shootingEntity, this.posX, this.posY, this.posZ, 2F, false, this.world.getGameRules().getBoolean("mobGriefing") && this.isInvulnerable());
			this.setDead();
		}
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