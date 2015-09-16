package net.camtech.fopmremastered;

import java.io.File;
import java.util.logging.Level;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.commands.FOPMR_CommandRegistry;
import net.camtech.fopmremastered.listeners.FOPMR_BlockListener;
import net.camtech.fopmremastered.listeners.FOPMR_CamVerifyListener;
import net.camtech.fopmremastered.listeners.FOPMR_CamzieListener;
import net.camtech.fopmremastered.listeners.FOPMR_JumpListener;
import net.camtech.fopmremastered.listeners.FOPMR_PlayerListener;
import net.camtech.fopmremastered.listeners.FOPMR_TelnetListener;
import net.camtech.fopmremastered.listeners.FOPMR_ToggleableEventsListener;
import net.camtech.fopmremastered.listeners.FOPMR_VoteListener;
import net.camtech.fopmremastered.worlds.FOPMR_WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.function.Function;
import org.bukkit.plugin.ServicePriority;

public class FreedomOpModRemastered extends JavaPlugin
{

    public static FreedomOpModRemastered plugin;
    public static FOPMR_Configs configs;
    public static FOPMR_CommandRegistry commandregistry;
    public static FOPMR_PlayerListener playerlistener;
    public static FOPMR_TelnetListener telnetlistener;
    public static FOPMR_CamVerifyListener camverifylistener;
    public static FOPMR_ToggleableEventsListener toggleableeventslistener;
    public static FOPMR_CamzieListener camzielistener;
    public static FOPMR_VoteListener votelistener;
    public static FOPMR_BlockListener blocklistener;
    public static FOPMR_JumpListener jumplistener;

    
    @Override
    public void onEnable()
    {
        
        plugin = this;
        PluginDescriptionFile pdf = this.getDescription();
        getLogger().log(Level.INFO, "{0}{1} v. {2} by {3} has been enabled!", new Object[]
        {
            ChatColor.BLUE, pdf.getName(), pdf.getVersion(), pdf.getAuthors()
        });
        configs = FreedomOpModRemasteredConfigs.configs;
        if(configs.getMainConfig().getConfig().getBoolean("general.wipe"))
        {
            Bukkit.broadcastMessage("Wiping main world.");
            configs.getMainConfig().getConfig().set("general.wipe", false);
            CUtils_Methods.deleteWorld(new File("world"));
        }
        commandregistry = new FOPMR_CommandRegistry();
        playerlistener = new FOPMR_PlayerListener();
        telnetlistener = new FOPMR_TelnetListener();
        camverifylistener = new FOPMR_CamVerifyListener();
        toggleableeventslistener = new FOPMR_ToggleableEventsListener();
        camzielistener = new FOPMR_CamzieListener();
        votelistener = new FOPMR_VoteListener();
        blocklistener = new FOPMR_BlockListener();
        jumplistener = new FOPMR_JumpListener();
        FOPMR_WorldManager.loadWorldsFromConfig();
        FOPMR_Announcements.setup();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            FileConfiguration config = configs.getAdmins().getConfig();
            if (config.getBoolean(player.getUniqueId().toString() + ".imposter"))
            {
                FOPMR_Commons.imposters.add(player.getName());
            }
        }
        FOPMR_RestManager.sendMessage(configs.getMainConfig().getConfig().getInt("rest.statusid"), "FreedomOpMod: Remastered has just been enabled.");
        this.getServer().getServicesManager().register(Function.class, FOPMR_Rank.ADMIN_SERVICE, plugin, ServicePriority.Highest);
    }
    

    @Override
    public void onDisable()
    {
        getLogger().log(Level.INFO, "{0}Unloading all FOPM: R Worlds", ChatColor.RED);
        FOPMR_WorldManager.unloadWorlds();
        getLogger().log(Level.INFO, "{0}Unloading all FOPM: R Listeners", ChatColor.RED);
        HandlerList.unregisterAll(plugin);
        getLogger().log(Level.INFO, "{0}Unloading all FOPM: R Commands", ChatColor.RED);
        FOPMR_CommandRegistry.unregisterCommands();
        PluginDescriptionFile pdf = this.getDescription();
        getLogger().log(Level.INFO, "{0}{1} has been disabled!", new Object[]
        {
            ChatColor.RED, pdf.getName()
        });
        FOPMR_RestManager.sendMessage(configs.getMainConfig().getConfig().getInt("rest.statusid"), "FreedomOpMod: Remastered has just been disabled.");
    }
}
