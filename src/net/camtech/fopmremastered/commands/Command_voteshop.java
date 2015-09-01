package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "voteshop", description = "Buy things with your votes!", usage = "/voteshop", aliases = "vs")
public class Command_voteshop
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Only in-game players can use the VoteShop!");
            return true;
        }
        Player player = (Player) sender;
        player.sendMessage(CUtils_Methods.colour("&-You have " + CUtils_Methods.randomChatColour() + FreedomOpModRemastered.configs.getAdmins().getConfig().getInt(player.getUniqueId().toString() + ".votes") + " &-votes."));
        FOPMR_Commons.openVoteShop(player);
        return true;
    }
}
