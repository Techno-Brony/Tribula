package net.minecraft.server;

import java.io.IOException;

public class PacketPlayInBlockPlace implements Packet<PacketListenerPlayIn> {

    public long timestamp; // Spigot
    private EnumHand a;

    @SuppressWarnings("unused")
    public PacketPlayInBlockPlace() {}

    @SuppressWarnings("unused")
    public PacketPlayInBlockPlace(EnumHand enumhand) {
        this.a = enumhand;
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.timestamp = System.currentTimeMillis(); // Spigot
        this.a = packetdataserializer.a(EnumHand.class);
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
        packetdataserializer.a(this.a);
    }

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }

    public EnumHand a() {
        return this.a;
    }
}
