package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import java.util.List;
import static me.StevenLawson.BukkitTelnet.BukkitTelnet.server;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_wildcard extends FOPMR_Command
{
    public Command_wildcard()
    {
        super("wildcard", "/wildcard [command]", "Run a command once for every player on the server (? gets replaced with their name).", Rank.SUPER);
    }

    @Override
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

        for (Player player : server.getOnlinePlayers())
        {
            String out_command = baseCommand.replaceAll("\\x3f", player.getName());
            sender.sendMessage("Running Command: " + out_command);
            server.dispatchCommand(sender, out_command);
        }

        return true;
    }

}
