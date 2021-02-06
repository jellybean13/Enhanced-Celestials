package corgitaco.enchancedcelestials.data.network.packet;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

public class LunarEventPacket {
    private final String event;

    public LunarEventPacket(String event) {
        this.event = event;
    }

    public static void writeToPacket(LunarEventPacket packet, PacketByteBuf buf) {
        buf.writeString(packet.event);
    }

    public static LunarEventPacket readFromPacket(PacketByteBuf buf) {
        return new LunarEventPacket(buf.readString());
    }

    public static void handle(LunarEventPacket message, World world) {
        EnhancedCelestials.getLunarData(world).setEvent(message.event);
    }
}