package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command_insult extends FOPMR_Command
{
    public Command_insult()
    {
        super("insult", "/insult <player>", "Insults the player. Brick Tamland Style. Used against trolls", Rank.SENIOR);     
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }
        
        Player player = FOPMR_Rank.getPlayer(args[0]);
        
        if (player == null)
        {
            sender.sendMessage(ChatColor.GRAY + "Player not found!");
            return true;
        }
        
        player.setOp(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFireTicks(10000);
        for (int i = 0; i < 100; i++)
        {
            player.getWorld().strikeLightning(player.getLocation());
        }
        player.setHealth(0.0);
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Hey " + player.getName() + ", where did you get those clothes from?? The..toilet...store??");
        for (int i = 0; i < 100; i++)
        {
            player.getWorld().strikeLightning(player.getLocation());
        }
        Bukkit.broadcastMessage(ChatColor.WHITE + player.getName() + " died.");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + player.getName() + " smells like a big bunch of faggots.");
        for (int i = 0; i < 100; i++)
        {
            player.getWorld().strikeLightning(player.getLocation());
        }
        Bukkit.broadcastMessage(ChatColor.WHITE + player.getName() + " died.");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Hey " + player.getName() + ", if you're from africa, why are you white??");
        for (int i = 0; i < 100; i++)
        {
            player.getWorld().strikeLightning(player.getLocation());
        }
        Bukkit.broadcastMessage(ChatColor.WHITE + player.getName() + " died.");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Oh, im sorry " + player.getName() + " did my hand wrist slip and hit you?");
        for (int i = 0; i < 100; i++)
        {
            player.getWorld().strikeLightning(player.getLocation());
        }
        Bukkit.broadcastMessage(ChatColor.WHITE + player.getName() + " died.");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "Hey " + player.getName() + ", did you know we've had vicious noobs and idiot noobs?");
        for (int i = 0; i < 100; i++)
        {
            player.getWorld().strikeLightning(player.getLocation());
        }
        Bukkit.broadcastMessage(ChatColor.WHITE + player.getName() + " died.");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "But we've never had vicious idiot noobs like you.");
        
        return true;
    }
}
