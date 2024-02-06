package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormSkull;
import net.minecraft.AgeOfMinecraft.models.ModelStormSkull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderStormSkull extends Render<EntityWitherStormSkull>
{
	private static final ResourceLocation witherTextures = new ResourceLocation("ageofminecraft", "textures/entities/wither_storm_skull.png");
	private final ModelStormSkull skeletonHeadModel = new ModelStormSkull();
	public RenderStormSkull(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}
	public void doRender(EntityWitherStormSkull entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translate((float)x, (float)y, (float)z);
		float f2 = 0.0625F;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.rotate(-entity.rotationYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-entity.rotationPitch, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, -0.5F, 0.0F);
		bindEntityTexture(entity);
		if (this.renderOutlines)
		{
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(getTeamColor(entity));
		}
		this.skeletonHeadModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f2);
		if (this.renderOutlines)
		{
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.enableCull();
		GlStateManager.pushMatrix();
		GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(-0.5F, 0.5F, -0.5F);
		blockrendererdispatcher.renderBlockBrightness(Blocks.FIRE.getDefaultState(), 1.0F);
		GlStateManager.popMatrix();
		GlStateManager.disableCull();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	protected ResourceLocation getEntityTexture(EntityWitherStormSkull entity)
	{
		return witherTextures;
	}
}


