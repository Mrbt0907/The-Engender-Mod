package net.mrbt0907.ageofminecraft.entity.tier6.witherstorm;

import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.EnumTier;

public class EntityWitherStorm extends EntityEngendered
{
	private static final DataParameter<Float> MASS = EntityDataManager.createKey(EntityWitherStorm.class, DataSerializers.FLOAT);
	private float[] xRotationHeads = new float[2];
	private float[] yRotationHeads = new float[2];
	
	public EntityWitherStorm(World world)
	{
		super(world);
		setSize(1.0F, 3.3F);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		//if (dataManager.get(MASS) == null)
		dataManager.register(MASS, Float.valueOf(0.0F));
	}
	
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setMass(nbt.getFloat("Mass"));
	}
	
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setFloat("Mass", getMass());
	}
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
	}
	
	//----- OVERRIDES -----\\
	@Override
	public boolean isNonBoss() {return false;}
	protected SoundEvent getAmbientSound() {return SoundEvents.ENTITY_WITHER_AMBIENT;}
	protected SoundEvent getHurtSound(DamageSource source) {return SoundEvents.ENTITY_WITHER_HURT;}
	protected SoundEvent getDeathSound() {return SoundEvents.ENTITY_WITHER_DEATH;}
	
	//----- GETTERS & SETTERS -----\\
	public float getMass()
	{
		return dataManager.get(MASS).floatValue();
	}
	
	public void setMass(float mass)
	{
		dataManager.set(MASS, Float.valueOf(mass));
	}
	
	//Base Stats
	@Override
	public EnumTier getTier() {return EnumTier.TIER6;}
	@Override
	public long getBaseVigor() {return 100;}
	@Override
	public long getBaseStrength() {return 100;}
	@Override
	public long getBaseStamina() {return 100;}
	@Override
	public long getBaseIntelligence() {return 100;}
	@Override
	public long getBaseDexterity() {return 100;}
	@Override
	public long getBaseAgility(){return 100;}
	
	//----- Wither Stuff -----\\
	@SideOnly(Side.CLIENT)
	public float getHeadXRotation(int headIndex)
	{
		return this.xRotationHeads[headIndex];
	}
	
	@SideOnly(Side.CLIENT)
	public float getHeadYRotation(int headIndex)
	{
		return this.yRotationHeads[headIndex];
	}
}
