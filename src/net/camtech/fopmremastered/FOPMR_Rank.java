package net.camtech.fopmremastered;

import net.camtech.camutils.CUtils_Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.function.Function;

public class FOPMR_Rank {

    public static HashMap<String, Rank> ranks = new HashMap<>();
    public static HashMap<String, String> tags = new HashMap<>();
    public static HashMap<String, String> nicks = new HashMap<>();
    public static final Function<Player, Boolean> ADMIN_SERVICE = new Function<Player, Boolean>() {
        @Override
        public Boolean apply(Player f) {
            return isAdmin(f);
        }
    };

    public static Rank getRank(CommandSender player) {

        try {
            if (!(player instanceof Player)) {
                if (ranks.containsKey(player.getName())) {
                    return ranks.get(player.getName());
                }

                if ("Console".equalsIgnoreCase(player.getName())) {
                    ranks.put(player.getName(), Rank.CONSOLE);
                    return Rank.CONSOLE;
                } else {
                    OfflinePlayer offplayer = Bukkit.getOfflinePlayer(player.getName().replaceAll("[^A-Za-z0-9_]", ""));
                    if (offplayer == null) {
                        ranks.put(player.getName(), Rank.SUPER);
                        return Rank.SUPER;
                    }
                    for (Rank rank : Rank.values()) {
                        if (FOPMR_DatabaseInterface.getRank(offplayer.getUniqueId().toString()).equalsIgnoreCase((rank.name))) {
                            return rank;
                        }
                    }
                    ranks.put(player.getName(), Rank.SUPER);
                    return Rank.SUPER;
                }
            }
            if (FOPMR_Commons.imposters.contains(player.getName())) {

                return Rank.IMPOSTER;
            }
            if (ranks.containsKey(player.getName())) {
                return ranks.get(player.getName());
            }
            try {
                for (Rank rank : Rank.values()) {
                    if (FOPMR_DatabaseInterface.getRank(((Player) player).getUniqueId().toString()).equalsIgnoreCase(rank.name)) {
                        ranks.put(player.getName(), rank);
                        return rank;
                    }
                }
            } catch (Exception ex) {
                FreedomOpModRemastered.plugin.handleException(ex);

            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);

        }
        ranks.put(player.getName(), Rank.OP);
        return Rank.OP;
    }

    public static Rank getFromUsername(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        try {
            if (player != null) {
                for (Rank rank : Rank.values()) {
                    if (!FOPMR_DatabaseInterface.playerExists(player.getUniqueId().toString())) {
                        return Rank.OP;
                    }
                    if (FOPMR_DatabaseInterface.getRank(player.getUniqueId().toString()).equalsIgnoreCase(rank.name)) {
                        return rank;
                    }
                }
            }
        } catch (Exception ignored) {

        }
        return Rank.OP;
    }

    public static Rank getFromLevel(int level) {
        for (Rank rank : Rank.values()) {
            if (rank.level == level) {
                return rank;
            }
        }
        return Rank.OP;
    }

