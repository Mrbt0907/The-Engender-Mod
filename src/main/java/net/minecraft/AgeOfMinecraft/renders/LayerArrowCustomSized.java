package net.minecraft.AgeOfMinecraft.renders;
import java.util.Random;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerArrowCustomSized implements net.minecraft.client.renderer.entity.layers.LayerRenderer<EntityLivingBase>
{
	private final RenderLivingBase<?> renderer;
	private float scale;
	public LayerArrowCustomSized(RenderLivingBase<?> rendererIn, float scale)
	{
		this.renderer = rendererIn;
		this.scale = scale;
	}
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		int i = entitylivingbaseIn.getArrowCountInEntity();
		if (i > 0)
		{
			Entity entity = new EntityTippedArrow(entitylivingbaseIn.world, entitylivingbaseIn.posX, entitylivingbaseIn.posY, entitylivingbaseIn.posZ);
			Random random = new Random(entitylivingbaseIn.getEntityId());
			RenderHelper.disableStandardItemLighting();
			for (int j = 0; j < i; j++)
			{
				GlStateManager.pushMatrix();
				ModelRenderer modelrenderer = this.renderer.getMainModel().getRandomModelBox(random);
				ModelBox modelbox = (ModelBox)modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
				modelrenderer.postRender(0.0625F);
				float f = random.nextFloat();
				float f1 = random.nextFloat();
				float f2 = random.nextFloat();
				float f3 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f) / 16.0F;
				float f4 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f1) / 16.0F;
				float f5 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f2) / 16.0F;
				GlStateManager.translate(f3, f4, f5);
				GlStateManager.scale(this.scale, this.scale, this.scale);
				f = f * 2.0F - 1.0F;
				f1 = f1 * 2.0F - 1.0F;
				f2 = f2 * 2.0F - 1.0F;
				f *= -1.0F;
				f1 *= -1.0F;
				f2 *= -1.0F;
				float f6 = MathHelper.sqrt(f * f + f2 * f2);
				entity.prevRotationYaw = (entity.rotationYaw = (float)(Math.atan2(f, f2) * 57.29577951308232D));
				entity.prevRotationPitch = (entity.rotationPitch = (float)(Math.atan2(f1, f6) * 57.29577951308232D));
				double d0 = 0.0D;
				double d1 = 0.0D;
				double d2 = 0.0D;
				this.renderer.getRenderManager().renderEntity(entity, d0, d1, d2, 0.0F, partialTicks, false);
				GlStateManager.popMatrix();
			}
			RenderHelper.enableStandardItemLighting();
		}
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
}


