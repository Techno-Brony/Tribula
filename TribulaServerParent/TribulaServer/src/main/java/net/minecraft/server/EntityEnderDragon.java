package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

// CraftBukkit start
// CraftBukkit end

// PAIL: Fixme
public class EntityEnderDragon extends EntityInsentient implements IComplex, IMonster { //TODO Complicated to customise

    public static final DataWatcherObject<Integer> PHASE = DataWatcher.a(EntityEnderDragon.class, DataWatcherRegistry.b);
    private static final Logger bJ = LogManager.getLogger();
    private final EnderDragonBattle bK;
    private final DragonControllerManager bL;
    private final PathPoint[] bO = new PathPoint[24];
    private final int[] bP = new int[24];
    private final Path bQ = new Path();
    public double[][] b = new double[64][3];
    public int c = -1;
    public EntityComplexPart[] children;
    public EntityComplexPart bw = new EntityComplexPart(this, "head", 6.0F, 6.0F);
    public EntityComplexPart bx = new EntityComplexPart(this, "neck", 6.0F, 6.0F);
    public EntityComplexPart by = new EntityComplexPart(this, "body", 8.0F, 8.0F);
    public EntityComplexPart bz = new EntityComplexPart(this, "tail", 4.0F, 4.0F);
    public EntityComplexPart bA = new EntityComplexPart(this, "tail", 4.0F, 4.0F);
    public EntityComplexPart bB = new EntityComplexPart(this, "tail", 4.0F, 4.0F);
    public EntityComplexPart bC = new EntityComplexPart(this, "wing", 4.0F, 4.0F);
    public EntityComplexPart bD = new EntityComplexPart(this, "wing", 4.0F, 4.0F);
    public float bE;
    public float bF;
    public boolean bG;
    public int bH;
    public EntityEnderCrystal currentEnderCrystal;
    private int bM = 200;
    private int bN;
    private Explosion explosionSource = new Explosion(null, this, Double.NaN, Double.NaN, Double.NaN, Float.NaN, true, true); // CraftBukkit - reusable source for CraftTNTPrimed.getSource()

    public EntityEnderDragon(World world) {
        super(world);
        this.children = new EntityComplexPart[] { this.bw, this.bx, this.by, this.bz, this.bA, this.bB, this.bC, this.bD};
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0F, 8.0F);
        this.noclip = true;
        this.fireProof = true;
        this.bM = 100;
        this.ai = true;
        if (!world.isClientSide && world.worldProvider instanceof WorldProviderTheEnd) {
            this.bK = ((WorldProviderTheEnd) world.worldProvider).s();
        } else {
            this.bK = null;
        }

