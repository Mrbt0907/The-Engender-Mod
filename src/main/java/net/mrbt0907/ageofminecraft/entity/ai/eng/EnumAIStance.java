package net.mrbt0907.ageofminecraft.entity.ai.eng;

import net.mrbt0907.ageofminecraft.util.mrbtutil.TranslateUtil;

public enum EnumAIStance
{
	AGGRESSIVE, DEFENSIVE, STAND_GROUND, PASSIVE;
	
	public String getName()
	{
		return TranslateUtil.translateServer("stance." + name().toLowerCase());
	}
}
