package net.mrbt0907.ageofminecraft.models;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.tier1.EntitySheep;
@SideOnly(Side.CLIENT)

public class ModelSheep1 extends ModelQuadruped
{
	private float headRotationAngleX;
	public ModelSheep1()
	{
		super(12, 0.0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3.0F, -4.0F, -4.0F, 6, 6, 6, 0.6F);
		head.setRotationPoint(0.0F, 6.0F, -8.0F);
		body = new ModelRenderer(this, 28, 8);
		body.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 1.75F);
		body.setRotationPoint(0.0F, 5.0F, 2.0F);
		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F);
		leg1.setRotationPoint(-3.0F, 12.0F, 7.0F);
		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F);
		leg2.setRotationPoint(3.0F, 12.0F, 7.0F);
		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F);
		leg3.setRotationPoint(-3.0F, 12.0F, -5.0F);
		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F);
		leg4.setRotationPoint(3.0F, 12.0F, -5.0F);
	}
	
	public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limSwingAmount, float partialTicks)
	{
		super.setLivingAnimations(entity, limbSwing, limSwingAmount, partialTicks);
		head.rotationPointY = (6.0F + ((EntitySheep)entity).getHeadRotationPointY(partialTicks) * 9.0F);
		headRotationAngleX = ((EntitySheep)entity).getHeadRotationAngleX(partialTicks);
	}
	
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch, float scale, Entity entity)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, age, netHeadYaw, headPitch, scale, entity);
		head.rotateAngleX = headRotationAngleX;
	}
}


