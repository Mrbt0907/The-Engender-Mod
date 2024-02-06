package net.minecraft.AgeOfMinecraft.blocks;

import net.minecraft.AgeOfMinecraft.items.ItemFusion;
import net.minecraft.AgeOfMinecraft.items.ItemManaCollector;
import net.minecraft.AgeOfMinecraft.items.ItemTierItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ContainerMobSpawner extends Container
{
	private final IInventory tileFuser;
	private int currentItemBurnTime;
	private int fuseTime;
	private int totalSpawnMobTime;
	public int mana;
	public int entropy;
	
	public ContainerMobSpawner(InventoryPlayer playerInventory, IInventory tileBrewingStandIn)
	{
		this.tileFuser = tileBrewingStandIn;
		this.addSlotToContainer(new ContainerMobSpawner.Ingredient(tileBrewingStandIn, 0, 79, 17));
		this.addSlotToContainer(new ContainerMobSpawner.Fuel(tileBrewingStandIn, 1, 27, 4));
		this.addSlotToContainer(new ContainerMobSpawner.Result(tileBrewingStandIn, 2, 79, 58));
		
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k)
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.tileFuser);
	}

	/**
	* Looks for changes made in the container, sends them to every listener.
	*/
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (int i = 0; i < this.listeners.size(); ++i)
		{
			IContainerListener icontainerlistener = this.listeners.get(i);
			
			if (this.currentItemBurnTime != this.tileFuser.getField(0))
			{
				icontainerlistener.sendWindowProperty(this, 0, this.tileFuser.getField(0));
			}

			if (this.fuseTime != this.tileFuser.getField(1))
			{
				icontainerlistener.sendWindowProperty(this, 1, this.tileFuser.getField(1));
			}
			if (this.totalSpawnMobTime != this.tileFuser.getField(2))
			{
				icontainerlistener.sendWindowProperty(this, 2, this.tileFuser.getField(2));
			}

			if (this.mana != this.tileFuser.getField(3))
			{
				icontainerlistener.sendWindowProperty(this, 3, this.tileFuser.getField(3));
			}

			if (this.entropy != this.tileFuser.getField(4))
			{
				icontainerlistener.sendWindowProperty(this, 4, this.tileFuser.getField(4));
			}
		}

		this.currentItemBurnTime = this.tileFuser.getField(0);
		this.fuseTime = this.tileFuser.getField(1);
		this.totalSpawnMobTime = this.tileFuser.getField(2);
		this.mana = this.tileFuser.getField(3);
		this.entropy = this.tileFuser.getField(4);
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		this.tileFuser.setField(id, data);
	}

	/**
	* Determines whether supplied player can use this container
	*/
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tileFuser.isUsableByPlayer(playerIn);
	}

	/**
	* Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	* inventory and the other inventory(s).
	*/
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index == 2)
			{
				if (!this.mergeItemStack(itemstack1, 3, 39, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index != 1 && index != 0)
			{
				if (itemstack1.getItem() instanceof ItemFusion)
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (itemstack1.getItem() instanceof ItemManaCollector)
				{
					if (!this.mergeItemStack(itemstack1, 1, 2, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 3 && index < 30)
				{
					if (!this.mergeItemStack(itemstack1, 30, 39, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 3, 39, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	static class Fuel extends Slot
	{
		public Fuel(IInventory iInventoryIn, int index, int xPosition, int yPosition)
		{
			super(iInventoryIn, index, xPosition, yPosition);
		}

		/**
		* Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
		*/
		public boolean isItemValid(ItemStack stack)
		{
			return canHoldItem(stack);
		}
		public static boolean canHoldItem(ItemStack stack)
		{
			return stack.getItem() instanceof ItemManaCollector;
		}

		/**
		* Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
		* the case of armor slots)
		*/
		public int getSlotStackLimit()
		{
			return 1;
		}
	}

	static class Ingredient extends Slot
	{
		public Ingredient(IInventory iInventoryIn, int index, int xPosition, int yPosition)
		{
			super(iInventoryIn, index, xPosition, yPosition);
		}

		/**
		* Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
		*/
		public boolean isItemValid(ItemStack stack)
		{
			return canHoldItem(stack);
		}
		public static boolean canHoldItem(ItemStack stack)
		{
			return stack.getItem() instanceof ItemFusion;
		}

		/**
		* Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
		* the case of armor slots)
		*/
		public int getSlotStackLimit()
		{
			return 64;
		}
	}

	static class Result extends Slot
	{
		public Result(IInventory p_i47598_1_, int p_i47598_2_, int p_i47598_3_, int p_i47598_4_)
		{
			super(p_i47598_1_, p_i47598_2_, p_i47598_3_, p_i47598_4_);
		}

		/**
		* Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in
		* the case of armor slots)
		*/
		public int getSlotStackLimit()
		{
			return 64;
		}

		public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
		{
			if (thePlayer instanceof EntityPlayerMP)
			{
				CriteriaTriggers.INVENTORY_CHANGED.trigger((EntityPlayerMP)thePlayer, ((EntityPlayerMP)thePlayer).inventory);
			}

			super.onTake(thePlayer, stack);
			return stack;
		}

		/**
		* Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
		*/
		public boolean isItemValid(ItemStack stack)
		{
			return canHoldItem(stack);
		}
		public static boolean canHoldItem(ItemStack stack)
		{
			return stack.getItem() instanceof ItemTierItem;
		}
	}
}