package net.minecraft.AgeOfMinecraft.entity;

import net.minecraft.AgeOfMinecraft.items.ItemManaCollector;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityManaOrb extends Entity
{
	/** A constantly increasing value that RenderXPOrb uses to control the colour shifting (Green / yellow) */
	public int xpColor;
	/** The age of the XP orb in ticks. */
	public int age;
	/** The closest EntityPlayer to this orb. */
	public EntityPlayer closestPlayer = null;
	public ItemStack magnet = ItemStack.EMPTY;
	/** Threshold color for tracking players */
	private int xpTargetColor;
	private static final DataParameter<Integer> amount = EntityDataManager.<Integer>createKey(EntityManaOrb.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> entropy = EntityDataManager.<Boolean>createKey(EntityManaOrb.class, DataSerializers.BOOLEAN);
	private int blinkTimer;
	public EntityManaOrb(World worldIn, double x, double y, double z, int expValue, boolean isEntropy)
	{
		this(worldIn);
		this.setPosition(x, y, z);
		this.setMana(expValue);
		this.setEntropy(isEntropy);
	}

	/**
	* returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	* prevent them from trampling crops
	*/
	protected boolean canTriggerWalking()
	{
		return false;
	}

	public EntityManaOrb(World worldIn)
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
		this.setNoGravity(true);
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * 0.1D - 0.05D) * 2.0F);
		this.motionY = (double)((float)(Math.random() * 0.1D) * 2.0F);
		this.motionZ = (double)((float)(Math.random() * 0.1D - 0.05D) * 2.0F);
	}

	protected void entityInit()
	{
		getDataManager().register(amount, Integer.valueOf(0));
		getDataManager().register(entropy, Boolean.valueOf(false));
	}
	public void setEntropy(boolean value)
	{
		getDataManager().set(entropy, Boolean.valueOf(value));
	}
	public boolean getEntropy()
	{
		return ((Boolean)this.dataManager.get(entropy)).booleanValue();
	}
	public void setMana(int mana)
	{
		getDataManager().set(amount, Integer.valueOf(mana));
	}
	public int getMana()
	{
		return ((Integer)this.dataManager.get(amount)).intValue();
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		float f = 0.5F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		int i = super.getBrightnessForRender();
		int j = i & 255;
		int k = i >> 16 & 255;
		j = j + (int)(f * 15.0F * 16.0F);
		
		if (j > 240)
		{
			j = 240;
		}

		return j | k << 16;
	}

	protected int getFireImmuneTicks()
	{
		return Integer.MAX_VALUE;
	}
	@SideOnly(Side.CLIENT)
	public static double getRenderDistanceWeight()
	{
		return 64D;
	}
	/**
	* Called to update the entity's position/logic.
	*/
	public void onUpdate()
	{
		super.onUpdate();
		if (!this.magnet.isEmpty() && !(this.magnet.getItem() instanceof ItemManaCollector))
		magnet = ItemStack.EMPTY;
		if (this.blinkTimer > 0)
		--this.blinkTimer;
		else if (this.getEntropy())
		this.blinkTimer += 20 + rand.nextInt(100);
		else
		this.blinkTimer += 20;
		
		if (this.ticksExisted % 5 == 0)
		{
			if (this.getEntropy())
				this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY + 0.125D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
			else
				this.world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, this.posX, this.posY + 0.125D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}
		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		if (!this.hasNoGravity())
		{
			this.motionY -= 0.029999999329447746D;
		}

		this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
		if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100)
		{
			this.xpTargetColor = this.xpColor;
		}

		if (!this.world.isRemote && this.ticksExisted > 20 && !this.magnet.isEmpty() && this.closestPlayer != null && this.getDistance(closestPlayer) <= 16D)
		{
			ItemStack stack = magnet;
			Item item = stack.getItem();
			if (item instanceof ItemManaCollector && (!this.getEntropy() && (((ItemManaCollector) item).getMana(stack) < ((ItemManaCollector) item).getMaxMana(stack)) || (this.getEntropy() && ((ItemManaCollector) item).getEntropy(stack) < ((ItemManaCollector) item).getMaxEntropy(stack))))
			{
				double d1 = (this.closestPlayer.posX - this.posX) / 24D;
				double d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / 24D;
				double d3 = (this.closestPlayer.posZ - this.posZ) / 24D;
				double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
				double d5 = 1.0D - d4;
				
				if (d5 > 0.0D)
				{
					d5 = d5 * d5;
					this.motionX += d1 / d4 * d5 * 0.1D;
					this.motionY += d2 / d4 * d5 * 0.1D;
					this.motionZ += d3 / d4 * d5 * 0.1D;
				}
			}
			else
			{
				this.magnet = ItemStack.EMPTY;
				this.closestPlayer = null;
			}
		}
		if (this.magnet.isEmpty() || this.closestPlayer == null || (this.closestPlayer != null && this.getDistance(closestPlayer) > 24D))
		{
			this.magnet = ItemStack.EMPTY;
			this.closestPlayer = null;
		}
		if (this.ticksExisted == 40)
		{
			this.motionX = 0D;
			this.motionY = 0D;
			this.motionZ = 0D;
		}

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		float f = 0.95F;
		
		if (this.onGround)
		{
			BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
			net.minecraft.block.state.IBlockState underState = this.world.getBlockState(underPos);
			f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.99F;
		}

		this.motionX *= (double)f;
		this.motionY *= 0.95D;
		this.motionZ *= (double)f;
		
		if (this.onGround)
		{
			this.motionY *= -0.8999999761581421D;
		}

		++this.xpColor;
		++this.age;
		
		if (this.age >= 6000)
			this.setDead();
	}

	/**
	* Returns if this entity is in water and will end up adding the waters velocity to the entity
	*/
	public boolean handleWaterMovement()
	{
		return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
	}

	/**
	* (abstract) Protected helper method to write subclass entity data to NBT.
	*/
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setShort("Age", (short)this.age);
		compound.setInteger("Value", this.getMana());
		compound.setBoolean("Entropy", this.getEntropy());
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		this.age = compound.getShort("Age");
		this.setMana(compound.getInteger("Value"));
		this.setEntropy(compound.getBoolean("Entropy"));
	}

	/**
	* Called by a player entity when they collide with an entity
	*/
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (!this.magnet.isEmpty() && this.closestPlayer != null && this.closestPlayer == entityIn)
		{
			ItemStack stack = magnet;
			Item item = stack.getItem();
			if (!this.world.isRemote && item instanceof ItemManaCollector)
			{
				if (this.getMana() > 0)
				{
					this.world.playSound(null, entityIn.getPosition(), this.getEntropy() ? SoundEvents.BLOCK_END_PORTAL_FRAME_FILL : SoundEvents.BLOCK_CHORUS_FLOWER_GROW, entityIn.getSoundCategory(), 2F, 2F);
					if (this.getEntropy() && ((ItemManaCollector) item).getEntropy(stack) < ((ItemManaCollector) item).getMaxEntropy(stack))
					{
						if (this.getMana() > (((ItemManaCollector)item).getMaxEntropy(stack) - ((ItemManaCollector)item).getEntropy(stack)))
						{
							int oldamount = this.getMana();
							this.setMana(oldamount - (((ItemManaCollector)item).getMaxEntropy(stack) - ((ItemManaCollector)item).getEntropy(stack)));
							((ItemManaCollector)item).increaseHolding(oldamount, stack, true);
							this.magnet = ItemStack.EMPTY;
							this.closestPlayer = null;
						}
						else
						{
							((ItemManaCollector)item).increaseHolding(getMana(), stack, true);
							this.setDead();
						}
					}
					else if (!this.getEntropy() && (((ItemManaCollector) item).getMana(stack) < ((ItemManaCollector) item).getMaxMana(stack)))
					{
						if (this.getMana() > (((ItemManaCollector)item).getMaxMana(stack) - ((ItemManaCollector)item).getMana(stack)))
						{
							int oldamount = this.getMana();
							this.setMana(oldamount - (((ItemManaCollector)item).getMaxMana(stack) - ((ItemManaCollector)item).getMana(stack)));
							((ItemManaCollector)item).increaseHolding(oldamount, stack, false);
							this.magnet = ItemStack.EMPTY;
							this.closestPlayer = null;
						}
						else
						{
							((ItemManaCollector)item).increaseHolding(getMana(), stack, false);
							this.setDead();
						}
					}
				}
				else
				this.setDead();
			}
		}
	}

	/**
	* Returns the XP value of this XP orb.
	*/
	public int getXpValue()
	{
		return this.getMana();
	}

	/**
	* Returns a number from 1 to 10 based on how much XP this orb is worth. This is used by RenderXPOrb to determine
	* what texture to use.
	*/
	public float getTextureByXP()
	{
		if (this.getEntropy())
		{
			if (this.getMana() >= 30)
			{
				if (this.blinkTimer > 20)
				return 1.992f;
				else if (this.blinkTimer > 10)
				return 2.496f;
				else
				return 3.0f;
			}
			else if (this.getMana() >= 20)
			{
				if (this.blinkTimer > 20)
				return 2.496f;
				else if (this.blinkTimer > 10)
				return 3.0f;
				else
				return 3.504f;
			}
			else if (this.getMana() >= 10)
			{
				if (this.blinkTimer > 20)
				return 3.0f;
				else if (this.blinkTimer > 10)
				return 3.504f;
				else
				return 1.992f;
			}
			else if (this.getMana() >= 5)
			{
				return 2.496f;
			}
			else
			{
				return 1.992f;
			}
		}
		else
		{
			if (this.getMana() >= 128)
			{
				if (this.blinkTimer > 13)
				return 0.0f;
				else if (this.blinkTimer > 7)
				return 0.504f;
				else
				return 1.008f;
			}
			else if (this.getMana() >= 24)
			{
				if (this.blinkTimer > 13)
				return 0.504f;
				else if (this.blinkTimer > 7)
				return 1.008f;
				else
				return 1.512f;
			}
			else if (this.getMana() >= 8)
			{
				if (this.blinkTimer > 10)
				return 1.512f;
				else
				return 0.0f;
			}
			else if (this.getMana() >= 4)
			{
				return 1.008f;
			}
			else if (this.getMana() >= 2)
			{
				return 0.5f;
			}
			else
			{
				return 0.0f;
			}
		}
	}
	/**
	* Returns a number from 1 to 10 based on how much XP this orb is worth. This is used by RenderXPOrb to determine
	* what texture to use.
	*/
	public float getTextureY()
	{
		if (this.getEntropy())
		{
			if (this.getMana() >= 30)
			{
				return 8.0f;
			}
			else if (this.getMana() >= 20)
			{
				return 4.0f;
			}
			else if (this.getMana() >= 10)
			{
				if (this.blinkTimer > 10)
				return 0.0f;
				else
				return 4.0f;
			}
			else if (this.getMana() >= 5)
			{
				return 0.0f;
			}
			else
			{
				return 0.0f;
			}
		}
		else
		{
			if (this.getMana() >= 512)
			{
				return 8.0f;
			}
			else if (this.getMana() >= 256)
			{
				return 8.0f;
			}
			else if (this.getMana() >= 128)
			{
				return 8.0f;
			}
			else if (this.getMana() >= 72)
			{
				return 4.0f;
			}
			else if (this.getMana() >= 48)
			{
				return 4.0f;
			}
			else if (this.getMana() >= 24)
			{
				return 4.0f;
			}
			else if (this.getMana() >= 8)
			{
				if (this.blinkTimer > 10)
				return 0.0f;
				else
				return 4.0f;
			}
			else if (this.getMana() >= 4)
			{
				return 0.0f;
			}
			else
			{
				return 0.0f;
			}
		}
	}

	/**
	* Get a fragment of the maximum experience points value for the supplied value of experience points value.
	*/
	public static int getXPSplit(int expValue)
	{
		if (expValue >= 1024)
		{
			return 1024;
		}
		else if (expValue >= 512)
		{
			return 512;
		}
		else if (expValue >= 256)
		{
			return 256;
		}
		else if (expValue >= 128)
		{
			return 128;
		}
		else if (expValue >= 64)
		{
			return 64;
		}
		else if (expValue >= 32)
		{
			return 32;
		}
		else if (expValue >= 16)
		{
			return 16;
		}
		else if (expValue >= 8)
		{
			return 8;
		}
		else if (expValue >= 4)
		{
			return 4;
		}
		else
		{
			return expValue >= 2 ? 2 : 1;
		}
	}

	/**
	* Returns true if it's possible to attack this entity with an item.
	*/
	public boolean canBeAttackedWithItem()
	{
		return false;
	}
}
