package net.mrbt0907.ageofminecraft.entity.tier3;

import java.util.Calendar;
import java.util.List;
import java.util.function.BiPredicate;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityChicken;
import net.mrbt0907.ageofminecraft.entity.tier2.EntityVillager;
import net.mrbt0907.ageofminecraft.entity.tier4.EntityPigZombie;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;
import net.mrbt0907.ageofminecraft.util.mrbtutil.WorldUtils;

public class EntityZombie extends EntityEngendered
{
	public static final BiPredicate<Entity, Entity> IS_VALID_ZOMBIE_MOUNT = new BiPredicate<Entity, Entity>()
	{
		@Override
		public boolean test(Entity entity, Entity target)
		{
			return target.isEntityAlive() && !target.isBeingRidden() && entity.isOnSameTeam(target) && (target instanceof EntityChicken || target instanceof net.minecraft.entity.passive.EntityChicken);
		}
	};
	
	private static final DataParameter<Integer> ZOMBIE_VARIANT = EntityDataManager.createKey(EntityZombie.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> VILLAGER_TYPE = EntityDataManager.createKey(EntityZombie.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> CONVERTING = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
	private static final Item END_ROD = Item.getItemFromBlock(Blocks.END_ROD);
	private int conversionTime;
	private int helmetCount = 1;
	
	public EntityZombie(World worldIn)
	{
		super(worldIn);
		setSize(0.5F, 1.95F);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(3, new EntityAIWander(this, 1.0D, 80));
		tasks.addTask(8, new EntityAILookIdle(this));
	}
	
	protected void entityInit()
	{
		super.entityInit();
		getDataManager().register(ZOMBIE_VARIANT, Integer.valueOf(0));
		getDataManager().register(VILLAGER_TYPE, Integer.valueOf(0));
		getDataManager().register(CONVERTING, Boolean.valueOf(false));
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.423000000417232513D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("ZombieType", getZombieType());
		if (isVillager())
		{
			tagCompound.setBoolean("IsVillager", true);
			tagCompound.setInteger("VillagerProfession", getVillagerType());
		}
		tagCompound.setInteger("ConversionTime", isConverting() ? conversionTime : -1);
		tagCompound.setInteger("Helmets", helmetCount);
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		setZombieType(tagCompund.getInteger("ZombieType"));
		if (tagCompund.getBoolean("IsVillager"))
		{
			if (tagCompund.hasKey("VillagerProfession", 99))
				setVillagerType(tagCompund.getInteger("VillagerProfession"));
			else
				setVillagerType(world.rand.nextInt(5));
		}
		if ((tagCompund.hasKey("Helmets", 99)))
			helmetCount = tagCompund.getInteger("Helmets");
	}

	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		if (livingdata == null)
			livingdata = new GroupData(world.rand.nextFloat() < ForgeModContainer.zombieBabyChance, world.rand.nextFloat() < 0.05F);
		
		if ((livingdata instanceof GroupData))
		{
			GroupData groupdata = (GroupData)livingdata;
			Biome biome = world.getBiome(new BlockPos(this));
			
			if (biome instanceof BiomeDesert && rand.nextInt(5) != 0)
			{
				setZombieType(1);
				setVillagerType(0);
			}

			if (groupdata.isVillager)
				setVillagerType(rand.nextInt(5));
			if (groupdata.isChild)
			{
				setChild(true);
				setGrowingAge(-60000);
				if (world.rand.nextFloat() < 0.05D)
				{
					List<EntityChicken> list = world.getEntitiesWithinAABB(EntityChicken.class, getEntityBoundingBox().grow(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);
					if (!list.isEmpty())
					{
						EntityChicken entity = (EntityChicken)list.get(0);
						entity.setChickenJockey(true);
						startRiding(entity);
						entity.setOwner(getOwnerId());
					}
				}
				else if (world.rand.nextFloat() < 0.05D)
				{
					EntityChicken entity = new EntityChicken(world);
					entity.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
					entity.onInitialSpawn(difficulty, (IEntityLivingData)null);
					entity.setChickenJockey(true);
					entity.setOwner(getOwnerId());
					world.spawnEntity(entity);
					startRiding(entity);
				}
			}
		}
		setEquipmentBasedOnDifficulty(difficulty);
		setEnchantmentBasedOnDifficulty(difficulty);
		if (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
		{
			Calendar calendar = world.getCurrentDate();
			
			if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && rand.nextFloat() < 0.25F)
			{
				setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
				inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
			}
		}

		return livingdata;
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		ItemStack hats = helmetCount > 0 ? new ItemStack(Items.LEATHER_HELMET, helmetCount) : ItemStack.EMPTY;
		inventory.setInventorySlotContents(7, hats);
		
		if (!world.isRemote && isConverting() && --conversionTime <= 0)
			convertToVillager();
	}
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
	}

	public void updateRidden()
	{
		super.updateRidden();
		Entity rider = getRidingEntity();
		if (rider instanceof EntityCreature)
		{
			EntityCreature entity = (EntityCreature) rider;
			EntityLivingBase target = getAttackTarget();
			renderYawOffset = entity.renderYawOffset;
			entity.rotationPitch = rotationPitch;
			entity.rotationYawHead = rotationYawHead;
			if (target != null)
				entity.setAttackTarget(target);
			if (ticksExisted % 40 == 0)
				renderYawOffset = (rotationYaw = rotationYawHead);
		}
	}
	
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		if (!world.isRemote)
		{
			if (helmetCount > 0)
			{
				dropItem(Items.LEATHER_HELMET, helmetCount);
			}
		}

		if (((cause.getTrueSource() instanceof EntityCreeper)) && (((EntityCreeper)cause.getTrueSource()).getPowered()))
		{
			entityDropItem(new ItemStack(Items.SKULL, 1, 2), 0.0F);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void onKillEntity(EntityLivingBase victim)
	{
		super.onKillEntity(victim);
		
		if (world.getDifficulty().getDifficultyId() > EnumDifficulty.EASY.getDifficultyId() && (victim instanceof EntityVillager || victim instanceof net.minecraft.entity.passive.EntityVillager))
		{
			EntityZombie entity = new EntityZombie(world);
			entity.rotationPitch = victim.rotationPitch;
			entity.renderYawOffset = entity.rotationYaw = entity.rotationYawHead = victim.rotationYawHead;
			entity.copyLocationAndAnglesFrom(victim);
			world.removeEntity(victim);
			entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), new EntityZombie.GroupData(false, true));
			entity.setVillagerType(victim instanceof EntityVillager ? ((EntityVillager)victim).getProfession() : ((net.minecraft.entity.passive.EntityVillager)victim).getProfession());
			entity.setChild(victim.isChild());
			entity.setNoAI(((EntityCreature)victim).isAIDisabled());
			entity.setOwner(getOwnerId());
			if (victim.hasCustomName())
				entity.setCustomNameTag(victim.getCustomNameTag());
			world.spawnEntity(entity);
			world.playEvent((EntityPlayer)null, 1026, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
		}
	}
	
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		if (isOnSameTeam(player))
		{
			ItemStack stack = player.getHeldItem(hand); Item item = stack.getItem();
			boolean isOwner = player.getUniqueID().equals(getOwnerId());
			if (stack.isEmpty())
			{
				if (isOwner && isChild() && player.isSneaking() && getRidingEntity() == null)
				{
					Entity entity = WorldUtils.getEntity(world, this, IS_VALID_ZOMBIE_MOUNT);
					if (entity != null)
					{
						entity.ticksExisted = 0;
						startRiding(entity);
						playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
						return true;
					}
				}
			}
			else if (item == Items.LEATHER_HELMET)
			{
				helmetCount += 1;
				playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
				player.swingArm(hand);
				if (!world.isRemote)
				stack.shrink(1);
				return true;
			}
			else if (getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() && (getSlotForItemStack(stack) == EntityEquipmentSlot.HEAD || item.equals(Items.BONE) || item.equals(END_ROD) || item.equals(Items.FEATHER)))
			{
				setItemStackToSlot(EntityEquipmentSlot.HEAD, stack);
				playEquipSound(stack);
				player.swingArm(hand);
				if (!world.isRemote)
				{
					setItemStackToSlot(EntityEquipmentSlot.HEAD, stack.copy());
					stack.shrink(1);
				}
				return true;
			}
			else if (getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() && getSlotForItemStack(stack) == EntityEquipmentSlot.CHEST)
			{
				setItemStackToSlot(EntityEquipmentSlot.CHEST, stack);
				playEquipSound(stack);
				player.swingArm(hand);
				if (!world.isRemote)
				{
					setItemStackToSlot(EntityEquipmentSlot.CHEST, stack.copy());
					stack.shrink(1);
				}
				return true;
			}
			else if (getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() && getSlotForItemStack(stack) == EntityEquipmentSlot.LEGS)
			{
				setItemStackToSlot(EntityEquipmentSlot.LEGS, stack);
				playEquipSound(stack);
				player.swingArm(hand);
				if (!world.isRemote)
				{
					setItemStackToSlot(EntityEquipmentSlot.LEGS, stack.copy());
					stack.shrink(1);
				}
				return true;
			}
			else if (getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty() && getSlotForItemStack(stack) == EntityEquipmentSlot.FEET)
			{
				setItemStackToSlot(EntityEquipmentSlot.FEET, stack);
				playEquipSound(stack);
				player.swingArm(hand);
				if (!world.isRemote)
				{
					setItemStackToSlot(EntityEquipmentSlot.FEET, stack.copy());
					stack.shrink(1);
				}
				return true;
			}
			else if (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty() && (item instanceof ItemSword || item instanceof ItemTool || item == Items.BOW))
			{
				playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 2.0F);
				player.swingArm(hand);
				if (!world.isRemote)
				{
					setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack.copy());
					stack.shrink(1);
				}
				return true;
			}
			else if (getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).isEmpty() && (item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemFood && !(item instanceof ItemAppleGold) || item == Items.GOLDEN_APPLE && stack.getMetadata() == 0 && isVillager() && isPotionActive(MobEffects.WEAKNESS) || item == Items.TIPPED_ARROW || item == Items.TOTEM_OF_UNDYING))
			{
				playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 2.0F);
				player.swingArm(hand);
				if (!world.isRemote)
				{
					setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack.copy());
					stack.shrink(1);
				}
				return true;
			}
		}
		/*if (this instanceof EntityPigZombie && !stack.isEmpty() && stack.getItem() == Items.SADDLE && getRidingEntity() == null && ((hasOwner(player)) || (player.isOnSameTeam(this))))
		{
			playSound(SoundEvents.ENTITY_PIG_SADDLE, 0.5F, 1.0F);
			player.startRiding(this);
			return true;
		}*/
		
		return super.processInteract(player, hand);
	}
	
	//----- OVERRIDES -----\\
	protected ResourceLocation getLootTable()
	{
		switch (getZombieType())
		{
			case 1:
				return LootRegistry.ENTITIES_HUSK;
			case 2:
				return LootRegistry.ENTITIES_PRISON_ZOMBIE;
			default:
				if (isVillager())
					return LootRegistry.ENTITIES_ZOMBIE_VILLAGER;
				else
					return LootRegistry.ENTITIES_ZOMBIE;
		}
	}
	protected SoundEvent getAmbientSound() {return getZombieType() == 1 ? SoundEvents.ENTITY_HUSK_AMBIENT : (isVillager() ? SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT : SoundEvents.ENTITY_ZOMBIE_AMBIENT);}
	protected SoundEvent getHurtSound(DamageSource source) {return getZombieType() == 1 ? SoundEvents.ENTITY_HUSK_HURT : (isVillager() ? SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT : SoundEvents.ENTITY_ZOMBIE_HURT);}
	protected SoundEvent getDeathSound() {return getZombieType() == 1 ? SoundEvents.ENTITY_HUSK_DEATH : (isVillager() ? SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH : SoundEvents.ENTITY_ZOMBIE_DEATH);}
	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		if (this instanceof EntityPigZombie)
			playSound(SoundEvents.ENTITY_PIG_STEP, 0.15F, 1.0F);
		playSound(getZombieType() == 1 ? SoundEvents.ENTITY_HUSK_STEP : (isVillager() ? SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP : SoundEvents.ENTITY_ZOMBIE_STEP), 0.15F, 1.0F);
	}
		
	@Override
	public EnumTier getTier() {return EnumTier.TIER3;}
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
	
	public boolean isVillager()
	{
		return ((Integer)getDataManager().get(VILLAGER_TYPE)).intValue() > 0;
	}
	
	public int getVillagerType()
	{
		return ((Integer)getDataManager().get(VILLAGER_TYPE)).intValue() - 1;
	}
	
	public void setVillagerType(int villagerType)
	{
		getDataManager().set(VILLAGER_TYPE, Integer.valueOf(villagerType + 1));
	}
	
	public int getZombieType()
	{
		return ((Integer)getDataManager().get(ZOMBIE_VARIANT)).intValue() - 1;
	}
	
	public void setZombieType(int villagerType)
	{
		getDataManager().set(ZOMBIE_VARIANT, Integer.valueOf(villagerType + 1));
	}
	
	public boolean isEntityUndead()
	{
		return true;
	}

	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	{
		if (rand.nextFloat() < (world.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.05F))
		{
			int i = rand.nextInt(3);
				
			if (i == 0)
			{
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
				if (getRNG().nextInt(3) == 0)
					setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.IRON_SWORD));
			}
			else
			{
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
				if (getRNG().nextInt(3) == 0)
					setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.IRON_SHOVEL));
			}
		}

		if (rand.nextFloat() < 0.25F * difficulty.getClampedAdditionalDifficulty())
		{
			int i = rand.nextInt(2);
			float f = world.getDifficulty() == EnumDifficulty.HARD ? 0.325F : 0.25F;
				
			if (rand.nextFloat() < 0.1F)
				++i;

			if (rand.nextFloat() < 0.15F)
				++i;

			if (rand.nextFloat() < 0.2F)
				++i;

			boolean flag = true;
				
			for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
			{
				if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
				{
					ItemStack itemstack = getItemStackFromSlot(entityequipmentslot);
					if (!flag && rand.nextFloat() < f)
						break;
					flag = false;
					if (itemstack.isEmpty())
					{
						Item item = getArmorByChance(entityequipmentslot, i);
							
						if (item != null)
							setItemStackToSlot(entityequipmentslot, new ItemStack(item));
					}
				}
			}
		}
	}
	
	public float getEyeHeight()
	{
		return (getZombieType() == 1 ? height * 0.9F : height * 0.87F);
	}
		
	protected boolean canEquipItem(ItemStack p_175448_1_)
	{
		return (p_175448_1_.getItem() == Items.EGG) && (isChild()) && (isRiding()) ? false : super.canEquipItem(p_175448_1_);
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 16)
		{
			if (!isSilent())
				world.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, getSoundCategory(), 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
		}
		else
			super.handleStatusUpdate(id);
	}

	public boolean isConverting()
	{
		return ((Boolean)getDataManager().get(CONVERTING)).booleanValue();
	}
	
	protected void convertToVillager()
	{
		net.mrbt0907.ageofminecraft.entity.tier2.EntityVillager entityvillager = new net.mrbt0907.ageofminecraft.entity.tier2.EntityVillager(world);
		entityvillager.copyLocationAndAnglesFrom(this);
		entityvillager.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null);
		entityvillager.renderYawOffset = entityvillager.rotationYaw = entityvillager.rotationYawHead = rotationYawHead;
		entityvillager.rotationPitch = rotationPitch;
		entityvillager.setNoAI(isAIDisabled());
		entityvillager.setProfession(getVillagerType());
		entityvillager.setOwnerId(getOwnerId());
		if (hasCustomName())
		{
			entityvillager.setCustomNameTag(getCustomNameTag());
		}
		if (!world.isRemote)
		{
			if (helmetCount > 0)
			{
				dropItem(Items.LEATHER_HELMET, helmetCount);
			}
		}
		world.spawnEntity(entityvillager);
		entityvillager.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
		world.playEvent((EntityPlayer)null, 1027, new BlockPos((int)posX, (int)posY, (int)posZ), 0);
		world.removeEntity(this);
	}
	
	public class GroupData implements IEntityLivingData
	{
		public boolean isChild;
		public boolean isVillager;
		
		public GroupData(boolean isBaby, boolean isVillagerZombie)
		{
			isChild = isBaby;
			isVillager = isVillagerZombie;
		}
	}
}