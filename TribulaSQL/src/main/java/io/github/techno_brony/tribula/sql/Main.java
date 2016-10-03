package io.github.techno_brony.tribula.sql;

import io.github.techno_brony.tribula.plugin.BukkitTemplate.TribulaPlugin;
import io.github.techno_brony.tribula.sql.commands.CommandSQL;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

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

    private void parseConfig() {
        host = getConfig().getString("host");
        port = getConfig().getInt("port");
        database = getConfig().getString("database");
        username = getConfig().getString("username");
        password = getConfig().getString("password");
    }

    private void openSQLConnection() throws SQLException, ClassNotFoundException {
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

    /**
     * Gets a row of data
     * Guaranteed to only be one row
     *
     * @param client   The plugin
     * @param player   The player to get data from
     * @param callback A callback which uses the data
     */
    public void getData(final TribulaSQLClient client, final UUID player, final TribulaSQLCallback callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    openSQLConnection();
                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(
                            "SELECT * FROM " + client.getUniqueTableID() + " WHERE UUID = '" + player + "';");
                    if (result.next()) {
                        ResultSetMetaData metaData = result.getMetaData();
                        HashMap<String, Object> data = new HashMap<>();
                        for (int i = 1; i <= metaData.getColumnCount(); i++) {
                            data.put(metaData.getColumnName(i), result.getObject(i));
                        }
                        callback.done(data);
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(this);
    }

    /**
     * Gets a single field of data
     *
     * @param client   The plugin
     * @param player   The player to get the field from
     * @param callback A callback which uses the field
     * @param column   The column name
     */
    public void getData(final TribulaSQLClient client, final UUID player, final String column, final TribulaSQLCallback callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                getData(client, player, new TribulaSQLCallback() {
                    @Override
                    public void done(HashMap<String, Object> o) {
                        HashMap<String, Object> field = new HashMap<>();
                        field.put(column, o.get(column));
                        callback.done(field);
                    }
                });
            }
        }.runTaskAsynchronously(this);
    }

    public void setData(final TribulaSQLClient client, final UUID player, final String column, final Object value) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    openSQLConnection();
                    Statement statement = connection.createStatement(); //TODO FINISH
                    statement.executeUpdate("INSERT INTO " + client.getUniqueTableID() + " (PLAYERNAME, BALANCE) VALUES ('Playername', 100);");
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(this);
    }
}
