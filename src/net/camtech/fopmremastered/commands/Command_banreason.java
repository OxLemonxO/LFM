package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "banreason", usage = "/banreason [player]", description = "Check the reason a player was banned.", rank = Rank.ADMIN)
public class Command_banreason
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        sender.sendMessage(ChatColor.RED + args[0] + " was banned for: " + FOPMR_Bans.getReason(args[0]));
        return true;
    }
}
