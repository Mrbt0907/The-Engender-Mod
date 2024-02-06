package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelIronGolem extends ModelBase implements ICappedModel
{
	public ModelRenderer ironGolemHead;
	public ModelRenderer ironGolemBody;
	public ModelRenderer ironGolemRightArm;
	public ModelRenderer ironGolemLeftArm;
	public ModelRenderer ironGolemLeftLeg;
	public ModelRenderer ironGolemRightLeg;
	public ModelRenderer bipedCape;
	public ModelIronGolem()
	{
		this(0.0F);
	}
	
	public ModelIronGolem(float p_i1161_1_)
	{
		this(p_i1161_1_, -7.0F);
	}
	
	public ModelIronGolem(float p_i46362_1_, float p_i46362_2_)
	{
	        ironGolemHead = (new ModelRenderer(this)).setTextureSize(128, 128);
	        ironGolemHead.setRotationPoint(0.0F, 0.0F + p_i46362_2_, -2.0F);
	        ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8, 10, 8, p_i46362_1_);
	        ironGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4, 2, p_i46362_1_);
	        ironGolemBody = (new ModelRenderer(this)).setTextureSize(128, 128);
	        ironGolemBody.setRotationPoint(0.0F, 0.0F + p_i46362_2_, 0.0F);
	        ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18, 12, 11, p_i46362_1_);
	        ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5, 6, p_i46362_1_ + 0.5F);
	        ironGolemRightArm = (new ModelRenderer(this)).setTextureSize(128, 128);
	        ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
	        ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4, 30, 6, p_i46362_1_);
	        ironGolemLeftArm = (new ModelRenderer(this)).setTextureSize(128, 128);
	        ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
	        ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4, 30, 6, p_i46362_1_);
	        ironGolemLeftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
	        ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + p_i46362_2_, 0.0F);
	        ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i46362_1_);
	        ironGolemRightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(128, 128);
	        ironGolemRightLeg.mirror = true;
	        ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F, 18.0F + p_i46362_2_, 0.0F);
	        ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i46362_1_);
		bipedCape = new ModelRenderer(this, 0, 0);
		bipedCape.setTextureSize(64, 32);
		bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46362_1_);
	}
	
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		ironGolemHead.render(p_78088_7_);
		ironGolemBody.render(p_78088_7_);
		ironGolemLeftLeg.render(p_78088_7_);
		ironGolemRightLeg.render(p_78088_7_);
		ironGolemRightArm.render(p_78088_7_);
		ironGolemLeftArm.render(p_78088_7_);
	}
	
	public void renderCape(float scale, float flo1, float flo2, float flo3)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scale(2F, 2F, 2F);
		GlStateManager.translate(0.0F, -0.275F, 0.0625F);
		GlStateManager.rotate(6.0F + flo2 / 2.0F + flo1, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(flo3 / 2.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(-flo3 / 2.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		bipedCape.render(scale);
		GlStateManager.popMatrix();
	}
	
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		this.ironGolemHead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.ironGolemHead.rotateAngleX = headPitch * 0.017453292F;
        this.ironGolemLeftLeg.rotateAngleX = -1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.ironGolemRightLeg.rotateAngleX = 1.5F * this.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.ironGolemLeftLeg.rotateAngleY = 0.0F;
        this.ironGolemRightLeg.rotateAngleY = 0.0F;
		EntityFriendlyCreature entity = (EntityFriendlyCreature)entityIn;
		if (entity.getJukeboxToDanceTo() != null)
		{
			ironGolemHead.rotateAngleZ = (MathHelper.cos(ageInTicks * 0.5F - 3.1415927F) * 0.25F) - (MathHelper.cos(ageInTicks * 0.25F) * 0.25F);
			ironGolemRightArm.rotateAngleX -= (MathHelper.cos(ageInTicks * 0.5F) * 0.5F) - (MathHelper.cos(ageInTicks * 0.25F - 3.1415927F) * 0.5F);
			ironGolemLeftArm.rotateAngleX += (MathHelper.cos(ageInTicks * 0.5F - 3.1415927F) * 0.25F) - (MathHelper.cos(ageInTicks * 0.25F) * 0.25F);
		}
	}
			public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
			{
				EntityFriendlyCreature entityirongolem = (EntityFriendlyCreature)entitylivingbaseIn;
				int i = entityirongolem.getAttackTimer();
				//EngenderMod.info(":"+i);
				if (entityirongolem.isSneaking())
				{
					ironGolemBody.rotateAngleX = 0.25F;
					ironGolemRightLeg.rotationPointZ = 4.0F;
					ironGolemLeftLeg.rotationPointZ = 4.0F;
					ironGolemRightArm.rotationPointZ = 2.0F;
					ironGolemLeftArm.rotationPointZ = 2.0F;
					ironGolemRightArm.rotationPointY = -6.0F;
					ironGolemLeftArm.rotationPointY = -6.0F;
					ironGolemHead.rotationPointY = -5.0F;
					ironGolemBody.rotationPointY = -6.0F;
				}
				else
				{
					ironGolemBody.rotateAngleX = 0.0F;
					ironGolemRightLeg.rotationPointZ = 0.0F;
					ironGolemLeftLeg.rotationPointZ = 0.0F;
					ironGolemRightArm.rotationPointZ = 0.0F;
					ironGolemLeftArm.rotationPointZ = 0.0F;
					ironGolemRightArm.rotationPointY = -7.0F;
					ironGolemLeftArm.rotationPointY = -7.0F;
					ironGolemHead.rotationPointY = -7.0F;
					ironGolemBody.rotationPointY = -7.0F;
				}

			        if (i > 0)
			        {
			            this.ironGolemRightArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float)i - partialTickTime, 10.0F);
			            this.ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float)i - partialTickTime, 10.0F);
			        }
			        else
			        {
			            int j = entityirongolem.getHoldRoseTick();

			            if (j > 0)
			            {
			                this.ironGolemRightArm.rotateAngleX = -0.8F + 0.025F * this.triangleWave((float)j, 70.0F);
			                this.ironGolemLeftArm.rotateAngleX = 0.0F;
			            }
			            else
			            {
			                this.ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * this.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
			                this.ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * this.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
			            }
			        }

				ironGolemRightArm.rotateAngleY = 0F;
				ironGolemLeftArm.rotateAngleY = 0F;
			        
				if (!entityirongolem.getCurrentBook().isEmpty())
				{
					ironGolemRightArm.rotateAngleY = (entityirongolem.bookSpread - 2F) * 0.25F;
					ironGolemLeftArm.rotateAngleY = (-entityirongolem.bookSpread + 2F) * 0.25F;
					ironGolemRightArm.rotateAngleZ = 0F;
					ironGolemLeftArm.rotateAngleZ = 0F;
					ironGolemRightArm.rotateAngleX = -1.25F + (0.1F + MathHelper.sin((float)entityirongolem.ticksExisted * 0.1F) * 0.01F);
					ironGolemLeftArm.rotateAngleX = -1.25F + (0.1F + MathHelper.sin((float)entityirongolem.ticksExisted * 0.1F) * 0.01F);
				}
			}
			
			private float triangleWave(float p_78172_1_, float p_78172_2_)
		    {
		        return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
		    }
		}

		
		