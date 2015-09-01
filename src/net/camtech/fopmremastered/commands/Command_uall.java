package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.ADMIN;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
        Bukkit.getOnlinePlayers().stream().filter((player) -> (FOPMR_Rank.getRank(sender).level > FOPMR_Rank.getRank(player).level && me.libraryaddict.disguise.DisguiseAPI.isDisguised(player))).forEach((player) ->
        {
            me.libraryaddict.disguise.DisguiseAPI.undisguiseToAll(player);
        });
        return true;
    }
}
