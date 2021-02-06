package corgitaco.enchancedcelestials;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import corgitaco.enchancedcelestials.config.ECConfig;
import corgitaco.enchancedcelestials.data.network.NetworkHandler;
import corgitaco.enchancedcelestials.data.network.packet.LunarEventPacket;
import corgitaco.enchancedcelestials.data.world.LunarData;
import corgitaco.enchancedcelestials.lunarevent.LunarEvent;
import corgitaco.enchancedcelestials.lunarevent.LunarEventSystem;
import corgitaco.enchancedcelestials.modcompat.OptifineCompat;
import corgitaco.enchancedcelestials.server.SetLunarEventCommand;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.WorldAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnhancedCelestials implements ModInitializer {

    public static final String MOD_ID = "enhancedcelestials"; //Enhanced Celestials/Celestial Enhancement

    public static final Logger LOGGER = LogManager.getLogger();

    public static LunarData lunarData = null;

    public static LunarEvent currentLunarEvent = null;

    public static final Path CONFIG_PATH = new File(String.valueOf(FabricLoader.getInstance().getConfigDirectory().toPath().resolve(MOD_ID))).toPath();

    public static boolean usingOptifine;


    public static ECConfig CONFIG;

    @Override
    public void onInitialize() {
        AutoConfig.register(ECConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(ECConfig.class).getConfig();

        setup();
        lateSetup();
        commandRegisterEvent();
    }

    private void setup() {
        NetworkHandler.init();
        LunarEventSystem.registerDefaultLunarEvents();
    }

    public static void clientSetup() {
        usingOptifine = OptifineCompat.IS_OPTIFINE_PRESENT.get();
    }

    private void lateSetup() {
        currentLunarEvent = LunarEventSystem.DEFAULT_EVENT;
        LunarEventSystem.fillLunarEventsMapAndWeatherEventController();
    }

    public static LunarData getLunarData(@Nullable WorldAccess world) {
        if (lunarData == null) {
            if (world == null)
                throw new NullPointerException("Attempting to set Lunar data w/ a null world!");
            else
                lunarData = LunarData.get(world);
        }
        return lunarData;
    }

    public static void worldTick(ServerWorld world) {
        if (EnhancedCelestialsUtils.isOverworld(world.getRegistryKey())) {
            if (EnhancedCelestialsUtils.modulosDaytime(world.getLevelProperties().getTimeOfDay()) % 13005 == 0) {
                AtomicBoolean lunarEventWasSet = new AtomicBoolean(false);
                LunarEventSystem.LUNAR_EVENTS_CONTROLLER.forEach((eventName, eventChance) -> {
                    if (world.random.nextDouble() < eventChance) {
                        lunarEventWasSet.set(true);
                        getLunarData(world).setEvent(eventName);
                    }
                });
                if (!lunarEventWasSet.get())
                    getLunarData(world).setEvent(LunarEventSystem.DEFAULT_EVENT.getID());

                world.getPlayers().forEach(player -> {
                    NetworkHandler.sendToClient(player, new LunarEventPacket(getLunarData(world).getEvent()));
                });
            }
            LunarEventSystem.updateLunarEventPacket(world);
        }
    }

    public static void onPlayerJoined(ServerPlayerEntity playerEntity) {
        updateLunarEventPacket(Collections.singletonList(playerEntity));
    }

    public static void updateLunarEventPacket(List<ServerPlayerEntity> players) {
        players.forEach(player -> {
            NetworkHandler.sendToClient(player, new LunarEventPacket(getLunarData(player.getServerWorld()).getEvent()));
        });
    }

    public static void commandRegisterEvent() {
        LOGGER.debug("Enhanced Celestials: \"Server Starting\" Event Starting...");
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            register(dispatcher);
        });
        LOGGER.info("Enhanced Celestials: \"Server Starting\" Event Complete!");
    }


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LOGGER.debug("Registering Enhanced Celestials commands...");
        LiteralArgumentBuilder<ServerCommandSource> requires = CommandManager.literal(MOD_ID).requires(commandSource -> commandSource.hasPermissionLevel(3));
        LiteralCommandNode<ServerCommandSource> source = dispatcher.register(requires.then(SetLunarEventCommand.register(dispatcher)));
        dispatcher.register(CommandManager.literal(MOD_ID).redirect(source));
        LOGGER.debug("Registered Enhanced Celestials Commands!");
    }
}

