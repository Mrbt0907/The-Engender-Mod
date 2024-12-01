package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier3.EntityCreeper;
@SideOnly(Side.CLIENT)

public class LayerCreeperGlow implements LayerRenderer<EntityCreeper>
{
	public LayerCreeperGlow(RenderCreeper creeperRendererIn) {}
	public void doRenderLayer(EntityCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{}
	public boolean shouldCombineTextures()
	{
		return true;
	}
}


