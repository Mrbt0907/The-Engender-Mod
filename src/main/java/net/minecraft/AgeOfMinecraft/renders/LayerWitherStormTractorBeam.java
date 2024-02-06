package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormHead;
import net.minecraft.AgeOfMinecraft.models.ModelTractorBeam;
import net.minecraft.AgeOfMinecraft.models.ModelWitherStormHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerWitherStormTractorBeam
implements LayerRenderer<EntityWitherStormHead>
{
	private static final ResourceLocation witherTextures = new ResourceLocation("ageofminecraft", "textures/entities/tractor_beam.png");
	private final ModelBase modelmatter = new ModelTractorBeam();
	private final RenderWitherStormHead witherRenderer;
	public LayerWitherStormTractorBeam(RenderWitherStormHead p_i46105_1_)
	{
		this.witherRenderer = p_i46105_1_;
	}
	public void func_177214_a(EntityWitherStormHead p_177214_1_, float p_177214_2_, float p_177214_3_, float p_177214_4_, float p_177214_5_, float p_177214_6_, float p_177214_7_, float p_177214_8_)
	{
		if (p_177214_1_ != null && !p_177214_1_.isInvisible())
		{
			GlStateManager.pushMatrix();
			boolean flag = p_177214_1_.isInvisible();
			GlStateManager.depthMask(!flag);
			((ModelWitherStormHead)this.witherRenderer.getMainModel()).BackMouth.postRender(0.0625F);
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(1.5F, 18.0F, 1.5F);
			GlStateManager.translate(0.025F, -0.1F, 0.475F);
			this.witherRenderer.bindTexture(witherTextures);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			float f = p_177214_1_.ticksExisted + p_177214_4_;
			GlStateManager.translate(0.0F, f * -0.005F, 0.0F);
			GlStateManager.matrixMode(5888);
			GlStateManager.enableBlend();
			float f1 = 0.75F;
			GlStateManager.color(f1, f1, f1, 1.0F);
			GlStateManager.disableLighting();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			this.modelmatter.render(p_177214_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			GlStateManager.matrixMode(5888);
			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(flag);
			GlStateManager.popMatrix();
		}
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityWitherStormHead p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177214_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


