package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntityCow;

@SideOnly(Side.CLIENT)
public class RenderCow extends RenderLiving<EntityCow>
{
	private static final ResourceLocation cowTextures = new ResourceLocation("textures/entity/cow/cow.png");
	
	public RenderCow(RenderManager manager)
	{
		super(manager, new ModelCow(), 0.7F);
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		addLayer(new LayerCustomHeadEngender(((ModelCow)this.mainModel).head));
		//addLayer(new LayerLearningBook(this));
	}
	
	protected void preRenderCallback(EntityCow entity, float partialTicks)
	{
		if (entity.ticksExisted <= 21 && entity.ticksExisted > 0)
		{
			float f5 = (entity.ticksExisted + partialTicks - 1.0F) / 20.0F * 1.6F;
			f5 = MathHelper.sqrt(f5);
			if (f5 > 1.0F)
				f5 = 1.0F;
			GlStateManager.scale(f5, f5, f5);
			GlStateManager.rotate(f5 * 90F - 90F, f5, f5, f5);
		}
	}
	
	protected ResourceLocation getEntityTexture(EntityCow entity)
	{
		return cowTextures;
	}
	
	public void doRender(EntityCow entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		/*if (entity.getGhostTime() > 0)
		{
			Vec3d[] avec3d = entity.getRenderLocations(partialTicks);
			float f = this.handleRotationFloat(entity, partialTicks);
			
			for (int i = 0; i < avec3d.length; ++i)
			{
				super.doRender(entity, x + avec3d[i].x + (double)MathHelper.cos((float)i + f * 0.5F) * 0.025D, y + avec3d[i].y + (double)MathHelper.cos((float)i + f * 0.75F) * 0.0125D, z + avec3d[i].z + (double)MathHelper.cos((float)i + f * 0.7F) * 0.025D, entityYaw, partialTicks);
			}
			this.shadowOpaque = 0F;
		}
		else
		{*/
			shadowOpaque = 1F;
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		//}
	}
}