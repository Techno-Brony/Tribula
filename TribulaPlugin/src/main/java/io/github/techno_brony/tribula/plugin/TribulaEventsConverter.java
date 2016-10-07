package io.github.techno_brony.tribula.plugin;

import io.github.techno_brony.tribula.plugin.wrappers.TribulaPlayer;
import io.github.techno_brony.tribula.plugin.wrappers.events.TribulaPlayerJoinEvent;
import io.github.techno_brony.tribula.plugin.wrappers.events.TribulaPlayerKillMobEvent;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

class TribulaEventsConverter implements Listener {
    private final TribulaCorePlugin plugin;

    TribulaEventsConverter(TribulaCorePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        TribulaPlayer.getTribulaPlayer(event.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        callEvent(new TribulaPlayerJoinEvent(event.getPlayer()));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            callEvent(new TribulaPlayerKillMobEvent()); //TODO
        }
    }

    private void callEvent(Event event) {
        plugin.getServer().getPluginManager().callEvent(event);
    }

}
