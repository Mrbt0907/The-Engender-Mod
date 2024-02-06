package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEversource;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderEversource
extends RenderLiving<EntityEversource>
{
	private static final ResourceLocation chickenTextures = new ResourceLocation("textures/entity/chicken.png");
	public RenderEversource(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelChicken(), 0.3F);
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		addLayer(new LayerEversource(this));
		this.addLayer(new LayerCustomHeadEngender(((ModelChicken)this.mainModel).head));
		this.addLayer(new LayerLearningBook(this));
	}
	protected void preRenderCallback(EntityEversource entitylivingbaseIn, float partialTickTime)
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
	protected ResourceLocation getEntityTexture(EntityEversource entity)
	{
		return chickenTextures;
	}
	protected float handleRotationFloat(EntityEversource livingBase, float partialTicks)
	{
		float f = livingBase.field_70888_h + (livingBase.wingRotation - livingBase.field_70888_h) * partialTicks;
		float f1 = livingBase.field_70884_g + (livingBase.destPos - livingBase.field_70884_g) * partialTicks;
		return (MathHelper.sin(f) + 1.0F) * f1;
	}
	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRender(EntityEversource entity, double x, double y, double z, float entityYaw, float partialTicks)
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


