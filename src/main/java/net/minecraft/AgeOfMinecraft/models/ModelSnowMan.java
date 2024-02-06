package net.minecraft.AgeOfMinecraft.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class ModelSnowMan extends ModelBase implements ICappedModel
{
	public ModelRenderer body;
	public ModelRenderer bottomBody;
	public ModelRenderer head;
	public ModelRenderer rightHand;
	public ModelRenderer leftHand;
	public ModelRenderer bipedCape;
	
	public ModelSnowMan()
	{
		this.head = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, -0.5F);
		this.head.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.rightHand = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 64);
		this.rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
		this.rightHand.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.leftHand = (new ModelRenderer(this, 32, 0)).setTextureSize(64, 64);
		this.leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, -0.5F);
		this.leftHand.setRotationPoint(0.0F, 6.0F, 0.0F);
		this.body = (new ModelRenderer(this, 0, 16)).setTextureSize(64, 64);
		this.body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, -0.5F);
		this.body.setRotationPoint(0.0F, 13.0F, 0.0F);
		this.bottomBody = (new ModelRenderer(this, 0, 36)).setTextureSize(64, 64);
		this.bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, -0.5F);
		this.bottomBody.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, 0.0F);
		this.rightHand.rotateAngleZ = 1.0F;
		this.leftHand.rotateAngleZ = -1.0F;
		this.rightHand.rotateAngleY = 0.0F + this.body.rotateAngleY;
		this.leftHand.rotateAngleY = (float)Math.PI + this.body.rotateAngleY;
	}

	/**
	* Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	* and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	* "far" arms and legs can swing at most.
	*/
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		this.head.rotateAngleY = netHeadYaw * 0.017453292F;
		this.head.rotateAngleX = headPitch * 0.017453292F;
		this.body.rotateAngleY = netHeadYaw * 0.017453292F * 0.25F;
		float f = MathHelper.sin(this.body.rotateAngleY);
		float f1 = MathHelper.cos(this.body.rotateAngleY);
		this.rightHand.rotateAngleZ = 1.0F;
		this.leftHand.rotateAngleZ = -1.0F;
		this.rightHand.rotateAngleY = 0.0F + this.body.rotateAngleY;
		this.leftHand.rotateAngleY = (float)Math.PI + this.body.rotateAngleY;
		this.rightHand.rotationPointX = f1 * 5.0F;
		this.rightHand.rotationPointZ = -f * 5.0F;
		this.leftHand.rotationPointX = -f1 * 5.0F;
		this.leftHand.rotationPointZ = f * 5.0F;
	}
	public void renderCape(float scale, float flo1, float flo2, float flo3)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 0.25F, 0.125F);
		GlStateManager.rotate(6.0F + flo2 / 2.0F + flo1, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(flo3 / 2.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(-flo3 / 2.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		this.bipedCape.render(scale);
		GlStateManager.popMatrix();
	}

	/**
	* Sets the models various rotation angles then renders the model.
	*/
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		this.body.render(scale);
		this.bottomBody.render(scale);
		this.head.render(scale);
		this.rightHand.render(scale);
		this.leftHand.render(scale);
	}
}