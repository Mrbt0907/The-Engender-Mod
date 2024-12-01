package net.mrbt0907.ageofminecraft.renders;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier4.EntityIceSpider;
import net.mrbt0907.ageofminecraft.entity.tier4.EntityPrisonSpider;

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