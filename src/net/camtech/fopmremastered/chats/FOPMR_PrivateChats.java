package net.camtech.fopmremastered.chats;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FOPMR_PrivateChats
{

    public static boolean canAccess(Player player, String chat)
    {
        if(!isValidChat(chat))
        {
            return false;
        }
        FOPMR_PrivateChat pchat = getFromName(chat);
        return pchat.canAccess(player);
    }

    public static boolean addPlayer(Player sender, Player player, String chat)
    {
        if(!isValidChat(chat))
        {
            return false;
        }
        FOPMR_PrivateChat pchat = getFromName(chat);
        return pchat.addPlayer(sender, player);
    }

    public static boolean addChat(Player player, String chat)
    {
        if(isValidChat(chat))
        {
            return false;
        }
        FOPMR_PrivateChat pchat = new FOPMR_PrivateChat(player, chat, ChatColor.RED);
        addChat(pchat);
        return true;
    }

    public static boolean changeColour(Player player, String chat, ChatColor colour)
    {
        if(!isValidChat(chat))
        {
            return false;
        }
        FOPMR_PrivateChat pchat = getFromName(chat);
        return pchat.changeColour(player, colour);
    }

    public static boolean removePlayer(Player sender, Player player, String chat)
    {
        if(!isValidChat(chat))
        {
            return false;
        }
        FOPMR_PrivateChat pchat = getFromName(chat);
        return pchat.removePlayer(sender, player);
    }

    public static boolean removeChat(Player player, String chat)
    {
        if(!isValidChat(chat))
        {
            return false;
        }
        FOPMR_PrivateChat pchat = getFromName(chat);
        if(pchat.isOwner(player))
        {
            pchat.sendToChat(player, "::WARNING:: CHAT IS BEING REMOVED ::WARNING::");
            removeChat(pchat);
            return true;
        }
        return false;
    }

    public static ArrayList<FOPMR_PrivateChat> getFromConfig()
    {
        ArrayList<FOPMR_PrivateChat> temp = new ArrayList<>();
        try
        {
            for(Object obj : FOPMR_DatabaseInterface.getAsArrayList("NAME", null, "NAME", "CHATS"))
            {
                temp.add(getFromName((String) obj));
            }
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return temp;
    }

    public static void sendToChat(Player player, String message, String chat)
    {
        if(!isValidChat(chat))
        {
            return;
        }
        if(!canAccess(player, chat))
        {
            return;
        }
        FOPMR_PrivateChat pchat = getFromName(chat);
        pchat.sendToChat(player, message);
    }

    public static boolean isValidChat(String chat)
    {
        try
        {
            return (FOPMR_DatabaseInterface.getFromTable("NAME", chat, "NAME", "CHATS") != null);
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
            return false;
        }
    }

    public static FOPMR_PrivateChat getFromName(String name)
    {
        if(!isValidChat(name))
        {
            return null;
        }
        try
        {
            String owner = (String) FOPMR_DatabaseInterface.getFromTable("NAME", name, "OWNER", "CHATS");
            Rank rank = FOPMR_Rank.getFromName((String) FOPMR_DatabaseInterface.getFromTable("NAME", name, "RANK", "CHATS"));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> allowed = gson.fromJson((String) FOPMR_DatabaseInterface.getFromTable("NAME", name, "ALLOWED", "CHATS"), type);
            ChatColor colour = ChatColor.getByChar((String) FOPMR_DatabaseInterface.getFromTable("NAME", name, "COLOUR", "CHATS"));
            return new FOPMR_PrivateChat(owner, name, colour, allowed, rank);
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return null;
    }

    public static void addChat(FOPMR_PrivateChat chat)
    {
        try
        {
            Connection c = FOPMR_DatabaseInterface.getConnection();
            PreparedStatement statement = c.prepareStatement("INSERT INTO CHATS (NAME, OWNER, RANK, ALLOWED, COLOUR) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, chat.getName());
            statement.setString(2, chat.getOwner());
            statement.setString(3, chat.getRank().name);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            statement.setString(4, gson.toJson(chat.getAllowed(), type));
            statement.setString(5, Character.toString(chat.getColour().getChar()));
            statement.executeUpdate();
            c.commit();
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }

    public static void removeChat(FOPMR_PrivateChat chat)
    {
        if(!isValidChat(chat.getName()))
        {
            return;
        }
        try
        {
            Connection c = FOPMR_DatabaseInterface.getConnection();
            PreparedStatement statement = c.prepareStatement("DELETE FROM CHATS WHERE NAME = ?");
            statement.setString(1, chat.getName());
            statement.executeUpdate();
            c.commit();
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
    }
}