package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelSkeleton extends ModelBiped implements ICappedModel
{
	public ModelRenderer bipedCape;
	public ModelSkeleton()
	{
		this(0.0F, false);
	}
	public ModelSkeleton(float modelSize, boolean p_i46303_2_)
	{
		super(modelSize, 0.0F, 64, 32);
		if (!p_i46303_2_)
		{
			this.bipedRightArm = new ModelRenderer(this, 40, 16);
			this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
			this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
			this.bipedLeftArm = new ModelRenderer(this, 40, 16);
			this.bipedLeftArm.mirror = true;
			this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
			this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
			this.bipedRightLeg = new ModelRenderer(this, 0, 16);
			this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
			this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
			this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
			this.bipedLeftLeg.mirror = true;
			this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
			this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		}
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, modelSize);
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
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
	{
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		ItemStack itemstack = entitylivingbaseIn.getHeldItem(EnumHand.MAIN_HAND);
		if ((itemstack != null) && (itemstack.getItem() instanceof ItemBow) && (((EntityFriendlyCreature)entitylivingbaseIn).isArmsRaised()))
		{
			if (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT)
			{
				this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
			else
			{
				this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
		}
		super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
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
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		ItemStack itemstack = ((EntityLivingBase)entityIn).getHeldItemMainhand();
		EntityFriendlyCreature entity = (EntityFriendlyCreature)entityIn;
		this.isChild = entity.isChild();
		this.isSneak = entity.isSneaking();
		float f = MathHelper.sin(this.swingProgress * 3.1415927F);
		float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
		
		this.bipedHead.rotateAngleX += (MathHelper.cos(ageInTicks * 0.1F) * 0.0125F) * 3.1415927F;
		
		if ((entity.isArmsRaised()) && ((itemstack == null) || (itemstack != null && !(itemstack.getItem() instanceof ItemBow))))
		{
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = (-(0.1F - f * 0.6F));
			this.bipedLeftArm.rotateAngleY = (0.1F - f * 0.6F);
			this.bipedRightArm.rotateAngleX = -1.5707964F;
			this.bipedLeftArm.rotateAngleX = -1.5707964F;
			this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
			this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
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

		ItemStack itemstack2 = entity.getHeldItem(EnumHand.OFF_HAND);
		
		if ((itemstack2 != null) && (itemstack2.getItem() == Items.SHIELD) && entity.isActiveItemStackBlocking())
		{
			if (entity.getPrimaryHand() == EnumHandSide.RIGHT)
			{
				this.bipedLeftArm.rotateAngleX += this.bipedLeftArm.rotateAngleX * 0.5F + 0.9424779F;
				this.bipedLeftArm.rotateAngleY += 0.75F;
			}
			else
			{
				this.bipedRightArm.rotateAngleX += this.bipedRightArm.rotateAngleX * 0.5F + 0.9424779F;
				this.bipedRightArm.rotateAngleY += -0.75F;
			}
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
			this.bipedRightArm.rotateAngleZ = 1.6561945F - (MathHelper.cos(ageInTicks * 0.25F) * 1F);
			this.bipedLeftArm.rotateAngleZ = -0.9561945F + (MathHelper.cos(ageInTicks * 0.25F) * 1F);
			this.bipedRightArm.rotateAngleY = 0.0F;
			this.bipedLeftArm.rotateAngleY = 0.0F;
			this.bipedHead.setRotationPoint(0.0F + (MathHelper.cos(ageInTicks * 0.25F) * 1F), 0.5F - fl, 0.0F);
		}

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
	public void postRenderArm(float scale, EnumHandSide side)
	{
		float f = side == EnumHandSide.RIGHT ? 1.0F : -1.0F;
		ModelRenderer modelrenderer = getArmForSide(side);
		modelrenderer.rotationPointX += f;
		modelrenderer.postRender(scale);
		modelrenderer.rotationPointX -= f;
	}
}


