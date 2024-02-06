package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelBlaze extends ModelBase implements ICappedModel
{
	public ModelRenderer[] blazeSticks = new ModelRenderer[12];
	public ModelRenderer blazeHead;
	public ModelRenderer bipedCape;
	public boolean isSneak;
	public ModelBlaze()
	{
		for (int i = 0; i < this.blazeSticks.length; i++)
		{
			this.blazeSticks[i] = new ModelRenderer(this, 0, 16);
			this.blazeSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
		}
		this.blazeHead = new ModelRenderer(this, 0, 0);
		this.blazeHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		
		this.bipedCape = new ModelRenderer(this, 0, 0);
		this.bipedCape.setTextureSize(64, 32);
		this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, 0.0F);
	}
	public void render(Entity entityIn, float p_78088_2_, float limbSwing, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(p_78088_2_, limbSwing, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		
		if (this.isChild)
		{
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.75F, 0.75F, 0.75F);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
			this.blazeHead.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			for (int i = 0; i < this.blazeSticks.length; i++)
			{
				this.blazeSticks[i].render(scale);
			}
			GlStateManager.popMatrix();
		}
		else
		{
			this.blazeHead.render(scale);
			for (int i = 0; i < this.blazeSticks.length; i++)
			{
				this.blazeSticks[i].render(scale);
			}
		}
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
		ageInTicks = 0;
		this.blazeHead.rotateAngleX = headPitch * 0.017453292F;
		this.blazeHead.rotateAngleY = netHeadYaw * 0.017453292F;
		this.isChild = entity.isChild();
		this.isSneak = entity.isSneaking();
		float swing = MathHelper.sin(this.swingProgress * (float)Math.PI);
		float f = ageInTicks * (float)Math.PI * (entity.getJukeboxToDanceTo() != null ? -0.2F : -0.1F);
		this.blazeHead.rotationPointX = 0.0F;
		if (this.isSneak)
		{
			f *= 0.5F;
			this.blazeHead.rotationPointY = 8.0F;
		}
		else
		{
			this.blazeHead.rotationPointY = 4.0F;
		}
		for (int i = 0; i < 4; i++)
		{
			if (this.isSneak)
			{
				this.blazeSticks[i].rotationPointY = (2.0F + MathHelper.cos((i * 2 + ageInTicks) * 0.25F));
			}
			else
			{
				this.blazeSticks[i].rotationPointY = (-2.0F + MathHelper.cos((i * 2 + ageInTicks) * 0.25F));
			}
			this.blazeSticks[i].rotationPointX = (MathHelper.cos(f - swing) * (9.0F));
			this.blazeSticks[i].rotationPointZ = (MathHelper.sin(f - swing) * (9.0F));
			f += 1.5F;
		}
		f = ((float)Math.PI / 4F) + ageInTicks * (float)Math.PI * (entity.getJukeboxToDanceTo() != null ? 0.1F : 0.03F);
		for (int j = 4; j < 8; j++)
		{
			if (this.isSneak)
			{
				this.blazeSticks[j].rotationPointY = (5.0F + MathHelper.cos((j * 2 + ageInTicks) * 0.25F));
			}
			else
			{
				this.blazeSticks[j].rotationPointY = (2.0F + MathHelper.cos((j * 2 + ageInTicks) * 0.25F));
			}
			this.blazeSticks[j].rotationPointX = (MathHelper.cos(f) * (7.0F));
			this.blazeSticks[j].rotationPointZ = (MathHelper.sin(f) * (7.0F));
			f += 1.5F;
		}
		f = 0.47123894F + ageInTicks * (float)Math.PI * (entity.getJukeboxToDanceTo() != null ? -0.15F : -0.05F);
		for (int k = 8; k < 12; k++)
		{
			this.blazeSticks[k].rotationPointY = (11.0F + MathHelper.cos((k * 1.5F + ageInTicks) * 0.5F));
			this.blazeSticks[k].rotationPointX = (MathHelper.cos(f) * (5.0F));
			this.blazeSticks[k].rotationPointZ = (MathHelper.sin(f) * (5.0F));
			f += 1.5F;
		}
		this.blazeHead.rotateAngleX -= swing;
		if ((!entity.isEntityAlive() && !entity.onGround))
		{
			this.blazeHead.rotateAngleX -= 0.5F;
			this.blazeHead.rotateAngleY += MathHelper.cos(ageInTicks * 0.6662F) * 0.5F;
		}
		if (entity.getJukeboxToDanceTo() != null)
		{
			float f4 = MathHelper.sin(ageInTicks * 0.125F) * 0.7F;
			this.blazeHead.rotationPointX += (MathHelper.cos(ageInTicks * 0.25F) * 1F);
			this.blazeHead.rotationPointY += f4 + (MathHelper.cos(ageInTicks * 0.5F) * 1F);
			for (int k = 0; k < 12; k++)
			{
				f4 = MathHelper.sin(k * 2F + ageInTicks * 0.125F) * 0.7F;
				this.blazeSticks[k].rotationPointY += f4 + (MathHelper.cos((k * 1.5F + ageInTicks) * 0.5F));
			}
		}

		this.bipedCape.rotationPointY = this.blazeHead.rotationPointY;
	}
}


