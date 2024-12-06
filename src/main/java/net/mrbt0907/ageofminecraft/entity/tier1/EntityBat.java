package net.mrbt0907.ageofminecraft.entity.tier1;
import java.util.Calendar;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EnumTier;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;

public class EntityBat extends EntityEngendered implements EntityFlying
{
	private static final DataParameter<Byte> HANGING = EntityDataManager.createKey(EntityBat.class, DataSerializers.BYTE);
	private BlockPos spawnPosition;
	public EntityBat(World worldIn)
	{
		super(worldIn);
		setSize(0.5F, 0.9F);
		setIsBatHanging(true);
	}
	
	protected void entityInit()
	{
		super.entityInit();
		dataManager.register(HANGING, Byte.valueOf((byte)0));
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}

	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		dataManager.set(HANGING, Byte.valueOf(tagCompund.getByte("BatFlags")));
	}
	
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setByte("BatFlags", ((Byte)dataManager.get(HANGING)).byteValue());
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		if (getIsBatHanging())
		{
			motionX = motionY = motionZ = 0.0D;
			posY = MathHelper.floor(posY) + 1.0D - height;
		}
		else if (!onGround && motionY < 0.0D && isEntityAlive())
			motionY *= 0.6D;
	}
	
	protected void updateAITasks()
	{
		super.updateAITasks();
		BlockPos position = getPosition();
		BlockPos abovePosition = position.up();
		
		if (getIsBatHanging())
		{
			if (ticksExisted % 100 == 0)
				if (!world.getBlockState(abovePosition).isNormalCube())
				{
					setIsBatHanging(false);
					world.playEvent((EntityPlayer)null, 1025, position, 0);
				}
				else
				{
					if (rand.nextInt(4) == 0)
						rotationYawHead = rand.nextInt(360);
					
					if (world.getNearestPlayerNotCreative(this, 4.0D) != null)
					{
						setIsBatHanging(false);
						world.playEvent((EntityPlayer)null, 1025, position, 0);
					}
				}
		}
		else
		{
			if (spawnPosition != null)
			{
				if (!world.isAirBlock(spawnPosition) || spawnPosition.getY() < 1)
					spawnPosition = null;
			}
			else if (rand.nextInt(30) == 0 || spawnPosition != null && spawnPosition.distanceSq((int)posX, (int)posY, (int)posZ) < 4.0D)
					spawnPosition = new BlockPos((int)posX + rand.nextInt(7) - rand.nextInt(7), (int)posY + rand.nextInt(6) - 2, (int)posZ + rand.nextInt(7) - rand.nextInt(7));
			if (rand.nextInt(100) == 0 && world.getBlockState(abovePosition).isNormalCube())
				setIsBatHanging(true);
			if (getActivePotionEffect(MobEffects.LUCK) == null)
			{
				EntityPlayer player = world.getClosestPlayerToEntity(this, 200D);
				EntityLivingBase target = getAttackTarget();
				Entity owner = getOwner();
				
				if (player != null && player.equals(owner) && target == null && getDistanceSq(owner) > 200D)
				{
					double dx = owner.posX - posX;
					double dy = owner.posY - posY;
					double dz = owner.posZ - posZ;
					float f2 = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
					motionX = (dx / f2 * 0.5D * 0.5D + motionX * 0.5D);
					motionY = (dy / f2 * 0.5D * 0.5D + motionZ * 0.5D);
					motionZ = (dz / f2 * 0.5D * 0.5D + motionZ * 0.5D);
					faceEntity(owner, 180.0F, 30.0F);
				}
				else
				{
					if (target != null)
					{
						double d01 = target.posX - posX;
						double d11 = target.posZ - posZ;
						float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11);
						motionX = (d01 / f2 * 0.5D * 0.5D + motionX);
						motionZ = (d11 / f2 * 0.5D * 0.5D + motionZ);
						faceEntity(target, 180.0F, 30.0F);
						if (posY < target.posY)
							motionY += (0.25D - motionY);
					}
					else if (spawnPosition != null)
					{
						double d0 = spawnPosition.getX() + 0.5D - posX;
						double d1 = spawnPosition.getY() + 0.1D - posY;
						double d2 = spawnPosition.getZ() + 0.5D - posZ;
						motionX += (Math.signum(d0) * 0.5D - motionX) * 0.10000000149011612D;
						motionY += (Math.signum(d1) * 0.699999988079071D - motionY) * 0.10000000149011612D;
						motionZ += (Math.signum(d2) * 0.5D - motionZ) * 0.10000000149011612D;
						float f = (float)(Maths.fastATan2(motionZ, motionX) * 180.0D / 3.141592653589793D) - 90.0F;
						float f1 = MathHelper.wrapDegrees(f - rotationYaw);
						moveForward = 0.5F;
						rotationYaw += f1;
					}
				}
			}
		}
	}
	
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		if (super.processInteract(player, hand)) return true;
		
		ItemStack stack = player.getHeldItem(hand);
		if (stack.isEmpty() && hasOwner())
		{
			addPotionEffect(new PotionEffect(MobEffects.LUCK, 100));
			player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, player.getActivePotionEffect(MobEffects.NIGHT_VISION) != null ? player.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration() + 200 : 200));
			player.swingArm(EnumHand.MAIN_HAND);
			return true;
		}
		
		return false;
	}
	
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (super.attackEntityFrom(source, amount))
			return true;
		if (!world.isRemote && getIsBatHanging())
			setIsBatHanging(false);
		return false;
	}
	
	protected float getSoundVolume()
	{
		return 0.1F;
	}
	
	protected float getSoundPitch()
	{
		return super.getSoundPitch() * 0.95F;
	}
	
	protected SoundEvent getAmbientSound()
	{
		return (getIsBatHanging()) && (rand.nextInt(4) != 0) ? null : SoundEvents.ENTITY_BAT_AMBIENT;
	}
	
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_BAT_HURT;
	}
	
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_BAT_DEATH;
	}
	
	public boolean canBePushed()
	{
		return false;
	}
	
	protected void collideWithEntity(Entity entityIn) { }
	protected void collideWithNearbyEntities() { }
	public boolean getIsBatHanging()
	{
		return (((Byte)dataManager.get(HANGING)).byteValue() & 0x1) != 0;
	}
	
	public void setIsBatHanging(boolean isHanging)
	{
		byte b0 = ((Byte)dataManager.get(HANGING)).byteValue();
		if (isHanging)
			dataManager.set(HANGING, Byte.valueOf((byte)(b0 | 0x1)));
		else
			dataManager.set(HANGING, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
	}
	
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	public boolean takesFallDamage()
	{
		return false;
	}
	
	protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_) { }
	
	public boolean doesEntityNotTriggerPressurePlate()
	{
		return true;
	}
	
	public float getEyeHeight()
	{
		return height / 2.0F;
	}
	
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return LootRegistry.ENTITIES_BAT;
	}
	
	public boolean getCanSpawnHere()
	{
		BlockPos blockpos = new BlockPos(posX, getEntityBoundingBox().minY, posZ);
		if (blockpos.getY() >= world.getSeaLevel())
			return false;
		int i = world.getLightFromNeighbors(blockpos);
		int j = 4;
		if (isDateAroundHalloween(world.getCurrentDate()))
			j = 7;
		else if (rand.nextBoolean())
			return false;
		return i >= rand.nextInt(j) ? false : super.getCanSpawnHere();
	}
	
	private boolean isDateAroundHalloween(Calendar calendar)
	{
		return calendar.get(2) + 1 == 10 && calendar.get(5) >= 20 || calendar.get(2) + 1 == 11 && calendar.get(5) <= 3;
	}
	
	@Override
	public EnumTier getTier()
	{
		return EnumTier.TIER1;
	}
	
	@Override
	public EnumAIStance getDefaultStance()
	{
		return EnumAIStance.AGGRESSIVE;
	}
	
	@Override
	public long getBaseVigor()
	{
		return 0;
	}
	
	@Override
	public long getBaseStrength()
	{
		return 0;
	}
	
	@Override
	public long getBaseStamina()
	{
		return 0;
	}
	
	@Override
	public long getBaseIntelligence()
	{
		return 0;
	}
	
	@Override
	public long getBaseDexterity()
	{
		return 0;
	}
	
	@Override
	public long getBaseAgility()
	{
		return 0;
	}
}