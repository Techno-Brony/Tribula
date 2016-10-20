package net.minecraft.server;

import java.io.File;

public interface IDataManager {

    WorldData getWorldData();

    void checkSession() throws ExceptionWorldConflict;

    IChunkLoader createChunkLoader(@SuppressWarnings("UnusedParameters") WorldProvider worldprovider);

    void saveWorldData(WorldData worlddata, NBTTagCompound nbttagcompound);

    @SuppressWarnings("unused")
    void saveWorldData(WorldData worlddata);

    IPlayerFileData getPlayerFileData();

    @SuppressWarnings("EmptyMethod")
    void a();

    File getDirectory();

    File getDataFile(String s);

    DefinedStructureManager h();

    java.util.UUID getUUID(); // CraftBukkit
}
