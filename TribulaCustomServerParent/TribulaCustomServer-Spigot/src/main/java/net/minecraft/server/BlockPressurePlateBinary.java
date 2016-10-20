package net.minecraft.server;

import org.bukkit.event.entity.EntityInteractEvent;

import java.util.Iterator;
import java.util.List;

public class BlockPressurePlateBinary extends BlockPressurePlateAbstract {

    public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
    private final BlockPressurePlateBinary.EnumMobType e;

    protected BlockPressurePlateBinary(Material material, BlockPressurePlateBinary.EnumMobType blockpressureplatebinary_enummobtype) {
        super(material);
        this.w(this.blockStateList.getBlockData().set(BlockPressurePlateBinary.POWERED, Boolean.FALSE));
        this.e = blockpressureplatebinary_enummobtype;
    }

    protected int getPower(IBlockData iblockdata) {
        return iblockdata.get(BlockPressurePlateBinary.POWERED) ? 15 : 0;
    }

    protected IBlockData a(IBlockData iblockdata, int i) {
        return iblockdata.set(BlockPressurePlateBinary.POWERED, i > 0);
    }

    protected void b(World world, BlockPosition blockposition) {
        if (this.material == Material.WOOD) {
            world.a(null, blockposition, SoundEffects.hy, SoundCategory.BLOCKS, 0.3F, 0.8F);
        } else {
            world.a(null, blockposition, SoundEffects.gs, SoundCategory.BLOCKS, 0.3F, 0.6F);
        }

    }

    protected void c(World world, BlockPosition blockposition) {
        if (this.material == Material.WOOD) {
            world.a(null, blockposition, SoundEffects.hx, SoundCategory.BLOCKS, 0.3F, 0.7F);
        } else {
            world.a(null, blockposition, SoundEffects.gr, SoundCategory.BLOCKS, 0.3F, 0.5F);
        }

    }

    protected int e(World world, BlockPosition blockposition) {
        AxisAlignedBB axisalignedbb = BlockPressurePlateBinary.c.a(blockposition);
        List list;

        switch (BlockPressurePlateBinary.SyntheticClass_1.a[this.e.ordinal()]) {
        case 1:
            list = world.getEntities(null, axisalignedbb);
            break;

        case 2:
            list = world.a(EntityLiving.class, axisalignedbb);
            break;

        default:
            return 0;
        }

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            //noinspection WhileLoopReplaceableByForEach
            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                // CraftBukkit start - Call interact event when turning on a pressure plate
                if (this.getPower(world.getType(blockposition)) == 0) {
                    org.bukkit.World bworld = world.getWorld();
                    org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
                    org.bukkit.event.Cancellable cancellable;

                    if (entity instanceof EntityHuman) {
                        cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityHuman) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
                    } else {
                        cancellable = new EntityInteractEvent(entity.getBukkitEntity(), bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                        manager.callEvent((EntityInteractEvent) cancellable);
                    }

                    // We only want to block turning the plate on if all events are cancelled
                    if (cancellable.isCancelled()) {
                        continue;
                    }
                }
                // CraftBukkit end

                if (entity.isIgnoreBlockTrigger()) {
                    return 15;
                }
            }
        }

        return 0;
    }

    @SuppressWarnings("deprecation")
    public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockPressurePlateBinary.POWERED, i == 1);
    }

    public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockPressurePlateBinary.POWERED) ? 1 : 0;
    }

    protected BlockStateList getStateList() {
        return new BlockStateList(this, BlockPressurePlateBinary.POWERED);
    }

    public enum EnumMobType {

        EVERYTHING, MOBS;

        EnumMobType() {}
    }

    static class SyntheticClass_1 {

        static final int[] a = new int[BlockPressurePlateBinary.EnumMobType.values().length];

        static {
            try {
                BlockPressurePlateBinary.SyntheticClass_1.a[BlockPressurePlateBinary.EnumMobType.EVERYTHING.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockPressurePlateBinary.SyntheticClass_1.a[BlockPressurePlateBinary.EnumMobType.MOBS.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

        }
    }
}
