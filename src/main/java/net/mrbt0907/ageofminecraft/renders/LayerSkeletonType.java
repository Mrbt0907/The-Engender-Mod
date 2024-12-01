package net.mrbt0907.ageofminecraft.renders;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier3.EntitySkeleton;
import net.mrbt0907.ageofminecraft.models.ModelSkeleton;

@SideOnly(Side.CLIENT)

public class LayerSkeletonType implements LayerRenderer<EntitySkeleton>
{
	private static final ResourceLocation STRAY_CLOTHES_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray_overlay.png");
	private static final ResourceLocation antiSTRAY_CLOTHES_TEXTURES = new ResourceLocation("ageofminecraft", "textures/entities/anti/stray_overlay.png");
	private final RenderLivingBase<?> renderer;
	private ModelSkeleton layerModel;
	
	public LayerSkeletonType(RenderLivingBase<?> p_i47131_1_)
	{
		this.renderer = p_i47131_1_;
		this.layerModel = new ModelSkeleton(0.25F, true);
	}

	public void doRenderLayer(EntitySkeleton entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (entitylivingbaseIn.getSkeletonType() == 2 && !entitylivingbaseIn.isInvisible())
		{
			this.layerModel.setModelAttributes(this.renderer.getMainModel());
			this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.renderer.bindTexture((entitylivingbaseIn.isAntiMob() ? antiSTRAY_CLOTHES_TEXTURES : STRAY_CLOTHES_TEXTURES));
			this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}

	public boolean shouldCombineTextures()
	{
		return true;
	}
}