package net.camtech.fopmremastered.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import static net.camtech.fopmremastered.FOPMR_Rank.isSuper;
import net.camtech.fopmremastered.FOPMR_RestManager;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import static net.camtech.fopmremastered.FreedomOpModRemastered.config;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "admin", usage = "/admin [[add | delete] [username] <rank>] | [list] | [purge]", description = "Add somebody to admin.")
public class Command_admin
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("list"))
            {
                try
                {
                    ArrayList<String> admins = new ArrayList<>();
                    ArrayList<String> sadmins = new ArrayList<>();
                    ArrayList<String> sradmins = new ArrayList<>();
                    ArrayList<String> execs = new ArrayList<>();
                    ArrayList<String> specs = new ArrayList<>();
                    ArrayList<String> systems = new ArrayList<>();
                    ArrayList<String> owners = new ArrayList<>();
                    ArrayList<ArrayList<String>> arrays = new ArrayList<>();
                    arrays.add(admins);
                    arrays.add(sadmins);
                    arrays.add(sradmins);
                    arrays.add(execs);
                    arrays.add(specs);
                    arrays.add(systems);
                    arrays.add(owners);
                    for(ArrayList<String> array : arrays)
                    {
                        Collections.sort(array, String.CASE_INSENSITIVE_ORDER);
                    }
                    Connection c = FOPMR_DatabaseInterface.getConnection();
                    PreparedStatement statement = c.prepareStatement("SELECT * FROM PLAYERS WHERE RANK != 'Op'");
                    ResultSet res = statement.executeQuery();
                    int number = 0;
                    while(res.next())
                    {
                        number++;
                        switch(res.getString("RANK"))
                        {
                            case "Admin":
                                admins.add(res.getString("NAME"));
                                break;
                            case "Super Admin":
                                sadmins.add(res.getString("NAME"));
                                break;
                            case "Senior Admin":
                                sradmins.add(res.getString("NAME"));
                                break;
                            case "Executive":
                                execs.add(res.getString("NAME"));
                                break;
                            case "Specialist":
                                specs.add(res.getString("NAME"));
                                break;
                            case "System Admin":
                                systems.add(res.getString("NAME"));
                                break;
                            case "Co-Owner":
                                owners.add(res.getString("NAME"));
                                break;
                            case "Owner":
                                owners.add(res.getString("NAME"));
                                break;
                            default:
                                break;
                        }
                    }
                    sender.sendMessage(ChatColor.AQUA + "FreedomOp: Remastered Admins:");
                    sender.sendMessage(ChatColor.GOLD + "  - Total Admins: " + number);
                    sender.sendMessage(ChatColor.YELLOW + "    - Admins:");
                    sender.sendMessage("        - " + StringUtils.join(admins, ", "));
                    sender.sendMessage(ChatColor.AQUA + "    - Super Admins:");
                    sender.sendMessage("        - " + StringUtils.join(sadmins, ", "));
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "    - Senior Admins:");
                    sender.sendMessage("        - " + StringUtils.join(sradmins, ", "));
                    sender.sendMessage(ChatColor.DARK_GREEN + "    - Executives:");
                    sender.sendMessage("        - " + StringUtils.join(execs, ", "));
                    sender.sendMessage(ChatColor.GREEN + "    - Specialists:");
                    sender.sendMessage("        - " + StringUtils.join(specs, ", "));
                    sender.sendMessage(ChatColor.DARK_PURPLE + "    - System Admins:");
                    sender.sendMessage("        - " + StringUtils.join(systems, ", "));
                    sender.sendMessage(ChatColor.DARK_BLUE + "    - Owners:");
                    sender.sendMessage("        - " + StringUtils.join(owners, ", "));
                    return true;
                }
                catch(SQLException ex)
                {
                    FreedomOpModRemastered.plugin.handleException(ex);
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("purge") && FOPMR_Rank.isSenior(sender))
            {
                if((sender instanceof Player || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != 1) && !sender.getName().equals("Camzie99"))
                {
                    sender.sendMessage(ChatColor.RED + "Purging can only be done on a Sunday by the Console.");
                    return true;
                }
                FOPMR_Commons.adminAction(sender.getName(), "Purging the player list.", true);
                sender.sendMessage(ChatColor.RED + "PREPARE FOR SPAM!");
                HashMap<String, Long> removedAdmins = new HashMap<>();
                try
                {
                    for(Object res : FOPMR_DatabaseInterface.getAsArrayList("UUID", null, "UUID", "PLAYERS"))
                    {
                        String name = (String) res;
                        long lasttime = (long) FOPMR_DatabaseInterface.getFromTable("UUID", name, "LASTLOGIN", "PLAYERS");
                        long current = System.currentTimeMillis();
                        long change = current - lasttime;
                        if(change > 604800000)
                        {
                            sender.sendMessage("Removed " + (String) FOPMR_DatabaseInterface.getFromTable("UUID", name, "NAME", "PLAYERS") + ", time since last login in milliseconds: " + change + ".");
                            if(FOPMR_Rank.getFromUsername((String) FOPMR_DatabaseInterface.getFromTable("UUID", name, "NAME", "PLAYERS")).level >= 1)
                            {
                                removedAdmins.put((String) FOPMR_DatabaseInterface.getFromTable("UUID", name, "NAME", "PLAYERS"), lasttime);
                            }
                            Connection c = FOPMR_DatabaseInterface.getConnection();
                            PreparedStatement statement = c.prepareStatement("DELETE FROM PLAYERS WHERE UUID = ?");
                            statement.setString(1, name);
                            statement.execute();
                            c.commit();
                        }
                    }
                }
                catch(Exception ex)
                {
                    FreedomOpModRemastered.plugin.handleException(ex);
                }
                Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - Wiping admin list.");
                Bukkit.broadcastMessage(ChatColor.RED + "The following admins were removed due to inactivity.");
                Bukkit.broadcastMessage(ChatColor.GOLD + "Admin Name " + ChatColor.RED + " :|: " + ChatColor.GOLD + "Last Login Date.");
                String message = "FreedomOpMod: Remastered Automatic Admin Purging\nThe following admins were removed due to inactivity.\nAdmin Name :|: Last Login Date.";
                for(String name : removedAdmins.keySet())
                {
                    Date date = new Date(removedAdmins.get(name));
                    sender.sendMessage(ChatColor.GOLD + name + ChatColor.RED + " :|: " + ChatColor.GOLD + date.toGMTString());
                    message += "\n" + name + " :|: " + date.toGMTString();
                }
                FOPMR_RestManager.sendMessage(config.getInt("rest.removalsid"), message);
                return true;
            }
            return false;
        }
        if(!isSuper(sender))
        {
            return true;
        }
        Player player;
        if(args.length == 2)
        {
            player = FOPMR_Rank.getPlayer(args[1]);
            if(player == null)
            {
                sender.sendMessage("Player: " + args[1] + " is not online.");
                return true;
            }
            if(args[0].equalsIgnoreCase("delete"))
            {
                if(FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(player), FOPMR_Rank.getRank(sender)))
                {
                    sender.sendMessage("You can only remove someone of a lower rank than yourself from admin.");
                    return true;
                }
                FOPMR_Rank.setRank(player, FOPMR_Rank.Rank.OP, sender);
                return true;
            }
            if(args[0].equalsIgnoreCase("add"))
            {
                FOPMR_Rank.setRank(player, FOPMR_Rank.Rank.ADMIN, sender);
                return true;
            }
            return false;
        }
        if(args.length >= 3)
        {
            if(!args[0].equalsIgnoreCase("add"))
            {
                sender.sendMessage("You only need 2 arguments for a removal.");
                return true;
            }
            player = FOPMR_Rank.getPlayer(args[1]);
            if(player == null)
            {
                sender.sendMessage("Player: " + args[1] + " is not online.");
                return true;
            }
            int level;
            try
            {
                level = Integer.parseInt(args[2]);
            }
            catch(Exception ex)
            {
                String rank = StringUtils.join(ArrayUtils.subarray(args, 2, args.length), " ");
                level = FOPMR_Rank.getFromName(rank).level;
            }
            if(level == 0)
            {
                Bukkit.broadcastMessage(StringUtils.join(ArrayUtils.subarray(args, 2, args.length), " ") + " is an invalid rank.");
                return true;
            }
            FOPMR_Rank.setRank(player, FOPMR_Rank.getFromLevel(level), sender);
            return true;
        }
        return false;
    }

}