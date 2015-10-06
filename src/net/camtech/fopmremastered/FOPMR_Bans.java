package net.camtech.fopmremastered;

import java.sql.Connection;
import java.sql.PreparedStatement;
import static net.camtech.fopmremastered.FreedomOpModRemastered.config;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.ChatColor;

public class FOPMR_Bans {


    public static void addBan(final Player player, final String reason, String banner) {
        addBan(player, reason, banner, false);
    }

    public static void addBan(String name, String reason, String banner) {
        addBan(name, reason, banner, false);
    }

    public static void HandleLemonBan(final Player player, final String reason, String banner) {
        player.setGameMode(GameMode.SURVIVAL);
        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + "'s inventory was cleared and was set to survival by the power of lemons!");
        player.getInventory().clear();
        player.setVelocity(player.getVelocity().add(new Vector(0, 6, 0)));
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
                player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
                player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
                player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
                player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
                player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
                player.getWorld().strikeLightning(player.getLocation());
                player.getWorld().strikeLightning(player.getLocation());
                player.getWorld().strikeLightning(player.getLocation());
                player.getWorld().strikeLightning(player.getLocation());
                player.getWorld().strikeLightning(player.getLocation());
                player.getWorld().strikeLightning(player.getLocation());
                player.getWorld().strikeLightning(player.getLocation());
                player.chat("Noo! Sorry!");
                Bukkit.broadcastMessage(ChatColor.YELLOW + "No sorry's are allowed. Bye!");
                player.kickPlayer(reason);
            }
        }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 3L);
        player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
        player.getWorld().strikeLightning(player.getLocation());
        player.setHealth(0d);
        addLemonBan(player.getName(), reason, banner, true);
    }

    public static void addLemonBan(String name, String reason, String banner, boolean post) {
        if (post) {
            String message = name + " has been banned by the power of Lemons" + " with the reason: " + reason.split(" — ")[0] + " by " + banner + ".";
            FOPMR_RestManager.sendMessage(config.getInt("rest.banid"), message);
        }
        Connection c = FOPMR_DatabaseInterface.getConnection();
        try {
            PreparedStatement statement = c.prepareStatement("INSERT OR REPLACE INTO NAME_BANS (NAME, REASON, PERM) VALUES (?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, reason);
            statement.setBoolean(3, false);
            statement.executeUpdate();
            if (FOPMR_DatabaseInterface.getIpFromName(name) != null) {
                statement = c.prepareStatement("INSERT OR REPLACE INTO IP_BANS (IP, REASON, PERM) VALUES (?, ?, ?)");
                statement.setString(1, FOPMR_DatabaseInterface.getIpFromName(name));

                statement.setString(2, reason);
                statement.setBoolean(3, false);
                statement.executeUpdate();
            }
            if (FOPMR_DatabaseInterface.getUuidFromName(name) != null) {
                statement = c.prepareStatement("INSERT OR REPLACE INTO UUID_BANS (UUID, REASON, PERM) VALUES (?, ?, ?)");
                statement.setString(1, FOPMR_DatabaseInterface.getUuidFromName(name));
                statement.setString(2, reason);
                statement.setBoolean(3, false);
                statement.executeUpdate();
                c.commit();
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    public static void addBan(final Player player, final String reason, String banner, boolean post) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setVelocity(player.getVelocity().add(new Vector(0, 3, 0)));
        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(reason);
                player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
                player.getWorld().strikeLightning(player.getLocation());
            }
        }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 3L);
        player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
        player.getWorld().strikeLightning(player.getLocation());
        player.setHealth(0d);
        addBan(player.getName(), reason, banner, post);
    }

    public static void addBan(String name, String reason, String banner, boolean post) {
        if (post) {
            String message = name + " has been banned by " + banner + " with the reason: " + reason.split(" â€” ")[0] + ".";
            FOPMR_RestManager.sendMessage(config.getInt("rest.banid"), message);
        }

        Connection c = FOPMR_DatabaseInterface.getConnection();
        try {
            PreparedStatement statement = c.prepareStatement("INSERT OR REPLACE INTO NAME_BANS (NAME, REASON, PERM) VALUES (?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, reason);
            statement.setBoolean(3, false);
            statement.executeUpdate();
            if (FOPMR_DatabaseInterface.getIpFromName(name) != null) {
                statement = c.prepareStatement("INSERT OR REPLACE INTO IP_BANS (IP, REASON, PERM) VALUES (?, ?, ?)");
                statement.setString(1, FOPMR_DatabaseInterface.getIpFromName(name));

                statement.setString(2, reason);
                statement.setBoolean(3, false);
                statement.executeUpdate();
            }
            if (FOPMR_DatabaseInterface.getUuidFromName(name) != null) {
                statement = c.prepareStatement("INSERT OR REPLACE INTO UUID_BANS (UUID, REASON, PERM) VALUES (?, ?, ?)");
                statement.setString(1, FOPMR_DatabaseInterface.getUuidFromName(name));
                statement.setString(2, reason);
                statement.setBoolean(3, false);
                statement.executeUpdate();
                c.commit();
            }

        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    public static void unBan(Player player) {
        unBan(player.getName());
    }

    public static void unBan(String name) {
        Connection c = FOPMR_DatabaseInterface.getConnection();
        try {
            PreparedStatement statement = c.prepareStatement("DELETE FROM NAME_BANS WHERE NAME = ? AND PERM = 0");
            statement.setString(1, name);
            statement.executeUpdate();
            c.commit();
            statement = c.prepareStatement("DELETE FROM IP_BANS WHERE IP = ? AND PERM = 0");
            statement.setString(1, FOPMR_DatabaseInterface.getIpFromName(name));
            statement.executeUpdate();
            c.commit();
            statement = c.prepareStatement("DELETE FROM UUID_BANS WHERE UUID = ? AND PERM = 0");
            statement.setString(1, FOPMR_DatabaseInterface.getUuidFromName(name));
            statement.executeUpdate();
            c.commit();
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    public static boolean isBanned(Player player) {
        return isBanned(player.getName(), player.getAddress().getHostName());
    }

    public static boolean isBanned(String name) {
        try {
            return isBanned(name, FOPMR_DatabaseInterface.getIpFromName(name));
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return false;
    }

    public static boolean isBanned(String name, String ip) {
        try {
            if (FOPMR_DatabaseInterface.getFromTable("NAME", name, "NAME", "NAME_BANS") != null) {
                return true;
            }

            if (FOPMR_DatabaseInterface.getFromTable("IP", ip, "IP", "IP_BANS") != null) {
                return true;
            }

            if (FOPMR_DatabaseInterface.getFromTable("UUID", FOPMR_DatabaseInterface.getUuidFromName(name), "UUID", "UUID_BANS") != null) {
                return true;
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return false;
    }

    public static String getReason(String name, String ip) {

        if (!isBanned(name, ip)) {
            return "Player is not banned.";
        }
        try {
            if (FOPMR_DatabaseInterface.getFromTable("NAME", name, "NAME", "NAME_BANS") != null) {
                return (String) FOPMR_DatabaseInterface.getFromTable("NAME", name, "REASON", "NAME_BANS");
            }
            if (FOPMR_DatabaseInterface.getFromTable("IP", ip, "IP", "IP_BANS") != null) {
                return (String) FOPMR_DatabaseInterface.getFromTable("IP", ip, "REASON", "IP_BANS");
            }
            if (FOPMR_DatabaseInterface.getFromTable("UUID", Bukkit.getOfflinePlayer(name).getUniqueId().toString(), "UUID", "UUID_BANS") != null) {
                return (String) FOPMR_DatabaseInterface.getFromTable("UUID", Bukkit.getOfflinePlayer(name).getUniqueId().toString(), "REASON", "UUID_BANS");
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
            return "An exception occurred...";
        }
        return "Player is not banned.";
    }

    public static String getReason(String name) {
        try {
            return getReason(name, FOPMR_DatabaseInterface.getIpFromName(name));
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
            return "An exception occurred";
        }
    }
}
