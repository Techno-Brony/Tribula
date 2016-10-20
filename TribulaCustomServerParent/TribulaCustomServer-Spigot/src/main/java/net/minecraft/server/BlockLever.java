package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent;

import javax.annotation.Nullable;
import java.util.Iterator;

public class BlockLever extends Block {

    public static final BlockStateEnum<BlockLever.EnumLeverPosition> FACING = BlockStateEnum.of("facing", BlockLever.EnumLeverPosition.class);
    public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
    protected static final AxisAlignedBB c = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.625D, 0.6875D, 0.800000011920929D, 1.0D);
    protected static final AxisAlignedBB d = new AxisAlignedBB(0.3125D, 0.20000000298023224D, 0.0D, 0.6875D, 0.800000011920929D, 0.375D);
    protected static final AxisAlignedBB e = new AxisAlignedBB(0.625D, 0.20000000298023224D, 0.3125D, 1.0D, 0.800000011920929D, 0.6875D);
    protected static final AxisAlignedBB f = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3125D, 0.375D, 0.800000011920929D, 0.6875D);
    protected static final AxisAlignedBB g = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.6000000238418579D, 0.75D);
    protected static final AxisAlignedBB B = new AxisAlignedBB(0.25D, 0.4000000059604645D, 0.25D, 0.75D, 1.0D, 0.75D);

    protected BlockLever() {
        super(Material.ORIENTABLE);
        this.w(this.blockStateList.getBlockData().set(BlockLever.FACING, BlockLever.EnumLeverPosition.NORTH).set(BlockLever.POWERED, Boolean.FALSE));
        this.a(CreativeModeTab.d);
    }

    protected static boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        return BlockButtonAbstract.a(world, blockposition, enumdirection);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    public AxisAlignedBB a(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return BlockLever.k;
    }

    @SuppressWarnings("deprecation")
    public boolean b(IBlockData iblockdata) {
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean c(IBlockData iblockdata) {
        return false;
    }

    public boolean canPlace(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        return a(world, blockposition, enumdirection.opposite());
    }

    public boolean canPlace(World world, BlockPosition blockposition) {
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        for (EnumDirection enumdirection : aenumdirection) {
            if (a(world, blockposition, enumdirection)) {
                return true;
            }
        }

        return false;
    }

    public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving) {
        IBlockData iblockdata = this.getBlockData().set(BlockLever.POWERED, Boolean.FALSE);

        if (a(world, blockposition, enumdirection.opposite())) {
            return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.a(enumdirection, entityliving.getDirection()));
        } else {
            Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

            EnumDirection enumdirection1;

            do {
                if (!iterator.hasNext()) {
                    if (world.getType(blockposition.down()).q()) {
                        return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.a(EnumDirection.UP, entityliving.getDirection()));
                    }

                    return iblockdata;
                }

                enumdirection1 = (EnumDirection) iterator.next();
            } while (enumdirection1 == enumdirection || !a(world, blockposition, enumdirection1.opposite()));

            return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.a(enumdirection1, entityliving.getDirection()));
        }
    }

    @SuppressWarnings("deprecation")
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Block block) {
        if (this.e(world, blockposition, iblockdata) && !a(world, blockposition, iblockdata.get(BlockLever.FACING).c().opposite())) {
            this.b(world, blockposition, iblockdata, 0);
            world.setAir(blockposition);
        }

    }

    private boolean e(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (this.canPlace(world, blockposition)) {
            return true;
        } else {
            this.b(world, blockposition, iblockdata, 0);
            world.setAir(blockposition);
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public AxisAlignedBB a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        switch (BlockLever.SyntheticClass_1.b[iblockdata.get(BlockLever.FACING).ordinal()]) {
        case 1:
        default:
            return BlockLever.f;

        case 2:
            return BlockLever.e;

        case 3:
            return BlockLever.d;

        case 4:
            return BlockLever.c;

        case 5:
        case 6:
            return BlockLever.g;

        case 7:
        case 8:
            return BlockLever.B;
        }
    }

    public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumHand enumhand, @Nullable ItemStack itemstack, EnumDirection enumdirection, float f, float f1, float f2) {
        if (world.isClientSide) {
            return true;
        } else {
            // CraftBukkit start - Interact Lever
            boolean powered = iblockdata.get(POWERED);
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) == powered) {
                return true;
            }
            // CraftBukkit end

            iblockdata = iblockdata.a(BlockLever.POWERED);
            world.setTypeAndData(blockposition, iblockdata, 3);
            float f3 = iblockdata.get(BlockLever.POWERED) ? 0.6F : 0.5F;

            world.a(null, blockposition, SoundEffects.dl, SoundCategory.BLOCKS, 0.3F, f3);
            world.applyPhysics(blockposition, this);
            EnumDirection enumdirection1 = iblockdata.get(BlockLever.FACING).c();

            world.applyPhysics(blockposition.shift(enumdirection1.opposite()), this);
            return true;
        }
    }

    public void remove(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (iblockdata.get(BlockLever.POWERED)) {
            world.applyPhysics(blockposition, this);
            EnumDirection enumdirection = iblockdata.get(BlockLever.FACING).c();

            world.applyPhysics(blockposition.shift(enumdirection.opposite()), this);
        }

        super.remove(world, blockposition, iblockdata);
    }

    @SuppressWarnings("deprecation")
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return iblockdata.get(BlockLever.POWERED) ? 15 : 0;
    }

    @SuppressWarnings("deprecation")
    public int c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return !iblockdata.get(BlockLever.POWERED) ? 0 : (iblockdata.get(BlockLever.FACING).c() == enumdirection ? 15 : 0);
    }

    @SuppressWarnings("deprecation")
    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    @SuppressWarnings("deprecation")
    public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockLever.FACING, BlockLever.EnumLeverPosition.a(i & 7)).set(BlockLever.POWERED, (i & 8) > 0);
    }

    public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | iblockdata.get(BlockLever.FACING).a();

        if (iblockdata.get(BlockLever.POWERED)) {
            i |= 8;
        }

        return i;
    }

    @SuppressWarnings("deprecation")
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        switch (BlockLever.SyntheticClass_1.c[enumblockrotation.ordinal()]) {
        case 1:
            switch (BlockLever.SyntheticClass_1.b[iblockdata.get(BlockLever.FACING).ordinal()]) {
            case 1:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.WEST);

            case 2:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.EAST);

            case 3:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.NORTH);

            case 4:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.SOUTH);

            default:
                return iblockdata;
            }

        case 2:
            switch (BlockLever.SyntheticClass_1.b[iblockdata.get(BlockLever.FACING).ordinal()]) {
            case 1:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.NORTH);

            case 2:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.SOUTH);

            case 3:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.EAST);

            case 4:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.WEST);

            case 5:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.UP_X);

            case 6:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.UP_Z);

            case 7:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.DOWN_Z);

            case 8:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.DOWN_X);
            }

        case 3:
            switch (BlockLever.SyntheticClass_1.b[iblockdata.get(BlockLever.FACING).ordinal()]) {
            case 1:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.SOUTH);

            case 2:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.NORTH);

            case 3:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.WEST);

            case 4:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.EAST);

            case 5:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.UP_X);

            case 6:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.UP_Z);

            case 7:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.DOWN_Z);

            case 8:
                return iblockdata.set(BlockLever.FACING, BlockLever.EnumLeverPosition.DOWN_X);
            }

        default:
            return iblockdata;
        }
    }

    @SuppressWarnings("deprecation")
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a(iblockdata.get(BlockLever.FACING).c()));
    }

    protected BlockStateList getStateList() {
        return new BlockStateList(this, BlockLever.FACING, BlockLever.POWERED);
    }

    public enum EnumLeverPosition implements INamable {

        DOWN_X(0, "down_x", EnumDirection.DOWN), EAST(1, "east", EnumDirection.EAST), WEST(2, "west", EnumDirection.WEST), SOUTH(3, "south", EnumDirection.SOUTH), NORTH(4, "north", EnumDirection.NORTH), UP_Z(5, "up_z", EnumDirection.UP), UP_X(6, "up_x", EnumDirection.UP), DOWN_Z(7, "down_z", EnumDirection.DOWN);

        private static final BlockLever.EnumLeverPosition[] i = new BlockLever.EnumLeverPosition[values().length];

        static {
            BlockLever.EnumLeverPosition[] ablocklever_enumleverposition = values();
            int i = ablocklever_enumleverposition.length;

            for (EnumLeverPosition blocklever_enumleverposition : ablocklever_enumleverposition) {
                EnumLeverPosition.i[blocklever_enumleverposition.a()] = blocklever_enumleverposition;
            }

        }

        private final int j;
        private final String k;
        private final EnumDirection l;

        EnumLeverPosition(int i, String s, EnumDirection enumdirection) {
            this.j = i;
            this.k = s;
            this.l = enumdirection;
        }

        public static BlockLever.EnumLeverPosition a(int i) {
            if (i < 0 || i >= BlockLever.EnumLeverPosition.i.length) {
                i = 0;
            }

            return BlockLever.EnumLeverPosition.i[i];
        }

        public static BlockLever.EnumLeverPosition a(EnumDirection enumdirection, EnumDirection enumdirection1) {
            switch (BlockLever.SyntheticClass_1.a[enumdirection.ordinal()]) {
            case 1:
                switch (BlockLever.SyntheticClass_1.d[enumdirection1.k().ordinal()]) {
                case 1:
                    return BlockLever.EnumLeverPosition.DOWN_X;

                case 2:
                    return BlockLever.EnumLeverPosition.DOWN_Z;

                default:
                    throw new IllegalArgumentException("Invalid entityFacing " + enumdirection1 + " for facing " + enumdirection);
                }

            case 2:
                switch (BlockLever.SyntheticClass_1.d[enumdirection1.k().ordinal()]) {
                case 1:
                    return BlockLever.EnumLeverPosition.UP_X;

                case 2:
                    return BlockLever.EnumLeverPosition.UP_Z;

                default:
                    throw new IllegalArgumentException("Invalid entityFacing " + enumdirection1 + " for facing " + enumdirection);
                }

            case 3:
                return BlockLever.EnumLeverPosition.NORTH;

            case 4:
                return BlockLever.EnumLeverPosition.SOUTH;

            case 5:
                return BlockLever.EnumLeverPosition.WEST;

            case 6:
                return BlockLever.EnumLeverPosition.EAST;

            default:
                throw new IllegalArgumentException("Invalid facing: " + enumdirection);
            }
        }

        public int a() {
            return this.j;
        }

        public EnumDirection c() {
            return this.l;
        }

        public String toString() {
            return this.k;
        }

        public String getName() {
            return this.k;
        }
    }

    static class SyntheticClass_1 {

        static final int[] a;
        static final int[] b;
        static final int[] c;
        static final int[] d = new int[EnumDirection.EnumAxis.values().length];

        static {
            try {
                BlockLever.SyntheticClass_1.d[EnumDirection.EnumAxis.X.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.d[EnumDirection.EnumAxis.Z.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            c = new int[EnumBlockRotation.values().length];

            try {
                BlockLever.SyntheticClass_1.c[EnumBlockRotation.CLOCKWISE_180.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.c[EnumBlockRotation.COUNTERCLOCKWISE_90.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.c[EnumBlockRotation.CLOCKWISE_90.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            b = new int[BlockLever.EnumLeverPosition.values().length];

            try {
                BlockLever.SyntheticClass_1.b[BlockLever.EnumLeverPosition.EAST.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.b[BlockLever.EnumLeverPosition.WEST.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.b[BlockLever.EnumLeverPosition.SOUTH.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.b[BlockLever.EnumLeverPosition.NORTH.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.b[BlockLever.EnumLeverPosition.UP_Z.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.b[BlockLever.EnumLeverPosition.UP_X.ordinal()] = 6;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.b[BlockLever.EnumLeverPosition.DOWN_X.ordinal()] = 7;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.b[BlockLever.EnumLeverPosition.DOWN_Z.ordinal()] = 8;
            } catch (NoSuchFieldError ignored) {
            }

            a = new int[EnumDirection.values().length];

            try {
                BlockLever.SyntheticClass_1.a[EnumDirection.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.a[EnumDirection.UP.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.a[EnumDirection.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.a[EnumDirection.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.a[EnumDirection.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockLever.SyntheticClass_1.a[EnumDirection.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError ignored) {
            }

        }
    }
}
