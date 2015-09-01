package net.camtech.fopmremastered.protectedareas;



import java.util.ArrayList;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FOPMR_ProtectedArea
{
    private final String owner;
    private final String name;
    private final ArrayList<String> allowed;
    private final Rank rank;
    private final Location loc;
    private final int range;
    
    public FOPMR_ProtectedArea(String owner, String name, ArrayList<String> allowed, Rank rank, Location loc, int range)
    {
        this.owner = owner;
        this.name = name;
        this.allowed = allowed;
        this.rank = rank;
        this.loc = loc;
        this.range = range;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public String getOwner()
    {
        return this.owner;
    }
    
    public Rank getRank()
    {
        return this.rank;
    }
    
    public ArrayList<String> getAllowed()
    {
        return this.allowed;
    }
    
    public Location getLocation()
    {
        return this.loc;
    }
    
    public int getRange()
    {
        return this.range;
    }
    
    public boolean canAccess(Player player)
    {
        if(isOwner(player))
        {
            return true;
        }
        if(allowed.contains(player.getName()))
        {
            return true;
        }
        return FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(player), rank);
    }
    
    public boolean addPlayer(Player sender, Player player)
    {
        if(!isOwner(sender) && getRank().level > FOPMR_Rank.getRank(sender).level)
        {
            return false;
        }
        if(this.allowed.contains(player.getName()) || this.isOwner(player))
        {
            return false;
        }
        this.allowed.add(player.getName());
        FOPMR_ProtectedAreas.areas.set(name + ".allowed", this.allowed);
        FOPMR_ProtectedAreas.config.saveConfig();
        player.sendMessage(ChatColor.GREEN + "You have been added to the protected area " + this.name + " by " + sender.getName() + ".");
        return true;
    }
    
    public boolean removePlayer(Player sender, Player player)
    {
        if((!isOwner(sender) || !this.allowed.contains(player.getName())) && getRank().level > FOPMR_Rank.getRank(sender).level)
        {
            return false;
        }
        if(this.allowed.contains(player.getName()))
        {
            this.allowed.remove(player.getName());
            FOPMR_ProtectedAreas.areas.set(name + ".allowed", this.allowed);
            FOPMR_ProtectedAreas.config.saveConfig();
            return true;
        }
        return false;
    }
    
    public boolean isOwner(Player player)
    {
        return (this.owner == null ? player.getName() == null : this.owner.equals(player.getName()));
    }
    
    public boolean isInRange(Location location)
    {
        if(this.loc.getWorld() == location.getWorld())
        {
            return this.loc.distance(location) < range;
        }
        return false;
    }
}
