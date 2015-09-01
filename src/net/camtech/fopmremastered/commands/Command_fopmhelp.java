package net.camtech.fopmremastered.commands;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.camtech.fopmremastered.FOPMR_Commons;
import net.camtech.fopmremastered.FOPMR_Rank;
import net.camtech.fopmremastered.FreedomOpModRemastered;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandParameters(name = "fopmhelp", description = "Receive info on the new commands in the FOPM: R.", usage = "/fopmhelp [page number]")
public class Command_fopmhelp
{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(args.length != 1)
        {
            return false;
        }
        ArrayList<String> messages = new ArrayList<>();
        List<List<String>> pages = null;
        sender.sendMessage(ChatColor.GOLD + "Command :|: Description :|: Usage :|: Aliases");
        try
        {
            Pattern PATTERN = Pattern.compile("net/camtech/fopmremastered/commands/(Command_[^\\$]+)\\.class");
            CodeSource codeSource = FreedomOpModRemastered.class.getProtectionDomain().getCodeSource();
            if(codeSource != null)
            {
                ZipInputStream zip = new ZipInputStream(codeSource.getLocation().openStream());
                ZipEntry zipEntry;
                while((zipEntry = zip.getNextEntry()) != null)
                {
                    String entryName = zipEntry.getName();
                    Matcher matcher = PATTERN.matcher(entryName);
                    if(matcher.find())
                    {
                        try
                        {
                            Class<?> commandClass = Class.forName("net.camtech.fopmremastered.commands." + matcher.group(1));
                            FOPMR_Command cmdconstructed;
                            if(commandClass.isAnnotationPresent(CommandParameters.class))
                            {
                                Annotation annotation = commandClass.getAnnotation(CommandParameters.class);
                                CommandParameters params = (CommandParameters) annotation;
                                cmdconstructed = new FOPMR_BlankCommand(params.name(), params.usage(), params.description(), Arrays.asList(params.aliases().split(", ")), params.rank(), commandClass);
                            }
                            else
                            {
                                Constructor construct = commandClass.getConstructor();
                                cmdconstructed = (FOPMR_Command) construct.newInstance();
                            }
                            String message = ChatColor.GOLD + cmdconstructed.command + ChatColor.GREEN + " :|: " + ChatColor.BLUE + cmdconstructed.description + ChatColor.GREEN + " :|: " + ChatColor.AQUA + cmdconstructed.usage;
                            if(!(cmdconstructed.alias == null) && !cmdconstructed.alias.isEmpty())
                            {
                                message = message + ChatColor.GREEN + " :|: " + ChatColor.RED + "[" + StringUtils.join(cmdconstructed.alias, ", ") + "]";
                            }
                            if(FOPMR_Rank.isEqualOrHigher(FOPMR_Rank.getRank(sender), cmdconstructed.rank))
                            {
                                messages.add(message);
                            }
                            pages = FOPMR_Commons.chopped(messages, 10);
                        }
                        catch(ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
                        {
                            Bukkit.broadcastMessage("" + ex);
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            FreedomOpModRemastered.plugin.getLogger().severe(ex.getLocalizedMessage());
        }
        try
        {
            int i = Integer.parseInt(args[0]);
            for(String command : pages.get(i - 1))
            {
                sender.sendMessage(command);
            }
            sender.sendMessage(ChatColor.GOLD + "Help page " + i + " / " + pages.size());
        }
        catch(Exception ex)
        {
            sender.sendMessage(ChatColor.RED + "The argument must be a page number!");
        }
        return true;
    }

}
