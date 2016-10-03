package io.github.techno_brony.tribula.sql;

import io.github.techno_brony.tribula.plugin.BukkitTemplate.TribulaPlugin;
import io.github.techno_brony.tribula.sql.commands.CommandSQL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends TribulaPlugin {

    private Connection connection;
    private String host, database, username, password;
    private int port;

    @Override
    public void onEnable() {
        createAndLoadConfig();
        parseConfig();
        try {
            openSQLConnection();
        } catch (SQLException | ClassNotFoundException e) {
            getLogger().severe("Unable to connect to database. Please check your config.");
        }
        getCommand("sql").setExecutor(new CommandSQL(this));
    }

    @Override
    public void onDisable() {

    }

    private void createAndLoadConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Creating config.yml ...");
                saveDefaultConfig();
            } else {
                getLogger().info("Loading config.yml ...");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void parseConfig() {
        host = getConfig().getString("host");
        port = getConfig().getInt("port");
        database = getConfig().getString("database");
        username = getConfig().getString("username");
        password = getConfig().getString("password");
    }

    public void openSQLConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }
}
