package corgitaco.enchancedcelestials.util;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

public class EnhancedCelestialsUtils {

    public static final int CHUNK_AREA = (int)Math.pow(17.0D, 2.0D);

    public static void modifySpawnCap(SpawnGroup mobCategory, int spawningChunkCount, Object2IntOpenHashMap<SpawnGroup> currentMobCategoryCounts, CallbackInfoReturnable<Boolean> cir) {
        EnhancedCelestials.currentLunarEvent.multiplySpawnCap(mobCategory, spawningChunkCount, currentMobCategoryCounts, cir);
    }

    public static boolean isOverworld(RegistryKey<World> worldKey) {
        return worldKey == World.OVERWORLD;
    }

    public static Color transformFloatColor(Vec3d floatColor) {
        return new Color((int) (floatColor.getX() * 255), (int) (floatColor.getY() * 255), (int) (floatColor.getZ() * 255));
    }

    public static final Tag<Item> FRUITS = forgeTag("fruits");
    public static final Tag<Item> VEGETABLES = forgeTag("vegetable");
    public static final Tag<Block> HARVEST_MOON_WHITELISTED_CROP_GROWTH = ecBlockTag("harvest_moon_whitelisted_crop_growth");
    public static final Tag<Block> HARVEST_MOON_BLACKLISTED_CROP_GROWTH = ecBlockTag("harvest_moon_blacklisted_crop_growth");

    public static final Tag<Item> HARVEST_MOON_WHITELISTED_CROP_DROPS = ecItemTag("harvest_moon_whitelisted_crop_drops");
    public static final Tag<Item> HARVEST_MOON_BLACKLISTED_CROP_DROPS = ecItemTag("harvest_moon_blacklisted_crop_drops");


    private static Tag<Item> forgeTag(String name) {
        return TagRegistry.item(new Identifier(EnhancedCelestials.MOD_ID, name));
    }

    private static Tag<Item> ecItemTag(String name) {
        return TagRegistry.item(new Identifier(EnhancedCelestials.MOD_ID, name));
    }

    private static Tag<Block> ecBlockTag(String name) {
        return TagRegistry.block(new Identifier(EnhancedCelestials.MOD_ID, name));
    }

    public static boolean filterRegistryID(Identifier id, Registry<?> registry, String registryTypeName) {
        if (registry.getIds().contains(id))
            return true;
        else {
            EnhancedCelestials.LOGGER.error("\"" + id.toString() + "\" was not a registryID in the " + registryTypeName + "! Skipping entry...");
            return false;
        }
    }

    public static long modulosDaytime(long daytime) {
        return daytime % 24000L;
    }
}
