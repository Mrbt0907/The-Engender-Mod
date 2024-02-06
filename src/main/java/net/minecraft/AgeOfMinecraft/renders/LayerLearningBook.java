package net.minecraft.AgeOfMinecraft.renders;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityChicken;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityCow;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityOcelot;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityPig;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntityRabbit;
import net.minecraft.AgeOfMinecraft.entity.tier1.EntitySheep;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityEndermite;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityLlama;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySilverfish;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntitySquid;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityWolf;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityPolarBear;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySkeleton;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySlime;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntitySpider;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityZombie;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityBlaze;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGhast;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityIcyEnderCreeper;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPrisonSpider;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityShulker;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityWitch;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEversource;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityGhasther;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityIceGolem;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityIronGolem;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityMagmaGolem;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityPrisonGolem;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither;
import net.minecraft.AgeOfMinecraft.items.ItemLearningBook;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class LayerLearningBook implements LayerRenderer<EntityFriendlyCreature>
{
	/** The texture for the book above the enchantment table. */
	private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
	private final ModelBook modelBook = new ModelBook();
	private final RenderLiving render;
	
	public LayerLearningBook(RenderLiving p_i46105_1_)
	{
		this.render = p_i46105_1_;
	}

	public void doRenderLayer(EntityFriendlyCreature entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (!entitylivingbaseIn.isInvisible() && entitylivingbaseIn.getBookID() != 0 && !entitylivingbaseIn.getCurrentBook().isEmpty())
		{
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableNormalize();
			GlStateManager.pushMatrix();
			float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
			
			GlStateManager.translate(0.0F, (0.1F + MathHelper.sin(f * 0.1F) * 0.01F) + 0.25F, -0.725F);
			GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(210.0F, 0.0F, 0.0F, 1.0F);
			if (entitylivingbaseIn.isSneaking())
			GlStateManager.translate(0F, -0.1F, 0F);
			if (entitylivingbaseIn instanceof EntityEnderman || entitylivingbaseIn instanceof EntityIcyEnderCreeper)
			GlStateManager.translate(-0.625F, 0.675F, 0.0F);
			if (entitylivingbaseIn instanceof EntityIronGolem || entitylivingbaseIn instanceof EntityIceGolem || entitylivingbaseIn instanceof EntityMagmaGolem || entitylivingbaseIn instanceof EntityPrisonGolem)
			GlStateManager.translate(-0.825F, 0.675F, 0.0F);
			if (entitylivingbaseIn instanceof EntityCreeper)
			GlStateManager.translate(0.0F, -0.25F, 0.0F);
			if (entitylivingbaseIn instanceof EntityGuardian)
			GlStateManager.translate(-0.125F, 0.125F, 0.0F);
			if (entitylivingbaseIn instanceof EntitySilverfish || entitylivingbaseIn instanceof EntityEndermite)
			GlStateManager.translate(-0.25F, -0.825F, 0.0F);
			if ((entitylivingbaseIn instanceof EntitySpider) || entitylivingbaseIn instanceof EntityPrisonSpider)
			GlStateManager.translate(-0.725F, -0.5F, 0.0F);
			if ((entitylivingbaseIn instanceof EntityVillager || entitylivingbaseIn instanceof EntityWitch))
			GlStateManager.translate(0.25F, 0.125F, 0.0F);
			if ((entitylivingbaseIn instanceof EntityGhast) || entitylivingbaseIn instanceof EntityGhasther)
			{
				GlStateManager.translate(-0.675F, -1.5F, 0.1F);
				GlStateManager.rotate(30.0F, 0.0F, 0.0F, 1.0F);
			}
			if (entitylivingbaseIn instanceof EntitySlime)
			{
				GlStateManager.translate(-0.1F, -0.95F, 0.0F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
			}
			if (entitylivingbaseIn instanceof EntitySquid)
			{
				GlStateManager.translate(-0.125F, 0.0F, 0.0F);
				GlStateManager.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
			}
			if (entitylivingbaseIn instanceof EntityPolarBear)
			GlStateManager.translate(-0.85F, 0.0F, 0.0F);
			if (entitylivingbaseIn instanceof EntityChicken || entitylivingbaseIn instanceof EntityEversource)
			GlStateManager.translate(-0.25F, -0.325F, 0.0F);
			if (entitylivingbaseIn instanceof EntityRabbit)
			GlStateManager.translate(-0.125F, -0.875F, 0.0F);
			if (entitylivingbaseIn instanceof EntityOcelot)
			GlStateManager.translate(-0.5F, -0.5F, 0.0F);
			if (entitylivingbaseIn instanceof EntityWolf)
			GlStateManager.translate(-0.5F, -0.5F, 0.0F);
			if (entitylivingbaseIn instanceof EntityPig)
			GlStateManager.translate(-0.675F, -0.5F, 0.0F);
			if (entitylivingbaseIn instanceof EntityCow)
			GlStateManager.translate(-0.5F, 0.0F, 0.0F);
			if (entitylivingbaseIn instanceof EntitySheep)
			GlStateManager.translate(-0.5F, 0.0F, 0.0F);
			if (entitylivingbaseIn instanceof EntityLlama)
			GlStateManager.translate(-0.45F, 0.75F, 0.0F);
			if (entitylivingbaseIn instanceof EntityEnderDragon)
			{
				GlStateManager.translate(-7F, 2.5F, 0.0F);
				GlStateManager.rotate(30.0F, 0.0F, 0.0F, 1.0F);
			}
			if (entitylivingbaseIn instanceof EntityShulker)
			{
				GlStateManager.translate(-0.125F, -0.25F, 0.0F);
				GlStateManager.rotate(-30.0F, 0.0F, 0.0F, 1.0F);
			}
			if (entitylivingbaseIn instanceof EntityWither)
			GlStateManager.translate(0.0F, 0.0F, (MathHelper.sin(f * 0.1F) * 0.5F));
			if (entitylivingbaseIn.isChild() && (entitylivingbaseIn instanceof EntityBlaze || entitylivingbaseIn instanceof EntityZombie || entitylivingbaseIn instanceof EntitySkeleton))
			{
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
				GlStateManager.translate(0.125F, -1.25F, 0.0F);
			}
			switch (((ItemLearningBook)entitylivingbaseIn.getCurrentBook().getItem()).getTier())
			{
				case 1:
				{
					GlStateManager.color(0.5F, 0.75F, 1F);
				}
				break;
				case 2:
				{
					GlStateManager.color(1F, 0.5F, 0F);
				}
				break;
				case 3:
				{
					GlStateManager.color(0.8F, 0.2F, 1F);
				}
				break;
				case 4:
				{
					GlStateManager.color(0F, 0.5F, 1F);
				}
				break;
				case 5:
				{
					GlStateManager.color(0.8F, 0.9F, 0F);
				}
				break;
				case 6:
				{
					GlStateManager.color((MathHelper.cos(ageInTicks * 0.1F) * 0.5F + 1F) + 0.5F, 0.9F, 0F);
				}
				break;
				default:
				GlStateManager.color(1F, 1F, 1F);
			}
			this.render.bindTexture(TEXTURE_BOOK);
			float f3 = entitylivingbaseIn.pageFlipPrev + (entitylivingbaseIn.pageFlip - entitylivingbaseIn.pageFlipPrev) * partialTicks + 0.25F;
			float f4 = entitylivingbaseIn.pageFlipPrev + (entitylivingbaseIn.pageFlip - entitylivingbaseIn.pageFlipPrev) * partialTicks + 0.75F;
			f3 = (f3 - (float)MathHelper.fastFloor((double)f3)) * 1.6F - 0.3F;
			f4 = (f4 - (float)MathHelper.fastFloor((double)f4)) * 1.6F - 0.3F;
			
			if (f3 < 0.0F)
			{
				f3 = 0.0F;
			}

			if (f4 < 0.0F)
			{
				f4 = 0.0F;
			}

			if (f3 > 1.0F)
			{
				f3 = 1.0F;
			}

			if (f4 > 1.0F)
			{
				f4 = 1.0F;
			}
			float f5 = entitylivingbaseIn.bookSpreadPrev + (entitylivingbaseIn.bookSpread - entitylivingbaseIn.bookSpreadPrev) * partialTicks;
			this.modelBook.render(entitylivingbaseIn, f, f3, f4, f5, 0.0F, 0.0625F);
			GlStateManager.color(1F, 1F, 1F);
			GlStateManager.popMatrix();
			GlStateManager.disableNormalize();
			GlStateManager.disableRescaleNormal();
		}
	}

	public boolean shouldCombineTextures()
	{
		return false;
	}
}