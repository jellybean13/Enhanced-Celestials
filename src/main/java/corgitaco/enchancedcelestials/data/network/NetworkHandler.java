package corgitaco.enchancedcelestials.data.network;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.data.network.packet.LunarEventPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
//    public static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
//            new Identifier(EnhancedCelestials.MOD_ID, "network"),
//            () -> PROTOCOL_VERSION,
//            PROTOCOL_VERSION::equals,
//            PROTOCOL_VERSION::equals
//    );

    private static final Map<Class<?>, BiConsumer<?, PacketByteBuf>> ENCODERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Identifier> PACKET_IDS = new ConcurrentHashMap<>();


    public static void init() {
        registerMessage("0", LunarEventPacket.class, LunarEventPacket::writeToPacket, LunarEventPacket::readFromPacket, mainThreadHandler(LunarEventPacket::handle));
    }

    private static <T> void registerMessage(String id, Class<T> clazz,
                                            BiConsumer<T, PacketByteBuf> encode,
                                            Function<PacketByteBuf, T> decode,
                                            BiConsumer<T, PacketContext> handler) {
        ENCODERS.put(clazz, encode);
        PACKET_IDS.put(clazz, new Identifier(EnhancedCelestials.MOD_ID, id));
        ClientSidePacketRegistry.INSTANCE.register(
                new Identifier(EnhancedCelestials.MOD_ID, id), (ctx, received) -> {
                    T packet = decode.apply(received);
                    handler.accept(packet, ctx);
                }
        );
    }

    public static <MSG> void sendToClient(ServerPlayerEntity player, MSG packet) {
        Identifier packetId = PACKET_IDS.get(packet.getClass());
        @SuppressWarnings("unchecked")
        BiConsumer<MSG, PacketByteBuf> encoder = (BiConsumer<MSG, PacketByteBuf>) ENCODERS.get(packet.getClass());
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        encoder.accept(packet, buf);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, packetId, buf);
    }

    private static <T> BiConsumer<T, PacketContext> mainThreadHandler(Consumer<? super T> handler) {
        return (packet, ctx) -> ctx.getTaskQueue().submit(() -> handler.accept(packet));
    }

    private static <T> BiConsumer<T, PacketContext> mainThreadHandler(BiConsumer<? super T, ? super World> handler) {
        return (packet, ctx) -> ctx.getTaskQueue().submit(() -> handler.accept(packet, MinecraftClient.getInstance().world));
    }
}