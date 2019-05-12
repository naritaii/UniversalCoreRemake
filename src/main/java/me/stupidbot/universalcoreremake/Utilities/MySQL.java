package me.stupidbot.universalcoreremake.Utilities;

import me.stupidbot.universalcoreremake.UniversalCoreRemake;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private static final String host = "54.37.159.155";
    private static final String port = "3306";
    private static final String database = "s36834_maindb";
    private static final String username = "u36834_fOf4IIcGgT";
    private static final String password = "rPtJbO1x1TzDK78s";
    private static Connection con;

    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();

    // connect
    public void connect() {
        if (!isConnected()) {
            synchronized (UniversalCoreRemake.getInstance()) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                    console.sendMessage("Connected to database " + database);
                }
                 catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // disconnect
    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                console.sendMessage("Disconnected from database " + database);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // isConnected
    public boolean isConnected() {
        return (con != null);
    }

    // getConnection
    public Connection getConnection() {
        return con;
    }
}