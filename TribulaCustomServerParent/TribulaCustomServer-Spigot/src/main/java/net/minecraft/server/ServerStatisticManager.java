package net.minecraft.server;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;

public class ServerStatisticManager extends StatisticManager {

    private static final Logger b = LogManager.getLogger();
    private final MinecraftServer c;
    private final File d;
    private final Set<Statistic> e = Sets.newHashSet();
    private int f = -300;
    private boolean g;

    public ServerStatisticManager(MinecraftServer minecraftserver, File file) {
        this.c = minecraftserver;
        this.d = file;
        // Spigot start
        for ( String name : org.spigotmc.SpigotConfig.forcedStats.keySet() )
        {
            StatisticWrapper wrapper = new StatisticWrapper();
            wrapper.a( org.spigotmc.SpigotConfig.forcedStats.get( name ) );
            a.put( StatisticList.getStatistic( name ), wrapper );
        }
        // Spigot end
    }

    public static String a(Map<Statistic, StatisticWrapper> map) {
        JsonObject jsonobject = new JsonObject();
        Iterator iterator = map.entrySet().iterator();

        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((StatisticWrapper) entry.getValue()).b() != null) {
                JsonObject jsonobject1 = new JsonObject();

                jsonobject1.addProperty("value", ((StatisticWrapper) entry.getValue()).a());

                try {
                    jsonobject1.add("progress", ((StatisticWrapper) entry.getValue()).b().a());
                } catch (Throwable throwable) {
                    ServerStatisticManager.b.warn("Couldn\'t save statistic {}: error serializing progress", ((Statistic) entry.getKey()).e(), throwable);
                }

                jsonobject.add(((Statistic) entry.getKey()).name, jsonobject1);
            } else {
                jsonobject.addProperty(((Statistic) entry.getKey()).name, ((StatisticWrapper) entry.getValue()).a());
            }
        }

        return jsonobject.toString();
    }

    public void a() {
        if (this.d.isFile()) {
            try {
                this.a.clear();
                this.a.putAll(this.a(FileUtils.readFileToString(this.d)));
            } catch (IOException ioexception) {
                ServerStatisticManager.b.error("Couldn\'t read statistics file {}", this.d, ioexception);
            } catch (JsonParseException jsonparseexception) {
                ServerStatisticManager.b.error("Couldn\'t parse statistics file {}", this.d, jsonparseexception);
            }
        }

    }

    public void b() {
        if ( org.spigotmc.SpigotConfig.disableStatSaving ) return; // Spigot
        try {
            FileUtils.writeStringToFile(this.d, a(this.a));
        } catch (IOException ioexception) {
            ServerStatisticManager.b.error("Couldn\'t save stats", ioexception);
        }

    }

    public void setStatistic(EntityHuman entityhuman, Statistic statistic, int i) {
        if ( org.spigotmc.SpigotConfig.disableStatSaving ) return; // Spigot
        int j = statistic.d() ? this.getStatisticValue(statistic) : 0;

        super.setStatistic(entityhuman, statistic, i);
        this.e.add(statistic);
        if (statistic.d() && j == 0 && i > 0) {
            this.g = true;
            if (this.c.ax()) {
                this.c.getPlayerList().sendMessage(new ChatMessage("chat.type.achievement", new Object[] { entityhuman.getScoreboardDisplayName(), statistic.j()}));
            }
        }

        if (statistic.d() && j > 0 && i == 0) {
            this.g = true;
            if (this.c.ax()) {
                this.c.getPlayerList().sendMessage(new ChatMessage("chat.type.achievement.taken", new Object[] { entityhuman.getScoreboardDisplayName(), statistic.j()}));
            }
        }

    }

    public Set<Statistic> c() {
        HashSet hashset = Sets.newHashSet(this.e);

        this.e.clear();
        this.g = false;
        //noinspection unchecked
        return hashset;
    }

    public Map<Statistic, StatisticWrapper> a(String s) {
        JsonElement jsonelement = (new JsonParser()).parse(s);

        if (!jsonelement.isJsonObject()) {
            return Maps.newHashMap();
        } else {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            HashMap hashmap = Maps.newHashMap();
            Iterator iterator = jsonobject.entrySet().iterator();

            //noinspection WhileLoopReplaceableByForEach
            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                Statistic statistic = StatisticList.getStatistic((String) entry.getKey());

                if (statistic != null) {
                    StatisticWrapper statisticwrapper = new StatisticWrapper();

                    if (((JsonElement) entry.getValue()).isJsonPrimitive() && ((JsonElement) entry.getValue()).getAsJsonPrimitive().isNumber()) {
                        statisticwrapper.a(((JsonElement) entry.getValue()).getAsInt());
                    } else if (((JsonElement) entry.getValue()).isJsonObject()) {
                        JsonObject jsonobject1 = ((JsonElement) entry.getValue()).getAsJsonObject();

                        if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber()) {
                            statisticwrapper.a(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                        }

                        if (jsonobject1.has("progress") && statistic.l() != null) {
                            try {
                                Constructor constructor = statistic.l().getConstructor();
                                IJsonStatistic ijsonstatistic = (IJsonStatistic) constructor.newInstance();

                                ijsonstatistic.a(jsonobject1.get("progress"));
                                statisticwrapper.a(ijsonstatistic);
                            } catch (Throwable throwable) {
                                ServerStatisticManager.b.warn("Invalid statistic progress in {}", this.d, throwable);
                            }
                        }
                    }

                    //noinspection unchecked
                    hashmap.put(statistic, statisticwrapper);
                } else {
                    ServerStatisticManager.b.warn("Invalid statistic in {}: Don\'t know what {} is", this.d, entry.getKey());
                }
            }

            //noinspection unchecked
            return hashmap;
        }
    }

    public void d() {
        Iterator iterator = this.a.keySet().iterator();

        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            Statistic statistic = (Statistic) iterator.next();

            this.e.add(statistic);
        }

    }

    public void a(EntityPlayer entityplayer) {
        int i = this.c.ap();
        HashMap hashmap = Maps.newHashMap();

        if (this.g || i - this.f > 300) {
            this.f = i;
            Iterator iterator = this.c().iterator();

            //noinspection WhileLoopReplaceableByForEach
            while (iterator.hasNext()) {
                Statistic statistic = (Statistic) iterator.next();

                //noinspection unchecked
                hashmap.put(statistic, this.getStatisticValue(statistic));
            }
        }

        //noinspection unchecked
        entityplayer.playerConnection.sendPacket(new PacketPlayOutStatistic(hashmap));
    }

    public void updateStatistics(EntityPlayer entityplayer) {
        HashMap hashmap = Maps.newHashMap();
        Iterator iterator = AchievementList.e.iterator();

        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            Achievement achievement = (Achievement) iterator.next();

            if (this.hasAchievement(achievement)) {
                //noinspection unchecked
                hashmap.put(achievement, this.getStatisticValue(achievement));
                this.e.remove(achievement);
            }
        }

        //noinspection unchecked
        entityplayer.playerConnection.sendPacket(new PacketPlayOutStatistic(hashmap));
    }

    public boolean e() {
        return this.g;
    }
}
