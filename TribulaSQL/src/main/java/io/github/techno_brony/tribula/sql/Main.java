package io.github.techno_brony.tribula.sql;

import io.github.techno_brony.tribula.plugin.TribulaPlugin;
import io.github.techno_brony.tribula.sql.commands.CommandSQL;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

enum SQLColumnType {
    varchar,
    smallint,
    bigint
}

public class Main extends TribulaPlugin implements TribulaSQLAPI {

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
                            "SELECT * FROM " + client.getUniqueTableID() + " WHERE UUID = '" + player.toString() + "';");
                    if (result.next()) {
                        ResultSetMetaData metaData = result.getMetaData();
                        HashMap<String, Object> data = new HashMap<>();
                        for (int i = 1; i <= metaData.getColumnCount(); i++) {
                            data.put(metaData.getColumnName(i), result.getObject(i));
                        }
                        callback.done(data);
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    getLogger().severe("Client: " + client.getUniqueTableID() + " attempted to access invalid data.");
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
                        HashMap<String, Object> value = new HashMap<>();
                        value.put(column, value.get(column));
                        callback.done(value);
                    }
                });
            }
        }.runTaskAsynchronously(this);
    }

    /**
     * Sets a field in the database table
     *
     * @param client The plugin
     * @param player The player
     * @param column The column of which is field lies in
     * @param value  The value of which to replace the field
     */
    public void setData(final TribulaSQLClient client, final UUID player, final String column, final Object value) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    openSQLConnection();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("UPDATE " + client.getUniqueTableID() +
                            " SET " + column + "='" + value.toString() + "' WHERE UUID='" + player + "';");
                } catch (SQLException | ClassNotFoundException e) {
                    getLogger().severe("Client: " + client.getUniqueTableID() + " attempted to set invalid data.");
                }
            }
        }.runTaskAsynchronously(this);
    }

    /**
     * Creates a table if one does not exist
     *
     * @param client  The plugin
     * @param columns The columns that will be added to the table
     */
    public void createTable(final TribulaSQLClient client, HashMap<String, SQLColumnType> columns) {
        try {
            openSQLConnection();
            Statement statement = connection.createStatement();
            String sqlupdate = "CREATE TABLE IF NOT EXISTS " + client.getUniqueTableID() + " (";
            for (String s : columns.keySet()) {
                sqlupdate += s + " " + columns.get(s).toString() + ",";
            }
            sqlupdate = sqlupdate.substring(0, sqlupdate.length() - 1);
            sqlupdate += ");";
            statement.executeUpdate(sqlupdate);
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().severe("An error occurred while trying to create table");
        }
    }
}
