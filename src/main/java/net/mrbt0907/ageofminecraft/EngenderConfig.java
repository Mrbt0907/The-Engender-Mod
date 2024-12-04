package net.mrbt0907.ageofminecraft;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = "ageofminecraft")
public class EngenderConfig
{
	@Name("Enable Debug Mode")
	@Comment({"Enable debug mode", "Warning: Will break balance!"})
	public static boolean debugMode = false;
}