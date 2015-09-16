package net.camtech.fopmremastered;

import java.util.function.Function;
import net.camtech.camutils.CUtils_Methods;
import static net.camtech.fopmremastered.FreedomOpModRemastered.configs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FOPMR_Rank
{
  public static final Function<Player, Boolean> ADMIN_SERVICE = new Function<Player, Boolean>()
    {
        @Override
        public Boolean apply(Player f)
        {
            return isAdmin(f);
        }
    };

    public static Rank getRank(CommandSender player)
    {
        if(!(player instanceof Player))
        {
            if("Console".equalsIgnoreCase(player.getName()))
            {
                return Rank.CONSOLE;
            }
            else
            {
                FileConfiguration config = FreedomOpModRemastered.configs.getAdmins().getConfig();
                OfflinePlayer offplayer = Bukkit.getOfflinePlayer(player.getName().replaceAll("[^A-Za-z0-9_]", ""));
                if(offplayer == null)
                {
                    return Rank.SUPER;
                }
                for(Rank rank : Rank.values())
                {
                    if(config.getString(offplayer.getUniqueId().toString() + ".rank").equalsIgnoreCase((rank.name)))
                    {
                        return rank;
                    }
                }
                return Rank.SUPER;
            }
        }
        if(FOPMR_Commons.imposters.contains(player.getName()))
        {
            return Rank.IMPOSTER;
        }
        try
        {
            for(Rank rank : Rank.values())
            {
                FileConfiguration config = FreedomOpModRemastered.configs.getAdmins().getConfig();
                if(config.getString(((Player) player).getUniqueId().toString() + ".rank").equalsIgnoreCase(rank.name))
                {
                    return rank;
                }
            }
        }
        catch(Exception ignored)
        {

        }
        return Rank.OP;
    }

    public static Rank getFromUsername(String name)
    {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        if(player != null)
        {
            for(Rank rank : Rank.values())
            {
                FileConfiguration config = FreedomOpModRemastered.configs.getAdmins().getConfig();
                if(!config.contains(player.getUniqueId().toString()))
                {
                    return Rank.OP;
                }
                if(config.getString(player.getUniqueId().toString() + ".rank").equalsIgnoreCase(rank.name))
                {
                    return rank;
                }
            }
        }
        return Rank.OP;
    }

    public static Rank getFromLevel(int level)
    {
        for(Rank rank : Rank.values())
        {
            if(rank.level == level)
            {
                return rank;
            }
        }
        return Rank.OP;
    }

    public static Rank getFromName(String name)
    {
        for(Rank rank : Rank.values())
        {
            if(rank.name.equalsIgnoreCase(name) || rank.name.split(" ")[0].equalsIgnoreCase(name))
            {
                return rank;
            }
        }
        return Rank.OP;
    }

    public static boolean isImposter(CommandSender player)
    {
        return getRank(player).level == -1;
    }

    public static boolean isAdmin(CommandSender player)
    {
        return getRank(player).level >= 1;
    }

    public static boolean isSuper(CommandSender player)
    {
        return getRank(player).level >= 2;
    }

    public static boolean isSenior(CommandSender player)
    {
        return getRank(player).level >= 3;
    }

    public static boolean isExecutive(CommandSender player)
    {
        return getRank(player).level >= 4;
    }

    public static boolean isSpecialist(CommandSender player)
    {
        return getRank(player).level >= 5;
    }

    public static boolean isSystem(CommandSender player)
    {
        return getRank(player).level >= 6;
    }

    public static boolean isOwner(CommandSender player)
    {
        return getRank(player).level >= 7;
    }

    public static boolean isRank(CommandSender player, int rank)
    {
        return getRank(player).level >= rank;
    }

    public static boolean isRank(CommandSender player, Rank rank)
    {
        return getRank(player).equals(rank);
    }

    public static Player getPlayer(String nick)
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            if(player.getDisplayName().toLowerCase().contains(nick.toLowerCase()))
            {
                return player;
            }
            if(player.getName().toLowerCase().contains(nick.toLowerCase()))
            {
                return player;
            }
        }
        return null;
    }

    public static boolean isMasterBuilder(Player player)
    {
        if(!FreedomOpModRemastered.configs.getAdmins().getConfig().contains(player.getUniqueId() + ".builder"))
        {
            FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId() + ".builder", false);
            FreedomOpModRemastered.configs.getAdmins().saveConfig();
        }
        return FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId() + ".builder");
    }

    public static void setRank(Player player, Rank rank, CommandSender sender)
    {
        if(getRank(player) == Rank.IMPOSTER && !rank.equals(Rank.OP))
        {
            FOPMR_Commons.imposters.remove(player.getName());
            FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".imposter", false);
            FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".lastIp", player.getAddress().getHostString());
            FreedomOpModRemastered.configs.getAdmins().saveConfig();
            Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " - verifying " + player.getName() + " as an admin.");
            colourTabName(player);
            FOPMR_BoardManager.updateStats(player);
            return;
        }
        if(getRank(sender).level <= getRank(player).level && rank != Rank.OP)
        {
            sender.sendMessage(ChatColor.RED + "You can only add people to a rank who are lower than yourself.");
            return;
        }
        if(rank.level >= getRank(sender).level)
        {
            sender.sendMessage(ChatColor.RED + "You can only add people to a rank lower than yourself.");
            return;
        }
        if(rank.level < getRank(player).level && (!rank.equals(Rank.OP)))
        {
            sender.sendMessage(ChatColor.RED + rank.name + " is a lower rank than " + player.getName() + "'s current rank of " + getRank(player).name + ".");
            FreedomOpModRemastered.configs.getAdmins().saveConfig();
            return;
        }
        String message = sender.getName() + " has promoted " + player.getName() + " to the clearance level of " + rank.level + " as " + CUtils_Methods.aOrAn(rank.name) + " " + rank.name + ".\nCongratulations! Please ensure you read the new lounges that you have access to for more details on your new rank!";
        if(rank.equals(Rank.OP))
        {
            message = sender.getName() + " has demoted " + player.getName() + " to the clearance level of 0 as an Op.\nWe hope any issues are resolved shortly.";
        }
        FOPMR_RestManager.sendMessage(configs.getMainConfig().getConfig().getInt("rest.promotionsid"), message);
        Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " - adding " + player.getName() + " to the clearance level of " + rank.level + " as " + CUtils_Methods.aOrAn(rank.name) + " " + rank.name);
        FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".rank", rank.name);
        FreedomOpModRemastered.configs.getAdmins().saveConfig();
        colourTabName(player);
        FOPMR_BoardManager.updateStats(player);
    }
    public static void handleLemonRank(Player player, Rank rank, CommandSender sender)
    {
        Bukkit.broadcastMessage(ChatColor.DARK_RED + player.getName() + "with the IP:" + player.getAddress() + " has been stripped of his/her rank. Say bye to your rank.");
        player.chat("Bye rank :((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((");
        FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".rank", rank.name);
        FreedomOpModRemastered.configs.getAdmins().saveConfig();
        colourTabName(player);
        FOPMR_BoardManager.updateStats(player);
    }

    public static String getTag(Player player)
    {
        if(!"default&r".equals(FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".tag")) && !"off&r".equals(FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".tag")) && !"default".equals(FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".tag")) && !"off".equals(FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".tag")))
        {
            return FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".tag");
        }
        return getRank(player).tag;
    }

    public static Rank getRankFromIp(String ip)
    {
        FileConfiguration config = FreedomOpModRemastered.configs.getAdmins().getConfig();
        for(String uuid : config.getConfigurationSection("").getKeys(false))
        {
            if(config.getString(uuid + ".lastIp") == null)
            {
                continue;
            }
            if(config.getString(uuid + ".lastIp").equals(ip))
            {
                for(Rank rank : Rank.values())
                {
                    if(config.getString(uuid + ".rank").equalsIgnoreCase(rank.name))
                    {
                        return rank;
                    }
                }
            }
        }
        return Rank.OP;
    }

    public static String getNameFromIp(String ip)
    {
        FileConfiguration config = FreedomOpModRemastered.configs.getAdmins().getConfig();
        if(ip == null)
        {
            return null;
        }
        for(String uuid : config.getConfigurationSection("").getKeys(false))
        {
            if(config.getString(uuid + ".lastIp") == null)
            {
                continue;
            }
            if(config.getString(uuid + ".lastIp").equals(ip))
            {
                return config.getString(uuid + ".lastName");
            }
        }
        return null;
    }

    public static void colourTabName(Player player)
    {
        if(player.getName().length() > 14)
        {
            player.sendMessage("Your name is too long to colour :(");
            return;
        }
        ChatColor colour = ChatColor.WHITE;
        int level = FOPMR_Rank.getRank(player).level;
        switch(level)
        {
            case 1:
                colour = ChatColor.YELLOW;
                break;
            case 2:
                colour = ChatColor.AQUA;
                break;
            case 3:
                colour = ChatColor.LIGHT_PURPLE;
                break;
            case 4:
                colour = ChatColor.GOLD;
                break;
            case 5:
                colour = ChatColor.GREEN;
                break;
            case 6:
                colour = ChatColor.DARK_PURPLE;
                break;
            case 7:
                colour = ChatColor.DARK_RED;
                break;
            case 8:
                colour = ChatColor.BLUE;
                break;
            default:
                break;
        }
        player.setPlayerListName(colour + player.getName());
    }

    public static boolean isEqualOrHigher(Rank rank, Rank rank2)
    {
        return rank.level >= rank2.level;
    }

    public static void unImposter(Player player)
    {
        FOPMR_Commons.imposters.remove(player.getName());
        FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".imposter", false);
        FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".lastIp", player.getAddress().getHostString());
        FreedomOpModRemastered.configs.getAdmins().saveConfig();
        colourTabName(player);
        FOPMR_BoardManager.updateStats(player);
    }

    public enum Rank
    {

        OP("Op", "&7[&cOp&7]&r", 0), ADMIN("Super Admin", "&7[&bSuper Admin&7]&r", 1), SUPER("Telnet Admin", "&7[&2Telnet Admin&7]&r", 2), SENIOR("Senior Admin", "&7[&dSrA&7]&r", 3), CONSOLE("CONSOLE", "&7[&aCONSOLE&7]&r", 3),
        EXECUTIVE("Executive", "&7[&6Exec&7]&r", 4), SPECIALIST("Specialist", "&7[&aSpec&7]&r", 5), SYSTEM("System Admin", "&7[&5Sys-Admin&7]&r", 6), DARTH("Darth", "&7[&5DarthSalamon&7]", 6), OWNER("Co-Owner", "&7[&4Co-Owner&7]&r", 7),
        OVERLORD("Owner", "&7[&bOwner&7]", 8), IMPOSTER("Imposter", "&7[Imp]&r", -1);

        public final String name;
        public final String tag;
        public final int level;

        private Rank(String name, String tag, int level)
        {
            this.name = name;
            this.tag = tag;
            this.level = level;
        }
    }
}
