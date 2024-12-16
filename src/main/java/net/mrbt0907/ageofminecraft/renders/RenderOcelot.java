package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityOcelot;
import net.mrbt0907.ageofminecraft.models.ModelOcelot;

@SideOnly(Side.CLIENT)

public class RenderOcelot extends RenderLiving<EntityOcelot>
{
	private static final ResourceLocation ocelotTextures = new ResourceLocation("textures/entity/cat/ocelot.png");
	
	public RenderOcelot(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelOcelot(), 0.4F);
	}
	
	protected ResourceLocation getEntityTexture(EntityOcelot entity)
	{
		return ocelotTextures;
	}

	protected boolean isVisible(EntityOcelot entity)
	{
		return !entity.isInvisible() || renderOutlines;
	}
}