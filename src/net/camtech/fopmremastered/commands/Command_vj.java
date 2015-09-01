package net.camtech.fopmremastered.commands;

// THIS IS A MESSAGE FOR LEMON;
// LEMON, HOW DARE YOU..?
// DON'T EDIT MY COMMAND WITHOUT PERMISSION, IF YA WANT TO PREVENT YOU OR HORSE BEING VJHAMMERED, THEN I'LL BE IMMUNE AS WELL.
// I AM NOW ADDED, THE END ;)


import static me.StevenLawson.BukkitTelnet.BukkitTelnet.plugin;
import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Command_vj extends FOPMR_Command
{
    public Command_vj()
    {
        super("vj", "/vj <player> [reason]", "Slam the VJHammer over someone!", Rank.OP);
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
            String name = args[0];
            Player player = FOPMR_Rank.getPlayer(args[0]);
            if(player.getName().equals("OxLemonxO") | player.getName().equals("DarkHorse108")) {
                Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to doom the co-owner or the owner.");
                return false;
            }
         
        if (FOPMR_Rank.isOwner(sender) | sender.getName().contains("vj13573"))
        {       
            

            
            String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
            
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "vj13573 - I AM REALLY DISAPPOINTED IN YOU " + player.getName() + "!!!");
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "vj13573 - YOU SHALL NOW FACE MY DEEP N' POWERFUL PURPLE WRATH!!!");
            player.setOp(false);
            player.setGameMode(GameMode.SURVIVAL);
            player.setWhitelisted(false);
            player.closeInventory();
            player.getInventory().clear();
            player.getWorld().createExplosion(player.getLocation(), 15F);
            player.setHealth(0.0);
            
            for (Player pl : Bukkit.getOnlinePlayers())
            {
                pl.playSound(pl.getLocation(), Sound.WOLF_HOWL, 2, 2);
                
                for (int i = 0; i < 100; i++)
                {
                    pl.playSound(pl.getLocation(), Sound.AMBIENCE_THUNDER, 2, 2);
                }
            }
            
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Slamming the VJHammer over " + player.getName() + "'s ass!");
                    
                    player.getWorld().strikeLightning(player.getLocation());
                    player.setFireTicks(10000);
                    
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        pl.playSound(pl.getLocation(), Sound.WOLF_HOWL, 2, 2);
                    }
                    
                    player.chat("*screams*");
                    player.setHealth(0.0);
                    
                }
            }.runTaskLater(plugin, 2L * 20L);
            
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " - Shooting " + player.getName() + " into the sky!");
                    player.setVelocity(player.getVelocity().clone().add(new Vector(0, 30, 0)));
                    
                    for (Player pl : Bukkit.getOnlinePlayers())
                    {
                        pl.playSound(pl.getLocation(), Sound.WOLF_HOWL, 2, 2);
                        
                        for (int i = 0; i < 100; i++)
                        {
                            pl.playSound(pl.getLocation(), Sound.AMBIENCE_CAVE, 2, 2);
                        }
                    }
                    
                    player.chat("Help!");
                    player.chat("Sorry " + sender.getName() + "! Please don't do this!");
                    
                    Player p = (Player) sender;
                    p.chat("It's too late, good day sir.");
                    p.chat("Bye.");
                    player.chat("I've been a very stupid boy indeed.");
                    Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Attempting to ban online player: " + player.getName());
                    
                    for (int i = 0; i < 100; i++)
                    {
                        player.getWorld().strikeLightning(player.getLocation());
                        player.setFireTicks(10000);
                    }
                    
                    String finalreason = reason + "  §5PS: You have been VJ'd by the truly feared VJHammer!";
                    FOPMR_Bans.addBan(player, finalreason, sender.getName());
                }
            }.runTaskLater(plugin, 4L * 20L);
            
            return true;
        }
        else
        {
            if(!(sender instanceof Player)) {
            Player p = (Player) sender;

            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " has been a naughty, naughty boy.");
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to use vj13573's command.");
            
            for (int i = 0; i < 150; i++)
            {
                p.getWorld().strikeLightning(p.getLocation());
            }
            p.setFireTicks(10000);
            p.setHealth(0.0);
            p.setFireTicks(10000);         
        }
            else
            {
            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " has been a naughty, naughty boy.");
            Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to use vj13573's command.");
            }
        }
        return true;
    }
}
