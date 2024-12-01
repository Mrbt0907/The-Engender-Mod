package net.mrbt0907.ageofminecraft.util.mrbtutil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StringUtil
{
	public static String repeat(String original, String input, int amount)
	{
		for (int i = 0; i < amount; i++)
			original = original.concat(input);
		return original;
	}
	
	public static String[] split(String original, String delimiter)
	{
		List<String> output = new LinkedList<String>();
		int size = original.length();
		int sizeDelimiter = delimiter.length();
		
		String input = "";
		char character;
		char characterDelimiter = delimiter.charAt(0);
		for (int i = 0, ii = 0; i < size; i++)
		{
			character = original.charAt(i);
			
			if (character == characterDelimiter)
			{
				ii++;
				if (ii < sizeDelimiter)
				{
					characterDelimiter = delimiter.charAt(ii);
				}
				else
				{
					ii = 0;
					characterDelimiter = delimiter.charAt(ii);
					if (!input.isEmpty())
						output.add(input);
					input = "";
				}
			}
			else
			{
				if (ii > 0)
				{
					ii = 0;
					characterDelimiter = delimiter.charAt(ii);
				}

				input += character;
			}

			if (i + 1 >= size && !input.isEmpty())
				output.add(input);
		}
		
		return output.toArray(new String[output.size()]);
	}
	
	public static Character[] getUniqueCharacters(String original)
	{
		List<Character> characters = new LinkedList<Character>();
		Character character;
		for (int i = 0; i < original.length(); i++)
		{
			character = original.charAt(i);
			if (!character.equals(' ') && !characters.contains(character))
				characters.add(character);
		}
			
		return characters.toArray(new Character[characters.size()]);
	}
	
	public static String parseDouble(double value)
	{
		String input;
		if (value >= 1000000000000000000000000000000000000.0D)
		input = "Infinity (" + (int)Math.floor((value / Double.MAX_VALUE) * 100) + "%)";
		else if (value >= 1000000000000000000000000000000000.0D)
		input = Math.floor(value * 0.00000000000000000000000000000001D) * 0.1D + " Decillion";
		else if (value >= 1000000000000000000000000000000.0D)
		input = Math.floor(value * 0.00000000000000000000000000001D) / 10 + " Nonillion";
		else if (value >= 1000000000000000000000000000.0D)
		input = Math.floor(value * 0.00000000000000000000000001D) / 10 + " Octillion";
		else if (value >= 1000000000000000000000000.0D)
		input = Math.floor(value * 0.00000000000000000000001D) / 10 + " Septillion";
		else if (value >= 1000000000000000000000.0D)
		input = Math.floor(value * 0.00000000000000000001D) / 10D + " Sextillion";
		else if (value >= 1000000000000000000.0D)
		input = Math.floor(value * 0.00000000000000001D) / 10 + " Quintillion";
		else if (value >= 1000000000000000.0D)
		input = Math.floor(value * 0.00000000000001D) / 10 + " Quadrillion";
		else if (value >= 1000000000000.0D)
		input = Math.floor(value * 0.00000000001D) / 10 + " Trillion";
		else if (value >= 1000000000.0D)
		input = Math.floor(value * 0.00000001D) / 10 + " Billion";
		else if (value >= 1000000.0D)
		input =  Math.floor(value * 0.00001D) / 10 + " Million";
		else
		input = (int)Math.floor(value) + "";
		return input;
	}
	
	public static String parseFloat(float value)
	{
		String input;
		if (value >= 1000000000000000000000000000000000000.0F)
		input = "Infinity (" + (int)Math.floor((value / Float.MAX_VALUE) * 100) + "%)";
		else if (value >= 1000000000000000000000000000000000.0F)
		input = Math.floor(value * 0.00000000000000000000000000000001F) * 0.1F + " Decillion";
		else if (value >= 1000000000000000000000000000000.0F)
		input = Math.floor(value * 0.00000000000000000000000000001F) / 10 + " Nonillion";
		else if (value >= 1000000000000000000000000000.0F)
		input = Math.floor(value * 0.00000000000000000000000001F) / 10 + " Octillion";
		else if (value >= 1000000000000000000000000.0F)
		input = Math.floor(value * 0.00000000000000000000001F) / 10 + " Septillion";
		else if (value >= 1000000000000000000000.0F)
		input = Math.floor(value * 0.00000000000000000001F) / 10F + " Sextillion";
		else if (value >= 1000000000000000000.0F)
		input = Math.floor(value * 0.00000000000000001F) / 10 + " Quintillion";
		else if (value >= 1000000000000000.0F)
		input = Math.floor(value * 0.00000000000001F) / 10 + " Quadrillion";
		else if (value >= 1000000000000.0F)
		input = Math.floor(value * 0.00000000001F) / 10 + " Trillion";
		else if (value >= 1000000000.0F)
		input = Math.floor(value * 0.00000001F) / 10 + " Billion";
		else if (value >= 1000000.0F)
		input =  Math.floor(value * 0.00001F) / 10 + " Million";
		else
		input = (int)Math.floor(value) + "";
		return input;
	}
	
	@SideOnly(Side.CLIENT)
	public static List<String> wordwrap(net.minecraft.client.gui.FontRenderer fontRenderer, String str, int width)
	{
		int length = str.length();
		List<String> strings = new ArrayList<String>();
		String string = "",  word = "";
		char character;
		
		for (int i = 0 ; i < length; i++)
		{
			character = str.charAt(i);
			word += character;
			
			if (fontRenderer.getStringWidth(word) >= width)
			{
				strings.add(string + word);
				string = "";
				word = "";
			}
			else if (character == ' ')
			{
				if (fontRenderer.getStringWidth(string + word) >= width)
				{
					strings.add(string);
					string = word;
					word = "";
				}
				else
				{
					string += word;
					word = "";
				}
			}
		}
		
			if (!string.isEmpty())
				if (fontRenderer.getStringWidth(string + word) >= width)
				{
					strings.add(string);
					if (!word.isEmpty())
						strings.add(word);
				}
				else
					strings.add(string + word);
			else if (!word.isEmpty())
				strings.add(word);
		
		return strings;
	}
}
