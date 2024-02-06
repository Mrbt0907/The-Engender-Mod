package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelTractorBeam extends net.minecraft.client.model.ModelBase
{
	public ModelRenderer shape1;
	public ModelTractorBeam()
	{
		this.textureWidth = 32;
		this.textureHeight = 256;
		this.shape1 = new ModelRenderer(this, 0, -8);
		this.shape1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.shape1.addBox(-4.0F, -256.0F, -4.0F, 8, 256, 8, 0.0F);
	}
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.shape1.render(f5);
	}
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}


