package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_banhammer extends FOPMR_Command
{

    public Command_banhammer()
    {
        super("banhammer", "/banhammer", "Unleash the banhammer...", Rank.SPECIALIST);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        try
        {
            if(!(sender instanceof Player))
            {
                sender.sendMessage(ChatColor.RED + "Only in-game players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            if(FOPMR_DatabaseInterface.hasBanHammer(player.getUniqueId().toString()))
            {
                player.getInventory().remove(FOPMR_Commons.getBanHammer());
                FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), false, "BANHAMMER", "PLAYERS");
                Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + " has placed the BanHammer back into its sheath");
                return true;
            }
            player.getInventory().addItem(FOPMR_Commons.getBanHammer());
            player.getWorld().strikeLightning(player.getLocation());
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has unleashed the BanHammer!");
            FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), true, "BANHAMMER", "PLAYERS");
            return true;
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }
}