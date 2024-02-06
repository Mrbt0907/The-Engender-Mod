package net.minecraft.AgeOfMinecraft.entity.other;

import java.util.UUID;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class AttributeModifierEX
{
	protected AttributeModifier defaultModifier;
	protected final String name;
	protected final UUID uuid;
	protected double defaultValue, value;
	
	/**Used to keep track of unique attributes and apply them with the same name and uuid*/
	public AttributeModifierEX(UUID uuid, String name)
	{
		this.name = name;
		this.uuid = uuid;
		defaultValue = 0.0D;
		defaultModifier = new AttributeModifier(uuid, name, defaultValue, 0);
	}
	
	/**Sets the default value for this modifier*/
	public AttributeModifierEX setDefaultValue(double value)
	{
		this.defaultValue = value;
		defaultModifier = getModifier();
		return this;
	}
	
	/**Returns the default value of this modifier*/
	public double getDefaultValue()
	{
		return defaultValue;
	}
	
	/**Returns the last applied value of this modifier*/
	public double getValue()
	{
		return value;
	}
	
	/**Returns an instance of the attribute modifier for the specified attribute*/
	public AttributeModifier getModifier()
	{
		return defaultModifier;
	}
	
	/**Applies this modifier to the given attribute<br>
	 * Operation Types<br>
	 * - 0: Add to the base value<br>
	 * - 1: Multiply directly to the base value<br>
	 * - 2: Multiply to the attribute value (In other words, multiply everything together)
	 * attribute: The attribute to apply to<br>
	 * operation: How the value should be applied<br>*/
	public AttributeModifierEX applyBase(final IAttributeInstance attribute)
	{
		return applyBase(attribute, defaultValue);
	}
	
	/**Applies the value to the given attribute's base value<br>
	 * attribute: The attribute to apply to<br>
	 * value: The numeric value to apply to the attribute*/
	public AttributeModifierEX applyBase(final IAttributeInstance attribute, double value)
	{
		attribute.setBaseValue(value);
		return this;
	}
	
	/**Applies this modifier to the given attribute<br>
	 * Operation Types<br>
	 * - 0: Add to the base value<br>
	 * - 1: Multiply directly to the base value<br>
	 * - 2: Multiply to the attribute value (In other words, multiply everything together)
	 * attribute: The attribute to apply to<br>
	 * operation: How the value should be applied<br>*/
	public AttributeModifierEX apply(final IAttributeInstance attribute, int operation)
	{
		return apply(attribute, defaultValue, operation);
	}
	
	/**Applies this modifier to the given attribute<br>
	 * Operation Types<br>
	 * - 0: Add to the base value<br>
	 * - 1: Multiply directly to the base value<br>
	 * - 2: Multiply to the attribute value (In other words, multiply everything together)
	 * attribute: The attribute to apply to<br>
	 * value: The numeric value to apply to the attribute<br>
	 * operation: How the value should be applied<br>*/
	public AttributeModifierEX apply(final IAttributeInstance attribute, double value, int operation)
	{
		this.value = value;
		if (!attribute.hasModifier(defaultModifier))
			attribute.applyModifier(new AttributeModifier(uuid, name, value, operation));
		return this;
	}
}
