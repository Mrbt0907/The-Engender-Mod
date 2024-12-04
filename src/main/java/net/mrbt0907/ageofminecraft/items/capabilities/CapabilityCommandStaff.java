package net.mrbt0907.ageofminecraft.items.capabilities;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;

public class CapabilityCommandStaff
{
	protected final List<EntityEngendered> entities = new ArrayList<EntityEngendered>();
	
	
	public boolean selectUnit(Entity owner, EntityEngendered entity)
	{
		return selectUnit(owner, entity, false);
	}
	
	public boolean selectUnit(Entity owner, EntityEngendered entity, boolean addUnit)
	{
		if (entity == null || !entity.isEntityAlive() || !owner.getUniqueID().equals(entity.getOwnerId())) return false;
		
		if (addUnit)
		{
			if (entities.contains(entity))
			{
				if (entity.world.isRemote)
					entity.selected = false;
				entities.remove(entity);
			}
			else
			{
				entities.add(entity);
				if (entity.world.isRemote)
					entity.selected = true;
			}
		}
		else
		{
			if (entities.contains(entity))
			{
				if (entity.world.isRemote)
					entities.forEach(selectedEntity -> selectedEntity.selected = false);
				entities.clear();
			}
			else
			{
				entities.add(entity);
				if (entity.world.isRemote)
					entity.selected = true;
			}
		}
		return true;
	}
	
	public boolean isSelected(EntityEngendered entity)
	{
		return entities.contains(entity);
	}
	
	public boolean hasSelection()
	{
		return !entities.isEmpty();
	}
	
	public void clearSelection()
	{
		entities.clear();
	}
	
	public EnumAIStance getStance()
	{
		EnumAIStance stance = null;
		for (EntityEngendered entity : entities)
			if (stance == null)
				stance = entity.getStance();
			else if (!stance.equals(entity.getStance()))
				return null;
		return stance;
	}
	
	public void setStance(@Nonnull EnumAIStance stance)
	{
		entities.forEach(entity -> entity.setStance(stance));
	}
	
	public static class Storage implements Capability.IStorage<CapabilityCommandStaff>
	{
		@Override
		public NBTBase writeNBT(Capability<CapabilityCommandStaff> capability, CapabilityCommandStaff instance, EnumFacing side) {return new NBTTagCompound();}

		@Override
		public void readNBT(Capability<CapabilityCommandStaff> capability, CapabilityCommandStaff instance, EnumFacing side, NBTBase nbtData) {}
	}
	
	public static class Provider implements ICapabilitySerializable<NBTTagCompound>
	{
		@CapabilityInject(CapabilityCommandStaff.class)
		public static final Capability<CapabilityCommandStaff> INSTANCE = null;
		public static final EnumFacing FACE = EnumFacing.DOWN;
		private CapabilityCommandStaff defaultInstance = INSTANCE.getDefaultInstance();
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return capability == INSTANCE && facing == FACE;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			return capability == INSTANCE && facing == FACE? INSTANCE.cast(defaultInstance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return (NBTTagCompound) INSTANCE.writeNBT(defaultInstance, FACE);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			INSTANCE.readNBT(defaultInstance, FACE, nbt);
		}
	}
}
