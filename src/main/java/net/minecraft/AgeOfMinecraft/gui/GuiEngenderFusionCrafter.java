package net.minecraft.AgeOfMinecraft.gui;

import net.minecraft.AgeOfMinecraft.blocks.ContainerMobSpawner;
import net.minecraft.AgeOfMinecraft.blocks.TileEntityMonsterSpawnerSPC;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class GuiEngenderFusionCrafter extends GuiContainer
{
	private static final ResourceLocation BREWING_STAND_GUI_TEXTURES = new ResourceLocation("ageofminecraft", "textures/fusion_crafter.png");
	/** The player inventory bound to this GUI. */
	private final InventoryPlayer playerInventory;
	private final IInventory tileBrewingStand;
	
	public GuiEngenderFusionCrafter(InventoryPlayer playerInv, IInventory p_i45506_2_)
	{
		super(new ContainerMobSpawner(playerInv, p_i45506_2_));
		this.playerInventory = playerInv;
		this.tileBrewingStand = p_i45506_2_;
	}

	/**
	* Draws the screen and all the components in it.
	*/
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	/**
	* Draw the foreground layer for the GuiContainer (everything in front of the items)
	*/
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = this.tileBrewingStand.getDisplayName().getUnformattedText();
		this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 3, 4210752);
		this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	* Draws the background layer of this container (behind the items).
	*/
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BREWING_STAND_GUI_TEXTURES);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
		
		float i1 = (float)(((TileEntityMonsterSpawnerSPC)this.tileBrewingStand).fuseTime / ((TileEntityMonsterSpawnerSPC)this.tileBrewingStand).totalSpawnMobTime);
		int progress = (int)(i1 * 24F);
		if (progress > 0)
		this.drawTexturedModalRect(i + 85, j + 34, 177, 0, 4, progress);
		float i2 = (float)(((TileEntityMonsterSpawnerSPC)this.tileBrewingStand).mana / 2000000F);
		int mana = (int)(i2 * 80F);
		if (mana > 0)
		this.drawTexturedModalRect(i + 47, j + 10, 0, 166, mana, 5);
		float i3 = (float)(((TileEntityMonsterSpawnerSPC)this.tileBrewingStand).entropy / 20000F);
		int entropy = (int)(i3 * 28F);
		if (entropy > 0)
		this.drawTexturedModalRect(i + 47, j + 21, 0, 171, entropy, 5);
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		i = (this.width - this.xSize);
		j = (this.height - this.ySize);
		this.drawHoveringText(TextFormatting.AQUA + "Mana: " + (int)((TileEntityMonsterSpawnerSPC)this.tileBrewingStand).mana + "/2000000", i + 324, j + 48);
		this.drawHoveringText(TextFormatting.DARK_RED + "Entropy: " + (int)((TileEntityMonsterSpawnerSPC)this.tileBrewingStand).entropy + "/20000", i + 156, j + 72);
		GlStateManager.scale(1, 1, 1);
		GlStateManager.popMatrix();
	}
}