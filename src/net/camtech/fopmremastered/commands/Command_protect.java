package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.protectedareas.FOPMR_ProtectedAreas;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "protect", description = "Protect an area from being edited.", usage = "protect [[list] | [create] [name] [radius] [rank] | [delete] [name] | [add] [name] [player] | [remove] [name] [player]", aliases="pa, protectarea, parea, protecta")
public class Command_protect
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Only in-game players can execute this command!");
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 0)
        {
            return false;
        }
        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("list"))
            {
                sender.sendMessage(ChatColor.GOLD + "List of currently active protected areas: ");
                FOPMR_ProtectedAreas.getFromConfig().stream().forEach((area) ->
                {
                    ChatColor colour = ChatColor.RED;
                    if(area.canAccess(player))
                    {
                        colour = ChatColor.GREEN;
                    }
                    sender.sendMessage(colour + " - " + area.getName());
                });
                return true;
            }
        }
        if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("delete"))
            {
                if(!FOPMR_ProtectedAreas.removeArea(player, args[1]))
                {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to manage the area, or the area does not exist.");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Area successfully deleted!");
                return true;
            }
            if(args[0].equalsIgnoreCase("tp"))
            {
                if(!FOPMR_ProtectedAreas.isValidArea(args[1]))
                {
                    sender.sendMessage(ChatColor.RED + "The area does not exist.");
                    return false;
                }
                sender.sendMessage(ChatColor.RED + "Teleporting to area.");
                player.teleport(FOPMR_ProtectedAreas.getFromName(args[1]).getLocation());
                return true;
            }
        }
        if(args.length == 3)
        {
            Player player2 = FOPMR_Rank.getPlayer(args[2]);
            if(args[0].equalsIgnoreCase("add"))
            {
                if(player2 == null)
                {
                    sender.sendMessage(ChatColor.RED + "Player could not be found.");
                    return true;
                }
                if(!FOPMR_ProtectedAreas.addPlayer(player, player2, args[1]))
                {
                    sender.sendMessage(ChatColor.RED + "Area does not exist, you do not have permission to manage the area or the player is already allowed to edit the area.");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Player added to area.");
                return true;
            }
            if(args[0].equalsIgnoreCase("remove"))
            {
                if(player2 == null)
                {
                    sender.sendMessage(ChatColor.RED + "Player could not be found.");
                    return true;
                }
                if(!FOPMR_ProtectedAreas.removePlayer(player, player2, args[1]))
                {
                    sender.sendMessage(ChatColor.RED + "Area does not exist, you do not have permission to manage the area or the player is already allowed to edit the area.");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Player removed from area.");
                return true;
            }
        }
        if(args.length == 4)
        {
            if(args[0].equalsIgnoreCase("create"))
            {
                int range;
                try
                {
                    range = Integer.parseInt(args[2]);
                }
                catch(Exception ex)
                {
                    return false;
                }
                if(range > 50 && !FOPMR_Rank.isAdmin(sender))
                {
                    sender.sendMessage(ChatColor.RED + "You can only create an area which is 50 or less blocks in radius.");
                    return true;
                }
                Rank rank = FOPMR_Rank.getFromName(args[3]);
                if(rank.level > FOPMR_Rank.getRank(sender).level)
                {
                    sender.sendMessage(ChatColor.RED + "You can only set an area to a rank equal to yourself.");
                    return true;
                }
                if(!FOPMR_ProtectedAreas.addArea(player, args[1], rank, player.getLocation(), range))
                {
                    sender.sendMessage(ChatColor.RED + "The protected area already exists!");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Created area.");
                return true;
            }
        }
        return false;
    }
}
