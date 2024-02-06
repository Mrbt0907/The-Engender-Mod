package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntitySheep;
import net.minecraft.AgeOfMinecraft.models.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class LayerSheepWool
implements LayerRenderer<EntitySheep>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
	private static final ResourceLocation antiTEXTURE = new ResourceLocation("ageofminecraft", "textures/entities/anti/sheep_fur.png");
	private final RenderSheep sheepRenderer;
	private final ModelSheep1 sheepModel = new ModelSheep1();
	public LayerSheepWool(RenderSheep sheepRendererIn)
	{
		this.sheepRenderer = sheepRendererIn;
	}
	public void doRenderLayer(EntitySheep entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if ((!entitylivingbaseIn.getSheared()) && (!entitylivingbaseIn.isInvisible()))
		{
			this.sheepRenderer.bindTexture((entitylivingbaseIn.isAntiMob() ? antiTEXTURE : TEXTURE));
			if ((entitylivingbaseIn.hasCustomName()) && ("jeb_".equals(entitylivingbaseIn.getCustomNameTag())))
			{
				int i = entitylivingbaseIn.ticksExisted / 5 + entitylivingbaseIn.getEntityId();
				int j = EnumDyeColor.values().length;
				int k = i % j;
				int l = (i + 1) % j;
				float f = (entitylivingbaseIn.ticksExisted % 5 + partialTicks) / 5.0F;
				float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
				float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
				GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
			}
			else
			{
				float[] afloat = EntitySheep.getDyeRgb(entitylivingbaseIn.getFleeceColor());
				GlStateManager.color(afloat[0], afloat[1], afloat[2]);
			}
			this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
			this.sheepModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
			this.sheepModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		}
	}
	public boolean shouldCombineTextures()
	{
		return true;
	}
}


