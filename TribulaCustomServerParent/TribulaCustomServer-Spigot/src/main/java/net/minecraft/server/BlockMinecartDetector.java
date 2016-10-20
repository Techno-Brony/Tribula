package net.minecraft.server;

import com.google.common.base.Predicate;
import org.bukkit.event.block.BlockRedstoneEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BlockMinecartDetector extends BlockMinecartTrackAbstract {

    @SuppressWarnings("unchecked")
    public static final BlockStateEnum<BlockMinecartTrackAbstract.EnumTrackPosition> SHAPE = BlockStateEnum.a("shape", BlockMinecartTrackAbstract.EnumTrackPosition.class, new Predicate() {
        public boolean a(@Nullable BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition) {
            return blockminecarttrackabstract_enumtrackposition != BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST && blockminecarttrackabstract_enumtrackposition != BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST && blockminecarttrackabstract_enumtrackposition != BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST;
        }

        public boolean apply(Object object) {
            return this.a((BlockMinecartTrackAbstract.EnumTrackPosition) object);
        }
    });
    public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");

    public BlockMinecartDetector() {
        super(true);
        this.w(this.blockStateList.getBlockData().set(BlockMinecartDetector.POWERED, Boolean.FALSE).set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH));
        this.a(true);
    }

    public int a(World world) {
        return 20;
    }

    @SuppressWarnings("deprecation")
    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity) {
        if (!world.isClientSide) {
            if (!iblockdata.get(BlockMinecartDetector.POWERED)) {
                this.e(world, blockposition, iblockdata);
            }
        }
    }

    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {}

    public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        if (!world.isClientSide && iblockdata.get(BlockMinecartDetector.POWERED)) {
            this.e(world, blockposition, iblockdata);
        }
    }

    @SuppressWarnings("deprecation")
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return iblockdata.get(BlockMinecartDetector.POWERED) ? 15 : 0;
    }

    @SuppressWarnings("deprecation")
    public int c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return !iblockdata.get(BlockMinecartDetector.POWERED) ? 0 : (enumdirection == EnumDirection.UP ? 15 : 0);
    }

    private void e(World world, BlockPosition blockposition, IBlockData iblockdata) {
        boolean flag = iblockdata.get(BlockMinecartDetector.POWERED);
        boolean flag1 = false;
        //noinspection unchecked
        List list = this.a(world, blockposition, EntityMinecartAbstract.class);

        if (!list.isEmpty()) {
            flag1 = true;
        }

        // CraftBukkit start
        if (flag != flag1) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, flag ? 15 : 0, flag1 ? 15 : 0);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            flag1 = eventRedstone.getNewCurrent() > 0;
        }
        // CraftBukkit end

        if (flag1 && !flag) {
            world.setTypeAndData(blockposition, iblockdata.set(BlockMinecartDetector.POWERED, Boolean.TRUE), 3);
            this.b(world, blockposition, iblockdata, true);
            world.applyPhysics(blockposition, this);
            world.applyPhysics(blockposition.down(), this);
            world.b(blockposition, blockposition);
        }

        if (!flag1 && flag) {
            world.setTypeAndData(blockposition, iblockdata.set(BlockMinecartDetector.POWERED, Boolean.FALSE), 3);
            this.b(world, blockposition, iblockdata, false);
            world.applyPhysics(blockposition, this);
            world.applyPhysics(blockposition.down(), this);
            world.b(blockposition, blockposition);
        }

        if (flag1) {
            world.a(new BlockPosition(blockposition), this, this.a(world));
        }

        world.updateAdjacentComparators(blockposition, this);
    }

    protected void b(World world, BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        BlockMinecartTrackAbstract.MinecartTrackLogic blockminecarttrackabstract_minecarttracklogic = new BlockMinecartTrackAbstract.MinecartTrackLogic(world, blockposition, iblockdata);
        List list = blockminecarttrackabstract_minecarttracklogic.a();
        Iterator iterator = list.iterator();

        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            BlockPosition blockposition1 = (BlockPosition) iterator.next();
            IBlockData iblockdata1 = world.getType(blockposition1);

            if (iblockdata1 != null) {
                iblockdata1.doPhysics(world, blockposition1, iblockdata1.getBlock());
            }
        }

    }

    public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata) {
        super.onPlace(world, blockposition, iblockdata);
        this.e(world, blockposition, iblockdata);
    }

    public IBlockState<BlockMinecartTrackAbstract.EnumTrackPosition> g() {
        return BlockMinecartDetector.SHAPE;
    }

    @SuppressWarnings("deprecation")
    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    @SuppressWarnings("deprecation")
    public int d(IBlockData iblockdata, World world, BlockPosition blockposition) {
        if (iblockdata.get(BlockMinecartDetector.POWERED)) {
            //noinspection unchecked
            List list = this.a(world, blockposition, EntityMinecartCommandBlock.class);

            if (!list.isEmpty()) {
                return ((EntityMinecartCommandBlock) list.get(0)).getCommandBlock().k();
            }

            //noinspection unchecked
            List list1 = this.a(world, blockposition, EntityMinecartAbstract.class, IEntitySelector.c);

            if (!list1.isEmpty()) {
                return Container.b((IInventory) list1.get(0));
            }
        }

        return 0;
    }

    protected <T extends EntityMinecartAbstract> List<T> a(World world, BlockPosition blockposition, Class<T> oclass, @SuppressWarnings("SameParameterValue") Predicate<Entity>... apredicate) {
        AxisAlignedBB axisalignedbb = this.a(blockposition);

        return apredicate.length != 1 ? world.a(oclass, axisalignedbb) : world.a(oclass, axisalignedbb, apredicate[0]);
    }

    private AxisAlignedBB a(BlockPosition blockposition) {
        float f = 0.2F;

        return new AxisAlignedBB((double) ((float) blockposition.getX() + 0.2F), (double) blockposition.getY(), (double) ((float) blockposition.getZ() + 0.2F), (double) ((float) (blockposition.getX() + 1) - 0.2F), (double) ((float) (blockposition.getY() + 1) - 0.2F), (double) ((float) (blockposition.getZ() + 1) - 0.2F));
    }

    @SuppressWarnings("deprecation")
    public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.a(i & 7)).set(BlockMinecartDetector.POWERED, (i & 8) > 0);
    }

    public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | iblockdata.get(BlockMinecartDetector.SHAPE).a();

        if (iblockdata.get(BlockMinecartDetector.POWERED)) {
            i |= 8;
        }

        return i;
    }

    @SuppressWarnings("deprecation")
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        switch (BlockMinecartDetector.SyntheticClass_1.b[enumblockrotation.ordinal()]) {
        case 1:
            switch (BlockMinecartDetector.SyntheticClass_1.a[iblockdata.get(BlockMinecartDetector.SHAPE).ordinal()]) {
            case 1:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST);

            case 2:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST);

            case 3:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH);

            case 4:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH);

            case 5:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST);

            case 6:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST);

            case 7:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST);

            case 8:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST);
            }

        case 2:
            switch (BlockMinecartDetector.SyntheticClass_1.a[iblockdata.get(BlockMinecartDetector.SHAPE).ordinal()]) {
            case 1:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH);

            case 2:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH);

            case 3:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST);

            case 4:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST);

            case 5:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST);

            case 6:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST);

            case 7:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST);

            case 8:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST);

            case 9:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST);

            case 10:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH);
            }

        case 3:
            switch (BlockMinecartDetector.SyntheticClass_1.a[iblockdata.get(BlockMinecartDetector.SHAPE).ordinal()]) {
            case 1:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH);

            case 2:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH);

            case 3:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST);

            case 4:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST);

            case 5:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST);

            case 6:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST);

            case 7:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST);

            case 8:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST);

            case 9:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST);

            case 10:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH);
            }

        default:
            return iblockdata;
        }
    }

    @SuppressWarnings("deprecation")
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        BlockMinecartTrackAbstract.EnumTrackPosition blockminecarttrackabstract_enumtrackposition = iblockdata.get(BlockMinecartDetector.SHAPE);

        switch (BlockMinecartDetector.SyntheticClass_1.c[enumblockmirror.ordinal()]) {
        case 1:
            switch (BlockMinecartDetector.SyntheticClass_1.a[blockminecarttrackabstract_enumtrackposition.ordinal()]) {
            case 3:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH);

            case 4:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH);

            case 5:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST);

            case 6:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST);

            case 7:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST);

            case 8:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST);

            default:
                //noinspection deprecation
                return super.a(iblockdata, enumblockmirror);
            }

        case 2:
            switch (BlockMinecartDetector.SyntheticClass_1.a[blockminecarttrackabstract_enumtrackposition.ordinal()]) {
            case 1:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST);

            case 2:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST);

            case 3:
            case 4:
            default:
                break;

            case 5:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST);

            case 6:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST);

            case 7:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST);

            case 8:
                return iblockdata.set(BlockMinecartDetector.SHAPE, BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST);
            }
        }

        //noinspection deprecation
        return super.a(iblockdata, enumblockmirror);
    }

    protected BlockStateList getStateList() {
        return new BlockStateList(this, BlockMinecartDetector.SHAPE, BlockMinecartDetector.POWERED);
    }

    static class SyntheticClass_1 {

        static final int[] a;
        static final int[] b;
        static final int[] c = new int[EnumBlockMirror.values().length];

        static {
            try {
                BlockMinecartDetector.SyntheticClass_1.c[EnumBlockMirror.LEFT_RIGHT.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.c[EnumBlockMirror.FRONT_BACK.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            b = new int[EnumBlockRotation.values().length];

            try {
                BlockMinecartDetector.SyntheticClass_1.b[EnumBlockRotation.CLOCKWISE_180.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.b[EnumBlockRotation.COUNTERCLOCKWISE_90.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.b[EnumBlockRotation.CLOCKWISE_90.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            a = new int[BlockMinecartTrackAbstract.EnumTrackPosition.values().length];

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_EAST.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_WEST.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.ASCENDING_SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_EAST.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.SOUTH_WEST.ordinal()] = 6;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_WEST.ordinal()] = 7;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_EAST.ordinal()] = 8;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.NORTH_SOUTH.ordinal()] = 9;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockMinecartDetector.SyntheticClass_1.a[BlockMinecartTrackAbstract.EnumTrackPosition.EAST_WEST.ordinal()] = 10;
            } catch (NoSuchFieldError ignored) {
            }

        }
    }
}
