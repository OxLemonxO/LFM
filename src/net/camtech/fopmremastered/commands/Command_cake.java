package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Command_cake extends FOPMR_Command
{
    public Command_cake()
    {
        super("cake", "/cake", "Give everyone a cake.", Rank.OP);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Bukkit.broadcastMessage(CUtils_Methods.randomChatColour() + sender.getName() + " - Everyone loves Cake! " + CUtils_Methods.randomChatColour() + "Enjoy!");   
        for (Player player : Bukkit.getOnlinePlayers())
        {
            ItemStack cookie = new ItemStack(Material.CAKE, 1);
            ItemMeta cookiemeta = cookie.getItemMeta();
            cookiemeta.setLore(Arrays.asList(CUtils_Methods.randomChatColour() + "Keep on trying 'til you run out of cake :)", CUtils_Methods.randomChatColour() + " - " + sender.getName()));
            cookiemeta.setDisplayName(CUtils_Methods.randomChatColour() + "The Lie");
            cookie.setItemMeta(cookiemeta);
            player.getInventory().addItem(cookie);
        }
        return true;
    }

}
