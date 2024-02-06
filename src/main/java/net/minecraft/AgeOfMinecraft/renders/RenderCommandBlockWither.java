package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityCommandBlockWither;
import net.minecraft.AgeOfMinecraft.models.ModelCommandBlockWither;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderCommandBlockWither
extends RenderLiving<EntityCommandBlockWither>
{
	public static final ResourceLocation enderDragonCrystalBeamTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal_beam.png");
	private static final ResourceLocation witherTextures = new ResourceLocation("ageofminecraft", "textures/entities/command_block_wither.png");
	private static final ResourceLocation witherStormTextures = new ResourceLocation("ageofminecraft", "textures/entities/command_block_wither_cycloptic.png");
	public RenderCommandBlockWither(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelCommandBlockWither(), 1.0F);
		addLayer(new LayerArrowCustomSized(this, 0.5F));
		addLayer(new LayerWitherBody(this));
		addLayer(new LayerWitherTractorBeam(this));
		this.addLayer(new LayerMobCape(this));
		this.addLayer(new LayerCommandBlock(this));
	}
	protected ResourceLocation getEntityTexture(EntityCommandBlockWither entity)
	{
		return entity.getSize() >= 5000 ? witherStormTextures : witherTextures;
	}
	protected void applyRotations(EntityCommandBlockWither entitylivingbaseIn, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		if (!entitylivingbaseIn.onGround && !entitylivingbaseIn.isBeingRidden() && !entitylivingbaseIn.isInvisible())
		GlStateManager.translate(0.0F, MathHelper.cos(p_77043_2_ * 0.2F) * 0.2F, 0.0F);
		super.applyRotations(entitylivingbaseIn, p_77043_2_, p_77043_3_, partialTicks);
	}
	protected void preRenderCallback(EntityCommandBlockWither entitylivingbaseIn, float partialTickTime)
	{
		float f = 2.0F;
		GlStateManager.scale(f, f, f);
		if (entitylivingbaseIn.getSize() >= 1000)
		{
			GlStateManager.translate(0.0F, 0.1F, -0.3F);
		}
		if (entitylivingbaseIn.isSneaking())
		{
			GlStateManager.translate(0.0F, 0.3F, 0.0F);
		}
	}
	public void doRender(EntityCommandBlockWither entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
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
		GlStateManager.rotate((float)-Math.atan2(f2, f) * 57.295776F - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((float)-Math.atan2(f3, f1) * 57.295776F - 90.0F, 1.0F, 0.0F, 0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.shadeModel(7425);
		float f5 = 0.0F - (p_188325_13_ + p_188325_6_) * 0.01F;
		float f6 = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2) / 32.0F - (p_188325_13_ + p_188325_6_) * 0.01F;
		vertexbuffer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
		for (int j = 0; j <= 8; j++)
		{
			float f7 = MathHelper.sin(j % 8 * 6.2831855F / 8.0F) * 0.75F;
			float f8 = MathHelper.cos(j % 8 * 6.2831855F / 8.0F) * 0.75F;
			float f9 = j % 8 / 8.0F;
			vertexbuffer.pos(f7 * 0.2F, f8 * 0.2F, 0.0D).tex(f9, f5).color(0, 0, 0, 255).endVertex();
			vertexbuffer.pos(f7, f8, f4).tex(f9, f6).color(255, 255, 255, 255).endVertex();
		}
		tessellator.draw();
		GlStateManager.enableCull();
		GlStateManager.shadeModel(7424);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}
	@SideOnly(Side.CLIENT)
	
	public class LayerCommandBlock implements LayerRenderer<EntityCommandBlockWither>
	{
		protected final RenderLivingBase<?> livingEntityRenderer;
		
		public LayerCommandBlock(RenderLivingBase<?> livingEntityRendererIn)
		{
			this.livingEntityRenderer = livingEntityRendererIn;
		}

		public void doRenderLayer(EntityCommandBlockWither entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
		{
			ItemStack itemstack = new ItemStack(Blocks.COMMAND_BLOCK);
			
			if (!entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isEntityAlive())
			{
				GlStateManager.pushMatrix();
				this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
				GlStateManager.popMatrix();
			}
		}

		private void renderHeldItem(EntityCommandBlockWither p_188358_1_, ItemStack p_188358_2_, ItemCameraTransforms.TransformType p_188358_3_, EnumHandSide handSide)
		{
			if (p_188358_1_.getSize() < 6000)
			{
				GlStateManager.pushMatrix();
				((ModelCommandBlockWither)this.livingEntityRenderer.getMainModel()).spine.postRender(0.0625F);
				GlStateManager.rotate(100.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-11.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(-43.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.scale(1.40F, 1.40F, 1.40F);
				GlStateManager.translate(0.09F, -0.32F, -0.23F);
				Minecraft.getMinecraft().getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, false);
				GlStateManager.popMatrix();
			}
		}

		protected void translateToHand(EnumHandSide p_191361_1_)
		{
			((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
		}

		public boolean shouldCombineTextures()
		{
			return true;
		}
	}
}