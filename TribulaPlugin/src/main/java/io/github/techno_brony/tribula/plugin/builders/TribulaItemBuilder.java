package io.github.techno_brony.tribula.plugin.builders;

import io.github.techno_brony.tribula.plugin.wrappers.TribulaItem;
import io.github.techno_brony.tribula.plugin.wrappers.enchantments.TribulaEnchantmentNull;
import io.github.techno_brony.tribula.plugin.wrappers.enums.TribulaItemRarity;
import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaEnchantment;
import io.github.techno_brony.tribula.plugin.wrappers.itemtypes.TribulaItemType;
import org.bukkit.Material;

public class TribulaItemBuilder {

    private String itemDisplayName;
    private TribulaItemType itemType;
    private TribulaItemRarity itemRarity;
    private Material displayItem;
    private ITribulaEnchantment itemEnchantment = TribulaEnchantmentNull.getInstance();
    private int levelRequirement;
    private String lore;

    public TribulaItemBuilder setItemDisplayName(String itemDisplayName) {
        this.itemDisplayName = itemDisplayName;
        return this;
    }

    public TribulaItemBuilder setItemType(TribulaItemType itemType) {
        this.itemType = itemType;
        return this;
    }

    public TribulaItemBuilder setItemRarity(TribulaItemRarity itemRarity) {
        this.itemRarity = itemRarity;
        return this;
    }

    public TribulaItemBuilder setDisplayItem(Material displayItem) {
        this.displayItem = displayItem;
        return this;
    }

    public TribulaItemBuilder setItemEnchantment(ITribulaEnchantment itemEnchantment) {
        this.itemEnchantment = itemEnchantment;
        return this;
    }

    public TribulaItemBuilder setLevelRequirement(int levelRequirement) {
        this.levelRequirement = levelRequirement;
        return this;
    }

    public TribulaItemBuilder setLore(String lore) {
        this.lore = lore;
        return this;
    }

    public TribulaItem build() {
        return new TribulaItem(itemDisplayName, itemType, itemRarity, displayItem, itemEnchantment, levelRequirement, lore);
    }

}
