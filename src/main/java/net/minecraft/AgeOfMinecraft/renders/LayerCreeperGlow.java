package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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


