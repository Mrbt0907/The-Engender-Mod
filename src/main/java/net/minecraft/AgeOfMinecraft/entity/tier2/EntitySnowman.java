package net.minecraft.AgeOfMinecraft.entity.tier2;
import javax.annotation.Nullable;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.Elemental;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIAttackRangedAlly;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyHurtByTarget;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAILeaderHurtByTarget;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAILeaderHurtTarget;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityBlaze;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntitySnowballHarmful;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESetup;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntitySnowman extends EntityFriendlyCreature implements IRangedAttackMob, Light, Elemental
{
	private static final DataParameter<Byte> PUMPKIN_EQUIPPED = EntityDataManager.createKey(EntitySnowman.class, DataSerializers.BYTE);
	public EntitySnowman(World worldIn)
	{
		super(worldIn);
		setSize(0.7F, 1.9F);
		this.isOffensive = true;
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.setPathPriority(PathNodeType.LAVA, -1.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathPriority(PathNodeType.DAMAGE_FIRE, -1.0F);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.0D, 24.0F, 6.0F));
		this.tasks.addTask(3, new EntityAIAttackRangedAlly(this, 1.25D, 30, 16.0F));
		this.tasks.addTask(4, new EntityAIWander(this, 1.0D, 80));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.targetTasks.addTask(0, new EntityAIFriendlyHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(1, new EntityAILeaderHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAILeaderHurtTarget(this));
		this.experienceValue = 1;
	}
	/**
	* Bonus damage vs mobs that implement Armored
	*/
	public float getBonusVSArmored()
	{
		return 0.25F;
	}

	/**
	* Bonus damage vs mobs that implement Massive
	*/
	public float getBonusVSMassive()
	{
		return 0.25F;
	}

	public double getDefaultStaminaStat()
	{
		return 100F;
	}

	public int timesToConvert()
	{
		return 3;
	}
	public boolean canWearEasterEggs()
	{
		return false;
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntitySnowman(this.world);
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(PUMPKIN_EQUIPPED, Byte.valueOf((byte)0));
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER2;
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
		return ESetup.CONSTRUCT;
	}
	public void onLivingUpdate()
	{
		ItemStack charge = this.isPumpkinEquipped() ? new ItemStack(Blocks.PUMPKIN) : ItemStack.EMPTY;
		charge.setStackDisplayName("Pumpkin Sheared");
		basicInventory.setInventorySlotContents(7, charge);
		super.onLivingUpdate();
		
		if (this.isEntityAlive() && this.getAttackTarget() != null && this.getAttackTarget().isEntityAlive() && this.isOffensive && !this.isChild() && !this.isOnSameTeam(getAttackTarget()) && this.getDistanceSq(getAttackTarget()) < (double)((this.reachWidth * this.reachWidth) + ((this.getAttackTarget() instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)this.getAttackTarget()).reachWidth : this.getAttackTarget().width) * (this.getAttackTarget() instanceof EntityFriendlyCreature ? ((EntityFriendlyCreature)this.getAttackTarget()).reachWidth : this.getAttackTarget().width)) + 9D) && (this.ticksExisted + this.getEntityId()) % 20 == 0)
		{
			this.attackEntityAsMob(this.getAttackTarget());
			this.swingArm(EnumHand.MAIN_HAND);
			if (!this.getHeldItemOffhand().isEmpty())
			this.swingArm(EnumHand.OFF_HAND);
		}

		setSize(0.7F, 1.9F);
		if (!this.world.isRemote)
		{
			int i = MathHelper.floor(this.posX);
			int j = MathHelper.floor(this.posY);
			int k = MathHelper.floor(this.posZ);
			
			if (this.isWet() || this.world.getBiome(new BlockPos(i, 0, k)).getTemperature(new BlockPos(i, j, k)) > 1.0F)
			{
				this.attackEntityFrom((new DamageSource("melt")).setFireDamage().setDamageBypassesArmor(), 1.0F);
			}

			if (!this.world.getGameRules().getBoolean("mobGriefing"))
			{
				return;
			}

			for (int l = 0; l < 4; ++l)
			{
				i = MathHelper.floor(this.posX + (double)((float)(l % 2 * 2 - 1) * 0.25F));
				j = MathHelper.floor(this.posY);
				k = MathHelper.floor(this.posZ + (double)((float)(l / 2 % 2 * 2 - 1) * 0.25F));
				BlockPos blockpos = new BlockPos(i, j, k);
				
				if (this.world.getBlockState(blockpos).getMaterial() == Material.AIR && this.world.getBiome(new BlockPos(i, 0, k)).getTemperature(blockpos) < 0.8F && Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, blockpos))
				{
					this.world.setBlockState(blockpos, Blocks.SNOW_LAYER.getDefaultState());
				}
			}
		}
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_SNOWMAN;
	}
	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (this.isChild())
		{
			if (this.hasOwner(player))
			{
				player.swingArm(EnumHand.MAIN_HAND);
				if (this.getRidingEntity() == null)
				{
					this.startRiding(player, true);
				}
				else
				{
					this.dismountRidingEntity();
				}
			}
			return true;
		}
		else if (!stack.isEmpty() && (stack.getItem() == Items.SHEARS))
		{
			if (this.hasOwner(player) && (!isPumpkinEquipped()) && (!this.world.isRemote))
			{
				this.world.playEvent(2001, this.getPosition().up(), Block.getStateId(Blocks.PUMPKIN.getDefaultState()));
				setPumpkinEquipped(true);
				stack.damageItem(1, player);
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean isPumpkinEquipped()
	{
		return (((Byte)this.dataManager.get(PUMPKIN_EQUIPPED)).byteValue() & 0x10) != 0;
	}
	public void setPumpkinEquipped(boolean p_184747_1_)
	{
		byte b0 = ((Byte)this.dataManager.get(PUMPKIN_EQUIPPED)).byteValue();
		if (p_184747_1_)
		{
			this.dataManager.set(PUMPKIN_EQUIPPED, Byte.valueOf((byte)(b0 | 0x10)));
		}
		else
		{
			this.dataManager.set(PUMPKIN_EQUIPPED, Byte.valueOf((byte)(b0 & 0xFFFFFFEF)));
		}
	}
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_)
	{
		EntitySnowballHarmful entitysnowball = new EntitySnowballHarmful(this.world, this);
		double d0 = target.posY + target.getEyeHeight() - 1.15D;
		double d1 = target.posX - this.posX;
		double d2 = d0 - entitysnowball.posY;
		double d3 = target.posZ - this.posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
		entitysnowball.shoot(d1, d2 + f, d3, 1.6F, (this.isPumpkinEquipped() ? 45F : 1.0F));
		this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(entitysnowball);
		this.swingArm(EnumHand.MAIN_HAND);
		entitysnowball.damage = (target instanceof EntityBlaze || target instanceof net.minecraft.entity.monster.EntityBlaze) ? 3F : ((rand.nextInt(3) == 0 || !(target instanceof EntityFriendlyCreature)) ? 1F : 0F);
	}
	public boolean takesFallDamage()
	{
		return false;
	}
	protected float getSoundPitch()
	{
		return super.getSoundPitch();
	}

	public float getEyeHeight()
	{
		return this.height * 0.89F;
	}
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_SNOWMAN_AMBIENT;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_SNOWMAN_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_SNOWMAN_DEATH;
	}
	@Override
	public void setSwingingArms(boolean swingingArms) {}
}


