package net.mrbt0907.ageofminecraft.renders;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier3.EntityZombie;
import net.mrbt0907.ageofminecraft.models.ModelZombie;
import net.mrbt0907.ageofminecraft.models.ModelZombieVillager;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;

@SideOnly(Side.CLIENT)
public class RenderZombie extends RenderEngendered<EntityZombie>
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
		armor = new LayerCustomArmor(this)
		{
			protected void initArmor()
			{
				modelLeggings = regularleggings;
				modelArmor =regulararmor;
			}
		};
		addLayer(armor);
		addLayer(new LayerElytra(this));
		addLayer(new LayerHeldItem(this));
	}
	
	private void changeModel(EntityZombie entitylivingbaseIn)
	{
		mainModel = (entitylivingbaseIn.isVillager() ? sregularmodel : regularmodel);
		
		layerRenderers.remove(armor);
		armor = new LayerCustomArmor(this)
		{
			protected void initArmor()
			{
				modelLeggings = (entitylivingbaseIn.isVillager() ? sregularleggings : regularleggings);
				modelArmor = (entitylivingbaseIn.isVillager() ? sregulararmor : regulararmor);
			}
		};
		addLayer(armor);
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
			GlStateManager.scale(1.0625F, 1.0625F, 1.0625F);
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}
	protected ResourceLocation getEntityTexture(EntityZombie entity)
	{
		switch (entity.getZombieType())
		{
			case 1:
				return HUSK_ZOMBIE_TEXTURES;
			case 2:
				return PRISON_ZOMBIE_TEXTURES;
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
				String s = TextFormatting.getTextWithoutFormattingCodes(entity.getName());
				return (s != null && s.equals("Dave") ? DAVE_ZOMBIE_TEXTURES : (s != null && s.equals("Mark") ? MARK_ZOMBIE_TEXTURES : ZOMBIE_TEXTURES));
			}
		}
		
	}
	protected void applyRotations(EntityZombie entity, float rotationPitch, float rotationYaw, float partialTicks)
	{
		if (entity.isConverting())
			rotationYaw += (float)(Maths.fastCos(entity.ticksExisted * 3.25D) * 3.141592653589793D);

		if (entity.isBurning())
			rotationYaw += (float)(Maths.fastCos(entity.ticksExisted * 1D) * 3.141592653589793D);

		if (entity.isElytraFlying())
		{
			super.applyRotations(entity, rotationPitch, rotationYaw, partialTicks);
			float f = (float)entity.getTicksElytraFlying() + partialTicks;
			float f1 = MathHelper.clamp(f * f / 100.0F, 0.0F, 1.0F);
			GlStateManager.rotate(f1 * (-90.0F - entity.rotationPitch), 1.0F, 0.0F, 0.0F);
			Vec3d vec3d = entity.getLook(partialTicks);
			double d0 = entity.motionX * entity.motionX + entity.motionZ * entity.motionZ;
			double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;
			
			if (d0 > 0.0D && d1 > 0.0D)
			{
				double d2 = (entity.motionX * vec3d.x + entity.motionZ * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
				double d3 = entity.motionX * vec3d.z - entity.motionZ * vec3d.x;
				GlStateManager.rotate((float)(Math.signum(d3) * Maths.fastACos(d2)) * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
			}
		}
		else
		{
			GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
			
			if (entity.deathTime > 0)
			{
				float f = ((float)entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
				f = MathHelper.sqrt(f);
				if (f > 1.0F)
					f = 1.0F;
				
				GlStateManager.rotate(f * getDeathMaxRotation(entity), 0.0F, 0.0F, 1.0F);
				GlStateManager.translate(f * 0.25F, 0.0F, 0.0F);
			}
			else
			{
				String s = TextFormatting.getTextWithoutFormattingCodes(entity.getName());
				
				if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)))
				{
					GlStateManager.translate(0.0F, entity.height + 0.1F, 0.0F);
					GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
				}
			}
		}
	}

	protected boolean isVisible(EntityZombie entity)
	{
		return !entity.isInvisible() || renderOutlines;
	}
}