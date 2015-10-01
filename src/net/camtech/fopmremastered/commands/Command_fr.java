package net.camtech.fopmremastered.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_fr extends FOPMR_Command
{

    public Command_fr()
    {
        super("fr", "/fr <player>", "Freeze a player.", Arrays.asList("freeze", "halt"), Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        try
        {
            if(args.length == 0)
            {
                FOPMR_Commons.adminAction(sender.getName(), "Toggling freeze over all players on the server.", true);
                FOPMR_Commons.globalFreeze = !FOPMR_Commons.globalFreeze;
                Connection c = FOPMR_DatabaseInterface.getConnection();
                for(Player player : Bukkit.getOnlinePlayers())
                {
                    if(FOPMR_Rank.isAdmin(player))
                    {
                        continue;
                    }
                    PreparedStatement statement = c.prepareStatement("UPDATE PLAYERS SET FROZEN = ? WHERE UUID = ?");
                    statement.setBoolean(1, FOPMR_Commons.globalFreeze);
                    statement.setString(2, player.getUniqueId().toString());
                    statement.executeUpdate();
                    player.sendMessage((FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "FROZEN", "PLAYERS") ? (ChatColor.RED + "You are now frozen.") : (ChatColor.AQUA + "You are now unfrozen.")));
                }
                c.commit();
                return true;
            }
            if(args.length == 1)
            {
                Player player = FOPMR_Rank.getPlayer(args[0]);
                if(player == null)
                {
                    sender.sendMessage("Player is not online.");
                    return true;
                }
                if(FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(player), FOPMR_Rank.getRank(sender)))
                {
                    sender.sendMessage(ChatColor.RED + "You can only freeze someone who is a lower clearance level than yourself.");
                    return true;
                }
                FOPMR_Commons.adminAction(sender.getName(), "Toggling freeze over " + player.getName() + ".", true);
                FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), !(FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "FROZEN", "PLAYERS")), "FROZEN", "PLAYERS");
                player.sendMessage((FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "FROZEN", "PLAYERS") ? (ChatColor.RED + "You are now frozen.") : (ChatColor.AQUA + "You are now unfrozen.")));
                return true;
            }
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return false;
    }

}