package net.camtech.fopmremastered.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
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
        try
        {
            if(args.length == 1)
            {
                if(args[0].equalsIgnoreCase("view"))
                {
                    if(!isAdmin(sender))
                    {
                        sender.sendMessage(ChatColor.RED + "You must be an admin to view reports.");
                        return true;
                    }
                    ResultSet set = FOPMR_DatabaseInterface.getAllResults(null, null, "REPORTS");
                    while(set.next())
                    {
                        sender.sendMessage(ChatColor.GREEN + (String) set.getObject("REPORTED") + ChatColor.GOLD + " was reported by " + ChatColor.GREEN + (String) set.getObject("REPORTER") + ChatColor.GOLD + " for the reason: " + ChatColor.GREEN + set.getObject("REASON") + ChatColor.GOLD + ".");
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
                    Connection c = FOPMR_DatabaseInterface.getConnection();
                    PreparedStatement statement = c.prepareStatement("DELETE FROM REPORTS WHERE REPORTED = ?");
                    statement.setString(1, name);
                    statement.executeUpdate();
                    c.commit();
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
            Connection c = FOPMR_DatabaseInterface.getConnection();
            PreparedStatement statement = c.prepareStatement("INSERT INTO REPORTS (REPORTED, REPORTER, REASON) VALUES (?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, sender.getName());
            statement.setString(3, reason);
            statement.executeUpdate();
            c.commit();
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }
}