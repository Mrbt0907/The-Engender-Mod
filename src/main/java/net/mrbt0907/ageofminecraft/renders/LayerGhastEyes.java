package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier4.EntityGhast;
@SideOnly(Side.CLIENT)

public class LayerGhastEyes implements LayerRenderer<EntityGhast>
{
	public LayerGhastEyes(RenderGhast creeperRendererIn)
	{
	}
	public void doRenderLayer(EntityGhast entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		
	}
	public boolean shouldCombineTextures()
	{
		return true;
	}
}


