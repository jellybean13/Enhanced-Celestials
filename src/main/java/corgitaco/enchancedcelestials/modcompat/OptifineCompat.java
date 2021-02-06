package corgitaco.enchancedcelestials.modcompat;

import net.minecraft.util.Lazy;

public class OptifineCompat {

    /**
     * @author Darkhax
     * Tracks whether or not Optifine is installed.
     * Allows for better compatibility with Optifine.
     */
    public static final Lazy<Boolean> IS_OPTIFINE_PRESENT = new Lazy<>(() -> {

        try {

            final Class<?> clazz = Class.forName("net.optifine.Config");
            return clazz != null;
        } catch (final Exception e) {

            return false;
        }
    });
}