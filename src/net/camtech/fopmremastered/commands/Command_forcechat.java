package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="forcechat", description="Force a player to send a chat message.", usage="/forcechat [player] [message]", rank=Rank.SENIOR, aliases="fc, fchat, forcec")
public class Command_forcechat
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }
        Player player = FOPMR_Rank.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage(ChatColor.RED + "The player you attempted to select is not online.");
            return true;
        }
        String chat = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        if (FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(player), FOPMR_Rank.getRank(sender)))
        {
            sender.sendMessage(ChatColor.RED + "You cannot force someone of an equal or higher rank than yourself to chat.");
            return true;
        }
        sender.sendMessage(ChatColor.BLUE + "Sending " + chat + " as " + player.getName() + ".");
        player.chat(chat);
        return true;
    }

}
