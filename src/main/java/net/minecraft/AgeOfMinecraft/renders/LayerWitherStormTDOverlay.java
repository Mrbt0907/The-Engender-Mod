package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormTentacleDevourer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerWitherStormTDOverlay
implements LayerRenderer
{
	private static final ResourceLocation witherStormTextures = new ResourceLocation("ageofminecraft", "textures/entities/wither_storm_hue.png");
	private final RenderWitherStormTentacleDevourer endermanRenderer;
	public LayerWitherStormTDOverlay(RenderWitherStormTentacleDevourer p_i46117_1_)
	{
		this.endermanRenderer = p_i46117_1_;
	}
	public void func_177201_a(EntityWitherStormTentacleDevourer p_177201_1_, float p_177201_2_, float p_177201_3_, float p_177201_4_, float p_177201_5_, float p_177201_6_, float p_177201_7_, float p_177201_8_)
	{
		this.endermanRenderer.bindTexture(witherStormTextures);
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177201_a((EntityWitherStormTentacleDevourer)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


