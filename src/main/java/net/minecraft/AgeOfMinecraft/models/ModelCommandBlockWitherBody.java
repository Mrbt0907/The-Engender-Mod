package net.minecraft.AgeOfMinecraft.models;
import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelCommandBlockWitherBody
extends ModelBase
{
	public ModelRenderer Body1;
	public ModelRenderer Body2;
	public ModelRenderer Body3;
	public ModelRenderer Body4;
	public ModelRenderer Body5;
	public ModelRenderer Body6;
	public ModelRenderer Body7;
	public ModelRenderer Body8;
	public ModelRenderer Body9;
	public ModelRenderer Body10;
	public ModelCommandBlockWitherBody()
	{
		Random random = new Random(1660L);
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.Body3 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body3.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body3.addBox(0.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
		this.Body8 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body8.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body8.addBox(4.0F, 2.0F, 0.0F, 8, 8, 8, 0.0F);
		this.Body9 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body9.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body9.addBox(6.0F, 2.0F, -4.0F, 8, 8, 8, 0.0F);
		this.Body7 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body7.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body7.addBox(-4.0F, -7.0F, -2.0F, 8, 8, 8, 0.0F);
		this.Body10 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body10.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body10.addBox(-4.0F, 8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.Body6 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body6.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body6.addBox(2.0F, 4.0F, -4.0F, 8, 8, 8, 0.0F);
		this.Body5 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body5.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body5.addBox(-10.0F, 4.0F, -4.0F, 8, 8, 8, 0.0F);
		this.Body4 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body4.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body4.addBox(-8.0F, -2.0F, 0.0F, 8, 8, 8, 0.0F);
		this.Body1 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body1.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body1.addBox(-2.0F, 0.0F, -2.0F, 8, 8, 8, 0.0F);
		this.Body2 = new ModelRenderer(this, random.nextInt(16), random.nextInt(16));
		this.Body2.setRotationPoint(0.0F, 16.0F, 0.0F);
		this.Body2.addBox(-12.0F, 1.0F, -4.0F, 8, 8, 8, 0.0F);
	}
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 0.0F);
		GlStateManager.scale(1.51F, 1.51F, 1.51F);
		GlStateManager.translate(0.0F, 1.0F, -0.45F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		this.Body3.render(f5);
		this.Body8.render(f5);
		this.Body9.render(f5);
		this.Body7.render(f5);
		this.Body10.render(f5);
		this.Body6.render(f5);
		this.Body5.render(f5);
		this.Body4.render(f5);
		this.Body1.render(f5);
		this.Body2.render(f5);
		GlStateManager.popMatrix();
	}
}


