package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import net.camtech.fopmremastered.FOPMR_BoardManager;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_djump extends FOPMR_Command
{
    public Command_djump()
    {
        super("djump", "/djump", "Toggle your double jumping ability.", Arrays.asList("doublejump"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("This can only be used in game.");
            return true;
        }
        Player player = (Player) sender;
        sender.sendMessage(ChatColor.GREEN + "Toggled double jump mode.");
        FOPMR_BoardManager.updateStats(player);
        FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".djump", !FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".djump"));
        FreedomOpModRemastered.configs.getAdmins().saveConfig();
        return true;
    }

}
