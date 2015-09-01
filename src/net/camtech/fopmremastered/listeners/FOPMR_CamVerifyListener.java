package net.camtech.fopmremastered.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import net.camtech.verification.CamVerifyEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class FOPMR_CamVerifyListener implements Listener
{

    public FOPMR_CamVerifyListener()
    {
        if (!Bukkit.getPluginManager().isPluginEnabled("CamVerify"))
        {
            Bukkit.broadcastMessage(ChatColor.RED + "CamVerify cannot be found, disabling integration.");
            return;
        }
        Bukkit.getPluginManager().registerEvents(this, FreedomOpModRemastered.plugin);
    }

    public static void close(final PrintWriter out, final BufferedReader in)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    out.flush();
                    out.close();
                    in.close();
                } catch (IOException ex)
                {
                    Logger.getLogger(FOPMR_CamVerifyListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 2L);

    }

    @EventHandler
    public void onVerify(CamVerifyEvent event)
    {
        final BufferedReader in = event.getIn();
        final PrintWriter out = event.getOut();
        String ip = event.getIp();
        String name;
        try
        {
            if (!"5.135.233.93".equalsIgnoreCase(ip))
            {
                out.println("You are the wrong host, you are " + ip + " not 5.135.233.93");
                close(out, in);
                return;
            }
            name = in.readLine();
            Player player = Bukkit.getPlayer(name);
            if (player == null)
            {
                out.println(name + " is not a valid player.");
                close(out, in);
                return;
            }
            if (FOPMR_Rank.getRank(player) == FOPMR_Rank.Rank.IMPOSTER)
            {
                FOPMR_Rank.unImposter(player);
                out.println(name + " has been successfully verified.");
                close(out, in);
                Bukkit.broadcastMessage(ChatColor.AQUA + name + " has been verified using CamVerify!");
                return;
            }
            out.println(name + " is not an imposter.");
            close(out, in);
        } catch (IOException ex)
        {
            Logger.getLogger(FOPMR_CamVerifyListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
