package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityCommandBlockWither;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelCommandBlockWither
extends ModelBase
{
	public ModelRenderer Supporter;
	public ModelRenderer MainHead;
	public ModelRenderer spine;
	public ModelRenderer LeftHead;
	public ModelRenderer RightHead;
	public ModelRenderer lowerspine;
	public ModelRenderer Rib1;
	public ModelRenderer Rib2;
	public ModelRenderer Rib3;
	public ModelRenderer commandblock;
	public ModelRenderer Rib11;
	public ModelRenderer Rib12;
	public ModelRenderer Rib21;
	public ModelRenderer Rib22;
	public ModelRenderer Rib31;
	public ModelRenderer Rib32;
	public ModelCommandBlockWither()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.Supporter = new ModelRenderer(this, 0, 16);
		this.Supporter.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.Supporter.addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, 0.0F);
		this.spine = new ModelRenderer(this, 0, 22);
		this.spine.setRotationPoint(0.0F, 6.9F, -0.5F);
		this.spine.addBox(-1.5F, 0.0F, 0.0F, 3, 10, 3, 0.0F);
		this.Rib21 = new ModelRenderer(this, 24, 22);
		this.Rib21.setRotationPoint(6.0F, 5.0F, 2.5F);
		this.Rib21.addBox(0.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F);
		setRotateAngle(this.Rib21, 0.0F, 1.5707964F, 0.0F);
		this.Rib22 = new ModelRenderer(this, 24, 22);
		this.Rib22.setRotationPoint(-3.0F, 5.0F, 2.5F);
		this.Rib22.addBox(0.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F);
		setRotateAngle(this.Rib22, 0.0F, 1.5707964F, 0.0F);
		this.Rib2 = new ModelRenderer(this, 24, 22);
		this.Rib2.setRotationPoint(-1.5F, 0.0F, 0.0F);
		this.Rib2.addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, 0.0F);
		this.Rib12 = new ModelRenderer(this, 24, 22);
		this.Rib12.setRotationPoint(-3.0F, 2.5F, 2.5F);
		this.Rib12.addBox(0.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F);
		setRotateAngle(this.Rib12, 0.0F, 1.5707964F, 0.0F);
		this.LeftHead = new ModelRenderer(this, 32, 0);
		this.LeftHead.mirror = true;
		this.LeftHead.setRotationPoint(9.0F, 3.0F, 0.0F);
		this.LeftHead.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
		this.Rib1 = new ModelRenderer(this, 24, 22);
		this.Rib1.setRotationPoint(-1.5F, 0.0F, 0.0F);
		this.Rib1.addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, 0.0F);
		this.Rib32 = new ModelRenderer(this, 24, 22);
		this.Rib32.setRotationPoint(-3.0F, 7.5F, 2.5F);
		this.Rib32.addBox(0.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F);
		setRotateAngle(this.Rib32, 0.0F, 1.5707964F, 0.0F);
		this.lowerspine = new ModelRenderer(this, 12, 22);
		this.lowerspine.setRotationPoint(0.0F, 10.0F, 0.0F);
		this.lowerspine.addBox(-1.5F, 0.0F, 0.0F, 3, 6, 3, 0.0F);
		this.Rib31 = new ModelRenderer(this, 24, 22);
		this.Rib31.setRotationPoint(6.0F, 7.5F, 2.5F);
		this.Rib31.addBox(0.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F);
		setRotateAngle(this.Rib31, 0.0F, 1.5707964F, 0.0F);
		this.commandblock = new ModelRenderer(this, 32, 48);
		this.commandblock.setRotationPoint(0.0F, 5.0F, -2.0F);
		this.commandblock.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, 0.0F);
		this.MainHead = new ModelRenderer(this, 0, 0);
		this.MainHead.setRotationPoint(0.0F, 5.0F, 0.0F);
		this.MainHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.Rib3 = new ModelRenderer(this, 24, 22);
		this.Rib3.setRotationPoint(-1.5F, 0.0F, 0.0F);
		this.Rib3.addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, 0.0F);
		this.Rib11 = new ModelRenderer(this, 24, 22);
		this.Rib11.setRotationPoint(6.0F, 2.5F, 2.5F);
		this.Rib11.addBox(0.0F, -1.0F, -1.0F, 9, 2, 2, 0.0F);
		setRotateAngle(this.Rib11, 0.0F, 1.5707964F, 0.0F);
		this.RightHead = new ModelRenderer(this, 32, 0);
		this.RightHead.setRotationPoint(-9.0F, 3.0F, 0.0F);
		this.RightHead.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
		this.Supporter.addChild(this.spine);
		this.Rib2.addChild(this.Rib21);
		this.Rib2.addChild(this.Rib22);
		this.spine.addChild(this.Rib2);
		this.Rib1.addChild(this.Rib12);
		this.Supporter.addChild(this.LeftHead);
		this.spine.addChild(this.Rib1);
		this.Rib3.addChild(this.Rib32);
		this.spine.addChild(this.lowerspine);
		this.Rib3.addChild(this.Rib31);
		this.spine.addChild(this.commandblock);
		this.spine.addChild(this.Rib3);
		this.Rib1.addChild(this.Rib11);
		this.Supporter.addChild(this.RightHead);
	}
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.Supporter.render(f5);
		this.MainHead.render(f5);
	}
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		this.MainHead.rotateAngleY = (p_78087_4_ / 57.295776F);
		this.MainHead.rotateAngleX = (p_78087_5_ / 57.295776F);
		EntityCommandBlockWither wither = (EntityCommandBlockWither)p_78087_7_;
		MainHead.rotateAngleZ = 0F;
		LeftHead.rotateAngleZ = 0F;
		RightHead.rotateAngleZ = 0F;
		spine.rotateAngleX = 0F;
		lowerspine.rotateAngleX = 0F;
		spine.rotateAngleZ = 0F;
		lowerspine.rotateAngleZ = 0F;
		if (wither.getJukeboxToDanceTo() != null)
		{
			MainHead.rotateAngleX += (MathHelper.cos(p_78087_3_ * 1F) * 0.5F);
			MainHead.rotateAngleZ += (MathHelper.cos(p_78087_3_ * 0.5F) * 0.5F);
			LeftHead.rotateAngleZ += (MathHelper.cos(p_78087_3_ * 0.5F - 2F) * 0.5F);
			RightHead.rotateAngleZ -= (MathHelper.cos(p_78087_3_ * 0.5F - 2F) * 0.5F);
			spine.rotateAngleX += (MathHelper.cos(p_78087_3_ * 0.5F) * 0.5F);
			lowerspine.rotateAngleX += (MathHelper.cos(p_78087_3_ * 0.5F + 1F) * 0.5F);
			spine.rotateAngleZ += (MathHelper.cos(p_78087_3_ * 0.25F) * 0.5F);
			lowerspine.rotateAngleZ += (MathHelper.cos(p_78087_3_ * 0.25F + 1F) * 0.5F);
		}if (wither.isSneaking())
			{
				float f6 = MathHelper.cos(p_78087_3_ * 0.25F);
				this.spine.rotateAngleX += ((0.35F + 0.025F * f6) * 3.1415927F);
				f6 = MathHelper.cos(p_78087_3_ * 0.25F - 1.5F);
				this.lowerspine.rotateAngleX += ((0.35F + 0.05F * f6) * 3.1415927F);
				f6 = MathHelper.cos(p_78087_3_ * 0.275F - 1.0F);
				this.LeftHead.rotateAngleX += (-0.01F + 0.01F * f6) * 3.1415927F;
				this.RightHead.rotateAngleX += (-0.01F + 0.01F * f6) * 3.1415927F;
			}
			else
			{
				float f6 = MathHelper.cos(p_78087_3_ * 0.1F);
				this.spine.rotateAngleX += ((0.065F + 0.05F * f6) * 3.1415927F);
				f6 = MathHelper.cos(p_78087_3_ * 0.1F - 1.5F);
				this.lowerspine.rotateAngleX += ((0.3F + 0.1F * f6) * 3.1415927F);
				f6 = MathHelper.cos(p_78087_3_ * 0.125F - 1.0F);
				this.LeftHead.rotateAngleX += (-0.01F + 0.01F * f6) * 3.1415927F;
				this.RightHead.rotateAngleX += (-0.01F + 0.01F * f6) * 3.1415927F;
			}
		}
		public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
		{
			EntityCommandBlockWither entitywither = (EntityCommandBlockWither)p_78086_1_;
			this.RightHead.rotateAngleY = ((entitywither.func_82207_a(1) - p_78086_1_.renderYawOffset) / 57.295776F);
			this.RightHead.rotateAngleX = (entitywither.func_82210_r(1) / 57.295776F);
			this.LeftHead.rotateAngleY = ((entitywither.func_82207_a(1) - p_78086_1_.renderYawOffset) / 57.295776F);
			this.LeftHead.rotateAngleX = (entitywither.func_82210_r(1) / 57.295776F);
		}
	}

	
	