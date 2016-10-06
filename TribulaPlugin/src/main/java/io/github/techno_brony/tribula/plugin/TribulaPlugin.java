package io.github.techno_brony.tribula.plugin;

import io.github.techno_brony.tribula.plugin.commands.CommandTribulaPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TribulaPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("tribulaplugin").setExecutor(new CommandTribulaPlugin(this));
    }

    @Override
    public void onDisable() {

    }

    private void createAndLoadConfig() {
        try {
            if (!getDataFolder().exists()) {
                boolean result = getDataFolder().mkdirs();
                if (!result) {
                    getLogger().severe("Some directories failed to be created.");
                }
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
    }
}
