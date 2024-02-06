package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelPortal extends net.minecraft.client.model.ModelBase
{
	public ModelRenderer Base;
	public ModelRenderer Edge1;
	public ModelRenderer Edge2;
	public ModelRenderer Edge3;
	public ModelRenderer Edge4;
	public ModelRenderer Spike1;
	public ModelRenderer Spike2;
	public ModelRenderer Spike3;
	public ModelRenderer Spike4;
	public boolean onlybase;
	public ModelPortal()
	{
		this(false);
	}
	public ModelPortal(boolean onlybase)
	{
		this.onlybase = onlybase;
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.Edge2 = new ModelRenderer(this, 0, 20);
		this.Edge2.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.Edge2.addBox(-12.0F, -6.0F, -8.0F, 4, 4, 16, 0.0F);
		this.Edge3 = new ModelRenderer(this, 0, 20);
		this.Edge3.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.Edge3.addBox(-12.0F, -6.0F, -8.0F, 4, 4, 16, 0.0F);
		setRotateAngle(this.Edge3, 0.0F, -3.1415927F, 0.0F);
		this.Edge1 = new ModelRenderer(this, 0, 20);
		this.Edge1.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.Edge1.addBox(-12.0F, -6.0F, -8.0F, 4, 4, 16, 0.0F);
		setRotateAngle(this.Edge1, 0.0F, -1.5707964F, 0.0F);
		this.Spike4 = new ModelRenderer(this, 0, 44);
		this.Spike4.setRotationPoint(9.0F, 22.0F, -9.0F);
		this.Spike4.addBox(-2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F);
		setRotateAngle(this.Spike4, -0.43633232F, 2.3561945F, 0.0F);
		this.Spike3 = new ModelRenderer(this, 0, 44);
		this.Spike3.setRotationPoint(-9.0F, 22.0F, -9.0F);
		this.Spike3.addBox(-2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F);
		setRotateAngle(this.Spike3, -0.43633232F, -2.3561945F, 0.0F);
		this.Base = new ModelRenderer(this, 0, 0);
		this.Base.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.Base.addBox(-8.0F, -4.0F, -8.0F, 16, 4, 16, 0.0F);
		this.Spike1 = new ModelRenderer(this, 0, 44);
		this.Spike1.setRotationPoint(9.0F, 22.0F, 9.0F);
		this.Spike1.addBox(-2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F);
		setRotateAngle(this.Spike1, -0.43633232F, 0.7853982F, 0.0F);
		this.Spike2 = new ModelRenderer(this, 0, 44);
		this.Spike2.setRotationPoint(-9.0F, 22.0F, 9.0F);
		this.Spike2.addBox(-2.0F, -16.0F, -2.0F, 4, 16, 4, 0.0F);
		setRotateAngle(this.Spike2, -0.43633232F, -0.7853982F, 0.0F);
		this.Edge4 = new ModelRenderer(this, 0, 20);
		this.Edge4.setRotationPoint(0.0F, 24.0F, 0.0F);
		this.Edge4.addBox(-12.0F, -6.0F, -8.0F, 4, 4, 16, 0.0F);
		setRotateAngle(this.Edge4, 0.0F, 1.5707964F, 0.0F);
	}
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		if (onlybase)
		this.Base.render(f5);else
		{
			this.Base.render(f5);
			this.Edge1.render(f5);
			this.Edge2.render(f5);
			this.Edge3.render(f5);
			this.Edge4.render(f5);
			this.Spike1.render(f5);
			this.Spike2.render(f5);
			this.Spike3.render(f5);
			this.Spike4.render(f5);
		}
	}
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		EntityFriendlyCreature entity = (EntityFriendlyCreature)entityIn;
		if (entity.deathTime > 0)
		{
			this.Spike1.rotateAngleX = (-0.4375F - entity.deathTime * 0.02F);
			this.Spike2.rotateAngleX = (-0.4375F - entity.deathTime * 0.02F);
			this.Spike3.rotateAngleX = (-0.4375F - entity.deathTime * 0.02F);
			this.Spike4.rotateAngleX = (-0.4375F - entity.deathTime * 0.02F);
		}
		else if (ageInTicks < 60.0F)
		{
			this.Spike1.rotateAngleX = (1.6F - ageInTicks / 30F);
			this.Spike2.rotateAngleX = (1.6F - ageInTicks / 30F);
			this.Spike3.rotateAngleX = (1.6F - ageInTicks / 30F);
			this.Spike4.rotateAngleX = (1.6F - ageInTicks / 30F);
		}
		else
		{
			float f6 = -MathHelper.cos(ageInTicks * 0.05F);
			this.Spike1.rotateAngleX = (-0.4375F + 0.021875F * f6 * 3.1415927F);
			this.Spike2.rotateAngleX = (-0.4375F + 0.021875F * f6 * 3.1415927F);
			this.Spike3.rotateAngleX = (-0.4375F + 0.021875F * f6 * 3.1415927F);
			this.Spike4.rotateAngleX = (-0.4375F + 0.021875F * f6 * 3.1415927F);
			
			if (entity.getJukeboxToDanceTo() != null)
			{
				f6 = -MathHelper.cos(ageInTicks * 0.25F);
				this.Spike1.rotateAngleX = (-0.4375F + (MathHelper.cos(ageInTicks * 0.25F)));
				this.Spike2.rotateAngleX = (-0.4375F + (MathHelper.cos(ageInTicks * 0.25F)));
				this.Spike3.rotateAngleX = (-0.4375F + (MathHelper.cos(ageInTicks * 0.25F)));
				this.Spike4.rotateAngleX = (-0.4375F + (MathHelper.cos(ageInTicks * 0.25F)));
			}}
			}
		}

		
		