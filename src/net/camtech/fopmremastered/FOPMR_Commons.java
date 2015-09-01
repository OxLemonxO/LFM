package net.camtech.fopmremastered;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FOPMR_Commons
{

    public static ArrayList<String> imposters = new ArrayList<>();
    public static boolean camOverlordMode = false;
    public static boolean globalFreeze = false;

    public static ItemStack getBanHammer()
    {
        ItemStack banhammer = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta banhammermeta = banhammer.getItemMeta();
        banhammermeta.setLore(Arrays.asList(ChatColor.BLUE + "Unleash the power of...", ChatColor.YELLOW + "The BanHammer!"));
        banhammermeta.setDisplayName(ChatColor.RED + "BanHammer!");
        banhammer.setItemMeta(banhammermeta);
        return banhammer;
    }

    public static void playerMsg(CommandSender sender, String msg, ChatColor colour)
    {
        sender.sendMessage(colour + msg);
    }

    public static void playerMsg(CommandSender sender, String msg)
    {
        playerMsg(sender, msg, ChatColor.RED);
    }

    public static void adminAction(String name, String msg, boolean isRed)
    {
        Bukkit.broadcastMessage((isRed ? ChatColor.RED : ChatColor.DARK_AQUA) + name + " - " + msg);
    }

    public static void openVoteShop(Player player)
    {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + "" + ChatColor.BOLD + "$$ " + ChatColor.GOLD + "VoteShop " + ChatColor.GREEN + "" + ChatColor.BOLD + "$$");
        ItemStack randomChat = new ItemStack(Material.SIGN, 1);
        ItemMeta randomChatMeta = randomChat.getItemMeta();
        if(!FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".randomChatColour"))
        {
            randomChatMeta.setDisplayName(ChatColor.BLUE + "Random Chat Colours");
            randomChatMeta.setLore(Arrays.asList(ChatColor.GREEN + "Gain access to use &- in chat, nicks and tags.", ChatColor.GREEN + "&- Randomly colours the following text!", ChatColor.GREEN + "Price: 3 Votes"));
        }
        else
        {
            randomChatMeta.setDisplayName(ChatColor.RED + "Random Chat Colours");
            randomChatMeta.setLore(Arrays.asList(ChatColor.GREEN + "Gain access to use &- in chat, nicks and tags.", ChatColor.GREEN + "&- Randomly colours the following text!", ChatColor.RED + "[PURCHASED]"));
        }
        randomChat.setItemMeta(randomChatMeta);
        inv.setItem(1, randomChat);
        ItemStack chat = new ItemStack(Material.PAPER, 1);
        ItemMeta chatMeta = randomChat.getItemMeta();
        if(!FreedomOpModRemastered.configs.getAdmins().getConfig().getBoolean(player.getUniqueId().toString() + ".chatColours"))
        {
            chatMeta.setDisplayName(ChatColor.BLUE + "Chat Colours");
            chatMeta.setLore(Arrays.asList(ChatColor.GREEN + "Gain access to use colours in chat, nicks and tags.", ChatColor.GREEN + "Price: 3 Votes"));
        }
        else
        {
            chatMeta.setDisplayName(ChatColor.RED + "Chat Colours");
            chatMeta.setLore(Arrays.asList(ChatColor.GREEN + "Gain access to use colours in chat, nicks and tags.", ChatColor.RED + "[PURCHASED]"));
        }
        chat.setItemMeta(chatMeta);
        inv.setItem(7, chat);
        ItemStack votes = new ItemStack(Material.DIAMOND, 1);
        ItemMeta votesMeta = votes.getItemMeta();
        votesMeta.setDisplayName(ChatColor.BLUE + "You have " + ChatColor.GOLD + FreedomOpModRemastered.configs.getAdmins().getConfig().getInt(player.getUniqueId().toString() + ".votes") + ChatColor.BLUE + " votes.");
        votes.setItemMeta(votesMeta);
        inv.setItem(13, votes);
        player.openInventory(inv);
    }

    public static <T> List<List<T>> chopped(List<T> list, final int L)
    {
        List<List<T>> parts = new ArrayList<>();
        final int N = list.size();
        for(int i = 0; i < N; i += L)
        {
            parts.add(new ArrayList<>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }
}
