package net.minecraft.AgeOfMinecraft.models;
import net.minecraft.AgeOfMinecraft.entity.tier4.EntityGuardian;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)

public class ModelGuardian extends ModelBase
{
	public ModelRenderer guardianBody;
	private ModelRenderer guardianEye;
	private ModelRenderer[] guardianSpines;
	private ModelRenderer[] guardianTail;
	public ModelGuardian()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.guardianSpines = new ModelRenderer[12];
		this.guardianBody = new ModelRenderer(this);
		this.guardianBody.setTextureOffset(0, 0).addBox(-6.0F, -6.0F, -8.0F, 12, 12, 16);
		this.guardianBody.setTextureOffset(0, 28).addBox(-8.0F, -6.0F, -6.0F, 2, 12, 12);
		this.guardianBody.setTextureOffset(0, 28).addBox(6.0F, -6.0F, -6.0F, 2, 12, 12, true);
		this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, -8.0F, -6.0F, 12, 2, 12);
		this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 6.0F, -6.0F, 12, 2, 12);
		for (int i = 0; i < this.guardianSpines.length; i++)
		{
			this.guardianSpines[i] = new ModelRenderer(this, 0, 0);
			this.guardianSpines[i].addBox(-1.0F, -8.5F, -1.0F, 2, 9, 2);
			this.guardianBody.addChild(this.guardianSpines[i]);
		}
		this.guardianEye = new ModelRenderer(this, 8, 0);
		this.guardianEye.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 1);
		this.guardianBody.addChild(this.guardianEye);
		this.guardianTail = new ModelRenderer[3];
		this.guardianTail[0] = new ModelRenderer(this, 40, 0);
		this.guardianTail[0].addBox(-2.0F, -2.0F, 7.0F, 4, 4, 8);
		this.guardianTail[1] = new ModelRenderer(this, 0, 54);
		this.guardianTail[1].addBox(0.0F, -2.0F, 0.0F, 3, 3, 7);
		this.guardianTail[2] = new ModelRenderer(this);
		this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0F, -2.0F, 0.0F, 2, 2, 6);
		this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0F, -5.5F, 3.0F, 1, 9, 9);
		this.guardianBody.addChild(this.guardianTail[0]);
		this.guardianTail[0].addChild(this.guardianTail[1]);
		this.guardianTail[1].addChild(this.guardianTail[2]);
	}
	public int func_178706_a()
	{
		return 54;
	}
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		this.guardianBody.render(p_78088_7_);
	}
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	{
		EntityGuardian entityguardian = (EntityGuardian)p_78087_7_;
		float f6 = p_78087_3_ - (entityguardian.ticksExisted);
		this.guardianBody.rotateAngleY = (p_78087_4_ / 57.295776F);
		this.guardianBody.rotateAngleX = !entityguardian.isEntityAlive() ? 0F : (p_78087_5_ / 57.295776F);
		float[] afloat = { 1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F };
	float[] afloat1 = { 0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F };
float[] afloat2 = { 0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F };
float[] afloat3 = { 0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F };
float[] afloat4 = { -8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F };
float[] afloat5 = { 8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F };
float f7 = (1.0F - (entityguardian.isAIDisabled() ? 0.5F : entityguardian.func_175469_o(f6))) * 0.55F;
for (int i = 0; i < 12; i++)
{
	this.guardianSpines[i].rotateAngleX = (3.1415927F * afloat[i]);
	this.guardianSpines[i].rotateAngleY = (3.1415927F * afloat1[i]);
	this.guardianSpines[i].rotateAngleZ = (3.1415927F * afloat2[i]);
	this.guardianSpines[i].rotationPointX = (afloat3[i] * (1.0F + MathHelper.cos((entityguardian.isAIDisabled() ? 0F : p_78087_3_) * 1.5F + i) * 0.01F - f7));
	this.guardianSpines[i].rotationPointY = (afloat4[i] * (1.0F + MathHelper.cos((entityguardian.isAIDisabled() ? 0F : p_78087_3_) * 1.5F + i) * 0.01F - f7));
	this.guardianSpines[i].rotationPointZ = (afloat5[i] * (1.0F + MathHelper.cos((entityguardian.isAIDisabled() ? 0F : p_78087_3_) * 1.5F + i) * 0.01F - f7));
}
this.guardianEye.rotationPointZ = -8.25F;
Object object = Minecraft.getMinecraft().getRenderViewEntity();
if (entityguardian.hasTargetedEntity())
{
	object = entityguardian.getTargetedEntity();
}
if (object != null)
{
	Vec3d vec3 = ((Entity)object).getPositionEyes(0.0F);
	Vec3d vec31 = p_78087_7_.getPositionEyes(0.0F);
	double d0 = vec3.y - vec31.y;
	if (d0 <= 0.0D || !entityguardian.getCurrentBook().isEmpty() || entityguardian.isAIDisabled())
	{
		this.guardianEye.rotationPointY = 1.0F;
	}
	else
	{
		this.guardianEye.rotationPointY = 0.0F;
	}
	Vec3d vec32 = p_78087_7_.getLook(0.0F);
	vec32 = new Vec3d(vec32.x, 0.0D, vec32.z);
	Vec3d vec33 = new Vec3d(vec31.x - vec3.x, 0.0D, vec31.z - vec3.z).normalize().rotateYaw(1.5707964F);
	double d1 = vec32.dotProduct(vec33);
	this.guardianEye.rotationPointX = entityguardian.isAIDisabled() ? 0F : (!entityguardian.getCurrentBook().isEmpty() ? (MathHelper.sin((p_78087_3_) * 0.1F + MathHelper.sin((p_78087_3_) * 0.05F)) * 1.5F) : (MathHelper.sqrt((float)Math.abs(d1)) * 2.0F * (float)Math.signum(d1)));
}
this.guardianEye.showModel = true;
float f8 = entityguardian.func_175471_a(f6);
this.guardianTail[0].rotateAngleX = MathHelper.cos(p_78087_3_ * 0.05F) * 0.05F;
this.guardianTail[0].rotateAngleY = (MathHelper.sin(f8) * 3.1415927F * 0.05F);
this.guardianTail[1].rotateAngleY = (MathHelper.sin(f8) * 3.1415927F * 0.1F);
this.guardianTail[1].rotationPointX = -1.5F;
this.guardianTail[1].rotationPointY = 0.5F;
this.guardianTail[1].rotationPointZ = 14.0F;
this.guardianTail[2].rotateAngleY = (MathHelper.sin(f8) * 3.1415927F * 0.15F);
this.guardianTail[2].rotationPointX = 0.5F;
this.guardianTail[2].rotationPointY = 0.5F;
this.guardianTail[2].rotationPointZ = 6.0F;

if (entityguardian.getJukeboxToDanceTo() != null)
{
	this.guardianBody.rotateAngleY += (MathHelper.sin(p_78087_3_ * 0.25F) * 0.125F);
	for (int k = 0; k < this.guardianTail.length; k++)
	{
		this.guardianTail[k].rotateAngleY = MathHelper.sin(-1 - k + (entityguardian.isAIDisabled() ? 1 : p_78087_3_) * 0.25F) * 0.5F;
	}
}
}
}


