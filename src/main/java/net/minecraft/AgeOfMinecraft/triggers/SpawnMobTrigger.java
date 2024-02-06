package net.minecraft.AgeOfMinecraft.triggers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;


public class SpawnMobTrigger implements ICriterionTrigger<SpawnMobTrigger.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation("spawn_mob");
	private final Map<PlayerAdvancements, SpawnMobTrigger.Listeners> listeners = Maps.<PlayerAdvancements, SpawnMobTrigger.Listeners>newHashMap();
	
	public ResourceLocation getId()
	{
		return ID;
	}

	public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<SpawnMobTrigger.Instance> listener)
	{
		SpawnMobTrigger.Listeners tameanimaltrigger$listeners = this.listeners.get(playerAdvancementsIn);
		
		if (tameanimaltrigger$listeners == null)
		{
			tameanimaltrigger$listeners = new SpawnMobTrigger.Listeners(playerAdvancementsIn);
			this.listeners.put(playerAdvancementsIn, tameanimaltrigger$listeners);
		}

		tameanimaltrigger$listeners.add(listener);
	}

	public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<SpawnMobTrigger.Instance> listener)
	{
		SpawnMobTrigger.Listeners tameanimaltrigger$listeners = this.listeners.get(playerAdvancementsIn);
		
		if (tameanimaltrigger$listeners != null)
		{
			tameanimaltrigger$listeners.remove(listener);
			
			if (tameanimaltrigger$listeners.isEmpty())
			{
				this.listeners.remove(playerAdvancementsIn);
			}
		}
	}

	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
	{
		this.listeners.remove(playerAdvancementsIn);
	}

	/**
	* Deserialize a ICriterionInstance of this trigger from the data in the JSON.
	*/
	public SpawnMobTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
	{
		EntityPredicate entitypredicate = EntityPredicate.deserialize(json.get("entity"));
		return new SpawnMobTrigger.Instance(entitypredicate);
	}

	public void trigger(EntityPlayerMP player, EntityFriendlyCreature entity)
	{
		SpawnMobTrigger.Listeners tameanimaltrigger$listeners = this.listeners.get(player.getAdvancements());
		
		if (tameanimaltrigger$listeners != null)
		{
			tameanimaltrigger$listeners.trigger(player, entity);
		}
	}

	public static class Instance extends AbstractCriterionInstance
	{
		private final EntityPredicate entity;
		
		public Instance(EntityPredicate entity)
		{
			super(SpawnMobTrigger.ID);
			this.entity = entity;
		}

		public boolean test(EntityPlayerMP player, EntityFriendlyCreature entity)
		{
			return this.entity.test(player, entity);
		}
	}

	static class Listeners
	{
		private final PlayerAdvancements playerAdvancements;
		private final Set<ICriterionTrigger.Listener<SpawnMobTrigger.Instance>> listeners = Sets.<ICriterionTrigger.Listener<SpawnMobTrigger.Instance>>newHashSet();
		
		public Listeners(PlayerAdvancements playerAdvancementsIn)
		{
			this.playerAdvancements = playerAdvancementsIn;
		}

		public boolean isEmpty()
		{
			return this.listeners.isEmpty();
		}

		public void add(ICriterionTrigger.Listener<SpawnMobTrigger.Instance> listener)
		{
			this.listeners.add(listener);
		}

		public void remove(ICriterionTrigger.Listener<SpawnMobTrigger.Instance> listener)
		{
			this.listeners.remove(listener);
		}

		public void trigger(EntityPlayerMP player, EntityFriendlyCreature entity)
		{
			List<ICriterionTrigger.Listener<SpawnMobTrigger.Instance>> list = null;
			
			for (ICriterionTrigger.Listener<SpawnMobTrigger.Instance> listener : this.listeners)
			{
				if (((SpawnMobTrigger.Instance)listener.getCriterionInstance()).test(player, entity))
				{
					if (list == null)
					{
						list = Lists.<ICriterionTrigger.Listener<SpawnMobTrigger.Instance>>newArrayList();
					}

					list.add(listener);
				}
			}

			if (list != null)
			{
				for (ICriterionTrigger.Listener<SpawnMobTrigger.Instance> listener1 : list)
				{
					listener1.grantCriterion(this.playerAdvancements);
				}
			}
		}
	}
}