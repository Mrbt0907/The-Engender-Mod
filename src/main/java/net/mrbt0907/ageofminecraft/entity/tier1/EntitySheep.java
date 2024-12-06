package net.mrbt0907.ageofminecraft.entity.tier1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;

public class EntitySheep extends EntityEngendered implements IShearable, IJumpingMount
{
	private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntitySheep.class, DataSerializers.BYTE);
	protected float jumpPower;
	
	private int sheepTimer;
	private EntityAIEatGrass entityAIEatGrass;
	
	public EntitySheep(World worldIn)
	{
		super(worldIn);
		setSize(0.9F, 1.3F);
	}
	
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(DYE_COLOR, Byte.valueOf((byte)0));
	}
	
	protected void initEntityAI()
	{
		super.initEntityAI();
		tasks.addTask(4, entityAIEatGrass = new EntityAIEatGrass(this));
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setBoolean("Sheared", getSheared());
		tagCompound.setByte("Color", (byte)getFleeceColor().getMetadata());
	}
	
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		setSheared(tagCompund.getBoolean("Sheared"));
		setFleeceColor(EnumDyeColor.byMetadata(tagCompund.getByte("Color")));
	}
	
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		setFleeceColor(getRandomSheepColor(world.rand));
		return livingdata;
	}
	
	public void onLivingUpdate()
	{
		if (world.isRemote)
			sheepTimer = Math.max(0, sheepTimer - 1);
		
		super.onLivingUpdate();
	}
	
	protected void updateAITasks()
	{
		sheepTimer = entityAIEatGrass.getEatingGrassTimer();
		super.updateAITasks();
	}

	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		setSheared(true);
		int i = 1 + rand.nextInt(3);
		List<ItemStack> wool = new ArrayList<ItemStack>();
		for (int ii = 0; ii < i; ii++)
			wool.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, getFleeceColor().getMetadata()));
		playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
		return wool;
	}

	public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
		ItemStack stack = player.getHeldItem(hand);
		Item item = stack.getItem();
		if (item.equals(Items.DYE) && player.getUniqueID().equals(getOwnerId()))
		{
			EnumDyeColor dyeColor = EnumDyeColor.byDyeDamage(stack.getMetadata());
			
			if (!getSheared() && getFleeceColor() != dyeColor)
			{
				playSound(getAmbientSound(), getSoundVolume(), getSoundPitch() - 0.2F);
				world.playEvent(2001, getPosition(), Block.getStateId(Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, dyeColor)));
				setFleeceColor(dyeColor);
				stack.shrink(1);
			}
			
			return true;
		}
		return super.processInteract(player, hand);
	}
	
	//----- OVERRIDES -----\\
	protected SoundEvent getAmbientSound() {return SoundEvents.ENTITY_SHEEP_AMBIENT;}
	protected SoundEvent getHurtSound(DamageSource source) {return SoundEvents.ENTITY_SHEEP_HURT;}
	protected SoundEvent getDeathSound() {return SoundEvents.ENTITY_SHEEP_DEATH;}
	protected void playStepSound(BlockPos pos, Block blockIn) {playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F);}
	
	@Override
	public EnumTier getTier() {return null;}
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
	
	@Override
	public void setJumpPower(int jumpPower)
	{
		if (isBeingRidden())
		{
			if (jumpPower < 0)
				jumpPower = 0;
			
			if (jumpPower >= 90)
				this.jumpPower = 1.0F;
			else
				this.jumpPower = 0.4F + 0.4F * (float)jumpPower / 90.0F;
		}
	}
	
	@Override
	public boolean canJump()
	{
		return true;
	}
	
	@Override
	public void handleStartJump(int jumpPower)
	{
		playLivingSound();
	}
	
	@Override
	public void handleStopJump() {}
	
	@Nullable
	protected ResourceLocation getLootTable()
	{
		if (getSheared())
			return LootRegistry.ENTITIES_SHEEP;
		else
			switch (getFleeceColor())
			{
				case WHITE: default: return LootRegistry.ENTITIES_SHEEP_WHITE;
				case ORANGE: return LootRegistry.ENTITIES_SHEEP_ORANGE;
				case MAGENTA: return LootRegistry.ENTITIES_SHEEP_MAGENTA;
				case LIGHT_BLUE: return LootRegistry.ENTITIES_SHEEP_LIGHT_BLUE;
				case YELLOW: return LootRegistry.ENTITIES_SHEEP_YELLOW;
				case LIME: return LootRegistry.ENTITIES_SHEEP_LIME;
				case PINK: return LootRegistry.ENTITIES_SHEEP_PINK;
				case GRAY: return LootRegistry.ENTITIES_SHEEP_GRAY;
				case SILVER: return LootRegistry.ENTITIES_SHEEP_SILVER;
				case CYAN: return LootRegistry.ENTITIES_SHEEP_CYAN;
				case PURPLE: return LootRegistry.ENTITIES_SHEEP_PURPLE;
				case BLUE: return LootRegistry.ENTITIES_SHEEP_BLUE;
				case BROWN: return LootRegistry.ENTITIES_SHEEP_BROWN;
				case GREEN: return LootRegistry.ENTITIES_SHEEP_GREEN;
				case RED: return LootRegistry.ENTITIES_SHEEP_RED;
				case BLACK: return LootRegistry.ENTITIES_SHEEP_BLACK;
			}
	}
	
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 10)
			sheepTimer = 40;
		else
			super.handleStatusUpdate(id);
	}
	
	@SideOnly(Side.CLIENT)
	public float getHeadRotationPointY(float partialTicks)
	{
		return sheepTimer <= 0 ? 0.0F : (sheepTimer >= 4 && sheepTimer <= 36 ? 1.0F : (sheepTimer < 4 ? ((float)sheepTimer - partialTicks) / 4.0F : -((float)(sheepTimer - 40) - partialTicks) / 4.0F));
	}
	
	@SideOnly(Side.CLIENT)
	public float getHeadRotationAngleX(float partialTicks)
	{
		if (sheepTimer > 4 && sheepTimer <= 36)
		{
			float f = (sheepTimer - 4 - partialTicks) / 32.0F;
			return 0.62831855F + 0.2199115F * (float) Maths.fastSin(f * 28.7F);
		}
		return sheepTimer > 0 ? 0.62831855F : rotationPitch * 0.017453292F;
	}
	
	public EnumDyeColor getFleeceColor()
	{
		return EnumDyeColor.byMetadata(((Byte)dataManager.get(DYE_COLOR)).byteValue() & 0xF);
	}
	
	public void setFleeceColor(EnumDyeColor color)
	{
		dataManager.set(DYE_COLOR, Byte.valueOf((byte)(dataManager.get(DYE_COLOR).byteValue() & 0xF0 | color.getMetadata() & 0xF)));
	}
	
	public boolean getSheared()
	{
		return (dataManager.get(DYE_COLOR).byteValue() & 0x10) != 0;
	}
	
	public void setSheared(boolean sheared)
	{
		byte b0 = dataManager.get(DYE_COLOR).byteValue();
		if (sheared)
			dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 | 0x10)));
		else
			dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 & 0xFFFFFFEF)));
	}
	
	public static EnumDyeColor getRandomSheepColor(Random random)
	{
		int i = random.nextInt(100);
		return random.nextInt(500) == 0 ? EnumDyeColor.PINK : i < 18 ? EnumDyeColor.BROWN : i < 15 ? EnumDyeColor.SILVER : i < 10 ? EnumDyeColor.GRAY : i < 5 ? EnumDyeColor.BLACK : EnumDyeColor.WHITE;
	}
	
	public void eatGrassBonus()
	{
		setSheared(false);
		heal(2.0F);
		dropItem(Items.WHEAT_SEEDS, 2 + rand.nextInt(3));
	}
	
	public double getMountedYOffset()
	{
		return (double)height * 0.775D;
	}
	
	public float getEyeHeight()
	{
		return 0.95F * height;
	}
	
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {return !getSheared() && !isChild();}
	
	public static float[] getDyeRgb(EnumDyeColor dyeColor)
	{
		return net.minecraft.entity.passive.EntitySheep.getDyeRgb(dyeColor);
	}
}