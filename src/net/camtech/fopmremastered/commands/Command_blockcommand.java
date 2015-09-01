package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_blockcommand extends FOPMR_Command
{
    public Command_blockcommand()
    {
        super("blockcommand", "/blockcommand [player]", "Block a player's commands.", Arrays.asList("blockcmd"), Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }
        Player player = FOPMR_Rank.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage("Player is not online.");
            return true;
        }
        if (FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(player), FOPMR_Rank.getRank(sender)) && !FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".cmdblock"))
        {
            sender.sendMessage("You can only block the commands of a player with lower clearance than yourself.");
            return true;
        }
        Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " - toggling command blockage for " + player.getName() + ".");
        FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".cmdblock", !FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".cmdblock"));
        FreedomOpModRemastered.configs.getAdmins().saveConfig();
        return true;
    }

}
