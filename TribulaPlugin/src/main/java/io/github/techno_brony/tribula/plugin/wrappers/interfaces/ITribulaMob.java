package io.github.techno_brony.tribula.plugin.wrappers.interfaces;

import io.github.techno_brony.tribula.plugin.wrappers.enums.TribulaMobAttackTrigger;
import org.bukkit.entity.LivingEntity;

public interface ITribulaMob {
    String getDisplayName();

    LivingEntity getMobType();

    int getLevel();

    TribulaMobAttackTrigger getAttackTrigger();

    void getSpecial(); //TODO
}
