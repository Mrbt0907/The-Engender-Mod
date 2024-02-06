package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStorm;
import net.minecraft.AgeOfMinecraft.models.ModelWitherStorm;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderWitherStorm
extends RenderLiving<EntityWitherStorm>
{
	private static final ResourceLocation witherStormTextures = new ResourceLocation("ageofminecraft", "textures/entities/wither_storm.png");
	public RenderWitherStorm(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelWitherStorm(), 16.0F);
		addLayer(new LayerWitherStormBody(this));
	}
	protected void preRenderCallback(EntityWitherStorm entitylivingbaseIn, float partialTickTime)
	{
		if (entitylivingbaseIn.doesntContainACommandBlock())
		{
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(6F, 6F, 6F);
			GlStateManager.translate(0.0F, -0.5F, -0.5F);
			GlStateManager.rotate(90F, 1.0F, 0.0F, 0.0F);
		}
	}
	protected ResourceLocation getEntityTexture(EntityWitherStorm entitylivingbaseIn)
	{
		return entitylivingbaseIn.doesntContainACommandBlock() ? null : witherStormTextures;
	}
}