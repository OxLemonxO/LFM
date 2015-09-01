package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "op", usage = "/op [player]", description = "Op a player.", rank = FOPMR_Rank.Rank.OP)
public class Command_op
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        Player player = FOPMR_Rank.getPlayer(args[0]);
        if(player == null)
        {
            sender.sendMessage(ChatColor.RED + args[0] + " is offline.");
            return true;
        }
        player.setOp(true);
        FOPMR_Commons.adminAction(sender.getName(), "Opping " + player.getName(), false);
        return true;
    }
}
