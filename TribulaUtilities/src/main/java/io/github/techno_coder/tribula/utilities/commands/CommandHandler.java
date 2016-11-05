package io.github.techno_coder.tribula.utilities.commands;

import io.github.techno_coder.tribula.internals.wrappers.TribulaPlayer;
import io.github.techno_coder.tribula.utilities.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {

    private final Main plugin;

    public CommandHandler(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("This command works :D");
        if (sender instanceof TribulaPlayer) {
            sender.sendMessage(Integer.toString(((TribulaPlayer) sender).getStateLevel()));
        }
        return true;
    }
}
