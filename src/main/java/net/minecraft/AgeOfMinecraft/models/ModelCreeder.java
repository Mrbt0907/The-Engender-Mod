package net.minecraft.AgeOfMinecraft.models;

import net.minecraft.AgeOfMinecraft.entity.tier4.EntityCreeder;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;


public class ModelCreeder extends ModelBase implements ICappedModel
{
	public ModelRenderer body;
	public ModelRenderer rightleg1;
	public ModelRenderer rightleg2;
	public ModelRenderer rightleg3;
	public ModelRenderer leftleg1;
	public ModelRenderer leftleg2;
	public ModelRenderer leftleg3;
	public ModelRenderer head;
	public boolean isSneak;
	public ModelRenderer bipedCape;
	
	public ModelCreeder()
	{
		this(0F);
	}
	public ModelCreeder(float p_i46366_1_)
	{
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.rightleg2 = new ModelRenderer(this, 18, 0);
		this.rightleg2.setRotationPoint(-4.0F, 15.0F, 0.0F);
		this.rightleg2.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, p_i46366_1_);
		this.setRotateAngle(rightleg2, 0.0F, 0.0F, -0.5811946409141118F);
		this.rightleg1 = new ModelRenderer(this, 18, 0);
		this.rightleg1.setRotationPoint(-4.0F, 15.0F, -1.0F);
		this.rightleg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, p_i46366_1_);
		this.setRotateAngle(rightleg1, 0.0F, -0.7853981633974483F, -0.7853981633974483F);
		this.rightleg3 = new ModelRenderer(this, 18, 0);
		this.rightleg3.setRotationPoint(-4.0F, 15.0F, 1.0F);
		this.rightleg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, p_i46366_1_);
		this.setRotateAngle(rightleg3, 0.0F, 0.7853981633974483F, -0.7853981633974483F);
		this.leftleg1 = new ModelRenderer(this, 18, 0);
		this.leftleg1.mirror = true;
		this.leftleg1.setRotationPoint(4.0F, 15.0F, -1.0F);
		this.leftleg1.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, p_i46366_1_);
		this.setRotateAngle(leftleg1, 0.0F, 0.7853981633974483F, 0.7853981633974483F);
		this.leftleg2 = new ModelRenderer(this, 18, 0);
		this.leftleg2.mirror = true;
		this.leftleg2.setRotationPoint(4.0F, 15.0F, 0.0F);
		this.leftleg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, p_i46366_1_);
		this.setRotateAngle(leftleg2, 0.0F, 0.0F, 0.5811946409141118F);
		this.leftleg3 = new ModelRenderer(this, 18, 0);
		this.leftleg3.mirror = true;
		this.leftleg3.setRotationPoint(4.0F, 15.0F, 1.0F);
		this.leftleg3.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, p_i46366_1_);
		this.setRotateAngle(leftleg3, 0.0F, -0.7853981633974483F, 0.7853981633974483F);
		this.head = new ModelRenderer(this, 32, 4);
		this.head.setRotationPoint(0.0F, 4.0F, 0.0F);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46366_1_);
		this.body = new ModelRenderer(this, 0, 4);
		this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.body.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4, p_i46366_1_);
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46366_1_);
	}
	public void renderCape(float scale, float flo1, float flo2, float flo3)
	{
		if (this.isChild)
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 1.0F, -0.025F);
			GlStateManager.rotate(6.0F + flo2 / 2.0F + flo1, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(flo3 / 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(-flo3 / 2.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			this.bipedCape.render(scale);
			GlStateManager.popMatrix();
		}
		else
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, isSneak ? 0.425F : 0.375F, isSneak ? -0.25F : -0.025F);
			GlStateManager.rotate(6.0F + flo2 / 2.0F + flo1, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(flo3 / 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(-flo3 / 2.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			this.bipedCape.render(scale);
			GlStateManager.popMatrix();
		}
	}

	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();
		
		if (this.isChild)
		{
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
			this.head.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.body.render(scale);
			this.rightleg1.render(scale);
			this.rightleg2.render(scale);
			this.rightleg3.render(scale);
			this.leftleg1.render(scale);
			this.leftleg2.render(scale);
			this.leftleg3.render(scale);
		}
		else
		{
			this.head.render(scale);
			this.body.render(scale);
			this.rightleg1.render(scale);
			this.rightleg2.render(scale);
			this.rightleg3.render(scale);
			this.leftleg1.render(scale);
			this.leftleg2.render(scale);
			this.leftleg3.render(scale);
		}

		GlStateManager.popMatrix();
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
		float l = 0.7853981633974483F;
		float lc = 0.5811946409141118F;
		this.rightleg1.rotateAngleZ = -l;
		this.rightleg2.rotateAngleZ = -lc;
		this.rightleg3.rotateAngleZ = -l;
		this.leftleg1.rotateAngleZ = l;
		this.leftleg2.rotateAngleZ = lc;
		this.leftleg3.rotateAngleZ = l;
		float f = MathHelper.sin(this.swingProgress * 3.1415927F);
		MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
		this.head.rotateAngleY = (netHeadYaw * 0.017453292F);
		this.head.rotateAngleX = (headPitch * 0.017453292F) + f;
		
		this.head.rotationPointX = 0F;
		if (this.isSneak)
		{
			this.body.rotateAngleX = 0.5F;
			this.head.rotationPointY = 6.0F;
			this.head.rotationPointZ = -7.0F;
		}
		else
		{
			this.body.rotateAngleX = 0.0F;
			this.head.rotationPointY = 4.0F;
			this.head.rotationPointZ = 0.0F;
		}
		EntityCreeder entity = (EntityCreeder)entityIn;
		this.isChild = entity.isChild();
		this.isSneak = entity.isSneaking();
		if (entity.isBurning() || (!entity.isEntityAlive() && !entity.onGround))
		{
			this.head.rotateAngleX -= 0.5F;
			this.head.rotateAngleY += MathHelper.cos(ageInTicks * 0.6662F) * 0.5F;
		}
		if (entity.getJukeboxToDanceTo() != null)
		{
			float fl = MathHelper.sin(ageInTicks * 0.5F) * 0.7F;
			this.head.rotationPointX += (MathHelper.cos(ageInTicks * 0.25F) * 1F);
			this.head.rotationPointY -= fl;
		}

		this.rightleg1.rotateAngleY = -l;
		this.rightleg2.rotateAngleY = 0;
		this.rightleg3.rotateAngleY = l;
		this.leftleg1.rotateAngleY = l;
		this.leftleg2.rotateAngleY = 0;
		this.leftleg3.rotateAngleY = -l;
		float f3 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
		float f4 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * limbSwingAmount;
		float f5 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * limbSwingAmount;
		float f7 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
		float f8 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 3.1415927F) * 0.4F) * limbSwingAmount;
		float f9 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 1.5707964F) * 0.4F) * limbSwingAmount;
		this.rightleg1.rotateAngleY += f3;
		this.leftleg1.rotateAngleY += -f3;
		this.rightleg2.rotateAngleY += f4;
		this.leftleg2.rotateAngleY += -f4;
		this.rightleg3.rotateAngleY += f5;
		this.leftleg3.rotateAngleY += -f5;
		this.rightleg1.rotateAngleZ += f7;
		this.leftleg1.rotateAngleZ += -f7;
		this.rightleg2.rotateAngleZ += f8;
		this.leftleg2.rotateAngleZ += -f8;
		this.rightleg3.rotateAngleZ += f9;
		this.leftleg3.rotateAngleZ += -f9;
	}
}
