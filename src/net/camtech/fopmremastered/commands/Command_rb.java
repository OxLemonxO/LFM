package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="rb", description="Rollback a player.", usage="/rb <player>", aliases="rollback,rollb,rback", rank=FOPMR_Rank.Rank.ADMIN)
public class Command_rb
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        String name = args[0];
        Player player = Bukkit.getPlayer(name);
        if(player != null)
        {
            name = player.getName();
        }
        FOPMR_Commons.adminAction(sender.getName(), "Rolling back " + name + ".", true);
        Bukkit.dispatchCommand(sender, "co rb t:1d u:" + name + " r:#global #silent");
        return true;
    }
}
