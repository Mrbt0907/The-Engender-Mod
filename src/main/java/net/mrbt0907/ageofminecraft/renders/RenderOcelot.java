package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityOcelot;
import net.mrbt0907.ageofminecraft.models.ModelOcelot;
import net.mrbt0907.ageofminecraft.util.RenderUtil;
@SideOnly(Side.CLIENT)

public class RenderOcelot extends RenderLiving<EntityOcelot>
{
	private static final ResourceLocation ocelotTextures = new ResourceLocation("textures/entity/cat/ocelot.png");
	public RenderOcelot(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelOcelot(), 0.4F);
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		//this.addLayer(new LayerLearningBook(this));
	}
	protected ResourceLocation getEntityTexture(EntityOcelot entity)
	{
		return ocelotTextures;
	}
	protected void preRenderCallback(EntityOcelot entitylivingbaseIn, float partialTickTime)
	{
//		if (entitylivingbaseIn.isHero())
//		GlStateManager.scale(1.05F, 1.05F, 1.05F);
		
//		if (!entitylivingbaseIn.onGround)
//		GlStateManager.rotate(entitylivingbaseIn.prevRotationPitchFalling + (entitylivingbaseIn.rotationPitchFalling - entitylivingbaseIn.prevRotationPitchFalling) * 2F - 1F, 1F, 0F, 0F);
		
		RenderUtil.doSpawnRender(entitylivingbaseIn, partialTickTime);
	}
	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRender(EntityOcelot entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		/*if (entity.getGhostTime() > 0)
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
		{*/
			shadowOpaque = 1F;
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		//}
	}

	protected boolean isVisible(EntityOcelot entity)
	{
		return !entity.isInvisible() || this.renderOutlines;
	}
}