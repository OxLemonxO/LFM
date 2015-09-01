package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_opall extends FOPMR_Command
{

    public Command_opall()
    {
        super("opall", "/opall", "Op all players.", "You aren't allowed to use this command.", Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Bukkit.broadcastMessage(ChatColor.BLUE + sender.getName() + " - Opping all players on the server.");
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.setOp(true);
        }
        return true;
    }

}
