package io.github.techno_coder.tribula.database.database_types;

import io.github.techno_coder.tribula.database.Main;
import io.github.techno_coder.tribula.database.interfaces.ITribulaDatabase;
import io.github.techno_coder.tribula.database.wrappers.TribulaDatabaseCredentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TribulaDatabaseMySQL implements ITribulaDatabase {

    private final Main plugin;
    private final TribulaDatabaseCredentials credentials;
    private Connection connection;

    public TribulaDatabaseMySQL(Main plugin, TribulaDatabaseCredentials credentials) {
        this.plugin = plugin;
        this.credentials = credentials;
    }

    @Override
    public void openConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            synchronized (this) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" +
                                credentials.getHostname() + ":" +
                                credentials.getPort() + "/" +
                                credentials.getDatabaseName(),
                        credentials.getUsername(),
                        credentials.getPassword());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            plugin.getLogger().severe("An SQL error occurred : " + e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            plugin.getLogger().severe("An error occurred while closing the SQL connection : " + e.getMessage());
        }
    }
}
