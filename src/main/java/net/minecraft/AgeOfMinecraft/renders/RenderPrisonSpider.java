package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.entity.tier4.EntityIceSpider;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPrisonSpider;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class RenderPrisonSpider extends RenderSpider<EntityPrisonSpider>
{
	private static final ResourceLocation spiderTextures = new ResourceLocation("ageofminecraft", "textures/entities/prison_spider.png");
	public RenderPrisonSpider(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}
	protected ResourceLocation getEntityTexture(EntityIceSpider entity)
	{
		return spiderTextures;
	}
}