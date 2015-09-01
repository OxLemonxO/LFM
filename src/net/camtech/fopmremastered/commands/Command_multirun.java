package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Command_multirun extends FOPMR_Command
{

    public Command_multirun()
    {
        super("multirun", "/multirun [amount] [command]", "Run a command an allotted number of times.", Rank.SUPER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }
        if (Integer.parseInt(args[0]) == 1 || Integer.parseInt(args[0]) == 0)
        {
            sender.sendMessage(ChatColor.RED + String.format("Why are you trying to run the command %s times?", Integer.parseInt(args[0])));
            return true;
        }
        String baseCommand = StringUtils.join(args, " ", 1, args.length);
        sender.sendMessage(ChatColor.BLUE + String.format("Running: %s %s times", baseCommand, Integer.parseInt(args[0])));
        int i = 0;
        do
        {
            Bukkit.dispatchCommand(sender, baseCommand);
            i++;
        }
        while (i < Integer.parseInt(args[0]));
        return true;
    }

}
