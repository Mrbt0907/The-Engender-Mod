package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelIceGolem extends ModelBase implements ICappedModel
{
	public ModelRenderer ironGolemHead;
	public ModelRenderer ironGolemBody;
	public ModelRenderer ironGolemRightArm;
	public ModelRenderer ironGolemLeftArm;
	public ModelRenderer ironGolemLeftLeg;
	public ModelRenderer ironGolemRightLeg;
	public ModelRenderer bipedCape;
	public ModelIceGolem()
	{
		this(0.0F);
	}
	public ModelIceGolem(float p_i1161_1_)
	{
		this(p_i1161_1_, -7.0F);
	}
	public ModelIceGolem(float p_i46362_1_, float p_i46362_2_)
	{
		short short1 = 128;
		short short2 = 128;
		this.ironGolemHead = new ModelRenderer(this).setTextureSize(short1, short2);
		this.ironGolemHead.setRotationPoint(0.0F, 0.0F + p_i46362_2_, -3.5F);
		this.ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46362_1_);
		this.ironGolemBody = new ModelRenderer(this).setTextureSize(short1, short2);
		this.ironGolemBody.setRotationPoint(0.0F, 0.0F + p_i46362_2_, 0.0F);
		this.ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, p_i46362_1_);
		this.ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, p_i46362_1_ + 0.5F);
		this.ironGolemRightArm = new ModelRenderer(this).setTextureSize(short1, short2);
		this.ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		this.ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, p_i46362_1_);
		this.ironGolemLeftArm = new ModelRenderer(this).setTextureSize(short1, short2);
		this.ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		this.ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, p_i46362_1_);
		this.ironGolemLeftLeg = new ModelRenderer(this, 0, 22).setTextureSize(short1, short2);
		this.ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + p_i46362_2_, 0.0F);
		this.ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i46362_1_);
		this.ironGolemRightLeg = new ModelRenderer(this, 0, 22).setTextureSize(short1, short2);
		this.ironGolemRightLeg.mirror = true;
		this.ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + p_i46362_2_, 0.0F);
		this.ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i46362_1_);
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46362_1_);
	}
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		this.ironGolemHead.render(p_78088_7_);
		this.ironGolemBody.render(p_78088_7_);
		this.ironGolemLeftLeg.render(p_78088_7_);
		this.ironGolemRightLeg.render(p_78088_7_);
		this.ironGolemRightArm.render(p_78088_7_);
		this.ironGolemLeftArm.render(p_78088_7_);
	}
	public void renderCape(float scale, float flo1, float flo2, float flo3)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scale(2F, 2F, 2F);
		GlStateManager.translate(0.0F, -0.275F, 0.0625F);
		GlStateManager.rotate(6.0F + flo2 / 2.0F + flo1, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(flo3 / 2.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(-flo3 / 2.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		this.bipedCape.render(scale);
		GlStateManager.popMatrix();
	}
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		this.ironGolemHead.rotateAngleY = (p_78087_4_ / 57.295776F);
		this.ironGolemHead.rotateAngleX = (p_78087_5_ / 57.295776F);
		this.ironGolemLeftLeg.rotateAngleX = (-1.5F * func_78172_a(p_78087_1_, 13.0F) * p_78087_2_);
		this.ironGolemRightLeg.rotateAngleX = (1.5F * func_78172_a(p_78087_1_, 13.0F) * p_78087_2_);
		this.ironGolemLeftLeg.rotateAngleY = 0.0F;
		this.ironGolemRightLeg.rotateAngleY = 0.0F;
		ironGolemHead.rotateAngleZ = 0F;
		this.ironGolemRightArm.rotateAngleZ = 0F;
		this.ironGolemLeftArm.rotateAngleZ = 0F;
		EntityFriendlyCreature entity = (EntityFriendlyCreature)p_78087_7_;
		if (entity.getJukeboxToDanceTo() != null)
		{
			ironGolemHead.rotateAngleZ = (MathHelper.cos(p_78087_3_ * 0.5F - 3.1415927F) * 0.25F) - (MathHelper.cos(p_78087_3_ * 0.25F) * 0.25F);
			this.ironGolemRightArm.rotateAngleX -= (MathHelper.cos(p_78087_3_ * 0.5F) * 0.5F) - (MathHelper.cos(p_78087_3_ * 0.25F - 3.1415927F) * 0.5F);
			this.ironGolemLeftArm.rotateAngleX += (MathHelper.cos(p_78087_3_ * 0.5F - 3.1415927F) * 0.25F) - (MathHelper.cos(p_78087_3_ * 0.25F) * 0.25F);
		}}
			public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
			{
				EntityFriendlyCreature entityirongolem = (EntityFriendlyCreature)p_78086_1_;
				int i = entityirongolem.getAttackTimer();
				if (entityirongolem.isSneaking())
				{
					this.ironGolemBody.rotateAngleX = 0.25F;
					this.ironGolemRightLeg.rotationPointZ = 4.0F;
					this.ironGolemLeftLeg.rotationPointZ = 4.0F;
					this.ironGolemRightArm.rotationPointZ = 2.0F;
					this.ironGolemLeftArm.rotationPointZ = 2.0F;
					this.ironGolemRightArm.rotationPointY = -6.0F;
					this.ironGolemLeftArm.rotationPointY = -6.0F;
					this.ironGolemHead.rotationPointY = -7.0F;
					this.ironGolemBody.rotationPointY = -6.0F;
				}
				else
				{
					this.ironGolemBody.rotateAngleX = 0.0F;
					this.ironGolemRightLeg.rotationPointZ = 0.0F;
					this.ironGolemLeftLeg.rotationPointZ = 0.0F;
					this.ironGolemRightArm.rotationPointZ = 0.0F;
					this.ironGolemLeftArm.rotationPointZ = 0.0F;
					this.ironGolemRightArm.rotationPointY = -7.0F;
					this.ironGolemLeftArm.rotationPointY = -7.0F;
					this.ironGolemHead.rotationPointY = -9.0F;
					this.ironGolemBody.rotationPointY = -7.0F;
				}

				if (i > 0)
				{
					this.ironGolemRightArm.rotateAngleX = (-2.0F + 1.5F * func_78172_a(i - p_78086_4_, 10.0F));
					this.ironGolemLeftArm.rotateAngleX = (-2.0F + 1.5F * func_78172_a(i - p_78086_4_, 10.0F));
				}
				else
				{
					this.ironGolemRightArm.rotateAngleX = ((-0.2F + 1.5F * func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_);
					this.ironGolemLeftArm.rotateAngleX = ((-0.2F - 1.5F * func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_);
				}
				this.ironGolemRightArm.rotateAngleY = 0F;
				this.ironGolemLeftArm.rotateAngleY = 0F;
				if (!entityirongolem.getCurrentBook().isEmpty())
				{
					this.ironGolemRightArm.rotateAngleY = (entityirongolem.bookSpread - 2F) * 0.25F;
					this.ironGolemLeftArm.rotateAngleY = (-entityirongolem.bookSpread + 2F) * 0.25F;
					this.ironGolemRightArm.rotateAngleZ = 0F;
					this.ironGolemLeftArm.rotateAngleZ = 0F;
					this.ironGolemRightArm.rotateAngleX = -1.25F + (0.1F + MathHelper.sin((float)entityirongolem.ticksExisted * 0.1F) * 0.01F);
					this.ironGolemLeftArm.rotateAngleX = -1.25F + (0.1F + MathHelper.sin((float)entityirongolem.ticksExisted * 0.1F) * 0.01F);
				}
			}
			private float func_78172_a(float p_78172_1_, float p_78172_2_)
			{
				return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
			}
		}