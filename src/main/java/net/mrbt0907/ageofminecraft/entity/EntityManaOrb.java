package net.mrbt0907.ageofminecraft.entity;

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
import net.mrbt0907.ageofminecraft.items.ItemManaCollector;


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
		isImmuneToFire = true;
		setPosition(x, y, z);
		setMana(expValue);
		setEntropy(isEntropy);
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
		setSize(0.25F, 0.25F);
		setNoGravity(true);
		rotationYaw = (float)(Math.random() * 360.0D);
		motionX = (double)((float)(Math.random() * 0.1D - 0.05D) * 2.0F);
		motionY = (double)((float)(Math.random() * 0.1D) * 2.0F);
		motionZ = (double)((float)(Math.random() * 0.1D - 0.05D) * 2.0F);
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
		return ((Boolean)dataManager.get(entropy)).booleanValue();
	}
	public void setMana(int mana)
	{
		getDataManager().set(amount, Integer.valueOf(mana));
	}
	public int getMana()
	{
		return ((Integer)dataManager.get(amount)).intValue();
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
		if (!magnet.isEmpty() && !(magnet.getItem() instanceof ItemManaCollector))
		magnet = ItemStack.EMPTY;
		if (blinkTimer > 0)
		--blinkTimer;
		else if (getEntropy())
		blinkTimer += 20 + rand.nextInt(100);
		else
		blinkTimer += 20;
		
		if (ticksExisted % 5 == 0)
		{
			if (getEntropy())
				world.spawnParticle(EnumParticleTypes.FLAME, posX, posY + 0.125D, posZ, 0.0D, 0.0D, 0.0D, new int[0]);
			else
				world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, posX, posY + 0.125D, posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}
		
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		if (!hasNoGravity())
		{
			motionY -= 0.029999999329447746D;
		}

		pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);
		if (xpTargetColor < xpColor - 20 + getEntityId() % 100)
		{
			xpTargetColor = xpColor;
		}

		if (!world.isRemote && ticksExisted > 20 && !magnet.isEmpty() && closestPlayer != null && getDistance(closestPlayer) <= 16D)
		{
			ItemStack stack = magnet;
			Item item = stack.getItem();
			if (item instanceof ItemManaCollector && (!getEntropy() && (((ItemManaCollector) item).getMana(stack) < ((ItemManaCollector) item).getMaxMana(stack)) || (getEntropy() && ((ItemManaCollector) item).getEntropy(stack) < ((ItemManaCollector) item).getMaxEntropy(stack))))
			{
				double d1 = (closestPlayer.posX - posX) / 24D;
				double d2 = (closestPlayer.posY + (double)closestPlayer.getEyeHeight() - posY) / 24D;
				double d3 = (closestPlayer.posZ - posZ) / 24D;
				double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
				double d5 = 1.0D - d4;
				
				if (d5 > 0.0D)
				{
					d5 = d5 * d5;
					motionX += d1 / d4 * d5 * 0.1D;
					motionY += d2 / d4 * d5 * 0.1D;
					motionZ += d3 / d4 * d5 * 0.1D;
				}
			}
			else
			{
				magnet = ItemStack.EMPTY;
				closestPlayer = null;
			}
		}
		if (magnet.isEmpty() || closestPlayer == null || (closestPlayer != null && getDistance(closestPlayer) > 24D))
		{
			magnet = ItemStack.EMPTY;
			closestPlayer = null;
		}
		if (ticksExisted == 40)
		{
			motionX = 0D;
			motionY = 0D;
			motionZ = 0D;
		}

		move(MoverType.SELF, motionX, motionY, motionZ);
		float f = 0.95F;
		
		if (onGround)
		{
			BlockPos underPos = new BlockPos(MathHelper.floor(posX), MathHelper.floor(getEntityBoundingBox().minY) - 1, MathHelper.floor(posZ));
			net.minecraft.block.state.IBlockState underState = world.getBlockState(underPos);
			f = underState.getBlock().getSlipperiness(underState, world, underPos, this) * 0.99F;
		}

		motionX *= (double)f;
		motionY *= 0.95D;
		motionZ *= (double)f;
		
		if (onGround)
		{
			motionY *= -0.8999999761581421D;
		}

		++xpColor;
		++age;
		
		if (age >= 6000)
			setDead();
	}

	/**
	* Returns if this entity is in water and will end up adding the waters velocity to the entity
	*/
	public boolean handleWaterMovement()
	{
		return world.handleMaterialAcceleration(getEntityBoundingBox(), Material.WATER, this);
	}

	/**
	* (abstract) Protected helper method to write subclass entity data to NBT.
	*/
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setShort("Age", (short)age);
		compound.setInteger("Value", getMana());
		compound.setBoolean("Entropy", getEntropy());
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		age = compound.getShort("Age");
		setMana(compound.getInteger("Value"));
		setEntropy(compound.getBoolean("Entropy"));
	}

	/**
	* Called by a player entity when they collide with an entity
	*/
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (!magnet.isEmpty() && closestPlayer != null && closestPlayer == entityIn)
		{
			ItemStack stack = magnet;
			Item item = stack.getItem();
			if (!world.isRemote && item instanceof ItemManaCollector)
			{
				if (getMana() > 0)
				{
					world.playSound(null, entityIn.getPosition(), getEntropy() ? SoundEvents.BLOCK_END_PORTAL_FRAME_FILL : SoundEvents.BLOCK_CHORUS_FLOWER_GROW, entityIn.getSoundCategory(), 2F, 2F);
					if (getEntropy() && ((ItemManaCollector) item).getEntropy(stack) < ((ItemManaCollector) item).getMaxEntropy(stack))
					{
						if (getMana() > (((ItemManaCollector)item).getMaxEntropy(stack) - ((ItemManaCollector)item).getEntropy(stack)))
						{
							int oldamount = getMana();
							setMana(oldamount - (((ItemManaCollector)item).getMaxEntropy(stack) - ((ItemManaCollector)item).getEntropy(stack)));
							((ItemManaCollector)item).increaseHolding(oldamount, stack, true);
							magnet = ItemStack.EMPTY;
							closestPlayer = null;
						}
						else
						{
							((ItemManaCollector)item).increaseHolding(getMana(), stack, true);
							setDead();
						}
					}
					else if (!getEntropy() && (((ItemManaCollector) item).getMana(stack) < ((ItemManaCollector) item).getMaxMana(stack)))
					{
						if (getMana() > (((ItemManaCollector)item).getMaxMana(stack) - ((ItemManaCollector)item).getMana(stack)))
						{
							int oldamount = getMana();
							setMana(oldamount - (((ItemManaCollector)item).getMaxMana(stack) - ((ItemManaCollector)item).getMana(stack)));
							((ItemManaCollector)item).increaseHolding(oldamount, stack, false);
							magnet = ItemStack.EMPTY;
							closestPlayer = null;
						}
						else
						{
							((ItemManaCollector)item).increaseHolding(getMana(), stack, false);
							setDead();
						}
					}
				}
				else
				setDead();
			}
		}
	}

	/**
	* Returns the XP value of this XP orb.
	*/
	public int getXpValue()
	{
		return getMana();
	}

	/**
	* Returns a number from 1 to 10 based on how much XP this orb is worth. This is used by RenderXPOrb to determine
	* what texture to use.
	*/
	public float getTextureByXP()
	{
		if (getEntropy())
		{
			if (getMana() >= 30)
			{
				if (blinkTimer > 20)
				return 1.992f;
				else if (blinkTimer > 10)
				return 2.496f;
				else
				return 3.0f;
			}
			else if (getMana() >= 20)
			{
				if (blinkTimer > 20)
				return 2.496f;
				else if (blinkTimer > 10)
				return 3.0f;
				else
				return 3.504f;
			}
			else if (getMana() >= 10)
			{
				if (blinkTimer > 20)
				return 3.0f;
				else if (blinkTimer > 10)
				return 3.504f;
				else
				return 1.992f;
			}
			else if (getMana() >= 5)
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
			if (getMana() >= 128)
			{
				if (blinkTimer > 13)
				return 0.0f;
				else if (blinkTimer > 7)
				return 0.504f;
				else
				return 1.008f;
			}
			else if (getMana() >= 24)
			{
				if (blinkTimer > 13)
				return 0.504f;
				else if (blinkTimer > 7)
				return 1.008f;
				else
				return 1.512f;
			}
			else if (getMana() >= 8)
			{
				if (blinkTimer > 10)
				return 1.512f;
				else
				return 0.0f;
			}
			else if (getMana() >= 4)
			{
				return 1.008f;
			}
			else if (getMana() >= 2)
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
		if (getEntropy())
		{
			if (getMana() >= 30)
			{
				return 8.0f;
			}
			else if (getMana() >= 20)
			{
				return 4.0f;
			}
			else if (getMana() >= 10)
			{
				if (blinkTimer > 10)
				return 0.0f;
				else
				return 4.0f;
			}
			else if (getMana() >= 5)
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
			if (getMana() >= 512)
			{
				return 8.0f;
			}
			else if (getMana() >= 256)
			{
				return 8.0f;
			}
			else if (getMana() >= 128)
			{
				return 8.0f;
			}
			else if (getMana() >= 72)
			{
				return 4.0f;
			}
			else if (getMana() >= 48)
			{
				return 4.0f;
			}
			else if (getMana() >= 24)
			{
				return 4.0f;
			}
			else if (getMana() >= 8)
			{
				if (blinkTimer > 10)
				return 0.0f;
				else
				return 4.0f;
			}
			else if (getMana() >= 4)
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

	public boolean canBeAttackedWithItem()
	{
		return false;
	}
}
