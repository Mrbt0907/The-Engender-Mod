package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityPig;

@SideOnly(Side.CLIENT)
public class RenderPig extends RenderLiving<EntityPig>
{
	private static final ResourceLocation pigTextures = new ResourceLocation("textures/entity/pig/pig.png");
	
	public RenderPig(RenderManager manager)
	{
		super(manager, new ModelPig(0.0F), 0.75F);
		addLayer(new LayerSaddle(this));
	}
	
	protected ResourceLocation getEntityTexture(EntityPig entity)
	{
		return pigTextures;
	}

	protected boolean isVisible(EntityPig entity)
	{
		return !entity.isInvisible() || this.renderOutlines;
	}
}