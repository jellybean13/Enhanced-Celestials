package corgitaco.enchancedcelestials.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsClientUtils;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRender {

    @Shadow private ClientWorld world;

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I"))
    private void changeMoonColor(MatrixStack matrixStackIn, float partialTicks, CallbackInfo ci) {
        Color color = EnhancedCelestials.currentLunarEvent.modifyMoonColor();
        Vector3f glColor = EnhancedCelestialsClientUtils.transformToVectorColor(color);
        RenderSystem.color4f(glColor.getX(), glColor.getY(), glColor.getZ(), 1.0F - this.world.getRainGradient(partialTicks));
    }
}
