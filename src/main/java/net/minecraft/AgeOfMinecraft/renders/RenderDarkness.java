package net.minecraft.AgeOfMinecraft.renders;
import net.endermanofdoom.mac.util.math.Maths.Vec3;
import net.minecraft.AgeOfMinecraft.entity.cameos.Darkness.EntityDarkness;
import net.minecraft.AgeOfMinecraft.entity.other.BeamHitbox;
import net.minecraft.AgeOfMinecraft.models.ModelDarkness;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderDarkness extends RenderLiving<EntityDarkness>
{
	public static final ResourceLocation ENDERCRYSTAL_BEAM_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
	private static final ResourceLocation DRAGON_EXPLODING_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
	private static final ResourceLocation DRAGON_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon.png");

	public RenderDarkness(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelDarkness(0.0F), 0.5F);
		this.addLayer(new LayerDarknessEyes(this));
		//this.addLayer(new LayerEnderDragonDeath());
	}

	protected void applyRotations(EntityDarkness entityLiving, float p_77043_2_, float rotationYaw, float partialTicks)
	{
		float f = (float)entityLiving.getMovementOffsets(7, partialTicks)[0];
		float f1 = (float)(entityLiving.getMovementOffsets(5, partialTicks)[1] - entityLiving.getMovementOffsets(10, partialTicks)[1]);
		GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(f1 * 10.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, 0.0F, 1.0F);

		if (entityLiving.deathTime > 0)
		{
			float f2 = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f2 = MathHelper.sqrt(f2);

			if (f2 > 1.0F)
			{
				f2 = 1.0F;
			}

			GlStateManager.rotate(f2 * this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
		}
	}

	/**
	 * Renders the model in RenderLiving
	 */
	protected void renderModel(EntityDarkness entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
	{
		if (entitylivingbaseIn.deathTicks > 0)
		{
			float f = (float)entitylivingbaseIn.deathTicks / 200.0F;
			GlStateManager.depthFunc(515);
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(516, f);
			this.bindTexture(DRAGON_EXPLODING_TEXTURES);
			this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.depthFunc(514);
		}

		this.bindEntityTexture(entitylivingbaseIn);
		this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

		if (entitylivingbaseIn.hurtTime > 0)
		{
			GlStateManager.depthFunc(514);
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 0.0F, 0.0F, 0.5F);
			this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			GlStateManager.depthFunc(515);
		}
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityDarkness entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		if (!entity.endCrystals.isEmpty())
		{
			bindTexture(ENDERCRYSTAL_BEAM_TEXTURES);
			entity.endCrystals.forEach(endCrystal ->
			{
				float f = MathHelper.sin(((float)endCrystal.ticksExisted + partialTicks) * 0.2F) / 2.0F + 0.5F;
				f = (f * f + f) * 0.2F;
				renderCrystalBeams(x, y, z, partialTicks, entity.posX + (entity.prevPosX - entity.posX) * (double)(1.0F - partialTicks), entity.posY + (entity.prevPosY - entity.posY) * (double)(1.0F - partialTicks), entity.posZ + (entity.prevPosZ - entity.posZ) * (double)(1.0F - partialTicks), entity.ticksExisted, endCrystal.posX, (double)f + endCrystal.posY, endCrystal.posZ);
			});
		}
		
		BeamHitbox lesserBeam = entity.getLesserBeam(); 
		if (lesserBeam.hasPosition())
		{
			Vec3 startPos = lesserBeam.getStartPos();
			Vec3 endPos = lesserBeam.getEndPos();
			
			renderLaserBeam(entity, x, y, z, startPos.posX, startPos.posY, startPos.posZ, endPos.posX, endPos.posY, endPos.posZ, lesserBeam.getSize(), partialTicks);
		}
	}
	
	public void renderLaserBeam(EntityDarkness entity, double x, double y, double z, double startX, double startY, double startZ, double endX, double endY, double endZ, double size, float partialTicks)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		this.bindTexture(TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM);
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
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x + (startX - entity.lastTickPosX), (float)y + (startY - entity.lastTickPosY), (float)z + (startZ - entity.lastTickPosZ));
		Vec3d vec3d = new Vec3d(endX, endY, endZ);
		Vec3d vec3d1 = new Vec3d(startX, startY, startZ);
		Vec3d vec3d2 = vec3d.subtract(vec3d1);
		double d0 = vec3d2.lengthVector() + 1.0D;
		vec3d2 = vec3d2.normalize();
		float f5 = (float)Math.acos(vec3d2.y);
		float f6 = (float)Math.atan2(vec3d2.z, vec3d2.x);
		GlStateManager.rotate((((float)Math.PI / 2F) + -f6) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(f5 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
			double d1 = (double)f2 * 0.05D * -1.5D;
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			int j = 255;
			int k = 155;
			int l = 0;
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
			bufferbuilder.pos(d12, d0, d13).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d12, 0.0D, d13).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d16, d0, d17).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d16, 0.0D, d17).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
			double d24 = 0.0D;

			if (entity.ticksExisted % 2 == 0)
			{
				d24 = 0.5D;
			}

			bufferbuilder.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
			bufferbuilder.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		
	}
	
	public static void renderCrystalBeams(double p_188325_0_, double p_188325_2_, double p_188325_4_, float p_188325_6_, double p_188325_7_, double p_188325_9_, double p_188325_11_, int p_188325_13_, double p_188325_14_, double p_188325_16_, double p_188325_18_)
	{
		float f = (float)(p_188325_14_ - p_188325_7_);
		float f1 = (float)(p_188325_16_ - 1.0D - p_188325_9_);
		float f2 = (float)(p_188325_18_ - p_188325_11_);
		float f3 = MathHelper.sqrt(f * f + f2 * f2);
		float f4 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)p_188325_0_, (float)p_188325_2_ + 2.0F, (float)p_188325_4_);
		GlStateManager.rotate((float)(-Math.atan2((double)f2, (double)f)) * (180F / (float)Math.PI) - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float)(-Math.atan2((double)f3, (double)f1)) * (180F / (float)Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.shadeModel(7425);
		float f5 = 0.0F - ((float)p_188325_13_ + p_188325_6_) * 0.01F;
		float f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 32.0F - ((float)p_188325_13_ + p_188325_6_) * 0.01F;
		bufferbuilder.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);

		for (int j = 0; j <= 8; ++j)
		{
			float f7 = MathHelper.sin((float)(j % 8) * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
			float f8 = MathHelper.cos((float)(j % 8) * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
			float f9 = (float)(j % 8) / 8.0F;
			bufferbuilder.pos((double)(f7 * 0.2F), (double)(f8 * 0.2F), 0.0D).tex((double)f9, (double)f5).color(0, 0, 0, 255).endVertex();
			bufferbuilder.pos((double)f7, (double)f8, (double)f4).tex((double)f9, (double)f6).color(255, 255, 255, 255).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableCull();
		GlStateManager.shadeModel(7424);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
	
	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityDarkness entity)
	{
		return DRAGON_TEXTURES;
	}
}