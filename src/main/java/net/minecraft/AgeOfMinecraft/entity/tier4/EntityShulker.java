package net.minecraft.AgeOfMinecraft.entity.tier4;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.AgeOfMinecraft.registry.ESound;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.Ender;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Structure;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityShulker
extends EntityFriendlyCreature implements Armored, Structure, Ender
{
	private static final UUID COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
	private static final AttributeModifier COVERED_ARMOR_BONUS_MODIFIER = new AttributeModifier(COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0D, 0).setSaved(false);
	protected static final DataParameter<EnumFacing> ATTACHED_FACE = EntityDataManager.createKey(EntityShulker.class, DataSerializers.FACING);
	protected static final DataParameter<Optional<BlockPos>> ATTACHED_BLOCK_POS = EntityDataManager.createKey(EntityShulker.class, DataSerializers.OPTIONAL_BLOCK_POS);
	protected static final DataParameter<Byte> PEEK_TICK = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
	protected static final DataParameter<Byte> COLOR = EntityDataManager.<Byte>createKey(EntityShulker.class, DataSerializers.BYTE);
	public static final EnumDyeColor DEFAULT_COLOR = EnumDyeColor.PURPLE;
	private float prevPeekAmount;
	private float peekAmount;
	private BlockPos currentAttachmentPosition;
	private int clientSideTeleportInterpolation;
	public EntityShulker(World p_i46779_1_)
	{
		super(p_i46779_1_);
		setSize(1.0F, 1.0F);
		this.prevRenderYawOffset = 180.0F;
		this.renderYawOffset = 180.0F;
		this.isOffensive = true;
		this.isImmuneToFire = true;
		this.currentAttachmentPosition = null;
		this.experienceValue = 10;
		this.tasks.addTask(0, new AIAttack());
		this.tasks.addTask(7, new AIPeek());
		this.tasks.addTask(8, new EntityAILookIdle(this));
		if (this.getAttachmentPos() == null)
		this.setAttachmentPos(this.getPosition().down());
	}
	
	public boolean leavesNoCorpse()
	{
		return true;
	}

	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 1.5F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 1.1F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.1F;
	}

	public double getDefaultAgilityStat()
	{
		return 1F;
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityShulker(this.world);
	}
	public boolean canUseGuardBlock()
	{
		return false;
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER4;
	}
	public boolean canBeMatedWith()
	{
		return false;
	}

	public boolean canBeMarried()
	{
		return false;
	}
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		this.renderYawOffset = 180.0F;
		this.prevRenderYawOffset = 180.0F;
		this.rotationYaw = 180.0F;
		this.prevRotationYaw = 180.0F;
		this.rotationYawHead = 180.0F;
		this.prevRotationYawHead = 180.0F;
		this.setColor(DEFAULT_COLOR);
		return super.onInitialSpawn(difficulty, livingdata);
	}
	/**
	* Get this Entity's EnumCreatureAttribute
	*/
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return ESetup.ENDER;
	}
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}
	protected boolean canTriggerWalking()
	{
		return false;
	}
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_SHULKER_AMBIENT;
	}
	public void playLivingSound()
	{
		if (!isClosed())
		{
			super.playLivingSound();
		}
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_SHULKER_DEATH;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return isClosed() ? SoundEvents.ENTITY_SHULKER_HURT_CLOSED : SoundEvents.ENTITY_SHULKER_HURT;
	}
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(ATTACHED_FACE, EnumFacing.DOWN);
		this.dataManager.register(ATTACHED_BLOCK_POS, Optional.<BlockPos>absent());
		this.dataManager.register(PEEK_TICK, Byte.valueOf((byte)0));
		this.dataManager.register(COLOR, Byte.valueOf((byte)DEFAULT_COLOR.getMetadata()));
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
	}
	protected EntityBodyHelper createBodyHelper()
	{
		return new BodyHelper(this);
	}
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.dataManager.set(ATTACHED_FACE, EnumFacing.getFront(compound.getByte("AttachFace")));
		this.dataManager.set(PEEK_TICK, Byte.valueOf(compound.getByte("Peek")));
		this.dataManager.set(COLOR, Byte.valueOf(compound.getByte("Color")));
		
		if (compound.hasKey("APX"))
		{
			int i = compound.getInteger("APX");
			int j = compound.getInteger("APY");
			int k = compound.getInteger("APZ");
			this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(new BlockPos(i, j, k)));
		}
		else
		{
			this.dataManager.set(ATTACHED_BLOCK_POS, Optional.<BlockPos>absent());
		}
	}
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setByte("AttachFace", (byte)((EnumFacing)this.dataManager.get(ATTACHED_FACE)).getIndex());
		compound.setByte("Peek", ((Byte)this.dataManager.get(PEEK_TICK)).byteValue());
		compound.setByte("Color", ((Byte)this.dataManager.get(COLOR)).byteValue());
		BlockPos blockpos = this.getAttachmentPos();
		
		if (blockpos != null)
		{
			compound.setInteger("APX", blockpos.getX());
			compound.setInteger("APY", blockpos.getY());
			compound.setInteger("APZ", blockpos.getZ());
		}
	}
	public EnumDyeColor getColor()
	{
		return EnumDyeColor.byMetadata(((Byte)this.dataManager.get(COLOR)).byteValue());
	}

	public void setColor(EnumDyeColor color)
	{
		byte b0 = ((Byte)this.dataManager.get(COLOR)).byteValue();
		this.dataManager.set(COLOR, Byte.valueOf((byte)(b0 & 0xF0 | color.getMetadata() & 0xF)));
	}

	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		ItemStack heldItem = new ItemStack(stack.getItem());
		
		if (stack != null && stack.getItem() == Items.DYE && hasOwner(player))
		{
			EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
			
			if (this.getColor() != enumdyecolor)
			{
				this.playSound(this.getAmbientSound(), this.getSoundVolume(), this.getSoundPitch());
				player.swingArm(hand);
				if (!this.world.isRemote)
				{
					this.entityDropItem(new ItemStack(Items.DYE, 1, this.getColor().getDyeDamage()), 1F);
					this.setColor(enumdyecolor);
					stack.shrink(1);
				}
			}

			return true;
		}
		else if (stack != null && stack.getItem() == Items.FIREWORKS && hasOwner(player))
		{
			playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, 1.0F, 1.0F);
			player.swingArm(hand);
			if (!this.world.isRemote)
			{
				if (!this.getHeldItemMainhand().isEmpty())
				this.entityDropItem(this.getHeldItemMainhand(), 1F);
				heldItem.setTagCompound(stack.getTagCompound());
				heldItem.setItemDamage(stack.getItemDamage());
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem);
				stack.shrink(1);
			}

			return true;
		}
		else
		{
			return false;
		}
	}
	public void onUpdate()
	{
		ItemStack charge = new ItemStack(Items.DYE, 1, this.getColor().getDyeDamage());
		charge.setStackDisplayName("Color: " + this.getColor().getName());
		basicInventory.setInventorySlotContents(7, charge);
		super.onUpdate();
		if (this.deathTime > 0)
		this.motionY = -0.6D;
		if (this.deathTime == 1)
		{
			playSound(ESound.buildingDeath, 2.0F, 1.0F);
			for (int k = 0; k < 200; k++)
			{
				double d2 = this.rand.nextGaussian() * 0.1D;
				this.rand.nextGaussian();
				double d1 = this.rand.nextGaussian() * 0.1D;
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height * 2.0F, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, -0.25D, d1, new int[0]);
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height * 2.0F, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, -0.25D, d1, new int[0]);
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand.nextFloat() * this.height * 2.0F, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d2, 0.25D, d1, new int[0]);
			}
		}

		BlockPos blockpos = (BlockPos)((Optional)this.dataManager.get(ATTACHED_BLOCK_POS)).orNull();
		
		if (blockpos == null && !this.world.isRemote)
		{
			blockpos = new BlockPos(this);
			this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
		}
		if (!this.isWild() && getDistanceSq(getOwner()) >= 576D)
		{
			teleportTo(getOwner().posX, getOwner().posY, getOwner().posZ);
		}

		if (this.isRiding())
		{
			blockpos = null;
			float f = this.getRidingEntity().rotationYaw;
			this.rotationYaw = f;
			this.renderYawOffset = f;
			this.prevRenderYawOffset = f;
			this.clientSideTeleportInterpolation = 0;
		}
		else if (!this.world.isRemote)
		{
			IBlockState iblockstate = this.world.getBlockState(blockpos);
			
			if (iblockstate.getMaterial() != Material.AIR)
			{
				if (iblockstate.getBlock() == Blocks.PISTON_EXTENSION)
				{
					EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(BlockPistonBase.FACING);
					
					if (this.world.isAirBlock(blockpos.offset(enumfacing)))
					{
						blockpos = blockpos.offset(enumfacing);
						this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
					}
					else
					{
						this.teleportShulkerToBlock();
					}
				}
				else if (iblockstate.getBlock() == Blocks.PISTON_HEAD)
				{
					EnumFacing enumfacing3 = (EnumFacing)iblockstate.getValue(BlockPistonExtension.FACING);
					
					if (this.world.isAirBlock(blockpos.offset(enumfacing3)))
					{
						blockpos = blockpos.offset(enumfacing3);
						this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos));
					}
					else
					{
						this.teleportShulkerToBlock();
					}
				}
				else
				{
					this.teleportShulkerToBlock();
				}
			}

			BlockPos blockpos1 = blockpos.offset(this.getAttachmentFacing());
			
			if (!this.world.isBlockNormalCube(blockpos1, false))
			{
				boolean flag = false;
				
				for (EnumFacing enumfacing1 : EnumFacing.values())
				{
					blockpos1 = blockpos.offset(enumfacing1);
					
					if (this.world.isBlockNormalCube(blockpos1, false))
					{
						this.dataManager.set(ATTACHED_FACE, enumfacing1);
						flag = true;
						break;
					}
				}

				if (!flag)
				{
					this.teleportShulkerToBlock();
				}
			}

			BlockPos blockpos2 = blockpos.offset(this.getAttachmentFacing().getOpposite());
			
			if (this.world.isBlockNormalCube(blockpos2, false))
			{
				this.teleportShulkerToBlock();
			}
		}

		float f1 = (float)this.getPeekTick() * 0.01F;
		this.prevPeekAmount = this.peekAmount;
		
		if (this.peekAmount > f1)
		{
			this.peekAmount = MathHelper.clamp(this.peekAmount - 0.05F, f1, 1.0F);
		}
		else if (this.peekAmount < f1)
		{
			this.peekAmount = MathHelper.clamp(this.peekAmount + 0.05F, 0.0F, f1);
		}

		if (blockpos != null && this.world.getBlockState(blockpos) != Blocks.AIR)
		{
			if (this.world.isRemote)
			{
				if (this.clientSideTeleportInterpolation > 0 && this.currentAttachmentPosition != null)
				{
					--this.clientSideTeleportInterpolation;
				}
				else
				{
					this.currentAttachmentPosition = blockpos;
				}
			}

			if (this.isEntityAlive() && this.world.getBlockState(blockpos) != Blocks.AIR)
			{
				this.lastTickPosX = (this.prevPosX = this.posX = blockpos.getX() + 0.5D);
				this.lastTickPosY = (this.prevPosY = this.posY = blockpos.getY());
				this.lastTickPosZ = (this.prevPosZ = this.posZ = blockpos.getZ() + 0.5D);
			}
			double d3 = 0.5D - (double)MathHelper.sin((0.5F + this.peekAmount) * (float)Math.PI) * 0.5D;
			double d4 = 0.5D - (double)MathHelper.sin((0.5F + this.prevPeekAmount) * (float)Math.PI) * 0.5D;
			double d5 = d3 - d4;
			double d0 = 0.0D;
			double d1 = 0.0D;
			double d2 = 0.0D;
			if (d5 > 0.0D)
			{
				List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
				
				if (!list.isEmpty())
				{
					for (Entity entity : list)
					{
						if (!(entity instanceof EntityShulker) && !entity.noClip)
						{
							entity.move(MoverType.SHULKER, d0, d1, d2);
						}
					}
				}
			}
			if (this.isChild())
			{
				float f = 0;
				if (f > f1)
				{
					f = MathHelper.clamp(f - 0.05F, f1, 1.0F);
				} else if (f < f1)
					{
						f = MathHelper.clamp(f + 0.05F, 0.0F, f1);
					}
					d3 = 0.25D - MathHelper.sin((0.5F + f) * 3.1415927F) * 0.25D;
					EnumFacing enumfacing2 = getAttachmentFacing();
					switch (enumfacing2)
					{
						case DOWN:default:setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.25D, this.posY, this.posZ - 0.25D, this.posX + 0.25D, this.posY + 0.5D + d3, this.posZ + 0.25D));
						break;
						case UP:setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.25D, this.posY - d3 + 0.5D, this.posZ - 0.25D, this.posX + 0.25D, this.posY + 1.0D, this.posZ + 0.25D));
						break;
						case NORTH:setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.25D, this.posY + 0.25D, this.posZ - 0.5D, this.posX + 0.25D, this.posY + 0.75D, this.posZ + 0.0D + d3));
						break;
						case SOUTH:setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.25D, this.posY + 0.25D, this.posZ - 0.0D - d3, this.posX + 0.25D, this.posY + 0.75D, this.posZ + 0.5D));
						break;
						case WEST:setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY + 0.25D, this.posZ - 0.25D, this.posX + 0.0D + d3, this.posY + 0.75D, this.posZ + 0.25D));
						break;
						case EAST:setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.0D - d3, this.posY + 0.25D, this.posZ - 0.25D, this.posX + 0.5D, this.posY + 0.75D, this.posZ + 0.25D));
					}
				}
				else
				{
					EnumFacing enumfacing2 = this.getAttachmentFacing();
					
					switch (enumfacing2)
					{
						case DOWN:
						this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D + d3, this.posZ + 0.5D));
						d1 = d5;
						break;
						case UP:
						this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY - d3, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
						d1 = -d5;
						break;
						case NORTH:
						this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D + d3));
						d2 = d5;
						break;
						case SOUTH:
						this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D - d3, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
						d2 = -d5;
						break;
						case WEST:
						this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D + d3, this.posY + 1.0D, this.posZ + 0.5D));
						d0 = d5;
						break;
						case EAST:
						this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D - d3, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
						d0 = -d5;
					}
					if (d5 > 0.0D)
					{
						List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
						
						if (!list.isEmpty())
						{
							for (Entity entity : list)
							{
								if (!(entity instanceof EntityShulker) && !entity.noClip)
								{
									entity.move(MoverType.SHULKER, d0, d1, d2);
								}
							}
						}
					}
				}
			}
		}
		public void setPosition(double x, double y, double z)
		{
			super.setPosition(x, y, z);
			if ((this.dataManager != null) && this.isEntityAlive())
			{
				Optional<BlockPos> optional = (Optional)this.dataManager.get(ATTACHED_BLOCK_POS);
				Optional<BlockPos> optional1 = Optional.of(new BlockPos(x, y, z));
				if (!optional1.equals(optional))
				{
					this.dataManager.set(ATTACHED_BLOCK_POS, optional1);
					this.dataManager.set(PEEK_TICK, Byte.valueOf((byte)0));
					this.isAirBorne = true;
				}
			}
		}
		public boolean teleportTo(double x, double y, double z)
		{
			this.currentAttachmentPosition = null;
			this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_SHULKER_TELEPORT, getSoundCategory(), this.getSoundVolume(), 0.95F);
			playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, this.getSoundVolume(), 0.95F);
			return true;
		}
		protected boolean teleportShulkerToBlock()
		{
			if ((!isAIDisabled()) && (isEntityAlive()) && !this.isRiding())
			{
				BlockPos blockpos = new BlockPos(this);
				for (int i = 0; i < 5; i++)
				{
					BlockPos blockpos1 = blockpos.add(16 - this.rand.nextInt(32), 16 - this.rand.nextInt(32), 16 - this.rand.nextInt(32));
					if ((blockpos1.getY() > 0) && (this.world.isAirBlock(blockpos1)) && this.world.isInsideWorldBorder(this) && (this.world.getCollisionBoxes(this, new AxisAlignedBB(blockpos1)).isEmpty()))
					{
						boolean flag = false;
						for (EnumFacing enumfacing : EnumFacing.values())
						{
							if (this.world.isBlockNormalCube(blockpos1.offset(enumfacing), false))
							{
								this.dataManager.set(ATTACHED_FACE, enumfacing);
								flag = true;
								break;
							}
						}
						if (flag)
						{
							playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, this.getSoundVolume(), 0.95F);
							this.dataManager.set(ATTACHED_BLOCK_POS, Optional.of(blockpos1));
							this.dataManager.set(PEEK_TICK, Byte.valueOf((byte)0));
							setAttackTarget((EntityLivingBase)null);
							return true;
						}
					}
				}
				return false;
			}
			return true;
		}
		public void performSpecialAttack()
		{
			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(48.0D, 48.0D, 48.0D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if ((list != null) && (!list.isEmpty()))
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityLivingBase entity = (EntityLivingBase)list.get(i1);
					if (entity != null)
					{
						if (!isOnSameTeam(entity))
						{
							EntityShulkerBulletFriendly entityshulkerbullet = new EntityShulkerBulletFriendly(this.world, this, entity, getAttachmentFacing().getAxis());
							this.world.spawnEntity(entityshulkerbullet);
							playSound(SoundEvents.ENTITY_SHULKER_SHOOT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F);
						}
					}
				}
			}
			setSpecialAttackTimer(800);
		}
		@SideOnly(Side.CLIENT)
		public boolean isAttachedToBlock()
		{
			return this.currentAttachmentPosition != null && this.getAttachmentPos() != null;
		}
		public void onLivingUpdate()
		{
			super.onLivingUpdate();
			
			if (!this.getCurrentBook().isEmpty())
			this.updateArmorModifier(100);
			
			this.setSize(1F, 1F);
			
			if (this.isEntityAlive() && this.world.getBlockState(getAttachmentPos()) != Blocks.AIR)
			{
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}

			if (this.world.getBlockState(getAttachmentPos()) == Blocks.AIR && !this.isRiding())
			{
				this.teleportShulkerToBlock();
			}

			if (!this.isWild())
			{
				if (getDistanceSq(getOwner()) >= 2304.0D)
				{
					this.ticksExisted = 0;
					this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), this.getSoundVolume(), 2.0F / this.height);
					this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundVolume(), 2.0F / this.height);
					this.getNavigator().clearPath();
					setLocationAndAngles(getOwner().posX, getOwner().posY, getOwner().posZ, this.rotationYaw, this.rotationPitch);
				}
			}
			this.prevRenderYawOffset = this.renderYawOffset = this.rotationYaw = 180.0F;
			this.isAirBorne = false;
			this.onGround = true;
			setSprinting(false);
			if ((getAttackTarget() != null) && (getDistanceSq(getAttackTarget()) < 512.0D) && (getSpecialAttackTimer() <= 0) && (isHero()))
			{
				performSpecialAttack();
			}

			List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(32D), Predicates.and(new Predicate[] { EntitySelectors.IS_ALIVE }));
			if ((list != null) && (!list.isEmpty()) && this.ticksExisted % 60 == 0 && this.isPositiveShulker() && !this.isAIDisabled())
			{
				for (int i1 = 0; i1 < list.size(); i1++)
				{
					EntityLivingBase entity = (EntityLivingBase)list.get(i1);
					if (!this.world.isRemote && entity != null && this.isOnSameTeam(entity) && this.rand.nextInt(60) == 0 && !(entity instanceof IMob))
					{
						EntityShulkerBulletFriendly entityshulkerbullet = new EntityShulkerBulletFriendly(EntityShulker.this.world, EntityShulker.this, entity, EntityShulker.this.getAttachmentFacing().getAxis());
						EntityShulker.this.world.spawnEntity(entityshulkerbullet);
						EntityShulker.this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0F, (EntityShulker.this.rand.nextFloat() - EntityShulker.this.rand.nextFloat()) * 0.2F + 1.0F);
						break;
					}
				}
			}
		}
		public void notifyDataManagerChange(DataParameter<?> key)
		{
			if ((ATTACHED_BLOCK_POS.equals(key)) && (this.world.isRemote) && (!isRiding()))
			{
				BlockPos blockpos = getAttachmentPos();
				if (blockpos != null)
				{
					if (this.currentAttachmentPosition == null)
					{
						this.currentAttachmentPosition = blockpos;
					}
					else
					{
						this.clientSideTeleportInterpolation = 6;
					}
					if (this.isEntityAlive() && this.world.getBlockState(getAttachmentPos()) != Blocks.AIR)
					{
						this.lastTickPosX = (this.prevPosX = this.posX = blockpos.getX() + 0.5D);
						this.lastTickPosY = (this.prevPosY = this.posY = blockpos.getY());
						this.lastTickPosZ = (this.prevPosZ = this.posZ = blockpos.getZ() + 0.5D);
					}
				}
			}
			super.notifyDataManagerChange(key);
		}
		@SideOnly(Side.CLIENT)
		public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_)
		{
			this.newPosRotationIncrements = 0;
		}
		public boolean attackEntityFrom(DamageSource source, float amount)
		{
			if (isClosed())
			{
				Entity entity = source.getTrueSource();
				if ((entity instanceof EntityArrow))
				{
					return false;
				}
			}
			if (super.attackEntityFrom(source, amount))
			{
				if (this.rand.nextInt(3) == 0)
				this.updateArmorModifier(0);
				
				if (getHealth() < getMaxHealth() * 0.5D)
				{
					teleportShulkerToBlock();
				}
				return true;
			}
			return false;
		}
		private boolean isClosed()
		{
			return this.getPeekTick() == 0;
		}
		public AxisAlignedBB getCollisionBoundingBox()
		{
			return isEntityAlive() ? getEntityBoundingBox() : null;
		}
		public EnumFacing getAttachmentFacing()
		{
			return (EnumFacing)this.dataManager.get(ATTACHED_FACE);
		}

		@Nullable
		public BlockPos getAttachmentPos()
		{
			return (BlockPos)((Optional)this.dataManager.get(ATTACHED_BLOCK_POS)).orNull();
		}

		public void setAttachmentPos(@Nullable BlockPos pos)
		{
			this.dataManager.set(ATTACHED_BLOCK_POS, Optional.fromNullable(pos));
		}

		public int getPeekTick()
		{
			return ((Byte)this.dataManager.get(PEEK_TICK)).byteValue();
		}

		/**
		* Applies or removes armor modifier
		*/
		public void updateArmorModifier(int p_184691_1_)
		{
			if (!this.world.isRemote)
			{
				this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(COVERED_ARMOR_BONUS_MODIFIER);
				
				if (p_184691_1_ == 0)
				{
					this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(COVERED_ARMOR_BONUS_MODIFIER);
					if (this.getCurrentBook().isEmpty())
					this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0F, 1.0F);
				}
				else
				{
					if (this.getCurrentBook().isEmpty())
					this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0F, 1.0F);
				}
			}

			this.dataManager.set(PEEK_TICK, Byte.valueOf((byte)p_184691_1_));
		}

		@SideOnly(Side.CLIENT)
		public float getClientPeekAmount(float p_184688_1_)
		{
			return this.prevPeekAmount + (this.peekAmount - this.prevPeekAmount) * p_184688_1_;
		}

		@SideOnly(Side.CLIENT)
		public int getClientTeleportInterp()
		{
			return this.clientSideTeleportInterpolation;
		}

		@SideOnly(Side.CLIENT)
		public BlockPos getOldAttachPos()
		{
			return this.currentAttachmentPosition;
		}
		public float getEyeHeight()
		{
			return this.isChild() ? (getAttachmentFacing() == EnumFacing.UP ? 0.75F : (getAttachmentFacing() == EnumFacing.DOWN ? 0.25F : 0.5F)) : 0.5F;
		}
		public int getVerticalFaceSpeed()
		{
			return 180;
		}
		public void applyEntityCollision(Entity entityIn)
		{
			teleportShulkerToBlock();
		}
		public float getCollisionBorderSize()
		{
			return 0.0F;
		}

		@Nullable
		protected ResourceLocation getLootTable()
		{
			switch (this.getColor())
			{
				case WHITE:
				default:
				return ELoot.ENTITIES_SHULKER_WHITE;
				case ORANGE:
				return ELoot.ENTITIES_SHULKER_ORANGE;
				case MAGENTA:
				return ELoot.ENTITIES_SHULKER_MAGENTA;
				case LIGHT_BLUE:
				return ELoot.ENTITIES_SHULKER_LIGHT_BLUE;
				case YELLOW:
				return ELoot.ENTITIES_SHULKER_YELLOW;
				case LIME:
				return ELoot.ENTITIES_SHULKER_LIME;
				case PINK:
				return ELoot.ENTITIES_SHULKER_PINK;
				case GRAY:
				return ELoot.ENTITIES_SHULKER_GRAY;
				case SILVER:
				return ELoot.ENTITIES_SHULKER_SILVER;
				case CYAN:
				return ELoot.ENTITIES_SHULKER_CYAN;
				case PURPLE:
				return ELoot.ENTITIES_SHULKER_PURPLE;
				case BLUE:
				return ELoot.ENTITIES_SHULKER_BLUE;
				case BROWN:
				return ELoot.ENTITIES_SHULKER_BROWN;
				case GREEN:
				return ELoot.ENTITIES_SHULKER_GREEN;
				case RED:
				return ELoot.ENTITIES_SHULKER_RED;
				case BLACK:
				return ELoot.ENTITIES_SHULKER_BLACK;
			}
		}

		public void inflictShulkerEffects(EntityLivingBase entity)
		{
			if (!this.world.isRemote)
			{
				switch (this.getColor())
				{
					case WHITE:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.INVISIBILITY, 2400, 0);
					break;
					case ORANGE:
					entity.setFire(15);
					break;
					case MAGENTA:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.NAUSEA, 400, 0);
					if (entity.isEntityUndead())
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.INSTANT_HEALTH, 1, 0);
					else
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.INSTANT_DAMAGE, 1, 1);
					break;
					case LIGHT_BLUE:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.LUCK, 2400, 0);
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.STRENGTH, 1200, 1);
					break;
					case YELLOW:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.FIRE_RESISTANCE, 2400, 0);
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.HASTE, 1200, 0);
					break;
					case LIME:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.JUMP_BOOST, 2400, 1);
					break;
					case PINK:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.RESISTANCE, 2400, 0);
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.REGENERATION, 600, 1);
					break;
					case GRAY:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.BLINDNESS, 400, 0);
					break;
					case SILVER:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.SLOWNESS, 200, 1);
					break;
					case CYAN:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.NIGHT_VISION, 2400, 0);
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.SPEED, 2400, 1);
					break;
					case BLUE:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.WATER_BREATHING, 2400, 0);
					break;
					case BROWN:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.MINING_FATIGUE, 200, 1);
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.WEAKNESS, 200, 0);
					break;
					case GREEN:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.POISON, 200, 1);
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.HUNGER, 200, 0);
					break;
					case RED:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.SATURATION, 1, 1);
					if (entity.isEntityUndead())
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.INSTANT_DAMAGE, 1, 1);
					else
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.INSTANT_HEALTH, 1, 1);
					break;
					case BLACK:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.WITHER, 200, 1);
					break;
					default:
					this.inflictCustomStatusEffect(this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? EnumDifficulty.PEACEFUL : EnumDifficulty.EASY, entity, MobEffects.LEVITATION, 200, 0);
				}
			}
		}
		public boolean attackEntityAsMob(Entity p_70652_1_)
		{
			this.inflictShulkerEffects((EntityLivingBase)p_70652_1_);
			return this.isOnSameTeam((EntityLivingBase)p_70652_1_) ? true : super.attackEntityAsMob(p_70652_1_);
		}
		public boolean isPositiveShulker()
		{
			switch (this.getColor())
			{
				case WHITE:
				case LIGHT_BLUE:
				case YELLOW:
				case LIME:
				case PINK:
				case CYAN:
				case BLUE:
				case RED:
				return true;
				default:
				return false;
			}
		}
		protected SoundEvent getRegularHurtSound()
		{
			return ESound.woodHit;
		}
		protected SoundEvent getPierceHurtSound()
		{
			return ESound.woodHitPierce;
		}
		protected SoundEvent getCrushHurtSound()
		{
			return ESound.woodHitCrush;
		}
		class AIAttack extends EntityAIBase
		{
			private int field_188520_b;
			public AIAttack()
			{
				setMutexBits(3);
			}
			public boolean shouldExecute()
			{
				EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
				return !EntityShulker.this.isPositiveShulker() && (entitylivingbase != null) && (entitylivingbase.isEntityAlive());
			}
			public void startExecuting()
			{
				EntityShulker.this.updateArmorModifier(100);
				this.field_188520_b = 60;
			}
			public void resetTask()
			{
				EntityShulker.this.updateArmorModifier(0);
				EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
				if (entitylivingbase != null && !entitylivingbase.isEntityAlive())
				{
					EntityShulker.this.setAttackTarget(null);
				}
			}
			public void updateTask()
			{
				this.field_188520_b -= 1;
				EntityLivingBase entitylivingbase = EntityShulker.this.getAttackTarget();
				EntityShulker.this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 180.0F, 180.0F);
				double d0 = EntityShulker.this.getDistanceSq(entitylivingbase);
				if (d0 < 2000D)
				{
					if (this.field_188520_b <= 0 && EntityShulker.this.canEntityBeSeen(entitylivingbase))
					{
						if (EntityShulker.this.moralRaisedTimer > 200)
						{
							this.field_188520_b = 5;
						} else
							this.field_188520_b = 20 + EntityShulker.this.rand.nextInt(10) * 20 / 2;
							if (d0 < 16.0D)
							{
								EntityShulker.this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0F, (EntityShulker.this.rand.nextFloat() - EntityShulker.this.rand.nextFloat()) * 0.2F + 1.25F);
								EntityShulker.this.attackEntityAsMob(entitylivingbase);
								if (d0 < 12.0D)
								{
									EntityShulker.this.teleportShulkerToBlock();
								}
							}
							else {
								EntityShulkerBulletFriendly entityshulkerbullet = new EntityShulkerBulletFriendly(EntityShulker.this.world, EntityShulker.this, entitylivingbase, EntityShulker.this.getAttachmentFacing().getAxis());
								EntityShulker.this.world.spawnEntity(entityshulkerbullet);
								EntityShulker.this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0F, (EntityShulker.this.rand.nextFloat() - EntityShulker.this.rand.nextFloat()) * 0.2F + 1.0F);
							}
						}
					}
					else {
						EntityShulker.this.setAttackTarget((EntityLivingBase)null);
						this.resetTask();
					}
					super.updateTask();
				}
			}
			class AIPeek
			extends EntityAIBase
			{
				private int field_188522_b;
				private AIPeek() { }
				public boolean shouldExecute()
				{
					return ((EntityShulker.this.getAttackTarget() == null) && (EntityShulker.this.rand.nextInt(20) == 0)) || (EntityShulker.this.isSneaking());
				}
				public boolean shouldContinueExecuting()
				{
					return (EntityShulker.this.getAttackTarget() == null) && (this.field_188522_b > 0);
				}
				public void startExecuting()
				{
					if (EntityShulker.this.isSneaking())
					{
						this.field_188522_b = 100;
					} else
						this.field_188522_b = (40 * (1 + EntityShulker.this.rand.nextInt(3)));
						EntityShulker.this.updateArmorModifier(30);
					}
					public void resetTask()
					{
						if (EntityShulker.this.getAttackTarget() == null)
						{
							this.field_188522_b = 0;
							EntityShulker.this.updateArmorModifier(0);
						}
					}
					public void updateTask()
					{
						this.field_188522_b -= 1;
					}
				}
				class BodyHelper extends EntityBodyHelper
				{
					public BodyHelper(EntityLivingBase p_i47062_2_)
					{
						super(p_i47062_2_);
					}
					public void updateRenderAngles() { }
				}
			}

			
			