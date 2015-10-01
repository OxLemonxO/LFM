package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "realname", description = "Find the real name of a player from their nickname.", usage = "/realname [player]")
public class Command_realname
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        try
        {
            for(Object obj : FOPMR_DatabaseInterface.getAsArrayList("UUID", null, "UUID", "PLAYERS"))
            {
                String uuid = (String) obj;
                String nick = (String) FOPMR_DatabaseInterface.getFromTable("UUID", uuid, "NICK", "PLAYERS");
                String name = (String) FOPMR_DatabaseInterface.getFromTable("UUID", uuid, "NAME", "PLAYERS");
                String onoroff = ((Bukkit.getPlayer(name) != null) ? (ChatColor.GREEN + "online") : (ChatColor.RED + "offline"));
                if(nick.toLowerCase().contains(args[0].toLowerCase()))
                {
                    sender.sendMessage(CUtils_Methods.colour(nick + ChatColor.WHITE + " is " + name + " and is " + onoroff + ChatColor.WHITE + "."));
                }
            }
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }
}