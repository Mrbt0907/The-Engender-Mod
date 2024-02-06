package net.minecraft.AgeOfMinecraft.entity.tier4;

import javax.annotation.Nullable;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityAbstractIllagers;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIVindicatorBreakDoor;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EntityVindicator extends EntityAbstractIllagers
{
	protected static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.<Byte>createKey(EntityVindicator.class, DataSerializers.BYTE);
	private boolean johnny;
	
	public EntityVindicator(World worldIn)
	{
		super(worldIn);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIFriendlyAttackMelee(this, 1.0D, true));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(5, new EntityAIWander(this, 0.6D, 80));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.0D, 32.0F, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.experienceValue = 10;
	}
	@SideOnly(Side.CLIENT)
	public EntityAbstractIllagers.IllagerArmPose getArmPose()
	{
		return this.isAggressive() ? EntityAbstractIllagers.IllagerArmPose.ATTACKING : EntityAbstractIllagers.IllagerArmPose.CROSSED;
	}

	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 0.75F;
	}

	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 1.5F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 1.5F;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityVindicator(this.world);
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3499999940395355D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0D);
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(DATA_FLAGS_ID, Byte.valueOf((byte)0));
	}
	protected float getSoundPitch()
	{
		return  super.getSoundPitch();
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

	/**
	* Get this Entity's EnumCreatureAttribute
	*/
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ILLAGER;
	}

	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_VINDICATION_ILLAGER;
	}

	@SideOnly(Side.CLIENT)
	private boolean getVindicatorFlag(int p_190637_1_)
	{
		int i = ((Byte)this.dataManager.get(DATA_FLAGS_ID)).byteValue();
		return (i & p_190637_1_) != 0;
	}

	private void setVindicatorFlag(int p_190638_1_, boolean p_190638_2_)
	{
		int i = ((Byte)this.dataManager.get(DATA_FLAGS_ID)).byteValue();
		
		if (p_190638_2_)
		{
			i = i | p_190638_1_;
		}
		else
		{
			i = i & ~p_190638_1_;
		}

		this.dataManager.set(DATA_FLAGS_ID, Byte.valueOf((byte)(i & 255)));
	}

	public void setAggressive(boolean p_190636_1_)
	{
		this.setVindicatorFlag(1, p_190636_1_);
	}

	@SideOnly(Side.CLIENT)
	public boolean isAggressive()
	{
		return this.getVindicatorFlag(1);
	}

	/**
	* (abstract) Protected helper method to write subclass entity data to NBT.
	*/
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		
		if (this.johnny)
		{
			compound.setBoolean("Johnny", true);
		}
	}

	/**
	* (abstract) Protected helper method to read subclass entity data from NBT.
	*/
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		
		if (compound.hasKey("Johnny", 99))
		{
			this.johnny = compound.getBoolean("Johnny");
		}
	}

	/**
	* Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
	* when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
	*/
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		IEntityLivingData ientitylivingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setEquipmentBasedOnDifficulty(difficulty);
		this.setEnchantmentBasedOnDifficulty(difficulty);
		return ientitylivingdata;
	}

	/**
	* Gives armor or weapon for entity based on given DifficultyInstance
	*/
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	{
		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		setSize(0.5F, 1.9F);
	}

	protected void updateAITasks()
	{
		super.updateAITasks();
		if (this.getAttackTarget() != null)
		this.setSprinting(true);
		this.setAggressive(this.johnny ? true : this.getAttackTarget() != null);
	}
	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		ItemStack heldItem = new ItemStack(stack.getItem());
		
		if (this.isOnSameTeam(player) && !stack.isEmpty() && (getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).isEmpty()) && (stack.getItem() instanceof ItemAxe))
		{
			this.playLivingSound();
			playSound(SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1.0F, 1.0F);
			player.swingArm(hand);
			if (!this.world.isRemote)
			{
				heldItem.setTagCompound(stack.getTagCompound());
				setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem);
				stack.shrink(1);
			}
			return true;
		}
		else if (this.isOnSameTeam(player) && stack.isEmpty() && player.isSneaking())
		{
			this.dropEquipmentUndamaged();
			player.swingArm(hand);
			playSound(SoundEvents.VINDICATION_ILLAGER_AMBIENT, 1.0F, 1.0F);
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	* Sets the custom name tag for this entity
	*/
	public void setCustomNameTag(String name)
	{
		super.setCustomNameTag(name);
		
		if (!this.johnny && "Johnny".equals(name))
		{
			this.ticksExisted = 0;
			this.playSound(ESound.heresJohnny, 2F, 1F);
			this.johnny = true;
			((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
			this.tasks.addTask(1, new EntityAIVindicatorBreakDoor(this));
		}
		else
		{
			this.johnny = false;
			((PathNavigateGround)this.getNavigator()).setBreakDoors(false);
			this.tasks.removeTask(new EntityAIVindicatorBreakDoor(this));
		}
	}

	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.VINDICATION_ILLAGER_AMBIENT;
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.VINDICATION_ILLAGER_DEATH;
	}

	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_VINDICATION_ILLAGER_HURT;
	}
	
	@Override
	public EnumTier getTier()
	{
		return EnumTier.TIER1;
	}
}