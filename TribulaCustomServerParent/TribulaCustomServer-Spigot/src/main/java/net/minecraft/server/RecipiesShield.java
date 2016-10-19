package net.minecraft.server;

import javax.annotation.Nullable;
import java.util.Collections;

public class RecipiesShield {

    public RecipiesShield() {}

    public void a(CraftingManager craftingmanager) {
        craftingmanager.registerShapedRecipe(new ItemStack(Items.SHIELD), "WoW", "WWW", " W ", 'W', Blocks.PLANKS, 'o', Items.IRON_INGOT);
        craftingmanager.a(new RecipiesShield.Decoration(null));
    }

    static class SyntheticClass_1 {    }

    static class Decoration extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

        // CraftBukkit start - Delegate to new parent class with bogus info
        private Decoration() {
            super(new ItemStack(Items.SHIELD, 0 ,0), Collections.singletonList(new ItemStack(Items.BANNER, 0, 0)));
        }
        // CraftBukkit end

        Decoration(@SuppressWarnings("SameParameterValue") RecipiesShield.SyntheticClass_1 recipiesshield_syntheticclass_1) {
            this();
        }

        public boolean a(InventoryCrafting inventorycrafting, World world) {
            ItemStack itemstack = null;
            ItemStack itemstack1 = null;

            for (int i = 0; i < inventorycrafting.getSize(); ++i) {
                ItemStack itemstack2 = inventorycrafting.getItem(i);

                if (itemstack2 != null) {
                    if (itemstack2.getItem() == Items.BANNER) {
                        if (itemstack1 != null) {
                            return false;
                        }

                        itemstack1 = itemstack2;
                    } else {
                        if (itemstack2.getItem() != Items.SHIELD) {
                            return false;
                        }

                        if (itemstack != null) {
                            return false;
                        }

                        if (itemstack2.a("BlockEntityTag", false) != null) {
                            return false;
                        }

                        itemstack = itemstack2;
                    }
                }
            }

            return itemstack != null && itemstack1 != null;
        }

        @Nullable
        public ItemStack craftItem(InventoryCrafting inventorycrafting) {
            ItemStack itemstack = null;

            for (int i = 0; i < inventorycrafting.getSize(); ++i) {
                ItemStack itemstack1 = inventorycrafting.getItem(i);

                if (itemstack1 != null && itemstack1.getItem() == Items.BANNER) {
                    itemstack = itemstack1;
                }
            }

            ItemStack itemstack2 = new ItemStack(Items.SHIELD, 1, 0);
            EnumColor enumcolor;
            NBTTagCompound nbttagcompound;

            if (itemstack.hasTag()) {
                nbttagcompound = itemstack.getTag().g();
                enumcolor = EnumColor.fromInvColorIndex(TileEntityBanner.b(itemstack));
            } else {
                nbttagcompound = new NBTTagCompound();
                enumcolor = EnumColor.fromInvColorIndex(itemstack.h());
            }

            itemstack2.setTag(nbttagcompound);
            TileEntityBanner.a(itemstack2, enumcolor);
            return itemstack2;
        }

        public int a() {
            return 2;
        }

        @Nullable
        public ItemStack b() {
            return null;
        }

        public ItemStack[] b(InventoryCrafting inventorycrafting) {
            ItemStack[] aitemstack = new ItemStack[inventorycrafting.getSize()];

            for (int i = 0; i < aitemstack.length; ++i) {
                ItemStack itemstack = inventorycrafting.getItem(i);

                if (itemstack != null && itemstack.getItem().r()) {
                    aitemstack[i] = new ItemStack(itemstack.getItem().q());
                }
            }

            return aitemstack;
        }
    }
}
