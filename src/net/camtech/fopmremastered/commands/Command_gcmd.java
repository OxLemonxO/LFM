package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="gcmd", description="Run a command as another player", usage="/gcmd [player] [command]", rank=Rank.ADMIN, aliases="sudo")
public class Command_gcmd
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }

        final Player player = FOPMR_Rank.getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage("The player " + args[0] + " is not online.");
            return true;
        }

        if (FOPMR_Rank.getRank(sender).level <= FOPMR_Rank.getRank(player).level)
        {
            sender.sendMessage(ChatColor.RED + "You cannot gcmd someone of an equal or higher rank than yourself.");
            return true;
        }

        final String outCommand = StringUtils.join(args, " ", 1, args.length);
        try
        {
            sender.sendMessage("Sending command as " + player.getName() + ": " + outCommand);
            player.chat("/" + outCommand);
        } catch (Throwable ex)
        {
            sender.sendMessage("Error sending command: " + ex.getMessage());
        }

        return true;
    }

}
