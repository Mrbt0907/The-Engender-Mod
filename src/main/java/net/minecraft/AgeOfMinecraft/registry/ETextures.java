package net.minecraft.AgeOfMinecraft.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ETextures
{
	//Internal Code
	private static final Map<String, ResourceLocation> textures = new HashMap<String, ResourceLocation>();
	private static TextureMap map;
	
	//Textures
	public static TextureAtlasSprite textureErasureSprite;
	public static final String textureErasure = "entities/projectiles/projectile_erasure";
	
	public static void init(TextureStitchEvent.Pre event)
	{
		EngenderMod.debug("Registering textures...");
		map = event.getMap();
		textureErasureSprite = register(textureErasure);
		map = null;
	}
	
	private static TextureAtlasSprite register(String location)
	{
		return register(EngenderMod.MODID, location);
	}
	
	private static TextureAtlasSprite register(String modid, String location)
	{
		if (map == null) return null;
		ResourceLocation resource = new ResourceLocation(modid, "textures/" + location + ".png");
		textures.put(location, resource);
		EngenderMod.debug("Registered texture for Engender: " + resource);
		return map.registerSprite(new ResourceLocation(modid, location));
	}
	
	public static ResourceLocation getTexture(String location)
	{
		return textures.get(location);
	}
}
