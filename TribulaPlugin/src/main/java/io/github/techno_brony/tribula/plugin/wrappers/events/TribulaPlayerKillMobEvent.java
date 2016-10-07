package io.github.techno_brony.tribula.plugin.wrappers.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TribulaPlayerKillMobEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    //TODO

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
