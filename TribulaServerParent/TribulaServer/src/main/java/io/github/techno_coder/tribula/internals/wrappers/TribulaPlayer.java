package io.github.techno_coder.tribula.internals.wrappers;

import io.github.techno_coder.tribula.internals.enums.TribulaPlayerDifficulty;
import io.github.techno_coder.tribula.internals.interfaces.ITribulaPlayer;
import net.minecraft.server.EntityPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class TribulaPlayer extends CraftPlayer implements ITribulaPlayer { //TODO Replace spawning of player with this instance

    public TribulaPlayer(CraftServer server, EntityPlayer entity) {
        super(server, entity);
    }


    @Override
    public TribulaPlayerDifficulty getStateDifficulty() { //TODO
        return null;
    }

    @Override
    public int getStateLevel() { //TODO
        return 0;
    }

    @Override
    public void getStateTribe() { //TODO

    }

    @Override
    public void getStateClass() { //TODO

    }
}
