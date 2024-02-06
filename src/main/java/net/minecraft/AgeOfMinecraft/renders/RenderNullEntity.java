package net.minecraft.AgeOfMinecraft.renders;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class RenderNullEntity extends Render<Entity>
{
	public RenderNullEntity(RenderManager manager)
	{
		super(manager);
	}

	/**
	* Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	*/
	@Nullable
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return null;
	}
}