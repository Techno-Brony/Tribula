package io.github.techno_brony.tribula.plugin;

import io.github.techno_brony.tribula.plugin.commands.CommandTribulaPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TribulaCorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("tribula").setExecutor(new CommandTribulaPlugin(this));
    }
}
