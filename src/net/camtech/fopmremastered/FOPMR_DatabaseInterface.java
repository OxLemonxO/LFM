package net.camtech.fopmremastered;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.entity.Player;

public class FOPMR_DatabaseInterface
{

    private static Connection connection;
    private static int queries;

    public static Connection getConnection()
    {
        queries++;
        if(connection != null)
        {
            if(queries >= 2000)
            {
                closeConnection(connection);
                queries = 0;
                connection = null;
                return getConnection();
            }
            try
            {
                if(connection.isClosed())
                {
                    connection = null;
                    return getConnection();
                }
                connection.setAutoCommit(false);
                return connection;
            }
            catch(SQLException ex)
            {
                FreedomOpModRemastered.plugin.handleException(ex);
            }
        }
        else
        {
            try
            {
                connection = DriverManager.getConnection("jdbc:sqlite:" + FreedomOpModRemastered.plugin.getDataFolder().getAbsolutePath() + "/FOPMRData.db");
                connection.setAutoCommit(false);
                return connection;
            }
            catch(SQLException ex)
            {
                FreedomOpModRemastered.plugin.handleException(ex);
            }
        }
        return connection;
    }

    public static void prepareDatabase() throws SQLException
    {
        PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ANNOUNCEMENTS ("
                + "ID INTEGER PRIMARY KEY,"
                + "MESSAGE TEXT,"
                + "INTERVAL INTEGER)");
        statement.executeUpdate();
        statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS AREAS ("
                + "ID INTEGER PRIMARY KEY,"
                + "NAME TEXT UNIQUE,"
                + "OWNER TEXT,"
                + "RANK TEXT,"
                + "WORLD TEXT,"
                + "X FLOAT,"
                + "Y FLOAT,"
                + "Z FLOAT,"
                + "RANGE INTEGER,"
                + "ALLOWED TEXT)");
        statement.executeUpdate();
        statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS CHATS ("
                + "ID INTEGER PRIMARY KEY,"
                + "NAME TEXT UNIQUE,"
                + "COLOUR TEXT,"
                + "OWNER TEXT,"
                + "RANK TEXT,"
                + "ALLOWED TEXT)");
        statement.executeUpdate();
        statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS COMMANDS ("
                + "ID INTEGER PRIMARY KEY,"
                + "COMMAND TEXT,"
                + "RANK INTEGER,"
                + "MESSAGE TEXT,"
                + "KICK BOOLEAN,"
                + "ARGS TEXT)");
        statement.executeUpdate();
        statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS IP_BANS ("
                + "ID INTEGER PRIMARY KEY,"
                + "IP TEXT UNIQUE,"
                + "REASON TEXT,"
                + "PERM BOOLEAN)");
        statement.executeUpdate();
        statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS NAME_BANS ("
                + "ID INTEGER PRIMARY KEY,"
                + "NAME TEXT UNIQUE,"
                + "REASON TEXT,"
                + "PERM BOOLEAN)");
        statement.executeUpdate();
        statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS PLAYERS ("
                + "ID INTEGER PRIMARY KEY,"
                + "UUID TEXT UNIQUE,"
                + "NAME TEXT,"
                + "IP TEXT,"
                + "RANK TEXT,"
                + "NICK TEXT,"
                + "TAG TEXT,"
                + "LOGIN TEXT,"
                + "CHAT TEXT,"
                + "IMPOSTER BOOLEAN,"
                + "BANHAMMER BOOLEAN,"
                + "BUILDER BOOLEAN,"
                + "DOUBLEJUMP BOOLEAN,"
                + "GODMODE BOOLEAN,"
                + "MUTE BOOLEAN,"
                + "FROZEN BOOLEAN,"
                + "CMDBLOCK BOOLEAN,"
                + "LASTLOGIN INTEGER,"
                + "CHATLEVEL INTEGER)");
        statement.executeUpdate();
        statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS REPORTS ("
                + "ID INTEGER PRIMARY KEY,"
                + "REPORTED TEXT,"
                + "REPORTER TEXT,"
                + "REASON TEXT)");
        statement.executeUpdate();
        statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS UUID_BANS ("
                + "ID INTEGER PRIMARY KEY,"
                + "UUID TEXT UNIQUE,"
                + "REASON TEXT,"
                + "PERM BOOLEAN)");
        statement.executeUpdate();
        statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS WORLDS ("
                + "ID INTEGER PRIMARY KEY,"
                + "NAME TEXT UNIQUE,"
                + "GENERATOR TEXT,"
                + "RANK TEXT,"
                + "ONENABLE BOOLEAN)");
        statement.executeUpdate();
        getConnection().commit();
    }

