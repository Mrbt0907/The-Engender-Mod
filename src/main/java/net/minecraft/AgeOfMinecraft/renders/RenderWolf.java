package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityWolf;
import net.minecraft.AgeOfMinecraft.models.ModelWolf;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderWolf
extends RenderLiving<EntityWolf>
{
	private static final ResourceLocation wolfTextures = new ResourceLocation("textures/entity/wolf/wolf.png");
	private static final ResourceLocation anrgyWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
	private static final ResourceLocation antiwolfTextures = new ResourceLocation("ageofminecraft", "textures/entities/anti/wolf.png");
	private static final ResourceLocation antianrgyWolfTextures = new ResourceLocation("ageofminecraft", "textures/entities/anti/wolf_angry.png");
	public RenderWolf(RenderManager p_i46128_1_)
	{
		super(p_i46128_1_, new ModelWolf(), 0.5F);
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		this.addLayer(new LayerCustomHeadEngender(((ModelWolf)this.mainModel).wolfHeadMain));
		this.addLayer(new LayerLearningBook(this));
	}
	protected float func_180593_a(EntityWolf p_180593_1_, float p_180593_2_)
	{
		return p_180593_1_.getTailRotation();
	}
	public void func_177135_a(EntityWolf p_177135_1_, double p_177135_2_, double p_177135_4_, double p_177135_6_, float p_177135_8_, float p_177135_9_)
	{
		if (p_177135_1_.isWolfWet())
		{
			float f2 = p_177135_1_.getBrightness() * p_177135_1_.getShadingWhileWet(p_177135_9_);
			GlStateManager.color(f2, f2, f2);
		}
		super.doRender(p_177135_1_, p_177135_2_, p_177135_4_, p_177135_6_, p_177135_8_, p_177135_9_);
	}
	protected ResourceLocation getEntityTexture(EntityWolf entity)
	{
		return entity.isAngry() ? (entity.isAntiMob() ? antianrgyWolfTextures : anrgyWolfTextures) : (entity.isAntiMob() ? antiwolfTextures : wolfTextures);
	}
	protected void preRenderCallback(EntityWolf entitylivingbaseIn, float partialTickTime)
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
	public void doRender(EntityWolf entity, double x, double y, double z, float entityYaw, float partialTicks)
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
		else if (!entity.isInvisible())
		{
			this.shadowOpaque = 1F;
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}

	protected boolean isVisible(EntityWolf entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}
}


