package net.mrbt0907.ageofminecraft.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EngenderedPathNavigator;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EntityAIFollow;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EntityTargetNearest;

/*
 * TODO:
 * - Level System
 * - Team System
 * - Command System
 * - Polymorph System
 */
public abstract class EntityEngendered extends EntityCreature implements IEntityOwnable
{
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
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(EntityEngendered.class, DataSerializers.BOOLEAN);

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
	
	protected Entity owner;
	protected double followRange = 32.0D;
	protected double knockbackResistance = 0.09D;
	
	
	public EntityEngendered(World world)
	{
		super(world);
		aiInit();
	}
	
	protected void aiInit()
	{
		tasks.addTask(5, new EntityAIAttackMelee(this, 1.0D, true));
		tasks.addTask(3, new EntityAIFollow(this, 1.1D));
		targetTasks.addTask(0, new EntityTargetNearest(this, EntityLivingBase.class));
	}
	
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(EXPERIENCE, 0.0F);
		dataManager.register(OWNER_UUID, Optional.<UUID>absent());
		dataManager.register(HERO, false);
		dataManager.register(IS_CHILD, false);
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
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(followRange);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(knockbackResistance);
	}
	
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		if (nbt.hasUniqueId("owner")) setOwner(nbt.getUniqueId("owner"));
		setChild(nbt.getBoolean("IsBaby"));
		
		if (nbt.hasKey("VGR")) setVigor(nbt.getLong("VGR"));
		if (nbt.hasKey("STR")) setStrength(nbt.getLong("STR"));
		if (nbt.hasKey("STA")) setStamina(nbt.getLong("STA"));
		if (nbt.hasKey("INT")) setIntelligence(nbt.getLong("INT"));
		if (nbt.hasKey("DEX")) setDexterity(nbt.getLong("DEX"));
		if (nbt.hasKey("AGI")) setAgility(nbt.getLong("AGI"));
	}
	
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		if (hasOwner())
			nbt.setUniqueId("owner", getOwnerId());
		nbt.setBoolean("IsBaby", isChild());
		nbt.setDouble("VGR", getVigor());
		nbt.setDouble("STR", getStrength());
		nbt.setDouble("STA", getStamina());
		nbt.setDouble("INT", getIntelligence());
		nbt.setDouble("DEX", getDexterity());
		nbt.setDouble("AGI", getAgility());
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		
	}
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
	}
	
	protected void onDeathUpdate()
	{
		super.onDeathUpdate();
	}
	
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isOnSameTeam(source.getTrueSource()) && !world.getGameRules().getBoolean("friendlyFire"))
			return false;
		return super.attackEntityFrom(source, amount);
	}
	
	//----- OVERRIDES -----\\
	
	@Override
	public boolean isOnSameTeam(Entity entity)
	{
		if (this.equals(entity) || entity == null) return true;
		
		if (hasOwner())
		{
			if (owner != null && owner.equals(entity))
				return true;
			if (entity instanceof IEntityOwnable && getOwnerId().equals(((IEntityOwnable)entity).getOwnerId()))
				return true;
		}
		else
		{
			if (entity instanceof IEntityOwnable && ((IEntityOwnable)entity).getOwnerId() == null)
				return true;
		}
		
		return super.isOnSameTeam(entity);
	}
	
	//----- GETTERS & SETTERS -----\\
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
	
	public boolean isChild()
	{
		return dataManager.get(IS_CHILD).booleanValue();
	}
	
	public void setChild(boolean isChild)
	{
		dataManager.set(IS_CHILD, Boolean.valueOf(isChild));
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
	public abstract long getBaseVigor();
	public abstract long getBaseStrength();
	public abstract long getBaseStamina();
	public abstract long getBaseIntelligence();
	public abstract long getBaseDexterity();
	public abstract long getBaseAgility();
}