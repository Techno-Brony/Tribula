package io.github.techno_brony.tribula.sql.commands;

import io.github.techno_brony.tribula.sql.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandSQL implements CommandExecutor {

    private final Main plugin;

    public CommandSQL(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("This command works :D"); //TODO
        return true;
    }
}
