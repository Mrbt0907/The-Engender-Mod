package net.minecraft.AgeOfMinecraft.entity.tier5.dragonphases;
import java.lang.reflect.Constructor;
import java.util.Arrays;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;

public class PhaseList<T extends IPhase>
{
	private static PhaseList<?>[] phases = new PhaseList[0];
	public static final PhaseList<PhaseCircle> HOLDING_PATTERN = create(PhaseCircle.class, "Circle");
	public static final PhaseList<PhaseFireballAndStrafe> STRAFE_PLAYER = create(PhaseFireballAndStrafe.class, "FireballAndStrafe");
	public static final PhaseList<PhaseApproachOwner> LANDING_APPROACH = create(PhaseApproachOwner.class, "ApproachOwner");
	public static final PhaseList<PhaseHoveringOverOwner> LANDING = create(PhaseHoveringOverOwner.class, "HoverOverOwner");
	public static final PhaseList<PhaseTakeoff> TAKEOFF = create(PhaseTakeoff.class, "Takeoff");
	public static final PhaseList<PhaseBreathing> SITTING_FLAMING = create(PhaseBreathing.class, "Breathe");
	public static final PhaseList<PhaseFaceNearestEnemy> SITTING_SCANNING = create(PhaseFaceNearestEnemy.class, "Search");
	public static final PhaseList<PhasePreBreathing> SITTING_ATTACKING = create(PhasePreBreathing.class, "Roar");
	public static final PhaseList<PhaseRamAttack> CHARGING_PLAYER = create(PhaseRamAttack.class, "MeleeAttack");
	public static final PhaseList<PhaseDeath> DYING = create(PhaseDeath.class, "Dying");
	public static final PhaseList<PhaseIdleHover> HOVER = create(PhaseIdleHover.class, "Hover");
	private final Class<? extends IPhase> clazz;
	private final int id;
	private final String name;
	private PhaseList(int idIn, Class<? extends IPhase> clazzIn, String nameIn)
	{
		this.id = idIn;
		this.clazz = clazzIn;
		this.name = nameIn;
	}
	public IPhase createPhase(EntityEnderDragon dragon)
	{
		try
		{
			Constructor<? extends IPhase> constructor = getConstructor();
			return (IPhase)constructor.newInstance(new Object[] { dragon });
		}
		catch (Exception exception)
		{
			throw new Error(exception);
		}
	}
	protected Constructor<? extends IPhase> getConstructor() throws NoSuchMethodException
	{
		return this.clazz.getConstructor(new Class[] { EntityEnderDragon.class });
	}
	public int getId()
	{
		return this.id;
	}
	public String toString()
	{
		return this.name + " (#" + this.id + ")";
	}
	public static PhaseList<?> getById(int p_188738_0_)
	{
		return (p_188738_0_ >= 0) && (p_188738_0_ < phases.length) ? phases[p_188738_0_] : HOLDING_PATTERN;
	}
	public static int getTotalPhases()
	{
		return phases.length;
	}
	private static <T extends IPhase> PhaseList<T> create(Class<T> phaseIn, String nameIn)
	{
		PhaseList<T> phaselist = new PhaseList(phases.length, phaseIn, nameIn);
		phases = (PhaseList[])Arrays.copyOf(phases, phases.length + 1);
		phases[phaselist.getId()] = phaselist;
		return phaselist;
	}
}


