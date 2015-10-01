package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="owner", usage="/owner <code>", description="Verify yourself as the owner, check the console for more details. (Disables after first usage!)")
public class Command_owner
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "This command must be sent from in-game.");
            return true;
        }
        if(FOPMR_Commons.verifyCode == null)
        {
            sender.sendMessage(ChatColor.RED + "An owner has already been set.");
            return true;
        }
        if(args[0].equals(FOPMR_Commons.verifyCode))
        {
            FOPMR_Commons.adminAction(sender.getName(), "Verifying myself as the Owner!", false);
            FOPMR_Rank.setRank((Player) sender, FOPMR_Rank.Rank.OWNER, null);
            FOPMR_Commons.verifyCode = null;
            FreedomOpModRemastered.plugin.getConfig().set("general.owner", true);
            FreedomOpModRemastered.plugin.saveConfig();
            return true;
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "This is the incorrect verification code!");
            return true;
        }
    }
}