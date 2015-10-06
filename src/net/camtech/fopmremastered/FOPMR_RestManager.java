package net.camtech.fopmremastered;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FOPMR_RestManager
{

    public static void sendConversation(int userToSendTo, String title, String message)
    {
        FileConfiguration config = FreedomOpModRemastered.plugin.getConfig();
        if(config.getBoolean("rest.enabled"))
        {
            String query = "action=createConversation&title=" + title + "&recipients=" + getFromId(userToSendTo) + "&hash=" + config.getString("rest.key") + "&message=" + message + "&grab_as=" + config.getString("rest.user") /*"FreedomOpMod: Post Bot"*/;
            try
            {
                URL url = new URL(config.getString("rest.url"));
                URLConnection urlc = url.openConnection();

                urlc.setDoOutput(true);
                urlc.setAllowUserInteraction(false);

                try(PrintStream ps = new PrintStream(urlc.getOutputStream()))
                {
                    String coded = URLEncoder.encode(query, "UTF-8").replaceAll("%3D", "=").replaceAll("%26", "&").replaceAll(Pattern.quote("+"), "%20");
                    ps.print(coded);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(urlc
                        .getInputStream()));
                String l;
                while((l = br.readLine()) != null)
                {
                    Bukkit.getLogger().info(l);
                }
                br.close();
            }
            catch(IOException ex)
            {
                FreedomOpModRemastered.plugin.handleException(ex);
            }
        }
    }

    public static String getFromId(int id)
    {
        FileConfiguration config = FreedomOpModRemastered.plugin.getConfig();
        if(config.getBoolean("rest.enabled"))
        {
            String query = "action=getUser&hash=" + config.getString("rest.key") + "&value=" + id + "&grab_as=" + config.getString("rest.user") /*"FreedomOpMod: Post Bot"*/;
            try
            {
                URL url = new URL(config.getString("rest.url"));
                URLConnection urlc = url.openConnection();

                urlc.setDoOutput(true);
                urlc.setAllowUserInteraction(false);

                try(PrintStream ps = new PrintStream(urlc.getOutputStream()))
                {
                    ps.print(query);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(urlc
                        .getInputStream()));
                String l;
                StringBuilder builder = new StringBuilder();
                while((l = br.readLine()) != null)
                {
                    builder.append(l);
                }
                br.close();
                JSONParser parser = new JSONParser();
                JSONObject object = (JSONObject) parser.parse(builder.toString());
                if(!object.containsKey("username"))
                {
                    return null;
                }
                return (String) object.get("username");
            }
            catch(IOException | ParseException ex)
            {
                return null;
            }
        }
        return null;
    }

    public static void sendMessage(int threadId, String message)
    {
        FileConfiguration config = FreedomOpModRemastered.plugin.getConfig();
        if(config.getBoolean("rest.enabled"))
        {
            String query = "action=createPost&thread_id=" + threadId + "&hash=" + config.getString("rest.key") + "&message=" + message + "&grab_as=" + config.getString("rest.user") /*"FreedomOpMod: Post Bot"*/;
            try
            {
                URL url = new URL(config.getString("rest.url"));
                URLConnection urlc = url.openConnection();

                urlc.setDoOutput(true);
                urlc.setAllowUserInteraction(false);

                try(PrintStream ps = new PrintStream(urlc.getOutputStream()))
                {
                    ps.print(query);
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(urlc
                        .getInputStream()));
                String l;
                while((l = br.readLine()) != null)
                {
                    Bukkit.getLogger().info(l);
                }
                br.close();
            }
            catch(IOException ex)
            {

            }
        }
    }
}