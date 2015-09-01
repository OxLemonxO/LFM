package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import net.camtech.fopmremastered.chats.FOPMR_PrivateChats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="privatechat", description="Manage private chat settings.", usage="/privatechat [name] | [list] | [colour] [chatname] [colourcode] | [add] [chatname] [player] | [remove] [chatname] [player] | [create] [chatname] | [delete] [chatname]", aliases="pchat")
public class Command_privatechat
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage("Only in-game players may use this command.");
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
                sender.sendMessage(ChatColor.GOLD + "List of currently active private chats: ");
                FOPMR_PrivateChats.getFromConfig().stream().forEach((chat) ->
                {
                    ChatColor colour = ChatColor.RED;
                    if(FOPMR_PrivateChats.canAccess(player, chat.getName()))
                    {
                        colour = ChatColor.GREEN;
                    }
                    sender.sendMessage(colour + " - " + chat.getName());
                });
                return true;
            }
            if(args[0].equalsIgnoreCase("off"))
            {
                sender.sendMessage(ChatColor.GREEN + "You are now talking in the public chat.");
                FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".chat", "");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "Set current private chat to " + args[0]);
            FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".chat", args[0]);
            return true;
        }
        if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("create"))
            {
                if(args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("off"))
                {
                    sender.sendMessage(ChatColor.RED + "Forbidden name.");
                    return true;
                }
                if(!FOPMR_PrivateChats.addChat(player, args[1]))
                {
                    sender.sendMessage(ChatColor.RED + "Chat already exists with that name...");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Created chat named " + args[1]);
                return true;
            }
            if(args[0].equalsIgnoreCase("delete"))
            {
                if(!FOPMR_PrivateChats.removeChat(player, args[1]))
                {
                    sender.sendMessage(ChatColor.RED + "Could not remove chat, you are not the owner or the chat does not exist.");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Removed chat named " + args[1]);
                return true;
            }
            return false;
        }
        if(args.length == 3)
        {
            if(!FOPMR_PrivateChats.isValidChat(args[1]))
            {
                sender.sendMessage(ChatColor.RED + "No chat could be found with that name.");
            }
            if(args[0].equalsIgnoreCase("colour") || args[0].equalsIgnoreCase("color"))
            {
                ChatColor colour = ChatColor.getByChar(args[2]);
                if(colour == null)
                {
                    sender.sendMessage("Invalid colour code...");
                    return true;
                }
                if(!FOPMR_PrivateChats.changeColour(player, args[1], colour))
                {
                    sender.sendMessage(ChatColor.RED + "You are not the owner of this chat or the chat does not exist.");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Successfully changed the colour code!");
                return true;
            }
            Player player2 = FOPMR_Rank.getPlayer(args[2]);
            if(player2 == null)
            {
                sender.sendMessage(ChatColor.RED + "Could not find a player with that name.");
                return true;
            }
            if(args[0].equalsIgnoreCase("add"))
            {
                if(!FOPMR_PrivateChats.addPlayer(player, player2, args[1]))
                {
                    sender.sendMessage(ChatColor.RED + "You are not the owner of the chat, the chat does not exist or the player is already in the chat.");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Player successfully added to the chat.");
                return true;
            }
            if(args[0].equalsIgnoreCase("remove"))
            {
                if(!FOPMR_PrivateChats.removePlayer(player, player2, args[1]))
                {
                    sender.sendMessage(ChatColor.RED + "You are not the owner of the chat (and the selected player is not you), the chat does not exist or the player is not in the chat.");
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Player successfully removed from the chat.");
                return true;
            }
            return false;
        }
        return false;
    }
}
