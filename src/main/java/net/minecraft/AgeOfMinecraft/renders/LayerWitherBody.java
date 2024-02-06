package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityCommandBlockWither;
import net.minecraft.AgeOfMinecraft.models.ModelCommandBlockWitherBody;
import net.minecraft.AgeOfMinecraft.models.ModelWitherStormTentecle;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerWitherBody
implements LayerRenderer<EntityLivingBase>
{
	private static final ResourceLocation witherTextures = new ResourceLocation("ageofminecraft", "textures/entities/wither_storm_hue_small.png");
	private final ModelBase modelmatter = new ModelCommandBlockWitherBody();
	private final ModelBase modelsmalltentacle = new ModelWitherStormTentecle();
	private final RenderCommandBlockWither witherRenderer;
	public LayerWitherBody(RenderCommandBlockWither p_i46105_1_)
	{
		this.witherRenderer = p_i46105_1_;
	}
	public void func_177214_a(EntityCommandBlockWither p_177214_1_, float p_177214_2_, float p_177214_3_, float p_177214_4_, float p_177214_5_, float p_177214_6_, float p_177214_7_, float p_177214_8_)
	{
		if (p_177214_1_.getSize() >= 1000)
		{
			GlStateManager.pushMatrix();
			this.witherRenderer.bindTexture(witherTextures);
			this.modelmatter.render(p_177214_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GlStateManager.popMatrix();
		}
		if (p_177214_1_.getSize() >= 5000)
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.51F, 1.51F, 1.51F);
			GlStateManager.translate(0.0F, 0.0F, 0.45F);
			this.witherRenderer.bindTexture(witherTextures);
			this.modelmatter.render(p_177214_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GlStateManager.popMatrix();
		}
		if (p_177214_1_.getSize() >= 8000)
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.05F, 0.05F, 0.05F);
			GlStateManager.translate(8.0F, -16.0F, 20.0F);
			GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
			this.witherRenderer.bindTexture(new ResourceLocation("ageofminecraft", "textures/entities/wither_storm_hue.png"));
			this.modelsmalltentacle.render(p_177214_1_, p_177214_2_, p_177214_3_, p_177214_5_, p_177214_6_, p_177214_7_, p_177214_8_);
			this.modelsmalltentacle.setRotationAngles(p_177214_2_, p_177214_3_, p_177214_5_, p_177214_6_, p_177214_7_, p_177214_8_, p_177214_1_);
			this.modelsmalltentacle.setLivingAnimations(p_177214_1_, p_177214_2_, p_177214_3_, p_177214_4_);
			GlStateManager.popMatrix();
		}
		if (p_177214_1_.getSize() >= 12250)
		{
			GlStateManager.pushMatrix();
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(4.0F, 4.0F, 4.0F);
			GlStateManager.translate(0.0F, -0.5F, -0.5F);
			this.witherRenderer.bindTexture(witherTextures);
			this.modelmatter.render(p_177214_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GlStateManager.popMatrix();
		}
		if (p_177214_1_.getSize() >= 5250)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 1.3F, -0.5F);
			GlStateManager.rotate(30.0F, 1.0F, 0.0F, 0.0F);
			this.witherRenderer.bindTexture(witherTextures);
			this.modelmatter.render(p_177214_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GlStateManager.popMatrix();
		}
	}
	public boolean shouldCombineTextures()
	{
		return true;
	}
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177214_a((EntityCommandBlockWither)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


