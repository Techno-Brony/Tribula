package io.github.techno_coder.tribula.database;

import io.github.techno_coder.tribula.database.builders.TribulaQueryBuilder;
import io.github.techno_coder.tribula.database.builders.TribulaUpdateBuilder;
import io.github.techno_coder.tribula.database.database_types.TribulaDatabaseMySQL;
import io.github.techno_coder.tribula.database.database_types.TribulaDatabaseSQLite;
import io.github.techno_coder.tribula.database.enums.TribulaDatabaseType;
import io.github.techno_coder.tribula.database.interfaces.ITribulaDatabase;
import io.github.techno_coder.tribula.database.wrappers.TribulaDatabaseCredentials;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.ResultSet;

public class Main extends JavaPlugin {

    private static Main instance;

    private TribulaDatabaseType databaseType;
    private TribulaDatabaseCredentials credentials;
    private ITribulaDatabase database;

    public static Main getInstance() {
        if (instance == null) {
            instance = (Main) Bukkit.getPluginManager().getPlugin("TribulaDatabase");
        }
        return instance;
    }

    @Override
    public void onEnable() {
        createAndLoadConfig();
        parseConfig();
        openConnection();
        prepareDatabase();
    }

    @Override
    public void onDisable() {
        database.closeConnection();
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

    private void prepareDatabase() {
        //TODO Create table if not exists
    }

    public void executeUpdate(TribulaUpdateBuilder.TribulaUpdateStatement update) {
//        try {
//            Statement statement = database.getConnection().createStatement();
//            statement.executeUpdate(update);
//            statement.close();
//        } catch (SQLException e) {
//            getLogger().severe("An SQL error occurred : " + e.getMessage());
//        }
    } //TODO

    public ResultSet executeQuery(TribulaQueryBuilder.TribulaQueryStatement query) { //TODO
//        try {
//            Statement statement = database.getConnection().createStatement();
//            ResultSet resultSet = statement.executeQuery(query);
//            statement.close();
//            return resultSet;
//        } catch (SQLException e) {
//            getLogger().severe("An SQL error occurred : " + e.getMessage());
//        }
        return null;
    }

}
