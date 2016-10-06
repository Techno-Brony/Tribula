package io.github.techno_brony.tribula.plugin.wrappers;

import io.github.techno_brony.tribula.plugin.wrappers.enums.TribulaItemRarity;
import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaEnchantment;
import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaItem;
import io.github.techno_brony.tribula.plugin.wrappers.itemtypes.TribulaItemType;
import org.bukkit.Material;

public class TribulaItem implements ITribulaItem {

    private final String itemDisplayName;
    private final TribulaItemType itemType;
    private final TribulaItemRarity itemRarity;
    private final Material displayItem;
    private final ITribulaEnchantment itemEnchantment;
    private final int levelRequirement;

    public TribulaItem(String itemDisplayName, TribulaItemType itemType, TribulaItemRarity itemRarity, Material displayItem, ITribulaEnchantment itemEnchantment, int levelRequirement) {
        this.itemDisplayName = itemDisplayName;
        this.itemType = itemType;
        this.itemRarity = itemRarity;
        this.displayItem = displayItem;
        this.itemEnchantment = itemEnchantment;
        this.levelRequirement = levelRequirement;
    }

    @Override
    public Material getDisplayItem() {
        return displayItem;
    }

    @Override
    public Class<?> getItemType() {
        return itemType.getClass().getSuperclass();
    }

    @Override
    public TribulaItemType getItemSpecificType() {
        return itemType;
    }

    @Override
    public TribulaItemRarity getRarity() {
        return itemRarity;
    }

    @Override
    public ITribulaEnchantment getEnchant() {
        return itemEnchantment;
    }

    @Override
    public int getLevelRequirement() {
        return levelRequirement;
    }

    @Override
    public String getItemDisplayName() {
        return itemDisplayName;
    }
}
