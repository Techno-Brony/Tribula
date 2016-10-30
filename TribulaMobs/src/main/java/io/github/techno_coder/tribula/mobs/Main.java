package io.github.techno_coder.tribula.mobs;

import io.github.techno_coder.tribula.mobs.commands.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        createAndLoadConfig();
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getCommand("templatecmd").setExecutor(new CommandHandler(this));
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

    private void parseConfig() {}
}
