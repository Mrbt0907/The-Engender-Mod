package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.models.ICappedModel;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class LayerMobCape implements LayerRenderer<EntityFriendlyCreature>
{
	@SuppressWarnings("unused")
	private static final ResourceLocation TEST_CAPE = new ResourceLocation("ageofminecraft", "textures/test_cape.png");
	private static final ResourceLocation DOGGO_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_sh.png");
	private static final ResourceLocation BUNNY_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_jd.png");
	private static final ResourceLocation MRBT_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_bt.png");
	private static final ResourceLocation BECK_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_bbj.png");
	private static final ResourceLocation ENDER_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_ug.png");
	private static final ResourceLocation ENTANOS_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_en.png");
	private static final ResourceLocation MILO_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_m.png");
	private static final ResourceLocation RAM_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_4c.png");
	private static final ResourceLocation SOURGE_CAPE = new ResourceLocation("ageofminecraft", "textures/cape_mc.png");
	private final RenderLivingBase<? extends EntityFriendlyCreature> playerRenderer;
	
	public LayerMobCape(RenderLivingBase<? extends EntityFriendlyCreature> playerRendererIn)
	{
		this.playerRenderer = playerRendererIn;
	}
	public void doRenderLayer(EntityFriendlyCreature entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (entitylivingbaseIn.getOwner() != null && !entitylivingbaseIn.isInvisible() && !entitylivingbaseIn.isChild() && !entitylivingbaseIn.isWild() && this.playerRenderer.getMainModel() instanceof ICappedModel && !entitylivingbaseIn.isWild() && ((AbstractClientPlayer)entitylivingbaseIn.getOwner()).hasPlayerInfo() && (((AbstractClientPlayer)entitylivingbaseIn.getOwner()).isWearing(EnumPlayerModelParts.CAPE) && (((AbstractClientPlayer)entitylivingbaseIn.getOwner()).getLocationCape() != null) || "Umbrella_Ghast".equals(entitylivingbaseIn.getOwner().getName()) || "Mrbt0907".equals(entitylivingbaseIn.getOwner().getName()) || "Milo1133".equals(entitylivingbaseIn.getOwner().getName()) || "4ChanMeta".equals(entitylivingbaseIn.getOwner().getName()) || "TheMCOverlordYT".equals(entitylivingbaseIn.getOwner().getName()) || "Entanos".equals(entitylivingbaseIn.getOwner().getName()) || "BeckBroJack".equals(entitylivingbaseIn.getOwner().getName())))
		{
			ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			
			if (itemstack.getItem() != Items.ELYTRA)
			{
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				this.playerRenderer.bindTexture("Umbrella_Ghast".equals(entitylivingbaseIn.getOwner().getName()) ? ENDER_CAPE : "Mrbt0907".equals(entitylivingbaseIn.getOwner().getName()) ? MRBT_CAPE : "Milo1133".equals(entitylivingbaseIn.getOwner().getName()) ? MILO_CAPE : "4ChanMeta".equals(entitylivingbaseIn.getOwner().getName()) ? RAM_CAPE :"TheMCOverlordYT".equals(entitylivingbaseIn.getOwner().getName()) ? SOURGE_CAPE : "Entanos".equals(entitylivingbaseIn.getOwner().getName()) ? ENTANOS_CAPE : "BeckBroJack".equals(entitylivingbaseIn.getOwner().getName()) ? BECK_CAPE : "JadeRabbitTsuki".equals(entitylivingbaseIn.getOwner().getName()) ? BUNNY_CAPE : "GB_Doge_9000".equals(entitylivingbaseIn.getOwner().getName()) ? DOGGO_CAPE : ((AbstractClientPlayer)entitylivingbaseIn.getOwner()).getLocationCape());
				GlStateManager.pushMatrix();
				GlStateManager.translate(0.0F, 0.0F, 0.125F);
				double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double)partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)partialTicks);
				double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double)partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)partialTicks);
				double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double)partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)partialTicks);
				float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
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

				float f4 = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.rotationYaw) * partialTicks;
				f1 = f1 + MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;
				
				if (entitylivingbaseIn.isSneaking())
				{
					f1 += 25.0F;
				}
				if (entitylivingbaseIn.isAirBorne)
				{
					f1 -= MathHelper.clamp((float)(entitylivingbaseIn.motionY * Math.PI * 45D), -75F, 3F);
				}
				if (entitylivingbaseIn.isInvisible())
				{
					f1 = 0F;
				}
				((ICappedModel)this.playerRenderer.getMainModel()).renderCape(0.0625F, f1, f2, f3);
				GlStateManager.popMatrix();
			}
		}
	}

	public boolean shouldCombineTextures()
	{
		return false;
	}
}