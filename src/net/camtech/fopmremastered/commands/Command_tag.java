package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="tag", description="Set your tag", usage="/tag [tag] | [off | default]")
public class Command_tag
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }
        if (!(sender instanceof Player))
        {
            sender.sendMessage("This can only be used in-game");
            return true;
        }
        String nick = StringUtils.join(args, " ");
        for (Rank rank : Rank.values())
        {
            if (nick.toLowerCase().contains(rank.name.toLowerCase()) && FOPMR_Rank.getRank(sender).level < rank.level)
            {
                sender.sendMessage(ChatColor.RED + nick + " contains the name of a rank higher than yourself.");
                return true;
            }
        }
        Player player = (Player) sender;
        if(!FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".randomChatColour") && nick.contains("&-"))
        {
            player.sendMessage(ChatColor.RED + "You cannot use random chat colours, you must purchase it in the VoteShop (/vs).");
            nick = nick.replaceAll("&-", "");
        }
        if(!FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".chatColours") && CUtils_Methods.hasChatColours(nick))
        {
            player.sendMessage(ChatColor.RED + "You cannot use chat colours, you may purchase them in the VoteShop (/vs).");
            nick = nick.replaceAll("&.", "");
        }
        player.sendMessage(ChatColor.GREEN + "Tag set to " + CUtils_Methods.colour(nick));
        FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".tag", nick + "&r");
        FreedomOpModRemastered.configs.getAdmins().saveConfig();
        return true;
    }

}
