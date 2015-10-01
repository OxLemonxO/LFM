package net.camtech.fopmremastered.commands;

import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "alladmincard", usage = "/alladmincard [command]", description = "Run a command once for every admin on the admin list (? gets replaced with their name).", rank = Rank.SYSTEM)
public class Command_alladmincard
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        try
        {
            if(args.length == 0)
            {
                return false;
            }
            String baseCommand = StringUtils.join(args, " ");

            for(Player player : Bukkit.getOnlinePlayers())
            {
                for(Object result : FOPMR_DatabaseInterface.getAsArrayList("UUID", null, "UUID", "PLAYERS"))
                {
                    String uuid = (String) result;
                    if(!((String) FOPMR_DatabaseInterface.getFromTable("UUID", uuid, "RANK", "PLAYERS")).equalsIgnoreCase("Op"))
                    {
                        String out_command = baseCommand.replaceAll("\\x3f", (String) FOPMR_DatabaseInterface.getFromTable("UUID", uuid, "NAME", "PLAYERS"));
                        sender.sendMessage("Running Command: " + out_command);
                        Bukkit.dispatchCommand(sender, out_command);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }
}