package net.camtech.fopmremastered.commands;

import static java.lang.Double.parseDouble;
import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CommandParameters(name = "launch", description = "Launch a player.", usage = "/launch <player> [strength]")
public class Command_launch
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Player player;
        Double strength;
        if (args.length != 1)
        {
            if (args.length != 2)
            {
                return false;
            }
            player = FOPMR_Rank.getPlayer(args[0]);
            if(!FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(sender), FOPMR_Rank.getRank(player)))
            {
                sender.sendMessage(ChatColor.RED + "You can only launch people of a lower rank than yourself.");
                return true;
            }
            strength = parseDouble(args[1]);
        }
        else
        {
            if(!(sender instanceof Player))
            {
                return false;
            }
            player = (Player) sender;
            strength = parseDouble(args[0]);
        }
        if (strength > 10)
        {
            sender.sendMessage(ChatColor.RED + "Please use a strength value that is less than or equal to 10.");
            return true;
        }
        player.setVelocity(new Vector(0, strength, 0));
        return true;
    }
}
