package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityIceGolem;
import net.minecraft.AgeOfMinecraft.models.ModelIceGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderIceGolem extends RenderLiving<EntityIceGolem>
{
	private static final ResourceLocation iceGolemTextures = new ResourceLocation("ageofminecraft", "textures/entities/ice_golem.png");
	private static ModelIceGolem regularmodel = new ModelIceGolem();
	
	public RenderIceGolem(RenderManager p_i46133_1_)
	{
		super(p_i46133_1_, regularmodel, 0.75F);
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		this.addLayer(new LayerSnowmanHead(regularmodel.ironGolemHead));
		this.addLayer(new LayerLearningBook(this));
		this.addLayer(new LayerMobCape(this));
	}
	protected ResourceLocation getEntityTexture(EntityIceGolem entity)
	{
		return iceGolemTextures;
	}

	protected void preRenderCallback(EntityIceGolem entitylivingbaseIn, float partialTickTime)
	{
		if (entitylivingbaseIn.isChild())
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		
		if (entitylivingbaseIn.isRiding())
		GlStateManager.translate(0.0F, -0.825F, 0.0F);
		
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

	protected void applyRotations(EntityIceGolem entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
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
		if (entityLiving.limbSwingAmount >= 0.01D)
		{
			float f3 = 13.0F;
			float f4 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
			float f5 = (Math.abs(f4 % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
			GlStateManager.rotate(-7.0F * f5, 0.0F, 0.0F, 1.0F);
		}
	}
	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRender(EntityIceGolem entity, double x, double y, double z, float entityYaw, float partialTicks)
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

	protected boolean isVisible(EntityIceGolem entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}
	@SideOnly(Side.CLIENT)
	
	public class LayerSnowmanHead implements LayerRenderer<EntityIceGolem>
	{
		private final ModelRenderer snowManRenderer;
		
		public LayerSnowmanHead(ModelRenderer snowManRendererIn)
		{
			this.snowManRenderer = snowManRendererIn;
		}

		public void doRenderLayer(EntityIceGolem entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
		{
			if (!entitylivingbaseIn.isInvisible())
			{
				GlStateManager.pushMatrix();
				this.snowManRenderer.postRender(0.0625F);
				float f = 0.55F;
				GlStateManager.translate(0.0F, -0.25F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.scale(f, -f, -f);
				Minecraft.getMinecraft().getItemRenderer().renderItem(entitylivingbaseIn, new ItemStack(Blocks.PUMPKIN, 1), ItemCameraTransforms.TransformType.HEAD);
				GlStateManager.popMatrix();
			}
		}

		public boolean shouldCombineTextures()
		{
			return true;
		}
	}
}