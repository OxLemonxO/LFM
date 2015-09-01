package net.camtech.fopmremastered.worlds;

import java.util.HashMap;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scheduler.BukkitRunnable;

public class FOPMR_WorldManager
{

    public static HashMap<World, Rank> worlds = new HashMap<>();
    public static HashMap<World, FOPMR_GuestList> guestlists = new HashMap<>();

    public static void loadWorldsFromConfig()
    {
        for(String worldName : FreedomOpModRemastered.configs.getWorlds().getConfig().getKeys(false))
        {
            if(!FreedomOpModRemastered.configs.getWorlds().getConfig().getBoolean(worldName + ".onenable"))
            {
                continue;
            }
            if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("flat"))
            {
                createNewWorld(worldName, new FOPMR_FlatGenerator(), FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
            }
            else if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("default"))
            {
                createNewWorld(worldName, FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
            }
            else if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("empty"))
            {
                createNewWorld(worldName, new FOPMR_EmptyGenerator(), FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
            }
            else if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("rollinghills"))
            {
                createNewWorld(worldName, new FOPMR_RollinghillsGenerator(), FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
            }
            else
            {
                Bukkit.broadcastMessage(ChatColor.RED + "The world: " + worldName + " could not be loaded because its generator was invalid!");
            }
        }
    }
    public static void reloadWorldsFromConfig()
    {
        for(String worldName : FreedomOpModRemastered.configs.getWorlds().getConfig().getKeys(false))
        {
            if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("flat"))
            {
                createNewWorld(worldName, new FOPMR_FlatGenerator(), FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
            }
            else if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("default"))
            {
                createNewWorld(worldName, FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
            }
            else if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("empty"))
            {
                createNewWorld(worldName, new FOPMR_EmptyGenerator(), FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
            }
            else if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("rollinghills"))
            {
                createNewWorld(worldName, new FOPMR_RollinghillsGenerator(), FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
            }
            else
            {
                Bukkit.broadcastMessage(ChatColor.RED + "The world: " + worldName + " could not be loaded because its generator was invalid!");
            }
        }
    }

