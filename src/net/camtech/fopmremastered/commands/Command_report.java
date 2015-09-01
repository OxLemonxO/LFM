package net.camtech.fopmremastered.commands;

import static net.camtech.fopmremastered.FOPMR_Rank.isAdmin;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "report", usage = "/report ([player] [reason]) | [view] | ([delete] [player])", description = "Manage reports.")
public class Command_report
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(args.length == 0)
        {
            return false;
        }
        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("view"))
            {
                if(!isAdmin(sender))
                {
                    sender.sendMessage(ChatColor.RED + "You must be an admin to view reports.");
                    return true;
                }
                for(String reported : FreedomOpModRemastered.configs.getReports().getConfig().getKeys(false))
                {
                    sender.sendMessage(ChatColor.RED + reported + ChatColor.GOLD + " was reported by " + ChatColor.GREEN + FreedomOpModRemastered.configs.getReports().getConfig().getString(reported + ".reporter") + " for " + ChatColor.AQUA + FreedomOpModRemastered.configs.getReports().getConfig().getString(reported + ".reason"));
                }
                return true;
            }
            return false;
        }
        if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("delete"))
            {
                if(!isAdmin(sender))
                {
                    sender.sendMessage(ChatColor.RED + "You must be an admin to delete reports.");
                    return true;
                }
                String name = args[1];
                if(Bukkit.getPlayer(name) != null)
                {
                    name = Bukkit.getPlayer(name).getName();
                }
                for(String reported : FreedomOpModRemastered.configs.getReports().getConfig().getKeys(false))
                {
                    if(reported.equalsIgnoreCase(name))
                    {
                        FreedomOpModRemastered.configs.getReports().getConfig().set(name, null);
                    }
                }
                return true;
            }
        }
        String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        String name = args[0];
        if(Bukkit.getPlayer(name) != null)
        {
            name = Bukkit.getPlayer(name).getName();
        }
        sender.sendMessage(ChatColor.RED + "You have reported " + name + " for \"" + reason + "\"");
        FreedomOpModRemastered.configs.getReports().getConfig().set(name + ".reporter", sender.getName());
        FreedomOpModRemastered.configs.getReports().getConfig().set(name + ".reason", reason);
        FreedomOpModRemastered.configs.getReports().saveConfig();
        return true;
    }
}
