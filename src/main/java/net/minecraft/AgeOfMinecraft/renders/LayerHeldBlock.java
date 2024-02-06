package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerHeldBlock
implements LayerRenderer<EntityLivingBase>
{
	private final RenderEnderman endermanRenderer;
	public LayerHeldBlock(RenderEnderman p_i46122_1_)
	{
		this.endermanRenderer = p_i46122_1_;
	}
	public void func_177173_a(EntityEnderman entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		IBlockState iblockstate = entitylivingbaseIn.getHeldBlockState();
		if (iblockstate != null)
		{
			BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			GlStateManager.enableRescaleNormal();
			GlStateManager.pushMatrix();
			
				if (entitylivingbaseIn.isChild())
				{
					GlStateManager.translate(0.0F, 0.75F, 0.0F);
					GlStateManager.scale(0.5F, 0.5F, 0.5F);
				}
				GlStateManager.translate(0.0F, 0.6875F, -0.75F);
				GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.25F, 0.1875F, 0.25F);
			
			float f = 0.5F;
			GlStateManager.scale(-f, -f, f);
			int i = entitylivingbaseIn.getBrightnessForRender();
			int j = i % 65536;
			int k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.endermanRenderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			blockrendererdispatcher.renderBlockBrightness(iblockstate, 1.0F);
			GlStateManager.popMatrix();
			GlStateManager.disableRescaleNormal();
		}
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177173_a((EntityEnderman)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


