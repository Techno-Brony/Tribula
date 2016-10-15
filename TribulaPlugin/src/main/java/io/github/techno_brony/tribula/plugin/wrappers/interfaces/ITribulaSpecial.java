package io.github.techno_brony.tribula.plugin.wrappers.interfaces;

import io.github.techno_brony.tribula.plugin.wrappers.enums.TribulaSpecialType;
import net.minecraft.server.v1_10_R1.EntityLiving;

public interface ITribulaSpecial {
    void applySpecial(EntityLiving entity);

    TribulaSpecialType getSpecialType();
}
