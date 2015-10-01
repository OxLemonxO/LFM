package net.camtech.fopmremastered;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
//Credit to totalfreedom


public class FOPMR_PlayerData {

    public static final Map<String, FOPMR_PlayerData> PLAYER_DATA = new HashMap<String, FOPMR_PlayerData>(); // ip,data
    public static final long AUTO_PURGE = 20L * 60L * 5L;

    public static boolean hasPlayerData(Player player) {
        return PLAYER_DATA.containsKey(player.getPlayer().getAddress().getAddress().getHostAddress().trim());
    }

    public static FOPMR_PlayerData getPlayerDataSync(Player player) {
        synchronized (PLAYER_DATA) {
            return getPlayerData(player);
        }
    }

    public static FOPMR_PlayerData getPlayerData(Player player) {
        final String ip = player.getPlayer().getAddress().getAddress().getHostAddress().trim();

        FOPMR_PlayerData data = FOPMR_PlayerData.PLAYER_DATA.get(ip);

        if (data != null) {
            return data;
        }

        if (Bukkit.getOnlineMode()) {
            for (FOPMR_PlayerData dataTest : PLAYER_DATA.values()) {
                if (dataTest.player.getName().equalsIgnoreCase(player.getName())) {
                    data = dataTest;
                    break;
                }
            }
        }

        if (data != null) {
            return data;
        }

        data = new FOPMR_PlayerData(player, player.getUniqueId(), ip);
        FOPMR_PlayerData.PLAYER_DATA.put(ip, data);

        return data;
    }
    //
    private final Player player;
    private final String ip;
    private final UUID uuid;
    //
    private BukkitTask unmuteTask;
    private BukkitTask unfreezeTask;
    private Location freezeLocation;
    private boolean isHalted = false;
    private int messageCount = 0;
    private int totalBlockDestroy = 0;
    private int totalBlockPlace = 0;
    private int freecamDestroyCount = 0;
    private int freecamPlaceCount = 0;
    private boolean isCaged = false;
    private Location cagePosition;
    private List<TFM_BlockData> cageHistory = new ArrayList<TFM_BlockData>();
    private Material cageOuterMaterial = Material.GLASS;
    private Material cageInnerMatterial = Material.AIR;
    private boolean isOrbiting = false;
    private double orbitStrength = 10.0;
    private boolean mobThrowerEnabled = false;
    private EntityType mobThrowerEntity = EntityType.PIG;
    private double mobThrowerSpeed = 4.0;
    private List<LivingEntity> mobThrowerQueue = new ArrayList<LivingEntity>();
    private BukkitTask mp44ScheduleTask = null;
    private boolean mp44Armed = false;
    private boolean mp44Firing = false;
    private BukkitTask lockupScheduleTask = null;
    private String lastMessage = "";
    private boolean inAdminchat = false;
    private boolean allCommandsBlocked = false;
    private boolean verifiedSuperadminId = false;
    private String lastCommand = "";
    private boolean cmdspyEnabled = false;
    private String tag = null;
    private int warningCount = 0;

    public FOPMR_PlayerData(Player player, UUID uuid, String ip) {
        this.player = player;
        this.uuid = uuid;
        this.ip = ip;
    }

