package net.minecraft.AgeOfMinecraft.entity.cameos;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;


public class EntitySans extends EntityFriendlyCreature implements IRangedAttackMob
{
	private static final DataParameter<Integer> DODGES = EntityDataManager.createKey(EntitySans.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> BADTIME = EntityDataManager.createKey(EntitySans.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> TIRED = EntityDataManager.createKey(EntitySans.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> SOULTHROW = EntityDataManager.createKey(EntitySans.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> FLASHINGEYE = EntityDataManager.createKey(EntitySans.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> SERIOUS = EntityDataManager.createKey(EntitySans.class, DataSerializers.BOOLEAN);
	
	public EntitySans(World worldIn)
	{
		super(worldIn);
	}

	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor)
	{
	}

	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(DODGES, Integer.valueOf(0));
		this.getDataManager().register(BADTIME, Boolean.valueOf(false));
		this.getDataManager().register(TIRED, Boolean.valueOf(false));
		this.getDataManager().register(SOULTHROW, Boolean.valueOf(false));
		this.getDataManager().register(FLASHINGEYE, Boolean.valueOf(false));
		this.getDataManager().register(SERIOUS, Boolean.valueOf(false));
	}

	public int getDodges()
	{
		return ((Integer)this.dataManager.get(DODGES)).intValue();
	}

	public void setDodges(int age)
	{
		this.dataManager.set(DODGES, Integer.valueOf(age));
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		getEntityAttribute(FITTNESS).setBaseValue(1.0D);
		getEntityAttribute(STRENGTH).setBaseValue(1.0D);
		getEntityAttribute(STAMINA).setBaseValue(100.0D);
		getEntityAttribute(INTELLIGENCE).setBaseValue(100.0D);
		getEntityAttribute(DEXTERITY).setBaseValue(100.0D);
		getEntityAttribute(AGILITY).setBaseValue(100.0D);
	}
	public double getKnockbackResistance()
	{
		return 1D;
	}

	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.setDodges(tagCompund.getInteger("Dodges"));
		this.dataManager.set(BADTIME, Boolean.valueOf(tagCompund.getBoolean("YourGonnaHaveABadTime")));
		this.dataManager.set(TIRED, Boolean.valueOf(tagCompund.getBoolean("Zzzzz")));
		this.dataManager.set(SOULTHROW, Boolean.valueOf(tagCompund.getBoolean("SoulThrow")));
		this.dataManager.set(FLASHINGEYE, Boolean.valueOf(tagCompund.getBoolean("FlashingEye")));
		this.dataManager.set(SERIOUS, Boolean.valueOf(tagCompund.getBoolean("Serious")));
	}
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("Dodges", this.getDodges());
		tagCompound.setBoolean("YourGonnaHaveABadTime", ((Boolean)this.dataManager.get(BADTIME)).booleanValue());
		tagCompound.setBoolean("Zzzzz", ((Boolean)this.dataManager.get(TIRED)).booleanValue());
		tagCompound.setBoolean("SoulThrow", ((Boolean)this.dataManager.get(SOULTHROW)).booleanValue());
		tagCompound.setBoolean("FlashingEye", ((Boolean)this.dataManager.get(FLASHINGEYE)).booleanValue());
		tagCompound.setBoolean("Serious", ((Boolean)this.dataManager.get(SERIOUS)).booleanValue());
	}

	public EnumTier getTier()
	{
		return EnumTier.TIER6;
	}
	public boolean isCameo()
	{
		return true;
	}
	public String getName()
	{
		return "sans";
	}

	public boolean attemptTeleport(double x, double y, double z)
	{
		double d0 = this.posX;
		double d1 = this.posY;
		double d2 = this.posZ;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		boolean flag = false;
		BlockPos blockpos = new BlockPos(this);
		World world = this.world;
		this.getRNG();
		
		if (world.isBlockLoaded(blockpos))
		{
			boolean flag1 = false;
			
			while (!flag1 && blockpos.getY() > 0)
			{
				BlockPos blockpos1 = blockpos.down();
				IBlockState iblockstate = world.getBlockState(blockpos1);
				
				if (iblockstate.isOpaqueCube())
				{
					flag1 = true;
				}
				else
				{
					--this.posY;
					blockpos = blockpos1;
				}
			}

			if (flag1)
			{
				this.setPositionAndUpdate(this.posX, this.posY, this.posZ);
				if (this.isBeingRidden())
				this.getControllingPassenger().setPositionAndUpdate(this.posX, this.posY, this.posZ);
				
				if (world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty())
				{
					flag = true;
				}
			}
		}

		if (!flag)
		{
			this.setPositionAndUpdate(d0, d1, d2);
			return false;
		}
		else
		{
			if (this instanceof EntityCreature)
			{
				((EntityCreature)this).getNavigator().clearPath();
			}

			return true;
		}
	}

	protected boolean teleportRandomly()
	{
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 16.0D;
		double d1 = this.posY + (this.rand.nextDouble() - 0.5D) * 16.0D;
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 16.0D;
		return teleportTo(d0, d1, d2);
	}
	protected boolean teleportToEntity(Entity p_70816_1_)
	{
		double d1 = p_70816_1_.posX + (this.rand.nextDouble() - 0.5D) * 16.0D;
		double d2 = p_70816_1_.posY + (this.rand.nextDouble() - 0.5D) * 16.0D;
		double d3 = p_70816_1_.posZ + (this.rand.nextDouble() - 0.5D) * 16.0D;
		return teleportTo(d1, d2, d3);
	}
	protected boolean teleportTo(double x, double y, double z)
	{
		EnderTeleportEvent event = new EnderTeleportEvent(this, x, y, z, 0.0F);
		if (MinecraftForge.EVENT_BUS.post(event)) return false;
		boolean flag = attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());
		if (flag)
		{
			this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, getSoundCategory(), 1.0F, 1.0F);
			playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
		}
		return flag;
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {}
}
