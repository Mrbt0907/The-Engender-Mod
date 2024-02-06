package net.minecraft.AgeOfMinecraft.renders;
import java.util.Random;

import net.minecraft.AgeOfMinecraft.entity.EntityPortal;
import net.minecraft.AgeOfMinecraft.models.ModelPortal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class RenderPortal
extends RenderLiving<EntityPortal>
{
	private static final ResourceLocation portalTextures = new ResourceLocation("ageofminecraft", "textures/entities/portals/portal.png");
	private Random rnd = new Random();
	public RenderPortal(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelPortal(), 4.0F);
		addLayer(new LayerPortalGlow(this));
		addLayer(new LayerPortalOverlay(this));
	}
	protected void preRenderCallback(EntityPortal entitylivingbaseIn, float partialTickTime)
	{
		GlStateManager.scale(4.0F, 4.0F, 4.0F);
		if (entitylivingbaseIn.ticksExisted < 60)
		{
			float f = (entitylivingbaseIn.ticksExisted + partialTickTime - 1.0F) / 60.0F * 1.6F;
			f = MathHelper.sqrt(f);
			if (f < -1.0F)
			{
				f = -1.0F;
			}
			GlStateManager.translate(0.0F, -f, 0.0F);
			GlStateManager.translate(0.0F, 1.25F, 0.0F);
		}
		GlStateManager.translate(0.0F, 0.001F, 0.0F);
		if (entitylivingbaseIn.deathTime > 0)
		{
			float f = (entitylivingbaseIn.deathTime + partialTickTime - 1.0F) / 60.0F * 1.6F;
			f = MathHelper.sqrt(f);
			if (f > 4.0F)
			{
				f = 4.0F;
			}
			GlStateManager.translate(0.0F, f, 0.0F);
			GlStateManager.rotate(f * 20.0F, 1.0F, 0.0F, 0.0F);
		}
	}
	public void doRender(EntityPortal entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (entity.deathTime > 0)
		{
			double d0 = 0.1D;
			x += this.rnd.nextGaussian() * d0;
			z += this.rnd.nextGaussian() * d0;
		}
		if (entity.hurtTime > 0)
		{
			double d0 = entity.hurtTime * 0.005D;
			x += this.rnd.nextGaussian() * d0;
			z += this.rnd.nextGaussian() * d0;
		}
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	protected void applyRotations(EntityPortal p_180588_1_, float p_180588_2_, float p_180588_3_, float p_180588_4_) { }
	protected ResourceLocation getEntityTexture(EntityPortal entity)
	{
		return portalTextures;
	}
}


