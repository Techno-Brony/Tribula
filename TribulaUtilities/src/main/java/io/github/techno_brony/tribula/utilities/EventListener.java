package io.github.techno_brony.tribula.utilities;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

class EventListener implements Listener {
    private final Main plugin;

    EventListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.getPlayer().sendMessage("You broke a block : " + event.getBlock().getType().name());
    }
}
