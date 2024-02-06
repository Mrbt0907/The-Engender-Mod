package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;

public class PhaseHoveringOverOwner extends PhaseBaseFriendly
{
	private Vec3d field_188685_b;
	public PhaseHoveringOverOwner(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public boolean getIsStationary()
	{
		return true;
	}
	public float getAdjustedDamage(MultiPartEntityPart pt, DamageSource src, float damage)
	{
		if (src.getTrueSource() instanceof EntityArrow)
		{
			src.getTrueSource().setFire(1);
			return 0.0F;
		}
		else
		{
			return super.getAdjustedDamage(pt, src, damage);
		}
	}
	public void doClientRenderEffects()
	{
		Vec3d vec3d = this.dragon.getHeadLookVec(1.0F).normalize();
		vec3d.rotateYaw(-0.7853982F);
		double d0 = this.dragon.dragonPartHead.posX;
		double d1 = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0F;
		double d2 = this.dragon.dragonPartHead.posZ;
		for (int i = 0; i < 8; i++)
		{
			double d3 = d0 + this.dragon.getRNG().nextGaussian() / 2.0D;
			double d4 = d1 + this.dragon.getRNG().nextGaussian() / 2.0D;
			double d5 = d2 + this.dragon.getRNG().nextGaussian() / 2.0D;
			this.dragon.world.spawnParticle(net.minecraft.util.EnumParticleTypes.DRAGON_BREATH, d3, d4, d5, -vec3d.x * 0.07999999821186066D + this.dragon.motionX, -vec3d.y * 0.30000001192092896D + this.dragon.motionY, -vec3d.z * 0.07999999821186066D + this.dragon.motionZ, new int[0]);
			vec3d.rotateYaw(0.19634955F);
		}
	}
	public void doLocalUpdate()
	{
		if (this.field_188685_b == null)
		{
			if (this.dragon.getOwner() != null)
			{
				this.field_188685_b = new Vec3d(new net.minecraft.util.math.BlockPos(this.dragon.getOwner().posX, this.dragon.getOwner().posY + 4.0D, this.dragon.getOwner().posZ));
			} else {
					this.field_188685_b = new Vec3d(this.dragon.world.getTopSolidOrLiquidBlock(net.minecraft.world.gen.feature.WorldGenEndPodium.END_PODIUM_LOCATION));
				}
			}
			if (this.field_188685_b.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ) < 9.0D)
			{
				((PhaseBreathing)this.dragon.getPhaseManager().getPhase(PhaseList.SITTING_FLAMING)).func_188663_j();
				this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
			}
		}
		public float getMaxRiseOrFall()
		{
			return 1.5F;
		}
		public float getYawFactor()
		{
			float f = net.minecraft.util.math.MathHelper.sqrt(this.dragon.motionX * this.dragon.motionX + this.dragon.motionZ * this.dragon.motionZ) + 1.0F;
			float f1 = Math.min(f, 40.0F);
			return f1 / f;
		}
		public void initPhase()
		{
			this.field_188685_b = null;
		}
		public Vec3d getTargetLocation()
		{
			return this.field_188685_b;
		}
		public PhaseList<PhaseHoveringOverOwner> getPhaseList()
		{
			return PhaseList.LANDING;
		}
	}

	
	