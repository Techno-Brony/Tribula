package io.github.techno_brony.tribula.plugin.wrappers;

import com.google.common.base.CaseFormat;
import io.github.techno_brony.tribula.plugin.wrappers.enchantments.TribulaEnchantmentNull;
import io.github.techno_brony.tribula.plugin.wrappers.enums.TribulaItemRarity;
import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaEnchantment;
import io.github.techno_brony.tribula.plugin.wrappers.interfaces.ITribulaItem;
import io.github.techno_brony.tribula.plugin.wrappers.itemtypes.TribulaItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TribulaItem extends ItemStack implements ITribulaItem {

    private final String itemDisplayName;
    private final TribulaItemType itemType;
    private final TribulaItemRarity itemRarity;
    private final ITribulaEnchantment itemEnchantment;
    private final int levelRequirement;

    private final String lore;

    public TribulaItem(String itemDisplayName, TribulaItemType itemType, TribulaItemRarity itemRarity, Material displayItem, ITribulaEnchantment itemEnchantment, int levelRequirement, String lore) {
        super(displayItem);

        this.itemDisplayName = itemDisplayName;
        this.itemType = itemType;
        this.itemRarity = itemRarity;
        this.itemEnchantment = itemEnchantment;
        this.levelRequirement = levelRequirement;
        this.lore = lore;

        ItemMeta displayItemMeta = super.getItemMeta();
        displayItemMeta.setDisplayName(itemDisplayName);
        ArrayList<String> displayItemMetaLore = new ArrayList<>();

        displayItemMetaLore.add(ChatColor.GOLD + lore);
        displayItemMetaLore.add("");
        displayItemMetaLore.add(ChatColor.GREEN + itemType.getTypeName() + " Item");
        displayItemMetaLore.add(ChatColor.LIGHT_PURPLE + "Rarity: " + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, itemRarity.toString()));
        if (itemEnchantment != TribulaEnchantmentNull.getInstance()) {
            displayItemMeta.addEnchant(itemEnchantment.getDisplayEnchantment(), itemEnchantment.getDisplayEnchantmentLevel(), true);
            displayItemMetaLore.add(ChatColor.AQUA + "Enchantment: " + itemEnchantment.getEnchantmentName());
        }
        displayItemMetaLore.add(ChatColor.RED + "Level Required: " + levelRequirement);

        displayItemMeta.setLore(displayItemMetaLore);
        super.setItemMeta(displayItemMeta);
    }

    @Override
    public ItemStack getDisplayItem() {
        return super.clone();
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

    @Override
    public String getLore() {
        return lore;
    }
}
