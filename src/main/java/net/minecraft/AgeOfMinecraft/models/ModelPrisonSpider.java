package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPrisonSpider;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelPrisonSpider
extends ModelBase
{
	public ModelRenderer spiderHead;
	public ModelRenderer spiderNeck;
	public ModelRenderer spiderBody;
	public ModelRenderer spiderLeg1;
	public ModelRenderer spiderLeg2;
	public ModelRenderer spiderLeg3;
	public ModelRenderer spiderLeg4;
	public ModelRenderer spiderLeg5;
	public ModelRenderer spiderLeg6;
	public ModelRenderer spiderLeg7;
	public ModelRenderer spiderLeg8;
	public ModelPrisonSpider()
	{
		float f = 0.0F;
		int i = 15;
		this.spiderHead = new ModelRenderer(this, 32, 4);
		this.spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f);
		this.spiderHead.setRotationPoint(0.0F, i, -3.0F);
		this.spiderNeck = new ModelRenderer(this, 0, 0);
		this.spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f);
		this.spiderNeck.setRotationPoint(0.0F, i, 0.0F);
		this.spiderBody = new ModelRenderer(this, 0, 12);
		this.spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f);
		this.spiderBody.setRotationPoint(0.0F, i, 9.0F);
		this.spiderLeg1 = new ModelRenderer(this, 18, 0);
		this.spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg1.setRotationPoint(-4.0F, i, 2.0F);
		this.spiderLeg2 = new ModelRenderer(this, 18, 0);
		this.spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg2.setRotationPoint(4.0F, i, 2.0F);
		this.spiderLeg3 = new ModelRenderer(this, 18, 0);
		this.spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg3.setRotationPoint(-4.0F, i, 1.0F);
		this.spiderLeg4 = new ModelRenderer(this, 18, 0);
		this.spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg4.setRotationPoint(4.0F, i, 1.0F);
		this.spiderLeg5 = new ModelRenderer(this, 18, 0);
		this.spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg5.setRotationPoint(-4.0F, i, 0.0F);
		this.spiderLeg6 = new ModelRenderer(this, 18, 0);
		this.spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg6.setRotationPoint(4.0F, i, 0.0F);
		this.spiderLeg7 = new ModelRenderer(this, 18, 0);
		this.spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg7.setRotationPoint(-4.0F, i, -1.0F);
		this.spiderLeg8 = new ModelRenderer(this, 18, 0);
		this.spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
		this.spiderLeg8.setRotationPoint(4.0F, i, -1.0F);
		this.spiderLeg2.mirror = true;
		this.spiderLeg4.mirror = true;
		this.spiderLeg6.mirror = true;
		this.spiderLeg8.mirror = true;
	}
	public void render(Entity entityIn, float p_78088_2_, float limbSwing, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		setRotationAngles(p_78088_2_, limbSwing, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		this.spiderHead.render(scale);
		this.spiderNeck.render(scale);
		this.spiderBody.render(scale);
		this.spiderLeg1.render(scale);
		this.spiderLeg2.render(scale);
		this.spiderLeg3.render(scale);
		this.spiderLeg4.render(scale);
		this.spiderLeg5.render(scale);
		this.spiderLeg6.render(scale);
		this.spiderLeg7.render(scale);
		this.spiderLeg8.render(scale);
	}
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		float swing = MathHelper.sin(this.swingProgress * 3.1415927F);
		this.spiderHead.rotateAngleY = (netHeadYaw * 0.017453292F);
		this.spiderHead.rotateAngleX = (headPitch * 0.017453292F) + swing;
		float f = 0.7853982F;
		this.spiderLeg1.rotateAngleZ = (-f);
		this.spiderLeg2.rotateAngleZ = f;
		this.spiderLeg3.rotateAngleZ = (-f * 0.74F);
		this.spiderLeg4.rotateAngleZ = (f * 0.74F);
		this.spiderLeg5.rotateAngleZ = (-f * 0.74F);
		this.spiderLeg6.rotateAngleZ = (f * 0.74F);
		this.spiderLeg7.rotateAngleZ = (-f) - swing;
		this.spiderLeg8.rotateAngleZ = f + swing;
		float f1 = -0.0F;
		float f2 = 0.3926991F;
		this.spiderLeg1.rotateAngleY = (f2 * 2.0F + f1);
		this.spiderLeg2.rotateAngleY = (-f2 * 2.0F - f1);
		this.spiderLeg3.rotateAngleY = (f2 * 1.0F + f1);
		this.spiderLeg4.rotateAngleY = (-f2 * 1.0F - f1);
		this.spiderLeg5.rotateAngleY = (-f2 * 1.0F + f1);
		this.spiderLeg6.rotateAngleY = (f2 * 1.0F - f1);
		this.spiderLeg7.rotateAngleY = (-f2 * 2.0F + f1) - swing;
		this.spiderLeg8.rotateAngleY = (f2 * 2.0F - f1) + swing;
		EntityPrisonSpider entityskeleton = (EntityPrisonSpider)entityIn;
		float f3 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * limbSwingAmount;
		float f4 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * limbSwingAmount;
		float f5 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * limbSwingAmount;
		float f6 = -(MathHelper.cos(limbSwing * 0.6662F * 2.0F + 4.712389F) * 0.4F) * limbSwingAmount;
		float f7 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 0.0F) * 0.4F) * limbSwingAmount;
		float f8 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 3.1415927F) * 0.4F) * limbSwingAmount;
		float f9 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 1.5707964F) * 0.4F) * limbSwingAmount;
		float f10 = Math.abs(MathHelper.sin(limbSwing * 0.6662F + 4.712389F) * 0.4F) * limbSwingAmount;
		this.spiderLeg1.rotateAngleY += f3;
		this.spiderLeg2.rotateAngleY += -f3;
		this.spiderLeg3.rotateAngleY += f4;
		this.spiderLeg4.rotateAngleY += -f4;
		this.spiderLeg5.rotateAngleY += f5;
		this.spiderLeg6.rotateAngleY += -f5;
		this.spiderLeg7.rotateAngleY += f6;
		this.spiderLeg8.rotateAngleY += -f6;
		this.spiderLeg1.rotateAngleZ += f7;
		this.spiderLeg2.rotateAngleZ += -f7;
		this.spiderLeg3.rotateAngleZ += f8;
		this.spiderLeg4.rotateAngleZ += -f8;
		this.spiderLeg5.rotateAngleZ += f9;
		this.spiderLeg6.rotateAngleZ += -f9;
		this.spiderLeg7.rotateAngleZ += f10;
		this.spiderLeg8.rotateAngleZ += -f10;
		if (entityskeleton.isSneaking())
		{
			this.spiderLeg1.rotateAngleY += 0.2F;
			this.spiderLeg2.rotateAngleY += -0.2F;
			this.spiderLeg3.rotateAngleY += 0.2F;
			this.spiderLeg4.rotateAngleY += -0.2F;
			this.spiderLeg5.rotateAngleY += 0.2F;
			this.spiderLeg6.rotateAngleY += -0.2F;
			this.spiderLeg7.rotateAngleY += 0.2F;
			this.spiderLeg8.rotateAngleY += -0.2F;
			this.spiderLeg1.rotateAngleZ -= f7 + -0.35F;
			this.spiderLeg2.rotateAngleZ -= -f7 + 0.35F;
			this.spiderLeg3.rotateAngleZ -= f8 + -0.3F;
			this.spiderLeg4.rotateAngleZ -= -f8 + 0.3F;
			this.spiderLeg5.rotateAngleZ -= f9 + -0.3F;
			this.spiderLeg6.rotateAngleZ -= -f9 + 0.3F;
			this.spiderLeg7.rotateAngleZ -= f10 + -0.35F;
			this.spiderLeg8.rotateAngleZ -= -f10 + 0.35F;
		}

		if (entityskeleton.isBurning() || (!entityskeleton.isEntityAlive() && !entityskeleton.onGround))
		{
			this.spiderHead.rotateAngleX -= 0.5F;
			this.spiderHead.rotateAngleY += MathHelper.cos(ageInTicks * 0.6662F) * 0.5F;
		}
		if (entityskeleton.getJukeboxToDanceTo() != null)
		{
			float fl = MathHelper.sin(ageInTicks * 0.5F) * 0.7F;
			this.spiderLeg7.rotateAngleZ += -fl - (MathHelper.cos(ageInTicks * 0.25F) * 0.5F);
			this.spiderLeg8.rotateAngleZ += fl + (MathHelper.cos(ageInTicks * 0.25F) * 0.5F);
			this.spiderHead.setRotationPoint(0.0F + (MathHelper.cos(ageInTicks * 0.25F) * 1F), 15.0F - fl - (MathHelper.cos(ageInTicks * 0.5F) * 1F), -3.0F);
		}
	}
}


