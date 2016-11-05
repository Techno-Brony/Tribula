package io.github.techno_coder.tribula.mobs.wrappers;

import io.github.techno_coder.tribula.internals.enums.TribulaMobTrigger;
import io.github.techno_coder.tribula.internals.wrappers.TribulaSpecial;
import io.github.techno_coder.tribula.mobs.interfaces.ITribulaBoss;
import net.minecraft.server.EntityLiving;

import java.util.List;

public class TribulaBoss implements ITribulaBoss { //TODO
    private EntityLiving displayMob;
    private String bossName;

    @Override
    public String getTribulaName() {
        return bossName;
    }

    @Override
    public double getTribulaNextDamage() {
        return 0; //TODO
    }

    @Override
    public EntityLiving getDisplayMob() {
        return displayMob;
    }

    @Override
    public int getTribulaLevel() {
        return 0; //TODO
    }

    @Override
    public double getTribulaHealth() {
        return 0; //TODO
    }

    @Override
    public double getTribulaMaxHealth() {
        return 0; //TODO
    }

    @Override
    public List<TribulaSpecial> getSpecials() {
        return null; //TODO
    }

    @Override
    public int getSpecialCooldownTicks() {
        return 0; //TODO
    }

    @Override
    public TribulaMobTrigger getAttackTrigger() {
        return null; //TODO
    }
}
