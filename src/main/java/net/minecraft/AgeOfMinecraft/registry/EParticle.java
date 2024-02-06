package net.minecraft.AgeOfMinecraft.registry;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EParticle
{
	public static TextureAtlasSprite powerupDarkness;
	
	@SubscribeEvent
	public static void registerParticles(TextureStitchEvent.Pre event)
	{
		TextureMap map = event.getMap();
		powerupDarkness = map.registerSprite(new ResourceLocation(EngenderMod.MODID, "particles/powerup_darkness"));
	}	
}
