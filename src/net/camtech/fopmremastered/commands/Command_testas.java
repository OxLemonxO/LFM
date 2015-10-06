package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="testas", usage="/testas <rank> <command to test>", description="Test a command as a different rank.")
public class Command_testas
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length < 2)
        {
            return false;
        }
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Only in-game players can use this command.");
            return true;
        }
        int level;
        try
        {
            level = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException ex)
        {
            sender.sendMessage(ChatColor.RED + "The rank must be an integer lower than your current rank.");
            return false;
        }
        Rank trueRank = FOPMR_Rank.getRank(sender);
        if(level >= trueRank.level)
        {
            sender.sendMessage(ChatColor.RED + "The rank must be an integer lower than your current rank.");
            return true;
        }
        String command = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        FOPMR_Rank.setRank((Player) sender, FOPMR_Rank.getFromLevel(level), null);
        ((Player) sender).chat("/" + command);
        FOPMR_Rank.setRank((Player) sender, trueRank, null);
        return true;
    }
}