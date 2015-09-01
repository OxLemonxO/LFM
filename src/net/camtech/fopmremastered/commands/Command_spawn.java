package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="spawn", usage="/spawn <player>", description="Teleport to the spawn!")
public class Command_spawn
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length == 1)
        {
            if(!FOPMR_Rank.isAdmin(sender))
            {
                sender.sendMessage(ChatColor.RED + "You may not teleport other players to spawn.");
                return true;
            }
            Player player = FOPMR_Rank.getPlayer(args[0]);
            if(player == null)
            {
                sender.sendMessage(ChatColor.RED + "That player could not be found!");
                return false;
            }
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "You can only teleport other players from console.");
            return false;
        }
        Player player = (Player) sender;
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        return true;
    }
}
