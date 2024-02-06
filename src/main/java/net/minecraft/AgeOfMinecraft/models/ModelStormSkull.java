package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelStormSkull extends net.minecraft.client.model.ModelBase
{
	public ModelRenderer Skull;
	public ModelStormSkull()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.Skull = new ModelRenderer(this, 0, 0);
		this.Skull.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Skull.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16, 0.0F);
	}
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		GlStateManager.pushMatrix();
		GlStateManager.scale(1F, -1F, 1F);
		GlStateManager.translate(0.0F, -2F, 0.0F);
		this.Skull.render(f5);
		GlStateManager.popMatrix();
	}
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		this.Skull.rotateAngleY = (netHeadYaw * 0.017453292F);
		this.Skull.rotateAngleX = (headPitch * 0.017453292F);
	}
}


