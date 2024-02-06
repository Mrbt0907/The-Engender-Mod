package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelWitherStormTentecle
extends ModelBase
{
	public ModelRenderer TentecleBlock;
	public ModelRenderer TentecleBase1;
	public ModelRenderer TentecleBase2;
	public ModelRenderer TentecleBase3;
	public ModelRenderer TentecleBase4;
	public ModelRenderer TentecleMiddle1;
	public ModelRenderer TentecleMiddle2;
	public ModelRenderer TentecleMiddle3;
	public ModelRenderer TentecleTip1;
	public ModelRenderer TentecleTip2;
	public ModelRenderer TentecleTip3;
	public ModelWitherStormTentecle()
	{
		this.textureWidth = 256;
		this.textureHeight = 256;
		this.TentecleBlock = new ModelRenderer(this, 0, 0);
		this.TentecleBlock.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.TentecleBlock.addBox(-32.0F, 0.0F, -32.0F, 64, 64, 64, 0.0F);
		this.TentecleTip1 = new ModelRenderer(this, 0, 0);
		this.TentecleTip1.setRotationPoint(0.0F, -148.0F, 0.0F);
		this.TentecleTip1.addBox(-10.0F, -164.0F, -10.0F, 20, 164, 20, 0.0F);
		this.TentecleTip2 = new ModelRenderer(this, 0, 0);
		this.TentecleTip2.setRotationPoint(0.0F, -164.0F, 0.0F);
		this.TentecleTip2.addBox(-8.0F, -128.0F, -8.0F, 16, 128, 16, 0.0F);
		this.TentecleMiddle3 = new ModelRenderer(this, 0, 0);
		this.TentecleMiddle3.setRotationPoint(0.0F, -128.0F, 0.0F);
		this.TentecleMiddle3.addBox(-14.0F, -148.0F, -14.0F, 28, 148, 28, 0.0F);
		this.TentecleBase3 = new ModelRenderer(this, 0, 0);
		this.TentecleBase3.setRotationPoint(0.0F, -64.0F, 0.0F);
		this.TentecleBase3.addBox(-22.0F, -64.0F, -22.0F, 44, 64, 44, 0.0F);
		this.TentecleBase1 = new ModelRenderer(this, 0, 0);
		this.TentecleBase1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.TentecleBase1.addBox(-16.0F, -128.0F, -16.0F, 32, 128, 32, 0.0F);
		this.TentecleBase4 = new ModelRenderer(this, 0, 0);
		this.TentecleBase4.setRotationPoint(0.0F, -64.0F, 0.0F);
		this.TentecleBase4.addBox(-20.0F, -96.0F, -20.0F, 40, 96, 40, 0.0F);
		this.TentecleTip3 = new ModelRenderer(this, 0, 0);
		this.TentecleTip3.setRotationPoint(0.0F, -128.0F, 0.0F);
		this.TentecleTip3.addBox(-8.0F, -128.0F, -8.0F, 16, 128, 16, 0.0F);
		this.TentecleMiddle1 = new ModelRenderer(this, 0, 0);
		this.TentecleMiddle1.setRotationPoint(0.0F, -96.0F, 0.0F);
		this.TentecleMiddle1.addBox(-18.0F, -64.0F, -18.0F, 36, 64, 36, 0.0F);
		this.TentecleMiddle2 = new ModelRenderer(this, 0, 0);
		this.TentecleMiddle2.setRotationPoint(0.0F, -64.0F, 0.0F);
		this.TentecleMiddle2.addBox(-16.0F, -128.0F, -16.0F, 32, 128, 32, 0.0F);
		this.TentecleBase2 = new ModelRenderer(this, 0, 0);
		this.TentecleBase2.setRotationPoint(0.0F, -128.0F, 0.0F);
		this.TentecleBase2.addBox(-24.0F, -64.0F, -24.0F, 48, 64, 48, 0.0F);
		this.TentecleMiddle3.addChild(this.TentecleTip1);
		this.TentecleTip1.addChild(this.TentecleTip2);
		this.TentecleMiddle2.addChild(this.TentecleMiddle3);
		this.TentecleBase2.addChild(this.TentecleBase3);
		this.TentecleBlock.addChild(this.TentecleBase1);
		this.TentecleBase3.addChild(this.TentecleBase4);
		this.TentecleTip2.addChild(this.TentecleTip3);
		this.TentecleBase4.addChild(this.TentecleMiddle1);
		this.TentecleMiddle1.addChild(this.TentecleMiddle2);
		this.TentecleBase1.addChild(this.TentecleBase2);
	}
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.TentecleBlock.render(f5);
	}
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		this.TentecleBlock.rotateAngleX = 1.575F;
		this.TentecleBase1.rotateAngleY = (0.45F * MathHelper.sin(ageInTicks * 0.031200068F + 3.9F));
		this.TentecleBase1.rotateAngleZ = (0.5F * MathHelper.sin(ageInTicks * 0.04F + 3.9F));
		this.TentecleBase2.rotateAngleZ = (0.2F * MathHelper.sin(ageInTicks * 0.04F + 3.6F));
		this.TentecleBase3.rotateAngleZ = (0.2F * MathHelper.sin(ageInTicks * 0.04F + 3.3F));
		this.TentecleBase4.rotateAngleZ = (0.2F * MathHelper.sin(ageInTicks * 0.04F + 3.0F));
		this.TentecleMiddle1.rotateAngleZ = (0.15F * MathHelper.sin(ageInTicks * 0.04F + 2.8F));
		this.TentecleMiddle2.rotateAngleZ = (0.15F * MathHelper.sin(ageInTicks * 0.04F + 2.4F));
		this.TentecleMiddle3.rotateAngleZ = (0.15F * MathHelper.sin(ageInTicks * 0.04F + 2.0F));
		this.TentecleTip1.rotateAngleZ = (0.1F * MathHelper.sin(ageInTicks * 0.04F + 1.8F));
		this.TentecleTip2.rotateAngleZ = (0.1F * MathHelper.sin(ageInTicks * 0.04F + 1.4F));
		this.TentecleTip3.rotateAngleZ = (0.2F * MathHelper.sin(ageInTicks * 0.04F + 1.0F));
		EntityFriendlyCreature entitytentacle = (EntityFriendlyCreature)entityIn;
		this.TentecleBase1.rotateAngleX = (0.5F * MathHelper.sin(ageInTicks * 0.075F + 3.9F));
		this.TentecleBase2.rotateAngleX = (0.3F * MathHelper.sin(ageInTicks * 0.075F + 3.6F));
		this.TentecleBase3.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.075F + 3.3F));
		this.TentecleBase4.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.075F + 3.0F));
		this.TentecleMiddle1.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.075F + 2.8F));
		this.TentecleMiddle2.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.075F + 2.4F));
		this.TentecleMiddle3.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.075F + 2.0F));
		this.TentecleTip1.rotateAngleX = (0.1F * MathHelper.sin(ageInTicks * 0.075F + 1.8F));
		this.TentecleTip2.rotateAngleX = (0.1F * MathHelper.sin(ageInTicks * 0.075F + 1.4F));
		this.TentecleTip3.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.075F + 1.0F));
		
		if (entitytentacle.getJukeboxToDanceTo() != null)
		{
			this.TentecleBase1.rotateAngleZ = (MathHelper.sin(ageInTicks * 0.5F) * 0.25F);
			this.TentecleBase1.rotateAngleX = (0.45F * MathHelper.sin(ageInTicks * 0.25F + 3.9F));
			this.TentecleBase2.rotateAngleX = (0.3F * MathHelper.sin(ageInTicks * 0.25F + 3.6F));
			this.TentecleBase3.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.25F + 3.3F));
			this.TentecleBase4.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.25F + 3.0F));
			this.TentecleMiddle1.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.25F + 2.8F));
			this.TentecleMiddle2.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.25F + 2.4F));
			this.TentecleMiddle3.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.25F + 2.0F));
			this.TentecleTip1.rotateAngleX = (0.1F * MathHelper.sin(ageInTicks * 0.25F + 1.8F));
			this.TentecleTip2.rotateAngleX = (0.1F * MathHelper.sin(ageInTicks * 0.25F + 1.4F));
			this.TentecleTip3.rotateAngleX = (0.2F * MathHelper.sin(ageInTicks * 0.25F + 1.0F));
		}}
			public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
			{
				modelRenderer.rotateAngleX = x;
				modelRenderer.rotateAngleY = y;
				modelRenderer.rotateAngleZ = z;
			}
		}

		
		