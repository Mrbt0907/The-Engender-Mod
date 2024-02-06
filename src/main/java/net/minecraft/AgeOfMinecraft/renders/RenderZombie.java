package net.minecraft.AgeOfMinecraft.renders;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.models.ModelZombie;
import net.minecraft.AgeOfMinecraft.models.ModelZombieVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class RenderZombie extends RenderLiving<EntityZombie>
{
private static final ResourceLocation ZOMBIE_VILLAGER_TEXTURES = new ResourceLocation("textures/entity/zombie_villager/zombie_villager.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_FARMER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_farmer.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_LIBRARIAN_LOC = new ResourceLocation("textures/entity/zombie_villager/zombie_librarian.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_PRIEST_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_priest.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_SMITH_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_smith.png");
	private static final ResourceLocation ZOMBIE_VILLAGER_BUTCHER_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_butcher.png");
	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");
	private static final ResourceLocation HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");
	private static final ResourceLocation PRISON_ZOMBIE_TEXTURES = new ResourceLocation("ageofminecraft", "textures/entities/prisonzombie.png");
	private static final ResourceLocation antiZOMBIE_TEXTURES = new ResourceLocation("ageofminecraft", "textures/entities/anti/zombie.png");
	private static final ResourceLocation antiHUSK_ZOMBIE_TEXTURES = new ResourceLocation("ageofminecraft", "textures/entities/anti/husk.png");
	private static final ResourceLocation antiPRISON_ZOMBIE_TEXTURES = new ResourceLocation("ageofminecraft", "textures/entities/anti/prisonzombie.png");
	private static final ResourceLocation DAVE_ZOMBIE_TEXTURES = new ResourceLocation("ageofminecraft", "textures/entities/dave.png");
	private static final ResourceLocation MARK_ZOMBIE_TEXTURES = new ResourceLocation("ageofminecraft", "textures/entities/mark.png");
	private LayerCustomArmor armor = new LayerCustomArmor(this);
	private static ModelZombieVillager sregularmodel = new ModelZombieVillager();
	private static ModelZombie regularmodel = new ModelZombie();
	private static ModelZombieVillager sregularleggings = new ModelZombieVillager(0.5F, 0.0F, true);
	private static ModelZombie regularleggings = new ModelZombie(0.5F, true);
	private static ModelZombieVillager sregulararmor = new ModelZombieVillager(1F, 0.0F, true);
	private static ModelZombie regulararmor = new ModelZombie(1F, true);
	public RenderZombie(RenderManager renderManagerIn)
	{
		super(renderManagerIn, regularmodel, 0.5F);
		this.addLayer(new LayerArrowCustomSized(this, 1.0F));
		this.addLayer(new LayerLearningBook(this));
		this.addLayer(new LayerMobCape(this));
		armor = new LayerCustomArmor(this)
		{
			protected void initArmor()
			{
				this.modelLeggings = regularleggings;
				this.modelArmor =regulararmor;
			}
		};
		this.addLayer(armor);
		this.addLayer(new LayerElytra(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerCustomHeadEngender(regularmodel.bipedHead));
	}
	private void changeModel(EntityZombie entitylivingbaseIn)
	{
		this.mainModel = (entitylivingbaseIn.isVillager() ? sregularmodel : regularmodel);
		
		this.layerRenderers.remove(armor);
		armor = new LayerCustomArmor(this)
		{
			protected void initArmor()
			{
				this.modelLeggings = (entitylivingbaseIn.isVillager() ? sregularleggings : regularleggings);
				this.modelArmor = (entitylivingbaseIn.isVillager() ? sregulararmor : regulararmor);
			}
		};
		this.addLayer(armor);
	}
	public void transformHeldFull3DItemLayer()
	{
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	protected void preRenderCallback(EntityZombie entitylivingbaseIn, float partialTickTime)
	{
		changeModel(entitylivingbaseIn);
		
			if (entitylivingbaseIn.isSneaking())
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		

		if (entitylivingbaseIn.getZombieType() == 1)
		{
			GlStateManager.scale(1.0625F, 1.0625F, 1.0625F);
		}
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
	protected ResourceLocation getEntityTexture(EntityZombie entity)
	{
		String s = TextFormatting.getTextWithoutFormattingCodes(entity.getName());
		
			switch (entity.getZombieType())
			{
				case 1:
				return entity.isAntiMob() ? antiHUSK_ZOMBIE_TEXTURES : HUSK_ZOMBIE_TEXTURES;
				case 2:
				return entity.isAntiMob() ? antiPRISON_ZOMBIE_TEXTURES : PRISON_ZOMBIE_TEXTURES;
				default:
				{
					if (entity.isVillager())
					{
						switch (entity.getVillagerType())
						{
							case 0:return ZOMBIE_VILLAGER_FARMER_LOCATION;
							case 1:return ZOMBIE_VILLAGER_LIBRARIAN_LOC;
							case 2:return ZOMBIE_VILLAGER_PRIEST_LOCATION;
							case 3:return ZOMBIE_VILLAGER_SMITH_LOCATION;
							case 4:return ZOMBIE_VILLAGER_BUTCHER_LOCATION;
						}
						return ZOMBIE_VILLAGER_TEXTURES;
					}
					return entity.isAntiMob() ? antiZOMBIE_TEXTURES : (s != null && s.equals("Dave") ? DAVE_ZOMBIE_TEXTURES : (s != null && s.equals("Mark") ? MARK_ZOMBIE_TEXTURES : ZOMBIE_TEXTURES));
				}
			}
		
	}
	protected void applyRotations(EntityZombie entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks)
	{
		if (entityLiving.isConverting())
		{
			p_77043_3_ += (float)(Math.cos(entityLiving.ticksExisted * 3.25D) * 3.141592653589793D);
		}

		if (entityLiving.isBurning())
		{
			p_77043_3_ += (float)(Math.cos(entityLiving.ticksExisted * 1D) * 3.141592653589793D);
		}
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
	}
	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityZombie entity, double x, double y, double z, float entityYaw, float partialTicks)
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
		else if (!entity.isInvisible())
		{
			this.shadowOpaque = 1F;
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}

	protected boolean isVisible(EntityZombie entity)
	{
		return !entity.isInvisible() || this.renderOutlines || entity.getGhostTime() > 0;
	}
}