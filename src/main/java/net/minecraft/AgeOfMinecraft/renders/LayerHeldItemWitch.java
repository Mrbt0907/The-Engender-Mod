package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.EngenderConfig;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityWitch;
import net.minecraft.AgeOfMinecraft.models.ModelWitch;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)

public class LayerHeldItemWitch implements net.minecraft.client.renderer.entity.layers.LayerRenderer<EntityLivingBase>
{
	private final RenderWitch witchRenderer;
	public LayerHeldItemWitch(RenderWitch p_i46106_1_)
	{
		this.witchRenderer = p_i46106_1_;
	}
	public void func_177143_a(EntityWitch entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		ItemStack itemstack = entitylivingbaseIn.getHeldItemMainhand();
		
		if (!itemstack.isEmpty())
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			GlStateManager.pushMatrix();
			
			((ModelWitch)this.witchRenderer.getMainModel()).villagerArms.postRender(0.0625F);
			GlStateManager.translate(-0.25F, 0.25F, -0.125F);
			GlStateManager.rotate(-150.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-15.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-37.5F, 0.0F, 0.0F, 1.0F);
			Item item = itemstack.getItem();
			Minecraft minecraft = Minecraft.getMinecraft();
			
			if (Block.getBlockFromItem(item).getDefaultState().getRenderType() == EnumBlockRenderType.ENTITYBLOCK_ANIMATED)
			{
				GlStateManager.translate(0.0F, 0.0625F, -0.25F);
				GlStateManager.rotate(30.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-5.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.scale(0.375F, -0.375F, 0.375F);
			}
			else if (item == Items.BOW)
			{
				GlStateManager.translate(0.0F, 0.125F, -0.125F);
				GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.scale(0.625F, -0.625F, 0.625F);
				GlStateManager.rotate(-100.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(-20.0F, 0.0F, 1.0F, 0.0F);
			}
			else if (item.isFull3D())
			{
				if (item.shouldRotateAroundWhenRendering())
				{
					GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
					GlStateManager.translate(0.0F, -0.0625F, 0.0F);
				}

				this.witchRenderer.transformHeldFull3DItemLayer();
				GlStateManager.translate(0.0625F, -0.125F, 0.0F);
				GlStateManager.scale(0.625F, -0.625F, 0.625F);
				GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
			}
			else
			{
				GlStateManager.translate(0.1875F, 0.1875F, 0.0F);
				GlStateManager.scale(0.875F, 0.875F, 0.875F);
			}

			GlStateManager.rotate(-15.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(40.0F, 0.0F, 0.0F, 1.0F);
			minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
			GlStateManager.popMatrix();
		}
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	{
		func_177143_a((EntityWitch)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}
}