    public String getIpAddress() {
        return this.ip;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public boolean isOrbiting() {
        return isOrbiting;
    }

    public void startOrbiting(double strength) {
        this.isOrbiting = true;
        this.orbitStrength = strength;
    }

    public void stopOrbiting() {
        this.isOrbiting = false;
    }

    public double orbitStrength() {
        return orbitStrength;
    }

    public void setCaged(boolean state) {
        this.isCaged = state;
    }

    public void setCaged(boolean state, Location location, Material outer, Material inner) {
        this.isCaged = state;
        this.cagePosition = location;
        this.cageOuterMaterial = outer;
        this.cageInnerMatterial = inner;
    }

    public boolean isCaged() {
        return isCaged;
    }

    public Material getCageMaterial(CageLayer layer) {
        switch (layer) {
            case OUTER:
                return this.cageOuterMaterial;
            case INNER:
                return this.cageInnerMatterial;
            default:
                return this.cageOuterMaterial;
        }
    }

    public Location getCagePos() {
        return cagePosition;
    }

    public void clearHistory() {
        cageHistory.clear();
    }

    public void insertHistoryBlock(Location location, Material material) {
        cageHistory.add(new TFM_BlockData(location, material));
    }

    public void regenerateHistory() {
        for (TFM_BlockData blockdata : this.cageHistory) {
            blockdata.location.getBlock().setType(blockdata.material);
        }
    }

    public Location getFreezeLocation() {
        return freezeLocation;
    }

    public boolean isFrozen() {
        return unfreezeTask != null;
    }

    public void resetMsgCount() {
        this.messageCount = 0;
    }

    public int incrementAndGetMsgCount() {
        return this.messageCount++;
    }

    public int incrementAndGetBlockDestroyCount() {
        return this.totalBlockDestroy++;
    }

    public void resetBlockDestroyCount() {
        this.totalBlockDestroy = 0;
    }

    public int incrementAndGetBlockPlaceCount() {
        return this.totalBlockPlace++;
    }

    public void resetBlockPlaceCount() {
        this.totalBlockPlace = 0;
    }

    public int incrementAndGetFreecamDestroyCount() {
        return this.freecamDestroyCount++;
    }

    public void resetFreecamDestroyCount() {
        this.freecamDestroyCount = 0;
    }

    public int incrementAndGetFreecamPlaceCount() {
        return this.freecamPlaceCount++;
    }

    public void resetFreecamPlaceCount() {
        this.freecamPlaceCount = 0;
    }

    public void enableMobThrower(EntityType mobThrowerCreature, double mobThrowerSpeed) {
        this.mobThrowerEnabled = true;
        this.mobThrowerEntity = mobThrowerCreature;
        this.mobThrowerSpeed = mobThrowerSpeed;
    }

    public void disableMobThrower() {
        this.mobThrowerEnabled = false;
    }

    public EntityType mobThrowerCreature() {
        return this.mobThrowerEntity;
    }

    public double mobThrowerSpeed() {
        return this.mobThrowerSpeed;
    }

    public boolean mobThrowerEnabled() {
        return this.mobThrowerEnabled;
    }

    public void enqueueMob(LivingEntity mob) {
        mobThrowerQueue.add(mob);
        if (mobThrowerQueue.size() > 4) {
            LivingEntity oldmob = mobThrowerQueue.remove(0);
            if (oldmob != null) {
                oldmob.damage(500.0);
            }
        }
    }

    public void startArrowShooter(FreedomOpModRemastered plugin) {
        this.stopArrowShooter();
        this.mp44ScheduleTask = new ArrowShooter(this.player).runTaskTimer(plugin, 1L, 1L);
        this.mp44Firing = true;
    }

    public void stopArrowShooter() {
        if (this.mp44ScheduleTask != null) {
            this.mp44ScheduleTask.cancel();
            this.mp44ScheduleTask = null;
        }
        this.mp44Firing = false;
    }

    public void armMP44() {
        this.mp44Armed = true;
        this.stopArrowShooter();
    }

    public void disarmMP44() {
        this.mp44Armed = false;
        this.stopArrowShooter();
    }

    public boolean isMP44Armed() {
        return this.mp44Armed;
    }

    public boolean toggleMP44Firing() {
        this.mp44Firing = !this.mp44Firing;
        return mp44Firing;
    }

    public boolean isMuted() {
        return unmuteTask != null;
    }

    public boolean isHalted() {
        return this.isHalted;
    }

    public BukkitTask getLockupScheduleID() {
        return this.lockupScheduleTask;
    }

    public void setLockupScheduleID(BukkitTask id) {
        this.lockupScheduleTask = id;
    }

    public void setLastMessage(String message) {
        this.lastMessage = message;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setAdminChat(boolean inAdminchat) {
        this.inAdminchat = inAdminchat;
    }

    public boolean inAdminChat() {
        return this.inAdminchat;
    }

    public boolean allCommandsBlocked() {
        return this.allCommandsBlocked;
    }

    public void setCommandsBlocked(boolean commandsBlocked) {
        this.allCommandsBlocked = commandsBlocked;
    }

    // If someone logs in to telnet or minecraft, and they are an admin, make sure that they are using a username that is associated with their IP.
    // After the check for this is done in TFM_PlayerListener, never change it elsewhere.
    public boolean isSuperadminIdVerified() {
        return this.verifiedSuperadminId;
    }

    // If someone logs in to telnet or minecraft, and they are an admin, make sure that they are using a username that is associated with their IP.
    // After the check for this is done in TFM_PlayerListener, never change it elsewhere.
    public void setSuperadminIdVerified(boolean verifiedSuperadminId) {
        this.verifiedSuperadminId = verifiedSuperadminId;
    }

    public String getLastCommand() {
        return lastCommand;
    }

    public void setLastCommand(String lastCommand) {
        this.lastCommand = lastCommand;
    }

    public void setCommandSpy(boolean enabled) {
        this.cmdspyEnabled = enabled;
    }

    public boolean cmdspyEnabled() {
        return cmdspyEnabled;
    }

    public String getTag() {
        return this.tag;
    }

    public int getWarningCount() {
        return this.warningCount;
    }

    public void cancel(BukkitTask task) {
        if (task == null) {
            return;
        }

        try {
            task.cancel();
        } catch (Exception ex) {
        }
    }

    public enum CageLayer {

        INNER, OUTER
    }

    private class TFM_BlockData {

        public Material material;
        public Location location;

        private TFM_BlockData(Location location, Material material) {
            this.location = location;
            this.material = material;
        }
    }

    private class ArrowShooter extends BukkitRunnable {

        private Player player;

        private ArrowShooter(Player player) {
            this.player = player;
        }

        @Override
        public void run() {
            Arrow shot = player.launchProjectile(Arrow.class);
            shot.setVelocity(shot.getVelocity().multiply(2.0));
        }
    }
}
