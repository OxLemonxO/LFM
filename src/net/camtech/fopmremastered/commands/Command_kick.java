package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="kick", description="Kick a player.", usage="/kick [player] <reason>", rank=Rank.ADMIN)
public class Command_kick extends FOPMR_Command
{

    public Command_kick()
    {
        super("kick", "/kick [player] <reason>", "Kick a player.", Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        if (args.length == 1)
        {
            Player player = FOPMR_Rank.getPlayer(args[0]);
            if (player == null)
            {
                sender.sendMessage("The player " + args[0] + " is not online.");
                return true;
            }
            if (FOPMR_Rank.getRank(sender).level > FOPMR_Rank.getRank(player).level)
            {
                player.kickPlayer("You have been kicked by " + sender.getName());
                return true;
            }
            sender.sendMessage(ChatColor.RED + "You cannot kick someone of an equal or higher rank than yourself.");
            return true;
        }
        else
        {
            String message = CUtils_Methods.colour(StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " "));
            Player player = FOPMR_Rank.getPlayer(args[0]);
            if (player == null)
            {
                sender.sendMessage("The player " + args[0] + " is not online.");
                return true;
            }
            if (FOPMR_Rank.getRank(sender).level > FOPMR_Rank.getRank(player).level)
            {
                player.kickPlayer(message + " - " + sender.getName());
                return true;
            }
            sender.sendMessage(ChatColor.RED + "You cannot kick someone of an equal or higher rank than yourself.");
            return true;
        }
    }

}
