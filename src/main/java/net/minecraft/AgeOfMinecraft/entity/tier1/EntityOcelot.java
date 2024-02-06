package net.minecraft.AgeOfMinecraft.entity.tier1;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.Animal;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Light;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAICustomLeapAttack;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFollowLeader;
import net.minecraft.AgeOfMinecraft.entity.ai.EntityAIFriendlyAttackMelee;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityOcelot extends EntityFriendlyCreature implements Light, Animal
{
	public EntityOcelot(World worldIn)
	{
		super(worldIn);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIFollowLeader(this, 1.33D, 15.0F, 4.0F));
		this.tasks.addTask(3, new EntityAICustomLeapAttack(this, 0.3F, 0.6F, 0.8F, 0.5F, 3.0D, 20.0D, 6));
		this.tasks.addTask(4, new EntityAIFriendlyAttackMelee(this, 1.33D, true));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D, 80));
		this.experienceValue = 1;
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityOcelot(this.world);
	}
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		setSize(0.6F, 0.7F);
		
		List<EntityCreeper> list = this.world.getEntitiesWithinAABB(EntityCreeper.class, getEntityBoundingBox().grow(16D, 16.0D, 16D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
		if ((list != null) && (!list.isEmpty()) && this.ticksExisted % 40 == 0)
		{
			for (int i1 = 0; i1 < list.size(); i1++)
			{
				EntityCreeper entity = (EntityCreeper)list.get(i1);
				if (entity != null)
				{
					entity.tasks.addTask(0, new EntityAIAvoidEntity(entity, EntityOcelot.class, 6.0F, 1.75D, 1.25D));
				}
			}
		}
	}
	/**
	* Bonus damage vs mobs that implement Light
	*/
	public float getBonusVSLight()
	{
		return 2;
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
	
	public void updateAITasks()
	{
		super.updateAITasks();
		if (getMoveHelper().isUpdating())
		{
			double d0 = getMoveHelper().getSpeed();
			if (d0 <= 0.6D)
			{
				setSneaking(true);
				setSprinting(false);
			}
			else if (d0 >= 1.33D)
			{
				setSneaking(false);
				setSprinting(true);
			}
			else
			{
				setSneaking(false);
				setSprinting(false);
			}
		}
		else
		{
			setSneaking(false);
			setSprinting(false);
		}
	}

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
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
	}
	public boolean takesFallDamage()
	{
		return false;
	}
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.ENTITY_CAT_HURT;
	}
	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_CAT_DEATH;
	}
	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_OCELOT;
	}

	public float getBlockPathWeight(BlockPos pos)
	{
		return this.world.getBlockState(pos.down()).getBlock() == this.spawnableBlock ? 10.0F : this.world.getLightBrightness(pos) - 0.5F;
	}
	public boolean isNotColliding()
	{
		if ((this.world.checkNoEntityCollision(getEntityBoundingBox(), this)) && (this.world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty()) && (!this.world.containsAnyLiquid(getEntityBoundingBox())))
		{
			BlockPos blockpos = new BlockPos(this.posX, getEntityBoundingBox().minY, this.posZ);
			if (blockpos.getY() < this.world.getSeaLevel())
			{
				return false;
			}
			IBlockState iblockstate = this.world.getBlockState(blockpos.down());
			Block block = iblockstate.getBlock();
			if ((block == Blocks.GRASS) || (block.isLeaves(iblockstate, this.world, blockpos.down())))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public EnumTier getTier()
	{
		return EnumTier.TIER1;
	}
}


