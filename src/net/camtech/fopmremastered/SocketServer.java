package net.camtech.fopmremastered;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.scheduler.BukkitRunnable;

public class SocketServer implements Runnable
{

    public ServerSocket sock;
    private Socket client;

    public SocketServer()
    {
        try
        {
            sock = new ServerSocket(2606);
        }
        catch(IOException ex)
        {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Socket clientSocket = sock.accept();
                System.out.println(clientSocket.getInetAddress() + " connected!");
                PrintWriter out
                        = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                String ip = clientSocket.getInetAddress().getHostAddress();
                String name;
                try
                {
                    if(!"5.135.233.93".equalsIgnoreCase(ip))
                    {
                        out.println("You are the wrong host, you are " + ip + " not 5.135.233.93");
                        close(out, in);
                        return;
                    }
                    name = in.readLine();
                    if(name.equalsIgnoreCase("###LISTALLNAMES###"))
                    {
                        String bans = "Banned names: ";
                        ResultSet set = FOPMR_DatabaseInterface.getAllResults("UUID", null, "NAMEBANS");
                        while(set.next())
                        {
                            bans += ", " + set.getString("NAME");
                        }
                        bans += ".";
                        out.println(bans);
                        System.out.println(clientSocket.getInetAddress() + " listed all name bans.");
                    }
                    else
                    {
                        out.println(FOPMR_Bans.getReason(name).replaceAll("—", "by"));
                        System.out.println(clientSocket.getInetAddress() + " checked ban reason for " + name + ".");
                    }
                    close(out, in);
                }
                catch(IOException ex)
                {

                }
                catch(SQLException ex)
                {
                    Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            catch(IOException e)
            {

            }
        }
    }

    public static void close(final PrintWriter out, final BufferedReader in)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    out.flush();
                    out.close();
                    in.close();
                }
                catch(IOException ex)
                {
                    FreedomOpModRemastered.plugin.handleException(ex);
                }
            }
        }.runTaskLater(FreedomOpModRemastered.plugin, 20L * 2L);

    }

}