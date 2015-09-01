package net.camtech.fopmremastered.commands;

import java.util.ArrayList;
import java.util.List;
import net.camtech.fopmremastered.FOPMR_Commons;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.SYSTEM;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;



public class Command_expel extends FOPMR_Command
{
    public Command_expel()
    {
       super("expel", "/expel", "Push players nearby.", SYSTEM);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        double radius = 40.0;
        double strength = 20.0;
         if (!(sender instanceof Player))
            {
                sender.sendMessage("This command is ingame-only.");
                return false;
            }
        if (args.length >= 1)
        {
            try
            {
                radius = Math.max(1.0, Math.min(100.0, Double.parseDouble(args[0])));
            }
            catch (NumberFormatException ex)
            {
            }
        }

        if (args.length >= 2)
        {
            try
            {
                strength = Math.max(0.0, Math.min(50.0, Double.parseDouble(args[1])));
            }
            catch (NumberFormatException ex)
            {
            }
        }

        List<String> pushedPlayers = new ArrayList<String>();
        
         Player sender_p = (Player) sender;
         
        final Vector senderPos = sender_p.getLocation().toVector();
        final List<Player> players = sender_p.getWorld().getPlayers();
        for (final Player player : players)
        {
            if (player.equals(sender_p))
            {
                continue;
            }

            final Location targetPos = player.getLocation();
            final Vector targetPosVec = targetPos.toVector();

            boolean inRange = false;
            try
            {
                inRange = targetPosVec.distanceSquared(senderPos) < (radius * radius);
            }
            catch (IllegalArgumentException ex)
            {
            }

            if (inRange)
            {
                player.getWorld().createExplosion(targetPos, 0.0f, false);
                player.setAllowFlight(true);
                player.setFlying(false);
                player.setVelocity(targetPosVec.subtract(senderPos).normalize().multiply(strength));
                pushedPlayers.add(player.getName());
            }
        }

        if (pushedPlayers.isEmpty())
        {
            FOPMR_Commons.playerMsg(sender,"No players pushed.");
        }
        else
        {
            FOPMR_Commons.playerMsg(sender,"Pushed " + pushedPlayers.size() + " players: " + StringUtils.join(pushedPlayers, ", "));
        }

        return true;
    }
}