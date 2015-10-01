package net.camtech.fopmremastered.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "tag", description = "Set your tag", usage = "/tag [tag] | [off | default]")
public class Command_tag
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length == 0)
        {
            return false;
        }
        if(!(sender instanceof Player))
        {
            sender.sendMessage("This can only be used in-game");
            return true;
        }
        String nick = StringUtils.join(args, " ");
        for(Rank rank : Rank.values())
        {
            if(nick.toLowerCase().contains(rank.name.toLowerCase()) && FOPMR_Rank.getRank(sender).level < rank.level)
            {
                sender.sendMessage(ChatColor.RED + nick + " contains the name of a rank higher than yourself.");
                return true;
            }
        }
        Player player = (Player) sender;
        player.sendMessage(ChatColor.GREEN + "Setting tag to " + CUtils_Methods.colour(nick) + ChatColor.GREEN + ".");
        try
        {
            Connection c = FOPMR_DatabaseInterface.getConnection();
            PreparedStatement statement = c.prepareStatement("UPDATE PLAYERS SET TAG = ? WHERE UUID = ?");
            statement.setString(1, nick + "&r");
            statement.setString(2, player.getUniqueId().toString());
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