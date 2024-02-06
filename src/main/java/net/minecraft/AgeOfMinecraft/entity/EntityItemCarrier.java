package net.minecraft.AgeOfMinecraft.entity;

import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.AgeOfMinecraft.registry.EItem;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class EntityItemCarrier extends EntityItem
{
	public EntityItemCarrier(World worldIn)
	{
		super(worldIn);
	}

	public void setDead()
	{
		this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
		
		ItemStack stack = this.getItem();
		
		if(!stack.hasTagCompound())
		stack.setTagCompound(new NBTTagCompound());
		
		if (stack.getItem() == EItem.carrier)
		{
			if (stack.getTagCompound().hasKey("Entity"))
			{
				Entity entity = EntityList.createEntityFromNBT(stack.getTagCompound().getCompoundTag("Entity"), world);
				if (entity instanceof EntityFriendlyCreature)
				{
					EntityFriendlyCreature entityliving = (EntityFriendlyCreature)entity;
					entityliving.writeToNBT(stack.getTagCompound().getCompoundTag("Entity"));
					entityliving.playLivingSound();
					if (!entityliving.isWild() && (entityliving instanceof EntityEnderDragon))
					entityliving.setLocationAndAngles(entityliving.getOwner().posX, entityliving.getOwner().posY + 4D, entityliving.getOwner().posZ, entityliving.rotationYaw, entityliving.rotationPitch);
					else
					entityliving.setLocationAndAngles(posX, posY, posZ, entityliving.rotationYaw, entityliving.rotationPitch);
					entity.copyLocationAndAnglesFrom(this);
					entity.world.setEntityState(entity, (byte)20);
					entity.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 2.0F);
					entity.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 2.0F);
					entity.playSound(ESound.createMob, 1.0F, 1.0F);
					entity.world.setEntityState(entity, (byte)35);
					stack.getTagCompound().removeTag("Entity");
					stack.getTagCompound().removeTag("EntityName");
					if (!world.isRemote)
					world.spawnEntity(entity);
				}
			}
		}

		super.setDead();
	}
}
