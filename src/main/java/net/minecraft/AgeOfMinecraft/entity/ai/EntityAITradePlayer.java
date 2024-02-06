package net.minecraft.AgeOfMinecraft.entity.ai;
import net.minecraft.AgeOfMinecraft.entity.tier2.EntityVillager;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAITradePlayer extends EntityAIBase
{
	private EntityVillager villager;
	public EntityAITradePlayer(EntityVillager p_i1658_1_)
	{
		this.villager = p_i1658_1_;
		setMutexBits(5);
	}
	public boolean shouldExecute()
	{
		if (!this.villager.isEntityAlive())
		{
			return false;
		}
		if (this.villager.isInWater())
		{
			return false;
		}
		if (!this.villager.onGround)
		{
			return false;
		}
		if (this.villager.velocityChanged)
		{
			return false;
		}
		EntityPlayer entityplayer = this.villager.getCustomer();
		return entityplayer == null ? false : this.villager.getDistanceSq(entityplayer) > 16.0D ? false : entityplayer.openContainer instanceof Container;
	}
	public void startExecuting()
	{
		this.villager.getNavigator().clearPath();
	}
	public void resetTask()
	{
		this.villager.setCustomer((EntityPlayer)null);
	}
}


