package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.SPECIALIST;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import net.camtech.fopmremastered.worlds.FOPMR_WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name="createworld", description="Create an entire new world!", usage="/createworld [worldname] [generator] [rank] [load on enable]", rank=SPECIALIST)
public class Command_createworld
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 4)
        {
            return false;
        }
        boolean pass;
        switch(args[1])
        {
            case "flat":
                pass = true;
                break;
            case "default":
                pass = true;
                break;
            case "empty":
                pass = true;
                break;
            case "rollinghills":
                pass = true;
                break;
            default:
                pass = false;
        }
        if(!pass)
        {
            sender.sendMessage(ChatColor.RED + "The available generators are: \"flat\", \"empty\", \"rollinghills\" and \"default\"");
            return true;
        }
        if(Bukkit.getWorld(args[0]) != null)
        {
            sender.sendMessage(ChatColor.RED + "A world already exists with that name.");
            return true;
        }
        FreedomOpModRemastered.configs.getWorlds().getConfig().set(args[0] + ".generator", args[1]);
        FreedomOpModRemastered.configs.getWorlds().getConfig().set(args[0] + ".rank", args[2]);
        boolean onEnable = false;
        if(args[3].equalsIgnoreCase("true"))
        {
            onEnable = true;
        }
        FreedomOpModRemastered.configs.getWorlds().getConfig().set(args[0] + ".onenable", onEnable);
        FreedomOpModRemastered.configs.getWorlds().saveConfig();
        FOPMR_WorldManager.reloadWorldsFromConfig();
        FOPMR_Commons.adminAction(sender.getName(), "creating new world named: " + args[0] + " with the generator of: " + args[1] + " accessible to anyone with the clearance level of " + FOPMR_Rank.getFromName(args[3]).level + " or above.", true);
        return true;
    }
}
