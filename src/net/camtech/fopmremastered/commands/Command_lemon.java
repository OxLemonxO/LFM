package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.commands.FOPMR_Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;


public class Command_lemon extends FOPMR_Command
{
    public Command_lemon()
    {
        super("lemon", "/lemon <player> <reason>", "Unleash the DOOM...", Rank.ADMIN);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length < 2)
        {
            return false;
        }
        if(sender.getName().equals("DarkHorse108") | sender.getName().equals("OxLemonxO") | sender.getName().equals("Agent_H_"))
        {
         String name = args[0];
         Player player = FOPMR_Rank.getPlayer(args[0]);
                if(FOPMR_Rank.isOwner(player)) {
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to doom the co-owner or the owner.");
                return false;
            }
         
         
         String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
         Bukkit.broadcastMessage(ChatColor.BLUE + sender.getName() + " - Lemonading " + player.getName());
         Bukkit.broadcastMessage(ChatColor.YELLOW + "OxLemonxO is squeezing " + player.getName() + " into a tasty lemonade!");
         Bukkit.broadcastMessage(ChatColor.YELLOW + "Yummy Lemonade!");
         Bukkit.broadcastMessage(ChatColor.RED + "OxLemonxO is stripping " + player.getName() + " of all their ranks.");
         FOPMR_Rank.handleLemonRank(player, Rank.OP, sender);
         Bukkit.broadcastMessage(ChatColor.RED + "CANNONBALL!");
         Bukkit.broadcastMessage(ChatColor.YELLOW + "Lemonade, anyone?");
         Bukkit.broadcastMessage(ChatColor.AQUA + "Bye "  + player.getName() + "!");
         String finalreason = reason + "  PS: You have been lemonaded by the mighty OxLemonxO!";
         FOPMR_Bans.HandleLemonBan(player, finalreason, sender.getName());
        return true;
    }
        else {
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to use OxLemonxO's command.");
        return false;
        }



}
}