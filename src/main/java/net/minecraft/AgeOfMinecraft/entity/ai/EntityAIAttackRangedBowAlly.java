package net.minecraft.AgeOfMinecraft.entity.ai;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;

public class EntityAIAttackRangedBowAlly<T extends EntityFriendlyCreature & IRangedAttackMob> extends EntityAIBase
{
	private final T entity;
	private final double moveSpeedAmp;
	private final int field_188501_c;
	private final float maxAttackDistance;
	private int field_188503_e = -1;
	private int field_188504_f;
	private boolean field_188505_g;
	private boolean field_188506_h;
	private int field_188507_i = -1;
	public EntityAIAttackRangedBowAlly(T p_i46805_1_, double p_i46805_2_, int p_i46805_4_, float p_i46805_5_)
	{
		this.entity = p_i46805_1_;
		this.moveSpeedAmp = p_i46805_2_;
		this.field_188501_c = p_i46805_4_;
		this.maxAttackDistance = (p_i46805_5_ * p_i46805_5_);
		setMutexBits(3);
	}
	public boolean shouldExecute()
	{
		return this.entity.getAttackTarget() == null ? false : func_188498_f();
	}
	protected boolean func_188498_f()
	{
		return (this.entity.getHeldItemMainhand() != null) && (this.entity.getHeldItemMainhand().getItem() instanceof ItemBow);
	}
	public boolean shouldContinueExecuting()
	{
		return ((shouldExecute()) || (!this.entity.getNavigator().noPath())) && (func_188498_f());
	}
	public void startExecuting()
	{
		super.startExecuting();
		this.entity.setArmsRaised(true);
		this.entity.setSitResting(false);
	}
	public void resetTask()
	{
		super.startExecuting();
		this.entity.setArmsRaised(false);
		this.field_188504_f = 0;
		this.field_188503_e = -1;
		this.entity.resetActiveHand();
		this.entity.getMoveHelper().strafe(0F, 0F);
	}
	public void updateTask()
	{
		EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
		if (entitylivingbase != null)
		{
			entity.setSitResting(false);
			double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
			boolean flag = this.entity.getEntitySenses().canSee(entitylivingbase);
			this.field_188504_f += 1;
			if (this.entity.moralRaisedTimer > 200)
			this.field_188504_f += 1;
			if (!flag)
			{
				this.field_188504_f += 1;
				this.field_188504_f += 1;
				this.field_188504_f += 1;
				this.field_188504_f += 1;
			}
			if ((d0 <= this.maxAttackDistance + entitylivingbase.width && this.field_188504_f >= 20) || !this.entity.onGround || moveSpeedAmp == 0D)
			{
				this.entity.getNavigator().clearPath();
				this.field_188507_i += 1;
			}
			else
			{
				this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
				this.field_188507_i = -1;
			}
			if (this.field_188507_i >= 20)
			{
				if (this.entity.getRNG().nextFloat() < 0.3D)
				{
					this.field_188505_g = (!this.field_188505_g);
				}
				if (this.entity.getRNG().nextFloat() < 0.3D)
				{
					this.field_188506_h = (!this.field_188506_h);
				}
				this.field_188507_i = 0;
			}
			if (this.field_188507_i > -1)
			{
				if (d0 > this.maxAttackDistance * 0.75F)
				{
					this.field_188506_h = false;
				}
				else if (d0 < this.maxAttackDistance * 0.25F)
				{
					this.field_188506_h = true;
				}
				if (this.entity.getRidingEntity() != null && this.entity.getRidingEntity() instanceof net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider)
				this.entity.getNavigator().setPath(((EntityLiving) this.entity.getRidingEntity()).getNavigator().getPathToEntityLiving(this.entity.getAttackTarget()), 1.2D);
				if (this.entity.hurtResistantTime > 0 || this.entity.getDistance(entitylivingbase) <= this.maxAttackDistance / 2)
				this.entity.getMoveHelper().strafe(-1F, entitylivingbase instanceof Flying || entitylivingbase instanceof IRangedAttackMob || entitylivingbase instanceof EntityPlayer ? (this.field_188505_g ? 1F : -1F) : 0F);
				this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
			}
			else
			{
				this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
			}
			if (this.entity.isHandActive())
			{
				if ((!flag) && (this.field_188504_f < -60))
				{
					this.entity.resetActiveHand();
				}
				else if (flag)
				{
					int i = this.entity.getItemInUseMaxCount();
					
					if (i >= 20)
					{
						this.entity.getMoveHelper().strafe(0F, 0F);
						this.entity.getNavigator().clearPath();
						this.entity.resetActiveHand();
						if (d0 < this.maxAttackDistance * 0.1F)
						this.entity.attackEntityAsMob(entitylivingbase);
						else
						this.entity.attackEntityWithRangedAttack(entitylivingbase, net.minecraft.item.ItemBow.getArrowVelocity(i));
						this.field_188503_e = this.field_188501_c;
					}
				}
			}
			else if ((--this.field_188503_e <= 0) && (this.field_188504_f >= -60))
			{
				this.entity.setActiveHand(net.minecraft.util.EnumHand.MAIN_HAND);
			}
		}
	}
}


