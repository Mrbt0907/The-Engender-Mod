package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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


