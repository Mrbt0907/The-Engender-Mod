package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityCaveSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderCaveSpider
extends RenderSpider<EntityCaveSpider>
{
	private static final ResourceLocation textures = new ResourceLocation("textures/entity/spider/cave_spider.png");
	private static final ResourceLocation antiTextures = new ResourceLocation("ageofminecraft", "textures/entities/anti/cave_spider.png");
	public RenderCaveSpider(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.shadowSize *= 0.7F;
	}
	protected void preRenderCallback(EntityCaveSpider entitylivingbaseIn, float partialTickTime)
	{
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
		this.shadowSize *= 0.7F;
		
			GlStateManager.scale(0.7F, 0.7F, 0.7F);
			if (entitylivingbaseIn.isRiding())
			GlStateManager.translate(0.0F, 0.25F, 0.0F);
		
		if (entitylivingbaseIn.ticksExisted <= 21 && entitylivingbaseIn.ticksExisted > 0)
		{
			float f5 = (entitylivingbaseIn.ticksExisted + partialTickTime - 1.0F) / 20.0F * 1.6F;
			f5 = MathHelper.sqrt(f5);
			if (f5 > 1.0F)
			f5 = 1.0F;
			GlStateManager.scale(f5, f5, f5);
			GlStateManager.rotate(f5 * 90F - 90F, f5, f5, f5);
		}
	}
	protected ResourceLocation getEntityTexture(EntityCaveSpider entity)
	{
		return (entity.isAntiMob() ? antiTextures : textures);
	}
}