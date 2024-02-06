package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PhaseDeath extends PhaseBaseFriendly
{
	private Vec3d field_188672_b;
	private int field_188673_c;
	public PhaseDeath(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public void doClientRenderEffects()
	{
		if (this.field_188673_c++ % 10 == 0)
		{
			float f = (this.dragon.getRNG().nextFloat() - 0.5F) * 8.0F;
			float f1 = (this.dragon.getRNG().nextFloat() - 0.5F) * 4.0F;
			float f2 = (this.dragon.getRNG().nextFloat() - 0.5F) * 8.0F;
			this.dragon.world.spawnParticle(net.minecraft.util.EnumParticleTypes.EXPLOSION_HUGE, this.dragon.posX + f, this.dragon.posY + 2.0D + f1, this.dragon.posZ + f2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}
	public void doLocalUpdate()
	{
		this.field_188673_c += 1;
		if (this.field_188672_b == null)
		{
			BlockPos blockpos = this.dragon.world.getHeight(net.minecraft.world.gen.feature.WorldGenEndPodium.END_PODIUM_LOCATION);
			if (this.dragon.getOwner() != null)
			blockpos = new BlockPos(this.dragon.getOwner());
			
			this.field_188672_b = new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
		}

		double d0 = this.field_188672_b.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);
		if (d0 >= 100.0D && d0 <= 25500.0D)
		{
			this.dragon.setHealth(1.0F);
		}
		else
		{
			this.dragon.setHealth(0.0F);
		}
	}
	public void initPhase()
	{
		this.field_188672_b = null;
		this.field_188673_c = 0;
	}
	public float getMaxRiseOrFall()
	{
		return 3.0F;
	}
	public Vec3d getTargetLocation()
	{
		return this.field_188672_b;
	}
	public PhaseList<PhaseDeath> getPhaseList()
	{
		return PhaseList.DYING;
	}
}


