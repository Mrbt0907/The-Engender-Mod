package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySlime;
import net.minecraft.AgeOfMinecraft.models.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderSlime
extends RenderLiving<EntitySlime>
{
	private static final ResourceLocation slimeTextures = new ResourceLocation("textures/entity/slime/slime.png");
	private static final ResourceLocation antislimeTextures = new ResourceLocation("ageofminecraft", "textures/entities/anti/slime.png");

	private static ModelSlime regularmodel = new ModelSlime(16);
	
	public RenderSlime(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 0.25F);
		addLayer(new LayerSlimeGel(this));
		this.addLayer(new LayerCustomHeadEngender(regularmodel.slimeBodies));
		this.addLayer(new LayerLearningBook(this));
	}
	public void doRender(EntitySlime entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		this.shadowSize = (0.25F * entity.getSlimeSize());
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	protected void preRenderCallback(EntitySlime entitylivingbaseIn, float partialTickTime)
	{
		this.mainModel = regularmodel;
		
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
	protected void applyRotations(EntitySlime entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
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
	protected ResourceLocation getEntityTexture(EntitySlime entity)
	{
		return (entity.isAntiMob() ? antislimeTextures : slimeTextures);
	}
}


