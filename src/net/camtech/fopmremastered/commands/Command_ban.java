package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_ban extends FOPMR_Command
{

    public Command_ban()
    {
        super("ban", "/ban [player] <reason>", "Ban a player.", "You don't have permission to use this command.", Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }
        final Player player = FOPMR_Rank.getPlayer(args[0]);
        String reason = "No reason given... - " + sender.getName();
        if (args.length > 1)
        {
            reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ") + " — " + sender.getName();
        }
        if (player == null)
        {
            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Attempting ban of offline player: " + args[0]);
            FOPMR_Bans.addBan(args[0], reason, sender.getName());
        }
        else
        {
            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Attempting to ban player: " + player.getName());
            FOPMR_Bans.addBan(player, reason, sender.getName());
        }
        return true;
    }

}
