package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.worlds.FOPMR_WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="world", usage="/world <list> | <world> [[add] | remove] [player]", description="Teleport to a world.", aliases="tptoworld")
public class Command_world
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length < 1)
        {
            return false;
        }
        if(!(sender instanceof Player))
        {
            return true;
        }
        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("list"))
            {
                sender.sendMessage(ChatColor.GOLD + "Here is a list of all currently loaded worlds: ");
                for(World world : Bukkit.getWorlds())
                {
                    ChatColor colour = RED;
                    if(FOPMR_WorldManager.canAccess(world.getName(), (Player) sender))
                    {
                        colour = GREEN;
                    }
                    String gueststring = "";
                    if(FOPMR_WorldManager.worlds.containsKey(world))
                    {
                        if(FOPMR_WorldManager.guestlists.get(world).isGuest(sender.getName()))
                        {
                            gueststring = " (Guest)";
                        }
                    }
                    sender.sendMessage(colour + " - " + world.getName() + gueststring);
                }
                return true;
            }
            FOPMR_WorldManager.sendToWorld(args[0], (Player) sender);
            return true;
        }
        if(args.length == 3)
        {
            Player player = FOPMR_Rank.getPlayer(args[2]);
            if(player == null)
            {
                return false;
            }
            if(args[1].equalsIgnoreCase("add"))
            {
                FOPMR_WorldManager.addGuest(args[0], player, (Player) sender);
                return true;
            }
            if(args[1].equalsIgnoreCase("remove"))
            {
                FOPMR_WorldManager.removeGuest(args[0], player, (Player) sender);
                return true;
            }
        }
        return false;
    }
}
