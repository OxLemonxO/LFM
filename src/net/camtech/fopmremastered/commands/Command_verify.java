package net.camtech.fopmremastered.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;
import net.camtech.fopmremastered.FOPMR_DatabaseInterface;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FOPMR_Rank.Rank;
import net.camtech.fopmremastered.FOPMR_RestManager;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "verify", usage = "/verify <<set> <forum user ID> | <verify> <code> | <sendcode>>", description = "Forum-based verification command!", rank=Rank.IMPOSTER)
public class Command_verify
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Only in-game players can execute this command.");
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("sendcode"))
            {
                try
                {
                    if(FOPMR_Rank.getRank(sender) != Rank.IMPOSTER)
                    {
                        sender.sendMessage(ChatColor.RED + "You are not an imposter, you don't need to verify.");
                        return true;
                    }
                    if(FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "FORUMID", "VERIFICATION") == null)
                    {
                        sender.sendMessage(ChatColor.GREEN + "There is no user defined as your forum user.");
                        return false;
                    }
                    sender.sendMessage(ChatColor.GREEN + "Sent a verification code to the defined user on the forums.");
                    String code = UUID.randomUUID().toString();
                    FOPMR_RestManager.sendConversation((Integer) FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "FORUMID", "VERIFICATION"), "Verification Code.", "Hi there, " + player.getName() + " has requested verification on the IP address of " + player.getAddress().getHostString() + " and this is the defined forum user for the account. If you have not requested in-game verification, please ignore this message. If this message applies to you, your verification code is " + code + ". Please type \"/verify verify " + code + "\" in-game to verify as an admin!");
                    FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), code, "CODE", "VERIFICATION");
                    return true;
                }
                catch(Exception ex)
                {
                    FreedomOpModRemastered.plugin.handleException(ex);
                }
            }
            return false;
        }
        else if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("set"))
            {
                if(!FOPMR_Rank.isAdmin(sender))
                {
                    sender.sendMessage(ChatColor.RED + "You must be a verified admin to set your forum user ID.");
                    return true;
                }
                int i;
                try
                {
                    i = Integer.parseInt(args[1]);
                }
                catch(NumberFormatException ex)
                {
                    sender.sendMessage("Your second argument must be your forum user ID.");
                    return true;
                }
                try
                {
                    Connection c = FOPMR_DatabaseInterface.getConnection();
                    PreparedStatement statement = c.prepareStatement("INSERT OR REPLACE INTO VERIFICATION (UUID, FORUMID, CODE) VALUES (?, ?, ?)");
                    statement.setString(1, player.getUniqueId().toString());
                    statement.setInt(2, i);
                    statement.setString(3, null);
                    statement.executeUpdate();
                    c.commit();
                }
                catch(Exception ex)
                {
                    sender.sendMessage(ChatColor.RED + "There was an SQL Error, please contact a developer.");
                    FreedomOpModRemastered.plugin.handleException(ex);
                    return true;
                }
                sender.sendMessage(ChatColor.GREEN + "Your user ID was successfully set to " + i + ".");
                return true;
            }
            if(args[0].equalsIgnoreCase("verify"))
            {
                try
                {
                    Object obj = FOPMR_DatabaseInterface.getFromTable("UUID", player.getUniqueId().toString(), "CODE", "VERIFICATION");
                    if(!(obj instanceof String))
                    {
                        sender.sendMessage(ChatColor.RED + "There is no code set as your verification code.");
                        return false;
                    }
                    String real = (String) obj;
                    if(args[1].equals(real))
                    {
                        FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), null, "CODE", "VERIFICATION");
                        FOPMR_Rank.unImposter(player);
                        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + " has successfully verified using the forum-based verification system.");
                        return true;
                    }
                    sender.sendMessage(ChatColor.RED + "Invalid verification code, now disabling code for safety, please send another code if you believe this is in error.");
                    FOPMR_DatabaseInterface.updateInTable("UUID", player.getUniqueId().toString(), null, "CODE", "VERIFICATION");
                    return true;
                }
                catch(Exception ex)
                {

                }
            }
        }
        return false;
    }
}