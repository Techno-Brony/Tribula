package net.minecraft.server;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// CraftBukkit start
// CraftBukkit end

public class EntityBoat extends Entity {

    private static final DataWatcherObject<Integer> a = DataWatcher.a(EntityBoat.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> b = DataWatcher.a(EntityBoat.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Float> c = DataWatcher.a(EntityBoat.class, DataWatcherRegistry.c);
    private static final DataWatcherObject<Integer> d = DataWatcher.a(EntityBoat.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Boolean>[] e = new DataWatcherObject[] { DataWatcher.a(EntityBoat.class, DataWatcherRegistry.h), DataWatcher.a(EntityBoat.class, DataWatcherRegistry.h)};
    private final float[] f;
    // CraftBukkit start
    // PAIL: Some of these haven't worked since a few updates, and since 1.9 they are less and less applicable.
    public double maxSpeed = 0.4D;
    public double occupiedDeceleration = 0.2D;
    public double unoccupiedDeceleration = -1;
    public boolean landBoats = false;
    private float g;
    private float h;
    private float au;
    private int av;
    private double aw;
    private double ax;
    private double ay;
    private double az;
    private double aA;
    private boolean aB;
    private boolean aC;
    private boolean aD;
    private boolean aE;
    private double aF;
    private float aG;
    private EntityBoat.EnumStatus aH;
    private EntityBoat.EnumStatus aI;
    private double aJ;
    // CraftBukkit end
    private Location lastLocation; // CraftBukkit

    public EntityBoat(World world) {
        super(world);
        this.f = new float[2];
        this.i = true;
        this.setSize(1.375F, 0.5625F);
    }

    public EntityBoat(World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
        this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleCreateEvent((Vehicle) this.getBukkitEntity())); // CraftBukkit
    }

    public static float a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        int i = iblockdata.get(BlockFluids.LEVEL);

        return (i & 7) == 0 && iblockaccess.getType(blockposition.up()).getMaterial() == Material.WATER ? 1.0F : 1.0F - BlockFluids.e(i);
    }

    public static float b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return (float) blockposition.getY() + a(iblockdata, iblockaccess, blockposition);
    }

    protected boolean playStepSound() {
        return false;
    }

    protected void i() {
        this.datawatcher.register(EntityBoat.a, 0);
        this.datawatcher.register(EntityBoat.b, 1);
        this.datawatcher.register(EntityBoat.c, 0.0F);
        this.datawatcher.register(EntityBoat.d, EnumBoatType.OAK.ordinal());
        DataWatcherObject[] adatawatcherobject = EntityBoat.e;
        int i = adatawatcherobject.length;

        for (DataWatcherObject datawatcherobject : adatawatcherobject) {
            this.datawatcher.register(datawatcherobject, Boolean.FALSE);
        }

    }

    @Nullable
    public AxisAlignedBB j(Entity entity) {
        return entity.getBoundingBox();
    }

    @Nullable
    public AxisAlignedBB ag() {
        return this.getBoundingBox();
    }

    public boolean isCollidable() {
        return true;
    }

    public double ay() {
        return -0.1D;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else if (!this.world.isClientSide && !this.dead) {
            if (damagesource instanceof EntityDamageSourceIndirect && damagesource.getEntity() != null && this.w(damagesource.getEntity())) {
                return false;
            } else {
                // CraftBukkit start
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity attacker = (damagesource.getEntity() == null) ? null : damagesource.getEntity().getBukkitEntity();

                VehicleDamageEvent event = new VehicleDamageEvent(vehicle, attacker, (double) f);
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }
                // f = event.getDamage(); // TODO Why don't we do this?
                // CraftBukkit end

                this.d(-this.q());
                this.b(10);
                this.setDamage(this.n() + f * 10.0F);
                this.ap();
                boolean flag = damagesource.getEntity() instanceof EntityHuman && ((EntityHuman) damagesource.getEntity()).abilities.canInstantlyBuild;

                if (flag || this.n() > 40.0F) {
                    // CraftBukkit start
                    VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, attacker);
                    this.world.getServer().getPluginManager().callEvent(destroyEvent);

                    if (destroyEvent.isCancelled()) {
                        this.setDamage(40F); // Maximize damage so this doesn't get triggered again right away
                        return true;
                    }
                    // CraftBukkit end
                    if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
                        this.a(this.j(), 1, 0.0F);
                    }

                    this.die();
                }

