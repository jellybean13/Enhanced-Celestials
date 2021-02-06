package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LightmapTextureManager.class)
public abstract class MixinLightMapTexture {

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/Vector3f;lerp(Lnet/minecraft/client/util/math/Vector3f;F)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void doOurLightMap(float partialTicks, CallbackInfo ci, ClientWorld clientworld, float f, float f1, float f3, Vector3f skyVector) {
        if (EnhancedCelestials.currentLunarEvent != null)
            EnhancedCelestials.currentLunarEvent.modifySkyLightMapColor(skyVector);
    }
}
