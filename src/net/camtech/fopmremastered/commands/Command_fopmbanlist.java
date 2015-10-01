package net.camtech.fopmremastered.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "fopmbanlist", description = "View and manage bans.", usage = "/fopmbanlist [[clear] [names | ips | uuids]]", rank = Rank.ADMIN)
public class Command_fopmbanlist
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 0 && args.length != 2)
        {
            return false;
        }
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> ips = new ArrayList<>();
        ArrayList<String> uuids = new ArrayList<>();
        try
        {
            for(Object obj : FOPMR_DatabaseInterface.getAsArrayList(null, null, "NAME", "NAME_BANS"))
            {
                String name = (String) obj;
                if(FOPMR_DatabaseInterface.getBooleanFromTable("NAME", name, "PERM", "NAME_BANS"))
                {
                    names.add(ChatColor.RED + name);
                }
                else
                {
                    names.add(ChatColor.AQUA + name);
                }
            }
            for(Object obj : FOPMR_DatabaseInterface.getAsArrayList(null, null, "IP", "IP_BANS"))
            {
                String ip = (String) obj;
                if(FOPMR_DatabaseInterface.getBooleanFromTable("IP", ip, "PERM", "IP_BANS"))
                {
                    ips.add(ChatColor.RED + ip);
                }
                else
                {
                    ips.add(ChatColor.AQUA + ip);
                }
            }
            for(Object obj : FOPMR_DatabaseInterface.getAsArrayList(null, null, "UUID", "UUID_BANS"))
            {
                String uuid = (String) obj;
                if(FOPMR_DatabaseInterface.getBooleanFromTable("UUID", uuid, "PERM", "UUID_BANS"))
                {
                    uuids.add(ChatColor.RED + uuid);
                }
                else
                {
                    uuids.add(ChatColor.AQUA + uuid);
                }
            }
            if(args.length == 0)
            {
                String concatname = "No name bans...";
                if(!names.isEmpty())
                {
                    concatname = StringUtils.join(names, ", ");
                }
                String concatip = "No IP bans...";
                if(!ips.isEmpty())
                {
                    concatip = StringUtils.join(ips, ", ");
                }
                String concatuuid = "No UUID bans...";
                if(!uuids.isEmpty())
                {
                    concatuuid = StringUtils.join(uuids, ", ");
                }
                sender.sendMessage(ChatColor.RED + "FreedomOp Banlists:");
                sender.sendMessage(ChatColor.GREEN + "    Name Bans:");
                sender.sendMessage(ChatColor.AQUA + "        " + concatname);
                sender.sendMessage(ChatColor.GREEN + "    IP Bans:");
                sender.sendMessage(ChatColor.AQUA + "        " + concatip);
                sender.sendMessage(ChatColor.GREEN + "    UUID Bans:");
                sender.sendMessage(ChatColor.AQUA + "        " + concatuuid);
            }
            else
            {
                if("clear".equalsIgnoreCase(args[0]))
                {
                    String message;
                    Connection c = FOPMR_DatabaseInterface.getConnection();
                    String table;
                    switch(args[1].toLowerCase())
                    {
                        case "names":
                            message = "Name";
                            table = "NAME_BANS";
                            break;
                        case "ips":
                            message = "IP";
                            table = "IP_BANS";
                            break;
                        case "uuids":
                            message = "UUID";
                            table = "UUID_BANS";
                            break;
                        default:
                            return false;
                    }
                    PreparedStatement statement = c.prepareStatement("DELETE FROM " + table + " WHERE PERM = 0");
                    statement.executeUpdate();
                    c.commit();
                    sender.sendMessage(ChatColor.AQUA + message + ChatColor.GREEN + " banlist has been cleared successfully.");
                    return true;
                }
                return false;
            }
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.handleException(ex);
        }
        return true;
    }

}