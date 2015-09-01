package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_deopall extends FOPMR_Command
{
    public Command_deopall()
    {
        super("deopall", "/deopall", "Deop all players on the server.", Rank.SUPER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!FOPMR_Rank.isSuper(sender))
        {
            sender.sendMessage(ChatColor.RED + "You don't have permissions to execute this command.");
            return true;
        }

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (!FOPMR_Rank.isAdmin(player))
            {
                player.setOp(false);
            }
        }
        return true;
    }

}
