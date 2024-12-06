package net.mrbt0907.ageofminecraft.entity.tier1;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.Animal;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EntityFriendlyCreature;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.Light;
import net.mrbt0907.ageofminecraft.entity.ai.EntityAIFollowLeader;
import net.mrbt0907.ageofminecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;

public class EntitySheep extends EntityEngendered implements IShearable, IJumpingMount
{
	private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntitySheep.class, DataSerializers.BYTE);
	protected float jumpPower;
	private final InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container()
	{
		public boolean canInteractWith(EntityPlayer playerIn)
		{
			return false;
		}
	}, 2, 1);
		private static final Map<EnumDyeColor, float[]> DYE_TO_RGB = Maps.newEnumMap(EnumDyeColor.class);
		private int sheepTimer;
		private EntityAIEatGrass entityAIEatGrass;
		public static float[] getDyeRgb(EnumDyeColor dyeColor)
		{
			return (float[])DYE_TO_RGB.get(dyeColor);
		}

		public EntitySheep(World worldIn)
		{
			super(worldIn);
			setSize(0.9F, 1.3F);
			this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.DYE));
			this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.DYE));
		}
		
		protected void initEntityAI()
		{
			super.initEntityAI();
			this.tasks.addTask(4, this.entityAIEatGrass = new EntityAIEatGrass(this));
		}
		
		protected void applyEntityAttributes()
		{
			super.applyEntityAttributes();
			getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
			getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
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

		protected void updateAITasks()
		{
			this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
			super.updateAITasks();
		}

		public void onLivingUpdate()
		{
			setSize(0.9F, 1.3F);
			if (this.world.isRemote)
			{
				this.sheepTimer = Math.max(0, this.sheepTimer - 1);
			}

			super.onLivingUpdate();
		}
		protected void entityInit()
		{
			super.entityInit();
			this.dataManager.register(DYE_COLOR, Byte.valueOf((byte)0));
		}
		@Nullable
		protected ResourceLocation getLootTable()
		{
			if (this.getSheared())
			{
				return LootRegistry.ENTITIES_SHEEP;
			}
			else
			{
				switch (this.getFleeceColor())
				{
					case WHITE:
					default:
					return LootRegistry.ENTITIES_SHEEP_WHITE;
					case ORANGE:
					return LootRegistry.ENTITIES_SHEEP_ORANGE;
					case MAGENTA:
					return LootRegistry.ENTITIES_SHEEP_MAGENTA;
					case LIGHT_BLUE:
					return LootRegistry.ENTITIES_SHEEP_LIGHT_BLUE;
					case YELLOW:
					return LootRegistry.ENTITIES_SHEEP_YELLOW;
					case LIME:
					return LootRegistry.ENTITIES_SHEEP_LIME;
					case PINK:
					return LootRegistry.ENTITIES_SHEEP_PINK;
					case GRAY:
					return LootRegistry.ENTITIES_SHEEP_GRAY;
					case SILVER:
					return LootRegistry.ENTITIES_SHEEP_SILVER;
					case CYAN:
					return LootRegistry.ENTITIES_SHEEP_CYAN;
					case PURPLE:
					return LootRegistry.ENTITIES_SHEEP_PURPLE;
					case BLUE:
					return LootRegistry.ENTITIES_SHEEP_BLUE;
					case BROWN:
					return LootRegistry.ENTITIES_SHEEP_BROWN;
					case GREEN:
					return LootRegistry.ENTITIES_SHEEP_GREEN;
					case RED:
					return LootRegistry.ENTITIES_SHEEP_RED;
					case BLACK:
					return LootRegistry.ENTITIES_SHEEP_BLACK;
				}
			}
		}
		@SideOnly(Side.CLIENT)
		public void handleStatusUpdate(byte id)
		{
			if (id == 10)
			{
				this.sheepTimer = 40;
			}
			else
			{
				super.handleStatusUpdate(id);
			}
		}
		public boolean interact(EntityPlayer player, EnumHand hand)
		{
			ItemStack stack = player.getHeldItem(hand);
			
			if (!stack.isEmpty() && stack.getItem() == Items.DYE && hasOwner())
			{
				EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
				
				if (!this.getSheared() && this.getFleeceColor() != enumdyecolor)
				{
					playSound(getAmbientSound(), getSoundVolume(), getSoundPitch() - 0.2F);
					this.world.playEvent(2001, this.getPosition(), Block.getStateId(Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, enumdyecolor)));
					this.setFleeceColor(enumdyecolor);
					stack.shrink(1);
				}

				return true;
			}
			else
			{
				return super.processInteract(player, hand);
			}
		}
			@SideOnly(Side.CLIENT)
			public float getHeadRotationPointY(float p_70894_1_)
			{
				return this.sheepTimer <= 0 ? 0.0F : (this.sheepTimer >= 4 && this.sheepTimer <= 36 ? 1.0F : (this.sheepTimer < 4 ? ((float)this.sheepTimer - p_70894_1_) / 4.0F : -((float)(this.sheepTimer - 40) - p_70894_1_) / 4.0F));
			}
			@SideOnly(Side.CLIENT)
			public float getHeadRotationAngleX(float p_70890_1_)
			{
				if ((this.sheepTimer > 4) && (this.sheepTimer <= 36))
				{
					float f = (this.sheepTimer - 4 - p_70890_1_) / 32.0F;
					return 0.62831855F + 0.2199115F * MathHelper.sin(f * 28.7F);
				}
				return this.sheepTimer > 0 ? 0.62831855F : this.rotationPitch * 0.017453292F;
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

			public EnumDyeColor getFleeceColor()
			{
				return EnumDyeColor.byMetadata(((Byte)this.dataManager.get(DYE_COLOR)).byteValue() & 0xF);
			}
			public void setFleeceColor(EnumDyeColor color)
			{
				byte b0 = ((Byte)this.dataManager.get(DYE_COLOR)).byteValue();
				this.dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 & 0xF0 | color.getMetadata() & 0xF)));
			}
			public boolean getSheared()
			{
				return (((Byte)this.dataManager.get(DYE_COLOR)).byteValue() & 0x10) != 0;
			}
			public void setSheared(boolean sheared)
			{
				byte b0 = ((Byte)this.dataManager.get(DYE_COLOR)).byteValue();
				if (sheared)
				{
					this.dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 | 0x10)));
				}
				else
				{
					this.dataManager.set(DYE_COLOR, Byte.valueOf((byte)(b0 & 0xFFFFFFEF)));
				}
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
				dropItem(Items.WHEAT_SEEDS, 2 + this.rand.nextInt(3));
			}
			@Nullable
			public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
			{
				livingdata = super.onInitialSpawn(difficulty, livingdata);
				setFleeceColor(getRandomSheepColor(this.world.rand));
				return livingdata;
			}
			public double getMountedYOffset()
			{
				return (double)this.height * 0.775D;
			}

			public float getEyeHeight()
			{
				return 0.95F * this.height;
			}
			static
			{
				DYE_TO_RGB.put(EnumDyeColor.WHITE, new float[] { 1.0F, 1.0F, 1.0F });
				DYE_TO_RGB.put(EnumDyeColor.ORANGE, new float[] { 0.85F, 0.5F, 0.2F });
				DYE_TO_RGB.put(EnumDyeColor.MAGENTA, new float[] { 0.7F, 0.3F, 0.85F });
				DYE_TO_RGB.put(EnumDyeColor.LIGHT_BLUE, new float[] { 0.4F, 0.6F, 0.85F });
				DYE_TO_RGB.put(EnumDyeColor.YELLOW, new float[] { 0.9F, 0.9F, 0.2F });
				DYE_TO_RGB.put(EnumDyeColor.LIME, new float[] { 0.5F, 0.8F, 0.1F });
				DYE_TO_RGB.put(EnumDyeColor.PINK, new float[] { 0.95F, 0.5F, 0.65F });
				DYE_TO_RGB.put(EnumDyeColor.GRAY, new float[] { 0.3F, 0.3F, 0.3F });
				DYE_TO_RGB.put(EnumDyeColor.SILVER, new float[] { 0.6F, 0.6F, 0.6F });
				DYE_TO_RGB.put(EnumDyeColor.CYAN, new float[] { 0.3F, 0.5F, 0.6F });
				DYE_TO_RGB.put(EnumDyeColor.PURPLE, new float[] { 0.5F, 0.25F, 0.7F });
				DYE_TO_RGB.put(EnumDyeColor.BLUE, new float[] { 0.2F, 0.3F, 0.7F });
				DYE_TO_RGB.put(EnumDyeColor.BROWN, new float[] { 0.4F, 0.3F, 0.2F });
				DYE_TO_RGB.put(EnumDyeColor.GREEN, new float[] { 0.4F, 0.5F, 0.2F });
				DYE_TO_RGB.put(EnumDyeColor.RED, new float[] { 0.6F, 0.2F, 0.2F });
				DYE_TO_RGB.put(EnumDyeColor.BLACK, new float[] { 0.1F, 0.1F, 0.1F });
			}
			public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) { return (!getSheared()) && (!isChild()); }
			public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
			{
				setSheared(true);
				int i = 1 + this.rand.nextInt(3);
				List<ItemStack> ret = new ArrayList();
				for (int j = 0; j < i; j++)
				{
					ret.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, getFleeceColor().getMetadata()));
				}
				playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
				return ret;
			}
}