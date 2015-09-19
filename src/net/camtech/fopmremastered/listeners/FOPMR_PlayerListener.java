package net.camtech.fopmremastered.listeners;

import com.connorlinfoot.titleapi.TitleAPI;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Pattern;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.camutils.CUtils_Player;
import net.camtech.fopmremastered.FOPMR_Bans;
import net.camtech.fopmremastered.FOPMR_BoardManager;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Config;
import net.camtech.fopmremastered.FOPMR_PermissionsInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.SYSTEM;
import static net.camtech.fopmremastered.FOPMR_Rank.isSystem;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.DARTH;
import org.bukkit.Sound;
import java.util.Random;
import org.bukkit.Location;
import java.util.List;
import net.camtech.fopmremastered.FOPMR_ClownfishHelper;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import net.camtech.fopmremastered.chats.FOPMR_PrivateChats;
import net.camtech.fopmremastered.commands.FOPMR_CommandRegistry;
import net.camtech.fopmremastered.worlds.FOPMR_WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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

public class FOPMR_PlayerListener implements Listener {

    private HashMap<String, Long> lastcmd = new HashMap<>();
    private HashMap<String, Long> lastmsg = new HashMap<>();
    private HashMap<String, Integer> warns = new HashMap<>();

    private CommandMap cmap = getCommandMap();
    private Object world;

    public FOPMR_PlayerListener() {
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
    /*
you can do this down in OnPlayerUseItem. Edit it.
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        final Player player = event.getPlayer();
        
        switch (event.getAction())
        {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
            {
                switch (event.getMaterial())
                {
                    // thor hammer (toy for the executives) :P
                    case DIAMOND_AXE:
                    {
                        if (FOPMR_Rank.isSystem(player) | FOPMR_Rank.isSpecialist(player) | FOPMR_Rank.isExecutive(player))
                        {
   

*/
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FOPMR_Config adminConfig = FreedomOpModRemastered.configs.getAdmins();
        FileConfiguration config = adminConfig.getConfig();
        final Player player = event.getPlayer();
        if (Bukkit.getPluginManager().getPlugin("TitleAPI") != null) {
            TitleAPI.sendTitle(player, 20, 40, 20, CUtils_Methods.colour("&-Hi there " + CUtils_Methods.randomChatColour() + player.getName() + "&-!"), CUtils_Methods.colour("&-Welcome to " + CUtils_Methods.randomChatColour() + FreedomOpModRemastered.configs.getMainConfig().getConfig().getString("general.name") + "&-!"));
            TitleAPI.sendTabTitle(player, CUtils_Methods.colour("&-Welcome to FreedomOp " + CUtils_Methods.randomChatColour() + player.getName() + "&-!"), CUtils_Methods.colour("&-Running the " + CUtils_Methods.randomChatColour() + "FreedomOpMod: Remastered &-by Camzie99!"));
        }
        for (String UUID : config.getKeys(false)) {
            if (((config.getString(UUID + ".lastName").equals(player.getName()))
                    && !(config.getString(UUID + ".lastIp").equals(player.getAddress().getHostString())))
                    && (!FOPMR_Rank.getRank(player).equals(FOPMR_Rank.Rank.OP) || FOPMR_Rank.isMasterBuilder(player))) {
                FOPMR_Commons.imposters.add(player.getName());
                config.set(UUID + ".imposter", true);
            }
        }
        if (config.contains(player.getUniqueId().toString())) {
            if (!(config.getString(player.getUniqueId().toString()) + ".lastName").equals(player.getName())) {
                config.set(player.getUniqueId().toString() + ".lastName", player.getName());
            }
            if (!(config.getString(player.getUniqueId().toString()) + ".lastIp").equals(player.getAddress().getHostString()) && FOPMR_Rank.getRank(player) == FOPMR_Rank.Rank.OP) {
                config.set(player.getUniqueId().toString() + ".lastIp", player.getAddress().getHostString());
            }
            if (!"default".equals(config.getString(player.getUniqueId().toString() + ".login"))) {
                event.setJoinMessage(ChatColor.AQUA + player.getName() + " " + CUtils_Methods.colour(config.getString(player.getUniqueId().toString() + ".login")));
            } else if (FOPMR_Rank.getRank(player) != FOPMR_Rank.Rank.OP) {
                event.setJoinMessage(ChatColor.AQUA + player.getName() + " is " + CUtils_Methods.aOrAn(FOPMR_Rank.getRank(player).name) + " " + FOPMR_Rank.getRank(player).name);
            }
            if (FOPMR_Rank.getRank(player) == Rank.IMPOSTER) {
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " is an imposter!");
                player.sendMessage(ChatColor.RED + "Please verify you are who you are logged in as or you will be banned!");
            }
        } else {
            player.sendMessage(ChatColor.GREEN + "Hey there! Welcome to the FreedomOpMod: Remastered!, do /fopm to find out more info.");
            config.set(player.getUniqueId().toString() + ".lastName", player.getName());
            config.set(player.getUniqueId().toString() + ".lastIp", player.getAddress().getHostString());
            config.set(player.getUniqueId().toString() + ".chat", "");
            config.set(player.getUniqueId().toString() + ".rank", "Op");
            config.set(player.getUniqueId().toString() + ".login", "default");
            config.set(player.getUniqueId().toString() + ".votes", 0);
            config.set(player.getUniqueId().toString() + ".imposter", false);
            config.set(player.getUniqueId().toString() + ".chatLevel", 0);
            config.set(player.getUniqueId().toString() + ".displayName", player.getName());
            config.set(player.getUniqueId().toString() + ".tag", "default");
            config.set(player.getUniqueId().toString() + ".builder", false);
            config.set(player.getUniqueId().toString() + ".banHammer", false);
            config.set(player.getUniqueId().toString() + ".cmdblock", false);
            config.set(player.getUniqueId().toString() + ".djump", false);
            config.set(player.getUniqueId().toString() + ".muted", false);
            config.set(player.getUniqueId().toString() + ".randomChatColour", false);
            config.set(player.getUniqueId().toString() + ".chatColours", false);
            config.set(player.getUniqueId().toString() + ".lastLogin", System.currentTimeMillis());
        }
        config.set(player.getUniqueId().toString() + ".chatColours", true);
        config.set(player.getUniqueId().toString() + ".randomChatColour", true);
        adminConfig.saveConfig();

