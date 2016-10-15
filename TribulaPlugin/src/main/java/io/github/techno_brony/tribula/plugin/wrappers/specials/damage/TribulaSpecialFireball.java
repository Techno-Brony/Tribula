package io.github.techno_brony.tribula.plugin.wrappers.specials.damage;

import io.github.techno_brony.tribula.plugin.wrappers.specials.TribulaSpecialDamage;
import net.minecraft.server.v1_10_R1.EntityLiving;

public class TribulaSpecialFireball extends TribulaSpecialDamage {

    private static TribulaSpecialFireball instance = new TribulaSpecialFireball();

    public static TribulaSpecialFireball getInstance() {
        return instance;
    }

    @Override
    public void applySpecial(EntityLiving entity) {
        //TODO
    }
}
