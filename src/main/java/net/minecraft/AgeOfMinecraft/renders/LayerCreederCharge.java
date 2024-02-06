package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityCreeder;
import net.minecraft.AgeOfMinecraft.models.ModelCreeder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerCreederCharge implements LayerRenderer<EntityCreeder>
{
	private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	private static final ResourceLocation Anti_LIGHTNING_TEXTURE = new ResourceLocation("ageofminecraft", "textures/entities/anti/creeper_armor.png");
	private final RenderCreeder creeperRenderer;
	private final ModelCreeder creeperModel = new ModelCreeder(2.0F);
	public LayerCreederCharge(RenderCreeder creeperRendererIn)
	{
		this.creeperRenderer = creeperRendererIn;
	}
	public void doRenderLayer(EntityCreeder entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (entitylivingbaseIn.getPowered())
		{
			boolean flag = entitylivingbaseIn.isInvisible();
			GlStateManager.depthMask(!flag);
			this.creeperRenderer.bindTexture(entitylivingbaseIn.isAntiMob() ? Anti_LIGHTNING_TEXTURE : LIGHTNING_TEXTURE);
			GlStateManager.matrixMode(5890);
			GlStateManager.loadIdentity();
			float f = entitylivingbaseIn.ticksExisted + partialTicks;
			if (entitylivingbaseIn.isAntiMob())
			f = -f;
			GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
			GlStateManager.matrixMode(5888);
			GlStateManager.enableBlend();
			int c0 = 15728880;
			int i = c0 % 65536;
			int j = c0 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
			GlStateManager.enableLighting();
			float ran = entitylivingbaseIn.getRNG().nextFloat() * 0.25F;
			GlStateManager.color(0.75F + ran, 0.75F + ran, 0.75F + ran, 1.0F);
			GlStateManager.blendFunc(net.minecraft.client.renderer.GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
			this.creeperModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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
		return true;
	}
}