package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.entity.tier2.EntityLlama;
import net.minecraft.AgeOfMinecraft.models.ModelLlama;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class RenderLlama extends RenderLiving<EntityLlama>
{
	private static final ResourceLocation[] LLAMA_TEXTURES = new ResourceLocation[] {new ResourceLocation("textures/entity/llama/llama_creamy.png"), new ResourceLocation("textures/entity/llama/llama_white.png"), new ResourceLocation("textures/entity/llama/llama_brown.png"), new ResourceLocation("textures/entity/llama/llama_gray.png")};

public RenderLlama(RenderManager p_i47203_1_)
{
	super(p_i47203_1_, new ModelLlama(0.0F), 0.75F);
	addLayer(new LayerArrowCustomSized(this, 1.0F));
	this.addLayer(new LayerCustomHeadEngender(((ModelLlama)this.mainModel).head));
	this.addLayer(new LayerLearningBook(this));
}

/**
* Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
*/
protected ResourceLocation getEntityTexture(EntityLlama entity)
{
	return LLAMA_TEXTURES[entity.getVariant()];
}
protected void preRenderCallback(EntityLlama entitylivingbaseIn, float partialTickTime)
{
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
/**
* Renders the desired {@code T} type Entity.
*/
public void doRender(EntityLlama entity, double x, double y, double z, float entityYaw, float partialTicks)
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

protected boolean isVisible(EntityLlama entity)
{
	return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
}
}