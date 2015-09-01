package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name="level", description="See what clearance level you have.", usage="/level")
public class Command_level
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        sender.sendMessage(ChatColor.GREEN + "You have level " + ChatColor.BLUE + Integer.toString(FOPMR_Rank.getRank(sender).level) + ChatColor.GREEN + " clearance as " + CUtils_Methods.aOrAn(FOPMR_Rank.getRank(sender).name) + " " + FOPMR_Rank.getRank(sender).name + ".");
        return true;
    }

}
