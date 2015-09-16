package net.camtech.fopmremastered;

import net.camtech.camutils.CUtils_Methods;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class FOPMR_Announcements
{

    public static void setup()
    {
        System.out.println("Announcements Loaded.");
        for(String announce : FreedomOpModRemastered.configs.getAnnouncements().getConfig().getKeys(false))
        {
            announce(FreedomOpModRemastered.configs.getAnnouncements().getConfig().getString(announce + ".message"), FreedomOpModRemastered.configs.getAnnouncements().getConfig().getInt(announce + ".time"));
        }
    }

    private static void announce(final String message, final long delay)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.broadcastMessage(CUtils_Methods.colour(message));
                announce(message, delay);
            }
        }.runTaskLaterAsynchronously(FreedomOpModRemastered.plugin, delay * 20);
    }
}