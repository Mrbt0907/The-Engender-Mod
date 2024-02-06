package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityCreeder;
import net.minecraft.AgeOfMinecraft.models.ModelCreeder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderCreeder extends RenderLiving<EntityCreeder>
{
	private static final ResourceLocation spiderTextures = new ResourceLocation("ageofminecraft", "textures/entities/creeder.png");
	
	public RenderCreeder(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelCreeder(), 1.0F);
		addLayer(new LayerCreederEyes(this));
		addLayer(new LayerCreederCharge(this));
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		this.addLayer(new LayerMobCape(this));
		this.addLayer(new LayerLearningBook(this));
	}

	protected void preRenderCallback(EntityCreeder entitylivingbaseIn, float partialTickTime)
	{
		float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
		float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f *= f;
		f *= f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		GlStateManager.scale(f2, f3, f2);
		if (entitylivingbaseIn.isRiding())
		GlStateManager.translate(0.0F, -0.325F, 0.0F);
		if (entitylivingbaseIn.isSneaking())
		GlStateManager.translate(0.0F, 0.2F, 0.0F);
		
		float fit = entitylivingbaseIn.getFittness();
		GlStateManager.scale(fit, fit, fit);
		
		if (entitylivingbaseIn.isHero())
		GlStateManager.scale(1.05F, 1.05F, 1.05F);
		
		if (!entitylivingbaseIn.onGround)
		GlStateManager.rotate(entitylivingbaseIn.prevRotationPitchFalling + (entitylivingbaseIn.rotationPitchFalling - entitylivingbaseIn.prevRotationPitchFalling) * 2F - 1F, 1F, 0F, 0F);
		
		if (entitylivingbaseIn.ticksExisted <= 21 && entitylivingbaseIn.ticksExisted > 0)
		{
			float f5 = (entitylivingbaseIn.ticksExisted + partialTickTime - 1.0F) / 20.0F * 1.6F;
			f5 = MathHelper.sqrt(f5);
			if (f5 > 1.0F)
			f5 = 1.0F;
			GlStateManager.scale(f5, f5, f5);
			GlStateManager.rotate(f5 * 90F - 90F, f5, f5, f5);
		}

	}
	protected int getColorMultiplier(EntityCreeder entitylivingbaseIn, float lightBrightness, float partialTickTime)
	{
		float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
		if ((int)(f * 10.0F) % 2 == 0)
		{
			return 0;
		}
		int i = (int)(f * 0.2F * 255.0F);
		i = MathHelper.clamp(i, 0, 255);
		return i << 24 | 0x30FFFFFF;
	}
	protected ResourceLocation getEntityTexture(EntityCreeder entity)
	{
		return spiderTextures;
	}
	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRender(EntityCreeder entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (entity.getGhostTime() > 0)
		{
			Vec3d[] avec3d = entity.getRenderLocations(partialTicks);
			float f = this.handleRotationFloat(entity, partialTicks);
			
			for (int i = 0; i < avec3d.length; ++i)
			{
				super.doRender(entity, x + avec3d[i].x + (double)MathHelper.cos((float)i + f * 0.5F) * 0.025D, y + avec3d[i].y + (double)MathHelper.cos((float)i + f * 0.75F) * 0.0125D, z + avec3d[i].z + (double)MathHelper.cos((float)i + f * 0.7F) * 0.025D, entityYaw, partialTicks);
			}
			this.shadowOpaque = 0F;
		}
		else
		{
			this.shadowOpaque = 1F;
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}
	protected boolean isVisible(EntityFriendlyCreature entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}
}