package net.minecraft.AgeOfMinecraft.entity.tier2;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import javax.annotation.Nullable;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityWitch;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityVillager
extends EntityFriendlyCreature
implements IMerchant, INpc, Light
{
	private static final DataParameter<Integer> PROFESSION = EntityDataManager.createKey(EntityVillager.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> CAREER = EntityDataManager.createKey(EntityVillager.class, DataSerializers.VARINT);
	private boolean isMating;
	private boolean isPlaying;
	Village villageObj;
	private EntityPlayer buyingPlayer;
	private MerchantRecipeList buyingList;
	private int timeUntilReset;
	private boolean needsInitilization;
	private boolean isWillingToMate;
	private int wealth;
	private String lastBuyingPlayer;
	private int careerLevel;
	private InventoryBasic villagerInventory;
	@Deprecated
	private static final ITradeList[][][][] DEFAULT_TRADE_LIST_MAP ={{{{new EmeraldForItems(Items.WHEAT, new PriceInfo(4, 8)),new EmeraldForItems(Items.POTATO, new PriceInfo(4, 8)),new EmeraldForItems(Items.CARROT, new PriceInfo(4, 8)),
	new EmeraldForItems(Items.BEETROOT, new PriceInfo(4, 8)),
	new EmeraldForItems(Item.getItemFromBlock(Blocks.PUMPKIN), new PriceInfo(1, 1)),
	new EmeraldForItems(Item.getItemFromBlock(Blocks.MELON_BLOCK), new PriceInfo(1, 1)),new EmeraldForItems(Items.COAL, new PriceInfo(4, 8)),new EmeraldForItems(Items.REDSTONE, new PriceInfo(4, 8)),new EmeraldForItems(Items.IRON_INGOT, new PriceInfo(2, 4)),new EmeraldForItems(Items.GOLD_INGOT, new PriceInfo(1, 2)),new EmeraldForItems(Items.DIAMOND, new PriceInfo(1, 1)),new EmeraldForItems(Items.ROTTEN_FLESH, new PriceInfo(8, 16)),new EmeraldForItems(Items.BONE, new PriceInfo(8, 16)),new EmeraldForItems(Items.STRING, new PriceInfo(8, 16)),new EmeraldForItems(Items.GUNPOWDER, new PriceInfo(8, 16)),new EmeraldForItems(Items.SPIDER_EYE, new PriceInfo(1, 4)),new EmeraldsForItem(Items.ENDER_PEARL, new PriceInfo(2, 4)),new EmeraldsForItem(Items.BLAZE_ROD, new PriceInfo(4, 8)),new EmeraldsForItem(Items.GHAST_TEAR, new PriceInfo(8, 8)),new EmeraldsForItem(Items.NETHER_STAR, new PriceInfo(64, 64)),new ListItemForEmeralds(Items.BREAD, new PriceInfo(-16, -8)),new ListItemForEmeralds(Items.APPLE, new PriceInfo(-24, -16)),new ListItemForEmeralds(Items.GOLDEN_APPLE, new PriceInfo(4, 4)),new ListItemForEmeralds(new ItemStack(Items.GOLDEN_APPLE, 1, 1), new PriceInfo(24, 24)),
	new ListItemForEmeralds(Items.GOLDEN_CARROT, new PriceInfo(4, 4)),
	new ListItemForEmeralds(Items.COOKIE, new PriceInfo(-64, -64)),
	new ListItemForEmeralds(Items.BAKED_POTATO, new PriceInfo(-32, -16)),
	new ListItemForEmeralds(Items.COOKED_CHICKEN, new PriceInfo(-16, -8)),
	new ListItemForEmeralds(Items.COOKED_MUTTON, new PriceInfo(-16, -8)),
	new ListItemForEmeralds(Items.COOKED_PORKCHOP, new PriceInfo(-16, -8)),
	new ListItemForEmeralds(Items.COOKED_BEEF, new PriceInfo(-16, -8)),
	new ListItemForEmeralds(Items.CAKE, new PriceInfo(1, 1)),
	new ListItemForEmeralds(Items.DIAMOND_HOE, new PriceInfo(2, 2)),
	new ListItemForEmeralds(Items.DIAMOND_AXE, new PriceInfo(3, 3)),
	new ListItemForEmeralds(Items.DIAMOND_SWORD, new PriceInfo(2, 2)),
	new ListItemForEmeralds(Items.SHIELD, new PriceInfo(3, 3)),
	new ListItemForEmeralds(Items.DIAMOND_HELMET, new PriceInfo(5, 5)),
	new ListItemForEmeralds(Items.DIAMOND_CHESTPLATE, new PriceInfo(8, 8)),
	new ListItemForEmeralds(Items.DIAMOND_LEGGINGS, new PriceInfo(7, 7)),
	new ListItemForEmeralds(Items.DIAMOND_BOOTS, new PriceInfo(4, 4)),
	new ItemAndEmeraldToItem(Items.CHORUS_FRUIT, new PriceInfo(64, 64), Items.ELYTRA, new PriceInfo(1, 1)),
	new ItemAndEmeraldToItem(Items.DARK_OAK_DOOR, new PriceInfo(64, 64), Items.TOTEM_OF_UNDYING, new PriceInfo(1, 1)),
	new ListEnchantedBookForEmeralds()
}}
}
};
public EntityVillager(World worldIn)
{
	this(worldIn, 0);
}
public EntityVillager(World worldIn, int professionId)
{
	super(worldIn);
	this.villagerInventory = new InventoryBasic("Items", false, 8);
	setProfession(professionId);
	setSize(0.5F, 1.9F);
	((PathNavigateGround)getNavigator()).setBreakDoors(true);
	setCanPickUpLoot(true);
	this.tasks.addTask(0, new EntityAISwimming(this));
	this.tasks.addTask(1, new EntityAITradePlayer(this));
	this.tasks.addTask(3, new EntityAIFollowLeader(this, 1.5D, 24.0F, 9.0F));
	this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
	this.tasks.addTask(2, new EntityAIFriendlyAttackMelee(this, 1.5D, true));
	this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 1.0D));
	this.tasks.addTask(7, new EntityAIWander(this, 1.0D, 80));
	this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
	setCanPickUpLoot(true);
	this.experienceValue = 5;
	this.populateBuyingList();
}


