package net.mrbt0907.ageofminecraft.renders;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.models.ICappedModel;
import net.mrbt0907.ageofminecraft.util.mrbtutil.Maths;

@SideOnly(Side.CLIENT)
public class LayerMobCape implements LayerRenderer<EntityEngendered>
{
	private static final Map<String, ResourceLocation> CAPES;
	
	//private static final ResourceLocation TEST_CAPE = new ResourceLocation("ageofminecraft", "textures/test_cape.png");
	//private static final ResourceLocation ENDER_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_ug.png");
	//private static final ResourceLocation BECK_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_bbj.png");
	//private static final ResourceLocation RAM_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_4c.png");
	//private static final ResourceLocation SOURGE_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_mc.png");
	private final RenderLivingBase<? extends EntityEngendered> renderer;
	private static final ResourceLocation TEST_CAPE = FMLLaunchHandler.isDeobfuscatedEnvironment() ? new ResourceLocation(EngenderMod.MODID, "textures/cape_bt.png") : null;
	
	static
	{
		CAPES = new HashMap<String, ResourceLocation>();
		CAPES.put("39c0cf10-5f5d-4c89-8057-cee67479c7c2", new ResourceLocation(EngenderMod.MODID, "textures/cape_bt.png"));
		CAPES.put("e3b0110c-14f3-4640-bf1b-d4f398b4b243", new ResourceLocation(EngenderMod.MODID, "textures/cape_m.png"));
		CAPES.put("19d96ed2-6c4d-42bd-9855-498482daa5ab", new ResourceLocation(EngenderMod.MODID, "textures/cape_jd.png"));
		CAPES.put("47e8b39b-3675-4851-8ba7-81f3840438ba", new ResourceLocation(EngenderMod.MODID, "textures/cape_en.png"));
		CAPES.put("7de33186-920a-4c80-a986-aafae469deff", new ResourceLocation(EngenderMod.MODID, "textures/cape_sh.png"));
	}
	
	public LayerMobCape(RenderLivingBase<? extends EntityEngendered> renderer)
	{
		this.renderer = renderer;
	}
	
	public void doRenderLayer(EntityEngendered entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		ResourceLocation texture = TEST_CAPE == null ? CAPES.get(String.valueOf(entity.getOwnerId())) : entity.hasOwner() ? TEST_CAPE : null;
		if (texture != null)
		{
			Item chestItem = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();
			if (chestItem.equals(Items.ELYTRA)) return;
			
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			renderer.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 0.0F, 0.125F);
			double d0 = entity.prevChasingPosX + (entity.chasingPosX - entity.prevChasingPosX) * (double)partialTicks - (entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks);
			double d1 = entity.prevChasingPosY + (entity.chasingPosY - entity.prevChasingPosY) * (double)partialTicks - (entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks);
			double d2 = entity.prevChasingPosZ + (entity.chasingPosZ - entity.prevChasingPosZ) * (double)partialTicks - (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks);
			float f = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;
			double d3 = Maths.fastSin(f * 0.017453292F);
			double d4 = -Maths.fastCos(f * 0.017453292F);
			float f1 = (float)d1 * 10.0F;
			f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
			float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
			float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;

			if (f2 < 0.0F)
				f2 = 0.0F;

			float f4 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
			f1 = (float) (f1 + Maths.fastSin((entity.prevDistanceWalkedModified + (entity.distanceWalkedModified - entity.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4);

			if (entity.isSneaking())
				f1 += 25.0F;

			GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			((ICappedModel)renderer.getMainModel()).renderCape(0.0625F, f1, f2, f3);
			GlStateManager.popMatrix();
		}
		
		/*if (entity.getOwner() != null && !entity.isInvisible() && !entity.isChild() && !entity.isWild() && renderer.getMainModel() instanceof ICappedModel && !entity.isWild() && ((AbstractClientPlayer)entity.getOwner()).hasPlayerInfo() && (((AbstractClientPlayer)entity.getOwner()).isWearing(EnumPlayerModelParts.CAPE) && (((AbstractClientPlayer)entity.getOwner()).getLocationCape() != null) || "Umbrella_Ghast".equals(entity.getOwner().getName()) || "Mrbt0907".equals(entity.getOwner().getName()) || "Milo1133".equals(entity.getOwner().getName()) || "4ChanMeta".equals(entity.getOwner().getName()) || "TheMCOverlordYT".equals(entity.getOwner().getName()) || "Entanos".equals(entity.getOwner().getName()) || "BeckBroJack".equals(entity.getOwner().getName())))
		{
			ItemStack itemstack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			
			if (itemstack.getItem() != Items.ELYTRA)
			{
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				renderer.bindTexture("Umbrella_Ghast".equals(entity.getOwner().getName()) ? ENDER_CAPE : "Mrbt0907".equals(entity.getOwner().getName()) ? MRBT_CAPE : "Milo1133".equals(entity.getOwner().getName()) ? MILO_CAPE : "4ChanMeta".equals(entity.getOwner().getName()) ? RAM_CAPE :"TheMCOverlordYT".equals(entity.getOwner().getName()) ? SOURGE_CAPE : "Entanos".equals(entity.getOwner().getName()) ? ENTANOS_CAPE : "BeckBroJack".equals(entity.getOwner().getName()) ? BECK_CAPE : "JadeRabbitTsuki".equals(entity.getOwner().getName()) ? BUNNY_CAPE : "GB_Doge_9000".equals(entity.getOwner().getName()) ? DOGGO_CAPE : ((AbstractClientPlayer)entity.getOwner()).getLocationCape());
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.0F, 0.0F, 0.125F);
				double d0 = entity.prevChasingPosX + (entity.chasingPosX - entity.prevChasingPosX) * (double)partialTicks - (entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks);
				double d1 = entity.prevChasingPosY + (entity.chasingPosY - entity.prevChasingPosY) * (double)partialTicks - (entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks);
				double d2 = entity.prevChasingPosZ + (entity.chasingPosZ - entity.prevChasingPosZ) * (double)partialTicks - (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks);
				float f = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * partialTicks;
				double d3 = (double)MathHelper.sin(f * 0.017453292F);
				double d4 = (double)(-MathHelper.cos(f * 0.017453292F));
				float f1 = (float)d1 * 10.0F;
				f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
				float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
				float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
				
				if (f2 < 0.0F)
				{
					f2 = 0.0F;
				}

				float f4 = entity.prevRotationYaw + (entity.rotationYaw - entity.rotationYaw) * partialTicks;
				f1 = f1 + MathHelper.sin((entity.prevDistanceWalkedModified + (entity.distanceWalkedModified - entity.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;
				
				if (entity.isSneaking())
				{
					f1 += 25.0F;
				}
				if (entity.isAirBorne)
				{
					f1 -= MathHelper.clamp((float)(entity.motionY * Math.PI * 45D), -75F, 3F);
				}
				if (entity.isInvisible())
				{
					f1 = 0F;
				}
				((ICappedModel)renderer.getMainModel()).renderCape(0.0625F, f1, f2, f3);
				GlStateManager.popMatrix();
			}
		}*/
	}
	
	public boolean shouldCombineTextures() {return false;}
}