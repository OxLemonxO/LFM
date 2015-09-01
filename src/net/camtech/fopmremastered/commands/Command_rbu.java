package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="rbu", description="Undo a rollback of a player.", usage="/rbu <player>", aliases="rollbackundo,rollbundo,rbackundo,rollbacku,rollbu,rbacku", rank=FOPMR_Rank.Rank.ADMIN)
public class Command_rbu
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
        FOPMR_Commons.adminAction(sender.getName(), "Undoing rollback of " + name + ".", true);
        Bukkit.dispatchCommand(sender, "co rs t:1d u:" + name + " r:#global #silent");
        return true;
    }
}
