package net.minecraft.AgeOfMinecraft.entity.tier5;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.Ender;
import net.minecraft.AgeOfMinecraft.entity.EntityBodyHelperDragon;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.ExtendMultiPartEntityPart;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.EntityDragonFireballOther;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.IPhase;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.PhaseFireballAndStrafe;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.PhaseList;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.PhaseManager;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.PhaseRamAttack;
import net.minecraft.AgeOfMinecraft.events.MobChunkLoader;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.util.Maths;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityEnderDragon extends EntityFriendlyCreature implements IEntityMultiPart, Massive, Flying, Armored, Ender
{
	private static final DataParameter<Boolean> CARRIES_CRYSTAL = EntityDataManager.createKey(EntityEnderDragon.class, DataSerializers.BOOLEAN);
	public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(EntityEnderDragon.class, DataSerializers.VARINT);
	public static final DataParameter<Integer> ID = EntityDataManager.createKey(EntityEnderDragon.class, DataSerializers.VARINT);
	public static final String[][] quotes = {
	//Spawning Darkness	
	{"You are killing my children, my... Creations!  No matter.  It's time to end your existence.", 
	"Dear oh dear, why do you persist?  You're destroying my creations.  Now I will destroy you!", 
	"I have been watching you for long enough.  Your utter treason will be satisfied with utter annihilation!", 
	"Welcome %1$s to your own destruction.  Prepare for your worst mistake!", 
	"You are no god, but I shall feast upon your ignorance!"},
	//Entering Phase 2
	{"Not bad.",
	"Hmph.",
	"You are going to have to try harder than that."},
	//Entering Phase 3
	{"I see.  You are the one in the prophecies.  Fine, I will show you my true terror!",
	"You are not an ordinary fighter.  I will show you my real power!",
	"You have surprised us %1$s.  No mortal can make it this far.  No matter, I will just have to unveil true darkness upon you!"},
	//The End Special Move (Not implemented)
	{"This is the end of the line!  Die!!!",
	"You won't survive this one!  Begone!!!",
	"I will annihilate you from existence!  Parish!!!"},
	//60% HP in Phase 3
	{"This can't be right!  I am a god!",
	  "No!  You will not get the best of me!",
	  "I won't lose here!  I won't!"},
	//20% HP in Phase 3
	{"I WILL NOT BE DEFEATED BECAUSE OF A MERE PROPHECY!",
	  "A GOD DOES NOT FEAR DEATH!  PARISH!",
	  "I WILL NOT LOSE HERE!"},
	//Darkness Death
	{"No..  I lost..  T-This is not the end...   I-I will return!  I...",
	  "I-It appears I was ignorant...  I-I can't believe I...",
	  "M-My world...  The world is going to end at your hands...  D-Damn you!...",
	  "No!...  You are going to destroy my world!..   H-How c-can't I beat you!...",
	  "I can't believe it...  I-I let my people d-down...   D-Damn you!..."},
	//Player Death
	{"This is the end for you!",
	  "You won't defeat me %1$s.  Get out of my world!",
	  "Your ignorance betrayed you; begone!",
	  "You made a fatal mistake!",
	  "You are no god puny player!",
	  "You can't kill me that easily puny player!",
	  "Another player to feast on!",
	  "Your flesh will feed my creations very well!",
	  "Return to your world puny player as I will feast upon it as well!",
	  "Your defeat will feed my world's endless darkness!"}};
	public int innerRotation;
	public double[][] ringBuffer = new double[64][3];
	public int ringBufferIndex = -1;
	public ExtendMultiPartEntityPart[] dragonPartArray;
	public ExtendMultiPartEntityPart dragonPartHead;
	public ExtendMultiPartEntityPart dragonPartNeck;
	public ExtendMultiPartEntityPart dragonPartBody;
	public ExtendMultiPartEntityPart dragonPartTail1;
	public ExtendMultiPartEntityPart dragonPartTail2;
	public ExtendMultiPartEntityPart dragonPartTail3;
	public ExtendMultiPartEntityPart dragonPartWing1;
	public ExtendMultiPartEntityPart dragonPartWing2;
	double sitPosX;
	double sitPosY;
	double sitPosZ;
	public float prevAnimTime;
	public float animTime;
	public boolean slowed;
	public boolean sitting;
	private final PhaseManager phaseManager;
	private int field_184678_bK = 200;
	private int sittingDamageReceived;
	private final PathPoint[] pathPoints = new PathPoint[24];
	private final int[] neighbors = new int[24];
	private final PathHeap pathFindQueue = new PathHeap();
	
	public EntityEnderDragon(World worldIn)
	{
	super(worldIn);
	this.isOffensive = true;
	this.dragonPartArray = new ExtendMultiPartEntityPart[] { this.dragonPartHead = new ExtendMultiPartEntityPart(this, "head", 6.0F, 6.0F), this.dragonPartNeck = new ExtendMultiPartEntityPart(this, "neck", 6.0F, 6.0F), this.dragonPartBody = new ExtendMultiPartEntityPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new ExtendMultiPartEntityPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new ExtendMultiPartEntityPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new ExtendMultiPartEntityPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new ExtendMultiPartEntityPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new ExtendMultiPartEntityPart(this, "wing", 4.0F, 4.0F) };
	setHealth(getMaxHealth());
	setSize(16.0F, 3.6F);
	this.noClip = true;
	this.isImmuneToFire = true;
	this.ignoreFrustumCheck = true;
	this.phaseManager = new PhaseManager(this);
	getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
	this.forceSpawn = true;
	this.experienceValue = 500;
	if (this.posX <= 1D || this.posX >= -1D)
		this.posX = 5D;
	if (this.posZ <= 1D || this.posZ >= -1D)
		this.posZ = 5D;
	if (this.posY <= 1D)
		this.posY = 5D;
}

public void onUpdate()
{
	ItemStack charge = this.isCarryingCrystal() ? new ItemStack(Items.END_CRYSTAL) : ItemStack.EMPTY;
	charge.setStackDisplayName("Carrying Crystal");
	basicInventory.setInventorySlotContents(7, charge);
	
	super.onUpdate();
	++this.innerRotation;
	
	if (this.ticksExisted <= 1)
	{
		if (this.posX <= 1D || this.posX >= -1D)
			this.posX = 5D;
		if (this.posZ <= 1D || this.posZ >= -1D)
			this.posZ = 5D;
		if (this.posY <= 1D)
			this.posY = 5D;
	}
}

public void updateBossBar()
{
	super.updateBossBar();
	this.bossInfo.setColor(this.getTier() == EnumTier.TIER6 ? BossInfo.Color.RED : BossInfo.Color.PINK);
}

public boolean isBoss()
{
	return true;
}

public boolean canUseGuardBlock()
{
	return false;
}

public boolean affectedByCommandingStaff()
{
	return false;
}

public boolean canWearEasterEggs()
{
	return false;
}

/**
* Bonus damage vs mobs that implement Armored
*/
public float getBonusVSArmored()
{
	return 1.5F;
}

/**
* Bonus damage vs mobs that implement Flying
*/
public float getBonusVSFlying()
{
	return 3F;
}

/**
* Bonus damage vs mobs that implement Massive
*/
public float getBonusVSMassive()
{
	return 2F;
}

/**
* Get this Entity's EnumCreatureAttribute
*/
public EnumCreatureAttribute getCreatureAttribute()
{
	return ESetup.ENDER;
}

public boolean isChild()
{
	return false;
}
public void setChild(boolean childZombie) { }

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

public EnumTier getTier()
{
	return EnumTier.TIER5;
}

public boolean canBreatheUnderwater()
{
	return true;
}
protected void applyEntityAttributes()
{
	super.applyEntityAttributes();
	getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(128.0D);
	getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);
	getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
	getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(10.0D);
	getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(27D);
	getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(15.0D);
}public double getKnockbackResistance()
	{
		return 1D;
	}
	protected void entityInit()
	{
		super.entityInit();
		getDataManager().register(PHASE, Integer.valueOf(PhaseList.HOLDING_PATTERN.getId()));
		getDataManager().register(ID, Integer.valueOf(0));
		getDataManager().register(CARRIES_CRYSTAL, Boolean.valueOf(false));
	}
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}

	public boolean isCarryingCrystal()
	{
		return ((Boolean)getDataManager().get(CARRIES_CRYSTAL)).booleanValue();
	}
	public void setCarryingCrystal(boolean childZombie)
	{
		getDataManager().set(CARRIES_CRYSTAL, Boolean.valueOf(childZombie));
	}

	protected boolean canTriggerWalking()
	{
		return false;
	}
	public float getEyeHeight()
	{
		return 2.325F;
	}
	public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_)
	{
		if (getHealth() <= 0.0F)
		{
			p_70974_2_ = 0.0F;
		}
		p_70974_2_ = 1.0F - p_70974_2_;
		int i = this.ringBufferIndex - p_70974_1_ & 0x3F;
		int j = this.ringBufferIndex - p_70974_1_ - 1 & 0x3F;
		double[] adouble = new double[3];
		double d0 = this.ringBuffer[i][0];
		double d1 = MathHelper.wrapDegrees(this.ringBuffer[j][0] - d0);
		adouble[0] = (d0 + d1 * p_70974_2_);
		d0 = this.ringBuffer[i][1];
		d1 = this.ringBuffer[j][1] - d0;
		adouble[1] = (d0 + d1 * p_70974_2_);
		adouble[2] = (this.ringBuffer[i][2] + (this.ringBuffer[j][2] - this.ringBuffer[i][2]) * p_70974_2_);
		return adouble;
	}

	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_ENDER_DRAGON;
	}

	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (!stack.isEmpty() && (stack.getItem() == Items.ENDER_EYE) && (hasOwner(player) || player.isOnSameTeam(this)))
		{
			this.playSound(getAmbientSound(), 0.25F, 0.75F);
			if (!this.isBeingRidden())
			if (sitting)
			this.sitting = false;
			else
			{
				this.sitting = true;
				this.sitPosX = this.posX;
				this.sitPosY = this.posY;
				this.sitPosZ = this.posZ;
			}
			return true;
		}
		else if (!stack.isEmpty() && (stack.getItem() == Items.END_CRYSTAL) && (hasOwner(player) || player.isOnSameTeam(this)))
		{
			if (!this.isCarryingCrystal())
			{
				this.setCarryingCrystal(true);
				this.playLivingSound();
				this.world.playEvent(3000, this.getPosition(), 0);
				if (!player.capabilities.isCreativeMode)
				{
					stack.shrink(1);
				}

			}
			return true;
		}
		else if (stack.isEmpty() && getRidingEntity() == null)
		{
			if (!isWild() && this.isOnSameTeam(player) && !this.isChild() && !this.world.isRemote)
			player.startRiding(this);
			return true;
		}
		else
		{
			return false;
		}
	}

	public double getMountedYOffset()
	{
		return 3.25D;
	}

	protected boolean canFitPassenger(Entity passenger)
	{
		return getPassengers().size() < 5;
	}

	public void updatePassenger(Entity passenger)
	{
		if (isPassenger(passenger))
		{
			int i = getPassengers().indexOf(passenger);
			
			float f3 = this.renderYawOffset * 3.1415927F / 180.0F;
			float f11 = MathHelper.sin(f3);
			float f4 = MathHelper.cos(f3);
			if (i == 5)
			passenger.setPosition(this.dragonPartTail3.posX, this.dragonPartTail3.posY + 1D, this.dragonPartTail3.posZ);
			if (i == 4)
			passenger.setPosition(this.dragonPartTail2.posX, this.dragonPartTail2.posY + 1D, this.dragonPartTail2.posZ);
			if (i == 3)
			passenger.setPosition(this.dragonPartTail1.posX, this.dragonPartTail1.posY + 1D, this.dragonPartTail1.posZ);
			if (i == 2)
			passenger.setPosition(this.posX + f11 * 1F, this.posY + getEyeHeight() + 1.0D, this.posZ - f4 * 1F);
			if (i == 1)
			passenger.setPosition(this.posX + f11 * -1F, this.posY + getEyeHeight() + 1.0D, this.posZ - f4 * -1F);
			if (i == 0)
			passenger.setPosition(this.dragonPartNeck.posX, this.dragonPartNeck.posY + 1D, this.dragonPartNeck.posZ);
		}
	}

	public void performSpecialAttack()
	{
		playLivingSound();
		playLivingSound();
		playLivingSound();
		playLivingSound();
		playLivingSound();
		playLivingSound();
		playLivingSound();
		playLivingSound();
		setSpecialAttackTimer(2000);
	}

	/**
	* Changes pitch and yaw so that the entity calling the function is facing the entity provided as an argument.
	*/
	public void faceEntity(Entity entityIn, float maxYawIncrease, float maxPitchIncrease)
	{
		double d0 = entityIn.posX - this.posX;
		double d2 = entityIn.posZ - this.posZ;
		double d1;
		
		if (entityIn instanceof EntityLivingBase)
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)entityIn;
			d1 = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (this.posY + (double)this.getEyeHeight());
		}
		else
		{
			d1 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
		}

		double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
		float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
		float f1 = (float)(-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
		this.rotationPitch = this.updateRotation(this.rotationPitch, f1, maxPitchIncrease);
		this.rotationYawHead = this.updateRotation(this.rotationYawHead, f, maxYawIncrease);
	}

	/**
	* Arguments: current rotation, intended rotation, max increment.
	*/
	private float updateRotation(float angle, float targetAngle, float maxIncrease)
	{
		float f = MathHelper.wrapDegrees(targetAngle - angle);
		
		if (f > maxIncrease)
		{
			f = maxIncrease;
		}

		if (f < -maxIncrease)
		{
			f = -maxIncrease;
		}

		return angle + f;
	}
	public boolean canBeSteered()
	{
		return this.getControllingPassenger() instanceof EntityPlayer;
	}

	public void setAttackTarget(EntityLivingBase entitylivingbaseIn)
	{
		super.setAttackTarget(entitylivingbaseIn);
	}

	protected EntityBodyHelper createBodyHelper()
	{
		return new EntityBodyHelperDragon(this);
	}
	
	public void onKillEntity(EntityLivingBase entityLivingIn)
	{
		if (!this.world.isRemote && this.getID() != 0 && this.isEntityAlive() && entityLivingIn instanceof EntityPlayer)
		{
			for (EntityPlayer entityplayer : this.world.playerEntities)
				entityplayer.sendMessage(new TextComponentTranslation(quotes[7][Maths.random(0, quotes[7].length - 1)]));
		}
		super.onKillEntity(entityLivingIn);
	}

	public void onLivingUpdate()
	{
		if (!this.world.isRemote)
		{
			// Spawn
			if (this.getID() == 1 && this.getName() != "Darkness")
			{
				this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(750D * (((double)this.world.playerEntities.size() * 0.35D) + 1.0D));
				this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20D);
				this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10D);
				this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(40D);
				this.heal(getMaxHealth());
				this.setOwnerId(null);
				this.setCustomNameTag("Darkness");
				for (EntityPlayer entityplayer : this.world.playerEntities)
					{
						entityplayer.sendStatusMessage(new TextComponentTranslation(TextFormatting.DARK_PURPLE + "You feel an unworldly energy surround you..."), true);		
						entityplayer.sendMessage(new TextComponentTranslation(quotes[0][Maths.random(0, quotes[0].length - 1)], new Object[] {entityplayer.getDisplayName()}));
					}
			}
			
			//Phase 2
			if (this.getID() == 1 && this.getHealth() <= this.getMaxHealth() * 0.75F)
			{
				setID(2);
				this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(75D);
				for (EntityPlayer entityplayer : this.world.playerEntities)
				{		
					entityplayer.sendMessage(new TextComponentTranslation(quotes[1][Maths.random(0, quotes[1].length - 1)], new Object[] {entityplayer.getDisplayName()}));
				}
			}
			
			//Phase 3
			else if (this.getID() == 2 && this.getHealth() <= this.getMaxHealth() * 0.15F)
			{
				setID(3);
				this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(200D);
				for (EntityPlayer entityplayer : this.world.playerEntities)
				{
					entityplayer.sendStatusMessage(new TextComponentTranslation(TextFormatting.DARK_PURPLE + "You feel the ground tremble..."), true);
					entityplayer.sendMessage(new TextComponentTranslation(quotes[2][Maths.random(0, quotes[2].length - 1)], new Object[] {entityplayer.getDisplayName()}));
				}
			}
			
			//Healing up to Phase 3
			else if (this.getID() == 3 && this.getHealth() < this.getMaxHealth())
			{
				heal(getMaxHealth() * 0.003F);
				if (this.getHealth() >= this.getMaxHealth())
				setID(4);
			}
			
			//60% Phase 3
			else if (this.getID() == 4 && this.getHealth() <= this.getMaxHealth() * 0.60F)
			{
				setID(5);
				for (EntityPlayer entityplayer : this.world.playerEntities)
				{		
					entityplayer.sendMessage(new TextComponentTranslation(quotes[4][Maths.random(0, quotes[4].length - 1)], new Object[] {entityplayer.getDisplayName()}));
				}
			}
			
			//20% Phase 3
			else if (this.getID() == 5 && this.getHealth() <= this.getMaxHealth() * 0.20F)
			{
				setID(6);
				for (EntityPlayer entityplayer : this.world.playerEntities)
				{		
					entityplayer.sendMessage(new TextComponentTranslation(quotes[5][Maths.random(0, quotes[5].length - 1)], new Object[] {entityplayer.getDisplayName()}));
				}
			}
		}
		if (this.posX == 0D && this.posY == 0D && this.posZ == 0D)
		{
			EntityEnderDragon entityliving = new EntityEnderDragon(this.world);
			if (!this.isWild() && getOwner() != null)
			entityliving.copyLocationAndAnglesFrom(this.getOwner());
			else
			entityliving.setPosition(0D, 64D, 0D);
			entityliving.setOwnerId(this.getOwnerId());
			entityliving.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
			entityliving.setLevel(this.getLevel());
			entityliving.setStrength(this.getStrength(), false);
			entityliving.setStamina(this.getStamina(), false);
			entityliving.setIntelligence(this.getIntelligence(), false);
			entityliving.setAgility(this.getAgility(), false);
			entityliving.setDexterity(this.getDexterity(), false);
			entityliving.setIsHero(this.isHero());
			entityliving.setLastChance(this.hasLastChance());
			this.world.removeEntity(this);
			this.world.spawnEntity(entityliving);
		}

		if (this.convertionInt > 0)
		{
			this.phaseManager.setPhase(PhaseList.LANDING);
			this.motionX *= 0.75D;
			this.motionY *= 0.75D;
			this.motionZ *= 0.75D;
		}

		if (this.getPhaseManager().getCurrentPhase() == PhaseList.LANDING_APPROACH)
		{
			this.setAttackTarget(null);
			double d0 = (this.isWild() ? 0D : this.getOwner().posX) - this.posX;
			double d1 = (this.isWild() ? this.world.getTopSolidOrLiquidBlock(net.minecraft.world.gen.feature.WorldGenEndPodium.END_PODIUM_LOCATION).getY() : this.getOwner().posY + 4D) - this.posY;
			double d2 = (this.isWild() ? 0D : this.getOwner().posZ) - this.posZ;
			double d3 = d0 * d0 + d2 * d2;
			double d5 = MathHelper.sqrt(d3);
			move(MoverType.SELF, (d0 / d5 * 0.99D - this.motionX), (d1 / d5 * 0.99D - this.motionY), (d2 / d5 * 0.99D - this.motionZ));
		}
		if (!this.isEntityAlive())
		{
			this.clearActivePotions();
		}
		if (this.sitting)
		{
			this.phaseManager.setPhase(PhaseList.HOLDING_PATTERN);
			this.setLocationAndAngles(sitPosX, sitPosY, sitPosZ, this.rotationYaw, this.rotationPitch);
			this.motionX = this.motionY = this.motionZ = 0D;
			if (this.getAttackTarget() != null && this.phaseManager.getCurrentPhase() != PhaseList.LANDING_APPROACH && this.phaseManager.getCurrentPhase() != PhaseList.LANDING)
			{
				this.faceEntity(this.getAttackTarget(), 10F, 90F);
				this.renderYawOffset = this.rotationYaw = this.rotationYawHead + 180F;
			}

			if (!this.isWild() && getOwner() != null && this.getDistanceSq(this.getOwner()) > 10000)
			{
				this.sitPosX = this.getOwner().posX;
				this.sitPosY = this.getOwner().posY + 12D;
				this.sitPosZ = this.getOwner().posZ;
			}
			else
			{
				this.sitPosX = this.posX;
				this.sitPosY = this.posY;
				this.sitPosZ = this.posZ;
			}
		}

		if (this.dead)
		{
			this.phaseManager.setPhase(PhaseList.DYING);
		}
		if (this.getHealth() <= this.getMaxHealth() / 5 && !this.phaseManager.getCurrentPhase().getIsStationary())
		{
			this.phaseManager.setPhase(PhaseList.LANDING_APPROACH);
			this.setAttackTarget(null);
		}
		if (this.getJukeboxToDanceTo() != null)
		{
			Vec3d vec3d = this.getHeadLookVec(1.0F).normalize();
			vec3d.rotateYaw(-0.7853982F);
			double d0 = this.dragonPartHead.posX;
			double d1 = this.dragonPartHead.posY + this.dragonPartHead.height / 2.0F;
			double d2 = this.dragonPartHead.posZ;
			for (int i = 0; i < 8; i++)
			{
				double d3 = d0 + this.getRNG().nextGaussian() / 2.0D;
				double d4 = d1 + this.getRNG().nextGaussian() / 2.0D;
				double d5 = d2 + this.getRNG().nextGaussian() / 2.0D;
				this.world.spawnParticle(net.minecraft.util.EnumParticleTypes.DRAGON_BREATH, d3, d4, d5, -vec3d.x * 0.07999999821186066D + this.motionX, -vec3d.y * 0.30000001192092896D + this.motionY, -vec3d.z * 0.07999999821186066D + this.motionZ, new int[0]);
				vec3d.rotateYaw(0.19634955F);
			}
		}

		if (this.moralRaisedTimer <= 0)
		this.moralRaisedTimer = 0;
		
		if (this.moralRaisedTimer > 0)
		this.moralRaisedTimer -= 1;
		
		if (this.isAIDisabled())
		{
			this.setNoAI(this.isAIDisabled());
			this.hurtResistantTime = this.maxHurtResistantTime;
			if (this.ticksExisted > 21)
			--this.ticksExisted;
		}
		else
		{
			if (!this.world.isRemote && this.getAttackTarget() == null)
			{
				this.targetTasks.onUpdateTasks();
				
				List<EntityFriendlyCreature> list = this.world.<EntityFriendlyCreature>getEntitiesWithinAABB(EntityFriendlyCreature.class, this.getEntityBoundingBox().grow(this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), Predicates.and(EntitySelectors.IS_ALIVE, EntitySelectors.NOT_SPECTATING));
				
				for (int j2 = 0; j2 < 10 && !list.isEmpty(); ++j2)
				{
					EntityFriendlyCreature entitylivingbase = list.get(this.rand.nextInt(list.size()));
					
					if (entitylivingbase != this && entitylivingbase.isEntityAlive() && this.canEntityBeSeen(entitylivingbase) && entitylivingbase.getOwnerId() == this.getOwnerId() && entitylivingbase.getFakeHealth() > 0F)
					{
						this.setAttackTarget(entitylivingbase);
						break;
					}

					list.remove(entitylivingbase);
				}
			}
			if (!world.isRemote)
			{
				if (this.isEntityAlive() && (getAttackTarget() != null) && getAttackTarget().isEntityAlive() && this.isOffensive && !this.isChild() && !this.isOnSameTeam(getAttackTarget()) && this.getDistanceSq(getAttackTarget()) < (double)((this.reachWidth * this.reachWidth) + ((getAttackTarget() instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)getAttackTarget()).reachWidth : getAttackTarget().width) * (getAttackTarget() instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)getAttackTarget()).reachWidth : getAttackTarget().width)) + 9D) && ((this.ticksExisted + this.getEntityId()) % 10 == 0))
				attackEntityAsMob(getAttackTarget());
				
				if (this.isEntityAlive())
				MobChunkLoader.updateLoaded(this);
				else
				MobChunkLoader.stopLoading(this);
			}
		}

		this.setSilent(this.isAIDisabled());
		
		this.isJumping = false;
		this.isAirBorne = false;
		this.onGround = true;
		if (this.getJukeboxToDanceTo() != null)
		{
			this.getNavigator().clearPath();
			IBlockState iblockstate = this.world.getBlockState(getJukeboxToDanceTo());
			Block block = iblockstate.getBlock();
			if (this.ticksExisted > 100)
			this.ticksExisted = 20;
			if (this.innerRotation > 500)
			this.innerRotation = 0;
			this.sitting = true;
			this.setPositionAndUpdate(this.getJukeboxToDanceTo().getX(), this.getJukeboxToDanceTo().getY() + 12D, this.getJukeboxToDanceTo().getZ());
			if (block != Blocks.JUKEBOX || (block == Blocks.JUKEBOX && !((Boolean)iblockstate.getValue(BlockJukebox.HAS_RECORD)).booleanValue()) || this.getDistanceSqToCenter(jukeBoxToDanceTo) > 10000D)
			{
				this.setJukeboxToDanceTo(null);
				getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
				this.sitting = false;
			}
		}

		if ((this.getJukeboxToDanceTo() == null) && this.ticksExisted % 60 == 0)
		{
			int i11 = (int)this.posY;
			int l1 = (int)this.posX;
			int i2 = (int)this.posZ;
			for (int k2 = -12 - (int)this.width; k2 <= 12 + (int)this.width; k2++)
			{
				for (int l2 = -12 - (int)this.width; l2 <= 12 + (int)this.width; l2++)
				{
					for (int j = -18 - (int)this.height; j <= 18 + (int)this.height; j++)
					{
						int i3 = l1 + k2;
						int k = i11 + j;
						int l = i2 + l2;
						BlockPos blockpos = new BlockPos(i3, k, l);
						IBlockState iblockstate = this.world.getBlockState(blockpos);
						Block block = iblockstate.getBlock();
						if (block == Blocks.JUKEBOX && ((Boolean)iblockstate.getValue(BlockJukebox.HAS_RECORD)).booleanValue())
						{
							this.setJukeboxToDanceTo(blockpos);
							if (this.ticksExisted > 100)
							this.ticksExisted = 20;
							if (this.innerRotation > 500)
							this.innerRotation = 0;
							getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
						}
					}
				}
			}
		}

		
		this.dragonPartHead.width = this.dragonPartHead.height = 2.5F * getFittness();
		this.dragonPartNeck.width = this.dragonPartNeck.height = 2.5F * getFittness();
		this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F * getFittness();
		this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F * getFittness();
		this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F * getFittness();
		this.dragonPartBody.height = 3.5F * getFittness();
		this.dragonPartBody.width = 5.0F * getFittness();
		this.dragonPartWing1.height = 3.0F * getFittness();
		this.dragonPartWing1.width = 4.0F * getFittness();
		this.dragonPartWing2.height = 3.0F * getFittness();
		this.dragonPartWing2.width = 4.0F * getFittness();
		Vec3d[] avec3d = new Vec3d[this.dragonPartArray.length];
		
		for (int j = 0; j < this.dragonPartArray.length; ++j)
		{
			avec3d[j] = new Vec3d(this.dragonPartArray[j].posX, this.dragonPartArray[j].posY, this.dragonPartArray[j].posZ);
		}

		float f14 = (float)(getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(10, 1.0F)[1]) * 10.0F * 0.017453292F;
		float f16 = MathHelper.cos(f14);
		float f18 = MathHelper.sin(f14);
		float f2 = this.rotationYaw * 0.017453292F;
		float f19 = MathHelper.sin(f2);
		float f3 = MathHelper.cos(f2);
		double[] adouble = getMovementOffsets(5, 1.0F);
		double[] adouble1 = getMovementOffsets(12 + 1 * 2, 1.0F);
		getMovementOffsets(12 + 2 * 2, 1.0F);
		this.dragonPartBody.onUpdate();
		this.dragonPartBody.setLocationAndAngles(this.posX, this.posY - (MathHelper.sin(this.animTime * (float)(Math.PI * 2) - 0.5F) * 0.1F * getFittness()), this.posZ, 0.0F, 0.0F);
		this.dragonPartWing1.onUpdate();
		this.dragonPartWing1.setLocationAndAngles(this.posX + f3 * (4.5F * getFittness()), this.posY + 1D + (MathHelper.sin(this.animTime * (float)(Math.PI * 2)) * 3F), this.posZ + f19 * (4.5F * getFittness()), 0.0F, 0.0F);
		this.dragonPartWing2.onUpdate();
		this.dragonPartWing2.setLocationAndAngles(this.posX - f3 * (4.5F * getFittness()), this.posY + 1D + (MathHelper.sin(this.animTime * (float)(Math.PI * 2)) * 3F), this.posZ - f19 * (4.5F * getFittness()), 0.0F, 0.0F);
		this.dragonPartNeck.onUpdate();
		this.dragonPartNeck.setLocationAndAngles(this.posX + f19 * (3.5F * getFittness()), this.posY + 1D - (MathHelper.sin(this.animTime * (float)(Math.PI * 2) + 1F) * 0.1F * getFittness()) + (double)(f18 * (2F * getFittness())) - (this.rotationPitch / 90F * Math.PI * 0.25F), this.posZ - f3 * (3.5F * getFittness()), 0.0F, 0.0F);
		this.dragonPartHead.onUpdate();
		this.dragonPartHead.setLocationAndAngles(this.posX + f19 * (6F * getFittness()), this.posY + 1D - (MathHelper.sin(this.animTime * (float)(Math.PI * 2)) * 0.1F * getFittness()) + (double)(f18 * (4F * getFittness())) - (this.rotationPitch / 90F * Math.PI * 1F), this.posZ - f3 * (6F * getFittness()), 0.0F, 0.0F);
		for (int j = 0; j < 3; j++)
		{
			ExtendMultiPartEntityPart MultiPartEntityPart = null;
			if (j == 0)
			{
				MultiPartEntityPart = this.dragonPartTail1;
			}
			if (j == 1)
			{
				MultiPartEntityPart = this.dragonPartTail2;
			}
			if (j == 2)
			{
				MultiPartEntityPart = this.dragonPartTail3;
			}
			adouble1 = getMovementOffsets(12 + j * 2, 1.0F);
			float f21 = this.rotationYaw * 0.017453292F + simplifyAngle(adouble1[0] - adouble[0]) * 0.017453292F;
			float f22 = MathHelper.sin(f21);
			float f7 = MathHelper.cos(f21);
			float f23 = 1.5F;
			float f24 = (j + 1) * 2.0F;
			MultiPartEntityPart.onUpdate();
			MultiPartEntityPart.setLocationAndAngles(this.posX - (f19 * f23 + f22 * f24 * getFittness()) * f16, this.posY - (MathHelper.sin(this.animTime * (float)(Math.PI * 2) + j) * (0.2F * (j + 1) * getFittness())) - (double)(f18 * (2F + 1 * j)) + f23, this.posZ + (f3 * f23 + f7 * f24 * getFittness()) * f16, 0.0F, 0.0F);
		}
		for (int l = 0; l < this.dragonPartArray.length; ++l)
		{
			this.dragonPartArray[l].prevPosX = avec3d[l].x;
			this.dragonPartArray[l].prevPosY = avec3d[l].y;
			this.dragonPartArray[l].prevPosZ = avec3d[l].z;
		}

		if (isWild() && this.getOwnerId() == null)
		{
			if (this.world.isRemote)
			{
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				double d3 = 10.0D;
				this.world.spawnParticle(EnumParticleTypes.TOWN_AURA, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width - d0 * d3, this.posY + this.rand.nextFloat() * this.height - d1 * d3, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width - d2 * d3, d0, d1, d2, new int[0]);
			}
		}
		else
		{
			if (this.getAttackTarget() != null && this.getJukeboxToDanceTo() == null && this.phaseManager.getCurrentPhase() != PhaseList.LANDING_APPROACH && this.phaseManager.getCurrentPhase() != PhaseList.LANDING)
			{
				this.faceEntity(this.getAttackTarget(), 10F, 90F);
				if (this.sitting)
				this.renderYawOffset = this.rotationYaw = this.rotationYawHead + 180F;
			}
			else
			{
				this.rotationYawHead = this.rotationYaw - 180F;
				this.rotationPitch = getPhaseManager().getCurrentPhase().getIsStationary() || this.sitting ? 20F : 0F;
			}
			this.convertionInt = 0;
			if (isHero())
			{
				for (int j = 0; j < this.dragonPartArray.length; ++j)
				{
					avec3d[j] = new Vec3d(this.dragonPartArray[j].posX, this.dragonPartArray[j].posY, this.dragonPartArray[j].posZ);
					double d0 = this.rand.nextGaussian() * 0.02D;
					double d1 = this.rand.nextGaussian() * 0.02D;
					double d2 = this.rand.nextGaussian() * 0.02D;
					double d3 = 10.0D;
					this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.dragonPartArray[j].posX + this.rand.nextFloat() * this.dragonPartArray[j].width * 2.0F - this.dragonPartArray[j].width - d0 * d3, this.dragonPartArray[j].posY + this.rand.nextFloat() * this.dragonPartArray[j].height - d1 * d3, this.dragonPartArray[j].posZ + this.rand.nextFloat() * this.dragonPartArray[j].width * 2.0F - this.dragonPartArray[j].width - d2 * d3, d0, 0.10000000149011612D, d2, new int[0]);
				}
			}
		}
		if ((this.rand.nextInt(5) == 0) && (!isWild()) && getOwner() != null && (getOwner().getRevengeTarget() != null) && (this.isOffensive))
		{
			setAttackTarget(getOwner().getRevengeTarget());
		}
		this.noClip = true;
		
		this.reachWidth = 8F;
		
		setSize(16F, 3.6F);
		this.onGround = false;
		this.isAirBorne = true;
		if (this.ticksExisted % 5 == 0)
		{
			this.slowed = false;
		}

		if (this.world.isRemote)
		{
			setHealth(getHealth());
			if (!isSilent())
			{
				float f = MathHelper.cos(this.animTime * 6.2831855F);
				float f1 = MathHelper.cos(this.prevAnimTime * 6.2831855F);
				if ((f1 <= -0.3F) && (f >= -0.3F))
				{
					if (isSneaking())
					this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, getSoundCategory(), 1.0F, 0.6F + this.rand.nextFloat() * 0.3F, false);
					else
					this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, getSoundCategory(), 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
				}
				if ((!this.phaseManager.getCurrentPhase().getIsStationary()) && (--this.field_184678_bK < 0))
				{
					if (isSneaking())
					this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, getSoundCategory(), 1.0F, 0.6F + this.rand.nextFloat() * 0.3F, false);
					else
					this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, getSoundCategory(), 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
					this.field_184678_bK = (200 + this.rand.nextInt(200));
				}
			}
		}
		this.prevAnimTime = this.animTime;
		
		if (isAIDisabled())
		{
			this.animTime = 0.0F;
		}
		else if (this.isEntityAlive())
		{
			if (this.ringBufferIndex < 0)
			{
				for (int i = 0; i < this.ringBuffer.length; i++)
				{
					this.ringBuffer[i][0] = this.rotationYaw;
					this.ringBuffer[i][1] = this.posY;
				}
			}
			if (++this.ringBufferIndex == this.ringBuffer.length)
			{
				this.ringBufferIndex = 0;
			}
			this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
			this.ringBuffer[this.ringBufferIndex][1] = this.posY;
			if (this.world.isRemote)
			{
				if (this.newPosRotationIncrements > 0)
				{
					double d5 = this.posX + (this.interpTargetX - this.posX) / this.newPosRotationIncrements;
					double d0 = this.posY + (this.interpTargetY - this.posY) / this.newPosRotationIncrements;
					double d1 = this.posZ + (this.interpTargetZ - this.posZ) / this.newPosRotationIncrements;
					double d2 = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
					this.rotationYaw = ((float)(this.rotationYaw + d2 / this.newPosRotationIncrements));
					this.rotationPitch = ((float)(this.rotationPitch + (this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements));
					this.newPosRotationIncrements -= 1;
					setPosition(d5, d0, d1);
					setRotation(this.rotationYaw, this.rotationPitch);
				}
				this.phaseManager.getCurrentPhase().doClientRenderEffects();
			}
			else
			{
				IPhase iphase = this.phaseManager.getCurrentPhase();
				iphase.doLocalUpdate();
				
				if (this.phaseManager.getCurrentPhase() != iphase)
				{
					iphase = this.phaseManager.getCurrentPhase();
					iphase.doLocalUpdate();
				}

				Vec3d vec3d = iphase.getTargetLocation();
				
				if (vec3d != null)
				{
					double d6 = vec3d.x - this.posX;
					double d7 = vec3d.y - this.posY;
					double d8 = vec3d.z - this.posZ;
					double d3 = d6 * d6 + d7 * d7 + d8 * d8;
					float f5 = iphase.getMaxRiseOrFall();
					d7 = MathHelper.clamp(d7 / (double)MathHelper.sqrt(d6 * d6 + d8 * d8), (double)(-f5), (double)f5);
					this.motionY += d7 * 0.10000000149011612D;
					this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
					double d4 = MathHelper.clamp(MathHelper.wrapDegrees(180.0D - MathHelper.atan2(d6, d8) * (180D / Math.PI) - (double)this.rotationYaw), -50.0D, 50.0D);
					Vec3d vec3d1 = (new Vec3d(vec3d.x - this.posX, vec3d.y - this.posY, vec3d.z - this.posZ)).normalize();
					Vec3d vec3d2 = (new Vec3d((double)MathHelper.sin(this.rotationYaw * 0.017453292F), this.motionY, (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)))).normalize();
					float f7 = Math.max(((float)vec3d2.dotProduct(vec3d1) + 0.5F) / 1.5F, 0.0F);
					this.randomYawVelocity *= 0.8F;
					this.randomYawVelocity = (float)((double)this.randomYawVelocity + d4 * (double)iphase.getYawFactor());
					this.rotationYaw += this.randomYawVelocity * 0.1F;
					float f8 = (float)(2.0D / (d3 + 1.0D));
					if (this.ticksExisted > 20 && !this.sitting)
					this.moveRelative(0.0F, 0.0F, -1.0F, 0.06F * (f7 * f8 + (1.0F - f8)));
					if (this.ticksExisted > 20 && !this.sitting)
					if (getPhaseManager().getCurrentPhase() == PhaseList.CHARGING_PLAYER)
					{
						if (isSneaking())
						{
							move(MoverType.SELF, this.motionX * (this.isBeingRidden() ? 0.375D : 0.25D), this.motionY * (this.isBeingRidden() ? 0.375D : 0.25D), this.motionZ * (this.isBeingRidden() ? 0.375D : 0.25D));
						}
						else if (this.moralRaisedTimer > 200)
						{
							move(MoverType.SELF, this.motionX * (this.isBeingRidden() ? 15D : 10D), this.motionY * (this.isBeingRidden() ? 15D : 10D), this.motionZ * (this.isBeingRidden() ? 15D : 10D));
						}
						else
						{
							move(MoverType.SELF, this.motionX * (this.isBeingRidden() ? 7.5D : 5D), this.motionY * (this.isBeingRidden() ? 7.5D : 5D), this.motionZ * (this.isBeingRidden() ? 7.5D : 5D));
						}
					}
					else
					{
						if (isSneaking())
						{
							move(MoverType.SELF, this.motionX * (this.isBeingRidden() ? 0.375D : 0.25D), this.motionY * (this.isBeingRidden() ? 0.375D : 0.25D), this.motionZ * (this.isBeingRidden() ? 0.375D : 0.25D));
						}
						if (this.moralRaisedTimer > 200)
						{
							move(MoverType.SELF, this.motionX * (this.isBeingRidden() ? 3D : 2D), this.motionY * (this.isBeingRidden() ? 3D : 2D), this.motionZ * (this.isBeingRidden() ? 3D : 2D));
						}
						else
						{
							move(MoverType.SELF, this.motionX * (this.isBeingRidden() ? 1.5D : 1D), this.motionY * (this.isBeingRidden() ? 1.5D : 1D), this.motionZ * (this.isBeingRidden() ? 1.5D : 1D));
						}
					}
					Vec3d vec3d3 = (new Vec3d(this.motionX, this.motionY, this.motionZ)).normalize();
					float f10 = ((float)vec3d3.dotProduct(vec3d2) + 1.0F) / 2.0F;
					f10 = 0.8F + 0.15F * f10;
					this.motionX *= (double)f10;
					this.motionZ *= (double)f10;
					this.motionY *= 0.9100000262260437D;
				}
			}

			if (!this.world.isRemote)
			{
				collideWithEntities(this.dragonPartHead, this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().grow(1D)));
				collideWithEntities(this.dragonPartNeck, this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartNeck.getEntityBoundingBox().grow(1D)));
				collideWithEntities(this.dragonPartBody, this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartBody.getEntityBoundingBox().grow(1D)));
				flingEntities(this.dragonPartWing1, this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().grow(4D).offset(0.0D, -2.0D, 0.0D)));
				flingEntities(this.dragonPartWing2, this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().grow(4D).offset(0.0D, -2.0D, 0.0D)));
				collideWithEntities(this.dragonPartTail1, this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartTail1.getEntityBoundingBox().grow(1D)));
				collideWithEntities(this.dragonPartTail2, this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartTail2.getEntityBoundingBox().grow(1D)));
				collideWithEntities(this.dragonPartTail3, this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartTail3.getEntityBoundingBox().grow(1D)));
				attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().grow(3D)));
				attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartNeck.getEntityBoundingBox().grow(2D)));
			}
			destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox());
			destroyBlocksInAABB(this.dragonPartNeck.getEntityBoundingBox());
			destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
			destroyBlocksInAABB(this.dragonPartWing1.getEntityBoundingBox());
			destroyBlocksInAABB(this.dragonPartWing2.getEntityBoundingBox());
			destroyBlocksInAABB(this.dragonPartTail1.getEntityBoundingBox());
			destroyBlocksInAABB(this.dragonPartTail2.getEntityBoundingBox());
			destroyBlocksInAABB(this.dragonPartTail3.getEntityBoundingBox());
		}
		if (getSpecialAttackTimer() > 0)
		{
			if (this.moralRaisedTimer > 200)
			{
				setSpecialAttackTimer(getSpecialAttackTimer() - 2);
			} else {
					setSpecialAttackTimer(getSpecialAttackTimer() - 1);
				}
			}
			if (getHealth() <= 0.0F && this.deathTicks > 0)
			{
				for (int j = 0; j < this.dragonPartArray.length; ++j)
				{
					avec3d[j] = new Vec3d(this.dragonPartArray[j].posX, this.dragonPartArray[j].posY, this.dragonPartArray[j].posZ);
					double d1 = rand.nextFloat() * this.dragonPartArray[j].width - (this.dragonPartArray[j].width / 2);
					double d2 = rand.nextFloat() * this.dragonPartArray[j].height - (this.dragonPartArray[j].height / 2);
					double d3 = rand.nextFloat() * this.dragonPartArray[j].width - (this.dragonPartArray[j].width / 2);
					this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.dragonPartArray[j].posX + d1, this.dragonPartArray[j].posY + d2, this.dragonPartArray[j].posZ + d3, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
				}
				float f13 = (this.rand.nextFloat() - 0.5F) * 8.0F;
				float f15 = (this.rand.nextFloat() - 0.5F) * 4.0F;
				float f17 = (this.rand.nextFloat() - 0.5F) * 8.0F;
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f13, this.posY + f15, this.posZ + f17, 0.0D, 0.0D, 0.0D, new int[0]);
			}
			else
			{
				if (this.getAttackTarget() != null && (!getPhaseManager().getCurrentPhase().getIsStationary()) && (getPhaseManager().getCurrentPhase() != PhaseList.CHARGING_PLAYER) && getPhaseManager().getCurrentPhase() != PhaseList.STRAFE_PLAYER && (getAttackTarget() != null) && this.ticksExisted % 60 == 0 && (getRNG().nextInt(5) == 0) && !this.sitting)
				{
					switch (this.rand.nextInt(2))
					{
						case 0:getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
						((PhaseRamAttack)getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER)).func_188668_a(new Vec3d(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ));
						break;
						case 1:getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
						((PhaseFireballAndStrafe)getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER)).func_188686_a(getAttackTarget());
					}
				}

				if ((isHero()) && (getSpecialAttackTimer() > 1995))
				{
					List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(256.0D, 256.0D, 256.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
					if ((list != null) && (!list.isEmpty()))
					{
						for (int i1 = 0; i1 < list.size(); i1++)
						{
							EntityLivingBase entity = (EntityLivingBase)list.get(i1);
							if (entity != null)
							{
								if (!isOnSameTeam(entity))
								{
									double d6 = this.posX + (this.rand.nextDouble() * 32.0D - 16.0D);
									double d7 = this.posY + 64.0D + (this.rand.nextDouble() * 32.0D - 16.0D);
									double d8 = this.posZ + (this.rand.nextDouble() * 32.0D - 16.0D);
									double d9 = entity.posX - d6;
									double d10 = entity.posY + entity.height / 2.0F - (d7 + this.height / 2.0F);
									double d11 = entity.posZ - d8;
									this.world.playEvent((EntityPlayer)null, 1016, new BlockPos(this), 0);
									EntityDragonFireballOther entitydragonfireball = new EntityDragonFireballOther(this.world, this, d9, d10, d11);
									entitydragonfireball.posX = d6;
									entitydragonfireball.posY = d7;
									entitydragonfireball.posZ = d8;
									this.world.spawnEntity(entitydragonfireball);
								}
							}
						}
					}
				}
				if ((getAttackTarget() != null) && (getAttackTarget().isEntityAlive()) && (getSpecialAttackTimer() <= 0) && (isHero()))
				{
					performSpecialAttack();
				}
				if ((isBeingRidden()) && (getControllingPassenger() != null) && ((getControllingPassenger() instanceof EntityPlayer)))
				{
					EntityPlayer passenger = (EntityPlayer)getControllingPassenger();
					
					passenger.fallDistance *= 0F;
					passenger.hurtResistantTime = 40;
					passenger.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 40, 4));
					getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
					this.renderYawOffset = this.rotationYaw = passenger.rotationYawHead + 180F;
					this.rotationPitch = 0F;
					for (int i = 0; i < 256; i++)
					{
						double d1 = 0.005D;
						if (this.moralRaisedTimer > 200)
						d1 *= 2.0D;
						if (this.isSprinting())
						d1 *= 2.0D;
						
						Vec3d vec3 = passenger.getLook(1.0F);
						if (passenger.moveForward > 0.0F)
						{
							setPosition(this.posX + vec3.x * d1, this.posY + vec3.y * d1, this.posZ + vec3.z * d1);
							Entity[] aentity = this.getParts();
							
							if (aentity != null)
							{
								for (Entity entity : aentity)
								{
									entity.setLocationAndAngles(entity.posX + vec3.x * d1, entity.posY + vec3.y * d1, entity.posZ + vec3.z * d1, 0.0F, 0.0F);
								}
							}
						}
						if (passenger.moveForward < 0.0F)
						{
							setPosition(this.posX - vec3.x * d1, this.posY - vec3.y * d1, this.posZ - vec3.z * d1);
						}
					}
				}
				if ((!isWild() && getOwner() != null) && (getOwner().isSprinting()))
				{
					setSprinting(true);
				} else {
						setSprinting(false);
					}
					if ((!isWild() && getOwner() != null) && (getOwner().isSneaking()))
					{
						setSneaking(true);
					} else {
							setSneaking(false);
						}
						if ((isSneaking()) && (!getPhaseManager().getCurrentPhase().getIsStationary()) && (this.rand.nextInt(100) == 0))
						{
							getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
						}
						if ((!getPhaseManager().getCurrentPhase().getIsStationary()) && !this.isWild() && getOwner() != null && (getOwner().getHealth() <= 6.0F || (!this.getOwner().getHeldItemMainhand().isEmpty() && this.getOwner().getHeldItemMainhand().getItem() == Items.GLASS_BOTTLE)) && (getRNG().nextInt(20) == 0))
						{
							getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
							((PhaseFireballAndStrafe)getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER)).func_188686_a(getOwner());
						}
						
						if (!this.isWild() && getOwner() != null && getDistanceSq(getOwner()) >= 48400.0D)
						{
							setLocationAndAngles(getOwner().posX, getOwner().posY, getOwner().posZ, this.rotationYaw, this.rotationPitch);
						}
						if (!this.world.isRemote && getAttackTarget() != null && getAttackTarget().isEntityAlive() && getAttackTarget().canEntityBeSeen(this) && this.rand.nextInt(120) == 0)
						{
							double d1 = this.dragonPartHead.posX;
							double d2 = this.dragonPartHead.posY + 0.25D;
							double d3 = this.dragonPartHead.posZ;
							if (this.getPolymorphTime() > 0 && this.rand.nextBoolean())
							{
								for (int i = 0; i < (this.isHero() ? 36 : 18); ++i)
								{
									EntityMagicMissile entitymagicmissiles = new EntityMagicMissile(this.world, getAttackTarget(), this, d1, d2, d3);
									Random random = new Random();
									entitymagicmissiles.motionX += random.nextDouble() * 2.0D - 1.0D + this.motionX;
									entitymagicmissiles.motionY += random.nextDouble() * 2.0D + this.motionY;
									entitymagicmissiles.motionZ += random.nextDouble() * 2.0D - 1.0D + this.motionZ;
									this.world.spawnEntity(entitymagicmissiles);
								}
							}
							else
							this.fireLightning(getAttackTarget(), d1, d2, d3);
						}

						if (!this.world.isRemote && (getAttackTarget() != null) && getAttackTarget().isEntityAlive() && getAttackTarget().canEntityBeSeen(this) && this.rand.nextInt(120) == 0)
						{
							double d6 = this.dragonPartHead.posX;
							double d7 = this.dragonPartHead.posY + 0.5D;
							double d8 = this.dragonPartHead.posZ;
							double d9 = (this.getAttackTarget().posX) - d6;
							double d10 = (this.getAttackTarget().posY + 1D) - d7;
							double d11 = (this.getAttackTarget().posZ) - d8;
							this.world.playEvent((EntityPlayer)null, 1016, new BlockPos(this), 0);
							if (this.getPolymorphTime() > 0 && this.rand.nextBoolean())
							{
								EntityInvisibleFangsProjectile entitydragonfireball = new EntityInvisibleFangsProjectile(this.world, this, d6, d7, d8);
								entitydragonfireball.posX = d6;
								entitydragonfireball.posY = d7;
								entitydragonfireball.posZ = d8;
								this.world.spawnEntity(entitydragonfireball);
							}
							else
							{
								EntityDragonFireballOther entitydragonfireball = new EntityDragonFireballOther(this.world, this, d9, d10, d11);
								entitydragonfireball.posX = d6;
								entitydragonfireball.posY = d7;
								entitydragonfireball.posZ = d8;
								this.world.spawnEntity(entitydragonfireball);
							}
						}

						if ((this.rand.nextInt(2) == 0) && (!isWild() && getOwner() != null) && (getOwner().getRevengeTarget() != null))
						{
							setAttackTarget(getOwner().getRevengeTarget());
						}
						if ((getAttackTarget() != null) && ((!getAttackTarget().isEntityAlive()) || (!this.isOffensive) || (isOnSameTeam(getAttackTarget()))))
						{
							setAttackTarget(null);
						}
						updateDragonEnderCrystal();
						float f12 = 0.2F / (MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
						f12 *= (float)Math.pow(2.0D, this.motionY);
						if (this.ticksExisted > 20)
						if ((this.phaseManager.getCurrentPhase().getIsStationary()) || (this.isBeingRidden() && ((EntityLivingBase)this.getControllingPassenger()).moveForward == 0F) || (getPhaseManager().getCurrentPhase() == PhaseList.CHARGING_PLAYER) || this.sitting)
						{
							if (isSneaking())
							{
								this.animTime += 0.05F;
							} else {
									this.animTime += 0.1F;
								}
							} else if (this.slowed)
								{
									this.animTime += f12 * 0.5F;
								}
								else
								{
									this.animTime += f12;
								}

								this.prevRenderYawOffset = this.renderYawOffset = this.rotationYaw;
							}

							if (this.ticksExisted % 20 == 0 && getID() == 0)
							{
								heal(1.0F);
							}
						}
						private void updateDragonEnderCrystal()
						{
							if (this.ticksExisted % 5 == 0 && this.isEntityAlive() && this.isCarryingCrystal())
							{
								this.setHealth(getHealth() + 1F);
							}
						}
						
						protected void collideWithEntities(ExtendMultiPartEntityPart part, List<?> list)
						{
							double d0 = (part.getEntityBoundingBox().minX + part.getEntityBoundingBox().maxX) / 2.0D;
							double d1 = (part.getEntityBoundingBox().minZ + part.getEntityBoundingBox().maxZ) / 2.0D;
							Iterator iterator = list.iterator();
							while (iterator.hasNext())
							{
								Entity entity = (Entity)iterator.next();
								if (this.isEntityAlive() && ((entity instanceof EntityLivingBase)) && (!isOnSameTeam((EntityLivingBase)entity)) && (!this.world.isRemote))
								{
									double d2 = entity.posX - d0;
									double d3 = entity.posZ - d1;
									double d4 = d2 * d2 + d3 * d3;
									entity.addVelocity(d2 / d4 * 4D, 0.75D, d3 / d4 * 4D);
									this.slowed = true;
									if (entity instanceof EntityLivingBase)
									{
										((EntityLivingBase)entity).renderYawOffset = ((EntityLivingBase)entity).rotationYaw = ((EntityLivingBase)entity).rotationYawHead = (float)MathHelper.atan2(entity.motionZ, entity.motionX) * (180F / (float)Math.PI) - 90.0F;
										((EntityLivingBase)entity).setRevengeTarget(null);
										if (entity instanceof EntityLiving)
										((EntityLiving)entity).setAttackTarget(null);
									}
								}
							}
						}

						protected void flingEntities(ExtendMultiPartEntityPart part, List<?> list)
						{
							double d0 = (part.getEntityBoundingBox().minX + part.getEntityBoundingBox().maxX) / 1.5D;
							double d1 = (part.getEntityBoundingBox().minZ + part.getEntityBoundingBox().maxZ) / 1.5D;
							Iterator iterator = list.iterator();
							while (iterator.hasNext())
							{
								Entity entity = (Entity)iterator.next();
								if (this.isEntityAlive() && ((entity instanceof EntityLivingBase)) && (!isOnSameTeam((EntityLivingBase)entity)) && (!this.world.isRemote))
								{
									double d2 = entity.posX - d0;
									double d3 = entity.posZ - d1;
									double d4 = d2 * d2 + d3 * d3;
									entity.addVelocity(d2 / d4 * 32D, 1.5D, d3 / d4 * 32D);
									if (entity instanceof EntityLivingBase)
									{
										((EntityLivingBase)entity).renderYawOffset = ((EntityLivingBase)entity).rotationYaw = ((EntityLivingBase)entity).rotationYawHead = (float)MathHelper.atan2(entity.motionZ, entity.motionX) * (180F / (float)Math.PI) - 90.0F;
										((EntityLivingBase)entity).setRevengeTarget(null);
										if (entity instanceof EntityLiving)
										((EntityLiving)entity).setAttackTarget(null);
									}
									entity.attackEntityFrom(DamageSource.FLY_INTO_WALL, 4F);
								}
							}
						}
						protected void attackEntitiesInList(List<?> p_70971_1_)
						{
							for (int i = 0; i < p_70971_1_.size(); i++)
							{
								Entity entity = (Entity)p_70971_1_.get(i);
								if (this.isEntityAlive() && entity.ticksExisted + entity.getEntityId() % 10 == 0 && !this.world.isRemote && ((entity instanceof EntityLivingBase)) && (!isOnSameTeam((EntityLivingBase)entity)))
								{
									attackEntityAsMob(entity);
									if (entity instanceof EntityLivingBase)
									{
										((EntityLivingBase)entity).renderYawOffset = ((EntityLivingBase)entity).rotationYaw = ((EntityLivingBase)entity).rotationYawHead = (float)MathHelper.atan2(entity.motionZ, entity.motionX) * (180F / (float)Math.PI) - 90.0F;
										((EntityLivingBase)entity).setRevengeTarget(null);
										if (entity instanceof EntityLiving)
										((EntityLiving)entity).setAttackTarget(null);
									}
									this.playSound(SoundEvents.BLOCK_NOTE_HAT, 5F, 0.5F);
									this.slowed = true;
								}
							}
						}
						private float simplifyAngle(double p_70973_1_)
						{
							return (float)MathHelper.wrapDegrees(p_70973_1_);
						}
						protected boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_)
						{
							int i = MathHelper.floor(p_70972_1_.minX);
							int j = MathHelper.floor(p_70972_1_.minY);
							int k = MathHelper.floor(p_70972_1_.minZ);
							int l = MathHelper.floor(p_70972_1_.maxX);
							int i1 = MathHelper.floor(p_70972_1_.maxY);
							int j1 = MathHelper.floor(p_70972_1_.maxZ);
							boolean flag1 = false;
							for (int k1 = i; k1 <= l; k1++)
							{
								for (int l1 = j; l1 <= i1; l1++)
								{
									for (int i2 = k; i2 <= j1; i2++)
									{
										BlockPos blockpos = new BlockPos(k1, l1, i2);
										IBlockState iblockstate = this.world.getBlockState(blockpos);
										Block block = iblockstate.getBlock();
										if (this.world.getGameRules().getBoolean("mobGriefing") && this.isEntityAlive() && !block.isAir(iblockstate, this.world, blockpos))
										{
											if (block.canEntityDestroy(iblockstate, this.world, blockpos, this))
											{
												if ((block != Blocks.END_PORTAL) && (block != Blocks.DRAGON_EGG) && (block != Blocks.BEDROCK) && (block != Blocks.END_STONE) && (block != Blocks.OBSIDIAN) && (block != Blocks.COMMAND_BLOCK) && (block != Blocks.REPEATING_COMMAND_BLOCK) && (block != Blocks.CHAIN_COMMAND_BLOCK) && (block != Blocks.IRON_BARS) && (block != Blocks.END_GATEWAY))
												{
													if (!this.world.isRemote)
													flag1 = (this.world.setBlockToAir(blockpos)) || (flag1);
												}
												else
												{
													this.slowed = true;
												}
											}
										}
									}
								}
							}
							return this.world.getGameRules().getBoolean("mobGriefing");
						}
						public int getDamageCap()
						{
							return 50;
						}
						
						public boolean isEntityInvulnerable(DamageSource source)
						{
							return this.getID() == 3 ? true : super.isEntityInvulnerable(source);
						}

						public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage)
						{
							if (getHealth() <= 0.0F)
							this.phaseManager.setPhase(PhaseList.DYING);
							
							damage = this.phaseManager.getCurrentPhase().getAdjustedDamage(dragonPart, source, damage);
							
							if (dragonPart != this.dragonPartHead)
							damage = damage / 5.0F + Math.min(damage, 1.0F);
							if (this.isEntityInvulnerable(source) || damage < 0.01F || source == DamageSource.WITHER || source == DamageSource.IN_WALL || source == DamageSource.DROWN || source == DamageSource.CRAMMING || source == DamageSource.CACTUS)
							{
								return false;
							}
							else
							{
								float f = this.getHealth();
								super.attackEntityFrom(source, damage);
								
								if (this.getHealth() <= 0.0F && !this.phaseManager.getCurrentPhase().getIsStationary() && !this.isWild())
								{
									this.phaseManager.setPhase(PhaseList.DYING);
								}

								if (this.phaseManager.getCurrentPhase().getIsStationary())
								{
									this.sittingDamageReceived = (int)((float)this.sittingDamageReceived + (f - this.getHealth()));
									
									if ((float)this.sittingDamageReceived > 50F)
									{
										this.sittingDamageReceived = 0;
										if (source.getTrueSource() instanceof EntityLivingBase)
										switch (this.rand.nextInt(2))
										{
											case 0:this.getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
											((PhaseRamAttack)this.getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER)).func_188668_a(new Vec3d(((EntityLivingBase) source.getTrueSource()).posX, ((EntityLivingBase) source.getTrueSource()).posY, ((EntityLivingBase) source.getTrueSource()).posZ));
											break;
											case 1:this.getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
											((PhaseFireballAndStrafe)this.getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER)).func_188686_a((EntityLivingBase) source.getTrueSource());
										}
									}
								}
								if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityLivingBase)
								{
									if (!this.isWild() && getOwner() != null && source.getTrueSource() == this.getOwner())
									{
										this.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
									}
									else
									{
										this.setAttackTarget((EntityLivingBase)source.getTrueSource());
										if (this.rand.nextInt(2) == 0)
										{
											this.getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
											((PhaseRamAttack)this.getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER)).func_188668_a(new Vec3d(source.getTrueSource().posX, source.getTrueSource().posY, source.getTrueSource().posZ));
										}
									}
								}

								return true;
							}
						}

						public boolean attackEntityFrom(DamageSource source, float amount)
						{
							return this.attackEntityFromPart(dragonPartBody, source, amount);
						}
						protected void onDeathUpdate()
						{
							Lists.newArrayList();
							
							List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.getPosition()).grow(256D));
							for(Entity entity : entities)
							{
								if(entity instanceof EntityEnderCrystal)
								{
									entity.attackEntityFrom(DamageSource.GENERIC, 1F);
									this.setHealth(50F);
									this.dead = false;
									this.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
									this.world.setEntityState(this, (byte)35);
									this.playSound(SoundEvents.ITEM_TOTEM_USE, 10F, 1F);
									this.deathTicks = 0;
									break;
								}
							}

							if ((this.deathTicks >= 180) && (this.deathTicks <= 200))
							{
								float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
								float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
								float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
								this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D, new int[0]);
							}
							if (!this.world.isRemote)
							{
								if ((this.deathTicks > 150) && (this.deathTicks % 5 == 0) && (this.world.getGameRules().getBoolean("doMobLoot")))
								{
									int i = 1000;
									while (i > 0)
									{
										int j = EntityXPOrb.getXPSplit(i);
										i -= j;
										this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 8.0D, this.posZ, j));
									}
								}
								if (this.deathTicks == 1)
								{
									this.world.playBroadcastSound(1028, new BlockPos(this), 0);
									if (getOwner() != null)
									{
										for (EntityPlayer entityplayer : world.playerEntities)
										{
											world.playSound(null, entityplayer.getPosition(), SoundEvents.ENTITY_ENDERDRAGON_DEATH, this.getSoundCategory(), getSoundVolume(), 1.0F);
											entityplayer.sendStatusMessage(new TextComponentTranslation("\u00A74"+ this.getOwner().getName() + "'s " + this.getName() + " has been killed!!!"), true);
										}
										((EntityPlayerMP)getOwner()).sendMessage(new TextComponentTranslation("Your Ender Dragon has fallen in battle.", new Object[0]));
									}
									
									if (!this.world.isRemote && this.getID() == 6)
									{
										setID(7);
										for (EntityPlayer entityplayer : this.world.playerEntities)
											entityplayer.sendMessage(new TextComponentTranslation(quotes[6][Maths.random(0, quotes[6].length - 1)], new Object[] {entityplayer.getDisplayName()}));
									}
								}
							}
							double d0 = (this.isWild() ? 0D : this.getOwner().posX) - this.posX;
							double d1 = (this.isWild() ? this.world.getTopSolidOrLiquidBlock(net.minecraft.world.gen.feature.WorldGenEndPodium.END_PODIUM_LOCATION).getY() : this.getOwner().posY + 4D) - this.posY;
							double d2 = (this.isWild() ? 0D : this.getOwner().posZ) - this.posZ;
							double d3 = d0 * d0 + d2 * d2;
							if (this.ticksExisted > 20)
							if (d3 > 1D && this.deathTicks < 1)
							{
								double d5 = MathHelper.sqrt(d3);
								this.renderYawOffset = this.rotationYaw = this.rotationYawHead = ((float)Math.atan2(this.motionZ, this.motionX) * 57.295776F - 90.0F) + 180F;
								this.motionX += (d0 / d5 * 0.75D - this.motionX);
								this.motionY += (d1 / d5 * 0.75D - this.motionY);
								this.motionZ += (d2 / d5 * 0.75D - this.motionZ);
								move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
							}
							else
							{
								move(MoverType.SELF, 0.0D, 0.10000000149011612D, 0.0D);
								this.renderYawOffset = this.rotationYawHead = this.rotationYaw += 10.0F;
								this.deathTicks += 1;
							}
							if ((this.deathTicks == 200) && (!this.world.isRemote))
							{
								int i = 2000;
								while (i > 0)
								{
									int j = EntityXPOrb.getXPSplit(i);
									i -= j;
									this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 8.0D, this.posZ, j));
								}
								this.world.setBlockState(new BlockPos(this.posX, this.posY + 4D, this.posZ), Blocks.DRAGON_EGG.getDefaultState(), 3);
								entityDropItem(new ItemStack(Items.SKULL, 1, 5), 4.0F);
								dropItemWithOffset(Items.ELYTRA, 1, 4.0F);
								setDead();
							}
						}
						/**
						* Generates values for the fields pathPoints, and neighbors, and then returns the nearest pathPoint to the
						* specified position.
						*/
						public int initPathPoints()
						{
							if (this.pathPoints[0] == null)
							{
								for (int i = 0; i < 24; ++i)
								{
									int j = 5;
									int l;
									int i1;
									
									if (i < 12)
									{
										l = (int)(60.0F * MathHelper.cos(2.0F * (-(float)Math.PI + 0.2617994F * (float)i)));
										i1 = (int)(60.0F * MathHelper.sin(2.0F * (-(float)Math.PI + 0.2617994F * (float)i)));
									}
									else if (i < 20)
									{
										int lvt_3_1_ = i - 12;
										l = (int)(40.0F * MathHelper.cos(2.0F * (-(float)Math.PI + 0.3926991F * (float)lvt_3_1_)));
										i1 = (int)(40.0F * MathHelper.sin(2.0F * (-(float)Math.PI + 0.3926991F * (float)lvt_3_1_)));
										j += 10;
									}
									else
									{
										int k1 = i - 20;
										l = (int)(20.0F * MathHelper.cos(2.0F * (-(float)Math.PI + ((float)Math.PI / 4F) * (float)k1)));
										i1 = (int)(20.0F * MathHelper.sin(2.0F * (-(float)Math.PI + ((float)Math.PI / 4F) * (float)k1)));
									}

									int j1 = Math.max(this.world.getSeaLevel() + 10, this.world.getTopSolidOrLiquidBlock(new BlockPos(l, 0, i1)).getY() + j);
									this.pathPoints[i] = new PathPoint(l, j1, i1);
								}

								this.neighbors[0] = 6146;
								this.neighbors[1] = 8197;
								this.neighbors[2] = 8202;
								this.neighbors[3] = 16404;
								this.neighbors[4] = 32808;
								this.neighbors[5] = 32848;
								this.neighbors[6] = 65696;
								this.neighbors[7] = 131392;
								this.neighbors[8] = 131712;
								this.neighbors[9] = 263424;
								this.neighbors[10] = 526848;
								this.neighbors[11] = 525313;
								this.neighbors[12] = 1581057;
								this.neighbors[13] = 3166214;
								this.neighbors[14] = 2138120;
								this.neighbors[15] = 6373424;
								this.neighbors[16] = 4358208;
								this.neighbors[17] = 12910976;
								this.neighbors[18] = 9044480;
								this.neighbors[19] = 9706496;
								this.neighbors[20] = 15216640;
								this.neighbors[21] = 13688832;
								this.neighbors[22] = 11763712;
								this.neighbors[23] = 8257536;
							}

							return this.getNearestPpIdx(this.posX, this.posY, this.posZ);
						}

						/**
						* Returns the index into pathPoints of the nearest PathPoint.
						*/
						public int getNearestPpIdx(double x, double y, double z)
						{
							float f = 10000.0F;
							int i = 0;
							PathPoint pathpoint = new PathPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
							int j = 0;
							
							for (int k = j; k < 24; ++k)
							{
								if (this.pathPoints[k] != null)
								{
									float f1 = this.pathPoints[k].distanceToSquared(pathpoint);
									
									if (f1 < f)
									{
										f = f1;
										i = k;
									}
								}
							}

							return i;
						}/**
							* Find and return a path among the circles described by pathPoints, or null if the shortest path would just be
							* directly between the start and finish with no intermediate points.
							** Starting with pathPoint[startIdx], it searches the neighboring points (and their neighboring points, and so on)
							* until it reaches pathPoint[finishIdx], at which point it calls makePath to seal the deal.
							*/
							@Nullable
							public Path findPath(int startIdx, int finishIdx, @Nullable PathPoint andThen)
							{
								for (int i = 0; i < 24; ++i)
								{
									PathPoint pathpoint = this.pathPoints[i];
									pathpoint.visited = false;
									pathpoint.distanceToTarget = 0.0F;
									pathpoint.totalPathDistance = 0.0F;
									pathpoint.distanceToNext = 0.0F;
									pathpoint.previous = null;
									pathpoint.index = -1;
								}

								PathPoint pathpoint4 = this.pathPoints[startIdx];
								PathPoint pathpoint5 = this.pathPoints[finishIdx];
								pathpoint4.totalPathDistance = 0.0F;
								pathpoint4.distanceToNext = pathpoint4.distanceTo(pathpoint5);
								pathpoint4.distanceToTarget = pathpoint4.distanceToNext;
								this.pathFindQueue.clearPath();
								this.pathFindQueue.addPoint(pathpoint4);
								PathPoint pathpoint1 = pathpoint4;
								int j = 0;
								
								while (!this.pathFindQueue.isPathEmpty())
								{
									PathPoint pathpoint2 = this.pathFindQueue.dequeue();
									
									if (pathpoint2.equals(pathpoint5))
									{
										if (andThen != null)
										{
											andThen.previous = pathpoint5;
											pathpoint5 = andThen;
										}

										return this.makePath(pathpoint4, pathpoint5);
									}

									if (pathpoint2.distanceTo(pathpoint5) < pathpoint1.distanceTo(pathpoint5))
									{
										pathpoint1 = pathpoint2;
									}

									pathpoint2.visited = true;
									int k = 0;
									
									for (int l = 0; l < 24; ++l)
									{
										if (this.pathPoints[l] == pathpoint2)
										{
											k = l;
											break;
										}
									}

									for (int i1 = j; i1 < 24; ++i1)
									{
										int ran = rand.nextInt(23);
										if ((this.neighbors[k] & 1 << ran) > 0)
										{
											PathPoint pathpoint3 = this.pathPoints[ran];
											
											if (!pathpoint3.visited)
											{
												float f = pathpoint2.totalPathDistance + pathpoint2.distanceTo(pathpoint3);
												
												if (!pathpoint3.isAssigned() || f < pathpoint3.totalPathDistance)
												{
													pathpoint3.previous = pathpoint2;
													pathpoint3.totalPathDistance = f;
													pathpoint3.distanceToNext = pathpoint3.distanceTo(pathpoint5);
													
													if (pathpoint3.isAssigned())
													{
														this.pathFindQueue.changeDistance(pathpoint3, pathpoint3.totalPathDistance + pathpoint3.distanceToNext);
													}
													else
													{
														pathpoint3.distanceToTarget = pathpoint3.totalPathDistance + pathpoint3.distanceToNext;
														this.pathFindQueue.addPoint(pathpoint3);
													}
												}
											}
										}
									}
								}

								if (pathpoint1 == pathpoint4)
								{
									return null;
								}
								else
								{
									if (andThen != null)
									{
										andThen.previous = pathpoint1;
										pathpoint1 = andThen;
									}

									return this.makePath(pathpoint4, pathpoint1);
								}
							}
							/**
							* Create and return a new PathEntity defining a path from the start to the finish, using the connections already
							* made by the caller, findPath.
							*/
							private Path makePath(PathPoint start, PathPoint finish)
							{
								int i = 1;
								
								for (PathPoint pathpoint = finish; pathpoint.previous != null; pathpoint = pathpoint.previous)
								{
									++i;
								}

								PathPoint[] apathpoint = new PathPoint[i];
								PathPoint pathpoint1 = finish;
								--i;
								
								for (apathpoint[i] = finish; pathpoint1.previous != null; apathpoint[i] = pathpoint1)
								{
									pathpoint1 = pathpoint1.previous;
									--i;
								}

								return new Path(apathpoint);
							}
							public void writeEntityToNBT(NBTTagCompound tagCompound)
							{
								super.writeEntityToNBT(tagCompound);
								tagCompound.setInteger("DragonPhase", this.phaseManager.getCurrentPhase().getPhaseList().getId());
								tagCompound.setBoolean("CarryingCrystal", isCarryingCrystal());

								tagCompound.setInteger("ID", getID());
							}
							public void readEntityFromNBT(NBTTagCompound tagCompund)
							{
								super.readEntityFromNBT(tagCompund);
								setID(tagCompund.getInteger("ID"));
								if (tagCompund.hasKey("DragonPhase"))
								{
									this.phaseManager.setPhase(PhaseList.getById(tagCompund.getInteger("DragonPhase")));
								}
								setCarryingCrystal(tagCompund.getBoolean("CarryingCrystal"));
							}
							protected void despawnEntity() { }
							public Entity[] getParts()
							{
								return this.dragonPartArray;
							}
							public boolean canBeCollidedWith()
							{
								return this.world.getClosestPlayerToEntity(this, this.width) != null && this.isEntityAlive();
							}
							public int getID()
							{
								return ((Integer)this.dataManager.get(ID)).intValue();
							}
							
							public void setID(int id)
							{
								getDataManager().set(ID, Integer.valueOf(id));
							}
							
							public World getWorld()
							{
								return this.world;
							}
							protected void collideWithNearbyEntities() { }
							protected SoundEvent getAmbientSound()
							{
								return SoundEvents.ENTITY_ENDERDRAGON_AMBIENT;
							}
							protected SoundEvent getHurtSound(DamageSource source)
							{
								return SoundEvents.ENTITY_ENDERDRAGON_HURT;
							}
							protected SoundEvent getDeathSound()
							{
								return SoundEvents.ENTITY_ENDERMEN_STARE;
							}
							protected float getSoundVolume()
							{
								return isSneaking() ? 0.1F : 10.0F;
							}
							@SideOnly(Side.CLIENT)
							public float getHeadPartYOffset(int p_184667_1_, double[] p_184667_2_, double[] p_184667_3_)
							{
								BlockPos blockpos = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
								if (getOwner() != null)
								{
									blockpos = new BlockPos(getOwner());
								}
								float f = Math.max(MathHelper.sqrt(getDistanceSqToCenter(blockpos)) / 4.0F, 1.0F);
								return (float)p_184667_1_ / f;
							}
							public Vec3d getHeadLookVec(float p_184665_1_)
							{
								IPhase iphase = this.phaseManager.getCurrentPhase();
								PhaseList<? extends IPhase> phaselist = iphase.getPhaseList();
								Vec3d vec3d;
								if ((phaselist != PhaseList.LANDING) && (phaselist != PhaseList.TAKEOFF))
								{
									if (iphase.getIsStationary())
									{
										float f4 = this.rotationPitch;
										float f5 = 1.5F;
										this.rotationPitch = (-6.0F * f5 * 5.0F);
										vec3d = getLook(p_184665_1_);
										this.rotationPitch = f4;
									}
									else
									{
										vec3d = getLook(p_184665_1_);
									}
								}
								else
								{
									BlockPos blockpos = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
									if (getOwner() != null)
									{
										blockpos = new BlockPos(getOwner());
									}
									float f = Math.max(MathHelper.sqrt(getDistanceSqToCenter(blockpos)) / 4.0F, 1.0F);
									float f1 = 6.0F / f;
									float f2 = this.rotationPitch;
									float f3 = 1.5F;
									this.rotationPitch = (-f1 * f3 * 5.0F);
									vec3d = getLook(p_184665_1_);
									this.rotationPitch = f2;
								}
								return vec3d;
							}
							public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc)
							{
								EntityPlayer entityplayer;
								if ((dmgSrc.getTrueSource() instanceof EntityPlayer))
								{
									entityplayer = (EntityPlayer)dmgSrc.getTrueSource();
								}
								else
								{
									entityplayer = this.world.getNearestAttackablePlayer(pos, 64.0D, 64.0D);
								}
								this.phaseManager.getCurrentPhase().onCrystalDestroyed(crystal, pos, dmgSrc, entityplayer);
							}
							public void notifyDataManagerChange(DataParameter<?> key)
							{
								if ((PHASE.equals(key)) && (this.world.isRemote))
								{
									this.phaseManager.setPhase(PhaseList.getById(((Integer)getDataManager().get(PHASE)).intValue()));
								}
								super.notifyDataManagerChange(key);
							}
							public PhaseManager getPhaseManager()
							{
								return this.phaseManager;
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

							protected SoundEvent getCrushHurtSound()
							{
								return ESound.fleshHitCrushHeavy;
							}
							public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) { }
						}