package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityCommandBlockWither;
import net.minecraft.AgeOfMinecraft.models.ModelCommandBlockWither;
import net.minecraft.AgeOfMinecraft.models.ModelTractorBeam;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerWitherTractorBeam implements LayerRenderer<EntityLivingBase>
{
	private static final ResourceLocation witherTextures = new ResourceLocation("ageofminecraft", "textures/entities/tractor_beam.png");
	private final ModelBase modelmatter = new ModelTractorBeam();
	private final RenderCommandBlockWither witherRenderer;
	public LayerWitherTractorBeam(RenderCommandBlockWither p_i46105_1_)
	{
		this.witherRenderer = p_i46105_1_;
	}
	public void func_177214_a(EntityCommandBlockWither p_177214_1_, float p_177214_2_, float p_177214_3_, float p_177214_4_, float p_177214_5_, float p_177214_6_, float p_177214_7_, float p_177214_8_)
	{
		if (p_177214_1_.getSize() >= 6000)
		{
			boolean flag = p_177214_1_.isInvisible();
			GlStateManager.depthMask(!flag);
			((ModelCommandBlockWither)this.witherRenderer.getMainModel()).MainHead.postRender(0.0625F);
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(0.5F, 4.0F, 0.5F);
			GlStateManager.translate(0.0F, 0.0F, 0.5F);
			this.witherRenderer.bindTexture(witherTextures);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			float f = p_177214_1_.ticksExisted + p_177214_4_;
			GlStateManager.translate(0.0F, f * -0.02F, 0.0F);
			GlStateManager.matrixMode(5888);
			GlStateManager.enableBlend();
			float f1 = 0.5F;
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
		}
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177214_a((EntityCommandBlockWither)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


