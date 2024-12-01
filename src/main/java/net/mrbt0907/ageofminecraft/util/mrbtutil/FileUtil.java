package net.mrbt0907.ageofminecraft.util.mrbtutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.annotation.Nonnull;

import org.apache.commons.io.FileUtils;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.mrbt0907.ageofminecraft.EngenderMod;

public class FileUtil
{
	private static String lastWorldSave;
	
	public static String getWorkingDirectory()
	{
		if (FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer())
    		return FMLClientHandler.instance().getClient().mcDataDir.getPath() + File.separator;
    	else
    		return FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory().getPath() + File.separator;
	}
	
	public static String getWorldFolderPath()
	{
    	if (FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer())
    		return getWorkingDirectory() + "saves" + File.separator;
    	else
    		return new File(".").getAbsolutePath() + File.separator;
    }
	
	public static String getWorldFolderName()
	{
		World world = DimensionManager.getWorld(0);
		
		if (world != null)
		{
			lastWorldSave = ((WorldServer)world).getChunkSaveLocation().getName();
			return lastWorldSave + File.separator;
		}
		
		return lastWorldSave + File.separator;
	}
	
	public static void saveCompactNBT(@Nonnull String saveLocation, @Nonnull String fileName, @Nonnull NBTTagCompound nbt)
	{
		EngenderMod.debug("Saving nbt data to file " + fileName + "...");
		try
		{
			if (!(new File(saveLocation).exists())) new File(saveLocation).mkdirs();
			FileOutputStream fos = new FileOutputStream(saveLocation + File.separator + fileName + ".dat");
			CompressedStreamTools.writeCompressed(nbt, fos);
			fos.close();
			EngenderMod.debug("Successfully saved file " + fileName);
		}
		catch (Exception ex)
		{
			EngenderMod.warn("Failed to save file " + fileName);
			EngenderMod.error(ex);
			ex.printStackTrace();
		}
	}
	
	public static NBTTagCompound loadCompactNBT(@Nonnull String saveLocation, @Nonnull String fileName, boolean backupFile)
	{
		EngenderMod.debug("Loading nbt data from file " + fileName + "...");
		NBTTagCompound nbt = new NBTTagCompound();
		String path = saveLocation + File.separator + fileName + ".dat";
		
		try
		{
			File file = new File(path);
			if (file.exists())
			{
				nbt = CompressedStreamTools.readCompressed(new FileInputStream(path));
				if (backupFile)
					FileUtils.copyFile(file, new File(path + ".bak"));
			}
		}
		catch (Exception ex)
		{
			EngenderMod.warn("Failed to load file " + fileName + ". " + (backupFile ? "Loading backup..." : "Returning empty nbt data..."));
			EngenderMod.error(ex);
			
			if (backupFile)
			{
				try
				{
					if (new File(path + ".bak").exists())
						nbt = CompressedStreamTools.readCompressed(new FileInputStream(path + ".bak"));
					else
						EngenderMod.warn("Failed to load backup file as the file does not exist. Returning empty nbt data...");
				}
				catch (Exception e)
				{
					EngenderMod.warn("Failed to load backup file as an error occured when loading the backup. Returning empty nbt data...");
					EngenderMod.error(ex);
				}
			}
		}
		
		return nbt;
	}
}
