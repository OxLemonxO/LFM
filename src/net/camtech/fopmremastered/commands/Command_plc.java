package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@CommandParameters(name="plc", description="Manage plugins.", usage="/plc [enable | disable | reload] [plugin name]", aliases="plugincontrol", rank=Rank.EXECUTIVE)
public class Command_plc
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length != 2)
        {
            return false;
        }
        PluginManager manager = Bukkit.getPluginManager();
        Plugin plugin = manager.getPlugin(args[1]);
        if (plugin == null)
        {
            sender.sendMessage(args[1] + " is not a valid plugin...");
            return true;
        }
        if (args[1].equalsIgnoreCase("Essentials"))
        {
            sender.sendMessage("Essentials cannot be managed at this time.");
            return true;
        }
        switch (args[0].toLowerCase())
        {
            case "enable":
                if (manager.isPluginEnabled(plugin))
                {
                    sender.sendMessage("Plugin is already enabled.");
                    break;
                }
                manager.enablePlugin(plugin);
                FOPMR_CommandRegistry.registerCommands();
                break;
            case "disable":
                if (!manager.isPluginEnabled(plugin))
                {
                    sender.sendMessage("Plugin is already disabled.");
                    break;
                }
                manager.disablePlugin(plugin);
                FOPMR_CommandRegistry.registerCommands();
                break;
            case "reload":
                manager.disablePlugin(plugin);
                manager.enablePlugin(plugin);
                FOPMR_CommandRegistry.registerCommands();
                break;
            default:
                return false;
        }
        sender.sendMessage(ChatColor.GREEN + "Action " + ChatColor.AQUA + args[0].toLowerCase() + ChatColor.GREEN + " performed on " + ChatColor.AQUA + plugin.getName() + ChatColor.GREEN + " successfully.");
        return true;
    }
}
