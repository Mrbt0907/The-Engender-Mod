package net.mrbt0907.ageofminecraft.renders;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier4.EntityIceSpider;
@SideOnly(Side.CLIENT)

public class RenderIceSpider
extends RenderSpider<EntityIceSpider>
{
	private static final ResourceLocation spiderTextures = new ResourceLocation("ageofminecraft", "textures/entities/ice_spider.png");
	public RenderIceSpider(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}

	protected ResourceLocation getEntityTexture(EntityIceSpider entity)
	{
		return spiderTextures;
	}
}