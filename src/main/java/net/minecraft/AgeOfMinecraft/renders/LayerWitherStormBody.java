package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStorm;
import net.minecraft.AgeOfMinecraft.models.ModelCommandBlockWitherBody;
import net.minecraft.AgeOfMinecraft.models.ModelWitherStormBody;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerWitherStormBody
implements LayerRenderer<EntityWitherStorm>
{
	private static final ResourceLocation withersmallTextures = new ResourceLocation("ageofminecraft", "textures/entities/wither_storm_hue_small.png");
	private static final ResourceLocation witherTextures = new ResourceLocation("ageofminecraft", "textures/entities/wither_storm_hue.png");
	private final ModelBase modelmatter = new ModelWitherStormBody();
	private final ModelBase modelmaterial = new ModelCommandBlockWitherBody();
	private final RenderWitherStorm witherRenderer;
	public LayerWitherStormBody(RenderWitherStorm p_i46105_1_)
	{
		this.witherRenderer = p_i46105_1_;
	}
	public void func_177214_a(EntityWitherStorm p_177214_1_, float p_177214_2_, float p_177214_3_, float p_177214_4_, float p_177214_5_, float p_177214_6_, float p_177214_7_, float p_177214_8_)
	{
		if (p_177214_1_.doesntContainACommandBlock())
		{
			GlStateManager.pushMatrix();
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(3F, 3F, 3F);
			GlStateManager.translate(0.0F, 0.5F, 0.5F);
			GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);
			this.witherRenderer.bindTexture(withersmallTextures);
			this.modelmaterial.render(p_177214_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
			GlStateManager.popMatrix();
		}
		else
		{
			float size = (p_177214_1_.getMaxHealth() * 0.000025F);
			GlStateManager.scale(0.01F + size, 0.01F + size, 0.01F + size);
			GlStateManager.translate(0.0F, -12F - (p_177214_1_.getMaxHealth() * 0.0000005F), 0.0F + (p_177214_1_.getMaxHealth() * 0.000000625F));
			if (p_177214_1_.getSize() > 50000)
			GlStateManager.translate(0.0F, 4F + (p_177214_1_.getMaxHealth() * 0.0000005F), 0.5F + (p_177214_1_.getMaxHealth() * 0.00001F));
			if (p_177214_1_.getSize() > 250000)
			GlStateManager.translate(0.0F, 2F - (p_177214_1_.getMaxHealth() * 0.0000001F), 1.0F - (p_177214_1_.getMaxHealth() * 0.00001F));
			GlStateManager.rotate((p_177214_1_.isSneaking() ? -60F : -20.0F), 1.0F, 0.0F, 0.0F);
			if (p_177214_1_.getMaxHealth() >= 300000F)
			GlStateManager.scale(1F + (MathHelper.cos(p_177214_1_.ticksExisted + p_177214_4_) * 0.01F), 1F + (MathHelper.cos(p_177214_1_.ticksExisted + p_177214_4_) * 0.01F), 1F + (MathHelper.sin(p_177214_1_.ticksExisted + p_177214_4_) * 0.01F));
			this.witherRenderer.bindTexture(witherTextures);
			this.modelmatter.render(p_177214_1_, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		}
	}
	public boolean shouldCombineTextures()
	{
		return true;
	}
	public void doRenderLayer(EntityWitherStorm p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177214_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}