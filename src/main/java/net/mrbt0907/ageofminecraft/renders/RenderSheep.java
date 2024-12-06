package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntitySheep;
import net.mrbt0907.ageofminecraft.models.ModelSheep2;
@SideOnly(Side.CLIENT)

public class RenderSheep
extends RenderLiving<EntitySheep>
{
	private static final ResourceLocation shearedSheepTextures = new ResourceLocation("textures/entity/sheep/sheep.png");
	public RenderSheep(RenderManager p_i46145_1_)
	{
		super(p_i46145_1_, new ModelSheep2(), 0.8F);
		addLayer(new LayerSheepWool(this));
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		addLayer(new LayerCustomHeadEngender(((ModelSheep2)mainModel).head));
//		this.addLayer(new LayerLearningBook(this));
	}
	
	protected ResourceLocation getEntityTexture(EntitySheep entity)
	{
		return shearedSheepTextures;
	}
	
	protected void preRenderCallback(EntitySheep entitylivingbaseIn, float partialTickTime)
	{
//		if (entitylivingbaseIn.isHero())
//		GlStateManager.scale(1.05F, 1.05F, 1.05F);
		
//		if (!entitylivingbaseIn.onGround)
//		GlStateManager.rotate(entitylivingbaseIn.prevRotationPitchFalling + (entitylivingbaseIn.rotationPitchFalling - entitylivingbaseIn.prevRotationPitchFalling) * 2F - 1F, 1F, 0F, 0F);
		
		if (entitylivingbaseIn.ticksExisted <= 21 && entitylivingbaseIn.ticksExisted > 0)
		{
			float f5 = MathHelper.sqrt((entitylivingbaseIn.ticksExisted + partialTickTime - 1.0F) / 20.0F * 1.6F);
			if (f5 > 1.0F)
				f5 = 1.0F;
			GlStateManager.scale(f5, f5, f5);
			GlStateManager.rotate(f5 * 90F - 90F, f5, f5, f5);
		}

	}
	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRender(EntitySheep entity, double x, double y, double z, float entityYaw, float partialTicks)
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

	protected boolean isVisible(EntitySheep entity)
	{
		return !entity.isInvisible() || renderOutlines;
	}
}