    public static void generateNewPlayer(Player player) throws SQLException
    {
        Connection c = getConnection();
        PreparedStatement statement = c.prepareStatement("INSERT OR IGNORE INTO PLAYERS (UUID, NAME, IP, RANK, NICK, TAG, LOGIN, CHAT, IMPOSTER, BANHAMMER, BUILDER, DOUBLEJUMP, GODMODE, MUTE, FROZEN, CMDBLOCK, LASTLOGIN, CHATLEVEL) VALUES (?, ?, ?, 'Op', 'off&r', 'off&r', '', '', 0, 0, 0, 0, 0, 0, 0, 0, ?, 0)");
        statement.setString(1, StringEscapeUtils.escapeSql(player.getUniqueId().toString()));
        statement.setString(2, player.getName());
        statement.setString(3, player.getAddress().getAddress().getHostAddress());
        statement.setLong(4, System.nanoTime());
        statement.executeUpdate();
        c.commit();
    }

    public static boolean updateInTable(String uniqueColumn, String uniqueValue, Object newValue, String columnToChange, String table) throws SQLException
    {
        Connection c = getConnection();
        PreparedStatement statement = c.prepareStatement("UPDATE " + table + " SET " + columnToChange + " = ? WHERE " + uniqueColumn + " = ?");
        statement.setObject(1, newValue);
        statement.setString(2, uniqueValue);
        int i = statement.executeUpdate();
        c.commit();
        return i > 0;
    }

    public static ResultSet getAllResults(String uniqueColumn, String uniqueValue, String inTable) throws SQLException
    {
        Connection c = getConnection();
        PreparedStatement statement = null;
        if(uniqueColumn != null && uniqueValue != null)
        {
            statement = c.prepareStatement("SELECT * FROM " + inTable + " WHERE " + uniqueColumn + " = ?");
            statement.setString(1, uniqueValue);
        }
        else
        {
            statement = c.prepareStatement("SELECT * FROM " + inTable + "");
        }
        return statement.executeQuery();
    }

    public static ArrayList<Object> getAsArrayList(String uniqueColumn, String uniqueValue, String lookingFor, String inTable) throws SQLException
    {
        ArrayList<Object> array = new ArrayList<>();
        Connection c = getConnection();
        PreparedStatement statement;
        if(uniqueColumn != null && uniqueValue != null)
        {
            statement = c.prepareStatement("SELECT * FROM " + inTable + " WHERE " + uniqueColumn + " = ?");
            statement.setString(1, uniqueValue);
        }
        else
        {
            statement = c.prepareStatement("SELECT * FROM " + inTable);
        }
        ResultSet set = statement.executeQuery();
        while(set.next())
        {
            array.add(set.getObject(lookingFor));
        }
        return array;
    }

    public static Object getFromTable(String uniqueColumn, String uniqueValue, String lookingFor, String inTable) throws SQLException
    {
        Connection c = getConnection();
        PreparedStatement statement = c.prepareStatement("SELECT * FROM " + inTable + " WHERE " + uniqueColumn + " = ?");
        statement.setString(1, uniqueValue);
        ResultSet res = statement.executeQuery();
        if(res.next())
        {
            return res.getObject(lookingFor);
        }
        return null;
    }

