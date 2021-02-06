package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class MixinServerWorld {

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickECWorld(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        EnhancedCelestials.worldTick((ServerWorld) (Object) this);
    }
}
