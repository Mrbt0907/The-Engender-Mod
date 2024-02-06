package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.tier3.EntityCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelCreeper extends ModelBase implements ICappedModel
{
	public ModelRenderer head;
	public ModelRenderer creeperArmor;
	public ModelRenderer body;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;
	public boolean isSneak;
	public ModelRenderer bipedCape;
	public ModelCreeper()
	{
		this(0.0F);
	}
	public ModelCreeper(float p_i46366_1_)
	{
		int i = 6;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46366_1_);
		this.head.setRotationPoint(0.0F, i, 0.0F);
		this.creeperArmor = new ModelRenderer(this, 32, 0);
		this.creeperArmor.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46366_1_ + 0.5F);
		this.creeperArmor.setRotationPoint(0.0F, i, 0.0F);
		this.body = new ModelRenderer(this, 16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i46366_1_);
		this.body.setRotationPoint(0.0F, i, 0.0F);
		this.leg1 = new ModelRenderer(this, 0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46366_1_);
		this.leg1.setRotationPoint(-2.0F, 12 + i, 4.0F);
		this.leg2 = new ModelRenderer(this, 0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46366_1_);
		this.leg2.setRotationPoint(2.0F, 12 + i, 4.0F);
		this.leg3 = new ModelRenderer(this, 0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46366_1_);
		this.leg3.setRotationPoint(-2.0F, 12 + i, -4.0F);
		this.leg4 = new ModelRenderer(this, 0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i46366_1_);
		this.leg4.setRotationPoint(2.0F, 12 + i, -4.0F);
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
			GlStateManager.scale(1.5F, 1.5F, 1.5F);
			this.head.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F, 1.0F, 1.0F);
			this.body.render(scale);
			this.leg1.render(scale);
			this.leg2.render(scale);
			this.leg3.render(scale);
			this.leg4.render(scale);
		}
		else
		{
			this.head.render(scale);
			this.body.render(scale);
			this.leg1.render(scale);
			this.leg2.render(scale);
			this.leg3.render(scale);
			this.leg4.render(scale);
		}

		GlStateManager.popMatrix();
	}
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
		MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
		this.head.rotateAngleY = (netHeadYaw * 0.017453292F);
		this.head.rotateAngleX = (headPitch * 0.017453292F) + f;
		this.leg1.rotateAngleX = (MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
		this.leg2.rotateAngleX = (MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount);
		this.leg3.rotateAngleX = (MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount);
		this.leg4.rotateAngleX = (MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
		
		this.head.rotationPointX = 0F;
		this.head.rotateAngleX += (MathHelper.cos(ageInTicks * 0.1F) * 0.0125F) * 3.1415927F;
		if (this.isSneak)
		{
			this.body.rotateAngleX = 0.5F;
			this.head.rotationPointY = 8.0F;
			this.body.rotationPointY = 8.0F;
			this.head.rotationPointZ = -5.0F;
			this.body.rotationPointZ = -5.0F;
		}
		else
		{
			this.body.rotateAngleX = 0.0F;
			this.head.rotationPointY = 6.0F;
			this.body.rotationPointY = 6.0F;
			this.head.rotationPointZ = 0.0F;
			this.body.rotationPointZ = 0.0F;
		}
		EntityCreeper entity = (EntityCreeper)entityIn;
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
	}
}