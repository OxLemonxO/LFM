package net.camtech.fopmremastered.commands;

import java.util.Arrays;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;

public class Command_rd extends FOPMR_Command
{
    public Command_rd()
    {
        super("rd", "/rd", "Remove all server entities.", Arrays.asList("re"), Rank.ADMIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        int cleared = 0;
        int entities = 0;
        int worlds = 0;
        for (World world : Bukkit.getWorlds())
        {
            worlds++;
            for (Entity e : world.getEntities())
            {
                entities++;
                if (!(e instanceof LivingEntity) && !(e instanceof ItemFrame))
                {
                    e.remove();
                    cleared++;
                }
            }
        }
        Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - removing all server entities.");
        sender.sendMessage(ChatColor.RED + "Removed " + cleared + "/" + entities + " entities in " + worlds + " worlds.");
        return true;
    }

}
