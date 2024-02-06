package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PhaseFaceNearestEnemy extends PhaseBaseFriendly
{
	private int field_188667_b;
	public PhaseFaceNearestEnemy(EntityEnderDragon dragonIn)
	{
		super(dragonIn);
	}
	public boolean getIsStationary()
	{
		return true;
	}
	public void doLocalUpdate()
	{
		BlockPos pos = this.dragon.world.getTopSolidOrLiquidBlock(net.minecraft.world.gen.feature.WorldGenEndPodium.END_PODIUM_LOCATION);
		EntityLivingBase owner = this.dragon.getOwner();
		if (this.dragon.getJukeboxToDanceTo() != null)
			this.dragon.setPositionAndUpdate(this.dragon.getJukeboxToDanceTo().getX(), this.dragon.getJukeboxToDanceTo().getY() + 12D, this.dragon.getJukeboxToDanceTo().getZ());
		else if (owner != null)
			this.dragon.setPositionAndUpdate(owner.posX, owner.posY + 4.0D, owner.posZ);
		else
			this.dragon.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
		
		if (this.dragon.getJukeboxToDanceTo() == null)
		this.field_188667_b += 1;
		EntityLivingBase entitylivingbase = this.dragon.getAttackTarget();
		if ((entitylivingbase != null) && (this.dragon.getDistanceSq(entitylivingbase) < 1024.0D || (!this.dragon.isWild() && !this.dragon.getOwner().getHeldItemMainhand().isEmpty() && this.dragon.getOwner().getHeldItemMainhand().getItem() == Items.GLASS_BOTTLE)))
		{
			if (this.field_188667_b > 30)
			{
				this.dragon.getPhaseManager().setPhase(PhaseList.SITTING_ATTACKING);
			}
			else
			{
				this.dragon.faceEntity(entitylivingbase, 10F, 90F);
				this.dragon.renderYawOffset = this.dragon.rotationYaw = this.dragon.rotationYawHead + 180F;
			}
		}
		else if (this.field_188667_b >= 200)
		{
			entitylivingbase = this.dragon.getAttackTarget();
			this.dragon.getPhaseManager().setPhase(PhaseList.TAKEOFF);
			if ((entitylivingbase != null) && (this.dragon.getDistanceSq(entitylivingbase) > 1024.0D))
			{
				this.dragon.getPhaseManager().setPhase(PhaseList.CHARGING_PLAYER);
				((PhaseRamAttack)this.dragon.getPhaseManager().getPhase(PhaseList.CHARGING_PLAYER)).func_188668_a(new Vec3d(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ));
			}
		}
		else if (owner != null && !this.dragon.isWild())
		{
			this.dragon.rotationYaw = (owner.rotationYaw - 180.0F);
		}
	}
	public void initPhase()
	{
		this.field_188667_b = 0;
	}
	public PhaseList<PhaseFaceNearestEnemy> getPhaseList()
	{
		return PhaseList.SITTING_SCANNING;
	}
}


