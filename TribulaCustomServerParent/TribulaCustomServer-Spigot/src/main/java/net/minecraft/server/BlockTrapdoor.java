package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent;

import javax.annotation.Nullable;

public class BlockTrapdoor extends Block {

    public static final BlockStateDirection FACING = BlockFacingHorizontal.FACING;
    public static final BlockStateBoolean OPEN = BlockStateBoolean.of("open");
    public static final BlockStateEnum<BlockTrapdoor.EnumTrapdoorHalf> HALF = BlockStateEnum.of("half", BlockTrapdoor.EnumTrapdoorHalf.class);
    protected static final AxisAlignedBB d = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB e = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB f = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB g = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB B = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
    protected static final AxisAlignedBB C = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);

    protected BlockTrapdoor(Material material) {
        super(material);
        this.w(this.blockStateList.getBlockData().set(BlockTrapdoor.FACING, EnumDirection.NORTH).set(BlockTrapdoor.OPEN, Boolean.FALSE).set(BlockTrapdoor.HALF, BlockTrapdoor.EnumTrapdoorHalf.BOTTOM));
        this.a(CreativeModeTab.d);
    }

    protected static EnumDirection e(int i) {
        switch (i & 3) {
        case 0:
            return EnumDirection.NORTH;

        case 1:
            return EnumDirection.SOUTH;

        case 2:
            return EnumDirection.WEST;

        case 3:
        default:
            return EnumDirection.EAST;
        }
    }

    protected static int a(EnumDirection enumdirection) {
        switch (BlockTrapdoor.SyntheticClass_1.a[enumdirection.ordinal()]) {
        case 1:
            return 0;

        case 2:
            return 1;

        case 3:
            return 2;

        case 4:
        default:
            return 3;
        }
    }

    @SuppressWarnings("deprecation")
    public AxisAlignedBB a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        AxisAlignedBB axisalignedbb;

        if (iblockdata.get(BlockTrapdoor.OPEN)) {
            switch (BlockTrapdoor.SyntheticClass_1.a[iblockdata.get(BlockTrapdoor.FACING).ordinal()]) {
            case 1:
            default:
                axisalignedbb = BlockTrapdoor.g;
                break;

            case 2:
                axisalignedbb = BlockTrapdoor.f;
                break;

            case 3:
                axisalignedbb = BlockTrapdoor.e;
                break;

            case 4:
                axisalignedbb = BlockTrapdoor.d;
            }
        } else if (iblockdata.get(BlockTrapdoor.HALF) == BlockTrapdoor.EnumTrapdoorHalf.TOP) {
            axisalignedbb = BlockTrapdoor.C;
        } else {
            axisalignedbb = BlockTrapdoor.B;
        }

        return axisalignedbb;
    }

    @SuppressWarnings("deprecation")
    public boolean b(IBlockData iblockdata) {
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean c(IBlockData iblockdata) {
        return false;
    }

    public boolean b(IBlockAccess iblockaccess, BlockPosition blockposition) {
        return !iblockaccess.getType(blockposition).get(BlockTrapdoor.OPEN);
    }

    public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumHand enumhand, @Nullable ItemStack itemstack, EnumDirection enumdirection, float f, float f1, float f2) {
        if (this.material == Material.ORE) {
            return true;
        } else {
            iblockdata = iblockdata.a(BlockTrapdoor.OPEN);
            world.setTypeAndData(blockposition, iblockdata, 2);
            this.a(entityhuman, world, blockposition, iblockdata.get(BlockTrapdoor.OPEN));
            return true;
        }
    }

    protected void a(@Nullable EntityHuman entityhuman, World world, BlockPosition blockposition, boolean flag) {
        int i;

        if (flag) {
            i = this.material == Material.ORE ? 1037 : 1007;
            world.a(entityhuman, i, blockposition, 0);
        } else {
            i = this.material == Material.ORE ? 1036 : 1013;
            world.a(entityhuman, i, blockposition, 0);
        }

    }

    @SuppressWarnings("deprecation")
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Block block) {
        if (!world.isClientSide) {
            boolean flag = world.isBlockIndirectlyPowered(blockposition);

            if (flag || block.getBlockData().m()) {
                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.Block bblock = bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

                int power = bblock.getBlockPower();
                int oldPower = iblockdata.get(OPEN) ? 15 : 0;

                if (oldPower == 0 ^ power == 0 || block.getBlockData().n()) {
                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bblock, oldPower, power);
                    world.getServer().getPluginManager().callEvent(eventRedstone);
                    flag = eventRedstone.getNewCurrent() > 0;
                }
                // CraftBukkit end
                boolean flag1 = iblockdata.get(BlockTrapdoor.OPEN);

                if (flag1 != flag) {
                    world.setTypeAndData(blockposition, iblockdata.set(BlockTrapdoor.OPEN, flag), 2);
                    this.a(null, world, blockposition, flag);
                }
            }

        }
    }

    public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving) {
        IBlockData iblockdata = this.getBlockData();

        if (enumdirection.k().c()) {
            iblockdata = iblockdata.set(BlockTrapdoor.FACING, enumdirection).set(BlockTrapdoor.OPEN, Boolean.FALSE);
            iblockdata = iblockdata.set(BlockTrapdoor.HALF, f1 > 0.5F ? BlockTrapdoor.EnumTrapdoorHalf.TOP : BlockTrapdoor.EnumTrapdoorHalf.BOTTOM);
        } else {
            iblockdata = iblockdata.set(BlockTrapdoor.FACING, entityliving.getDirection().opposite()).set(BlockTrapdoor.OPEN, Boolean.FALSE);
            iblockdata = iblockdata.set(BlockTrapdoor.HALF, enumdirection == EnumDirection.UP ? BlockTrapdoor.EnumTrapdoorHalf.BOTTOM : BlockTrapdoor.EnumTrapdoorHalf.TOP);
        }

        return iblockdata;
    }

    public boolean canPlace(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        return true;
    }

    @SuppressWarnings("deprecation")
    public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockTrapdoor.FACING, e(i)).set(BlockTrapdoor.OPEN, (i & 4) != 0).set(BlockTrapdoor.HALF, (i & 8) == 0 ? BlockTrapdoor.EnumTrapdoorHalf.BOTTOM : BlockTrapdoor.EnumTrapdoorHalf.TOP);
    }

    public int toLegacyData(IBlockData iblockdata) {
        byte b0 = 0;
        int i = b0 | a(iblockdata.get(BlockTrapdoor.FACING));

        if (iblockdata.get(BlockTrapdoor.OPEN)) {
            i |= 4;
        }

        if (iblockdata.get(BlockTrapdoor.HALF) == BlockTrapdoor.EnumTrapdoorHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    @SuppressWarnings("deprecation")
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return iblockdata.set(BlockTrapdoor.FACING, enumblockrotation.a(iblockdata.get(BlockTrapdoor.FACING)));
    }

    @SuppressWarnings("deprecation")
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a(iblockdata.get(BlockTrapdoor.FACING)));
    }

    protected BlockStateList getStateList() {
        return new BlockStateList(this, BlockTrapdoor.FACING, BlockTrapdoor.OPEN, BlockTrapdoor.HALF);
    }

    public enum EnumTrapdoorHalf implements INamable {

        TOP("top"), BOTTOM("bottom");

        private final String c;

        EnumTrapdoorHalf(String s) {
            this.c = s;
        }

        public String toString() {
            return this.c;
        }

        public String getName() {
            return this.c;
        }
    }

    static class SyntheticClass_1 {

        static final int[] a = new int[EnumDirection.values().length];

        static {
            try {
                BlockTrapdoor.SyntheticClass_1.a[EnumDirection.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockTrapdoor.SyntheticClass_1.a[EnumDirection.SOUTH.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockTrapdoor.SyntheticClass_1.a[EnumDirection.WEST.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                BlockTrapdoor.SyntheticClass_1.a[EnumDirection.EAST.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }

        }
    }
}