public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
{
	return new EntityVillager(this.world);
}

public String getName()
{
	if (hasCustomName())
	{
		return getCustomNameTag();
	}

	
		String s = EntityList.getEntityString(this);
		
		if (s == null)
		{
			s = "generic";
		}

		return TranslateUtil.translateServer("entity." + s + ".name");
	
}
public EnumTier getTier()
{
	return EnumTier.TIER2;
}

protected float getSoundPitch()
{
	return super.getSoundPitch();
}

protected void applyEntityAttributes()
{
	super.applyEntityAttributes();
	getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
}

public boolean isNotColliding()
{
	if (this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox()))
	{
		BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
		
		IBlockState iblockstate = this.world.getBlockState(blockpos.down());
		Block block = iblockstate.getBlock();
		
		if (block == Blocks.GRASS_PATH || block == Blocks.GRAVEL || block == Blocks.SANDSTONE || block == Blocks.LOG || block == Blocks.PLANKS || block == Blocks.COBBLESTONE || block == Blocks.WOOL)
		{
			return true;
		}
	}

	return false;
}

public void onLivingUpdate()
{
	super.onLivingUpdate();
	setSize(0.5F, 1.9F);
	if (this.isTrading())
	this.getLookHelper().setLookPositionWithEntity(getCustomer(), 10F, 40F);
}

