package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySnowman;
import net.minecraft.AgeOfMinecraft.models.ModelSnowMan;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderSnowMan
extends RenderLiving<EntitySnowman>
{
	private static final ResourceLocation snowManTextures = new ResourceLocation("textures/entity/snowman.png");
	private static final ResourceLocation antisnowManTextures = new ResourceLocation("ageofminecraft", "textures/entities/anti/snowman.png");

	private static ModelSnowMan regularmodel = new ModelSnowMan();
	
	public RenderSnowMan(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 0.5F);
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		this.addLayer(new LayerSnowmanHead(regularmodel.head));
		this.addLayer(new LayerLearningBook(this));
		this.addLayer(new LayerMobCape(this));
	}
	protected void applyRotations(EntitySnowman entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
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
				GlStateManager.translate(f * 0.25F, 0.0F, 0.0F);
			
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
	protected ResourceLocation getEntityTexture(EntitySnowman entity)
	{
		return (entity.isAntiMob() ? antisnowManTextures : snowManTextures);
	}
	protected void preRenderCallback(EntitySnowman entitylivingbaseIn, float partialTickTime)
	{
		this.mainModel = regularmodel;
		
		if (!entitylivingbaseIn.getCurrentBook().isEmpty())
		{
			regularmodel.rightHand.rotateAngleY = entitylivingbaseIn.bookSpread - 1F;
			regularmodel.leftHand.rotateAngleY = -entitylivingbaseIn.bookSpread + 1F;
			regularmodel.rightHand.rotateAngleZ = 0F;
			regularmodel.leftHand.rotateAngleZ = 0F;
			regularmodel.rightHand.rotateAngleX = -1.5F + (0.1F + MathHelper.sin((float)entitylivingbaseIn.ticksExisted * 0.1F) * 0.01F);
			regularmodel.leftHand.rotateAngleX = -1.5F + (0.1F + MathHelper.sin((float)entitylivingbaseIn.ticksExisted * 0.1F) * 0.01F);
		}

			if (entitylivingbaseIn.isChild())
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			if (entitylivingbaseIn.isRiding())
			GlStateManager.translate(0.0F, -1.0F, 0.0F);
		

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
}