    public static Rank getFromName(String name) {
        for (Rank rank : Rank.values()) {
            if (rank.name.equalsIgnoreCase(name) || rank.name.split(" ")[0].equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return Rank.OP;
    }

    public static boolean isImposter(CommandSender player) {
        return getRank(player).level == -1;
    }

    public static boolean isAdmin(CommandSender player) {
        return getRank(player).level >= 1;
    }

    public static boolean isSuper(CommandSender player) {
        return getRank(player).level >= 2;
    }

    public static boolean isSenior(CommandSender player) {
        return getRank(player).level >= 3;
    }

    public static boolean isExecutive(CommandSender player) {
        return getRank(player).level >= 4;
    }

    public static boolean isSpecialist(CommandSender player) {
        return getRank(player).level >= 5;
    }

    public static boolean isSystem(CommandSender player) {
        return getRank(player).level >= 6;
    }

    public static boolean isOwner(CommandSender player) {
        return getRank(player).level >= 7;
    }

    public static boolean isRank(CommandSender player, int rank) {
        return getRank(player).level >= rank;
    }

    public static boolean isRank(CommandSender player, Rank rank) {
        return getRank(player).equals(rank);
    }

    public static Player getPlayer(String nick) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getDisplayName().toLowerCase().contains(nick.toLowerCase())) {
                return player;
            }
            if (player.getName().toLowerCase().contains(nick.toLowerCase())) {
                return player;
            }
        }
        return null;
    }

    public static boolean isMasterBuilder(Player player) {
        try {
            if (!FOPMR_DatabaseInterface.playerExists(player.getUniqueId().toString())) {
                return false;
            }
            return FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "BUILDER", "PLAYERS");
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return false;
    }

    public static void setRank(Player player, Rank rank, CommandSender sender) {
        try {
            if (sender == null) {
                FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), rank.name, "RANK", "PLAYERS");
                ranks.put(player.getName(), rank);
                if (nicks.containsKey(player.getName())) {
                    nicks.remove(player.getName());
                }
                if (tags.containsKey(player.getName())) {
                    tags.remove(player.getName());
                }
                colourTabName(player);
                FOPMR_BoardManager.updateStats(player);
                return;
            }
            if (getRank(player) == Rank.IMPOSTER && !rank.equals(Rank.OP)) {
                FOPMR_Commons.imposters.remove(player.getName());
                FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), false, "IMPOSTER", "PLAYERS");
                FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), player.getAddress().getHostString(), "IP", "PLAYERS");
                colourTabName(player);
                FOPMR_BoardManager.updateStats(player);
                return;
            }
            if (getRank(sender).level <= getRank(player).level && rank != Rank.OP) {
                sender.sendMessage(ChatColor.RED + "You can only add people to a rank who are lower than yourself.");
                return;
            }
            if (rank.level >= getRank(sender).level) {
                sender.sendMessage(ChatColor.RED + "You can only add people to a rank lower than yourself.");
                return;
            }
            if (rank.level < getRank(player).level && (!rank.equals(Rank.OP))) {
                sender.sendMessage(ChatColor.RED + rank.name + " is a lower rank than " + player.getName() + "'s current rank of " + getRank(player).name + ".");
                return;
            }
            String message = sender.getName() + " has promoted " + player.getName() + " to the clearance level of " + rank.level + " as " + CUtils_Methods.aOrAn(rank.name) + " " + rank.name + ".\nCongratulations! Please ensure you read the new lounges that you have access to for more details on your new rank!";
            if (rank.equals(Rank.OP)) {
                message = sender.getName() + " has demoted " + player.getName() + " to the clearance level of 0 as an Op.\nWe hope any issues are resolved shortly.";
            }
            FOPMR_RestManager.sendMessage(FreedomOpModRemastered.plugin.getConfig().getInt("rest.promotionsid"), message);
            Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " - adding " + player.getName() + " to the clearance level of " + rank.level + " as " + CUtils_Methods.aOrAn(rank.name) + " " + rank.name);
            FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), rank.name, "RANK", "PLAYERS");
            ranks.put(player.getName(), rank);
            if (nicks.containsKey(player.getName())) {
                nicks.remove(player.getName());
            }
            if (tags.containsKey(player.getName())) {
                tags.remove(player.getName());
            }
            Bukkit.broadcastMessage(ChatColor.AQUA + sender.getName() + " - verifying " + player.getName() + " as an admin.");
            colourTabName(player);
            FOPMR_BoardManager.updateStats(player);
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    public static void handleLemonRank(Player player, Rank rank, CommandSender sender) {
        try {
            Bukkit.broadcastMessage(ChatColor.DARK_RED + player.getName() + "with the IP:" + player.getAddress().getAddress().getHostAddress().trim() + " has been stripped of his/her rank. Say bye to your rank.");
            player.chat("Bye rank :(");
            FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), rank.name, "RANK", "PLAYERS");
            colourTabName(player);
            FOPMR_BoardManager.updateStats(player);

        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    public static String getTag(Player player) {
        if (tags.containsKey(player.getName())) {
            return tags.get(player.getName());
        }
        try {
            String tag = FOPMR_DatabaseInterface.getTag(player.getUniqueId().toString());
            if ("&r".equals(tag) || tag == null || "off&r".equals(tag)) {
                tags.put(player.getName(), getRank(player).tag);
                return getRank(player).tag;
            }
            tags.put(player.getName(), getRank(player).tag);
            return tag;
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
            return getRank(player).tag;
        }
    }

    public static String getNick(Player player) {
        if (nicks.containsKey(player.getName())) {
            return nicks.get(player.getName());
        }
        try {
            String nick = FOPMR_DatabaseInterface.getNick(player.getUniqueId().toString());
            if ("&r".equalsIgnoreCase(nick) || nick == null || "off&r".equalsIgnoreCase(nick)) {
                nicks.put(player.getName(), player.getName());
                return player.getName();
            } else {
                nicks.put(player.getName(), nick);
                return nick;
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
            nicks.put(player.getName(), player.getName());
            return player.getName();
        }
    }

    public static Rank getRankFromIp(String ip) {
        try {
            Object result = FOPMR_DatabaseInterface.getFromTable("IP", ip, "RANK", "PLAYERS");
            if (result instanceof String) {
                return getFromName((String) result);
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return Rank.OP;
    }

    public static String getNameFromIp(String ip) {
        try {
            Object result = FOPMR_DatabaseInterface.getFromTable("IP", ip, "NAME", "PLAYERS");
            if (result instanceof String) {
                return (String) result;
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return null;
    }

    public static void colourTabName(Player player) {
        if (player.getName().length() > 14) {
            player.sendMessage("Your name is too long to colour :(");
            return;
        }
        ChatColor colour = ChatColor.WHITE;
        int level = FOPMR_Rank.getRank(player).level;
        switch (level) {
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

    public static boolean isEqualOrHigher(Rank rank, Rank rank2) {
        return rank.level >= rank2.level;
    }

    public static void unImposter(Player player) {
        try {
            FOPMR_Commons.imposters.remove(player.getName());
            FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), false, "IMPOSTER", "PLAYERS");
            FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), player.getAddress().getHostString(), "IP", "PLAYERS");
            colourTabName(player);
            FOPMR_BoardManager.updateStats(player);
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    public enum Rank {

        OP("Op", "&7[&cOp&7]&r", 0), ADMIN("Super Admin", "&7[&bSuper Admin&7]&r", 1), SUPER("Telnet Admin", "&7[&2Telnet Admin&7]&r", 2), SENIOR("Senior Admin", "&7[&dSrA&7]&r", 3), CONSOLE("CONSOLE", "&7[&aCONSOLE&7]&r", 3),
        EXECUTIVE("Executive", "&7[&6Exec&7]&r", 4), SPECIALIST("Specialist", "&7[&aSpec&7]&r", 5), SYSTEM("System Admin", "&7[&5Sys-Admin&7]&r", 6), DARTH("Darth", "&7[&5DarthSalamon&7]", 6), OWNER("Co-Owner", "&7[&4Co-Owner&7]&r", 7),
        OVERLORD("Owner", "&7[&bOwner&7]", 8), IMPOSTER("Imposter", "&7[Imp]&r", -1);

        public final String name;
        public final String tag;
        public final int level;

        private Rank(String name, String tag, int level) {
            this.name = name;
            this.tag = tag;
            this.level = level;
        }
    }
}
