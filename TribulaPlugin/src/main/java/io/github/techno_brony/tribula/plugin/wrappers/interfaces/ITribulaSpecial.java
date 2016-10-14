package io.github.techno_brony.tribula.plugin.wrappers.interfaces;

import io.github.techno_brony.tribula.plugin.wrappers.enums.TribulaSpecialType;
import org.bukkit.entity.LivingEntity;

public interface ITribulaSpecial {
    void applySpecial(LivingEntity entity);

    TribulaSpecialType getSpecialType();
}
