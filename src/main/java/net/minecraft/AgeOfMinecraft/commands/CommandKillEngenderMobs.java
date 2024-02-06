package net.minecraft.AgeOfMinecraft.commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.AgeOfMinecraft.entity.EntityFriendlyCreature;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandKillEngenderMobs extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getName()
    {
        return "destroyteam";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getUsage(ICommandSender sender)
    {
        return "commands.destroyteam.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
		if(args.length != 0)
		{
	    	EntityPlayerMP pkill = getCommandSenderAsPlayer(sender);
			if (pkill != null)
			{
				if (args.length == 1 && args[0].equals("wild"))
				{
			    	for (Entity entity : pkill.world.loadedEntityList)
			    	{
			    		if (entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature)entity).isWild())
			    		{
				    		entity.setDead();
				    		notifyCommandListener(sender, this, "commands.destroyteam.successful", new Object[] {entity.getDisplayName()});
			    		}
			    	}
				}
				else if (args.length == 1 && args[0].equals("all"))
				{
			    	for (Entity entity : pkill.world.loadedEntityList)
			    	{
			    		if (entity instanceof EntityFriendlyCreature)
			    		{
				    		entity.setDead();
				    		notifyCommandListener(sender, this, "commands.destroyteam.successful", new Object[] {entity.getDisplayName()});
			    		}
			    	}
				}
				else if (args.length == 1 && args[0].equals("not"))
				{
			    	for (Entity entity : pkill.world.loadedEntityList)
			    	{
			    		if (entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature) entity).getOwnerId() != pkill.getUniqueID())
			    		{
				    		entity.setDead();
				    		notifyCommandListener(sender, this, "commands.destroyteam.successful", new Object[] {entity.getDisplayName()});
			    		}
			    	}
				}
				else if (args.length == 1 && args[0].equals("only"))
				{
			    	for (Entity entity : pkill.world.loadedEntityList)
			    	{
			    		if (entity instanceof EntityFriendlyCreature && ((EntityFriendlyCreature) entity).getOwnerId() == pkill.getUniqueID())
			    		{
				    		entity.setDead();
				    		notifyCommandListener(sender, this, "commands.destroyteam.successful", new Object[] {entity.getDisplayName()});
			    		}
			    	}
				}
			}
		}
		else
			throw new WrongUsageException("command.destroyteam.fail");
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }


    /**
     * Get a list of options for when the user presses the TAB key
     */
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
		if (args.length > 0 && args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, new String[] {"wild", "all", "not", "only"});
		}
		else return Collections.emptyList();
    }
}