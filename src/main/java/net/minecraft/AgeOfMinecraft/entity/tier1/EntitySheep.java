package net.minecraft.AgeOfMinecraft.entity.tier1;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.AgeOfMinecraft.entity.Animal;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
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

public class EntitySheep extends EntityFriendlyCreature implements Light, Animal, IShearable, IJumpingMount
{
	private static final DataParameter<Byte> DYE_COLOR = EntityDataManager.createKey(EntitySheep.class, DataSerializers.BYTE);
	private int shootTimer;
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
		public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
		{
			return new EntitySheep(this.world);
		}
		public EntitySheep(World worldIn)
		{
			super(worldIn);
			setSize(0.9F, 1.3F);
			this.inventoryCrafting.setInventorySlotContents(0, new ItemStack(Items.DYE));
			this.inventoryCrafting.setInventorySlotContents(1, new ItemStack(Items.DYE));
			this.tasks.addTask(0, new EntityAISwimming(this));
			this.tasks.addTask(1, new EntityAIFollowLeader(this, 1.2D, 16.0F, 4.0F));
			this.tasks.addTask(2, new EntityAIFriendlyAttackMelee(this, 1.2D, true));
			this.tasks.addTask(4, this.entityAIEatGrass = new EntityAIEatGrass(this));
			this.tasks.addTask(5, new EntityAIWander(this, 1.0D, 80));
			this.tasks.addTask(8, new EntityAILookIdle(this));
			this.experienceValue = 1;
		}
		public boolean canBeButchered()
		{
			return true;
		}
		/**
		* Bonus damage vs mobs that implement Armored
		*/
		public float getBonusVSArmored()
		{
			return 0.5F;
		}

		/**
		* Bonus damage vs mobs that implement Massive
		*/
		public float getBonusVSMassive()
		{
			return 0.1F;
		}

		protected void updateAITasks()
		{
			this.sheepTimer = this.entityAIEatGrass.getEatingGrassTimer();
			super.updateAITasks();
		}

		public boolean canBeMatedWith()
		{
			return false;
		}

		public boolean canBeMarried()
		{
			return false;
		}

