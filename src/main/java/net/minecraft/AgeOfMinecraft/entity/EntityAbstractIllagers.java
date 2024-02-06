package net.minecraft.AgeOfMinecraft.entity;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityAbstractIllagers extends EntityFriendlyCreature
{
	protected static final DataParameter<Byte> AGGRESSIVE = EntityDataManager.<Byte>createKey(EntityAbstractIllagers.class, DataSerializers.BYTE);
	
	public EntityAbstractIllagers(World p_i47509_1_)
	{
		super(p_i47509_1_);
		this.setSize(0.5F, 1.9F);
		this.isOffensive = true;
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(AGGRESSIVE, Byte.valueOf((byte)0));
	}

	@SideOnly(Side.CLIENT)
	protected boolean isAggressive(int mask)
	{
		int i = ((Byte)this.dataManager.get(AGGRESSIVE)).byteValue();
		return (i & mask) != 0;
	}

	protected void setAggressive(int mask, boolean value)
	{
		int i = ((Byte)this.dataManager.get(AGGRESSIVE)).byteValue();
		
		if (value)
		{
			i = i | mask;
		}
		else
		{
			i = i & ~mask;
		}

		this.dataManager.set(AGGRESSIVE, Byte.valueOf((byte)(i & 255)));
	}

	/**
	* Get this Entity's EnumCreatureAttribute
	*/
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ILLAGER;
	}

	@SideOnly(Side.CLIENT)
	public EntityAbstractIllagers.IllagerArmPose getArmPose()
	{
		return EntityAbstractIllagers.IllagerArmPose.CROSSED;
	}

	@SideOnly(Side.CLIENT)
	public static enum IllagerArmPose
	{
		CROSSED,
		ATTACKING,
		SPELLCASTING,
		BOW_AND_ARROW;
	}
}