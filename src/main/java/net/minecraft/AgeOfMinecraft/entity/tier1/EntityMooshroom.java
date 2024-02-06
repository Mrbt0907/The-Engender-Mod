package net.minecraft.AgeOfMinecraft.entity.tier1;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.registry.ELoot;
import net.minecraft.AgeOfMinecraft.registry.ESound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityMooshroom extends EntityCow implements net.minecraftforge.common.IShearable
{
	public EntityMooshroom(World worldIn)
	{
		super(worldIn);
		setSize(0.9F, 1.4F);
		this.spawnableBlock = Blocks.MYCELIUM;
		this.experienceValue = 3;
	}
	public EntityFriendlyCreature spawnBaby(EntityFriendlyCreature par1idleTimeable)
	{
		return new EntityMooshroom(this.world);
	}


	public boolean interact(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (hasOwner(player) && !stack.isEmpty() && (stack.getItem() == Items.BOWL))
		{
			player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
			stack.shrink(1);
			
			if (stack.isEmpty())
			{
				player.setHeldItem(hand, new ItemStack(Items.MUSHROOM_STEW));
			}
			else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MUSHROOM_STEW)))
			{
				player.dropItem(new ItemStack(Items.MUSHROOM_STEW), false);
			}
			return true;
		}
		else if (!stack.isEmpty() && stack.getItem() == Items.SHEARS)
		{
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + this.height, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
			if (!this.world.isRemote)
			{
				for (int i = 0; i < 5; i++)
				{
					this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY + this.height, this.posZ, new ItemStack(Blocks.RED_MUSHROOM)));
				}
				stack.damageItem(16, player);
				playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5F, 0.5F);
			}
			return true;
		}
		else
		{
			return super.interact(player, hand);
		}
	}
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Nullable
	protected ResourceLocation getLootTable()
	{
		return ELoot.ENTITIES_MUSHROOM_COW;
	}
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + this.height / 2.0F, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		for (int i = 0; i < 5; i++)
		{
			ret.add(new ItemStack(Blocks.RED_MUSHROOM));
		}
		playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0F, 0.7F);
		return ret;
	}
	protected SoundEvent getRegularHurtSound()
	{
		return ESound.woodHit;
	}
	protected SoundEvent getPierceHurtSound()
	{
		return ESound.woodHitPierce;
	}
	protected SoundEvent getCrushHurtSound()
	{
		return ESound.woodHitCrush;
	}
}


