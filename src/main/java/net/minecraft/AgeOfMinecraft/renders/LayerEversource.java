package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEversource;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class LayerEversource implements LayerRenderer<EntityEversource>
{
	private static final ResourceLocation CROWN_AND_MEDALLION = new ResourceLocation("ageofminecraft", "textures/entities/eversource_overlay.png");
	private final RenderLivingBase<?> renderer;
	private ModelChicken layerModel;
	
	public LayerEversource(RenderLivingBase<?> p_i47131_1_)
	{
		this.renderer = p_i47131_1_;
		this.layerModel = new ModelChicken();
	}

	public void doRenderLayer(EntityEversource entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.layerModel.setModelAttributes(this.renderer.getMainModel());
		this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.translate(0.0F, -0.06F, 0.0F);
		this.renderer.bindTexture(CROWN_AND_MEDALLION);
		this.layerModel.body.setRotationPoint(0.0F, 17.0F, -0.001F);
		this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

	public boolean shouldCombineTextures()
	{
		return true;
	}
}