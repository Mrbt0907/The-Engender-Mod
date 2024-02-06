package net.minecraft.AgeOfMinecraft.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.AgeOfMinecraft.util.DialogColors;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.endermanofdoom.mac.dialogue.DialogueManager;
import net.endermanofdoom.mac.music.IMusicInteractable;
import net.endermanofdoom.mac.util.ReflectionUtil;
import net.endermanofdoom.mac.util.TranslateUtil;
import net.endermanofdoom.mac.util.math.Maths;
import net.minecraft.AgeOfMinecraft.EngenderCompat;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.blocks.BlockGuardBlock;
import net.minecraft.AgeOfMinecraft.effects.EngenderExplosion;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAvoidEntitySPC;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFindNearestUnalliedTarget;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowWildAdult;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyHurtByTarget;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAILeaderHurtByTarget;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAILeaderHurtTarget;
import net.minecraft.AgeOfMinecraft.entity.other.AttributeModifierEX;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityChicken;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityCow;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySquid;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySlime;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPigZombie;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityVindicator;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEvoker;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStorm;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormHead;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormTentacle;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormTentacleDevourer;
import net.minecraft.AgeOfMinecraft.items.ItemEngenderStatChecker;
import net.minecraft.AgeOfMinecraft.items.ItemLearningBook;
import net.minecraft.AgeOfMinecraft.particles.ParticleCustom;
import net.minecraft.AgeOfMinecraft.registry.EItem;
import net.minecraft.AgeOfMinecraft.registry.EParticle;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEndGateway;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.INpc;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseDying;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.pathfinding.FlyingNodeProcessor;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@Interface(iface = "com.github.alexthe666.iceandfire.entity.IBlacklistedFromStatues", modid = "iceandfire")
public abstract class EntityFriendlyCreature extends EntityCreature implements IEntityOwnable, ITeamedMobs
{
	//TODO: Make sure to revisit the reflection helper lines to see if they error
	public static float EXP_FACTOR = 1.0F / (float)EngenderConfig.mobs.levelFactor;
	private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"), "Baby speed boost", 0.5D, 1);
	private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> HERO = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> REBIRTH = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> ANTIMOB = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> SITRESTING = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D838");
	private static final AttributeModifier attackingSpeedBoostModifier = new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.5D, 1).setSaved(false);
	private static final UUID attackingBoostModifierUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D839");
	private static final AttributeModifier attackingBoostModifier = new AttributeModifier(attackingBoostModifierUUID, "Attacking boost", 0.5D, 1).setSaved(false);
	private static final DataParameter<Integer> HEROSPECIALATTACKTIMER = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.VARINT);
	public static final IAttribute VIGOR = new RangedAttribute((IAttribute)null, "engender.vigor", 9.0D, 1.0D, 100.0D).setDescription("Mob Vigor").setShouldWatch(true);
	public static final IAttribute STRENGTH = new RangedAttribute((IAttribute)null, "engender.strength", 9.0D, 1.0D, 100.0D).setDescription("Mob Strength").setShouldWatch(true);
	public static final IAttribute STAMINA = new RangedAttribute((IAttribute)null, "engender.stamina", 9.0D, 1.0D, 100.0D).setDescription("Mob Stamina").setShouldWatch(true);
	public static final IAttribute INTELLIGENCE = new RangedAttribute((IAttribute)null, "engender.intelligence", 9.0D, 1.0D, 100.0D).setDescription("Mob Intelligence").setShouldWatch(true);
	public static final IAttribute DEXTERITY = new RangedAttribute((IAttribute)null, "engender.dexterity", 9.0D, 1.0D, 100.0D).setDescription("Mob Dexterity").setShouldWatch(true);
	public static final IAttribute AGILITY = new RangedAttribute((IAttribute)null, "engender.agility", 9.0D, 1.0D, 100.0D).setDescription("Mob Agility").setShouldWatch(true);
	public static final IAttribute FITTNESS = new RangedAttribute((IAttribute)null, "engender.fittness", 1.0D, 0.01D, Double.MAX_VALUE).setDescription("Mob Fittness").setShouldWatch(true);
	private static final AttributeModifierEX VIGOR_MODIFIER = new AttributeModifierEX(UUID.fromString("b038ce84-bc47-49a4-8c9c-2d4ed6e03867"), "Mob Vigor");
	private static final AttributeModifierEX STRENGTH_MODIFIER = new AttributeModifierEX(UUID.fromString("8ce1d897-9f46-415d-9b17-2349c664308e"), "Mob Strength");
	private static final AttributeModifierEX STAMINA_MODIFIER = new AttributeModifierEX(UUID.fromString("05b559fc-409b-4379-a87e-8de2ba4c9811"), "Mob Stamina");
	private static final AttributeModifierEX INTELLIGENCE_MODIFIER = new AttributeModifierEX(UUID.fromString("643d2a29-0f58-4f6b-94b9-c63254015afe"), "Mob Intelligence");
	private static final AttributeModifierEX DEXTERITY_MODIFIER = new AttributeModifierEX(UUID.fromString("71f1bde9-6962-4c30-bf20-48de06cb25db"), "Mob Dexterity");
	private static final AttributeModifierEX AGILITY_MODIFIER = new AttributeModifierEX(UUID.fromString("e3be3d18-3162-415a-b611-c83fadf96360"), "Mob Agility");
	private static final AttributeModifierEX FITTNESS_MODIFIER = new AttributeModifierEX(UUID.fromString("6c8b07dd-6160-4a2b-a55c-7e80dc9a6ee3"), "Mob Fitness");
	private static final DataParameter<Integer> AGE = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.VARINT);
	private static final DataParameter<Float> TOTALEXP = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ENERGY = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.FLOAT);
	private static final DataParameter<Optional<BlockPos>> GUARD_BLOCK_POS = EntityDataManager.<Optional<BlockPos>>createKey(EntityFriendlyCreature.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final DataParameter<Integer> BOOK_ID = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> BOOK_DURABILITY = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.VARINT);
	private static final DataParameter<Float> FAKE_HEALTH = EntityDataManager.<Float>createKey(EntityFriendlyCreature.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> GHOST_TIME = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> ILLUSION_FORM_TIME = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> POLYMORPH_TIME = EntityDataManager.createKey(EntityFriendlyCreature.class, DataSerializers.VARINT);
	private int level;
	private float currentEXP, neededEXP, totalEXPClient;
	public boolean isOffensive;
	public double randPosX;
	public double randPosY;
	public double randPosZ;
	public double prevChasingPosX;
	public double prevChasingPosY;
	public double prevChasingPosZ;
	public double chasingPosX;
	public double chasingPosY;
	public double chasingPosZ;
	public BlockPos jukeBoxToDanceTo;
	public int mutationTimer;
	public int moralRaisedTimer;
	public int convertionInt;
	public int convertionDelay;
	protected Block spawnableBlock = Blocks.GRASS;

	public float reachWidth;
	public int attackTimer;
	public int holdRoseTick;
	public int lastChanceInvul;
	public float pageFlip;
	public float pageFlipPrev;
	public float bookSpread;
	public float bookSpreadPrev;
	public float flipT;
	public float flipA;
	public int deathTicks;
	protected int blockTimer;
	public float rotationPitchFalling;
	public float prevRotationPitchFalling;
	protected BossInfoServer bossInfo = new BossInfoServer(new TextComponentTranslation(getName(), new Object[0]), BossInfo.Color.WHITE, BossInfo.Overlay.PROGRESS);
	private EnumStudy currentStudy = EnumStudy.Physical;
	private ItemStack currentReadingBook = ItemStack.EMPTY;
	public NBTTagCompound polymorpherData;
	
	EntityAINearestAttackableTarget<EntityFriendlyCreature> trainingAI = new EntityAINearestAttackableTarget<EntityFriendlyCreature>(this, EntityFriendlyCreature.class, 0, false, false, new Predicate<Entity>() {public boolean apply(@Nullable Entity p_apply_1_) {return p_apply_1_ != null && p_apply_1_ instanceof EntityFriendlyCreature && EntityFriendlyCreature.this.isOnSameTeam((EntityFriendlyCreature)p_apply_1_) && (EntityFriendlyCreature)p_apply_1_ != EntityFriendlyCreature.this && ((EntityFriendlyCreature)p_apply_1_).getFakeHealth() > 0F && EntityFriendlyCreature.this.getFakeHealth() > 0F;}});
	
	public InventoryBasic basicInventory;
	public final Vec3d[][] renderLocations;
	private int rideCooldownCounter;
	private boolean limitedLifespan;
	private int limitedLifeTicks;
	
	public EntityFriendlyCreature(World worldIn)
	{
		super(worldIn);
		if (worldIn != null && worldIn.isRemote && this instanceof IMusicInteractable)
			net.endermanofdoom.mac.internal.music.MusicManager.addMusicInteractable((IMusicInteractable) this);
		
		timeUntilPortal = 100;
		basicInventory = new InventoryBasic("Basic inventory", false, 8);
		lastChanceInvul = getSpawnTimer();
		updateBossBar();
		chasingPosX = posX;
		chasingPosY = posY + getEyeHeight();
		chasingPosZ = posZ;
		setDoorAItask(getIntelligence() < 12F);
		renderYawOffset = rotationYaw = rotationYawHead = rand.nextFloat() * 360F;
		onGround = true;
		for (int i = 0; i < inventoryArmorDropChances.length; i++)
			inventoryArmorDropChances[i] = 0F;
		for (int j = 0; j < inventoryHandsDropChances.length; j++)
			inventoryHandsDropChances[j] = 0F;
		
		experienceValue = 10;
		tasks.addTask(1, new EntityAIAvoidEntitySPC<EntityLivingBase>(this, EntityLivingBase.class, new Predicate<EntityLivingBase>()
		{
			public boolean apply(EntityLivingBase p_apply_1_)
			{
				return p_apply_1_ != null && ((EntityFriendlyCreature.this.shouldFleeAtLowHealth() && !EntityFriendlyCreature.this.isOnSameTeam(p_apply_1_) && p_apply_1_.isEntityAlive() && EntityFriendlyCreature.this.getIntelligence() > 4F) || (p_apply_1_ instanceof EntityLiving && p_apply_1_.getHealth() <= 0.0F && p_apply_1_.deathTime <= 0 && !(p_apply_1_ instanceof EntityFriendlyCreature)) || (p_apply_1_ instanceof net.minecraft.entity.boss.EntityWither && ((net.minecraft.entity.boss.EntityWither)p_apply_1_).getInvulTime() > 0));
			}
		}, 16F, 1.25D, 1.25D));
		tasks.addTask(0, new EntityAIOpenDoor(this, true));
		tasks.addTask(0, new EntityAIFollowWildAdult(this, 1.25D));
		
		tasks.addTask(13, new EntityAIWatchClosest(this, EntityLivingBase.class, 8.0F)
		{
			public boolean shouldExecute()
			{
				if (!EntityFriendlyCreature.this.getCurrentBook().isEmpty() || EntityFriendlyCreature.this.getAttackTarget() != null || EntityFriendlyCreature.this.getCreatureAttribute() != ESetup.WITHER_STORM)
					return false;
				else
					return super.shouldExecute();
			}
		});
		targetTasks.addTask(0, new EntityAIFriendlyHurtByTarget(this, true, new Class[0]));
		targetTasks.addTask(0, new EntityAILeaderHurtByTarget(this));
		targetTasks.addTask(0, new EntityAILeaderHurtTarget(this));
		targetTasks.addTask(2, new EntityAIFindNearestUnalliedTarget(this, EntityLivingBase.class));
		
		if (getBookID() != 0)
			setCurrentBook(new ItemStack(EItem.SKILL_BOOKS.get(getBookID()), 1, getBookDurability()));

		renderLocations = new Vec3d[2][4];
		
		for (int i = 0; i < 4; ++i)
		{
			renderLocations[0][i] = new Vec3d(0.0D, 0.0D, 0.0D);
			renderLocations[1][i] = new Vec3d(0.0D, 0.0D, 0.0D);
		}
		for (Entity entity4 : world.playerEntities)
			if (!isWild() && isOnSameTeam(entity4))
				setOwnerId(entity4.getPersistentID());
	}
	
	protected void entityInit()
	{
		super.entityInit();
		getDataManager().register(ARMS_RAISED, Boolean.valueOf(false));
		getDataManager().register(IS_CHILD, Boolean.valueOf(false));
		getDataManager().register(OWNER_UNIQUE_ID, Optional.<UUID>absent());
		getDataManager().register(HERO, Boolean.valueOf(false));
		getDataManager().register(REBIRTH, Boolean.valueOf(false));
		getDataManager().register(ANTIMOB, Boolean.valueOf(false));
		getDataManager().register(SITRESTING, Boolean.valueOf(false));
		getDataManager().register(HEROSPECIALATTACKTIMER, Integer.valueOf(0));
		getDataManager().register(AGE, Integer.valueOf(0));
		getDataManager().register(TOTALEXP, Float.valueOf(0));
		getDataManager().register(ENERGY, Float.valueOf(100F));
		getDataManager().register(FAKE_HEALTH, Float.valueOf(0F));
		getDataManager().register(GUARD_BLOCK_POS, Optional.<BlockPos>absent());
		getDataManager().register(BOOK_ID, Integer.valueOf(0));
		getDataManager().register(BOOK_DURABILITY, Integer.valueOf(0));
		getDataManager().register(GHOST_TIME, Integer.valueOf(0));
		getDataManager().register(ILLUSION_FORM_TIME, Integer.valueOf(0));
		getDataManager().register(POLYMORPH_TIME, Integer.valueOf(0));
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
		getAttributeMap().registerAttribute(VIGOR);
		getAttributeMap().registerAttribute(STRENGTH);
		getAttributeMap().registerAttribute(STAMINA);
		getAttributeMap().registerAttribute(INTELLIGENCE);
		getAttributeMap().registerAttribute(DEXTERITY);
		getAttributeMap().registerAttribute(AGILITY);
		getAttributeMap().registerAttribute(FITTNESS);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(getKnockbackResistance());
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		
		if (tagCompund.hasKey("PolymorpherData"))
			polymorpherData = tagCompund.getCompoundTag("PolymorpherData");
		if (tagCompund.hasKey("LifeTicks"))
			setLimitedLife(tagCompund.getInteger("LifeTicks"));
		if (tagCompund.hasKey("FakeHealth", 99))
			setFakeHealth(tagCompund.getFloat("FakeHealth"));
		if (tagCompund.hasKey("CurrentBook", 9))
		{
			NBTTagList nbttaglist1 = tagCompund.getTagList("CurrentBook", 10);
			setCurrentBook(new ItemStack(nbttaglist1.getCompoundTagAt(0)));
		}

		if (tagCompund.hasKey("Level"))
			setLevel(tagCompund.getInteger("Level"));
		else
			setTotalEXP(tagCompund.getFloat("TotalEXP"));
		
		if (tagCompund.hasKey("VGR", 99)) setVigor(tagCompund.getDouble("VGR"), false);
		if (tagCompund.hasKey("STR", 99)) setStrength(tagCompund.getDouble("STR"), false);
		if (tagCompund.hasKey("STA", 99)) setStamina(tagCompund.getDouble("STA"), false);
		if (tagCompund.hasKey("INT", 99)) setIntelligence(tagCompund.getDouble("INT"), false);
		if (tagCompund.hasKey("DEX", 99)) setDexterity(tagCompund.getDouble("DEX"), false);
		if (tagCompund.hasKey("AGI", 99)) setAgility(tagCompund.getDouble("AGI"), false);
		if (tagCompund.hasKey("FIT", 99)) setFittness(tagCompund.getDouble("FIT"), false);
		
		setChild(tagCompund.getBoolean("IsBaby"));
		String s;
		
		if (tagCompund.hasKey("OwnerUUID", 8))
		s = tagCompund.getString("OwnerUUID");
		else
		{
			String s1 = tagCompund.getString("Owner");
			s = PreYggdrasilConverter.convertMobOwnerIfNeeded(getServer(), s1);
		}
		
		if (!s.isEmpty())
			try
			{
				setOwnerId(UUID.fromString(s));
			}
			catch (Throwable var4) {}
	
	dataManager.set(REBIRTH, Boolean.valueOf(tagCompund.getBoolean("LastChance")));
	dataManager.set(HERO, Boolean.valueOf(tagCompund.getBoolean("Hero")));
	dataManager.set(ANTIMOB, Boolean.valueOf(tagCompund.getBoolean("Anti")));
	dataManager.set(SITRESTING, Boolean.valueOf(tagCompund.getBoolean("SitResting")));
	setSpecialAttackTimer(tagCompund.getInteger("SAT"));
	setGrowingAge(tagCompund.getInteger("Age"));
	setBookID(tagCompund.getInteger("BookID"));
	setBookDurability(tagCompund.getInteger("BookDurability"));
	if (tagCompund.hasKey("Energy", 99))
		setEnergy(tagCompund.getFloat("Energy"));
	
	setGhostTime(tagCompund.getInteger("MirrorTime"));
	setIllusionFormTime(tagCompund.getInteger("IllusionTime"));
	setPolymorphTime(tagCompund.getInteger("MorphTime"));
	setDoorAItask(getIntelligence() < 12F);
	
	if (tagCompund.hasKey("GPX"))
	{
	int i = tagCompund.getInteger("GPX");
	int j = tagCompund.getInteger("GPY");
	int k = tagCompund.getInteger("GPZ");
	dataManager.set(GUARD_BLOCK_POS, Optional.of(new BlockPos(i, j, k)));
	randPosX = i;
	randPosY = j;
	randPosZ = k;
	}
	else
	{
	dataManager.set(GUARD_BLOCK_POS, Optional.<BlockPos>absent());
	}
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		if (polymorpherData != null)
			tagCompound.setTag("PolymorpherData", polymorpherData);
		NBTTagList nbttaglist = new NBTTagList();
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		
		if (hasLimitedLife())
			tagCompound.setInteger("LifeTicks", getLimitedLife());
		
		if (!getCurrentBook().isEmpty())
		getCurrentBook().writeToNBT(nbttagcompound);
		
		nbttaglist.appendTag(nbttagcompound);
		
		tagCompound.setTag("CurrentBook", nbttaglist);
		tagCompound.setFloat("FakeHealth", getFakeHealth());
		tagCompound.setDouble("VGR", VIGOR_MODIFIER.getValue());
		tagCompound.setDouble("STR", STRENGTH_MODIFIER.getValue());
		tagCompound.setDouble("STA", STAMINA_MODIFIER.getValue());
		tagCompound.setDouble("INT", INTELLIGENCE_MODIFIER.getValue());
		tagCompound.setDouble("DEX", DEXTERITY_MODIFIER.getValue());
		tagCompound.setDouble("AGI", AGILITY_MODIFIER.getValue());
		tagCompound.setDouble("FIT", FITTNESS_MODIFIER.getValue());
		
		if (getOwnerId() == null)
			tagCompound.setString("OwnerUUID", "");
		else
			tagCompound.setString("OwnerUUID", getOwnerId().toString());
		
		tagCompound.setBoolean("IsBaby", isChild());
		tagCompound.setBoolean("Hero", isHero());
		tagCompound.setBoolean("LastChance", ((Boolean)dataManager.get(REBIRTH)).booleanValue());
		tagCompound.setBoolean("Anti", ((Boolean)dataManager.get(ANTIMOB)).booleanValue());
		tagCompound.setBoolean("SitResting", ((Boolean)dataManager.get(SITRESTING)).booleanValue());
		tagCompound.setInteger("SAT", getSpecialAttackTimer());
		tagCompound.setFloat("TotalEXP", getTotalEXP());
		tagCompound.setInteger("Age", getGrowingAge());
		tagCompound.setInteger("BookID", getBookID());
		tagCompound.setInteger("BookDurability", getBookDurability());
		tagCompound.setFloat("Energy", getEnergy());
		tagCompound.setInteger("MirrorTime", getGhostTime());
		tagCompound.setInteger("IllusionTime", getIllusionFormTime());
		tagCompound.setInteger("MorphTime", getPolymorphTime());
		
		if (getGuardBlock() != null)
		{
			tagCompound.setInteger("GPX", getGuardBlock().getX());
			tagCompound.setInteger("GPY", getGuardBlock().getY());
			tagCompound.setInteger("GPZ", getGuardBlock().getZ());
		}
	}
	
	public void onUpdateClient()
	{
		EnumTier tier = getTier();
		boolean isBoss = isBoss(), isAlive = isEntityAlive(), isRiding = isRiding();
		
		//----------- General Operations -----------\\
		if (getEnergy() <= 5F) spawnStressParticle();
		if (attackTimer > 0) --attackTimer;
		
		//TODO: Figure out how to get exp to detect on client
		if (ticksExisted % 10 == 0 && (totalEXPClient != getTotalEXP() || neededEXP <= 0.0F))
			setLevel();
			
		if (getLevel() >= 299 && !tier.equals(EnumTier.TIER6) && rand.nextInt(5) == 0)
			world.spawnParticle(EnumParticleTypes.END_ROD, posX + ((double)((rand.nextFloat() * width * 2.0F) - width) * 0.6D), posY + (rand.nextDouble() * height), posZ + ((double)((rand.nextFloat() * width * 2.0F) - width) * 0.6D), 0D, 0.01D, 0D, new int[0]);
		
		//----------- Falling Animation -----------\\
		if (!isBoss && (!isRiding || isRiding && getRidingEntity() instanceof EntityThrowable) && (!(this instanceof Flying) || !isAlive) && !onGround && (hurtTime > 0 || isRiding || !isAlive))
		{
			renderYawOffset = rotationYaw = rotationYawHead;
			
			if (isRiding)
				for (rotationPitchFalling = (float)(-getRidingEntity().motionY * (360D / Math.PI)); rotationPitchFalling - prevRotationPitchFalling < -180.0F; prevRotationPitchFalling -= 360.0F);
			else
				for (rotationPitchFalling = (float)(-motionY * (360D / Math.PI)); rotationPitchFalling - prevRotationPitchFalling < -180.0F; prevRotationPitchFalling -= 360.0F);
			
			while (rotationPitchFalling - prevRotationPitchFalling >= 180.0F)
				prevRotationPitchFalling += 360.0F;
			
			rotationPitchFalling = prevRotationPitchFalling + (rotationPitchFalling - prevRotationPitchFalling) * (float)(motionY == 0D ? 0.01D : (motionY < 0D ? -motionY / 5 : motionY / 5));
			if (rotationPitchFalling >= 90F)
				rotationPitchFalling = 90F;
			if (rotationPitchFalling <= -90F)
				rotationPitchFalling = -90F;
			if (rotationPitchFalling < 90F && rotationPitchFalling > -90F && !onGround && rotationYawHead != (float)MathHelper.atan2(motionZ, motionX) * (180F / (float)Math.PI) - 90.0F)
			{
				prevRenderYawOffset = prevRotationYaw = prevRotationYawHead = renderYawOffset = rotationYaw = rotationYawHead = (float)MathHelper.atan2(motionZ, motionX) * (180F / (float)Math.PI) - 90.0F;
				float xRatio = MathHelper.sin(rotationYawHead * 0.017453292F);
				float zRatio = -MathHelper.cos(rotationYawHead * 0.017453292F);
				MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
			}
		}
		else
			prevRotationPitchFalling = rotationPitchFalling = 0F;
		
		prevRotationPitchFalling = rotationPitchFalling;
	}
	
	//TODO: Set last attacked entity to null on owner when they damage this entity
	public void onUpdateServer()
	{
		//If owner is null, then this entity is a wild one
		EntityLivingBase owner = getOwner(), attacker = getAttackTarget();
		EnumDifficulty difficulty = world.getDifficulty();
		EnumTier tier = getTier();
		ItemStack mainStack = getHeldItemMainhand(), offStack = getHeldItemOffhand();
		Item mainItem = mainStack.isEmpty() ? null : mainStack.getItem(),
			offItem = offStack.isEmpty() ? null : offStack.getItem();
		boolean isBoss = isBoss(), isHero = isHero(), lastChance = hasLastChance(), isAlive = isEntityAlive(), isRiding = isRiding();
		int ghostTime = getGhostTime(), illusionTime = getIllusionFormTime(), polymorphTime = getPolymorphTime(), limitedLifeTime = getLimitedLife();
		
		//----------- Basic Operations -----------\\
		++rideCooldownCounter;
		setAlwaysRenderNameTag(false);
		
		if (!isEntityAlive() || isPotionActive(MobEffects.BLINDNESS) || attacker != null && (isOnSameTeam(getAttackTarget()) || isPotionActive(MobEffects.NAUSEA) && getDistance(attacker) > reachWidth || !attacker.isEntityAlive() || attacker instanceof EntityPlayer && ((EntityPlayer)attacker).capabilities.disableDamage))
			setAttackTarget(null);
		
		if (ticksExisted % 10 == 0 && isPotionActive(MobEffects.NAUSEA))
			getNavigator().clearPath();
		
		if (ghostTime > 0) setGhostTime(ghostTime -= 1);
		if (illusionTime > 0) setIllusionFormTime(illusionTime -= 1);
		if (polymorphTime > 0) setPolymorphTime(polymorphTime -= 1);
		if (blockTimer > 0) --blockTimer;
		if (attackTimer > 0) --attackTimer;
		if (holdRoseTick > 0) holdRoseTick -= 1;
		if (moralRaisedTimer > 0) moralRaisedTimer -= 1;
		if (isAIDisabled()) limbSwing = 0F;
		if (lastChanceInvul > 0)
		{
			hurtResistantTime = lastChanceInvul;
			--lastChanceInvul;
		}
		
		isAirBorne = !onGround;
		
		//TODO: Optimize inventory setting
		currentReadingBook = getBookID() != 0 ? new ItemStack(EItem.SKILL_BOOKS.get(getBookID()), 1, getBookDurability()) : ItemStack.EMPTY;
		basicInventory.setInventorySlotContents(0, getItemStackFromSlot(EntityEquipmentSlot.HEAD));
		basicInventory.setInventorySlotContents(1, getItemStackFromSlot(EntityEquipmentSlot.CHEST));
		basicInventory.setInventorySlotContents(2, getItemStackFromSlot(EntityEquipmentSlot.LEGS));
		basicInventory.setInventorySlotContents(3, getItemStackFromSlot(EntityEquipmentSlot.FEET));
		basicInventory.setInventorySlotContents(4, getItemStackFromSlot(EntityEquipmentSlot.MAINHAND));
		basicInventory.setInventorySlotContents(5, getItemStackFromSlot(EntityEquipmentSlot.OFFHAND));
		basicInventory.setInventorySlotContents(6, getCurrentBook());
		
		prevChasingPosX = chasingPosX;
		prevChasingPosY = chasingPosY;
		prevChasingPosZ = chasingPosZ;
		
		if (posY > 200.0D)
			posY = 200.0D;
		
		
		double d0 = posX - chasingPosX;
		double d1 = posY - chasingPosY;
		double d2 = posZ - chasingPosZ;
		double d3 = 10.0D;
		
		if (d0 > d3)
		{
			chasingPosX = posX;
			prevChasingPosX = chasingPosX;
		}
		
		if (d2 > d3)
		{
			chasingPosZ = posZ;
			prevChasingPosZ = chasingPosZ;
		}
		
		if (d1 > d3)
		{
			chasingPosY = posY;
			prevChasingPosY = chasingPosY;
		}
		
		if (d0 < -d3)
		{
			chasingPosX = posX;
			prevChasingPosX = chasingPosX;
		}
		
		if (d2 < -d3)
		{
			chasingPosZ = posZ;
			prevChasingPosZ = chasingPosZ;
		}
		
		if (d1 < -d3)
		{
			chasingPosY = posY;
			prevChasingPosY = chasingPosY;
		}
		
		chasingPosX += d0 * 0.25D;
		chasingPosZ += d2 * 0.25D;
		chasingPosY += d1 * 0.25D;
		
		//----------- General Operations -----------\\
		updateBossBar();
		
		if (isAlive && posY <= -200D)
		{
			setDead();
			return;
		}
			
		if (tier.equals(EnumTier.TIER6))
			setEnergy(100F);
		
		if (isBeingRidden())
		{
			if (isAlive)
				for (Entity entity : getPassengers())
					entity.motionY = motionY;
			else
			{
				for (Entity entity : getPassengers())
					if (entity.isRiding())
						entity.dismountRidingEntity();
				
				dismountRidingEntity();
			}
		}
		
		if (ticksExisted % 300 == 0 && getGuardBlock() == null && !isRiding() && !isBeingRidden() && attacker == null && !getNavigator().noPath() && onGround && motionX == 0D && motionZ == 0D)
			setSitResting(true);
		
		if (isSitResting())
			getNavigator().clearPath();
		
		/*if (fallDistance >= getHealth() * 2F && isEntityAlive() && takesFallDamage() && posY <= -45D)
			attackEntityFrom(DamageSource.FALL, Float.MAX_VALUE);*/
		
		if (isBoss)
		{
			List<PotionEffect> potions = new ArrayList<PotionEffect>(getActivePotionEffects());
			PotionEffect potion;
			for (int i = 0; i < potions.size(); i++)
			{
				potion = potions.get(i);
				
				if (potion.getPotion().isBadEffect())
					removeActivePotionEffect(potion.getPotion());
			}
		}
		if (owner == null)
		{
			if (!isBoss && !isHero && difficulty.equals(EnumDifficulty.PEACEFUL))
			{
				setDead();
				return;
			}
		}
		
		//----------- Falling Animation -----------\\
		if (!isBoss && (!isRiding || isRiding && getRidingEntity() instanceof EntityThrowable) && (!(this instanceof Flying) || !isAlive) && !onGround && (hurtTime > 0 || isRiding || !isAlive))
		{
			if (isInWater() || isInLava())
				motionY -= 0.1F;
			else
				hurtTime = 10;
					
			isInWeb = false;
			getNavigator().clearPath();
		}
		
		//----------- Health Handling -----------\\
		if (getFakeHealth() <= 0F) setFakeHealth(0F);
		
		if (getHealth() > 0.0F)
		{
			if (deathTime > 0)
				--deathTime;
			dead = false;
		}
		
		//----------- Energy Handling -----------\\
		float energy = getEnergy(), lastEnergy = energy;
		
		if (energy > 100F)
		{
			heal(energy - 100F);
			energy = 100F;
		}
		else if (energy <= 0F)
		{
			energy = 0F;
			if (isBeingRidden())
				dismountRidingEntity();
		}
		
		if (isUndead() || isBoss || tier.equals(EnumTier.TIER6))
			energy = 100F;
		else if (ticksExisted % 20 == 0)
		{
			if (isPotionActive(MobEffects.HUNGER))
				energy -= 2.0F * getActivePotionEffect(MobEffects.HUNGER).getAmplifier();
			if (isSprinting() && ticksExisted % 20 == 0)
				energy--;
			if (isInWater() && !canBreatheUnderwater())
				energy -= 1.01F - (float) (getEntityAttribute(STAMINA).getBaseValue() / 100D);
			if (Maths.speed(motionX, motionY, motionZ) == 0.0D && energy < 100F)
				energy++;
			if (isPotionActive(MobEffects.REGENERATION) && energy < 100F)
				energy += 2.0F * getActivePotionEffect(MobEffects.REGENERATION).getAmplifier();
		}
		
		if (isAlive)
		{
			if (energy <= 0.0F && ticksExisted % 100 == 0)
				attackEntityFrom(DamageSource.STARVE, 2F);
			
			if (getHealth() < getMaxHealth())
			{
				if (ticksExisted % 60 == 0 && energy == 100F)
					heal(1F);
				if (ticksExisted % 40 == 0 && energy <= 90F && energy > 80F && hurtResistantTime <= 10)
				{
					heal(1F);
					setEnergy(getEnergy() - (float)(0.501F - (getEntityAttribute(STAMINA).getBaseValue() / 50F)));
				}
				if (ticksExisted % 10 == 0 && !isBoss && !isUndead() && energy < 100F && energy > 90F && hurtResistantTime <= 10)
				{
					heal(2F);
					setEnergy(getEnergy() - (float)(5.05F - (getEntityAttribute(STAMINA).getBaseValue() / 20F)));
				}
			}
		}
		
		setSprinting(owner != null && isAlive && owner.isSprinting() && energy > 20F);
		setSneaking(owner != null && isAlive && owner.isSneaking());
		
		if (energy != lastEnergy)
			setEnergy(energy);
		
		
		
		//----------- Experience Handling -----------\\
		
		
		//----------- Item Handling -----------\\
		if (mainItem != null)
		{
			if (mainItem instanceof ItemLearningBook)
			{
				setBookDurability(getCurrentBook().getItemDamage());
				
				if (getBookDurability() >= mainStack.getMaxDamage())
				{
					setBookID(0);
					setBookDurability(0);
				}
				else if (attacker == null)
				{
					setSitResting(true);
					rotationPitch = this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker || this instanceof EntityGuardian ? 0F : 30F;
					rotationYawHead = rotationYaw = renderYawOffset;
					
					if (ticksExisted % (80 - (int)(getEntityAttribute(INTELLIGENCE).getBaseValue() * 0.2D)) == 0)
					{
						++flipT;
						int randomRead = rand.nextInt(80);
						if (getCurrentBook().getItem() instanceof ItemLearningBook && randomRead == 1)
							learnLevelUp((ItemLearningBook)getCurrentBook().getItem());
						if (!getCurrentBook().isEmpty() && !getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty() && getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() instanceof ItemLearningBook)
							setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
					}
				}
				else
				{
					entityDropItem(mainStack, getEyeHeight());
					setCurrentBook(ItemStack.EMPTY);
					setBookID(0);
					setBookDurability(0);
					playSound(SoundEvents.ENTITY_WITCH_THROW, 1.0F, 0.8F + rand.nextFloat() * 0.4F);	
				}
				
				pageFlipPrev = pageFlip;
				bookSpreadPrev = bookSpread;
				
				if (isAlive)
				{
					bookSpread += 0.05F;
					if (bookSpread == 0.1F)
						playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.3F + rand.nextFloat() * 0.4F);
				}
				else
					bookSpread -= 0.1F;
				
				bookSpread = MathHelper.clamp(bookSpread, 0.0F, 1.0F);
				pageFlipPrev = pageFlip;
				float f = (flipT - pageFlip) * 0.4F;
				f = MathHelper.clamp(f, -0.2F, 0.2F);
				flipA += (f - flipA) * 0.9F;
				pageFlip += flipA;
			}
			else if (mainItem.equals(Items.BOWL))
			{
				entityDropItem(new ItemStack(Items.BOWL), getEyeHeight());
				setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
				playSound(SoundEvents.ENTITY_WITCH_THROW, 1.0F, 0.8F + rand.nextFloat() * 0.4F);
			}
			else if (mainItem.equals(EItem.carrier) || offItem != null && offItem.equals(EItem.carrier))
				dropEquipmentUndamaged();
		}
		//----------- Polymorphing -----------\\
		if (polymorpherData != null && (polymorphTime <= 0 || getHealth() <= 0) && !(this instanceof EntityEvoker))
		{
			EntityEvoker entity = new EntityEvoker(world);
			entity.copyLocationAndAnglesFrom(this);
			entity.playSound(ESound.bugSpecial, 10F, 0.5F);
			entity.playSound(ESound.blast, 10F, 1F);
			entity.spawnExplosionParticle();
			entity.readEntityFromNBT(polymorpherData);
			entity.writeEntityToNBT(polymorpherData);
			entity.setOwnerId(getOwnerId());
			entity.setIsHero(isHero);
			entity.setLastChance(lastChance);
			entity.setPolymorphTime(getHealth() <= 0 ? 2000 : 600);
			world.spawnEntity(entity);
			world.removeEntity(this);
		}
		
		//----------- Vex Stuff -----------\\
		if (limitedLifeTime > 0)
		{
			limitedLifespan = true;
			for (int i = 0; i < width + height; ++i)
			if (world.isRemote)
			{
			d0 = rand.nextGaussian() * 0.02D;
			d1 = rand.nextGaussian() * 0.02D;
			d2 = rand.nextGaussian() * 0.02D;
			d3 = 10.0D;
			world.spawnParticle(EnumParticleTypes.CRIT, posX + rand.nextFloat() * width * 2.0F - width - d0 * d3, posY + rand.nextFloat() * height - d1 * d3, posZ + rand.nextFloat() * width * 2.0F - width - d2 * d3, 0, 0, 0, new int[0]);
			}
		}
		
		if (hasLimitedLife())
		{
			setLimitedLife(limitedLifeTime -= 1);
			if (getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() <= 4)
			{
				if (!tasks.taskEntries.isEmpty())
					tasks.taskEntries.clear();
				spawnExplosionParticle();
			}
			if (limitedLifeTime <= 0)
			{
				getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() - 1D);
				if (getHealth() > getMaxHealth())
					setHealth(getMaxHealth());
				if (getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() < 1)
					onKillCommand();
				setLimitedLife(5);
			}
		}
		
		//----------- Misc -----------\\
		if (ticksExisted % 20 == 0 && isElytraFlying()) {setCurrentStudy(EnumStudy.Physical, 3);}
		if (ticksExisted % 60 == 0 && isBeingRidden()) {setCurrentStudy(EnumStudy.Physical, 1);}
		
		if (!isAlive) clearActivePotions();
		if (isHero) extinguish();
		if (!isSitResting() || owner != null || isHero || !isBoss || tier.equals(EnumTier.TIER6) || this instanceof IEntityMultiPart)
			idleTime = 0;
		
		if (ticksExisted > 20 && getRidingEntity() != null && getRidingEntity() instanceof EntityPlayer && !(this instanceof EntitySlime) && !isChild())
			dismountRidingEntity();
		
		if (isEntityImmuneToDarkness())
			removeActivePotionEffect(MobEffects.BLINDNESS);
		
		if (isPotionActive(MobEffects.FIRE_RESISTANCE) || isImmuneToFire())
			extinguish();
		
		if (isPotionActive(MobEffects.FIRE_RESISTANCE) || isImmuneToFire() || getIntelligence() <= 6F)
		{
			setPathPriority(PathNodeType.LAVA, 0.0F);
			setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
			setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
		}
		else
		{
			setPathPriority(PathNodeType.LAVA, -1.0F);
			setPathPriority(PathNodeType.DANGER_FIRE, 8.0F);
			setPathPriority(PathNodeType.DAMAGE_FIRE, 16.0F);
		}
		
		if (isAIDisabled())
		{
			hurtResistantTime = maxHurtResistantTime;
			if (ticksExisted > 21)
				--ticksExisted;
		}
		
		setSilent(isAIDisabled());
		setNoAI(deathTime > 40 || isBoss && !isAlive || isAlive && getOwnerId() != null && owner == null);
	}
	
	public void onUpdate()
	{
		super.onUpdate();

		if (world.isRemote)
			onUpdateClient();
		else
			onUpdateServer();
	}
	
	public void onLivingClient()
	{
		//If owner is null, then this entity is a wild one
		EntityLivingBase owner = getOwner();
		ItemStack mainStack = getHeldItemMainhand(), offStack = getHeldItemOffhand();
		Item mainItem = mainStack.isEmpty() ? null : mainStack.getItem(),
			offItem = offStack.isEmpty() ? null : offStack.getItem();
		boolean isBoss = isBoss(), isHero = isHero(), isAlive = isEntityAlive(), isRiding = isRiding();
		int ghostTime = getGhostTime();
		
		//----------- Conversion Staff Mechanics -----------\\
		if (convertionInt > 0)
			rotationYawHead = rotationYaw = renderYawOffset = ticksExisted * 5;
		
		//----------- Eat Handling -----------\\
		if ((mainItem != null && mainItem instanceof ItemFood || offItem != null && offItem instanceof ItemFood) && (getEnergy() < 90F || getHealth() <= getMaxHealth() * 0.5F))
		{
			if (ticksExisted > 53)
				ticksExisted = 20;
			
			if (ticksExisted % 2 == 0)
				rotationPitch = 40F;
			else
				rotationPitch = 0F;
			
			if (ticksExisted == 50)
			{
				for (int ai = 0; ai < ((ItemFood)mainItem).getHealAmount(mainStack); ++ai)
					spawnHeartParticle();
				playSound(SoundEvents.ENTITY_PLAYER_BURP, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			}
		}
				
		//----------- Moral System -----------\\
		if (moralRaisedTimer >= 600)
		{
			playSound(ESound.battlecry, 10.0F, 1.0F);
			playSound(getAmbientSound(), getSoundVolume(), getSoundPitch() + (rand.nextFloat() * 0.15F));
			playSound(getHurtSound(null), getSoundVolume(), getSoundPitch() + (rand.nextFloat() * 0.35F));
		}
		
		//----------- Ghost Mechanics -----------\\
		if (ghostTime > 0 && !isBoss)
		{
			if (ghostTime == 1)
			{
				for (int k = 0; k < 4; ++k)
				{
					renderLocations[0][k] = renderLocations[1][k];
					renderLocations[1][k] = new Vec3d(0.0D, 0.0D, 0.0D);
				}
				
				spawnExplosionParticle();
				world.playSound(posX, posY, posZ, SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE, getSoundCategory(), 1.0F, 1.0F, false);
			}
			
			if (ghostTime <= 1 || hurtResistantTime == 10 || !isAlive)
				for (int k = 0; k < 4; ++k)
				{
					renderLocations[0][k] = renderLocations[1][k];
					renderLocations[1][k] = new Vec3d(0.0D, 0.0D, 0.0D);
				}
			else if (hurtResistantTime == 1 || moralRaisedTimer == 1)
			{
				float f1, f11;
				
				for (int j = 0; j < 4; ++j)
				{
					f1 = (1 + (width < 1 ? 1 : width) * 1F);
					f11 = (1 + (width < 1 ? 1 : width) * 2F);
					renderLocations[0][j] = renderLocations[1][j];
					renderLocations[1][j] = new Vec3d((double)(rand.nextFloat() * f11 - f1), (double)Math.max(0, rand.nextInt((int)(2 * (height < 1 ? 1 : height)))), (double)(rand.nextFloat() * f11 - f1));
				}
				
				spawnExplosionParticle();
				world.playSound(posX, posY, posZ, SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE, getSoundCategory(), 1.0F, 1.0F, false);
			}
		}
		
		//----------- Misc -----------\\
		if (isPotionActive(MobEffects.NAUSEA))
		{
			rotationYawHead += MathHelper.sin(ticksExisted * 0.2F) * 10F;
			rotationPitch += MathHelper.cos(ticksExisted * 0.1F) * 10F;
		}
		
		
		if (isChild() && isRiding && getRidingEntity() instanceof EntityPlayer)
		{
			if (((EntityPlayer)getRidingEntity()).getRidingEntity() != null && (((EntityPlayer)getRidingEntity()).getRidingEntity() instanceof EntityLivingBase))
			{
				renderYawOffset = rotationYaw = (((EntityLivingBase)(((EntityPlayer)getRidingEntity()).getRidingEntity()))).rotationYaw;
				rotationYawHead = (((EntityLivingBase)(((EntityPlayer)getRidingEntity()).getRidingEntity()))).rotationYawHead;
			}
			else
			{
				renderYawOffset = rotationYaw = ((EntityPlayer)getRidingEntity()).rotationYaw;
				rotationYawHead = ((EntityPlayer)getRidingEntity()).rotationYawHead;
			}
		}
		
		if (isAlive && isHero && rand.nextInt(2) == 0)
		{
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			double d3 = 10.0D;
				
			if (owner == null)
				world.spawnParticle(EnumParticleTypes.REDSTONE, posX + rand.nextFloat() * width * 2.0F - width - d0 * d3, posY + rand.nextFloat() * height - d1 * d3, posZ + rand.nextFloat() * width * 2.0F - width - d2 * d3, 0.5D, 0D, 0D, new int[0]);
			else
				world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, posX + rand.nextFloat() * width * 2.0F - width - d0 * d3, posY + rand.nextFloat() * height - d1 * d3, posZ + rand.nextFloat() * width * 2.0F - width - d2 * d3, d0, 0.10000000149011612D, d2, new int[0]);
		}
	}
	
	public void onLivingServer()
	{
		//If owner is null, then this entity is a wild one
		EntityLivingBase attacker = getAttackTarget();
		ItemStack mainStack = getHeldItemMainhand(), offStack = getHeldItemOffhand();
		Item mainItem = mainStack.isEmpty() ? null : mainStack.getItem(),
			offItem = offStack.isEmpty() ? null : offStack.getItem();
		boolean isBoss = isBoss(), isAlive = isEntityAlive();

		//----------- General Operations -----------\\
		updateArmSwingProgress();
		stepHeight = height >= 2F ? height / 2 : 1F;
		
		if (ticksExisted < 20) fallDistance *= 0F;
		if (getLeashed() && !onGround) fallDistance *= 0F;
		if (getSpecialAttackTimer() > 0) setSpecialAttackTimer(getSpecialAttackTimer() - 1);
		
		if (isBoss)
		{
			removeActivePotionEffect(MobEffects.POISON);
			removeActivePotionEffect(MobEffects.WITHER);
			removeActivePotionEffect(MobEffects.SLOWNESS);
			removeActivePotionEffect(MobEffects.WEAKNESS);
			removeActivePotionEffect(MobEffects.BLINDNESS);
			removeActivePotionEffect(MobEffects.NAUSEA);
			removeActivePotionEffect(MobEffects.LEVITATION);
			removeActivePotionEffect(MobEffects.HUNGER);
		}

		if (isAlive && onGround && attacker != null && attacker.posY >= posY + 2.0D && rand.nextInt(100) == 0)
		{
			motionY = (getJumpUpwardsMotion() + 0.20000000298023224D);
			isAirBorne = true;
			ForgeHooks.onLivingJump(this);
			double d0 = attacker.posX - posX;
			double d1 = attacker.posZ - posZ;
			float f1 = MathHelper.sqrt(d0 * d0 + d1 * d1);
			motionX += d0 / f1 * 0.5D * 0.5D + motionX * 0.5D;
			motionZ += d1 / f1 * 0.5D * 0.5D + motionZ * 0.5D;
		}
		
		//----------- Conversion Staff Mechanics -----------\\
		if (convertionInt > 0)
		{
			setAttackTarget(null);
			
			//if (ticksExisted % 10 == 0)
				//world.setEntityState(this, (byte)21);
			
			--convertionDelay;
			
			if (convertionDelay <= 0)
			{
				convertionDelay = 40;
				--convertionInt;
			}
		}
		
		//----------- Health Handling -----------\\
		if (getFakeHealth() > 0 || motionY > 0D)
			fallDistance = 0;
		
		//----------- Eat Handling -----------\\
		boolean mainHand = mainItem != null;
		if ((mainHand && mainItem instanceof ItemFood || offItem != null && offItem instanceof ItemFood) && (getEnergy() < 90F || getHealth() <= getMaxHealth() * 0.5F))
		{
			if (ticksExisted > 53)
				ticksExisted = 20;
				
			if (mainHand)
			{
				swingArm(EnumHand.MAIN_HAND);
				setActiveHand(EnumHand.MAIN_HAND);
			}
			else
			{
				swingArm(EnumHand.OFF_HAND);
				setActiveHand(EnumHand.OFF_HAND);
			}
				
			if (ticksExisted == 50)
			{
				heal((((ItemFood)getHeldItemMainhand().getItem()).getHealAmount(getHeldItemMainhand())));
				setEnergy(getEnergy() + (((ItemFood)getHeldItemMainhand().getItem()).getHealAmount(getHeldItemMainhand()) * 5));
			}
		}
		
		//----------- Moral System -----------\\
		//TODO: Fix client not recieveing moral information
		IAttributeInstance speedAttribute = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		IAttributeInstance damageAttribute = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		
		if (moralRaisedTimer >= 600)
		{
			moralRaisedTimer = 600;
			addPotionEffect(new PotionEffect(MobEffects.HASTE, 400));
			if (onGround)
				jump();
		}
		
		if (moralRaisedTimer <= 0)
		{
			if (speedAttribute.hasModifier(attackingSpeedBoostModifier))
				speedAttribute.removeModifier(attackingSpeedBoostModifier);
			if (damageAttribute.hasModifier(attackingBoostModifier))
				damageAttribute.removeModifier(attackingBoostModifier);
		}
		else
		{
			setSprinting(!getNavigator().noPath());
			if (!speedAttribute.hasModifier(attackingSpeedBoostModifier))
				speedAttribute.applyModifier(attackingSpeedBoostModifier);
			if (!damageAttribute.hasModifier(attackingBoostModifier))
				damageAttribute.applyModifier(attackingBoostModifier);
		}
		
		if (isBeingRidden() && !(this instanceof EntitySquid) && !(this instanceof EntityGuardian))
		{
			if (isInWater() || isInLava())
				motionY += 0.05D;
			
			isJumping = false;
		}
		
		//----------- Misc -----------\\
		if (getRidingEntity() != null && getRidingEntity() instanceof EntityBoat && ((EntityBoat)getRidingEntity()).getControllingPassenger().equals(this))
		{
			((EntityBoat)getRidingEntity()).rotationYaw = rotationYaw;
			float f1 = 0.0F;
			
			if (moveForward > 0F)
				f1 = getAttackTarget() == null ? 0.0F : 0.04F;
			else if (moveForward < 0F)
				f1 = -0.005F;
			
			((EntityBoat)getRidingEntity()).motionX += (double)(MathHelper.sin(-((EntityBoat)getRidingEntity()).rotationYaw * 0.017453292F) * f1);
			((EntityBoat)getRidingEntity()).motionZ += (double)(MathHelper.cos(((EntityBoat)getRidingEntity()).rotationYaw * 0.017453292F) * f1);
		}
		
		
		
		//----------- Mod Compatibilities -----------\\
		if (EngenderCompat.ICE_AND_FIRE_LOADED)
		{
			world.loadedEntityList.forEach(entity ->
			{
				if (entity instanceof EntityMob && entity.isEntityAlive() && entity.width == 0.8F && entity.height == 1.99F)
				{
					EntityMob mob = (EntityMob) entity;
					if (mob.getAttackTarget() != null) mob.setAttackTarget(mob);
				}
			});
		}
	}
	
	public void onLivingUpdate()
	{
		if (world.isRemote)
			onLivingClient();
		else
			onLivingServer();
		
		if (getJukeboxToDanceTo() == null)
		{
			if (getAttackingEntity() == null && ticksExisted % 20 == 0)
			{
				int i11 = MathHelper.floor(posY),
				l1 = MathHelper.floor(posX),
				i2 = MathHelper.floor(posZ);
				
				for (int k2 = -8 - (int)width; k2 <= 8 + (int)width; k2++)
					for (int l2 = -8 - (int)width; l2 <= 8 + (int)width; l2++)
						for (int j = -8 - (int)height; j <= 8 + (int)height; j++)
						{
							int i3 = l1 + k2;
							int k = i11 + j;
							int l = i2 + l2;
							BlockPos blockpos = new BlockPos(i3, k, l);
							IBlockState iblockstate = world.getBlockState(blockpos);
							Block block = iblockstate.getBlock();
							
							if (block == Blocks.JUKEBOX && ((Boolean)iblockstate.getValue(BlockJukebox.HAS_RECORD)).booleanValue())
								setJukeboxToDanceTo(blockpos);
						}
			}
		}
		else
		{
			if (world.isRemote)
			{
				renderYawOffset = rotationYaw = rotationYawHead;
				
				if (ticksExisted % 10 == 0)
					world.spawnParticle(EnumParticleTypes.NOTE, posX, posY + height, posZ, rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), new int[0]);
			}
			else if (ticksExisted % 20 == 0 && this instanceof EntityCreeper && ((EntityCreeper)this).getPowered())
			{
				world.addWeatherEffect(new EntityLightningBolt(world, posX - 0.5D, posY + 1.9D, posZ - 0.5D, true));
				setSitResting(false);
				getNavigator().clearPath();
			}
			
			IBlockState iblockstate = world.getBlockState(getJukeboxToDanceTo());
			Block block = iblockstate.getBlock();
			if (block != Blocks.JUKEBOX || (block == Blocks.JUKEBOX && !((Boolean)iblockstate.getValue(BlockJukebox.HAS_RECORD)).booleanValue()) || getDistanceSqToCenter(jukeBoxToDanceTo) > 10000D)
				setJukeboxToDanceTo(null);
		}
		
		if (ticksExisted > getSpawnTimer())
			super.onLivingUpdate();
		else
		{
			if (world.isRemote)
			{
				if (ticksExisted == 2)
					spawnExplosionParticle();
				renderYawOffset = rotationYaw = rotationYawHead;
			}
			
			motionX = motionZ = 0.0D;
		}
	}
	
	protected void onDeathUpdate()
	{
		
	renderYawOffset = rotationYaw = rotationYawHead;
	getNavigator().clearPath();
	extinguish();
	clearActivePotions();
	setAttackTarget(null);
	
	if (onGround)
	{
	++deathTime;
	limbSwingAmount = 0F;
	limbSwing = 0F;
	setArmsRaised(false);
	prevRotationPitchFalling = rotationPitchFalling = 0F;
	}
	else
	{
	if (getTier().ordinal() > EnumTier.TIER5.ordinal() || this instanceof net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex || this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker || this instanceof EntityWither)
	++deathTime;
	else
	deathTime = 0;
	}
	
	if (!world.isRemote)
	{
	if (deathTime == 2)
	{
	tasks.taskEntries.clear();
	targetTasks.taskEntries.clear();
	renderYawOffset = rotationYaw = rotationYawHead;
	
	if (!world.isRemote && canDropLoot() && world.getGameRules().getBoolean("doMobLoot"))
	{
	int i = getExperiencePoints(attackingPlayer);
	i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, attackingPlayer, i);
	while (i > 0)
	{
	int j = EntityXPOrb.getXPSplit(i);
	i -= j;
	world.spawnEntity(new EntityXPOrb(world, posX, posY + getEyeHeight(), posZ, j));
	}
	experienceValue = 0;
	}
	}
	}
	
	if (recentlyHit > 0)
	++recentlyHit;
	
	if (deathTime < 22 && deathTime > 2)
	--rotationPitch;
	
	if (deathTime > 100)
	{
	if (onGround)
	setNoAI(true);
	if (deathTime > 500 && !world.getBlockState(getPosition().up((int) height + 1)).getMaterial().isSolid())
	posY -= 0.025D;
	}
	
	if (!world.isRemote && deathTime >= (leavesNoCorpse() ? 20 : 600))
	{
	spawnExplosionParticle();
	world.removeEntity(this);
	}
	}
	public void onDeath(DamageSource cause)
	{
		if (!world.isRemote && EngenderConfig.general.useMessage && (isHero() || isBoss()))
		{
			Entity owner = this.getOwner();
			List<EntityPlayerMP> players = getServer().getPlayerList().getPlayers();
			for (EntityPlayerMP player : players)
			{
				if (owner != null && owner.equals(player))
					DialogueManager.sendSubDialogue(player, "", "Your " + getName() + " was slain!", null, false, DialogColors.COLOR_TEXT_ENEMY, DialogColors.COLOR_BACKGROUND_ENEMY);
				else
					DialogueManager.sendSubDialogue(player, "", getName() + (owner == null ? "" : " of " + owner.getName() + "'s forces ") + " was slain", null, false, DialogColors.COLOR_TEXT_ALLY, DialogColors.COLOR_BACKGROUND_ALLY);
			}
		}
	if (cause.getDamageType() == "antimatter")
	{
	getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() * 2D);
	setHealth(getMaxHealth());
	deathTime = 0;
	dead = false;
	setIsAntiMob(true);
	ticksExisted = 0;
	renderYawOffset = rotationYaw = rotationYawHead = 0F;
	rotationPitch = 0F;
	int i = experienceValue;
	if (this instanceof EntityEvoker)
	{
	setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
	setDropChance(EntityEquipmentSlot.MAINHAND, 0.0F);
	setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
	setDropChance(EntityEquipmentSlot.OFFHAND, 0.0F);
	}
	while (i > 0 && !world.isRemote)
	{
	int j = EntityXPOrb.getXPSplit(i);
	i -= j;
	world.spawnEntity(new EntityXPOrb(world, posX, posY, posZ, j));
	}
	}
	else if (hasLastChance())
	{
	lastChanceInvul = 200;
	setHealth(1.0F);
	clearActivePotions();
	inflictCustomStatusEffect(EnumDifficulty.PEACEFUL, this, MobEffects.GLOWING, 10, 1);
	setRevengeTarget(null);
	setAttackTarget(null);
	setEnergy(100F);
	world.setEntityState(this, (byte)35);
	if (!isWild())
	copyLocationAndAnglesFrom(getOwner());
	setLastChance(false);
	}
	else
	{
	if (hasLimitedLife())
	onKillCommand();
	if (EngenderConfig.general.useMessage && !isWild() && getOwner() instanceof EntityPlayerMP && !(this instanceof EntityWitherStormHead) && !(this instanceof EntityWitherStormTentacle) && !(this instanceof EntityWitherStormTentacleDevourer))
	{
	if (!world.isRemote)
	((EntityPlayerMP)getOwner()).sendMessage(getCombatTracker().getDeathMessage());
	world.playSound((EntityPlayerMP) getOwner(), getOwner().getPosition(), getDeathSound(), getSoundCategory(), getSoundVolume(), getSoundPitch());
	net.minecraftforge.common.ForgeHooks.onLivingDeath(this, DamageSource.causeMobDamage(getOwner()));
	}
	dropEquipmentUndamaged();
	setAttackTarget(null);
	setRevengeTarget(null);
	if (this instanceof Flying)
	{
	float xRatio = MathHelper.sin(rotationYawHead * 0.017453292F);
	float zRatio = -MathHelper.cos(rotationYawHead * 0.017453292F);
	float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
	motionX -= xRatio / (double)f * ((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() + 1F);
	motionZ -= zRatio / (double)f * ((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() + 1F);
	}
	if (cause.getTrueSource() instanceof EntityPlayerMP)
	CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger((EntityPlayerMP)cause.getTrueSource(), this, cause);
	super.onDeath(cause);
	}
	}
	
	public void onKillEntity(EntityLivingBase entityLivingIn)
	{
	if (!isWild())
	{
		EntityPlayer player = (EntityPlayer)getOwner();
		player.onKillEntity(entityLivingIn);
	}
	super.onKillEntity(entityLivingIn);
	
	setTotalEXP(getTotalEXP() + entityLivingIn.getMaxHealth() * 4.5F * (entityLivingIn.isNonBoss() ? 1.0F : 2.0F));
	
	getNavigator().clearPath();
	//getNavigator().tryMoveToEntityLiving(this, 1D);
	
	if (getAttackTarget() != null && !getAttackTarget().isEntityAlive() && entityLivingIn == getAttackTarget())
	setAttackTarget((EntityLivingBase)null);
	
	if (getAttackTarget() != null && getAttackTarget() instanceof EntityFriendlyCreature && isOnSameTeam((EntityFriendlyCreature)getAttackTarget()) && ((EntityFriendlyCreature)getAttackTarget()).getFakeHealth() <= 0F)
	setAttackTarget((EntityLivingBase)null);
	}
	
	public void setLimitedLife(int limitedLifeTicksIn)
	{
		limitedLifeTicks = limitedLifeTicksIn;
	}
	
	public boolean hasLimitedLife()
	{
		return limitedLifespan;
	}
	
	public int getLimitedLife()
	{
		return limitedLifeTicks;
	}
	
	public boolean setEntityOnShoulder(EntityPlayer p_191994_1_)
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("id", getEntityString());
		writeToNBT(nbttagcompound);
		
		if (p_191994_1_.addShoulderEntity(nbttagcompound))
		{
			world.removeEntity(this);
			return true;
		}
		else
		return false;
	}
	
	public boolean canSitOnShoulder()
	{
		return rideCooldownCounter > 100;
	}
	
	public static enum EnumStudy
	{
		Physical(),
		Mental(),
		Combative()
	}
	
	public Vec3d[] getRenderLocations(float p_193098_1_)
	{
		if (getGhostTime() <= 0)
		{
		return renderLocations[1];
		}
		else
		{
		double d0 = (double)(((float)getGhostTime() - p_193098_1_) / 3.0F);
		d0 = Math.pow(d0, 0.25D);
		Vec3d[] avec3d = new Vec3d[4];
		
		for (int i = 0; i < 4; ++i)
		{
		avec3d[i] = renderLocations[1][i].scale(1.0D - d0).add(renderLocations[0][i].scale(d0));
		}
		
		return avec3d;
		}
	}
	
	public void setCurrentBook(ItemStack stack)
	{
	currentReadingBook = stack;
	}
	public ItemStack getCurrentBook()
	{
	return getBookID() != 0 ? currentReadingBook : ItemStack.EMPTY;
	}
	protected boolean isMovementBlocked()
	{
	return getHealth() <= 0.0F || (!getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() && getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemLearningBook);
	}
	public int getHoldRoseTick()
	{
	return holdRoseTick;
	}
	public int getAttackTimer()
	{
	return attackTimer;
	}
	public boolean shouldFleeAtLowHealth()
	{
	return getHealth() <= 4F && getHealth() < getMaxHealth();
	}
	public static EngenderExplosion createEngenderModExplosion(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
	{
		EngenderExplosion explosion = new EngenderExplosion(entityIn.world, entityIn, x, y, z, strength, isFlaming, isSmoking);
		explosion.doExplosionA();
		explosion.doExplosionB(true);
		return explosion;
	}
	public static EngenderExplosion createEngenderModExplosionFireless(@Nullable Entity entityIn, double x, double y, double z, float strength, boolean isSmoking)
	{
	return createEngenderModExplosion(entityIn, x, y, z, strength, false, isSmoking);
	}
	public void setArmsRaised(boolean armsRaised)
	{
	getDataManager().set(ARMS_RAISED, Boolean.valueOf(armsRaised));
	}
	
	public boolean isArmsRaised()
	{
		return ((Boolean)getDataManager().get(ARMS_RAISED)).booleanValue();
	}
	
	public int timesToConvert()
	{
		switch (getTier())
		{
			case TIER2:
				return 5;
			case TIER3:
				return 11;
			case TIER4:
				return 23;
			case TIER5:
				return 79;
			case TIER6:
				return Integer.MAX_VALUE;
			default:
				return 3;
		}
	}
	
	public final float getEnergy()
	{
	return ((Float)dataManager.get(ENERGY)).floatValue();
	}
	
	public void setEnergy(float health)
	{
	dataManager.set(ENERGY, Float.valueOf(health));
	}
	
	/**
	* Used to level up stats of a mob with a Learning Book
	* Uses 1 durability on success
	* @param book
	*/
	public void learnLevelUp(ItemLearningBook book)
	{
		TextFormatting STR;
		TextFormatting STA;
		TextFormatting INT;
		TextFormatting DEX;
		TextFormatting AGL;
		double STRCAL, STRCUR = getStrength();
		double STACAL, STACUR = getStamina();
		double INTCAL, INTCUR = getIntelligence();
		double DEXCAL, DEXCUR = getDexterity();
		double AGLCAL, AGLCUR = getAgility();
		
		if (book.isArtifact())
		{
			STRCAL = 100.0D;
			STACAL = 100.0D;
			INTCAL = 100.0D;
			DEXCAL = 100.0D;
			AGLCAL = 100.0D;
			setLevel(300);
		}
		else
		{
			STRCAL = Math.round((rand.nextFloat() * book.STRENGTH) * 100.0f) / 100.0f;
			STACAL = Math.round((rand.nextFloat() * book.STAMINA) * 100.0f) / 100.0f;
			INTCAL = Math.round((rand.nextFloat() * book.INTELEGENCE) * 100.0f) / 100.0f;
			DEXCAL = Math.round((rand.nextFloat() * book.DEXTERITY) * 100.0f) / 100.0f;
			AGLCAL = Math.round((rand.nextFloat() * book.AGILITY) * 100.0f) / 100.0f;
		}
		
		String STRS;
		String STAS;
		String INTS;
		String DEXS;
		String AGLS;
		
		if (STRCAL + STRCUR < STRCUR)
		{
			STR = TextFormatting.RED;
			STRS = "" + STRCAL + " STR ";
		}
		else if (STRCAL + STRCUR > STRCUR)
		{
			STR = TextFormatting.GREEN;
			STRS = "+" + STRCAL + " STR ";
		}
		else
		{
			STR = TextFormatting.RESET;
			STRS = "";
		}
		
		if (STACAL + STACUR < STACUR)
		{
			STA = TextFormatting.RED;
			STAS = "" + STACAL + " STA ";
		}
		else if (STACAL + STACUR > STACUR)
		{
			STA = TextFormatting.GREEN;
			STAS = "+" + STACAL + " STA ";
		}
		else
		{
			STA = TextFormatting.RESET;
			STAS = "";
		}
		
		if (INTCAL + INTCUR < INTCUR)
		{
			INT = TextFormatting.RED;
			INTS = "" + INTCAL + " INT ";
		}
		else if (INTCAL + INTCUR > INTCUR)
		{
			INT = TextFormatting.GREEN;
			INTS = "+" + INTCAL + " INT ";
		}
		else
		{
			INT = TextFormatting.RESET;
			INTS = "";
		}
		
		if (DEXCAL + DEXCUR < DEXCUR)
		{
			DEX = TextFormatting.RED;
			DEXS = "" + DEXCAL + " DEX ";
		}
		else if (DEXCAL + DEXCUR > DEXCUR)
		{
			DEX = TextFormatting.GREEN;
			DEXS = "+" + DEXCAL + " DEX ";
		}
		else
		{
			DEX = TextFormatting.RESET;
			DEXS = "";
		}
		
		if (AGLCAL + AGLCUR < AGLCUR)
		{
			AGL = TextFormatting.RED;
			AGLS = "" + AGLCAL + " AGL ";
		}
		else if (AGLCAL + AGLCUR > AGLCUR)
		{
			AGL = TextFormatting.GREEN;
			AGLS = "+" + AGLCAL + " AGL ";
		}
		else
		{
			AGL = TextFormatting.RESET;
			AGLS = "";
		}
		
		if (STRCAL + STACAL + INTCAL + DEXCAL + AGLCAL == 0)
		{
			STR = TextFormatting.RED;
			STRS = "Nothing...";
			playSound(SoundEvents.ENTITY_EGG_THROW, 0.5F, 1F);
		}
		else
			playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 0.5F, 1F);
		
		getCurrentBook().damageItem(1, this);
		setStrength(getStrength() + STRCAL, false);
		setStamina(getStamina() + STACAL, false);
		setIntelligence(getIntelligence() + INTCAL, false);
		setDexterity(getDexterity() + DEXCAL, false);
		setAgility(getAgility() + AGLCAL, false);
		
		if (book.tier == 6)
		{
			if (!world.isRemote && !isWild())
				getOwner().sendMessage(new TextComponentTranslation(TextFormatting.AQUA + getName() + TextFormatting.RESET + " has read \"" + TextFormatting.GOLD + getCurrentBook().getDisplayName() + TextFormatting.RESET + "\" and has learned: " + STR + STRS + TextFormatting.RESET + STA + STAS + TextFormatting.RESET + INT + INTS + TextFormatting.RESET + DEX + DEXS + TextFormatting.RESET + AGL + AGLS , new Object[0]));
		}
		else
		{
			if (!world.isRemote && !isWild())
				getOwner().sendMessage(new TextComponentTranslation(TextFormatting.AQUA + getName() + TextFormatting.RESET + " has read \"" + TextFormatting.GOLD + getCurrentBook().getDisplayName().substring(15) + TextFormatting.RESET + "\" and has learned: " + STR + STRS + TextFormatting.RESET + STA + STAS + TextFormatting.RESET + INT + INTS + TextFormatting.RESET + DEX + DEXS + TextFormatting.RESET + AGL + AGLS , new Object[0]));
		}
	}
	
	@Deprecated
	public EnumStudy getCurrentStudy()
	{
		return currentStudy;
	}
	
	@Deprecated
	public void setCurrentStudy(EnumStudy study, int exp)
	{
		if (!world.isRemote && !hasLimitedLife())
			currentStudy = study;
	}
	
	public final float getFakeHealth()
	{
	return ((Float)dataManager.get(FAKE_HEALTH)).floatValue();
	}
	
	public void setFakeHealth(float health)
	{
	dataManager.set(FAKE_HEALTH, Float.valueOf(MathHelper.clamp(health, 0.0F, getMaxHealth() * 2F)));
	}
	
	protected void jump()
	{
	if (getEnergy() > 5F)
	super.jump();
	setEnergy(getEnergy() - 0.05F);
	}
	
	public void levelUp()
	{
		levelUp(true);
	}
	
	public void levelUp(boolean forceLevel)
	{
		int level = getLevel(), maxLevel = EngenderConfig.mobs.maxLevel - 1;
		spawnExplosionParticle();
		clearActivePotions();
		addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200));
		setEnergy(100F);
		
		if (forceLevel)
		{
			if (level < maxLevel)
				level++;
			setLevel(level);
		}
		
		if (level >= maxLevel)
		{
			playSound(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 0.5F, 1F);
			if (!world.isRemote)
				world.spawnEntity(new EntityXPOrb(world, posX, posY + getEyeHeight(), posZ, 10 + rand.nextInt(40)));
		}
		else
		{
				if (level == maxLevel - 1)
				{
					if (!world.isRemote && !isWild())
						getOwner().sendMessage(new TextComponentTranslation(TextFormatting.AQUA + getName() + TextFormatting.RESET + " has reached " + TextFormatting.GOLD + "Max Level" + TextFormatting.RESET + "!", new Object[0]));
					playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
					if (!world.isRemote)
						world.spawnEntity(new EntityXPOrb(world, posX, posY + getEyeHeight(), posZ, 10 + rand.nextInt(40)));
				}
				else
				{
					if (!world.isRemote && !isWild())
						getOwner().sendMessage(new TextComponentTranslation(TextFormatting.AQUA + getName() + TextFormatting.RESET + " has reached " + TextFormatting.BLUE + "Level " + (level + 1) + TextFormatting.RESET + "!", new Object[0]));
					playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.5F, 1F);
				}
		}
	}
	
	public void onKillCommand()
	{
	playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
	spawnExplosionParticle();
	setDead();
	}
	
	protected void damageArmor(float damage)
	{
	damage = damage / 4.0F;
	
	if (damage < 1.0F)
	{
	damage = 1.0F;
	}
	if (!world.isRemote)
	{
	for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
	{
	ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);
	
	if (!itemstack.isEmpty() && entityequipmentslot != EntityEquipmentSlot.MAINHAND && entityequipmentslot != EntityEquipmentSlot.OFFHAND)
	{
	if (itemstack.getItem() instanceof ItemArmor)
	{
	setCurrentStudy(EnumStudy.Combative, (int)damage);
	itemstack.damageItem((int)damage, this);
	}
	}
	}
	}
	}
	
	protected void damageShield(float damage)
	{
	if (!world.isRemote)
	{
	if (damage >= 3.0F && activeItemStack.getItem() == Items.SHIELD)
	{
	setCurrentStudy(EnumStudy.Combative, (int)damage);
	blockTimer = 40;
	int i = 1 + MathHelper.floor(damage);
	activeItemStack.damageItem(i, this);
	playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 1.0F);
	if (activeItemStack.isEmpty())
	{
	EnumHand enumhand = getActiveHand();
	
	if (enumhand == EnumHand.MAIN_HAND)
	{
	setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
	}
	else
	{
	setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
	}
	
	activeItemStack = ItemStack.EMPTY;
	playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + world.rand.nextFloat() * 0.4F);
	}
	}
	}
	}
	
	public boolean isActiveItemStackBlocking()
	{
	return getAttackTarget() != null && blockTimer <= 0 && getHeldItemOffhand() != null && !isHandActive() && getHeldItemOffhand().getItem().getItemUseAction(getHeldItemOffhand()) == EnumAction.BLOCK && getDistanceSq(getAttackTarget()) < 25.0D;
	}
	
	protected boolean canDropLoot()
	{
	return !isOffensive && isChild() ? false : !hasLimitedLife();
	}
	
	public int getPolymorphTime()
	{
	return ((Integer)dataManager.get(POLYMORPH_TIME)).intValue();
	}
	
	public void setPolymorphTime(int age)
	{
	dataManager.set(POLYMORPH_TIME, Integer.valueOf(age));
	}
	public int getIllusionFormTime()
	{
	return ((Integer)dataManager.get(ILLUSION_FORM_TIME)).intValue();
	}
	
	public void setIllusionFormTime(int age)
	{
	dataManager.set(ILLUSION_FORM_TIME, Integer.valueOf(age));
	}
	public int getGhostTime()
	{
	return ((Integer)dataManager.get(GHOST_TIME)).intValue();
	}
	
	public void setGhostTime(int age)
	{
	dataManager.set(GHOST_TIME, Integer.valueOf(age));
	}
	public int getBookID()
	{
	return ((Integer)dataManager.get(BOOK_ID)).intValue();
	}
	
	public void setBookID(int age)
	{
	dataManager.set(BOOK_ID, Integer.valueOf(age));
	}
	public int getBookDurability()
	{
	return ((Integer)dataManager.get(BOOK_DURABILITY)).intValue();
	}
	
	public void setBookDurability(int age)
	{
	dataManager.set(BOOK_DURABILITY, Integer.valueOf(age));
	}
	
	public boolean isChild()
	{
	return ((Boolean)getDataManager().get(IS_CHILD)).booleanValue();
	}
	public void setChild(boolean childZombie)
	{
	getDataManager().set(IS_CHILD, Boolean.valueOf(childZombie));
	
	if (world != null && !world.isRemote)
	{
	IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
	iattributeinstance.removeModifier(BABY_SPEED_BOOST);
	
	if (childZombie)
	iattributeinstance.applyModifier(BABY_SPEED_BOOST);
	}
	
	setSize(width, height);
	}
	
	protected int getExperiencePoints(EntityPlayer player)
	{
	int xp = super.getExperiencePoints(player);
	
	if (isChild())
	xp = (int)(xp * 2.5F);
	
	xp += getStrength() / 10;
	xp += getStamina() / 10;
	xp += getAgility() / 10;
	xp += getIntelligence() / 10;
	xp += getDexterity() / 10;
	xp *= getFittness();
	
	return xp;
	}
	
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		setLeftHanded(rand.nextFloat() < 0.1F);
		setGrowingAge(world.rand.nextFloat() <= 0.05F ? -20000 : 4000);
		setEnergy(100F);
		setStrength(getDefaultStrengthStat(), true);
		setStamina(getDefaultStaminaStat(), true);
		setIntelligence(getDefaultIntelligenceStat(), true);
		setDexterity(getDefaultDexterityStat(), true);
		setAgility(getDefaultAgilityStat(), true);
		setFittness(getDefaultFittnessStat(), true);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random spawn bonus", rand.nextGaussian() * 0.05D, 1));
		return livingdata;
	}
	
	public void playSound(SoundEvent soundIn, float volume, float pitch)
	{
	if (soundIn != null)
	{
	super.playSound(soundIn, volume, pitch);
	}
	}
	
	/**
	* If the creature in question shouldn't be considered alive. Undead and Construts by default are this
	*/
	public boolean isUndead() 
	{
		return this instanceof EntitySlime || isEntityUndead() || getCreatureAttribute() == ESetup.CONSTRUCT || this instanceof Structure || this instanceof Elemental;
	}
	
	public final double getVigor()
	{
		return getEntityAttribute(VIGOR).getAttributeValue();
	}
	
	public final double getBaseVigor()
	{
		return getEntityAttribute(VIGOR).getBaseValue();
	}
	
	public void setVigor(double value, boolean setBaseValue)
	{
		if (!world.isRemote)
		{
			if (setBaseValue)
				VIGOR_MODIFIER.applyBase(getEntityAttribute(VIGOR), value);
			else
				VIGOR_MODIFIER.apply(getEntityAttribute(VIGOR), value, 0);
			
			if (!getEntityAttribute(FITTNESS).hasModifier(VIGOR_MODIFIER.getModifier()))
				VIGOR_MODIFIER.apply(getEntityAttribute(FITTNESS), value * 0.3D, 0);
		}
	}
	
	public final double getStrength()
	{
		return getEntityAttribute(STRENGTH).getAttributeValue();
	}
	
	public final double getBaseStrength()
	{
		return getEntityAttribute(STRENGTH).getBaseValue();
	}
	
	public void setStrength(double value, boolean setBaseValue)
	{
		if (!world.isRemote)
		{
			if (setBaseValue)
				STRENGTH_MODIFIER.applyBase(getEntityAttribute(STRENGTH), value);
			else
				STRENGTH_MODIFIER.apply(getEntityAttribute(STRENGTH), value, 0);

			if (!getEntityAttribute(STRENGTH).hasModifier(FLEEING_SPEED_MODIFIER))
					getEntityAttribute(STRENGTH).applyModifier(FLEEING_SPEED_MODIFIER);
		}
	}
	
	public final double getStamina()
	{
		return getEntityAttribute(STAMINA).getAttributeValue();
	}
	
	public final double getBaseStamina()
	{
		return getEntityAttribute(STAMINA).getBaseValue();
	}
	
	public void setStamina(double value, boolean setBaseValue)
	{
		if (!world.isRemote)
			if (setBaseValue)
				STAMINA_MODIFIER.applyBase(getEntityAttribute(STAMINA), value);
			else
				STAMINA_MODIFIER.apply(getEntityAttribute(STAMINA), value, 0);
	}
	
	public final double getIntelligence()
	{
		return getEntityAttribute(INTELLIGENCE).getAttributeValue();
	}
	
	public final double getBaseIntelligence()
	{
		return getEntityAttribute(INTELLIGENCE).getBaseValue();
	}
	
	public void setIntelligence(double value, boolean setBaseValue)
	{
		if (!world.isRemote)
			if (setBaseValue)
				INTELLIGENCE_MODIFIER.applyBase(getEntityAttribute(INTELLIGENCE), value);
			else
				INTELLIGENCE_MODIFIER.apply(getEntityAttribute(INTELLIGENCE), value, 0);
	}
	
	public final double getDexterity()
	{
		return getEntityAttribute(DEXTERITY).getAttributeValue();
	}
	
	public final double getBaseDexterity()
	{
		return getEntityAttribute(DEXTERITY).getBaseValue();
	}
	
	public void setDexterity(double value, boolean setBaseValue)
	{
		if (!world.isRemote)
			if (setBaseValue)
				DEXTERITY_MODIFIER.applyBase(getEntityAttribute(DEXTERITY), value);
			else
				DEXTERITY_MODIFIER.apply(getEntityAttribute(DEXTERITY), value, 0);
	}
	
	public final double getAgility()
	{
		return getEntityAttribute(AGILITY).getAttributeValue();
	}
	
	public final double getBaseAgility()
	{
		return getEntityAttribute(AGILITY).getBaseValue();
	}
	
	public void setAgility(double value, boolean setBaseValue)
	{
		if (!world.isRemote)
			if (setBaseValue)
				AGILITY_MODIFIER.applyBase(getEntityAttribute(AGILITY), value);
			else
				AGILITY_MODIFIER.apply(getEntityAttribute(AGILITY), value, 0);
	}
	
	public final float getFittness()
	{
		return (float)getFittnessD();
	}
	
	public final double getFittnessD()
	{
		return getEntityAttribute(FITTNESS).getAttributeValue();
	}
	
	public final double getBaseFittness()
	{
		return getEntityAttribute(FITTNESS).getBaseValue();
	}
	
	public void setFittness(double value, boolean setBaseValue)
	{
		if (!world.isRemote)
			if (setBaseValue)
				FITTNESS_MODIFIER.applyBase(getEntityAttribute(FITTNESS), value);
			else
				FITTNESS_MODIFIER.apply(getEntityAttribute(FITTNESS), value, 0);
	}
	
	public double getDefaultStrengthStat()
	{
		return 4D + rand.nextDouble() * 8D;
	}
	
	public double getDefaultStaminaStat()
	{
		return isUndead() ? 100D : 4D + (rand.nextDouble() * 8D);
	}
	
	public double getDefaultIntelligenceStat()
	{
		return 4D + rand.nextDouble() * 8D;
	}
	
	public double getDefaultDexterityStat()
	{
		return 4D + rand.nextDouble() * 8D;
	}
	
	public double getDefaultAgilityStat()
	{
		return 4D + rand.nextDouble() * 8D;
	}
	
	public double getDefaultFittnessStat()
	{
	return 0.9D + (rand.nextFloat() * 0.2D);
	}
	
	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
	return 1;
	}
	
	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
	return 1;
	}
	
	/**
	* Bonus damage vs mobs that implement Flying
	*/
	public float getBonusVSFlying()
	{
	return 1;
	}
	
	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
	return 1;
	}
	
	/**
	* Bonus damage vs mobs that implement Tiny
	*/
	public float getBonusVSTiny()
	{
	return 1;
	}
	
	/**
	* Bonus damage vs mobs that implement Structure
	*/
	public float getBonusVSStructure()
	{
	return 1;
	}
	
	/**
	* Bonus damage vs mobs that implement Elemental
	*/
	public float getBonusVSElemental()
	{
	return 1;
	}
	
	/**
	* Bonus damage vs mobs that implement Undead
	*/
	public float getBonusVSUndead()
	{
	return 1;
	}
	
	/**
	* Bonus damage vs mobs that implement Ender
	*/
	public float getBonusVSEnder()
	{
	return 1;
	}
	
	/**
	* Bonus damage vs mobs that implement Animal
	*/
	public float getBonusVSAnimal()
	{
	return 1;
	}
	
	
	public boolean canBeSteered()
	{
	return getControllingPassenger() instanceof EntityLivingBase;
	}
	
	public void incrementConversion(EntityPlayer player)
	{
		if (isWild())
		{
			EngenderMod.debug("Converting...");
			if (convertionInt >= timesToConvert())
			{
				EngenderMod.debug("Converting A");
				setAttackTarget(null);
				world.playEvent((EntityPlayer)null, 3000, new BlockPos(posX, posY, posZ), 0);
				setOwnerId(player.getPersistentID());
				ticksExisted = 0;
				playSound(ESound.converted, 3.0F, 1.0F);
				if (player instanceof EntityPlayerMP)
				{
					ESetup.CONVERT_MOB.trigger((EntityPlayerMP)player, this);
				}
				if (player != null && !isWild() && !world.isRemote)
					player.sendMessage(new TextComponentTranslation(getName() + " has been converted by " + player.getName() + " (" + (int)posX + ", " + (int)posY + ", " + (int)posZ + ", " + ")", new Object[0]));
				convertionInt = 0;
			}
			else
			{
				convertionInt++;
				EngenderMod.debug("Converting B");
				playSound(ESound.converting, 3.0F, 1.0F);
				setAttackTarget(null);
				convertionDelay = 200;
			}
		}
		else
		{
			EngenderMod.debug("Converting C");
			targetTasks.removeTask(trainingAI);
			if (getFakeHealth() > 0F)
				targetTasks.addTask(3, trainingAI);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
	double d0 = getEntityBoundingBox().getAverageEdgeLength();
	if (Double.isNaN(d0))
	{
	d0 = 1.0D;
	}
	d0 = d0 * (isSneaking() ? 32D : 512D);
	return ignoreFrustumCheck ? true : distance < d0 * d0;
	}
	public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_)
	{
	return !isOnSameTeam(p_142018_1_);
	}
	protected boolean canDespawn()
	{
	return isWild() && !isBoss();
	}
	public boolean getCanSpawnHere()
	{
	return (world.getWorldInfo().getTerrainType() == WorldType.FLAT ? rand.nextInt(getSpawnChanceReduction() * 5) == 0 : rand.nextInt(getSpawnChanceReduction()) == 0) && super.getCanSpawnHere();
	}
	
	public float getBlockPathWeight(BlockPos pos)
	{
	return 0.0F;
	}
	
	public int getSpawnChanceReduction()
	{
	switch (getTier())
	{
	case TIER2:return 100;
	case TIER3:return 200;
	case TIER4:return 400;
	case TIER5:return 800;
	default:
	return 50;
	}
	}
	
	public int getDoorBreakingThreashHold()
	{
	return 32;
	}
	
	public int getInstaDoorBreakingThreashHold()
	{
	return 64;
	}
	
	public int getIronDoorBreakingThreashHold()
	{
	return 96;
	}
	
	public boolean teleportTo_(double x, double y, double z)
	{
	double d0 = posX;
	double d1 = posY;
	double d2 = posZ;
	posX = x;
	posY = y;
	posZ = z;
	boolean flag = false;
	BlockPos blockpos = new BlockPos(this);
	getRNG();
	
	if (world.isBlockLoaded(blockpos))
	{
	boolean flag1 = false;
	
	while (!flag1 && blockpos.getY() > 0)
	{
	BlockPos blockpos1 = blockpos.down();
	IBlockState iblockstate = world.getBlockState(blockpos1);
	
	if (iblockstate.getMaterial().blocksMovement())
	{
	flag1 = true;
	}
	else
	{
	--posY;
	blockpos = blockpos1;
	}
	}
	
	if (flag1)
	{
	setPositionAndUpdate(posX, posY, posZ);
	
	if (world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(getEntityBoundingBox()))
	{
	flag = true;
	}
	}
	}
	
	if (!flag)
	{
	setPositionAndUpdate(d0, d1, d2);
	return false;
	}
	else
	{
	getNavigator().clearPath();
	
	return true;
	}
	}
	protected boolean isValidLightLevel()
	{
	return true;
	}
	
	public boolean doesEntityNotTriggerPressurePlate()
	{
	return getDexterity() > 8;
	}
	public int getSpecialAttackTimer()
	{
	return ((Integer)dataManager.get(HEROSPECIALATTACKTIMER)).intValue();
	}
	public void setSpecialAttackTimer(int p_82215_1_)
	{
	dataManager.set(HEROSPECIALATTACKTIMER, Integer.valueOf(p_82215_1_));
	}
	public void performSpecialAttack() { }
	private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
	private final EntityAIOpenDoor openDoor = new EntityAIOpenDoor(this, true);
	public void setDoorAItask(boolean enabled)
	{
	((PathNavigateGround)getNavigator()).setBreakDoors(enabled);
	((PathNavigateGround)getNavigator()).setEnterDoors(true);
	
	if (enabled)
	{
	tasks.removeTask(openDoor);
	tasks.addTask(1, breakDoor);
	}
	else
	{
	tasks.removeTask(breakDoor);
	tasks.addTask(1, openDoor);
	}
	}
	
		
		protected void dropEquipmentUndamaged()
		{
		if (!world.isRemote && !hasLimitedLife())
		{
		for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
		{
		ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);
		
		if (!itemstack.isEmpty())
		{
		if (EnchantmentHelper.hasBindingCurse(itemstack))
		attackEntityFrom((new DamageSource("cramming")).setDamageBypassesArmor().setDamageIsAbsolute().setDifficultyScaled(), 10F);
		if (EnchantmentHelper.hasVanishingCurse(itemstack))
		{
		playSound(SoundEvents.ITEM_TOTEM_USE, 1F, 1F);
		spawnExplosionParticle();
		setItemStackToSlot(entityequipmentslot, ItemStack.EMPTY);
		}
		else
		{
		playEquipSound(itemstack);
		entityDropItem(itemstack, getEyeHeight());
		setItemStackToSlot(entityequipmentslot, ItemStack.EMPTY);
		}
		}
		}
		}
	}
	
	public Team getTeam()
	{
	if (!isWild())
	{
	EntityLivingBase entitylivingbase = getOwner();
	
	if (entitylivingbase != null)
	{
	return entitylivingbase.getTeam();
	}
	}
	
	return super.getTeam();
	}
	
	public boolean affectedByCommandingStaff()
	{
	return !isHero() && !isCameo();
	}
	
	public boolean attackable()
	{
	return ticksExisted > getSpawnTimer() && !isAIDisabled() && isEntityAlive();
	}
	
	public boolean isASwarmingMob()
	{
	return false;
	}
	
	public EnumPushReaction getPushReaction()
	{
	return isAIDisabled() ? EnumPushReaction.IGNORE : super.getPushReaction();
	}
	
	public boolean isOnSameTeam(Entity entity)
	{
		if (entity.equals(this) || !(entity instanceof EntityLivingBase)) return true;
		EntityLivingBase entityLiving = (EntityLivingBase)entity, owner = getOwner();
		if (entityLiving.equals(owner) || !entityLiving.attackable()) return true;
		
		if (entityLiving instanceof EntityWitherStorm)
			return false;
		
		if (isWild())
		{
			if (entityLiving instanceof EntityFriendlyCreature)
				return ((EntityFriendlyCreature)entityLiving).isWild();
			
			if (entityLiving instanceof EntityMob && this instanceof EntityWither && !(entityLiving instanceof net.minecraft.entity.boss.EntityWither) && ((EntityMob)entityLiving).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD)
				return true;
			
			if (entityLiving instanceof net.minecraft.entity.passive.EntityVillager && !isEntityUndead() && !(this instanceof EntityVindicator) && !(this instanceof EntityVex) && !(this instanceof EntityEvoker))
				return true;
			
			if (entityLiving instanceof net.minecraft.entity.passive.EntitySquid && !(this instanceof EntityWither) && !(this instanceof EntityGuardian))
				return true;
			
			if (convertionInt > 0)
				return true;
			
			if (getAttackingEntity() != null && getAttackingEntity().equals(entityLiving))
				return false;
		}
		else
		{	
			if (entityLiving instanceof net.minecraft.entity.monster.IMob)
				return false;
			
			if (owner != null && (entityLiving == owner.getAttackingEntity() || entityLiving == owner.getLastAttackedEntity()))
				return false;
			
			if (entityLiving instanceof EntityFriendlyCreature)
					if (((EntityFriendlyCreature)entityLiving).isWild())
						return false;
					else if (((EntityFriendlyCreature)entityLiving).getOwner() == owner && ((EntityFriendlyCreature)entityLiving).getFakeHealth() <= 0.0F)
						return true;
			if (!isAThreatToOwner(entityLiving))
				return true;
		}
		
		return super.isOnSameTeam(entity);
	}
	
	public boolean isAThreatToOwner(EntityLivingBase otherEntity)
	{
	if (otherEntity == this || !otherEntity.attackable())
	{
		return false;
	}
	
	EntityLivingBase entitylivingbase = getOwner();
	
	if (otherEntity instanceof EntityPlayer)
	{
	if (otherEntity == entitylivingbase)
	{
	return false;
	}
	
	if (((EntityPlayer)otherEntity).capabilities.disableDamage)
	{
	return false;
	}
	
	if (entitylivingbase.isOnSameTeam(otherEntity))
	{
	return false;
	}
	}
	
	if (otherEntity instanceof net.minecraft.entity.EntityCreature && (((net.minecraft.entity.EntityCreature)otherEntity).getAttackTarget() == getOwner() || ((net.minecraft.entity.EntityCreature)otherEntity).getAttackTarget() == this))
	{
	return false;
	}
	
	if (otherEntity instanceof net.minecraft.entity.monster.EntityPigZombie && !((net.minecraft.entity.monster.EntityPigZombie)otherEntity).isAngry())
	{
	return false;
	}
	
	if (otherEntity instanceof EntityFriendlyCreature)
	{
	if (((EntityFriendlyCreature)otherEntity).isWild() && (((EntityFriendlyCreature)otherEntity).convertionInt <= 0 || (((EntityFriendlyCreature)otherEntity).convertionInt > 0 && this instanceof EntityEvoker)))
	{
	return false;
	}
	
	if ((!((EntityFriendlyCreature)otherEntity).isWild() && ((EntityFriendlyCreature)otherEntity).getOwner() != entitylivingbase && !((EntityFriendlyCreature)otherEntity).getOwner().isOnSameTeam(entitylivingbase)))
	{
	return false;
	}
	
	if (((EntityFriendlyCreature)otherEntity).getFakeHealth() <= 0F && !((EntityFriendlyCreature)otherEntity).isWild() && ((EntityFriendlyCreature)otherEntity).getOwner() != null && ((EntityFriendlyCreature)otherEntity).getOwner() == entitylivingbase)
	{
	return false;
	}
	
	if (((EntityFriendlyCreature)otherEntity).isAIDisabled() || ((EntityFriendlyCreature)otherEntity).deathTime > 40 || (((EntityFriendlyCreature)otherEntity).isBoss() && !((EntityFriendlyCreature)otherEntity).isEntityAlive()) || (((EntityFriendlyCreature)otherEntity).getOwnerId() != null && ((EntityFriendlyCreature)otherEntity).getOwner() == null))
	{
	return false;
	}
	
	if (((EntityFriendlyCreature)otherEntity).isWild() && ((EntityFriendlyCreature)otherEntity).convertionInt <= 0)
	{
	return true;
	}
	}
	
	if (otherEntity instanceof EntityDragon && ((EntityDragon)otherEntity).getPhaseManager().getCurrentPhase() instanceof PhaseDying)
	{
	return false;
	}
	
	if ((otherEntity instanceof EntityZombieVillager && ((EntityZombieVillager)otherEntity).isConverting()) || (otherEntity instanceof INpc && !(otherEntity instanceof net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager)) || otherEntity instanceof EntitySnowman || otherEntity instanceof EntityAmbientCreature || otherEntity instanceof EntityWaterMob || otherEntity instanceof EntityAnimal)
	{
	return false;
	}
	
	return true;
	}
	
	protected void despawnEntity()
	{
	if ((isWild()) && (isNonBoss()) && (!(this instanceof EntityPortal)))
	{
	if (isNoDespawnRequired())
	idleTime = 0;
	else
	{
	Entity entity = world.getClosestPlayerToEntity(this, -1.0D);
	
	if (entity != null)
	{
	double d0 = entity.posX - posX;
	double d1 = entity.posY - posY;
	double d2 = entity.posZ - posZ;
	double d3 = d0 * d0 + d1 * d1 + d2 * d2;
	
	if (canDespawn() && d3 > 50000.0D)
	{
	setDead();
	}
	
	if (idleTime > 10000 && rand.nextInt(1000) == 0 && d3 > 2000D && canDespawn())
	{
	setDead();
	}
	else if (d3 < 2000D)
	{
	idleTime = 0;
	}
	}
	}
	}
	}
	
	protected float getSoundVolume()
	{
		return isSneaking() ? 0.1F : 1.0F;
	}
	
	public boolean isEntityInvulnerable(DamageSource source)
	{
	return isWild() && getOwnerId() != null ? true : super.isEntityInvulnerable(source);
	}
	protected float applyPotionDamageCalculations(DamageSource source, float damage)
	{
	damage = super.applyPotionDamageCalculations(source, damage);
	
	if (source.getTrueSource() == this)
	{
	damage = 0.0F;
	}
	
	if (source.isMagicDamage() && isHero())
	{
	damage = (float)((double)damage * 0.25D);
	}
	
	return damage;
	}
	
	/**
	* Reduces damage, depending on armor
	*/
	protected float applyArmorCalculations(DamageSource source, float damage)
	{
	if (!source.isUnblockable())
	{
	damageArmor(damage);
	damage = CombatRules.getDamageAfterAbsorb(damage, (float)getTotalArmorValue(), (float)getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
	}
	else
	{
	damage *= 0.5F;
	}
	
	return damage;
	}
	protected void damageEntity(DamageSource source, float amount)
	{
		if (!isEntityInvulnerable(source))
		{amount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, source, amount);
		Entity entity = source.getTrueSource();
		
		if (amount <= 0) return;
		if (isHero() && (source.getDamageType() == "fall") && amount > 4F)
		amount = 4F;
		
		if (world.getDifficulty() == EnumDifficulty.EASY)
		amount = Math.min(amount / 2.0F + 1.0F, amount);
		
		if (world.getDifficulty() == EnumDifficulty.HARD)
		amount = amount * 3.0F / 2.0F;
		
		if (source.getTrueSource() instanceof net.minecraft.entity.monster.EntityVex)
		amount *= 0.33F;
		if (isHero() && (source.getDamageType() == "chaosImplosion" || source.getDamageType() == "de.GuardianFireball" || source.getDamageType() == "de.GuardianEnergyBall" || source.getDamageType() == "de.GuardianChaosBall"))
		amount *= 0.05F;
		if (isHero() && entity != null)amount *= entity instanceof IMob ? 0.33333334F : 0.75F;
		String s = TextFormatting.getTextWithoutFormattingCodes(getName());
		if (getClass().equals(EntityCow.class) && s != null && s.equals("Bessy"))
		amount *= 0.01F;
		amount = applyArmorCalculations(source, amount);
		amount = applyPotionDamageCalculations(source, amount);
		float f = amount;
		amount = Math.max(amount - getAbsorptionAmount(), 0.0F);
		setAbsorptionAmount(getAbsorptionAmount() - (f - amount));
		amount /= getFittness();
		
		if(getDamageCap() > 0 && amount >= getDamageCap() / 2)
			amount /= height + width;
		if(getDamageCap() > 0 && amount >= getDamageCap())
			amount = getDamageCap();
		if (amount != 0.0F && lastChanceInvul <= 0)
		{
		if (getIllusionFormTime() > 0)
		{
		playSound(ESound.bugSpecial, 1F, 1F);
		spawnExplosionParticle();
		spawnExplosionParticle();
		spawnExplosionParticle();
		spawnExplosionParticle();
		spawnExplosionParticle();
		spawnExplosionParticle();
		spawnExplosionParticle();
		spawnExplosionParticle();
		spawnExplosionParticle();
		spawnExplosionParticle();
		setIllusionFormTime(0);
		}
		++limbSwingAmount;
		setEnergy(getEnergy() - 0.1F);
		float f1 = getHealth();
		setHealth(f1 - amount);
		getCombatTracker().trackDamage(source, f1, amount);
		setAbsorptionAmount(getAbsorptionAmount() - amount);
		setCurrentStudy(EnumStudy.Physical, (int)(amount));
		}
		}
	}
	
	protected void damageEntityTraining(DamageSource source, float amount)
	{
	if (!isEntityInvulnerable(source) && getFakeHealth() > 0F)
	{
	Entity entity = source.getTrueSource();
	
	if (amount <= 0) return;
	
	if (world.getDifficulty() == EnumDifficulty.PEACEFUL)
	amount = 0.0F;
	
	if (world.getDifficulty() == EnumDifficulty.EASY)
	amount *= 0.75F;
	
	if (world.getDifficulty() == EnumDifficulty.HARD)
	amount *= 1.5F;
	
	if (source.getTrueSource() instanceof net.minecraft.entity.monster.EntityVex)
	amount *= 0.33F;
	if (isHero() && entity != null)amount *= entity instanceof IMob ? 0.33333334F : 0.75F;
	
	amount = applyArmorCalculations(source, amount);
	amount = applyPotionDamageCalculations(source, amount);
	float f = amount;
	amount = Math.max(amount - getAbsorptionAmount(), 0.0F);
	setAbsorptionAmount(getAbsorptionAmount() - (f - amount));
	
	if (amount != 0.0F)
	{
	setTotalEXP(getTotalEXP() + amount);
	++limbSwingAmount;
	float f1 = getFakeHealth();
	setFakeHealth(f1 - amount);
	heal(getMaxHealth());
	getCombatTracker().trackDamage(source, f1, amount);
	setAbsorptionAmount(getAbsorptionAmount() - amount);
	setCurrentStudy(EnumStudy.Physical, (int)(amount <= 2 ? 1 : amount / 2));
	if (getFakeHealth() <= 0F)
	{
	ticksExisted = 0;
	lastChanceInvul = 100;
	clearActivePotions();
	heal(getMaxHealth());
	addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 4));
	playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
	setAttackTarget(null);
	if (entity instanceof EntityFriendlyCreature)
	{
	((EntityFriendlyCreature)entity).clearActivePotions();
	((EntityFriendlyCreature)entity).heal(getMaxHealth());
	((EntityFriendlyCreature)entity).setAttackTarget(null);
	}
	if (!isWild())
	{
	incrementConversion((EntityPlayer) getOwner());
	getOwner().sendMessage(new TextComponentTranslation(getName() + " has been defeated in training by " + entity.getName(), new Object[0]));
	}
	}
	}
	}
	}
	
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
	Entity entity = source.getTrueSource();
	if (!isEntityAlive() || this == entity)
	return false;
	if (entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entity).isOnSameTeam((EntityFriendlyCreature)entity) && getFakeHealth() > 0F)
	{
	boolean flag1 = true;
	if ((float)hurtResistantTime > (float)maxHurtResistantTime / 2.0F)
	{
	damageEntityTraining(source, amount - lastDamage);
	flag1 = false;
	}
	else
	{
	lastDamage = amount;
	hurtResistantTime = maxHurtResistantTime;
	damageEntityTraining(source, amount);
	maxHurtTime = 10;
	hurtTime = maxHurtTime;
	}
	if (flag1)
	{
	if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
	{
	world.setEntityState(this, (byte)33);
	}
	else
	{
	world.setEntityState(this, (byte)2);
	}
	
	
	double d1 = entity.posX - posX;
	double d0;
	
	for (d0 = entity.posZ - posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
	{
	d1 = (Math.random() - Math.random()) * 0.01D;
	}
	
	attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * (180D / Math.PI) - (double)rotationYaw);
	knockBack(entity, 0.4F, d1, d0);
	if (entity instanceof EntityLivingBase)
	setAttackTarget((EntityLivingBase) entity);
	}
	
	playHurtSound(source);
	}
	else if (entity instanceof EntityFriendlyCreature && isOnSameTeam((EntityFriendlyCreature)entity))
	return false;
	if (getFakeHealth() <= 0F && entity != null && entity instanceof EntityFriendlyCreature && isOnSameTeam((EntityFriendlyCreature)entity))
	{
	((EntityFriendlyCreature)entity).setAttackTarget(null);
	}
	if (!net.minecraftforge.common.ForgeHooks.onLivingAttack(this, source, amount)) return false;
	
	if (getOwner() != null && entity == getOwner())
	{
	if (this instanceof EntityChicken)
	((EntityChicken)this).timeUntilNextEgg -= 40;
	moralRaisedTimer += 40;
	prevChasingPosX = chasingPosX = posX + (MathHelper.sin(renderYawOffset * 0.017453292F) * 6D);
	prevChasingPosY = chasingPosY = posY + getEyeHeight();
	prevChasingPosZ = chasingPosZ = posX - (MathHelper.cos(renderYawOffset * 0.017453292F) * 6D);
	distanceWalkedModified = 0;
	setAttackTarget(null);
	setRevengeTarget(null);
	getNavigator().clearPath();
	getNavigator().tryMoveToEntityLiving(this, 0D);
	if (!world.isRemote && getArrowCountInEntity() > 0)
	{
	setArrowCountInEntity(getArrowCountInEntity() - 1);
	attackEntityFrom(DamageSource.CACTUS, 1F);
	playSound(SoundEvents.ENCHANT_THORNS_HIT, getSoundVolume(), getSoundPitch());
	EntityArrow entityarrow =new EntityTippedArrow(world, this);
	entityarrow.copyLocationAndAnglesFrom(this);
	entityarrow.posY = posY + getEyeHeight();
	entityarrow.shoot(this, -30F, rand.nextFloat() * 360F, 0.0F, 0.35F, 1.0F);
	entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
	world.spawnEntity(entityarrow);
	}
	}
	if (
	(isEntityInvulnerable(source)) ||
	(amount == 0.0F) ||
	(!takesFallDamage() && source.getDamageType() == "fall") ||
	(isImmuneToExplosions() && source.isExplosion()) ||(isHero() && (source.getDamageType() == "sulphuric_acid" || source.getDamageType() == "thermal" || source.getDamageType() == "oxygen_suffocation" || source.getDamageType() == "wither" || source.getDamageType() == "inFire" || source.getDamageType() == "onFire" || source.getDamageType() == "lava" || source.getDamageType() == "hotFloor" || source.getDamageType() == "magic" || source.getDamageType() == "indirectMagic")) ||
	(source.isFireDamage() && (isPotionActive(MobEffects.FIRE_RESISTANCE) || isImmuneToFire())) ||
	(isEntityImmuneToCoralium() && source.getDamageType() == "coralium") ||
	(isEntityImmuneToDread() && source.getDamageType() == "dread") ||
	(isEntityImmuneToAntiMatter() && source.getDamageType() == "antimatter") ||
	(isEntityImmuneToDarkness() && source.getDamageType() == "shadow") ||
	(entity != null && entity instanceof EntityLivingBase && isOnSameTeam((EntityLivingBase)entity) && !world.getGameRules().getBoolean("friendlyFire")) ||
	(getTier().ordinal() > EnumTier.TIER5.ordinal() && (source.getDamageType() == "sulphuric_acid" || source.getDamageType() == "thermal" || source.getDamageType() == "oxygen_suffocation" || source.getDamageType() == "wither" || source.getDamageType() == "inFire" || source.getDamageType() == "onFire" || source.getDamageType() == "lava" || source.getDamageType() == "hotFloor" || source.getDamageType() == "chaosImplosion")) ||
	(source == DamageSource.CRAMMING)
	)return false;
	else
	{
	setSitResting(false);
	
	if (!world.isRemote && getAttackTarget() == null && entity != null && entity instanceof EntityLivingBase && !isOnSameTeam((EntityLivingBase)entity) && !source.isExplosion())
	setAttackTarget((EntityLivingBase)entity);
	if (hurtResistantTime <= 1)
	if (source.isProjectile())playSound(getPierceHurtSound(), 3.0F, 1.0F);
	else if ((amount >= 7.0F) || (source.isExplosion()) || (source.isDamageAbsolute()) || (source.isUnblockable()) || (source == DamageSource.ANVIL) || (source.canHarmInCreative()) || (source.isMagicDamage()) || (source == DamageSource.LAVA))
	playSound(getCrushHurtSound(), 3.0F, 1.0F);
	else playSound(getRegularHurtSound(), 3.0F, 1.0F);

	setTotalEXP(getTotalEXP() + amount);
	setCurrentStudy(EnumStudy.Combative, (int)amount);
	
	return super.attackEntityFrom(source, amount);
	}
	}
	
	public int getDamageCap()
	{
	return 0;
	}
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio)
	{
	if (rand.nextDouble() >= (isEntityAlive() || rand.nextInt(200) != 0 ? getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue() : 0D))
	{
	isAirBorne = true;
	motionY += (double)strength;
	getNavigator().clearPath();
	prevRenderYawOffset = prevRotationYaw = prevRotationYawHead = renderYawOffset = rotationYaw = rotationYawHead = (float)MathHelper.atan2(motionZ, motionX) * (180F / (float)Math.PI) - 90.0F;
	if (!isEntityAlive() && strength >= 1)
	strength *= 2;
	float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
	motionX /= 2.0D;
	motionZ /= 2.0D;
	motionX -= xRatio / (double)f * (double)strength;
	motionZ -= zRatio / (double)f * (double)strength;
	}
	}
	public void cleave(int lootingModifier, DamageSource source)
	{
	if (!world.isRemote)
	{
	recentlyHit = 100;
	for (int ai = 0; ai <= lootingModifier; ++ai)
	{
	EntityFriendlyCreature addon = spawnBaby(this);
	addon.copyLocationAndAnglesFrom(this);
	world.spawnEntity(addon);
	addon.attackEntityFrom(DamageSource.STARVE, addon.getMaxHealth());
	addon.setHealth(0F);
	addon.setDead();
	}
	attackEntityFrom(DamageSource.STARVE, getMaxHealth());
	playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
	setHealth(0F);
	}
	}
	
	protected float getStrengthMultiplier()
	{
	return 1F;
	}
	
	public void attackWithAdditionalEffects(Entity entity)
	{
	}
	
	public boolean attackEntityAsMob(Entity entity)
	{
	if (!isEntityAlive())
	return false;
	
	if (this instanceof EntityEnderman && ((EntityEnderman)this).canDodgeAllAttacks())
	entity.hurtResistantTime = 0;
	
	float f = (float)getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * ((isSneaking() || isInvisible() || !canEntityBeSeen(entity)) && entity instanceof EntityLiving && ((EntityLiving)entity).getAttackTarget() != this ? 3 : 1) * (isHero() ? (entity instanceof IMob ? 3F : 1.5F) : 1F) * getStrengthMultiplier();
	int i = 0;
	
	
	f *= (getStrength() + 50F) / 100F;
	
	if ((entity instanceof EntityFriendlyCreature && isOnSameTeam((EntityFriendlyCreature)entity) && getFakeHealth() > 0F))
	{
	setCurrentStudy(EnumStudy.Combative, (int)(f <= 2 ? 1 : f / 2));
	entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
	((EntityLivingBase)entity).knockBack(this, i * 0.5F + 0.3F, MathHelper.sin(rotationYaw * 0.017453292F), -MathHelper.cos(rotationYaw * 0.017453292F));
	}
	if (entity instanceof EntityLivingBase && isOnSameTeam((EntityLivingBase)entity) && !world.getGameRules().getBoolean("friendlyFire"))
	{
	return false;
	}
	else
	{
	
	if (entity instanceof EntityAmbientCreature)
	{
	entity.startRiding(this);
	playSound(SoundEvents.ENTITY_PLAYER_BURP, 1F, 1.5F);
	heal(2F);
	}
	setSitResting(false);
	ReflectionUtil.set(EntityLivingBase.class, entity, "recentlyHit", "field_70718_bc", 100);
	if (isASwarmingMob())
	{
	if (entity != null)
	{
	List<EntityFriendlyCreature> allies = world.getEntitiesWithinAABB(getClass(), getEntityBoundingBox().grow(3D));
	
	if(!world.isRemote)
	if(!allies.isEmpty())
	{
	for (int i1 = 0; i1 < allies.size(); i1++)
	{
	EntityFriendlyCreature entities = (EntityFriendlyCreature)allies.get(i1);
	
	if (entities.isEntityAlive() && isOnSameTeam(entities) && entities.isASwarmingMob() && entities.getClass() == getClass())
	{
	f += 0.1F;
	}
	}
	}
	}
	}
	IAttributeInstance bonus = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	
	AttributeModifier vslight = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D831"), "Light Bonus", getBonusVSLight(), 1).setSaved(false);
	AttributeModifier vsarmored = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D832"), "Armored Bonus", getBonusVSArmored(), 1).setSaved(false);
	AttributeModifier vsmassive = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D833"), "Massive Bonus", getBonusVSMassive(), 1).setSaved(false);
	AttributeModifier vsflying = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D834"), "Flying Bonus", getBonusVSFlying(), 1).setSaved(false);
	AttributeModifier vstiny = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D835"), "Tiny Bonus", getBonusVSTiny(), 1).setSaved(false);
	AttributeModifier vsender = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D836"), "Ender Bonus", getBonusVSEnder(), 1).setSaved(false);
	AttributeModifier vselemental = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D837"), "Elemental Bonus", getBonusVSElemental(), 1).setSaved(false);
	AttributeModifier vsstructure = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D838"), "Structure Bonus", getBonusVSStructure(), 1).setSaved(false);
	AttributeModifier vsundead = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D839"), "Undead Bonus", getBonusVSUndead(), 1).setSaved(false);
	AttributeModifier vsanimal = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D840"), "Animal Bonus", getBonusVSAnimal(), 1).setSaved(false);
	
	if (entity instanceof EntityLivingBase)
	{
	if (entity instanceof Light)
	{
	if (!bonus.hasModifier(vslight))
	bonus.applyModifier(vslight);
	}
	
	if (entity instanceof Armored)
	{
	if (!bonus.hasModifier(vsarmored))
	bonus.applyModifier(vsarmored);
	}
	
	if (entity instanceof Flying)
	{
	if (!bonus.hasModifier(vsflying))
	bonus.applyModifier(vsflying);
	}
	
	if (entity instanceof Massive)
	{
	if (!bonus.hasModifier(vsmassive))
	bonus.applyModifier(vsmassive);
	}
	
	if (entity instanceof Tiny)
	{
	if (!bonus.hasModifier(vstiny))
	bonus.applyModifier(vstiny);
	}
	
	if (entity instanceof Elemental)
	{
	if (!bonus.hasModifier(vselemental))
	bonus.applyModifier(vselemental);
	}
	
	if (entity instanceof Structure)
	{
	if (!bonus.hasModifier(vsstructure))
	bonus.applyModifier(vsstructure);
	}
	
	if (entity instanceof Undead)
	{
	if (!bonus.hasModifier(vsundead))
	bonus.applyModifier(vsundead);
	}
	
	if (entity instanceof Ender)
	{
	if (!bonus.hasModifier(vsender))
	bonus.applyModifier(vsender);
	}
	
	if (entity instanceof Animal)
	{
	if (!bonus.hasModifier(vsanimal))
	bonus.applyModifier(vsanimal);
	}
	}
	
	AttributeModifier summondebuff = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D855"), "Summon Debuff", 0.5F, 1).setSaved(false);
	if (hasLimitedLife() && !bonus.hasModifier(summondebuff))
	bonus.applyModifier(summondebuff);
	
	if (this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman && ((net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman)this).andr)
	{
	i += 60;
	f *= 10000F;
	if ((entity instanceof EntityLivingBase))
	{
	entity.hurtResistantTime = 0;
	entity.motionY = (height * 0.25D);
	((EntityLivingBase)entity).knockBack(this, i * 0.5F + 0.3F, MathHelper.sin(rotationYaw * 0.017453292F), -MathHelper.cos(rotationYaw * 0.017453292F));
	}
	if (!world.isRemote)createEngenderModExplosionFireless(this, entity.posX, entity.posY, entity.posZ, 7F + entity.height + entity.width, false);
	}
	
	if (this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast && ((net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast)this).eleanor)
	{
	i += 30;
	f *= 1000F;
	if ((entity instanceof EntityLivingBase))
	{
	entity.hurtResistantTime = 0;
	entity.motionY = (height * 0.25D);
	((EntityLivingBase)entity).knockBack(this, i * 0.5F + 0.3F, MathHelper.sin(rotationYaw * 0.017453292F), -MathHelper.cos(rotationYaw * 0.017453292F));
	}
	if (!world.isRemote)createEngenderModExplosionFireless(this, entity.posX, entity.posY, entity.posZ, 7F + entity.height + entity.width, false);
	}
	
	if (isSneaking() && entity instanceof EntityLiving && ((EntityLiving)entity).getAttackTarget() != this)
	i += 4;
	if ((entity instanceof EntityLivingBase))
	{
	f += EnchantmentHelper.getModifierForCreature(getHeldItemMainhand(), ((EntityLivingBase)entity).getCreatureAttribute());
	f += EnchantmentHelper.getModifierForCreature(getHeldItemOffhand(), ((EntityLivingBase)entity).getCreatureAttribute());
	i += EnchantmentHelper.getKnockbackModifier(this);
	
	if (this instanceof EntityWitherStormHead)
	{
	i += 3;
	double d2 = entity.posX - posX;
	double d3 = entity.posZ - posZ;
	double d4 = d2 * d2 + d3 * d3;
	entity.motionX += d2 / d4 * 2;
	entity.motionY += 0.5D;
	entity.motionZ += d3 / d4 * 2;
	}
	if (this instanceof EntityEnderDragon)
	{
	i += 4;
	double d2 = entity.posX - posX;
	double d3 = entity.posZ - posZ;
	double d4 = d2 * d2 + d3 * d3;
	entity.motionX += d2 / d4 * 2;
	entity.motionY += 0.5D;
	entity.motionZ += d3 / d4 * 2;
	}
	
	if (this instanceof EntityWitherStormTentacle)
	{
	i += 9;
	double d2 = entity.posX - posX;
	double d3 = entity.posZ - posZ;
	double d4 = d2 * d2 + d3 * d3;
	entity.motionX += d2 / d4 * 4;
	entity.motionY += 0.5D;
	entity.motionZ += d3 / d4 * 4;
	}
	
	if (this instanceof EntityWitherStormTentacleDevourer)
	{
	i += 12;
	double d2 = entity.posX - posX;
	double d3 = entity.posZ - posZ;
	double d4 = d2 * d2 + d3 * d3;
	entity.motionX += d2 / d4 * 6;
	entity.motionY += 0.5D;
	entity.motionZ += d3 / d4 * 6;
	}
	}
	
	if (getEntityAttribute(AGILITY).getBaseValue() >= rand.nextDouble() * 10000D)
	{
	entity.hurtResistantTime = 0;
	f *= 10F;
	playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 5F, 1F);
	if (EngenderConfig.general.useMessage && !isWild())
	getOwner().sendMessage(new TextComponentTranslation(getName() + " got a critical hit!", new Object[0]));
	}
	
	boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
	
	if (this instanceof EntityPortal)
	flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this).setDamageBypassesArmor(), f);
	
	if (entity instanceof EntityLivingBase && !(entity instanceof EntityFriendlyCreature) && getCreatureAttribute() == ((EntityLivingBase)entity).getCreatureAttribute() && ((EntityLivingBase)entity).getCreatureAttribute() != EnumCreatureAttribute.UNDEFINED)
	flag = entity.attackEntityFrom(!isWild() ? DamageSource.causePlayerDamage((EntityPlayer) getOwner()) : new DamageSource("generic"), f);
	
	if (entity instanceof EntityPlayer && world.getDifficulty() == EnumDifficulty.PEACEFUL)
	flag = ((EntityPlayer)entity).attackEntityFrom(DamageSource.GENERIC, f);
	
	
	if (entity instanceof IEntityMultiPart)
	{
	i = 0;
	
	if (entity != null)
	{
	Entity[] aentity = entity.getParts();
	
	if (aentity != null)
	{
	for (Entity parts : aentity)
	if (parts instanceof MultiPartEntityPart)
	{
	flag = ((IEntityMultiPart)entity).attackEntityFromPart((MultiPartEntityPart)parts, entity instanceof EntityFriendlyCreature ? DamageSource.causeMobDamage(this) : DamageSource.causePlayerDamage(isWild() ? world.getClosestPlayerToEntity(this, -1D) : (EntityPlayer)getOwner()), f);
	}
	}
	}
	}
	
	if (entity instanceof EntityLivingBase && (!isOnSameTeam((EntityLivingBase)entity)|| (entity instanceof EntityFriendlyCreature && isOnSameTeam((EntityFriendlyCreature)entity) && getFakeHealth() <= 0F)) && !world.getGameRules().getBoolean("friendlyFire"))
	{
	flag = false;
	}
	
	if (flag && entity != null)
	{
	if (!(this instanceof IEntityMultiPart))
	{
	faceEntity(entity, 180F, getVerticalFaceSpeed());
	renderYawOffset = rotationYaw = rotationYawHead;
	}
	if (this instanceof EntitySlime)
	playSound(SoundEvents.ENTITY_SLIME_ATTACK, getSoundVolume(), getSoundPitch());
	
	EntityPlayer player = world.getClosestPlayerToEntity(this, 16D);
	
	if (isHero())
	{
	entity.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1F, getSoundPitch());
	if (player != null)
	{
	player.onCriticalHit(entity);
	}
	
	if (entity instanceof IMob)
	{
	entity.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 1F, getSoundPitch());
	if (player != null)
	{
	player.onEnchantmentCritical(entity);
	}
	}
	}
	}
	
	if ((entity instanceof EntityPlayer))
	{
	EntityPlayer entityplayer = (EntityPlayer)entity;
	ItemStack itemstack = getHeldItemMainhand();
	ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : null;
	if ((this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian || !isNonBoss()) && (itemstack1 != null && itemstack1.getItem() == Items.SHIELD))
	{
	entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
	world.setEntityState(entityplayer, (byte)30);
	}
	
	if (((itemstack != null && itemstack.getItem() instanceof ItemAxe)) && (itemstack1 != null && itemstack1.getItem() == Items.SHIELD))
	{
	float f1 = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
	if (rand.nextFloat() < f1)
	{
	entityplayer.getCooldownTracker().setCooldown(Items.SHIELD, 100);
	world.setEntityState(entityplayer, (byte)30);
	}
	}
	}
	if (isOnSameTeam(entity))
	setAttackTarget(null);
	
	if ((entity instanceof EntityLivingBase || (entity instanceof EntityPlayer && !((EntityPlayer)entity).capabilities.disableDamage)) && ((!isOnSameTeam((EntityLivingBase)entity)|| (entity instanceof EntityFriendlyCreature && isOnSameTeam((EntityFriendlyCreature)entity) && getFakeHealth() <= 0F))) && (entity != this || (!isWild() && entity != getOwner())))
	{
	swingArm(EnumHand.MAIN_HAND);
	setCurrentStudy(EnumStudy.Combative, (int)f);
	applyEnchantments(this, entity);
	if ((entity instanceof EntityLivingBase && !((EntityLivingBase)entity).isNonBoss() && ((EntityLivingBase)entity).getHealth() <= 1F))
	((EntityLivingBase)entity).setHealth(0F);
	
	if (this instanceof net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex)
	{
	((net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex)this).setIsCharging(false);
	}
	
	++((EntityLivingBase)entity).limbSwingAmount;
	if (((EntityLivingBase)entity).getHealth() > 1F)
	((EntityLivingBase)entity).setHealth(((EntityLivingBase)entity).getHealth() - (isHero() ? 0.03F : 0.01F));
	
	if (entity.isEntityInvulnerable(DamageSource.causeMobDamage(this)) && f >= 6.0F)
	{
	((EntityLivingBase)entity).setHealth(((EntityLivingBase)entity).getHealth() - f);
	if (EngenderMod.isWoodLikeMob(entity))
	{
	entity.playSound(ESound.woodHitCrush, 2F, 1.0F);
	}
	else if (EngenderMod.isMetalLikeMob(entity))
	{
	entity.playSound(ESound.metalHitCrush, 2F, 1.0F);
	}
	else
	{
	if (entity.height >= 5.0F)
	entity.playSound(ESound.fleshHitCrushHeavy, 2F, 1.0F);
	else
	entity.playSound(ESound.fleshHitCrush, 2F, 1.0F);
	}
	if (((EntityLivingBase)entity).getHealth() <= 0F)
	((EntityLivingBase)entity).onDeath(DamageSource.causeMobDamage(this));
	}
	}
	
	if (entity instanceof EntityLivingBase && (entity != this || (!isWild() && entity != getOwner())) && flag)
	{
	((EntityLivingBase)entity).knockBack(this, i * 0.5F + 0.3F, MathHelper.sin(rotationYaw * 0.017453292F), -MathHelper.cos(rotationYaw * 0.017453292F));
	if (entity instanceof EntityPlayerMP)
	((EntityPlayerMP)entity).connection.sendPacket(new SPacketEntityVelocity((EntityPlayerMP)entity));
	int j = EnchantmentHelper.getFireAspectModifier(this);
	if (j > 0)
	{
	entity.setFire(j * 4);
	}
	if (!getHeldItemMainhand().isEmpty() && getHeldItemMainhand().getItem() != null && !isWild())
	getHeldItemMainhand().hitEntity((EntityLivingBase)entity, (EntityPlayer) getOwner());
	
	if (!getHeldItemOffhand().isEmpty() && getHeldItemOffhand().getItem() != null && !isWild())
	getHeldItemOffhand().hitEntity((EntityLivingBase)entity, (EntityPlayer) getOwner());
	
	if (!getHeldItemMainhand().isEmpty() && getHeldItemMainhand().getItem() != null && this instanceof EntityPigZombie && getHeldItemMainhand().getItem() == Items.GOLDEN_SWORD)
	getHeldItemMainhand().setItemDamage(0);
	
	if (!getHeldItemOffhand().isEmpty() && getHeldItemOffhand().getItem() != null && this instanceof EntityPigZombie && getHeldItemOffhand().getItem() == Items.GOLDEN_SWORD)
	getHeldItemOffhand().setItemDamage(0);
	
	if (!getHeldItemMainhand().isEmpty() && getHeldItemMainhand().getItem() != null && this instanceof EntityVex && getHeldItemMainhand().getItem() == Items.IRON_SWORD)
	getHeldItemMainhand().setItemDamage(0);
	
	if (!getHeldItemOffhand().isEmpty() && getHeldItemOffhand().getItem() != null && this instanceof EntityVex && getHeldItemOffhand().getItem() == Items.IRON_SWORD)
	getHeldItemOffhand().setItemDamage(0);
	
	applyEnchantments(this, entity);
	}
	
	setEnergy(getEnergy() - 0.25F);
	
	if (EngenderConfig.general.useMessage && !entity.isEntityAlive() && !isWild())
	{
	boolean flag1 = (boolean) ReflectionUtil.get(EntityLivingBase.class, entity, "dead", "field_70729_aU");
	
	if (!flag1)
	{
	if (entity instanceof EntityLivingBase)
	setCurrentStudy(EnumStudy.Combative, (int)((EntityLivingBase)entity).getMaxHealth());
	getOwner().sendMessage(new TextComponentTranslation(entity.getName() + " was " + (isInvisible() ? "ambushed" : (isSneaking() ? "assassinated" : "slain")) + " by " + getName() + " (" + getOwner().getName() + ")", new Object[0]));
	}
	}
	
	if (bonus.hasModifier(vslight))
	bonus.removeModifier(vslight);
	if (bonus.hasModifier(vsarmored))
	bonus.removeModifier(vsarmored);
	if (bonus.hasModifier(vsmassive))
	bonus.removeModifier(vsmassive);
	if (bonus.hasModifier(vsflying))
	bonus.removeModifier(vsflying);
	if (bonus.hasModifier(vstiny))
	bonus.removeModifier(vstiny);
	if (bonus.hasModifier(vselemental))
	bonus.removeModifier(vselemental);
	if (bonus.hasModifier(vsstructure))
	bonus.removeModifier(vsstructure);
	if (bonus.hasModifier(vsundead))
	bonus.removeModifier(vsundead);
	if (bonus.hasModifier(vsender))
	bonus.removeModifier(vsender);
	if (bonus.hasModifier(vsanimal))
	bonus.removeModifier(vsanimal);
	
	if (bonus.hasModifier(summondebuff))
	bonus.removeModifier(summondebuff);
	setTotalEXP(getTotalEXP() + f);
	return true;
	}
	}
	
	public void setAttackTarget(@Nullable EntityLivingBase entitylivingbaseIn)
	{
	if (!attackable())
	entitylivingbaseIn = null;
	
	if (entitylivingbaseIn != null)
	setSitResting(false);
	
	super.setAttackTarget(entitylivingbaseIn);
	}
	
	public void fireLightning(Entity entity, double x, double y, double z)
	{
	if (entity != null && entity.isEntityAlive())
	{
	double d3 = x;
	double d4 = y;
	double d5 = z;
	double d6 = entity.posX - d3;
	double d7 = entity.posY - d4;
	double d8 = entity.posZ - d5;
	EntityPortalLightning entitywitherskull = new EntityPortalLightning(world, entity, this, d6, d7, d8);
	entitywitherskull.posY = d4;
	entitywitherskull.posX = d3;
	entitywitherskull.posZ = d5;
	entitywitherskull.accelerationY = d4;
	entitywitherskull.accelerationX = d3;
	entitywitherskull.accelerationZ = d5;
	entitywitherskull.targetEntity = entity;
	world.spawnEntity(entitywitherskull);
	}
	}
	
	public void inflictCustomStatusEffect(EnumDifficulty scaling, EntityLivingBase entity, Potion effect, int time, int power)
	{
	if ((scaling == EnumDifficulty.PEACEFUL || isOnSameTeam(entity)) && effect.isBadEffect())
	return;
	if (scaling == EnumDifficulty.NORMAL)
	time *= 2;
	if (scaling == EnumDifficulty.HARD)
	time *= 5;
	if (time > 0)
	entity.addPotionEffect(new PotionEffect(effect, time * 20, power - 1 + scaling.getDifficultyId()));
	}
	public void inflictEngenderMobDamage(EntityLivingBase entity, String killmessage, DamageSource attacktype, float damage)
	{
	
	IAttributeInstance bonus = getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
	
	AttributeModifier vslight = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D831"), "Light Bonus", getBonusVSLight(), 1).setSaved(false);
	AttributeModifier vsarmored = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D832"), "Armored Bonus", getBonusVSArmored(), 1).setSaved(false);
	AttributeModifier vsmassive = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D833"), "Massive Bonus", getBonusVSMassive(), 1).setSaved(false);
	AttributeModifier vsflying = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D834"), "Flying Bonus", getBonusVSFlying(), 1).setSaved(false);
	AttributeModifier vstiny = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D835"), "Tiny Bonus", getBonusVSTiny(), 1).setSaved(false);
	AttributeModifier vsender = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D836"), "Ender Bonus", getBonusVSEnder(), 1).setSaved(false);
	AttributeModifier vselemental = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D837"), "Elemental Bonus", getBonusVSElemental(), 1).setSaved(false);
	AttributeModifier vsstructure = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D838"), "Structure Bonus", getBonusVSStructure(), 1).setSaved(false);
	AttributeModifier vsundead = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D839"), "Undead Bonus", getBonusVSUndead(), 1).setSaved(false);
	AttributeModifier vsanimal = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D840"), "Animal Bonus", getBonusVSAnimal(), 1).setSaved(false);
	
	if (entity instanceof EntityLivingBase)
	{
	if (entity instanceof Light)
	{
	if (!bonus.hasModifier(vslight))
	bonus.applyModifier(vslight);
	}
	if (entity instanceof Armored)
	{
	if (!bonus.hasModifier(vsarmored))
	bonus.applyModifier(vsarmored);
	}
	if (entity instanceof Flying)
	{
	if (!bonus.hasModifier(vsflying))
	bonus.applyModifier(vsflying);
	}
	if (entity instanceof Massive)
	{
	if (!bonus.hasModifier(vsmassive))
	bonus.applyModifier(vsmassive);
	}
	if (entity instanceof Tiny)
	{
	if (!bonus.hasModifier(vstiny))
	bonus.applyModifier(vstiny);
	}
	if (entity instanceof Elemental)
	{
	if (!bonus.hasModifier(vselemental))
	bonus.applyModifier(vselemental);
	}
	if (entity instanceof Structure)
	{
	if (!bonus.hasModifier(vsstructure))
	bonus.applyModifier(vsstructure);
	}
	if (entity instanceof Undead)
	{
	if (!bonus.hasModifier(vsundead))
	bonus.applyModifier(vsundead);
	}
	if (entity instanceof Ender)
	{
	if (!bonus.hasModifier(vsender))
	bonus.applyModifier(vsender);
	}
	if (entity instanceof Animal)
	{
	if (!bonus.hasModifier(vsanimal))
	bonus.applyModifier(vsanimal);
	}
	}
	AttributeModifier summondebuff = new AttributeModifier(UUID.fromString("B9766B59-8566-4402-BC1F-3EE2A276D855"), "Summon Debuff", 0.5F, 1).setSaved(false);
	if (hasLimitedLife() && !bonus.hasModifier(summondebuff))
	bonus.applyModifier(summondebuff);
	if (!isOnSameTeam(entity) || (entity instanceof EntityFriendlyCreature && isOnSameTeam((EntityFriendlyCreature)entity) && getFakeHealth() > 0F))
	{
	++entity.limbSwingAmount;
	damage *= ((isSneaking() || isInvisible() || !canEntityBeSeen(entity)) && entity instanceof EntityLiving && ((EntityLiving)entity).getAttackTarget() != this ? 3 : 1) * (isHero() ? (entity instanceof IMob ? 3F : 1.5F) : 1F);
	
	ReflectionUtil.set(EntityLivingBase.class, entity, "recentlyHit", "field_70718_bc", 100);
	
	if (attacktype.getTrueSource() != null && !(attacktype instanceof EntityDamageSourceIndirect) && getEntityAttribute(AGILITY).getBaseValue() >= rand.nextDouble() * 10000D)
	{
	entity.hurtResistantTime = 0;
	damage *= 10F;
	playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 5F, 1F);
	if (EngenderConfig.general.useMessage && !isWild())
	getOwner().sendMessage(new TextComponentTranslation(getName() + " got a critical hit!", new Object[0]));
	}
	
	damage *= getStrengthMultiplier();
	if (entity.isEntityAlive())
	{
	if (entity instanceof IEntityMultiPart)
	{
	if (entity != null)
	{
	Entity[] aentity = entity.getParts();
	
	if (aentity != null)
	{
	Entity mob = aentity[rand.nextInt(entity.getParts().length)];
	if (mob instanceof MultiPartEntityPart)
	{
	((IEntityMultiPart)entity).attackEntityFromPart((MultiPartEntityPart)mob, entity instanceof EntityFriendlyCreature ? DamageSource.causeMobDamage(this) : DamageSource.causePlayerDamage(isWild() ? world.getClosestPlayerToEntity(this, -1D) : (EntityPlayer)getOwner()), damage);
	}
	}
	}
	}
	else
	entity.attackEntityFrom(attacktype, damage);
	if (entity.isEntityInvulnerable(attacktype) && (damage >= 6.0F || attacktype.isExplosion() || attacktype.isDamageAbsolute() || attacktype.isUnblockable() || attacktype == DamageSource.ANVIL || attacktype.canHarmInCreative() || (attacktype.isMagicDamage()) || attacktype == DamageSource.LAVA))
	{
	entity.setHealth(entity.getHealth() - damage);
	if (EngenderMod.isWoodLikeMob(entity))
	{
	entity.playSound(ESound.woodHitCrush, 2F, 1.0F);
	}
	else if (EngenderMod.isMetalLikeMob(entity))
	{
	entity.playSound(ESound.metalHitCrush, 2F, 1.0F);
	}
	else
	{
	if (entity.height >= 5.0F)
	entity.playSound(ESound.fleshHitCrushHeavy, 2F, 1.0F);
	else
	entity.playSound(ESound.fleshHitCrush, 2F, 1.0F);
	}
	if (entity.getHealth() <= 0F)
	entity.onDeath(attacktype);
	}
	--entity.hurtResistantTime;
	if (isBoss() && entity.isNonBoss())
	entity.hurtResistantTime = 0;
	setCurrentStudy(EnumStudy.Combative, (int)(damage / 4));
	
	++entity.limbSwingAmount;
	if (entity.getHealth() > 1F)
	entity.setHealth(entity.getHealth() - (isHero() ? 0.03F : 0.01F));
	if (EngenderConfig.general.useMessage && !entity.isEntityAlive() && !isWild() && entity.getHealth() <= 0F)
	{
	boolean flag1 = (boolean) ReflectionUtil.get(EntityLivingBase.class, entity, "dead", "field_70729_aU");

	if (!flag1)
	{
	setCurrentStudy(EnumStudy.Combative, (int)(entity.getMaxHealth()));
	getOwner().sendMessage(new TextComponentTranslation(entity.getName() + killmessage + getName() + " (" + getOwner().getName() + ")", new Object[0]));
	}
	}
	if (bonus.hasModifier(vslight))
	bonus.removeModifier(vslight);
	if (bonus.hasModifier(vsarmored))
	bonus.removeModifier(vsarmored);
	if (bonus.hasModifier(vsmassive))
	bonus.removeModifier(vsmassive);
	if (bonus.hasModifier(vsflying))
	bonus.removeModifier(vsflying);
	if (bonus.hasModifier(vstiny))
	bonus.removeModifier(vstiny);
	if (bonus.hasModifier(vselemental))
	bonus.removeModifier(vselemental);
	if (bonus.hasModifier(vsstructure))
	bonus.removeModifier(vsstructure);
	if (bonus.hasModifier(vsundead))
	bonus.removeModifier(vsundead);
	if (bonus.hasModifier(vsender))
	bonus.removeModifier(vsender);
	if (bonus.hasModifier(vsanimal))
	bonus.removeModifier(vsanimal);
	if (bonus.hasModifier(summondebuff))
	bonus.removeModifier(summondebuff);
	}
	}
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRender3d(double x, double y, double z)
	{
	return true;
	}
	
	protected final void setSize(float width, float height)
	{
	if (ticksExisted >= 1 && getTier().ordinal() < EnumTier.TIER6.ordinal())
	{
		float fit = (float) getFittness();
		width *= fit;
		height *= fit;
	}
	
	if (isChild())
	{
		width *= 0.5F;
		height *= 0.5F;
	}
	
	reachWidth = width;
	
	if (this.width != width || this.height != height)
	{
		float f = width;
		this.width = width;
		this.height = height;
		
		if (this.width < f)
		{
			double d0 = (double)this.width / 2.0D;
			setEntityBoundingBox(new AxisAlignedBB(posX - d0, posY, posZ - d0, posX + d0, posY + (double)this.height, posZ + d0));
			return;
		}
		
		AxisAlignedBB axisalignedbb = getEntityBoundingBox();
		setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)this.width, axisalignedbb.minY + (double)this.height, axisalignedbb.minZ + (double)this.width));
	}
	}
	
	public int getGrowingAge()
	{
	return ((Integer)dataManager.get(AGE)).intValue();
	}
	
	public void setGrowingAge(int age)
	{
		dataManager.set(AGE, Integer.valueOf(age));
		if (age < 0 && !isChild())
			setChild(true);
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void setLevel(int level)
	{
		setTotalEXP(calculateExperience(Math.min(level, EngenderConfig.mobs.maxLevel - 1)));
	}
	
	public void setLevel()
	{
		float totalExp = getTotalEXP();
		int newLevel = (int) Math.min(calculateLevel(totalExp), EngenderConfig.mobs.maxLevel - 1);
		currentEXP = Math.max(totalExp - calculateExperience(level), 0.0F);
		
		if (!world.isRemote && !isWild() && newLevel > level)
		{
			level = newLevel;
			levelUp(false);
		}
		level = newLevel;
		
		
		this.neededEXP = calculateExperience(level + 1) - calculateExperience(level);
	}
	
	public float getEXP()
	{
		return currentEXP;
	}
	
	public float getTotalEXP()
	{
		return ((Float)dataManager.get(TOTALEXP)).floatValue();
	}
	
	public void setTotalEXP(float exp)
	{
		dataManager.set(TOTALEXP, Float.valueOf(exp));
		setLevel();
	}
	
	public float getNextLevelRequirement()
	{
		return neededEXP;
	}
	
	public boolean isImmuneToExplosions()
	{
	return ticksExisted <= 20 || lastChanceInvul > 0 || isHero() || isAIDisabled();
	}
	
	public boolean takesFallDamage()
	{
	return !isBeingRidden() || getFakeHealth() > 0 || lastChanceInvul > 0 || this instanceof Flying || this instanceof Massive;
	}
	
	public Entity changeDimension(int dimensionIn)
	{
	if (ticksExisted < 400) return null;
	return super.changeDimension(dimensionIn);
	}
	
	protected void updateAITasks()
	{
	if (!world.isRemote && getAttackTarget() == null)
	{
	List<EntityFriendlyCreature> list = world.<EntityFriendlyCreature>getEntitiesWithinAABB(EntityFriendlyCreature.class, getEntityBoundingBox().grow(getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()), Predicates.and(EntitySelectors.IS_ALIVE, EntitySelectors.NOT_SPECTATING));
	
	for (int j2 = 0; j2 < 10 && !list.isEmpty(); ++j2)
	{
	EntityFriendlyCreature entitylivingbase = list.get(rand.nextInt(list.size()));
	
	if (entitylivingbase != this && entitylivingbase.isEntityAlive() && canEntityBeSeen(entitylivingbase) && entitylivingbase.getOwnerId() == getOwnerId() && entitylivingbase.getFakeHealth() > 0F)
	{
	setAttackTarget(entitylivingbase);
	break;
	}
	
	list.remove(entitylivingbase);
	}
	}
	setGrowingAge(getGrowingAge() + 1);
	
	if (getGrowingAge() < 0 && !isChild())
	setChild(true);
	else if (getGrowingAge() >= 0 && isChild())
	{
	setChild(false);
	ticksExisted = 1;
	playSound(ESound.hero, 1.0F, 1.5F);
	}
	
	if (getGuardBlock() != null)
	{
	if (ticksExisted % 20 == 0 && rand.nextInt(20) == 0 || (randPosX == getGuardBlock().getX() && randPosZ == getGuardBlock().getZ()))
	{
	randPosX = getGuardBlock().getX() + (rand.nextFloat() * 24.0F - 12.0F);
	randPosZ = getGuardBlock().getZ() + (rand.nextFloat() * 24.0F - 12.0F);
	randPosY = world.getTopSolidOrLiquidBlock(new BlockPos(randPosX, 1D, randPosZ)).getY();
	}
	
	if (getDistanceSqToCenter(new BlockPos(randPosX,randPosY,randPosZ)) > 4D && getAttackTarget() == null)
	{
	getNavigator().tryMoveToXYZ(randPosX, randPosY, randPosZ, 1D);
	getLookHelper().setLookPosition(randPosX, randPosY, randPosZ, 10F, 0F);
	if (getDistanceSqToCenter(new BlockPos(randPosX,randPosY,randPosZ)) > (getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue() * getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue()))
	{
	setPosition(randPosX, randPosY + 1D, randPosZ);
	if (isRiding())
	{
	getRidingEntity().setPosition(posX, posY, posZ);
	}
	}
	}
	
	IBlockState iblockstate = world.getBlockState(getGuardBlock());
	Block block = iblockstate.getBlock();
	if (!(block instanceof BlockGuardBlock))
	setGuardBlock(null);
	}
	else
	{
	randPosX = posX;
	randPosY = posY;
	randPosZ = posZ;
	}
	
	--timeUntilPortal;
	
	if (ticksExisted > 400 && rand.nextInt(60) == 0 && !isWild() && getOwner().ticksExisted > 400 && getAttackTarget() == null && !isRiding() && !isBeingRidden())
	{
	
	if (!isWild() && getAttackTarget() == null && !isRiding() && !isBeingRidden())
	{
	int i11 = MathHelper.floor(getOwner().posY);
	int l1 = MathHelper.floor(getOwner().posX);
	int i2 = MathHelper.floor(getOwner().posZ);
	for (int k2 = (int) -2; k2 <= (int) 2; k2++)
	{
	for (int l2 = (int) -2; l2 <= (int) 2; l2++)
	{
	for (int j = (int) -2; j <= (int) 2; j++)
	{
	int i3 = l1 + k2;
	int k = i11 + j;
	int l = i2 + l2;
	BlockPos blockpos = new BlockPos(i3 + 0.5, k, l + 0.5);
	IBlockState iblockstate = world.getBlockState(blockpos);
	Block block = iblockstate.getBlock();
	if ((block instanceof BlockEndGateway || block instanceof BlockEndPortal || block instanceof BlockPortal) && world.getBlockState(blockpos.down()).getBlock() != Blocks.AIR && world.getBlockState(blockpos.down()).getBlock() != Blocks.PORTAL)
	{
	if (this instanceof net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex || this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast)
	getMoveHelper().setMoveTo(i3, k, l, 1D);
	else
	getNavigator().tryMoveToXYZ(i3, k, l, 1D);
	if (this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian || this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker || this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast || getDistanceSqToCenter(blockpos) < 4D)
	{
	if (block instanceof BlockEndPortal && !isNonBoss())
	changeDimension(1);
	
	setPositionAndUpdate(i3 + 0.5, k, l + 0.5);
	if (this instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker)
	playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, getSoundVolume(), 0.95F);
	if (block instanceof BlockEndGateway)
	{
	world.setBlockToAir(blockpos.up(1));
	world.setBlockToAir(blockpos.up(2));
	world.setBlockToAir(blockpos.up(1).east());
	world.setBlockToAir(blockpos.up(1).north());
	world.setBlockToAir(blockpos.up(1).west());
	world.setBlockToAir(blockpos.up(1).south());
	}
	}
	}
	}
	}
	}
	}
	}
	
	if (!noClip && isEntityInsideOpaqueBlock())
	{
	motionY += 0.5D;
	setPosition(posX, posY + 1D, posZ);
	if (isRiding() && getRidingEntity().isEntityInsideOpaqueBlock())
	{
	getRidingEntity().motionY += 0.5D;
	getRidingEntity().setPosition(posX, posY + 1D, posZ);
	}
	}
	
	if (ticksExisted > 20 && isEntityAlive())
	super.updateAITasks();
	}
	
	public double getKnockbackResistance()
	{
	return getEntityAttribute(STRENGTH).getBaseValue() / 100D;
	}
	public boolean canUseGuardBlock()
	{
	return !isChild() && !isCameo();
	}
	
	public boolean canTrample(World world, Block block, BlockPos pos, float fallDistance)
	{
	return false;
	}
	
	public EnumCreatureAttribute getCreatureAttribute()
	{
	return EnumCreatureAttribute.UNDEFINED;
	}
	
	public boolean isPotionApplicable(PotionEffect potioneffectIn)
	{
	Potion potion = potioneffectIn.getPotion();
	if (isEntityUndead() && (potion == MobEffects.REGENERATION || potion == MobEffects.POISON))
	return false;
	if (isEntityImmuneToCoralium() && potion.getName() == "potion.Cplague")
	{
	onFinishedPotionEffect(potioneffectIn);
	return false;
	}
	if (isEntityImmuneToDread() && potion.getName() == "potion.Dplague")
	{
	onFinishedPotionEffect(potioneffectIn);
	return false;
	}
	if (isEntityImmuneToAntiMatter() && potion.getName() == "potion.Antimatter")
	{
	onFinishedPotionEffect(potioneffectIn);
	return false;
	}
	
	return true;
	}
	
	public double getYOffset()
	{
	return height <= 0.5F ? 0.2D : 0.0D;
	}
	
	public void dismountRidingEntity()
	{
	Entity entity = getRidingEntity();
	super.dismountRidingEntity();
	
	if (entity != null && entity != getRidingEntity())
	{
	copyLocationAndAnglesFrom(entity);
	lastTickPosY = prevPosY = posY += entity.height;
	}
	}
	
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
	return null;
	}
	
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack playerStack = player.getHeldItem(hand);
		boolean isAlive = isEntityAlive(), isOnTeam = isOnSameTeam(player), isOwner = hasOwner(player), hasItem = !playerStack.isEmpty();
		Item playerItem = hasItem ? playerStack.getItem() : null;
		
		
		if (!isAlive || hasLimitedLife())
			return false;
		
		if (hasItem)
		{
			if (playerItem instanceof ItemEngenderStatChecker)
			{
				playerStack.getItem().itemInteractionForEntity(playerStack, player, this, hand);
				return true;
			}
			else if (playerItem instanceof ItemLearningBook)
			{
				int id = -1;
				player.swingArm(hand);
				
				for (ItemLearningBook book : EItem.SKILL_BOOKS)
					if (playerItem.equals(book))
					{
						id = EItem.SKILL_BOOKS.indexOf(book);
						break;
					}
				
				setBookID(id);
				setBookDurability(playerStack.getItemDamage());
				ItemStack stack = new ItemStack(EItem.SKILL_BOOKS.get(id), 1 , getBookDurability());
				stack.setTagCompound(playerStack.getTagCompound());
				stack.setItemDamage(playerStack.getItemDamage());
				playerStack.shrink(1);
				return true;
			}
		}
		else
		{
			if (player.isSneaking() && getGuardBlock() != null)
			{
				player.swingArm(hand);
				
				if (world.isRemote)
				{
					playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1F, 2F);
					spawnExplosionParticle();
				}
				
				randPosX = posX;
				randPosY = posY;
				randPosZ = posZ;
				setGuardBlock(null);
				return true;
			}
			
		}
		
		
		
		
		if (getBookID() != 0 && playerStack.isEmpty())
		{
			player.swingArm(hand);
			playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.3F + rand.nextFloat() * 0.4F);
			if (!world.isRemote)
			{
			if (getBookDurability() >= getCurrentBook().getMaxDamage())
			{
			setBookID(0);
			setBookDurability(0);
			}
			else
			{
			entityDropItem(new ItemStack(EItem.SKILL_BOOKS.get(getBookID()), 1 , getBookDurability()), 1F);
			setBookID(0);
			setBookDurability(0);
			}
			}
			return true;
		}
		if (playerStack.getItem() == Items.SPAWN_EGG)
		{
			if (!world.isRemote)
			{
			Class <? extends Entity > oclass = EntityList.getClass(ItemMonsterPlacer.getNamedIdFrom(playerStack));
			
			if (oclass != null && getClass() == oclass)
			{
			EntityFriendlyCreature idleTimeable = spawnBaby(this);
			
			if (idleTimeable != null)
			{
			idleTimeable.setOwnerId(getOwnerId());
			idleTimeable.copyLocationAndAnglesFrom(this);
			world.spawnEntity(idleTimeable);
			idleTimeable.onInitialSpawn(world.getDifficultyForLocation(getPosition()), null);
			idleTimeable.setGrowingAge(-24000);
			if (playerStack.hasDisplayName())
			{
			idleTimeable.setCustomNameTag(playerStack.getDisplayName());
			}
			
			if (!player.capabilities.isCreativeMode)
			{
			playerStack.shrink(1);
			}
			}
			}
			}
			
			return true;
		}
		if (isChild())
		{
			if (hasOwner(player))
			{
			player.swingArm(EnumHand.MAIN_HAND);
			if (getRidingEntity() == null)
			{
			startRiding(player, true);
			}
			else
			{
			dismountRidingEntity();
			}
			}
			return true;
		}
		if (hasOwner(player) && getRidingEntity() != null)
		{
			player.swingArm(EnumHand.MAIN_HAND);
			dismountRidingEntity();
			return true;
		}
		
		if (playerStack.getItem().itemInteractionForEntity(playerStack, player, this, hand))
		{
			return true;
		}
		if (canWearEasterEggs() && isOnSameTeam(player) && !playerStack.isEmpty() && getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() && (playerStack.getItem() == Items.SKULL || playerStack.getItem() == Items.FISH || playerStack.getItem() == Items.BONE || playerStack.getItem() == Item.getItemFromBlock(Blocks.END_ROD) || playerStack.getItem() == Items.FEATHER))
		{
			setItemStackToSlot(EntityEquipmentSlot.HEAD, playerStack);
			playEquipSound(playerStack);
			player.swingArm(hand);
			if (!world.isRemote)
			{
				ItemStack stack = new ItemStack(playerItem);
				stack.setTagCompound(playerStack.getTagCompound());
				stack.setItemDamage(playerStack.getItemDamage());
				setItemStackToSlot(EntityEquipmentSlot.HEAD, stack);
				playerStack.shrink(1);
			}
			return true;
		}
		if (canWearEasterEggs() && isOnSameTeam(player) && player.isSneaking() && playerStack.isEmpty())
		{
			dropEquipmentUndamaged();
			player.swingArm(hand);
			return true;
		}
		
		if (isOnSameTeam(player) && !playerStack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) && playerStack.getItem() != Items.NAME_TAG && playerStack.getItem() instanceof ItemFood && getEnergy() <= 100F)
		{
		playSound(SoundEvents.ENTITY_PLAYER_BURP, 1.0F, 1.0F);
		player.swingArm(hand);
		if (!world.isRemote)
		{
			ItemStack stack = new ItemStack(playerItem);
			stack.setTagCompound(playerStack.getTagCompound());
			stack.setItemDamage(playerStack.getItemDamage());
			stack.setCount(playerStack.getCount());
			setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
			playerStack.shrink(playerStack.getCount());
		}
		return true;
		}
		
		return interact(player, hand);
	}
	
	public boolean canWearEasterEggs()
	{
	return true;
	}
	
	public boolean interact(EntityPlayer player, EnumHand hand)
	{
	return false;
	}
	
	/**
	* Drop the equipment for this entity.
	*/
	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier)
	{
	super.dropEquipment(wasRecentlyHit, lootingModifier);
	
	if (!getCurrentBook().isEmpty())
	{
	playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.3F + rand.nextFloat() * 0.4F);
	if (!world.isRemote)
	entityDropItem(new ItemStack(EItem.SKILL_BOOKS.get(getBookID()), 1 , getBookDurability()), 1F);
	setBookID(0);
	setBookDurability(0);
	}
	}
	
	public int getVerticalFaceSpeed()
	{
	return !isEntityAlive() ? 180 : 60;
	}
	
	public boolean leavesNoCorpse()
	{
	return world.getGameRules().getBoolean("disableCorpses");
	}
	
	protected void collideWithEntity(Entity p_82167_1_)
	{
	if (attackable() && getRevengeTarget() == null && p_82167_1_ instanceof EntityLivingBase && !isOnSameTeam((EntityLivingBase)p_82167_1_))
	{
	if (!(p_82167_1_ instanceof EntityPlayer) || (p_82167_1_ instanceof EntityPlayer && !((EntityPlayer)p_82167_1_).capabilities.disableDamage))
	setAttackTarget((EntityLivingBase)p_82167_1_);
	}
	if (p_82167_1_ instanceof EntityLivingBase && isEntityAlive())
	super.collideWithEntity(p_82167_1_);
	}
	
	
	
	protected void updateLeashedState()
	{
	if (getLeashed())
	{
	if (!isEntityAlive() || this instanceof EntityPortal || this instanceof EntityWitherStormHead || this instanceof EntityWitherStormTentacle || this instanceof EntityWitherStormTentacleDevourer)
	{
	clearLeashed(true, true);
	}
	
	if (getLeashHolder() == null || getLeashHolder().isDead)
	{
	clearLeashed(true, true);
	}
	}
	
	if (getLeashed() && getLeashHolder() != null && getLeashHolder().world == world)
	{
	Entity entity = getLeashHolder();
	setHomePosAndDistance(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
	float f = getDistance(entity);
	
	onLeashDistance(f);
	
	if (f > 3.0F)
	{
	getNavigator().tryMoveToEntityLiving(entity, 1.0D);
	}
	
	if (f > 9.0F)
	{
	double d0 = (entity.posX - posX) / (double)f;
	double d1 = (entity.posY - posY) / (double)f;
	double d2 = (entity.posZ - posZ) / (double)f;
	motionX += d0 * Math.abs(d0) * 0.4D;
	motionY += d1 * Math.abs(d1) * 0.4D;
	motionZ += d2 * Math.abs(d2) * 0.4D;
	}
	}
	}
	
	/**
	* Spawns an explosion particle around the Entity's location
	*/
	public void spawnHeartParticle()
	{
	if (world.isRemote)
	{
	if (isEntityAlive())
	world.spawnParticle(EnumParticleTypes.HEART, posX + rand.nextGaussian(), posY + height, posZ + rand.nextGaussian(), 0D, 0D, 0D, new int[0]);
	}
	else
	{
	world.setEntityState(this, (byte)22);
	}
	}
	
	public void spawnStressParticle()
	{
		if (world.isRemote)
		{
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			if (isEntityAlive())
				world.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX + (double)(rand.nextFloat() * width * 2.0F) - (double)width, posY + getEyeHeight(), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double)width, d0, d1, d2, new int[0]);
		}
		else
			world.setEntityState(this, (byte)23);
	}
	
	public void spawnExplosionParticle()
	{
	if (world.isRemote)
	{
	for (int i = 0; i < 2 * (int)(width + height) + 5; ++i)
	{
	double d0 = rand.nextGaussian() * (width / 2);
	double d1 = rand.nextDouble() * height;
	double d2 = rand.nextGaussian() * (width / 2);
	if (isEntityAlive())
	{
	world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + d0, posY + d1, posZ + d2, rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D, new int[0]);
	world.spawnParticle(EnumParticleTypes.SPELL_WITCH, posX + d0, posY + d1, posZ + d2, rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D, new int[0]);
	world.spawnParticle(EnumParticleTypes.SPELL_MOB, posX + d0, posY + d1, posZ + d2, rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), new int[0]);
	}
	}
	}
	else
	{
	world.setEntityState(this, (byte)20);
	}
	}
	
	
	public void spawnConversionParticle()
	{
	if (world.isRemote)
	{
	for (int i1 = 0; i1 < convertionInt; i1++)
	{
	float f1 = (float)i1 * (float)Math.PI / (timesToConvert() * 0.5F);
	if (isEntityAlive())
	world.spawnParticle(EnumParticleTypes.END_ROD, true, posX + ((double)MathHelper.cos(f1) * (width > 6F ? 6F : width)), posY + height + 1D, posZ + ((double)MathHelper.sin(f1) * (width > 6F ? 6F : width)), motionX, motionY, motionZ, new int[0]);
	}
	}
	else
	{
	world.setEntityState(this, (byte)21);
	}
	}
	
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
	if (id == 23)
	{
	spawnStressParticle();
	}
	else if (id == 22)
	{
	spawnHeartParticle();
	}
	else if (id == 21)
	{
	spawnConversionParticle();
	}
	else
	{
	super.handleStatusUpdate(id);
	}
	}
	protected int decreaseAirSupply(int air)
	{
	int i = EnchantmentHelper.getRespirationModifier(this);
	if (isUndead())
	return air;
	else
	return i > 0 && rand.nextInt(i + 1) > 0 ? air : air - 1;
	}
	
	public void fall(float distance, float damageMultiplier)
	{
	motionX = 0;
	motionZ = 0;
	hurtTime = 0;
	prevRotationPitchFalling = rotationPitchFalling = 0F;
	float[] ret = net.minecraftforge.common.ForgeHooks.onLivingFall(this, distance, damageMultiplier);
	if (ret == null) return;
	distance = ret[0]; damageMultiplier = ret[1];
	if (isBeingRidden())
	{
	for (Entity entity : getPassengers())
	{
	entity.fall(distance, damageMultiplier);
	}
	}
	PotionEffect potioneffect = getActivePotionEffect(MobEffects.JUMP_BOOST);
	float f = potioneffect == null ? 0.0F : (float)(potioneffect.getAmplifier() + 1);
	float energy = getEnergy() <= 10F ? 0.0F : getEnergy() / 10F;
	int i = MathHelper.ceil((distance - 3.0F - f - energy) * damageMultiplier);
	if (i > 0)
	{
	playSound(getFallSound(i), 1.0F, 1.0F);
	if (takesFallDamage())
	{
	setEnergy(getEnergy() - i);
	attackEntityFrom(DamageSource.FALL, (float)i);
	}
	int j = MathHelper.floor(posX);
	int k = MathHelper.floor(posY - 0.20000000298023224D);
	int l = MathHelper.floor(posZ);
	IBlockState iblockstate = world.getBlockState(new BlockPos(j, k, l));
	
	if (iblockstate.getMaterial() != Material.AIR)
	{
	SoundType soundtype = iblockstate.getBlock().getSoundType(iblockstate, world, new BlockPos(j, k, l), this);
	playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
	playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
	playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
	}
	}
	onGround = true;
	}
	
	public boolean canBeCollidedWith()
	{
	return !isDead && isEntityAlive() && !isAIDisabled();
	}
	
	
	public void setSprinting(boolean sprinting)
	{
	if (getEnergy() <= 30F)
	sprinting = false;
	super.setSprinting(sprinting);
	}
	public void updateBossBar()
	{
	bossInfo.setName(getDisplayName());
	bossInfo.setPercent(getFakeHealth() > 0 ? getFakeHealth() / (getMaxHealth() * 2) : getHealth() / getMaxHealth());
	bossInfo.setVisible(!isSneaking() && !isInvisible() && isEntityAlive());
	bossInfo.setOverlay(getTier().ordinal() > EnumTier.TIER5.ordinal() ? BossInfo.Overlay.NOTCHED_20 : (getTier() == EnumTier.TIER5 ? (getMaxHealth() >= 250 ? BossInfo.Overlay.NOTCHED_12 : BossInfo.Overlay.NOTCHED_10) : (getMaxHealth() >= 50 ? BossInfo.Overlay.NOTCHED_6 : BossInfo.Overlay.PROGRESS)));
	}
	
	public boolean isBoss()
	{
		return false;
	}
	
	@Override
	public boolean isNonBoss()
	{
		return !isBoss();
	}
	
	/**
	* Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
	* order to view its associated boss bar.
	*/
	public void addTrackingPlayer(EntityPlayerMP player)
	{
	super.addTrackingPlayer(player);
	if (isBoss() || (getGrowingAge() <= 0 && player.getName().equals("Mrbt0907")))
	bossInfo.addPlayer(player);
	}
	
	/**
	* Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for
	* more information on tracking.
	*/
	public void removeTrackingPlayer(EntityPlayerMP player)
	{
	super.removeTrackingPlayer(player);
	bossInfo.removePlayer(player);
	}
	public int getSpawnTimer()
	{
	return 20;
	}
	
	public float getHealthPercent()
	{
	return (getHealth() / getMaxHealth());
	}
	
	public float getEnergyPercent()
	{
	return (getEnergy() / 100F);
	}
	
	public float getEXPPercent()
	{
		return (float)(getEXP() / getNextLevelRequirement());
	}
	
	public boolean isEntityImmuneToCoralium()
	{
	return isBoss() || isHero() || isCameo();
	}
	
	public boolean isEntityImmuneToDread()
	{
	return isBoss() || isHero() || isCameo();
	}
	
	public boolean isEntityImmuneToAntiMatter()
	{
	return isBoss() || isHero() || isCameo() || isAntiMob();
	}
	
	public boolean isEntityImmuneToDarkness()
	{
	return isBoss() || isHero() || isCameo();
	}
	
	public boolean passesCoraliumPlague()
	{
	return false;
	}
	
	public boolean passesDreadPlague()
	{
	return false;
	}
	protected SoundEvent getRegularHurtSound()
	{
	return ESound.fleshHit;
	}
	protected SoundEvent getPierceHurtSound()
	{
	return ESound.fleshHitPierce;
	}
	protected SoundEvent getCrushHurtSound()
	{
	return ESound.fleshHitCrush;
	}
	
	public EnumSoundType getSoundType()
	{
		return EnumSoundType.NORMAL;
	}
	
	public boolean canBeTurnedToStone()
	{
		return getTier() != EnumTier.TIER6 && isNonBoss() && !isHero() && !(this instanceof Structure);
	}
	
	public Entity getControllingPassenger()
	{
	return ((getPassengers().isEmpty() ? null : (Entity)getPassengers().get(0)));
	}
	
	public boolean canAttackClass(Class <? extends EntityLivingBase > cls)
	{
	return true;
	}
	public boolean isCameo()
	{
	return false;
	}
	
	public boolean canBeButchered()
	{
	return false;
	}
	
	public boolean isSitResting()
	{
	return ((Boolean)dataManager.get(SITRESTING)).booleanValue();
	}
	
	public void setSitResting(boolean bool)
	{
	dataManager.set(SITRESTING, Boolean.valueOf(bool));
	}
	
	public boolean isAntiMob()
	{
	return ((Boolean)dataManager.get(ANTIMOB)).booleanValue();
	}
	public void setIsAntiMob(boolean bool)
	{
	dataManager.set(ANTIMOB, Boolean.valueOf(bool));
	}
	public boolean isHero()
	{
	return ((Boolean)dataManager.get(HERO)).booleanValue();
	}
	public void setIsHero(boolean bool)
	{
	dataManager.set(HERO, Boolean.valueOf(bool));
	}
	public void becomeAHero()
	{
	setIsHero(true);
	playSound(ESound.hero, 100.0F, 1.0F);
	ticksExisted = -20;
	}
	public boolean hasLastChance()
	{
	return ((Boolean)dataManager.get(REBIRTH)).booleanValue();
	}
	public void setLastChance(boolean bool)
	{
	dataManager.set(REBIRTH, Boolean.valueOf(bool));
	}
	public SoundCategory getSoundCategory()
	{
	return SoundCategory.MASTER;
	}
	@Nullable
	public BlockPos getGuardBlock()
	{
	return (BlockPos)((Optional<?>)dataManager.get(GUARD_BLOCK_POS)).orNull();
	}
	
	public void setGuardBlock(@Nullable BlockPos pos)
	{
	dataManager.set(GUARD_BLOCK_POS, Optional.fromNullable(pos));
	}
	
	public BlockPos getJukeboxToDanceTo()
	{
		return jukeBoxToDanceTo;
	}
	
	public void setJukeboxToDanceTo(BlockPos block)
	{
		jukeBoxToDanceTo = block;
	}
	
	@Nullable
	public UUID getOwnerId()
	{
		return (UUID)((Optional<?>)dataManager.get(OWNER_UNIQUE_ID)).orNull();
	}
	
	public void setOwnerId(@Nullable UUID p_184754_1_)
	{
		dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(p_184754_1_));
	}
	
	public boolean canFollowOwner()
	{
		return !isElytraFlying() && !isWild() && !isAIDisabled() && !isMovementBlocked() && getGuardBlock() == null && getAttackTarget() == null && getCurrentBook().isEmpty();
	}
	
	@Nullable
	public EntityLivingBase getOwner()
	{
		UUID uuid = getOwnerId();
		return uuid == null ? null : world.getPlayerEntityByUUID(uuid);
	}
	
	public boolean isOwner(EntityLivingBase entityIn)
	{
		return entityIn == getOwner();
	}
	
	public boolean isWild()
	{
		return getOwnerId() == null;
	}
	
	public boolean hasOwner(EntityPlayer player)
	{
		return !isWild() && getOwner() == player;
	}
	
	public class PathNavigateFlying extends PathNavigateGround
	{
		public PathNavigateFlying(EntityLiving p_i47412_1_, World p_i47412_2_)
		{
		super(p_i47412_1_, p_i47412_2_);
		}
		
		protected PathFinder getPathFinder()
		{
		nodeProcessor = new FlyingNodeProcessor();
		nodeProcessor.setCanEnterDoors(true);
		return new PathFinder(nodeProcessor);
		}
		
		/**
		* If on ground or swimming and can swim
		*/
		protected boolean canNavigate()
		{
		return canFloat() && isInLiquid() || !entity.isRiding();
		}
		
		protected Vec3d getEntityPosition()
		{
		return new Vec3d(entity.posX, entity.posY, entity.posZ);
		}
		
		/**
		* Returns the path to the given EntityLiving. Args : entity
		*/
		public Path getPathToEntityLiving(Entity entityIn)
		{
		return getPathToPos(new BlockPos(entityIn));
		}
		
		public void onUpdateNavigation()
		{
		++totalTicks;
		
		if (tryUpdatePath)
		{
		updatePath();
		}
		
		if (!noPath())
		{
		if (canNavigate())
		{
		pathFollow();
		}
		else if (currentPath != null && currentPath.getCurrentPathIndex() < currentPath.getCurrentPathLength())
		{
		Vec3d vec3d = currentPath.getVectorFromIndex(entity, currentPath.getCurrentPathIndex());
		
		if (MathHelper.floor(entity.posX) == MathHelper.floor(vec3d.x) && MathHelper.floor(entity.posY) == MathHelper.floor(vec3d.y) && MathHelper.floor(entity.posZ) == MathHelper.floor(vec3d.z))
		{
		currentPath.setCurrentPathIndex(currentPath.getCurrentPathIndex() + 1);
		}
		}
		
		debugPathFinding();
		
		if (!noPath())
		{
		Vec3d vec3d1 = currentPath.getPosition(entity);
		entity.getMoveHelper().setMoveTo(vec3d1.x, vec3d1.y, vec3d1.z, speed);
		}
		}
		}
		
		/**
		* Checks if the specified entity can safely walk to the specified location.
		*/
		protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ)
		{
		int i = MathHelper.floor(posVec31.x);
		int j = MathHelper.floor(posVec31.y);
		int k = MathHelper.floor(posVec31.z);
		double d0 = posVec32.x - posVec31.x;
		double d1 = posVec32.y - posVec31.y;
		double d2 = posVec32.z - posVec31.z;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;
		
		if (d3 < 1.0E-8D)
		{
		return false;
		}
		else
		{
		double d4 = 1.0D / Math.sqrt(d3);
		d0 = d0 * d4;
		d1 = d1 * d4;
		d2 = d2 * d4;
		double d5 = 1.0D / Math.abs(d0);
		double d6 = 1.0D / Math.abs(d1);
		double d7 = 1.0D / Math.abs(d2);
		double d8 = (double)i - posVec31.x;
		double d9 = (double)j - posVec31.y;
		double d10 = (double)k - posVec31.z;
		
		if (d0 >= 0.0D)
		{
		++d8;
		}
		
		if (d1 >= 0.0D)
		{
		++d9;
		}
		
		if (d2 >= 0.0D)
		{
		++d10;
		}
		
		d8 = d8 / d0;
		d9 = d9 / d1;
		d10 = d10 / d2;
		int l = d0 < 0.0D ? -1 : 1;
		int i1 = d1 < 0.0D ? -1 : 1;
		int j1 = d2 < 0.0D ? -1 : 1;
		int k1 = MathHelper.floor(posVec32.x);
		int l1 = MathHelper.floor(posVec32.y);
		int i2 = MathHelper.floor(posVec32.z);
		int j2 = k1 - i;
		int k2 = l1 - j;
		int l2 = i2 - k;
		
		while (j2 * l > 0 || k2 * i1 > 0 || l2 * j1 > 0)
		{
		if (d8 < d10 && d8 <= d9)
		{
		d8 += d5;
		i += l;
		j2 = k1 - i;
		}
		else if (d9 < d8 && d9 <= d10)
		{
		d9 += d6;
		j += i1;
		k2 = l1 - j;
		}
		else
		{
		d10 += d7;
		k += j1;
		l2 = i2 - k;
		}
		}
		
		return true;
		}
		}
		
		public void setCanOpenDoors(boolean p_192879_1_)
		{
		nodeProcessor.setCanOpenDoors(p_192879_1_);
		}
		
		public void setCanEnterDoors(boolean p_192878_1_)
		{
		nodeProcessor.setCanEnterDoors(p_192878_1_);
		}
		
		public void setCanFloat(boolean p_192877_1_)
		{
		nodeProcessor.setCanSwim(p_192877_1_);
		}
		
		public boolean canFloat()
		{
		return nodeProcessor.getCanSwim();
		}
		
		public boolean canEntityStandOnPos(BlockPos pos)
		{
		return world.getBlockState(pos).isSideSolid(world, pos, EnumFacing.UP);
		}	
	}
	
	public static float calculateLevel(float experience)
	{
		return (float) Math.sqrt(experience * EngenderConfig.mobs.levelFactor);
	}
	
	public static float calculateExperience(float level)
	{
		return (float) Math.pow(level, 2.0F) * EXP_FACTOR;
	}
}