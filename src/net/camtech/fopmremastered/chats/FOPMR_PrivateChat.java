package net.camtech.fopmremastered.chats;

import com.google.gson.Gson;
import java.util.ArrayList;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FOPMR_PrivateChat
{

    private final String owner;
    private final String name;
    private ChatColor colour;
    private final ArrayList<String> allowed;
    private final Rank rank;

    public FOPMR_PrivateChat(Player owner, String name, ChatColor colour)
    {
        this.owner = owner.getName();
        this.name = name;
        this.colour = colour;
        this.allowed = new ArrayList<>();
        this.rank = FOPMR_Rank.getRank(owner);
    }

    public FOPMR_PrivateChat(String owner, String name, ChatColor colour, ArrayList<String> allowed, Rank rank)
    {
        this.owner = owner;
        this.name = name;
        this.colour = colour;
        this.allowed = allowed;
        this.rank = rank;
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

    public ChatColor getColour()
    {
        return this.colour;
    }

    public ArrayList<String> getAllowed()
    {
        return this.allowed;
    }

    public void sendToChat(Player player, String message)
    {
        for(String to : allowed)
        {
            Player player2 = Bukkit.getPlayer(to);
            if(player2 != null)
            {
                player2.sendMessage(colour
                        + "[" + this.name + "] "
                        + player.getName() + ": "
                        + message);
            }
        }
        Player playerowner = Bukkit.getPlayer(this.owner);
        if(playerowner != null)
        {
            playerowner.sendMessage(colour + "[" + this.name + "] " + player.getName() + ": " + message);
        }
        for(Player admin : Bukkit.getOnlinePlayers())
        {
            if(FOPMR_Rank.getRank(admin).level > this.rank.level && !(admin.getName().equals(this.owner)) && !this.canAccess(admin))
            {
                admin.sendMessage(colour + "[" + FOPMR_PrivateChat.this.name + " (PChat Spy)] " + player.getName() + ": " + message);
            }
        }
    }

    public boolean canAccess(Player player)
    {
        if(isOwner(player))
        {
            return true;
        }
        return allowed.contains(player.getName());
    }

    public boolean changeColour(Player sender, ChatColor colour)
    {
        if(!isOwner(sender))
        {
            return false;
        }
        try
        {
            this.colour = colour;
            FOPMR_DatabaseInterface.updateInTable("NAME", this.name, Character.toString(this.colour.getChar()), "COLOUR", "CHATS");
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }

    public boolean addPlayer(Player sender, Player player)
    {
        if(!isOwner(sender))
        {
            return false;
        }
        if(this.allowed.contains(player.getName()) || this.isOwner(player))
        {
            return false;
        }
        try
        {
            this.allowed.add(player.getName());
            FOPMR_DatabaseInterface.updateInTable("NAME", this.name, (new Gson()).toJson(this.allowed), "ALLOWED", "CHATS");
            player.sendMessage(ChatColor.GREEN + "You have been added to the private chat " + this.name + " by " + sender.getName() + ". You can access this chat by typing /pchat " + this.name + " or you can leave by typing /pchat remove " + this.name + " " + player.getName() + ".");
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }

    public boolean removePlayer(Player sender, Player player)
    {
        if((!isOwner(sender) && !sender.getName().equals(player.getName())) || !this.allowed.contains(player.getName()))
        {
            return false;
        }
        if(this.allowed.contains(player.getName()))
        {
            try
            {
                this.allowed.remove(player.getName());
                FOPMR_DatabaseInterface.updateInTable("NAME", this.name, (new Gson()).toJson(this.allowed), "ALLOWED", "CHATS");
            }
            catch(Exception ex)
            {
                FreedomOpModRemastered.plugin.handleException(ex);
            }
            return true;
        }
        return false;
    }

    public boolean isOwner(Player player)
    {
        if(FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(player), rank))
        {
            return true;
        }
        return (this.owner == null ? player.getName() == null : this.owner.equals(player.getName()));
    }
}