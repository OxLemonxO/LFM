package net.camtech.fopmremastered.commands;

import java.util.ArrayList;
import java.util.List;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_list extends FOPMR_Command
{

    public Command_list()
    {
        super("list", "/list <rank>", "See the online players.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        final List<String> names = new ArrayList<>();
        final List<String> joined = new ArrayList<>();
        final List<Rank> ranks = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            names.add(player.getName());
            ranks.add(FOPMR_Rank.getRank(player));
        }
        sender.sendMessage(ChatColor.WHITE + "There are " + names.size() + "/" + Bukkit.getMaxPlayers() + " players online:\n");
        for (int i = 0; i < names.size(); i++)
        {
            String named = CUtils_Methods.colour(ranks.get(i).tag) + " " + ChatColor.GOLD + names.get(i);
            if (args.length != 0 && args[0].equalsIgnoreCase("-a"))
            {
                if (ranks.get(i) != Rank.OP && ranks.get(i) != Rank.IMPOSTER)
                {
                    joined.add(named);
                }
            }
            else if (args.length != 0 && args[0].equalsIgnoreCase("-i"))
            {
                if (ranks.get(i) == Rank.IMPOSTER)
                {
                    joined.add(named);
                }
            }
            else
            {
                joined.add(named);
            }
        }
        if (joined.size() != names.size())
        {
            sender.sendMessage(ChatColor.GREEN + "Showing all " + ChatColor.BLUE + joined.size() + ChatColor.GREEN + " players that fit the category selected.");
        }
        sender.sendMessage(StringUtils.join(joined, ", "));
        return true;
    }

}
