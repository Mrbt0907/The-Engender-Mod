package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityPrisonSlime;
import net.minecraft.AgeOfMinecraft.models.ModelSlime;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderPrisonSlime
extends RenderLiving<EntityPrisonSlime>
{
	private static final ResourceLocation slimeTextures = new ResourceLocation("ageofminecraft", "textures/entities/prison_slime.png");
	private static ModelSlime regularmodel = new ModelSlime(16);
	
	public RenderPrisonSlime(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 0.25F);
		addLayer(new LayerSlimeGel(this));
		this.addLayer(new LayerCustomHeadEngender(regularmodel.slimeBodies));
		this.addLayer(new LayerLearningBook(this));
	}
	protected void preRenderCallback(EntityPrisonSlime entitylivingbaseIn, float partialTickTime)
	{
		this.shadowSize = 0.25F * entitylivingbaseIn.getSlimeSize();
		float f1 = entitylivingbaseIn.getSlimeSize();
		float f2 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f1 * 0.5F + 1.0F);
		float f3 = 1.0F / (f2 + 1.0F);
		
		GlStateManager.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
		if (entitylivingbaseIn.isSneaking())
		GlStateManager.scale(1.25F, 0.75F, 1.25F);
		
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
	protected void applyRotations(EntityPrisonSlime entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		if (entityLiving.getJukeboxToDanceTo() != null)
		GlStateManager.scale(1F + (MathHelper.cos(p_77043_2_ * 1F) * 0.1F), 1F - (MathHelper.cos(p_77043_2_ * 1F) * 0.05F), 1F + (MathHelper.cos(p_77043_2_ * 1F) * 0.1F));
		
		GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
		
		if (entityLiving.deathTime > 0)
		{
			float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = MathHelper.sqrt(f);
			
			if (f > 1.0F)
			{
				f = 1.0F;
			}

			GlStateManager.rotate(f * this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
		}
		else
		{
			String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());
			
			if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)))
			{
				GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}
	protected ResourceLocation getEntityTexture(EntityPrisonSlime entity)
	{
		return slimeTextures;
	}
	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRender(EntityPrisonSlime entity, double x, double y, double z, float entityYaw, float partialTicks)
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

	protected boolean isVisible(EntityPrisonSlime entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}

	@SideOnly(Side.CLIENT)
	
	public class LayerSlimeGel implements LayerRenderer<EntityPrisonSlime>
	{
		private final ResourceLocation slimeTextures = new ResourceLocation("ageofminecraft", "textures/entities/prison_slime.png");
		private final RenderPrisonSlime slimeRenderer;
		private ModelBase slimeModel = new ModelSlime(0);
		public LayerSlimeGel(RenderPrisonSlime p_i46111_1_)
		{
			this.slimeRenderer = p_i46111_1_;
		}
		public void doRenderLayer(EntityPrisonSlime p_177159_1_, float p_177159_2_, float p_177159_3_, float p_177159_4_, float p_177159_5_, float p_177159_6_, float p_177159_7_, float p_177159_8_)
		{
			if (!p_177159_1_.isInvisible())
			{
				GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
				GlStateManager.enableNormalize();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(770, 771);
				this.slimeRenderer.bindTexture(slimeTextures);
				this.slimeModel.setModelAttributes(this.slimeRenderer.getMainModel());
				this.slimeModel.render(p_177159_1_, p_177159_2_, p_177159_3_, p_177159_5_, p_177159_6_, p_177159_7_, p_177159_8_);
				GlStateManager.disableBlend();
				GlStateManager.disableNormalize();
			}
		}
		public boolean shouldCombineTextures()
		{
			return true;
		}
		//public void doRenderLayer(EntityPrisonSlime p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
		//{
			//doRenderLayer(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
		//}
	}
}