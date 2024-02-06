package net.minecraft.AgeOfMinecraft.renders;

import java.util.Random;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class LayerEnderDragonDeath implements LayerRenderer<EntityEnderDragon>
{
	public void doRenderLayer(EntityEnderDragon entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (entitylivingbaseIn.deathTicks > 0)
		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder vertexbuffer = tessellator.getBuffer();
			RenderHelper.disableStandardItemLighting();
			float f = ((float)entitylivingbaseIn.deathTicks + partialTicks) / 200.0F;
			float f1 = 0.0F;
			
			if (f > 0.8F)
			{
				f1 = (f - 0.8F) / 0.2F;
			}

			Random random = new Random(432L);
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(7425);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			GlStateManager.disableAlpha();
			GlStateManager.enableCull();
			GlStateManager.depthMask(false);
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, -1.0F, -2.0F);
			
			for (int i = 0; (float)i < (f + f * f) / 2.0F * 60.0F; ++i)
			{
				GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 360.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
				float f2 = random.nextFloat() * 20.0F + 5.0F + f1 * 10.0F;
				float f3 = random.nextFloat() * 2.0F + 1.0F + f1 * 2.0F;
				vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
				vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(255, 255, 255, (int)(255.0F * (1.0F - f1))).endVertex();
				vertexbuffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 255, 0).endVertex();
				vertexbuffer.pos(0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 255, 0).endVertex();
				vertexbuffer.pos(0.0D, (double)f2, (double)(1.0F * f3)).color(255, 0, 255, 0).endVertex();
				vertexbuffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 0, 255, 0).endVertex();
				tessellator.draw();
			}

			GlStateManager.popMatrix();
			GlStateManager.depthMask(true);
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.shadeModel(7424);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			RenderHelper.enableStandardItemLighting();
		}
		if (entitylivingbaseIn.getJukeboxToDanceTo() != null)
		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder vertexbuffer = tessellator.getBuffer();
			RenderHelper.disableStandardItemLighting();
			float f = ((float)entitylivingbaseIn.ticksExisted + 20 + partialTicks) / 60.0F;
			float f1 = 0.0F;
			
			if (f > 0.8F)
			{
				f1 = (f - 0.8F) / 0.2F;
			}

			Random random = new Random(1024L);
			GlStateManager.disableTexture2D();
			GlStateManager.shadeModel(7425);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			GlStateManager.disableAlpha();
			GlStateManager.enableCull();
			GlStateManager.depthMask(false);
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.25F, 0.25F, 0.25F);
			GlStateManager.translate(0.0F, 2F, -30F);
			
			for (int i = 0; (float)i < (f + f * f) / 2.0F * 30.0F; ++i)
			{
				GlStateManager.rotate(random.nextFloat() * 720.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 720.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 720.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(random.nextFloat() * 720.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 720.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(random.nextFloat() * 720.0F + f * 90.0F, 0.0F, 0.0F, 1.0F);
				float f2 = random.nextFloat() * 20.0F + 5.0F + f1 * 10.0F;
				float f3 = random.nextFloat() * 2.0F + 1.0F + f1 * 2.0F;
				vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
				vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(random.nextInt(255), random.nextInt(255), random.nextInt(255), (int)(255.0F * (1.0F - f1))).endVertex();
				vertexbuffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 255, 255, 0).endVertex();
				vertexbuffer.pos(0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 255, 255, 0).endVertex();
				vertexbuffer.pos(0.0D, (double)f2, (double)(1.0F * f3)).color(255, 255, 255, 0).endVertex();
				vertexbuffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(255, 255, 255, 0).endVertex();
				tessellator.draw();
			}

			GlStateManager.popMatrix();
			GlStateManager.depthMask(true);
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.shadeModel(7424);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			RenderHelper.enableStandardItemLighting();
		}
	}

	public boolean shouldCombineTextures()
	{
		return false;
	}
}