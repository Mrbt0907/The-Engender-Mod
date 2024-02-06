package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerWitherStormEyes implements LayerRenderer
{
	private static final ResourceLocation RES_ENDERMAN_EYES = new ResourceLocation("ageofminecraft", "textures/entities/wither_storm_head.png");
	private final RenderWitherStormHead endermanRenderer;
	public LayerWitherStormEyes(RenderWitherStormHead p_i46117_1_)
	{
		this.endermanRenderer = p_i46117_1_;
	}
	public void func_177201_a(EntityWitherStormHead p_177201_1_, float p_177201_2_, float p_177201_3_, float p_177201_4_, float p_177201_5_, float p_177201_6_, float p_177201_7_, float p_177201_8_)
	{
		this.endermanRenderer.bindTexture(RES_ENDERMAN_EYES);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(1, 1);
		GlStateManager.disableLighting();
		if (p_177201_1_.isInvisible())
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
		GlStateManager.enableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.endermanRenderer.getMainModel().render(p_177201_1_, p_177201_2_, p_177201_3_, p_177201_5_, p_177201_6_, p_177201_7_, p_177201_8_);
		this.endermanRenderer.setLightmap(p_177201_1_);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177201_a((EntityWitherStormHead)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


