package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither;
import net.minecraft.AgeOfMinecraft.models.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderWither
extends RenderLiving<EntityWither>
{
	private static final ResourceLocation invulnerableWitherTextures = new ResourceLocation("textures/entity/wither/wither_invulnerable.png");
	private static final ResourceLocation witherTextures = new ResourceLocation("textures/entity/wither/wither.png");
	private static ModelWither regularmodel = new ModelWither(0.0F);
	
	public RenderWither(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 1.0F);
		addLayer(new LayerWitherAura(this));
		addLayer(new LayerArrowCustomSized(this, 0.5F));
		this.addLayer(new LayerCustomHeadEngender(regularmodel.heads[0]));
		this.addLayer(new LayerLearningBook(this));
		this.addLayer(new LayerMobCape(this));
	}
	protected ResourceLocation getEntityTexture(EntityWither entity)
	{
		int i = entity.getInvulTime();
		return (i > 0) && ((i > 80) || (i / 5 % 2 != 1)) ? invulnerableWitherTextures : witherTextures;
	}
	protected void applyRotations(EntityWither entitylivingbaseIn, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		if (!entitylivingbaseIn.onGround && !entitylivingbaseIn.isBeingRidden() && !entitylivingbaseIn.isInvisible())
		GlStateManager.translate(0.0F, MathHelper.cos(p_77043_2_ * 0.2F) * 0.2F, 0.0F);
		super.applyRotations(entitylivingbaseIn, p_77043_2_, p_77043_3_, partialTicks);
	}
	protected void preRenderCallback(EntityWither entitylivingbaseIn, float partialTickTime)
	{
		float fit = entitylivingbaseIn.getFittness();
		GlStateManager.scale(fit, fit, fit);
		
		float f = 2.0F;
		int i = entitylivingbaseIn.getInvulTime();
		if (i > 0)
		{
			f -= (i - partialTickTime) / 220.0F * 0.5F;
		}
		GlStateManager.scale(f, f, f);
		GlStateManager.translate(0.0F, 0.1F, 0.0F);
		if (entitylivingbaseIn.isSneaking() || (entitylivingbaseIn.getRamTime() < 180 && entitylivingbaseIn.getRamTime() > 100))
		{
			GlStateManager.translate(0.0F, 0.5F, 0.0F);
		}
		if (entitylivingbaseIn.isHero())
		{
			GlStateManager.scale(1.05F, 1.05F, 1.05F);
		}

		float f1 = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
		float f11 = 1.0F + MathHelper.sin(f1 * 100.0F) * f1 * 0.01F;
		f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
		f1 *= f1;
		f1 *= f1;
		float f2 = (1.0F + f1 * 0.8F) * f11;
		float f3 = (1.0F + f1 * 0.4F) / f11;
		GlStateManager.scale(f2, f3, f2);
	}

	protected int getColorMultiplier(EntityWither entitylivingbaseIn, float lightBrightness, float partialTickTime)
	{
		float f = entitylivingbaseIn.getCreeperFlashIntensity(partialTickTime);
		
		if ((int)(f * 10.0F) % 2 == 0)
		{
			return 0;
		}
		else
		{
			int i = (int)(f * 0.2F * 255.0F);
			i = MathHelper.clamp(i, 0, 255);
			return i << 24 | 822083583;
		}
	}
}


