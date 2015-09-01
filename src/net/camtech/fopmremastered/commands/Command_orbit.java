package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_PlayerData;
import net.camtech.fopmremastered.FOPMR_Commons;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.ADMIN;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.Bukkit;


public class Command_orbit extends FOPMR_Command
{
    public Command_orbit()
    {
    super("orbit", "/orbit <target> [<<power> | stop>]", "POW!!! Right in the kisser! One of these days Alice, straight to the Moon!", ADMIN);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null)
        {
            FOPMR_Commons.playerMsg(sender,"Player not found.", ChatColor.RED);
            return true;
        }

        FOPMR_PlayerData playerdata = FOPMR_PlayerData.getPlayerData(player);

        double strength = 10.0;

        if (args.length >= 2)
        {
            if (args[1].equals("stop"))
            {
                FOPMR_Commons.playerMsg(sender,"Stopped orbiting " + player.getName());
                playerdata.stopOrbiting();
                return true;
            }

            try
            {
                strength = Math.max(1.0, Math.min(150.0, Double.parseDouble(args[1])));
            }
            catch (NumberFormatException ex)
            {
                FOPMR_Commons.playerMsg(sender, ex.getMessage(), ChatColor.RED);
                return true;
            }
        }

        player.setGameMode(GameMode.SURVIVAL);
        playerdata.startOrbiting(strength);

        player.setVelocity(new Vector(0, strength, 0));
       FOPMR_Commons.adminAction(sender.getName(), "Orbiting " + player.getName(), false);

        return true;
    }
}