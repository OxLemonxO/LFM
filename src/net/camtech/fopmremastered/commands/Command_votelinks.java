package net.camtech.fopmremastered.commands;

import net.camtech.camutils.CUtils_Methods;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "votelinks", usage = "/votelinks", description = "View the links to vote.")
public class Command_votelinks
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        ChatColor sameRandom = CUtils_Methods.randomChatColour();
        sender.sendMessage(sameRandom + "####################");
        sender.sendMessage(sameRandom + "#### Vote Links ####");
        sender.sendMessage(sameRandom + "####################");
        sender.sendMessage(CUtils_Methods.randomChatColour() + "Planet Minecraft: http://www.planetminecraft.com/server/freedomop-remastered/");
        sender.sendMessage(CUtils_Methods.randomChatColour() + "MinecraftServers.org: http://minecraftservers.org/vote/193818");
        sender.sendMessage(CUtils_Methods.randomChatColour() + "Minecraft-MP: http://minecraft-mp.com/server/76391/vote/");
        sender.sendMessage(CUtils_Methods.randomChatColour() + "Minecraft-Server-List.com: http://minecraft-server-list.com/server/288060/vote/");
        return true;
    }
}
