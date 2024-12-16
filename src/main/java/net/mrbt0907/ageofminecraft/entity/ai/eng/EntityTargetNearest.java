package net.mrbt0907.ageofminecraft.entity.ai.eng;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;

public class EntityTargetNearest extends EntityAIBase
{
	private final EntityEngendered entity;
	private final Class<? extends EntityLivingBase> targetClass;
	private final BiPredicate<EntityEngendered, EntityLivingBase> predicate;
	private EntityLivingBase target;
	private ArrayList<Entity> cachedEntities;
	
	public EntityTargetNearest(EntityEngendered entity, Class<? extends EntityLivingBase> targetClass)
	{
		this(entity, targetClass, EntityEngendered.TARGET_WILD);
	}
	
	public EntityTargetNearest(EntityEngendered entity, Class<? extends EntityLivingBase> targetClass, BiPredicate<EntityEngendered, EntityLivingBase> predicate)
	{
		this.entity = entity;
		this.targetClass = targetClass;
		this.predicate = predicate;
	}
	
	@Override
	public boolean shouldExecute()
	{
		if (entity.getAttackTarget() != null) return false;
		if (entity.ticksExisted % 100 == 0)
			cachedEntities = new ArrayList<Entity>(entity.world.loadedEntityList);
		if (cachedEntities == null) return false;
		IAttributeInstance attribute = entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
		double followDistance = Math.pow(attribute.getAttributeValue(), 2.0D), resultDistance = followDistance, targetDistance;
		
		for(Entity entity : cachedEntities)
		{
			targetDistance = Maths.distance(this.entity.posX, this.entity.posY, this.entity.posZ, entity.posX, entity.posY, entity.posZ);
			if (!entity.equals(this.entity) && targetClass.isAssignableFrom(entity.getClass()) && targetDistance < resultDistance && (predicate == null ? true : predicate.test(this.entity, (EntityLivingBase) entity)))
			{
				resultDistance = targetDistance;
				target = (EntityLivingBase) entity;
			}
		}
		return target != null;
	}
	
	@Override
	public boolean shouldContinueExecuting()
	{
		return entity.getAttackTarget() != null && target != null && target.isEntityAlive();
	}
	
	@Override
	public void resetTask()
	{
		entity.setAttackTarget(null);
		target = null;
	}
	
	@Override
	public void startExecuting()
	{
		entity.setAttackTarget(target);
	}
}
