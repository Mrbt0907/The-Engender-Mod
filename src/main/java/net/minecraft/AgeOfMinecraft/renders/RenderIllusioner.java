package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityIllusioner;
import net.minecraft.AgeOfMinecraft.models.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class RenderIllusioner extends RenderLiving<EntityIllusioner>
{
	private static final ResourceLocation ILLUSIONIST = new ResourceLocation("textures/entity/illager/illusionist.png");
	private static final ResourceLocation DISGUISE_VILLAGER = new ResourceLocation("textures/entity/villager/farmer.png");
	private static final ResourceLocation DISGUISE_VINDICATOR = new ResourceLocation("textures/entity/illager/vindicator.png");
	private static final ResourceLocation DISGUISE_EVOKER = new ResourceLocation("textures/entity/illager/evoker.png");
	
	public RenderIllusioner(RenderManager p_i47477_1_)
	{
		super(p_i47477_1_, new ModelIllager(0.0F, 0.0F, 64, 64), 0.5F);
		this.addLayer(new LayerHeldItem(this)
		{
			public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
			{
				if (((EntityIllusioner)entitylivingbaseIn).isSpellcasting() || ((EntityIllusioner)entitylivingbaseIn).isArmsRaised())
				{
					super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
				}
			}
			protected void translateToHand(EnumHandSide p_191361_1_)
			{
				((ModelIllager)this.livingEntityRenderer.getMainModel()).getArm(p_191361_1_).postRender(0.0625F);
			}
		});
			((ModelIllager)this.getMainModel()).hat.showModel = true;
		}

		/**
		* Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
		*/
		protected ResourceLocation getEntityTexture(EntityIllusioner entity)
		{
			if (entity.getDisguiseID() > 0 && entity.getDisguiseTime() > 0)
			{
				switch (entity.getDisguiseID())
				{
					case 2:
					return DISGUISE_VINDICATOR;
					case 3:
					return DISGUISE_EVOKER;
					default:
					return DISGUISE_VILLAGER;
				}
			}
			else
			{
				return ILLUSIONIST;
			}
		}

		/**
		* Allows the render to do state modifications necessary before the model is rendered.
		*/
		protected void preRenderCallback(EntityIllusioner entitylivingbaseIn, float partialTickTime)
		{
			float f = 0.9375F;
			GlStateManager.scale(f, f, f);
			if (entitylivingbaseIn.isChild())
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
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
		public void doRender(EntityIllusioner entity, double x, double y, double z, float entityYaw, float partialTicks)
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

		protected boolean isVisible(EntityIllusioner entity)
		{
			return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
		}
	}
	