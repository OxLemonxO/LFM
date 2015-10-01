package net.camtech.fopmremastered;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.camtech.fopmremastered.commands.FOPMR_CommandRegistry;
import net.camtech.fopmremastered.listeners.FOPMR_BlockListener;
import net.camtech.fopmremastered.listeners.FOPMR_CamVerifyListener;
import net.camtech.fopmremastered.listeners.FOPMR_CamzieListener;
import net.camtech.fopmremastered.listeners.FOPMR_JumpListener;
import net.camtech.fopmremastered.listeners.FOPMR_PlayerListener;
import net.camtech.fopmremastered.listeners.FOPMR_TelnetListener;
import net.camtech.fopmremastered.listeners.FOPMR_ToggleableEventsListener;
import net.camtech.fopmremastered.listeners.FOPMR_WorldEditListener;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.worlds.FOPMR_WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class FreedomOpModRemastered extends JavaPlugin
{

    public static FreedomOpModRemastered plugin;
    public static FOPMR_CommandRegistry commandregistry;
    public static FOPMR_PlayerListener playerlistener;
    public static FOPMR_TelnetListener telnetlistener;
    public static FOPMR_CamVerifyListener camverifylistener;
    public static FOPMR_ToggleableEventsListener toggleableeventslistener;
    public static FOPMR_CamzieListener camzielistener;
    public static FOPMR_BlockListener blocklistener;
    public static FOPMR_JumpListener jumplistener;
    public static FOPMR_WorldEditListener worldeditlistener;
    public static SocketServer socketServer;
    public static FileConfiguration config;
    public static Thread thread;

    @Override
    public void onEnable()
    {
        plugin = this;
        PluginDescriptionFile pdf = this.getDescription();
        getLogger().log(Level.INFO, "{0}{1} v. {2} by {3} has been enabled!", new Object[]
        {
            ChatColor.BLUE, pdf.getName(), pdf.getVersion(), pdf.getAuthors()
        });
        try
        {
            FOPMR_DatabaseInterface.prepareDatabase();
        }
        catch(Exception ex)
        {
            plugin.handleException(ex);
        }
        commandregistry = new FOPMR_CommandRegistry();
        playerlistener = new FOPMR_PlayerListener();
        telnetlistener = new FOPMR_TelnetListener();
        camverifylistener = new FOPMR_CamVerifyListener();
        toggleableeventslistener = new FOPMR_ToggleableEventsListener();
        camzielistener = new FOPMR_CamzieListener();
        blocklistener = new FOPMR_BlockListener();
        jumplistener = new FOPMR_JumpListener();
        worldeditlistener = new FOPMR_WorldEditListener();
        config = this.getConfig();
        this.saveDefaultConfig();
        if(!config.getBoolean("general.owner"))
        {
            System.out.println("Welcome to the FreedomOpMod: Remastered, an Owner has not yet been defined, to set yourself to Owner please run \"/owner " + FOPMR_Commons.verifyCode + "\" in-game to set yourself to the Owner rank!");
        }
        else
        {
            FOPMR_Commons.verifyCode = null;
        }
        FOPMR_WorldManager.loadWorldsFromConfig();
        FOPMR_Announcements.setup();
        FOPMR_Rank rank = new FOPMR_Rank();
        for(Player player : Bukkit.getOnlinePlayers())
        {
            try
            {
                if(FOPMR_DatabaseInterface.getBooleanFromTable("UUID", player.getUniqueId().toString(), "IMPOSTER", "PLAYERS"))
                {
                    FOPMR_Commons.imposters.add(player.getName());
                }
            }
            catch(SQLException ex)
            {
                Logger.getLogger(FreedomOpModRemastered.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FOPMR_RestManager.sendMessage(config.getInt("rest.statusid"), "FreedomOpMod: Remastered has just been enabled.");
        socketServer = new SocketServer();
        thread = new Thread(socketServer);
        thread.start();
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
        FOPMR_RestManager.sendMessage(config.getInt("rest.statusid"), "FreedomOpMod: Remastered has just been disabled.");
        try
        {
            socketServer.sock.close();
        }
        catch(IOException ex)
        {
            Logger.getLogger(FreedomOpModRemastered.class.getName()).log(Level.SEVERE, null, ex);
        }
        FOPMR_DatabaseInterface.closeConnection(FOPMR_DatabaseInterface.getConnection());
    }

    public void handleException(Exception ex)
    {
        getLogger().log(Level.SEVERE, null, ex);
        System.out.println("An exception has occurred in LemonFreedomMod, it is very likely this was an SQLite error, please check the logs for more details!");
    }
}