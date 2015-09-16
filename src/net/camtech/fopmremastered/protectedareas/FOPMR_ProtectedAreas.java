package net.camtech.fopmremastered.protectedareas;

import java.util.ArrayList;
import net.camtech.fopmremastered.FOPMR_Config;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FOPMR_ProtectedAreas
{

    public static FOPMR_Config config = FreedomOpModRemastered.configs.getAreas();
    public static FileConfiguration areas = FreedomOpModRemastered.configs.getAreas().getConfig();

    public static boolean canAccess(Player player, String area)
    {
        if(!isValidArea(area))
        {
            return false;
        }
        FOPMR_ProtectedArea parea = getFromName(area);
        return parea.canAccess(player);
    }

    //MASSIVE CREDIT TO TOTALFREEDOM FOR THIS
    public static ArrayList<FOPMR_ProtectedArea> areasIn(final Vector min, final Vector max, final String worldName)
    {
        ArrayList<FOPMR_ProtectedArea> tempareas = new ArrayList<>();
        for(FOPMR_ProtectedArea area : getFromConfig())
        {
            if(worldName.equals(area.getLocation().getWorld().getName()))
            {
                if(cubeIntersectsSphere(min, max, area.getLocation().toVector(), area.getRange()))
                {
                    tempareas.add(area);
                }
            }
        }
        return tempareas;
    }
    
    //MASSIVE CREDIT TO TOTALFREEDOM FOR THIS
    private static boolean cubeIntersectsSphere(Vector min, Vector max, Vector sphere, double radius)
    {
        double d = square(radius);

        if (sphere.getX() < min.getX())
        {
            d -= square(sphere.getX() - min.getX());
        }
        else if (sphere.getX() > max.getX())
        {
            d -= square(sphere.getX() - max.getX());
        }
        if (sphere.getY() < min.getY())
        {
            d -= square(sphere.getY() - min.getY());
        }
        else if (sphere.getY() > max.getY())
        {
            d -= square(sphere.getY() - max.getY());
        }
        if (sphere.getZ() < min.getZ())
        {
            d -= square(sphere.getZ() - min.getZ());
        }
        else if (sphere.getZ() > max.getZ())
        {
            d -= square(sphere.getZ() - max.getZ());
        }

        return d > 0;
    }

    private static double square(double v)
    {
        return v * v;
    }

    public static boolean addPlayer(Player sender, Player player, String area)
    {
        if(!isValidArea(area))
        {
            return false;
        }
        FOPMR_ProtectedArea parea = getFromName(area);
        return parea.addPlayer(sender, player);
    }

    public static boolean addArea(Player player, String area, Rank rank, Location loc, int range)
    {
        if(isValidArea(area))
        {
            return false;
        }
        FOPMR_ProtectedArea narea = new FOPMR_ProtectedArea(player.getName(), area, new ArrayList<String>(), rank, loc, range);
        addArea(narea);
        return true;
    }

    public static boolean removePlayer(Player sender, Player player, String area)
    {
        if(!isValidArea(area))
        {
            return false;
        }
        FOPMR_ProtectedArea parea = getFromName(area);
        return parea.removePlayer(sender, player);
    }

    public static boolean removeArea(Player player, String area)
    {
        if(!isValidArea(area))
        {
            return false;
        }
        FOPMR_ProtectedArea parea = getFromName(area);
        if(parea.isOwner(player) || parea.getRank().level <= FOPMR_Rank.getRank(player).level)
        {
            removeArea(parea);
            player.sendMessage("Deleted area successfully!");
            return true;
        }
        return false;
    }

    public static ArrayList<FOPMR_ProtectedArea> getFromConfig()
    {
        ArrayList<FOPMR_ProtectedArea> temp = new ArrayList<>();
        for(String area : areas.getKeys(false))
        {
            temp.add(getFromName(area));
        }
        return temp;
    }

    public static boolean isValidArea(String area)
    {
        for(String configarea : areas.getKeys(false))
        {
            if(area.equalsIgnoreCase(configarea))
            {
                return true;
            }
        }
        return false;
    }

    public static FOPMR_ProtectedArea getFromName(String name)
    {
        if(!isValidArea(name))
        {
            return null;
        }
        for(String check : areas.getKeys(false))
        {
            if(check.equalsIgnoreCase(name))
            {
                String owner = areas.getString(name + ".owner");
                Rank rank = FOPMR_Rank.getFromName(areas.getString(name + ".rank"));
                ArrayList<String> allowed = new ArrayList<>(areas.getStringList(name + ".allowed"));
                int x = areas.getInt(name + ".x");
                int y = areas.getInt(name + ".y");
                int z = areas.getInt(name + ".z");
                World world = Bukkit.getWorld(areas.getString(name + ".world"));
                Location loc = new Location(world, x, y, z);
                int range = areas.getInt(name + ".range");
                return new FOPMR_ProtectedArea(owner, name, allowed, rank, loc, range);
            }
        }
        return null;
    }

    public static void addArea(FOPMR_ProtectedArea area)
    {
        areas.set(area.getName() + ".owner", area.getOwner());
        areas.set(area.getName() + ".rank", area.getRank().name);
        areas.set(area.getName() + ".allowed", area.getAllowed());
        areas.set(area.getName() + ".x", area.getLocation().getX());
        areas.set(area.getName() + ".y", area.getLocation().getY());
        areas.set(area.getName() + ".z", area.getLocation().getZ());
        areas.set(area.getName() + ".world", area.getLocation().getWorld().getName());
        areas.set(area.getName() + ".range", area.getRange());
        config.saveConfig();
    }

    public static void removeArea(FOPMR_ProtectedArea area)
    {
        if(!isValidArea(area.getName()))
        {
            return;
        }
        areas.set(area.getName(), null);
        config.saveConfig();
    }
}