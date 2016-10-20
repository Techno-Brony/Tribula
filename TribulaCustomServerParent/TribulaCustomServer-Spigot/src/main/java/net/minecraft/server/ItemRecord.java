package net.minecraft.server;

import com.google.common.collect.Maps;

import java.util.Map;

public class ItemRecord extends Item {

    private static final Map<SoundEffect, ItemRecord> a = Maps.newHashMap();

    @SuppressWarnings("unused")
    protected ItemRecord(String s, SoundEffect soundeffect) {
        String c = "item.record." + s + ".desc";
        this.maxStackSize = 1;
        this.a(CreativeModeTab.f);
        ItemRecord.a.put(soundeffect, this);
    }

    public EnumInteractionResult a(ItemStack itemstack, EntityHuman entityhuman, World world, BlockPosition blockposition, EnumHand enumhand, EnumDirection enumdirection, float f, float f1, float f2) {
        IBlockData iblockdata = world.getType(blockposition);

        if (iblockdata.getBlock() == Blocks.JUKEBOX && !iblockdata.get(BlockJukeBox.HAS_RECORD)) {
            if (!world.isClientSide) {
                return EnumInteractionResult.SUCCESS; // CraftBukkit - handled in ItemStack
//                ((BlockJukeBox) Blocks.JUKEBOX).a(world, blockposition, iblockdata, itemstack);
//                world.a(null, 1010, blockposition, Item.getId(this));
//                --itemstack.count;
//                entityhuman.b(StatisticList.Z);
            }

            return EnumInteractionResult.SUCCESS;
        } else {
            return EnumInteractionResult.PASS;
        }
    }

    public EnumItemRarity g(ItemStack itemstack) {
        return EnumItemRarity.RARE;
    }
}
