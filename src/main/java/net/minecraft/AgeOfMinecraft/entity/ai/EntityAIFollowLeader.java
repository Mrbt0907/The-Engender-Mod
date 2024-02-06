package net.minecraft.AgeOfMinecraft.entity.ai;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.Flying;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySlime;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityVex;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityCommandBlockWither;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStorm;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAIFollowLeader
extends EntityAIBase
{
	private EntityFriendlyCreature theCreature;
	private EntityLivingBase theOwner;
	World theWorld;
	private double followSpeed;
	private PathNavigate petPathfinder;
	private int field_75343_h;
	float maxDist;
	float minDist;
	private float field_75344_i;
	public EntityAIFollowLeader(EntityFriendlyCreature p_i1625_1_, double p_i1625_2_, float p_i1625_4_, float p_i1625_5_)
	{
		this.theCreature = p_i1625_1_;
		this.theWorld = p_i1625_1_.world;
		this.followSpeed = p_i1625_2_;
		this.petPathfinder = p_i1625_1_.getNavigator();
		this.minDist = p_i1625_4_;
		this.maxDist = p_i1625_5_;
		setMutexBits(0);
	}

	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.theCreature.getOwner();
		if (entitylivingbase == null)
		{
			return false;
		}
		else if (!this.theCreature.canFollowOwner())
		{
			return false;
		}
		else if (this.theCreature.world.provider != entitylivingbase.world.provider)
		{
			return false;
		}
		else
		{
			this.theOwner = entitylivingbase;
			double d0 = this.theCreature.getDistance(entitylivingbase);
			return d0 >= getMinDis() || this.theOwner.isSneaking();
		}
	}

	public boolean shouldContinueExecuting()
	{
		return this.theCreature.canFollowOwner() && (this.theCreature.getDistance(this.theOwner) >= getMaxDis()) || (this.theOwner.isSneaking());
	}

	public void startExecuting()
	{
		this.field_75343_h = 0;
		this.field_75344_i = this.theCreature.getPathPriority(PathNodeType.WATER);
		this.theCreature.setPathPriority(PathNodeType.WATER, 0.0F);
		this.theCreature.setSitResting(false);
	}

	public void resetTask()
	{
		this.theOwner = null;
		this.petPathfinder.clearPath();
		this.petPathfinder.tryMoveToEntityLiving(this.theCreature, 0D);
		this.theCreature.setPathPriority(PathNodeType.WATER, this.field_75344_i);
	}
	private boolean func_181065_a(BlockPos p_181065_1_)
	{
		IBlockState iblockstate = this.theWorld.getBlockState(p_181065_1_);
		Block block = iblockstate.getBlock();
		return block == Blocks.AIR;
	}
	private double getMaxDis()
	{
		return this.maxDist;
	}

	private double getMinDis()
	{
		return this.minDist;
	}
	public void updateTask()
	{
		Vec3d vec3 = this.theOwner.getLook(1.0F);
		
		if (this.theCreature instanceof EntityWither)
		{
			((EntityWither)this.theCreature).updateWatchedTargetId(0, this.theOwner.getEntityId());
			if ((this.theCreature.posY < theOwner.posY) || ((!((EntityWither)this.theCreature).isArmored()) && (theCreature.posY < theOwner.posY + 5.0D + theOwner.getEyeHeight())))
			this.theCreature.motionY += (0.5D - this.theCreature.motionY);
			theCreature.faceEntity(theOwner, 180F, 40F);
			double d0 = theOwner.posX - theCreature.posX;
			double d1 = theOwner.posZ - theCreature.posZ;
			double d3 = d0 * d0 + d1 * d1;
			if (d3 > 9.0D)
			{
				double d5 = MathHelper.sqrt(d3);
				if (theCreature.moralRaisedTimer > 200)
				{
					theCreature.motionX += (d0 / d5 * 0.75D - theCreature.motionX);
					theCreature.motionZ += (d1 / d5 * 0.75D - theCreature.motionZ);
				}
				else
				{
					theCreature.motionX += (d0 / d5 * 0.5D - theCreature.motionX);
					theCreature.motionZ += (d1 / d5 * 0.5D - theCreature.motionZ);
				}
			}
		}

		if (this.theCreature instanceof EntityCommandBlockWither)
		{
			if ((this.theCreature.posY < theOwner.posY) || ((!((EntityCommandBlockWither)this.theCreature).isArmored()) && (theCreature.posY < theOwner.posY + 5.0D + theOwner.getEyeHeight())))
			this.theCreature.motionY += (0.5D - this.theCreature.motionY);
			theCreature.faceEntity(theOwner, 180F, 40F);
			double d0 = theOwner.posX - theCreature.posX;
			double d1 = theOwner.posZ - theCreature.posZ;
			double d3 = d0 * d0 + d1 * d1;
			if (d3 > 9.0D)
			{
				double d5 = MathHelper.sqrt(d3);
				if (theCreature.moralRaisedTimer > 200)
				{
					theCreature.motionX += (d0 / d5 * 0.75D - theCreature.motionX);
					theCreature.motionZ += (d1 / d5 * 0.75D - theCreature.motionZ);
				}
				else
				{
					theCreature.motionX += (d0 / d5 * 0.5D - theCreature.motionX);
					theCreature.motionZ += (d1 / d5 * 0.5D - theCreature.motionZ);
				}
			}
		}

		double d1 = 1D + this.theCreature.width + this.theCreature.height;
		
		if (this.theCreature instanceof EntityWitherStorm)
		this.theCreature.getMoveHelper().setMoveTo(this.theOwner.lastTickPosX - vec3.x * d1, this.theOwner.lastTickPosY - this.theCreature.height - 16D, this.theOwner.lastTickPosZ - vec3.z * d1, this.followSpeed);
		
		if (this.theCreature instanceof EntityGuardian)
		{
			if (((EntityGuardian)this.theCreature).isInWater())
			{
				this.theCreature.getMoveHelper().setMoveTo(this.theOwner.lastTickPosX - vec3.x * d1, this.theOwner.lastTickPosY, this.theOwner.lastTickPosZ - vec3.z * d1, this.followSpeed);
			}
			else
			{
				double d01 = this.theOwner.posX - ((EntityGuardian)this.theCreature).posX;
				double d11 = this.theOwner.posZ - ((EntityGuardian)this.theCreature).posZ;
				float f2 = MathHelper.sqrt(d01 * d01 + d11 * d11);
				((EntityGuardian)this.theCreature).motionX = (d01 / f2 * 0.5D * 0.5D + ((EntityGuardian)this.theCreature).motionX);
				((EntityGuardian)this.theCreature).motionZ = (d11 / f2 * 0.5D * 0.5D + ((EntityGuardian)this.theCreature).motionZ);
				((EntityGuardian)this.theCreature).faceEntity(this.theOwner, 180.0F, 30.0F);
				 if ((((EntityGuardian)this.theCreature).onGround))
				{
					 ((EntityGuardian)this.theCreature).motionY += 0.6D;
					 ((EntityGuardian)this.theCreature).playSound(SoundEvents.ENTITY_GUARDIAN_FLOP, 1.0F, 1.0F);
					 }
				}
			}

			if (!this.theCreature.isSneaking())
			{
				this.theCreature.getLookHelper().setLookPositionWithEntity(this.theOwner, 180.0F, this.theCreature.getVerticalFaceSpeed());
				this.theCreature.setAttackTarget(null);
			}
			if ((--this.field_75343_h <= 0) || (this.theCreature.isSneaking()))
			{
				this.field_75343_h = 20;
				if (this.theCreature.isSneaking())
				{
					this.theCreature.getLookHelper().setLookPosition(this.theOwner.posX + vec3.x * 16.0D, this.theOwner.posY + this.theOwner.getEyeHeight() + vec3.y * 4.0D, this.theOwner.posZ + vec3.z * 16.0D, 180.0F, 180.0F);
				}
				if (this.theCreature instanceof EntityVex)
				this.theCreature.getMoveHelper().setMoveTo(this.theOwner.lastTickPosX - vec3.x * d1, this.theOwner.lastTickPosY, this.theOwner.lastTickPosZ - vec3.z * d1, this.followSpeed);
				
				if (this.theCreature instanceof EntitySlime)
				{
					this.theCreature.faceEntity(this.theOwner, 10.0F, 10.0F);
					((EntitySlime.SlimeMoveHelper)this.theCreature.getMoveHelper()).setDirection(this.theCreature.rotationYawHead, true);
				}

				if (this.theCreature.getGuardBlock() == null && (!this.petPathfinder.tryMoveToXYZ(this.theOwner.lastTickPosX - vec3.x * d1, this.theOwner.lastTickPosY, this.theOwner.lastTickPosZ - vec3.z * d1, this.followSpeed) || this.theCreature.isInWater() || this.theCreature.isInLava()))
				{
					if ((this.theCreature.getDistance(this.theOwner) >= this.getMinDis() + 4D) && (!this.theCreature.getLeashed()) && (this.theCreature.getGuardBlock() == null))
					{
						int i = MathHelper.floor(this.theOwner.posX);
						int j = MathHelper.floor(this.theOwner.posZ);
						int k = MathHelper.floor(this.theOwner.world.getTopSolidOrLiquidBlock(this.theOwner.getPosition()).getY());
						for (int l = 0; l <= 4; l++)
						{
							for (int i1 = 0; i1 <= 4; i1++)
							{
								if (((l < 1) || (i1 < 1) || (l > 3) || (i1 > 3)) && (this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).getBlock() != Blocks.AIR || this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).getBlock() == Blocks.WATER || this.theCreature instanceof Flying || this.theCreature.getOwner().isRiding()) && (this.theWorld.getBlockState(new BlockPos(i + l, k - 1, j + i1)).getBlock() != Blocks.LAVA) && (this.theWorld.getBlockState(new BlockPos(i + l, k + this.theCreature.getEntityBoundingBox().maxY, j + i1)).getBlock() == Blocks.AIR) && (func_181065_a(new BlockPos(i + l, k, j + i1))) && (func_181065_a(new BlockPos(i + l, k + 1, j + i1))))
								{
									this.theCreature.fallDistance *= 0F;
									if (this.theCreature.isRiding())
									{
										this.theCreature.getRidingEntity().setLocationAndAngles(i + l + 0.5F, k, j + i1 + 0.5F, this.theCreature.rotationYaw, this.theCreature.rotationPitch);
										if (this.theCreature.getRidingEntity().isRiding())
										{
											this.theCreature.getRidingEntity().dismountRidingEntity();
										}
									}
									this.theCreature.setLocationAndAngles(i + l + 0.5F, k, j + i1 + 0.5F, this.theCreature.rotationYaw, this.theCreature.rotationPitch);
									this.petPathfinder.clearPath();
									return;
								}
							}
						}
					}
				}
			}
		}
	}