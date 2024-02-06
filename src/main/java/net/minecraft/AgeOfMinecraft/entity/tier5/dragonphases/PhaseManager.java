package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;

public class PhaseManager
{
	private final EntityEnderDragon dragon;
	private final IPhase[] phases = new IPhase[PhaseList.getTotalPhases()];
	private IPhase phase;
	public PhaseManager(EntityEnderDragon dragonIn)
	{
		this.dragon = dragonIn;
		setPhase(PhaseList.HOVER);
	}
	public void setPhase(PhaseList<?> phaseIn)
	{
		if ((this.phase == null) || (phaseIn != this.phase.getPhaseList()))
		{
			if (this.phase != null)
			{
				this.phase.removeAreaEffect();
			}
			this.phase = getPhase(phaseIn);
			if (!this.dragon.world.isRemote)
			{
				this.dragon.getDataManager().set(EntityEnderDragon.PHASE, Integer.valueOf(phaseIn.getId()));
			}
			this.phase.initPhase();
		}
	}
	public IPhase getCurrentPhase()
	{
		return this.phase;
	}
	public <T extends IPhase> T getPhase(PhaseList<T> phaseIn)
	{
		int i = phaseIn.getId();
		if (this.phases[i] == null)
		{
			this.phases[i] = phaseIn.createPhase(this.dragon);
		}
		return (T)this.phases[i];
	}
}


