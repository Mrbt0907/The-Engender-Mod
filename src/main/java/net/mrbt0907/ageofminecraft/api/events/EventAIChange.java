package net.mrbt0907.ageofminecraft.api.events;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;
import net.mrbt0907.ageofminecraft.entity.ai.eng.EnumAIStance;

public class EventAIChange extends Event
{
	public final EntityEngendered entity;
	protected final List<EntityAITaskEntry> oldTasks = new ArrayList<EntityAITaskEntry>();
	protected final List<EntityAITaskEntry> oldTargetTasks =  new ArrayList<EntityAITaskEntry>();
	protected final List<EntityAITaskEntry> tasks = new ArrayList<EntityAITaskEntry>();
	protected final List<EntityAITaskEntry> targetTasks =  new ArrayList<EntityAITaskEntry>();
	
	public EventAIChange(EntityEngendered entity)
	{
		this.entity = entity;
		oldTasks.addAll(EntityAITaskEntry.convert(entity.tasks));
		oldTargetTasks.addAll(EntityAITaskEntry.convert(entity.targetTasks));
		tasks.addAll(oldTasks);
		targetTasks.addAll(oldTargetTasks);
	}
	
	public EnumAIStance getStance()
	{
		return entity.getStance();
	}
	
	public void addTask(int priority, @Nonnull EntityAIBase task)
	{
		tasks.add(new EntityAITaskEntry(priority, task));
	}
	
	public void addTargetTask(int priority, @Nonnull EntityAIBase task)
	{
		targetTasks.add(new EntityAITaskEntry(priority, task));
	}
	
	public void removeTask(int index)
	{
		tasks.remove(index);
	}
	
	public void removeTask(EntityAITaskEntry task)
	{
		tasks.remove(task);
	}
	
	public void removeTargetTask(int index)
	{
		targetTasks.remove(index);
	}
	
	public void removeTargetTask(EntityAITaskEntry task)
	{
		targetTasks.remove(task);
	}
	
	public List<EntityAITaskEntry> getTasks()
	{
		return tasks;
	}
	
	public List<EntityAITaskEntry> getTargetTasks()
	{
		return targetTasks;
	}
	
	public List<EntityAITaskEntry> getOriginalTasks()
	{
		return new ArrayList<EntityAITaskEntry>(oldTasks);
	}
	
	public List<EntityAITaskEntry> getOriginalTargetTasks()
	{
		return new ArrayList<EntityAITaskEntry>(oldTargetTasks);
	}
	
	public int getTaskAmount()
	{
		return tasks.size();
	}
	
	public int getTargetTaskAmount()
	{
		return targetTasks.size();
	}
	
	public static class EntityAITaskEntry
	{
		public final EntityAIBase action;
		public final int priority;
		
		public EntityAITaskEntry(EntityAITasks.EntityAITaskEntry task)
		{
			this(task.priority, task.action);
		}
		
		public EntityAITaskEntry(int priority, EntityAIBase task)
		{
			this.priority = priority;
			action = task;
		}

		public static List<EntityAITaskEntry> convert(EntityAITasks taskList)
		{
			List<EntityAITaskEntry> tasks = new ArrayList<EntityAITaskEntry>();
			for (EntityAITasks.EntityAITaskEntry task : taskList.taskEntries)
				tasks.add(new EntityAITaskEntry(task));
			return tasks;
		}
		
		public boolean equals(@Nullable Object obj)
		{
			return this == obj || obj != null && getClass() == obj.getClass() ? action.equals(((EntityAITasks.EntityAITaskEntry)obj).action) : false;
		}

		public int hashCode()
		{
			return action.hashCode();
		}
	}
}
