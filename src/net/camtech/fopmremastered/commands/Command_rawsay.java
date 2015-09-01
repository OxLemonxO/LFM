package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Command_rawsay extends FOPMR_Command
{

    public Command_rawsay()
    {
        super("rawsay", "/rawsay [message]", "Send a raw chat message.", Rank.SENIOR);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }
        Bukkit.broadcastMessage(CUtils_Methods.colour(StringUtils.join(ArrayUtils.subarray(args, 0, args.length), " ")));
        return true;
    }

}
