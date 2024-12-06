package net.mrbt0907.ageofminecraft.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.entity.EntityEngendered;

@SideOnly(Side.CLIENT)
public class RenderUtil
{
	public static void doSpawnRender(EntityEngendered entity, float partialTicks)
	{
		if (entity.ticksExisted < 22 && entity.doSpawnAnimation)
		{
			float f5 = MathHelper.sqrt((entity.ticksExisted + partialTicks - 1.0F) * 0.08F);
			if (f5 > 1.0F)
				f5 = 1.0F;
			GlStateManager.scale(f5, f5, f5);
			GlStateManager.rotate(f5 * 90F - 90F, f5, f5, f5);
		}
	}
}
