package net.minecraft.AgeOfMinecraft.renders;
import java.util.Random;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.AgeOfMinecraft.models.ModelEnderDragon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderEnderDragon extends RenderLiving<EntityEnderDragon>
{
	private static final ResourceLocation ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
	public static final ResourceLocation ENDERCRYSTAL_BEAM_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
	private static final ResourceLocation DRAGON_EXPLODING_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
	private static final ResourceLocation DRAGON_TEXTURES = new ResourceLocation("textures/entity/enderdragon/dragon.png");
	private final ModelBase modelEnderCrystalNoBase = new ModelEnderCrystal(0.0F, false);
	protected ModelEnderDragon modelDragon;
	public RenderEnderDragon(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelEnderDragon(0.0F), 8.0F);
		this.modelDragon = ((ModelEnderDragon)this.mainModel);
		addLayer(new LayerEnderDragonEyes(this));
		addLayer(new LayerEnderDragonDeath());
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		this.addLayer(new LayerLearningBook(this));
	}
	protected void applyRotations(EntityEnderDragon bat, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		float f = (float)bat.getMovementOffsets(7, partialTicks)[0];
		float f1 = (float)(bat.getMovementOffsets(5, partialTicks)[1] - bat.getMovementOffsets(10, partialTicks)[1]);
		GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(f1 * 10.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, 0.0F, 1.0F);
	}
	protected void renderModel(EntityEnderDragon entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
	{
		boolean flag = this.isVisible(entitylivingbaseIn);
		boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().player);
		
		if (flag || flag1)
		{
			if (flag1)
			{
				GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}
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
			if (flag1)
			{
				GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			}
		}
	}
	protected void preRenderCallback(EntityEnderDragon entitylivingbaseIn, float partialTickTime)
	{
		float fit = entitylivingbaseIn.getFittness();
		GlStateManager.scale(fit, fit, fit);
		if (entitylivingbaseIn.isHero())
		GlStateManager.scale(1.05F, 1.05F, 1.05F);
		if (entitylivingbaseIn.deathTicks > 0)
		GlStateManager.rotate(entitylivingbaseIn.deathTicks * 10, 0.0F, 1.0F, 0.0F);
	}
	public void doRender(EntityEnderDragon entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		if (entity.isCarryingCrystal())
		{
			GlStateManager.pushMatrix();
			float f = (float)entity.innerRotation + partialTicks;
			float fl1 = entity.getJukeboxToDanceTo() != null ? MathHelper.sin(f * 0.1F) * 10F: ((float)entity.motionX * 10F);
			float fl2 = entity.getJukeboxToDanceTo() != null ? MathHelper.sin(f * 0.3F) * 5F : ((float)entity.motionY * 5F);
			float fl3 = entity.getJukeboxToDanceTo() != null ? MathHelper.cos(f * 0.1F) * 10F : ((float)entity.motionZ * 10F);
			GlStateManager.translate((float)x - fl1, (float)y + 10D - fl2, (float)z - fl3);
			this.bindTexture(ENDER_CRYSTAL_TEXTURES);
			float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
			f1 = f1 * f1 + f1;
			if (this.renderOutlines)
			{
				GlStateManager.enableColorMaterial();
				GlStateManager.enableOutlineMode(this.getTeamColor(entity));
			}
			this.modelEnderCrystalNoBase.render(entity, 0.0F, f * 3.0F, f1 * 0.2F, 0.0F, 0.0F, 0.0625F);
			if (this.renderOutlines)
			{
				GlStateManager.disableOutlineMode();
				GlStateManager.disableColorMaterial();
			}
			if (entity.getJukeboxToDanceTo() != null)
			{
				GlStateManager.pushMatrix();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder vertexbuffer = tessellator.getBuffer();
				RenderHelper.disableStandardItemLighting();
				float u = f / 250F;
				float u1 = 0.0F;
				
				if (f > 0.9F)
				{
					u1 = (u - 0.9F) / 0.2F;
				}

				Random random = new Random(432L);
				GlStateManager.disableTexture2D();
				GlStateManager.shadeModel(7425);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
				GlStateManager.disableAlpha();
				GlStateManager.enableCull();
				GlStateManager.depthMask(false);
				GlStateManager.translate((float)x - fl1, (float)y + 10D - fl2 + f1, (float)z - fl3);
				for (int i = 0; (float)i < (u + u * u) / 2.0F * 30.0F; ++i)
				{
					GlStateManager.rotate(random.nextFloat() * 720.0F, 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate(random.nextFloat() * 720.0F, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(random.nextFloat() * 720.0F, 0.0F, 0.0F, 1.0F);
					GlStateManager.rotate(random.nextFloat() * 720.0F, 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate(random.nextFloat() * 720.0F, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(random.nextFloat() * 720.0F + u * 180.0F, 0.0F, 0.0F, 1.0F);
					float f2 = random.nextFloat() * 20.0F + 5.0F + u1 * 10.0F;
					float f3 = random.nextFloat() * 2.0F + 1.0F + u1 * 2.0F;
					vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
					vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(random.nextInt(255), random.nextInt(255), random.nextInt(255), (int)(255.0F * (1.0F - u1))).endVertex();
					vertexbuffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0).endVertex();
					vertexbuffer.pos(0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0).endVertex();
					vertexbuffer.pos(0.0D, (double)f2, (double)(1.0F * f3)).color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0).endVertex();
					vertexbuffer.pos(-0.866D * (double)f3, (double)f2, (double)(-0.5F * f3)).color(random.nextInt(255), random.nextInt(255), random.nextInt(255), 0).endVertex();
					tessellator.draw();
				}
				GlStateManager.depthMask(true);
				GlStateManager.disableCull();
				GlStateManager.disableBlend();
				GlStateManager.shadeModel(7424);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableTexture2D();
				GlStateManager.enableAlpha();
				RenderHelper.enableStandardItemLighting();
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();
			this.bindTexture(ENDERCRYSTAL_BEAM_TEXTURES);
			float f2 = (float)entity.posX + fl1;
			float f3 = (float)entity.posY - 10F + fl2;
			float f4 = (float)entity.posZ + fl3;
			double d0 = (double)f2 - (entity.posX + fl1);
			double d1 = (double)f3 - (entity.posY - 10D + fl2);
			double d2 = (double)f4 - (entity.posZ + fl3);
			if (entity.getHealth() < entity.getMaxHealth())
			func_188325_a(x + d0, y - 0.3D + (double)(f1 * 0.4F) + d1, z + d2, partialTicks, (double)f2, (double)f3, (double)f4, entity.innerRotation, entity.posX, entity.posY, entity.posZ);
		}
	}
	public static void func_188325_a(double p_188325_0_, double p_188325_2_, double p_188325_4_, float p_188325_6_, double p_188325_7_, double p_188325_9_, double p_188325_11_, int p_188325_13_, double p_188325_14_, double p_188325_16_, double p_188325_18_)
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
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.shadeModel(7425);
		float f5 = 0.0F - ((float)p_188325_13_ + p_188325_6_) * 0.01F;
		float f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 32.0F - ((float)p_188325_13_ + p_188325_6_) * 0.01F;
		vertexbuffer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
		for (int j = 0; j <= 8; ++j)
		{
			float f7 = MathHelper.sin((float)(j % 8) * ((float)Math.PI * 2F) / 8F) * 0.5F;
			float f8 = MathHelper.cos((float)(j % 8) * ((float)Math.PI * 2F) / 8F) * 0.5F;
			float f9 = (float)(j % 8) / 8F;
			vertexbuffer.pos((double)(f7 * 0.25F), (double)(f8 * 0.25F), 0.0D).tex((double)f9, (double)f5).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos((double)f7, (double)f8, (double)f4).tex((double)f9, (double)f6).color(255, 255, 255, 255).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableCull();
		GlStateManager.shadeModel(7424);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
	protected ResourceLocation getEntityTexture(EntityEnderDragon entity)
	{
		return DRAGON_TEXTURES;
	}
}