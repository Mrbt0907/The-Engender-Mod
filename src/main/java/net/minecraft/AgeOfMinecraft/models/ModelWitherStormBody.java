package net.minecraft.AgeOfMinecraft.models;
import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWitherStormBody
extends ModelBase
{
	public ModelRenderer MainBody;
	public ModelRenderer TopBody1;
	public ModelRenderer TopBody4;
	public ModelRenderer TopBody7;
	public ModelRenderer TopBody10;
	public ModelRenderer TopBody12;
	public ModelRenderer TopBody14;
	public ModelRenderer TopBody2;
	public ModelRenderer TopBody3;
	public ModelRenderer TopBody5;
	public ModelRenderer TopBody6;
	public ModelRenderer TopBody8;
	public ModelRenderer TopBody9;
	public ModelRenderer TopBody11;
	public ModelRenderer TopBody13;
	public ModelRenderer TopBody15;
	public ModelWitherStormBody()
	{
		Random random = new Random(1660L);
		this.textureWidth = 1024;
		this.textureHeight = 1024;
		this.TopBody3 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody3.setRotationPoint(0.0F, -200.0F, 0.0F);
		this.TopBody3.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody3, -0.17453292F, 0.0F, 0.0F);
		this.TopBody15 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody15.setRotationPoint(0.0F, -200.0F, 0.0F);
		this.TopBody15.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody15, 0.5235988F, 0.0F, 0.0F);
		this.TopBody5 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody5.setRotationPoint(0.0F, -200.0F, 0.0F);
		this.TopBody5.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody5, -0.17453292F, 0.0F, 0.0F);
		this.TopBody11 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody11.setRotationPoint(0.0F, -200.0F, 0.0F);
		this.TopBody11.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody11, 0.34906584F, 0.0F, 0.0F);
		this.TopBody1 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.TopBody1.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody1, -0.34906584F, 0.0F, 0.0F);
		this.TopBody6 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody6.setRotationPoint(0.0F, -200.0F, 0.0F);
		this.TopBody6.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody6, -0.17453292F, 0.0F, 0.0F);
		this.TopBody8 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody8.setRotationPoint(0.0F, -200.0F, 0.0F);
		this.TopBody8.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody8, -0.17453292F, 0.0F, 0.0F);
		this.TopBody9 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody9.setRotationPoint(0.0F, -200.0F, 0.0F);
		this.TopBody9.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody9, -0.17453292F, 0.0F, 0.0F);
		this.TopBody7 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody7.setRotationPoint(210.0F, 0.0F, 8.0F);
		this.TopBody7.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody7, -0.34906584F, -0.08726646F, 0.0F);
		this.MainBody = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.MainBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.MainBody.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.TopBody14 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody14.setRotationPoint(0.0F, -32.0F, 148.0F);
		this.TopBody14.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody14, -0.6981317F, 0.0F, 0.0F);
		this.TopBody2 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody2.setRotationPoint(0.0F, -200.0F, 0.0F);
		this.TopBody2.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody2, -0.17453292F, 0.0F, 0.0F);
		this.TopBody4 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody4.setRotationPoint(-210.0F, 0.0F, 8.0F);
		this.TopBody4.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody4, -0.34906584F, 0.08726646F, 0.0F);
		this.TopBody10 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody10.setRotationPoint(-100.0F, -32.0F, 64.0F);
		this.TopBody10.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody10, -0.6981317F, -0.17453292F, 0.0F);
		this.TopBody13 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody13.setRotationPoint(0.0F, -200.0F, 0.0F);
		this.TopBody13.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody13, 0.34906584F, 0.0F, 0.0F);
		this.TopBody12 = new ModelRenderer(this, random.nextInt(256), random.nextInt(256));
		this.TopBody12.setRotationPoint(100.0F, -32.0F, 64.0F);
		this.TopBody12.addBox(-128.0F, -256.0F, -128.0F, 256, 256, 256, 0.0F);
		setRotateAngle(this.TopBody12, -0.6981317F, 0.17453292F, 0.0F);
		this.TopBody2.addChild(this.TopBody3);
		this.TopBody14.addChild(this.TopBody15);
		this.TopBody4.addChild(this.TopBody5);
		this.TopBody10.addChild(this.TopBody11);
		this.TopBody5.addChild(this.TopBody6);
		this.TopBody7.addChild(this.TopBody8);
		this.TopBody8.addChild(this.TopBody9);
		this.TopBody1.addChild(this.TopBody2);
		this.TopBody12.addChild(this.TopBody13);
	}
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.TopBody1.render(f5);
		this.TopBody7.render(f5);
		this.MainBody.render(f5);
		this.TopBody14.render(f5);
		this.TopBody4.render(f5);
		this.TopBody10.render(f5);
		this.TopBody12.render(f5);
	}
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}


