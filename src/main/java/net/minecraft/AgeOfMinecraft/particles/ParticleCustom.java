package net.minecraft.AgeOfMinecraft.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

public class ParticleCustom extends Particle
{
	public ParticleCustom(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, TextureAtlasSprite sprite)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		setParticleTexture(sprite);
		particleScale = 3.0F;
	}
	
	@Override
	public int getFXLayer()
    {
        return 1;
    }
}
