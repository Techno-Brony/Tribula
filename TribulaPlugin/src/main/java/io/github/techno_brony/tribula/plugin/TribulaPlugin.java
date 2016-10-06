package io.github.techno_brony.tribula.plugin;

import io.github.techno_brony.tribula.plugin.commands.CommandTribulaPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TribulaPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("tribulaplugin").setExecutor(new CommandTribulaPlugin(this));
    }
}