    public static Boolean getBooleanFromTable(String uniqueColumn, String uniqueValue, String lookingFor, String inTable) throws SQLException
    {
        int i = (Integer) getFromTable(uniqueColumn, uniqueValue, lookingFor, inTable);
        return i == 1;
    }

    public static String getUuidFromName(String name) throws SQLException
    {
        Connection c = getConnection();
        PreparedStatement statement = c.prepareStatement("SELECT * FROM PLAYERS WHERE NAME = ?");
        statement.setString(1, name);
        ResultSet res = statement.executeQuery();
        if(res.next())
        {
            if(res.getObject("UUID") != null && res.getObject("UUID") instanceof String)
            {
                return (String) res.getObject("UUID");
            }
        }
        return null;
    }

    public static String getIpFromName(String name) throws SQLException
    {
        Connection c = getConnection();
        PreparedStatement statement = c.prepareStatement("SELECT * FROM PLAYERS WHERE NAME = ?");
        statement.setString(1, name);
        ResultSet res = statement.executeQuery();
        if(res.next())
        {
            if(res.getObject("IP") != null && res.getObject("IP") instanceof String)
            {
                return (String) res.getObject("IP");
            }
        }
        return null;
    }

    public static boolean playerExists(String uuid) throws SQLException
    {
        return getFromTable("UUID", uuid, "NAME", "PLAYERS") != null;
    }

    public static boolean existsInTable(String uniqueColumn, Object uniqueValue, String table) throws SQLException
    {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE ? = ?");
        statement.setString(1, uniqueColumn);
        statement.setObject(2, uniqueValue);
        ResultSet set = statement.executeQuery();
        return set.next();
    }

    public static String getLoginMessage(String uuid) throws SQLException
    {
        if(!playerExists(uuid))
        {
            return null;
        }
        Object obj = getFromTable("UUID", uuid, "LOGIN", "PLAYERS");
        if(obj instanceof String)
        {
            return (String) obj;
        }
        return null;
    }

    public static String getRank(String uuid) throws SQLException
    {
        if(!playerExists(uuid))
        {
            return "Op";
        }
        Object obj = getFromTable("UUID", uuid, "RANK", "PLAYERS");
        if(obj instanceof String)
        {
            return (String) obj;
        }
        return "Op";
    }

    public static String getTag(String uuid) throws SQLException
    {
        if(!playerExists(uuid))
        {
            return "&7[&c" + getRank(uuid) + "&7]";
        }
        Object obj = getFromTable("UUID", uuid, "TAG", "PLAYERS");
        if(obj instanceof String)
        {
            return (String) obj;
        }
        return "&7[&c" + getRank(uuid) + "&7]";
    }

    public static String getNick(String uuid) throws SQLException
    {
        if(!playerExists(uuid))
        {
            return uuid;
        }
        Object obj = getFromTable("UUID", uuid, "NICK", "PLAYERS");
        if(obj instanceof String)
        {
            return (String) obj;
        }
        return uuid;
    }

    public static boolean hasBanHammer(String uuid) throws SQLException
    {
        if(!playerExists(uuid))
        {
            return false;
        }
        Object obj = getFromTable("UUID", uuid, "BANHAMMER", "PLAYERS");
        if(obj instanceof Boolean)
        {
            return (Boolean) obj;
        }
        return false;
    }

    public static boolean isGod(String uuid) throws SQLException
    {
        if(!playerExists(uuid))
        {
            return false;
        }
        Object obj = getFromTable("UUID", uuid, "GODMODE", "PLAYERS");
        if(obj instanceof Boolean)
        {
            return (Boolean) obj;
        }
        return false;
    }

    public static void closeConnection(Connection connection)
    {
        try
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        catch(SQLException e)
        {
            System.err.println(e);
        }
    }
}