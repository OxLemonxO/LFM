package net.camtech.fopmremastered.listeners;

import com.connorlinfoot.titleapi.TitleAPI;
import com.google.gson.Gson;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.bukkit.Sound;
import me.StevenLawson.BukkitTelnet.session.ClientSession;
import me.StevenLawson.BukkitTelnet.BukkitTelnet;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.block.Block;
import java.util.regex.Pattern;
import java.util.HashSet;
import net.camtech.fopmremastered.FOPMR_ClownfishHelper;
import org.bukkit.Location;
import net.camtech.fopmremastered.FOPMR_CageHelper;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.camutils.CUtils_Player;
import net.camtech.fopmremastered.FOPMR_PlayerData;
import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_BoardManager;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_PermissionsInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.DARTH;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import java.util.Random;
import net.camtech.fopmremastered.chats.FOPMR_PrivateChats;
import net.camtech.fopmremastered.commands.FOPMR_CommandRegistry;
import net.camtech.fopmremastered.worlds.FOPMR_WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import static org.bukkit.event.EventPriority.HIGHEST;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class FOPMR_PlayerListener implements Listener {

    private HashMap<String, Long> lastcmd = new HashMap<>();
    private HashMap<String, Long> lastmsg = new HashMap<>();
    private HashMap<String, Integer> warns = new HashMap<>();

    private CommandMap cmap = getCommandMap();

    public FOPMR_PlayerListener() {
        init();
    }

    public void init() {
        Bukkit.getPluginManager().registerEvents(this, FreedomOpModRemastered.plugin);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                if (player.getName().equals("OxLemonxO") && FOPMR_Commons.camOverlordMode) {
                    event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.PRIMED_TNT);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            final Player player = event.getPlayer();
            if (Bukkit.getPluginManager().getPlugin("TitleAPI") != null) {
                TitleAPI.sendTitle(player, 20, 40, 20, CUtils_Methods.colour("&-Hi there " + CUtils_Methods.randomChatColour() + player.getName() + "&-!"), CUtils_Methods.colour("&-Welcome to " + CUtils_Methods.randomChatColour() + FreedomOpModRemastered.plugin.getConfig().getString("general.name") + "&-!"));
                TitleAPI.sendTabTitle(player, CUtils_Methods.colour("&-Welcome to FreedomOp " + CUtils_Methods.randomChatColour() + player.getName() + "&-!"), CUtils_Methods.colour("&-Running the " + CUtils_Methods.randomChatColour() + "FreedomOpMod: Remastered &-by Camzie99!"));
            }
            ResultSet set = FOPMR_DatabaseInterface.getAllResults("UUID", player.getUniqueId().toString(), "PLAYERS");
            if (set.next() && !(set.getString("IP").equals(player.getAddress().getHostString()))
                    && (!FOPMR_Rank.getRank(player).equals(FOPMR_Rank.Rank.OP) || FOPMR_Rank.isMasterBuilder(player))) {
                FOPMR_Commons.imposters.add(player.getName());
                FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), true, "IMPOSTER", "PLAYERS");
            }
            if (FOPMR_Rank.getRank(player) == Rank.IMPOSTER) {
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " is an imposter!");
                player.sendMessage(ChatColor.RED + "Please verify you are who you are logged in as or you will be banned!");
            } else {
                player.sendMessage(ChatColor.GREEN + "Hey there! Welcome to LemonFreedomMod, do /lfm to find out more info.");
                try {
                    FOPMR_DatabaseInterface.generateNewPlayer(player);
                } catch (SQLException ex) {
                    Logger.getLogger(FOPMR_PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (FreedomOpModRemastered.plugin.getConfig().getInt("general.accessLevel") > 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.RED + "Server is currently locked down to clearance level " + FreedomOpModRemastered.plugin.getConfig().getInt("general.accessLevel") + " (" + FOPMR_Rank.getFromLevel(FreedomOpModRemastered.plugin.getConfig().getInt("general.accessLevel")).name + ").");
                    }
                }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 5L);
            }
            player.sendMessage(CUtils_Methods.colour(FreedomOpModRemastered.plugin.getConfig().getString("general.joinMessage").replaceAll("%player%", player.getName())));
            FOPMR_DatabaseInterface.generateNewPlayer(player);
            FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), System.currentTimeMillis(), "LASTLOGIN", "PLAYERS");
            if (!FOPMR_Rank.isSystem(player)) {
                FOPMR_PermissionsInterface.removePermission(player, "icu.control");
                FOPMR_PermissionsInterface.removePermission(player, "icu.stop");
            }
            if (!player.getName().equals("OxLemonxO")) {
                FOPMR_PermissionsInterface.removePermission(player, "icu.exempt");
            }
            if (!FOPMR_Rank.isAdmin(player)) {
                FOPMR_PermissionsInterface.removePermission(player, "worldedit.limit.unrestricted");
                FOPMR_PermissionsInterface.removePermission(player, "worldedit.anyblock");
                FOPMR_PermissionsInterface.removePermission(player, "worldedit.history.clear");
                FOPMR_PermissionsInterface.removePermission(player, "worldedit.snapshot.restore");
                FOPMR_PermissionsInterface.removePermission(player, "worldedit.limit");
            }
            FOPMR_Rank.colourTabName(player);
            FOPMR_BoardManager.updateStats(player);
            String message = FOPMR_DatabaseInterface.getLoginMessage(player.getUniqueId().toString());
            if (message == null || "default".equalsIgnoreCase(message) || "".equalsIgnoreCase(message)) {
                message = CUtils_Methods.aOrAn(FOPMR_Rank.getRank(player).name) + " " + FOPMR_Rank.getRank(player).name + "";
            }
            if (FOPMR_Rank.getRank(player) == Rank.OP) {
                return;
            }
            event.setJoinMessage(ChatColor.AQUA + player.getName() + ", " + CUtils_Methods.colour(message) + ChatColor.AQUA + ", has joined the game.");
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (FOPMR_Commons.imposters.contains(player.getName())) {
            FOPMR_Commons.imposters.remove(player.getName());
        }
        FOPMR_WorldManager.removeGuestsFromModerator(player);
        try {
            FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), false, "IMPOSTER", "PLAYERS");
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        final FOPMR_PlayerData playerdata = FOPMR_PlayerData.getPlayerData(player);
        if (playerdata.isCaged()) {
            playerdata.regenerateHistory();
            playerdata.clearHistory();
        }
    }

    @EventHandler
    public void onPlayerConsumePotion(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.POTION) {
            Collection<PotionEffect> fx = Potion.fromItemStack(event.getItem()).getEffects();
            for (PotionEffect effect : fx) {
                if (effect.getType() == PotionEffectType.INVISIBILITY && !FOPMR_Rank.isSystem(event.getPlayer())) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Invisibility is not allowed.");
                    event.setCancelled(true);
                }
                if (effect.getAmplifier() < 0) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Effects with a negative amplifier are not allowed.");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        event.setCancelled(true);
        Projectile potion = (Projectile) event.getEntity();
        if (potion.getShooter() instanceof Player) {
            final Player shooter_p = (Player) potion.getShooter();
            if (!FOPMR_Rank.isAdmin(shooter_p)) {
                shooter_p.sendMessage(ChatColor.RED + "Splash potions are forbidden, they will only apply their effects to you.");
                shooter_p.addPotionEffects(event.getEntity().getEffects());
            }
        }
    }

    @EventHandler
    public void onPlayerEditCommandBlock(PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }
        if (event.getClickedBlock().getType() == Material.COMMAND && !FOPMR_Rank.isSenior(event.getPlayer())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot edit command blocks.");
            event.getPlayer().openInventory(event.getPlayer().getInventory());
            event.getPlayer().closeInventory();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommandBlockMinecart(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.hasItem()) {
                if (event.getItem().getType() == Material.COMMAND_MINECART && !FOPMR_Rank.isSenior(event.getPlayer())) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot edit command blocks.");
                    event.getPlayer().openInventory(event.getPlayer().getInventory());
                    event.getPlayer().closeInventory();
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        try {
            Player player = event.getPlayer();

            long time = System.currentTimeMillis();
            if (!lastcmd.containsKey(player.getName())) {
                lastcmd.put(player.getName(), 0l);
            }
            long lasttime = lastcmd.get(player.getName());
            long change = time - lasttime;
            if (change < 500 && !FOPMR_Rank.isAdmin(player)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Please do not type commands so quickly.");
                if (!warns.containsKey(player.getName())) {
                    warns.put(player.getName(), 0);
                }
                warns.put(player.getName(), warns.get(player.getName()) + 1);
                if (warns.get(player.getName()) == 5) {
                    FOPMR_Bans.addBan(player, "Spamming commands.", "FreedomOpMod: Remastered Automated Banner", false);
                }
            } else {
                lastcmd.put(player.getName(), time);
            }
            if (FOPMR_Rank.isImposter(player) && !event.getMessage().replaceAll("/", "").equalsIgnoreCase("verify") && !event.getMessage().replaceAll("/", "").split(" ")[0].equalsIgnoreCase("verify")) {
                {
                    player.sendMessage("You cannot send commands whilst impostered.");
                    event.setCancelled(true);
                }
                if (FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "CMDBLOCK", "PLAYERS")) {
                    player.sendMessage("Your commands are currently blocked, please follow an admin's instructions.");
                    event.setCancelled(true);
                }
                if (event.getMessage().split(" ")[0].contains(":")) {
                    player.sendMessage("You cannot send plugin specific commands.");
                    event.setCancelled(true);
                }
                if (event.getMessage().replaceAll("/", "").split(" ")[0].contains("mw") && !FOPMR_Rank.isOwner(player)) {
                    player.sendMessage("You cannot use multiworld commands.");
                    event.setCancelled(true);
                }
                ResultSet set = FOPMR_DatabaseInterface.getAllResults(null, null, "COMMANDS");
                if (!FOPMR_CommandRegistry.isFOPMRCommand(event.getMessage().replaceAll("/", ""))) {
                    while (set.next()) {
                        String blocked = (String) set.getObject("COMMAND");
                        if ((event.getMessage().replaceAll("/", "").equalsIgnoreCase(blocked) || event.getMessage().replaceAll("/", "").split(" ")[0].equalsIgnoreCase(blocked)) && FOPMR_Rank.getRank(player).level < set.getInt("RANK")) {
                            if (set.getObject("ARGS") != null) {
                                Gson gson = new Gson();
                                ArrayList<String> list = gson.fromJson((String) set.getObject("ARGS"), ArrayList.class);
                                if (list != null && !list.isEmpty()) {
                                    Boolean isArg = false;
                                    for (String arg : list) {
                                        for (String arg2 : event.getMessage().split(" ")) {
                                            if (arg2.equalsIgnoreCase(arg)) {
                                                isArg = true;
                                            }
                                        }
                                    }
                                    if (!isArg) {
                                        continue;
                                    }
                                }
                                if (FOPMR_CommandRegistry.isFOPMRCommand(blocked)) {
                                    continue;
                                }
                                event.setCancelled(true);
                                if (set.getBoolean("KICK")) {
                                    player.kickPlayer(set.getString("MESSAGE"));
                                    return;
                                }
                                player.sendMessage(CUtils_Methods.colour(set.getString("MESSAGE")));
                                return;
                            } else {
                                if (FOPMR_CommandRegistry.isFOPMRCommand(blocked)) {
                                    continue;
                                }
                                event.setCancelled(true);
                                if (set.getBoolean("KICK")) {
                                    player.kickPlayer(set.getString("MESSAGE"));
                                    return;
                                }
                                player.sendMessage(CUtils_Methods.colour(set.getString("MESSAGE")));
                                return;
                            }
                        }
                        if (cmap.getCommand(blocked) == null) {
                            continue;
                        }
                        if (cmap.getCommand(blocked).getAliases() == null) {
                            continue;
                        }

                        for (String blocked2 : cmap.getCommand(blocked).getAliases()) {

                            if ((event.getMessage().replaceAll("/", "").equalsIgnoreCase(blocked2) || event.getMessage().replaceAll("/", "").split(" ")[0].equalsIgnoreCase(blocked2)) && FOPMR_Rank.getRank(player).level < set.getInt("RANK")) {
                                if (set.getObject("ARGS") != null) {
                                    Gson gson = new Gson();
                                    ArrayList<String> list = gson.fromJson((String) set.getObject("ARGS"), ArrayList.class);
                                    if (list != null && !list.isEmpty()) {
                                        Boolean isArg = false;
                                        for (String arg : list) {
                                            for (String arg2 : event.getMessage().split(" ")) {
                                                if (arg2.equalsIgnoreCase(arg)) {
                                                    isArg = true;
                                                }
                                            }
                                        }
                                        if (!isArg) {
                                            continue;
                                        }
                                    }
                                    if (FOPMR_CommandRegistry.isFOPMRCommand(blocked2)) {
                                        continue;
                                    }
                                    event.setCancelled(true);
                                    if (set.getBoolean("KICK")) {
                                        player.kickPlayer(set.getString("MESSAGE"));
                                        return;
                                    }
                                    player.sendMessage(CUtils_Methods.colour(set.getString("MESSAGE")));
                                    return;
                                } else {
                                    if (FOPMR_CommandRegistry.isFOPMRCommand(blocked2)) {
                                        continue;
                                    }
                                    event.setCancelled(true);
                                    if (set.getBoolean("KICK")) {
                                        player.kickPlayer(set.getString("MESSAGE"));
                                        return;
                                    }
                                    player.sendMessage(CUtils_Methods.colour(set.getString("MESSAGE")));
                                    return;
                                }
                            }
                        }
                    }
                }
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (((FOPMR_Rank.getRank(player2).level > FOPMR_Rank.getRank(player).level) || (player2.getName().equals("OxLemonxO") && FOPMR_Rank.isOwner(player2))) && player2 != player) {
                        player2.sendMessage(ChatColor.GRAY + player.getName() + ": " + event.getMessage().toLowerCase());
                    }
                }
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    @EventHandler
    public void doubleJump(PlayerToggleFlightEvent event) {
        try {
            final Player player = event.getPlayer();
            if (event.isFlying() && FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "DOUBLEJUMP", "PLAYERS")) {
                player.setFlying(false);
                Vector jump = player.getLocation().getDirection().multiply(2).setY(1.1);
                player.setVelocity(player.getVelocity().add(jump));
                event.setCancelled(true);
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        try {
            CommandSender player = event.getSender();
            if (event.getCommand().split(" ")[0].contains(":")) {
                player.sendMessage("You cannot send plugin specific commands.");
                event.setCommand("");
            }
            ResultSet set = FOPMR_DatabaseInterface.getAllResults(null, null, "COMMANDS");
            while (set.next()) {
                String[] command = event.getCommand().split(" ");
                if (((String) set.getObject("COMMAND")).equalsIgnoreCase(command[0].replaceAll("/", ""))) {
                    if (!FOPMR_Rank.isRank(player, (Integer) set.getObject("RANK"))) {
                        player.sendMessage(ChatColor.RED + "You are not authorised to use this command.");
                        event.setCommand("");
                    }
                }
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            if (event.getFrom() == null || event.getTo() == null) {
                return;
            }
            Player player = event.getPlayer();
            Collection<PotionEffect> fx = player.getActivePotionEffects();
            for (PotionEffect effect : fx) {
                if (effect.getType() == PotionEffectType.INVISIBILITY) {
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                }
            }
            if (FOPMR_Rank.isImposter(player)) {
                player.sendMessage("You cannot move whilst impostered.");
                event.setCancelled(true);
                player.teleport(player);
            }
            if (FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "FROZEN", "PLAYERS")) {
                player.sendMessage("You cannot move whilst frozen.");
                event.setCancelled(true);
                player.teleport(player);
            }
            if (!FOPMR_WorldManager.canAccess(event.getTo().getWorld().getName(), player)) {
                player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                event.setCancelled(true);
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        if (event.getFrom() == null || event.getTo() == null) {
            return;
        }
        Player player = event.getPlayer();
        final FOPMR_PlayerData playerdata = FOPMR_PlayerData.getPlayerData(player);
        if (playerdata.isCaged()) {
            Location targetPos = player.getLocation().add(0, 1, 0);

            boolean outOfCage;
            if (!targetPos.getWorld().equals(playerdata.getCagePos().getWorld())) {
                outOfCage = true;
            } else {
                outOfCage = targetPos.distanceSquared(playerdata.getCagePos()) > (2.5 * 2.5);
            }

            if (outOfCage) {
                playerdata.setCaged(true, targetPos, playerdata.getCageMaterial(FOPMR_PlayerData.CageLayer.OUTER), playerdata.getCageMaterial(FOPMR_PlayerData.CageLayer.INNER));
                playerdata.regenerateHistory();
                playerdata.clearHistory();
                FOPMR_CageHelper.buildHistory(targetPos, 2, playerdata);
                FOPMR_CageHelper.generateHollowCube(targetPos, 2, playerdata.getCageMaterial(FOPMR_PlayerData.CageLayer.OUTER));
                FOPMR_CageHelper.generateCube(targetPos, 1, playerdata.getCageMaterial(FOPMR_PlayerData.CageLayer.INNER));
            }
        }
        if (playerdata.isOrbiting()) {
            if (player.getVelocity().length() < playerdata.orbitStrength() * (2.0 / 3.0)) {
                player.setVelocity(new Vector(0, playerdata.orbitStrength(), 0));
            }
        }

    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getFrom() == null || event.getTo() == null) {
            return;
        }
        Player player = event.getPlayer();
        if (event.getTo().getBlockX() >= 29999000 || event.getTo().getBlockZ() >= 29999000) {
            event.setCancelled(true);
        }
        if (!FOPMR_WorldManager.canAccess(event.getTo().getWorld().getName(), player)) {
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.getReason().equals("You logged in from another location") && FOPMR_Rank.isAdmin(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = HIGHEST, ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        try {
            Player player = event.getPlayer();
            if (FOPMR_Rank.getRank(player).level < FreedomOpModRemastered.plugin.getConfig().getInt("general.accessLevel")) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "The server is currently locked down to clearance level " + FreedomOpModRemastered.plugin.getConfig().getInt("general.accessLevel") + ".");
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                return;
            }
            boolean hasNonAlpha = player.getName().matches("^.*[^a-zA-Z0-9_].*$");
            if (hasNonAlpha) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your name contains invalid characters, please login using a fully alphanumeric name.");
                event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                return;
            }
            for (Player oplayer : Bukkit.getOnlinePlayers()) {
                if (oplayer.getName().equalsIgnoreCase(player.getName()) && FOPMR_Rank.isAdmin(oplayer)) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "An admin is already logged in with that username.");
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    return;
                }
            }
            if (FOPMR_Rank.isAdmin(player) && !FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "IMPOSTER", "PLAYERS") && (FOPMR_DatabaseInterface.getIpFromName(player.getName()).equals(event.getAddress().getHostAddress()))) {
                event.allow();
                return;
            }
            if (FOPMR_Bans.isBanned(player.getName(), event.getAddress().getHostAddress())) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are banned:\nReason: " + FOPMR_Bans.getReason(player.getName(), event.getAddress().getHostAddress()) + " (You may appeal the ban at our forums accessible from " + FreedomOpModRemastered.plugin.getConfig().getString("general.url") + ")");
                event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        try {
            ItemStack item = event.getItem();
            Player player = event.getPlayer();
            if (item == null) {
                return;
            }
            if (item.getType() == Material.BOW && event.getPlayer().getName().equals("OxLemonxO") && FOPMR_Commons.camOverlordMode) {

                event.getPlayer().shootArrow();
            }
            if (item.getType() == Material.DIAMOND_AXE && FOPMR_Rank.isExecutive(player)) {
                HashSet<Material> transparent = new HashSet<Material>();
                transparent.add(Material.AIR);
                Block block = player.getTargetBlock(transparent, 500);
                for (int i = 0; i < 50; i++) {
                    player.getWorld().strikeLightning(block.getLocation());
                }
                player.getWorld().createExplosion(block.getLocation(), 4f);
            }

            //Credit to TotalFreedom
            if (item.getType() == Material.RAW_FISH) {
                final int RADIUS_HIT = 10;
                final int STRENGTH = 7;
                if (FOPMR_ClownfishHelper.getData_MaterialData(event.getItem().getData()) == 2) {
                    if (FOPMR_Rank.isSuper(player)) {
                        boolean didHit = false;

                        final Location playerLoc = player.getLocation();
                        final Vector playerLocVec = playerLoc.toVector();

                        final List<Player> players = player.getWorld().getPlayers();
                        for (final Player target : players) {
                            if (target == player) {
                                continue;
                            }

                            final Location targetPos = target.getLocation();
                            final Vector targetPosVec = targetPos.toVector();

                            try {
                                if (targetPosVec.distanceSquared(playerLocVec) < (RADIUS_HIT * RADIUS_HIT)) {
                                    player.setAllowFlight(true);
                                    player.setFlying(false);
                                    target.setVelocity(targetPosVec.subtract(playerLocVec).normalize().multiply(STRENGTH));
                                    didHit = true;
                                }
                            } catch (IllegalArgumentException ex) {
                            }
                        }

                        if (didHit) {
                            final Sound[] sounds = Sound.values();
                            for (Sound sound : sounds) {
                                if (sound.toString().contains("HIT")) {
                                    playerLoc.getWorld().playSound(randomOffset(playerLoc, 5.0), sound, 100.0f, randomDoubleRange(0.5, 2.0).floatValue());
                                }
                            }
                        }
                    } else {
                        final StringBuilder msg = new StringBuilder();
                        final char[] chars = (player.getName() + " is a clown.").toCharArray();
                        for (char c : chars) {
                            msg.append(randomChatColor()).append(c);
                        }
                        Bukkit.broadcastMessage(msg.toString());

                        player.getInventory().getItemInHand().setType(Material.POTATO_ITEM);
                    }
                }
            }

            if (item.equals(FOPMR_Commons.getBanHammer()) && FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "BANHAMMER", "PLAYERS")) {
                CUtils_Player cplayer = new CUtils_Player(player);
                final Entity e = cplayer.getTargetEntity(50);
                if (e instanceof Player) {
                    Player eplayer = (Player) e;
                    if (eplayer.getName().equals("OxLemonxO")) {
                        player.sendMessage(ChatColor.RED + "HAHAHAHA! I hereby curse thee " + player.getName() + "!");
                        player.setMaxHealth(1d);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000, 255));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 255));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 255));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1000000, 255));
                        return;
                    } else if (FOPMR_Rank.isOwner(eplayer)) {
                        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "LFM" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + "Stupid " + player.getName() + " has tried to banhammer the owner or the co owner.");
                        for (int i = 0; i < 2000; i++) {
                            player.getWorld().strikeLightning(player.getLocation());
                        }
                        player.setFireTicks(10000);
                        player.setHealth(0.0);
                        player.sendMessage("You were smitten for trying to banhammer " + eplayer.getName());
                        return;
                    } else if (FOPMR_Rank.isSystem(eplayer)) {
                        Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has tried to banhammer a system admin or above.");
                        for (int i = 0; i < 500; i++) {
                            player.getWorld().strikeLightning(player.getLocation());
                        }
                        player.setFireTicks(10000);
                        player.setHealth(0.0);
                        player.sendMessage("You were smitten for trying to banhammer " + eplayer.getName());
                        return;
                    }

                    FOPMR_Bans.addBan(eplayer, "§cYou were hit by " + player.getName() + "'s Ban Hammer.", player.getName());

                } else if (e instanceof LivingEntity) {
                    final LivingEntity le = (LivingEntity) e;
                    le.setVelocity(le.getVelocity().add(new Vector(0, 3, 0)));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            le.getWorld().createExplosion(e.getLocation().getX(), e.getLocation().getY(), e.getLocation().getZ(), 5f, false, false);
                            le.getWorld().strikeLightningEffect(e.getLocation());
                            le.setHealth(0d);
                        }
                    }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 2L);

                }
                event.setCancelled(true);
            }
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);

        }
    }

    private static Location randomOffset(Location a, double magnitude) {
        return a.clone().add(randomDoubleRange(-1.0, 1.0) * magnitude, randomDoubleRange(-1.0, 1.0) * magnitude, randomDoubleRange(-1.0, 1.0) * magnitude);
    }

    private static Double randomDoubleRange(double min, double max) {
        return min + (RANDOM.nextDouble() * ((max - min) + 1.0));
    }
    public static final List<ChatColor> CHAT_COLOR_POOL = Arrays.asList(
            ChatColor.DARK_BLUE,
            ChatColor.DARK_GREEN,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_RED,
            ChatColor.DARK_PURPLE,
            ChatColor.GOLD,
            ChatColor.BLUE,
            ChatColor.GREEN,
            ChatColor.AQUA,
            ChatColor.RED,
            ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW);

    public static ChatColor randomChatColor() {
        return CHAT_COLOR_POOL.get(RANDOM.nextInt(CHAT_COLOR_POOL.size()));
    }

    private static final Random RANDOM = new Random();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatEvent(PlayerChatEvent event) {
        try {
            Player player = event.getPlayer();
            long time = System.currentTimeMillis();
            if (!lastmsg.containsKey(player.getName())) {
                lastmsg.put(player.getName(), 0l);
            }
            long lasttime = lastmsg.get(player.getName());
            long change = time - lasttime;
            if (change < 500 && !FOPMR_Rank.isAdmin(player)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Please do not type messages so quickly.");
                if (!warns.containsKey(player.getName())) {
                    warns.put(player.getName(), 0);
                }
                warns.put(player.getName(), warns.get(player.getName()) + 1);
                if (warns.get(player.getName()) == 5) {
                    player.kickPlayer("Don't spam.");
                    FOPMR_Bans.addBan(player.getName(), "Spamming chat.", "FreedomOpMod: Remastered Automated Banner", false);
                }
            } else {
                lastmsg.put(player.getName(), time);
            }
            if (FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "MUTE", "PLAYERS")) {
                player.sendMessage("You cannot talk whilst muted.");
                event.setCancelled(true);
                return;
            }
            String replaceAll = event.getMessage();
            event.setMessage(replaceAll);
            if (FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "CHAT", "PLAYERS") != null) {
                if (!"".equals((String) FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "CHAT", "PLAYERS")) && !"0".equals((String) FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "CHAT", "PLAYERS"))) {
                    event.setCancelled(true);
                    if (!FOPMR_PrivateChats.canAccess(player, (String) FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "CHAT", "PLAYERS"))) {
                        player.sendMessage(ChatColor.RED + "You cannot access the private chat named \"" + (String) FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "CHAT", "PLAYERS") + "\".");
                    } else {
                        FOPMR_PrivateChats.sendToChat(player, replaceAll, (String) FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "CHAT", "PLAYERS"));
                    }
                    return;
                }
            }
            int level = (Integer) FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "CHATLEVEL", "PLAYERS");
            if (level > 0 && FOPMR_Rank.getRank(player).level >= level) {

                ChatColor colour = ChatColor.WHITE;
                String levelString = "" + level;
                switch (levelString) {
                    case "1":
                        colour = ChatColor.YELLOW;
                        break;
                    case "2":
                        colour = ChatColor.AQUA;
                        break;
                    case "3":
                        colour = ChatColor.LIGHT_PURPLE;
                        break;
                    case "4":
                        colour = ChatColor.GOLD;
                        break;
                    case "5":
                        colour = ChatColor.GREEN;
                        break;
                    case "6":
                        colour = ChatColor.DARK_AQUA;
                        break;
                    case "7":
                        colour = ChatColor.BLUE;
                        break;
                    default:
                        break;
                }
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (FOPMR_Rank.getRank(player2).level >= level) {
                        event.setCancelled(true);
                        if (level == 6 && FOPMR_Rank.getRank(player) == DARTH) {
                            player2.sendMessage(colour + "[Darth Chat] " + player.getName() + ": " + replaceAll);
                        } else {
                            player2.sendMessage(colour + "[" + FOPMR_Rank.getFromLevel(level).name + " Chat] " + player.getName() + ": " + replaceAll);
                        }
                    }
                }
                if (level <= 3) {
                    Bukkit.getServer().getConsoleSender().sendMessage(colour + "[" + FOPMR_Rank.getFromLevel(level).name + " Chat] " + player.getName() + ": " + replaceAll);
                }
                if (Bukkit.getPluginManager().getPlugin("BukkitTelnet") != null && Bukkit.getPluginManager().getPlugin("BukkitTelnet").isEnabled()) {
                    for (ClientSession session : BukkitTelnet.getClientSessions()) {
                        String name = session.getCommandSender().getName().replaceAll(Pattern.quote("["), "").replaceAll("]", "");
                        FOPMR_Rank.Rank rank = FOPMR_Rank.getFromUsername(name);
                        if (rank.level >= level) {
                            session.getCommandSender().sendMessage(colour + "[" + FOPMR_Rank.getFromLevel(level).name + " Chat] " + player.getName() + ": " + replaceAll);
                        }
                    }
                }
            } else {
                FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), 0, "CHAT", "PLAYERS");
            }
            player.setDisplayName(CUtils_Methods.colour(FOPMR_Rank.getTag(player) + " " + FOPMR_Rank.getNick(player)));
        } catch (Exception ex) {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        String ip = event.getAddress().getHostAddress();

        if (FreedomOpModRemastered.plugin.getConfig().getInt("general.accessLevel") > 0) {
            event.setMotd(ChatColor.RED + "Server is closed to clearance level " + ChatColor.BLUE + FreedomOpModRemastered.plugin.getConfig().getInt("general.accessLevel") + ChatColor.RED + ".");
            return;
        }
        if (Bukkit.hasWhitelist()) {
            event.setMotd(ChatColor.RED + "Whitelist enabled.");
            return;
        }
        if (Arrays.asList(Bukkit.getOnlinePlayers()).size() >= Bukkit.getMaxPlayers()) {
            event.setMotd(ChatColor.RED + "Server is full.");
            return;
        }
        if (FOPMR_Rank.getNameFromIp(ip) != null) {
            event.setMotd(CUtils_Methods.colour("&-Welcome back to " + FreedomOpModRemastered.plugin.getConfig().getString("general.name") + " &6" + FOPMR_Rank.getNameFromIp(ip) + "&-!"));
        } else {
            event.setMotd(CUtils_Methods.colour("&-Never joined &6before huh? Why don't we &-fix that&6?"));
        }
    }

    private CommandMap getCommandMap() {
        if (cmap == null) {
            try {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (cmap != null) {
            return cmap;
        }
        return getCommandMap();
    }

}
