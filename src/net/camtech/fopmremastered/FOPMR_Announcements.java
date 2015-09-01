package net.camtech.fopmremastered;

import net.camtech.camutils.CUtils_Methods;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class FOPMR_Announcements
{

    public static void setup()
    {
        System.out.println("Announcements Loaded.");
        FreedomOpModRemastered.configs.getAnnouncements().getConfig().getKeys(false).stream().forEach((announce) ->
        {
            announce(FreedomOpModRemastered.configs.getAnnouncements().getConfig().getString(announce + ".message"), FreedomOpModRemastered.configs.getAnnouncements().getConfig().getInt(announce + ".time"));
        });
    }

    private static void announce(String message, long delay)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.broadcastMessage(CUtils_Methods.colour(message));
                announce(message, delay);
            }
        }.runTaskLater(FreedomOpModRemastered.plugin, delay * 20);
    }
}
