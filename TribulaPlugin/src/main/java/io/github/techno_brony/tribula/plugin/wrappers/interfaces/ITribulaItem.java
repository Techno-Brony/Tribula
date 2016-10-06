package io.github.techno_brony.tribula.plugin.wrappers.interfaces;

import io.github.techno_brony.tribula.plugin.wrappers.enums.TribulaItemRarity;
import io.github.techno_brony.tribula.plugin.wrappers.itemtypes.TribulaItemType;
import org.bukkit.inventory.ItemStack;

public interface ITribulaItem {
    ItemStack getDisplayItem();

    Class<?> getItemType();

    TribulaItemType getItemSpecificType();

    TribulaItemRarity getRarity();

    ITribulaEnchantment getEnchant();

    int getLevelRequirement();

    String getItemDisplayName();

    String getLore();
}
