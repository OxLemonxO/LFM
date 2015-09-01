package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_unban extends FOPMR_Command
{

    public Command_unban()
    {
        super("unban", "/unban [player]", "Unan a player.", "You don't have permission to use this command.", Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }
        Player player = FOPMR_Rank.getPlayer(args[0]);
        if (player == null)
        {
            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Attempting unban of offline player: " + args[0]);
            FOPMR_Bans.unBan(args[0]);
        }
        else
        {
            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Attempting to unban player: " + player.getName());
            FOPMR_Bans.unBan(player);
        }
        return true;
    }

}
