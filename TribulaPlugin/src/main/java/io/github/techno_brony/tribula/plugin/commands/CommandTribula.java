package io.github.techno_brony.tribula.plugin.commands;

import io.github.techno_brony.tribula.plugin.TribulaCorePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTribula implements CommandExecutor {

    private final TribulaCorePlugin plugin;

    public CommandTribula(TribulaCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "This plugin is a dependency for all other Tribula Plugins.");

        //TODO REMOVE
        if (sender instanceof Player) {

        }

        return true;
    }
}
