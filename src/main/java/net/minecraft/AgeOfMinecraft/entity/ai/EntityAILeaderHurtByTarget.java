package net.minecraft.AgeOfMinecraft.entity.ai;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPigZombie;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.PhaseList;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.PhaseRamAttack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAILeaderHurtByTarget extends EntityAITarget
{
	EntityFriendlyCreature theDefendingTameable;
	EntityLivingBase theOwnerAttacker;
	public EntityAILeaderHurtByTarget(EntityFriendlyCreature p_i1667_1_)
	{
		super(p_i1667_1_, false);
		this.theDefendingTameable = p_i1667_1_;
		setMutexBits(1);
	}
	public boolean shouldExecute()
	{
		if (this.theDefendingTameable.getOwner() == null)
		{
			return false;
		}
		if (this.theDefendingTameable.isChild())
		{
			return false;
		}
		EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwner();
		if (entitylivingbase == null)
		{
			return !this.theDefendingTameable.isChild();
		}
		this.theOwnerAttacker = entitylivingbase.getRevengeTarget();
		return (isSuitableTarget(this.theOwnerAttacker, false)) && (this.theDefendingTameable.shouldAttackEntity(this.theOwnerAttacker, entitylivingbase)) && !this.theDefendingTameable.isOnSameTeam(theOwnerAttacker);
	}
	public void startExecuting()
	{
		if (!this.theDefendingTameable.isOnSameTeam(theOwnerAttacker))
		this.theDefendingTameable.setAttackTarget(this.theOwnerAttacker);
		this.theDefendingTameable.getOwner();
		if ((this.theDefendingTameable instanceof EntityPigZombie))
		{
			((EntityPigZombie)this.theDefendingTameable).becomeAngryAt(this.theOwnerAttacker);
		}
		if ((this.theDefendingTameable instanceof EntityEnderDragon))
		{
			((EntityEnderDragon)this.theDefendingTameable).getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
			((PhaseRamAttack)((EntityEnderDragon)this.theDefendingTameable).getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER)).func_188668_a(new net.minecraft.util.math.Vec3d(this.theOwnerAttacker.posX, this.theOwnerAttacker.posY, this.theOwnerAttacker.posZ));
		}
		super.startExecuting();
	}
}


