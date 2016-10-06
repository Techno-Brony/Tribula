package io.github.techno_brony.tribula.plugin.wrappers.interfaces;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public interface ITribulaEnchantment {
    String getEnchantmentName();
    Enchantment getDisplayEnchantment();

    int getDisplayEnchantmentLevel();
    void applyEnchantmentEffect(Player player);
}
