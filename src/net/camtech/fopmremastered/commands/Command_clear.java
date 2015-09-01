package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "clear", usage = "/clear <player>", description = "Clear inventories.", aliases = "ci")
public class Command_clear
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
            player.getInventory().clear();
            sender.sendMessage(ChatColor.GOLD + "Inventory cleared.");
            player.sendMessage(ChatColor.GOLD + sender.getName() + " cleared your inventory.");
            return true;
        }
        ((Player) sender).getInventory().clear();
        sender.sendMessage(ChatColor.GOLD + "Inventory cleared.");
        return true;
    }

}
