package corgitaco.enchancedcelestials.mixin;

import corgitaco.enchancedcelestials.misc.AdditionalEntityDensityManagerData;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.Info.class)
public class EntityDensityDensityManagerMixin implements AdditionalEntityDensityManagerData {
	private boolean isOverworld;

	@Shadow @Final private int spawningChunkCount;

	@Shadow @Final private Object2IntOpenHashMap<SpawnGroup> groupToCount;

	@Inject(at = @At("RETURN"), method = "isBelowCap", cancellable = true)
	private void modifySpawnCap(SpawnGroup entityClassification, CallbackInfoReturnable<Boolean> cir) {
		if (isOverworld)
			EnhancedCelestialsUtils.modifySpawnCap(entityClassification, this.spawningChunkCount, this.groupToCount, cir);
	}

	@Override
	public void setIsOverworld(boolean isOverworld) {
		this.isOverworld = isOverworld;
	}
}
