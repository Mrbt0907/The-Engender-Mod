package net.minecraft.AgeOfMinecraft.entity.tier4;
import javax.annotation.Nullable;

import net.endermanofdoom.mac.util.TranslateUtil;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityCaveSpider extends EntitySpider
{
	public EntityCaveSpider(World worldIn)
	{
		super(worldIn);
		this.setSize(0.8F, 0.475F);
		this.experienceValue = 2;
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
	}

	protected float getSoundPitch()
	{
		return super.getSoundPitch();
	}

	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityCaveSpider(this.world);
	}
	public int timesToConvert()
	{
		return 17;
	}
	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		if (super.attackEntityAsMob(p_70652_1_))
		{
			if ((p_70652_1_ instanceof EntityLivingBase))
			{
				byte b0 = 7;
				if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
				{
					b0 = 15;
				}
				else if (this.world.getDifficulty() == EnumDifficulty.HARD)
				{
					b0 = 21;
				}
				if (b0 > 0)
				{
					((EntityLivingBase)p_70652_1_).addPotionEffect(new PotionEffect(MobEffects.POISON, b0 * 20, 1));
				}
			}
			return true;
		}
		return false;
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
	public EnumTier getTier()
	{
		return EnumTier.TIER4;
	}
	public boolean isNotColliding()
	{
		if (this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(this.getEntityBoundingBox()))
		{
			BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
			
			if (this.world.canBlockSeeSky(blockpos.up()))
			{
				return false;
			}

			IBlockState iblockstate = this.world.getBlockState(blockpos.down());
			Block block = iblockstate.getBlock();
			
			if (block == Blocks.PLANKS)
			{
				return true;
			}
		}

		return false;
	}
	public float getEyeHeight()
	{
		return this.height * 0.74F;
	}
	public void updateRiderPosition()
	{
		Entity entity = getControllingPassenger();
		if (entity != null)
		{
			double d8 = -0.13D;
			Vec3d vec3 = getLook(1.0F);
			double dx = vec3.x * d8;
			double dz = vec3.z * d8;
			entity.setPosition(this.posX + dx, this.posY + getMountedYOffset() + entity.getYOffset(), this.posZ + dz);
		}
	}
	public double getMountedYOffset()
	{
		return this.height * 0.5D;
	}
	public void travel(float strafe, float vertical, float forward)
	{
		this.moveForward *= 1.5F;
		this.moveStrafing *= 1.5F;
		super.travel(strafe, vertical, forward);
	}

	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_CAVE_SPIDER;
	}
}