		public float getBlockPathWeight(BlockPos pos)
		{
			return this.world.getBlockState(pos.down()).getBlock() == this.spawnableBlock ? 10.0F : this.world.getLightBrightness(pos) - 0.5F;
		}
		public void onLivingUpdate()
		{
			setSize(0.9F, 1.3F);
			if (this.world.isRemote)
			{
				this.sheepTimer = Math.max(0, this.sheepTimer - 1);
			}
			if (this.getAttackTarget() != null)
			++this.shootTimer;
			else
			{
				if (this.shootTimer > 0)
				--this.shootTimer;
			}

			if (this.ticksExisted > 20 && (this.hasCustomName()) && ("jeb_".equals(this.getCustomNameTag())) && this.getAttackTarget() != null)
			{
				if (this.shootTimer > 0)
				{
					this.getNavigator().clearPath();
					this.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, this.getSoundVolume(), 0.5F + (float)(this.shootTimer / 20));
				}
				if (this.shootTimer >= 60)
				{
					for (int i = 0; i < 256; i++)
					{
						double d9 = i / (256 - 1.0D);
						double d6 = this.posX + (this.posX - getAttackTarget().posX) * -d9;
						double d7 = this.posY + this.getEyeHeight() + (this.posY - getAttackTarget().posY) * -d9;
						double d8 = this.posZ + (this.posZ - getAttackTarget().posZ) * -d9;
						this.world.spawnParticle(EnumParticleTypes.END_ROD, d6, d7, d8, 0.0D, 0.01D, 0.0D, new int[0]);
					}
					this.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundVolume(), this.rand.nextFloat() - this.rand.nextFloat() * 0.4F + 1.0F);
					this.getAttackTarget().attackEntityFrom(DamageSource.causeExplosionDamage(this), 12F);
					this.shootTimer = -200;
				}
			}
			super.onLivingUpdate();
		}
		protected void applyEntityAttributes()
		{
			super.applyEntityAttributes();
			getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
			getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
			getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
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
				return ELoot.ENTITIES_SHEEP;
			}
			else
			{
				switch (this.getFleeceColor())
				{
					case WHITE:
					default:
					return ELoot.ENTITIES_SHEEP_WHITE;
					case ORANGE:
					return ELoot.ENTITIES_SHEEP_ORANGE;
					case MAGENTA:
					return ELoot.ENTITIES_SHEEP_MAGENTA;
					case LIGHT_BLUE:
					return ELoot.ENTITIES_SHEEP_LIGHT_BLUE;
					case YELLOW:
					return ELoot.ENTITIES_SHEEP_YELLOW;
					case LIME:
					return ELoot.ENTITIES_SHEEP_LIME;
					case PINK:
					return ELoot.ENTITIES_SHEEP_PINK;
					case GRAY:
					return ELoot.ENTITIES_SHEEP_GRAY;
					case SILVER:
					return ELoot.ENTITIES_SHEEP_SILVER;
					case CYAN:
					return ELoot.ENTITIES_SHEEP_CYAN;
					case PURPLE:
					return ELoot.ENTITIES_SHEEP_PURPLE;
					case BLUE:
					return ELoot.ENTITIES_SHEEP_BLUE;
					case BROWN:
					return ELoot.ENTITIES_SHEEP_BROWN;
					case GREEN:
					return ELoot.ENTITIES_SHEEP_GREEN;
					case RED:
					return ELoot.ENTITIES_SHEEP_RED;
					case BLACK:
					return ELoot.ENTITIES_SHEEP_BLACK;
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
			
			if (!stack.isEmpty() && stack.getItem() == Items.DYE && hasOwner(player))
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
			else if (stack.isEmpty() && getRidingEntity() == null)
			{
				if (!isWild() && this.isOnSameTeam(player) && !this.isChild() && !this.world.isRemote)
				player.startRiding(this);
				playSound(getAmbientSound(), getSoundVolume(), getSoundPitch());
				this.world.playEvent(2001, this.getPosition(), Block.getStateId(Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, this.getFleeceColor())));
				return true;
			}
			else
			{
				return false;
			}
		}

		protected float jumpPower;
		
		public void setJumpPower(int jumpPowerIn)
		{
			if (this.isBeingRidden())
			{
				if (jumpPowerIn < 0)
				{
					jumpPowerIn = 0;
				}

				if (jumpPowerIn >= 90)
				{
					this.jumpPower = 1.0F;
				}
				else
				{
					this.jumpPower = 0.4F + 0.4F * (float)jumpPowerIn / 90.0F;
				}
			}
		}

		public boolean canJump()
		{
			return true;
		}

		public void handleStartJump(int p_184775_1_)
		{
			this.playLivingSound();
		}

		public void handleStopJump()
		{
		}public void travel(float strafe, float vertical, float forward)
			{
				if (isBeingRidden())
				{
					this.stepHeight = 1F;
					EntityLivingBase entitylivingbase = (EntityLivingBase)getControllingPassenger();
					this.rotationYawHead = entitylivingbase.rotationYawHead;
					this.rotationPitch = entitylivingbase.rotationPitch;
					setRotation(this.rotationYaw, this.rotationPitch);
					strafe = entitylivingbase.moveStrafing;
					forward = entitylivingbase.moveForward;
					
					if (forward != 0F)
					{
						this.rotationYaw = this.renderYawOffset = this.rotationYawHead;
						this.prevRotationYaw = (this.rotationYaw = entitylivingbase.rotationYaw);
					}
					if (canPassengerSteer())
					{
						setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
						super.travel(strafe, vertical, forward);
					}
					else if ((entitylivingbase instanceof EntityPlayer))
					{
						this.motionX = 0.0D;
						this.motionY = 0.0D;
						this.motionZ = 0.0D;
					}
					if (this.jumpPower > 0.0F && this.onGround)
					{
						this.motionY = 0.7D * (double)this.jumpPower;
						
						if (this.isPotionActive(MobEffects.JUMP_BOOST))
						{
							this.motionY += (double)((float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
						}

						this.isAirBorne = true;
						
						if (forward > 0.0F)
						{
							float f = MathHelper.sin(this.rotationYaw * 0.017453292F);
							float f1 = MathHelper.cos(this.rotationYaw * 0.017453292F);
							this.motionX += (double)(-0.4F * f * this.jumpPower);
							this.motionZ += (double)(0.4F * f1 * this.jumpPower);
						}

						this.jumpPower = 0.0F;
					}
					this.prevLimbSwingAmount = this.limbSwingAmount;
					double d1 = this.posX - this.prevPosX;
					double d0 = this.posZ - this.prevPosZ;
					float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
					if (f2 > 1.0F)
					{
						f2 = 1.0F;
					}
					this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.1F;
					this.limbSwing += this.limbSwingAmount;
				}
				else {
					super.travel(strafe, vertical, forward);
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
			protected SoundEvent getAmbientSound()
			{
				return SoundEvents.ENTITY_SHEEP_AMBIENT;
			}
			protected SoundEvent getHurtSound(DamageSource source)
			{
				return SoundEvents.ENTITY_SHEEP_HURT;
			}
			protected SoundEvent getDeathSound()
			{
				return SoundEvents.ENTITY_SHEEP_DEATH;
			}
			protected void playStepSound(BlockPos pos, Block blockIn)
			{
				playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.15F, 1.0F / this.getFittness());
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
			
			@Override
			public EnumTier getTier()
			{
				return EnumTier.TIER1;
			}
		}

		
		