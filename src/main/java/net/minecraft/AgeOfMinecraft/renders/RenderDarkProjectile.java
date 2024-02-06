package net.minecraft.AgeOfMinecraft.renders;

import org.lwjgl.opengl.GL11;

import net.minecraft.AgeOfMinecraft.entity.cameos.Darkness.EntityDarkProjectile;
import net.minecraft.AgeOfMinecraft.registry.ETextures;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderDarkProjectile extends Render<EntityDarkProjectile>
{	
	
	
	public RenderDarkProjectile(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntityDarkProjectile projectile, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		bindEntityTexture(projectile);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0F);
		GlStateManager.disableLighting();
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		double height = projectile.ticksExisted % 8.0D * 0.125D;
		double scale = projectile.getSize();
		double u1 = 0.0D, u2 = 1.0D, u3 = height, u4 = height + 0.125D;
		double f1 = 0.5D, f2 = 1.0D, f3 = 0.25D;
		
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(0.0D - f1, 0.0D - f3, 0.0D).tex(u1, u4).endVertex();
		buffer.pos(f2 - f1, 0.0D - f3, 0.0D).tex(u2, u4).endVertex();
		buffer.pos(f2 - f1, 1.0D - f3, 0.0D).tex(u2, u3).endVertex();
		buffer.pos(0.0D - f1, 1.0D - f3, 0.0D).tex(u1, u3).endVertex();
		tessellator.draw();
		
		GlStateManager.enableLighting();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.disableBlend();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDarkProjectile entity)
	{
		switch (entity.getType())
		{
			default:
				return ETextures.getTexture(ETextures.textureErasure);
		}
	}

}
