package net.camtech.fopmremastered.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import net.camtech.fopmremastered.FOPMR_BoardManager;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "builder", description = "Master Builder Management!", usage = "/builder [list] | [add] [player] | [remove] [player]")
public class Command_builder
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        try
        {
            if(!(sender instanceof Player))
            {
                sender.sendMessage(ChatColor.RED + "You must be ingame to use this command.");
                return true;
            }
            if(args.length == 0 || args.length > 2)
            {
                return false;
            }
            if(args.length == 1)
            {
                if(args[0].equalsIgnoreCase("list"))
                {
                    sender.sendMessage(ChatColor.GREEN + "The following users are Master Builders.");
                    Connection c = FOPMR_DatabaseInterface.getConnection();
                    PreparedStatement statement = c.prepareStatement("SELECT * FROM PLAYERS WHERE BUILDER = 1");
                    ResultSet set = statement.executeQuery();
                    while(set.next())
                    {
                        sender.sendMessage(ChatColor.GREEN + " - " + set.getString("NAME"));
                    }
                    return true;
                }
            }
            if(args.length == 2)
            {
                if(!FOPMR_Rank.isSpecialist(sender))
                {
                    sender.sendMessage("You do not have permission to use this command.");
                    return true;
                }
                Player player = FOPMR_Rank.getPlayer(args[1]);
                if(player == null)
                {
                    sender.sendMessage(ChatColor.RED + "The player could not be found.");
                    return true;
                }
                if(args[0].equalsIgnoreCase("add"))
                {
                    FOPMR_Commons.adminAction(sender.getName(), "Adding " + player.getName() + " to Master Builder.", false);
                    FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), true, "BUILDER", "PLAYERS");
                    FOPMR_BoardManager.updateStats(player);
                    return true;
                }
                if(args[0].equalsIgnoreCase("remove"))
                {
                    FOPMR_Commons.adminAction(sender.getName(), "Removing " + player.getName() + " from Master Builder.", true);
                    FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), false, "BUILDER", "PLAYERS");
                    FOPMR_BoardManager.updateStats(player);
                    return true;
                }
            }
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return false;
    }
}