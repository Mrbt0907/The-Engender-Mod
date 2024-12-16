package net.mrbt0907.ageofminecraft.renders;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntitySheep;
import net.mrbt0907.ageofminecraft.models.ModelSheep2;

@SideOnly(Side.CLIENT)
public class RenderSheep extends RenderLiving<EntitySheep>
{
	private static final ResourceLocation shearedSheepTextures = new ResourceLocation("textures/entity/sheep/sheep.png");
	
	public RenderSheep(RenderManager p_i46145_1_)
	{
		super(p_i46145_1_, new ModelSheep2(), 0.8F);
		addLayer(new LayerSheepWool(this));
	}
	
	protected ResourceLocation getEntityTexture(EntitySheep entity)
	{
		return shearedSheepTextures;
	}
	
	protected boolean isVisible(EntitySheep entity)
	{
		return !entity.isInvisible() || renderOutlines;
	}
}