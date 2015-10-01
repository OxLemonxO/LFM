package net.camtech.fopmremastered;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class FOPMR_RestManager
{

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
