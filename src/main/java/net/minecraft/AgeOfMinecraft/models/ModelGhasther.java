package net.minecraft.AgeOfMinecraft.models;

import java.util.Random;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityGhasther;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
* ModelGhast - Either Mojang or a mod author
* Created using Tabula 4.1.1
*/

public class ModelGhasther extends ModelBase {
	public ModelRenderer rightface;
	public ModelRenderer leftface;
	public ModelRenderer body;
	public ModelRenderer target;
	public ModelRenderer[] tentacles = new ModelRenderer[9];
	
	public ModelGhasther()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.body = new ModelRenderer(this, 0, 0);
		this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.body.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
		this.body.rotationPointY += 8.0F;
		this.rightface = new ModelRenderer(this, 0, 0);
		this.rightface.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightface.addBox(-5.0F, -11.0F, -14.0F, 16, 16, 16, -4.0F);
		this.setRotateAngle(rightface, 0.0F, 1.5707963267948966F, 0.0F);
		this.leftface = new ModelRenderer(this, 0, 0);
		this.leftface.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftface.addBox(-7.0F, -9.0F, -13.0F, 16, 16, 16, -2.0F);
		this.setRotateAngle(leftface, 0.0F, -1.5707963267948966F, 0.0F);
		this.target = new ModelRenderer(this, 42, 0);
		this.target.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.target.addBox(-3.0F, -8.1F, -2.0F, 6, 0, 6, 0.0F);
		this.body.addChild(this.target);
		this.body.addChild(this.leftface);
		this.body.addChild(this.rightface);
		Random random = new Random(1660L);
		
		for (int j = 0; j < this.tentacles.length; ++j)
		{
			this.tentacles[j] = new ModelRenderer(this, 0, 0);
			float f = (((float)(j % 3) - (float)(j / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
			float f1 = ((float)(j / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
			int k = random.nextInt(7) + 8;
			this.tentacles[j].addBox(-1.0F, 0.0F, -1.0F, 2, k, 2);
			this.tentacles[j].rotationPointX = f;
			this.tentacles[j].rotationPointZ = f1;
			this.tentacles[j].rotationPointY = 15.0F;
			this.body.addChild(this.tentacles[j]);
		}
	}

	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, -0.45F, 0.0F);
		this.body.render(scale);
		GlStateManager.popMatrix();
	}
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		this.body.rotateAngleY = (netHeadYaw / 57.295776F);
		this.body.rotateAngleX = (headPitch / 57.295776F);
		EntityGhasther entity = (EntityGhasther)entityIn;
		for (int i = 0; i < this.tentacles.length; ++i)
		{
			this.tentacles[i].rotateAngleZ = 0F;
			this.tentacles[i].rotationPointY = 7.0F;
			this.tentacles[i].rotateAngleX = 0.2F * MathHelper.sin((entity.isAIDisabled() ? 1 : ageInTicks) * 0.3F + (float)i) + 0.4F;
		}
		if (entity.getJukeboxToDanceTo() != null)
		{
			this.body.rotateAngleX += (MathHelper.sin(ageInTicks * 0.5F) * 0.25F);
			this.body.rotateAngleY += (MathHelper.sin(ageInTicks * 0.25F) * 0.5F);
			for (int k = 0; k < this.tentacles.length; k++)
			{
				this.tentacles[k].rotateAngleZ += MathHelper.sin(k * 2F + (entity.isAIDisabled() ? 1 : ageInTicks) * 0.25F) * 0.5F;
			}
		}
		if (!entity.getCurrentBook().isEmpty())
		{
			this.tentacles[0].rotateAngleY = entity.bookSpread - 1F;
			this.tentacles[2].rotateAngleY = -entity.bookSpread + 1F;
			this.tentacles[0].rotateAngleZ = 0F;
			this.tentacles[2].rotateAngleZ = 0F;
			this.tentacles[0].rotateAngleX = -1.5F + (0.1F + MathHelper.sin((float)entity.ticksExisted * 0.1F) * 0.01F);
			this.tentacles[2].rotateAngleX = -1.5F + (0.1F + MathHelper.sin((float)entity.ticksExisted * 0.1F) * 0.01F);
		}
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
}
