package io.github.techno_coder.tribula.database.database_types;

import io.github.techno_coder.tribula.database.Main;
import io.github.techno_coder.tribula.database.interfaces.ITribulaDatabase;

import java.sql.Connection;
import java.sql.DriverManager;

public class TribulaDatabaseSQLite implements ITribulaDatabase {

    private final Main plugin;

    public TribulaDatabaseSQLite(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void openConnection() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch ( Exception e ) {
            plugin.getLogger().severe("An SQL error occurred : " + e.getMessage());
        }
    }



}
