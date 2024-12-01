package net.mrbt0907.ageofminecraft.util.mrbtutil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TranslateUtil
{
	public static String getTranslationKey(String key, int amount)
	{
		if (amount > 1)
		{
			int index = Maths.random(amount - 1);
			if (index > 0)
				return key + "." + index;
		}

		return key;
	}
	
	public static void sendChatMult(EntityPlayer player, String key, int amount, Object... values)
	{
		sendChat(player, getTranslationKey(key, amount), values);
	}

	public static void sendChat(EntityPlayer player, String key, Object... values)
	{
		player.sendMessage(new TextComponentTranslation(key, player.getDisplayNameString(), values));
	}
	
	public static void sendChatAllMult(String key, int amount, Object... values)
	{
		sendChatAll(getTranslationKey(key, amount), values);
	}

	public static void sendChatAll(String key, Object... values)
	{
		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().forEach(player -> sendChat(player, key, values));
	}
	
	public static ITextComponent translateChatMult(String key, int amount, Object... values)
	{
		return translateChat(getTranslationKey(key, amount), values);
	}

	public static ITextComponent translateChat(String key, Object... values)
	{
		return new TextComponentTranslation(key, values);
	}
	
	public static String translateMult(String key, int amount, Object... values)
	{
		return translate(getTranslationKey(key, amount), values);
	}

	public static String translate(String key, Object... values)
	{
		String value = net.minecraft.client.resources.I18n.format(key, values); 
		return value;
	}
	
	public static String translateMultServer(String key, int amount, Object... values)
	{
		return translateServer(getTranslationKey(key, amount), values);
	}
	
	@SuppressWarnings("deprecation")
	public static String translateServer(String key, Object... values)
	{
		String value = net.minecraft.util.text.translation.I18n.translateToLocalFormatted(key, values); 
		return value;
	}
}


