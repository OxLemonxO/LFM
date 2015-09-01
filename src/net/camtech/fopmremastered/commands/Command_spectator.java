package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="spectator", description="Set a player's gamemode to spectator mode.", usage="/spectator <player>", aliases="gmsp")
public class Command_spectator
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
            player.setGameMode(GameMode.SPECTATOR);
            sender.sendMessage(ChatColor.GOLD + "Gamemode set to Spectator.");
            player.sendMessage(ChatColor.GOLD + sender.getName() + " set your gamemode to Spectator.");
            return true;
        }
        ((Player) sender).setGameMode(GameMode.SPECTATOR);
        sender.sendMessage(ChatColor.GOLD + "Gamemode set to Spectator.");
        return true;
    }
}
