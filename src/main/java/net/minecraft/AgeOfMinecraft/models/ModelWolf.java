package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityWolf;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelWolf
extends ModelBase
{
	public ModelRenderer wolfHeadMain;
	public ModelRenderer wolfBody;
	public ModelRenderer wolfLeg1;
	public ModelRenderer wolfLeg2;
	public ModelRenderer wolfLeg3;
	public ModelRenderer wolfLeg4;
	ModelRenderer wolfTail;
	ModelRenderer wolfMane;
	public ModelWolf()
	{
		float f = 0.0F;
		this.wolfHeadMain = new ModelRenderer(this, 0, 0);
		this.wolfHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, f);
		this.wolfHeadMain.setRotationPoint(-1.0F, 13.5F, -7.0F);
		this.wolfBody = new ModelRenderer(this, 18, 14);
		this.wolfBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, f);
		this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
		this.wolfMane = new ModelRenderer(this, 21, 0);
		this.wolfMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, f);
		this.wolfMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
		this.wolfLeg1 = new ModelRenderer(this, 0, 18);
		this.wolfLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
		this.wolfLeg2 = new ModelRenderer(this, 0, 18);
		this.wolfLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
		this.wolfLeg3 = new ModelRenderer(this, 0, 18);
		this.wolfLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
		this.wolfLeg4 = new ModelRenderer(this, 0, 18);
		this.wolfLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
		this.wolfTail = new ModelRenderer(this, 9, 18);
		this.wolfTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
		this.wolfHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, f);
		this.wolfHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, f);
		this.wolfHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, f);
	}
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		if (this.isChild)
		{
			float f6 = 2.0F;
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 5.0F * p_78088_7_, 2.0F * p_78088_7_);
			this.wolfHeadMain.renderWithRotation(p_78088_7_);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GlStateManager.translate(0.0F, 24.0F * p_78088_7_, 0.0F);
			this.wolfBody.render(p_78088_7_);
			this.wolfLeg1.render(p_78088_7_);
			this.wolfLeg2.render(p_78088_7_);
			this.wolfLeg3.render(p_78088_7_);
			this.wolfLeg4.render(p_78088_7_);
			this.wolfTail.renderWithRotation(p_78088_7_);
			this.wolfMane.render(p_78088_7_);
			GlStateManager.popMatrix();
		}
		else
		{
			this.wolfHeadMain.renderWithRotation(p_78088_7_);
			this.wolfBody.render(p_78088_7_);
			this.wolfLeg1.render(p_78088_7_);
			this.wolfLeg2.render(p_78088_7_);
			this.wolfLeg3.render(p_78088_7_);
			this.wolfLeg4.render(p_78088_7_);
			this.wolfTail.renderWithRotation(p_78088_7_);
			this.wolfMane.render(p_78088_7_);
		}
	}
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
	{
		EntityWolf entitywolf = (EntityWolf)p_78086_1_;
		if (entitywolf.isAngry())
		{
			this.wolfTail.rotateAngleY = 0.0F;
		}
		else
		{
			this.wolfTail.rotateAngleY = (MathHelper.cos(p_78086_2_ * 0.6662F) * 0.75F * p_78086_3_);
		}
		this.wolfTail.rotateAngleX = 0.5F + entitywolf.limbSwingAmount;
		this.wolfBody.setRotationPoint(0.0F, 14.0F, 2.0F);
		this.wolfBody.rotateAngleX = 1.5707964F;
		this.wolfMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
		this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
		this.wolfTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
		this.wolfLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
		this.wolfLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
		this.wolfLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
		this.wolfLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
		this.wolfLeg1.rotateAngleX = (MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_);
		this.wolfLeg2.rotateAngleX = (MathHelper.cos(p_78086_2_ * 0.6662F + 3.1415927F) * 1.4F * p_78086_3_);
		this.wolfLeg3.rotateAngleX = (MathHelper.cos(p_78086_2_ * 0.6662F + 3.1415927F) * 1.4F * p_78086_3_);
		this.wolfLeg4.rotateAngleX = (MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_);
		this.wolfHeadMain.rotateAngleZ = (entitywolf.getInterestedAngle(p_78086_4_) + entitywolf.getShakeAngle(p_78086_4_, 0.0F));
		this.wolfMane.rotateAngleZ = entitywolf.getShakeAngle(p_78086_4_, -0.08F);
		this.wolfBody.rotateAngleZ = entitywolf.getShakeAngle(p_78086_4_, -0.16F);
		this.wolfTail.rotateAngleZ = entitywolf.getShakeAngle(p_78086_4_, -0.2F);
	}
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
		this.wolfHeadMain.rotateAngleX = (p_78087_5_ / 57.295776F);
		this.wolfHeadMain.rotateAngleY = (p_78087_4_ / 57.295776F);
		this.wolfHeadMain.setRotationPoint(0.0F, 13.5F, -7.0F);
		this.wolfBody.setRotationPoint(1.0F, 14.0F, 2.0F);
		this.wolfMane.setRotationPoint(0.0F, 14.0F, -2.0F);
		this.wolfLeg1.setRotationPoint(-1.5F, 16.0F, 7.0F);
		this.wolfLeg2.setRotationPoint(1.5F, 16.0F, 7.0F);
		this.wolfLeg3.setRotationPoint(-1.5F, 16.0F, -4.0F);
		this.wolfLeg4.setRotationPoint(1.5F, 16.0F, -4.0F);
		this.wolfTail.setRotationPoint(0.0F, 12.0F, 8.0F);
	}
}


