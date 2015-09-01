package net.camtech.fopmremastered.commands;

import java.util.ArrayList;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name="fopmbanlist", description="View and manage bans.", usage="/fopmbanlist [[clear] [names | ips | uuids]]", rank=Rank.ADMIN)
public class Command_fopmbanlist
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length != 0 && args.length != 2)
        {
            return false;
        }
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> ips = new ArrayList<>();
        ArrayList<String> uuids = new ArrayList<>();

        for (String name : FreedomOpModRemastered.configs.getBans().getConfig().getConfigurationSection("names").getKeys(false))
        {
            if (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean("names." + name + ".perm"))
            {
                names.add(ChatColor.RED + name);
            }
            else
            {
                names.add(ChatColor.AQUA + name);
            }
        }
        for (String ip : FreedomOpModRemastered.configs.getBans().getConfig().getConfigurationSection("ips").getKeys(false))
        {
            if (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean("ips." + ip + ".perm"))
            {
                ips.add(ChatColor.RED + ip.replaceAll("-", "\\."));
            }
            else
            {
                ips.add(ChatColor.AQUA + ip.replaceAll("-", "\\."));
            }
        }
        for (String uuid : FreedomOpModRemastered.configs.getBans().getConfig().getConfigurationSection("uuids").getKeys(false))
        {
            if (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean("uuids." + uuid + ".perm"))
            {
                uuids.add(ChatColor.RED + uuid);
            }
            else
            {
                uuids.add(ChatColor.AQUA + uuid);
            }
        }
        if (args.length == 0)
        {
            String concatname = "No name bans...";
            if (!names.isEmpty())
            {
                concatname = StringUtils.join(names, ", ");
            }
            String concatip = "No IP bans...";
            if (!ips.isEmpty())
            {
                concatip = StringUtils.join(ips, ", ");
            }
            String concatuuid = "No UUID bans...";
            if (!uuids.isEmpty())
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
            if ("clear".equalsIgnoreCase(args[0]))
            {
                String message;
                switch (args[1].toLowerCase())
                {
                    case "names":
                        message = "Name";
                        for (String name : FreedomOpModRemastered.configs.getBans().getConfig().getConfigurationSection("names").getKeys(false))
                        {
                            if (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean("names." + name + ".perm"))
                            {
                                continue;
                            }
                            FreedomOpModRemastered.configs.getBans().getConfig().set("names." + name, null);
                            FreedomOpModRemastered.configs.getBans().saveConfig();
                        }
                        break;
                    case "ips":
                        message = "IP";
                        for (String ip : FreedomOpModRemastered.configs.getBans().getConfig().getConfigurationSection("ips").getKeys(false))
                        {
                            if (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean("ips." + ip.replaceAll("\\.", "-") + ".perm"))
                            {
                                continue;
                            }
                            FreedomOpModRemastered.configs.getBans().getConfig().set("ips." + ip.replaceAll("\\.", "-"), null);
                            FreedomOpModRemastered.configs.getBans().saveConfig();
                        }
                        break;
                    case "uuids":
                        message = "UUID";
                        for (String uuid : FreedomOpModRemastered.configs.getBans().getConfig().getConfigurationSection("uuids").getKeys(false))
                        {
                            if (FreedomOpModRemastered.configs.getBans().getConfig().getBoolean("uuids." + uuid + ".perm"))
                            {
                                continue;
                            }
                            FreedomOpModRemastered.configs.getBans().getConfig().set("uuids." + uuid, null);
                            FreedomOpModRemastered.configs.getBans().saveConfig();
                        }
                        break;
                    default:
                        return false;
                }
                sender.sendMessage(ChatColor.AQUA + message + ChatColor.GREEN + " banlist has been cleared successfully.");
                return true;
            }
            return false;
        }
        return true;
    }

}
