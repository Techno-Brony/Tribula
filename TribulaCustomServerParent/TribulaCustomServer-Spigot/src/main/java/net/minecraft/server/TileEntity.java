package net.minecraft.server;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.inventory.InventoryHolder;
import org.spigotmc.CustomTimingsHandler;

import javax.annotation.Nullable;
import java.util.Map;

public abstract class TileEntity {

    private static final Logger a = LogManager.getLogger();
    private static final Map<String, Class<? extends TileEntity>> f = Maps.newHashMap();
    private static final Map<Class<? extends TileEntity>, String> g = Maps.newHashMap();

    static {
        a(TileEntityFurnace.class, "Furnace");
        a(TileEntityChest.class, "Chest");
        a(TileEntityEnderChest.class, "EnderChest");
        a(BlockJukeBox.TileEntityRecordPlayer.class, "RecordPlayer");
        a(TileEntityDispenser.class, "Trap");
        a(TileEntityDropper.class, "Dropper");
        a(TileEntitySign.class, "Sign");
        a(TileEntityMobSpawner.class, "MobSpawner");
        a(TileEntityNote.class, "Music");
        a(TileEntityPiston.class, "Piston");
        a(TileEntityBrewingStand.class, "Cauldron");
        a(TileEntityEnchantTable.class, "EnchantTable");
        a(TileEntityEnderPortal.class, "Airportal");
        a(TileEntityBeacon.class, "Beacon");
        a(TileEntitySkull.class, "Skull");
        a(TileEntityLightDetector.class, "DLDetector");
        a(TileEntityHopper.class, "Hopper");
        a(TileEntityComparator.class, "Comparator");
        a(TileEntityFlowerPot.class, "FlowerPot");
        a(TileEntityBanner.class, "Banner");
        a(TileEntityStructure.class, "Structure");
        a(TileEntityEndGateway.class, "EndGateway");
        a(TileEntityCommand.class, "Control");
    }

    @SuppressWarnings("CanBeFinal")
    public CustomTimingsHandler tickTimer = org.bukkit.craftbukkit.SpigotTimings.getTileEntityTimings(this); // Spigot
    protected World world;
    protected BlockPosition position;
    protected boolean d;
    protected Block e;
    private int h;

    public TileEntity() {
        this.position = BlockPosition.ZERO;
        this.h = -1;
    }

    private static void a(Class<? extends TileEntity> oclass, String s) {
        if (TileEntity.f.containsKey(s)) {
            throw new IllegalArgumentException("Duplicate id: " + s);
        } else {
            TileEntity.f.put(s, oclass);
            TileEntity.g.put(oclass, s);
        }
    }

    public static TileEntity a(World world, NBTTagCompound nbttagcompound) {
        TileEntity tileentity = null;
        String s = nbttagcompound.getString("id");

        try {
            Class oclass = (Class) TileEntity.f.get(s);

            if (oclass != null) {
                tileentity = (TileEntity) oclass.newInstance();
            }
        } catch (Throwable throwable) {
            TileEntity.a.error("Failed to create block entity {}", s, throwable);
        }

        if (tileentity != null) {
            try {
                tileentity.b(world);
                tileentity.a(nbttagcompound);
            } catch (Throwable throwable1) {
                TileEntity.a.error("Failed to load data for block entity {}", s, throwable1);
                tileentity = null;
            }
        } else {
            TileEntity.a.warn("Skipping BlockEntity with id {}", s);
        }

        return tileentity;
    }

    public World getWorld() {
        return this.world;
    }

    public void a(World world) {
        this.world = world;
    }

