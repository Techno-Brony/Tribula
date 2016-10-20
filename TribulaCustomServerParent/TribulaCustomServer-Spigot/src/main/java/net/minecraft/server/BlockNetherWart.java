package net.minecraft.server;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockNetherWart extends BlockPlant {

    public static final BlockStateInteger AGE = BlockStateInteger.of("age", 0, 3);
    private static final AxisAlignedBB[] c = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D)};

    protected BlockNetherWart() {
        super(Material.PLANT, MaterialMapColor.D);
        this.w(this.blockStateList.getBlockData().set(BlockNetherWart.AGE, 0));
        this.a(true);
        this.a((CreativeModeTab) null);
    }

    public AxisAlignedBB a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return BlockNetherWart.c[iblockdata.get(BlockNetherWart.AGE)];
    }

    protected boolean i(IBlockData iblockdata) {
        return iblockdata.getBlock() == Blocks.SOUL_SAND;
    }

    public boolean f(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return this.i(world.getType(blockposition.down()));
    }

    public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        int i = iblockdata.get(BlockNetherWart.AGE);

        if (i < 3 && random.nextInt(Math.max(1, (int) (100.0F / world.spigotConfig.wartModifier) * 10)) == 0) { // Spigot
            iblockdata = iblockdata.set(BlockNetherWart.AGE, i + 1);
            // world.setTypeAndData(blockposition, iblockdata, 2); // CraftBukkit
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this, toLegacyData(iblockdata)); // CraftBukkit
        }

        super.b(world, blockposition, iblockdata, random);
    }

    public void dropNaturally(World world, BlockPosition blockposition, IBlockData iblockdata, float f, int i) {
        if (!world.isClientSide) {
            int j = 1;

            if (iblockdata.get(BlockNetherWart.AGE) >= 3) {
                j = 2 + world.random.nextInt(3);
                if (i > 0) {
                    j += world.random.nextInt(i + 1);
                }
            }

            for (int k = 0; k < j; ++k) {
                a(world, blockposition, new ItemStack(Items.NETHER_WART));
            }

        }
    }

    @Nullable
    public Item getDropType(IBlockData iblockdata, Random random, int i) {
        return null;
    }

    public int a(Random random) {
        return 0;
    }

    public ItemStack a(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return new ItemStack(Items.NETHER_WART);
    }

    @SuppressWarnings("deprecation")
    public IBlockData fromLegacyData(int i) {
        return this.getBlockData().set(BlockNetherWart.AGE, i);
    }

    public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockNetherWart.AGE);
    }

    protected BlockStateList getStateList() {
        return new BlockStateList(this, BlockNetherWart.AGE);
    }
}