        if (FreedomOpModRemastered.configs.getMainConfig().getConfig().getInt("general.accessLevel") > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(ChatColor.RED + "Server is currently locked down to clearance level " + FreedomOpModRemastered.configs.getMainConfig().getConfig().getInt("general.accessLevel") + " (" + FOPMR_Rank.getFromLevel(FreedomOpModRemastered.configs.getMainConfig().getConfig().getInt("general.accessLevel")).name + ").");
                }
            }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 5L);
        }
        player.sendMessage(CUtils_Methods.colour(FreedomOpModRemastered.configs.getMainConfig().getConfig().getString("general.joinMessage").replaceAll("%player%", player.getName())));
        config.set(player.getUniqueId().toString() + ".lastLogin", System.currentTimeMillis());
        FOPMR_PermissionsInterface.removeMoreProtectPermissions(player);
        if (!FOPMR_Rank.isSystem(player)) {
            FOPMR_PermissionsInterface.removePermission(player, "icu.control");
            FOPMR_PermissionsInterface.removePermission(player, "icu.stop");
        }
        if (!player.getName().equals("Camzie99")) {
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
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (FOPMR_Commons.imposters.contains(player.getName())) {
            FOPMR_Commons.imposters.remove(player.getName());
        }
        FOPMR_WorldManager.removeGuestsFromModerator(player);
        FileConfiguration admins = FreedomOpModRemastered.configs.getAdmins().getConfig();
        if (admins.getBoolean(player.getUniqueId().toString() + ".imposter")) {
            admins.set(player.getUniqueId().toString() + ".imposter", false);
        }
        FreedomOpModRemastered.configs.getAdmins().saveConfig();
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
            ((Player) potion.getShooter()).sendMessage(ChatColor.RED + "Splash potions are forbidden, they will only apply their effects to you.");
            ((Player) potion.getShooter()).addPotionEffects(event.getEntity().getEffects());
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
                FOPMR_Commons.playerMsg(player, "Stop spamming commands or you will be banned.");
            }
        } else {
            lastcmd.put(player.getName(), time);
        }
        if (FOPMR_Rank.isImposter(player)) {
            player.sendMessage("You cannot send commands whilst impostered.");
            event.setCancelled(true);
        }
        if (FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".cmdblock")) {
            player.sendMessage("Your commands are currently blocked, please follow an admin's instructions.");
            event.setCancelled(true);
        }
        if (event.getMessage().split(" ")[0].contains(":")) {
            player.sendMessage("You cannot send plugin specific commands.");
            event.setCancelled(true);
        }
        if (event.getMessage().replaceAll("/", "").split(" ")[0].contains("mv") && !FOPMR_Rank.isOwner(player)) {
            player.sendMessage("You cannot use multiverse commands.");
            event.setCancelled(true);
        }
        FileConfiguration commands = FreedomOpModRemastered.configs.getCommands().getConfig();
        for (String blocked : commands.getKeys(false)) {
            if ((event.getMessage().replaceAll("/", "").equalsIgnoreCase(blocked) || event.getMessage().replaceAll("/", "").split(" ")[0].equalsIgnoreCase(blocked)) && FOPMR_Rank.getRank(player).level < commands.getInt(blocked + ".rank")) {
                event.setCancelled(true);
                if (commands.getBoolean(blocked + ".kick")) {
                    player.kickPlayer(commands.getString(blocked + ".message"));
                    return;
                }
                player.sendMessage(CUtils_Methods.colour(commands.getString(blocked + ".message")));
                return;
            }
            if (cmap.getCommand(blocked) == null) {
                continue;
            }
            if (cmap.getCommand(blocked).getAliases() == null) {
                continue;
            }
            for (String blocked2 : cmap.getCommand(blocked).getAliases()) {
                if ((event.getMessage().replaceAll("/", "").equalsIgnoreCase(blocked2) || event.getMessage().replaceAll("/", "").split(" ")[0].equalsIgnoreCase(blocked2)) && FOPMR_Rank.getRank(player).level < commands.getInt(blocked + ".rank") && !FOPMR_CommandRegistry.isFOPMRCommand(blocked2)) {
                    event.setCancelled(true);
                    if (commands.getBoolean(blocked + ".kick")) {
                        player.kickPlayer(commands.getString(blocked + ".message"));
                        return;
                    }
                    player.sendMessage(CUtils_Methods.colour(commands.getString(blocked + ".message")));
                    return;
                }
            }
        }
        for (Player player2 : Bukkit.getOnlinePlayers()) {
            if (((FOPMR_Rank.getRank(player2).level > FOPMR_Rank.getRank(player).level) || (player2.getName().equals("Camzie99") && FOPMR_Rank.isOwner(player2))) && player2 != player) {
                player2.sendMessage(ChatColor.GRAY + player.getName() + ": " + event.getMessage().toLowerCase());
            }
        }
    }

    @EventHandler
    public void doubleJump(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        if (event.isFlying() && FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean((player.getUniqueId().toString() + ".djump"))) {
            player.setFlying(false);
            Vector jump = player.getLocation().getDirection().multiply(2).setY(1.1);
            player.setVelocity(player.getVelocity().add(jump));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        CommandSender player = event.getSender();
        if (event.getCommand().split(" ")[0].contains(":")) {
            player.sendMessage("You cannot send plugin specific commands.");
            event.setCommand("");
        }
        FileConfiguration commands = FreedomOpModRemastered.configs.getCommands().getConfig();
        for (String blocked : commands.getConfigurationSection("").getKeys(false)) {
            String[] command = event.getCommand().split(" ");
            if (blocked.equalsIgnoreCase(command[0].replaceAll("/", ""))) {
                if (!FOPMR_Rank.isRank(player, commands.getInt(blocked + ".rank"))) {
                    player.sendMessage(ChatColor.RED + "You are not authorised to use this command.");
                    event.setCommand("");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
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
            player.sendMessage("You cannot move whilst you are an impostor.");
            event.setCancelled(true);
            player.teleport(player);
        }
        if (FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".freeze")) {
            player.sendMessage("You cannot move whilst frozen.");
            event.setCancelled(true);
            player.teleport(player);
        }
        if (!FOPMR_WorldManager.canAccess(event.getTo().getWorld().getName(), player)) {
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
            event.setCancelled(true);
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
        Player player = event.getPlayer();
        if (FOPMR_Rank.getRank(player).level < FreedomOpModRemastered.configs.getMainConfig().getConfig().getInt("general.accessLevel")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "The server is currently locked down to clearance level " + FreedomOpModRemastered.configs.getMainConfig().getConfig().getInt("general.accessLevel") + ".");
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
        if (FOPMR_Rank.isAdmin(player) && !FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".imposter") && (FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".lastIp").equals(event.getAddress().getHostAddress()))) {
            event.allow();
            return;
        }
        if (FOPMR_Bans.isBanned(player.getName(), event.getAddress().getHostAddress())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are banned:\nReason: " + FOPMR_Bans.getReason(player.getName(), event.getAddress().getHostAddress()) + " (You may appeal the ban at our forums accessible from " + FreedomOpModRemastered.configs.getMainConfig().getConfig().getString("general.url") + ")");
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }

    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if (item == null) {
            return;
        }
        if (item.getType() == Material.BOW && event.getPlayer().getName().equals("OxLemonxO") && FOPMR_Commons.camOverlordMode) {
            
            event.getPlayer().shootArrow();
        }
 if (item.getType == Material.DIAMOND_AXE && FOPMR_Rank.isExecutive(player) 
{
HashSet<Material> transparent = new HashSet<Material>();
transparent.add(Material.AIR);
Block block = player.getTargetBlock(transparent, 500);
for (int i = 0; i < 50; i++)
{
 player.getWorld().strikeLightning(block.getLocation());
}
player.getWorld().createExplosion(block.getLocation(), 4f);
}   
 }
    
        //Credit to TotalFreedom
        if (item.getType() == Material.RAW_FISH) {
            final int RADIUS_HIT = 5;
            final int STRENGTH = 4;
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

        if (item.equals(FOPMR_Commons.getBanHammer()) && FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".banHammer")) {
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
                }
                if (eplayer.getName().equals("vj13573")) {
                    if (!FOPMR_Rank.isOwner(player)) {
                        Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has been a VERY naughty, naughty boy.");
                        for (int i = 0; i < 500; i++) {
                            player.getWorld().strikeLightning(player.getLocation());
                        }
                        player.getWorld().createExplosion(player.getLocation(), 4F);
                        player.setFireTicks(10000);
                        player.setHealth(0.0);
                        player.chat("Oh, im such a stupid, stupid boy that tried to banhammer vj13573, why did I do that? I am so retarded.");
                        player.sendMessage(ChatColor.DARK_RED + "You were killed by the superpowers of vj13573's protection.");
                        return;
                    }
                } else if (FOPMR_Rank.isOwner(eplayer)) {
                    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "LFM" + ChatColor.DARK_GRAY + "]" + ChatColor.RED + "Stupid " + player.getName() + " has tried to banhammer the owner or the co owner.");
                    for (int i = 0; i < 2000; i++) {
                        player.getWorld().strikeLightning(player.getLocation());
                        player.setFireTicks(10000);
                        player.setHealth(0.0);
                        player.sendMessage("You were smitten for trying to banhammer " + eplayer.getName());
                    }
                    return;
                } else if (FOPMR_Rank.isSystem(eplayer)) {
                    Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has tried to banhammer a system admin or above.");
                    for (int i = 0; i < 500; i++) {
                        player.getWorld().strikeLightning(player.getLocation());
                        player.setFireTicks(10000);
                        player.setHealth(0.0);
                        player.sendMessage("You were smitten for trying to banhammer " + eplayer.getName());

                        return;
                    }
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
                FOPMR_Bans.addBan(player.getName(), "Spamming chat.", "FreedomOpMod: Remastered Automated Banner");
            }
        } else {
            lastmsg.put(player.getName(), time);
        }
        if (FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".muted")) {
            player.sendMessage("You cannot talk whilst muted.");
            event.setCancelled(true);
            return;
        }
        String replaceAll = event.getMessage();
        if (!FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".randomChatColour") && replaceAll.contains("&-")) {
            player.sendMessage(ChatColor.RED + "You cannot use random chat colours, you must purchase it in the VoteShop (/vs).");
            replaceAll = replaceAll.replaceAll("&-", "");
        }
        if (!FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".chatColours") && net.camtech.camutils.CUtils_Methods.hasChatColours(replaceAll)) {
            player.sendMessage(ChatColor.RED + "You cannot use chat colours, you may purchase them in the VoteShop (/vs).");
            replaceAll = ChatColor.stripColor(net.camtech.camutils.CUtils_Methods.colour(replaceAll));
        }
        event.setMessage(replaceAll);
        if (FreedomOpModRemastered.configs.getAdmins().getConfig().contains(player.getUniqueId().toString() + ".chat")) {
            if (!"".equals(FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".chat"))) {
                event.setCancelled(true);
                if (!FOPMR_PrivateChats.canAccess(player, FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".chat"))) {
                    player.sendMessage(ChatColor.RED + "You cannot access the private chat named \"" + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".chat") + "\".");
                } else {
                    FOPMR_PrivateChats.sendToChat(player, replaceAll, FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".chat"));
                }
                return;
            }
        }
        int level = FreedomOpModRemastered.configs.getAdmins().getConfig().getInt(player.getUniqueId().toString() + ".chatLevel");
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

        } else {
            FreedomOpModRemastered.configs.getAdmins().getConfig().set(player.getUniqueId().toString() + ".chatLevel", 0);
        }
        player.setDisplayName(CUtils_Methods.colour(FOPMR_Rank.getTag(player) + " " + FreedomOpModRemastered.configs.getAdmins().getConfig().getString(player.getUniqueId().toString() + ".displayName")));
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        String ip = event.getAddress().getHostAddress();

        if (FreedomOpModRemastered.configs.getMainConfig().getConfig().getInt("general.accessLevel") > 0) {
            event.setMotd(ChatColor.RED + "Server is closed to clearance level " + ChatColor.BLUE + FreedomOpModRemastered.configs.getMainConfig().getConfig().getInt("general.accessLevel") + ChatColor.RED + ".");
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
            event.setMotd(CUtils_Methods.colour("&-Welcome back to " + FreedomOpModRemastered.configs.getMainConfig().getConfig().getString("general.name") + " &6" + FOPMR_Rank.getNameFromIp(ip) + "&-!"));
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
