package net.camtech.fopmremastered.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import net.camtech.fopmremastered.FOPMR_Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_nick extends FOPMR_Command
{

    public Command_nick()
    {
        super("nick", "/nick [name]", "Give yourself a custom nickname.", Arrays.asList("nickname", "name"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length == 0)
        {
            return false;
        }
        if(!(sender instanceof Player))
        {
            sender.sendMessage("This can only be used in-game.");
            return true;
        }
        Player player = (Player) sender;
        String nick = StringUtils.join(args, " ");
        int standard = 0;
        for(char Char : nick.toCharArray())
        {
            if(standard >= 3)
            {
                continue;
            }
            else if(Char >= 'a' && Char <= 'z')
            {
                standard++;
            }
            else if(Char >= 'A' && Char <= 'Z')
            {
                standard++;
            }
            else if(Char >= '0' && Char <= '9')
            {
                standard++;
            }
            else
            {
                standard = 0;
            }
        }
        if(standard < 3)
        {
            sender.sendMessage(ChatColor.RED + "Your nick must have at least 3 alphanumeric characters consecutively.");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + "Setting nick to " + CUtils_Methods.colour(StringUtils.join(args, " ")) + ChatColor.GREEN + ".");
        try
        {
            Connection c = FOPMR_DatabaseInterface.getConnection();
            PreparedStatement statement = c.prepareStatement("UPDATE PLAYERS SET NICK = ? WHERE UUID = ?");
            statement.setString(1, nick + "&r");
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
            FOPMR_Rank.nicks.put(player.getName(), nick + "&r");
            c.commit();
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }

}