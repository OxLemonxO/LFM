package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="uuid", usage="/uuid <player>", description="See your UUID or someone else's UUID.")
public class Command_uuid
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length == 1)
        {
            OfflinePlayer player = FOPMR_Rank.getPlayer(args[0]);
            if(player == null)
            {
                player = Bukkit.getOfflinePlayer(args[0]);
            }
            if(player == null)
            {
                sender.sendMessage(ChatColor.RED + "Player could not be found.");
                return false;
            }
            sender.sendMessage(ChatColor.GREEN + "The uuid of " + player.getName() + " is " + player.getUniqueId() +".");
        }
        if(!(sender instanceof Player))
        {
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "Your UUID is " + ((Player) sender).getUniqueId() + ".");
        return true;
    }
}
