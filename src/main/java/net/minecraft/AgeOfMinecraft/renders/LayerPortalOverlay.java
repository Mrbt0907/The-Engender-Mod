package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.EntityPortal;
import net.minecraft.AgeOfMinecraft.models.ModelPortal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)

public class LayerPortalOverlay implements LayerRenderer
{
	private static final ResourceLocation PORTAL_OVERLAY = new ResourceLocation("ageofminecraft", "textures/entities/portals/portal_portal.png");
	private final RenderPortal renderer;
	private ModelPortal layerModel;
	public LayerPortalOverlay(RenderPortal p_i46109_1_)
	{
		this.renderer = p_i46109_1_;
		this.layerModel = new ModelPortal(true);
	}
	public void func_177148_a(EntityPortal entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		float f = entitylivingbaseIn.ticksExisted + partialTicks;
		int c0 = 15728880;
		int i = c0 % 65536;
		int j = c0 / 65536;
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
		this.renderer.bindTexture(new ResourceLocation("textures/entity/end_portal.png"));
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.translate(f * 0.5F, f * 0.25F, f * -0.1F);
		GlStateManager.rotate(30F, 0F, 0F, 1F);
		GlStateManager.matrixMode(5888);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
		GlStateManager.color(1F, 1F, 1F, 0.9F);
		GlStateManager.scale(0.999F, 0.999F, 0.999F);
		this.layerModel.onlybase = true;
		this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.layerModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
		this.renderer.setLightmap(entitylivingbaseIn);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableNormalize();
		
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
		this.renderer.bindTexture(PORTAL_OVERLAY);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.translate(f * -0.00005F, f * 0.00025F, f * 0.0001F);
		GlStateManager.rotate(-55F, 0F, 0F, 1F);
		GlStateManager.matrixMode(5888);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
		GlStateManager.color(1F, 1F, 1F, 0.8F);
		this.layerModel.onlybase = true;
		this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.layerModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
		this.renderer.setLightmap(entitylivingbaseIn);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableNormalize();
		
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
		this.renderer.bindTexture(PORTAL_OVERLAY);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.translate(f * 0.00005F, f * 0.00025F, f * -0.0001F);
		GlStateManager.rotate(f * -0.01F, 0F, 0F, 1F);
		GlStateManager.matrixMode(5888);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
		GlStateManager.color(1F, 1F, 1F, 0.75F);
		this.layerModel.onlybase = true;
		this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.layerModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
		this.renderer.setLightmap(entitylivingbaseIn);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableNormalize();
		
		GlStateManager.enableNormalize();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.depthMask(!entitylivingbaseIn.isInvisible());
		this.renderer.bindTexture(new ResourceLocation("textures/entity/end_portal.png"));
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.translate(f * 0.95F, f * 0.925F, f * -0.1F);
		GlStateManager.matrixMode(5888);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
		GlStateManager.color(0.4F + (MathHelper.cos(f * 0.1F) * 0.25F), 0.4F + (MathHelper.cos(f * 0.1F) * 0.25F), 0.4F + (MathHelper.cos(f * 0.1F) * 0.25F), 0.4F + (MathHelper.cos(f * 0.1F) * 0.25F));
		this.layerModel.onlybase = true;
		this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.layerModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
		this.renderer.setLightmap(entitylivingbaseIn);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.disableNormalize();
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


