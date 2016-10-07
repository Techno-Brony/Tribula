package io.github.techno_brony.tribula.plugin.wrappers.events;

import io.github.techno_brony.tribula.plugin.wrappers.TribulaPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class TribulaPlayerJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private UUID playerID;

    public TribulaPlayerJoinEvent(Player playerJoined) {
        playerID = playerJoined.getUniqueId();
    }

    public TribulaPlayer getTribulaPlayer() {
        return TribulaPlayer.getTribulaPlayer(playerID);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
