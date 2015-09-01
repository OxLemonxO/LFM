package net.camtech.fopmremastered.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "a", description = "Always returns unknown command message.", usage = "Unknown command. Type \"/help\" for help.")
public class Command_a
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        return false;
    }
}
