package corgitaco.enchancedcelestials.util;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;

import java.awt.*;

public class EnhancedCelestialsClientUtils {

    public static boolean updateLightmap(float partialTicks, float torchFlicker, MinecraftClient client, GameRenderer entityRenderer, NativeImage nativeImage, NativeImageBackedTexture dynamicTexture) {
        client.getProfiler().push("lightTex");
        ClientWorld clientworld = client.world;
        if (clientworld != null) {
            float sunBrightness = clientworld.method_23783(1.0F);
            float worldBrightness;
            if (clientworld.getLightningTicksLeft() > 0) {
                worldBrightness = 1.0F;
            } else {
                worldBrightness = sunBrightness * 0.95F + 0.05F;
            }

            float waterBrightness = client.player.getUnderwaterVisibility();
            float generalBrightness;
            if (client.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                generalBrightness = GameRenderer.getNightVisionStrength(client.player, partialTicks);
            } else if (waterBrightness > 0.0F && client.player.hasStatusEffect(StatusEffects.CONDUIT_POWER)) {
                generalBrightness = waterBrightness;
            } else {
                generalBrightness = 0.0F;
            }

            Vector3f surfaceColor = new Vector3f(1.0F, 0, 0);

            if (EnhancedCelestials.currentLunarEvent != null)
                EnhancedCelestials.currentLunarEvent.modifySkyLightMapColor(surfaceColor);

            float torchFlicker2 = torchFlicker + 1.5F;
            Vector3f vector3f1 = new Vector3f();

            for (int y = 0; y < 16; ++y) {
                for (int x = 0; x < 16; ++x) {
                    float worldLightBrightness = getLightBrightness(clientworld, y) * worldBrightness;
                    float blockBrightness = getLightBrightness(clientworld, x) * torchFlicker2;
                    float f7 = blockBrightness * ((blockBrightness * 0.6F + 0.4F) * 0.6F + 0.4F);
                    float f8 = blockBrightness * (blockBrightness * blockBrightness * 0.6F + 0.4F);
                    vector3f1.set(blockBrightness, f7, f8);
                    if (clientworld.getSkyProperties().shouldBrightenLighting()) { //shouldRenderSky
                        vector3f1.lerp(new Vector3f(0.99F, 1.12F, 1.0F), 0.25F);
                    } else {
                        Vector3f surfaceColor2 = surfaceColor.copy();
                        surfaceColor2.scale(worldLightBrightness);
                        vector3f1.add(surfaceColor2);
                        vector3f1.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
                        if (entityRenderer.getSkyDarkness(partialTicks) > 0.0F) {
                            float bossColorModifier = entityRenderer.getSkyDarkness(partialTicks);
                            Vector3f vector3f3 = vector3f1.copy();
                            vector3f3.multiplyComponentwise(0.7F, 0.6F, 0.6F);
                            vector3f1.lerp(vector3f3, bossColorModifier);
                        }
                    }

                    vector3f1.clamp(0.0F, 1.0F);
                    if (generalBrightness > 0.0F) {
                        float f10 = Math.max(vector3f1.getX(), Math.max(vector3f1.getY(), vector3f1.getZ()));
                        if (f10 < 1.0F) {
                            float f12 = 1.0F / f10;
                            Vector3f vector3f5 = vector3f1.copy();
                            vector3f5.scale(f12);
                            vector3f1.lerp(vector3f5, generalBrightness);
                        }
                    }

                    float gamma = (float) client.options.gamma;
                    Vector3f vector3f4 = vector3f1.copy();
                    vector3f4.modify(EnhancedCelestialsClientUtils::invGamma);
                    vector3f1.lerp(vector3f4, gamma);
                    vector3f1.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
                    vector3f1.clamp(0.0F, 1.0F);
                    vector3f1.scale(255.0F); //Translate back to rgb range of 0-255 as opposed to the 0-1.0 range
                    int k = (int) vector3f1.getX();
                    int l = (int) vector3f1.getY();
                    int i1 = (int) vector3f1.getZ();
                    nativeImage.setPixelColor(x, y, -16777216 | i1 << 16 | l << 8 | k);
                }
            }

            dynamicTexture.upload();
            client.getProfiler().pop();
        }
        return false;
    }

    private static float invGamma(float valueIn) {
        float f = 1.0F - valueIn;
        return 1.0F - f * f * f * f;
    }

    private static float getLightBrightness(World worldIn, int lightLevelIn) {
        return worldIn.getDimension().method_28516(lightLevelIn);
    }


    public static Vector3f transformToVectorColor(Color color) {
        int rgbColor = color.getRGB();
        float r = (float) (rgbColor >> 16 & 255) / 255.0F;
        float g = (float) (rgbColor >> 8 & 255) / 255.0F;
        float b = (float) (rgbColor & 255) / 255.0F;
        return new Vector3f(r, g, b);
    }

}
