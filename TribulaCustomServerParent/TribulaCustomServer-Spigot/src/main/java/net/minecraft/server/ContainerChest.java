package net.minecraft.server;

import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;

import javax.annotation.Nullable;

// CraftBukkit start
// CraftBukkit end

public class ContainerChest extends Container {

    private final IInventory container;
    private final int f;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    @SuppressWarnings("CanBeFinal")
    private PlayerInventory player;

    public ContainerChest(IInventory iinventory, IInventory iinventory1, EntityHuman entityhuman) {
        this.container = iinventory1;
        this.f = iinventory1.getSize() / 9;
        iinventory1.startOpen(entityhuman);
        int i = (this.f - 4) * 18;

        // CraftBukkit start - Save player
        // TODO: Should we check to make sure it really is an InventoryPlayer?
        this.player = (PlayerInventory) iinventory;
        // CraftBukkit end

        int j;
        int k;

        for (j = 0; j < this.f; ++j) {
            for (k = 0; k < 9; ++k) {
                this.a(new Slot(iinventory1, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.a(new Slot(iinventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(iinventory, j, 8 + j * 18, 161 + i));
        }

    }
    // CraftBukkit end

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory;
        if (this.container instanceof PlayerInventory) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryPlayer((PlayerInventory) this.container);
        } else if (this.container instanceof InventoryLargeChest) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) this.container);
        } else {
            inventory = new CraftInventory(this.container);
        }

        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }

    public boolean a(EntityHuman entityhuman) {
        // CraftBukkit
        return this.checkReachable && !this.container.a(entityhuman);
    }

    @Nullable
    public ItemStack b(EntityHuman entityhuman, int i) {
        ItemStack itemstack = null;
        Slot slot = this.c.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i < this.f * 9) {
                if (this.a(itemstack1, this.f * 9, this.c.size(), true)) {
                    return null;
                }
            } else if (this.a(itemstack1, 0, this.f * 9, false)) {
                return null;
            }

            if (itemstack1.count == 0) {
                slot.set(null);
            } else {
                slot.f();
            }
        }

        return itemstack;
    }

    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.container.closeContainer(entityhuman);
    }

    public IInventory e() {
        return this.container;
    }
}
