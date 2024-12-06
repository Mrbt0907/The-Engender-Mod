package net.mrbt0907.ageofminecraft.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.api.events.EventAIChange;
import net.mrbt0907.ageofminecraft.api.events.EventAIChange.EntityAITaskEntry;
import net.mrbt0907.ageofminecraft.api.events.EventStanceChange;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EntityAIFollow;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EntityAIOpenDoor;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EntityAIPath;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EntityTargetAttacker;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EntityTargetLead;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EntityTargetNearest;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.util.mrbtutil.TranslateUtil;

/*
 * TODO:
 * - Level System
 * - Team System
 * - Command System
 * - Polymorph System
 */
public abstract class EntityEngendered extends EntityAgeable implements IEntityOwnable
{
	public static final BiPredicate<EntityEngendered, EntityLivingBase> TARGET_WILD = new BiPredicate<EntityEngendered, EntityLivingBase>()
	{
		@Override
		public boolean test(EntityEngendered entity, EntityLivingBase target)
		{
			EntityPlayer player = target instanceof EntityPlayer ? (EntityPlayer) target : null;
			return target.isEntityAlive() && target.attackable() && (player == null ? true : !(player.capabilities.isCreativeMode || player.isSpectator())) && !target.isInvisible() &&!entity.isOnSameTeam(target) && entity.canEntityBeSeen(target);
		}
	};
	public static final Predicate<EntityEngendered> DONT_TARGET_WILD = new Predicate<EntityEngendered>() {
		@Override
		public boolean apply(EntityEngendered entity)
		{
			return entity.hasOwner();
		}
	};
	