    public boolean t() {
        return this.world != null;
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.position = new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z"));
    }

    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        return this.c(nbttagcompound);
    }

    private NBTTagCompound c(NBTTagCompound nbttagcompound) {
        String s = TileEntity.g.get(this.getClass());

        if (s == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            nbttagcompound.setString("id", s);
            nbttagcompound.setInt("x", this.position.getX());
            nbttagcompound.setInt("y", this.position.getY());
            nbttagcompound.setInt("z", this.position.getZ());
            return nbttagcompound;
        }
    }

    protected void b(World world) {}

    public int u() {
        if (this.h == -1) {
            IBlockData iblockdata = this.world.getType(this.position);

            this.h = iblockdata.getBlock().toLegacyData(iblockdata);
        }

        return this.h;
    }

    public void update() {
        if (this.world != null) {
            IBlockData iblockdata = this.world.getType(this.position);

            this.h = iblockdata.getBlock().toLegacyData(iblockdata);
            this.world.b(this.position, this);
            if (this.getBlock() != Blocks.AIR) {
                this.world.updateAdjacentComparators(this.position, this.getBlock());
            }
        }

    }

    public BlockPosition getPosition() {
        return this.position;
    }

    public void setPosition(BlockPosition blockposition) {
        if (blockposition instanceof BlockPosition.MutableBlockPosition || blockposition instanceof BlockPosition.PooledBlockPosition) {
            TileEntity.a.warn("Tried to assign a mutable BlockPos to a block entity...", new Error(blockposition.getClass().toString()));
            blockposition = new BlockPosition(blockposition);
        }

        this.position = blockposition;
    }

    public Block getBlock() {
        if (this.e == null && this.world != null) {
            this.e = this.world.getType(this.position).getBlock();
        }

        return this.e;
    }

    @Nullable
    public PacketPlayOutTileEntityData getUpdatePacket() {
        return null;
    }

    @SuppressWarnings("unused")
    public NBTTagCompound c() {
        return this.c(new NBTTagCompound());
    }

    public boolean x() {
        return this.d;
    }

    public void y() {
        this.d = true;
    }

    public void z() {
        this.d = false;
    }

    @SuppressWarnings("unused")
    public boolean c(int i, int j) {
        return false;
    }

    public void invalidateBlockCache() {
        this.e = null;
        this.h = -1;
    }

    public void a(CrashReportSystemDetails crashreportsystemdetails) {
        //noinspection unchecked
        crashreportsystemdetails.a("Name", new CrashReportCallable() {
            public String a() {
                return TileEntity.g.get(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        if (this.world != null) {
            CrashReportSystemDetails.a(crashreportsystemdetails, this.position, this.getBlock(), this.u());
            //noinspection unchecked
            crashreportsystemdetails.a("Actual block type", new CrashReportCallable() {
                public String a() {
                    int i = Block.getId(TileEntity.this.world.getType(TileEntity.this.position).getBlock());

                    try {
                        return String.format("ID #%d (%s // %s)", i, Block.getById(i).a(), Block.getById(i).getClass().getCanonicalName());
                    } catch (Throwable throwable) {
                        return "ID #" + i;
                    }
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
            //noinspection unchecked
            crashreportsystemdetails.a("Actual block data value", new CrashReportCallable() {
                public String a() {
                    IBlockData iblockdata = TileEntity.this.world.getType(TileEntity.this.position);
                    int i = iblockdata.getBlock().toLegacyData(iblockdata);

                    if (i < 0) {
                        return "Unknown? (Got " + i + ")";
                    } else {
                        String s = String.format("%4s", new Object[] { Integer.toBinaryString(i)}).replace(" ", "0");

                        return String.format("%1$d / 0x%1$X / 0b%2$s", i, s);
                    }
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
        }
    }

    public boolean isFilteredNBT() {
        return false;
    }

    @SuppressWarnings({"unused", "SameReturnValue"})
    @Nullable
    public IChatBaseComponent i_() {
        return null;
    }

    @SuppressWarnings("unused")
    public void a(EnumBlockRotation enumblockrotation) {}

    @SuppressWarnings("unused")
    public void a(EnumBlockMirror enumblockmirror) {}

    // CraftBukkit start - add method
    public InventoryHolder getOwner() {
        if (world == null) return null;
        // Spigot start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
        if (block == null) {
            org.bukkit.Bukkit.getLogger().log(java.util.logging.Level.WARNING, "No block for owner at %s %d %d %d", new Object[]{world.getWorld(), position.getX(), position.getY(), position.getZ()});
            return null;
        }
        // Spigot end
        org.bukkit.block.BlockState state = block.getState();
        if (state instanceof InventoryHolder) return (InventoryHolder) state;
        return null;
    }
    // CraftBukkit end
}
