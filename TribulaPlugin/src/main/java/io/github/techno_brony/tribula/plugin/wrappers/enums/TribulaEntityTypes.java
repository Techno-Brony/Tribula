package io.github.techno_brony.tribula.plugin.wrappers.enums;

import net.minecraft.server.v1_10_R1.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;

import java.lang.reflect.Field;
import java.util.Map;

public enum TribulaEntityTypes {

    TRIBULA_MOB_ZOMBIE("Zombie", 54, TribulaMobZombie.class); //TODO

    TribulaEntityTypes(String name, int id, Class<? extends Entity> custom) {
        addToMaps(custom, name, id);
    }

    public static void spawnEntity(Entity entity, Location loc) {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

    private static void addToMaps(Class clazz, String name, int id) {
        ((Map) getPrivateField("c", net.minecraft.server.v1_10_R1.EntityTypes.class, null)).put(name, clazz);
        ((Map) getPrivateField("d", net.minecraft.server.v1_10_R1.EntityTypes.class, null)).put(clazz, name);
        ((Map) getPrivateField("f", net.minecraft.server.v1_10_R1.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));

    }
}
