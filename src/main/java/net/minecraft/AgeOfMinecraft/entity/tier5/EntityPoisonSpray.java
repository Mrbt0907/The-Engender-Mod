package net.minecraft.AgeOfMinecraft.entity.tier5;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.EntityAreaEffectCloudOther;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPoisonSpray extends EntitySnowball
{
	public EntityPoisonSpray(World worldIn)
	{
		super(worldIn);
	}

	public EntityPoisonSpray(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public EntityPoisonSpray(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	protected void onImpact(RayTraceResult movingObject)
	{
		byte b0 = 10;
		if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
		b0 = 20;
		else if (this.world.getDifficulty() == EnumDifficulty.HARD)
		b0 = 60;
		if (movingObject.entityHit != null)
		{
			if ((this.getThrower() != null) && (this.getThrower() instanceof EntityFriendlyCreature) && ((movingObject.entityHit instanceof EntityLivingBase)) && (!((EntityFriendlyCreature)this.getThrower()).isOnSameTeam((EntityLivingBase)movingObject.entityHit)))
			{
				movingObject.entityHit.hurtResistantTime = 0;
				if (this.getThrower().attackEntityAsMob(movingObject.entityHit))
				{
					if (b0 > 0 && movingObject.entityHit instanceof EntityLivingBase)
					((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(MobEffects.POISON, 20 * b0));
					setDead();
					applyEnchantments(this.getThrower(), movingObject.entityHit);
					EntityAreaEffectCloudOther entityareaeffectcloud = new EntityAreaEffectCloudOther(this.world, this.posX, this.posY, this.posZ);
					if (this.getThrower() != null && this.getThrower() instanceof EntityFriendlyCreature)
					entityareaeffectcloud.setOwner((EntityFriendlyCreature)this.getThrower());
					entityareaeffectcloud.setParticle(EnumParticleTypes.SLIME);
					entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.POISON, 20 * b0));
					entityareaeffectcloud.setRadius(2.0F);
					entityareaeffectcloud.setDuration(100);
					this.playSound(SoundEvents.ENTITY_SMALL_SLIME_SQUISH, 1F, 1F);
					this.world.spawnEntity(entityareaeffectcloud);
				}
			}
		}
		else
		{
			this.setDead();
			EntityAreaEffectCloudOther entityareaeffectcloud = new EntityAreaEffectCloudOther(this.world, this.posX, this.posY, this.posZ);
			entityareaeffectcloud.setOwner((EntityFriendlyCreature)this.getThrower());
			entityareaeffectcloud.setParticle(EnumParticleTypes.SLIME);
			entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.POISON, 20 * b0));
			entityareaeffectcloud.setRadius(2.0F);
			entityareaeffectcloud.setDuration(100);
			this.playSound(SoundEvents.ENTITY_SMALL_SLIME_SQUISH, 1F, 1F);
			this.world.spawnEntity(entityareaeffectcloud);
		}

		List<EntityLivingBase> list1 = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
		
		if ((list1 != null) && (!list1.isEmpty()))
		{
			for (int i1 = 0; i1 < list1.size(); i1++)
			{
				EntityLivingBase entity1 = (EntityLivingBase)list1.get(i1);
				if (!((EntityFriendlyCreature)this.getThrower()).isOnSameTeam(entity1))
				{
					this.getThrower().attackEntityAsMob(entity1);
					if (b0 > 0 && entity1 instanceof EntityLivingBase)
					((EntityLivingBase)entity1).addPotionEffect(new PotionEffect(MobEffects.POISON, 20 * b0));
				}
			}
		}
	}
}