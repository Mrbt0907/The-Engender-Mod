package net.minecraft.AgeOfMinecraft.entity.ai;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPigZombie;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAILeaderHurtTarget
extends EntityAITarget
{
	EntityFriendlyCreature theEntityTameable;
	EntityLivingBase theTarget;
	public EntityAILeaderHurtTarget(EntityFriendlyCreature p_i1668_1_)
	{
		super(p_i1668_1_, false);
		this.theEntityTameable = p_i1668_1_;
		setMutexBits(1);
	}
	public boolean shouldExecute()
	{
		if (this.theEntityTameable.getOwner() == null)
		{
			return false;
		}
		if (this.theEntityTameable.isChild())
		{
			return false;
		}
		EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
		if (entitylivingbase == null)
		{
			return !this.theEntityTameable.isChild();
		}
		this.theTarget = entitylivingbase.getLastAttackedEntity();
		return (isSuitableTarget(this.theTarget, false)) && (this.theEntityTameable.shouldAttackEntity(this.theTarget, entitylivingbase));
	}
	public void startExecuting()
	{
		this.theEntityTameable.setAttackTarget(this.theTarget);
		EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
		if (entitylivingbase != null)
		{
			entitylivingbase.getLastAttackedEntityTime();
		}
		if ((this.theEntityTameable instanceof EntityPigZombie))
		{
			((EntityPigZombie)this.theEntityTameable).becomeAngryAt(this.theTarget);
		}
		super.startExecuting();
	}
}


