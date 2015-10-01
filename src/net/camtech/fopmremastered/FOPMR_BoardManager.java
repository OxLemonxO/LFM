package net.camtech.fopmremastered;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class FOPMR_BoardManager
{

    public static ScoreboardManager manager = Bukkit.getScoreboardManager();

    public static void updateStats(Player player)
    {
        try
        {
            Scoreboard board = manager.getNewScoreboard();
            String name = player.getName();
            Objective o = board.getObjective("stats");
            if(o == null)
            {
                o = board.registerNewObjective("stats", "dummy");
                o.setDisplaySlot(DisplaySlot.SIDEBAR);
                o.setDisplayName(ChatColor.GOLD + "" + ChatColor.MAGIC + "|@|" + ChatColor.BLUE + "Your Info" + ChatColor.GOLD + "" + ChatColor.MAGIC + "|@|");
            }
            String builderstring = "No";
            String djumpstring = "No";
            if(FOPMR_Rank.isMasterBuilder(player))
            {
                builderstring = "Yes";
            }
            if(FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "DOUBLEJUMP", "PLAYERS"))
            {
                djumpstring = "Yes";
            }
            Score rankhead = o.getScore(ChatColor.GOLD + "Your rank: ");
            Score rank = o.getScore(ChatColor.GOLD + " " + FOPMR_Rank.getRank(player).name.split(" ")[0]);
            Score level = o.getScore(ChatColor.GOLD + "Clr Level: " + ChatColor.RED + FOPMR_Rank.getRank(player).level);
            Score chat = o.getScore(ChatColor.GOLD + "Chat Level: " + ChatColor.RED + (Integer) FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "CHATLEVEL", "PLAYERS"));
            Score builder = o.getScore(ChatColor.GOLD + "Builder: " + ChatColor.RED + builderstring);
            Score djump = o.getScore(ChatColor.GOLD + "D. Jump: " + ChatColor.RED + djumpstring);

            rankhead.setScore(6);
            rank.setScore(5);
            level.setScore(4);
            chat.setScore(3);
            builder.setScore(2);
            djump.setScore(1);

            player.setScoreboard(board);
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }
}