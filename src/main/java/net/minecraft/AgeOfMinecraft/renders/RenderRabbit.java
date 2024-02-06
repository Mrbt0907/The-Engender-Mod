package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityRabbit;
import net.minecraft.AgeOfMinecraft.models.ModelRabbit;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderRabbit
extends RenderLiving<EntityRabbit>
{
	private static final ResourceLocation BROWN = new ResourceLocation("textures/entity/rabbit/brown.png");
	private static final ResourceLocation WHITE = new ResourceLocation("textures/entity/rabbit/white.png");
	private static final ResourceLocation BLACK = new ResourceLocation("textures/entity/rabbit/black.png");
	private static final ResourceLocation GOLD = new ResourceLocation("textures/entity/rabbit/gold.png");
	private static final ResourceLocation SALT = new ResourceLocation("textures/entity/rabbit/salt.png");
	private static final ResourceLocation WHITE_SPLOTCHED = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
	private static final ResourceLocation TOAST = new ResourceLocation("textures/entity/rabbit/toast.png");
	private static final ResourceLocation CAERBANNOG = new ResourceLocation("textures/entity/rabbit/caerbannog.png");
	public RenderRabbit(RenderManager p_i46146_1_)
	{
		super(p_i46146_1_, new ModelRabbit(), 0.3F);
		addLayer(new LayerArrowCustomSized(this, 0.6F));
		this.addLayer(new LayerLearningBook(this));
	}
	protected ResourceLocation getEntityTexture(EntityRabbit entity)
	{
		String s = TextFormatting.getTextWithoutFormattingCodes(entity.getName());
		
		if (s != null && s.equals("Toast"))
		{
			return TOAST;
		}
		else
		{
			switch (entity.getRabbitType())
			{
				case 0:
				default:
				return BROWN;
				case 1:
				return WHITE;
				case 2:
				return BLACK;
				case 3:
				return WHITE_SPLOTCHED;
				case 4:
				return GOLD;
				case 5:
				return SALT;
				case 99:
				return CAERBANNOG;
			}
		}
	}

	protected void preRenderCallback(EntityRabbit entitylivingbaseIn, float partialTickTime)
	{
		float fit = entitylivingbaseIn.getFittness();
		GlStateManager.scale(fit, fit, fit);
		
		if (entitylivingbaseIn.isHero())
		GlStateManager.scale(1.05F, 1.05F, 1.05F);
		
		if (!entitylivingbaseIn.onGround)
		GlStateManager.rotate(entitylivingbaseIn.prevRotationPitchFalling + (entitylivingbaseIn.rotationPitchFalling - entitylivingbaseIn.prevRotationPitchFalling) * 2F - 1F, 1F, 0F, 0F);
		
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

	/**
	* Renders the desired {@code T} type Entity.
	*/
	public void doRender(EntityRabbit entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (entity.getGhostTime() > 0)
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
		{
			this.shadowOpaque = 1F;
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}

	protected boolean isVisible(EntityRabbit entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}
}