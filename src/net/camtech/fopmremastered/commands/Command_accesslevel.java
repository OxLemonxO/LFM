package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_RestManager;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import static net.camtech.fopmremastered.FreedomOpModRemastered.configs;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "accesslevel", usage = "/accesslevel [level]", description = "Change server access level.", aliases = "al, alevel, accessl, ld, ldown, lockdown, lockd", rank = FOPMR_Rank.Rank.ADMIN)
public class Command_accesslevel
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        int level;
        try
        {
            level = Integer.parseInt(args[0]);
        } catch (Exception ex)
        {
            level = FOPMR_Rank.getFromName(StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ")).level;
        }
        if (FOPMR_Rank.getRank(sender).level < level)
        {
            sender.sendMessage(ChatColor.RED + "You can only set the access level to your rank or lower.");
            return true;
        }
        FreedomOpModRemastered.configs.getMainConfig().getConfig().set("general.accessLevel", level);
        FreedomOpModRemastered.configs.getMainConfig().saveConfig();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (FOPMR_Rank.getRank(player).level < level)
            {
                player.kickPlayer("Server has been put into lockdown mode.");
            }
        }
        String message = sender.getName() + " has locked the server down to level " + level + ".";
        if(level == 0)
        {
            message = sender.getName() + " has reopened the server to all players.";
        }
        FOPMR_RestManager.sendMessage(configs.getMainConfig().getConfig().getInt("rest.lockdownid"), message);
        Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " - Locking server down to clearance level " + level + " (" + FOPMR_Rank.getFromLevel(level).name + ").");
        return true;
    }

}
