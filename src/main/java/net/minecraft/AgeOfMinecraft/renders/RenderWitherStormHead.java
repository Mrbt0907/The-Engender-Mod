package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormHead;
import net.minecraft.AgeOfMinecraft.models.ModelWitherStormHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderWitherStormHead
extends RenderLiving<EntityWitherStormHead>
{
	private static final ResourceLocation witherStormTextures = new ResourceLocation("ageofminecraft", "textures/entities/wither_storm_head.png");
	public RenderWitherStormHead(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelWitherStormHead(), 4.0F);
		addLayer(new LayerWitherStormTractorBeam(this));
	}
	protected ResourceLocation getEntityTexture(EntityWitherStormHead entity)
	{
		return witherStormTextures;
	}
	protected void preRenderCallback(EntityWitherStormHead entitylivingbaseIn, float partialTickTime)
	{
		float f = 2.0F;
		GlStateManager.scale(f, f, f);
		GlStateManager.translate(0.0F, -0.25F, 2.25F);
		if (entitylivingbaseIn.ticksExisted <= 40)
		{
			float f5 = (entitylivingbaseIn.ticksExisted + partialTickTime - 1.0F) / 40.0F * 1.6F;
			f5 = MathHelper.sqrt(f5);
			if (f5 > 1.0F)
			f5 = 1.0F;
			GlStateManager.scale(f5, f5, f5);
		}
	}
}


