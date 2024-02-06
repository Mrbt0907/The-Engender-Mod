package net.minecraft.AgeOfMinecraft.renders;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.mojang.authlib.GameProfile;

import net.minecraft.AgeOfMinecraft.entity.tier1.EntityCow;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityPig;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntitySheep;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityEndermite;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityLlama;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySilverfish;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityWolf;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityPolarBear;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySlime;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityVindicator;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityWitch;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEvoker;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityIronGolem;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityMagmaGolem;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityPrisonGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class LayerCustomHeadEngender implements LayerRenderer<EntityLivingBase>
{
	private final ModelRenderer modelRenderer;
	
	public LayerCustomHeadEngender(ModelRenderer primary)
	{
		this.modelRenderer = primary;
	}

	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		
		if (!itemstack.isEmpty() && modelRenderer != null&& !entitylivingbaseIn.isChild())
		{
			Item item = itemstack.getItem();
			Minecraft minecraft = Minecraft.getMinecraft();
			GlStateManager.pushMatrix();
			
			boolean flag = (entitylivingbaseIn instanceof EntityZombie && ((EntityZombie)entitylivingbaseIn).isVillager()) || entitylivingbaseIn instanceof EntityVillager || entitylivingbaseIn instanceof EntityWitch || entitylivingbaseIn instanceof EntityVindicator || entitylivingbaseIn instanceof EntityEvoker;
			if (entitylivingbaseIn instanceof EntityGhast)
			{
				float f = 2.0F;
				GlStateManager.translate(0.0F, 0.5F * scale, 0.0F);
				GlStateManager.scale(f, f, f);
				GlStateManager.translate(0.0F, 4.0F * scale, 0.0F);
			}
			if (entitylivingbaseIn instanceof EntityGuardian)
			{
				float f = 2.0F;
				GlStateManager.translate(0.0F, 0.5F * scale, 0.0F);
				GlStateManager.scale(f, f, f);
				GlStateManager.translate(0.0F, 4.0F * scale, 0.0F);
			}
			if (entitylivingbaseIn instanceof EntitySlime)
			{
				GlStateManager.translate(0.0F, 1.5F, 0.0F);
			}
			if ((entitylivingbaseIn instanceof EntityIronGolem) || entitylivingbaseIn instanceof EntityMagmaGolem || entitylivingbaseIn instanceof EntityPrisonGolem)
			{
				GlStateManager.translate(0.0F, -0.25F, -0.125F + (entitylivingbaseIn.rotationPitch / 180F));
			}
			if (entitylivingbaseIn instanceof EntitySpider)
			{
				GlStateManager.translate(0.0F, 0.25F, -0.25F + (entitylivingbaseIn.rotationPitch / 180F));
			}
			if (entitylivingbaseIn instanceof EntityCow || entitylivingbaseIn instanceof EntitySheep || entitylivingbaseIn instanceof EntityPig)
			{
				GlStateManager.translate(0.0F, 0.25F, (entitylivingbaseIn instanceof EntityPig ? -0.25F : -0.125F) + (entitylivingbaseIn.rotationPitch / 180F));
			}
			if (entitylivingbaseIn instanceof EntityLlama)
			{
				GlStateManager.translate(0.0F, -0.5F, -0.25F + (entitylivingbaseIn.rotationPitch / 180F));
			}
			if ((entitylivingbaseIn instanceof EntitySilverfish) || entitylivingbaseIn instanceof EntityEndermite || entitylivingbaseIn instanceof EntityWolf || entitylivingbaseIn instanceof EntityPolarBear)
			{
				GlStateManager.translate(0.0F, 0.25F, 0.0F);
			}

			this.modelRenderer.postRender(0.0625F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			
			if (item == Items.SKULL)
			{
				GlStateManager.scale(1.1875F, -1.1875F, -1.1875F);
				
				if (flag)
				{
					GlStateManager.translate(0.0F, 0.0625F, 0.0F);
				}

				GameProfile gameprofile = null;
				
				if (itemstack.hasTagCompound())
				{
					NBTTagCompound nbttagcompound = itemstack.getTagCompound();
					
					if (nbttagcompound.hasKey("SkullOwner", 10))
					{
						gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
					}
					else if (nbttagcompound.hasKey("SkullOwner", 8))
					{
						String s = nbttagcompound.getString("SkullOwner");
						
						if (!StringUtils.isBlank(s))
						{
							gameprofile = TileEntitySkull.updateGameprofile(new GameProfile((UUID)null, s));
							nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
						}
					}
				}

				TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, itemstack.getMetadata(), gameprofile, -1, limbSwing);
			}
			else if (!(item instanceof ItemArmor) || ((ItemArmor)item).getEquipmentSlot() != EntityEquipmentSlot.HEAD)
			{
				GlStateManager.translate(0.0F, -0.25F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.scale(0.625F, -0.625F, -0.625F);
				
				if (flag)
				{
					GlStateManager.translate(0.0F, 0.1875F, 0.0F);
				}

				minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.HEAD);
			}

			GlStateManager.popMatrix();
		}
	}

	public boolean shouldCombineTextures()
	{
		return false;
	}
}