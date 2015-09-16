package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Command_rage extends FOPMR_Command
{
    public Command_rage()
    {
        super("rage", "/rage [-f]", "Explode with rage!", Rank.SUPER);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Player player = (Player) sender;
        
        if (!args[0].equalIgnoreCase("-f"))
        {
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has exploded with rage!");
            player.getInventory().clear();
            player.getWorld().createExplosion(player.getLocation(), 5f);
            //removed because of lag
            //player.getInventory().addItem(new ItemStack(Material.INK_SACK, 10000, (short) 1));
            player.setHealth(0.0);
            return true;
        }     
        
        else if (args[0].equalsIgnoreCase("-f"))
        {
            Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has exploded with " + ChatColor.DARK_RED + "fiery " + ChatColor.RED + "rage!");
            player.getInventory().clear();
            player.getWorld().createExplosion(player.getLocation(), 5f);
            //removed for lag
           // player.getInventory().addItem(new ItemStack(Material.INK_SACK, 10000, (short) 1));
            //player.performCommand("/sphere 51 10");
            player.setHealth(0.0);
            return true;
        }
        
        return true;
    }
}
