package io.github.techno_coder.tribula.database;

import io.github.techno_coder.tribula.database.database_types.TribulaDatabaseMySQL;
import io.github.techno_coder.tribula.database.database_types.TribulaDatabaseSQLite;
import io.github.techno_coder.tribula.database.enums.TribulaDatabaseType;
import io.github.techno_coder.tribula.database.interfaces.ITribulaDatabase;
import io.github.techno_coder.tribula.database.wrappers.TribulaDatabaseCredentials;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private TribulaDatabaseType databaseType;
    private TribulaDatabaseCredentials credentials;
    private ITribulaDatabase database;

    @Override
    public void onEnable() {
        createAndLoadConfig();
        parseConfig();
        openConnection();
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
        databaseType = TribulaDatabaseType.valueOf(getConfig().getString("databaseType"));
        credentials = new TribulaDatabaseCredentials(
                getConfig().getString("username"),
                getConfig().getString("password"),
                (short) getConfig().getInt("port"),
                getConfig().getString("databaseName"),
                getConfig().getString("hostname"));
    }

    private void openConnection() {
        switch (databaseType) {
            case SQLITE:
                database = new TribulaDatabaseSQLite(this);
                break;

            case MYSQL:
                database = new TribulaDatabaseMySQL(this, credentials);
                break;

            default:
                getLogger().severe("Invalid database type.");
                return;
        }

        database.openConnection();
    }
}
