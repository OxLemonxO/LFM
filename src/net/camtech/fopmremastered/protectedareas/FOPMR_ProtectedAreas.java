package net.camtech.fopmremastered.protectedareas;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FOPMR_ProtectedAreas
{

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
        for(FOPMR_ProtectedArea area : getFromDatabase())
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

        if(sphere.getX() < min.getX())
        {
            d -= square(sphere.getX() - min.getX());
        }
        else if(sphere.getX() > max.getX())
        {
            d -= square(sphere.getX() - max.getX());
        }
        if(sphere.getY() < min.getY())
        {
            d -= square(sphere.getY() - min.getY());
        }
        else if(sphere.getY() > max.getY())
        {
            d -= square(sphere.getY() - max.getY());
        }
        if(sphere.getZ() < min.getZ())
        {
            d -= square(sphere.getZ() - min.getZ());
        }
        else if(sphere.getZ() > max.getZ())
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

    public static ArrayList<FOPMR_ProtectedArea> getFromDatabase()
    {
        ArrayList<FOPMR_ProtectedArea> temp = new ArrayList<>();
        try
        {
            for(Object object : FOPMR_DatabaseInterface.getAsArrayList("NAME", null, "NAME", "AREAS"))
            {
                if(object instanceof String)
                {
                    temp.add(getFromName((String) object));
                }
            }
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return temp;
    }

    public static boolean isValidArea(String area)
    {
        try
        {
            return (FOPMR_DatabaseInterface.getFromTable("NAME", area, "NAME", "AREAS") != null);
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
            return false;
        }
    }

    public static FOPMR_ProtectedArea getFromName(String name)
    {
        if(!isValidArea(name))
        {
            return null;
        }
        try
        {
            String owner = (String) FOPMR_DatabaseInterface.getFromTable("NAME", name, "OWNER", "AREAS");;
            Rank rank = FOPMR_Rank.getFromName((String) FOPMR_DatabaseInterface.getFromTable("NAME", name, "RANK", "AREAS"));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> allowed = gson.fromJson((String) FOPMR_DatabaseInterface.getFromTable("NAME", name, "ALLOWED", "AREAS"), type);
            double x = (Double) FOPMR_DatabaseInterface.getFromTable("NAME", name, "X", "AREAS");
            double y = (Double) FOPMR_DatabaseInterface.getFromTable("NAME", name, "Y", "AREAS");
            double z = (Double) FOPMR_DatabaseInterface.getFromTable("NAME", name, "Z", "AREAS");
            World world = Bukkit.getWorld((String) FOPMR_DatabaseInterface.getFromTable("NAME", name, "WORLD", "AREAS"));
            Location loc = new Location(world, x, y, z);
            int range = (Integer) FOPMR_DatabaseInterface.getFromTable("NAME", name, "RANGE", "AREAS");
            return new FOPMR_ProtectedArea(owner, name, allowed, rank, loc, range);
        }
        catch(SQLException | JsonSyntaxException ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
            return null;
        }
    }

    public static void addArea(FOPMR_ProtectedArea area)
    {
        try
        {
            Connection c = FOPMR_DatabaseInterface.getConnection();
            PreparedStatement statement = c.prepareStatement("INSERT OR IGNORE INTO AREAS (NAME, OWNER, RANK, WORLD, X, Y, Z, RANGE, ALLOWED) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, area.getName());
            statement.setString(2, area.getOwner());
            statement.setString(3, area.getRank().name);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            statement.setString(9, gson.toJson(area.getAllowed(), type));
            statement.setInt(5, (int) area.getLocation().getX());
            statement.setInt(6, (int) area.getLocation().getY());
            statement.setInt(7, (int) area.getLocation().getZ());
            statement.setString(4, area.getLocation().getWorld().getName());
            statement.setInt(8, area.getRange());
            statement.execute();
            c.commit();
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    public static void removeArea(FOPMR_ProtectedArea area)
    {
        if(!isValidArea(area.getName()))
        {
            return;
        }
        try
        {
            Connection c = FOPMR_DatabaseInterface.getConnection();
            PreparedStatement statement = c.prepareStatement("DELETE FROM AREAS WHERE NAME = ?");
            statement.setString(1, area.getName());
            statement.executeUpdate();
            c.commit();
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }
}