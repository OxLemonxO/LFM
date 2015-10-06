package net.camtech.fopmremastered.listeners;

import java.util.ArrayList;
import me.StevenLawson.worldedit.LimitChangedEvent;
import me.StevenLawson.worldedit.SelectionChangedEvent;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import net.camtech.fopmremastered.protectedareas.FOPMR_ProtectedArea;
import net.camtech.fopmremastered.protectedareas.FOPMR_ProtectedAreas;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

//MASSIVE CREDIT TO TOTALFREEDOM FOR THIS!
public final class FOPMR_WorldEditListener implements Listener {

    public FOPMR_WorldEditListener() {
        init();
    }

    public void init() {
        Bukkit.getPluginManager().registerEvents(this, FreedomOpModRemastered.plugin);
    }

    @EventHandler
    public void onSelectionChange(final SelectionChangedEvent event) {
        final Player player = event.getPlayer();
        ArrayList<FOPMR_ProtectedArea> areas = FOPMR_ProtectedAreas.areasIn(event.getMinVector(), event.getMaxVector(), event.getWorld().getName());
        if (!areas.isEmpty()) {
            for (FOPMR_ProtectedArea area : areas) {
                if (!area.canAccess(player)) {
                    player.sendMessage(ChatColor.RED + "The region that you selected contained a protected area which you do not have access to. Selection cleared.");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onLimitChanged(LimitChangedEvent event) {
        final Player player = event.getPlayer();

        if (FOPMR_Rank.isAdmin(player)) {
            return;
        }

        if (!event.getPlayer().equals(event.getTarget())) {
            player.sendMessage(ChatColor.RED + "Only admins can change the limit for other players!");
            event.setCancelled(true);
        }

        if (event.getLimit() < 0 || event.getLimit() > 50000) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot set your limit higher than 50000 or to -1!");
        }
    }
}
