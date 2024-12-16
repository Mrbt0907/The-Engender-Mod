package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityMooshroom;

@SideOnly(Side.CLIENT)
public class RenderMooshroom extends RenderEngendered<EntityMooshroom>
{
	private static final ResourceLocation mooshroomTextures = new ResourceLocation("textures/entity/cow/mooshroom.png");
	
	public RenderMooshroom(RenderManager manager)
	{
		super(manager, new ModelCow(), 0.9F);
		addLayer(new LayerMooshroomMushroom(this));
	}
	
	protected ResourceLocation getEntityTexture(EntityMooshroom p_180582_1_)
	{
		return mooshroomTextures;
	}
}