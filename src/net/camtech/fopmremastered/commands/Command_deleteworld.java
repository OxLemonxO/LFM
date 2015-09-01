package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.SPECIALIST;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name="deleteworld", usage="/deleteworld [world]", description="Remove a world's entry from the config so it no longer loads.", rank=SPECIALIST)
public class Command_deleteworld
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        if(!FreedomOpModRemastered.configs.getWorlds().getConfig().contains(args[0]))
        {
            sender.sendMessage(ChatColor.RED + "The world does not exist or is not a custom FOPM: R world.");
            return true;
        }
        if(Bukkit.getWorld(args[0]) != null)
        {
            CUtils_Methods.unloadWorld(Bukkit.getWorld(args[0]));
        }
        FreedomOpModRemastered.configs.getWorlds().getConfig().set(args[0], null);
        FreedomOpModRemastered.configs.getWorlds().saveConfig();
        return true;
    }
}
