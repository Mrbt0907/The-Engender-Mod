package net.minecraft.AgeOfMinecraft.entity.cameos.Darkness;

import java.util.List;

import net.endermanofdoom.mac.util.ReflectionUtil;
import net.endermanofdoom.mac.util.math.Maths;
import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.sources.EngenderDamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityDarkProjectile extends Entity
{
	protected static final DataParameter<Byte> TYPE = EntityDataManager.<Byte>createKey(EntityDarkProjectile.class, DataSerializers.BYTE);
	public Entity shooter, target;
	public boolean chase;
	private float heath;

	public EntityDarkProjectile(World world)
	{
		this(world, null, null, (byte) 0, false);
	}

	public EntityDarkProjectile(World world, Entity shooter, Entity target, byte type, boolean chase)
	{
		super(world);
		
		this.target = target;
		this.shooter = shooter;
		this.chase = chase;
		noClip = true;
		heath = type > 1 ? 5.0F : 100.0F;
		
		setType(type);
		setSize(2F, 2F);
		
		if (world.isRemote)
			world.playSound(posX, posY, posZ, SoundEvents.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.HOSTILE, 15.0F, rand.nextFloat() * 0.3F + ((int) type > 1 ? 0.85F : 0.4F), false);
		
		if (shooter != null)
		{
			
			
			rotationYaw = shooter instanceof EntityDarkness ? shooter.rotationYaw + 180F : shooter.rotationYaw;
			rotationPitch = shooter.rotationPitch;

			if (type > 1)
			{
				rotationPitch += (rand.nextFloat() - 0.5F) * 20F;
				rotationYaw += (rand.nextFloat() - 0.5F) * 20F;
			}
			
			if (shooter instanceof EntityDarkness)
			{
				motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI)) * 5.0D;
				motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI)) * 5.0D;
				motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI)) * 5.0D;
			}
			
			motionX *= 3.0F;
			motionY *= 3.0F;
			motionZ *= 3.0F;
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if (!world.isRemote)
		{
			setType((byte) compound.getInteger("Type"));
			chase = compound.getBoolean("Chase");
		}
		
		noClip = true;
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		if (!world.isRemote)
		{
			compound.setInteger("Type", getType());
			compound.setBoolean("Chase", chase);
		}
	}

	@Override
	protected void entityInit()
	{
		dataManager.register(TYPE, (byte) 0);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (!world.isRemote)
		{
			//Target Update
			if (ticksExisted % 20 == 0 && (target == null || !target.isEntityAlive()))
			{
				List<Entity> entities = world.loadedEntityList;
				double distance = Double.MAX_VALUE, curDistance;
				boolean player;
				target = null;
				
				for (Entity entity : entities)
				{
					curDistance = this.getDistanceSq(entity);
					
					if (curDistance < distance && entity.isEntityAlive() && (shooter != null && !entity.equals(shooter) && !shooter.isOnSameTeam(entity)) || shooter == null)
					{
						player = entity instanceof EntityPlayer;
						if (player && !((EntityPlayer)entity).isSpectator() && !((EntityPlayer)entity).isCreative() || !player)
						{
							distance = curDistance;
							target = entity;
						}
					}
				}
			}
			
			//Chase target
			if (target != null && chase)
			{
				double tDist = Maths.distanceSq(target.posX, target.posY, target.posZ, posX, posY, posZ);
				double x = (target.posX - posX) / tDist,
					y = (target.posY - posY) / tDist,
					z = (target.posZ - posZ) / tDist;
				int type = getType();
				
				motionX *= type > 1 ? 0.9D : 0.15D;
				motionY *= type > 1 ? 0.9D : 0.15D;
				motionZ *= type > 1 ? 0.9D : 0.15D;
				motionX += x * 0.2D;
				motionY += y * 0.2D;
				motionZ += z * 0.2D;
			}
			
			onProjectileTick();
		}
		
		move(MoverType.SELF, motionX, motionY, motionZ);
	}

	private void onProjectileTick()
	{
		if (world.isRemote || ticksExisted % 2 != 0) return;
		double distance = target == null ? Double.MAX_VALUE : Maths.distanceSq(posX, posY, posZ, target.posX, target.posY, target.posZ);
		double size = getSize();
		
		switch (getType())
		{
			case 2:
				if (distance <= size || collided || ticksExisted > 1200 || heath <= 0)
				{
					EntityFriendlyCreature.createEngenderModExplosion(shooter == null ? this : shooter, posX, posY, posZ, 1F, false, world.getGameRules().getBoolean("mobGriefing"));
					attackEntities(EngenderDamageSources.VOID, 6.0D, 1.0F, 1.0F);
					setDead();
				}
				break;
			case 1:
				attackEntities(EngenderDamageSources.ERASURE, size + 3.0D, 2.0F, 5.0F);
				if (ticksExisted > 600 || heath <= 0)
				{
					EntityFriendlyCreature.createEngenderModExplosion(shooter == null ? this : shooter, posX, posY, posZ, 10F, false, world.getGameRules().getBoolean("mobGriefing"));
					attackEntities(EngenderDamageSources.ERASURE, 20.0D, 60.0F, 260.0F);
					setDead();
				}
				break;
			default:
				attackEntities(EngenderDamageSources.VOID, size + 3.0D, 1.0F, 1.5F);
				if (ticksExisted > 300 || heath <= 0)
				{
					EntityFriendlyCreature.createEngenderModExplosion(shooter == null ? this : shooter, posX, posY, posZ, 5F, false, world.getGameRules().getBoolean("mobGriefing"));
					attackEntities(EngenderDamageSources.VOID, 10.0D, 20.0F, 40.0F);
					setDead();
				}
		}
	}

	private void attackEntities(DamageSource source, double range, float damage, float absoluteDamage)
	{
		if (world.isRemote) return;
		List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(range));  
		boolean player;
		float factor;
		
		for (Entity target : entities)
		{
			player = target instanceof EntityPlayer;
			
			if (target.isEntityAlive() && (player && !((EntityPlayer)target).isSpectator() || !player) && (shooter != null && !shooter.isOnSameTeam(target) || shooter == null))
			{
				factor = Math.min((float) range / getDistance(target), 1.0F) * (player ? 0.05F : 1.0F);
				switch(source.damageType)
				{
					default:
						target.hurtResistantTime = 0;
						if (target instanceof EntityLivingBase)
						{
							ReflectionUtil.set(EntityLivingBase.class, target, "recentlyHit", "field_70718_bc", 100);
							if (absoluteDamage > 0.0F)
								((EntityLivingBase) target).setHealth(((EntityLivingBase) target).getHealth() - absoluteDamage * factor);
						}
						target.attackEntityFrom(source, damage * factor);
				}
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float dmg)
	{
		Entity attacker = source.getTrueSource();
		
		if (heath <= 0)
			return false;
			
		if (attacker instanceof EntityLivingBase || attacker instanceof IProjectile)
			heath -= dmg;
		
		if (attacker instanceof IProjectile)
			attacker.setDead();

		if (heath <= 0)
		{
			world.newExplosion(this, posX, posY, posZ, 10.0F, false, false);
			setDead();
		}

		return true;
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}
	
	@Override
	public float getCollisionBorderSize()
	{
		return 1.0F;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		double d0 = getEntityBoundingBox().getAverageEdgeLength();
			
		if (Double.isNaN(d0))
			d0 = 1.0D;
		
		d0 = d0 * 64.0D * 100D;
		return distance < d0 * d0;
	}
	
	public float getSize()
	{
		switch(getType())
		{
			case 0:
				return 4.0F;
			case 1:
				return 8.0F;
			default:
				return 2.0F;
		}
	}
	
	public byte getType()
	{
		return dataManager.get(TYPE).byteValue();
	}
	
	public void setType(byte type)
	{
		dataManager.set(TYPE, Byte.valueOf(type));
	}
}