    public static void loadWorld(String worldName)
    {
        if(!FreedomOpModRemastered.configs.getWorlds().getConfig().contains(worldName))
        {
            return;
        }
        if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("flat"))
        {
            createNewWorld(worldName, new FOPMR_FlatGenerator(), FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
        }
        else if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("default"))
        {
            createNewWorld(worldName, FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
        }
        else if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("empty"))
        {
            createNewWorld(worldName, new FOPMR_EmptyGenerator(), FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
        }
        else if(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".generator").equalsIgnoreCase("rollinghills"))
        {
            createNewWorld(worldName, new FOPMR_RollinghillsGenerator(), FOPMR_Rank.getFromName(FreedomOpModRemastered.configs.getWorlds().getConfig().getString(worldName + ".rank")));
        }
        else
        {
            Bukkit.broadcastMessage(ChatColor.RED + "The world: " + worldName + " could not be loaded because its generator was invalid!");
        }

    }

    public static void unloadWorlds()
    {
        for(World world : worlds.keySet())
        {
            CUtils_Methods.unloadWorld(world);
        }
    }

    public static void createNewWorld(String name, ChunkGenerator generator, Rank rank)
    {
        World world = Bukkit.getWorld(name);
        if(world == null)
        {
            WorldCreator creator = new WorldCreator(name);
            creator.generator(generator);
            world = creator.createWorld();
            worlds.put(world, rank);
            guestlists.put(world, new FOPMR_GuestList());
        }
    }

    public static void createNewWorld(String name, Rank rank)
    {
        World world = Bukkit.getWorld(name);
        if(world == null)
        {
            WorldCreator creator = new WorldCreator(name);
            world = creator.createWorld();
            worlds.put(world, rank);
            guestlists.put(world, new FOPMR_GuestList());
        }
    }

    public static void addGuest(String worldname, Player guest, Player moderator)
    {
        World world = Bukkit.getWorld(worldname);
        if(!worlds.containsKey(world))
        {
            moderator.sendMessage(ChatColor.RED + "This world cannot have guests.");
            return;
        }
        FOPMR_GuestList list = guestlists.get(world);
        if(canAccess(world.getName(), guest))
        {
            moderator.sendMessage(ChatColor.RED + "The player can already access the world.");
            return;
        }
        if(FOPMR_Rank.getRank(moderator).level < worlds.get(world).level)
        {
            moderator.sendMessage(ChatColor.RED + "You cannot add guests to this world.");
            return;
        }
        list.addGuest(guest.getName(), moderator.getName());
        moderator.sendMessage(ChatColor.GREEN + "You have added " + guest.getName() + " to the " + worldname + " guest list.");
        guest.sendMessage(ChatColor.GREEN + "You now have access to the world: " + worldname);
    }

    public static void removeGuest(String worldname, Player guest, Player moderator)
    {
        World world = Bukkit.getWorld(worldname);
        if(!worlds.containsKey(world))
        {
            moderator.sendMessage(ChatColor.RED + "This world cannot have guests.");
            return;
        }
        FOPMR_GuestList list = guestlists.get(world);
        if(!list.isGuest(guest.getName()))
        {
            moderator.sendMessage(ChatColor.RED + "This player is not a guest of this world.");
            return;
        }
        if(FOPMR_Rank.getRank(moderator).level < worlds.get(world).level)
        {
            moderator.sendMessage(ChatColor.RED + "You do not have permission to remove guests from this world.");
            return;
        }
        list.removeGuest(guest.getName());
        moderator.sendMessage(ChatColor.RED + "You have removed " + guest.getName() + " from the " + worldname + " guest list.");
    }

    public static void removeGuestsFromModerator(Player moderator)
    {
        for(FOPMR_GuestList list : guestlists.values())
        {
            list.removeGuestsFromModerator(moderator.getName());
        }
    }

    public static boolean canAccess(String name, Player player)
    {
        World world = Bukkit.getWorld(name);
        if(world == null)
        {
            return false;
        }
        if(!worlds.containsKey(world))
        {
            return true;
        }
        if(("builderworld".equals(name) || "buildernormal".equals(name)) && FOPMR_Rank.isMasterBuilder(player))
        {
            return true;
        }
        if(guestlists.get(world).isGuest(player.getName()))
        {
            return true;
        }
        return FOPMR_Rank.getRank(player).level >= worlds.get(world).level;
    }

    public static void sendToWorld(String name, Player player)
    {
        World world = Bukkit.getWorld(name);
        if(world == null)
        {
            player.sendMessage(ChatColor.RED + "The world \"" + name + "\" does not exist.");
        }
        else if(!worlds.containsKey(world))
        {
            player.sendMessage(ChatColor.GREEN + "Teleporting you to \"" + name + "\".");
            player.teleport(world.getSpawnLocation());
        }
        else if(canAccess(name, player))
        {
            player.sendMessage(ChatColor.GREEN + "Teleporting you to \"" + name + "\".");
            player.teleport(world.getSpawnLocation());
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You do not have permission to access \"" + name + "\".");
        }
    }

    public static void wipeFlatlands()
    {
        World flatlands = Bukkit.getWorld("flatlands");
        for(Player player : flatlands.getPlayers())
        {
            player.setOp(false);
            player.setWhitelisted(false);
        }
        Bukkit.getServer().setWhitelist(true);
        CUtils_Methods.unloadWorld(flatlands);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                CUtils_Methods.deleteWorld(flatlands.getWorldFolder());
                Bukkit.getServer().setWhitelist(false);
            }
        }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 5L);
        createNewWorld("flatlands", new FOPMR_FlatGenerator(), Rank.OP);
    }
}
