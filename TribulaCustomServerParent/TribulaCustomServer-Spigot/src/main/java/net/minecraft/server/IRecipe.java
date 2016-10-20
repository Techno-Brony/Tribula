package net.minecraft.server;

import javax.annotation.Nullable;

public interface IRecipe {

    boolean a(InventoryCrafting inventorycrafting, @SuppressWarnings("UnusedParameters") World world);

    @Nullable
    ItemStack craftItem(InventoryCrafting inventorycrafting);

    int a();

    @SuppressWarnings("unused")
    @Nullable
    ItemStack b();

    ItemStack[] b(InventoryCrafting inventorycrafting);

    org.bukkit.inventory.Recipe toBukkitRecipe(); // CraftBukkit

    @SuppressWarnings("unused")
    java.util.List<ItemStack> getIngredients(); // Spigot
}
