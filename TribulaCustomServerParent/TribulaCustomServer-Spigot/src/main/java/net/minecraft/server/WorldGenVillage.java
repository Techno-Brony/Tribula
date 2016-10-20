package net.minecraft.server;

import java.util.*;
import java.util.Map.Entry;

public class WorldGenVillage extends StructureGenerator {

    public static final List<BiomeBase> a = Arrays.asList(Biomes.c, Biomes.d, Biomes.K, Biomes.g);
    private int b;
    private int d;

    @SuppressWarnings("unused")
    public WorldGenVillage() {
        this.d = 32;
        int h = 8;
    }

    @SuppressWarnings("unused")
    public WorldGenVillage(Map<String, String> map) {
        this();
        Iterator iterator = map.entrySet().iterator();

        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (entry.getKey().equals("size")) {
                this.b = MathHelper.a((String) entry.getValue(), this.b, 0);
            } else if (entry.getKey().equals("distance")) {
                this.d = MathHelper.a((String) entry.getValue(), this.d, 9);
            }
        }

    }

    public String a() {
        return "Village";
    }

    protected boolean a(int i, int j) {
        int k = i;
        int l = j;

        if (i < 0) {
            i -= this.d - 1;
        }

        if (j < 0) {
            j -= this.d - 1;
        }

        int i1 = i / this.d;
        int j1 = j / this.d;
        Random random = this.g.a(i1, j1, this.g.spigotConfig.villageSeed); // Spigot

        i1 *= this.d;
        j1 *= this.d;
        i1 += random.nextInt(this.d - 8);
        j1 += random.nextInt(this.d - 8);
        if (k == i1 && l == j1) {
            boolean flag = this.g.getWorldChunkManager().a(k * 16 + 8, l * 16 + 8, 0, WorldGenVillage.a);

            if (flag) {
                return true;
            }
        }

        return false;
    }

    protected StructureStart b(int i, int j) {
        return new WorldGenVillage.WorldGenVillageStart(this.g, this.f, i, j, this.b);
    }

    public static class WorldGenVillageStart extends StructureStart {

        private boolean c;

        @SuppressWarnings("unused")
        public WorldGenVillageStart() {}

        public WorldGenVillageStart(World world, Random random, int i, int j, int k) {
            super(i, j);
            List list = WorldGenVillagePieces.a(random, k);
            //noinspection unchecked
            WorldGenVillagePieces.WorldGenVillageStartPiece worldgenvillagepieces_worldgenvillagestartpiece = new WorldGenVillagePieces.WorldGenVillageStartPiece(world.getWorldChunkManager(), 0, random, (i << 4) + 2, (j << 4) + 2, list, k);

            this.a.add(worldgenvillagepieces_worldgenvillagestartpiece);
            worldgenvillagepieces_worldgenvillagestartpiece.a(worldgenvillagepieces_worldgenvillagestartpiece, this.a, random);
            List list1 = worldgenvillagepieces_worldgenvillagestartpiece.f;
            List list2 = worldgenvillagepieces_worldgenvillagestartpiece.e;

            int l;

            while (!list1.isEmpty() || !list2.isEmpty()) {
                StructurePiece structurepiece;

                if (list1.isEmpty()) {
                    l = random.nextInt(list2.size());
                    structurepiece = (StructurePiece) list2.remove(l);
                    structurepiece.a(worldgenvillagepieces_worldgenvillagestartpiece, this.a, random);
                } else {
                    l = random.nextInt(list1.size());
                    structurepiece = (StructurePiece) list1.remove(l);
                    structurepiece.a(worldgenvillagepieces_worldgenvillagestartpiece, this.a, random);
                }
            }

            this.d();
            l = 0;
            Iterator iterator = this.a.iterator();

            //noinspection WhileLoopReplaceableByForEach
            while (iterator.hasNext()) {
                StructurePiece structurepiece1 = (StructurePiece) iterator.next();

                if (!(structurepiece1 instanceof WorldGenVillagePieces.WorldGenVillageRoadPiece)) {
                    ++l;
                }
            }

            this.c = l > 2;
        }

        public boolean a() {
            return this.c;
        }

        public void a(NBTTagCompound nbttagcompound) {
            super.a(nbttagcompound);
            nbttagcompound.setBoolean("Valid", this.c);
        }

        public void b(NBTTagCompound nbttagcompound) {
            super.b(nbttagcompound);
            this.c = nbttagcompound.getBoolean("Valid");
        }
    }
}
