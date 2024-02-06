package net.minecraft.AgeOfMinecraft.items;

import java.util.List;

import net.minecraft.AgeOfMinecraft.registry.EItem;
import net.minecraft.AgeOfMinecraft.registry.ESetup;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.registry.ETab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSimpleFoiled;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;


public class ItemLearningBook extends ItemSimpleFoiled
{
	public int tier;
	public boolean artifact;
	public float EXPERIENCE;
	public float STRENGTH;
	public float STAMINA;
	public float INTELEGENCE;
	public float DEXTERITY;
	public float AGILITY;
	public String tooltip;
	
	/**
	 * The Learning Book item [
	 * Tiers:
	 * 0 = Basic,* 1 = Modern,* 2 = Advanced,* 3 = Complex,* 4 = Master,* 5+ = Artifact ]
	 * @param Tier - Tier of the book
	 * @param Name - Name of the book
	 * @param Description - Description of the book
	 * @param Durability - Max Available Successes
	 * @param Experience - Max Experience gained per read
	 * @param Strength - Max Experience gained per read
	 * @param Stamina - Max Experience gained per read
	 * @param Intelegence - Max Experience gained per read
	 * @param Dexterity - Max Experience gained per read
	 * @param Agility - Max Experience gained per read
	 */
	public ItemLearningBook(int tier, String name, String description, int durability, int experience, float strength, float stamina, float intelegence, float dexterity, float agility)
	{
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(durability);
		this.setCreativeTab(ETab.engender);
		this.tier = tier;
		this.artifact = tier >= 6 ? true : false;
		this.EXPERIENCE = experience;
		this.STRENGTH = strength;
		this.STAMINA = stamina;
		this.INTELEGENCE = intelegence;
		this.DEXTERITY = dexterity;
		this.AGILITY = agility;
		this.tooltip = description;
		EItem.SKILL_BOOKS.add(this);
	}
	public EnumRarity getRarity(ItemStack stack)
	{
		return artifact ? ESetup.UBEREPIC : super.getRarity(stack);
	}
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		TextFormatting tierF;
		TextFormatting STR;
		TextFormatting STA;
		TextFormatting INT;
		TextFormatting DEX;
		TextFormatting AGL;
		String tierS;
		String STRS;
		String STAS;
		String INTS;
		String DEXS;
		String AGLS;
		
		if (this.tier == 0)
		{
			tierS = "Basic";
			tierF = TextFormatting.RESET;
		}

		else if (this.tier == 1)
		{
			tierS = "Modern";
			tierF = TextFormatting.BLUE;
		}

		else if (this.tier == 2)
		{
			tierS = "Advanced";
			tierF = TextFormatting.RED;
		}

		else if (this.tier == 3)
		{
			tierS = "Complex";
			tierF = TextFormatting.LIGHT_PURPLE;
		}

		else if (this.tier == 4)
		{
			tierS = "Expert";
			tierF = TextFormatting.DARK_PURPLE;
		}

		else if (this.tier == 5)
		{
			tierS = "Master";
			tierF = TextFormatting.YELLOW;
		}

		else
		{
			tierS = "ARTIFACT";
			tierF = TextFormatting.GOLD;
		}

		
		tooltip.add("(" + tierF + tierS + TextFormatting.GRAY + ")");
		tooltip.add(TextFormatting.ITALIC + this.tooltip);
		if (this.STRENGTH < 0.0f)
		{
			STR = TextFormatting.RED;
			STRS = "";
		}
		else if (this.STRENGTH > 0.0f)
		{
			STR = TextFormatting.GREEN;
			STRS = "+";
		}
		else
		{
			STR = TextFormatting.GOLD;
			STRS = "";
		}

		if (this.STAMINA < 0.0f)
		{
			STA = TextFormatting.RED;
			STAS = "";
		}
		else if (this.STAMINA > 0.0f)
		{
			STA = TextFormatting.GREEN;
			STAS = "+";
		}
		else
		{
			STA = TextFormatting.GOLD;
			STAS = "";
		}

		if (this.INTELEGENCE < 0.0f)
		{
			INT = TextFormatting.RED;
			INTS = "";
		}
		else if (this.INTELEGENCE > 0.0f)
		{
			INT = TextFormatting.GREEN;
			INTS = "+";
		}
		else
		{
			INT = TextFormatting.GOLD;
			INTS = "";
		}

		if (this.DEXTERITY < 0.0f)
		{
			DEX = TextFormatting.RED;
			DEXS = "";
		}
		else if (this.DEXTERITY > 0.0f)
		{
			DEX = TextFormatting.GREEN;
			DEXS = "+";
		}
		else
		{
			DEX = TextFormatting.GOLD;
			DEXS = "";
		}

		if (this.AGILITY < 0.0f)
		{
			AGL = TextFormatting.RED;
			AGLS = "";
		}
		else if (this.AGILITY > 0.0f)
		{
			AGL = TextFormatting.GREEN;
			AGLS = "+";
		}
		else
		{
			AGL = TextFormatting.GOLD;
			AGLS = "";
		}
		tooltip.add("");
		tooltip.add(STR + "STR: " + STRS + this.STRENGTH);
		tooltip.add(STA + "STA: " + STAS + this.STAMINA);
		tooltip.add(INT + "INT: " + INTS + this.INTELEGENCE);
		tooltip.add(DEX + "DEX: " + DEXS + this.DEXTERITY);
		tooltip.add(AGL + "AGL: " + AGLS + this.AGILITY);
	}
	/**
	* Returns the tier of the book
	* @return
	*/
	public int getTier()
	{
		return this.tier;
	}
	/**
	* Sets the tier of the book
	* @param tier
	*/
	public void setTier(int tier)
	{
		this.tier = tier;
	}
	/**
	* Returns true if this book is an artifact (Tier 5)
	* @return
	*/
	public boolean isArtifact()
	{
		return this.artifact;
	}
}