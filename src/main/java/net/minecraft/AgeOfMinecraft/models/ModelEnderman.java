package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityEnderman;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelEnderman extends ModelBiped implements ICappedModel
{
	public boolean isCarrying;
	public boolean isAttacking;
	public ModelRenderer bipedCape;
	public ModelEnderman()
	{
		this(0F);
	}
	public ModelEnderman(float scale)
	{
		super(0.0F, -14.0F, 64, 32);
		float f = -14.0F;
		this.bipedHeadwear = new ModelRenderer(this, 0, 16);
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale - 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + f, 0.0F);
		this.bipedBody = new ModelRenderer(this, 32, 16);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
		this.bipedRightArm = new ModelRenderer(this, 56, 0);
		this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, scale);
		this.bipedRightArm.setRotationPoint(-3.0F, 2.0F + f, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 56, 0);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 30, 2, scale);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + f, 0.0F);
		this.bipedRightLeg = new ModelRenderer(this, 56, 0);
		this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, scale);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + f, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 56, 0);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 30, 2, scale);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + f, 0.0F);
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, scale);
	}
	public void renderCape(float scale, float flo1, float flo2, float flo3)
	{
		if (this.isChild)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, -0.875F, 0.0F);
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
			GlStateManager.translate(0.0F, -0.875F, 0.0F);
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
		this.setRotationAngles(limbSwing, limbSwingAmount, ((EntityFriendlyCreature)entityIn).isAIDisabled() ? 0 : ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();
		
		if (this.isChild)
		{
			GlStateManager.scale(1.5F, 1.5F, 1.5F);
			GlStateManager.translate(0.0F, 5.0F * scale, 0.0F);
			this.bipedHead.render(scale);
			this.bipedHeadwear.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F, 1.0F, 1.0F);
			this.bipedBody.render(scale);
			this.bipedRightArm.render(scale);
			this.bipedLeftArm.render(scale);
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
		}
		else
		{
			if (entityIn.isSneaking())
			{
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.bipedHead.render(scale);
			this.bipedBody.render(scale);
			this.bipedRightArm.render(scale);
			this.bipedLeftArm.render(scale);
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
			this.bipedHeadwear.render(scale);
		}

		GlStateManager.popMatrix();
	}
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		this.bipedHead.showModel = true;
		float f = -14.0F;
		this.bipedBody.rotateAngleX = 0.0F;
		this.bipedBody.rotationPointY = f;
		this.bipedBody.rotationPointZ = -0.0F;
		this.bipedRightLeg.rotateAngleX -= 0.0F;
		this.bipedLeftLeg.rotateAngleX -= 0.0F;
		this.bipedRightArm.rotateAngleX = ((float)(this.bipedRightArm.rotateAngleX * 0.5D));
		this.bipedLeftArm.rotateAngleX = ((float)(this.bipedLeftArm.rotateAngleX * 0.5D));
		this.bipedRightLeg.rotateAngleX = ((float)(this.bipedRightLeg.rotateAngleX * 0.5D));
		this.bipedLeftLeg.rotateAngleX = ((float)(this.bipedLeftLeg.rotateAngleX * 0.5D));
		float f1 = 0.4F;
		
		if (this.bipedRightArm.rotateAngleX > f1)
		{
			this.bipedRightArm.rotateAngleX = f1;
		}

		if (this.bipedLeftArm.rotateAngleX > f1)
		{
			this.bipedLeftArm.rotateAngleX = f1;
		}

		if (this.bipedRightArm.rotateAngleX < -f1)
		{
			this.bipedRightArm.rotateAngleX = (-f1);
		}

		if (this.bipedLeftArm.rotateAngleX < -f1)
		{
			this.bipedLeftArm.rotateAngleX = (-f1);
		}

		if (this.bipedRightLeg.rotateAngleX > f1)
		{
			this.bipedRightLeg.rotateAngleX = f1;
		}

		if (this.bipedLeftLeg.rotateAngleX > f1)
		{
			this.bipedLeftLeg.rotateAngleX = f1;
		}

		if (this.bipedRightLeg.rotateAngleX < -f1)
		{
			this.bipedRightLeg.rotateAngleX = (-f1);
		}

		if (this.bipedLeftLeg.rotateAngleX < -f1)
		{
			this.bipedLeftLeg.rotateAngleX = (-f1);
		}
		if (this.isCarrying)
		{
			this.bipedRightArm.rotateAngleX = -0.5F;
			this.bipedLeftArm.rotateAngleX = -0.5F;
			this.bipedRightArm.rotateAngleZ = 0.05F;
			this.bipedLeftArm.rotateAngleZ = -0.05F;
		}
		this.bipedRightArm.rotationPointZ = 0.0F;
		this.bipedLeftArm.rotationPointZ = 0.0F;
		this.bipedRightLeg.rotationPointZ = 0.0F;
		this.bipedLeftLeg.rotationPointZ = 0.0F;
		this.bipedRightLeg.rotationPointY = (9.0F + f);
		this.bipedLeftLeg.rotationPointY = (9.0F + f);
		this.bipedHead.rotationPointZ = -0.0F;
		this.bipedHead.rotationPointY = (f + 1.0F);
		this.bipedHeadwear.rotationPointX = this.bipedHead.rotationPointX;
		this.bipedHeadwear.rotationPointY = this.bipedHead.rotationPointY;
		this.bipedHeadwear.rotationPointZ = this.bipedHead.rotationPointZ;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;
		if (this.isSneak)
		{
			this.bipedBody.rotateAngleX = 0.5F;
			if (this.isCarrying)
			{
				this.bipedRightArm.rotateAngleX = -0.85F;
				this.bipedLeftArm.rotateAngleX = -0.85F;
				this.bipedRightArm.rotateAngleZ = 0.05F;
				this.bipedLeftArm.rotateAngleZ = -0.05F;
			}
			this.bipedRightArm.rotationPointZ = 6.0F;
			this.bipedLeftArm.rotationPointZ = 6.0F;
			this.bipedBody.rotationPointZ = 6.0F;
			this.bipedHead.rotationPointZ = 6.0F;
			this.bipedHeadwear.rotationPointZ = 6.0F;
			this.bipedRightLeg.rotationPointZ = 12.0F;
			this.bipedLeftLeg.rotationPointZ = 12.0F;
			this.bipedRightLeg.rotationPointY = -3.0F;
			this.bipedLeftLeg.rotationPointY = -3.0F;
			this.bipedRightArm.rotationPointY = -10.0F;
			this.bipedLeftArm.rotationPointY = -10.0F;
			this.bipedBody.rotationPointY = -12.0F;
			this.bipedHead.rotationPointY = -11.0F;
			this.bipedHeadwear.rotationPointY = -11.0F;
		}
		else
		{
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedRightLeg.rotationPointZ = 0.0F;
			this.bipedLeftLeg.rotationPointZ = 0.0F;
			this.bipedRightLeg.rotationPointY = -5.0F;
			this.bipedLeftLeg.rotationPointY = -5.0F;
			this.bipedRightArm.rotationPointY = -12.0F;
			this.bipedLeftArm.rotationPointY = -12.0F;
			this.bipedBody.rotationPointY = -14.0F;
			this.bipedHead.rotationPointY = -13.0F;
			this.bipedHeadwear.rotationPointY = -13.0F;
		}
		EntityEnderman entity = (EntityEnderman)entityIn;
		this.isAttacking = entity.isArmsRaised();
		this.isCarrying = entity.getHeldBlockState() != null || entity.getControllingPassenger() != null;
		this.isChild = entity.isChild();
		this.isSneak = entity.isSneaking();
		
		if (isAttacking)
		{
			float f2 = 1.0F;
			this.bipedHead.rotationPointY -= f2 * 5.0F;
		}
		if (entity.getJukeboxToDanceTo() != null)
		{
			float fl = MathHelper.sin(ageInTicks * 0.5F) * 4F;
			this.bipedHead.setRotationPoint(0.0F, -16F - fl, 0.0F);
		}

		if ((entity.isBurning() || entity.isWet() || (!entity.isEntityAlive() && !entity.onGround)))
		{
			this.bipedHead.rotateAngleX -= 0.5F;
			this.bipedHead.rotateAngleY += MathHelper.cos(ageInTicks * 0.6662F) * 0.5F;
			this.bipedHeadwear.rotateAngleX -= 0.5F;
			this.bipedHeadwear.rotateAngleY += MathHelper.cos(ageInTicks * 0.6662F) * 0.5F;
			this.bipedRightArm.rotationPointZ = 0.0F;
			this.bipedRightArm.rotationPointX = -5.0F;
			this.bipedLeftArm.rotationPointZ = 0.0F;
			this.bipedLeftArm.rotationPointX = 5.0F;
			this.bipedRightLeg.rotateAngleX = -MathHelper.cos(ageInTicks * 0.6662F);
			this.bipedLeftLeg.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F);
			this.bipedRightArm.rotateAngleX = -MathHelper.cos(ageInTicks * 0.6662F);
			this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F);
			this.bipedRightArm.rotateAngleZ = 2.3561945F;
			this.bipedLeftArm.rotateAngleZ = -2.3561945F;
			this.bipedRightArm.rotateAngleY = 0.0F;
			this.bipedLeftArm.rotateAngleY = 0.0F;
		}

		if (!entity.getCurrentBook().isEmpty())
		{
			this.bipedRightArm.rotateAngleY = (entity.bookSpread - 1F) * 0.25F;
			this.bipedLeftArm.rotateAngleY = (-entity.bookSpread + 1F) * 0.25F;
			this.bipedRightArm.rotateAngleZ = 0F;
			this.bipedLeftArm.rotateAngleZ = 0F;
			this.bipedRightArm.rotateAngleX = -1.25F + (0.1F + MathHelper.sin((float)entity.ticksExisted * 0.1F) * 0.01F);
			this.bipedLeftArm.rotateAngleX = -1.25F + (0.1F + MathHelper.sin((float)entity.ticksExisted * 0.1F) * 0.01F);
		}
	}
}


