package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerEndermanEyes implements net.minecraft.client.renderer.entity.layers.LayerRenderer<EntityLivingBase>
{
	private static final ResourceLocation RES_ENDERMAN_EYES = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
	private static final ResourceLocation antiRES_ENDERMAN_EYES = new ResourceLocation("ageofminecraft", "textures/entities/anti/enderman_eyes.png");
	private final RenderEnderman endermanRenderer;
	public LayerEndermanEyes(RenderEnderman p_i46117_1_)
	{
		this.endermanRenderer = p_i46117_1_;
	}
	public void func_177201_a(EntityEnderman p_177201_1_, float p_177201_2_, float p_177201_3_, float p_177201_4_, float p_177201_5_, float p_177201_6_, float p_177201_7_, float p_177201_8_)
	{
		if (p_177201_1_.isEntityAlive())
		{
			this.endermanRenderer.bindTexture(p_177201_1_.canDodgeAllAttacks() ? new ResourceLocation("textures/entity/wither/wither_invulnerable.png") : (p_177201_1_.isAntiMob() ? antiRES_ENDERMAN_EYES : RES_ENDERMAN_EYES));
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.blendFunc(1, 1);
			GlStateManager.disableLighting();
			if (p_177201_1_.canDodgeAllAttacks())
			{
				GlStateManager.matrixMode(5890);
				GlStateManager.loadIdentity();
				float f7 = p_177201_1_.ticksExisted + p_177201_4_;
				float f8 = MathHelper.cos(f7 * 0.02F) * 3.0F;
				float f9 = f7 * 0.01F;
				GlStateManager.translate(f8, f9, 0.0F);
				GlStateManager.matrixMode(5888);
			}
			if (p_177201_1_.isInvisible())
			{
				GlStateManager.depthMask(false);
			}
			else
			{
				GlStateManager.depthMask(true);
			}
			int c0 = 15728880;
			int i = c0 % 65536;
			int j = c0 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
			GlStateManager.enableLighting();
			if (p_177201_1_.canDodgeAllAttacks())
				GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
			else
			GlStateManager.color(1.0F, p_177201_1_.andr ? 0.5F : 1.0F, p_177201_1_.andr ? 0.5F : 1.0F, 1.0F);
			this.endermanRenderer.getMainModel().render(p_177201_1_, p_177201_2_, p_177201_3_, p_177201_5_, p_177201_6_, p_177201_7_, p_177201_8_);
			this.endermanRenderer.setLightmap(p_177201_1_);
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
			if (p_177201_1_.canDodgeAllAttacks())
			{
				GlStateManager.matrixMode(5890);
				GlStateManager.loadIdentity();
				GlStateManager.matrixMode(5888);
			}
		}
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177201_a((EntityEnderman)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}