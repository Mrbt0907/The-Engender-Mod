package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.entity.EntityManaOrb;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class RenderManaOrb extends Render<EntityManaOrb>
{
	private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("ageofminecraft", "textures/entities/essence_orb.png");
	
	public RenderManaOrb(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}

	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRender(EntityManaOrb entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (!this.renderOutlines)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y, (float)z);
			this.bindEntityTexture(entity);
			RenderHelper.enableStandardItemLighting();
			float i = entity.getTextureByXP();
			float i1 = entity.getTextureY();
			float f = (float)(i % 4 * 16 + 8) / 64.0F;
			float f1 = (float)(i % 4 * 16 + 0) / 64.0F;
			float f2 = (float)(i1 / 4 * 16 + 0) / 64.0F;
			float f3 = (float)(i1 / 4 * 16 + 16) / 64.0F;
			int j = entity.getBrightnessForRender();
			int k = j % 65536;
			int l = j / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k, (float)l);
			GlStateManager.color(1, 1, 1);
			float f9 = ((float)entity.xpColor + partialTicks) / 2.0F;
			l = (int)((MathHelper.sin(f9 + 0.0F) + 1.0F) * 0.5F * 255.0F);
			MathHelper.sin(f9 + 4.1887903F);
			GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 5.0F, 0.0F);
			GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
			if (entity.getEntropy())
			{
				bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex((double)f, (double)f3).color(255, l, l, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
				bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex((double)f1, (double)f3).color(255, l, l, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
				bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex((double)f1, (double)f2).color(255, l, l, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
				bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex((double)f, (double)f2).color(255, l, l, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
			}
			else
			{
				bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex((double)f, (double)f3).color(l, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
				bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex((double)f1, (double)f3).color(l, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
				bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex((double)f1, (double)f2).color(l, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
				bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex((double)f, (double)f2).color(l, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
			}
			tessellator.draw();
			GlStateManager.disableBlend();
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}

	/**
	* Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	*/
	protected ResourceLocation getEntityTexture(EntityManaOrb entity)
	{
		return EXPERIENCE_ORB_TEXTURES;
	}
}