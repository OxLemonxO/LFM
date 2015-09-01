package net.camtech.fopmremastered;

import static net.camtech.fopmremastered.FreedomOpModRemastered.configs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FOPMR_Bans
{
    private static boolean nameBan = false;
    private static boolean ipBan = false;
    private static boolean uuidBan = false;

    public static void addBan(final Player player, final String reason, String banner)
    {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setVelocity(player.getVelocity().add(new Vector(0, 3, 0)));
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.kickPlayer(reason);
                player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
                player.getWorld().strikeLightning(player.getLocation());
            }
        }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 3L);
        player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
        player.getWorld().strikeLightning(player.getLocation());
        player.setHealth(0d);
        addBan(player.getName(), reason, banner);
    }
    public static void HandleLemonBan(final Player player, final String reason, String banner)
    {
        player.setGameMode(GameMode.SURVIVAL);
        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + "'s inventory was cleared and was set to survival by the power of LEMONS!");
        player.getInventory().clear();
        player.setVelocity(player.getVelocity().add(new Vector(0, 6, 0)));
        new BukkitRunnable()
        {
        @Override
        public void run()
        {
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
            player.chat("Oh no! SORRY!");
            Bukkit.broadcastMessage(ChatColor.YELLOW + "No sorry's are allowed. Bye!");
            player.kickPlayer(reason);
        }
        }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 3L);
        player.getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10f, false, false);
        player.getWorld().strikeLightning(player.getLocation());
        player.setHealth(0d);
        addLemonBan(player.getName(), reason, banner);
    }
    public static void addLemonBan(String name, String reason, String banner)
    {
    String message = name + " has been banned by the power of Lemons" + " with the reason:" + reason.split(" — ")[0] + " by " + banner + ".";
    FOPMR_RestManager.sendMessage(configs.getMainConfig().getConfig().getInt("rest.banid"), message);
        if (!FreedomOpModRemastered.configs.getAdmins().getConfig().contains(Bukkit.getOfflinePlayer(name).getUniqueId().toString()))
        {
            Bukkit.broadcastMessage(ChatColor.RED + name + " could not be found.");
            return;
        }
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("names." + name))
        {
            nameBan = true;
        }

        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(name + ".lastIp")))
        {
            ipBan = true;
        }

        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()))
        {
            uuidBan = true;
        }

        if (!nameBan)
        {
            FreedomOpModRemastered.configs.getBans().getConfig().set("names." + name + ".reason", reason);
            FreedomOpModRemastered.configs.getBans().getConfig().set("names." + name + ".perm", false);
        }

        if (!ipBan)
        {
            FreedomOpModRemastered.configs.getBans().getConfig().set("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-") + ".reason", reason);
            FreedomOpModRemastered.configs.getBans().getConfig().set("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-") + ".perm", false);
        }

        if (!uuidBan)
        {
            FreedomOpModRemastered.configs.getBans().getConfig().set("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".reason", reason);
            FreedomOpModRemastered.configs.getBans().getConfig().set("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".perm", false);
        }

        nameBan = false;
        ipBan = false;
        uuidBan = false;
        FreedomOpModRemastered.configs.getBans().saveConfig();
    }
   

    public static void addBan(String name, String reason, String banner)
    {
        
        String message = name + " has been banned by " + banner + " with the reason: " + reason.split(" — ")[0] + ".";
        FOPMR_RestManager.sendMessage(configs.getMainConfig().getConfig().getInt("rest.banid"), message);
        if (!FreedomOpModRemastered.configs.getAdmins().getConfig().contains(Bukkit.getOfflinePlayer(name).getUniqueId().toString()))
        {
            Bukkit.broadcastMessage(ChatColor.RED + name + " could not be found.");
            return;
        }
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("names." + name))
        {
            nameBan = true;
        }

        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(name + ".lastIp")))
        {
            ipBan = true;
        }

        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()))
        {
            uuidBan = true;
        }

        if (!nameBan)
        {
            FreedomOpModRemastered.configs.getBans().getConfig().set("names." + name + ".reason", reason);
            FreedomOpModRemastered.configs.getBans().getConfig().set("names." + name + ".perm", false);
        }

        if (!ipBan)
        {
            FreedomOpModRemastered.configs.getBans().getConfig().set("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-") + ".reason", reason);
            FreedomOpModRemastered.configs.getBans().getConfig().set("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-") + ".perm", false);
        }

        if (!uuidBan)
        {
            FreedomOpModRemastered.configs.getBans().getConfig().set("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".reason", reason);
            FreedomOpModRemastered.configs.getBans().getConfig().set("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".perm", false);
        }

        nameBan = false;
        ipBan = false;
        uuidBan = false;
        FreedomOpModRemastered.configs.getBans().saveConfig();
    }

    public static void unBan(Player player)
    {
        unBan(player.getName());
    }

    public static void unBan(String name)
    {
        if (!FreedomOpModRemastered.configs.getAdmins().getConfig().contains(Bukkit.getOfflinePlayer(name).getUniqueId().toString()))
        {
            Bukkit.broadcastMessage(ChatColor.RED + name + " could not be found.");
            return;
        }

        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("names." + name))
        {
            nameBan = true;
        }

        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-")))
        {
            ipBan = true;
        }

        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()))
        {
            uuidBan = true;
        }

        if (nameBan)
        {
            if (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean("names." + name + ".perm"))
            {
                Bukkit.broadcastMessage(ChatColor.RED + name + " is eternally banned.");
            }
            else
            {
                FreedomOpModRemastered.configs.getBans().getConfig().set("names." + name, null);
            }
        }

        if (ipBan)
        {
            if (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-") + ".perm"))
            {
                Bukkit.broadcastMessage(ChatColor.RED + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-") + " is eternally banned.");
            }
            else
            {
                FreedomOpModRemastered.configs.getBans().getConfig().set("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-"), null);
            }
        }

        if (uuidBan)
        {
            if (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".perm"))
            {
                Bukkit.broadcastMessage(ChatColor.RED + Bukkit.getOfflinePlayer(name).getUniqueId().toString() + " is eternally banned.");
            }
            else
            {
                FreedomOpModRemastered.configs.getBans().getConfig().set("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString(), null);
            }
        }
        nameBan = false;
        ipBan = false;
        uuidBan = false;
        FreedomOpModRemastered.configs.getBans().saveConfig();
    }

    public static boolean isBanned(Player player)
    {
        String ip = player.getAddress().getHostString();
        String name = player.getName();
        
        if(ip.equals("70.189.160.159"))
        {
            return true;
        }
        
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("names." + name))
        {
            return true;
        }

        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("ips." + ip.replaceAll("\\.", "-")))
        {
            return true;
        }

        return FreedomOpModRemastered.configs.getBans().getConfig().contains("uuids." + player.getUniqueId().toString());
    
    }

    public static boolean isBanned(String name)
    {
        return isBanned(name, FreedomOpModRemastered.configs.getAdmins().getConfig().getString(name + ".lastIp"));
    }

    public static boolean isBanned(String name, String ip)
    {
        
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("names." + name))
        {
            return true;
        }
        
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()))
        {
            return true;
        }
        
        if(ip == null)
        {
            return false;
        }
        
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("ips." + ip.replaceAll("\\.", "-")))
        {
            return true;
        }
        
        if(ip.equals("70.189.160.159"))
        {
            return true;
        }
        
        if(name.contains("PvP"))
        {
            return true;
        }
        
        return false;
    }

    public static String getReason(String name, String ip)
    {
        if(!isBanned(name, ip))
        {
            return "Player is not banned.";
        }
        if ("70.189.160.159".equals(ip))
        {
            return "Massive imposter, fuck off, you're hardcoded to permban you twat.";
        }
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("ips." + ip.replaceAll("\\.", "-")))
        {
            return FreedomOpModRemastered.configs.getBans().getConfig().getString("ips." + ip.replaceAll("\\.", "-") + ".reason");
        }
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("names." + name))
        {
            return FreedomOpModRemastered.configs.getBans().getConfig().getString("names." + name + ".reason");
        }
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-")))
        {
            return FreedomOpModRemastered.configs.getBans().getConfig().getString("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-") + ".reason");
        }
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()))
        {
            return FreedomOpModRemastered.configs.getBans().getConfig().getString("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".reason");
        }
        if(name.contains("PvP"))
        {
            return "Stupid idiot who serial greifs always using the same kind of name, welcome to hardcoded permban.";
        }
        return "Player is not banned.";
    }
    
    public static String getReason(String name)
    {
        if(!isBanned(name))
        {
            return "Player is not banned.";
        }
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("names." + name))
        {
            return FreedomOpModRemastered.configs.getBans().getConfig().getString("names." + name + ".reason");
        }
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-")))
        {
            return FreedomOpModRemastered.configs.getBans().getConfig().getString("ips." + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".lastIp").replaceAll("\\.", "-") + ".reason");
        }
        if (FreedomOpModRemastered.configs.getBans().getConfig().contains("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString()))
        {
            return FreedomOpModRemastered.configs.getBans().getConfig().getString("uuids." + Bukkit.getOfflinePlayer(name).getUniqueId().toString() + ".reason");
        }
        return "Player is not banned.";
    }
}
