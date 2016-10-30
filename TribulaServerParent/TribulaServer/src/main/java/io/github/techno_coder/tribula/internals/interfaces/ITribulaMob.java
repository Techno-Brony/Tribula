package io.github.techno_coder.tribula.internals.interfaces;

import io.github.techno_coder.tribula.internals.enums.TribulaMobTrigger;
import org.bukkit.entity.LivingEntity;

public interface ITribulaMob {
    String getTribulaName();
    double getTribulaNextDamage();
    LivingEntity getDisplayMob();
    int getTribulaLevel();
    int getTribulaHealth();
    int getTribulaMaxHealth();
    void getSpecials(); //TODO
    int getSpecialCooldownTicks();
    TribulaMobTrigger getAttackTrigger();
}
