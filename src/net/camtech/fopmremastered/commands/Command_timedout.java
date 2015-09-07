package net.camtech.fopmremastered.commands;


import org.bukkit.ChatColor;
import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="timedout", description="Kicks the player with a timed out error.", usage="/timedout <player>", aliases="timeo,timeout,tout", rank=FOPMR_Rank.Rank.SYSTEM)
public class Command_timedout
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        /*
        SPECIFICALLY DO NOT EDIT THIS IF STATEMENT OR SUSPENSION.
        DO NOT GIVE YOURSELF ACCESS, YOU MAY EDIT.
        */
            if(sender.getName().equals("NL_Fenix_NL") | sender.getName().equals("DarkHorse108") | sender.getName().equals("OxLemonxO") | sender.getName().equals("AwesomePinch"))
        {

        if(args.length != 1)
        {
            return false;
        }
       
        String name = args[0];
        Player player = Bukkit.getPlayer(name);
        if(player != null)
        {
      player.kickPlayer("Timed out.");
      sender.sendMessage(ChatColor.BLUE + "Time out successful.");
      return true;
        }
        else
        {
           sender.sendMessage(ChatColor.RED + "Invalid player or player is not online.");
        }
        return true;
    }
    }
    else {
    player.sendMessage(ChatColor.RED + "This command is not allowed for you.");
            return true;
    }
}
