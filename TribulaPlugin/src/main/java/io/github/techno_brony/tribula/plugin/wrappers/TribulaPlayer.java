package io.github.techno_brony.tribula.plugin.wrappers;

import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaPlayer;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;

public class TribulaPlayer extends CraftPlayer implements ITribulaPlayer {

    public TribulaPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
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
