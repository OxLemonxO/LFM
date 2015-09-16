package net.camtech.fopmremastered.commands;

import me.libraryaddict.disguise.DisguiseAPI;
import net.camtech.fopmremastered.FOPMR_Rank;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.ADMIN;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="uall",description="Undisguise all players.",usage="/uall",rank=ADMIN)
public class Command_uall
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(Bukkit.getPluginManager().getPlugin("LibsDisguises") == null)
        {
            sender.sendMessage(ChatColor.RED + "LibsDigsuises could not be found.");
            return true;
        }
        for(Player player : Bukkit.getOnlinePlayers())
        {
            if(FOPMR_Rank.getRank(sender).level > FOPMR_Rank.getRank(player).level)
            {
                DisguiseAPI.undisguiseToAll(player);
                      
            }
        }
        return true;
    }
}