                return true;
            }
        } else {
            return true;
        }
    }

    public void collide(Entity entity) {
        if (entity instanceof EntityBoat) {
            if (entity.getBoundingBox().b < this.getBoundingBox().e) {
                // CraftBukkit start
                VehicleEntityCollisionEvent event = new VehicleEntityCollisionEvent((Vehicle) this.getBukkitEntity(), entity.getBukkitEntity());
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
                // CraftBukkit end
                super.collide(entity);
            }
        } else if (entity.getBoundingBox().b <= this.getBoundingBox().b) {
            // CraftBukkit start
            VehicleEntityCollisionEvent event = new VehicleEntityCollisionEvent((Vehicle) this.getBukkitEntity(), entity.getBukkitEntity());
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end
            super.collide(entity);
        }

    }

    public Item j() {
        switch (EntityBoat.SyntheticClass_1.a[this.getType().ordinal()]) {
        case 1:
        default:
            return Items.aG;

        case 2:
            return Items.aH;

        case 3:
            return Items.aI;

        case 4:
            return Items.aJ;

        case 5:
            return Items.aK;

        case 6:
            return Items.aL;
        }
    }

    public boolean isInteractable() {
        return !this.dead;
    }

    public EnumDirection bm() {
        return this.getDirection().e();
    }

    public void m() {
        this.aI = this.aH;
        this.aH = this.t();
        if (this.aH != EntityBoat.EnumStatus.UNDER_WATER && this.aH != EntityBoat.EnumStatus.UNDER_FLOWING_WATER) {
            this.h = 0.0F;
        } else {
            ++this.h;
        }

        if (!this.world.isClientSide && this.h >= 60.0F) {
            this.az();
        }

        if (this.o() > 0) {
            this.b(this.o() - 1);
        }

        if (this.n() > 0.0F) {
            this.setDamage(this.n() - 1.0F);
        }
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        super.m();
        this.s();
        if (this.bA()) {
            if (this.bx().size() == 0 || !(this.bx().get(0) instanceof EntityHuman)) {
                this.a(false, false);
            }

            this.w();
            if (this.world.isClientSide) {
                this.x();
                this.world.a(new PacketPlayInBoatMove(this.a(0), this.a(1)));
            }

            this.move(this.motX, this.motY, this.motZ);
        } else {
            this.motX = 0.0D;
            this.motY = 0.0D;
            this.motZ = 0.0D;
        }

        // CraftBukkit start
        org.bukkit.Server server = this.world.getServer();
        org.bukkit.World bworld = this.world.getWorld();

        Location to = new Location(bworld, this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        Vehicle vehicle = (Vehicle) this.getBukkitEntity();

        server.getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));

        if (lastLocation != null && !lastLocation.equals(to)) {
            VehicleMoveEvent event = new VehicleMoveEvent(vehicle, lastLocation, to);
            server.getPluginManager().callEvent(event);
        }
        lastLocation = vehicle.getLocation();
        // CraftBukkit end

        for (int i = 0; i <= 1; ++i) {
            if (this.a(i)) {
                this.f[i] = (float) ((double) this.f[i] + 0.01D);
            } else {
                this.f[i] = 0.0F;
            }
        }

        this.checkBlockCollisions();
        List list = this.world.getEntities(this, this.getBoundingBox().grow(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), IEntitySelector.a(this));

        if (!list.isEmpty()) {
            boolean flag = !this.world.isClientSide && !(this.bw() instanceof EntityHuman);

            for (Object aList : list) {
                Entity entity = (Entity) aList;

                if (!entity.w(this)) {
                    if (flag && this.bx().size() < 2 && !entity.isPassenger() && entity.width < this.width && entity instanceof EntityLiving && !(entity instanceof EntityWaterAnimal) && !(entity instanceof EntityHuman)) {
                        entity.startRiding(this);
                    } else {
                        this.collide(entity);
                    }
                }
            }
        }

    }

    private void s() {
        if (this.av > 0 && !this.bA()) {
            double d0 = this.locX + (this.aw - this.locX) / (double) this.av;
            double d1 = this.locY + (this.ax - this.locY) / (double) this.av;
            double d2 = this.locZ + (this.ay - this.locZ) / (double) this.av;
            double d3 = MathHelper.g(this.az - (double) this.yaw);

            this.yaw = (float) ((double) this.yaw + d3 / (double) this.av);
            this.pitch = (float) ((double) this.pitch + (this.aA - (double) this.pitch) / (double) this.av);
            --this.av;
            this.setPosition(d0, d1, d2);
            this.setYawPitch(this.yaw, this.pitch);
        }
    }

    public void a(boolean flag, boolean flag1) {
        this.datawatcher.set(EntityBoat.e[0], flag);
        this.datawatcher.set(EntityBoat.e[1], flag1);
    }

    private EntityBoat.EnumStatus t() {
        EntityBoat.EnumStatus entityboat_enumstatus = this.v();

        if (entityboat_enumstatus != null) {
            this.aF = this.getBoundingBox().e;
            return entityboat_enumstatus;
        } else if (this.u()) {
            return EntityBoat.EnumStatus.IN_WATER;
        } else {
            float f = this.l();

            if (f > 0.0F) {
                this.aG = f;
                return EntityBoat.EnumStatus.ON_LAND;
            } else {
                return EntityBoat.EnumStatus.IN_AIR;
            }
        }
    }

    public float k() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.f(axisalignedbb.d);
        int k = MathHelper.floor(axisalignedbb.e);
        int l = MathHelper.f(axisalignedbb.e - this.aJ);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.f(axisalignedbb.f);
        BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();

        try {
            label108:
            for (int k1 = k; k1 < l; ++k1) {
                float f = 0.0F;

                for (int l1 = i; l1 < j; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockposition_pooledblockposition.f(l1, k1, i2);
                        IBlockData iblockdata = this.world.getType(blockposition_pooledblockposition);

                        if (iblockdata.getMaterial() == Material.WATER) {
                            f = Math.max(f, a(iblockdata, this.world, blockposition_pooledblockposition));
                        }

                        if (f >= 1.0F) {
                            continue label108;
                        }
                    }
                }

                if (f < 1.0F) {

                    return (float) blockposition_pooledblockposition.getY() + f;
                }
            }

            return (float) (l + 1);
        } finally {
            blockposition_pooledblockposition.t();
        }
    }

    public float l() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.a, axisalignedbb.b - 0.001D, axisalignedbb.c, axisalignedbb.d, axisalignedbb.b, axisalignedbb.f);
        int i = MathHelper.floor(axisalignedbb1.a) - 1;
        int j = MathHelper.f(axisalignedbb1.d) + 1;
        int k = MathHelper.floor(axisalignedbb1.b) - 1;
        int l = MathHelper.f(axisalignedbb1.e) + 1;
        int i1 = MathHelper.floor(axisalignedbb1.c) - 1;
        int j1 = MathHelper.f(axisalignedbb1.f) + 1;
        ArrayList arraylist = Lists.newArrayList();
        float f = 0.0F;
        int k1 = 0;
        BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();

        try {
            for (int l1 = i; l1 < j; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);

                    if (j2 != 2) {
                        for (int k2 = k; k2 < l; ++k2) {
                            if (j2 <= 0 || k2 != k && k2 != l - 1) {
                                blockposition_pooledblockposition.f(l1, k2, i2);
                                IBlockData iblockdata = this.world.getType(blockposition_pooledblockposition);

                                //noinspection unchecked
                                iblockdata.a(this.world, blockposition_pooledblockposition, axisalignedbb1, arraylist, this);
                                if (!arraylist.isEmpty()) {
                                    f += iblockdata.getBlock().frictionFactor;
                                    ++k1;
                                }

                                arraylist.clear();
                            }
                        }
                    }
                }
            }
        } finally {
            blockposition_pooledblockposition.t();
        }

        return f / (float) k1;
    }

    private boolean u() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.f(axisalignedbb.d);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.f(axisalignedbb.b + 0.001D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.f(axisalignedbb.f);
        boolean flag = false;

        this.aF = Double.MIN_VALUE;
        BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();

        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockposition_pooledblockposition.f(k1, l1, i2);
                        IBlockData iblockdata = this.world.getType(blockposition_pooledblockposition);

                        if (iblockdata.getMaterial() == Material.WATER) {
                            float f = b(iblockdata, this.world, blockposition_pooledblockposition);

                            this.aF = Math.max((double) f, this.aF);
                            flag |= axisalignedbb.b < (double) f;
                        }
                    }
                }
            }
        } finally {
            blockposition_pooledblockposition.t();
        }

        return flag;
    }

    @Nullable
    private EntityBoat.EnumStatus v() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        double d0 = axisalignedbb.e + 0.001D;
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.f(axisalignedbb.d);
        int k = MathHelper.floor(axisalignedbb.e);
        int l = MathHelper.f(d0);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.f(axisalignedbb.f);
        boolean flag = false;
        BlockPosition.PooledBlockPosition blockposition_pooledblockposition = BlockPosition.PooledBlockPosition.s();

        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockposition_pooledblockposition.f(k1, l1, i2);
                        IBlockData iblockdata = this.world.getType(blockposition_pooledblockposition);

                        if (iblockdata.getMaterial() == Material.WATER && d0 < (double) b(iblockdata, this.world, blockposition_pooledblockposition)) {
                            if (iblockdata.get(BlockFluids.LEVEL) != 0) {

                                return EnumStatus.UNDER_FLOWING_WATER;
                            }

                            flag = true;
                        }
                    }
                }
            }

            return flag ? EntityBoat.EnumStatus.UNDER_WATER : null;
        } finally {
            blockposition_pooledblockposition.t();
        }
    }

    private void w() {
        double d0 = -0.03999999910593033D;
        double d1 = this.isNoGravity() ? 0.0D : -0.03999999910593033D;
        double d2 = 0.0D;

        this.g = 0.05F;
        if (this.aI == EntityBoat.EnumStatus.IN_AIR && this.aH != EntityBoat.EnumStatus.IN_AIR && this.aH != EntityBoat.EnumStatus.ON_LAND) {
            this.aF = this.getBoundingBox().b + (double) this.length;
            this.setPosition(this.locX, (double) (this.k() - this.length) + 0.101D, this.locZ);
            this.motY = 0.0D;
            this.aJ = 0.0D;
            this.aH = EntityBoat.EnumStatus.IN_WATER;
        } else {
            if (this.aH == EntityBoat.EnumStatus.IN_WATER) {
                d2 = (this.aF - this.getBoundingBox().b) / (double) this.length;
                this.g = 0.9F;
            } else if (this.aH == EntityBoat.EnumStatus.UNDER_FLOWING_WATER) {
                d1 = -7.0E-4D;
                this.g = 0.9F;
            } else if (this.aH == EntityBoat.EnumStatus.UNDER_WATER) {
                d2 = 0.009999999776482582D;
                this.g = 0.45F;
            } else if (this.aH == EntityBoat.EnumStatus.IN_AIR) {
                this.g = 0.9F;
            } else if (this.aH == EntityBoat.EnumStatus.ON_LAND) {
                this.g = this.aG;
                if (this.bw() instanceof EntityHuman) {
                    this.aG /= 2.0F;
                }
            }

            this.motX *= (double) this.g;
            this.motZ *= (double) this.g;
            this.au *= this.g;
            this.motY += d1;
            if (d2 > 0.0D) {
                double d3 = 0.65D;

                this.motY += d2 * 0.06153846016296973D;
                double d4 = 0.75D;

                this.motY *= 0.75D;
            }
        }

    }

    private void x() {
        if (this.isVehicle()) {
            float f = 0.0F;

            if (this.aB) {
                this.au += -1.0F;
            }

            if (this.aC) {
                ++this.au;
            }

            if (this.aC != this.aB && !this.aD && !this.aE) {
                f += 0.005F;
            }

            this.yaw += this.au;
            if (this.aD) {
                f += 0.04F;
            }

            if (this.aE) {
                f -= 0.005F;
            }

            this.motX += (double) (MathHelper.sin(-this.yaw * 0.017453292F) * f);
            this.motZ += (double) (MathHelper.cos(this.yaw * 0.017453292F) * f);
            this.a(this.aC || this.aD, this.aB || this.aD);
        }
    }

    public void k(Entity entity) {
        if (this.w(entity)) {
            float f = 0.0F;
            float f1 = (float) ((this.dead ? 0.009999999776482582D : this.ay()) + entity.ax());

            if (this.bx().size() > 1) {
                int i = this.bx().indexOf(entity);

                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }

                if (entity instanceof EntityAnimal) {
                    f = (float) ((double) f + 0.2D);
                }
            }

            Vec3D vec3d = (new Vec3D((double) f, 0.0D, 0.0D)).b(-this.yaw * 0.017453292F - 1.5707964F);

            entity.setPosition(this.locX + vec3d.x, this.locY + (double) f1, this.locZ + vec3d.z);
            entity.yaw += this.au;
            entity.h(entity.getHeadRotation() + this.au);
            this.a(entity);
            if (entity instanceof EntityAnimal && this.bx().size() > 1) {
                int j = entity.getId() % 2 == 0 ? 90 : 270;

                entity.i(((EntityAnimal) entity).aO + (float) j);
                entity.h(entity.getHeadRotation() + (float) j);
            }

        }
    }

    protected void a(Entity entity) {
        entity.i(this.yaw);
        float f = MathHelper.g(entity.yaw - this.yaw);
        float f1 = MathHelper.a(f, -105.0F, 105.0F);

        entity.lastYaw += f1 - f;
        entity.yaw += f1 - f;
        entity.h(entity.yaw);
    }

    protected void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Type", this.getType().a());
    }

    protected void a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKeyOfType("Type", 8)) {
            this.setType(EntityBoat.EnumBoatType.a(nbttagcompound.getString("Type")));
        }

    }

    public boolean a(EntityHuman entityhuman, @Nullable ItemStack itemstack, EnumHand enumhand) {
        if (!this.world.isClientSide && !entityhuman.isSneaking() && this.h < 60.0F) {
            entityhuman.startRiding(this);
        }

        return false;
    }

    protected void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition) {
        this.aJ = this.motY;
        if (!this.isPassenger()) {
            if (flag) {
                if (this.fallDistance > 3.0F) {
                    if (this.aH != EntityBoat.EnumStatus.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.e(this.fallDistance, 1.0F);
                    if (!this.world.isClientSide && !this.dead) {
                    // CraftBukkit start
                    Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                    VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, null);
                    this.world.getServer().getPluginManager().callEvent(destroyEvent);
                    if (!destroyEvent.isCancelled()) {
                        this.die();
                        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
                            int i;

                            for (i = 0; i < 3; ++i) {
                                this.a(new ItemStack(Item.getItemOf(Blocks.PLANKS), 1, this.getType().b()), 0.0F);
                            }

                            for (i = 0; i < 2; ++i) {
                                this.a(Items.STICK, 1, 0.0F);
                            }
                        }
                    }
                    } // CraftBukkit end
                }

                this.fallDistance = 0.0F;
            } else if (this.world.getType((new BlockPosition(this)).down()).getMaterial() != Material.WATER && d0 < 0.0D) {
                this.fallDistance = (float) ((double) this.fallDistance - d0);
            }

        }
    }

    public boolean a(int i) {
        return this.datawatcher.get(EntityBoat.e[i]) && this.bw() != null;
    }

    public void setDamage(float f) {
        this.datawatcher.set(EntityBoat.c, f);
    }

    public float n() {
        return this.datawatcher.get(EntityBoat.c);
    }

    public void b(int i) {
        this.datawatcher.set(EntityBoat.a, i);
    }

    public int o() {
        return this.datawatcher.get(EntityBoat.a);
    }

    public void d(int i) {
        this.datawatcher.set(EntityBoat.b, i);
    }

    public int q() {
        return this.datawatcher.get(EntityBoat.b);
    }

    public EntityBoat.EnumBoatType getType() {
        return EntityBoat.EnumBoatType.a(this.datawatcher.get(EntityBoat.d));
    }

    public void setType(EntityBoat.EnumBoatType entityboat_enumboattype) {
        this.datawatcher.set(EntityBoat.d, entityboat_enumboattype.ordinal());
    }

    protected boolean q(Entity entity) {
        return this.bx().size() < 2;
    }

    @Nullable
    public Entity bw() {
        List list = this.bx();

        return list.isEmpty() ? null : (Entity) list.get(0);
    }

    public enum EnumBoatType {

        OAK(BlockWood.EnumLogVariant.OAK.a(), "oak"), SPRUCE(BlockWood.EnumLogVariant.SPRUCE.a(), "spruce"), BIRCH(BlockWood.EnumLogVariant.BIRCH.a(), "birch"), JUNGLE(BlockWood.EnumLogVariant.JUNGLE.a(), "jungle"), ACACIA(BlockWood.EnumLogVariant.ACACIA.a(), "acacia"), DARK_OAK(BlockWood.EnumLogVariant.DARK_OAK.a(), "dark_oak");

        private final String g;
        private final int h;

        EnumBoatType(int i, String s) {
            this.g = s;
            this.h = i;
        }

        public static EntityBoat.EnumBoatType a(int i) {
            if (i < 0 || i >= values().length) {
                i = 0;
            }

            return values()[i];
        }

        public static EntityBoat.EnumBoatType a(String s) {
            for (int i = 0; i < values().length; ++i) {
                if (values()[i].a().equals(s)) {
                    return values()[i];
                }
            }

            return values()[0];
        }

        public String a() {
            return this.g;
        }

        public int b() {
            return this.h;
        }

        public String toString() {
            return this.g;
        }
    }

    public enum EnumStatus {

        IN_WATER, UNDER_WATER, UNDER_FLOWING_WATER, ON_LAND, IN_AIR;

        EnumStatus() {}
    }

    static class SyntheticClass_1 {

        static final int[] a = new int[EntityBoat.EnumBoatType.values().length];

        static {
            try {
                EntityBoat.SyntheticClass_1.a[EntityBoat.EnumBoatType.OAK.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                EntityBoat.SyntheticClass_1.a[EntityBoat.EnumBoatType.SPRUCE.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                EntityBoat.SyntheticClass_1.a[EntityBoat.EnumBoatType.BIRCH.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                EntityBoat.SyntheticClass_1.a[EntityBoat.EnumBoatType.JUNGLE.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                EntityBoat.SyntheticClass_1.a[EntityBoat.EnumBoatType.ACACIA.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {
            }

            try {
                EntityBoat.SyntheticClass_1.a[EntityBoat.EnumBoatType.DARK_OAK.ordinal()] = 6;
            } catch (NoSuchFieldError ignored) {
            }

        }
    }
}
