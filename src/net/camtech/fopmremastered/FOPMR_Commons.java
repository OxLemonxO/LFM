package net.camtech.fopmremastered;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FOPMR_Commons
{

    public static ArrayList<String> imposters = new ArrayList<>();
    public static boolean camOverlordMode = false;
    public static boolean globalFreeze = false;
    public static String verifyCode = UUID.randomUUID().toString();

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
    
    public static boolean intToBoolean(int i)
    {
        return i == 1;
    }
}