package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Commons;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.SPECIALIST;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name="unloadworld", usage="/unloadworld [world]", description="Unload an loaded world.", rank=SPECIALIST)
public class Command_unloadworld
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        boolean loaded = false;
        for(World world : Bukkit.getWorlds())
        {
            if(world.getName().equals(args[0]))
            {
                loaded = true;
            }
        }
        if(!loaded)
        {
            sender.sendMessage(ChatColor.RED + "The world you are trying to unload, is not loaded.");
            return true;
        }
        if(!FreedomOpModRemastered.configs.getWorlds().getConfig().contains(args[0]))
        {
            sender.sendMessage(ChatColor.RED + "The world you are trying to load, does not exist or is not a custom FOPM: R world.");
            return true;
        }
        CUtils_Methods.unloadWorld(Bukkit.getWorld(args[0]));
        FOPMR_Commons.adminAction(sender.getName(), "unloading the world: \"" + args[0] + "\" from memory.", true);
        return true;
    }
}
