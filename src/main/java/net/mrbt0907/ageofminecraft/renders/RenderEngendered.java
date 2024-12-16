package net.mrbt0907.ageofminecraft.renders;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.models.ICappedModel;

public abstract class RenderEngendered<T extends EntityEngendered> extends RenderLiving<T>
{
	public RenderEngendered(RenderManager manager, ModelBase model, float shadowSize)
	{
		super(manager, model, shadowSize);
		if (model instanceof ModelBiped)
			addLayer(new LayerCustomHeadEngender(((ModelBiped)model).bipedHead));
		else if (model instanceof ModelQuadruped)
			addLayer(new LayerCustomHeadEngender(((ModelQuadruped)model).head));
		else if (model instanceof ModelVillager)
			addLayer(new LayerCustomHeadEngender(((ModelVillager)model).villagerHead));	
		else if (model instanceof IModelHead)
			addLayer(new LayerCustomHeadEngender(((IModelHead)model).getHead()));
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		if (model instanceof ICappedModel)
			addLayer(new LayerMobCape(this));
	}

	protected void preRenderCallback(T entity, float partialTicks)
	{
		if (entity.ticksExisted < 22)
		{
			float f5 = MathHelper.sqrt((entity.ticksExisted + partialTicks - 1.0F) * 0.08F);
			if (f5 > 1.0F)
				f5 = 1.0F;
			GlStateManager.scale(f5, f5, f5);
			GlStateManager.rotate(f5 * 90F - 90F, f5, f5, f5);
		}
	}
	
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		shadowOpaque = 1F;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}
