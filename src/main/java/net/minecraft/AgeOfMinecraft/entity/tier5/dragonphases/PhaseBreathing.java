package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PhaseBreathing extends PhaseBaseFriendly
{
	private int field_188664_b;
	private int field_188665_c;
	private EntityAreaEffectCloudOther areaEffectCloud;
	public PhaseBreathing(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public boolean getIsStationary()
	{
		return true;
	}
	public void doClientRenderEffects()
	{
		this.field_188664_b += 1;
		if (this.field_188664_b < 30)
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
				for (int j = 0; j < 4; j++)
				{
					this.dragon.world.spawnParticle(net.minecraft.util.EnumParticleTypes.DRAGON_BREATH, d3, d4, d5, -vec3d.x * 0.1D * j, -vec3d.y * 0.75D, -vec3d.z * 0.1D * j, new int[0]);
				}
				vec3d.rotateYaw(0.19634955F);
			}
		}
	}
	public void doLocalUpdate()
	{
		BlockPos pos = this.dragon.world.getTopSolidOrLiquidBlock(net.minecraft.world.gen.feature.WorldGenEndPodium.END_PODIUM_LOCATION);
		
		if (this.dragon.getJukeboxToDanceTo() != null)
		this.dragon.setPositionAndUpdate(this.dragon.getJukeboxToDanceTo().getX(), this.dragon.getJukeboxToDanceTo().getY() + 16.0D, this.dragon.getJukeboxToDanceTo().getZ());
		else if (!this.dragon.isWild())
		this.dragon.setPositionAndUpdate(this.dragon.getOwner().posX, this.dragon.getOwner().posY + 4.0D, this.dragon.getOwner().posZ);
		else
		this.dragon.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		this.field_188664_b += 1;
		
		if (this.field_188664_b < 30)
		this.dragon.playSound(SoundEvents.ENTITY_ENDERDRAGON_SHOOT, 5F, 0.5F);
		if (this.field_188664_b >= 200)
		{
			if (this.field_188665_c >= 4)
			{
				this.dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
			}
			else
			{
				this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
			}
		}
		else if (this.field_188664_b == 20)
		{
			Vec3d vec3d = new Vec3d(this.dragon.dragonPartHead.posX - this.dragon.posX, 0.0D, this.dragon.dragonPartHead.posZ - this.dragon.posZ).normalize();
			float f = 5.0F;
			double d0 = this.dragon.dragonPartHead.posX + vec3d.x * f / 2.0D;
			double d1 = this.dragon.dragonPartHead.posZ + vec3d.z * f / 2.0D;
			double d2 = this.dragon.dragonPartHead.posY + this.dragon.dragonPartHead.height / 2.0F;
			net.minecraft.util.math.BlockPos.MutableBlockPos blockpos$mutableblockpos = new net.minecraft.util.math.BlockPos.MutableBlockPos(MathHelper.floor(d0), MathHelper.floor(d2), MathHelper.floor(d1));
			while (this.dragon.world.isAirBlock(blockpos$mutableblockpos))
			{
				d2 -= 1.0D;
				blockpos$mutableblockpos.setPos(MathHelper.floor(d0), MathHelper.floor(d2), MathHelper.floor(d1));
			}
			d2 = MathHelper.floor(d2) + 1;
			this.areaEffectCloud = new EntityAreaEffectCloudOther(this.dragon.world, d0, d2, d1);
			this.areaEffectCloud.setOwner(this.dragon);
			this.areaEffectCloud.setRadius(1F);
			this.areaEffectCloud.setDuration(80);
			this.areaEffectCloud.setRadiusPerTick((f) / areaEffectCloud.getDuration());
			this.areaEffectCloud.setParticle(net.minecraft.util.EnumParticleTypes.DRAGON_BREATH);
			this.areaEffectCloud.addEffect(new net.minecraft.potion.PotionEffect(net.minecraft.init.MobEffects.INSTANT_DAMAGE));
			this.dragon.world.spawnEntity(this.areaEffectCloud);
		}
	}
	public void initPhase()
	{
		this.field_188664_b = 0;
		this.field_188665_c += 1;
	}
	public void removeAreaEffect()
	{
		if (this.areaEffectCloud != null)
		{
			this.areaEffectCloud.setDead();
			this.areaEffectCloud = null;
		}
	}
	public PhaseList<PhaseBreathing> getPhaseList()
	{
		return PhaseList.SITTING_FLAMING;
	}
	public void func_188663_j()
	{
		this.field_188665_c = 0;
	}
}


