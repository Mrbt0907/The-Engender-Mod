package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.EntityPortal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)

public class LayerPortalGlow implements LayerRenderer
{
	private static final ResourceLocation PORTAL_OVERLAY = new ResourceLocation("ageofminecraft", "textures/entities/portals/portal_glow.png");
	private static final ResourceLocation PORTAL_OVERLAY2 = new ResourceLocation("ageofminecraft", "textures/entities/portals/portal_glow2.png");
	private static final ResourceLocation PORTAL_OVERLAY3 = new ResourceLocation("ageofminecraft", "textures/entities/portals/portal_glow3.png");
	private static final ResourceLocation PORTAL_OVERLAY4 = new ResourceLocation("ageofminecraft", "textures/entities/portals/portal_glow4.png");
	private static final ResourceLocation PORTAL_OVERLAY5 = new ResourceLocation("ageofminecraft", "textures/entities/portals/portal_glow5.png");
	private final RenderPortal spiderRenderer;
	public LayerPortalGlow(RenderPortal p_i46109_1_)
	{
		this.spiderRenderer = p_i46109_1_;
	}
	public void func_177148_a(EntityPortal p_177148_1_, float p_177148_2_, float p_177148_3_, float p_177148_4_, float p_177148_5_, float p_177148_6_, float p_177148_7_, float p_177148_8_)
	{
		if (p_177148_1_.getMetaData() > 3)
		this.spiderRenderer.bindTexture(PORTAL_OVERLAY5);
		else if (p_177148_1_.getMetaData() == 3)
		this.spiderRenderer.bindTexture(PORTAL_OVERLAY4);
		else if (p_177148_1_.getMetaData() == 2)
		this.spiderRenderer.bindTexture(PORTAL_OVERLAY3);
		else if (p_177148_1_.getMetaData() == 1)
		this.spiderRenderer.bindTexture(PORTAL_OVERLAY2);
		else
		this.spiderRenderer.bindTexture(PORTAL_OVERLAY);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(1, 1);
		if (p_177148_1_.isInvisible())
		{
			GlStateManager.depthMask(false);
		}
		else
		{
			GlStateManager.depthMask(true);
		}
		int c0 = 15728880;
		int i = c0 % 65536;
		int j = c0 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.spiderRenderer.getMainModel().render(p_177148_1_, p_177148_2_, p_177148_3_, p_177148_5_, p_177148_6_, p_177148_7_, p_177148_8_);
		this.spiderRenderer.setLightmap(p_177148_1_);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177148_a((EntityPortal)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


