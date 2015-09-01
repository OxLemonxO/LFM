package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name="eternalban", description="Toggles the eternalban flag on a user's ban entry.", usage="/eternalban <player>", rank=Rank.SYSTEM, aliases="permban, eternban")
public class Command_eternalban
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        if(!FOPMR_Bans.isBanned(args[0]))
        {
            sender.sendMessage(ChatColor.RED + "The player: " + args[0] + " is not banned.");
            return true;
        }
        String location = "names." + args[0] + ".perm";
        FreedomOpModRemastered.configs.getBans().getConfig().set(location, !FreedomOpModRemastered.configs.getBans().getConfig().getBoolean(location));
        String message = (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean(location) ? ChatColor.GREEN + "Toggled eternal ban on." : ChatColor.RED + "Toggled eternal ban off.");
        sender.sendMessage(message);
        return true;
    }
}
