package io.github.techno_brony.tribula.plugin.wrappers.specials.damage;

import io.github.techno_brony.tribula.plugin.wrappers.specials.TribulaSpecialDamage;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftFireball;
import org.bukkit.entity.LivingEntity;

public class TribulaSpecialFireball extends TribulaSpecialDamage {

    private static TribulaSpecialFireball instance = new TribulaSpecialFireball();

    public static TribulaSpecialFireball getInstance() {
        return instance;
    }

    @Override
    public void applySpecial(LivingEntity entity) {
        entity.launchProjectile(CraftFireball.class);
    }
}
