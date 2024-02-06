package net.minecraft.AgeOfMinecraft.entity.tier3;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTippedArrowOther extends EntityTippedArrow
{
	EntityLivingBase shooter;
	public EntityTippedArrowOther(World worldIn)
	{
		super(worldIn);
	}
	public EntityTippedArrowOther(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	public EntityTippedArrowOther(World worldIn, EntityLivingBase shooter)
	{
		super(worldIn, shooter);
		this.shooter = shooter;
	}

	protected void onHit(RayTraceResult p_184549_1_)
	{
		Entity entity = p_184549_1_.entityHit;
		if (!this.world.isRemote && (this.shooter != null) && (entity != null) && ((entity instanceof EntityLivingBase)) && (!this.shooter.isOnSameTeam(entity)) && (entity != ((EntityFriendlyCreature)this.shooter).getOwner()))
		{
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			int i = MathHelper.ceil((double)f * this.getDamage());
			
			if (this.getIsCritical())
			{
				i += this.rand.nextInt(i / 2 + 2);
			}
			DamageSource damagesource;
			
			if (this.shootingEntity == null)
			{
				damagesource = DamageSource.causeArrowDamage(this, this);
			}
			else
			{
				damagesource = DamageSource.causeArrowDamage(this, this.shooter);
			}

			if (entity instanceof EntityLivingBase && this.shooter != null && entity.isEntityAlive() && this.shooter instanceof EntityFriendlyCreature && (!((EntityFriendlyCreature)shooter).isOnSameTeam((EntityLivingBase)entity) || (entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)shooter).isOnSameTeam((EntityFriendlyCreature)entity) && ((EntityFriendlyCreature)shooter).getFakeHealth() > 0F)))
			{
				((EntityFriendlyCreature)shooter).inflictEngenderMobDamage((EntityLivingBase)entity, " was shot by ", damagesource, i);
				this.arrowHit((EntityLivingBase) entity);
				
				if (shooter instanceof EntitySkeleton && (entity instanceof net.minecraft.entity.monster.EntityCreeper || entity instanceof EntityCreeper) && !entity.isEntityAlive())
				{
					int i1 = Item.getIdFromItem(Items.RECORD_13);
					int j = Item.getIdFromItem(Items.RECORD_WAIT);
					int k = i1 + this.rand.nextInt(j - i1 + 1);
					entity.dropItem(Item.getItemById(k), 1);
				}
				if (((EntityFriendlyCreature)shooter).isHero() && getDamage() > 0)
				this.setDamage(this.getDamage() - 0.5D);
				else
				this.setDead();
			}
		}

		else if (!this.world.isRemote && entity == null && this.isEntityInsideOpaqueBlock())
		{
			this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			setDead();
		}
	}

	public void onUpdate()
	{
		super.onUpdate();
		
		if (this.isEntityInsideOpaqueBlock())
		{
			for (int u = 0; u < 3 + getDamage(); ++u)
			{
				int i = MathHelper.floor(this.posX);
				int j = MathHelper.floor(this.posY - 0.20000000298023224D);
				int k = MathHelper.floor(this.posZ);
				IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));
				if (iblockstate.getMaterial() != Material.AIR)
				{
					this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX, getEntityBoundingBox().minY + 0.3D, this.posZ, -this.motionX, 2D, -this.motionZ, new int[] { Block.getStateId(iblockstate) });
				}
			}
		}
		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().grow(2D), Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING }));
		if (!list.isEmpty())
		for (EntityLivingBase entity1 : list)
		if (this.shooter != null && entity1 instanceof IEntityMultiPart && entity1 != null && entity1.isEntityAlive() && !((EntityFriendlyCreature)this.shooter).isOnSameTeam(entity1))this.onHit(new RayTraceResult(entity1));
	}
}