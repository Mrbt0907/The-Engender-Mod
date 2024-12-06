package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityBat;
import net.mrbt0907.ageofminecraft.models.ModelBat;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;
 
@SideOnly(Side.CLIENT)
public class RenderBat extends RenderLiving<EntityBat>
{
	private static final ResourceLocation batTextures = new ResourceLocation("textures/entity/bat.png");
	private static ModelBat regularmodel = new ModelBat();
	
	public RenderBat(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 0.25F);
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		//this.addLayer(new LayerLearningBook(this));
	}
	
	protected ResourceLocation getEntityTexture(EntityBat entity)
	{
		return batTextures;
	}
	
	protected void preRenderCallback(EntityBat entitylivingbaseIn, float partialTickTime)
	{
		this.mainModel = regularmodel;
		
        GlStateManager.scale(0.35F, 0.35F, 0.35F);
		
		//if (entitylivingbaseIn.isHero())
		//GlStateManager.scale(1.05F, 1.05F, 1.05F);
		
		//if (!entitylivingbaseIn.onGround)
		//GlStateManager.rotate(entitylivingbaseIn.prevRotationPitchFalling + (entitylivingbaseIn.rotationPitchFalling - entitylivingbaseIn.prevRotationPitchFalling) * 2F - 1F, 1F, 0F, 0F);
		
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
	
	protected void applyRotations(EntityBat bat, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		if (!bat.getIsBatHanging())
			GlStateManager.translate(0.0F, Maths.fastCos((bat.isAIDisabled() ? 1 : p_77043_2_) * 0.3F) * 0.1F, 0.0F);
		else
			GlStateManager.translate(0.0F, -0.1F, 0.0F);
		super.applyRotations(bat, p_77043_2_, p_77043_3_, partialTicks);
	}
	
	public void doRender(EntityBat entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		shadowOpaque = 1F;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	protected boolean isVisible(EntityBat entity)
	{
		return !entity.isInvisible() || this.renderOutlines;
	}
}