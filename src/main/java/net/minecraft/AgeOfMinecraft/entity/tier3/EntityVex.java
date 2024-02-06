package net.minecraft.AgeOfMinecraft.entity.tier3;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.Elemental;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyHurtByTarget;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAILeaderHurtByTarget;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAILeaderHurtTarget;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityVex extends EntityFriendlyCreature implements Light, Flying, Elemental
{
	protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.<Byte>createKey(EntityVex.class, DataSerializers.BYTE);
	@Nullable
	private BlockPos boundOrigin;
	
	public EntityVex(World worldIn)
	{
		super(worldIn);
		this.isImmuneToFire = true;
		this.moveHelper = new EntityVex.AIMoveControl(this);
		this.setSize(0.4F, 0.8F);
		this.experienceValue = 3;
	}
	
	public boolean leavesNoCorpse()
	{
		return true;
	}


	/**
	* Tries to move the entity towards the specified location.
	*/
	public void move(MoverType type, double x, double y, double z)
	{
		super.move(type, x, y, z);
		this.doBlockCollisions();
	}
	protected void dropEquipmentUndamaged()
	{
	}
	public boolean canBeMatedWith()
	{
		return false;
	}

	public boolean canBeMarried()
	{
		return false;
	}
	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 1.1F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 0.9F;
	}
	/**
	* Bonus damage vs mobs that implement Flying
	*/
	public float getBonusVSFlying()
	{
		return 1.5F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.1F;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityVex(this.world);
	}

	/**
	* Called to update the entity's position/logic.
	*/
	public void onUpdate()
	{
		this.setSize(0.4F, 0.8F);
		this.noClip = true;
		super.onUpdate();
		this.noClip = false;
		if (this.isEntityAlive())
		this.setNoGravity(true);
		else
		this.setNoGravity(false);
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
		if (!this.isWild())
		{
			if (this.getAttackTarget() != null)this.setBoundOrigin(this.getAttackTarget().getPosition());
			else
			this.setBoundOrigin(this.getJukeboxToDanceTo() != null ? this.getJukeboxToDanceTo().up(2) : (this.getGuardBlock() != null ? new BlockPos(this.randPosX, this.randPosY, this.randPosZ) : this.getOwner().getPosition().up((int)this.getOwner().getEyeHeight())));
		}
		if (!this.world.isRemote && this.isEntityAlive() && this.getAttackTarget() == null && this.getSpecialAttackTimer() > 600)
		{
			List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(24D), Predicates.<EntityLivingBase>and(EntitySelectors.IS_ALIVE));
			
			for (int j2 = 0; j2 < 10 && !list.isEmpty(); ++j2)
			{
				EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));
				
				if (entitylivingbase != this && !this.isOnSameTeam(entitylivingbase) && entitylivingbase.isEntityAlive())
				{
					this.setAttackTarget(entitylivingbase);
					Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
					this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1D);
					break;
				}
			}
		}
	}

	protected void initEntityAI()
	{
		super.initEntityAI();
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(4, new EntityVex.AIChargeAttack());
		this.tasks.addTask(8, new EntityVex.AIMoveRandom());
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.0D, 24.0F, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(0, new EntityAIFriendlyHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(1, new EntityAILeaderHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAILeaderHurtTarget(this));
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(VEX_FLAGS, Byte.valueOf((byte)0));
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		
		if (compound.hasKey("BoundX"))
		{
			this.boundOrigin = new BlockPos(compound.getInteger("BoundX"), compound.getInteger("BoundY"), compound.getInteger("BoundZ"));
		}
	}

	/**
	* (abstract) Protected helper method to write subclass entity data to NBT.
	*/
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		
		if (this.boundOrigin != null)
		{
			compound.setInteger("BoundX", this.boundOrigin.getX());
			compound.setInteger("BoundY", this.boundOrigin.getY());
			compound.setInteger("BoundZ", this.boundOrigin.getZ());
		}
	}

	@Nullable
	public BlockPos getBoundOrigin()
	{
		return this.boundOrigin;
	}

	public void setBoundOrigin(@Nullable BlockPos boundOriginIn)
	{
		this.boundOrigin = boundOriginIn;
	}

	private boolean getVexFlag(int p_190656_1_)
	{
		int i = ((Byte)this.dataManager.get(VEX_FLAGS)).byteValue();
		return (i & p_190656_1_) != 0;
	}

	private void setVexFlag(int p_190660_1_, boolean p_190660_2_)
	{
		int i = ((Byte)this.dataManager.get(VEX_FLAGS)).byteValue();
		
		if (p_190660_2_)
		{
			i = i | p_190660_1_;
		}
		else
		{
			i = i & ~p_190660_1_;
		}

		this.dataManager.set(VEX_FLAGS, Byte.valueOf((byte)(i & 255)));
	}

	public boolean isCharging()
	{
		return this.getVexFlag(1) || this.getJukeboxToDanceTo() != null;
	}

	public void setIsCharging(boolean p_190648_1_)
	{
		this.setVexFlag(1, p_190648_1_);
	}

	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_VEX_AMBIENT;
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_VEX_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_VEX_HURT;
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15728880;
	}

	/**
	* Gets how bright this entity is.
	*/
	public float getBrightness()
	{
		return 1.0F;
	}

	/**
	* Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
	* when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
	*/
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		return super.onInitialSpawn(difficulty, livingdata);
	}

	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_VEX;
	}
	/**
	* Gives armor or weapon for entity based on given DifficultyInstance
	*/
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	{
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		this.setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
	}

	class AIChargeAttack extends EntityAIBase
	{
		public AIChargeAttack()
		{
			this.setMutexBits(1);
		}

		/**
		* Returns whether the EntityAIBase should begin execution.
		*/
		public boolean shouldExecute()
		{
			return EntityVex.this.getAttackTarget() != null && EntityVex.this.getSpecialAttackTimer() > 600 ? true : EntityVex.this.getAttackTarget() != null && !EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.rand.nextInt(7) == 0 ? EntityVex.this.getDistanceSq(EntityVex.this.getAttackTarget()) > 4.0D : false;
		}

		/**
		* Returns whether an in-progress EntityAIBase should continue executing
		*/
		public boolean shouldContinueExecuting()
		{
			return EntityVex.this.getAttackTarget() != null && EntityVex.this.getSpecialAttackTimer() > 600 ? true : EntityVex.this.getMoveHelper().isUpdating() && EntityVex.this.isCharging() && EntityVex.this.getAttackTarget() != null && EntityVex.this.getAttackTarget().isEntityAlive();
		}

		/**
		* Execute a one shot task or start executing a continuous task
		*/
		public void startExecuting()
		{
			EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();
			Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
			EntityVex.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1D);
			EntityVex.this.setIsCharging(true);
			EntityVex.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
			if (EntityVex.this.getAttackTarget() != null && EntityVex.this.getSpecialAttackTimer() <= 0 && EntityVex.this.isHero())
			{
				setSpecialAttackTimer(900);
				playSound(SoundEvents.ENTITY_VEX_CHARGE, 10.0F, 0.75F);
			}
		}

		/**
		* Resets the task
		*/
		public void resetTask()
		{
			EntityVex.this.setIsCharging(false);
		}

		/**
		* Updates the task
		*/
		public void updateTask()
		{
			EntityLivingBase entitylivingbase = EntityVex.this.getAttackTarget();
			if (entitylivingbase != null)
				if (EntityVex.this.getDistanceSq(entitylivingbase) <= (EntityVex.this.width * EntityVex.this.width) + (entitylivingbase.width * entitylivingbase.width) + 9D)
				{
					EntityVex.this.attackEntityAsMob(entitylivingbase);
				}
				else
				{
					double d0 = EntityVex.this.getDistanceSq(entitylivingbase);
					
					if (d0 < 9.0D)
					{
						Vec3d vec3d = entitylivingbase.getPositionEyes(1.0F);
						EntityVex.this.moveHelper.setMoveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
					}
				}
		}
	}

	class AIMoveControl extends EntityMoveHelper
	{
		public AIMoveControl(EntityVex vex)
		{
			super(vex);
		}

		public void onUpdateMoveHelper()
		{
			if (this.action == EntityMoveHelper.Action.MOVE_TO)
			{
				double d0 = this.posX - EntityVex.this.posX;
				double d1 = this.posY - EntityVex.this.posY;
				double d2 = this.posZ - EntityVex.this.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				d3 = (double)MathHelper.sqrt(d3);
				
				if (d3 < EntityVex.this.getEntityBoundingBox().getAverageEdgeLength())
				{
					this.action = EntityMoveHelper.Action.WAIT;
					EntityVex.this.motionX *= 0.5D;
					EntityVex.this.motionY *= 0.5D;
					EntityVex.this.motionZ *= 0.5D;
				}
				else
				{
					EntityVex.this.motionX += d0 / d3 * 0.2D * this.speed;
					EntityVex.this.motionY += d1 / d3 * 0.05D * this.speed;
					EntityVex.this.motionZ += d2 / d3 * 0.2D * this.speed;
					
					if (EntityVex.this.getAttackTarget() == null)
					{
						EntityVex.this.rotationYaw = -((float)MathHelper.atan2(EntityVex.this.motionX, EntityVex.this.motionZ)) * (180F / (float)Math.PI);
						EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
					}
					else
					{
						double d4 = EntityVex.this.getAttackTarget().posX - EntityVex.this.posX;
						double d5 = EntityVex.this.getAttackTarget().posZ - EntityVex.this.posZ;
						EntityVex.this.rotationYaw = -((float)MathHelper.atan2(d4, d5)) * (180F / (float)Math.PI);
						EntityVex.this.renderYawOffset = EntityVex.this.rotationYaw;
						EntityVex.this.faceEntity(EntityVex.this.getAttackTarget(), 10F, 40F);
					}
				}
			}
		}
	}

	class AIMoveRandom extends EntityAIBase
	{
		public AIMoveRandom()
		{
			this.setMutexBits(1);
		}

		/**
		* Returns whether the EntityAIBase should begin execution.
		*/
		public boolean shouldExecute()
		{
			return !EntityVex.this.getMoveHelper().isUpdating() && (EntityVex.this.rand.nextInt(7) == 0 || EntityVex.this.getJukeboxToDanceTo() != null);
		}

		/**
		* Returns whether an in-progress EntityAIBase should continue executing
		*/
		public boolean shouldContinueExecuting()
		{
			return false;
		}

		/**
		* Updates the task
		*/
		public void updateTask()
		{
			BlockPos blockpos = EntityVex.this.getBoundOrigin();
			
			if (blockpos == null)
			{
				blockpos = new BlockPos(EntityVex.this);
			}

			for (int i = 0; i < 3; ++i)
			{
				BlockPos blockpos1 = blockpos.add(EntityVex.this.rand.nextInt(15) - 7, EntityVex.this.rand.nextInt(11) - 5, EntityVex.this.rand.nextInt(15) - 7);
				
				if (EntityVex.this.world.isAirBlock(blockpos1))
				{
					EntityVex.this.moveHelper.setMoveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
					
					if (EntityVex.this.getAttackTarget() == null)
					{
						if (EntityVex.this.getJukeboxToDanceTo() != null)
						EntityVex.this.getLookHelper().setLookPosition((double)EntityVex.this.getJukeboxToDanceTo().getX() + 0.5D, (double)EntityVex.this.getJukeboxToDanceTo().getY() + 0.5D, (double)EntityVex.this.getJukeboxToDanceTo().getZ() + 0.5D, 180.0F, 0.0F);
						else
						EntityVex.this.getLookHelper().setLookPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}

					break;
				}
			}
		}
	}
	
	@Override
	public EnumTier getTier()
	{
		return EnumTier.TIER1;
	}
}