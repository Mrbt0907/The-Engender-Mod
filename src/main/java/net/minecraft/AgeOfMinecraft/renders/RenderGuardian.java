package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian;
import net.minecraft.AgeOfMinecraft.models.ModelGuardian;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderGuardian
extends RenderLiving<EntityGuardian>
{
	private static final ResourceLocation GUARDIAN_TEXTURE = new ResourceLocation("textures/entity/guardian.png");
	private static final ResourceLocation GUARDIAN_ELDER_TEXTURE = new ResourceLocation("textures/entity/guardian_elder.png");
	private static final ResourceLocation antiGUARDIAN_TEXTURE = new ResourceLocation("ageofminecraft", "textures/entities/anti/guardian.png");
	private static final ResourceLocation antiGUARDIAN_ELDER_TEXTURE = new ResourceLocation("ageofminecraft", "textures/entities/anti/guardian_elder.png");
	private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");

	private static ModelGuardian regularmodel = new ModelGuardian();
	
	public RenderGuardian(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 0.5F);
		addLayer(new LayerArrowCustomSized(this, 0.5F));
		this.addLayer(new LayerCustomHeadEngender(regularmodel.guardianBody));
		this.addLayer(new LayerLearningBook(this));
		this.addLayer(new LayerMobCape(this));
	}
	public boolean shouldRender(EntityGuardian livingEntity, ICamera camera, double camX, double camY, double camZ)
	{
		if (super.shouldRender(livingEntity, camera, camX, camY, camZ))
		{
			return true;
		}
		else
		{
			if (livingEntity.hasTargetedEntity())
			{
				EntityLivingBase entitylivingbase = livingEntity.getTargetedEntity();
				
				if (entitylivingbase != null)
				{
					Vec3d vec3d = this.getPosition(entitylivingbase, (double)entitylivingbase.height * 0.5D, 1.0F);
					Vec3d vec3d1 = this.getPosition(livingEntity, (double)livingEntity.getEyeHeight(), 1.0F);
					
					if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z)))
					{
						return true;
					}
				}
			}

			return false;
		}
	}

	private Vec3d getPosition(EntityLivingBase entityLivingBaseIn, double p_177110_2_, float p_177110_4_)
	{
		double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * (double)p_177110_4_;
		double d1 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * (double)p_177110_4_;
		double d2 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * (double)p_177110_4_;
		return new Vec3d(d0, d1, d2);
	}

	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRenderGuardian(EntityGuardian entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		EntityLivingBase entitylivingbase = entity.getTargetedEntity();
		
		if (entitylivingbase != null)
		{
			float f = entity.getAttackAnimationScale(partialTicks);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder vertexbuffer = tessellator.getBuffer();
			this.bindTexture(GUARDIAN_BEAM_TEXTURE);
			GlStateManager.glTexParameteri(3553, 10242, 10497);
			GlStateManager.glTexParameteri(3553, 10243, 10497);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			float f2 = (float)entity.world.getTotalWorldTime() + partialTicks;
			float f3 = f2 * 0.5F % 1.0F;
			float f4 = entity.getEyeHeight();
			GlStateManager.pushMatrix();
			GlStateManager.translate((float)x, (float)y + f4, (float)z);
			Vec3d vec3d = this.getPosition(entitylivingbase, (double)entitylivingbase.getEyeHeight(), partialTicks);
			Vec3d vec3d1 = this.getPosition(entity, (double)f4, partialTicks);
			Vec3d vec3d2 = vec3d.subtract(vec3d1);
			double d0 = vec3d2.lengthVector() + 1.0D;
			vec3d2 = vec3d2.normalize();
			float f5 = (float)Math.acos(vec3d2.y);
			float f6 = (float)Math.atan2(vec3d2.z, vec3d2.x);
			GlStateManager.rotate((((float)Math.PI / 2F) + -f6) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(f5 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
			double d1 = (double)f2 * 0.05D * -1.5D;
			vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			float f7 = f * f;
			int j = 64 + (int)(f7 * 191.0F);
			int k = 32 + (int)(f7 * 191.0F);
			int l = 128 - (int)(f7 * 64.0F);
			double d4 = 0.0D + Math.cos(d1 + 2.356194490192345D) * 0.282D;
			double d5 = 0.0D + Math.sin(d1 + 2.356194490192345D) * 0.282D;
			double d6 = 0.0D + Math.cos(d1 + (Math.PI / 4D)) * 0.282D;
			double d7 = 0.0D + Math.sin(d1 + (Math.PI / 4D)) * 0.282D;
			double d8 = 0.0D + Math.cos(d1 + 3.9269908169872414D) * 0.282D;
			double d9 = 0.0D + Math.sin(d1 + 3.9269908169872414D) * 0.282D;
			double d10 = 0.0D + Math.cos(d1 + 5.497787143782138D) * 0.282D;
			double d11 = 0.0D + Math.sin(d1 + 5.497787143782138D) * 0.282D;
			double d12 = 0.0D + Math.cos(d1 + Math.PI) * 0.2D;
			double d13 = 0.0D + Math.sin(d1 + Math.PI) * 0.2D;
			double d14 = 0.0D + Math.cos(d1 + 0.0D) * 0.2D;
			double d15 = 0.0D + Math.sin(d1 + 0.0D) * 0.2D;
			double d16 = 0.0D + Math.cos(d1 + (Math.PI / 2D)) * 0.2D;
			double d17 = 0.0D + Math.sin(d1 + (Math.PI / 2D)) * 0.2D;
			double d18 = 0.0D + Math.cos(d1 + (Math.PI * 3D / 2D)) * 0.2D;
			double d19 = 0.0D + Math.sin(d1 + (Math.PI * 3D / 2D)) * 0.2D;
			double d22 = (double)(-1.0F + f3);
			double d23 = d0 * 2.5D + d22;
			vertexbuffer.pos(d12, d0, d13).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d12, 0.0D, d13).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d16, d0, d17).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d16, 0.0D, d17).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
			double d24 = 0.0D;
			
			if (entity.ticksExisted % 2 == 0)
			{
				d24 = 0.5D;
			}
			GlStateManager.scale(entity.getFittness(), 1F, entity.getFittness());
			if (entity.isHero() && entity.getSpecialAttackTimer() <= 0)
			GlStateManager.scale(4F, 1F, 4F);
			vertexbuffer.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
			vertexbuffer.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
	}

	protected void applyRotations(EntityGuardian entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
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
				GlStateManager.translate(f * 0.5F, -f * 0.5F, 0.0F);
			
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
	protected void preRenderCallback(EntityGuardian entitylivingbaseIn, float partialTickTime)
	{
		this.mainModel = regularmodel;
		
			if (entitylivingbaseIn.isRiding())
			GlStateManager.translate(0.0F, -0.5F, 0.0F);
			if (entitylivingbaseIn.isChild())
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 1F, 0.0F);
			if (entitylivingbaseIn.isElder())
			{
				GlStateManager.scale(2.35F, 2.35F, 2.35F);
				if (entitylivingbaseIn.isEntityAlive() && !entitylivingbaseIn.isAirBorne && entitylivingbaseIn.onGround && !entitylivingbaseIn.isSneaking() && (!entitylivingbaseIn.isInWater()) && (!entitylivingbaseIn.isInLava()))
				GlStateManager.translate(0.0F, 0.3F, 0.0F);
				else
				GlStateManager.translate(0.0F, 0.6F, 0.0F);
			}
		
		double fit = entitylivingbaseIn.getFittness();
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
	protected ResourceLocation getEntityTexture(EntityGuardian entity)
	{
		return (entity.isElder() ? (entity.isAntiMob() ? antiGUARDIAN_ELDER_TEXTURE : GUARDIAN_ELDER_TEXTURE) : (entity.isAntiMob() ? antiGUARDIAN_TEXTURE : GUARDIAN_TEXTURE));
	}
	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRender(EntityGuardian entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (entity.getGhostTime() > 0)
		{
			Vec3d[] avec3d = entity.getRenderLocations(partialTicks);
			float f = this.handleRotationFloat(entity, partialTicks);
			
			for (int i = 0; i < avec3d.length; ++i)
			{
				this.doRenderGuardian(entity, x + avec3d[i].x + (double)MathHelper.cos((float)i + f * 0.5F) * 0.025D, y + avec3d[i].y + (double)MathHelper.cos((float)i + f * 0.75F) * 0.0125D, z + avec3d[i].z + (double)MathHelper.cos((float)i + f * 0.7F) * 0.025D, entityYaw, partialTicks);
				super.doRender(entity, x + avec3d[i].x + (double)MathHelper.cos((float)i + f * 0.5F) * 0.025D, y + avec3d[i].y + (double)MathHelper.cos((float)i + f * 0.75F) * 0.0125D, z + avec3d[i].z + (double)MathHelper.cos((float)i + f * 0.7F) * 0.025D, entityYaw, partialTicks);
			}
			this.shadowOpaque = 0F;
		}
		else
		{
			this.shadowOpaque = 1F;
			this.doRenderGuardian(entity, x, y, z, entityYaw, partialTicks);
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}

	protected boolean isVisible(EntityGuardian entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}
}


