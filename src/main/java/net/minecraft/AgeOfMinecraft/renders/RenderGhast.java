package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast;
import net.minecraft.AgeOfMinecraft.models.ModelGhast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderGhast
extends RenderLiving<EntityGhast>
{
	private static final ResourceLocation ghastTextures = new ResourceLocation("textures/entity/ghast/ghast.png");
	private static final ResourceLocation ghastShootingTextures = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");
	private static final ResourceLocation antighastTextures = new ResourceLocation("ageofminecraft", "textures/entities/anti/ghast.png");
	private static final ResourceLocation antighastShootingTextures = new ResourceLocation("ageofminecraft", "textures/entities/anti/ghast_shooting.png");

	private static ModelGhast regularmodel = new ModelGhast();
	
	public RenderGhast(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 3.0F);
		addLayer(new LayerArrowCustomSized(this, 0.2F));
		addLayer(new LayerGhastEyes(this));
		this.addLayer(new LayerCustomHeadEngender(regularmodel.body));
		this.addLayer(new LayerLearningBook(this));
		this.addLayer(new LayerMobCape(this));
	}
	protected ResourceLocation getEntityTexture(EntityGhast entity)
	{
		return  (entity.isAttacking() ? (entity.isAntiMob() ? antighastShootingTextures : ghastShootingTextures) : (entity.isAntiMob() ? antighastTextures : ghastTextures));
	}
	protected void applyRotations(EntityGhast entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
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
				GlStateManager.translate(f * 2.25F, -f * 2.25F, 0.0F);
			
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

	protected void preRenderCallback(EntityGhast entitylivingbaseIn, float partialTickTime)
	{
		this.mainModel = regularmodel;
		this.shadowSize = 3.0F;
		
			if (entitylivingbaseIn.isBeingRidden() && entitylivingbaseIn.getControllingPassenger() != null && entitylivingbaseIn.getControllingPassenger() instanceof EntityPlayer)
			entitylivingbaseIn.setInvisible(true);
			else
			entitylivingbaseIn.setInvisible(entitylivingbaseIn.isInvisible());
			
			float f = 1.0F;
			float f1 = (8.0F + f) / 2.0F;
			float f2 = (8.0F + 1.0F / f) / 2.0F;
			GlStateManager.scale(f2, f1, f2);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.translate(0.0F, -0.05F, 0.0F);
			if (entitylivingbaseIn.isRiding())
			GlStateManager.translate(0.0F, -0.125F, 0.0F);
			if (entitylivingbaseIn.isChild())
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
		
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
	public void doRender(EntityGhast entity, double x, double y, double z, float entityYaw, float partialTicks)
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

	protected boolean isVisible(EntityGhast entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}
}