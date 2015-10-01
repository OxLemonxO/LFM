package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "eternalban", description = "Toggles the eternalban flag on a user's ban entry.", usage = "/eternalban <player>", rank = Rank.SYSTEM, aliases = "permban, eternban")
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
        try
        {
            FOPMR_DatabaseInterface.updateInTable("NAME", args[0], !(FOPMR_DatabaseInterface.getBooleanFromTable("NAME", args[0], "PERM", "NAME_BANS")), "PERM", "NAME_BANS");
            FOPMR_DatabaseInterface.updateInTable("IP", FOPMR_DatabaseInterface.getIpFromName(args[0]), !(FOPMR_DatabaseInterface.getBooleanFromTable("IP", FOPMR_DatabaseInterface.getIpFromName(args[0]), "PERM", "IP_BANS")), "PERM", "IP_BANS");
            FOPMR_DatabaseInterface.updateInTable("UUID", FOPMR_DatabaseInterface.getUuidFromName(args[0]), !(FOPMR_DatabaseInterface.getBooleanFromTable("UUID", FOPMR_DatabaseInterface.getUuidFromName(args[0]), "PERM", "UUID_BANS")), "PERM", "UUID_BANS");
            String message = (FOPMR_DatabaseInterface.getBooleanFromTable("NAME", args[0], "PERM", "NAME_BANS") ? ChatColor.GREEN + "Toggled eternal ban on." : ChatColor.RED + "Toggled eternal ban off.");
            sender.sendMessage(message);
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }
}