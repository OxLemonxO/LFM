package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "overlord", usage = "/overlord", description = "Toggle Camzie99's Overlord Mode.", rank = FOPMR_Rank.Rank.OVERLORD)
public class Command_overlord
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        FOPMR_Commons.camOverlordMode = !FOPMR_Commons.camOverlordMode;
        sender.sendMessage(ChatColor.AQUA + "Overlord mode turned " + (FOPMR_Commons.camOverlordMode ? (ChatColor.GREEN + "on") : (ChatColor.RED + "off")) + ChatColor.AQUA  + ".");
        return true;
    }
}
