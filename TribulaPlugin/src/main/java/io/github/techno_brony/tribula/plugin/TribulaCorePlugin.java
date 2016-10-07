package io.github.techno_brony.tribula.plugin;

import io.github.techno_brony.tribula.plugin.commands.CommandTribula;
import org.bukkit.plugin.java.JavaPlugin;

public class TribulaCorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("tribula").setExecutor(new CommandTribula(this));
        getServer().getPluginManager().registerEvents(new TribulaEventsConverter(this), this);
    }
}
