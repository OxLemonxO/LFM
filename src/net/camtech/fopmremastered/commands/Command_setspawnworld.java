package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "setspawnworld", description = "Set the spawn of the world you are in.", usage = "/setspawnworld", aliases = "ssw", rank = FOPMR_Rank.Rank.ADMIN)
public class Command_setspawnworld
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player)) 
        {
            sender.sendMessage(ChatColor.RED + "You must be in-game to use this command.");
            return true;
        }
        Player player = (Player) sender;
        World world = player.getWorld();
        world.setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
        sender.sendMessage(ChatColor.GREEN + "Successfully changed the spawn point of " + world.getName() + " to " + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ() + ".");
        return true;
    }
}
