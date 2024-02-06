package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityPigZombie;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)

public class ModelZombie extends ModelBiped implements ICappedModel
{
	public ModelRenderer bipedCape;
	
	public ModelZombie()
	{
		this(0.0F, false);
	}
	public ModelZombie(float modelSize, boolean p_i1168_2_)
	{
		super(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, modelSize);
	}

	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ((EntityFriendlyCreature)entityIn).isAIDisabled() ? 0 : ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();
		
		if (this.isChild)
		{
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
			this.bipedHead.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.bipedBody.render(scale);
			this.bipedRightArm.render(scale);
			this.bipedLeftArm.render(scale);
			this.bipedRightLeg.render(scale);
			this.bipedLeftLeg.render(scale);
			this.bipedHeadwear.render(scale);
		}
		else
		{
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

	public void renderCape(float scale, float flo1, float flo2, float flo3)
	{
		if (this.isChild)
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 1.5F, -0.125F);
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
			GlStateManager.rotate(6.0F + flo2 / 2.0F + flo1, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(flo3 / 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(-flo3 / 2.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			this.bipedCape.render(scale);
			GlStateManager.popMatrix();
		}
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		EntityFriendlyCreature entity = (EntityFriendlyCreature)entityIn;
		if (entity.isAIDisabled())
		ageInTicks = 1;
		
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		this.isChild = entity.isChild();
		this.isSneak = entity.isSneaking();
		
		if (this.isSneak)
		this.bipedCape.rotationPointY = 2.0F;
		else
		this.bipedCape.rotationPointY = 0.0F;
		
		this.bipedHeadwear.isHidden = entity instanceof EntityPigZombie && ((EntityPigZombie)entity).isOldPEPigman();
		if (!(entity instanceof EntityPigZombie) || (entity instanceof EntityPigZombie && !((EntityPigZombie)entity).isOldPEPigman()))
		{
			float f = MathHelper.sin(this.swingProgress * 3.1415927F);
			float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = (-(f * 0.6F));
			this.bipedLeftArm.rotateAngleY = ((f * 0.6F));
			float f2 = entity.isArmsRaised() ? -1.5F : -0.75F;
			this.bipedRightArm.rotateAngleX = (this.bipedHead.rotateAngleX + f2 - 0.5F);
			this.bipedLeftArm.rotateAngleX = (this.bipedHead.rotateAngleX + f2 - 0.5F);
			this.bipedRightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
			this.bipedLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.1F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.1F;
		}

		this.bipedHead.rotateAngleX += (MathHelper.cos(ageInTicks * 0.1F) * 0.0125F) * 3.1415927F;
		
		if (entity.isBeingRidden() && entity instanceof EntityPigZombie)
		{
			if (entity.getPrimaryHand() == EnumHandSide.RIGHT)
			{
				this.bipedRightArm.rotateAngleX -= 0.325F;
				this.bipedLeftArm.rotateAngleX = -1.875F;
			}
			else
			{
				this.bipedRightArm.rotateAngleX = -1.875F;
				this.bipedLeftArm.rotateAngleX -= 0.325F;
			}
		}

		if (entity.isBurning() || (!entity.isEntityAlive() && !entity.onGround))
		{
			this.bipedHead.rotateAngleX -= 0.5F;
			this.bipedHead.rotateAngleY += MathHelper.cos(ageInTicks * 0.6662F) * 0.5F;
			this.bipedRightArm.rotationPointZ = 0.0F;
			this.bipedRightArm.rotationPointX = -5.0F;
			this.bipedLeftArm.rotationPointZ = 0.0F;
			this.bipedLeftArm.rotationPointX = 5.0F;
			this.bipedRightArm.rotateAngleX = -MathHelper.cos(ageInTicks * 0.6662F) * 0.5F;
			this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageInTicks * 0.6662F) * 0.5F;
			this.bipedRightArm.rotateAngleZ = 2.3561945F;
			this.bipedLeftArm.rotateAngleZ = -2.3561945F;
			this.bipedRightArm.rotateAngleY = 0.0F;
			this.bipedLeftArm.rotateAngleY = 0.0F;
		}

		if (entity.getJukeboxToDanceTo() != null)
		{
			float fl = MathHelper.sin(ageInTicks * 0.5F) * 0.7F;
			this.bipedRightArm.rotationPointZ = 0.0F;
			this.bipedRightArm.rotationPointX = -5.0F;
			this.bipedLeftArm.rotationPointZ = 0.0F;
			this.bipedLeftArm.rotationPointX = 5.0F;
			this.bipedRightArm.rotateAngleX = -fl;
			this.bipedLeftArm.rotateAngleX = fl;
			this.bipedRightArm.rotateAngleZ = 1.6561945F - (MathHelper.cos(ageInTicks * 0.125F) * 1F);
			this.bipedLeftArm.rotateAngleZ = -1.6561945F + (MathHelper.cos(ageInTicks * 0.125F) * 1F);
			this.bipedRightArm.rotateAngleY = 0.0F;
			this.bipedLeftArm.rotateAngleY = 0.0F;
			this.bipedHead.setRotationPoint(0.0F + (MathHelper.cos(ageInTicks * 0.25F) * 1F), 0.5F - fl, 0.0F);
		}
		else
		
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		copyModelAngles(this.bipedHead, this.bipedHeadwear);
		
		if (!entity.getCurrentBook().isEmpty())
		{
			this.bipedRightArm.rotateAngleY = entity.bookSpread - 1F;
			this.bipedLeftArm.rotateAngleY = -entity.bookSpread + 1F;
			this.bipedRightArm.rotateAngleZ = 0F;
			this.bipedLeftArm.rotateAngleZ = 0F;
			this.bipedRightArm.rotateAngleX = -1.5F + (0.1F + MathHelper.sin((float)entity.ticksExisted * 0.1F) * 0.01F);
			this.bipedLeftArm.rotateAngleX = -1.5F + (0.1F + MathHelper.sin((float)entity.ticksExisted * 0.1F) * 0.01F);
		}
	}
}