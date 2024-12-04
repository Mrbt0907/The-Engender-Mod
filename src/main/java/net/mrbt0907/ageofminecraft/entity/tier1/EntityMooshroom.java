package net.mrbt0907.ageofminecraft.entity.tier1;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.mrbt0907.ageofminecraft.registry.LootRegistry;

public class EntityMooshroom extends EntityCow implements net.minecraftforge.common.IShearable
{
	public EntityMooshroom(World worldIn)
	{
		super(worldIn);
		setSize(0.9F, 1.4F);
		//spawnableBlock = Blocks.MYCELIUM;
		experienceValue = 3;
	}
	
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute(INTELLIGENCE).setBaseValue(30);
	}
/*
 * 	
 * 
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
			world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY + height, posZ, 0.0D, 0.0D, 0.0D, new int[0]);
			if (!world.isRemote)
			{
				for (int i = 0; i < 5; i++)
				{
					world.spawnEntity(new EntityItem(world, posX, posY + height, posZ, new ItemStack(Blocks.RED_MUSHROOM)));
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
	}*/
	
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Nullable
	protected ResourceLocation getLootTable()
	{
		return LootRegistry.ENTITIES_MUSHROOM_COW;
	}
	
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (int i = 0; i < 5; i++)
			items.add(new ItemStack(Blocks.RED_MUSHROOM));
		return items;
	}
}