	private static final DataParameter<Float> EXPERIENCE = EntityDataManager.createKey(EntityEngendered.class, DataSerializers.FLOAT);
	private static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.createKey(EntityEngendered.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Boolean> HERO = EntityDataManager.createKey(EntityEngendered.class, DataSerializers.BOOLEAN);
	
	public static final IAttribute VIGOR = new RangedAttribute((IAttribute)null, "engender.vigor", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE).setDescription("Mob Vigor").setShouldWatch(true);
	public static final IAttribute STRENGTH = new RangedAttribute((IAttribute)null, "engender.strength", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE).setDescription("Mob Strength").setShouldWatch(true);
	public static final IAttribute STAMINA = new RangedAttribute((IAttribute)null, "engender.stamina", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE).setDescription("Mob Stamina").setShouldWatch(true);
	public static final IAttribute INTELLIGENCE = new RangedAttribute((IAttribute)null, "engender.intelligence", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE).setDescription("Mob Intelligence").setShouldWatch(true);
	public static final IAttribute DEXTERITY = new RangedAttribute((IAttribute)null, "engender.dexterity", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE).setDescription("Mob Dexterity").setShouldWatch(true);
	public static final IAttribute AGILITY = new RangedAttribute((IAttribute)null, "engender.agility", 0.0D, -Double.MAX_VALUE, Double.MAX_VALUE).setDescription("Mob Agility").setShouldWatch(true);
	public static final UUID VIGOR_UUID = UUID.fromString("b038ce84-bc47-49a4-8c9c-2d4ed6e03867");
	public static final UUID STRENGTH_UUID = UUID.fromString("8ce1d897-9f46-415d-9b17-2349c664308e");
	public static final UUID STAMINA_UUID = UUID.fromString("05b559fc-409b-4379-a87e-8de2ba4c9811");
	public static final UUID INTELLIGENCE_UUID = UUID.fromString("643d2a29-0f58-4f6b-94b9-c63254015afe");
	public static final UUID DEXTERITY_UUID = UUID.fromString("71f1bde9-6962-4c30-bf20-48de06cb25db");
	public static final UUID AGILITY_UUID = UUID.fromString("e3be3d18-3162-415a-b611-c83fadf96360");
	public final InventoryBasic inventory;
	protected EnumAIStance stance;
	protected Entity owner;
	public BlockPos[] followPos;
	public boolean doSpawnAnimation;
	protected short healTime;
	
	@SideOnly(Side.CLIENT)
	public boolean selected;
	
	public EntityEngendered(World world)
	{
		super(world);
		if (world.isRemote)
		{
			stance = getDefaultStance();
			doSpawnAnimation = true;
		}
		inventory = new InventoryBasic("Basic inventory", false, 8);
	}
	
	protected void initEntityAI()
	{
		super.initEntityAI();
		stance = getDefaultStance();
		onAIChange();
	}
	
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(EXPERIENCE, 0.0F);
		dataManager.register(OWNER_UUID, Optional.<UUID>absent());
		dataManager.register(HERO, false);
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(VIGOR);
		getAttributeMap().registerAttribute(STRENGTH);
		getAttributeMap().registerAttribute(STAMINA);
		getAttributeMap().registerAttribute(INTELLIGENCE);
		getAttributeMap().registerAttribute(DEXTERITY);
		getAttributeMap().registerAttribute(AGILITY);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.9D);
	}
	
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		if (nbt.hasUniqueId("Owner"))
			setOwner(nbt.getUniqueId("Owner"));
		setStance(nbt.getInteger("Stance"));
		
		if (nbt.hasKey("VGR")) setVigor(nbt.getLong("VGR"));
		if (nbt.hasKey("STR")) setStrength(nbt.getLong("STR"));
		if (nbt.hasKey("STA")) setStamina(nbt.getLong("STA"));
		if (nbt.hasKey("INT")) setIntelligence(nbt.getLong("INT"));
		if (nbt.hasKey("DEX")) setDexterity(nbt.getLong("DEX"));
		if (nbt.hasKey("AGI")) setAgility(nbt.getLong("AGI"));

		if (nbt.hasKey("Path"))
		{
			NBTTagCompound nbtPos = nbt.getCompoundTag("Path");
			Set<String> keys = nbtPos.getKeySet();
			int i = 0;
			
			followPos = new BlockPos[nbtPos.getSize()];
			for (String key : keys)
				followPos[i] = BlockPos.fromLong(nbtPos.getLong(key)); i++;
		}
		else
			followPos = null;
		
		doSpawnAnimation = nbt.getBoolean("doSpawnAnimation");
	}
	
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		if (hasOwner())
			nbt.setUniqueId("Owner", getOwnerId());
		nbt.setInteger("Stance", getStance().ordinal());
		
		nbt.setDouble("VGR", getVigor());
		nbt.setDouble("STR", getStrength());
		nbt.setDouble("STA", getStamina());
		nbt.setDouble("INT", getIntelligence());
		nbt.setDouble("DEX", getDexterity());
		nbt.setDouble("AGI", getAgility());
		
		if (followPos != null)
		{
			NBTTagCompound nbtPos = new NBTTagCompound();
			
			for (BlockPos pos : followPos)
				if (pos != null)
					nbtPos.setLong(nbtPos.getSize() + "", pos.toLong());
			nbt.setTag("Path", nbtPos);
		}
		nbt.setBoolean("doSpawnAnimation", doSpawnAnimation);
	}
	
	public void onUpdate()
	{
		super.onUpdate();
	}
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (healTime > 200)
		{
			if (ticksExisted % 20 == 0 && getHealth() < getMaxHealth() && hasOwner())
				heal((float)(1.0D + Math.max(0.0D, getVigor() * 0.01D)));
		}
		else
			healTime++;
	}
	
	protected void onDeathUpdate()
	{
		super.onDeathUpdate();
	}
	
	protected void updateAITasks()
	{
		if (ticksExisted % 100 == 0)
		{
			EntityLivingBase target = getAttackTarget();
			if (target != null && !target.isEntityAlive())
				setAttackTarget(null);
		}
	}
	
	public boolean attackEntityAsMob(Entity entity)
	{
		boolean truth = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) (getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * Math.max(0.0D, 1.0D + getStrength() * 0.01D)));
		if (truth)
			entity.hurtResistantTime = 0;
		return truth;
	}
	
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (isOnSameTeam(source.getTrueSource()) && !world.getGameRules().getBoolean("friendlyFire"))
			return false;
		healTime = 0;
		return super.attackEntityFrom(source, amount);
	}
	
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		if (isOnSameTeam(player) && isEntityAlive())
		{
			ItemStack stack = player.getHeldItem(hand);
			Item item = stack != null ? stack.getItem() : Items.AIR;
			if (item.equals(Items.AIR))
				if (isChild())
				{
					if (getRidingEntity() == null)
						startRiding(player, true);
					else
						dismountRidingEntity();
	
					player.swingArm(EnumHand.MAIN_HAND);
					return true;
				}
				else
				{
					if (this instanceof IJumpingMount)
					{
						if (player.getRidingEntity() == null)
							player.startRiding(this, true);
		
						player.swingArm(EnumHand.MAIN_HAND);
						return true;
					}
				}
			
		}
		return super.processInteract(player, hand);
	}
	
	public void travel(float strafe, float vertical, float forward)
	{
		if (isBeingRidden() && canBeSteered())
		{
			EntityLivingBase entity = (EntityLivingBase)getControllingPassenger();
			rotationYaw = entity.rotationYaw;
			prevRotationYaw = rotationYaw;
			rotationPitch = entity.rotationPitch * 0.5F;
			setRotation(rotationYaw, rotationPitch);
			renderYawOffset = rotationYaw;
			rotationYawHead = renderYawOffset;
			strafe = entity.moveStrafing * 0.5F;
			forward = entity.moveForward;

			if (forward <= 0.0F)
				forward *= 0.25F;

			jumpMovementFactor = getAIMoveSpeed() * 0.1F;

			if (canPassengerSteer())
			{
				setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.175F);
				super.travel(strafe, vertical, forward);
			}
			else if (entity instanceof EntityPlayer)
			{
				motionX = 0.0D;
				motionY = 0.0D;
				motionZ = 0.0D;
			}

			prevLimbSwingAmount = limbSwingAmount;
			double d1 = posX - prevPosX;
			double d0 = posZ - prevPosZ;
			float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

			if (f2 > 1.0F)
				f2 = 1.0F;

			limbSwingAmount += (f2 - limbSwingAmount) * 0.4F;
			limbSwing += limbSwingAmount;
		}
		else
		{
			jumpMovementFactor = 0.02F;
			super.travel(strafe, vertical, forward);
		}
	}
	
	protected void onStanceChange(EnumAIStance oldStance)
	{
	}
	
	protected void onAIChange()
	{
		if (!world.isRemote)
		{
			super.initEntityAI();
			switch(stance)
			{
				case AGGRESSIVE:
					tasks.addTask(0, new EntityAIOpenDoor(this));
					tasks.addTask(1, new EntityAIAttackMelee(this, 0.75D, true));
					tasks.addTask(2, new EntityAIPath(this, 1.0D));
					tasks.addTask(3, new EntityAIFollow(this, 1.0D));
					targetTasks.addTask(0, new EntityTargetLead(this));
					targetTasks.addTask(1, new EntityTargetAttacker(this, true));
					targetTasks.addTask(2, new EntityTargetNearest(this, EntityLivingBase.class));
					break;
				case DEFENSIVE:
					tasks.addTask(0, new EntityAIOpenDoor(this));
					tasks.addTask(1, new EntityAIAttackMelee(this, 0.75D, true));
					tasks.addTask(2, new EntityAIPath(this, 1.0D));
					tasks.addTask(3, new EntityAIFollow(this, 1.0D));
					targetTasks.addTask(1, new EntityTargetAttacker(this, true));
					targetTasks.addTask(2, new EntityTargetNearest(this, EntityLivingBase.class));
					break;
				case STAND_GROUND:
					tasks.addTask(1, new EntityAIAttackMelee(this, 0.0D, true));
					tasks.addTask(2, new EntityAIPath(this, 1.0D));
					targetTasks.addTask(1, new EntityTargetAttacker(this, false));
					targetTasks.addTask(2, new EntityTargetNearest(this, EntityLivingBase.class));
					break;
				case PASSIVE:
					if (getAttackTarget() != null)
						setAttackTarget(null);
					tasks.addTask(0, new EntityAIOpenDoor(this));
					tasks.addTask(2, new EntityAIPath(this, 1.0D));
					tasks.addTask(3, new EntityAIFollow(this, 1.0D));
					targetTasks.addTask(1, new EntityTargetAttacker(this, false));
					targetTasks.addTask(2, new EntityTargetNearest(this, EntityLivingBase.class));
					break;
				default:
			}
			
			EventAIChange eventAI = new EventAIChange(this);
			MinecraftForge.EVENT_BUS.post(eventAI);
			List<EntityAITaskEntry> tasks = eventAI.getTasks();
			List<EntityAITaskEntry> targetTasks = eventAI.getTargetTasks();
			this.tasks.taskEntries.clear();
			this.targetTasks.taskEntries.clear();
			tasks.forEach(task -> this.tasks.addTask(task.priority, task.action));
			targetTasks.forEach(task -> this.targetTasks.addTask(task.priority, task.action));
		}
	}
	
	//----- OVERRIDES -----\\
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		if (!(ageable instanceof EntityEngendered)) return null;
		EntityEngendered entity; try {entity = (EntityEngendered) ageable.getClass().getConstructor(World.class).newInstance(world);} catch (Exception e) {entity = null;}
		if (entity != null)
			entity.setOwner(((EntityEngendered)ageable).getOwnerId());
		return entity;
	}
	
	protected void onGrowingAdult()
	{
		if (getRidingEntity() instanceof EntityPlayer)
			dismountRidingEntity();
		super.onGrowingAdult();
	}
	
	@Override
	public Entity getControllingPassenger()
	{
		return getPassengers().isEmpty() ? null : getPassengers().get(0);
	}
	
	@Override
	public boolean canBeSteered()
	{
		return isOnSameTeam(getControllingPassenger());
	}
	
	@Override
	protected void removePassenger(Entity passenger)
    {
		if (getPassengers().size() <= 1 && followPos != null)
			followPos = new BlockPos[] {getPosition()};
		super.removePassenger(passenger);
    }
	
	@Override
	public boolean isOnSameTeam(Entity entity)
	{
		if (entity == null) return false;
		if (equals(entity)) return true;

		if (hasOwner())
		{
			Entity owner = getOwner();
			if (owner != null && owner.equals(entity))
				return true;

			if (entity instanceof IEntityOwnable && getOwnerId().equals(((IEntityOwnable)entity).getOwnerId()))
				return true;

			if (entity instanceof EntityLiving && (entity instanceof EntityAnimal || entity.getClass().equals(IAnimals.class) || entity instanceof IMerchant || entity instanceof INpc))
				return owner != null ? ((EntityLiving)entity).getAttackTarget() != owner && ((EntityLiving)entity).getAttackTarget() != this : true;

		}
		else
		{
			if (entity instanceof IEntityOwnable && ((IEntityOwnable)entity).getOwnerId() == null)
				return true;
		}
		return super.isOnSameTeam(entity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean getAlwaysRenderNameTagForRender()
	{
		return selected ? true : super.getAlwaysRenderNameTagForRender();
	}
	
	@Override
	public String getName()
	{
		if (world.isRemote)
		{
			if (super.hasCustomName())
				return selected ? getCustomNameTag() + " (" + TextFormatting.GOLD + stance.getName() + TextFormatting.RESET + ")" : getCustomNameTag();
			else
			{
				String s = EntityList.getEntityString(this);
				return selected ? TranslateUtil.translate("entity." + (s == null ? "generic" : s) + ".name") + " (" + TextFormatting.GOLD + stance.getName() + TextFormatting.RESET + ")" : super.getName();
			}
		}
		else
			return super.getName();
	}
	
	@Override
	public boolean hasCustomName()
	{
		return world.isRemote ? selected ? true : super.hasCustomName() : super.hasCustomName();
	}
	
	@Override
	public void setAttackTarget(@Nullable EntityLivingBase entity)
	{
		if (entity != null)
			healTime = Short.MIN_VALUE;
		else
			healTime = healTime > 0 ? healTime : 0;
		super.setAttackTarget(entity);
	}
	
	@Override
	protected boolean canDespawn()
	{
		return false;
	}
	
	//----- GETTERS & SETTERS -----\\
	public EnumAIStance getStance()
	{
		return stance;
	}
	
	public void setStance(@Nonnull EnumAIStance stance)
	{
		EnumAIStance oldStance = this.stance;
		EventStanceChange eventStance = new EventStanceChange(this, oldStance, stance);
		MinecraftForge.EVENT_BUS.post(eventStance);
		this.stance = eventStance.getStance();
		onStanceChange(oldStance);
		if (!world.isRemote)
		{
			clearTasks(tasks);
			clearTasks(targetTasks);
			onAIChange();
		}
	}
	
	public void setStance(int stanceID)
	{
		EnumAIStance[] stances = EnumAIStance.values();
		EnumAIStance stance = stances[MathHelper.clamp(stanceID, 0, stances.length - 1)];
		EnumAIStance oldStance = this.stance;
		EventStanceChange eventStance = new EventStanceChange(this, oldStance, stance);
		MinecraftForge.EVENT_BUS.post(eventStance);
		this.stance = eventStance.getStance();
		onStanceChange(oldStance);
		
		if (!world.isRemote)
		{
			clearTasks(tasks);
			clearTasks(targetTasks);
			onAIChange();
		}
	}
	
	private void clearTasks(EntityAITasks taskList)
	{
		new ArrayList<EntityAITasks.EntityAITaskEntry>(taskList.taskEntries).forEach(task -> taskList.removeTask(task.action));
	}
	
	public long getVigor()
	{
		return (long) Math.max(0.0D, getBaseVigor() + getEntityAttribute(VIGOR).getAttributeValue());
	}
	
	public void setVigor(long vigor)
	{
		if (!world.isRemote)
		{
			IAttributeInstance attribute = getEntityAttribute(VIGOR);
			attribute.removeModifier(VIGOR_UUID);
			attribute.applyModifier(new AttributeModifier(VIGOR_UUID, "Engendered Vigor", (double) vigor, 0));
		}
	}
	
	public long getStrength()
	{
		return (long) Math.max(0.0D, getBaseStrength() + getEntityAttribute(STRENGTH).getAttributeValue());
	}
	
	public void setStrength(long strength)
	{
		if (!world.isRemote)
		{
			IAttributeInstance attribute = getEntityAttribute(STRENGTH);
			attribute.removeModifier(STRENGTH_UUID);
			attribute.applyModifier(new AttributeModifier(STRENGTH_UUID, "Engendered Strength", (double) strength, 0));
		}
	}
	
	public long getStamina()
	{
		return (long) Math.max(0.0D, getBaseStamina() + getEntityAttribute(STAMINA).getAttributeValue());
	}
	
	public void setStamina(long stamina)
	{
		if (!world.isRemote)
		{
			IAttributeInstance attribute = getEntityAttribute(STAMINA);
			attribute.removeModifier(STAMINA_UUID);
			attribute.applyModifier(new AttributeModifier(STAMINA_UUID, "Engendered Stamina", (double) stamina, 0));
		}
	}
	
	public long getIntelligence()
	{
		return (long) Math.max(0.0D, getBaseIntelligence() + getEntityAttribute(INTELLIGENCE).getAttributeValue());
	}
	
	public void setIntelligence(long intelligence)
	{
		if (!world.isRemote)
		{
			IAttributeInstance attribute = getEntityAttribute(INTELLIGENCE);
			attribute.removeModifier(INTELLIGENCE_UUID);
			attribute.applyModifier(new AttributeModifier(INTELLIGENCE_UUID, "Engendered Intelligence", (double) intelligence, 0));
		}
	}
	
	public long getDexterity()
	{
		return (long) Math.max(0.0D, getBaseDexterity() + getEntityAttribute(DEXTERITY).getAttributeValue());
	}
	
	public void setDexterity(long dexterity)
	{
		if (!world.isRemote)
		{
			IAttributeInstance attribute = getEntityAttribute(DEXTERITY);
			attribute.removeModifier(DEXTERITY_UUID);
			attribute.applyModifier(new AttributeModifier(DEXTERITY_UUID, "Engendered Dexterity", (double) dexterity, 0));
		}
	}
	
	public long getAgility()
	{
		return (long) Math.max(0.0D, getBaseAgility() + getEntityAttribute(AGILITY).getAttributeValue());
	}
	
	public void setAgility(long agility)
	{
		if (!world.isRemote)
		{
			IAttributeInstance attribute = getEntityAttribute(AGILITY);
			attribute.removeModifier(AGILITY_UUID);
			attribute.applyModifier(new AttributeModifier(AGILITY_UUID, "Engendered Agility", (double) agility, 0));
		}
	}
	
	protected void updateOwner()
	{
		UUID uuid = dataManager.get(OWNER_UUID).orNull();
		owner = null;
		
		if (uuid != null)
		{
			List<Entity> entities = new ArrayList<Entity>(world.loadedEntityList);
			for (Entity entity : entities)
				if (uuid.equals(entity.getUniqueID()))
				{
					owner = entity;
					break;
				}
		}
	}
	
	public boolean hasOwner()
	{
		return dataManager.get(OWNER_UUID).isPresent();
	}
	
	@Override
	public Entity getOwner()
	{
		if (owner == null && dataManager.get(OWNER_UUID).isPresent())
			updateOwner();
		return owner;
	}

	@Override
	public UUID getOwnerId()
	{
		return dataManager.get(OWNER_UUID).orNull();
	}
	
	public void setOwner(EntityLivingBase entity)
	{
		dataManager.set(OWNER_UUID, Optional.fromNullable(entity.getUniqueID()));
		owner = entity;
	}
	
	public void setOwner(UUID uuid)
	{
		dataManager.set(OWNER_UUID, Optional.fromNullable(uuid));
		updateOwner();
	}
	
	//----- ABSTRACTS -----\\
	public abstract EnumTier getTier();
	public abstract EnumAIStance getDefaultStance();
	public abstract long getBaseVigor();
	public abstract long getBaseStrength();
	public abstract long getBaseStamina();
	public abstract long getBaseIntelligence();
	public abstract long getBaseDexterity();
	public abstract long getBaseAgility();
}