package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import net.camtech.fopmremastered.FOPMR_BoardManager;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_chatlevel extends FOPMR_Command
{

    public Command_chatlevel()
    {
        super("chatlevel", "/chatlevel [level]", "Change your chat level.", Arrays.asList("cl"), Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.GREEN + "You must be in-game to use this command.");
            return true;
        }
        Player player = (Player) sender;
        int oldlevel = FreedomOpModRemastered.configs.getAdmins().getConfig().getInt(player.getUniqueId().toString() + ".chatLevel");
        int level;
        try
        {
            level = Integer.parseInt(args[0]);
        } catch (Exception ex)
        {
            level = FOPMR_Rank.getFromName(StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ")).level;
        }
        if (FOPMR_Rank.getRank(sender).level >= level)
        {
            FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".chatLevel", level);
            sender.sendMessage(ChatColor.GREEN + "You are now talking in " + FOPMR_Rank.getFromLevel(level).name + " Chat.");
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "You do not have permission to access this chat level!");
        }
        if(args.length > 1)
        {
            String msg =  StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
            player.chat(msg);
            FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".chatLevel", oldlevel);
        }
        FOPMR_BoardManager.updateStats(player);
        return true;
    }

}
