package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityChicken;
import net.mrbt0907.ageofminecraft.models.ModelChicken;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;

@SideOnly(Side.CLIENT)
public class RenderChicken extends RenderEngendered<EntityChicken>
{
	private static final ResourceLocation textures = new ResourceLocation("textures/entity/chicken.png");
	
	public RenderChicken(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelChicken(), 0.3F);
	}
	
	protected ResourceLocation getEntityTexture(EntityChicken entity)
	{
		return textures;
	}
	
	protected float handleRotationFloat(EntityChicken livingBase, float partialTicks)
	{
		float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
		float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
		return ((float)(Maths.fastSin(f)) + 1.0F) * f1;
	}
	
	protected boolean isVisible(EntityChicken entity)
	{
		return !entity.isInvisible() || this.renderOutlines;
	}
}