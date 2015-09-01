package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import net.camtech.fopmremastered.FOPMR_Rank;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_survival extends FOPMR_Command
{
    public Command_survival()
    {
        super("survival", "/survival <player>", "Set yourself to survival mode.", Arrays.asList("gms"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            if (args.length != 1)
            {
                return false;
            }
        }
        if (!FOPMR_Rank.isAdmin(sender) && args.length > 0)
        {
            sender.sendMessage("This can only be executed by admins.");
            return true;
        }
        if (args.length > 0)
        {
            Player player = FOPMR_Rank.getPlayer(args[0]);
            if (player == null)
            {
                sender.sendMessage("The player selected is not online.");
                return true;
            }
            sender.sendMessage(ChatColor.GOLD + "Gamemode set to Survival.");
            player.sendMessage(ChatColor.GOLD + sender.getName() + " set your gamemode to Survival.");
            player.setGameMode(GameMode.SURVIVAL);
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Gamemode set to Survival.");
        ((Player) sender).setGameMode(GameMode.SURVIVAL);
        return true;
    }

}