protected void updateAITasks()
{
	setSize(0.5F, 1.9F);
	
	if ((!isTrading()) && (this.timeUntilReset > 0))
	{
		this.timeUntilReset -= 1;
		if (this.timeUntilReset <= 0)
		{
			if (this.needsInitilization)
			{
				Iterator iterator = this.buyingList.iterator();
				while (iterator.hasNext())
				{
					MerchantRecipe merchantrecipe = (MerchantRecipe)iterator.next();
					merchantrecipe.increaseMaxTradeUses(2048);
				}
				populateBuyingList();
				this.needsInitilization = false;
				if ((this.villageObj != null) && (this.lastBuyingPlayer != null))
				{
					this.world.setEntityState(this, (byte)14);
				}
			}
			addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200, 1));
		}
	}
	super.updateAITasks();
}
public boolean interact(EntityPlayer player, EnumHand hand)
{
	player.getHeldItem(hand);
	
	if (!this.isTrading() && !this.isAntiMob())
	{
		if (this.isOnSameTeam(player))
		{
			if (this.buyingList == null)
			{
				this.populateBuyingList();
			}

			if (hand == EnumHand.MAIN_HAND)
			{
				player.addStat(StatList.TALKED_TO_VILLAGER);
			}

			if (!this.world.isRemote && !this.buyingList.isEmpty() && !this.isSneaking())
			{
				this.setCustomer(player);
				player.displayVillagerTradeGui(this);
			}
			else if (this.buyingList.isEmpty())
			{
				return false;
			}

			return true;
		}
		return true;
	}
	else
	{
		return false;
	}
}
protected void entityInit()
{
	super.entityInit();
	this.dataManager.register(PROFESSION, Integer.valueOf(0));
	this.dataManager.register(CAREER, Integer.valueOf(0));
}
public void writeEntityToNBT(NBTTagCompound tagCompound)
{
	super.writeEntityToNBT(tagCompound);
	tagCompound.setInteger("Profession", this.getProfession());
	tagCompound.setInteger("Riches", this.wealth);
	tagCompound.setInteger("Career", this.getCareer());
	tagCompound.setInteger("CareerLevel", this.careerLevel);
	tagCompound.setBoolean("Willing", this.isWillingToMate);
	if (this.buyingList != null)
	{
		tagCompound.setTag("Offers", this.buyingList.getRecipiesAsTags());
	}

	NBTTagList nbttaglist = new NBTTagList();
	
	for (int i = 0; i < this.villagerInventory.getSizeInventory(); ++i)
	{
		ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
		
		if (!itemstack.isEmpty())
		{
			nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
		}
	}

	tagCompound.setTag("Inventory", nbttaglist);
}
public void readEntityFromNBT(NBTTagCompound tagCompund)
{
	super.readEntityFromNBT(tagCompund);
	setProfession(tagCompund.getInteger("Profession"));
	this.wealth = tagCompund.getInteger("Riches");
	setCareer(tagCompund.getInteger("Career"));
	this.careerLevel = tagCompund.getInteger("CareerLevel");
	this.isWillingToMate = tagCompund.getBoolean("Willing");
	if (tagCompund.hasKey("Offers", 10))
	{
		NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Offers");
		this.buyingList = new MerchantRecipeList(nbttagcompound);
	}

	NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);
	
	for (int i = 0; i < nbttaglist.tagCount(); ++i)
	{
		ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));
		
		if (!itemstack.isEmpty())
		{
			this.villagerInventory.addItem(itemstack);
		}
	}
	setCanPickUpLoot(true);
}
public int getTalkInterval()
{
	return getAttackTarget() != null ? 5 : 80;
}
protected SoundEvent getAmbientSound()
{
	return isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
}
protected SoundEvent getHurtSound(DamageSource source)
{
	return SoundEvents.ENTITY_VILLAGER_HURT;
}
protected SoundEvent getDeathSound()
{
	return SoundEvents.ENTITY_VILLAGER_DEATH;
}

