package net.minecraft.server;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;

import javax.annotation.Nullable;

public interface IInventory extends INamableTileEntity {

    int MAX_STACK = 64;

    int getSize();

    @Nullable
    ItemStack getItem(int i);

    @Nullable
    ItemStack splitStack(int i, int j);

    @Nullable
    ItemStack splitWithoutUpdate(int i);

    void setItem(int i, @Nullable ItemStack itemstack);

    int getMaxStackSize();

    void setMaxStackSize(int size);

    void update();

    boolean a(EntityHuman entityhuman);

    void startOpen(EntityHuman entityhuman);

    void closeContainer(EntityHuman entityhuman);

    boolean b(int i, ItemStack itemstack);

    int getProperty(int i);

    void setProperty(int i, int j);

    int g();

    @SuppressWarnings("unused")
    void l();

    // CraftBukkit start
    ItemStack[] getContents();

    void onOpen(CraftHumanEntity who);

    void onClose(CraftHumanEntity who);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    org.bukkit.Location getLocation();
    // CraftBukkit end
}
