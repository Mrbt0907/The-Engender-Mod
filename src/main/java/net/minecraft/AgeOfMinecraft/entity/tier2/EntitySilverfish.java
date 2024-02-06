package net.minecraft.AgeOfMinecraft.entity.tier2;
import java.util.Random;

import javax.annotation.Nullable;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.Tiny;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntitySilverfish
extends EntityFriendlyCreature implements Light, Tiny
{
	private AISummonSilverfish summonSilverfish;
	public EntitySilverfish(World worldIn)
	{
		super(worldIn);
		
		setSize(0.4F, 0.3F);
		this.isOffensive = true;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(0, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(1, this.summonSilverfish = new AISummonSilverfish(this));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.2D, 24.0F, 6.0F));
		this.tasks.addTask(3, new EntityAIFriendlyAttackMelee(this, 1.2D, true));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D, 80));
		this.tasks.addTask(8, new EntityAILookIdle(this));
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

	protected float getSoundPitch()
	{
		return super.getSoundPitch();
	}
	public int timesToConvert()
	{
		return 3;
	}
	public boolean isASwarmingMob()
	{
		return true;
	}
	public EnumTier getTier()
	{
		return EnumTier.TIER2;
	}
	public float getEyeHeight()
	{
		return this.height / 3;
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

	public boolean isChild()
	{
		return false;
	}
	public void setChild(boolean childZombie) { }
	
	public boolean canBeMatedWith()
	{
		return false;
	}

	public boolean canBeMarried()
	{
		return false;
	}
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}
	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_SILVERFISH_AMBIENT;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_SILVERFISH_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_SILVERFISH_DEATH;
	}
	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.1F, 1F / this.getFittness());
		
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_SILVERFISH;
	}
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (isEntityInvulnerable(source))
		{
			return false;
		}
		if ((((source instanceof EntityDamageSource)) || (source == DamageSource.MAGIC)) && (this.summonSilverfish != null))
		{
			this.summonSilverfish.func_179462_f();
		}
		return super.attackEntityFrom(source, amount);
	}
	public float getBlockPathWeight(BlockPos pos)
	{
		return this.world.getBlockState(pos.down()).getBlock() == Blocks.STONE ? 10.0F : super.getBlockPathWeight(pos);
	}
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ARTHROPOD;
	}
	public void performSpecialAttack()
	{
		setSpecialAttackTimer(400);
		playSound(ESound.bugSpecial, 10.0F, 1.0F);
		if (!this.world.isRemote)
		{
			for (int i = 0; i < 2; i++)
			{
				EntitySilverfish mob = new EntitySilverfish(this.world);
				mob.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				mob.onInitialSpawn(this.world.getDifficultyForLocation(getPosition()), null);
				this.world.spawnEntity(mob);
				mob.setOwnerId(getOwnerId());
			}
		}
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if (this.motionX * this.motionX + this.motionZ * this.motionZ != 0D)
		this.renderYawOffset = this.rotationYaw = this.rotationYawHead;
		

		setSize(0.4F, 0.3F);
		if ((getAttackTarget() != null) && (getSpecialAttackTimer() <= 0) && (isHero()))
		{
			performSpecialAttack();
		}
		if ((getSpecialAttackTimer() >= 380) && (isHero()))
		{
			this.getNavigator().clearPath();
			this.motionX = 0D;
			this.motionZ = 0D;
			float f2 = this.renderYawOffset * 0.017453292F;
			float f19 = MathHelper.sin(f2);
			float f3 = MathHelper.cos(f2);
			for (int i = 0; i < 32; i++)
			{
				this.world.spawnParticle(EnumParticleTypes.END_ROD, true, this.posX, this.posY + this.rand.nextDouble() * this.height * 2.0D, this.posZ, f3 * 0.15F, 0.01D, f19 * 0.15F, new int[0]);
				this.world.spawnParticle(EnumParticleTypes.END_ROD, true, this.posX, this.posY + this.rand.nextDouble() * this.height * 2.0D, this.posZ, f3 * -0.15F, 0.01D, f19 * -0.15F, new int[0]);
			}
		}
	}
	static class AISummonSilverfish extends EntityAIBase
	{
		private EntitySilverfish silverfish;
		private int field_179463_b;
		public AISummonSilverfish(EntitySilverfish silverfishIn)
		{
			this.silverfish = silverfishIn;
		}
		public void func_179462_f()
		{
			if (this.field_179463_b == 0)
			{
				this.field_179463_b = 20;
			}
		}
		public boolean shouldExecute()
		{
			return this.field_179463_b > 0;
		}
		public void updateTask()
		{
			this.field_179463_b -= 1;
			if (this.field_179463_b <= 0)
			{
				World world = this.silverfish.world;
				Random random = this.silverfish.getRNG();
				BlockPos blockpos = new BlockPos(this.silverfish);
				for (int i = 0; (i <= 5) && (i >= -5); i = i <= 0 ? 1 - i : 0 - i)
				{
					for (int j = 0; (j <= 10) && (j >= -10); j = j <= 0 ? 1 - j : 0 - j)
					{
						for (int k = 0; (k <= 10) && (k >= -10); k = k <= 0 ? 1 - k : 0 - k)
						{
							BlockPos blockpos1 = blockpos.add(j, i, k);
							IBlockState iblockstate = world.getBlockState(blockpos1);
							if (iblockstate.getBlock() == Blocks.MONSTER_EGG)
							{
								if (world.getGameRules().getBoolean("mobGriefing"))
								{
									world.destroyBlock(blockpos1, true);
								}
								else
								{
									world.setBlockState(blockpos1, ((BlockSilverfish.EnumType)iblockstate.getValue(BlockSilverfish.VARIANT)).getModelBlock(), 3);
								}
								if (random.nextBoolean())
								{
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}


