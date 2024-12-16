package net.mrbt0907.ageofminecraft.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.MoverType;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
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

	public final Predicate<Entity> canCollide = EntitySelectors.getTeamCollisionPredicate(this);
	public final InventoryBasic inventory;
	/** The current targeting ai of the mob*/
	protected EnumAIStance stance;
	protected Entity owner;
	/** The positions that the mob will patrol. If null, follow owner*/
	public BlockPos[] followPos;
	/** How long until healing can start*/
	protected short healTime;
	
	/** Whether the mob is selected by the player*/
	@SideOnly(Side.CLIENT)
	public boolean selected;
	/** Current X position of the mob cape */
	@SideOnly(Side.CLIENT)
	public double chasingPosX;
	/** Current Y position of the mob cape */
	@SideOnly(Side.CLIENT)
	public double chasingPosY;
	/** Current Z position of the mob's cape */
	@SideOnly(Side.CLIENT)
	public double chasingPosZ;
	/** Previous X position of the mob cape */
	@SideOnly(Side.CLIENT)
	public double prevChasingPosX;
	/** Previous Y position of the mob cape */
	@SideOnly(Side.CLIENT)
	public double prevChasingPosY;
	/** Previous Z position of the mob cape */
	@SideOnly(Side.CLIENT)
	public double prevChasingPosZ;
	
	public EntityEngendered(World world)
	{
		super(world);
		if (world.isRemote)
			stance = getDefaultStance();
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
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		if (world.isRemote)
			updateCape();
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
	
	private void updateCape()
	{
		prevChasingPosX = chasingPosX;
		prevChasingPosY = chasingPosY;
		prevChasingPosZ = chasingPosZ;
		double d0 = posX - chasingPosX;
		double d1 = posY - chasingPosY;
		double d2 = posZ - chasingPosZ;

		if (d0 > 10.0D)
		{
			chasingPosX = posX;
			prevChasingPosX = chasingPosX;
		}

		if (d2 > 10.0D)
		{
			chasingPosZ = posZ;
			prevChasingPosZ = chasingPosZ;
		}

		if (d1 > 10.0D)
		{
			chasingPosY = posY;
			prevChasingPosY = chasingPosY;
		}

		if (d0 < -10.0D)
		{
			chasingPosX = posX;
			prevChasingPosX = chasingPosX;
		}

		if (d2 < -10.0D)
		{
			chasingPosZ = posZ;
			prevChasingPosZ = chasingPosZ;
		}

		if (d1 < -10.0D)
		{
			chasingPosY = posY;
			prevChasingPosY = chasingPosY;
		}

		chasingPosX += d0 * 0.25D;
		chasingPosZ += d2 * 0.25D;
		chasingPosY += d1 * 0.25D;
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
	public void move(MoverType type, double x, double y, double z)
	{
		if (this.noClip)
        {
            this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            this.resetPositionToBB();
        }
        else
        {
            if (type == MoverType.PISTON)
            {
                long i = this.world.getTotalWorldTime();

                if (i != this.pistonDeltasGameTime)
                {
                    Arrays.fill(this.pistonDeltas, 0.0D);
                    this.pistonDeltasGameTime = i;
                }

                if (x != 0.0D)
                {
                    int j = EnumFacing.Axis.X.ordinal();
                    double d0 = MathHelper.clamp(x + this.pistonDeltas[j], -0.51D, 0.51D);
                    x = d0 - this.pistonDeltas[j];
                    this.pistonDeltas[j] = d0;

                    if (Math.abs(x) <= 9.999999747378752E-6D)
                    {
                        return;
                    }
                }
                else if (y != 0.0D)
                {
                    int l4 = EnumFacing.Axis.Y.ordinal();
                    double d12 = MathHelper.clamp(y + this.pistonDeltas[l4], -0.51D, 0.51D);
                    y = d12 - this.pistonDeltas[l4];
                    this.pistonDeltas[l4] = d12;

                    if (Math.abs(y) <= 9.999999747378752E-6D)
                    {
                        return;
                    }
                }
                else
                {
                    if (z == 0.0D)
                    {
                        return;
                    }

                    int i5 = EnumFacing.Axis.Z.ordinal();
                    double d13 = MathHelper.clamp(z + this.pistonDeltas[i5], -0.51D, 0.51D);
                    z = d13 - this.pistonDeltas[i5];
                    this.pistonDeltas[i5] = d13;

                    if (Math.abs(z) <= 9.999999747378752E-6D)
                    {
                        return;
                    }
                }
            }

            this.world.profiler.startSection("move");
            double d10 = this.posX;
            double d11 = this.posY;
            double d1 = this.posZ;

            if (this.isInWeb)
            {
                this.isInWeb = false;
                x *= 0.25D;
                y *= 0.05000000074505806D;
                z *= 0.25D;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            double d2 = x;
            double d3 = y;
            double d4 = z;

            if ((type == MoverType.SELF || type == MoverType.PLAYER) && this.onGround && this.isSneaking())
            {
                for (; x != 0.0D && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(x, (double)(-this.stepHeight), 0.0D)).isEmpty(); d2 = x)
                {
                    if (x < 0.05D && x >= -0.05D)
                    {
                        x = 0.0D;
                    }
                    else if (x > 0.0D)
                    {
                        x -= 0.05D;
                    }
                    else
                    {
                        x += 0.05D;
                    }
                }

                for (; z != 0.0D && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(0.0D, (double)(-this.stepHeight), z)).isEmpty(); d4 = z)
                {
                    if (z < 0.05D && z >= -0.05D)
                    {
                        z = 0.0D;
                    }
                    else if (z > 0.0D)
                    {
                        z -= 0.05D;
                    }
                    else
                    {
                        z += 0.05D;
                    }
                }

                for (; x != 0.0D && z != 0.0D && this.world.getCollisionBoxes(this, this.getEntityBoundingBox().offset(x, (double)(-this.stepHeight), z)).isEmpty(); d4 = z)
                {
                    if (x < 0.05D && x >= -0.05D)
                    {
                        x = 0.0D;
                    }
                    else if (x > 0.0D)
                    {
                        x -= 0.05D;
                    }
                    else
                    {
                        x += 0.05D;
                    }

                    d2 = x;

                    if (z < 0.05D && z >= -0.05D)
                    {
                        z = 0.0D;
                    }
                    else if (z > 0.0D)
                    {
                        z -= 0.05D;
                    }
                    else
                    {
                        z += 0.05D;
                    }
                }
            }

            List<AxisAlignedBB> list1 = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().expand(x, y, z));
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

            if (y != 0.0D)
            {
                int k = 0;

                for (int l = list1.size(); k < l; ++k)
                {
                    y = ((AxisAlignedBB)list1.get(k)).calculateYOffset(this.getEntityBoundingBox(), y);
                }

                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));
            }

            if (x != 0.0D)
            {
                int j5 = 0;

                for (int l5 = list1.size(); j5 < l5; ++j5)
                {
                    x = ((AxisAlignedBB)list1.get(j5)).calculateXOffset(this.getEntityBoundingBox(), x);
                }

                if (x != 0.0D)
                {
                    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0D, 0.0D));
                }
            }

            if (z != 0.0D)
            {
                int k5 = 0;

                for (int i6 = list1.size(); k5 < i6; ++k5)
                {
                    z = ((AxisAlignedBB)list1.get(k5)).calculateZOffset(this.getEntityBoundingBox(), z);
                }

                if (z != 0.0D)
                {
                    this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, z));
                }
            }

            boolean flag = this.onGround || d3 != y && d3 < 0.0D;

            if (this.stepHeight > 0.0F && flag && (d2 != x || d4 != z))
            {
                double d14 = x;
                double d6 = y;
                double d7 = z;
                AxisAlignedBB axisalignedbb1 = this.getEntityBoundingBox();
                this.setEntityBoundingBox(axisalignedbb);
                y = (double)this.stepHeight;
                List<AxisAlignedBB> list = this.world.getCollisionBoxes(this, this.getEntityBoundingBox().expand(d2, y, d4));
                AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.expand(d2, 0.0D, d4);
                double d8 = y;
                int j1 = 0;

                for (int k1 = list.size(); j1 < k1; ++j1)
                {
                    d8 = ((AxisAlignedBB)list.get(j1)).calculateYOffset(axisalignedbb3, d8);
                }

                axisalignedbb2 = axisalignedbb2.offset(0.0D, d8, 0.0D);
                double d18 = d2;
                int l1 = 0;

                for (int i2 = list.size(); l1 < i2; ++l1)
                {
                    d18 = ((AxisAlignedBB)list.get(l1)).calculateXOffset(axisalignedbb2, d18);
                }

                axisalignedbb2 = axisalignedbb2.offset(d18, 0.0D, 0.0D);
                double d19 = d4;
                int j2 = 0;

                for (int k2 = list.size(); j2 < k2; ++j2)
                {
                    d19 = ((AxisAlignedBB)list.get(j2)).calculateZOffset(axisalignedbb2, d19);
                }

                axisalignedbb2 = axisalignedbb2.offset(0.0D, 0.0D, d19);
                AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
                double d20 = y;
                int l2 = 0;

                for (int i3 = list.size(); l2 < i3; ++l2)
                {
                    d20 = ((AxisAlignedBB)list.get(l2)).calculateYOffset(axisalignedbb4, d20);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, d20, 0.0D);
                double d21 = d2;
                int j3 = 0;

                for (int k3 = list.size(); j3 < k3; ++j3)
                {
                    d21 = ((AxisAlignedBB)list.get(j3)).calculateXOffset(axisalignedbb4, d21);
                }

                axisalignedbb4 = axisalignedbb4.offset(d21, 0.0D, 0.0D);
                double d22 = d4;
                int l3 = 0;

                for (int i4 = list.size(); l3 < i4; ++l3)
                {
                    d22 = ((AxisAlignedBB)list.get(l3)).calculateZOffset(axisalignedbb4, d22);
                }

                axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d22);
                double d23 = d18 * d18 + d19 * d19;
                double d9 = d21 * d21 + d22 * d22;

                if (d23 > d9)
                {
                    x = d18;
                    z = d19;
                    y = -d8;
                    this.setEntityBoundingBox(axisalignedbb2);
                }
                else
                {
                    x = d21;
                    z = d22;
                    y = -d20;
                    this.setEntityBoundingBox(axisalignedbb4);
                }

                int j4 = 0;

                for (int k4 = list.size(); j4 < k4; ++j4)
                {
                    y = ((AxisAlignedBB)list.get(j4)).calculateYOffset(this.getEntityBoundingBox(), y);
                }

                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));

                if (d14 * d14 + d7 * d7 >= x * x + z * z)
                {
                    x = d14;
                    y = d6;
                    z = d7;
                    this.setEntityBoundingBox(axisalignedbb1);
                }
            }

            this.world.profiler.endSection();
            this.world.profiler.startSection("rest");
            this.resetPositionToBB();
            this.collidedHorizontally = d2 != x || d4 != z;
            this.collidedVertically = d3 != y;
            this.onGround = this.collidedVertically && d3 < 0.0D;
            this.collided = this.collidedHorizontally || this.collidedVertically;
            int j6 = MathHelper.floor(this.posX);
            int i1 = MathHelper.floor(this.posY - 0.20000000298023224D);
            int k6 = MathHelper.floor(this.posZ);
            BlockPos blockpos = new BlockPos(j6, i1, k6);
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() == Material.AIR)
            {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate1 = this.world.getBlockState(blockpos1);
                Block block1 = iblockstate1.getBlock();

                if (block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate)
                {
                    iblockstate = iblockstate1;
                    blockpos = blockpos1;
                }
            }

            this.updateFallState(y, this.onGround, iblockstate, blockpos);

            if (d2 != x)
            {
                this.motionX = 0.0D;
            }

            if (d4 != z)
            {
                this.motionZ = 0.0D;
            }

            Block block = iblockstate.getBlock();

            if (d3 != y)
            {
                block.onLanded(this.world, this);
            }

            if (this.canTriggerWalking() && (!this.onGround || !this.isSneaking()) && !this.isRiding())
            {
                double d15 = this.posX - d10;
                double d16 = this.posY - d11;
                double d17 = this.posZ - d1;

                if (block != Blocks.LADDER)
                {
                    d16 = 0.0D;
                }

                if (block != null && this.onGround)
                {
                    block.onEntityWalk(this.world, blockpos, this);
                }

                this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt(d15 * d15 + d17 * d17) * 0.6D);
                this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt(d15 * d15 + d16 * d16 + d17 * d17) * 0.6D);

                if (this.distanceWalkedOnStepModified > (float)this.nextStepDistance && iblockstate.getMaterial() != Material.AIR)
                {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;

                    if (this.isInWater())
                    {
                        Entity entity = this.isBeingRidden() && this.getControllingPassenger() != null ? this.getControllingPassenger() : this;
                        float f = entity == this ? 0.35F : 0.4F;
                        float f1 = MathHelper.sqrt(entity.motionX * entity.motionX * 0.20000000298023224D + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ * 0.20000000298023224D) * f;

                        if (f1 > 1.0F)
                        {
                            f1 = 1.0F;
                        }

                        this.playSound(this.getSwimSound(), f1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    }
                    else
                    {
                        this.playStepSound(blockpos, block);
                    }
                }
                else if (this.distanceWalkedOnStepModified > this.nextFlap && this.makeFlySound() && iblockstate.getMaterial() == Material.AIR)
                {
                    this.nextFlap = this.playFlySound(this.distanceWalkedOnStepModified);
                }
            }

            try
            {
                this.doBlockCollisions();
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            boolean flag1 = this.isWet();

            if (this.world.isFlammableWithin(this.getEntityBoundingBox().shrink(0.001D)))
            {
                this.dealFireDamage(1);

                if (!flag1)
                {
                    ++this.fire;

                    if (this.fire == 0)
                    {
                        this.setFire(8);
                    }
                }
            }
            else if (this.fire <= 0)
            {
                this.fire = -this.getFireImmuneTicks();
            }

            if (flag1 && this.isBurning())
            {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                this.fire = -this.getFireImmuneTicks();
            }

            this.world.profiler.endSection();
        }
	}
	
	protected void collideWithNearbyEntities()
	{
		if (world.isRemote) return;
		Iterator<Entity> list = world.loadedEntityList.iterator();
		Entity entity; boolean canCram = false;
		int cram = world.getGameRules().getInt("maxEntityCramming"), size = 0;
		
		
		if (cram > 0 && rand.nextInt(4) == 0)
			canCram = true;
		
		
		while(list.hasNext())
		{
			entity = list.next();
			if (entity.getEntityBoundingBox().intersects(getEntityBoundingBox()) && canCollide.apply(entity))
			{
				collideWithEntity(entity);
				if (canCram && !entity.isRiding())
					size++;
			}
		}
		
		if (canCram && cram < size)
			attackEntityFrom(DamageSource.CRAMMING, 6.0F);
	}
	
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
	
	public void setChild(boolean isChild)
	{
		int age = getGrowingAge();
		setGrowingAge(isChild ? -24000 : age < 0 ? 0 : age);
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