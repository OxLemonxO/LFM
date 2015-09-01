package net.camtech.fopmremastered.chats;



import java.util.ArrayList;
import net.camtech.fopmremastered.FOPMR_Config;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FOPMR_PrivateChats
{
    public static FOPMR_Config config = FreedomOpModRemastered.configs.getChats();
    public static FileConfiguration chats = FreedomOpModRemastered.configs.getChats().getConfig();
    
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
        chats.getKeys(false).stream().forEach((chat) ->
        {
            temp.add(getFromName(chat));
        });
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
        return chats.getKeys(false).stream().anyMatch((_item) -> (chat.equalsIgnoreCase(_item)));
    }
    
    public static FOPMR_PrivateChat getFromName(String name)
    {
        if(!isValidChat(name))
        {
            return null;
        }
        for(String check : chats.getKeys(false))
        {
            if(check.equalsIgnoreCase(name))
            {
                String owner = chats.getString(name + ".owner");
                Rank rank = FOPMR_Rank.getFromName(chats.getString(name + ".rank"));
                ArrayList<String> allowed = new ArrayList<>(chats.getStringList(name + ".allowed"));
                ChatColor colour = ChatColor.getByChar(chats.getString(name + ".colour"));
                return new FOPMR_PrivateChat(owner, name, colour, allowed, rank);
            }
        }
        return null;
    }
    
    public static void addChat(FOPMR_PrivateChat chat)
    {
        chats.set(chat.getName() + ".owner", chat.getOwner());
        chats.set(chat.getName() + ".rank", chat.getRank().name);
        chats.set(chat.getName() + ".allowed", chat.getAllowed());
        chats.set(chat.getName() + ".colour", chat.getColour().getChar());
        config.saveConfig();
    }
    
    public static void removeChat(FOPMR_PrivateChat chat)
    {
        if(!isValidChat(chat.getName()))
        {
            return;
        }
        chats.set(chat.getName(), null);
        config.saveConfig();
    }
}
