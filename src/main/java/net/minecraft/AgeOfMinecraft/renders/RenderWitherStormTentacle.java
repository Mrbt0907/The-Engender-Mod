package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormTentacle;
import net.minecraft.AgeOfMinecraft.models.ModelWitherStormTentecle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderWitherStormTentacle
extends RenderLiving<EntityWitherStormTentacle>
{
	private static final ResourceLocation witherStormTextures = new ResourceLocation("ageofminecraft", "textures/entities/wither_storm_hue.png");
	public RenderWitherStormTentacle(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelWitherStormTentecle(), 3.0F);
	}
	protected ResourceLocation getEntityTexture(EntityWitherStormTentacle entity)
	{
		return witherStormTextures;
	}
	protected void preRenderCallback(EntityWitherStormTentacle entitylivingbaseIn, float partialTickTime)
	{
		float f = 1.5F;
		GlStateManager.scale(f, f, f);
		GlStateManager.translate(0.0F, -0.5F, -2.0F);
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


