package net.camtech.fopmremastered;

import java.sql.ResultSet;
import net.camtech.camutils.CUtils_Methods;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class FOPMR_Announcements
{

    public static void setup()
    {
        System.out.println("Announcements Loading.");
        try
        {
            ResultSet set = FOPMR_DatabaseInterface.getAllResults("", null, "ANNOUNCEMENTS");
            while(set.next())
            {
                Object message = set.getObject("MESSAGE");
                Object interval = set.getObject("INTERVAL");
                if(message instanceof String && interval instanceof Integer)
                {
                    announce((String) message, (Integer) interval);
                }
            }
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    private static void announce(final String message, final long interval)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.broadcastMessage(CUtils_Methods.colour(message));
            }
        }.runTaskTimerAsynchronously(FreedomOpModRemastered.plugin, 0, interval * 20);
        
    }
}