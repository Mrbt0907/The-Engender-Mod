package net.minecraft.AgeOfMinecraft.entity.tier2;

import java.util.List;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityLlamaSpitOther extends EntityLlamaSpit
{
	public EntityLlama owner;
	public EntityLlamaSpitOther(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	public EntityLlamaSpitOther(World worldIn, EntityLlama p_i47273_2_)
	{
		super(worldIn);
		this.owner = p_i47273_2_;
		this.setPosition(p_i47273_2_.posX - (double)(p_i47273_2_.width + 1.0F) * 0.5D * (double)MathHelper.sin(p_i47273_2_.renderYawOffset * 0.017453292F), p_i47273_2_.posY + (double)p_i47273_2_.getEyeHeight(), p_i47273_2_.posZ + (double)(p_i47273_2_.width + 1.0F) * 0.5D * (double)MathHelper.cos(p_i47273_2_.renderYawOffset * 0.017453292F));
		this.setSize(0.25F, 0.25F);
	}

	@SideOnly(Side.CLIENT)
	public EntityLlamaSpitOther(World worldIn, double x, double y, double z, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
		
		for (int i = 0; i < 20; ++i)
		{
			double d0 = 0.4D + 0.1D * (double)i;
			worldIn.spawnParticle(EnumParticleTypes.SPIT, x, y, z, p_i47274_8_ * d0, p_i47274_10_, p_i47274_12_ * d0, new int[0]);
		}

		this.motionX = p_i47274_8_;
		this.motionY = p_i47274_10_;
		this.motionZ = p_i47274_12_;
	}

	public void onHit(RayTraceResult movingObject)
	{
		if (!this.world.isRemote && movingObject.entityHit != null)
		{
			if (movingObject.entityHit != this.owner && movingObject.entityHit instanceof EntityLivingBase && (!this.owner.isOnSameTeam((EntityLivingBase)movingObject.entityHit) || (movingObject.entityHit instanceof EntityFriendlyCreature && this.owner.isOnSameTeam((EntityFriendlyCreature)movingObject.entityHit) && this.owner.getFakeHealth() > 0F)) && movingObject.entityHit != null && this.owner != null && movingObject.entityHit != this.owner)
			{
				owner.inflictEngenderMobDamage((EntityLivingBase)movingObject.entityHit, " was spat on by ", DamageSource.causeThrownDamage(owner, this), 1F);
				if (((EntityLivingBase)movingObject.entityHit).isNonBoss())
				((EntityLivingBase)movingObject.entityHit).knockBack(this, 0.75F, MathHelper.sin(this.owner.rotationYawHead * 0.017453292F), -MathHelper.cos(this.owner.rotationYawHead * 0.017453292F));
				if (!this.world.isRemote)
				this.setDead();
			}
		}
		if (!this.world.isRemote && movingObject.entityHit == null)
		{
			this.setDead();
		}
	}

	protected void entityInit()
	{
	}
	public void onUpdate()
	{
		super.onUpdate();
		
		this.setSize(0.25F, 0.25F);
		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(4D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
		if (!list.isEmpty())
		for (EntityLivingBase entity1 : list)
		if (this.owner != null && entity1 instanceof IEntityMultiPart && entity1 != null && entity1.isEntityAlive() && !this.owner.isOnSameTeam(entity1))this.onHit(new RayTraceResult(entity1));
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("Owner", 10))
		{
			compound.getCompoundTag("Owner");
		}
		this.setSize(0.25F, 0.25F);
	}

	/**
	* (abstract) Protected helper method to write subclass entity data to NBT.
	*/
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		if (this.owner != null)
		{
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			UUID uuid = this.owner.getUniqueID();
			nbttagcompound.setUniqueId("OwnerUUID", uuid);
			compound.setTag("Owner", nbttagcompound);
		}
		this.setSize(0.25F, 0.25F);
	}
}