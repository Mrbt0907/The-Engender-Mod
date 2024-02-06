package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityIceSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)

public class LayerIceSpiderEyes implements LayerRenderer<EntityIceSpider>
{
	private static final ResourceLocation SPIDER_EYES = new ResourceLocation("ageofminecraft", "textures/entities/ice_spider_eyes.png");
	private final RenderIceSpider spiderRenderer;
	public LayerIceSpiderEyes(RenderIceSpider p_i46109_1_)
	{
		this.spiderRenderer = p_i46109_1_;
	}
	public void func_177148_a(EntityIceSpider p_177148_1_, float p_177148_2_, float p_177148_3_, float p_177148_4_, float p_177148_5_, float p_177148_6_, float p_177148_7_, float p_177148_8_)
	{
		this.spiderRenderer.bindTexture(SPIDER_EYES);
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
		char c0 = 61680;
		int i = c0 % 65536;
		int j = c0 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.spiderRenderer.getMainModel().render(p_177148_1_, p_177148_2_, p_177148_3_, p_177148_5_, p_177148_6_, p_177148_7_, p_177148_8_);
		int k = p_177148_1_.getBrightnessForRender();
		i = k % 65536;
		j = k / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
		this.spiderRenderer.setLightmap(p_177148_1_);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityIceSpider p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177148_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


