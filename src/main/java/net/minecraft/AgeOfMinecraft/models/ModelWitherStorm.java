package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWitherStorm extends net.minecraft.client.model.ModelBase
{
	public ModelRenderer CommandBlock;
	public ModelRenderer Cavity;
	public ModelRenderer Body1;
	public ModelRenderer Body2;
	public ModelRenderer Body3;
	public ModelRenderer Body4;
	public ModelRenderer Bottom1;
	public ModelRenderer Bottom2;
	public ModelRenderer Bottom3;
	public ModelRenderer Body5;
	public ModelRenderer Body6;
	public ModelRenderer Body7;
	public ModelRenderer BodyTop1;
	public ModelRenderer Body9;
	public ModelRenderer Body10;
	public ModelRenderer Body11;
	public ModelRenderer Body12;
	public ModelRenderer BodyTop7;
	public ModelRenderer BodyTop13;
	public ModelRenderer BodyLeft1;
	public ModelRenderer BodyLeft3;
	public ModelRenderer BodyLeft5;
	public ModelRenderer BodyRight1;
	public ModelRenderer BodyRight3;
	public ModelRenderer BodyRight5;
	public ModelRenderer BodyTop2;
	public ModelRenderer BodyTop3;
	public ModelRenderer BodyTop5;
	public ModelRenderer BodyTop4;
	public ModelRenderer BodyTop6;
	public ModelRenderer BodyTop8;
	public ModelRenderer BodyTop9;
	public ModelRenderer BodyTop11;
	public ModelRenderer BodyTop10;
	public ModelRenderer BodyTop12;
	public ModelRenderer BodyTop14;
	public ModelRenderer BodyTop15;
	public ModelRenderer BodyTop17;
	public ModelRenderer BodyTop16;
	public ModelRenderer BodyTop18;
	public ModelRenderer BodyLeft2;
	public ModelRenderer BodyLeft4;
	public ModelRenderer BodyLeft6;
	public ModelRenderer BodyRight2;
	public ModelRenderer BodyRight4;
	public ModelRenderer BodyRight6;
	public ModelWitherStorm()
	{
		this.textureWidth = 512;
		this.textureHeight = 512;
		this.BodyTop3 = new ModelRenderer(this, 0, 0);
		this.BodyTop3.setRotationPoint(48.0F, -44.0F, 0.0F);
		this.BodyTop3.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyTop3, 0.0F, 0.0F, 0.5235988F);
		this.Body7 = new ModelRenderer(this, 0, 128);
		this.Body7.setRotationPoint(-24.0F, 0.0F, 32.0F);
		this.Body7.addBox(-32.0F, -100.0F, 0.0F, 64, 256, 64, 0.0F);
		this.BodyRight6 = new ModelRenderer(this, 0, 0);
		this.BodyRight6.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyRight6.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.BodyTop17 = new ModelRenderer(this, 0, 0);
		this.BodyTop17.setRotationPoint(-48.0F, -44.0F, 0.0F);
		this.BodyTop17.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyTop17, 0.0F, 0.0F, -0.5235988F);
		this.BodyTop18 = new ModelRenderer(this, 0, 0);
		this.BodyTop18.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyTop18.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.BodyTop13 = new ModelRenderer(this, 0, 0);
		this.BodyTop13.setRotationPoint(0.0F, -128.0F, -32.0F);
		this.BodyTop13.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyTop13, 0.6981317F, 0.0F, 0.0F);
		this.BodyLeft1 = new ModelRenderer(this, 0, 0);
		this.BodyLeft1.setRotationPoint(100.0F, -128.0F, 0.0F);
		this.BodyLeft1.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyLeft1, 0.0F, 0.0F, 1.0471976F);
		this.BodyLeft4 = new ModelRenderer(this, 0, 0);
		this.BodyLeft4.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyLeft4.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.CommandBlock = new ModelRenderer(this, 0, 0);
		this.CommandBlock.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.CommandBlock.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
		setRotateAngle(this.CommandBlock, 0.0F, 0.0F, 0.0017453292F);
		this.Body1 = new ModelRenderer(this, 0, 128);
		this.Body1.setRotationPoint(-24.0F, 0.0F, 0.0F);
		this.Body1.addBox(-64.0F, -100.0F, -32.0F, 64, 200, 64, 0.0F);
		this.Body6 = new ModelRenderer(this, 0, 128);
		this.Body6.setRotationPoint(-16.0F, 0.0F, 8.0F);
		this.Body6.addBox(16.0F, -100.0F, 16.0F, 64, 256, 64, 0.0F);
		this.Body4 = new ModelRenderer(this, 0, 128);
		this.Body4.setRotationPoint(0.0F, 124.0F, 0.0F);
		this.Body4.addBox(-32.0F, -100.0F, -32.0F, 64, 200, 64, 0.0F);
		this.BodyTop4 = new ModelRenderer(this, 0, 0);
		this.BodyTop4.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyTop4.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.Body5 = new ModelRenderer(this, 256, 0);
		this.Body5.setRotationPoint(-32.0F, 200.0F, -32.0F);
		this.Body5.addBox(-24.0F, -100.0F, -24.0F, 48, 256, 48, 0.0F);
		this.BodyRight3 = new ModelRenderer(this, 0, 0);
		this.BodyRight3.setRotationPoint(-196.0F, -164.0F, 0.0F);
		this.BodyRight3.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyRight3, 0.0F, 0.0F, -1.3089969F);
		this.BodyTop16 = new ModelRenderer(this, 0, 0);
		this.BodyTop16.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyTop16.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.BodyRight4 = new ModelRenderer(this, 0, 0);
		this.BodyRight4.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyRight4.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.Body9 = new ModelRenderer(this, 0, 128);
		this.Body9.setRotationPoint(24.0F, 0.0F, -8.0F);
		this.Body9.addBox(0.0F, -64.0F, 0.0F, 64, 256, 64, 0.0F);
		this.BodyTop10 = new ModelRenderer(this, 0, 0);
		this.BodyTop10.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyTop10.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.Body10 = new ModelRenderer(this, 256, 0);
		this.Body10.setRotationPoint(32.0F, 200.0F, 32.0F);
		this.Body10.addBox(-24.0F, -100.0F, -24.0F, 48, 256, 48, 0.0F);
		this.BodyTop5 = new ModelRenderer(this, 0, 0);
		this.BodyTop5.setRotationPoint(-48.0F, -44.0F, 0.0F);
		this.BodyTop5.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyTop5, 0.0F, 0.0F, -0.5235988F);
		this.BodyRight2 = new ModelRenderer(this, 0, 0);
		this.BodyRight2.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyRight2.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.BodyTop1 = new ModelRenderer(this, 0, 0);
		this.BodyTop1.setRotationPoint(0.0F, -84.0F, 0.0F);
		this.BodyTop1.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.Cavity = new ModelRenderer(this, 0, 0);
		this.Cavity.setRotationPoint(0.0F, -8.0F, 0.0F);
		this.Cavity.addBox(-32.0F, -32.0F, -32.0F, 64, 64, 64, 0.0F);
		this.Body3 = new ModelRenderer(this, 0, 128);
		this.Body3.setRotationPoint(0.0F, 0.0F, -32.0F);
		this.Body3.addBox(-64.0F, -100.0F, -64.0F, 64, 240, 64, 0.0F);
		this.BodyTop8 = new ModelRenderer(this, 0, 0);
		this.BodyTop8.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyTop8.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.BodyRight1 = new ModelRenderer(this, 0, 0);
		this.BodyRight1.setRotationPoint(-100.0F, -128.0F, 0.0F);
		this.BodyRight1.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyRight1, 0.0F, 0.0F, -0.87266463F);
		this.BodyTop15 = new ModelRenderer(this, 0, 0);
		this.BodyTop15.setRotationPoint(48.0F, -44.0F, 0.0F);
		this.BodyTop15.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyTop15, 0.0F, 0.0F, 0.5235988F);
		this.Body12 = new ModelRenderer(this, 0, 128);
		this.Body12.setRotationPoint(48.0F, -24.0F, 0.0F);
		this.Body12.addBox(0.0F, 0.0F, -64.0F, 64, 240, 64, 0.0F);
		this.BodyTop2 = new ModelRenderer(this, 0, 0);
		this.BodyTop2.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyTop2.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.Bottom2 = new ModelRenderer(this, 256, 0);
		this.Bottom2.setRotationPoint(0.0F, 200.0F, 0.0F);
		this.Bottom2.addBox(-32.0F, 148.0F, -32.0F, 32, 128, 32, 0.0F);
		this.BodyTop12 = new ModelRenderer(this, 0, 0);
		this.BodyTop12.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyTop12.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.BodyTop9 = new ModelRenderer(this, 0, 0);
		this.BodyTop9.setRotationPoint(48.0F, -44.0F, 0.0F);
		this.BodyTop9.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyTop9, 0.0F, 0.0F, 0.5235988F);
		this.BodyTop11 = new ModelRenderer(this, 0, 0);
		this.BodyTop11.setRotationPoint(-48.0F, -44.0F, 0.0F);
		this.BodyTop11.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyTop11, 0.0F, 0.0F, -0.5235988F);
		this.Body11 = new ModelRenderer(this, 0, 128);
		this.Body11.setRotationPoint(48.0F, -24.0F, -32.0F);
		this.Body11.addBox(-64.0F, 0.0F, -64.0F, 64, 240, 64, 0.0F);
		this.BodyLeft5 = new ModelRenderer(this, 0, 0);
		this.BodyLeft5.setRotationPoint(196.0F, -164.0F, 0.0F);
		this.BodyLeft5.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyLeft5, 0.0F, 0.0F, 0.5235988F);
		this.BodyLeft6 = new ModelRenderer(this, 0, 0);
		this.BodyLeft6.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyLeft6.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.Bottom1 = new ModelRenderer(this, 256, 0);
		this.Bottom1.setRotationPoint(0.0F, 250.0F, 0.0F);
		this.Bottom1.addBox(-24.0F, -100.0F, -24.0F, 48, 256, 48, 0.0F);
		this.BodyTop14 = new ModelRenderer(this, 0, 0);
		this.BodyTop14.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyTop14.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.Body2 = new ModelRenderer(this, 0, 128);
		this.Body2.setRotationPoint(-24.0F, 0.0F, 32.0F);
		this.Body2.addBox(-32.0F, -100.0F, 0.0F, 64, 256, 64, 0.0F);
		this.BodyLeft3 = new ModelRenderer(this, 0, 0);
		this.BodyLeft3.setRotationPoint(196.0F, -164.0F, 0.0F);
		this.BodyLeft3.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyLeft3, 0.0F, 0.0F, 1.3089969F);
		this.BodyTop6 = new ModelRenderer(this, 0, 0);
		this.BodyTop6.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyTop6.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.BodyLeft2 = new ModelRenderer(this, 0, 0);
		this.BodyLeft2.setRotationPoint(0.0F, -112.0F, 0.0F);
		this.BodyLeft2.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		this.Bottom3 = new ModelRenderer(this, 256, 0);
		this.Bottom3.setRotationPoint(0.0F, 200.0F, 0.0F);
		this.Bottom3.addBox(0.0F, 96.0F, 0.0F, 32, 256, 32, 0.0F);
		this.BodyTop7 = new ModelRenderer(this, 0, 0);
		this.BodyTop7.setRotationPoint(0.0F, -128.0F, 32.0F);
		this.BodyTop7.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyTop7, -0.6981317F, 0.0F, 0.0F);
		this.BodyRight5 = new ModelRenderer(this, 0, 0);
		this.BodyRight5.setRotationPoint(-196.0F, -164.0F, 0.0F);
		this.BodyRight5.addBox(-64.0F, -64.0F, -64.0F, 128, 128, 128, 0.0F);
		setRotateAngle(this.BodyRight5, 0.0F, 0.0F, -0.5235988F);
		this.BodyTop1.addChild(this.BodyTop3);
		this.BodyRight5.addChild(this.BodyRight6);
		this.BodyTop13.addChild(this.BodyTop17);
		this.BodyTop17.addChild(this.BodyTop18);
		this.BodyLeft3.addChild(this.BodyLeft4);
		this.BodyTop3.addChild(this.BodyTop4);
		this.BodyTop15.addChild(this.BodyTop16);
		this.BodyRight3.addChild(this.BodyRight4);
		this.BodyTop9.addChild(this.BodyTop10);
		this.BodyTop1.addChild(this.BodyTop5);
		this.BodyRight1.addChild(this.BodyRight2);
		this.BodyTop7.addChild(this.BodyTop8);
		this.BodyTop13.addChild(this.BodyTop15);
		this.BodyTop1.addChild(this.BodyTop2);
		this.BodyTop11.addChild(this.BodyTop12);
		this.BodyTop7.addChild(this.BodyTop9);
		this.BodyTop7.addChild(this.BodyTop11);
		this.BodyLeft5.addChild(this.BodyLeft6);
		this.BodyTop13.addChild(this.BodyTop14);
		this.BodyTop5.addChild(this.BodyTop6);
		this.BodyLeft1.addChild(this.BodyLeft2);
	}
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.Body7.render(f5);
		this.BodyTop13.render(f5);
		this.BodyLeft1.render(f5);
		this.CommandBlock.render(f5);
		this.Body1.render(f5);
		this.Body6.render(f5);
		this.Body4.render(f5);
		this.Body5.render(f5);
		this.BodyRight3.render(f5);
		this.Body9.render(f5);
		this.Body10.render(f5);
		this.BodyTop1.render(f5);
		this.Cavity.render(f5);
		this.Body3.render(f5);
		this.BodyRight1.render(f5);
		this.Body12.render(f5);
		this.Bottom2.render(f5);
		this.Body11.render(f5);
		this.BodyLeft5.render(f5);
		this.Bottom1.render(f5);
		this.Body2.render(f5);
		this.BodyLeft3.render(f5);
		this.Bottom3.render(f5);
		this.BodyTop7.render(f5);
		this.BodyRight5.render(f5);
	}
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}


