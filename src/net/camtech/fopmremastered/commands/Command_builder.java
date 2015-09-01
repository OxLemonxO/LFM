package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_BoardManager;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "builder", description = "Master Builder Management!", usage = "/builder [list] | [add] [player] | [remove] [player]")
public class Command_builder
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "You must be ingame to use this command.");
            return true;
        }
        if(args.length == 0 || args.length > 2)
        {
            return false;
        }
        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("list"))
            {
                sender.sendMessage(ChatColor.GREEN + "The following users are Master Builders.");
                FreedomOpModRemastered.configs.getAdmins().getConfig().getKeys(false).stream().filter((uuid) -> (FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(uuid + ".builder"))).forEach((uuid) ->
                {
                    sender.sendMessage(ChatColor.GREEN + " - " + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(uuid + ".lastName"));
                });
                return true;
            }
        }
        if(args.length == 2)
        {
            if(!FOPMR_Rank.isSpecialist(sender))
            {
                sender.sendMessage("You do not have permission to use this command.");
                return true;
            }
            Player player = FOPMR_Rank.getPlayer(args[1]);
            if(player == null)
            {
                sender.sendMessage(ChatColor.RED + "The player could not be found.");
                return true;
            }
            if(args[0].equalsIgnoreCase("add"))
            {
                FOPMR_Commons.adminAction(sender.getName(), "Adding " + player.getName() + " to Master Builder.", false);
                FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId() + ".builder", true);
                FreedomOpModRemastered.configs.getAdmins().saveConfig();
                FOPMR_BoardManager.updateStats(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("remove"))
            {
                FOPMR_Commons.adminAction(sender.getName(), "Removing " + player.getName() + " from Master Builder.", true);
                FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId() + ".builder", false);
                FreedomOpModRemastered.configs.getAdmins().saveConfig();
                FOPMR_BoardManager.updateStats(player);
                return true;
            }
        }
        return false;
    }
}
