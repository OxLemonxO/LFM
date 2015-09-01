package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_Commons;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.SYSTEM;
import net.camtech.fopmremastered.FOPMR_PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.ChatColor;
import net.camtech.fopmremastered.FreedomOpModRemastered;


public class Command_wrath extends FOPMR_Command
{
    public Command_wrath()
    {
       super("wrath", "/wrath <all | purge | <<name> on | off>>", "Unleash Yurippe's Wrath", SYSTEM);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(sender.getName().contains("xYurippe") | sender.getName().contains("OxLemonxO"))
            {
        if (args.length == 1)
        {

            if (args[0].equalsIgnoreCase("all"))
            {
                FOPMR_Commons.adminAction(sender.getName(), ChatColor.RED + "Unleashing Yurripe's wrath upon all players.", true);

                for (Player player : Bukkit.getServer().getOnlinePlayers())
                {
                    startLockup(player);
                }
                FOPMR_Commons.playerMsg(sender, "Wrath unleashed.", ChatColor.BLACK);
            }
            else if (args[0].equalsIgnoreCase("purge"))
            {
                FOPMR_Commons.adminAction(sender.getName(), ChatColor.AQUA + "Saving all players from Yurripe's wrath.", true);
                for (Player player : Bukkit.getServer().getOnlinePlayers())
                {
                    cancelLockup(player);
                }

                FOPMR_Commons.playerMsg(sender, "Wrath removed.", ChatColor.BLACK);
            }
            else
            {
                return false;
            }
        }
        else if (args.length == 2)
        {
            if (args[1].equalsIgnoreCase("on"))
            {
                final Player player = Bukkit.getPlayer(args[0]);

                if (player == null)
                {
                    sender.sendMessage("Player not found.");
                    return true;
                }

                FOPMR_Commons.adminAction(sender.getName(), "Unleashed Yurripe's wrath upon " + player.getName(), true);
                startLockup(player);
                FOPMR_Commons.playerMsg(sender, "Wrath unleashed " + player.getName() + ".", ChatColor.BLACK);
            }
            else if ("off".equals(args[1]))
            {
                final Player player = Bukkit.getPlayer(args[0]);

                if (player == null)
                {
                    sender.sendMessage("Player not found.");
                    return true;
                }

                FOPMR_Commons.adminAction(sender.getName(), "Saving " + player.getName() + " from Yurripe's wrath.", true);
                cancelLockup(player);
                FOPMR_Commons.playerMsg(sender, "Saved " + player.getName() + ".", ChatColor.BLACK);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }

        return true;
        
    }
        else {
      Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "LFM" + ChatColor.DARK_GRAY + "] " + ChatColor.DARK_RED + sender.getName() + " has tried to use Yurippe's command.");
        return false;
        }
    }

    private void cancelLockup(FOPMR_PlayerData playerdata)
    {
        BukkitTask lockupScheduleID = playerdata.getLockupScheduleID();
        if (lockupScheduleID != null)
        {
            lockupScheduleID.cancel();
            playerdata.setLockupScheduleID(null);
        }
    }

    private void cancelLockup(final Player player)
    {
        cancelLockup(FOPMR_PlayerData.getPlayerData(player));
    }

    private void startLockup(final Player player)
    {
        final FOPMR_PlayerData playerdata = FOPMR_PlayerData.getPlayerData(player);

        cancelLockup(playerdata);

        playerdata.setLockupScheduleID(new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (player.isOnline())
                {
                    player.openInventory(player.getInventory());
                }
                else
                {
                    cancelLockup(playerdata);
                }
            }
        }.runTaskTimer(FreedomOpModRemastered.plugin, 0L, 5L));
    }
}