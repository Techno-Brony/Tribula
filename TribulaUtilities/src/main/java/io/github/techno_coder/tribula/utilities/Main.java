package io.github.techno_coder.tribula.utilities;

import io.github.techno_coder.tribula.utilities.commands.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        getCommand("templatecmd").setExecutor(new CommandHandler(this));
    }
}
