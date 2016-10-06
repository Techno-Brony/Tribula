package io.github.techno_brony.tribula.plugin.wrappers;

import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TribulaPlayer implements ITribulaPlayer {

    private static HashMap<UUID, TribulaPlayer> playerHashMap = new HashMap<>();

    private Player player;
    //TODO private fields for caching

    public TribulaPlayer(Player player) {
        this.player = player;
        //TODO set all this shit
    }

    public static TribulaPlayer getTribulaPlayer(Player player) {
        UUID uniqueID = player.getUniqueId();
        if (!playerHashMap.containsKey(uniqueID)) {
            playerHashMap.put(uniqueID, new TribulaPlayer(player));
        }
        return playerHashMap.get(player.getUniqueId());
    }

    public UUID getPlayerUUID() {
        return player.getUniqueId();
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void getDonorRank() {
        //TODO
    }

    @Override
    public void getEquippedPet() {
        //TODO
    }

    @Override
    public void getCurrentClass() {
        //TODO
    }

    @Override
    public void getCurrentTribe() {
        //TODO
    }

    @Override
    public void getCurrentParty() {
        //TODO
    }

    @Override
    public void getCurrentDifficulty() {
        //TODO
    }
}
