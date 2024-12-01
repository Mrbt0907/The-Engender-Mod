package net.mrbt0907.ageofminecraft.registry;
import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.mrbt0907.ageofminecraft.EngenderMod;

public class FusionRecipeRegistry
{
	public static final FusionRecipeRegistry INSTANCE = new FusionRecipeRegistry();
	private List<FusionRecipe> fusionEntries = new ArrayList<FusionRecipe>();
	private ImmutableList<FusionRecipe> fusionRecipes;
	
	
	public void postInit()
	{
		if (fusionRecipes == null)
		{
			fusionRecipes = ImmutableList.copyOf(fusionEntries);
			fusionEntries = null;
		}
		else
			EngenderMod.error("Tried to refinalize all fusion recipes when they are already finalized");
	}

	public void addRecipe(ItemStack input, ItemStack output, int mana, int entropy, int fusionTime)
	{
		if (fusionEntries == null)
		{
			EngenderMod.error("Tried to register a fusion recipe after recipes have been finalized");
			return;
		}
		FusionRecipe recipe = new FusionRecipe(input, output, mana, entropy, fusionTime);
		if (fusionEntries.contains(recipe))
		{
			EngenderMod.error("Tried to register a fusion recipe that alreay exists");
			return;
		}
		
		fusionEntries.add(recipe);
		EngenderMod.debug("Registered fusion recipe " + recipe);
	}
	
	public void removeRecipe(ItemStack input, ItemStack output)
	{
		if (fusionEntries == null)
		{
			EngenderMod.error("Tried to remove a fusion recipe after recipes have been finalized");
			return;
		}
		FusionRecipe recipe = new FusionRecipe(input, output, 0, 0, 0);
		if (fusionEntries.contains(recipe))
		{
			fusionEntries.remove(recipe);
			EngenderMod.debug("Removed fusion recipe " + recipe);
		}
		else
			EngenderMod.error("Tried to remove a fusion recipe that was not registered");
	}
	
	public boolean exists(ItemStack input)
	{
		return fusionEntries == null ? fusionRecipes.contains(new FusionRecipe(input, null, 0, 0, 0)) : fusionEntries.contains(new FusionRecipe(input, null, 0, 0, 0));
	}
	
	public FusionRecipe getRecipe(ItemStack input)
	{
		if (fusionEntries == null)
		{
			for (FusionRecipe recipe : fusionRecipes)
				if (equals(recipe.input, input))
					return recipe;
		}
		else
		{
			for (FusionRecipe recipe : fusionEntries)
				if (equals(recipe.input, input))
					return recipe;
		}
		return null;
	}
	
	public List<FusionRecipe> getRecipes()
	{
		
		return new ArrayList<FusionRecipe>(fusionEntries == null ? fusionRecipes : fusionEntries);
	}
	
	private boolean equals(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem().equals(stack1.getItem()) && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }
	
	public static class FusionRecipe
	{
		public final ItemStack input;
		public final ItemStack output;
		public final int mana;
		public final int entropy;
		public final int fusionTime;
		
		public FusionRecipe(ItemStack input, ItemStack output, int mana, int entropy, int fusionTime)
		{
			this.input = input;
			this.output = output;
			this.mana = mana;
			this.entropy = entropy;
			this.fusionTime = fusionTime;
		}
		
		@Override
		public String toString()
		{
			return "FusionRecipe{input: " + input + ", output: " + output + ", fusionTime: " + fusionTime + "}";
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof FusionRecipe && ((FusionRecipe)obj).input == input;
		}
	}
}