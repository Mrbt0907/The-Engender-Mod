package net.minecraft.AgeOfMinecraft.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
* ModelIronGolemTitan - Either Mojang or a mod author
* Created using Tabula 5.1.0
*/

public class ModelGiantMagmaGolem extends ModelBase{
	public ModelRenderer LeftLeg1;
	public ModelRenderer RightLeg1;
	public ModelRenderer Torso;
	public ModelRenderer LeftLeg2;
	public ModelRenderer RightLeg2;
	public ModelRenderer Body;
	public ModelRenderer Head;
	public ModelRenderer LeftArm1;
	public ModelRenderer RightArm1;
	public ModelRenderer Nose;
	public ModelRenderer LeftArm2;
	public ModelRenderer LeftArm3;
	public ModelRenderer LeftArm4;
	public ModelRenderer RightArm2;
	public ModelRenderer RightArm3;
	public ModelRenderer RightArm4;
	
	public ModelGiantMagmaGolem()
	{
		this.textureWidth = 128;
		this.textureHeight = 64;
		this.LeftArm4 = new ModelRenderer(this, 58, 29);
		this.LeftArm4.mirror = true;
		this.LeftArm4.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.LeftArm4.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
		this.Torso = new ModelRenderer(this, 0, 41);
		this.Torso.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.Torso.addBox(-4.5F, -5.0F, -3.0F, 9, 5, 6, 0.5F);
		this.RightLeg2 = new ModelRenderer(this, 98, 0);
		this.RightLeg2.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.RightLeg2.addBox(-3.0F, 0.0F, -2.5F, 6, 8, 5, 0.0F);
		this.RightArm1 = new ModelRenderer(this, 58, 13);
		this.RightArm1.setRotationPoint(-9.0F, -9.0F, 0.0F);
		this.RightArm1.addBox(-4.0F, -3.0F, -3.0F, 4, 6, 6, 0.0F);
		this.RightArm2 = new ModelRenderer(this, 78, 13);
		this.RightArm2.setRotationPoint(-2.0F, 2.0F, 0.0F);
		this.RightArm2.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
		this.RightArm3 = new ModelRenderer(this, 98, 13);
		this.RightArm3.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.RightArm3.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
		this.LeftArm3 = new ModelRenderer(this, 98, 13);
		this.LeftArm3.mirror = true;
		this.LeftArm3.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.LeftArm3.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
		this.Head = new ModelRenderer(this, 0, 0);
		this.Head.setRotationPoint(0.0F, -12.0F, -3.0F);
		this.Head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
		this.Body = new ModelRenderer(this, 0, 18);
		this.Body.setRotationPoint(0.0F, -5.0F, 0.0F);
		this.Body.addBox(-9.0F, -12.0F, -6.0F, 18, 12, 11, 0.0F);
		this.RightLeg1 = new ModelRenderer(this, 76, 0);
		this.RightLeg1.setRotationPoint(-4.0F, 8.0F, 0.0F);
		this.RightLeg1.addBox(-3.0F, 0.0F, -2.5F, 6, 8, 5, 0.0F);
		this.LeftArm2 = new ModelRenderer(this, 78, 13);
		this.LeftArm2.mirror = true;
		this.LeftArm2.setRotationPoint(2.0F, 2.0F, 0.0F);
		this.LeftArm2.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
		this.LeftArm1 = new ModelRenderer(this, 58, 13);
		this.LeftArm1.mirror = true;
		this.LeftArm1.setRotationPoint(9.0F, -9.0F, 0.0F);
		this.LeftArm1.addBox(0.0F, -3.0F, -3.0F, 4, 6, 6, 0.0F);
		this.Nose = new ModelRenderer(this, 0, 0);
		this.Nose.setRotationPoint(0.0F, 0.0F, -5.0F);
		this.Nose.addBox(-1.0F, -3.0F, -1.0F, 2, 4, 2, 0.0F);
		this.RightArm4 = new ModelRenderer(this, 58, 29);
		this.RightArm4.setRotationPoint(0.0F, 7.0F, 0.0F);
		this.RightArm4.addBox(-2.0F, 0.0F, -3.0F, 4, 10, 6, 0.0F);
		this.LeftLeg1 = new ModelRenderer(this, 32, 0);
		this.LeftLeg1.mirror = true;
		this.LeftLeg1.setRotationPoint(4.0F, 8.0F, 0.0F);
		this.LeftLeg1.addBox(-3.0F, 0.0F, -2.5F, 6, 8, 5, 0.0F);
		this.LeftLeg2 = new ModelRenderer(this, 54, 0);
		this.LeftLeg2.mirror = true;
		this.LeftLeg2.setRotationPoint(0.0F, 8.0F, 0.0F);
		this.LeftLeg2.addBox(-3.0F, 0.0F, -2.5F, 6, 8, 5, 0.0F);
		this.LeftArm3.addChild(this.LeftArm4);
		this.RightLeg1.addChild(this.RightLeg2);
		this.Body.addChild(this.RightArm1);
		this.RightArm1.addChild(this.RightArm2);
		this.RightArm2.addChild(this.RightArm3);
		this.LeftArm2.addChild(this.LeftArm3);
		this.Body.addChild(this.Head);
		this.Torso.addChild(this.Body);
		this.LeftArm1.addChild(this.LeftArm2);
		this.Body.addChild(this.LeftArm1);
		this.Head.addChild(this.Nose);
		this.RightArm3.addChild(this.RightArm4);
		this.LeftLeg1.addChild(this.LeftLeg2);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5){this.Torso.render(f5);
	this.RightLeg1.render(f5);
	this.LeftLeg1.render(f5);
}

public void setAngles()
{
	this.setRotateAngle(Body, 0F, 0F, 0F);
	this.setRotateAngle(Torso, 0F, 0F, 0F);
	this.setRotateAngle(LeftLeg1, 0F, 0F, 0F);
	this.setRotateAngle(RightLeg1, 0F, 0F, 0F);
	this.setRotateAngle(LeftLeg2, 0F, 0F, 0F);
	this.setRotateAngle(RightLeg2, 0F, 0F, 0F);
	this.setRotateAngle(LeftArm1, 0F, 0F, 0F);
	this.setRotateAngle(RightArm1, 0F, 0F, 0F);
	this.setRotateAngle(LeftArm2, 0F, 0F, 0F);
	this.setRotateAngle(RightArm2, 0F, 0F, 0F);
	this.setRotateAngle(LeftArm3, 0F, 0F, 0F);
	this.setRotateAngle(RightArm3, 0F, 0F, 0F);
	this.setRotateAngle(LeftArm4, 0F, 0F, 0F);
	this.setRotateAngle(RightArm4, 0F, 0F, 0F);
	this.RightArm1.setRotationPoint(-9.0F, -9.0F, 0.0F);
	this.RightArm2.setRotationPoint(-2.0F, 1.0F, 0.0F);
	this.RightArm3.setRotationPoint(0.0F, 8.0F, 0.0F);
	this.RightArm4.setRotationPoint(0.0F, 8.0F, 0.0F);
	this.LeftArm1.setRotationPoint(9.0F, -9.0F, 0.0F);
	this.LeftArm2.setRotationPoint(2.0F, 1.0F, 0.0F);
	this.LeftArm3.setRotationPoint(0.0F, 8.0F, 0.0F);
	this.LeftArm4.setRotationPoint(0.0F, 8.0F, 0.0F);
	this.Nose.setRotationPoint(0.0F, 0.0F, -5.0F);
	this.Head.setRotationPoint(0.0F, -12.0F, -3.0F);
	this.Body.setRotationPoint(0.0F, -5.0F, 0.0F);
	this.Torso.setRotationPoint(0.0F, 8.0F, 0.0F);
	this.RightLeg1.setRotationPoint(-4.0F, 8.0F, 0.0F);
	this.RightLeg2.setRotationPoint(0.0F, 8.0F, 0.0F);
	this.LeftLeg1.setRotationPoint(4.0F, 8.0F, 0.0F);
	this.LeftLeg2.setRotationPoint(0.0F, 8.0F, 0.0F);
}
/**
* This is a helper function from Tabula to set the rotation of model parts
*/
public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
{
	modelRenderer.rotateAngleX = x;
	modelRenderer.rotateAngleY = y;
	modelRenderer.rotateAngleZ = z;
}
public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
{
	this.setAngles();
	float fo = 0.6662F / 3;
	
	this.Head.rotateAngleY = (netHeadYaw / 57.295776F);
	this.Head.rotateAngleX = (headPitch / 57.295776F);
	this.Torso.rotateAngleZ = (MathHelper.cos(limbSwing * fo) * 0.2F * limbSwingAmount);
	this.Body.rotateAngleZ = (MathHelper.cos(limbSwing * fo - 1F) * 0.2F * limbSwingAmount);
	this.RightLeg1.rotateAngleX = (MathHelper.cos(limbSwing * fo - 0.5F) * 0.75F * limbSwingAmount);
	this.LeftLeg1.rotateAngleX = (MathHelper.cos(limbSwing * fo + 2.6415927F) * 0.75F * limbSwingAmount);
	this.RightLeg2.rotateAngleX = (MathHelper.cos(limbSwing * fo + 3.1415927F) * 0.75F * limbSwingAmount);
	this.LeftLeg2.rotateAngleX = (MathHelper.cos(limbSwing * fo) * 0.75F * limbSwingAmount);
	this.RightArm1.rotateAngleX = (MathHelper.cos(limbSwing * fo + 3.1415927F) * 0.5F * limbSwingAmount);
	this.RightArm2.rotateAngleX = (MathHelper.cos(limbSwing * fo + 2.1415927F) * 0.5F * limbSwingAmount);
	this.RightArm3.rotateAngleX = (MathHelper.cos(limbSwing * fo + 1.6415927F) * 0.5F * limbSwingAmount);
	this.RightArm4.rotateAngleX = (MathHelper.cos(limbSwing * fo + 1.1415927F) * 0.5F * limbSwingAmount);
	this.LeftArm1.rotateAngleX = (MathHelper.cos(limbSwing * fo) * 0.5F * limbSwingAmount);
	this.LeftArm2.rotateAngleX = (MathHelper.cos(limbSwing * fo - 1.0F) * 0.5F * limbSwingAmount);
	this.LeftArm3.rotateAngleX = (MathHelper.cos(limbSwing * fo - 1.5F) * 0.5F * limbSwingAmount);
	this.LeftArm4.rotateAngleX = (MathHelper.cos(limbSwing * fo - 2.0F) * 0.5F * limbSwingAmount);
	
	if (this.RightLeg2.rotateAngleX < 0.0F)
	this.RightLeg2.rotateAngleX = 0.0F;
	if (this.LeftLeg2.rotateAngleX < 0.0F)
	this.LeftLeg2.rotateAngleX = 0.0F;
	if (this.RightArm2.rotateAngleX > 0.0F)
	{
		this.RightArm2.rotateAngleX = 0.0F;
	}
	if (this.LeftArm2.rotateAngleX > 0.0F)
	{
		this.LeftArm2.rotateAngleX = 0.0F;
	}
	if (this.RightArm3.rotateAngleX > 0.0F)
	{
		this.RightArm3.rotateAngleX = 0.0F;
	}
	if (this.LeftArm3.rotateAngleX > 0.0F)
	{
		this.LeftArm3.rotateAngleX = 0.0F;
	}
	if (this.RightArm4.rotateAngleX > 0.0F)
	{
		this.RightArm4.rotateAngleX = 0.0F;
	}
	if (this.LeftArm4.rotateAngleX > 0.0F)
	{
		this.LeftArm4.rotateAngleX = 0.0F;
	}
	this.RightLeg1.setRotationPoint(-4.0F, Torso.rotationPointY, 0.0F);
	this.LeftLeg1.setRotationPoint(4.0F, Torso.rotationPointY, 0.0F);
}
}
