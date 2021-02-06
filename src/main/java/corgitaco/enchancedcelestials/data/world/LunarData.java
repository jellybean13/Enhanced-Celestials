package corgitaco.enchancedcelestials.data.world;

import corgitaco.enchancedcelestials.EnhancedCelestials;
import corgitaco.enchancedcelestials.lunarevent.LunarEventSystem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class LunarData extends PersistentState {
    public static String DATA_NAME = EnhancedCelestials.MOD_ID + ":lunar_data";

    private String event = LunarEventSystem.DEFAULT_EVENT_ID;

    public LunarData() {
        super(DATA_NAME);
    }

    public LunarData(String s) {
        super(s);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        setEvent(nbt.getString("event"));
    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        compound.putString("event", event);
        return compound;
    }

    public String getEvent() {
        return this.event;
    }


    public void setEvent(String event) {
        this.event = event;
        markDirty();
        EnhancedCelestials.currentLunarEvent = LunarEventSystem.LUNAR_EVENTS_MAP.get(event);
    }


    public static LunarData get(WorldAccess world) {
        if (!(world instanceof ServerWorld))
            return new LunarData();
        ServerWorld overWorld = ((ServerWorld) world).toServerWorld().getServer().getWorld(World.OVERWORLD);
        PersistentStateManager data = overWorld.getPersistentStateManager();
        LunarData weatherData = data.getOrCreate(LunarData::new, DATA_NAME);

        if (weatherData == null) {
            weatherData = new LunarData();
            data.set(weatherData);
        }

        return weatherData;
    }
}