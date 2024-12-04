package net.mrbt0907.ageofminecraft.items.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.mrbt0907.ageofminecraft.EngenderMod;
import net.mrbt0907.ageofminecraft.items.ItemCommandingStaff;

public class CapabilityManager
{
	public static final ResourceLocation COMMAND_STAFF = new ResourceLocation(EngenderMod.MODID, "command_staff");
	
	public static void preInit()
	{
		net.minecraftforge.common.capabilities.CapabilityManager.INSTANCE.register(CapabilityCommandStaff.class, new CapabilityCommandStaff.Storage(), CapabilityCommandStaff::new);
	}
	
	@SubscribeEvent
	public static void attach(AttachCapabilitiesEvent<ItemStack> event)
	{
		if (event.getObject().getItem() instanceof ItemCommandingStaff)
			event.addCapability(COMMAND_STAFF, new CapabilityCommandStaff.Provider());
	}
}
