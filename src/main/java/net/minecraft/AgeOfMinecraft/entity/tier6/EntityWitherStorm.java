package net.minecraft.AgeOfMinecraft.entity.tier6;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.endermanofdoom.mac.music.IMusicInteractable;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.Undead;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAvoidEntitySPC;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityTippedArrowOther;
import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.registry.EItem;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWitherStorm extends EntityFriendlyCreature implements Massive, Armored, Flying, Undead, IMusicInteractable
{
	private static final DataParameter<Boolean> DOESNT_HAVE_COMMAND_BLOCK = EntityDataManager.createKey(EntityWitherStorm.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> SIZE = EntityDataManager.createKey(EntityWitherStorm.class, DataSerializers.VARINT);
	private float[] field_82220_d = new float[2];
	private float[] field_82221_e = new float[2];
	private float[] field_82217_f = new float[2];
	private float[] field_82218_g = new float[2];
	public EntityWitherStormHead centerHead = new EntityWitherStormHead(this.world);
	public EntityWitherStormHead rightHead = new EntityWitherStormHead(this.world);
	public EntityWitherStormHead leftHead = new EntityWitherStormHead(this.world);
	public EntityWitherStormTentacle tentacle1 = new EntityWitherStormTentacle(this.world);
	public EntityWitherStormTentacle tentacle2 = new EntityWitherStormTentacle(this.world);
	public EntityWitherStormTentacle tentacle3 = new EntityWitherStormTentacle(this.world);
	public EntityWitherStormTentacle tentacle4 = new EntityWitherStormTentacle(this.world);
	public EntityWitherStormTentacle tentacle5 = new EntityWitherStormTentacle(this.world);
	public EntityWitherStormTentacleDevourer tentacledevourer1 = new EntityWitherStormTentacleDevourer(this.world);
	public EntityWitherStormTentacleDevourer tentacledevourer2 = new EntityWitherStormTentacleDevourer(this.world);
	public EntityWitherStorm(World worldIn)
	{
		super(worldIn);
		setSize(9.0F, 32.0F);
		this.isOffensive = true;
		this.isImmuneToFire = true;
		this.noClip = true;
		((PathNavigateGround)getNavigator()).setCanSwim(true);
		this.moveHelper = new WitherStormMoveHelper(this);
		this.tasks.addTask(5, new AIRandomFly(this));
		this.tasks.addTask(7, new AILookAround());
		this.experienceValue = 50000;
		this.setLevel(300);
		this.ignoreFrustumCheck = true;
		for (EntityPlayer entityplayer : worldIn.playerEntities)
		{
			worldIn.playSound(null, entityplayer.getPosition(), ESound.witherStormFinish, this.getSoundCategory(), Float.MAX_VALUE, 1.0F);
		}

		if (!worldIn.isRemote)
		{
			this.centerHead = new EntityWitherStormHead(worldIn);
			this.centerHead.residentWitherStorm = this;
			this.centerHead.copyLocationAndAnglesFrom(this);
			this.centerHead.setOwnerId(this.getOwnerId());
			worldIn.spawnEntity(this.centerHead);
			
			this.rightHead = new EntityWitherStormHead(worldIn);
			this.rightHead.residentWitherStorm = this;
			this.rightHead.copyLocationAndAnglesFrom(this);
			this.rightHead.setOwnerId(this.getOwnerId());
			worldIn.spawnEntity(this.rightHead);
			
			this.leftHead = new EntityWitherStormHead(worldIn);
			this.leftHead.residentWitherStorm = this;
			this.leftHead.copyLocationAndAnglesFrom(this);
			this.leftHead.setOwnerId(this.getOwnerId());
			worldIn.spawnEntity(this.leftHead);
			
			this.tentacle1 = new EntityWitherStormTentacle(worldIn);
			this.tentacle1.residentWitherStorm = this;
			this.tentacle1.copyLocationAndAnglesFrom(this);
			this.tentacle1.setOwnerId(this.getOwnerId());
			this.world.spawnEntity(this.tentacle1);
			
			this.tentacle2 = new EntityWitherStormTentacle(worldIn);
			this.tentacle2.residentWitherStorm = this;
			this.tentacle2.copyLocationAndAnglesFrom(this);
			this.tentacle2.setOwnerId(this.getOwnerId());
			worldIn.spawnEntity(this.tentacle2);
			
			this.tentacle3 = new EntityWitherStormTentacle(worldIn);
			this.tentacle3.residentWitherStorm = this;
			this.tentacle3.copyLocationAndAnglesFrom(this);
			this.tentacle3.setOwnerId(this.getOwnerId());
			worldIn.spawnEntity(this.tentacle3);
			
			this.tentacle4 = new EntityWitherStormTentacle(worldIn);
			this.tentacle4.residentWitherStorm = this;
			this.tentacle4.copyLocationAndAnglesFrom(this);
			this.tentacle4.setOwnerId(this.getOwnerId());
			worldIn.spawnEntity(this.tentacle4);
			
			this.tentacle5 = new EntityWitherStormTentacle(worldIn);
			this.tentacle5.residentWitherStorm = this;
			this.tentacle5.copyLocationAndAnglesFrom(this);
			this.tentacle5.setOwnerId(this.getOwnerId());
			worldIn.spawnEntity(this.tentacle5);
			
			this.tentacledevourer1 = new EntityWitherStormTentacleDevourer(worldIn);
			this.tentacledevourer1.residentWitherStorm = this;
			this.tentacledevourer1.copyLocationAndAnglesFrom(this);
			this.tentacledevourer1.setOwnerId(this.getOwnerId());
			worldIn.spawnEntity(this.tentacledevourer1);
			
			this.tentacledevourer2 = new EntityWitherStormTentacleDevourer(worldIn);
			this.tentacledevourer2.residentWitherStorm = this;
			this.tentacledevourer2.copyLocationAndAnglesFrom(this);
			this.tentacledevourer2.setOwnerId(this.getOwnerId());
			worldIn.spawnEntity(this.tentacledevourer2);
		}
	}
	
	public boolean leavesNoCorpse()
	{
		return true;
	}
	
	public boolean isChild()
	{
		return false;
	}
	public void setChild(boolean childZombie) { }
	
	public void updateBossBar()
	{
		super.updateBossBar();
		this.bossInfo.setColor(BossInfo.Color.PURPLE);
		this.bossInfo.setDarkenSky(true);
	}

	public int getSpawnTimer()
	{
		return 0;
	}

	public boolean isBoss()
	{
		return true;
	}

	/**
	* Get this Entity's EnumCreatureAttribute
	*/
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return ESetup.WITHER_STORM;
	}

	public double getDefaultStrengthStat()
	{
		return 100F;
	}

	public double getDefaultStaminaStat()
	{
		return 100F;
	}

	public double getDefaultIntelligenceStat()
	{
		return 32F + (rand.nextFloat() * 32F);
	}

	public double getDefaultDexterityStat()
	{
		return 64F + (rand.nextFloat() * 24F);
	}

	public double getDefaultAgilityStat()
	{
		return 16F + (rand.nextFloat() * 16F);
	}

	public double getDefaultFittnessStat()
	{
		return 1F;
	}

	public boolean canUseGuardBlock()
	{
		return false;
	}

	public boolean canWearEasterEggs()
	{
		return false;
	}

	public EnumTier getTier()
	{
		return EnumTier.TIER6;
	}
	public void onKillCommand()
	{
		super.onKillCommand();
		if (centerHead != null)
			this.centerHead.setDead();
		if (rightHead != null)
			this.rightHead.setDead();
		if (leftHead != null)
			this.leftHead.setDead();
		if (tentacle1 != null)
			this.tentacle1.setDead();
		if (tentacle2 != null)
			this.tentacle2.setDead();
		if (tentacle3 != null)
			this.tentacle3.setDead();
		if (tentacle4 != null)
			this.tentacle4.setDead();
		if (tentacle5 != null)
			this.tentacle5.setDead();
		if (tentacledevourer1 != null)
			this.tentacledevourer1.setDead();
		if (tentacledevourer2 != null)
			this.tentacledevourer2.setDead();
		this.centerHead = null;
		this.rightHead = null;
		this.leftHead = null;
		this.tentacle1 = null;
		this.tentacle2 = null;
		this.tentacle3 = null;
		this.tentacle4 = null;
		this.tentacle5 = null;
		this.tentacledevourer1 = null;
		this.tentacledevourer2 = null;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12500D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.8D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(100D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(30.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(20.0D);
	}

	public double getKnockbackResistance()
	{
		return 1D;
	}
	public String getName()
	{
		if (hasCustomName())
		{
			return getCustomNameTag();
		}
		return this.doesntContainACommandBlock() ? "Severed Wither Storm" : "The Wither Storm";
	}
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SIZE, Integer.valueOf(0));
		this.dataManager.register(DOESNT_HAVE_COMMAND_BLOCK, Boolean.valueOf(false));
	}
	public void outOfWorld() { }
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("Growth", getSize());
		tagCompound.setBoolean("NoCommandBlock", this.doesntContainACommandBlock());
	}
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		Grow(tagCompund.getInteger("Growth"));
		this.setNotContainingCommandBlock(tagCompund.getBoolean("NoCommandBlock"));
	}
	protected SoundEvent getAmbientSound()
	{
		return ESound.witherStormAmbient;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return this.doesntContainACommandBlock() ? ESound.witherStormHurt : ESound.witherStormHurtCommandBlock;
	}
	protected SoundEvent getDeathSound()
	{
		return this.doesntContainACommandBlock() ? ESound.witherStormHurt : ESound.witherStormDeath;
	}
	protected float getSoundVolume()
	{
		return isSneaking() ? 1.0F : 100.0F;
	}
	protected float getSoundPitch()
	{
		return 1.0F;
	}
	public float getEyeHeight()
	{
		return 0.5F;
	}
	public boolean canBePushed()
	{
		return false;
	}
	public EnumWitherStormPhase getPhase()
	{
		return (getSize() <= 250000) && (getSize() > 50000) ? EnumWitherStormPhase.Devourer : getSize() > 250000 ? EnumWitherStormPhase.ThunderStorm : EnumWitherStormPhase.Destroyer;
	}

	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (!this.isWild() && getDistanceSq(getOwner()) >= 48400.0D)
		{
			setLocationAndAngles(getOwner().posX, getOwner().posY, getOwner().posZ, this.rotationYaw, this.rotationPitch);
		}

		if (!this.doesntContainACommandBlock() && !world.isRemote)
		{
			if (this.isEntityAlive())
			MobChunkLoader.updateLoaded(this);
			else
			MobChunkLoader.stopLoading(this);
		}

		this.getNavigator().clearPath();
		this.experienceValue = this.getSize();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((doesntContainACommandBlock() ? 1000D : (this.getSize())));
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(doesntContainACommandBlock() ? 20D : 24.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(doesntContainACommandBlock() ? 10D : 20.0D);
		
		if (!this.doesntContainACommandBlock() && getSize() >= 300000 && !this.world.isRemote && this.ticksExisted % 1000 == 0)
		{
			EntityWitherStorm entityzombie = new EntityWitherStorm(this.world);
			entityzombie.copyLocationAndAnglesFrom(this);
			entityzombie.setNoAI(isAIDisabled());
			this.world.spawnEntity(entityzombie);
			entityzombie.setOwnerId(getOwnerId());
			entityzombie.setNotContainingCommandBlock(true);
			entityzombie.motionX = rand.nextDouble() - 0.5D;
			entityzombie.motionZ = rand.nextDouble() - 0.5D;
			this.Grow(this.getSize() - 12500);
		}

		if (this.motionX > 1D)
		this.motionX = 1D;
		if (this.motionZ < -1D)
		this.motionZ = -1D;
		if (this.motionY > 1D)
		this.motionY = 1D;
		if (this.motionY < -1D)
		this.motionY = -1D;
		if (this.motionX > 1D)
		this.motionX = 1D;
		if (this.motionZ < -1D)
		this.motionZ = -1D;
		
		if (!this.isWild())
		{
			this.getOwner().removeActivePotionEffect(MobEffects.WITHER);
		}

		if (this.deathTicks <= 0)
		{
			if (!this.world.isRemote)
			{
				if (this.centerHead != null)
				{
					if (this.centerHead.isDead)
					{
						this.centerHead = null;
						this.attackEntityFrom(DamageSource.GENERIC, 1000F);
					}
					else
					{
					if (this.getJukeboxToDanceTo() != null)
					this.centerHead.setJukeboxToDanceTo(getJukeboxToDanceTo());
					
					if (!this.isWild())
					this.centerHead.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.centerHead = new EntityWitherStormHead(this.world);
					this.centerHead.residentWitherStorm = this;
					this.centerHead.copyLocationAndAnglesFrom(this);
					this.centerHead.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.centerHead);
					centerHead.onUpdate();
				}

				if (this.rightHead != null)
				{
					if (this.rightHead.isDead)
					{
						this.rightHead = null;
						this.attackEntityFrom(DamageSource.GENERIC, 1000F);
					}
					else
					{
					if (this.getJukeboxToDanceTo() != null)
					this.rightHead.setJukeboxToDanceTo(getJukeboxToDanceTo());
					
					if (!this.isWild())
					this.rightHead.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.rightHead = new EntityWitherStormHead(this.world);
					this.rightHead.residentWitherStorm = this;
					this.rightHead.copyLocationAndAnglesFrom(this);
					this.rightHead.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.rightHead);
					rightHead.onUpdate();
				}

				if (this.leftHead != null)
				{
					if (this.leftHead.isDead)
					{
						this.leftHead = null;
						this.attackEntityFrom(DamageSource.GENERIC, 1000F);
					}
					else
					{
					if (this.getJukeboxToDanceTo() != null)
					this.leftHead.setJukeboxToDanceTo(getJukeboxToDanceTo());
					
					if (!this.isWild())
					this.leftHead.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.leftHead = new EntityWitherStormHead(this.world);
					this.leftHead.residentWitherStorm = this;
					this.leftHead.copyLocationAndAnglesFrom(this);
					this.leftHead.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.leftHead);
					leftHead.onUpdate();
				}

				if (this.tentacle1 != null)
				{
					if (this.tentacle1.isDead)
					{
						this.tentacle1 = null;
						this.attackEntityFrom(DamageSource.GENERIC, 100F);
					}
					else
					{
					if (this.getJukeboxToDanceTo() != null)
					this.tentacle1.setJukeboxToDanceTo(getJukeboxToDanceTo());
					
					if (!this.isWild())
					this.tentacle1.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.tentacle1 = new EntityWitherStormTentacle(this.world);
					this.tentacle1.residentWitherStorm = this;
					this.tentacle1.copyLocationAndAnglesFrom(this);
					this.tentacle1.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.tentacle1);
					tentacle1.onUpdate();
				}

				if (this.tentacle2 != null)
				{
					if (this.tentacle2.isDead)
					{
						this.tentacle2 = null;
						this.attackEntityFrom(DamageSource.GENERIC, 100F);
					}
					else
					{
					if (this.getJukeboxToDanceTo() != null)
					this.tentacle2.setJukeboxToDanceTo(getJukeboxToDanceTo());
					
					if (!this.isWild())
					this.tentacle2.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.tentacle2 = new EntityWitherStormTentacle(this.world);
					this.tentacle2.residentWitherStorm = this;
					this.tentacle2.copyLocationAndAnglesFrom(this);
					this.tentacle2.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.tentacle1);
					tentacle2.onUpdate();
				}

				if (this.tentacle3 != null)
				{
					if (this.tentacle3.isDead)
					{
						this.tentacle3 = null;
						this.attackEntityFrom(DamageSource.GENERIC, 100F);
					}
					else
					{
						
						if (this.getJukeboxToDanceTo() != null)
						this.tentacle3.setJukeboxToDanceTo(getJukeboxToDanceTo());
						
						if (!this.isWild())
						this.tentacle3.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.tentacle3 = new EntityWitherStormTentacle(this.world);
					this.tentacle3.residentWitherStorm = this;
					this.tentacle3.copyLocationAndAnglesFrom(this);
					this.tentacle3.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.tentacle3);
					tentacle3.onUpdate();
				}

				if (this.tentacle4 != null)
				{
					if (this.tentacle4.isDead)
					{
						this.tentacle4 = null;
						this.attackEntityFrom(DamageSource.GENERIC, 100F);
					}
					else
					{
					if (this.getJukeboxToDanceTo() != null)
					this.tentacle4.setJukeboxToDanceTo(getJukeboxToDanceTo());
					
					if (!this.isWild())
					this.tentacle4.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.tentacle4 = new EntityWitherStormTentacle(this.world);
					this.tentacle4.residentWitherStorm = this;
					this.tentacle4.copyLocationAndAnglesFrom(this);
					this.tentacle4.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.tentacle4);
					tentacle4.onUpdate();
				}

				if (this.tentacle5 != null)
				{
					if (this.tentacle5.isDead)
					{
						this.tentacle5 = null;
						this.attackEntityFrom(DamageSource.GENERIC, 100F);
					}
					else
					{
					if (this.getJukeboxToDanceTo() != null)
					this.tentacle5.setJukeboxToDanceTo(getJukeboxToDanceTo());
					
					if (!this.isWild())
					this.tentacle5.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.tentacle5 = new EntityWitherStormTentacle(this.world);
					this.tentacle5.residentWitherStorm = this;
					this.tentacle5.copyLocationAndAnglesFrom(this);
					this.tentacle5.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.tentacle5);
					tentacle5.onUpdate();
				}

				if (this.tentacledevourer1 != null)
				{
					if (this.tentacledevourer1.isDead)
					{
						this.tentacledevourer1 = null;
						this.attackEntityFrom(DamageSource.GENERIC, 200F);
					}
					else
					{
					if (this.getJukeboxToDanceTo() != null)
					this.tentacledevourer1.setJukeboxToDanceTo(getJukeboxToDanceTo());
					
					if (!this.isWild())
					this.tentacledevourer1.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.tentacledevourer1 = new EntityWitherStormTentacleDevourer(this.world);
					this.tentacledevourer1.residentWitherStorm = this;
					this.tentacledevourer1.copyLocationAndAnglesFrom(this);
					this.tentacledevourer1.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.tentacledevourer1);
					tentacledevourer1.onUpdate();
				}

				if (this.tentacledevourer2 != null)
				{
					if (this.tentacledevourer2.isDead)
					{
						this.tentacledevourer2 = null;
						this.attackEntityFrom(DamageSource.GENERIC, 200F);
					}
					else
					{
						if (this.getJukeboxToDanceTo() != null)
						this.tentacledevourer2.setJukeboxToDanceTo(getJukeboxToDanceTo());
						
						if (!this.isWild())
						this.tentacledevourer2.setOwnerId(getOwnerId());
					}
				}
				else
				{
					this.tentacledevourer2 = new EntityWitherStormTentacleDevourer(this.world);
					this.tentacledevourer2.residentWitherStorm = this;
					this.tentacledevourer2.copyLocationAndAnglesFrom(this);
					this.tentacledevourer2.setOwnerId(this.getOwnerId());
					this.world.spawnEntity(this.tentacledevourer2);
					tentacledevourer2.onUpdate();
				}
			}

			if (this.doesntContainACommandBlock())
			{
				if (tentacle1 != null)
					this.tentacle1.rotationYawHead = (this.rotationYawHead - 30.0F);
				if (tentacle2 != null)
					this.tentacle2.rotationYawHead = (this.rotationYawHead + 90.0F);
				if (tentacle3 != null)
					this.tentacle3.rotationYawHead = (this.rotationYawHead - 130.0F);
				if (tentacle4 != null)
					this.tentacle4.rotationYawHead = (this.rotationYawHead + 120.0F);
				if (tentacle5 != null)
					this.tentacle5.rotationYawHead = (this.rotationYawHead - 100.0F);
				if (tentacledevourer1 != null)
					this.tentacledevourer1.rotationYawHead = (this.rotationYawHead - 180.0F);
				if (tentacledevourer2 != null)
					this.tentacledevourer2.rotationYawHead = (this.rotationYawHead + 180.0F);
				if (centerHead != null)
					this.centerHead.renderYawOffset = this.centerHead.rotationYaw = this.rotationYawHead;
				if (rightHead != null)
					this.rightHead.renderYawOffset = this.rightHead.rotationYaw = this.rotationYawHead + 10.0F;
				if (leftHead != null)
					this.leftHead.renderYawOffset = this.leftHead.rotationYaw = this.rotationYawHead - 10.0F;
			}
			else
			{
				if (tentacle1 != null)
					this.tentacle1.rotationYawHead = (this.rotationYawHead - 60.0F);
				if (tentacle2 != null)
					this.tentacle2.rotationYawHead = (this.rotationYawHead + 60.0F);
				if (tentacle3 != null)
					this.tentacle3.rotationYawHead = (this.rotationYawHead - 120.0F);
				if (tentacle4 != null)
					this.tentacle4.rotationYawHead = (this.rotationYawHead + 105.0F);
				if (tentacle5 != null)
					this.tentacle5.rotationYawHead = (this.rotationYawHead - 180.0F);
				if (tentacledevourer1 != null)
					this.tentacledevourer1.rotationYawHead = (this.rotationYawHead - 120.0F);
				if (tentacledevourer2 != null)
					this.tentacledevourer2.rotationYawHead = (this.rotationYawHead + 120.0F);
				if (centerHead != null)
					this.centerHead.renderYawOffset = this.centerHead.rotationYaw = this.rotationYawHead;
				if (rightHead != null)
					this.rightHead.renderYawOffset = this.rightHead.rotationYaw = this.rotationYawHead + 30.0F;
				if (leftHead != null)
					this.leftHead.renderYawOffset = this.leftHead.rotationYaw = this.rotationYawHead - 30.0F;
			}
		}

		if ((this.ticksExisted + this.getEntityId()) % 100 == 0)
		{
			playSound(ESound.commandBlockWitherHum, 0.25F, 1.0F);
		}

		float rot = this.rotationYawHead * 0.017453292F;
		float oned = MathHelper.sin(rot);
		float twod = MathHelper.cos(rot);
		
		if (this.getHealth() <= 0F)
		{
			float f13 = (this.rand.nextFloat() - 0.5F) * 12.0F;
			float f15 = (this.rand.nextFloat() - 0.5F) * 36.0F;
			float f17 = (this.rand.nextFloat() - 0.5F) * 12.0F;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f13, this.posY - 4.0D + f15, this.posZ + f17, 0.0D, 0.0D, 0.0D, new int[0]);
		}
		if (!this.world.isRemote && (getOwner() != null) && (this.posY <= getOwner().posY) && (this.posY >= getOwner().posY - 0.5D) && (!isSneaking()))
		{
			createEngenderModExplosion(this, this.posX, this.posY, this.posZ, 8.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
		}
		this.onGround = false;
		this.isAirBorne = true;
		this.noClip = true;
		if (getSize() < 12500)
		{
			Grow(12500);
		}
		if (getSize() >= 250000)
		{
			WorldInfo worldinfo = this.world.getWorldInfo();
			worldinfo.setCleanWeatherTime(0);
			worldinfo.setRainTime(200);
			worldinfo.setThunderTime(200);
			worldinfo.setRaining(true);
			worldinfo.setThundering(true);
		}
		if (this.motionY > 1.0D)
		{
			this.motionY = 0.0D;
		}

		this.renderYawOffset = (this.rotationYaw = this.rotationYawHead);
		
		if (this.deathTicks <= 0)
		{
			if ((this.recentlyHit <= 50) && (getHealth() <= 0.0F))setHealth(1.0F);
			else if ((this.recentlyHit > 50) && (getHealth() <= 0.0F))setHealth(0.0F);
			
			if (this.ticksExisted % 20 == 0 && !this.isAIDisabled() && this.isEntityAlive() && this.world.getGameRules().getBoolean("mobGriefing") && !this.doesntContainACommandBlock())
			{
				for (int i1 = 0; i1 < this.getSize() / 100; i1++)
				{
					if (this.rand.nextInt(2) == 0)
					{
						int l1 = MathHelper.floor(this.posX) + MathHelper.getInt(this.rand, 2, 128) * MathHelper.getInt(this.rand, -1, 1);
						int i2 = MathHelper.floor(this.posZ) + MathHelper.getInt(this.rand, 2, 128) * MathHelper.getInt(this.rand, -1, 1);
						BlockPos blockpos = new BlockPos(l1, MathHelper.floor(this.posY), i2);
						blockpos = this.world.getTopSolidOrLiquidBlock(blockpos);
						IBlockState iblockstate = this.world.getBlockState(blockpos);
						Block block = iblockstate.getBlock();
						if (this.world.getBlockState(blockpos.up()).getBlock().isAir(this.world.getBlockState(blockpos.up()), this.world, blockpos.up()) && (!block.isAir(iblockstate, this.world, blockpos)) && !this.world.isRemote && this.world.isAreaLoaded(blockpos, blockpos) && iblockstate.getBlockHardness(this.world, new BlockPos(l1, blockpos.getY(), i2)) != -1)
						{
							if (iblockstate.getMaterial().isLiquid())
								this.world.setBlockToAir(new BlockPos(l1, blockpos.getY(), i2));
							else
								this.world.spawnEntity(new EntityFallingBlock(world, l1, blockpos.getY(), i2, iblockstate));
						}
					}
				}
			}
			
			if (this.ticksExisted % 60 == 0)
			{
				List<EntityCreature> list = this.world.getEntitiesWithinAABB(EntityCreature.class, getEntityBoundingBox().grow(128D), EntitySelectors.NOT_SPECTATING);
				if (list != null && !list.isEmpty())
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityCreature entity = (EntityCreature)list.get(i1);
						EntityAIAvoidEntitySPC<EntityWitherStorm> ai = new EntityAIAvoidEntitySPC<EntityWitherStorm>(entity, EntityWitherStorm.class, 128F, 1.5D, 1.5D);
						
						if (entity != null && entity.isEntityAlive() && (entity.isNonBoss()) && !(entity instanceof net.minecraft.entity.monster.EntityEnderman) && !(entity instanceof EntityFriendlyCreature))
						{
							boolean truth = false;
							for (EntityAITaskEntry entry : entity.tasks.taskEntries)
							{
								if (entry.action.equals(ai))
								{
									entity.tasks.addTask(0, ai);
									truth = true;
									break;
								}
							}
							
							if (!truth)
							{
								list.remove(entity);
							}
						}
						else
							list.remove(entity);
					}
				}
			}

			if (this.ticksExisted > 20 && !this.doesntContainACommandBlock())
			{
				List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, getEntityBoundingBox().grow(128.0D, 128.0D, 128.0D), EntitySelectors.NOT_SPECTATING);
				if ((list != null) && (!list.isEmpty()) && this.isEntityAlive())
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						Entity entity = (Entity)list.get(i1);
						if (!(entity instanceof EntityFallingBlock) && entity != null && entity.getEyeHeight() == 0.0F && entity.height == 0.98F && entity.width == 0.98F)
						{
							entity.noClip = entity.collidedHorizontally || entity.collidedVertically;
							double d01 = this.posX - entity.posX;
							double d11 = this.posY + getEyeHeight() - entity.posY;
							double d21 = this.posZ - entity.posZ;
							float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11 + d21 * d21);
							entity.motionX = (d01 / f2 * 0.5D * 0.5D + entity.motionX * 0.5D);
							entity.motionY = (d11 / f2 * 0.5D * 0.5D + entity.motionY * 0.5D);
							entity.motionZ = (d21 / f2 * 0.5D * 0.5D + entity.motionZ * 0.5D);
						}
					}
				}
			}
			List<EntityFallingBlock> list = this.world.getEntitiesWithinAABB(EntityFallingBlock.class, getEntityBoundingBox().grow(128.0D, 128.0D, 128.0D), EntitySelectors.NOT_SPECTATING );
			if ((list != null) && (!list.isEmpty()) && this.isEntityAlive() && !this.doesntContainACommandBlock())
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityFallingBlock entity = (EntityFallingBlock)list.get(i1);
					if (entity != null)
					{
						double d01 = this.posX - entity.posX;
						double d11 = this.posY + getEyeHeight() - entity.posY;
						double d21 = this.posZ - entity.posZ;
						float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11 + d21 * d21);
						entity.motionX = (d01 / f2 * 0.5D * 0.5D + entity.motionX * 0.5D);
						entity.motionY = (d11 / f2 * 0.5D * 0.5D + entity.motionY * 0.5D);
						entity.motionZ = (d21 / f2 * 0.5D * 0.5D + entity.motionZ * 0.5D);
						
						List<EntityLivingBase> sublist = this.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox(), EntitySelectors.NOT_SPECTATING);
						if (this.isEntityAlive() && (sublist != null) && (!sublist.isEmpty()))
						{
							for (int i11 = 0; i11 < sublist.size(); i11++)
							{
								EntityLivingBase subentity = (EntityLivingBase)sublist.get(i11);
								
								if (subentity != null && !this.isOnSameTeam(subentity))
								{
									subentity.attackEntityFrom(DamageSource.IN_WALL, 5F);
								}
							}
						}
					}
				}
			}
			List<EntityFallingBlock> list1 = this.world.getEntitiesWithinAABB(EntityFallingBlock.class, getEntityBoundingBox(), EntitySelectors.NOT_SPECTATING);
			if ((list1 != null) && (!list1.isEmpty()) && this.isEntityAlive() && !this.doesntContainACommandBlock())
			{
				for (int i1 = 0; i1 < list1.size(); i1++)
				{
					EntityFallingBlock entity = (EntityFallingBlock)list1.get(i1);
					if (entity != null)
					{
						entity.setDead();
						Grow(getSize() + 3);
						this.heal(2);
					}
				}
			}
			List<EntityItem> list11 = this.world.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D), EntitySelectors.NOT_SPECTATING);
			if ((list11 != null) && (!list11.isEmpty()) && this.isEntityAlive() && !this.doesntContainACommandBlock())
			{
				for (int i1 = 0; i1 < list11.size(); i1++)
				{
					EntityItem entity = (EntityItem)list11.get(i1);
					if (entity != null && entity.getItem().getItem() != EItem.witheredNetherStar)
					{
						double d01 = this.posX - entity.posX;
						double d11 = this.posY + 2.0D - entity.posY;
						double d21 = this.posZ - entity.posZ;
						float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11 + d21 * d21);
						entity.motionX = (d01 / f2 * 0.6D * 0.6D + entity.motionX * 0.6D);
						entity.motionY = (d11 / f2 * 0.6D * 0.6D + entity.motionY * 0.6D);
						entity.motionZ = (d21 / f2 * 0.6D * 0.6D + entity.motionZ * 0.6D);
					}
				}
			}
			List<EntityItem> list111 = this.world.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().grow(4.0D, 4.0D, 4.0D), EntitySelectors.NOT_SPECTATING);
			if ((list111 != null) && (!list111.isEmpty()) && this.isEntityAlive() && !this.doesntContainACommandBlock())
			{
				for (int i1 = 0; i1 < list111.size(); i1++)
				{
					EntityItem entity = (EntityItem)list111.get(i1);
					if (entity != null && entity.getItem().getItem() != EItem.witheredNetherStar)
					{
						entity.setDead();
						Grow(getSize() + 1 + entity.getItem().getCount());
						this.heal(1 + entity.getItem().getCount());
					}
				}
			}

			List<EntityArrow> list1111 = this.world.getEntitiesWithinAABB(EntityArrow.class, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D), EntitySelectors.NOT_SPECTATING);
			if ((list1111 != null) && (!list1111.isEmpty()) && this.isEntityAlive() && !this.doesntContainACommandBlock())
			{
				for (int i1 = 0; i1 < list1111.size(); i1++)
				{
					EntityArrow entity = (EntityArrow)list1111.get(i1);
					if (entity != null && !(entity instanceof EntityTippedArrowOther))
					{
						double d01 = this.posX - entity.posX;
						double d11 = this.posY + 2.0D - entity.posY;
						double d21 = this.posZ - entity.posZ;
						float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11 + d21 * d21);
						entity.motionX = (d01 / f2 * 0.6D * 0.6D + entity.motionX * 0.6D);
						entity.motionY = (d11 / f2 * 0.6D * 0.6D + entity.motionY * 0.6D);
						entity.motionZ = (d21 / f2 * 0.6D * 0.6D + entity.motionZ * 0.6D);
					}
				}
			}
			List<EntityArrow> list11111 = this.world.getEntitiesWithinAABB(EntityArrow.class, getEntityBoundingBox().grow(4.0D, 4.0D, 4.0D), EntitySelectors.NOT_SPECTATING);
			if ((list11111 != null) && (!list11111.isEmpty()) && this.isEntityAlive() && !this.doesntContainACommandBlock())
			{
				for (int i1 = 0; i1 < list11111.size(); i1++)
				{
					EntityArrow entity = (EntityArrow)list11111.get(i1);
					if (entity != null && !(entity instanceof EntityTippedArrowOther))
					{
						entity.setDead();
						Grow(getSize() + 1);
						this.heal(1);
					}
				}
			}
		}
		this.ignoreFrustumCheck = true;
		super.onLivingUpdate();
		for (int i = 0; i < 2; i++)
		{
			this.field_82218_g[i] = this.field_82221_e[i];
			this.field_82217_f[i] = this.field_82220_d[i];
		}
		if (this.world.isRemote)
		{
			for (int i = 0; i < 15; i++)
			{
				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width * 12.0D, this.posY + this.rand.nextDouble() * this.height * 48.0D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width * 12.0D, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
		if (this.doesntContainACommandBlock())
		{
			if (centerHead != null)
			this.centerHead.setLocationAndAngles(this.posX + oned * -7.0F, this.posY, this.posZ - twod * -7.0F, 0.0F, 0.0F);
			if (rightHead != null)
			this.rightHead.setLocationAndAngles(this.posX + twod * -7.0F + oned * -7.0F, this.posY, this.posZ + oned * -7.0F - twod * -7.0F, 0.0F, 0.0F);
			if (leftHead != null)
			this.leftHead.setLocationAndAngles(this.posX - twod * -7.0F + oned * -7.0F, this.posY, this.posZ - oned * -7.0F - twod * -7.0F, 0.0F, 0.0F);
			if (tentacle1 != null)
			this.tentacle1.setLocationAndAngles(this.posX, this.posY - 2.0D, this.posZ, 0.0F, 0.0F);
			if (tentacle2 != null)
			this.tentacle2.setLocationAndAngles(this.posX + twod * 4.0F, this.posY - 1.0D, this.posZ + oned * 4.0F, 0.0F, 0.0F);
			if (tentacle3 != null)
			this.tentacle3.setLocationAndAngles(this.posX - twod * -2.0F, this.posY + 4.0D, this.posZ - oned * -2.0F, 0.0F, 0.0F);
			if (tentacle4 != null)
			this.tentacle4.setLocationAndAngles(this.posX + twod * -5.0F, this.posY + 3.0D, this.posZ + oned * -5.0F, 0.0F, 0.0F);
			if (tentacle5 != null)
			this.tentacle5.setLocationAndAngles(this.posX, this.posY + 2.0D, this.posZ, 0.0F, 0.0F);
			if (tentacledevourer1 != null)
			this.tentacledevourer1.setLocationAndAngles(this.posX, this.posY + 10.0D, this.posZ - oned, 0.0F, 0.0F);
			if (tentacledevourer2 != null)
			this.tentacledevourer2.setLocationAndAngles(this.posX, this.posY + 10.0D, this.posZ + oned, 0.0F, 0.0F);
			if (!this.world.isRemote)
			heal(1F);
			setSize(12.0F, 12.0F);
		}
		else
		{
			if (centerHead != null)
			{
				this.centerHead.setLocationAndAngles(this.posX + oned * -7.0F, this.posY, this.posZ - twod * -7.0F, 0.0F, 0.0F);
			}
			if (rightHead != null)
			{
				this.rightHead.setLocationAndAngles(this.posX + twod * -20.0F + oned * -4.0F, this.posY + 10.0D, this.posZ + oned * -20.0F - twod * -4.0F, 0.0F, 0.0F);
				this.rightHead.setInvisible(this.getHealth() <= 1000);
			}
			if (leftHead != null)
			{
				this.leftHead.setLocationAndAngles(this.posX - twod * -20.0F + oned * -4.0F, this.posY + 10.0D, this.posZ - oned * -20.0F - twod * -4.0F, 0.0F, 0.0F);
				this.leftHead.setInvisible(this.getHealth() <= 2000);
			}
			if (tentacle1 != null)
			{
				this.tentacle1.setLocationAndAngles(this.posX, this.posY - 12.0D, this.posZ, 0.0F, 0.0F);
				this.tentacle1.setInvisible(this.getHealth() <= 9000);
			}
			if (tentacle2 != null)
			{
				this.tentacle2.setLocationAndAngles(this.posX + twod * 2.0F, this.posY - 8.0D, this.posZ + oned * 2.0F, 0.0F, 0.0F);
				this.tentacle2.setInvisible(this.getHealth() <= 7500);
			}
			if (tentacle3 != null)
			{
				this.tentacle3.setLocationAndAngles(this.posX - twod * -8.0F, this.posY + 8.0D, this.posZ - oned * -8.0F, 0.0F, 0.0F);
				this.tentacle3.setInvisible(this.getHealth() <= 5000);
			}
			if (tentacle4 != null)
			{
				this.tentacle4.setLocationAndAngles(this.posX + twod * -5.0F, this.posY + 4.0D, this.posZ + oned * -5.0F, 0.0F, 0.0F);
				this.tentacle4.setInvisible(this.getHealth() <= 4000);
			}
			if (tentacle5 != null)
			{
				this.tentacle5.setLocationAndAngles(this.posX, this.posY + 3.0D, this.posZ, 0.0F, 0.0F);
				this.tentacle5.setInvisible(this.getHealth() <= 3000);
			}
			if (tentacledevourer1 != null)
			{
				this.tentacledevourer1.setLocationAndAngles(this.posX - twod * -12.0F, this.posY + 10.0D, this.posZ - oned * -12.0F, 0.0F, 0.0F);
				this.tentacledevourer1.setInvisible(this.getHealth() <= 11500 || getSize() < 50000 || doesntContainACommandBlock());
			}
			if (tentacledevourer2 != null)
			{
				this.tentacledevourer2.setLocationAndAngles(this.posX + twod * -12.0F, this.posY + 10.0D, this.posZ + oned * -12.0F, 0.0F, 0.0F);
				this.tentacledevourer2.setInvisible(this.getHealth() <= 10000 || getSize() < 50000 || doesntContainACommandBlock());
			}
			if (!this.world.isRemote)
			heal(2F);
			setSize(9.0F, 32.0F);

			if (posY > 200)
				posY = 200;
			else if (posY < 0)
				posY = 0;
		}
	}
	public void setInWeb() { }
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) { }
	public void travel(float strafe, float vertical, float forward)
	{
		float f = doesntContainACommandBlock() ? 0.99F : (this.getSize() > 250000 ? 0.85F : (this.getSize() > 250000 ? 0.88F : 0.91F));
		this.moveRelative(strafe, vertical, forward, 0.02F);
		move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		this.motionX *= f;
		this.motionY *= f;
		this.motionZ *= f;
		this.prevLimbSwingAmount = this.limbSwingAmount;
		double d1 = this.posX - this.prevPosX;
		double d0 = this.posZ - this.prevPosZ;
		float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
		if (f2 > 1.0F)
		{
			f2 = 1.0F;
		}
		this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
		this.limbSwing += this.limbSwingAmount;
	}
	public boolean isOnLadder()
	{
		return false;
	}
	public void setPartAttackTarget(@Nullable EntityFriendlyCreature part, @Nullable EntityLivingBase entitylivingbaseIn)
	{
		if (part != null)
		part.setAttackTarget(entitylivingbaseIn);
	}
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.getSize() > 250000 && amount < 20)
		return false;
		
		if (source.getDamageType() == "chaosImplosion")
		amount *= 0.2F;
		
		if (source.isExplosion())
		amount *= 5F;
		
		if (amount > 5000 && source.isExplosion() && !this.doesntContainACommandBlock() && getSize() >= 50000 && !this.world.isRemote)
		{
			EntityWitherStorm entityzombie = new EntityWitherStorm(this.world);
			entityzombie.copyLocationAndAnglesFrom(this);
			entityzombie.setNoAI(isAIDisabled());
			this.world.spawnEntity(entityzombie);
			entityzombie.setOwnerId(getOwnerId());
			entityzombie.setNotContainingCommandBlock(true);
			entityzombie.motionX = rand.nextDouble() - 0.5D;
			entityzombie.motionZ = rand.nextDouble() - 0.5D;
			this.Grow(this.getSize() - 12500);
		}

		if (isEntityInvulnerable(source))
		{
			return false;
		}
		if ((!source.isProjectile()) && (!source.isMagicDamage()) && (source != DamageSource.LAVA) && (source != DamageSource.ON_FIRE) && (source != DamageSource.IN_FIRE) && (source != DamageSource.IN_WALL) && (source != DamageSource.FALL) && (source != DamageSource.DROWN))
		{
			if ((source.getTrueSource() != null) && ((source.getTrueSource() instanceof EntityLivingBase)))
			{
				setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.centerHead != null)
				this.centerHead.setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.leftHead != null)
				this.leftHead.setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.rightHead != null)
				this.rightHead.setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.tentacle1 != null)
				this.tentacle1.setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.tentacle2 != null)
				this.tentacle2.setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.tentacle3 != null)
				this.tentacle3.setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.tentacle4 != null)
				this.tentacle4.setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.tentacle5 != null)
				this.tentacle5.setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.tentacledevourer1 != null)
				this.tentacledevourer1.setAttackTarget((EntityLivingBase)source.getTrueSource());
				if (this.tentacledevourer2 != null)
				{
					this.tentacledevourer2.setAttackTarget((EntityLivingBase)source.getTrueSource());
				}
			}
			return super.attackEntityFrom(source, amount);
		}
		return false;
	}
	protected void despawnEntity()
	{
		this.idleTime = 0;
	}
	public boolean isEntityUndead()
	{
		return true;
	}
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15728880;
	}
	public float getBrightness()
	{
		return 1.0F;
	}
	public void addPotionEffect(PotionEffect potioneffectIn)
	{
		if (!potioneffectIn.getPotion().isBadEffect())
		super.addPotionEffect(potioneffectIn);
	}

	public boolean takesFallDamage()
	{
		return false;
	}

	public boolean isEntityImmuneToCoralium()
	{
		return true;
	}

	public boolean isEntityImmuneToDread()
	{
		return true;
	}

	public boolean isEntityImmuneToAntiMatter()
	{
		return true;
	}

	public boolean isEntityImmuneToDarkness()
	{
		return true;
	}
	@SideOnly(Side.CLIENT)
	public float func_82207_a(int p_82207_1_)
	{
		return this.field_82221_e[p_82207_1_];
	}
	@SideOnly(Side.CLIENT)
	public float func_82210_r(int p_82210_1_)
	{
		return this.field_82220_d[p_82210_1_];
	}
	public int getSize()
	{
		return ((Integer)this.dataManager.get(SIZE)).intValue();
	}
	public void Grow(int p_82215_1_)
	{
		if (!this.world.isRemote)
		{
			this.dataManager.set(SIZE, Integer.valueOf(doesntContainACommandBlock() ? 12500 : p_82215_1_));
			getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((doesntContainACommandBlock() ? 1000D : (p_82215_1_)));
			if (p_82215_1_ == 12500 && !this.isWild())
			{
				for (EntityPlayer entityplayer : world.playerEntities)
				{
					entityplayer.sendStatusMessage(new TextComponentTranslation(doesntContainACommandBlock() ? "\u00A75 A Wither Storm has fissioned!" : "\u00A75"+ this.getOwner().getName() + "'s Wither Storm has grown to Destroyer form!!"), true);
				}
			}
		}
	}
	public boolean doesntContainACommandBlock()
	{
		return ((Boolean)this.dataManager.get(DOESNT_HAVE_COMMAND_BLOCK)).booleanValue();
	}
	public void setNotContainingCommandBlock(boolean p_82215_1_)
	{
		if (p_82215_1_)
		this.lastDamage = Float.MAX_VALUE;
		this.dataManager.set(DOESNT_HAVE_COMMAND_BLOCK, Boolean.valueOf(p_82215_1_));
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return this.doesntContainACommandBlock() ? ELoot.ENTITIES_WITHER_STORM_MULAGEN : ELoot.ENTITIES_WITHER_STORM;
	}
	protected SoundEvent getCrushHurtSound()
	{
		return ESound.fleshHitCrushHeavy;
	}

	protected void onDeathUpdate()
	{
		BlockPos blockpos = this.getPosition();
		
		if (this.getGrowingAge() < 50000)
		{
			if (this.tentacledevourer1 != null)
			this.tentacledevourer1.setDead();
			if (this.tentacledevourer2 != null)
			this.tentacledevourer2.setDead();
		}

		if (this.doesntContainACommandBlock())
		{
			++this.deathTicks;
			super.onDeathUpdate();
		}
		else
		{
			++this.deathTicks;
			
			if (this.getSize() > 12500 && !this.world.isRemote && this.world.isAreaLoaded(blockpos, blockpos))
			{
				for (int j = 0; j < (this.getSize() > 50000 ? 20 : 10); j++)
				{
					this.world.setBlockState(this.getPosition().up(j), Blocks.OBSIDIAN.getDefaultState());
					EntityFallingBlock deathBlocks = new EntityFallingBlock(world, this.posX, this.posY + 0.5D + j, this.posZ, this.world.getBlockState(getPosition().up(j)));
					deathBlocks.motionX += this.rand.nextDouble() * 4D - 2D;
					deathBlocks.motionY -= this.rand.nextDouble();
					deathBlocks.motionZ += this.rand.nextDouble() * 4D - 2D;
					deathBlocks.setHurtEntities(true);
					this.world.spawnEntity(deathBlocks);
					this.Grow(this.getSize() - 3);
				}
			}

			if (this.deathTicks % 20 == 0)
			++this.deathTime;
			if (getHealth() <= 0.0F)
			{
				this.setAttackTarget((EntityLivingBase)null);
				float f13 = (this.rand.nextFloat() - 0.5F) * 12.0F;
				float f15 = (this.rand.nextFloat() - 0.5F) * 36.0F;
				float f17 = (this.rand.nextFloat() - 0.5F) * 12.0F;
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f13, this.posY - 4.0D + f15, this.posZ + f17, 0.0D, 0.0D, 0.0D, new int[0]);
			}
			if (!this.world.isRemote)
			{
				if (this.deathTicks == 1)
				{
					if (getOwner() != null)
					{
						for (EntityPlayer entityplayer : world.playerEntities)
						{
							this.world.playSound(null, entityplayer.getPosition(), getDeathSound(), this.getSoundCategory(), getSoundVolume(), 1.0F);
							entityplayer.sendStatusMessage(new TextComponentTranslation("\u00A74"+ this.getOwner().getName() + "'s Wither Storm has been killed!!!"), true);
						}
						((EntityPlayerMP)getOwner()).sendMessage(new TextComponentTranslation("Your Wither Storm has been destroyed!", new Object[0]));
					}
				}
			}
			if (this.deathTicks == 80)
			{
				if ((this.tentacle1 != null) && (this.tentacle1.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					if (!this.world.isRemote)
					createEngenderModExplosionFireless(this, this.tentacle1.posX, this.tentacle1.posY, this.tentacle1.posZ, 6.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.tentacle1.motionX = ((this.rand.nextFloat() - 0.5F) * 3.0F);
					this.tentacle1.motionY = 0.800000011920929D;
					this.tentacle1.motionZ = ((this.rand.nextFloat() - 0.5F) * 3.0F);
				}
			}
			if (this.deathTicks == 100)
			{
				if ((this.tentacle2 != null) && (this.tentacle2.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					if (!this.world.isRemote)
					createEngenderModExplosionFireless(this, this.tentacle2.posX, this.tentacle2.posY, this.tentacle2.posZ, 6.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.tentacle2.motionX = ((this.rand.nextFloat() - 0.5F) * 3.0F);
					this.tentacle2.motionY = 0.800000011920929D;
					this.tentacle2.motionZ = ((this.rand.nextFloat() - 0.5F) * 3.0F);
				}
			}
			if (this.deathTicks == 110)
			{
				if ((this.tentacledevourer1 != null) && (this.tentacledevourer1.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					if (!this.world.isRemote)
					createEngenderModExplosionFireless(this, this.tentacledevourer1.posX, this.tentacledevourer1.posY, this.tentacledevourer1.posZ, 15.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.tentacledevourer1.motionX = ((this.rand.nextFloat() - 0.5F) * 3.0F);
					this.tentacledevourer1.motionY = 0.800000011920929D;
					this.tentacledevourer1.motionZ = ((this.rand.nextFloat() - 0.5F) * 3.0F);
				}
			}
			if (this.deathTicks == 150)
			{
				if ((this.tentacle4 != null) && (this.tentacle4.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					if (!this.world.isRemote)
					createEngenderModExplosionFireless(this, this.tentacle4.posX, this.tentacle4.posY, this.tentacle4.posZ, 6.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.tentacle4.motionX = ((this.rand.nextFloat() - 0.5F) * 3.0F);
					this.tentacle4.motionY = 0.800000011920929D;
					this.tentacle4.motionZ = ((this.rand.nextFloat() - 0.5F) * 3.0F);
				}
				if ((this.tentacle3 != null) && (this.tentacle3.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					if (!this.world.isRemote)
					createEngenderModExplosionFireless(this, this.tentacle3.posX, this.tentacle3.posY, this.tentacle3.posZ, 6.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.tentacle3.motionX = ((this.rand.nextFloat() - 0.5F) * 3.0F);
					this.tentacle3.motionY = 0.800000011920929D;
					this.tentacle3.motionZ = ((this.rand.nextFloat() - 0.5F) * 3.0F);
				}
			}
			if (this.deathTicks == 180)
			{
				if ((this.tentacle5 != null) && (this.tentacle5.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					if (!this.world.isRemote)
					createEngenderModExplosionFireless(this, this.tentacle5.posX, this.tentacle5.posY, this.tentacle5.posZ, 6.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.tentacle5.motionX = ((this.rand.nextFloat() - 0.5F) * 3.0F);
					this.tentacle5.motionY = 0.800000011920929D;
					this.tentacle5.motionZ = ((this.rand.nextFloat() - 0.5F) * 3.0F);
				}
				if ((this.tentacledevourer2 != null) && (this.tentacledevourer2.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					if (!this.world.isRemote)
					createEngenderModExplosionFireless(this, this.tentacledevourer2.posX, this.tentacledevourer2.posY, this.tentacledevourer2.posZ, 15.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.tentacledevourer2.motionX = ((this.rand.nextFloat() - 0.5F) * 3.0F);
					this.tentacledevourer2.motionY = 0.800000011920929D;
					this.tentacledevourer2.motionZ = ((this.rand.nextFloat() - 0.5F) * 3.0F);
				}
			}
			if (this.deathTicks == 180)
			{
				if ((this.leftHead != null) && (this.leftHead.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					if (!this.world.isRemote)
					createEngenderModExplosionFireless(this, this.leftHead.posX, this.leftHead.posY, this.leftHead.posZ, 9.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.leftHead.motionX = ((this.rand.nextFloat() - 0.5F) * 3.0F);
					this.leftHead.motionZ = ((this.rand.nextFloat() - 0.5F) * 3.0F);
				}
			}
			if (this.deathTicks == 200)
			{
				if ((this.rightHead != null) && (this.rightHead.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					if (!this.world.isRemote)
					createEngenderModExplosionFireless(this, this.rightHead.posX, this.rightHead.posY, this.rightHead.posZ, 9.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					this.rightHead.motionX = ((this.rand.nextFloat() - 0.5F) * 3.0F);
					this.rightHead.motionZ = ((this.rand.nextFloat() - 0.5F) * 3.0F);
				}
			}
			if ((this.deathTicks >= 300) && (!this.world.isRemote))
			{
				if ((this.centerHead != null) && (this.centerHead.residentWitherStorm != null))
				{
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					playSound(getHurtSound(null), getSoundVolume(), 2.0F);
					createEngenderModExplosionFireless(this, this.centerHead.posX, this.centerHead.posY, this.centerHead.posZ, 9.0F, this.world.getGameRules().getBoolean("mobGriefing"));
					double d01 = this.centerHead.posX - this.posX;
					double d21 = this.centerHead.posZ - this.posZ;
					float f2 = MathHelper.sqrt(d01 * d01 + d21 * d21);
					this.centerHead.motionX = (d01 / f2 * 0.6D * 0.6D + this.centerHead.motionX);
					this.centerHead.motionZ = (d21 / f2 * 0.6D * 0.6D + this.centerHead.motionZ);
				}
				List<EntityFallingBlock> list = this.world.getEntitiesWithinAABB(EntityFallingBlock.class, getEntityBoundingBox().grow(128.0D, 128.0D, 128.0D), EntitySelectors.NOT_SPECTATING);
				if ((list != null) && (!list.isEmpty()) && this.world.isRemote)
				{
					for (int i1 = 0; i1 < list.size(); i1++)
					{
						EntityFallingBlock entity = (EntityFallingBlock)list.get(i1);
						if (entity != null)
						{
							createEngenderModExplosionFireless(this, entity.posX, entity.posY, entity.posZ, 2F, false);
							entity.setDead();
						}
					}
				}
				for (EntityPlayer entityplayer : world.playerEntities)
				{
					this.world.playSound(null, entityplayer.getPosition(), getDeathSound(), this.getSoundCategory(), getSoundVolume(), 1.0F);
				}
				int i = this.getSize();
				while (i > 0)
				{
					int j = EntityXPOrb.getXPSplit(i);
					i -= j;
					this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 8.0D, this.posZ, j));
				}
				EntityItem entityitem = entityDropItem(new ItemStack(EItem.witheredNetherStar), 0.0F);
				if (entityitem != null)
				{
					entityitem.setNoDespawn();
				}
				if (!this.world.isRemote)
				{
					for (EntityWitherStorm allmulegans : this.world.getEntitiesWithinAABB(EntityWitherStorm.class, getEntityBoundingBox().grow(256D)))
					{
						if (allmulegans.doesntContainACommandBlock())
						allmulegans.setHealth(0F);
					}
				}
				for (int i1 = 0; i1 < this.getSize() / 10000; i1++)
				entityDropItem(new ItemStack(Blocks.OBSIDIAN, 64), 0.0F);
				setDead();
			}
		}
	}

	class AILookAround extends EntityAIBase
	{
		private EntityWitherStorm witherStorm = EntityWitherStorm.this;
		public AILookAround()
		{
			setMutexBits(2);
		}
		public boolean shouldExecute()
		{
			return true;
		}
		public void updateTask()
		{
			if (this.witherStorm.getAttackTarget() != null)
			{
				this.witherStorm.getLookHelper().setLookPositionWithEntity(this.witherStorm.getAttackTarget(), 3.0F, 0.0F);
			} else {
					
					if (witherStorm != null && this.witherStorm.centerHead.isBeingRidden())
					{
						Vec3d vec3 = this.witherStorm.centerHead.getControllingPassenger().getLook(1.0F);
						this.witherStorm.getLookHelper().setLookPosition(this.witherStorm.centerHead.getControllingPassenger().posX + (vec3.x * 8D), this.witherStorm.centerHead.getControllingPassenger().posY + (vec3.y * 8D), this.witherStorm.centerHead.getControllingPassenger().posZ + (vec3.z * 8D), 180F, 0.0F);
					}
					else
					this.witherStorm.getLookHelper().setLookPosition(this.witherStorm.getMoveHelper().getX(), this.witherStorm.posY, this.witherStorm.getMoveHelper().getZ(), 3.0F, 0.0F);
				}
			}
		}
		static class AIRandomFly extends EntityAIBase
		{
			private EntityWitherStorm witherStorm;
			public AIRandomFly(EntityWitherStorm ghast)
			{
				this.witherStorm = ghast;
				setMutexBits(1);
			}
			public boolean shouldExecute()
			{
				EntityMoveHelper entitymovehelper = this.witherStorm.getMoveHelper();
				if (!entitymovehelper.isUpdating())
				{
					return true;
				}
				double d0 = entitymovehelper.getX() - this.witherStorm.posX;
				double d1 = entitymovehelper.getY() - this.witherStorm.posY;
				double d2 = entitymovehelper.getZ() - this.witherStorm.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return (d3 < 1.0D) || (d3 > 3600.0D);
			}
			public boolean shouldContinueExecuting()
			{
				return false;
			}
			public void startExecuting()
			{
				Random random = this.witherStorm.getRNG();
				if (this.witherStorm.getOwner() != null)
				{
					double d0 = this.witherStorm.getOwner().posX + (random.nextFloat() * 2.0F - 1.0F) * 48.0F;
					double d1 = this.witherStorm.getOwner().posY + 40.0D + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
					double d2 = this.witherStorm.getOwner().posZ + (random.nextFloat() * 2.0F - 1.0F) * 48.0F;
					if (this.witherStorm.isSneaking())
					{
						d0 = this.witherStorm.getOwner().posX + (random.nextFloat() * 2.0F - 1.0F) * 2.0F;
						d1 = this.witherStorm.getOwner().posY - 60.0D - (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						d2 = this.witherStorm.getOwner().posZ + (random.nextFloat() * 2.0F - 1.0F) * 2.0F;
					}
					if (this.witherStorm.centerHead.isBeingRidden())
					{
						Vec3d vec3 = this.witherStorm.getOwner().getLook(1.0F);
						d0 = this.witherStorm.posX + (vec3.x * 8D);
						d1 = this.witherStorm.posY + (vec3.y * 8D);
						d2 = this.witherStorm.posZ + (vec3.z * 8D);
					}
					this.witherStorm.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
				}
				else
				{
					EntityPlayer player = this.witherStorm.world.getClosestPlayerToEntity(this.witherStorm, 256.0D);
					if (this.witherStorm.getAttackTarget() != null)
					{
						double d0 = this.witherStorm.getAttackTarget().posX + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						double d1 = this.witherStorm.world.getTopSolidOrLiquidBlock(this.witherStorm.getPosition()).getY() + 48D + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						double d2 = this.witherStorm.getAttackTarget().posZ + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						this.witherStorm.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
					}
					else if ((player != null) && (this.witherStorm.getDistanceSq(player) > 2304.0D))
					{
						double d0 = player.posX + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						double d1 = player.posY + 48D + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						double d2 = player.posZ + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						this.witherStorm.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
					}
					else
					{
						double d0 = this.witherStorm.posX + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						double d1 = this.witherStorm.posY + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						double d2 = this.witherStorm.posZ + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
						if (d1 < 32.0D)
						d1 = 32.0D;
						this.witherStorm.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
					}
				}
			}
		}
		static class WitherStormMoveHelper extends EntityMoveHelper
		{
			private EntityWitherStorm witherStorm;
			private int courseChangeCooldown;
			public WitherStormMoveHelper(EntityWitherStorm ghast)
			{
				super(ghast);
				this.witherStorm = ghast;
			}
			public void onUpdateMoveHelper()
			{
				if (this.action == EntityMoveHelper.Action.MOVE_TO)
				{
					double d0 = this.posX - this.witherStorm.posX;
					double d1 = this.posY - this.witherStorm.posY;
					double d2 = this.posZ - this.witherStorm.posZ;
					double d3 = d0 * d0 + d1 * d1 + d2 * d2;
					if (this.courseChangeCooldown-- <= 0)
					{
						this.courseChangeCooldown += this.witherStorm.getRNG().nextInt(5) + 2;
						d3 = MathHelper.sqrt(d3);
						if ((this.witherStorm.getOwner() != null) && (this.witherStorm.getDistanceSq(this.witherStorm.getOwner()) > 5184D) && (this.witherStorm.getGuardBlock() == null))
						{
							this.witherStorm.motionX += d0 / d3 * 0.2D;
							this.witherStorm.motionY += d1 / d3 * 0.2D;
							this.witherStorm.motionZ += d2 / d3 * 0.2D;
						}
						else if (this.witherStorm.moralRaisedTimer > 200)
						{
							this.witherStorm.motionX += d0 / d3 * 0.2D;
							this.witherStorm.motionY += d1 / d3 * 0.2D;
							this.witherStorm.motionZ += d2 / d3 * 0.2D;
						}
						else
						{
							this.witherStorm.motionX += d0 / d3 * 0.1D;
							this.witherStorm.motionY += d1 / d3 * 0.1D;
							this.witherStorm.motionZ += d2 / d3 * 0.1D;
						}
					}
				}
			}
		}
		
		public static boolean checkIfBoss(Entity entity)
		{
			return !entity.isNonBoss() || entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entity).isHero();
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public boolean isMusicDead()
		{
			return isDead;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public int getMusicPriority()
		{
			return 6;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public SoundEvent getMusic()
		{
			int size = getSize();
			return isOnSameTeam(FMLClientHandler.instance().getClientPlayerEntity()) && false ? null : size >= 250000 ? ESound.witherStormTheme3 : size >= 50000 ? ESound.witherStormTheme2 : ESound.witherStormTheme;
		}
	}