        this.bL = new DragonControllerManager(this);
    }

    public static void b(DataConverterManager dataconvertermanager) {
        EntityInsentient.a(dataconvertermanager, "EnderDragon");
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(200.0D);
    }

    protected void i() {
        super.i();
        this.getDataWatcher().register(EntityEnderDragon.PHASE, Integer.valueOf(DragonControllerPhase.k.b()));
    }

    public double[] a(int i, float f) {
        if (this.getHealth() <= 0.0F) {
            f = 0.0F;
        }

        f = 1.0F - f;
        int j = this.c - i & 63;
        int k = this.c - i - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.b[j][0];
        double d1 = MathHelper.g(this.b[k][0] - d0);

        adouble[0] = d0 + d1 * (double) f;
        d0 = this.b[j][1];
        d1 = this.b[k][1] - d0;
        adouble[1] = d0 + d1 * (double) f;
        adouble[2] = this.b[j][2] + (this.b[k][2] - this.b[j][2]) * (double) f;
        return adouble;
    }

    public void n() {
        float f;
        float f1;

        if (this.world.isClientSide) {
            this.setHealth(this.getHealth());
            if (!this.isSilent()) {
                f = MathHelper.cos(this.bF * 6.2831855F);
                f1 = MathHelper.cos(this.bE * 6.2831855F);
                if (f1 <= -0.3F && f >= -0.3F) {
                    this.world.a(this.locX, this.locY, this.locZ, SoundEffects.aR, this.bC(), 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
                }

                if (!this.bL.a().a() && --this.bM < 0) {
                    this.world.a(this.locX, this.locY, this.locZ, SoundEffects.aS, this.bC(), 2.5F, 0.8F + this.random.nextFloat() * 0.3F, false);
                    this.bM = 200 + this.random.nextInt(200);
                }
            }
        }

        this.bE = this.bF;
        float f2;

        if (this.getHealth() <= 0.0F) {
            f = (this.random.nextFloat() - 0.5F) * 8.0F;
            f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.locX + (double) f, this.locY + 2.0D + (double) f1, this.locZ + (double) f2, 0.0D, 0.0D, 0.0D);
        } else {
            this.da();
            f = 0.2F / (MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 10.0F + 1.0F);
            f *= (float) Math.pow(2.0D, this.motY);
            if (this.bL.a().a()) {
                this.bF += 0.1F;
            } else if (this.bG) {
                this.bF += f * 0.5F;
            } else {
                this.bF += f;
            }

            this.yaw = MathHelper.g(this.yaw);
            if (this.hasAI()) {
                this.bF = 0.5F;
            } else {
                if (this.c < 0) {
                    for (int i = 0; i < this.b.length; ++i) {
                        this.b[i][0] = (double) this.yaw;
                        this.b[i][1] = this.locY;
                    }
                }

                if (++this.c == this.b.length) {
                    this.c = 0;
                }

                this.b[this.c][0] = (double) this.yaw;
                this.b[this.c][1] = this.locY;
                double d0;
                double d1;
                double d2;
                float f3;

                if (this.world.isClientSide) {
                    if (this.bi > 0) {
                        double d3 = this.locX + (this.bj - this.locX) / (double) this.bi;

                        d0 = this.locY + (this.bk - this.locY) / (double) this.bi;
                        d1 = this.locZ + (this.bl - this.locZ) / (double) this.bi;
                        d2 = MathHelper.g(this.bm - (double) this.yaw);
                        this.yaw = (float) ((double) this.yaw + d2 / (double) this.bi);
                        this.pitch = (float) ((double) this.pitch + (this.bn - (double) this.pitch) / (double) this.bi);
                        --this.bi;
                        this.setPosition(d3, d0, d1);
                        this.setYawPitch(this.yaw, this.pitch);
                    }

                    this.bL.a().b();
                } else {
                    IDragonController idragoncontroller = this.bL.a();

                    idragoncontroller.c();
                    if (this.bL.a() != idragoncontroller) {
                        idragoncontroller = this.bL.a();
                        idragoncontroller.c();
                    }

                    Vec3D vec3d = idragoncontroller.g();

                    if (vec3d != null && idragoncontroller.getControllerPhase() != DragonControllerPhase.k) { // CraftBukkit - Don't move when hovering // PAIL: rename
                        d0 = vec3d.x - this.locX;
                        d1 = vec3d.y - this.locY;
                        d2 = vec3d.z - this.locZ;
                        double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                        f3 = idragoncontroller.f();
                        d1 = MathHelper.a(d1 / (double) MathHelper.sqrt(d0 * d0 + d2 * d2), (double) (-f3), (double) f3);
                        this.motY += d1 * 0.10000000149011612D;
                        this.yaw = MathHelper.g(this.yaw);
                        double d5 = MathHelper.a(MathHelper.g(180.0D - MathHelper.b(d0, d2) * 57.2957763671875D - (double) this.yaw), -50.0D, 50.0D);
                        Vec3D vec3d1 = (new Vec3D(vec3d.x - this.locX, vec3d.y - this.locY, vec3d.z - this.locZ)).a();
                        Vec3D vec3d2 = (new Vec3D((double) MathHelper.sin(this.yaw * 0.017453292F), this.motY, (double) (-MathHelper.cos(this.yaw * 0.017453292F)))).a();
                        float f4 = Math.max(((float) vec3d2.b(vec3d1) + 0.5F) / 1.5F, 0.0F);

                        this.bh *= 0.8F;
                        this.bh = (float) ((double) this.bh + d5 * (double) idragoncontroller.h());
                        this.yaw += this.bh * 0.1F;
                        float f5 = (float) (2.0D / (d4 + 1.0D));
                        float f6 = 0.06F;

                        this.a(0.0F, -1.0F, 0.06F * (f4 * f5 + (1.0F - f5)));
                        if (this.bG) {
                            this.move(this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
                        } else {
                            this.move(this.motX, this.motY, this.motZ);
                        }

                        Vec3D vec3d3 = (new Vec3D(this.motX, this.motY, this.motZ)).a();
                        float f7 = ((float) vec3d3.b(vec3d2) + 1.0F) / 2.0F;

                        f7 = 0.8F + 0.15F * f7;
                        this.motX *= (double) f7;
                        this.motZ *= (double) f7;
                        this.motY *= 0.9100000262260437D;
                    }
                }

                this.aO = this.yaw;
                this.bw.width = 1.0F;
                this.bw.length = 1.0F;
                this.bx.width = 3.0F;
                this.bx.length = 3.0F;
                this.bz.width = 2.0F;
                this.bz.length = 2.0F;
                this.bA.width = 2.0F;
                this.bA.length = 2.0F;
                this.bB.width = 2.0F;
                this.bB.length = 2.0F;
                this.by.length = 3.0F;
                this.by.width = 5.0F;
                this.bC.length = 2.0F;
                this.bC.width = 4.0F;
                this.bD.length = 3.0F;
                this.bD.width = 4.0F;
                f1 = (float) (this.a(5, 1.0F)[1] - this.a(10, 1.0F)[1]) * 10.0F * 0.017453292F;
                f2 = MathHelper.cos(f1);
                float f8 = MathHelper.sin(f1);
                float f9 = this.yaw * 0.017453292F;
                float f10 = MathHelper.sin(f9);
                float f11 = MathHelper.cos(f9);

                this.by.m();
                this.by.setPositionRotation(this.locX + (double) (f10 * 0.5F), this.locY, this.locZ - (double) (f11 * 0.5F), 0.0F, 0.0F);
                this.bC.m();
                this.bC.setPositionRotation(this.locX + (double) (f11 * 4.5F), this.locY + 2.0D, this.locZ + (double) (f10 * 4.5F), 0.0F, 0.0F);
                this.bD.m();
                this.bD.setPositionRotation(this.locX - (double) (f11 * 4.5F), this.locY + 2.0D, this.locZ - (double) (f10 * 4.5F), 0.0F, 0.0F);
                if (!this.world.isClientSide && this.hurtTicks == 0) {
                    this.a(this.world.getEntities(this, this.bC.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
                    this.a(this.world.getEntities(this, this.bD.getBoundingBox().grow(4.0D, 2.0D, 4.0D).c(0.0D, -2.0D, 0.0D)));
                    this.b(this.world.getEntities(this, this.bw.getBoundingBox().g(1.0D)));
                    this.b(this.world.getEntities(this, this.bx.getBoundingBox().g(1.0D)));
                }

                double[] adouble = this.a(5, 1.0F);
                float f12 = MathHelper.sin(this.yaw * 0.017453292F - this.bh * 0.01F);
                float f13 = MathHelper.cos(this.yaw * 0.017453292F - this.bh * 0.01F);

                this.bw.m();
                this.bx.m();
                float f14 = this.q(1.0F);

                this.bw.setPositionRotation(this.locX + (double) (f12 * 6.5F * f2), this.locY + (double) f14 + (double) (f8 * 6.5F), this.locZ - (double) (f13 * 6.5F * f2), 0.0F, 0.0F);
                this.bx.setPositionRotation(this.locX + (double) (f12 * 5.5F * f2), this.locY + (double) f14 + (double) (f8 * 5.5F), this.locZ - (double) (f13 * 5.5F * f2), 0.0F, 0.0F);

                for (int j = 0; j < 3; ++j) {
                    EntityComplexPart entitycomplexpart = null;

                    if (j == 0) {
                        entitycomplexpart = this.bz;
                    }

                    if (j == 1) {
                        entitycomplexpart = this.bA;
                    }

                    if (j == 2) {
                        entitycomplexpart = this.bB;
                    }

                    double[] adouble1 = this.a(12 + j * 2, 1.0F);

                    f3 = this.yaw * 0.017453292F + this.c(adouble1[0] - adouble[0]) * 0.017453292F;
                    float f15 = MathHelper.sin(f3);
                    float f16 = MathHelper.cos(f3);
                    float f17 = 1.5F;
                    float f18 = (float) (j + 1) * 2.0F;

                    entitycomplexpart.m();
                    entitycomplexpart.setPositionRotation(this.locX - (double) ((f10 * 1.5F + f15 * f18) * f2), this.locY + (adouble1[1] - adouble[1]) - (double) ((f18 + 1.5F) * f8) + 1.5D, this.locZ + (double) ((f11 * 1.5F + f16 * f18) * f2), 0.0F, 0.0F);
                }

                if (!this.world.isClientSide) {
                    this.bG = this.b(this.bw.getBoundingBox()) | this.b(this.bx.getBoundingBox()) | this.b(this.by.getBoundingBox());
                    if (this.bK != null) {
                        this.bK.b(this);
                    }
                }

            }
        }
    }

    private float q(float f) {
        double d0;

        if (this.bL.a().a()) {
            d0 = -1.0D;
        } else {
            double[] adouble = this.a(5, 1.0F);
            double[] adouble1 = this.a(0, 1.0F);

            d0 = adouble[1] - adouble1[0];
        }

        return (float) d0;
    }

    private void da() {
        if (this.currentEnderCrystal != null) {
            if (this.currentEnderCrystal.dead) {
                this.currentEnderCrystal = null;
            } else if (this.ticksLived % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                // CraftBukkit start
                EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), 1.0F, EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.setHealth((float) (this.getHealth() + event.getAmount()));
                }
                // CraftBukkit end
            }
        }

        if (this.random.nextInt(10) == 0) {
            List list = this.world.a(EntityEnderCrystal.class, this.getBoundingBox().g(32.0D));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal) iterator.next();
                double d1 = entityendercrystal1.h(this);

                if (d1 < d0) {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.currentEnderCrystal = entityendercrystal;
        }

    }

    private void a(List<Entity> list) {
        double d0 = (this.by.getBoundingBox().a + this.by.getBoundingBox().d) / 2.0D;
        double d1 = (this.by.getBoundingBox().c + this.by.getBoundingBox().f) / 2.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityLiving) {
                double d2 = entity.locX - d0;
                double d3 = entity.locZ - d1;
                double d4 = d2 * d2 + d3 * d3;

                entity.g(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
                if (!this.bL.a().a() && ((EntityLiving) entity).bK() < entity.ticksLived - 2) {
                    entity.damageEntity(DamageSource.mobAttack(this), 5.0F);
                    this.a(this, entity);
                }
            }
        }

    }

    private void b(List<Entity> list) {
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = list.get(i);

            if (entity instanceof EntityLiving) {
                entity.damageEntity(DamageSource.mobAttack(this), 10.0F);
                this.a(this, entity);
            }
        }

    }

    private float c(double d0) {
        return (float) MathHelper.g(d0);
    }

    private boolean b(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.b);
        int k = MathHelper.floor(axisalignedbb.c);
        int l = MathHelper.floor(axisalignedbb.d);
        int i1 = MathHelper.floor(axisalignedbb.e);
        int j1 = MathHelper.floor(axisalignedbb.f);
        boolean flag = false;
        boolean flag1 = false;
        // CraftBukkit start - Create a list to hold all the destroyed blocks
        List<org.bukkit.block.Block> destroyedBlocks = new java.util.ArrayList<org.bukkit.block.Block>();
        org.bukkit.craftbukkit.CraftWorld craftWorld = this.world.getWorld();
        // CraftBukkit end

        for (int k1 = i; k1 <= l; ++k1) {
            for (int l1 = j; l1 <= i1; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    BlockPosition blockposition = new BlockPosition(k1, l1, i2);
                    IBlockData iblockdata = this.world.getType(blockposition);
                    Block block = iblockdata.getBlock();

                    if (iblockdata.getMaterial() != Material.AIR && iblockdata.getMaterial() != Material.FIRE) {
                        if (!this.world.getGameRules().getBoolean("mobGriefing")) {
                            flag = true;
                        } else if (block != Blocks.BARRIER && block != Blocks.OBSIDIAN && block != Blocks.END_STONE && block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME) {
                            if (block != Blocks.COMMAND_BLOCK && block != Blocks.dc && block != Blocks.dd && block != Blocks.IRON_BARS && block != Blocks.END_GATEWAY) {
                                // CraftBukkit start - Add blocks to list rather than destroying them
                                // flag1 = this.world.setAir(blockposition) || flag1;
                                flag1 = true;
                                destroyedBlocks.add(craftWorld.getBlockAt(k1, l1, i2));
                                // CraftBukkit end
                            } else {
                                flag = true;
                            }
                        } else {
                            flag = true;
                        }
                    }
                }
            }
        }

        // CraftBukkit start - Set off an EntityExplodeEvent for the dragon exploding all these blocks
        org.bukkit.entity.Entity bukkitEntity = this.getBukkitEntity();
        EntityExplodeEvent event = new EntityExplodeEvent(bukkitEntity, bukkitEntity.getLocation(), destroyedBlocks, 0F);
        bukkitEntity.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            // This flag literally means 'Dragon hit something hard' (Obsidian, White Stone or Bedrock) and will cause the dragon to slow down.
            // We should consider adding an event extension for it, or perhaps returning true if the event is cancelled.
            return flag;
        } else if (event.getYield() == 0F) {
            // Yield zero ==> no drops
            for (org.bukkit.block.Block block : event.blockList()) {
                this.world.setAir(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            }
        } else {
            for (org.bukkit.block.Block block : event.blockList()) {
                org.bukkit.Material blockId = block.getType();
                if (blockId == org.bukkit.Material.AIR) {
                    continue;
                }

                int blockX = block.getX();
                int blockY = block.getY();
                int blockZ = block.getZ();

                Block nmsBlock = org.bukkit.craftbukkit.util.CraftMagicNumbers.getBlock(blockId);
                if (nmsBlock.a(explosionSource)) {
                    nmsBlock.dropNaturally(this.world, new BlockPosition(blockX, blockY, blockZ), nmsBlock.fromLegacyData(block.getData()), event.getYield(), 0);
                }
                nmsBlock.wasExploded(world, new BlockPosition(blockX, blockY, blockZ), explosionSource);

                this.world.setAir(new BlockPosition(blockX, blockY, blockZ));
            }
        }
        // CraftBukkit end

        if (flag1) {
            double d0 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * (double) this.random.nextFloat();
            double d1 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * (double) this.random.nextFloat();
            double d2 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * (double) this.random.nextFloat();

            this.world.addParticle(EnumParticle.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

        return flag;
    }

    public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, float f) {
        f = this.bL.a().a(entitycomplexpart, damagesource, f);
        if (entitycomplexpart != this.bw) {
            f = f / 4.0F + Math.min(f, 1.0F);
        }

        if (f < 0.01F) {
            return false;
        } else {
            if (damagesource.getEntity() instanceof EntityHuman || damagesource.isExplosion()) {
                float f1 = this.getHealth();

                this.dealDamage(damagesource, f);
                if (this.getHealth() <= 0.0F && !this.bL.a().a()) {
                    this.setHealth(1.0F);
                    this.bL.setControllerPhase(DragonControllerPhase.j);
                }

                if (this.bL.a().a()) {
                    this.bN = (int) ((float) this.bN + (f1 - this.getHealth()));
                    if ((float) this.bN > 0.25F * this.getMaxHealth()) {
                        this.bN = 0;
                        this.bL.setControllerPhase(DragonControllerPhase.e);
                    }
                }
            }

            return true;
        }
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (damagesource instanceof EntityDamageSource && ((EntityDamageSource) damagesource).x()) {
            this.a(this.by, damagesource, f);
        }

        return false;
    }

    protected boolean dealDamage(DamageSource damagesource, float f) {
        return super.damageEntity(damagesource, f);
    }

    public void Q() {
        this.die();
        if (this.bK != null) {
            this.bK.b(this);
            this.bK.a(this);
        }

    }

    protected void bF() {
        if (this.bK != null) {
            this.bK.b(this);
        }

        ++this.bH;
        if (this.bH >= 180 && this.bH <= 200) {
            float f = (this.random.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;

            this.world.addParticle(EnumParticle.EXPLOSION_HUGE, this.locX + (double) f, this.locY + 2.0D + (double) f1, this.locZ + (double) f2, 0.0D, 0.0D, 0.0D);
        }

        boolean flag = this.world.getGameRules().getBoolean("doMobLoot");
        short short0 = 500;

        if (this.bK != null && !this.bK.d()) {
            short0 = 12000;
        }

        if (!this.world.isClientSide) {
            if (this.bH > 150 && this.bH % 5 == 0 && flag) {
                this.a(MathHelper.d((float) short0 * 0.08F));
            }

            if (this.bH == 1) {
                // CraftBukkit start - Use relative location for far away sounds
                // this.world.a(1028, new BlockPosition(this), 0);
                int viewDistance = this.world.getServer().getViewDistance() * 16;
                for (EntityPlayer player : MinecraftServer.getServer().getPlayerList().players) {
                    double deltaX = this.locX - player.locX;
                    double deltaZ = this.locZ - player.locZ;
                    double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    if ( world.spigotConfig.dragonDeathSoundRadius > 0 && distanceSquared > world.spigotConfig.dragonDeathSoundRadius * world.spigotConfig.dragonDeathSoundRadius ) continue; // Spigot
                    if (distanceSquared > viewDistance * viewDistance) {
                        double deltaLength = Math.sqrt(distanceSquared);
                        double relativeX = player.locX + (deltaX / deltaLength) * viewDistance;
                        double relativeZ = player.locZ + (deltaZ / deltaLength) * viewDistance;
                        player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1028, new BlockPosition((int) relativeX, (int) this.locY, (int) relativeZ), 0, true));
                    } else {
                        player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1028, new BlockPosition((int) this.locX, (int) this.locY, (int) this.locZ), 0, true));
                    }
                }
                // CraftBukkit end
            }
        }

        this.move(0.0D, 0.10000000149011612D, 0.0D);
        this.yaw += 20.0F;
        this.aO = this.yaw;
        if (this.bH == 200 && !this.world.isClientSide) {
            if (flag) {
                this.a(MathHelper.d((float) short0 * 0.2F));
            }

            if (this.bK != null) {
                this.bK.a(this);
            }

            this.die();
        }

    }

    private void a(int i) {
        while (i > 0) {
            int j = EntityExperienceOrb.getOrbValue(i);

            i -= j;
            this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
        }

    }

    public int o() {
        if (this.bO[0] == null) {
            for (int i = 0; i < 24; ++i) {
                int j = 5;
                int k;
                int l;

                if (i < 12) {
                    k = (int) (60.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.2617994F * (float) i)));
                    l = (int) (60.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.2617994F * (float) i)));
                } else {
                    int i1;

                    if (i < 20) {
                        i1 = i - 12;
                        k = (int) (40.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.3926991F * (float) i1)));
                        l = (int) (40.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.3926991F * (float) i1)));
                        j += 10;
                    } else {
                        i1 = i - 20;
                        k = (int) (20.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.7853982F * (float) i1)));
                        l = (int) (20.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.7853982F * (float) i1)));
                    }
                }

                int j1 = Math.max(this.world.K() + 10, this.world.q(new BlockPosition(k, 0, l)).getY() + j);

                this.bO[i] = new PathPoint(k, j1, l);
            }

            this.bP[0] = 6146;
            this.bP[1] = 8197;
            this.bP[2] = 8202;
            this.bP[3] = 16404;
            this.bP[4] = '\u8028';
            this.bP[5] = '\u8050';
            this.bP[6] = 65696;
            this.bP[7] = 131392;
            this.bP[8] = 131712;
            this.bP[9] = 263424;
            this.bP[10] = 526848;
            this.bP[11] = 525313;
            this.bP[12] = 1581057;
            this.bP[13] = 3166214;
            this.bP[14] = 2138120;
            this.bP[15] = 6373424;
            this.bP[16] = 4358208;
            this.bP[17] = 12910976;
            this.bP[18] = 9044480;
            this.bP[19] = 9706496;
            this.bP[20] = 15216640;
            this.bP[21] = 13688832;
            this.bP[22] = 11763712;
            this.bP[23] = 8257536;
        }

        return this.l(this.locX, this.locY, this.locZ);
    }

    public int l(double d0, double d1, double d2) {
        float f = 10000.0F;
        int i = 0;
        PathPoint pathpoint = new PathPoint(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2));
        byte b0 = 0;

        if (this.bK == null || this.bK.c() == 0) {
            b0 = 12;
        }

        for (int j = b0; j < 24; ++j) {
            if (this.bO[j] != null) {
                float f1 = this.bO[j].b(pathpoint);

                if (f1 < f) {
                    f = f1;
                    i = j;
                }
            }
        }

        return i;
    }

    @Nullable
    public PathEntity a(int i, int j, @Nullable PathPoint pathpoint) {
        PathPoint pathpoint1;

        for (int k = 0; k < 24; ++k) {
            pathpoint1 = this.bO[k];
            pathpoint1.i = false;
            pathpoint1.g = 0.0F;
            pathpoint1.e = 0.0F;
            pathpoint1.f = 0.0F;
            pathpoint1.h = null;
            pathpoint1.d = -1;
        }

        PathPoint pathpoint2 = this.bO[i];

        pathpoint1 = this.bO[j];
        pathpoint2.e = 0.0F;
        pathpoint2.f = pathpoint2.a(pathpoint1);
        pathpoint2.g = pathpoint2.f;
        this.bQ.a();
        this.bQ.a(pathpoint2);
        PathPoint pathpoint3 = pathpoint2;
        byte b0 = 0;

        if (this.bK == null || this.bK.c() == 0) {
            b0 = 12;
        }

        label70:
        while (!this.bQ.e()) {
            PathPoint pathpoint4 = this.bQ.c();

            if (pathpoint4.equals(pathpoint1)) {
                if (pathpoint != null) {
                    pathpoint.h = pathpoint1;
                    pathpoint1 = pathpoint;
                }

                return this.a(pathpoint2, pathpoint1);
            }

            if (pathpoint4.a(pathpoint1) < pathpoint3.a(pathpoint1)) {
                pathpoint3 = pathpoint4;
            }

            pathpoint4.i = true;
            int l = 0;
            int i1 = 0;

            while (true) {
                if (i1 < 24) {
                    if (this.bO[i1] != pathpoint4) {
                        ++i1;
                        continue;
                    }

                    l = i1;
                }

                i1 = b0;

                while (true) {
                    if (i1 >= 24) {
                        continue label70;
                    }

                    if ((this.bP[l] & 1 << i1) > 0) {
                        PathPoint pathpoint5 = this.bO[i1];

                        if (!pathpoint5.i) {
                            float f = pathpoint4.e + pathpoint4.a(pathpoint5);

                            if (!pathpoint5.a() || f < pathpoint5.e) {
                                pathpoint5.h = pathpoint4;
                                pathpoint5.e = f;
                                pathpoint5.f = pathpoint5.a(pathpoint1);
                                if (pathpoint5.a()) {
                                    this.bQ.a(pathpoint5, pathpoint5.e + pathpoint5.f);
                                } else {
                                    pathpoint5.g = pathpoint5.e + pathpoint5.f;
                                    this.bQ.a(pathpoint5);
                                }
                            }
                        }
                    }

                    ++i1;
                }
            }
        }

        if (pathpoint3 == pathpoint2) {
            return null;
        } else {
            EntityEnderDragon.bJ.debug("Failed to find path from {} to {}", Integer.valueOf(i), Integer.valueOf(j));
            if (pathpoint != null) {
                pathpoint.h = pathpoint3;
                pathpoint3 = pathpoint;
            }

            return this.a(pathpoint2, pathpoint3);
        }
    }

    private PathEntity a(PathPoint pathpoint, PathPoint pathpoint1) {
        int i = 1;

        PathPoint pathpoint2;

        for (pathpoint2 = pathpoint1; pathpoint2.h != null; pathpoint2 = pathpoint2.h) {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];

        pathpoint2 = pathpoint1;
        --i;

        for (apathpoint[i] = pathpoint1; pathpoint2.h != null; apathpoint[i] = pathpoint2) {
            pathpoint2 = pathpoint2.h;
            --i;
        }

        return new PathEntity(apathpoint);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("DragonPhase", this.bL.a().getControllerPhase().b());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("DragonPhase")) {
            this.bL.setControllerPhase(DragonControllerPhase.getById(nbttagcompound.getInt("DragonPhase")));
        }

    }

    protected void L() {}

    public Entity[] aT() {
        return this.children;
    }

    public boolean isInteractable() {
        return false;
    }

    public World a() {
        return this.world;
    }

    public SoundCategory bC() {
        return SoundCategory.HOSTILE;
    }

    protected SoundEffect G() {
        return SoundEffects.aO;
    }

    protected SoundEffect bV() {
        return SoundEffects.aT;
    }

    protected float ch() {
        return 5.0F;
    }

    public Vec3D a(float f) {
        IDragonController idragoncontroller = this.bL.a();
        DragonControllerPhase dragoncontrollerphase = idragoncontroller.getControllerPhase();
        Vec3D vec3d;
        float f1;

        if (dragoncontrollerphase != DragonControllerPhase.d && dragoncontrollerphase != DragonControllerPhase.e) {
            if (idragoncontroller.a()) {
                float f2 = this.pitch;

                f1 = 1.5F;
                this.pitch = -45.0F;
                vec3d = this.f(f);
                this.pitch = f2;
            } else {
                vec3d = this.f(f);
            }
        } else {
            BlockPosition blockposition = this.world.q(WorldGenEndTrophy.a);

            f1 = Math.max(MathHelper.sqrt(this.d(blockposition)) / 4.0F, 1.0F);
            float f3 = 6.0F / f1;
            float f4 = this.pitch;
            float f5 = 1.5F;

            this.pitch = -f3 * 1.5F * 5.0F;
            vec3d = this.f(f);
            this.pitch = f4;
        }

        return vec3d;
    }

    public void a(EntityEnderCrystal entityendercrystal, BlockPosition blockposition, DamageSource damagesource) {
        EntityHuman entityhuman;

        if (damagesource.getEntity() instanceof EntityHuman) {
            entityhuman = (EntityHuman) damagesource.getEntity();
        } else {
            entityhuman = this.world.a(blockposition, 64.0D, 64.0D);
        }

        if (entityendercrystal == this.currentEnderCrystal) {
            this.a(this.bw, DamageSource.b(entityhuman), 10.0F);
        }

        this.bL.a().a(entityendercrystal, blockposition, damagesource, entityhuman);
    }

    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityEnderDragon.PHASE.equals(datawatcherobject) && this.world.isClientSide) {
            this.bL.setControllerPhase(DragonControllerPhase.getById(this.getDataWatcher().get(EntityEnderDragon.PHASE).intValue()));
        }

        super.a(datawatcherobject);
    }

    public DragonControllerManager getDragonControllerManager() {
        return this.bL;
    }

    @Nullable
    public EnderDragonBattle cZ() {
        return this.bK;
    }

    public void addEffect(MobEffect mobeffect) {}

    protected boolean n(Entity entity) {
        return false;
    }

    public boolean aX() {
        return false;
    }
}
