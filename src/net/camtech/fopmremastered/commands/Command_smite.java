package net.camtech.fopmremastered.commands;

import me.confuser.barapi.BarAPI;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name="smite", description="Smite a player.", usage="/smite [player] [reason]", rank=Rank.ADMIN)
public class Command_smite
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }
        Player player = FOPMR_Rank.getPlayer(args[0]);
        if (player == null)
        {
            sender.sendMessage("The player " + args[0] + " is not online.");
            return true;
        }
        String reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
        if (FOPMR_Rank.getRank(sender).level <= FOPMR_Rank.getRank(player).level)
        {
            sender.sendMessage(ChatColor.RED + "You cannot smite someone of an equal or higher rank than yourself.");
            return true;
        }
        smite(player, reason);
        return true;
    }

    public void smite(Player player, String reason)
    {
        Bukkit.broadcastMessage(String.format(ChatColor.GOLD + "%s has been a naughty, naughty person.\nThey have thus been smitten!\n" + ChatColor.GOLD + "Reason: %s", player.getName(), reason));
        String full = String.format(ChatColor.RED + "%s has been smitten for %s", player.getName(), reason);
        if (Bukkit.getPluginManager().isPluginEnabled("BarAPI"))
        {
            BarAPI.setMessage((full.length() <= 64 ? full : String.format("%s has been smitten!", player.getName())), 10);
        }

        //Deop
        player.setOp(false);

        //Set gamemode to survival:
        player.setGameMode(GameMode.SURVIVAL);

        //Clear inventory:
        player.getInventory().clear();

        //Strike with lightning effect:
        final Location targetPos = player.getLocation();
        final World world = player.getWorld();
        for (int x = -1; x <= 1; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                final Location strike_pos = new Location(world, targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
                world.strikeLightning(strike_pos);
            }
        }

        //Kill:
        player.setHealth(0.0);
    }

}
