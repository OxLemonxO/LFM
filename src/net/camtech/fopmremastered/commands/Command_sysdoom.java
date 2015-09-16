package net.camtech.fopmremastered.commands;
 
import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import static net.camtech.fopmremastered.FreedomOpModRemastered.plugin;
import net.camtech.fopmremastered.commands.FOPMR_Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
 
 
public class Command_sysdoom extends FOPMR_Command
{
    public Command_sysdoom()
    {
        super("sysdoom", "/sysdoom <player> <reason>", "Sysdoom!", Rank.OP);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length < 1)
        {
            return false;
        }
        if (FOPMR_Rank.isSystem(sender))
        {
         String name = args[0];
         Player player = FOPMR_Rank.getPlayer(args[0]);
         
           
             if(player.getName().equals("OxLemonxO") | player.getName().equals("DarkHorse108")) {
             Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to sysdoom the co-owner or the owner.");
             return true;
            }
           else if (FOPMR_Rank.isSystem(player))
         {
             sender.sendMessage(ChatColor.DARK_RED + "You cannot sysdoom this user!");
             return true;
         }
         
         String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
         player.setGameMode(GameMode.SURVIVAL);
         player.setFireTicks(10000);
         player.setVelocity(player.getVelocity().clone().add(new Vector(0, 50, 0)));
         Bukkit.broadcastMessage(ChatColor.BLUE + sender.getName() + " - Systemdooming " + player.getName());
         Bukkit.broadcastMessage(ChatColor.YELLOW + sender.getName() + " - Systemdooming " + player.getName() + " into a never-ending oblivion.");
         Bukkit.broadcastMessage(ChatColor.DARK_RED + sender.getName() + " - Stupid rank of " + player.getName() + "'s, go away. Your rank is now gone, forever most likely.");
         FOPMR_Rank.setRank(player, Rank.OP, sender);
         Bukkit.broadcastMessage(ChatColor.BLUE + sender.getName() + " - Bye "  + player.getName() + "!");
         
         new BukkitRunnable()
         {
             @Override
             public void run()
             {
                 Bukkit.broadcastMessage(ChatColor.RED + "Up for some explosions, anyone?");
                 player.getWorld().createExplosion(player.getLocation(), 10F, true);
                 
                 for (int i = 0; i < 100; i++)
                 {
                     player.getWorld().strikeLightning(player.getLocation());
                 }              
                 player.setFireTicks(10000);
                 player.setHealth(0.0);
             }
         }.runTaskLater(plugin, 2L * 20L);
         
         new BukkitRunnable()
         {
             @Override
             public void run()
             {
                 String finalreason = reason + "  PS: You are banned for 24-48 hours. You might be banned forever- theres'a chance. If you are banned forever - appeal at http://lemonfreedom.boards.net";
                 FOPMR_Bans.addBan(player, finalreason, sender.getName());
             }
         }.runTaskLater(plugin, 4L * 20L);
         
         return true;
        }
        
        else {
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to use a system administration command.");
        return false;
        }
 
        

 
    }
}
