package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name="commandunblock", usage="/commandunblock [command]", description="Unblock a command.", rank=Rank.SYSTEM)
public class Command_commandunblock
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        if(!FreedomOpModRemastered.configs.getCommands().getConfig().contains(args[0].toLowerCase()))
        {
            return false;
        }
        FreedomOpModRemastered.configs.getCommands().getConfig().set(args[0].toLowerCase(), null);
        FreedomOpModRemastered.configs.getCommands().saveConfig();
        sender.sendMessage(ChatColor.RED + "Unblocked command: " + args[0]);
        return true;
    }
}
