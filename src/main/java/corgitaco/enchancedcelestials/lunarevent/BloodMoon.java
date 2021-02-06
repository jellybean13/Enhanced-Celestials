package corgitaco.enchancedcelestials.lunarevent;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

public class BloodMoon extends LunarEvent {

    public static final Color COLOR = new Color(166, 16, 30);

    public BloodMoon() {
        super(LunarEventSystem.BLOOD_MOON_EVENT_ID, EnhancedCelestials.CONFIG.bloodMoonSettings.bloodMoonChance);
    }

    @Override
    public boolean modifySkyLightMapColor(Vector3f lightMapSkyColor) {
        if (lightMapSkyColor != null)
            lightMapSkyColor.lerp(new Vector3f(2.0F, 0, 0), 1.0F);
        return true;
    }

    @Override
    public void multiplySpawnCap(SpawnGroup mobCategory, int spawningChunkCount, Object2IntOpenHashMap<SpawnGroup> currentMobCategoryCounts, CallbackInfoReturnable<Boolean> cir) {
        if (mobCategory == SpawnGroup.MONSTER) {
            int spawnCap = (int) (mobCategory.getCapacity() * (spawningChunkCount * EnhancedCelestials.CONFIG.bloodMoonSettings.monsterSpawnCapMultiplier) / EnhancedCelestialsUtils.CHUNK_AREA);
            cir.setReturnValue(currentMobCategoryCounts.getInt(mobCategory) < spawnCap);
        }
    }

    @Override
    public Color modifyMoonColor() {
        return new Color(166, 16, 30, 255);
    }

    @Override
    public Color modifySkyColor(Color originalSkyColor) {
        return COLOR;
    }

    @Override
    public Color modifyFogColor(Color originalSkyColor) {
        return COLOR;
    }

    @Override
    public Color modifyWaterColor(Color originalWaterColor) {
        return COLOR;
    }

    @Override
    public Color modifyWaterFogColor(Color originalWaterFogColor) {
        return new Color(206, 56, 70);
    }

    @Override
    public Color modifyCloudColor(Color originalCloudColor) {
        if (EnhancedCelestials.CONFIG.bloodMoonSettings.bloodMoonRedClouds)
            return COLOR;
        else
            return super.modifyCloudColor(originalCloudColor);
    }

    @Override
    public boolean stopSleeping(PlayerEntity player) {
        player.sendMessage(new TranslatableText("enhancedcelestials.sleep.fail.blood_moon"), true);
        return true;
    }
}
