package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySquid;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderSquid
extends RenderLiving<EntitySquid>
{
	private static final ResourceLocation squidTextures = new ResourceLocation("textures/entity/squid.png");
	private static final ResourceLocation antisquidTextures = new ResourceLocation("textures/entity/squid.png");
	public RenderSquid(RenderManager p_i46138_1_)
	{
		super(p_i46138_1_, new ModelSquid(), 0.7F);
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		this.addLayer(new LayerLearningBook(this));
	}
	protected ResourceLocation getEntityTexture(EntitySquid entity)
	{
		return entity.isAntiMob() ? antisquidTextures : squidTextures;
	}
	protected void applyRotations(EntitySquid entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		float f = entityLiving.prevSquidPitch + (entityLiving.squidPitch - entityLiving.prevSquidPitch) * partialTicks;
		float f1 = entityLiving.prevSquidYaw + (entityLiving.squidYaw - entityLiving.prevSquidYaw) * partialTicks;
		GlStateManager.translate(0.0F, 0.5F, 0.0F);
		GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(f, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(0.0F, -1.2F, 0.0F);
		if (entityLiving.isChild())
		GlStateManager.translate(0.0F, 0.6F, 0.0F);
		GlStateManager.rotate(90.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
		
		if (entityLiving.deathTime > 0)
		{
			float f11 = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f11 = MathHelper.sqrt(f11);
			
			if (f11 > 1.0F)
			{
				f11 = 1.0F;
			}

			GlStateManager.rotate(f11 * 90, 0.0F, 1.0F, 0.0F);
		}
		else
		{
			String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());
			
			if (s != null && (s.equals("Dinnerbone") || s.equals("Grumm")))
			{
				GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}

	protected float handleRotationFloat(EntitySquid livingBase, float partialTicks)
	{
		return livingBase.lastTentacleAngle + (livingBase.tentacleAngle - livingBase.lastTentacleAngle) * partialTicks;
	}
	protected void preRenderCallback(EntitySquid entitylivingbaseIn, float partialTickTime)
	{
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
	public void doRender(EntitySquid entity, double x, double y, double z, float entityYaw, float partialTicks)
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

	protected boolean isVisible(EntitySquid entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}
}