public void onDeath(DamageSource cause)
{
	super.onDeath(cause);
	
	if (!this.world.isRemote)
	{
		for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++)
		{
			if (this.villagerInventory != null)
			{
				ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
				if (itemstack != null)
				{
					dropItem(itemstack.getItem(), itemstack.getCount());
					this.villagerInventory.decrStackSize(i, itemstack.getCount());
				}
			}
		}
		if (this.wealth > 0)
		{
			dropItem(Items.EMERALD, this.wealth);
		}
	}
}

@Nullable
protected ResourceLocation getLootTable()
{
	return ELoot.ENTITIES_VILLAGER;
}

public void setProfession(int professionId)
{
	this.dataManager.set(PROFESSION, Integer.valueOf(professionId));
}
public int getProfession()
{
	return Math.max(((Integer)this.dataManager.get(PROFESSION)).intValue() % 5, 0);
}

public void setCareer(int professionId)
{
	this.dataManager.set(CAREER, Integer.valueOf(professionId));
}
public int getCareer()
{
	return Math.max(((Integer)this.dataManager.get(CAREER)).intValue() % 5, 0);
}
public boolean isMating()
{
	return this.isMating;
}
public void setMating(boolean mating)
{
	this.isMating = mating;
}
public void setPlaying(boolean playing)
{
	this.isPlaying = playing;
}
public boolean isPlaying()
{
	return this.isPlaying;
}
public void setCustomer(EntityPlayer p_70932_1_)
{
	this.buyingPlayer = p_70932_1_;
}
public EntityPlayer getCustomer()
{
	return this.buyingPlayer;
}
public boolean isTrading()
{
	return this.buyingPlayer != null;
}
public boolean getIsWillingToMate(boolean p_175550_1_)
{
	if ((!this.isWillingToMate) && (p_175550_1_) && (func_175553_cp()))
	{
		boolean flag1 = false;
		for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++)
		{
			ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
			if (itemstack != null)
			{
				if ((itemstack.getItem() == Items.BREAD) && (itemstack.getCount() >= 3))
				{
					flag1 = true;
					this.villagerInventory.decrStackSize(i, 3);
				}
				else if (((itemstack.getItem() == Items.POTATO) || (itemstack.getItem() == Items.CARROT)) && (itemstack.getCount() >= 12))
				{
					flag1 = true;
					this.villagerInventory.decrStackSize(i, 12);
				}
			}
			if (flag1)
			{
				this.world.setEntityState(this, (byte)18);
				this.isWillingToMate = true;
				break;
			}
		}
	}
	return this.isWillingToMate;
}
public void setIsWillingToMate(boolean p_175549_1_)
{
	this.isWillingToMate = p_175549_1_;
}
public void useRecipe(MerchantRecipe p_70933_1_)
{
	p_70933_1_.incrementToolUses();
	this.livingSoundTime = (-getTalkInterval());
	playSound(SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
	int i = 3 + this.rand.nextInt(4);
	this.setCurrentStudy(EnumStudy.Mental, 1);
	this.timeUntilReset = 40;
	this.needsInitilization = true;
	this.isWillingToMate = true;
	if (this.buyingPlayer != null)
	{
		this.lastBuyingPlayer = this.buyingPlayer.getName();
	}
	else
	{
		this.lastBuyingPlayer = null;
	}
	i += 5;
	if (p_70933_1_.getItemToBuy().getItem() == Items.EMERALD)
	{
		this.wealth += p_70933_1_.getItemToBuy().getCount();
	}
	if (p_70933_1_.getRewardsExp())
	{
		this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
	}
}
public void verifySellingItem(ItemStack p_110297_1_)
{
	if ((!this.world.isRemote) && (this.livingSoundTime > -getTalkInterval() + 20))
	{
		this.livingSoundTime = (-getTalkInterval());
		if (p_110297_1_ != null)
		{
			playSound(SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
		}
		else
		{
			playSound(SoundEvents.ENTITY_VILLAGER_NO, getSoundVolume(), getSoundPitch());
		}
	}
}
public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_)
{
	if (this.buyingList == null)
	{
		populateBuyingList();
	}
	return this.buyingList;
}
private void populateBuyingList()
{
	ITradeList[][][] aitradelist = DEFAULT_TRADE_LIST_MAP[0];
	if ((this.getCareer() != 0) && (this.careerLevel != 0))
	{
		this.careerLevel += 1;
	}
	else
	{
		this.setCareer(this.rand.nextInt(aitradelist.length) + 1);
		this.careerLevel = 1;
	}
	if (this.buyingList == null)
	{
		this.buyingList = new MerchantRecipeList();
	}
	int i = this.getCareer() - 1;
	int j = this.careerLevel - 1;
	ITradeList[][] aitradelist1 = aitradelist[i];
	if (j < aitradelist1.length)
	{
		ITradeList[] aitradelist2 = aitradelist1[j];
		ITradeList[] aitradelist3 = aitradelist2;
		int k = aitradelist2.length;
		for (int l = 0; l < k; l++)
		{
			ITradeList itradelist = aitradelist3[l];
			itradelist.addMerchantRecipe(this, this.buyingList, this.rand);
		}
	}
}
@SideOnly(Side.CLIENT)
public void setRecipes(MerchantRecipeList p_70930_1_) { }

/**
* Get the formatted ChatComponent that will be used for the sender's username in chat
*/
public ITextComponent getDisplayName()
{
	Team team = this.getTeam();
	String s = this.getCustomNameTag();
	
	if (s != null && !s.isEmpty())
	{
		TextComponentString textcomponentstring = new TextComponentString(ScorePlayerTeam.formatPlayerName(team, s));
		textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
		textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
		return textcomponentstring;
	}
	else
	{
		if (this.buyingList == null)
		{
			this.populateBuyingList();
		}

		String s1 = null;
		
		switch (this.getProfession())
		{
			case 0:
			
			if (this.getCareer() == 1)
			{
				s1 = "farmer";
			}
			else if (this.getCareer() == 2)
			{
				s1 = "fisherman";
			}
			else if (this.getCareer() == 3)
			{
				s1 = "shepherd";
			}
			else if (this.getCareer() == 4)
			{
				s1 = "fletcher";
			}

			break;
			case 1:
			
			if (this.getCareer() == 1)
			{
				s1 = "librarian";
			}
			else if (this.getCareer() == 2)
			{
				s1 = "cartographer";
			}

			break;
			case 2:
			s1 = "cleric";
			break;
			case 3:
			
			if (this.getCareer() == 1)
			{
				s1 = "armor";
			}
			else if (this.getCareer() == 2)
			{
				s1 = "weapon";
			}
			else if (this.getCareer() == 3)
			{
				s1 = "tool";
			}

			break;
			case 4:
			
			if (this.getCareer() == 1)
			{
				s1 = "butcher";
			}
			else if (this.getCareer() == 2)
			{
				s1 = "leather";
			}

			break;
			case 5:
			s1 = "nitwit";
		}

		{
			ITextComponent itextcomponent = new TextComponentTranslation("entity.Villager." + s1, new Object[0]);
			itextcomponent.getStyle().setHoverEvent(this.getHoverEvent());
			itextcomponent.getStyle().setInsertion(this.getCachedUniqueIdString());
			
			if (team != null)
			{
				itextcomponent.getStyle().setColor(team.getColor());
			}

			return itextcomponent;
		}
	}
}
public float getEyeHeight()
{
	return this.height * 0.86F;
}
@SideOnly(Side.CLIENT)
public void handleStatusUpdate(byte id)
{
	if (id == 12)
	{
		spawnParticles(EnumParticleTypes.HEART);
	}
	else if (id == 13)
	{
		spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
	}
	else if (id == 14)
	{
		spawnParticles(EnumParticleTypes.VILLAGER_HAPPY);
	}
	else
	{
		super.handleStatusUpdate(id);
	}
}
@SideOnly(Side.CLIENT)
private void spawnParticles(EnumParticleTypes particleType)
{
	for (int i = 0; i < 5; i++)
	{
		double d0 = this.rand.nextGaussian() * 0.02D;
		double d1 = this.rand.nextGaussian() * 0.02D;
		double d2 = this.rand.nextGaussian() * 0.02D;
		this.world.spawnParticle(particleType, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 1.0D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d0, d1, d2, new int[0]);
	}
}
public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
{
	livingdata = super.onInitialSpawn(difficulty, livingdata);
	setRandomProfession(this, this.world.rand);
	return livingdata;
}
public void setRandomProfession(EntityVillager entity, Random rand)
{
	entity.setProfession(rand.nextInt(5));
	switch (this.getProfession())
	{
		case 0:
		entity.setProfession(1 + rand.nextInt(3));
		break;
		case 1:
		entity.setProfession(1 + rand.nextInt());
		break;
		case 3:
		entity.setProfession(1 + rand.nextInt());
		break;
		case 4:
		entity.setProfession(1 + rand.nextInt());
		break;
		default:
		entity.setProfession(1);
	}
}
public void onStruckByLightning(EntityLightningBolt lightningBolt)
{
	if ((!this.world.isRemote) && (!this.isDead))
	{
		EntityWitch entitywitch = new EntityWitch(this.world);
		entitywitch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
		entitywitch.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitywitch)), (IEntityLivingData)null);
		entitywitch.setNoAI(isAIDisabled());
		if (hasCustomName())
		{
			entitywitch.setCustomNameTag(getCustomNameTag());
		}
		if (!this.isWild())
		{
			entitywitch.setOwnerId(this.getOwnerId());
		}
		this.world.spawnEntity(entitywitch);
		setDead();
	}
}
public InventoryBasic getVillagerInventory()
{
	return this.villagerInventory;
}
protected void updateEquipmentIfNeeded(EntityItem p_175445_1_)
{
	ItemStack itemstack = p_175445_1_.getItem();
	Item item = itemstack.getItem();
	if (canVillagerPickupItem(item))
	{
		ItemStack itemstack1 = this.villagerInventory.addItem(itemstack);
		if (itemstack1 == null)
		{
			p_175445_1_.setDead();
		}
		else
		{
			itemstack.setCount(itemstack1.getCount());
		}
	}
}
private boolean canVillagerPickupItem(Item p_175558_1_)
{
	return (p_175558_1_ == Items.BEETROOT) || (p_175558_1_ == Items.BEETROOT_SEEDS) || (p_175558_1_ == Items.BREAD) || (p_175558_1_ == Items.POTATO) || (p_175558_1_ == Items.CARROT) || (p_175558_1_ == Items.WHEAT) || (p_175558_1_ == Items.WHEAT_SEEDS);
}
public boolean func_175553_cp()
{
	return hasEnoughItems(1);
}
public boolean canAbondonItems()
{
	return hasEnoughItems(2);
}
public boolean func_175557_cr()
{
	getProfession();
	return !hasEnoughItems(5);
}
private boolean hasEnoughItems(int p_175559_1_)
{
	boolean flag = getProfession() == 0;
	for (int j = 0; j < this.villagerInventory.getSizeInventory(); j++)
	{
		ItemStack itemstack = this.villagerInventory.getStackInSlot(j);
		if (itemstack != null)
		{
			if (((itemstack.getItem() == Items.BREAD) && (itemstack.getCount() >= 3 * p_175559_1_)) || ((itemstack.getItem() == Items.POTATO) && (itemstack.getCount() >= 12 * p_175559_1_)) || ((itemstack.getItem() == Items.CARROT) && (itemstack.getCount() >= 12 * p_175559_1_)))
			{
				return true;
			}
			if ((flag) && (itemstack.getItem() == Items.WHEAT) && (itemstack.getCount() >= 9 * p_175559_1_))
			{
				return true;
			}
		}
	}
	return false;
}
public boolean isFarmItemInInventory()
{
	for (int i = 0; i < this.villagerInventory.getSizeInventory(); i++)
	{
		ItemStack itemstack = this.villagerInventory.getStackInSlot(i);
		if ((itemstack != null) && ((itemstack.getItem() == Items.BEETROOT_SEEDS) || (itemstack.getItem() == Items.WHEAT_SEEDS) || (itemstack.getItem() == Items.POTATO) || (itemstack.getItem() == Items.CARROT)))
		{
			return true;
		}
	}
	return false;
}
public boolean replaceItemInInventory(int p_174820_1_, ItemStack p_174820_2_)
{
	if (super.replaceItemInInventory(p_174820_1_, p_174820_2_))
	{
		return true;
	}
	int j = p_174820_1_ - 300;
	if ((j >= 0) && (j < this.villagerInventory.getSizeInventory()))
	{
		this.villagerInventory.setInventorySlotContents(j, p_174820_2_);
		if (p_174820_2_.getItem() == Items.BREAD)
		playSound(SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
		return true;
	}
	return false;
}
public static class EmeraldForItems
implements EntityVillager.ITradeList
{
	public Item sellItem;
	public EntityVillager.PriceInfo price;
	public EmeraldForItems(Item p_i45815_1_, EntityVillager.PriceInfo p_i45815_2_)
	{
		this.sellItem = p_i45815_1_;
		this.price = p_i45815_2_;
	}
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
	{
		int i = 1;
		if (this.price != null)
		{
			i = this.price.getPrice(random);
		}
		recipeList.add(new MerchantRecipe(new ItemStack(this.sellItem, i, 0), Items.EMERALD));
	}
}
public static class EmeraldsForItem
implements EntityVillager.ITradeList
{
	public Item buyItem;
	public EntityVillager.PriceInfo price;
	public EmeraldsForItem(Item p_i45815_1_, EntityVillager.PriceInfo p_i45815_2_)
	{
		this.buyItem = p_i45815_1_;
		this.price = p_i45815_2_;
	}
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
	{
		int i = 1;
		if (this.price != null)
		{
			i = this.price.getPrice(random);
		}
		recipeList.add(new MerchantRecipe(new ItemStack(this.buyItem), new ItemStack(Items.EMERALD, i, 0)));
	}
}
public static abstract interface ITradeList
{
	void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random);
}
public static class ItemAndEmeraldToItem
implements EntityVillager.ITradeList
{
	public ItemStack buyingItemStack;
	public EntityVillager.PriceInfo buyingPriceInfo;
	public ItemStack sellingItemstack;
	public EntityVillager.PriceInfo sellingPriceInfo;
	public ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_)
	{
		this.buyingItemStack = new ItemStack(p_i45813_1_);
		this.buyingPriceInfo = p_i45813_2_;
		this.sellingItemstack = new ItemStack(p_i45813_3_);
		this.sellingPriceInfo = p_i45813_4_;
	}
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
	{
		int i = 1;
		if (this.buyingPriceInfo != null)
		{
			i = this.buyingPriceInfo.getPrice(random);
		}
		int j = 1;
		if (this.sellingPriceInfo != null)
		{
			j = this.sellingPriceInfo.getPrice(random);
		}
		recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.EMERALD), new ItemStack(this.sellingItemstack.getItem(), j, this.sellingItemstack.getMetadata())));
	}
}
public static class ListEnchantedBookForEmeralds
implements EntityVillager.ITradeList
{
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
	{
		Enchantment enchantment = (Enchantment)Enchantment.REGISTRY.getRandomObject(random);
		int i = MathHelper.getInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
		ItemStack itemstack = ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(enchantment, i));
		int j = 1 + random.nextInt(1 + i) + 2 * i;
		if (j > 10)
		{
			j = 10;
		}
		recipeList.add(new MerchantRecipe(new ItemStack(Items.BOOK), new ItemStack(Items.EMERALD, j), itemstack));
	}
}
public static class ListEnchantedItemForEmeralds implements EntityVillager.ITradeList
{
	public ItemStack enchantedItemStack;
	public EntityVillager.PriceInfo priceInfo;
	public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_)
	{
		this.enchantedItemStack = new ItemStack(p_i45814_1_);
		this.priceInfo = p_i45814_2_;
	}
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
	{
		int i = 1;
		if (this.priceInfo != null)
		{
			i = this.priceInfo.getPrice(random);
		}
		ItemStack itemstack = new ItemStack(Items.EMERALD, i, 0);
		ItemStack itemstack1 = new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata());
		itemstack1 = EnchantmentHelper.addRandomEnchantment(random, itemstack1, 30, true);
		recipeList.add(new MerchantRecipe(itemstack, itemstack1));
	}
}
public static class ListItemForEmeralds implements EntityVillager.ITradeList
{
	public ItemStack itemToBuy;
	public EntityVillager.PriceInfo priceInfo;
	public ListItemForEmeralds(Item p_i45811_1_, EntityVillager.PriceInfo p_i45811_2_)
	{
		this.itemToBuy = new ItemStack(p_i45811_1_);
		this.priceInfo = p_i45811_2_;
	}
	public ListItemForEmeralds(ItemStack p_i45812_1_, EntityVillager.PriceInfo p_i45812_2_)
	{
		this.itemToBuy = p_i45812_1_;
		this.priceInfo = p_i45812_2_;
	}
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
	{
		int i = 1;
		if (this.priceInfo != null)
		{
			i = this.priceInfo.getPrice(random);
		}
		ItemStack itemstack1;
		ItemStack itemstack;
		if (i < 0)
		{
			itemstack = new ItemStack(Items.EMERALD, 1, 0);
			itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
		}
		else
		{
			itemstack = new ItemStack(Items.EMERALD, i, 0);
			itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
		}
		recipeList.add(new MerchantRecipe(itemstack, itemstack1));
	}
}
static class TreasureMapForEmeralds implements EntityVillager.ITradeList
{
	public EntityVillager.PriceInfo value;
	public String destination;
	public MapDecoration.Type destinationType;
	
