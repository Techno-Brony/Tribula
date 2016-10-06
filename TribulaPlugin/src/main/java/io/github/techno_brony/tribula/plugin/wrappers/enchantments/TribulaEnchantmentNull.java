package io.github.techno_brony.tribula.plugin.wrappers.enchantments;

import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class TribulaEnchantmentNull implements ITribulaEnchantment {
    private static TribulaEnchantmentNull instance;

    public static TribulaEnchantmentNull getInstance() {
        if (instance == null) {
            instance = new TribulaEnchantmentNull();
        }
        return instance;
    }

    @Override
    public String getEnchantmentName() {
        return "None";
    }

    @Override
    public Enchantment getDisplayEnchantment() {
        return null;
    }

    @Override
    public int getDisplayEnchantmentLevel() {
        return 0;
    }

    @Override
    public void applyEnchantmentEffect(Player player) {

    }

}
