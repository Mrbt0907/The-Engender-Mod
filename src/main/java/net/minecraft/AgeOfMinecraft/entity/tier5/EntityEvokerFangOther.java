package net.minecraft.AgeOfMinecraft.entity.tier5;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityEvokerFangOther extends EntityEvokerFangs
{
	private int warmupDelayTicks;
	private boolean sentSpikeEvent;
	private int lifeTicks;
	private boolean clientSideAttackStarted;
	private EntityLivingBase caster;
	private UUID casterUuid;
	
	public EntityEvokerFangOther(World worldIn)
	{
		super(worldIn);
		this.lifeTicks = 22;
	}

	public EntityEvokerFangOther(World worldIn, double x, double y, double z, float p_i47276_8_, int p_i47276_9_, EntityLivingBase casterIn)
	{
		this(worldIn);
		this.warmupDelayTicks = p_i47276_9_;
		this.setCaster(casterIn);
		this.rotationYaw = p_i47276_8_ * (180F / (float)Math.PI);
		this.setPosition(x, y, z);
	}

	protected void entityInit()
	{
	}

	public void setCaster(@Nullable EntityLivingBase p_190549_1_)
	{
		this.caster = p_190549_1_;
		this.casterUuid = p_190549_1_ == null ? null : p_190549_1_.getUniqueID();
	}

	@Nullable
	public EntityLivingBase getCaster()
	{
		if (this.caster == null && this.casterUuid != null && this.world instanceof WorldServer)
		{
			Entity entity = ((WorldServer)this.world).getEntityFromUuid(this.casterUuid);
			
			if (entity instanceof EntityLivingBase)
			{
				this.caster = (EntityLivingBase)entity;
			}
		}

		return this.caster;
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.warmupDelayTicks = compound.getInteger("Warmup");
		this.casterUuid = compound.getUniqueId("OwnerUUID");
	}

	/**
	* (abstract) Protected helper method to write subclass entity data to NBT.
	*/
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("Warmup", this.warmupDelayTicks);
		
		if (this.casterUuid != null)
		{
			compound.setUniqueId("OwnerUUID", this.casterUuid);
		}
	}

	/**
	* Called to update the entity's position/logic.
	*/
	public void onUpdate()
	{
		if (!this.world.isRemote)
		{
			this.setFlag(6, this.isGlowing());
		}

		this.onEntityUpdate();
		
		if (this.world.isRemote)
		{
			if (this.clientSideAttackStarted)
			{
				--this.lifeTicks;
				
				if (this.lifeTicks == 14)
				{
					for (int i = 0; i < 12; ++i)
					{
						double d0 = this.posX + (this.rand.nextDouble() * 2.0D - 1.0D) * (double)this.width * 0.5D;
						double d1 = this.posY + 0.05D + this.rand.nextDouble() * 1.0D;
						double d2 = this.posZ + (this.rand.nextDouble() * 2.0D - 1.0D) * (double)this.width * 0.5D;
						double d3 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
						double d4 = 0.3D + this.rand.nextDouble() * 0.3D;
						double d5 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
						this.world.spawnParticle(EnumParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5, new int[0]);
					}
				}
			}
		}
		else if (--this.warmupDelayTicks < 0)
		{
			if (this.warmupDelayTicks == -8)
			{
				for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(1D)))
				{
					if (this.getCaster() != null && this.getCaster() instanceof EntityFriendlyCreature && (!((EntityFriendlyCreature)this.getCaster()).isOnSameTeam(entitylivingbase) || (entitylivingbase instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)this.getCaster()).isOnSameTeam(entitylivingbase) && ((EntityFriendlyCreature)this.getCaster()).getFakeHealth() > 0F)))
					this.damage(entitylivingbase);
				}
			}

			if (!this.sentSpikeEvent)
			{
				this.world.setEntityState(this, (byte)4);
				this.sentSpikeEvent = true;
			}

			if (--this.lifeTicks < 0)
			{
				this.setDead();
			}
		}
	}

	private void damage(EntityLivingBase p_190551_1_)
	{
		EntityLivingBase entitylivingbase = this.getCaster();
		
		if (p_190551_1_.isEntityAlive() && !p_190551_1_.getIsInvulnerable())
		{
			p_190551_1_.hurtResistantTime = 0;
			if (entitylivingbase instanceof EntityFriendlyCreature && entitylivingbase != null && p_190551_1_ != entitylivingbase)
			{
				p_190551_1_.playSound(SoundEvents.ENTITY_PLAYER_HURT, 1F, 0.9F);
				applyEnchantments(entitylivingbase, p_190551_1_);
				p_190551_1_.motionY += rand.nextDouble();
				((EntityFriendlyCreature)entitylivingbase).inflictEngenderMobDamage(p_190551_1_, " was chopped up by ", DamageSource.causeIndirectMagicDamage(this, entitylivingbase), 6F);
			}
		}
		if (entitylivingbase == null)
		{
			p_190551_1_.attackEntityFrom(DamageSource.MAGIC, 8F);
		}
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		super.handleStatusUpdate(id);
		
		if (id == 4)
		{
			this.clientSideAttackStarted = true;
			
			if (!this.isSilent())
			{
				this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.EVOCATION_FANGS_ATTACK, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.2F + 0.85F, false);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public float getAnimationProgress(float p_190550_1_)
	{
		if (!this.clientSideAttackStarted)
		{
			return 0.0F;
		}
		else
		{
			int i = this.lifeTicks - 2;
			return i <= 0 ? 1.0F : 1.0F - ((float)i - p_190550_1_) / 20.0F;
		}
	}
}