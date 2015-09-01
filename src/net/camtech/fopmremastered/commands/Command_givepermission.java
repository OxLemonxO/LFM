/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

@CommandParameters(name="givepermission", description="Give a user a custom permission flag!", usage="/givepermission [player] [permission]",rank=Rank.OWNER)
public class Command_givepermission
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 2)
        {
            return false;
        }
        Player player = FOPMR_Rank.getPlayer(args[0]);
        if(player == null)
        {
            sender.sendMessage(ChatColor.RED + "The player could not be found.");
            return true;
        }
        PermissionAttachment attach = player.addAttachment(FreedomOpModRemastered.plugin);
        attach.setPermission(args[1], true);
        return true;
    }
}
