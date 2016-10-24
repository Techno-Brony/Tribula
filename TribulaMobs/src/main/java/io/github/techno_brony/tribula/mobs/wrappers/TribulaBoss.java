package io.github.techno_brony.tribula.mobs.wrappers;

import io.github.techno_brony.tribula.internals.enums.TribulaMobTrigger;
import io.github.techno_brony.tribula.internals.interfaces.ITribulaMob;
import org.bukkit.entity.LivingEntity;

public class TribulaBoss implements ITribulaMob { //TODO
    private LivingEntity displayMob;
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
    public LivingEntity getDisplayMob() {
        return displayMob;
    }

    @Override
    public int getTribulaLevel() {
        return 0; //TODO
    }

    @Override
    public int getTribulaHealth() {
        return 0; //TODO
    }

    @Override
    public int getTribulaMaxHealth() {
        return 0; //TODO
    }

    @Override
    public void getSpecial() {
        //TODO
    }

    public void getSecondSpecial() {
        //TODO
    }

    @Override
    public TribulaMobTrigger getAttackTrigger() {
        return null; //TODO
    }
}
