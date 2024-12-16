package net.mrbt0907.ageofminecraft.entity.tier1;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;
import net.mrbt0907.ageofminecraft.util.mrbtutil.TranslateUtil;

public class EntityRabbit extends EntityEngendered
{
	private static final DataParameter<Integer> RABBIT_TYPE = EntityDataManager.createKey(EntityRabbit.class, DataSerializers.VARINT);
	private int jumpTicks = 0;
	private int jumpDuration = 0;
	private boolean wasOnGround = false;
	private int currentMoveTypeDuration = 0;
	private int carrotTicks = 0;
	
	public EntityRabbit(World worldIn)
	{
		super(worldIn);
		setSize(0.4F, 0.5F);
		jumpHelper = new RabbitJumpHelper(this);
		moveHelper = new RabbitMoveHelper(this);
		tasks.addTask(5, new AIRaidFarm(this));
		tasks.addTask(5, new EntityAIWander(this, 0.6D, 80));
		setMovementSpeed(0.0D);
	}
	
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(RABBIT_TYPE, Integer.valueOf(0));
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("RabbitType", getRabbitType());
		tagCompound.setInteger("MoreCarrotTicks", carrotTicks);
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		setRabbitType(tagCompund.getInteger("RabbitType"));
		carrotTicks = tagCompund.getInteger("MoreCarrotTicks");
	}
	
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		int i = rand.nextInt(6);
		if ((livingdata instanceof RabbitTypeData))
			i = ((RabbitTypeData)livingdata).typeData;
		else
			livingdata = new RabbitTypeData(i);
		setRabbitType(i);
		if (rand.nextInt(2500) == 0)
			setRabbitType(99);
		return livingdata;
	}
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (jumpTicks != jumpDuration)
		{
			if (!world.isRemote && jumpTicks == 0)
				world.setEntityState(this, (byte)1);
			jumpTicks += 1;
		}
		else if (jumpDuration != 0)
		{
			jumpTicks = 0;
			jumpDuration = 0;
		}
	}

	public void updateAITasks()
	{
		super.updateAITasks();
		EntityLivingBase target = getAttackTarget();
		boolean killerRabbit = getRabbitType() == 99;
		boolean isValidTarget = target != null && target.isEntityAlive() && !isOnSameTeam(target);
		
		if (killerRabbit && isEntityAlive() && !isChild() && isValidTarget && getDistanceSq(target) < (double)((width * width) + (target.width * target.width) + 4D) && ticksExisted + getEntityId() % 10 == 0)
			attackEntityAsMob(getAttackTarget());
		
		if (currentMoveTypeDuration > 0)
			currentMoveTypeDuration -= 1;
		if (carrotTicks > 0)
		{
			carrotTicks -= rand.nextInt(3);
			if (carrotTicks < 0)
				carrotTicks = 0;
		}
		if (onGround)
		{
			if (!wasOnGround)
			{
				setJumping(false);
				checkLandingDelay();
			}
			if (killerRabbit && currentMoveTypeDuration == 0 && isValidTarget && getDistanceSq(target) < 16.0D)
			{
				calculateRotationYaw(target.posX, target.posZ);
				moveHelper.setMoveTo(target.posX, target.posY, target.posZ, moveHelper.getSpeed());
				startJumping();
				wasOnGround = true;
			}
			
			RabbitJumpHelper rabbitJumpHelper = (RabbitJumpHelper)jumpHelper;
			if (!rabbitJumpHelper.getIsJumping())
			{
				if (moveHelper.isUpdating() && currentMoveTypeDuration == 0)
				{
					Path pathentity = navigator.getPath();
					Vec3d vec3d = new Vec3d(moveHelper.getX(), moveHelper.getY(), moveHelper.getZ());
					if ((pathentity != null) && (pathentity.getCurrentPathIndex() < pathentity.getCurrentPathLength()))
						vec3d = pathentity.getPosition(this);
					calculateRotationYaw(vec3d.x, vec3d.z);
					startJumping();
				}
			}
			else if (!rabbitJumpHelper.canJump())
			{
				func_175518_cr();
			}
		}
		wasOnGround = onGround;
	}
	
	//----- OVERRIDES -----\\
	protected ResourceLocation getLootTable() {return LootRegistry.ENTITIES_RABBIT;}
	protected SoundEvent getJumpSound() {return SoundEvents.ENTITY_RABBIT_JUMP;}
	protected SoundEvent getAmbientSound() {return SoundEvents.ENTITY_RABBIT_AMBIENT;}
	protected SoundEvent getHurtSound(DamageSource source) {return SoundEvents.ENTITY_RABBIT_HURT;}
	protected SoundEvent getDeathSound() {return SoundEvents.ENTITY_RABBIT_DEATH;}
	
	@Override
	public EnumTier getTier() {return getRabbitType() == 99 ? EnumTier.TIER4 : EnumTier.TIER1;}
	@Override
	public long getBaseVigor() {return 10;}
	@Override
	public long getBaseStrength() {return 0;}
	@Override
	public long getBaseStamina() {return 0;}
	@Override
	public long getBaseIntelligence() {return 0;}
	@Override
	public long getBaseDexterity() {return 0;}
	@Override
	public long getBaseAgility() {return 0;}
	@Override
	public EnumAIStance getDefaultStance() {return EnumAIStance.AGGRESSIVE;}

	public float getBlockPathWeight(BlockPos pos)
	{
		return world.getLightBrightness(pos) - 0.5F;
	}
	
	protected float getJumpUpwardsMotion()
	{
		if ((!collidedHorizontally) && ((!moveHelper.isUpdating()) || (moveHelper.getY() <= posY + 0.5D)))
		{
			Path pathentity = navigator.getPath();
			if ((pathentity != null) && (pathentity.getCurrentPathIndex() < pathentity.getCurrentPathLength()))
			{
				Vec3d vec3d = pathentity.getPosition(this);
				if (vec3d.y > posY)
				{
					return 0.5F;
				}
			}
			return moveHelper.getSpeed() <= 0.6D ? 0.2F : 0.3F;
		}
		return 0.5F;
	}
	
	public void startJumping()
	{
		setJumping(true);
		jumpDuration = 10;
		jumpTicks = 0;
	}
	
	public void spawnRunningParticles() {}
	
	private void calculateRotationYaw(double x, double z)
	{
		rotationYaw = ((float)(MathHelper.atan2(z - posZ, x - posX) * 57.29577951308232D) - 90.0F);
	}
	
	private void func_175518_cr()
	{
		((RabbitJumpHelper)jumpHelper).setCanJump(true);
	}
	
	private void func_175520_cs()
	{
		((RabbitJumpHelper)jumpHelper).setCanJump(false);
	}
	
	private void updateMoveTypeDuration()
	{
		if (moveHelper.getSpeed() < 2.2D)
		{
			currentMoveTypeDuration = 10;
		}
		else
		{
			currentMoveTypeDuration = 1;
		}
	}
	
	private void checkLandingDelay()
	{
		updateMoveTypeDuration();
		func_175520_cs();
	}
	
	public boolean attackEntityAsMob(Entity entityIn)
	{
		if (getRabbitType() == 99)
		{
			entityIn.hurtResistantTime = 0;
			playSound(SoundEvents.ENTITY_RABBIT_ATTACK, 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		}
		return super.attackEntityAsMob(entityIn);
	}
	
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if ((isEntityInvulnerable(source)) || ((source.isMagicDamage()) && (getRabbitType() == 99)))
		{
			return false;
		}
		return super.attackEntityFrom(source, amount);
	}
	
	public int getRabbitType()
	{
		return ((Integer)dataManager.get(RABBIT_TYPE)).intValue();
	}
	
	public void setRabbitType(int rabbitTypeId)
	{
		if (rabbitTypeId == 99)
		{
			getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
			/*(tasks.addTask(4, new AIEvilAttack(this));
			targetTasks.addTask(0, new EntityAIHurtByTarget(this, false, new Class[0]));
			targetTasks.addTask(1, new EntityAILeaderHurtByTarget(this));
			targetTasks.addTask(2, new EntityAILeaderHurtTarget(this));
			isOffensive = true;*/
			getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
			getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
			
			if (!hasCustomName())
				setCustomNameTag(TranslateUtil.translateServer("entity.KillerBunny.name"));
		}
		else
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		dataManager.set(RABBIT_TYPE, Integer.valueOf(rabbitTypeId));
	}
	
	protected void jump()
	{
		super.jump();
		double d0 = moveHelper.getSpeed();
		
		if (d0 > 0.0D)
		{
			double d1 = motionX * motionX + motionZ * motionZ;
			
			if (d1 < 0.010000000000000002D)
			{
				moveRelative(0.0F, 0.0F, 1.0F, 0.1F);
			}
		}

		if (!world.isRemote)
		{
			world.setEntityState(this, (byte)1);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public float setJumpCompletion(float p_175521_1_)
	{
		return jumpDuration == 0 ? 0.0F : (jumpTicks + p_175521_1_) / jumpDuration;
	}
	
	public void setMovementSpeed(double newSpeed)
	{
		getNavigator().setSpeed(newSpeed);
		moveHelper.setMoveTo(moveHelper.getX(), moveHelper.getY(), moveHelper.getZ(), newSpeed);
	}
	
	private boolean isCarrotEaten()
	{
		return carrotTicks == 0;
	}
	
	protected void createEatingParticles()
	{
		BlockCarrot blockcarrot = (BlockCarrot)Blocks.CARROTS;
		IBlockState iblockstate = blockcarrot.withAge(blockcarrot.getMaxAge());
		world.spawnParticle(EnumParticleTypes.BLOCK_DUST, posX + rand.nextFloat() * width * 2.0F - width, posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, 0.0D, 0.0D, 0.0D, new int[] { Block.getStateId(iblockstate) });
		carrotTicks = 40;
	}
	
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 1)
		{
			createRunningParticles();
			jumpDuration = 10;
			jumpTicks = 0;
		}
		else
		{
			super.handleStatusUpdate(id);
		}
	}
	
	public void notifyDataManagerChange(DataParameter<?> key)
	{
		super.notifyDataManagerChange(key);
	}
	/*static class AIEvilAttack extends EntityAIFriendlyAttackMelee
	{
		public AIEvilAttack(EntityRabbit rabbit)
		{
			super(rabbit, 3.0D, true);
		}
	}*/
	protected class AIPanic extends EntityAIPanic
	{
		private EntityRabbit theEntity;
		public AIPanic(EntityRabbit rabbit, double speedIn)
		{
			super(rabbit, speedIn);
			theEntity = rabbit;
		}
		public void updateTask()
		{
			super.updateTask();
			theEntity.setMovementSpeed(speed);
		}
	}
	
	protected class AIRaidFarm extends EntityAIMoveToBlock
	{
		private final EntityRabbit entity;
		private boolean wantsToRaid;
		private boolean canRaid = false;
		
		public AIRaidFarm(EntityRabbit rabbitIn)
		{
			super(rabbitIn, 0.75D, 16);
			entity = rabbitIn;
		}
		
		public boolean shouldExecute()
		{
			if (runDelay <= 0)
			{
				if (!entity.world.getGameRules().getBoolean("mobGriefing"))
					return false;
				canRaid = false;
				wantsToRaid = entity.isCarrotEaten();
				wantsToRaid = true;
			}
			return super.shouldExecute();
		}
		
		public boolean shouldContinueExecuting()
		{
			return canRaid && super.shouldContinueExecuting();
		}
		
		public void updateTask()
		{
			super.updateTask();
			
			entity.getLookHelper().setLookPosition(destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1, destinationBlock.getZ() + 0.5D, 10.0F, entity.getVerticalFaceSpeed());
			if (getIsAboveDestination())
			{
				BlockPos blockpos = destinationBlock.up();
				IBlockState state = entity.world.getBlockState(blockpos);
				Block block = state.getBlock();
				if ((canRaid) && ((block instanceof BlockCarrot)))
				{
					Integer integer = (Integer)state.getValue(BlockCarrot.AGE);
					if (integer.intValue() == 0)
					{
						entity.world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
						entity.world.destroyBlock(blockpos, true);
					}
					else
					{
						entity.world.setBlockState(blockpos, state.withProperty(BlockCarrot.AGE, Integer.valueOf(integer.intValue() - 1)), 2);
						entity.world.playEvent(2001, blockpos, Block.getStateId(state));
					}
					entity.createEatingParticles();
				}
				canRaid = false;
				runDelay = 10;
			}
		}
		
		protected boolean shouldMoveTo(World worldIn, BlockPos pos)
		{
			Block block = worldIn.getBlockState(pos).getBlock();
			if (wantsToRaid && !canRaid && block == Blocks.FARMLAND)
			{
				pos = pos.up();
				IBlockState state = worldIn.getBlockState(pos);
				block = state.getBlock();
				if (block instanceof BlockCarrot && ((BlockCarrot)block).isMaxAge(state))
				{
					canRaid = true;
					return true;
				}
			}
			return false;
		}
	}

	protected class RabbitJumpHelper extends EntityJumpHelper
	{
		private EntityRabbit entity;
		private boolean canJump = false;
		
		public RabbitJumpHelper(EntityRabbit rabbit)
		{
			super(rabbit);
			entity = rabbit;
		}
		public boolean getIsJumping()
		{
			return isJumping;
		}
		public boolean canJump()
		{
			return canJump;
		}
		public void setCanJump(boolean canJump)
		{
			this.canJump = canJump;
		}
		public void doJump()
		{
			if (isJumping)
			{
				entity.startJumping();
				isJumping = false;
			}
		}
	}
	
	protected class RabbitMoveHelper extends EntityMoveHelper
	{
		private EntityRabbit entity;
		private double nextJumpSpeed;
		
		public RabbitMoveHelper(EntityRabbit rabbit)
		{
			super(rabbit);
			entity = rabbit;
		}
		
		public void onUpdateMoveHelper()
		{
			if (entity.onGround && !entity.isJumping && !((EntityRabbit.RabbitJumpHelper)entity.jumpHelper).getIsJumping())
				entity.setMovementSpeed(0.0D);
			else if (isUpdating())
				entity.setMovementSpeed(nextJumpSpeed);
			super.onUpdateMoveHelper();
		}
		
		public void setMoveTo(double x, double y, double z, double speedIn)
		{
			if (entity.isInWater())
				speedIn = 1.5D;
			super.setMoveTo(x, y, z, speedIn);
			if (speedIn > 0.0D)
				nextJumpSpeed = speedIn;
		}
	}
	
	public static class RabbitTypeData implements IEntityLivingData
	{
		public int typeData;
		public RabbitTypeData(int type)
		{
			typeData = type;
		}
	}
}