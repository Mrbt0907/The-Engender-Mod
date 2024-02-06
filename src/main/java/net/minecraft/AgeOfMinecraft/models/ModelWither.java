package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityWither;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelWither extends ModelBase implements ICappedModel
{
	private ModelRenderer[] upperBodyParts;
	public ModelRenderer[] heads;
	public ModelRenderer bipedCape;
	public ModelWither(float p_i46302_1_)
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.upperBodyParts = new ModelRenderer[3];
		this.upperBodyParts[0] = new ModelRenderer(this, 0, 16);
		this.upperBodyParts[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, p_i46302_1_);
		this.upperBodyParts[1] = new ModelRenderer(this).setTextureSize(this.textureWidth, this.textureHeight);
		this.upperBodyParts[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
		this.upperBodyParts[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3, p_i46302_1_);
		this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, p_i46302_1_);
		this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, p_i46302_1_);
		this.upperBodyParts[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, p_i46302_1_);
		this.upperBodyParts[2] = new ModelRenderer(this, 12, 22);
		this.upperBodyParts[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, p_i46302_1_);
		this.heads = new ModelRenderer[3];
		this.heads[0] = new ModelRenderer(this, 0, 0);
		this.heads[0].addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i46302_1_);
		this.heads[1] = new ModelRenderer(this, 32, 0);
		this.heads[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
		this.heads[1].rotationPointX = -8.0F;
		this.heads[1].rotationPointY = 4.0F;
		this.heads[2] = new ModelRenderer(this, 32, 0);
		this.heads[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
		this.heads[2].rotationPointX = 10.0F;
		this.heads[2].rotationPointY = 4.0F;
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46302_1_);
	}
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		GlStateManager.pushMatrix();
		EntityWither wither = (EntityWither)p_78088_1_;
		if (wither.getRamTime() < 180 && wither.getRamTime() > 100)
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		ModelRenderer[] amodelrenderer = this.heads;
		int i = amodelrenderer.length;
		for (int j = 0; j < i; j++)
		{
			ModelRenderer modelrenderer = amodelrenderer[j];
			modelrenderer.render(p_78088_7_);
		}
		amodelrenderer = this.upperBodyParts;
		i = amodelrenderer.length;
		for (int j = 0; j < i; j++)
		{
			ModelRenderer modelrenderer = amodelrenderer[j];
			modelrenderer.render(p_78088_7_);
		}
		GlStateManager.popMatrix();
	}
	public void renderCape(float scale, float flo1, float flo2, float flo3)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.0F, 0.25F, 0.0F);
		GlStateManager.rotate(6.0F + flo2 / 2.0F + flo1 + (this.upperBodyParts[1].rotateAngleX * 50F), 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(flo3 / 2.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(-flo3 / 2.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		this.bipedCape.render(scale);
		GlStateManager.popMatrix();
	}
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
		this.heads[0].rotateAngleY = (p_78087_4_ / 57.295776F);
		this.upperBodyParts[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos(this.upperBodyParts[1].rotateAngleX) * 10.0F, -0.5F + MathHelper.sin(this.upperBodyParts[1].rotateAngleX) * 10.0F);
		EntityWither wither = (EntityWither)p_78087_7_;
		
		float f2 = MathHelper.sin(this.swingProgress * 3.1415927F);
		float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.1415927F);
		this.heads[0].rotateAngleX = (p_78087_5_ / 57.295776F);
		this.heads[0].rotationPointX = 0F;
		this.heads[0].rotationPointY = 4F;
		heads[0].rotateAngleZ = 0F;
		heads[1].rotateAngleZ = 0F;
		heads[2].rotateAngleZ = 0F;
		upperBodyParts[1].rotateAngleX = 0F;
		upperBodyParts[2].rotateAngleX = 0F;
		upperBodyParts[1].rotateAngleZ = 0F;
		upperBodyParts[2].rotateAngleZ = 0F;
		if (wither.getJukeboxToDanceTo() != null)
		{
			float f4 = MathHelper.sin(p_78087_3_ * 0.125F) * 0.7F;
			this.heads[0].rotationPointX += (MathHelper.cos(p_78087_3_ * 0.25F) * 1F);
			this.heads[0].rotationPointY += f4 + (MathHelper.cos(p_78087_3_ * 0.5F) * 1F);
			heads[0].rotateAngleX += (MathHelper.cos(p_78087_3_ * 0.5F) * 0.5F);
			heads[0].rotateAngleZ += (MathHelper.cos(p_78087_3_ * 0.25F) * 0.5F);
			heads[1].rotateAngleZ += (MathHelper.cos(p_78087_3_ * 0.25F - 2F) * 0.5F);
			heads[2].rotateAngleZ -= (MathHelper.cos(p_78087_3_ * 0.25F - 2F) * 0.5F);
			upperBodyParts[1].rotateAngleX += (MathHelper.cos(p_78087_3_ * 0.25F) * 0.5F);
			upperBodyParts[2].rotateAngleX += (MathHelper.cos(p_78087_3_ * 0.25F - 1F) * 0.5F);
		}if (wither.isSneaking())
			{
				float f6 = MathHelper.cos(p_78087_3_ * 0.25F);
				this.upperBodyParts[1].rotateAngleX += (p_78087_5_ / 57.295776F) + ((0.35F + 0.025F * f6) * 3.1415927F);
				f6 = MathHelper.cos(p_78087_3_ * 0.25F - 1.5F);
				this.upperBodyParts[2].rotateAngleX += (p_78087_5_ / 57.295776F) + ((0.6F + 0.05F * f6) * 3.1415927F);
				this.heads[0].rotateAngleX -= 0.075F + (0.05F + 0.025F * f6);
				f6 = MathHelper.cos(p_78087_3_ * 0.275F - 1.0F);
				this.heads[1].rotateAngleX += (-0.01F + 0.01F * f6) * 3.1415927F;
				this.heads[2].rotateAngleX += (-0.01F + 0.01F * f6) * 3.1415927F;
				this.upperBodyParts[1].rotateAngleX -= f2 * 2F - f1;
				this.upperBodyParts[2].rotateAngleX -= f2 * 4F - f1;
			}
			else
			{
				float f6 = MathHelper.cos(p_78087_3_ * 0.1F);
				this.upperBodyParts[1].rotateAngleX += ((0.065F + 0.05F * f6) * 3.1415927F);
				f6 = MathHelper.cos(p_78087_3_ * 0.1F - 1.5F);
				this.upperBodyParts[2].rotateAngleX += ((0.3F + 0.1F * f6) * 3.1415927F);
				this.heads[0].rotateAngleX -= (0.05F + 0.025F * f6);
				f6 = MathHelper.cos(p_78087_3_ * 0.125F - 1.0F);
				this.heads[1].rotateAngleX += ((-0.01F + 0.01F * f6) * 3.1415927F);
				this.heads[2].rotateAngleX += ((-0.01F + 0.01F * f6) * 3.1415927F);
				this.upperBodyParts[1].rotateAngleX -= f2 * 2F - f1;
				this.upperBodyParts[2].rotateAngleX -= f2 * 4F - f1;
			}
		}
		public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
		{
			EntityWither entitywither = (EntityWither)p_78086_1_;
			for (int i = 1; i < 3; ++i)
			{
				this.heads[i].rotateAngleY = (entitywither.getHeadYRotation(i - 1) - p_78086_1_.renderYawOffset) * 0.017453292F + (!entitywither.getCurrentBook().isEmpty() ? -(MathHelper.sin(entitywither.ticksExisted * 0.1F) * 0.5F + (i == 1 ? 0.5F : -0.5F)) : 0F);
				this.heads[i].rotateAngleX = entitywither.getHeadXRotation(i - 1) * 0.017453292F;
			}
		}
	}

	
	