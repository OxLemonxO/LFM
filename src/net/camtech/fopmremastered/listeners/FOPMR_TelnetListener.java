package net.camtech.fopmremastered.listeners;

import me.StevenLawson.BukkitTelnet.api.TelnetCommandEvent;
import me.StevenLawson.BukkitTelnet.api.TelnetPreLoginEvent;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FOPMR_TelnetListener implements Listener
{
    public FOPMR_TelnetListener()
    {
        if (!Bukkit.getPluginManager().isPluginEnabled("BukkitTelnet"))
        {
            Bukkit.broadcastMessage(ChatColor.RED + "BukkitTelnet cannot be found, disabling integration.");
            return;
        }
        Bukkit.getPluginManager().registerEvents(this, FreedomOpModRemastered.plugin);
    }

    @EventHandler
    public void onTelnetPreLoginEvent(TelnetPreLoginEvent event)
    {
        String ip = event.getIp();
        if (FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRankFromIp(ip), FOPMR_Rank.Rank.SUPER))
        {
            event.setBypassPassword(true);
            event.setName("[" + FOPMR_Rank.getNameFromIp(ip) + "]");
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + FOPMR_Rank.getNameFromIp(ip) + " logged in via telnet.");
            for(Player player : Bukkit.getOnlinePlayers())
            {
                if(FOPMR_Rank.isExecutive(player))
                {
                    player.sendMessage(ChatColor.DARK_GREEN + FOPMR_Rank.getNameFromIp(ip) + " is on the IP of " + ip + ".");
                }
            }
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTelnetCommand(TelnetCommandEvent event)
    {
        CommandSender player = event.getSender();
        FileConfiguration commands = FreedomOpModRemastered.configs.getCommands().getConfig();
        for (String blocked : commands.getConfigurationSection("").getKeys(false))
        {
            if (blocked.equalsIgnoreCase(event.getCommand().replaceAll("/", "")))
            {
                if (!FOPMR_Rank.isRank(player, commands.getInt(blocked + ".rank")))
                {
                    player.sendMessage(ChatColor.RED + "You are not authorised to use this command.");
                    event.setCancelled(true);
                }
            }
        }
        for (Player player2 : Bukkit.getOnlinePlayers())
        {
            if (FOPMR_Rank.isSpecialist(player2))
            {
                player2.sendMessage(ChatColor.GRAY + ChatColor.ITALIC.toString() + player.getName() + ": " + event.getCommand().toLowerCase());
            }
        }
    }
}
