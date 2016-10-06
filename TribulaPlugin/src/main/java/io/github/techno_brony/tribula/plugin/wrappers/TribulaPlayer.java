package io.github.techno_brony.tribula.plugin.wrappers;

import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaPlayer;
import org.bukkit.entity.Player;

public class TribulaPlayer implements ITribulaPlayer {

    private Player player;

    public TribulaPlayer(Player player) {
        this.player = player;
        //TODO set all this shit
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
