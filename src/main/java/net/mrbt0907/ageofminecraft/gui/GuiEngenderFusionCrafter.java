package net.mrbt0907.ageofminecraft.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.mrbt0907.ageofminecraft.blocks.ContainerMobSpawner;
import net.mrbt0907.ageofminecraft.blocks.TileFusionCrafter;

@SideOnly(Side.CLIENT)

public class GuiEngenderFusionCrafter extends GuiContainer
{
	private static final ResourceLocation TEXTURES = new ResourceLocation("ageofminecraft", "textures/fusion_crafter.png");
	private final InventoryPlayer playerInventory;
	private final TileFusionCrafter fusionCrafter;
	
	public GuiEngenderFusionCrafter(InventoryPlayer playerInv, IInventory tile)
	{
		super(new ContainerMobSpawner(playerInv, tile));
		playerInventory = playerInv;
		
		if (tile instanceof TileFusionCrafter)
			fusionCrafter = (TileFusionCrafter) tile;
		else
			fusionCrafter = null;
	}

	/**
	* Draws the screen and all the components in it.
	*/
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if (fusionCrafter == null)
			return;
		String s = fusionCrafter.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 3, 4210752);
		fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96 + 2, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		if (fusionCrafter == null)
			return;
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(TEXTURES);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		drawTexturedModalRect(i, j, 0, 0, xSize, ySize);
		
		int fuseTime = fusionCrafter.getFuseTime();
		int maxMana = fusionCrafter.getMaxMana(), maxEntropy = fusionCrafter.getMaxEntropy();
		if(fuseTime > -1)
		{
			float progress = (1.0F - (float) fusionCrafter.getCurFuseTime() / (float) fusionCrafter.getFuseTime()) * 24.0F;
			if (progress > 0.0F)
				drawTexturedModalRect(i + 85, j + 34, 177, 0, 4, (int) progress);
		}
		float i2 = (float) fusionCrafter.mana / (float) maxMana;
		int mana = (int)(i2 * 80F);
		if (mana > 0)
			drawTexturedModalRect(i + 47, j + 10, 0, 166, mana, 5);
		float i3 = (float) fusionCrafter.entropy / (float) maxEntropy;
		int entropy = (int)(i3 * 28F);
		if (entropy > 0)
			drawTexturedModalRect(i + 47, j + 21, 0, 171, entropy, 5);
		
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		i = (width - xSize);
		j = (height - ySize);
		drawHoveringText(TextFormatting.AQUA + "Mana: " + fusionCrafter.mana + "/" +  maxMana, i + 156, j + 72);
		drawHoveringText(TextFormatting.DARK_RED + "Entropy: " + fusionCrafter.entropy + "/" + maxEntropy, i + 156, j + 92);
		if (fuseTime > -1)
			drawHoveringText(TextFormatting.AQUA + "Crafting... " + (fusionCrafter.getCurFuseTime() / 20) + " Seconds Left", i + 156, j + 112);
		else
			drawHoveringText(TextFormatting.AQUA + "Waiting for fusion...", i + 156, j + 112);
		GlStateManager.scale(1, 1, 1);
		GlStateManager.popMatrix();
	}
}