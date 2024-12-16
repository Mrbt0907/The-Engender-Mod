package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityCow;

@SideOnly(Side.CLIENT)
public class RenderCow extends RenderEngendered<EntityCow>
{
	private static final ResourceLocation cowTextures = new ResourceLocation("textures/entity/cow/cow.png");
	
	public RenderCow(RenderManager manager)
	{
		super(manager, new ModelCow(), 0.7F);
	}
	
	protected ResourceLocation getEntityTexture(EntityCow entity)
	{
		return cowTextures;
	}
}