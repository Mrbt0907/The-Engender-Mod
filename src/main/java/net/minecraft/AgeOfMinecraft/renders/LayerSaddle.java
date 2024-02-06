package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityPig;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerSaddle implements LayerRenderer<EntityLivingBase>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/pig/pig_saddle.png");
	private final RenderPig pigRenderer;
	private final ModelPig pigModel = new ModelPig(0.5F);
	public LayerSaddle(RenderPig p_i46113_1_)
	{
		this.pigRenderer = p_i46113_1_;
	}
	public void doRenderLayer(EntityPig p_177155_1_, float p_177155_2_, float p_177155_3_, float p_177155_4_, float p_177155_5_, float p_177155_6_, float p_177155_7_, float p_177155_8_)
	{
		if (p_177155_1_.getSaddled())
		{
			this.pigRenderer.bindTexture(TEXTURE);
			this.pigModel.setModelAttributes(this.pigRenderer.getMainModel());
			this.pigModel.render(p_177155_1_, p_177155_2_, p_177155_3_, p_177155_5_, p_177155_6_, p_177155_7_, p_177155_8_);
		}
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		doRenderLayer((EntityPig)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


