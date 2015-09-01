package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.SPECIALIST;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import net.camtech.fopmremastered.worlds.FOPMR_WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name="loadworld", usage="/loadworld [world]", description="Load an unloaded world.", rank=SPECIALIST)
public class Command_loadworld
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        for(World world : Bukkit.getWorlds())
        {
            if(world.getName().equals(args[0]))
            {
                sender.sendMessage(ChatColor.RED + "The world you are trying to load, is already loaded!");
                return true;
            }
        }
        if(!FreedomOpModRemastered.configs.getWorlds().getConfig().contains(args[0]))
        {
            sender.sendMessage(ChatColor.RED + "The world you are trying to load, does not exist or is not a custom FOPM: R world.");
            return true;
        }
        FOPMR_WorldManager.loadWorld(args[0]);
        World world = Bukkit.getWorld(args[0]);
        FOPMR_Commons.adminAction(sender.getName(), "loading the world: \"" + world.getName() + "\" into memory.", false);
        return true;
    }
}
