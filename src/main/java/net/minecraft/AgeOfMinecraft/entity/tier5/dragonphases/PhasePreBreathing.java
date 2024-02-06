package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.util.math.BlockPos;

public class PhasePreBreathing extends PhaseBaseFriendly
{
	private int field_188662_b;
	public PhasePreBreathing(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public boolean getIsStationary()
	{
		return true;
	}
	public void doClientRenderEffects()
	{
		this.dragon.world.playSound(this.dragon.posX, this.dragon.posY, this.dragon.posZ, net.minecraft.init.SoundEvents.ENTITY_ENDERDRAGON_GROWL, this.dragon.getSoundCategory(), 2.5F, 0.8F + this.dragon.getRNG().nextFloat() * 0.3F, false);
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
		if (this.field_188662_b++ >= 40)
		{
			this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_FLAMING);
		}
		if (this.dragon.getAttackTarget() != null)
		{
			this.dragon.faceEntity(this.dragon.getAttackTarget(), 10F, 20F);
		}
	}
	public void initPhase()
	{
		this.field_188662_b = 0;
	}
	public PhaseList<PhasePreBreathing> getPhaseList()
	{
		return PhaseList.SITTING_ATTACKING;
	}
}


