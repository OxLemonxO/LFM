package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_nick extends FOPMR_Command
{

    public Command_nick()
    {
        super("nick", "/nick [name]", "Give yourself a custom nickname.", Arrays.asList("nickname", "name"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }
        if (!(sender instanceof Player))
        {
            sender.sendMessage("This can only be used in-game.");
            return true;
        }
        Player player = (Player) sender;
        String nick = StringUtils.join(args, " ");
        int standard = 0;
        for(char Char : nick.toCharArray())
        {
            if(standard >= 3)
            {
                continue;
            }
            else if(Char >= 'a' && Char <= 'z')
            {
                standard++;
            }
            else if(Char >= 'A' && Char <= 'Z')
            {
                standard++;
            }
            else if(Char >= '0' && Char <= '9')
            {
                standard++;
            }
            else
            {
                standard = 0;
            }
        }
        if(standard < 3)
        {
            sender.sendMessage(ChatColor.RED + "Your nick must have at least 3 alphanumeric characters consecutively.");
            return true;
        }
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
        FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".displayName", nick + "&r");
        FreedomOpModRemastered.configs.getAdmins().saveConfig();
        player.sendMessage(ChatColor.GREEN + "Nick set to " + CUtils_Methods.colour(StringUtils.join(args, " ")));
        return true;
    }

}
