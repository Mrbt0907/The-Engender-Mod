package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityPig;
@SideOnly(Side.CLIENT)

public class RenderPig
extends RenderLiving<EntityPig>
{
	private static final ResourceLocation pigTextures = new ResourceLocation("textures/entity/pig/pig.png");
	public RenderPig(RenderManager p_i46149_1_)
	{
		super(p_i46149_1_, new ModelPig(0.0F), 0.75F);
		addLayer(new LayerSaddle(this));
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		this.addLayer(new LayerCustomHeadEngender(((ModelPig)this.mainModel).head));
//		this.addLayer(new LayerLearningBook(this));
	}
	protected ResourceLocation getEntityTexture(EntityPig entity)
	{
		return pigTextures;
	}
	protected void preRenderCallback(EntityPig entitylivingbaseIn, float partialTickTime)
	{
//		if (entitylivingbaseIn.isHero())
//		GlStateManager.scale(1.05F, 1.05F, 1.05F);
		
//		if (!entitylivingbaseIn.onGround)
//		GlStateManager.rotate(entitylivingbaseIn.prevRotationPitchFalling + (entitylivingbaseIn.rotationPitchFalling - entitylivingbaseIn.prevRotationPitchFalling) * 2F - 1F, 1F, 0F, 0F);
		
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
	public void doRender(EntityPig entity, double x, double y, double z, float entityYaw, float partialTicks)
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

	protected boolean isVisible(EntityPig entity)
	{
		return !entity.isInvisible() || this.renderOutlines;
	}
}