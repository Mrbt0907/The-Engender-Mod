package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityBat;
import net.mrbt0907.ageofminecraft.models.ModelBat;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;
 
@SideOnly(Side.CLIENT)
public class RenderBat extends RenderEngendered<EntityBat>
{
	private static final ResourceLocation batTextures = new ResourceLocation("textures/entity/bat.png");
	private static ModelBat regularmodel = new ModelBat();
	
	public RenderBat(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 0.25F);
	}
	
	protected ResourceLocation getEntityTexture(EntityBat entity)
	{
		return batTextures;
	}
	
	protected void preRenderCallback(EntityBat entitylivingbaseIn, float partialTickTime)
	{
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
		this.mainModel = regularmodel;
        GlStateManager.scale(0.35F, 0.35F, 0.35F);
		
		//if (entitylivingbaseIn.isHero())
		//GlStateManager.scale(1.05F, 1.05F, 1.05F);
		
		//if (!entitylivingbaseIn.onGround)
		//GlStateManager.rotate(entitylivingbaseIn.prevRotationPitchFalling + (entitylivingbaseIn.rotationPitchFalling - entitylivingbaseIn.prevRotationPitchFalling) * 2F - 1F, 1F, 0F, 0F);
	}
	
	protected void applyRotations(EntityBat bat, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		if (!bat.getIsBatHanging())
			GlStateManager.translate(0.0F, Maths.fastCos((bat.isAIDisabled() ? 1 : p_77043_2_) * 0.3F) * 0.1F, 0.0F);
		else
			GlStateManager.translate(0.0F, -0.1F, 0.0F);
		super.applyRotations(bat, p_77043_2_, p_77043_3_, partialTicks);
	}
	
	protected boolean isVisible(EntityBat entity)
	{
		return !entity.isInvisible() || renderOutlines;
	}
}