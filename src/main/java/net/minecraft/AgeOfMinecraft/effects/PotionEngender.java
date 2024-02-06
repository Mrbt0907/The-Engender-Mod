package net.minecraft.AgeOfMinecraft.effects;

import net.minecraft.AgeOfMinecraft.EngenderMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionEngender extends Potion 
{
    protected final double bonusPerLevel;

	public PotionEngender(String name, boolean isBadPotion, int color, int IconIndexX, int IconIndexY, double bonusPerLevelIn) 
	{
		super(isBadPotion, color);
		setPotionName("effect." + name);
		setIconIndex(IconIndexX, IconIndexY);
		setRegistryName(new ResourceLocation(EngenderMod.MODID + ":" + name));
        this.bonusPerLevel = bonusPerLevelIn;
	}

    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier)
    {
        return this.bonusPerLevel * (double)(amplifier + 1);
    }

	public Potion setIconIndex(int par1, int par2) 
	{
		super.setIconIndex(par1, par2);
		return this;
	}
	
	public boolean hasStatusIcon() 
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(EngenderMod.MODID + ":textures/effects.png"));
		return true;
	}
}