package corgitaco.enchancedcelestials.lunarevent;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsClientUtils;
import corgitaco.enchancedcelestials.util.EnhancedCelestialsUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class HarvestMoon extends LunarEvent {

    public HarvestMoon() {
        super(LunarEventSystem.HARVEST_MOON_EVENT_ID, EnhancedCelestials.CONFIG.harvestMoonSettings.harvestMoonChance);
    }

    @Override
    public boolean modifySkyLightMapColor(Vector3f originalLightmapColor) {
        originalLightmapColor.lerp(EnhancedCelestialsClientUtils.transformToVectorColor(new Color(255, 219, 99)), 1.0F);
        return true;
    }

    @Override
    public Color modifyMoonColor() {
        return new Color(255, 219, 99, 255);
    }


    @Override
    public void blockTick(ServerWorld world, BlockPos pos, Block block, BlockState blockState) {
        if (!EnhancedCelestialsUtils.HARVEST_MOON_BLACKLISTED_CROP_GROWTH.contains(block)) {
            if (EnhancedCelestialsUtils.HARVEST_MOON_WHITELISTED_CROP_GROWTH.contains(block)) {
                for (int i = 0; i < EnhancedCelestials.CONFIG.harvestMoonSettings.cropGrowthMultiplier; i++) {
                    if (i > 0) {
                        blockState = world.getBlockState(pos);
                        block = blockState.getBlock();
                    }

                    block.randomTick(blockState, world, pos, world.random);
                }
            }
        }
    }

    @Override
    public void multiplyDrops(ServerWorld world, ItemStack itemStack) {
        Item item = itemStack.getItem();

        if (!EnhancedCelestialsUtils.HARVEST_MOON_BLACKLISTED_CROP_DROPS.contains(item)) {
            if (EnhancedCelestialsUtils.HARVEST_MOON_WHITELISTED_CROP_DROPS.contains(item)) {
                itemStack.setCount((int) (itemStack.getCount() * EnhancedCelestials.CONFIG.harvestMoonSettings.cropGrowthMultiplier));
            }
        }
    }
}
