package net.minecraft.AgeOfMinecraft.gui;

import net.minecraft.AgeOfMinecraft.entity.Animal;
import net.minecraft.AgeOfMinecraft.entity.Armored;
import net.minecraft.AgeOfMinecraft.entity.Elemental;
import net.minecraft.AgeOfMinecraft.entity.Ender;
import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.AgeOfMinecraft.entity.EnumTier;
import net.minecraft.AgeOfMinecraft.entity.Massive;
import net.minecraft.AgeOfMinecraft.entity.Structure;
import net.minecraft.AgeOfMinecraft.entity.Tiny;
import net.minecraft.AgeOfMinecraft.entity.Undead;
import net.minecraft.AgeOfMinecraft.entity.tier5.EntityEnderDragon;
import net.minecraft.AgeOfMinecraft.entity.tier6.EntityWitherStorm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class GuiEngenderMobInventory extends GuiContainer
{
	public static final ResourceLocation INVENTORY_BACKGROUND = new ResourceLocation("ageofminecraft", "textures/engender_inventory_new.png");
	/** The old x position of the mouse pointer */
	private float oldMouseX;
	/** The old y position of the mouse pointer */
	private float oldMouseY;
	private final EntityFriendlyCreature mob;
	private Slot theSlot;
	private ItemStack draggedStack = ItemStack.EMPTY;
	private Slot clickedSlot;
	private boolean isRightMouseClick;
	private int dragSplittingLimit;
	public GuiEngenderMobInventory(EntityPlayer player, EntityFriendlyCreature entity)
	{
		super(player.inventoryContainer);
		this.allowUserInput = true;
		this.mob = entity;
	}

	/**
	* Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	* window resizes, the buttonList is cleared beforehand.
	*/
	public void initGui()
	{
		this.buttonList.clear();
		super.initGui();
	}
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		int tier = mob.getTier() == EnumTier.TIER6 ? 6 : mob.getTier() == EnumTier.TIER5 ? 5 : mob.getTier() == EnumTier.TIER4 ? 4 : mob.getTier() == EnumTier.TIER3 ? 3 : mob.getTier() == EnumTier.TIER2 ? 2 : 1;
		this.fontRenderer.drawString(this.mob.getName() + " (Tier " + tier + ")", 0, -10, 16777215);
		this.fontRenderer.drawString("EXP: " + ((int)this.mob.getEXP() + "/" + (int)(this.mob.getNextLevelRequirement())), 70, 100, 4210752);
		this.fontRenderer.drawString("Total EXP: " + (this.mob.getTotalEXP() >= Integer.MAX_VALUE ? "N/A" : (int)this.mob.getTotalEXP()), 70, 110, 4210752);
		this.fontRenderer.drawString("Level " + (this.mob.getLevel() + 1), 80, 82, 4210752);
		this.fontRenderer.drawString("STR (" + TextFormatting.RED + (int)(mob.getStrength()) + TextFormatting.RESET + ")", 8, 90, 4210752);
		this.fontRenderer.drawString("STA (" + TextFormatting.DARK_GREEN + (int)(mob.getStamina()) + TextFormatting.RESET + ")", 8, 100, 4210752);
		this.fontRenderer.drawString("INT (" + TextFormatting.BLUE + (int)(mob.getIntelligence()) + TextFormatting.RESET + ")", 8, 110, 4210752);
		this.fontRenderer.drawString("DEX (" + TextFormatting.LIGHT_PURPLE + (int)(mob.getDexterity()) + TextFormatting.RESET + ")", 8, 120, 4210752);
		this.fontRenderer.drawString("AGI (" + TextFormatting.DARK_AQUA + (int)(mob.getAgility()) + TextFormatting.RESET + ")", 8, 130, 4210752);
		TextFormatting render;
		String renderS;
		if (!this.mob.isEntityAlive())
		{
			render = TextFormatting.RED;
			renderS = "Dead";
		}
		else if (this.mob.getHealth() <= this.mob.getMaxHealth() / 10)
		{
			render = TextFormatting.RED;
			renderS = "Near Death";
		}
		else if (this.mob.isBurning())
		{
			render = TextFormatting.RED;
			renderS = "On Fire";
		}
		else if (this.mob.getHealth() <= this.mob.getMaxHealth() / 5)
		{
			render = TextFormatting.RED;
			renderS = "Critical";
		}
		else if (this.mob.getHealth() <= this.mob.getMaxHealth() / 3)
		{
			render = TextFormatting.YELLOW;
			renderS = "Injured";
		}
		else if (this.mob.getHealth() <= this.mob.getMaxHealth() / 2)
		{
			render = TextFormatting.YELLOW;
			renderS = "Damaged";
		}
		else
		{
			render = TextFormatting.DARK_GREEN;
			renderS = this.mob.getFittness() > 1.1F ? "Super Healthy" : this.mob.getFittness() < 0.9F ? "Slightly Sickly" : "Healthy";
		}
		this.fontRenderer.drawString("Status: " + render + renderS, 8, 150, 4210752);GlStateManager.pushMatrix();
		GlStateManager.scale(0.75F, 0.75F, 1F);
		this.fontRenderer.drawString((mob.isWild() ? TextFormatting.LIGHT_PURPLE + "Mother Nature" : TextFormatting.GOLD + "Clan Leader " + mob.getOwner().getName()), 0, -25, 16777215);
		this.fontRenderer.drawString("HP " + (int)this.mob.getHealth() + "/" + (int)this.mob.getMaxHealth(), 110, 11, 0);
		this.fontRenderer.drawString("EN " + (int)this.mob.getEnergy() + "/" + 100, 110, 33, 0);
		this.fontRenderer.drawString("Armor " + mob.getTotalArmorValue() + "/" +(int)(MathHelper.floor(mob.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue())), 110, 56, 0);
		GlStateManager.scale(1F, 1F, 1F);
		GlStateManager.popMatrix();
	}

	/**
	* Draws the screen and all the components in it.
	*/
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.oldMouseX = (float)mouseX;
		this.oldMouseY = (float)mouseY;
		this.drawDefaultBackground();
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)i, (float)j, 0.0F);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		for (int i1 = 0; i1 < this.buttonList.size(); ++i1)
		{
			((GuiButton)this.buttonList.get(i1)).drawButton(this.mc, mouseX, mouseY, partialTicks);
		}

		for (int j1 = 0; j1 < this.labelList.size(); ++j1)
		{
			((GuiLabel)this.labelList.get(j1)).drawLabel(this.mc, mouseX, mouseY);
		}
		this.drawGuiContainerForegroundLayer(mouseX, mouseY);
		for (int i1 = 0; i1 < mob.basicInventory.getSizeInventory(); ++i1)
		{
			Slot slot = new Slot(mob.basicInventory, i1, 8, 8);
			if (i1 == 1)
			slot = new Slot(mob.basicInventory, i1, 8, 26);
			if (i1 == 2)
			slot = new Slot(mob.basicInventory, i1, 8, 44);
			if (i1 == 3)
			slot = new Slot(mob.basicInventory, i1, 8, 62);
			if (i1 == 4)
			slot = new Slot(mob.basicInventory, i1, 77, 62);
			if (i1 == 5)
			slot = new Slot(mob.basicInventory, i1, 95, 62);
			if (i1 == 6)
			slot = new Slot(mob.basicInventory, i1, 131, 62);
			if (i1 == 7 && !mob.basicInventory.getStackInSlot(7).isEmpty())
			slot = new Slot(mob.basicInventory, i1, 113, 62);
			if (slot.isEnabled())
			{
				this.drawSlot(slot, i1);
				if (this.isMouseOverSlot(slot, mouseX, mouseY))
				{
					this.theSlot = slot;
					GlStateManager.disableLighting();
					GlStateManager.disableDepth();
					int j1 = slot.xPos;
					int k1 = slot.yPos;
					GlStateManager.colorMask(true, true, true, false);
					this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
					GlStateManager.colorMask(true, true, true, true);
					GlStateManager.enableLighting();
					GlStateManager.enableDepth();
				}
			}
		}
		GlStateManager.popMatrix();
		if (this.theSlot != null && this.theSlot.getHasStack() && this.isMouseOverSlot(theSlot, mouseX, mouseY))
		{
			ItemStack itemstack1 = this.theSlot.getStack();
			this.renderToolTip(itemstack1, mouseX, mouseY);
		}
	}
	private void drawSlot(Slot slotIn, int index)
	{
		int i = slotIn.xPos;
		int j = slotIn.yPos;
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;
		boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
		ItemStack itemstack1 = this.mob.basicInventory.getStackInSlot(index);
		String s = null;
		
		if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty())
		{
			itemstack = itemstack.copy();
			itemstack.setCount(itemstack.getCount() / 2);
		}
		else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty())
		{
			if (this.dragSplittingSlots.size() == 1)
			{
				return;
			}

			if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn))
			{
				itemstack = itemstack1.copy();
				flag = true;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
				int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));
				
				if (itemstack.getCount() > k)
				{
					s = TextFormatting.YELLOW.toString() + k;
					itemstack.setCount(k);
				}
			}
			else
			{
				this.dragSplittingSlots.remove(slotIn);
				this.updateDragSplitting();
			}
		}

		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;
		
		if (itemstack.isEmpty() && slotIn.isEnabled())
		{
			TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();
			
			if (textureatlassprite != null)
			{
				GlStateManager.disableLighting();
				this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
				this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
				GlStateManager.enableLighting();
				flag1 = true;
			}
		}

		if (!flag1)
		{
			if (flag)
			{
				drawRect(i, j, i + 16, j + 16, -2130706433);
			}

			GlStateManager.enableDepth();
			this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
			this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
		}

		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}
	private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY)
	{
		return this.isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
	}
	private void updateDragSplitting()
	{
		ItemStack itemstack = this.mc.player.inventory.getItemStack();
		
		if (!itemstack.isEmpty() && this.dragSplitting)
		{
			if (this.dragSplittingLimit == 2)
			{
				itemstack.getMaxStackSize();
			}
			else
			{
				itemstack.getCount();
				
				for (Slot slot : this.dragSplittingSlots)
				{
					ItemStack itemstack1 = itemstack.copy();
					ItemStack itemstack2 = slot.getStack();
					int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
					Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
					int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));
					
					if (itemstack1.getCount() > j)
					{
						itemstack1.setCount(j);
					}
				}
			}
		}
	}

	/**
	* Draws the background layer of this container (behind the items).
	*/
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		if (mob.basicInventory.getStackInSlot(5).isEmpty())
		this.drawTexturedModalRect(i + 95, j + 62, 80, 201, 16, 16);
		if (mob.basicInventory.getStackInSlot(7).isEmpty())
		this.drawTexturedModalRect(i + 113, j + 62, 96, 201, 16, 16);
		if (mob.basicInventory.getStackInSlot(6).isEmpty())
		this.drawTexturedModalRect(i + 131, j + 62, 112, 201, 16, 16);
		if (mob.getLevel() > 299)
		this.drawTypeBox(1, 0, 16, true);
		if (mob.isBoss())
		this.drawTypeBox(1, 1, 19, true);
		if (mob.isHero())
		this.drawTypeBox(1, 2, 17, true);
		if (mob.hasLastChance())
		this.drawTypeBox(1, 3, 14, true);
		if (mob instanceof Animal)
		this.drawTypeBox(0, 0, 7, false);
		if (mob instanceof Armored)
		this.drawTypeBox(0, 1, 0, false);
		if (mob instanceof Elemental)
		this.drawTypeBox(0, 2, 3, false);
		if (mob instanceof Ender)
		this.drawTypeBox(0, 3, 4, false);
		if (mob instanceof Massive)
		this.drawTypeBox(0, 4, 28, false);
		if (mob instanceof Structure)
		this.drawTypeBox(0, 5, 18, false);
		if (mob instanceof Tiny)
		this.drawTypeBox(0, 6, 6, false);
		if (mob instanceof Undead)
		this.drawTypeBox(0, 7, 1, false);
		
		//this.drawTexturedModalRect(i + 152, j + 60, 0, 211, 20, 20);
		int health = (int)(mob.getHealthPercent() * 94F);
		if (health > 0)
		this.drawTexturedModalRect(i + 77, j + 15, 0, 166, health, 5);
		int energy = (int)(mob.getEnergyPercent() * 94F);
		if (energy > 0)
		this.drawTexturedModalRect(i + 77, j + 31, 0, 171, energy, 5);
		int exp = (int)(mob.getEXPPercent() * 101F);
		if (exp > 0)
		this.drawTexturedModalRect(i + 70, j + 92, 0, 176, exp, 5);
		int armor = (int)(mob.getTotalArmorValue() * 2F);
		if (armor > 0)
		this.drawTexturedModalRect(i + 77, j + 48, 95, 166, armor + 1, 5);
		int armort = (int)(MathHelper.floor(mob.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()) * 2F);
		if (armort > 0)
		this.drawTexturedModalRect(i + 77, j + 48, 95, 171, armort + 1, 5);
		drawEntityOnScreen(i + 50, j + 72, 32, (float)(i + 51) - this.oldMouseX, (float)(j + 75 - 50) - this.oldMouseY, this.mob);
	}

	public void drawTypeBox(int slotX, int slotY, int type, boolean spikeyborder)
	{
		int posX = -20 * (slotX + 1);
		float posY = 20f * ((float)slotY);
		int typeX;
		int typeY;
		int typeZ = 0;
		int typeZ2 = 0;
		int typePP = type;
		for (typePP = type; typePP > 7; typePP -= 8)
		{
			typeZ += 10;
			typeZ2 += 8;
		}
		if (type == 0)
		{
			typeX = 0 - typeZ;
			typeY = 181 + typeZ;
		}
		else if (type == 1)
		{
			typeX = 10 - typeZ;
			typeY = 181 + typeZ;
		}
		else
		{
			typeX = (10 * type) - (10 * typeZ2);
			typeY = 181 + typeZ;
		}
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawTexturedModalRect(i + posX, j + posY, spikeyborder ? 100 : 80, 181, 20, 20);
		this.drawTexturedModalRect(i + posX + 5, j + posY + 5, typeX, typeY, 10, 10);
	}
	/**
	* Draws an entity on the screen looking toward the cursor.
	*/
	public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
	{
		GlStateManager.enableColorMaterial();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)posX, (float)posY, 50.0F);
		GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		float f = ent.renderYawOffset;
		float f1 = ent.rotationYaw;
		float f2 = ent.rotationPitch;
		float f3 = ent.prevRotationYawHead;
		float f4 = ent.rotationYawHead;
		GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
		ent.renderYawOffset = 0F;
		ent.rotationYaw = 0F;
		ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
		ent.rotationYawHead = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
		ent.prevRotationYawHead = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
		ent.rotationPitch = 0F;
		ent.rotationYawHead = 0F;
		ent.prevRotationYawHead = 0F;
		ent.setAlwaysRenderNameTag(false);
		ent.setInvisible(false);
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		rendermanager.setPlayerViewY(180.0F);
		rendermanager.setRenderShadow(false);
		float fi = 1F;
		float fi1 = Math.max(ent.width, ent.height + 0.25F);
		if (ent.width >= ent.height || ent instanceof Animal)
		fi = 1F;
		else
		fi = 2F;
		if ((double)fi1 > 1.0D) fi /= fi1;
		GlStateManager.scale(fi, fi, fi);
		if (ent.width >= ent.height || ent instanceof Animal)
		fi = 0.5F;
		else
		fi = 0F;
		GlStateManager.translate(0.0F, fi, 0.0F);
		if (ent instanceof EntityWitherStorm)
		{
			GlStateManager.translate(0F, 18F, 0F);
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
		}
		if (ent instanceof EntityEnderDragon)
		{
			GlStateManager.scale(2F, 2F, 2F);
			GlStateManager.translate(0F, 0.5F, 0F);
			ent.prevRotationPitch = ent.rotationPitch = ent.prevRenderYawOffset = ent.prevRotationYaw = ent.prevRotationYawHead = ent.renderYawOffset = ent.rotationYaw = ent.rotationYawHead = 0F;
		}
		rendermanager.renderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
		if (ent.isBeingRidden())
		for (Entity entity : ent.getPassengers())
		{
			if (entity instanceof EntityLivingBase)
			{
				EntityLivingBase rider = (EntityLivingBase)entity;
				rider.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
				rider.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
				rider.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
				rider.rotationYawHead = ent.rotationYaw;
				rider.prevRotationYawHead = ent.rotationYaw;
				rider.setAlwaysRenderNameTag(false);
				rider.setInvisible(false);
				rendermanager.renderEntity(rider, 0.0D, rider.posY - ent.posY, 0.0D, 0.0F, 1.0F, false);
			}
		}
		if (ent.isRiding() && ent.getRidingEntity() instanceof EntityLivingBase)
		{
			EntityLivingBase rider = (EntityLivingBase)ent.getRidingEntity();
			
			rider.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
			rider.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
			rider.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
			rider.rotationYawHead = ent.rotationYaw;
			rider.prevRotationYawHead = ent.rotationYaw;
			rider.setAlwaysRenderNameTag(false);
			rider.setInvisible(false);
			rendermanager.renderEntity(rider, 0.0D, rider.posY - ent.posY, 0.0D, 0.0F, 1.0F, false);
		}
		rendermanager.setRenderShadow(true);
		ent.renderYawOffset = f;
		ent.rotationYaw = f1;
		ent.rotationPitch = f2;
		ent.prevRotationYawHead = f3;
		ent.rotationYawHead = f4;
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.disableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}