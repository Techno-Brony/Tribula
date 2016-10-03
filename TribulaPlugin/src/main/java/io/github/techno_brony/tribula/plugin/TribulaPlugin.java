package io.github.techno_brony.tribula.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TribulaPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    protected void createAndLoadConfig() {
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

}
