package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStormHead;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelWitherStormHead
extends ModelBase
{
	public ModelRenderer BackMouth;
	public ModelRenderer Jaw;
	public ModelRenderer Head;
	public ModelRenderer JawRight;
	public ModelRenderer JawLeft;
	public ModelRenderer JawFront;
	public ModelRenderer BottomJawTooth1;
	public ModelRenderer BottomJawTooth5;
	public ModelRenderer BottomJawTooth9;
	public ModelRenderer JawRight_1;
	public ModelRenderer JawLeft_1;
	public ModelRenderer BottomJawTooth2;
	public ModelRenderer BottomJawTooth3;
	public ModelRenderer BottomJawTooth4;
	public ModelRenderer BottomJawTooth6;
	public ModelRenderer BottomJawTooth7;
	public ModelRenderer BottomJawTooth8;
	public ModelRenderer BottomJawTooth10;
	public ModelRenderer BottomJawTooth11;
	public ModelRenderer BottomJawTooth12;
	public ModelRenderer Face;
	public ModelRenderer HeadRightSide;
	public ModelRenderer HeadLeftSide;
	public ModelRenderer HeadTop;
	public ModelRenderer UpperJawTooth1;
	public ModelRenderer UpperJawTooth5;
	public ModelRenderer UpperJawTooth9;
	public ModelRenderer UpperJawTooth2;
	public ModelRenderer UpperJawTooth3;
	public ModelRenderer UpperJawTooth4;
	public ModelRenderer UpperJawTooth6;
	public ModelRenderer UpperJawTooth7;
	public ModelRenderer UpperJawTooth8;
	public ModelRenderer UpperJawTooth10;
	public ModelRenderer UpperJawTooth11;
	public ModelRenderer UpperJawTooth12;
	public ModelWitherStormHead()
	{
		this.textureWidth = 256;
		this.textureHeight = 256;
		this.UpperJawTooth4 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth4.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.UpperJawTooth4.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.JawFront = new ModelRenderer(this, 0, 0);
		this.JawFront.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.JawFront.addBox(-20.0F, 0.0F, -56.0F, 40, 12, 8, 0.0F);
		this.HeadLeftSide = new ModelRenderer(this, 0, 0);
		this.HeadLeftSide.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadLeftSide.addBox(20.0F, -28.0F, -40.0F, 8, 32, 48, 0.0F);
		this.UpperJawTooth5 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth5.setRotationPoint(24.0F, 4.0F, 0.0F);
		this.UpperJawTooth5.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.JawLeft = new ModelRenderer(this, 0, 0);
		this.JawLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.JawLeft.addBox(20.0F, 0.0F, -48.0F, 8, 12, 48, 0.0F);
		this.Face = new ModelRenderer(this, 0, 96);
		this.Face.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.Face.addBox(-20.0F, -28.0F, -48.0F, 40, 32, 8, 0.0F);
		this.UpperJawTooth10 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth10.setRotationPoint(-10.0F, 0.0F, 0.0F);
		this.UpperJawTooth10.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BottomJawTooth9 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth9.setRotationPoint(16.0F, 0.0F, -52.0F);
		this.BottomJawTooth9.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.JawLeft_1 = new ModelRenderer(this, 0, 0);
		this.JawLeft_1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.JawLeft_1.addBox(20.0F, 0.0F, -48.0F, 8, 12, 48, 0.0F);
		this.Head = new ModelRenderer(this, 0, 0);
		this.Head.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.Head.addBox(-20.0F, -28.0F, -40.0F, 40, 32, 48, 0.0F);
		this.UpperJawTooth1 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth1.setRotationPoint(-24.0F, 4.0F, 0.0F);
		this.UpperJawTooth1.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BottomJawTooth6 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth6.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.BottomJawTooth6.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.UpperJawTooth12 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth12.setRotationPoint(-10.0F, 0.0F, 0.0F);
		this.UpperJawTooth12.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BottomJawTooth2 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth2.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.BottomJawTooth2.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.UpperJawTooth2 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth2.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.UpperJawTooth2.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.HeadRightSide = new ModelRenderer(this, 0, 0);
		this.HeadRightSide.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadRightSide.addBox(-28.0F, -28.0F, -40.0F, 8, 32, 48, 0.0F);
		this.BottomJawTooth10 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth10.setRotationPoint(-10.0F, 0.0F, 0.0F);
		this.BottomJawTooth10.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BottomJawTooth1 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth1.setRotationPoint(-24.0F, 0.0F, -8.0F);
		this.BottomJawTooth1.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BottomJawTooth7 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth7.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.BottomJawTooth7.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.JawRight = new ModelRenderer(this, 0, 0);
		this.JawRight.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.JawRight.addBox(-28.0F, 0.0F, -48.0F, 8, 12, 48, 0.0F);
		this.BottomJawTooth8 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth8.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.BottomJawTooth8.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BottomJawTooth3 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth3.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.BottomJawTooth3.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.UpperJawTooth11 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth11.setRotationPoint(-10.0F, 0.0F, 0.0F);
		this.UpperJawTooth11.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BottomJawTooth4 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth4.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.BottomJawTooth4.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.JawRight_1 = new ModelRenderer(this, 0, 0);
		this.JawRight_1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.JawRight_1.addBox(-28.0F, 0.0F, -48.0F, 8, 12, 48, 0.0F);
		this.UpperJawTooth3 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth3.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.UpperJawTooth3.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BackMouth = new ModelRenderer(this, 0, 0);
		this.BackMouth.setRotationPoint(0.0F, 0.0F, -18.0F);
		this.BackMouth.addBox(-28.0F, -28.0F, -8.0F, 56, 54, 8, 0.0F);
		this.Jaw = new ModelRenderer(this, 0, 0);
		this.Jaw.setRotationPoint(0.0F, 14.0F, -4.0F);
		this.Jaw.addBox(-20.0F, 0.0F, -48.0F, 40, 12, 48, 0.0F);
		this.UpperJawTooth6 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth6.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.UpperJawTooth6.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.UpperJawTooth7 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth7.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.UpperJawTooth7.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.UpperJawTooth8 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth8.setRotationPoint(0.0F, 0.0F, -12.0F);
		this.UpperJawTooth8.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BottomJawTooth5 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth5.setRotationPoint(24.0F, 0.0F, -8.0F);
		this.BottomJawTooth5.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.HeadTop = new ModelRenderer(this, 0, 0);
		this.HeadTop.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.HeadTop.addBox(-20.0F, -36.0F, -40.0F, 40, 8, 48, 0.0F);
		this.BottomJawTooth11 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth11.setRotationPoint(-10.0F, 0.0F, 0.0F);
		this.BottomJawTooth11.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.UpperJawTooth9 = new ModelRenderer(this, 0, 88);
		this.UpperJawTooth9.setRotationPoint(16.0F, 4.0F, -44.0F);
		this.UpperJawTooth9.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4, 0.0F);
		this.BottomJawTooth12 = new ModelRenderer(this, 0, 88);
		this.BottomJawTooth12.setRotationPoint(-10.0F, 0.0F, 0.0F);
		this.BottomJawTooth12.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4, 0.0F);
		this.UpperJawTooth3.addChild(this.UpperJawTooth4);
		this.Jaw.addChild(this.JawFront);
		this.Head.addChild(this.HeadLeftSide);
		this.Head.addChild(this.UpperJawTooth5);
		this.Jaw.addChild(this.JawLeft);
		this.Head.addChild(this.Face);
		this.UpperJawTooth9.addChild(this.UpperJawTooth10);
		this.Jaw.addChild(this.BottomJawTooth9);
		this.JawFront.addChild(this.JawLeft_1);
		this.BackMouth.addChild(this.Head);
		this.Head.addChild(this.UpperJawTooth1);
		this.BottomJawTooth5.addChild(this.BottomJawTooth6);
		this.UpperJawTooth11.addChild(this.UpperJawTooth12);
		this.BottomJawTooth1.addChild(this.BottomJawTooth2);
		this.UpperJawTooth1.addChild(this.UpperJawTooth2);
		this.Head.addChild(this.HeadRightSide);
		this.BottomJawTooth9.addChild(this.BottomJawTooth10);
		this.Jaw.addChild(this.BottomJawTooth1);
		this.BottomJawTooth6.addChild(this.BottomJawTooth7);
		this.Jaw.addChild(this.JawRight);
		this.BottomJawTooth7.addChild(this.BottomJawTooth8);
		this.BottomJawTooth2.addChild(this.BottomJawTooth3);
		this.UpperJawTooth10.addChild(this.UpperJawTooth11);
		this.BottomJawTooth3.addChild(this.BottomJawTooth4);
		this.JawFront.addChild(this.JawRight_1);
		this.UpperJawTooth2.addChild(this.UpperJawTooth3);
		this.BackMouth.addChild(this.Jaw);
		this.UpperJawTooth5.addChild(this.UpperJawTooth6);
		this.UpperJawTooth6.addChild(this.UpperJawTooth7);
		this.UpperJawTooth7.addChild(this.UpperJawTooth8);
		this.Jaw.addChild(this.BottomJawTooth5);
		this.Head.addChild(this.HeadTop);
		this.BottomJawTooth10.addChild(this.BottomJawTooth11);
		this.Head.addChild(this.UpperJawTooth9);
		this.BottomJawTooth11.addChild(this.BottomJawTooth12);
	}
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.BackMouth.render(f5);
	}
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		this.BackMouth.rotateAngleY = (p_78087_4_ / 57.295776F);
		this.BackMouth.rotateAngleX = (p_78087_5_ / 57.295776F);
		this.BackMouth.rotateAngleZ = 0F;
		EntityWitherStormHead wither = (EntityWitherStormHead)p_78087_7_;
		if (wither.getJukeboxToDanceTo() != null)
		{
			BackMouth.rotateAngleX += (MathHelper.cos(p_78087_3_ * 0.5F) * 0.5F);
			BackMouth.rotateAngleZ += (MathHelper.cos(p_78087_3_ * 0.25F) * 0.5F);
		}}
			public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
			{
				EntityWitherStormHead entity = (EntityWitherStormHead)p_78086_1_;
				if (entity.openMouthCounter > 0)
				{
					this.Jaw.rotateAngleX = 0.5F;
				} else {
						this.Jaw.rotateAngleX = 0.0F;
					}
				}
			}

			
			