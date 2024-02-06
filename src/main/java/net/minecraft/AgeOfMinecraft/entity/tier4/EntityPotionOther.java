package net.minecraft.AgeOfMinecraft.entity.tier4;

import java.util.List;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases.EntityAreaEffectCloudOther;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityPotionOther extends EntityPotion
{
	public EntityPotionOther(World worldIn)
	{
		super(worldIn);
	}

	public EntityPotionOther(World worldIn, EntityLivingBase throwerIn, ItemStack potionDamageIn)
	{
		super(worldIn, throwerIn, potionDamageIn);
	}

	public EntityPotionOther(World worldIn, double x, double y, double z, ItemStack potionDamageIn)
	{
		super(worldIn, x, y, z, potionDamageIn);
	}

	/**
	* Called when this EntityThrowable hits a block or entity.
	*/
	protected void onImpact(RayTraceResult result)
	{
		if (!this.world.isRemote && this.getThrower() == null)
		{
			this.setDead();
			return;
		}
		if (!this.world.isRemote && this.getThrower() != null && this.getThrower() instanceof EntityFriendlyCreature)
		{
			ItemStack itemstack = this.getPotion();
			PotionType potiontype = PotionUtils.getPotionFromItem(itemstack);
			List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
			list.isEmpty();
			
			if (result.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				BlockPos blockpos = result.getBlockPos().offset(result.sideHit);
				this.extinguishFires(blockpos);
				
				for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
				{
					this.extinguishFires(blockpos.offset(enumfacing));
				}
			}

			this.applyWater();
			if (result.entityHit != null)
			{
				if (result.entityHit instanceof EntityLivingBase && !((EntityFriendlyCreature)this.getThrower()).isOnSameTeam((EntityLivingBase)result.entityHit))
				{
					if (!list.isEmpty())
					{
						if (this.isLingering())
						{
							this.makeAreaOfEffectCloud(itemstack, potiontype);
						}
						else
						{
							this.applySplash(result, list);
						}
					}

					int i = potiontype.hasInstantEffect() ? 2007 : 2002;
					this.world.playEvent(i, new BlockPos(this), PotionUtils.getColor(itemstack));
					this.setDead();
				}
			}
			else if (result.entityHit == null)
			{
				if (!list.isEmpty())
				{
					if (this.isLingering())
					{
						this.makeAreaOfEffectCloud(itemstack, potiontype);
					}
					else
					{
						this.applySplash(result, list);
					}
				}

				int i = potiontype.hasInstantEffect() ? 2007 : 2002;
				this.world.playEvent(i, new BlockPos(this), PotionUtils.getColor(itemstack));
				this.setDead();
			}
		}
	}

	private void applyWater()
	{
		AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
		List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, WATER_SENSITIVE);
		
		if (!list.isEmpty())
		{
			for (EntityLivingBase entitylivingbase : list)
			{
				double d0 = this.getDistanceSq(entitylivingbase);
				
				if (d0 < 16.0D && isWaterSensitiveEntity(entitylivingbase))
				{
					entitylivingbase.attackEntityFrom(DamageSource.DROWN, 1.0F);
				}
			}
		}
	}

	private void applySplash(RayTraceResult p_190543_1_, List<PotionEffect> p_190543_2_)
	{
		AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D);
		List<EntityLivingBase> list = this.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
		
		if (!list.isEmpty())
		{
			for (EntityLivingBase entitylivingbase : list)
			{
				if (entitylivingbase.canBeHitWithPotion())
				{
					double d0 = this.getDistanceSq(entitylivingbase);
					
					if (d0 < 16.0D)
					{
						Math.sqrt(d0);
						
						if (entitylivingbase == p_190543_1_.entityHit)
						{
							this.copyLocationAndAnglesFrom(entitylivingbase);
						}
						if (((EntityFriendlyCreature)this.getThrower()).isOnSameTeam(entitylivingbase))
						{
							for (PotionEffect potioneffect : p_190543_2_)
							{
								if (potioneffect.getPotion().isInstant())
								{
									potioneffect.getPotion().affectEntity(this, ((EntityFriendlyCreature)this.getThrower()), entitylivingbase, potioneffect.getAmplifier(), 0.5D);
								}
								else if (!potioneffect.getPotion().isBadEffect())
								{
									entitylivingbase.addPotionEffect(new PotionEffect(potioneffect));
								}
							}
						}
						else
						{
							for (PotionEffect potioneffect : p_190543_2_)
							{
								if (potioneffect.getPotion().isInstant())
								{
									((EntityFriendlyCreature)this.getThrower()).inflictEngenderMobDamage(entitylivingbase, " was killed by magic created by ", new EntityDamageSource("indirectMagic", ((EntityFriendlyCreature)this.getThrower())).setMagicDamage().setDamageBypassesArmor().setDamageIsAbsolute(), 8F);
								}
								else if (potioneffect.getPotion().isBadEffect() && entitylivingbase.isPotionApplicable(potioneffect))
								{
									entitylivingbase.addPotionEffect(new PotionEffect(potioneffect));
								}
								else
								{
									((EntityFriendlyCreature)this.getThrower()).inflictEngenderMobDamage(entitylivingbase, " was killed by magic created by ", new EntityDamageSource("indirectMagic", ((EntityFriendlyCreature)this.getThrower())).setMagicDamage().setDamageBypassesArmor().setDamageIsAbsolute(), 8F);
								}
							}}
							}
						}
					}
				}
			}

			private void makeAreaOfEffectCloud(ItemStack p_190542_1_, PotionType p_190542_2_)
			{
				EntityAreaEffectCloudOther entityareaeffectcloud = new EntityAreaEffectCloudOther(this.world, this.posX, this.posY, this.posZ);
				if (this.getThrower() != null && this.getThrower() instanceof EntityFriendlyCreature)
				entityareaeffectcloud.setOwner((EntityFriendlyCreature) this.getThrower());
				entityareaeffectcloud.setRadius(3.0F);
				entityareaeffectcloud.setRadiusOnUse(-0.5F);
				entityareaeffectcloud.setWaitTime(10);
				entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float)entityareaeffectcloud.getDuration());
				entityareaeffectcloud.setPotion(p_190542_2_);
				
				for (PotionEffect potioneffect : PotionUtils.getFullEffectsFromItem(p_190542_1_))
				{
					entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
				}

				NBTTagCompound nbttagcompound = p_190542_1_.getTagCompound();
				
				if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99))
				{
					entityareaeffectcloud.setColor(nbttagcompound.getInteger("CustomPotionColor"));
				}

				this.world.spawnEntity(entityareaeffectcloud);
			}

			private boolean isLingering()
			{
				return this.getPotion().getItem() == Items.LINGERING_POTION;
			}

			private void extinguishFires(BlockPos pos)
			{
				if (this.world.getBlockState(pos).getBlock() == Blocks.FIRE)
				{
					this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				}
			}

			private static boolean isWaterSensitiveEntity(EntityLivingBase p_190544_0_)
			{
				return p_190544_0_ instanceof EntityEnderman || p_190544_0_ instanceof EntityBlaze || p_190544_0_ instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityBlaze || p_190544_0_ instanceof net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman;
			}
		}