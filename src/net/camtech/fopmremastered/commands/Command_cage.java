package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_PlayerData;
import net.camtech.fopmremastered.FOPMR_Commons;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.ADMIN;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import net.camtech.fopmremastered.FOPMR_CageHelper;


public class Command_cage extends FOPMR_Command
{
    public Command_cage()
    {
        super("cage", "/cage <purge | off | <partialname> [outermaterial] [innermaterial]>", "Place a box around a player.", ADMIN);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        if ("off".equals(args[0]) && sender instanceof Player)
        {
            Player sender_p = (Player) sender;
            FOPMR_Commons.adminAction(sender.getName(), "Uncaging " + sender.getName(), true);
            FOPMR_PlayerData playerdata = FOPMR_PlayerData.getPlayerData(sender_p);

            playerdata.setCaged(false);
            playerdata.regenerateHistory();
            playerdata.clearHistory();

            return true;
        }
        else if ("purge".equals(args[0]))
        {
            FOPMR_Commons.adminAction(sender.getName(), "Uncaging all players", true);

            for (Player player : Bukkit.getServer().getOnlinePlayers())
            {
                FOPMR_PlayerData playerdata = FOPMR_PlayerData.getPlayerData(player);
                playerdata.setCaged(false);
                playerdata.regenerateHistory();
                playerdata.clearHistory();
            }

            return true;
        }

        final Player player = Bukkit.getPlayer(args[0]);

        if (player == null)
        {
            sender.sendMessage("Player not found.");
            return true;
        }

        FOPMR_PlayerData playerdata = FOPMR_PlayerData.getPlayerData(player);

        Material outerMaterial = Material.GLASS;
        Material innerMaterial = Material.AIR;

        if (args.length >= 2)
        {
            if ("off".equals(args[1]))
            {
                FOPMR_Commons.adminAction(sender.getName(), "Uncaging " + player.getName(), true);

                playerdata.setCaged(false);
                playerdata.regenerateHistory();
                playerdata.clearHistory();

                return true;
            }
            else
            {
                if ("darth".equalsIgnoreCase(args[1]))
                {
                    outerMaterial = Material.SKULL;
                }
                else if (Material.matchMaterial(args[1]) != null)
                {
                    outerMaterial = Material.matchMaterial(args[1]);
                }
            }
        }

        if (args.length >= 3)
        {
            if (args[2].equalsIgnoreCase("water"))
            {
                innerMaterial = Material.STATIONARY_WATER;
            }
            else if (args[2].equalsIgnoreCase("lava"))
            {
                innerMaterial = Material.STATIONARY_LAVA;
            }
        }

        Location targetPos = player.getLocation().clone().add(0, 1, 0);
        playerdata.setCaged(true, targetPos, outerMaterial, innerMaterial);
        playerdata.regenerateHistory();
        playerdata.clearHistory();
        FOPMR_CageHelper.buildHistory(targetPos, 2, playerdata);
        FOPMR_CageHelper.generateHollowCube(targetPos, 2, outerMaterial);
        FOPMR_CageHelper.generateCube(targetPos, 1, innerMaterial);

        player.setGameMode(GameMode.SURVIVAL);

        if (outerMaterial != Material.SKULL)
        {
            FOPMR_Commons.adminAction(sender.getName(), "Caging " + player.getName(), true);
        }
        else
        {
            FOPMR_Commons.adminAction(sender.getName(), "Caging " + player.getName() + " in PURE_DARTH", true);
        }

        return true;
        
    }
}