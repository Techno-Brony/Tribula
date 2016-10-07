package io.github.techno_brony.tribula.plugin.wrappers;

import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TribulaPlayer implements ITribulaPlayer {

    private static HashMap<UUID, TribulaPlayer> playerHashMap = new HashMap<>();
    int level = 0;
    private UUID playerUUID;
    //TODO private fields for caching

    private TribulaPlayer(UUID playerID) {
        this.playerUUID = playerID;
        //TODO set all this shit
    }

    public static TribulaPlayer getTribulaPlayer(UUID playerUUID) {
        if (!playerHashMap.containsKey(playerUUID)) {
            playerHashMap.put(playerUUID, new TribulaPlayer(playerUUID));
        }
        return playerHashMap.get(playerUUID);
    }

    public static TribulaPlayer getTribulaPlayer(Player player) {
        return getTribulaPlayer(player.getUniqueId());
    }

    public UUID getPlayerUUID() {
        return playerUUID;
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

    @Override
    public int getCurrentLevel() {
        return 0; //TODO
    }
}
