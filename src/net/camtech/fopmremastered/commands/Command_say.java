package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Command_say extends FOPMR_Command
{

    public Command_say()
    {
        super("say", "/say [message]", "Broadcast a message to the server.", "You don't have permission to use this command.", Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }
        String message = StringUtils.join(args, " ");
        Bukkit.broadcastMessage(ChatColor.GREEN + "[" + FOPMR_Rank.getRank(sender).name + " : " + sender.getName() + "] " + message);
        return true;
    }

}
