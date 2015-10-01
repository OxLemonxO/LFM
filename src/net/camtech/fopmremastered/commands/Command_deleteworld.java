package net.camtech.fopmremastered.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import static net.camtech.fopmremastered.FOPMR_Rank.Rank.SPECIALIST;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "deleteworld", usage = "/deleteworld [world]", description = "Remove a world's entry from the config so it no longer loads.", rank = SPECIALIST)
public class Command_deleteworld
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        try
        {
            if(args.length != 1)
            {
                return false;
            }
            if(Bukkit.getWorld(args[0]) != null)
            {
                CUtils_Methods.unloadWorld(Bukkit.getWorld(args[0]));
            }
            Connection c = FOPMR_DatabaseInterface.getConnection();
            PreparedStatement statement = c.prepareStatement("DELETE FROM WORLDS WHERE NAME = ?");
            statement.setString(1, args[0]);
            statement.executeUpdate();
            c.commit();
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }
}