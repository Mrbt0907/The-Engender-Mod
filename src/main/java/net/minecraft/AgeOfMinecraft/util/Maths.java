package net.minecraft.AgeOfMinecraft.util;

import java.util.Random;

public class Maths
{
	private static Random random = new Random();

	public static boolean chance(int chance)
	{
		return (random(0,100) <= chance) ? true : false;
	}
	
	public static boolean chance(float chance)
	{
		return (random(0.0F,1.0F) <= chance) ? true : false;
	}
	
	public static boolean chance(double chance)
	{
		return (random(0.0D,1.0D) <= chance) ? true : false;
	}
	
	public static int random(int integerA, int integerB)
	{
		if (integerA >= integerB)
			return integerA;
		else
			return random.nextInt(integerB - integerA + 1) + integerA;
	}
	
	public static float random(float floatA, float floatB)
	{
		if (floatA >= floatB)
			return floatA;
		else
			return (random.nextFloat() * (floatB - floatA + 1.0F)) + floatA;
	}
	
	public static double random(double doubleA, double doubleB)
	{
		if (doubleA >= doubleB)
			return doubleA;
		else
			return (random.nextDouble() * (doubleB - doubleA + 1.0D)) + doubleA;
	}
}
