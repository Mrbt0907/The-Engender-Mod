package net.mrbt0907.ageofminecraft.models;

import net.minecraft.client.model.ModelRenderer;
import net.mrbt0907.ageofminecraft.renders.IModelHead;

public class ModelChicken extends net.minecraft.client.model.ModelChicken implements IModelHead
{
	@Override
	public ModelRenderer getHead(){return head;}
}
