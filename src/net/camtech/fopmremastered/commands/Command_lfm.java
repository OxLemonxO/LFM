package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "lfm", usage = "/lfm <reload>", description = "Check info about the plugin or reload the configuration file.")
public class Command_lfm
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if(args.length > 0)
        {
            if(args[0].equalsIgnoreCase("reload"))
            {
                if(!FOPMR_Rank.isAdmin(sender))
                {
                    sender.sendMessage(ChatColor.RED + "Only admins can reload the LemonFreedomMod config.");
                    return true;
                }
                FreedomOpModRemastered.plugin.reloadConfig();
                FOPMR_DatabaseInterface.closeConnection(FOPMR_DatabaseInterface.getConnection());
            }
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "This is LemonFreedomMod");
        sender.sendMessage(CUtils_Methods.randomChatColour() + "Edited from Camzie99's FreedomOpMod: Remastered to better suite the LemonFreedom server.");
        return true;
    }

}