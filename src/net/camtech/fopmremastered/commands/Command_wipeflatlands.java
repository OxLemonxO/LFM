package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class Command_wipeflatlands extends FOPMR_Command
{

    public Command_wipeflatlands()
    {
        super("wipeflatlands", "/wipeflatlands", "Wipe the flatlands.", Rank.EXECUTIVE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        World flatlands = Bukkit.getWorld("flatlands");
        CUtils_Methods.unloadWorld(flatlands);
        CUtils_Methods.deleteWorld(flatlands.getWorldFolder());
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
            }
        }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 5L);
        Bukkit.broadcastMessage(ChatColor.GREEN + sender.getName() + " - Wiping flatlands.");
        return true;
    }

}
