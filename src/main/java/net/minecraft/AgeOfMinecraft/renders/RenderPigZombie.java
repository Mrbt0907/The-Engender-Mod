package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPigZombie;
import net.minecraft.AgeOfMinecraft.models.ModelZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderPigZombie extends RenderLiving<EntityPigZombie>
{
	private static final ResourceLocation ZOMBIE_PIGMAN_TEXTURE = new ResourceLocation("textures/entity/zombie_pigman.png");
	private static final ResourceLocation antiZOMBIE_PIGMAN_TEXTURE = new ResourceLocation("ageofminecraft", "textures/entities/anti/zombie_pigman.png");
	private LayerBipedArmor armor = new LayerBipedArmor(this);
	private static ModelZombie regularmodel = new ModelZombie();
	private static ModelZombie regularleggings = new ModelZombie(0.5F, true);
	private static ModelZombie regulararmor = new ModelZombie(1F, true);
	
	public RenderPigZombie(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 0.5F);
		addLayer(new LayerArrowCustomSized(this, 1.0F));
		armor = new LayerBipedArmor(this)
		{
			protected void initArmor()
			{
				this.modelLeggings = regularleggings;
				this.modelArmor = regulararmor;
			}
		};
		this.addLayer(armor);
		this.addLayer(new LayerElytra(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerCustomHeadEngender(regularmodel.bipedHead));
		this.addLayer(new LayerLearningBook(this));
		this.addLayer(new LayerMobCape(this));
	}
	protected void applyRotations(EntityPigZombie entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
		
		if (entityLiving.deathTime > 0)
		{
			float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
			f = MathHelper.sqrt(f);
			
			if (f > 1.0F)
			{
				f = 1.0F;
			}

				GlStateManager.rotate(f * this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F, 1.0F);
				GlStateManager.translate(f * 0.25F, 0.0F, 0.0F);
			
		}
		else
		{
			String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());
			
			if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)))
			{
				GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			}
		}
	}

	public void transformHeldFull3DItemLayer()
	{
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	protected ResourceLocation getEntityTexture(EntityPigZombie entity)
	{
		return (entity.isAntiMob() ? antiZOMBIE_PIGMAN_TEXTURE : ZOMBIE_PIGMAN_TEXTURE);
	}

	private void changeModel()
	{
		this.mainModel = regularmodel;
		
		this.layerRenderers.remove(armor);
		armor = new LayerBipedArmor(this)
		{
			protected void initArmor()
			{
				this.modelLeggings = regularleggings;
				this.modelArmor = regulararmor;
			}
		};
		this.addLayer(armor);
	}

	protected void preRenderCallback(EntityPigZombie entitylivingbaseIn, float partialTickTime)
	{
		this.changeModel();
		
		if (entitylivingbaseIn.isSneaking())
		GlStateManager.translate(0.0F, 0.2F, 0.0F);
		
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
	public void doRender(EntityPigZombie entity, double x, double y, double z, float entityYaw, float partialTicks)
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

	protected boolean isVisible(EntityPigZombie entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}
}