package io.github.techno_coder.tribula.internals.interfaces;

import io.github.techno_coder.tribula.internals.enums.TribulaMobTrigger;
import io.github.techno_coder.tribula.internals.wrappers.TribulaSpecial;
import net.minecraft.server.EntityLiving;

import java.util.List;

public interface ITribulaMob {

    /**
     * Returns the custom name of the mob
     * @return The name of the mob
     */
    String getTribulaName();

    /**
     * Gets the next amount of damage this mob should deal
     * @return The amount of damage
     */
    double getTribulaNextDamage();

    /**
     * Gets the mob that should be displayed
     * @return A vanilla server mob class
     */
    EntityLiving getDisplayMob();

    /**
     * Gets the level of this mob
     * @return The level
     */
    int getTribulaLevel();

    /**
     * Get the current health this mob is at
     * @return The amount of health
     */
    double getTribulaHealth();

    /**
     * Gets the maximum amount of health this mob could be at
     * @return The max health
     */
    double getTribulaMaxHealth();

    /**
     * Gets a list of specials
     * @return a list of specials
     */
    List<TribulaSpecial> getSpecials(); //TODO

    /**
     * Gets the amount of ticks per special usage
     * @return the amount of ticks
     */
    int getSpecialCooldownTicks();

    /**
     * Gets when this mob attacks
     * @return An attack trigger
     */
    TribulaMobTrigger getAttackTrigger();
}
