package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_fr extends FOPMR_Command
{

    public Command_fr()
    {
        super("fr", "/fr <player>", "Freeze a player.", Arrays.asList("freeze", "halt"), Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            FOPMR_Commons.adminAction(sender.getName(), "Toggling freeze over all players on the server.", true);
            FOPMR_Commons.globalFreeze = !FOPMR_Commons.globalFreeze;
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if(FOPMR_Rank.isAdmin(player))
                {
                    continue;
                }
                FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".freeze", FOPMR_Commons.globalFreeze);
                FreedomOpModRemastered.configs.getAdmins().saveConfig();
                player.sendMessage((FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".freeze") ? (ChatColor.RED + "You are now frozen.") : (ChatColor.AQUA + "You are now unfrozen.")));
            }
            return true;
        }
        if (args.length == 1)
        {
            Player player = FOPMR_Rank.getPlayer(args[0]);
            if (player == null)
            {
                sender.sendMessage("Player is not online.");
                return true;
            }
            if(FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(player), FOPMR_Rank.getRank(sender)))
            {
                sender.sendMessage(ChatColor.RED + "You can only freeze someone who is a lower clearance level than yourself.");
                return true;
            }
            FOPMR_Commons.adminAction(sender.getName(), "Toggling freeze over " + player.getName() + ".", true);
            FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".freeze", !FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".freeze"));
            FreedomOpModRemastered.configs.getAdmins().saveConfig();
            player.sendMessage((FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".freeze") ? (ChatColor.RED + "You are now frozen.") : (ChatColor.AQUA + "You are now unfrozen.")));
            return true;
        }
        return false;
    }

}
