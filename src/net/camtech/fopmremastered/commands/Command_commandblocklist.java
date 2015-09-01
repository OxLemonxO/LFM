package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name="commandblocklist", description="View all currently blocked commands.", usage="/commandblocklist", rank=Rank.SYSTEM)
public class Command_commandblocklist
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        sender.sendMessage(ChatColor.RED + "The following commands are blocked:");
        for(String command : FreedomOpModRemastered.configs.getCommands().getConfig().getKeys(false))
        {
            sender.sendMessage(ChatColor.RED + " - " + command + " (Locked to clearance level " + FreedomOpModRemastered.configs.getCommands().getConfig().getInt(command + ".rank") + ")");
        }
        return true;
    }
}
