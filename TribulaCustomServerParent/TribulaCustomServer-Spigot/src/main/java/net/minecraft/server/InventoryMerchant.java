package net.minecraft.server;

import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.entity.HumanEntity;

import javax.annotation.Nullable;
import java.util.List;

// CraftBukkit start
// CraftBukkit end

public class InventoryMerchant implements IInventory {

    private final IMerchant merchant;
    private final ItemStack[] itemsInSlots = new ItemStack[3];
    private final EntityHuman player;
    public int selectedIndex;
    // CraftBukkit start - add fields and methods
    @SuppressWarnings("CanBeFinal")
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private MerchantRecipe recipe;
    private int maxStack = MAX_STACK;

    public InventoryMerchant(EntityHuman entityhuman, IMerchant imerchant) {
        this.player = entityhuman;
        this.merchant = imerchant;
    }

    public ItemStack[] getContents() {
        return this.itemsInSlots;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return (CraftVillager) ((EntityVillager) this.merchant).getBukkitEntity();
    }

    @Override
    public Location getLocation() {
        return ((EntityVillager) this.merchant).getBukkitEntity().getLocation();
    }
    // CraftBukkit end

    public int getSize() {
        return this.itemsInSlots.length;
    }

    @Nullable
    public ItemStack getItem(int i) {
        return this.itemsInSlots[i];
    }

    @Nullable
    public ItemStack splitStack(int i, int j) {
        if (i == 2 && this.itemsInSlots[i] != null) {
            return ContainerUtil.a(this.itemsInSlots, i, this.itemsInSlots[i].count);
        } else {
            ItemStack itemstack = ContainerUtil.a(this.itemsInSlots, i, j);

            if (itemstack != null && this.e(i)) {
                this.h();
            }

            return itemstack;
        }
    }

    private boolean e(int i) {
        return i == 0 || i == 1;
    }

    @Nullable
    public ItemStack splitWithoutUpdate(int i) {
        return ContainerUtil.a(this.itemsInSlots, i);
    }

    public void setItem(int i, @Nullable ItemStack itemstack) {
        this.itemsInSlots[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        if (this.e(i)) {
            this.h();
        }

    }

    public String getName() {
        return "mob.villager";
    }

    public boolean hasCustomName() {
        return false;
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatMessage(this.getName());
    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public void setMaxStackSize(int i) {
        maxStack = i;
    }

    public boolean a(EntityHuman entityhuman) {
        return this.merchant.getTrader() == entityhuman;
    }

    public void startOpen(EntityHuman entityhuman) {}

    public void closeContainer(EntityHuman entityhuman) {}

    public boolean b(int i, ItemStack itemstack) {
        return true;
    }

    public void update() {
        this.h();
    }

    public void h() {
        this.recipe = null;
        ItemStack itemstack = this.itemsInSlots[0];
        ItemStack itemstack1 = this.itemsInSlots[1];

        if (itemstack == null) {
            itemstack = itemstack1;
            itemstack1 = null;
        }

        if (itemstack == null) {
            this.setItem(2, null);
        } else {
            MerchantRecipeList merchantrecipelist = this.merchant.getOffers(this.player);

            if (merchantrecipelist != null) {
                MerchantRecipe merchantrecipe = merchantrecipelist.a(itemstack, itemstack1, this.selectedIndex);

                if (merchantrecipe != null && !merchantrecipe.h()) {
                    this.recipe = merchantrecipe;
                    this.setItem(2, merchantrecipe.getBuyItem3().cloneItemStack());
                } else if (itemstack1 != null) {
                    merchantrecipe = merchantrecipelist.a(itemstack1, itemstack, this.selectedIndex);
                    if (merchantrecipe != null && !merchantrecipe.h()) {
                        this.recipe = merchantrecipe;
                        this.setItem(2, merchantrecipe.getBuyItem3().cloneItemStack());
                    } else {
                        this.setItem(2, null);
                    }
                } else {
                    this.setItem(2, null);
                }
            }
        }

        this.merchant.a(this.getItem(2));
    }

    public MerchantRecipe getRecipe() {
        return this.recipe;
    }

    public void d(int i) {
        this.selectedIndex = i;
        this.h();
    }

    public int getProperty(int i) {
        return 0;
    }

    public void setProperty(int i, int j) {}

    public int g() {
        return 0;
    }

    public void l() {
        for (int i = 0; i < this.itemsInSlots.length; ++i) {
            this.itemsInSlots[i] = null;
        }

    }
}
