package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.misc.AdditionalEntityDensityManagerData;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnHelper.class)
public class MixinEntityWorldSpawner {

    @Inject(method = "spawn", at = @At("HEAD"))
    private static void bindData(ServerWorld world, WorldChunk chunk, SpawnHelper.Info densityManager, boolean p_234979_3_, boolean p_234979_4_, boolean p_234979_5_, CallbackInfo ci) {
        ((AdditionalEntityDensityManagerData) densityManager).setIsOverworld(world.getRegistryKey() == World.OVERWORLD);
    }
}
