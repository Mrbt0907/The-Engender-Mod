package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityGhasther;
import net.minecraft.AgeOfMinecraft.models.ModelGhasther;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderGhasther
extends RenderLiving<EntityGhasther>
{
	private static final ResourceLocation ghastTextures = new ResourceLocation("ageofminecraft", "textures/entities/giant_ghast.png");
	private static final ResourceLocation ghastShootingTextures = new ResourceLocation("ageofminecraft", "textures/entities/giant_ghast_shooting.png");
	private static ModelGhasther regularmodel = new ModelGhasther();
	
	public RenderGhasther(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 6.0F);
		addLayer(new LayerArrowCustomSized(this, 0.1F));
		this.addLayer(new LayerCustomHeadEngender(regularmodel.body));
		this.addLayer(new LayerLearningBook(this));
		this.addLayer(new LayerMobCape(this));
	}
	protected ResourceLocation getEntityTexture(EntityGhasther entity)
	{
		return entity.isAttacking() ? ghastShootingTextures : ghastTextures;
	}

	protected void preRenderCallback(EntityGhasther entitylivingbaseIn, float partialTickTime)
	{
		float fit = entitylivingbaseIn.getFittness();
		GlStateManager.scale(fit, fit, fit);
		
		float f = 1.0F;
		float f1 = (8.0F + f);
		float f2 = (8.0F + 1.0F / f);
		GlStateManager.scale(f2, f1, f2);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.translate(0.0F, -0.05F, 0.0F);
		if (entitylivingbaseIn.isRiding())
		GlStateManager.translate(0.0F, -0.125F, 0.0F);
		if (entitylivingbaseIn.isChild())
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		
		if (entitylivingbaseIn.isHero())
		GlStateManager.scale(1.05F, 1.05F, 1.05F);
		
		if (entitylivingbaseIn.ticksExisted <= 81 && entitylivingbaseIn.ticksExisted > 0)
		{
			float f5 = (entitylivingbaseIn.ticksExisted + partialTickTime - 1.0F) / 80.0F * 1.6F;
			f5 = MathHelper.sqrt(f5);
			if (f5 < -2.0F) f5 = -2F;
			GlStateManager.translate(0.0F, -f5, 0.0F);
			GlStateManager.translate(0.0F, 1.25F, 0.0F);
		}
	}
}