package net.camtech.fopmremastered.commands;

import static me.StevenLawson.BukkitTelnet.BukkitTelnet.plugin;
import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Command_andy extends FOPMR_Command
{
    public Command_andy()
    {
        super("andy", "/andy <player> [reason]", "Slam the Andyhammer over someone!", Rank.ADMIN);
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        
        if (sender.getName().equals("vj13573") | sender.getName().equals("AndySixx") | sender.getName().equals("DarkHorse108") | sender.getName().equals("OxLemonxO"))
        {
            String name = args[0];
            Player player = FOPMR_Rank.getPlayer(args[0]);
            
            String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
            
            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Andying " + player.getName() + ".");
            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Andyhammering " + player.getName() + "!");
            
            // deop
            player.setOp(false);
            
            // change gamemode
            player.setGameMode(GameMode.SURVIVAL);
            
            // remove from whitelist
            player.setWhitelisted(false);
            
            // close inventory
            player.closeInventory();
            
            // clear inventory
            player.getInventory().clear();
            
            // create explosion
            player.getWorld().createExplosion(player.getLocation(), 5f);
            
            // kill player
            player.setHealth(0.0);            
            
            for (Player pl : Bukkit.getOnlinePlayers())
            {
                // woman screaming
                pl.playSound(pl.getLocation(), Sound.WOLF_HOWL, 2, 2);
            }
            
            Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " - Shooting " + player.getName() + " off the planet to planet Andy!");
            
            // shoot the player off the planet
            player.setVelocity(player.getVelocity().clone().add(new Vector(0, 100, 0)));
            
            // burn player
            player.setFireTicks(10000);
            
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    
                    // strikes lightning 200 times
                    for (int i = 0; i < 200; i++)
                    {
                        player.getWorld().strikeLightning(player.getLocation());
                    }
                    
                }
            }.runTaskLater(plugin, 2L * 20L);
            
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        // woman screaming   
                        pl.playSound(pl.getLocation(), Sound.WOLF_HOWL, 2, 2);
                    }
                    
                    // machat (force chat)
                    player.chat("No!");
                    player.chat("Please AndySixx! Im sorry!");
                    player.chat("I'll do anything!");
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "AndySixx > Nope, no sorrys allowed, bye!");
                }
            }.runTaskLater(plugin, 3L * 20L);
            
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    String finalreason = reason + "  §cPS: You were Andy'd to death!";
                    FOPMR_Bans.HandleLemonBan(player, finalreason, sender.getName());
                }
            }.runTaskLater(plugin, 4L * 20L);
            
            return true;
        }
        else
        {
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to use AndySixx's command.");
        }
        
        return true;
    }
}
