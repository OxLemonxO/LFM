package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import java.util.List;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "admincard", usage = "/admincard [command]", description = "Run a command once for every admin on the server (? gets replaced with their name).", rank = Rank.SENIOR)
public class Command_admincard
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        List<String> blocked = Arrays.asList("doom", "ban", "wildcard", "smite", "forcechat", "fchat", "fc");

        String baseCommand = StringUtils.join(args, " ");

        for (String block : blocked)
        {
            if (baseCommand.toLowerCase().contains(block) && !FOPMR_Rank.isSpecialist(sender))
            {
                sender.sendMessage(ChatColor.RED + String.format("You cannot use %s in a WildCard!", block));
                return true;
            }
        }

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (FOPMR_Rank.isAdmin(player))
            {
                String out_command = baseCommand.replaceAll("\\x3f", player.getName());
                sender.sendMessage("Running Command: " + out_command);
                Bukkit.dispatchCommand(sender, out_command);
            }
        }

        return true;
    }
}
