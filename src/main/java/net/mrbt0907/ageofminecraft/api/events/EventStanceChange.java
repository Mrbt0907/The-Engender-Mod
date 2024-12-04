package net.mrbt0907.ageofminecraft.api.events;

import javax.annotation.Nonnull;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;

public class EventStanceChange extends Event
{
	public final EntityEngendered entity;
	protected final EnumAIStance oldStance;
	protected EnumAIStance stance;
	
	public EventStanceChange(EntityEngendered entity, EnumAIStance oldStance, EnumAIStance stance)
	{
		this.entity = entity;
		this.oldStance = oldStance;
		this.stance = stance;
	}
	
	public void setStance(int stanceID)
	{
		EnumAIStance[] stances = EnumAIStance.values();
		stance = stances[MathHelper.clamp(stanceID, 0, stances.length - 1)];
	}
	
	public void setStance(@Nonnull EnumAIStance stance)
	{
		this.stance = stance;
	}
	
	public EnumAIStance getStance()
	{
		return stance;
	}
	
	public EnumAIStance getOriginalStance()
	{
		return oldStance;
	}
}