	public TreasureMapForEmeralds(EntityVillager.PriceInfo p_i47340_1_, String p_i47340_2_, MapDecoration.Type p_i47340_3_)
	{
		this.value = p_i47340_1_;
		this.destination = p_i47340_2_;
		this.destinationType = p_i47340_3_;
	}

	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
	{
		int i = this.value.getPrice(random);
		World world = merchant.getWorld();
		BlockPos blockpos = world.findNearestStructure(this.destination, merchant.getPos(), true);
		
		if (blockpos != null)
		{
			ItemStack itemstack = ItemMap.setupNewMap(world, (double)blockpos.getX(), (double)blockpos.getZ(), (byte)2, true, true);
			ItemMap.renderBiomePreviewMap(world, itemstack);
			MapData.addTargetDecoration(itemstack, blockpos, "+", this.destinationType);
			itemstack.setTranslatableName("filled_map." + this.destination.toLowerCase(Locale.ROOT));
			recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack));
		}
	}
}

public static class PriceInfo
extends Tuple
{
	public PriceInfo(int p_i45810_1_, int p_i45810_2_)
	{
		super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
	}
	public int getPrice(Random p_179412_1_)
	{
		return ((Integer)getFirst()).intValue() >= ((Integer)getSecond()).intValue() ? ((Integer)getFirst()).intValue() : ((Integer)getFirst()).intValue() + p_179412_1_.nextInt(((Integer)getSecond()).intValue() - ((Integer)getFirst()).intValue() + 1);
	}
}
public World getWorld()
{
	return this.world;
}

public BlockPos getPos()
{
	return new BlockPos(this);
}

}


