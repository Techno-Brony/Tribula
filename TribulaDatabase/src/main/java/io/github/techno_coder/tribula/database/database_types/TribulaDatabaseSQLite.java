package io.github.techno_coder.tribula.database.database_types;

import io.github.techno_coder.tribula.database.Main;
import io.github.techno_coder.tribula.database.interfaces.ITribulaDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TribulaDatabaseSQLite implements ITribulaDatabase {

    private final Main plugin;
    private Connection connection;

    public TribulaDatabaseSQLite(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void openConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            synchronized (this) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            }
        } catch ( Exception e ) {
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
