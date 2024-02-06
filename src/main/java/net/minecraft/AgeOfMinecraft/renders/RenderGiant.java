package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityGiant;
import net.minecraft.AgeOfMinecraft.models.ModelGiant;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderGiant
extends RenderLiving<EntityGiant>
{
	private static final ResourceLocation ZOMBIE_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final ResourceLocation antiZOMBIE_ZOMBIE_TEXTURES = new ResourceLocation("ageofminecraft", "textures/entities/anti/zombie.png");

	private static ModelGiant regularmodel = new ModelGiant();
	public RenderGiant(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 3.0F);
		addLayer(new LayerArrowCustomSized(this, 0.1675F));
		addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerCustomHeadEngender(regularmodel.bipedHead));
		this.addLayer(new LayerLearningBook(this));
		this.addLayer(new LayerMobCape(this));
		addLayer(new LayerBipedArmor(this)
		{
			protected void initArmor()
			{
				this.modelLeggings = new ModelGiant(0.5F, true);
				this.modelArmor = new ModelGiant(1.0F, true);
			}
		});
		}
		public void transformHeldFull3DItemLayer()
		{
			GlStateManager.translate(0.0F, 0.1875F, 0.0F);
		}
		protected void applyRotations(EntityGiant entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
		{
			if (entityLiving.isElytraFlying())
			{
				super.applyRotations(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
				float f = (float)entityLiving.getTicksElytraFlying() + partialTicks;
				float f1 = MathHelper.clamp(f * f / 100.0F, 0.0F, 1.0F);
				GlStateManager.rotate(f1 * (-90.0F - entityLiving.rotationPitch), 1.0F, 0.0F, 0.0F);
				Vec3d vec3d = entityLiving.getLook(partialTicks);
				double d0 = entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ;
				double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;
				
				if (d0 > 0.0D && d1 > 0.0D)
				{
					double d2 = (entityLiving.motionX * vec3d.x + entityLiving.motionZ * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
					double d3 = entityLiving.motionX * vec3d.z - entityLiving.motionZ * vec3d.x;
					GlStateManager.rotate((float)(Math.signum(d3) * Math.acos(d2)) * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
				}
			}
			else
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
		}
		protected void preRenderCallback(EntityGiant entitylivingbaseIn, float partialTickTime)
		{
			this.mainModel = regularmodel;
			
			GlStateManager.scale(6.0F, 6.0F, 6.0F);
			
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
		protected ResourceLocation getEntityTexture(EntityGiant entity)
		{
			return (entity.isAntiMob() ? antiZOMBIE_ZOMBIE_TEXTURES : ZOMBIE_ZOMBIE_TEXTURES);
		}
		/**
		* Renders the desired {@code T} type Entity.
		*/
		public void doRender(EntityGiant entity, double x, double y, double z, float entityYaw, float partialTicks)
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

		protected boolean isVisible(EntityGiant entity)
		{
			return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
		}
	}

	
	