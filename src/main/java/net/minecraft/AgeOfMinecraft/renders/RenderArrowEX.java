package net.minecraft.AgeOfMinecraft.renders;

import net.endermanofdoom.mac.entity.EntityArrowEX;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderArrowEX extends RenderArrow<EntityArrowEX>
{

	public RenderArrowEX(RenderManager manager)
	{
		super(manager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityArrowEX entity)
	{
		return entity.getTexture();
	}

}
