package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_doom extends FOPMR_Command
{
    public Command_doom()
    {
        super("doom", "/doom [player] [reason]", "Ban someone and remove them from the admin list.", Rank.SUPER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }
        Player player = FOPMR_Rank.getPlayer(args[0]);
        
            if(FOPMR_Rank.isOwner(player)) {
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to doom the co-owner or the owner.");
                return false;
            }
        
        if (player == null)
        {
            sender.sendMessage("Player is not online.");
            return true;
        }
        if (FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(player), FOPMR_Rank.getRank(sender)))
        {
            sender.sendMessage("You can only doom someone of a lower rank than yourself.");
            return true;
        }
        String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        FOPMR_Bans.addBan(player, reason, sender.getName());
        FOPMR_Rank.setRank(player, Rank.OP, sender);
        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Dooming " + player.getName() + " to a never-ending oblivion in the deepest burning pits of Hell.");
        return true;
    }

}
