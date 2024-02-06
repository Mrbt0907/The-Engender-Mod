package net.minecraft.AgeOfMinecraft.entity.other;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public interface IBeamHitboxHandler
{
	public void onBlockCollision(BlockPos pos);
	public void onEntityCollision(Entity entity);
}
