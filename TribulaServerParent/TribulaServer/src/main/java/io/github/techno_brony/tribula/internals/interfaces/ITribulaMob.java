package io.github.techno_brony.tribula.internals.interfaces;

import io.github.techno_brony.tribula.internals.enums.TribulaMobTrigger;
import org.bukkit.entity.LivingEntity;

public interface ITribulaMob {
    String getName();
    double getTribulaNextDamage();
    LivingEntity getDisplayMob();
    int getLevel();
    int getTribulaHealth();
    int getTribulaMaxHealth();
    void getSpecial(); //TODO
    TribulaMobTrigger getAttackTrigger();
}
