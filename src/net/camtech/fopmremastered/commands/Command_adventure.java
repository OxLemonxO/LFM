package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="adventure", description="Set a player's gamemode to adventure mode.", usage="/adventure <player>", aliases="gma")
public class Command_adventure
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            if (args.length != 1)
            {
                return false;
            }
        }
        if (!FOPMR_Rank.isAdmin(sender) && args.length > 0)
        {
            sender.sendMessage("This can only be executed by admins.");
            return true;
        }
        if (args.length > 0)
        {
            Player player = FOPMR_Rank.getPlayer(args[0]);
            if (player == null)
            {
                sender.sendMessage("The player selected is not online.");
                return true;
            }
            player.setGameMode(GameMode.CREATIVE);
            sender.sendMessage(ChatColor.GOLD + "Gamemode set to Adventure.");
            player.sendMessage(ChatColor.GOLD + sender.getName() + " set your gamemode to Adventure.");
            return true;
        }
        ((Player) sender).setGameMode(GameMode.ADVENTURE);
        sender.sendMessage(ChatColor.GOLD + "Gamemode set to Adventure.");
        return true;
    }
}
